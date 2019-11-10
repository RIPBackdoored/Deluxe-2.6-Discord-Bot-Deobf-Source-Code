/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public abstract class AbstractMessageWriter<T extends HttpMessage>
implements HttpMessageWriter<T> {
    protected final SessionOutputBuffer sessionBuffer;
    protected final CharArrayBuffer lineBuf;
    protected final LineFormatter lineFormatter;

    @Deprecated
    public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
        Args.notNull(buffer, "Session input buffer");
        this.sessionBuffer = buffer;
        this.lineBuf = new CharArrayBuffer(128);
        this.lineFormatter = formatter != null ? formatter : BasicLineFormatter.INSTANCE;
    }

    public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
        this.sessionBuffer = Args.notNull(buffer, "Session input buffer");
        this.lineFormatter = formatter != null ? formatter : BasicLineFormatter.INSTANCE;
        this.lineBuf = new CharArrayBuffer(128);
    }

    protected abstract void writeHeadLine(T var1) throws IOException;

    @Override
    public void write(T message) throws IOException, HttpException {
        Args.notNull(message, "HTTP message");
        this.writeHeadLine(message);
        HeaderIterator it = message.headerIterator();
        do {
            if (!it.hasNext()) {
                this.lineBuf.clear();
                this.sessionBuffer.writeLine(this.lineBuf);
                return;
            }
            Header header = it.nextHeader();
            this.sessionBuffer.writeLine(this.lineFormatter.formatHeader(this.lineBuf, header));
        } while (true);
    }
}

