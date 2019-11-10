/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import org.apache.http.impl.io.AbstractSessionInputBuffer;
import org.apache.http.io.EofSensor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
public class SocketInputBuffer
extends AbstractSessionInputBuffer
implements EofSensor {
    private final Socket socket;
    private boolean eof;

    public SocketInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        Args.notNull(socket, "Socket");
        this.socket = socket;
        this.eof = false;
        int n = buffersize;
        if (n < 0) {
            n = socket.getReceiveBufferSize();
        }
        if (n < 1024) {
            n = 1024;
        }
        this.init(socket.getInputStream(), n, params);
    }

    @Override
    protected int fillBuffer() throws IOException {
        int i = super.fillBuffer();
        this.eof = i == -1;
        return i;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isDataAvailable(int timeout) throws IOException {
        boolean result = this.hasBufferedData();
        if (result) return result;
        int oldtimeout = this.socket.getSoTimeout();
        try {
            this.socket.setSoTimeout(timeout);
            this.fillBuffer();
            result = this.hasBufferedData();
            return result;
        }
        finally {
            this.socket.setSoTimeout(oldtimeout);
        }
    }

    @Override
    public boolean isEof() {
        return this.eof;
    }
}

