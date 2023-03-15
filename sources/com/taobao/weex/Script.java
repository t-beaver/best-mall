package com.taobao.weex;

public class Script {
    private byte[] mBinary;
    private String mContent;

    public Script(String str) {
        this.mContent = str;
    }

    public Script(byte[] bArr) {
        this.mBinary = bArr;
    }

    public String getContent() {
        return this.mContent;
    }

    public byte[] getBinary() {
        return this.mBinary;
    }

    public int length() {
        String str = this.mContent;
        if (str != null) {
            return str.length();
        }
        byte[] bArr = this.mBinary;
        if (bArr != null) {
            return bArr.length;
        }
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r0 = r1.mBinary;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isEmpty() {
        /*
            r1 = this;
            java.lang.String r0 = r1.mContent
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0011
            byte[] r0 = r1.mBinary
            if (r0 == 0) goto L_0x000f
            int r0 = r0.length
            if (r0 != 0) goto L_0x0011
        L_0x000f:
            r0 = 1
            goto L_0x0012
        L_0x0011:
            r0 = 0
        L_0x0012:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.Script.isEmpty():boolean");
    }
}
