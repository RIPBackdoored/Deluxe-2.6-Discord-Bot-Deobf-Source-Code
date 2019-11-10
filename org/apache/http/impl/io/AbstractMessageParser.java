/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.MessageConstraintException;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public abstract class AbstractMessageParser<T extends HttpMessage>
implements HttpMessageParser<T> {
    private static final int HEAD_LINE = 0;
    private static final int HEADERS = 1;
    private final SessionInputBuffer sessionBuffer;
    private final MessageConstraints messageConstraints;
    private final List<CharArrayBuffer> headerLines;
    protected final LineParser lineParser;
    private int state;
    private T message;

    @Deprecated
    public AbstractMessageParser(SessionInputBuffer buffer, LineParser parser, HttpParams params) {
        Args.notNull(buffer, "Session input buffer");
        Args.notNull(params, "HTTP parameters");
        this.sessionBuffer = buffer;
        this.messageConstraints = HttpParamConfig.getMessageConstraints(params);
        this.lineParser = parser != null ? parser : BasicLineParser.INSTANCE;
        this.headerLines = new ArrayList<CharArrayBuffer>();
        this.state = 0;
    }

    public AbstractMessageParser(SessionInputBuffer buffer, LineParser lineParser, MessageConstraints constraints) {
        this.sessionBuffer = Args.notNull(buffer, "Session input buffer");
        this.lineParser = lineParser != null ? lineParser : BasicLineParser.INSTANCE;
        this.messageConstraints = constraints != null ? constraints : MessageConstraints.DEFAULT;
        this.headerLines = new ArrayList<CharArrayBuffer>();
        this.state = 0;
    }

    public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser) throws HttpException, IOException {
        LineParser lineParser;
        ArrayList<CharArrayBuffer> headerLines = new ArrayList<CharArrayBuffer>();
        if (parser != null) {
            lineParser = parser;
            return AbstractMessageParser.parseHeaders(inbuffer, maxHeaderCount, maxLineLen, lineParser, headerLines);
        }
        lineParser = BasicLineParser.INSTANCE;
        return AbstractMessageParser.parseHeaders(inbuffer, maxHeaderCount, maxLineLen, lineParser, headerLines);
    }

    public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser, List<CharArrayBuffer> headerLines) throws HttpException, IOException {
        int i;
        block10 : {
            Args.notNull(inbuffer, "Session input buffer");
            Args.notNull(parser, "Line parser");
            Args.notNull(headerLines, "Header line list");
            CharArrayBuffer current = null;
            CharArrayBuffer previous = null;
            do {
                char ch;
                if (current == null) {
                    current = new CharArrayBuffer(64);
                } else {
                    current.clear();
                }
                int l = inbuffer.readLine(current);
                if (l == -1 || current.length() < 1) break block10;
                if ((current.charAt(0) == ' ' || current.charAt(0) == '\t') && previous != null) {
                } else {
                    headerLines.add(current);
                    previous = current;
                    current = null;
                    continue;
                }
                for (i = 0; i < current.length() && ((ch = current.charAt(i)) == ' ' || ch == '\t'); ++i) {
                }
                if (maxLineLen > 0 && previous.length() + 1 + current.length() - i > maxLineLen) {
                    throw new MessageConstraintException("Maximum line length limit exceeded");
                }
                previous.append(' ');
                previous.append(current, i, current.length() - i);
            } while (maxHeaderCount <= 0 || headerLines.size() < maxHeaderCount);
            throw new MessageConstraintException("Maximum header count exceeded");
        }
        Header[] headers = new Header[headerLines.size()];
        i = 0;
        while (i < headerLines.size()) {
            CharArrayBuffer buffer = headerLines.get(i);
            try {
                headers[i] = parser.parseHeader(buffer);
            }
            catch (ParseException ex) {
                throw new ProtocolException(ex.getMessage());
            }
            ++i;
        }
        return headers;
    }

    protected abstract T parseHead(SessionInputBuffer var1) throws IOException, HttpException, ParseException;

    @Override
    public T parse() throws IOException, HttpException {
        int st = this.state;
        switch (st) {
            case 0: {
                try {
                    this.message = this.parseHead(this.sessionBuffer);
                }
                catch (ParseException px) {
                    throw new ProtocolException(px.getMessage(), px);
                }
                this.state = 1;
            }
            case 1: {
                Header[] headers = AbstractMessageParser.parseHeaders(this.sessionBuffer, this.messageConstraints.getMaxHeaderCount(), this.messageConstraints.getMaxLineLength(), this.lineParser, this.headerLines);
                this.message.setHeaders(headers);
                T result = this.message;
                this.message = null;
                this.headerLines.clear();
                this.state = 0;
                return result;
            }
        }
        throw new IllegalStateException("Inconsistent parser state");
    }
}

