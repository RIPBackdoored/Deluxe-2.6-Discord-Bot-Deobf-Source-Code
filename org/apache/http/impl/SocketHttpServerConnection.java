/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import org.apache.http.HttpInetConnection;
import org.apache.http.impl.AbstractHttpServerConnection;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class SocketHttpServerConnection
extends AbstractHttpServerConnection
implements HttpInetConnection {
    private volatile boolean open;
    private volatile Socket socket = null;

    protected void assertNotOpen() {
        Asserts.check(!this.open, "Connection is already open");
    }

    @Override
    protected void assertOpen() {
        Asserts.check(this.open, "Connection is not open");
    }

    protected SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        return new SocketInputBuffer(socket, buffersize, params);
    }

    protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        return new SocketOutputBuffer(socket, buffersize, params);
    }

    protected void bind(Socket socket, HttpParams params) throws IOException {
        Args.notNull(socket, "Socket");
        Args.notNull(params, "HTTP parameters");
        this.socket = socket;
        int buffersize = params.getIntParameter("http.socket.buffer-size", -1);
        this.init(this.createSessionInputBuffer(socket, buffersize, params), this.createSessionOutputBuffer(socket, buffersize, params), params);
        this.open = true;
    }

    protected Socket getSocket() {
        return this.socket;
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public InetAddress getLocalAddress() {
        if (this.socket == null) return null;
        return this.socket.getLocalAddress();
    }

    @Override
    public int getLocalPort() {
        if (this.socket == null) return -1;
        return this.socket.getLocalPort();
    }

    @Override
    public InetAddress getRemoteAddress() {
        if (this.socket == null) return null;
        return this.socket.getInetAddress();
    }

    @Override
    public int getRemotePort() {
        if (this.socket == null) return -1;
        return this.socket.getPort();
    }

    @Override
    public void setSocketTimeout(int timeout) {
        this.assertOpen();
        if (this.socket == null) return;
        try {
            this.socket.setSoTimeout(timeout);
            return;
        }
        catch (SocketException ignore) {
            // empty catch block
        }
    }

    @Override
    public int getSocketTimeout() {
        if (this.socket == null) return -1;
        try {
            return this.socket.getSoTimeout();
        }
        catch (SocketException ignore) {
            return -1;
        }
    }

    @Override
    public void shutdown() throws IOException {
        this.open = false;
        Socket tmpsocket = this.socket;
        if (tmpsocket == null) return;
        tmpsocket.close();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() throws IOException {
        if (!this.open) {
            return;
        }
        this.open = false;
        this.open = false;
        Socket sock = this.socket;
        try {
            this.doFlush();
            try {
                try {
                    sock.shutdownOutput();
                }
                catch (IOException ignore) {
                    // empty catch block
                }
                try {
                    sock.shutdownInput();
                    return;
                }
                catch (IOException ignore) {
                    return;
                }
            }
            catch (UnsupportedOperationException ignore) {
                // empty catch block
                return;
            }
        }
        finally {
            sock.close();
        }
    }

    private static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
        if (!(socketAddress instanceof InetSocketAddress)) {
            buffer.append(socketAddress);
            return;
        }
        InetSocketAddress addr = (InetSocketAddress)socketAddress;
        buffer.append(addr.getAddress() != null ? addr.getAddress().getHostAddress() : addr.getAddress()).append(':').append(addr.getPort());
    }

    public String toString() {
        if (this.socket == null) return Object.super.toString();
        StringBuilder buffer = new StringBuilder();
        SocketAddress remoteAddress = this.socket.getRemoteSocketAddress();
        SocketAddress localAddress = this.socket.getLocalSocketAddress();
        if (remoteAddress == null) return buffer.toString();
        if (localAddress == null) return buffer.toString();
        SocketHttpServerConnection.formatAddress(buffer, localAddress);
        buffer.append("<->");
        SocketHttpServerConnection.formatAddress(buffer, remoteAddress);
        return buffer.toString();
    }
}

