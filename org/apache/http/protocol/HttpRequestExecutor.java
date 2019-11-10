/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class HttpRequestExecutor {
    public static final int DEFAULT_WAIT_FOR_CONTINUE = 3000;
    private final int waitForContinue;

    public HttpRequestExecutor(int waitForContinue) {
        this.waitForContinue = Args.positive(waitForContinue, "Wait for continue time");
    }

    public HttpRequestExecutor() {
        this(3000);
    }

    protected boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
        if ("HEAD".equalsIgnoreCase(request.getRequestLine().getMethod())) {
            return false;
        }
        int status = response.getStatusLine().getStatusCode();
        if (status < 200) return false;
        if (status == 204) return false;
        if (status == 304) return false;
        if (status == 205) return false;
        return true;
    }

    public HttpResponse execute(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
        Args.notNull(request, "HTTP request");
        Args.notNull(conn, "Client connection");
        Args.notNull(context, "HTTP context");
        try {
            HttpResponse response = this.doSendRequest(request, conn, context);
            if (response != null) return response;
            return this.doReceiveResponse(request, conn, context);
        }
        catch (IOException ex) {
            HttpRequestExecutor.closeConnection(conn);
            throw ex;
        }
        catch (HttpException ex) {
            HttpRequestExecutor.closeConnection(conn);
            throw ex;
        }
        catch (RuntimeException ex) {
            HttpRequestExecutor.closeConnection(conn);
            throw ex;
        }
    }

    private static void closeConnection(HttpClientConnection conn) {
        try {
            conn.close();
            return;
        }
        catch (IOException ignore) {
            // empty catch block
        }
    }

    public void preProcess(HttpRequest request, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(processor, "HTTP processor");
        Args.notNull(context, "HTTP context");
        context.setAttribute("http.request", request);
        processor.process(request, context);
    }

    protected HttpResponse doSendRequest(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
        Args.notNull(request, "HTTP request");
        Args.notNull(conn, "Client connection");
        Args.notNull(context, "HTTP context");
        HttpResponse response = null;
        context.setAttribute("http.connection", conn);
        context.setAttribute("http.request_sent", Boolean.FALSE);
        conn.sendRequestHeader(request);
        if (request instanceof HttpEntityEnclosingRequest) {
            boolean sendentity = true;
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            if (((HttpEntityEnclosingRequest)request).expectContinue() && !ver.lessEquals(HttpVersion.HTTP_1_0)) {
                conn.flush();
                if (conn.isResponseAvailable(this.waitForContinue)) {
                    int status;
                    response = conn.receiveResponseHeader();
                    if (this.canResponseHaveBody(request, response)) {
                        conn.receiveResponseEntity(response);
                    }
                    if ((status = response.getStatusLine().getStatusCode()) < 200) {
                        if (status != 100) {
                            throw new ProtocolException("Unexpected response: " + response.getStatusLine());
                        }
                        response = null;
                    } else {
                        sendentity = false;
                    }
                }
            }
            if (sendentity) {
                conn.sendRequestEntity((HttpEntityEnclosingRequest)request);
            }
        }
        conn.flush();
        context.setAttribute("http.request_sent", Boolean.TRUE);
        return response;
    }

    protected HttpResponse doReceiveResponse(HttpRequest request, HttpClientConnection conn, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(conn, "Client connection");
        Args.notNull(context, "HTTP context");
        HttpResponse response = null;
        int statusCode = 0;
        do {
            if (response != null) {
                if (statusCode >= 200) return response;
            }
            if ((statusCode = (response = conn.receiveResponseHeader()).getStatusLine().getStatusCode()) < 100) {
                throw new ProtocolException("Invalid response: " + response.getStatusLine());
            }
            if (!this.canResponseHaveBody(request, response)) continue;
            conn.receiveResponseEntity(response);
        } while (true);
    }

    public void postProcess(HttpResponse response, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        Args.notNull(processor, "HTTP processor");
        Args.notNull(context, "HTTP context");
        context.setAttribute("http.response", response);
        processor.process(response, context);
    }
}

