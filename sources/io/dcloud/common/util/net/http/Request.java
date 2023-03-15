package io.dcloud.common.util.net.http;

import java.io.IOException;
import java.io.InputStream;

public class Request {
    private InputStream input;
    private String mData;
    private String uri;

    public Request(InputStream inputStream) {
        this.input = inputStream;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0009, code lost:
        r2 = r1 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String parseUri(java.lang.String r4) {
        /*
            r3 = this;
            r0 = 32
            int r1 = r4.indexOf(r0)
            r2 = -1
            if (r1 == r2) goto L_0x0016
            int r2 = r1 + 1
            int r0 = r4.indexOf(r0, r2)
            if (r0 <= r1) goto L_0x0016
            java.lang.String r4 = r4.substring(r2, r0)
            return r4
        L_0x0016:
            r4 = 0
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.net.http.Request.parseUri(java.lang.String):java.lang.String");
    }

    public String getData() {
        return this.mData;
    }

    public String getUri() {
        return this.uri;
    }

    public void parse() {
        int i;
        StringBuffer stringBuffer = new StringBuffer(2048);
        byte[] bArr = new byte[2048];
        try {
            i = this.input.read(bArr);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append((char) bArr[i2]);
        }
        String stringBuffer2 = stringBuffer.toString();
        this.mData = stringBuffer2;
        String parseUri = parseUri(stringBuffer2);
        this.uri = parseUri;
        this.uri = (parseUri == null || !parseUri.startsWith("/")) ? this.uri : this.uri.substring(1);
    }
}
