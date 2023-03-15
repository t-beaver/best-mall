package dc.squareup.okhttp3.internal.http;

import dc.squareup.okhttp3.Interceptor;
import dc.squareup.okhttp3.Request;
import dc.squareup.okhttp3.Response;
import dc.squareup.okhttp3.internal.Util;
import dc.squareup.okhttp3.internal.connection.RealConnection;
import dc.squareup.okhttp3.internal.connection.StreamAllocation;
import dc.squareup.okio.Buffer;
import dc.squareup.okio.BufferedSink;
import dc.squareup.okio.ForwardingSink;
import dc.squareup.okio.Okio;
import dc.squareup.okio.Sink;
import java.io.IOException;
import java.net.ProtocolException;

public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    static final class CountingSink extends ForwardingSink {
        long successfulCount;

        CountingSink(Sink sink) {
            super(sink);
        }

        public void write(Buffer buffer, long j) throws IOException {
            super.write(buffer, j);
            this.successfulCount += j;
        }
    }

    public CallServerInterceptor(boolean z) {
        this.forWebSocket = z;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response response;
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        HttpCodec httpStream = realInterceptorChain.httpStream();
        StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        RealConnection realConnection = (RealConnection) realInterceptorChain.connection();
        Request request = realInterceptorChain.request();
        long currentTimeMillis = System.currentTimeMillis();
        realInterceptorChain.eventListener().requestHeadersStart(realInterceptorChain.call());
        httpStream.writeRequestHeaders(request);
        realInterceptorChain.eventListener().requestHeadersEnd(realInterceptorChain.call(), request);
        Response.Builder builder = null;
        if (HttpMethod.permitsRequestBody(request.method()) && request.body() != null) {
            if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
                httpStream.flushRequest();
                realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
                builder = httpStream.readResponseHeaders(true);
            }
            if (builder == null) {
                realInterceptorChain.eventListener().requestBodyStart(realInterceptorChain.call());
                CountingSink countingSink = new CountingSink(httpStream.createRequestBody(request, request.body().contentLength()));
                BufferedSink buffer = Okio.buffer((Sink) countingSink);
                request.body().writeTo(buffer);
                buffer.close();
                realInterceptorChain.eventListener().requestBodyEnd(realInterceptorChain.call(), countingSink.successfulCount);
            } else if (!realConnection.isMultiplexed()) {
                streamAllocation.noNewStreams();
            }
        }
        httpStream.finishRequest();
        if (builder == null) {
            realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
            builder = httpStream.readResponseHeaders(false);
        }
        Response build = builder.request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(currentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int code = build.code();
        if (code == 100) {
            build = httpStream.readResponseHeaders(false).request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(currentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
            code = build.code();
        }
        realInterceptorChain.eventListener().responseHeadersEnd(realInterceptorChain.call(), build);
        if (!this.forWebSocket || code != 101) {
            response = build.newBuilder().body(httpStream.openResponseBody(build)).build();
        } else {
            response = build.newBuilder().body(Util.EMPTY_RESPONSE).build();
        }
        if (AbsoluteConst.EVENTS_CLOSE.equalsIgnoreCase(response.request().header("Connection")) || AbsoluteConst.EVENTS_CLOSE.equalsIgnoreCase(response.header("Connection"))) {
            streamAllocation.noNewStreams();
        }
        if ((code != 204 && code != 205) || response.body().contentLength() <= 0) {
            return response;
        }
        throw new ProtocolException("HTTP " + code + " had non-zero Content-Length: " + response.body().contentLength());
    }
}
