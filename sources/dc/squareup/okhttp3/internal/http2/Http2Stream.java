package dc.squareup.okhttp3.internal.http2;

import dc.squareup.okhttp3.Headers;
import dc.squareup.okhttp3.internal.Util;
import dc.squareup.okhttp3.internal.http2.Header;
import dc.squareup.okio.AsyncTimeout;
import dc.squareup.okio.Buffer;
import dc.squareup.okio.BufferedSource;
import dc.squareup.okio.Sink;
import dc.squareup.okio.Source;
import dc.squareup.okio.Timeout;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public final class Http2Stream {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    long bytesLeftInWriteWindow;
    final Http2Connection connection;
    ErrorCode errorCode;
    private boolean hasResponseHeaders;
    /* access modifiers changed from: private */
    public Header.Listener headersListener;
    /* access modifiers changed from: private */
    public final Deque<Headers> headersQueue;
    final int id;
    final StreamTimeout readTimeout;
    final FramingSink sink;
    private final FramingSource source;
    long unacknowledgedBytesRead = 0;
    final StreamTimeout writeTimeout;

    final class FramingSink implements Sink {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long EMIT_BUFFER_SIZE = 16384;
        boolean closed;
        boolean finished;
        private final Buffer sendBuffer = new Buffer();

        static {
            Class<Http2Stream> cls = Http2Stream.class;
        }

        FramingSink() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
            r1.writeTimeout.exitAndThrowIfTimedOut();
            r11.this$0.checkOutNotClosed();
            r9 = java.lang.Math.min(r11.this$0.bytesLeftInWriteWindow, r11.sendBuffer.size());
            r1 = r11.this$0;
            r1.bytesLeftInWriteWindow -= r9;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void emitFrame(boolean r12) throws java.io.IOException {
            /*
                r11 = this;
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                monitor-enter(r0)
                dc.squareup.okhttp3.internal.http2.Http2Stream r1 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x007f }
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r1 = r1.writeTimeout     // Catch:{ all -> 0x007f }
                r1.enter()     // Catch:{ all -> 0x007f }
            L_0x000a:
                dc.squareup.okhttp3.internal.http2.Http2Stream r1 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x0076 }
                long r2 = r1.bytesLeftInWriteWindow     // Catch:{ all -> 0x0076 }
                r4 = 0
                int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r6 > 0) goto L_0x0024
                boolean r2 = r11.finished     // Catch:{ all -> 0x0076 }
                if (r2 != 0) goto L_0x0024
                boolean r2 = r11.closed     // Catch:{ all -> 0x0076 }
                if (r2 != 0) goto L_0x0024
                dc.squareup.okhttp3.internal.http2.ErrorCode r2 = r1.errorCode     // Catch:{ all -> 0x0076 }
                if (r2 != 0) goto L_0x0024
                r1.waitForIo()     // Catch:{ all -> 0x0076 }
                goto L_0x000a
            L_0x0024:
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r1 = r1.writeTimeout     // Catch:{ all -> 0x007f }
                r1.exitAndThrowIfTimedOut()     // Catch:{ all -> 0x007f }
                dc.squareup.okhttp3.internal.http2.Http2Stream r1 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x007f }
                r1.checkOutNotClosed()     // Catch:{ all -> 0x007f }
                dc.squareup.okhttp3.internal.http2.Http2Stream r1 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x007f }
                long r1 = r1.bytesLeftInWriteWindow     // Catch:{ all -> 0x007f }
                dc.squareup.okio.Buffer r3 = r11.sendBuffer     // Catch:{ all -> 0x007f }
                long r3 = r3.size()     // Catch:{ all -> 0x007f }
                long r9 = java.lang.Math.min(r1, r3)     // Catch:{ all -> 0x007f }
                dc.squareup.okhttp3.internal.http2.Http2Stream r1 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x007f }
                long r2 = r1.bytesLeftInWriteWindow     // Catch:{ all -> 0x007f }
                long r2 = r2 - r9
                r1.bytesLeftInWriteWindow = r2     // Catch:{ all -> 0x007f }
                monitor-exit(r0)     // Catch:{ all -> 0x007f }
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r0 = r1.writeTimeout
                r0.enter()
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x006d }
                dc.squareup.okhttp3.internal.http2.Http2Connection r5 = r0.connection     // Catch:{ all -> 0x006d }
                int r6 = r0.id     // Catch:{ all -> 0x006d }
                if (r12 == 0) goto L_0x005e
                dc.squareup.okio.Buffer r12 = r11.sendBuffer     // Catch:{ all -> 0x006d }
                long r0 = r12.size()     // Catch:{ all -> 0x006d }
                int r12 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
                if (r12 != 0) goto L_0x005e
                r12 = 1
                r7 = 1
                goto L_0x0060
            L_0x005e:
                r12 = 0
                r7 = 0
            L_0x0060:
                dc.squareup.okio.Buffer r8 = r11.sendBuffer     // Catch:{ all -> 0x006d }
                r5.writeData(r6, r7, r8, r9)     // Catch:{ all -> 0x006d }
                dc.squareup.okhttp3.internal.http2.Http2Stream r12 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r12 = r12.writeTimeout
                r12.exitAndThrowIfTimedOut()
                return
            L_0x006d:
                r12 = move-exception
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r0 = r0.writeTimeout
                r0.exitAndThrowIfTimedOut()
                throw r12
            L_0x0076:
                r12 = move-exception
                dc.squareup.okhttp3.internal.http2.Http2Stream r1 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x007f }
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r1 = r1.writeTimeout     // Catch:{ all -> 0x007f }
                r1.exitAndThrowIfTimedOut()     // Catch:{ all -> 0x007f }
                throw r12     // Catch:{ all -> 0x007f }
            L_0x007f:
                r12 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x007f }
                goto L_0x0083
            L_0x0082:
                throw r12
            L_0x0083:
                goto L_0x0082
            */
            throw new UnsupportedOperationException("Method not decompiled: dc.squareup.okhttp3.internal.http2.Http2Stream.FramingSink.emitFrame(boolean):void");
        }

        public Timeout timeout() {
            return Http2Stream.this.writeTimeout;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
            if (r8.sendBuffer.size() <= 0) goto L_0x002d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0027, code lost:
            if (r8.sendBuffer.size() <= 0) goto L_0x003a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0029, code lost:
            emitFrame(true);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
            r0 = r8.this$0;
            r0.connection.writeData(r0.id, true, (dc.squareup.okio.Buffer) null, 0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x003a, code lost:
            r2 = r8.this$0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x003c, code lost:
            monitor-enter(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            r8.closed = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x003f, code lost:
            monitor-exit(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0040, code lost:
            r8.this$0.connection.flush();
            r8.this$0.cancelStreamIfNecessary();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x004c, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0011, code lost:
            if (r8.this$0.sink.finished != false) goto L_0x003a;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void close() throws java.io.IOException {
            /*
                r8 = this;
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                monitor-enter(r0)
                boolean r1 = r8.closed     // Catch:{ all -> 0x0050 }
                if (r1 == 0) goto L_0x0009
                monitor-exit(r0)     // Catch:{ all -> 0x0050 }
                return
            L_0x0009:
                monitor-exit(r0)     // Catch:{ all -> 0x0050 }
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                dc.squareup.okhttp3.internal.http2.Http2Stream$FramingSink r0 = r0.sink
                boolean r0 = r0.finished
                r1 = 1
                if (r0 != 0) goto L_0x003a
                dc.squareup.okio.Buffer r0 = r8.sendBuffer
                long r2 = r0.size()
                r4 = 0
                int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r0 <= 0) goto L_0x002d
            L_0x001f:
                dc.squareup.okio.Buffer r0 = r8.sendBuffer
                long r2 = r0.size()
                int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r0 <= 0) goto L_0x003a
                r8.emitFrame(r1)
                goto L_0x001f
            L_0x002d:
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                dc.squareup.okhttp3.internal.http2.Http2Connection r2 = r0.connection
                int r3 = r0.id
                r4 = 1
                r5 = 0
                r6 = 0
                r2.writeData(r3, r4, r5, r6)
            L_0x003a:
                dc.squareup.okhttp3.internal.http2.Http2Stream r2 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                monitor-enter(r2)
                r8.closed = r1     // Catch:{ all -> 0x004d }
                monitor-exit(r2)     // Catch:{ all -> 0x004d }
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                dc.squareup.okhttp3.internal.http2.Http2Connection r0 = r0.connection
                r0.flush()
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                r0.cancelStreamIfNecessary()
                return
            L_0x004d:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x004d }
                throw r0
            L_0x0050:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0050 }
                goto L_0x0054
            L_0x0053:
                throw r1
            L_0x0054:
                goto L_0x0053
            */
            throw new UnsupportedOperationException("Method not decompiled: dc.squareup.okhttp3.internal.http2.Http2Stream.FramingSink.close():void");
        }

        public void flush() throws IOException {
            synchronized (Http2Stream.this) {
                Http2Stream.this.checkOutNotClosed();
            }
            while (this.sendBuffer.size() > 0) {
                emitFrame(false);
                Http2Stream.this.connection.flush();
            }
        }

        public void write(Buffer buffer, long j) throws IOException {
            this.sendBuffer.write(buffer, j);
            while (this.sendBuffer.size() >= 16384) {
                emitFrame(false);
            }
        }
    }

    private final class FramingSource implements Source {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        boolean closed;
        boolean finished;
        private final long maxByteCount;
        private final Buffer readBuffer = new Buffer();
        private final Buffer receiveBuffer = new Buffer();

        static {
            Class<Http2Stream> cls = Http2Stream.class;
        }

        FramingSource(long j) {
            this.maxByteCount = j;
        }

        public void close() throws IOException {
            long size;
            Header.Listener listener;
            ArrayList<Headers> arrayList;
            synchronized (Http2Stream.this) {
                this.closed = true;
                size = this.readBuffer.size();
                this.readBuffer.clear();
                listener = null;
                if (Http2Stream.this.headersQueue.isEmpty() || Http2Stream.this.headersListener == null) {
                    arrayList = null;
                } else {
                    ArrayList arrayList2 = new ArrayList(Http2Stream.this.headersQueue);
                    Http2Stream.this.headersQueue.clear();
                    ArrayList arrayList3 = arrayList2;
                    listener = Http2Stream.this.headersListener;
                    arrayList = arrayList3;
                }
                Http2Stream.this.notifyAll();
            }
            if (size > 0) {
                updateConnectionFlowControl(size);
            }
            Http2Stream.this.cancelStreamIfNecessary();
            if (listener != null) {
                for (Headers onHeaders : arrayList) {
                    listener.onHeaders(onHeaders);
                }
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v33, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v6, resolved type: dc.squareup.okhttp3.Headers} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long read(dc.squareup.okio.Buffer r17, long r18) throws java.io.IOException {
            /*
                r16 = this;
                r1 = r16
                r2 = r18
                r4 = 0
                int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r0 < 0) goto L_0x00e0
            L_0x000a:
                dc.squareup.okhttp3.internal.http2.Http2Stream r6 = dc.squareup.okhttp3.internal.http2.Http2Stream.this
                monitor-enter(r6)
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00dd }
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r0 = r0.readTimeout     // Catch:{ all -> 0x00dd }
                r0.enter()     // Catch:{ all -> 0x00dd }
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00d4 }
                dc.squareup.okhttp3.internal.http2.ErrorCode r7 = r0.errorCode     // Catch:{ all -> 0x00d4 }
                if (r7 == 0) goto L_0x001b
                goto L_0x001c
            L_0x001b:
                r7 = 0
            L_0x001c:
                boolean r9 = r1.closed     // Catch:{ all -> 0x00d4 }
                if (r9 != 0) goto L_0x00cc
                java.util.Deque r0 = r0.headersQueue     // Catch:{ all -> 0x00d4 }
                boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x00d4 }
                if (r0 != 0) goto L_0x0049
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00d4 }
                dc.squareup.okhttp3.internal.http2.Header$Listener r0 = r0.headersListener     // Catch:{ all -> 0x00d4 }
                if (r0 == 0) goto L_0x0049
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00d4 }
                java.util.Deque r0 = r0.headersQueue     // Catch:{ all -> 0x00d4 }
                java.lang.Object r0 = r0.removeFirst()     // Catch:{ all -> 0x00d4 }
                r8 = r0
                dc.squareup.okhttp3.Headers r8 = (dc.squareup.okhttp3.Headers) r8     // Catch:{ all -> 0x00d4 }
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00d4 }
                dc.squareup.okhttp3.internal.http2.Header$Listener r0 = r0.headersListener     // Catch:{ all -> 0x00d4 }
                r13 = r17
                r10 = r0
                goto L_0x00a6
            L_0x0049:
                dc.squareup.okio.Buffer r0 = r1.readBuffer     // Catch:{ all -> 0x00d4 }
                long r11 = r0.size()     // Catch:{ all -> 0x00d4 }
                int r0 = (r11 > r4 ? 1 : (r11 == r4 ? 0 : -1))
                if (r0 <= 0) goto L_0x008d
                dc.squareup.okio.Buffer r0 = r1.readBuffer     // Catch:{ all -> 0x00d4 }
                long r11 = r0.size()     // Catch:{ all -> 0x00d4 }
                long r11 = java.lang.Math.min(r2, r11)     // Catch:{ all -> 0x00d4 }
                r13 = r17
                long r11 = r0.read(r13, r11)     // Catch:{ all -> 0x00d4 }
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00d4 }
                long r14 = r0.unacknowledgedBytesRead     // Catch:{ all -> 0x00d4 }
                long r14 = r14 + r11
                r0.unacknowledgedBytesRead = r14     // Catch:{ all -> 0x00d4 }
                if (r7 != 0) goto L_0x008a
                dc.squareup.okhttp3.internal.http2.Http2Connection r0 = r0.connection     // Catch:{ all -> 0x00d4 }
                dc.squareup.okhttp3.internal.http2.Settings r0 = r0.okHttpSettings     // Catch:{ all -> 0x00d4 }
                int r0 = r0.getInitialWindowSize()     // Catch:{ all -> 0x00d4 }
                int r0 = r0 / 2
                long r8 = (long) r0     // Catch:{ all -> 0x00d4 }
                int r0 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1))
                if (r0 < 0) goto L_0x008a
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00d4 }
                dc.squareup.okhttp3.internal.http2.Http2Connection r8 = r0.connection     // Catch:{ all -> 0x00d4 }
                int r9 = r0.id     // Catch:{ all -> 0x00d4 }
                long r14 = r0.unacknowledgedBytesRead     // Catch:{ all -> 0x00d4 }
                r8.writeWindowUpdateLater(r9, r14)     // Catch:{ all -> 0x00d4 }
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00d4 }
                r0.unacknowledgedBytesRead = r4     // Catch:{ all -> 0x00d4 }
            L_0x008a:
                r8 = 0
                r10 = 0
                goto L_0x00a8
            L_0x008d:
                r13 = r17
                boolean r0 = r1.finished     // Catch:{ all -> 0x00d4 }
                if (r0 != 0) goto L_0x00a4
                if (r7 != 0) goto L_0x00a4
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00d4 }
                r0.waitForIo()     // Catch:{ all -> 0x00d4 }
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00dd }
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r0 = r0.readTimeout     // Catch:{ all -> 0x00dd }
                r0.exitAndThrowIfTimedOut()     // Catch:{ all -> 0x00dd }
                monitor-exit(r6)     // Catch:{ all -> 0x00dd }
                goto L_0x000a
            L_0x00a4:
                r8 = 0
                r10 = 0
            L_0x00a6:
                r11 = -1
            L_0x00a8:
                dc.squareup.okhttp3.internal.http2.Http2Stream r0 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00dd }
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r0 = r0.readTimeout     // Catch:{ all -> 0x00dd }
                r0.exitAndThrowIfTimedOut()     // Catch:{ all -> 0x00dd }
                monitor-exit(r6)     // Catch:{ all -> 0x00dd }
                if (r8 == 0) goto L_0x00b9
                if (r10 == 0) goto L_0x00b9
                r10.onHeaders(r8)
                goto L_0x000a
            L_0x00b9:
                r2 = -1
                int r0 = (r11 > r2 ? 1 : (r11 == r2 ? 0 : -1))
                if (r0 == 0) goto L_0x00c3
                r1.updateConnectionFlowControl(r11)
                return r11
            L_0x00c3:
                if (r7 != 0) goto L_0x00c6
                return r2
            L_0x00c6:
                dc.squareup.okhttp3.internal.http2.StreamResetException r0 = new dc.squareup.okhttp3.internal.http2.StreamResetException
                r0.<init>(r7)
                throw r0
            L_0x00cc:
                java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x00d4 }
                java.lang.String r2 = "stream closed"
                r0.<init>(r2)     // Catch:{ all -> 0x00d4 }
                throw r0     // Catch:{ all -> 0x00d4 }
            L_0x00d4:
                r0 = move-exception
                dc.squareup.okhttp3.internal.http2.Http2Stream r2 = dc.squareup.okhttp3.internal.http2.Http2Stream.this     // Catch:{ all -> 0x00dd }
                dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r2 = r2.readTimeout     // Catch:{ all -> 0x00dd }
                r2.exitAndThrowIfTimedOut()     // Catch:{ all -> 0x00dd }
                throw r0     // Catch:{ all -> 0x00dd }
            L_0x00dd:
                r0 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x00dd }
                throw r0
            L_0x00e0:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "byteCount < 0: "
                r4.append(r5)
                r4.append(r2)
                java.lang.String r2 = r4.toString()
                r0.<init>(r2)
                goto L_0x00f8
            L_0x00f7:
                throw r0
            L_0x00f8:
                goto L_0x00f7
            */
            throw new UnsupportedOperationException("Method not decompiled: dc.squareup.okhttp3.internal.http2.Http2Stream.FramingSource.read(dc.squareup.okio.Buffer, long):long");
        }

        public Timeout timeout() {
            return Http2Stream.this.readTimeout;
        }

        private void updateConnectionFlowControl(long j) {
            Http2Stream.this.connection.updateConnectionFlowControl(j);
        }

        /* access modifiers changed from: package-private */
        public void receive(BufferedSource bufferedSource, long j) throws IOException {
            boolean z;
            boolean z2;
            boolean z3;
            long j2;
            while (j > 0) {
                synchronized (Http2Stream.this) {
                    z = this.finished;
                    z2 = true;
                    z3 = this.readBuffer.size() + j > this.maxByteCount;
                }
                if (z3) {
                    bufferedSource.skip(j);
                    Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                    return;
                } else if (z) {
                    bufferedSource.skip(j);
                    return;
                } else {
                    long read = bufferedSource.read(this.receiveBuffer, j);
                    if (read != -1) {
                        j -= read;
                        synchronized (Http2Stream.this) {
                            if (this.closed) {
                                j2 = this.receiveBuffer.size();
                                this.receiveBuffer.clear();
                            } else {
                                if (this.readBuffer.size() != 0) {
                                    z2 = false;
                                }
                                this.readBuffer.writeAll(this.receiveBuffer);
                                if (z2) {
                                    Http2Stream.this.notifyAll();
                                }
                                j2 = 0;
                            }
                        }
                        if (j2 > 0) {
                            updateConnectionFlowControl(j2);
                        }
                    } else {
                        throw new EOFException();
                    }
                }
            }
        }
    }

    class StreamTimeout extends AsyncTimeout {
        StreamTimeout() {
        }

        public void exitAndThrowIfTimedOut() throws IOException {
            if (exit()) {
                throw newTimeoutException((IOException) null);
            }
        }

        /* access modifiers changed from: protected */
        public IOException newTimeoutException(IOException iOException) {
            SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
            if (iOException != null) {
                socketTimeoutException.initCause(iOException);
            }
            return socketTimeoutException;
        }

        /* access modifiers changed from: protected */
        public void timedOut() {
            Http2Stream.this.closeLater(ErrorCode.CANCEL);
            Http2Stream.this.connection.sendDegradedPingLater();
        }
    }

    Http2Stream(int i, Http2Connection http2Connection, boolean z, boolean z2, Headers headers) {
        ArrayDeque arrayDeque = new ArrayDeque();
        this.headersQueue = arrayDeque;
        this.readTimeout = new StreamTimeout();
        this.writeTimeout = new StreamTimeout();
        this.errorCode = null;
        Objects.requireNonNull(http2Connection, "connection == null");
        this.id = i;
        this.connection = http2Connection;
        this.bytesLeftInWriteWindow = (long) http2Connection.peerSettings.getInitialWindowSize();
        FramingSource framingSource = new FramingSource((long) http2Connection.okHttpSettings.getInitialWindowSize());
        this.source = framingSource;
        FramingSink framingSink = new FramingSink();
        this.sink = framingSink;
        framingSource.finished = z2;
        framingSink.finished = z;
        if (headers != null) {
            arrayDeque.add(headers);
        }
        if (isLocallyInitiated() && headers != null) {
            throw new IllegalStateException("locally-initiated streams shouldn't have headers yet");
        } else if (!isLocallyInitiated() && headers == null) {
            throw new IllegalStateException("remotely-initiated streams should have headers");
        }
    }

    /* access modifiers changed from: package-private */
    public void addBytesToWriteWindow(long j) {
        this.bytesLeftInWriteWindow += j;
        if (j > 0) {
            notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    public void checkOutNotClosed() throws IOException {
        FramingSink framingSink = this.sink;
        if (framingSink.closed) {
            throw new IOException("stream closed");
        } else if (framingSink.finished) {
            throw new IOException("stream finished");
        } else if (this.errorCode != null) {
            throw new StreamResetException(this.errorCode);
        }
    }

    public void close(ErrorCode errorCode2) throws IOException {
        if (closeInternal(errorCode2)) {
            this.connection.writeSynReset(this.id, errorCode2);
        }
    }

    public void closeLater(ErrorCode errorCode2) {
        if (closeInternal(errorCode2)) {
            this.connection.writeSynResetLater(this.id, errorCode2);
        }
    }

    public Http2Connection getConnection() {
        return this.connection;
    }

    public synchronized ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public int getId() {
        return this.id;
    }

    public Sink getSink() {
        synchronized (this) {
            if (!this.hasResponseHeaders) {
                if (!isLocallyInitiated()) {
                    throw new IllegalStateException("reply before requesting the sink");
                }
            }
        }
        return this.sink;
    }

    public Source getSource() {
        return this.source;
    }

    public boolean isLocallyInitiated() {
        if (this.connection.client == ((this.id & 1) == 1)) {
            return true;
        }
        return false;
    }

    public synchronized boolean isOpen() {
        if (this.errorCode != null) {
            return false;
        }
        FramingSource framingSource = this.source;
        if (framingSource.finished || framingSource.closed) {
            FramingSink framingSink = this.sink;
            if ((framingSink.finished || framingSink.closed) && this.hasResponseHeaders) {
                return false;
            }
        }
        return true;
    }

    public Timeout readTimeout() {
        return this.readTimeout;
    }

    /* access modifiers changed from: package-private */
    public synchronized void receiveRstStream(ErrorCode errorCode2) {
        if (this.errorCode == null) {
            this.errorCode = errorCode2;
            notifyAll();
        }
    }

    public synchronized void setHeadersListener(Header.Listener listener) {
        this.headersListener = listener;
        if (!this.headersQueue.isEmpty() && listener != null) {
            notifyAll();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0035, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0036, code lost:
        r2.readTimeout.exitAndThrowIfTimedOut();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003b, code lost:
        throw r0;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized dc.squareup.okhttp3.Headers takeHeaders() throws java.io.IOException {
        /*
            r2 = this;
            monitor-enter(r2)
            dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r0 = r2.readTimeout     // Catch:{ all -> 0x003c }
            r0.enter()     // Catch:{ all -> 0x003c }
        L_0x0006:
            java.util.Deque<dc.squareup.okhttp3.Headers> r0 = r2.headersQueue     // Catch:{ all -> 0x0035 }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x0035 }
            if (r0 == 0) goto L_0x0016
            dc.squareup.okhttp3.internal.http2.ErrorCode r0 = r2.errorCode     // Catch:{ all -> 0x0035 }
            if (r0 != 0) goto L_0x0016
            r2.waitForIo()     // Catch:{ all -> 0x0035 }
            goto L_0x0006
        L_0x0016:
            dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r0 = r2.readTimeout     // Catch:{ all -> 0x003c }
            r0.exitAndThrowIfTimedOut()     // Catch:{ all -> 0x003c }
            java.util.Deque<dc.squareup.okhttp3.Headers> r0 = r2.headersQueue     // Catch:{ all -> 0x003c }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x003c }
            if (r0 != 0) goto L_0x002d
            java.util.Deque<dc.squareup.okhttp3.Headers> r0 = r2.headersQueue     // Catch:{ all -> 0x003c }
            java.lang.Object r0 = r0.removeFirst()     // Catch:{ all -> 0x003c }
            dc.squareup.okhttp3.Headers r0 = (dc.squareup.okhttp3.Headers) r0     // Catch:{ all -> 0x003c }
            monitor-exit(r2)
            return r0
        L_0x002d:
            dc.squareup.okhttp3.internal.http2.StreamResetException r0 = new dc.squareup.okhttp3.internal.http2.StreamResetException     // Catch:{ all -> 0x003c }
            dc.squareup.okhttp3.internal.http2.ErrorCode r1 = r2.errorCode     // Catch:{ all -> 0x003c }
            r0.<init>(r1)     // Catch:{ all -> 0x003c }
            throw r0     // Catch:{ all -> 0x003c }
        L_0x0035:
            r0 = move-exception
            dc.squareup.okhttp3.internal.http2.Http2Stream$StreamTimeout r1 = r2.readTimeout     // Catch:{ all -> 0x003c }
            r1.exitAndThrowIfTimedOut()     // Catch:{ all -> 0x003c }
            throw r0     // Catch:{ all -> 0x003c }
        L_0x003c:
            r0 = move-exception
            monitor-exit(r2)
            goto L_0x0040
        L_0x003f:
            throw r0
        L_0x0040:
            goto L_0x003f
        */
        throw new UnsupportedOperationException("Method not decompiled: dc.squareup.okhttp3.internal.http2.Http2Stream.takeHeaders():dc.squareup.okhttp3.Headers");
    }

    /* access modifiers changed from: package-private */
    public void waitForIo() throws InterruptedIOException {
        try {
            wait();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
        }
    }

    public Timeout writeTimeout() {
        return this.writeTimeout;
    }

    private boolean closeInternal(ErrorCode errorCode2) {
        synchronized (this) {
            if (this.errorCode != null) {
                return false;
            }
            if (this.source.finished && this.sink.finished) {
                return false;
            }
            this.errorCode = errorCode2;
            notifyAll();
            this.connection.removeStream(this.id);
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public void receiveData(BufferedSource bufferedSource, int i) throws IOException {
        this.source.receive(bufferedSource, (long) i);
    }

    /* access modifiers changed from: package-private */
    public void receiveFin() {
        boolean isOpen;
        synchronized (this) {
            this.source.finished = true;
            isOpen = isOpen();
            notifyAll();
        }
        if (!isOpen) {
            this.connection.removeStream(this.id);
        }
    }

    /* access modifiers changed from: package-private */
    public void receiveHeaders(List<Header> list) {
        boolean isOpen;
        synchronized (this) {
            this.hasResponseHeaders = true;
            this.headersQueue.add(Util.toHeaders(list));
            isOpen = isOpen();
            notifyAll();
        }
        if (!isOpen) {
            this.connection.removeStream(this.id);
        }
    }

    /* access modifiers changed from: package-private */
    public void cancelStreamIfNecessary() throws IOException {
        boolean z;
        boolean isOpen;
        synchronized (this) {
            FramingSource framingSource = this.source;
            if (!framingSource.finished && framingSource.closed) {
                FramingSink framingSink = this.sink;
                if (framingSink.finished || framingSink.closed) {
                    z = true;
                    isOpen = isOpen();
                }
            }
            z = false;
            isOpen = isOpen();
        }
        if (z) {
            close(ErrorCode.CANCEL);
        } else if (!isOpen) {
            this.connection.removeStream(this.id);
        }
    }

    public void writeHeaders(List<Header> list, boolean z) throws IOException {
        boolean z2;
        boolean z3;
        boolean z4;
        Objects.requireNonNull(list, "headers == null");
        synchronized (this) {
            z2 = true;
            this.hasResponseHeaders = true;
            if (!z) {
                this.sink.finished = true;
                z3 = true;
                z4 = true;
            } else {
                z3 = false;
                z4 = false;
            }
        }
        if (!z3) {
            synchronized (this.connection) {
                if (this.connection.bytesLeftInWriteWindow != 0) {
                    z2 = false;
                }
            }
            z3 = z2;
        }
        this.connection.writeSynReply(this.id, z4, list);
        if (z3) {
            this.connection.flush();
        }
    }
}
