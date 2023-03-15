package com.taobao.weex.appfram.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.util.HashMap;

public class WXClipboardModule extends WXModule implements IWXClipboard {
    private static final String DATA = "data";
    private static final String RESULT = "result";
    private static final String RESULT_FAILED = "failed";
    private static final String RESULT_OK = "success";
    private final String CLIP_KEY = "WEEX_CLIP_KEY_MAIN";

    @JSMethod
    public void setString(String str) {
        if (str != null) {
            ((ClipboardManager) this.mWXSDKInstance.getContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("WEEX_CLIP_KEY_MAIN", str));
        }
    }

    @JSMethod
    public void getString(JSCallback jSCallback) {
        Context context = this.mWXSDKInstance.getContext();
        HashMap hashMap = new HashMap(2);
        ClipData primaryClip = ((ClipboardManager) context.getSystemService("clipboard")).getPrimaryClip();
        CharSequence charSequence = "";
        String str = "failed";
        if (primaryClip == null || primaryClip.getItemCount() <= 0) {
            hashMap.put("result", str);
            hashMap.put("data", charSequence);
        } else {
            CharSequence coerceToText = coerceToText(context, primaryClip.getItemAt(0));
            if (coerceToText != null) {
                str = "success";
            }
            hashMap.put("result", str);
            if (coerceToText != null) {
                charSequence = coerceToText;
            }
            hashMap.put("data", charSequence);
        }
        if (jSCallback != null) {
            jSCallback.invoke(hashMap);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0068, code lost:
        if (r7 == null) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0085, code lost:
        if (r7 == null) goto L_0x0088;
     */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0063 A[SYNTHETIC, Splitter:B:38:0x0063] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0072 A[SYNTHETIC, Splitter:B:47:0x0072] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0079 A[SYNTHETIC, Splitter:B:51:0x0079] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0080 A[SYNTHETIC, Splitter:B:58:0x0080] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.CharSequence coerceToText(android.content.Context r7, android.content.ClipData.Item r8) {
        /*
            r6 = this;
            java.lang.CharSequence r0 = r8.getText()
            if (r0 == 0) goto L_0x0007
            return r0
        L_0x0007:
            android.net.Uri r0 = r8.getUri()
            r1 = 0
            if (r0 == 0) goto L_0x008d
            android.content.ContentResolver r7 = r7.getContentResolver()     // Catch:{ FileNotFoundException -> 0x007d, IOException -> 0x0058, all -> 0x0055 }
            java.lang.String r8 = "text/*"
            android.content.res.AssetFileDescriptor r7 = r7.openTypedAssetFileDescriptor(r0, r8, r1)     // Catch:{ FileNotFoundException -> 0x007d, IOException -> 0x0058, all -> 0x0055 }
            java.io.FileInputStream r7 = r7.createInputStream()     // Catch:{ FileNotFoundException -> 0x007d, IOException -> 0x0058, all -> 0x0055 }
            java.io.InputStreamReader r8 = new java.io.InputStreamReader     // Catch:{ FileNotFoundException -> 0x0053, IOException -> 0x004e, all -> 0x004c }
            java.lang.String r2 = "UTF-8"
            r8.<init>(r7, r2)     // Catch:{ FileNotFoundException -> 0x0053, IOException -> 0x004e, all -> 0x004c }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x004a, IOException -> 0x0048 }
            r2 = 128(0x80, float:1.794E-43)
            r1.<init>(r2)     // Catch:{ FileNotFoundException -> 0x004a, IOException -> 0x0048 }
            r2 = 8192(0x2000, float:1.14794E-41)
            char[] r2 = new char[r2]     // Catch:{ FileNotFoundException -> 0x004a, IOException -> 0x0048 }
        L_0x002e:
            int r3 = r8.read(r2)     // Catch:{ FileNotFoundException -> 0x004a, IOException -> 0x0048 }
            if (r3 <= 0) goto L_0x0039
            r4 = 0
            r1.append(r2, r4, r3)     // Catch:{ FileNotFoundException -> 0x004a, IOException -> 0x0048 }
            goto L_0x002e
        L_0x0039:
            java.lang.String r0 = r1.toString()     // Catch:{ FileNotFoundException -> 0x004a, IOException -> 0x0048 }
            r8.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x0042
        L_0x0041:
        L_0x0042:
            if (r7 == 0) goto L_0x0047
            r7.close()     // Catch:{ IOException -> 0x0047 }
        L_0x0047:
            return r0
        L_0x0048:
            r1 = move-exception
            goto L_0x005c
        L_0x004a:
            r1 = r8
            goto L_0x007e
        L_0x004c:
            r0 = move-exception
            goto L_0x0070
        L_0x004e:
            r8 = move-exception
            r5 = r1
            r1 = r8
            r8 = r5
            goto L_0x005c
        L_0x0053:
            goto L_0x007e
        L_0x0055:
            r0 = move-exception
            r7 = r1
            goto L_0x0070
        L_0x0058:
            r7 = move-exception
            r8 = r1
            r1 = r7
            r7 = r8
        L_0x005c:
            java.lang.String r2 = "ClippedData Failure loading text."
            com.taobao.weex.utils.WXLogUtils.w((java.lang.String) r2, (java.lang.Throwable) r1)     // Catch:{ all -> 0x006e }
            if (r8 == 0) goto L_0x0068
            r8.close()     // Catch:{ IOException -> 0x0067 }
            goto L_0x0068
        L_0x0067:
        L_0x0068:
            if (r7 == 0) goto L_0x0088
        L_0x006a:
            r7.close()     // Catch:{ IOException -> 0x0088 }
            goto L_0x0088
        L_0x006e:
            r0 = move-exception
            r1 = r8
        L_0x0070:
            if (r1 == 0) goto L_0x0077
            r1.close()     // Catch:{ IOException -> 0x0076 }
            goto L_0x0077
        L_0x0076:
        L_0x0077:
            if (r7 == 0) goto L_0x007c
            r7.close()     // Catch:{ IOException -> 0x007c }
        L_0x007c:
            throw r0
        L_0x007d:
            r7 = r1
        L_0x007e:
            if (r1 == 0) goto L_0x0085
            r1.close()     // Catch:{ IOException -> 0x0084 }
            goto L_0x0085
        L_0x0084:
        L_0x0085:
            if (r7 == 0) goto L_0x0088
            goto L_0x006a
        L_0x0088:
            java.lang.String r7 = r0.toString()
            return r7
        L_0x008d:
            android.content.Intent r7 = r8.getIntent()
            if (r7 == 0) goto L_0x0099
            r8 = 1
            java.lang.String r7 = r7.toUri(r8)
            return r7
        L_0x0099:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.appfram.clipboard.WXClipboardModule.coerceToText(android.content.Context, android.content.ClipData$Item):java.lang.CharSequence");
    }
}
