package dc.squareup.okio;

import android.support.v4.media.session.PlaybackStateCompat;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.IApp;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

final class RealBufferedSource implements BufferedSource {
    public final Buffer buffer = new Buffer();
    boolean closed;
    public final Source source;

    RealBufferedSource(Source source2) {
        Objects.requireNonNull(source2, "source == null");
        this.source = source2;
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.source.close();
            this.buffer.clear();
        }
    }

    public boolean exhausted() throws IOException {
        if (!this.closed) {
            return this.buffer.exhausted() && this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1;
        }
        throw new IllegalStateException("closed");
    }

    public Buffer getBuffer() {
        return this.buffer;
    }

    public long indexOf(byte b) throws IOException {
        return indexOf(b, 0, Long.MAX_VALUE);
    }

    public long indexOfElement(ByteString byteString) throws IOException {
        return indexOfElement(byteString, 0);
    }

    public InputStream inputStream() {
        return new InputStream() {
            public int available() throws IOException {
                RealBufferedSource realBufferedSource = RealBufferedSource.this;
                if (!realBufferedSource.closed) {
                    return (int) Math.min(realBufferedSource.buffer.size, 2147483647L);
                }
                throw new IOException("closed");
            }

            public void close() throws IOException {
                RealBufferedSource.this.close();
            }

            public int read() throws IOException {
                RealBufferedSource realBufferedSource = RealBufferedSource.this;
                if (!realBufferedSource.closed) {
                    Buffer buffer = realBufferedSource.buffer;
                    if (buffer.size == 0 && realBufferedSource.source.read(buffer, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1) {
                        return -1;
                    }
                    return RealBufferedSource.this.buffer.readByte() & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
                }
                throw new IOException("closed");
            }

            public String toString() {
                return RealBufferedSource.this + ".inputStream()";
            }

            public int read(byte[] bArr, int i, int i2) throws IOException {
                if (!RealBufferedSource.this.closed) {
                    Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
                    RealBufferedSource realBufferedSource = RealBufferedSource.this;
                    Buffer buffer = realBufferedSource.buffer;
                    if (buffer.size == 0 && realBufferedSource.source.read(buffer, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1) {
                        return -1;
                    }
                    return RealBufferedSource.this.buffer.read(bArr, i, i2);
                }
                throw new IOException("closed");
            }
        };
    }

    public boolean isOpen() {
        return !this.closed;
    }

    public BufferedSource peek() {
        return Okio.buffer((Source) new PeekSource(this));
    }

    public boolean rangeEquals(long j, ByteString byteString) throws IOException {
        return rangeEquals(j, byteString, 0, byteString.size());
    }

    public long read(Buffer buffer2, long j) throws IOException {
        if (buffer2 == null) {
            throw new IllegalArgumentException("sink == null");
        } else if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        } else if (!this.closed) {
            Buffer buffer3 = this.buffer;
            if (buffer3.size == 0 && this.source.read(buffer3, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1) {
                return -1;
            }
            return this.buffer.read(buffer2, Math.min(j, this.buffer.size));
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public long readAll(Sink sink) throws IOException {
        if (sink != null) {
            long j = 0;
            while (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_URI) != -1) {
                long completeSegmentByteCount = this.buffer.completeSegmentByteCount();
                if (completeSegmentByteCount > 0) {
                    j += completeSegmentByteCount;
                    sink.write(this.buffer, completeSegmentByteCount);
                }
            }
            if (this.buffer.size() <= 0) {
                return j;
            }
            long size = j + this.buffer.size();
            Buffer buffer2 = this.buffer;
            sink.write(buffer2, buffer2.size());
            return size;
        }
        throw new IllegalArgumentException("sink == null");
    }

    public byte readByte() throws IOException {
        require(1);
        return this.buffer.readByte();
    }

    public byte[] readByteArray() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }

    public ByteString readByteString() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long readDecimalLong() throws java.io.IOException {
        /*
            r6 = this;
            r0 = 1
            r6.require(r0)
            r0 = 0
            r1 = 0
        L_0x0007:
            int r2 = r1 + 1
            long r3 = (long) r2
            boolean r3 = r6.request(r3)
            if (r3 == 0) goto L_0x0040
            dc.squareup.okio.Buffer r3 = r6.buffer
            long r4 = (long) r1
            byte r3 = r3.getByte(r4)
            r4 = 48
            if (r3 < r4) goto L_0x001f
            r4 = 57
            if (r3 <= r4) goto L_0x0026
        L_0x001f:
            if (r1 != 0) goto L_0x0028
            r4 = 45
            if (r3 == r4) goto L_0x0026
            goto L_0x0028
        L_0x0026:
            r1 = r2
            goto L_0x0007
        L_0x0028:
            if (r1 == 0) goto L_0x002b
            goto L_0x0040
        L_0x002b:
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.Byte r3 = java.lang.Byte.valueOf(r3)
            r2[r0] = r3
            java.lang.String r0 = "Expected leading [0-9] or '-' character but was %#x"
            java.lang.String r0 = java.lang.String.format(r0, r2)
            r1.<init>(r0)
            throw r1
        L_0x0040:
            dc.squareup.okio.Buffer r0 = r6.buffer
            long r0 = r0.readDecimalLong()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dc.squareup.okio.RealBufferedSource.readDecimalLong():long");
    }

    public void readFully(byte[] bArr) throws IOException {
        try {
            require((long) bArr.length);
            this.buffer.readFully(bArr);
        } catch (EOFException e) {
            int i = 0;
            while (true) {
                Buffer buffer2 = this.buffer;
                long j = buffer2.size;
                if (j > 0) {
                    int read = buffer2.read(bArr, i, (int) j);
                    if (read != -1) {
                        i += read;
                    } else {
                        throw new AssertionError();
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    public long readHexadecimalUnsignedLong() throws IOException {
        require(1);
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (!request((long) i2)) {
                break;
            }
            byte b = this.buffer.getByte((long) i);
            if ((b >= 48 && b <= 57) || ((b >= 97 && b <= 102) || (b >= 65 && b <= 70))) {
                i = i2;
            } else if (i == 0) {
                throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", new Object[]{Byte.valueOf(b)}));
            }
        }
        return this.buffer.readHexadecimalUnsignedLong();
    }

    public int readInt() throws IOException {
        require(4);
        return this.buffer.readInt();
    }

    public int readIntLe() throws IOException {
        require(4);
        return this.buffer.readIntLe();
    }

    public long readLong() throws IOException {
        require(8);
        return this.buffer.readLong();
    }

    public long readLongLe() throws IOException {
        require(8);
        return this.buffer.readLongLe();
    }

    public short readShort() throws IOException {
        require(2);
        return this.buffer.readShort();
    }

    public short readShortLe() throws IOException {
        require(2);
        return this.buffer.readShortLe();
    }

    public String readString(Charset charset) throws IOException {
        if (charset != null) {
            this.buffer.writeAll(this.source);
            return this.buffer.readString(charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    public String readUtf8() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readUtf8();
    }

    public int readUtf8CodePoint() throws IOException {
        require(1);
        byte b = this.buffer.getByte(0);
        if ((b & 224) == 192) {
            require(2);
        } else if ((b & 240) == 224) {
            require(3);
        } else if ((b & 248) == 240) {
            require(4);
        }
        return this.buffer.readUtf8CodePoint();
    }

    public String readUtf8Line() throws IOException {
        long indexOf = indexOf((byte) 10);
        if (indexOf != -1) {
            return this.buffer.readUtf8Line(indexOf);
        }
        long j = this.buffer.size;
        if (j != 0) {
            return readUtf8(j);
        }
        return null;
    }

    public String readUtf8LineStrict() throws IOException {
        return readUtf8LineStrict(Long.MAX_VALUE);
    }

    public boolean request(long j) throws IOException {
        Buffer buffer2;
        if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        } else if (!this.closed) {
            do {
                buffer2 = this.buffer;
                if (buffer2.size >= j) {
                    return true;
                }
            } while (this.source.read(buffer2, PlaybackStateCompat.ACTION_PLAY_FROM_URI) != -1);
            return false;
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public void require(long j) throws IOException {
        if (!request(j)) {
            throw new EOFException();
        }
    }

    public int select(Options options) throws IOException {
        if (!this.closed) {
            do {
                int selectPrefix = this.buffer.selectPrefix(options, true);
                if (selectPrefix == -1) {
                    return -1;
                }
                if (selectPrefix != -2) {
                    this.buffer.skip((long) options.byteStrings[selectPrefix].size());
                    return selectPrefix;
                }
            } while (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_URI) != -1);
            return -1;
        }
        throw new IllegalStateException("closed");
    }

    public void skip(long j) throws IOException {
        if (!this.closed) {
            while (j > 0) {
                Buffer buffer2 = this.buffer;
                if (buffer2.size == 0 && this.source.read(buffer2, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1) {
                    throw new EOFException();
                }
                long min = Math.min(j, this.buffer.size());
                this.buffer.skip(min);
                j -= min;
            }
            return;
        }
        throw new IllegalStateException("closed");
    }

    public Timeout timeout() {
        return this.source.timeout();
    }

    public String toString() {
        return "buffer(" + this.source + Operators.BRACKET_END_STR;
    }

    public long indexOf(byte b, long j) throws IOException {
        return indexOf(b, j, Long.MAX_VALUE);
    }

    public long indexOfElement(ByteString byteString, long j) throws IOException {
        if (!this.closed) {
            while (true) {
                long indexOfElement = this.buffer.indexOfElement(byteString, j);
                if (indexOfElement != -1) {
                    return indexOfElement;
                }
                Buffer buffer2 = this.buffer;
                long j2 = buffer2.size;
                if (this.source.read(buffer2, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1) {
                    return -1;
                }
                j = Math.max(j, j2);
            }
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public boolean rangeEquals(long j, ByteString byteString, int i, int i2) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        } else if (j < 0 || i < 0 || i2 < 0 || byteString.size() - i < i2) {
            return false;
        } else {
            for (int i3 = 0; i3 < i2; i3++) {
                long j2 = ((long) i3) + j;
                if (!request(1 + j2) || this.buffer.getByte(j2) != byteString.getByte(i + i3)) {
                    return false;
                }
            }
            return true;
        }
    }

    public String readUtf8LineStrict(long j) throws IOException {
        if (j >= 0) {
            long j2 = j == Long.MAX_VALUE ? Long.MAX_VALUE : j + 1;
            long indexOf = indexOf((byte) 10, 0, j2);
            if (indexOf != -1) {
                return this.buffer.readUtf8Line(indexOf);
            }
            if (j2 < Long.MAX_VALUE && request(j2) && this.buffer.getByte(j2 - 1) == 13 && request(1 + j2) && this.buffer.getByte(j2) == 10) {
                return this.buffer.readUtf8Line(j2);
            }
            Buffer buffer2 = new Buffer();
            Buffer buffer3 = this.buffer;
            buffer3.copyTo(buffer2, 0, Math.min(32, buffer3.size()));
            throw new EOFException("\\n not found: limit=" + Math.min(this.buffer.size(), j) + " content=" + buffer2.readByteString().hex() + 8230);
        }
        throw new IllegalArgumentException("limit < 0: " + j);
    }

    public long indexOf(byte b, long j, long j2) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        } else if (j < 0 || j2 < j) {
            throw new IllegalArgumentException(String.format("fromIndex=%s toIndex=%s", new Object[]{Long.valueOf(j), Long.valueOf(j2)}));
        } else {
            while (j < j2) {
                long indexOf = this.buffer.indexOf(b, j, j2);
                if (indexOf == -1) {
                    Buffer buffer2 = this.buffer;
                    long j3 = buffer2.size;
                    if (j3 >= j2 || this.source.read(buffer2, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1) {
                        break;
                    }
                    j = Math.max(j, j3);
                } else {
                    return indexOf;
                }
            }
            return -1;
        }
    }

    public byte[] readByteArray(long j) throws IOException {
        require(j);
        return this.buffer.readByteArray(j);
    }

    public ByteString readByteString(long j) throws IOException {
        require(j);
        return this.buffer.readByteString(j);
    }

    public String readUtf8(long j) throws IOException {
        require(j);
        return this.buffer.readUtf8(j);
    }

    public String readString(long j, Charset charset) throws IOException {
        require(j);
        if (charset != null) {
            return this.buffer.readString(j, charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        long j = (long) i2;
        Util.checkOffsetAndCount((long) bArr.length, (long) i, j);
        Buffer buffer2 = this.buffer;
        if (buffer2.size == 0 && this.source.read(buffer2, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1) {
            return -1;
        }
        return this.buffer.read(bArr, i, (int) Math.min(j, this.buffer.size));
    }

    public void readFully(Buffer buffer2, long j) throws IOException {
        try {
            require(j);
            this.buffer.readFully(buffer2, j);
        } catch (EOFException e) {
            buffer2.writeAll(this.buffer);
            throw e;
        }
    }

    public long indexOf(ByteString byteString) throws IOException {
        return indexOf(byteString, 0);
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        Buffer buffer2 = this.buffer;
        if (buffer2.size == 0 && this.source.read(buffer2, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1) {
            return -1;
        }
        return this.buffer.read(byteBuffer);
    }

    public long indexOf(ByteString byteString, long j) throws IOException {
        if (!this.closed) {
            while (true) {
                long indexOf = this.buffer.indexOf(byteString, j);
                if (indexOf != -1) {
                    return indexOf;
                }
                Buffer buffer2 = this.buffer;
                long j2 = buffer2.size;
                if (this.source.read(buffer2, PlaybackStateCompat.ACTION_PLAY_FROM_URI) == -1) {
                    return -1;
                }
                j = Math.max(j, (j2 - ((long) byteString.size())) + 1);
            }
        } else {
            throw new IllegalStateException("closed");
        }
    }
}
