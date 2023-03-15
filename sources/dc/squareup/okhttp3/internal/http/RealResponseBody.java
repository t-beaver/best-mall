package dc.squareup.okhttp3.internal.http;

import dc.squareup.okhttp3.MediaType;
import dc.squareup.okhttp3.ResponseBody;
import dc.squareup.okio.BufferedSource;

public final class RealResponseBody extends ResponseBody {
    private final long contentLength;
    private final String contentTypeString;
    private final BufferedSource source;

    public RealResponseBody(String str, long j, BufferedSource bufferedSource) {
        this.contentTypeString = str;
        this.contentLength = j;
        this.source = bufferedSource;
    }

    public long contentLength() {
        return this.contentLength;
    }

    public MediaType contentType() {
        String str = this.contentTypeString;
        if (str != null) {
            return MediaType.parse(str);
        }
        return null;
    }

    public BufferedSource source() {
        return this.source;
    }
}
