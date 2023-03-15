package io.dcloud.js.file;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.taobao.weex.common.Constants;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.AsyncTaskHandler;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.Base64;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.Md5Utils;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import java.io.File;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileFeatureImpl implements IFeature {
    private static String a;
    private static String b;
    private static String c;
    private static String d;
    private static String e;

    class a implements AsyncTaskHandler.IAsyncTaskListener {
        final /* synthetic */ String a;
        final /* synthetic */ int b;
        final /* synthetic */ int c;
        final /* synthetic */ String d;
        final /* synthetic */ IWebview e;
        final /* synthetic */ String f;

        a(String str, int i, int i2, String str2, IWebview iWebview, String str3) {
            this.a = str;
            this.b = i;
            this.c = i2;
            this.d = str2;
            this.e = iWebview;
            this.f = str3;
        }

        public void onCancel() {
        }

        public void onExecuteBegin() {
        }

        public void onExecuteEnd(Object obj) {
            if (obj != null) {
                Deprecated_JSUtil.execCallback(this.e, this.f, String.valueOf(obj), JSUtil.OK, true, false);
            } else {
                FileFeatureImpl.this.a(10, this.e, this.f);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:34:0x007e, code lost:
            r2 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0080, code lost:
            r2 = e;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x007e A[ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x000b] */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x0090 A[SYNTHETIC, Splitter:B:44:0x0090] */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x0098 A[Catch:{ IOException -> 0x0094 }] */
        /* JADX WARNING: Removed duplicated region for block: B:56:0x00a9 A[SYNTHETIC, Splitter:B:56:0x00a9] */
        /* JADX WARNING: Removed duplicated region for block: B:61:0x00b1 A[Catch:{ IOException -> 0x00ad }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object onExecuting() {
            /*
                r10 = this;
                r0 = 0
                java.lang.String r1 = r10.a     // Catch:{ Exception -> 0x0085, all -> 0x0082 }
                java.lang.Object r1 = io.dcloud.common.adapter.io.DHFile.createFileHandler(r1)     // Catch:{ Exception -> 0x0085, all -> 0x0082 }
                java.io.InputStream r1 = io.dcloud.common.adapter.io.DHFile.getInputStream(r1)     // Catch:{ Exception -> 0x0085, all -> 0x0082 }
                int r2 = r1.available()     // Catch:{ Exception -> 0x0080, all -> 0x007e }
                io.dcloud.common.adapter.io.UnicodeInputStream r3 = new io.dcloud.common.adapter.io.UnicodeInputStream     // Catch:{ Exception -> 0x001e, all -> 0x007e }
                java.nio.charset.Charset r4 = java.nio.charset.Charset.defaultCharset()     // Catch:{ Exception -> 0x001e, all -> 0x007e }
                java.lang.String r4 = r4.name()     // Catch:{ Exception -> 0x001e, all -> 0x007e }
                r3.<init>(r1, r4)     // Catch:{ Exception -> 0x001e, all -> 0x007e }
                r1 = r3
                goto L_0x0022
            L_0x001e:
                r3 = move-exception
                r3.printStackTrace()     // Catch:{ Exception -> 0x0080, all -> 0x007e }
            L_0x0022:
                java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0080, all -> 0x007e }
                r3.<init>()     // Catch:{ Exception -> 0x0080, all -> 0x007e }
                int r4 = r10.b     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                r5 = 0
                if (r4 <= 0) goto L_0x0042
                if (r4 < r2) goto L_0x0030
                int r4 = r2 + -1
            L_0x0030:
                int r2 = r10.c     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                int r4 = r4 - r2
                int r4 = r4 + 1
                byte[] r6 = new byte[r4]     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                long r7 = (long) r2     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                r1.skip(r7)     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                r1.read(r6, r5, r4)     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                r3.write(r6, r5, r4)     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                goto L_0x004e
            L_0x0042:
                r2 = 204800(0x32000, float:2.86986E-40)
                byte[] r2 = new byte[r2]     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                int r4 = r1.read(r2)     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                r6 = -1
                if (r4 != r6) goto L_0x0071
            L_0x004e:
                java.lang.String r2 = r10.d     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r2)     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                if (r2 == 0) goto L_0x005b
                java.lang.String r0 = r3.toString()     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                goto L_0x0061
            L_0x005b:
                java.lang.String r2 = r10.d     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                java.lang.String r0 = r3.toString(r2)     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
            L_0x0061:
                java.lang.String r0 = io.dcloud.common.util.JSONUtil.toJSONableString(r0)     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                r1.close()     // Catch:{ IOException -> 0x006c }
                r3.close()     // Catch:{ IOException -> 0x006c }
                goto L_0x00a0
            L_0x006c:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x00a0
            L_0x0071:
                r3.write(r2, r5, r4)     // Catch:{ Exception -> 0x0077, all -> 0x0075 }
                goto L_0x0042
            L_0x0075:
                r0 = move-exception
                goto L_0x00a5
            L_0x0077:
                r2 = move-exception
                r9 = r1
                r1 = r0
                r0 = r3
                r3 = r2
                r2 = r9
                goto L_0x008b
            L_0x007e:
                r2 = move-exception
                goto L_0x00a7
            L_0x0080:
                r2 = move-exception
                goto L_0x0088
            L_0x0082:
                r2 = move-exception
                r1 = r0
                goto L_0x00a7
            L_0x0085:
                r1 = move-exception
                r2 = r1
                r1 = r0
            L_0x0088:
                r3 = r2
                r2 = r1
                r1 = r0
            L_0x008b:
                r3.printStackTrace()     // Catch:{ all -> 0x00a1 }
                if (r2 == 0) goto L_0x0096
                r2.close()     // Catch:{ IOException -> 0x0094 }
                goto L_0x0096
            L_0x0094:
                r0 = move-exception
                goto L_0x009c
            L_0x0096:
                if (r0 == 0) goto L_0x009f
                r0.close()     // Catch:{ IOException -> 0x0094 }
                goto L_0x009f
            L_0x009c:
                r0.printStackTrace()
            L_0x009f:
                r0 = r1
            L_0x00a0:
                return r0
            L_0x00a1:
                r1 = move-exception
                r3 = r0
                r0 = r1
                r1 = r2
            L_0x00a5:
                r2 = r0
                r0 = r3
            L_0x00a7:
                if (r1 == 0) goto L_0x00af
                r1.close()     // Catch:{ IOException -> 0x00ad }
                goto L_0x00af
            L_0x00ad:
                r0 = move-exception
                goto L_0x00b5
            L_0x00af:
                if (r0 == 0) goto L_0x00b8
                r0.close()     // Catch:{ IOException -> 0x00ad }
                goto L_0x00b8
            L_0x00b5:
                r0.printStackTrace()
            L_0x00b8:
                goto L_0x00ba
            L_0x00b9:
                throw r2
            L_0x00ba:
                goto L_0x00b9
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.file.FileFeatureImpl.a.onExecuting():java.lang.Object");
        }
    }

    class b extends CustomTarget<File> {
        final /* synthetic */ String a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;

        b(String str, IWebview iWebview, String str2) {
            this.a = str;
            this.b = iWebview;
            this.c = str2;
        }

        /* renamed from: a */
        public void onResourceReady(File file, Transition<? super File> transition) {
            if (PdrUtil.isEmpty(this.a)) {
                FileFeatureImpl.this.a(file.getAbsolutePath(), this.b, this.c);
            } else if (DHFile.copyFile(file.getPath(), this.a) != 1) {
                Deprecated_JSUtil.execCallback(this.b, this.c, DOMException.toJSON(13, "Failed to load resource"), JSUtil.ERROR, true, false);
            } else {
                FileFeatureImpl.this.a(this.a, this.b, this.c);
            }
        }

        public void onLoadCleared(Drawable drawable) {
        }

        public void onLoadFailed(Drawable drawable) {
            Deprecated_JSUtil.execCallback(this.b, this.c, DOMException.toJSON(13, "Failed to load resource"), JSUtil.ERROR, true, false);
        }
    }

    class c implements Runnable {
        final /* synthetic */ File a;
        final /* synthetic */ String b;
        final /* synthetic */ IWebview c;
        final /* synthetic */ String d;

        c(File file, String str, IWebview iWebview, String str2) {
            this.a = file;
            this.b = str;
            this.c = iWebview;
            this.d = str2;
        }

        public void run() {
            String md5 = Md5Utils.md5(this.a, this.b);
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(AbsoluteConst.JSON_KEY_SIZE, this.a.length());
                if (md5 != null) {
                    jSONObject.put(Constants.CodeCache.BANNER_DIGEST, md5.toUpperCase(Locale.US));
                }
            } catch (JSONException unused) {
            }
            Deprecated_JSUtil.execCallback(this.c, this.d, jSONObject.toString(), JSUtil.OK, true, false);
        }
    }

    class d implements ISysEventListener {
        final /* synthetic */ int a;
        final /* synthetic */ IApp b;
        final /* synthetic */ IWebview c;
        final /* synthetic */ String d;

        d(int i, IApp iApp, IWebview iWebview, String str) {
            this.a = i;
            this.b = iApp;
            this.c = iWebview;
            this.d = str;
        }

        public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
            Object[] objArr = (Object[]) obj;
            int intValue = ((Integer) objArr[0]).intValue();
            Intent intent = (Intent) objArr[2];
            ISysEventListener.SysEventType sysEventType2 = ISysEventListener.SysEventType.onActivityResult;
            if (sysEventType == sysEventType2 && intValue == this.a) {
                this.b.unregisterSysEventListener(this, sysEventType2);
                if (intent == null || (intent.getData() == null && intent.getClipData() == null)) {
                    Deprecated_JSUtil.execCallback(this.c, this.d, StringUtil.format(DOMException.JSON_ERROR_INFO, -2, DOMException.MSG_USER_CANCEL), JSUtil.ERROR, true, false);
                } else {
                    JSONArray jSONArray = new JSONArray();
                    ClipData clipData = intent.getClipData();
                    if (clipData != null) {
                        int itemCount = clipData.getItemCount();
                        for (int i = 0; i < itemCount; i++) {
                            jSONArray.put(FileUtil.getPathFromUri(this.b.getActivity(), clipData.getItemAt(i).getUri()));
                        }
                    } else {
                        Uri data = intent.getData();
                        String pathFromUri = FileUtil.getPathFromUri(this.b.getActivity(), data);
                        if (PdrUtil.isEmpty(pathFromUri)) {
                            pathFromUri = data.toString();
                        }
                        jSONArray.put(pathFromUri);
                    }
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("files", jSONArray);
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                    Deprecated_JSUtil.execCallback(this.c, this.d, jSONObject.toString(), JSUtil.OK, true, false);
                }
            }
            return false;
        }
    }

    class e implements AsyncTaskHandler.IAsyncTaskListener {
        final /* synthetic */ String[] a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;
        final /* synthetic */ int d;
        final /* synthetic */ String e;

        e(String[] strArr, IWebview iWebview, String str, int i, String str2) {
            this.a = strArr;
            this.b = iWebview;
            this.c = str;
            this.d = i;
            this.e = str2;
        }

        public void onCancel() {
        }

        public void onExecuteBegin() {
        }

        public void onExecuteEnd(Object obj) {
        }

        public Object onExecuting() {
            byte[] decode2bytes = Base64.decode2bytes(this.a[1]);
            if (decode2bytes == null) {
                Deprecated_JSUtil.execCallback(this.b, this.c, StringUtil.format(DOMException.JSON_ERROR_INFO, 16, this.b.getContext().getString(R.string.dcloud_io_write_non_base64)).toString(), JSUtil.ERROR, true, false);
                return null;
            }
            DHFile.writeFile(decode2bytes, this.d, this.e);
            JSUtil.execCallback(this.b, this.c, (double) decode2bytes.length, JSUtil.OK, false);
            return null;
        }
    }

    class f implements AsyncTaskHandler.IAsyncTaskListener {
        final /* synthetic */ String a;
        final /* synthetic */ int b;
        final /* synthetic */ int c;
        final /* synthetic */ IWebview d;
        final /* synthetic */ String e;

        f(String str, int i, int i2, IWebview iWebview, String str2) {
            this.a = str;
            this.b = i;
            this.c = i2;
            this.d = iWebview;
            this.e = str2;
        }

        public void onCancel() {
        }

        public void onExecuteBegin() {
        }

        public void onExecuteEnd(Object obj) {
            if (obj != null) {
                Deprecated_JSUtil.execCallback(this.d, this.e, String.valueOf(obj), JSUtil.OK, true, false);
            } else {
                FileFeatureImpl.this.a(10, this.d, this.e);
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: java.io.InputStream} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v9, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v12, resolved type: java.lang.String} */
        /* JADX WARNING: type inference failed for: r3v0, types: [java.io.InputStream] */
        /* JADX WARNING: type inference failed for: r3v1 */
        /* JADX WARNING: type inference failed for: r3v3, types: [java.io.InputStream] */
        /* JADX WARNING: type inference failed for: r3v10 */
        /* JADX WARNING: type inference failed for: r3v13 */
        /* JADX WARNING: type inference failed for: r3v14 */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x0091 A[SYNTHETIC, Splitter:B:38:0x0091] */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x0099 A[Catch:{ IOException -> 0x0095 }] */
        /* JADX WARNING: Removed duplicated region for block: B:50:0x00a6 A[SYNTHETIC, Splitter:B:50:0x00a6] */
        /* JADX WARNING: Removed duplicated region for block: B:55:0x00ae A[Catch:{ IOException -> 0x00aa }] */
        /* JADX WARNING: Unknown variable types count: 2 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object onExecuting() {
            /*
                r14 = this;
                r0 = 1
                r1 = 0
                r2 = 0
                java.lang.String r3 = r14.a     // Catch:{ Exception -> 0x0068, all -> 0x0065 }
                java.lang.Object r3 = io.dcloud.common.adapter.io.DHFile.createFileHandler(r3)     // Catch:{ Exception -> 0x0068, all -> 0x0065 }
                java.io.InputStream r3 = io.dcloud.common.adapter.io.DHFile.getInputStream(r3)     // Catch:{ Exception -> 0x0068, all -> 0x0065 }
                int r4 = r3.available()     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
                int r5 = r14.b     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
                if (r5 <= 0) goto L_0x002d
                if (r5 < r4) goto L_0x0019
                int r5 = r4 + -1
            L_0x0019:
                int r4 = r14.c     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
                int r5 = r5 - r4
                int r5 = r5 + r0
                byte[] r6 = new byte[r5]     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
                long r7 = (long) r4     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
                r3.skip(r7)     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
                r3.read(r6, r1, r5)     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
                java.lang.String r4 = io.dcloud.common.util.Base64.encode((byte[]) r6)     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
                r5 = r4
                r4 = r2
                goto L_0x0046
            L_0x002d:
                java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
                r4.<init>()     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            L_0x0032:
                r5 = 204800(0x32000, float:2.86986E-40)
                byte[] r5 = new byte[r5]     // Catch:{ Exception -> 0x005e, all -> 0x005c }
                int r6 = r3.read(r5)     // Catch:{ Exception -> 0x005e, all -> 0x005c }
                r7 = -1
                if (r6 != r7) goto L_0x0058
                byte[] r5 = r4.toByteArray()     // Catch:{ Exception -> 0x005e, all -> 0x005c }
                java.lang.String r5 = io.dcloud.common.util.Base64.encode((byte[]) r5)     // Catch:{ Exception -> 0x005e, all -> 0x005c }
            L_0x0046:
                java.lang.String r2 = io.dcloud.common.util.JSONUtil.toJSONableString(r5)     // Catch:{ Exception -> 0x005e, all -> 0x005c }
                r3.close()     // Catch:{ IOException -> 0x0053 }
                if (r4 == 0) goto L_0x00a0
                r4.close()     // Catch:{ IOException -> 0x0053 }
                goto L_0x00a0
            L_0x0053:
                r0 = move-exception
                r0.printStackTrace()
                goto L_0x00a0
            L_0x0058:
                r4.write(r5, r1, r6)     // Catch:{ Exception -> 0x005e, all -> 0x005c }
                goto L_0x0032
            L_0x005c:
                r0 = move-exception
                goto L_0x00a4
            L_0x005e:
                r5 = move-exception
                goto L_0x006c
            L_0x0060:
                r0 = move-exception
                r4 = r2
                goto L_0x00a2
            L_0x0063:
                r4 = move-exception
                goto L_0x006a
            L_0x0065:
                r0 = move-exception
                r4 = r2
                goto L_0x00a3
            L_0x0068:
                r4 = move-exception
                r3 = r2
            L_0x006a:
                r5 = r4
                r4 = r2
            L_0x006c:
                java.lang.String r6 = "{code:%d,message:'%s'}"
                r7 = 2
                java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x00a1 }
                r8 = 13
                java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x00a1 }
                r7[r1] = r8     // Catch:{ all -> 0x00a1 }
                java.lang.String r1 = r5.getMessage()     // Catch:{ all -> 0x00a1 }
                r7[r0] = r1     // Catch:{ all -> 0x00a1 }
                java.lang.String r10 = io.dcloud.common.util.StringUtil.format(r6, r7)     // Catch:{ all -> 0x00a1 }
                io.dcloud.common.DHInterface.IWebview r8 = r14.d     // Catch:{ all -> 0x00a1 }
                java.lang.String r9 = r14.e     // Catch:{ all -> 0x00a1 }
                int r11 = io.dcloud.common.util.JSUtil.ERROR     // Catch:{ all -> 0x00a1 }
                r12 = 1
                r13 = 0
                io.dcloud.common.util.Deprecated_JSUtil.execCallback(r8, r9, r10, r11, r12, r13)     // Catch:{ all -> 0x00a1 }
                if (r3 == 0) goto L_0x0097
                r3.close()     // Catch:{ IOException -> 0x0095 }
                goto L_0x0097
            L_0x0095:
                r0 = move-exception
                goto L_0x009d
            L_0x0097:
                if (r4 == 0) goto L_0x00a0
                r4.close()     // Catch:{ IOException -> 0x0095 }
                goto L_0x00a0
            L_0x009d:
                r0.printStackTrace()
            L_0x00a0:
                return r2
            L_0x00a1:
                r0 = move-exception
            L_0x00a2:
                r2 = r3
            L_0x00a3:
                r3 = r2
            L_0x00a4:
                if (r3 == 0) goto L_0x00ac
                r3.close()     // Catch:{ IOException -> 0x00aa }
                goto L_0x00ac
            L_0x00aa:
                r1 = move-exception
                goto L_0x00b2
            L_0x00ac:
                if (r4 == 0) goto L_0x00b5
                r4.close()     // Catch:{ IOException -> 0x00aa }
                goto L_0x00b5
            L_0x00b2:
                r1.printStackTrace()
            L_0x00b5:
                goto L_0x00b7
            L_0x00b6:
                throw r0
            L_0x00b7:
                goto L_0x00b6
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.file.FileFeatureImpl.f.onExecuting():java.lang.Object");
        }
    }

    private void b(IWebview iWebview, String[] strArr, String str) {
        try {
            String str2 = strArr[0];
            if (!FileUtil.checkPathAccord(iWebview.getContext(), str2)) {
                a(15, iWebview, str);
                return;
            }
            boolean checkPrivateDir = iWebview.obtainApp().checkPrivateDir(str2);
            int intValue = Integer.valueOf(strArr[2]).intValue();
            if (strArr[1] != null) {
                if (!checkPrivateDir) {
                    AsyncTaskHandler.executeThreadTask(new e(strArr, iWebview, str, intValue, str2));
                    return;
                }
            }
            a(4, iWebview, str);
        } catch (Exception unused) {
            a(10, iWebview, str);
        }
    }

    private String c(String str) {
        if (str.startsWith(a)) {
            return AbsoluteConst.MINI_SERVER_APP_WWW + str.substring(a.length(), str.length());
        } else if (str.startsWith(c)) {
            return AbsoluteConst.MINI_SERVER_APP_DOC + str.substring(c.length(), str.length());
        } else if (str.startsWith(d)) {
            return "_documents/" + str.substring(d.length(), str.length());
        } else if (!str.startsWith(e)) {
            return null;
        } else {
            return "_downloads/" + str.substring(e.length(), str.length());
        }
    }

    private String d(String str) {
        boolean z;
        String b2 = b(str);
        if (PdrUtil.isEmpty(b2)) {
            b2 = String.valueOf(-1);
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return b2;
        }
        if (str.startsWith(a)) {
            return String.valueOf(1);
        }
        if (str.startsWith(c)) {
            return String.valueOf(2);
        }
        if (str.startsWith(d)) {
            return String.valueOf(3);
        }
        return str.startsWith(e) ? String.valueOf(4) : b2;
    }

    private boolean e(String str) {
        return str.endsWith(BaseInfo.REL_PRIVATE_WWW_DIR) || str.endsWith(BaseInfo.REL_PUBLIC_DOCUMENTS_DIR) || str.endsWith(BaseInfo.REL_PUBLIC_DOWNLOADS_DIR) || str.endsWith(BaseInfo.REL_PRIVATE_DOC_DIR) || str.endsWith(AbsoluteConst.MINI_SERVER_APP_WWW) || str.endsWith("_documents/") || str.endsWith("_downloads/") || str.endsWith(AbsoluteConst.MINI_SERVER_APP_DOC);
    }

    public void dispose(String str) {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:589:0x0c4a, code lost:
        if (io.dcloud.common.adapter.io.DHFile.copyAssetsFile(r0, r2) != false) goto L_0x0c4c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x0260  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0267  */
    /* JADX WARNING: Removed duplicated region for block: B:184:0x03ae  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x03b5  */
    /* JADX WARNING: Removed duplicated region for block: B:221:0x043f A[Catch:{ JSONException -> 0x0471 }] */
    /* JADX WARNING: Removed duplicated region for block: B:222:0x046a A[Catch:{ JSONException -> 0x0471 }] */
    /* JADX WARNING: Removed duplicated region for block: B:323:0x0696  */
    /* JADX WARNING: Removed duplicated region for block: B:324:0x069d  */
    /* JADX WARNING: Removed duplicated region for block: B:396:0x07f4  */
    /* JADX WARNING: Removed duplicated region for block: B:397:0x07fb  */
    /* JADX WARNING: Removed duplicated region for block: B:595:0x0c55  */
    /* JADX WARNING: Removed duplicated region for block: B:596:0x0c6f  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x01fc  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0200  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r21, java.lang.String r22, java.lang.String[] r23) {
        /*
            r20 = this;
            r9 = r20
            r7 = r21
            r0 = r22
            r1 = r23
            java.lang.String r2 = "MD5"
            r8 = 0
            r10 = r1[r8]
            io.dcloud.common.DHInterface.IFrameView r3 = r21.obtainFrameView()
            io.dcloud.common.DHInterface.IApp r11 = r3.obtainApp()
            android.content.Context r3 = r21.getContext()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Io-"
            r4.append(r5)
            r4.append(r0)
            java.lang.String r4 = r4.toString()
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r3, r4)
            boolean r3 = r11.isOnAppRunningMode()
            java.lang.String r4 = r11.getPathByType(r8)
            a = r4
            r4 = -1
            java.lang.String r5 = r11.getPathByType(r4)
            b = r5
            r12 = 1
            java.lang.String r5 = r11.getPathByType(r12)
            c = r5
            r5 = 2
            java.lang.String r6 = r11.getPathByType(r5)
            d = r6
            r6 = 3
            java.lang.String r13 = r11.getPathByType(r6)
            e = r13
            r22.hashCode()
            int r13 = r22.hashCode()
            switch(r13) {
                case -1354714928: goto L_0x019c;
                case -1224446278: goto L_0x0191;
                case -1068263892: goto L_0x0186;
                case -1017341515: goto L_0x017b;
                case -972189769: goto L_0x0170;
                case -941263860: goto L_0x0165;
                case -934610812: goto L_0x015a;
                case -888091149: goto L_0x014f;
                case -91542719: goto L_0x0141;
                case -75538958: goto L_0x0133;
                case 113399775: goto L_0x0124;
                case 178260718: goto L_0x0116;
                case 324074165: goto L_0x0108;
                case 529285085: goto L_0x00fa;
                case 676070354: goto L_0x00eb;
                case 700591008: goto L_0x00dd;
                case 1068167127: goto L_0x00cf;
                case 1323018515: goto L_0x00c1;
                case 1342041536: goto L_0x00b3;
                case 1352737307: goto L_0x00a5;
                case 1572117509: goto L_0x0097;
                case 1627895973: goto L_0x0089;
                case 1852984678: goto L_0x007b;
                case 1963978266: goto L_0x006d;
                case 2023226419: goto L_0x005f;
                default: goto L_0x005d;
            }
        L_0x005d:
            goto L_0x01a7
        L_0x005f:
            java.lang.String r13 = "chooseFile"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0069
            goto L_0x01a7
        L_0x0069:
            r13 = 24
            goto L_0x01a8
        L_0x006d:
            java.lang.String r13 = "readEntries"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0077
            goto L_0x01a7
        L_0x0077:
            r13 = 23
            goto L_0x01a8
        L_0x007b:
            java.lang.String r13 = "truncate"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0085
            goto L_0x01a7
        L_0x0085:
            r13 = 22
            goto L_0x01a8
        L_0x0089:
            java.lang.String r13 = "getMetadata"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0093
            goto L_0x01a7
        L_0x0093:
            r13 = 21
            goto L_0x01a8
        L_0x0097:
            java.lang.String r13 = "resolveLocalFileSystemURL"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x00a1
            goto L_0x01a7
        L_0x00a1:
            r13 = 20
            goto L_0x01a8
        L_0x00a5:
            java.lang.String r13 = "removeRecursively"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x00af
            goto L_0x01a7
        L_0x00af:
            r13 = 19
            goto L_0x01a8
        L_0x00b3:
            java.lang.String r13 = "getFileInfo"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x00bd
            goto L_0x01a7
        L_0x00bd:
            r13 = 18
            goto L_0x01a8
        L_0x00c1:
            java.lang.String r13 = "getVideoInfo"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x00cb
            goto L_0x01a7
        L_0x00cb:
            r13 = 17
            goto L_0x01a8
        L_0x00cf:
            java.lang.String r13 = "readAsBase64"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x00d9
            goto L_0x01a7
        L_0x00d9:
            r13 = 16
            goto L_0x01a8
        L_0x00dd:
            java.lang.String r13 = "getParent"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x00e7
            goto L_0x01a7
        L_0x00e7:
            r13 = 15
            goto L_0x01a8
        L_0x00eb:
            java.lang.String r13 = "writeAsBinary"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x00f6
            goto L_0x01a7
        L_0x00f6:
            r13 = 14
            goto L_0x01a8
        L_0x00fa:
            java.lang.String r13 = "readAsDataURL"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0104
            goto L_0x01a7
        L_0x0104:
            r13 = 13
            goto L_0x01a8
        L_0x0108:
            java.lang.String r13 = "convertAbsoluteFileSystem"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0112
            goto L_0x01a7
        L_0x0112:
            r13 = 12
            goto L_0x01a8
        L_0x0116:
            java.lang.String r13 = "getAudioInfo"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0120
            goto L_0x01a7
        L_0x0120:
            r13 = 11
            goto L_0x01a8
        L_0x0124:
            java.lang.String r13 = "write"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x012f
            goto L_0x01a7
        L_0x012f:
            r13 = 10
            goto L_0x01a8
        L_0x0133:
            java.lang.String r13 = "getFile"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x013d
            goto L_0x01a7
        L_0x013d:
            r13 = 9
            goto L_0x01a8
        L_0x0141:
            java.lang.String r13 = "getFileMetadata"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x014b
            goto L_0x01a7
        L_0x014b:
            r13 = 8
            goto L_0x01a8
        L_0x014f:
            java.lang.String r13 = "getImageInfo"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0158
            goto L_0x01a7
        L_0x0158:
            r13 = 7
            goto L_0x01a8
        L_0x015a:
            java.lang.String r13 = "remove"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0163
            goto L_0x01a7
        L_0x0163:
            r13 = 6
            goto L_0x01a8
        L_0x0165:
            java.lang.String r13 = "convertLocalFileSystemURL"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x016e
            goto L_0x01a7
        L_0x016e:
            r13 = 5
            goto L_0x01a8
        L_0x0170:
            java.lang.String r13 = "getDirectory"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0179
            goto L_0x01a7
        L_0x0179:
            r13 = 4
            goto L_0x01a8
        L_0x017b:
            java.lang.String r13 = "readAsText"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x0184
            goto L_0x01a7
        L_0x0184:
            r13 = 3
            goto L_0x01a8
        L_0x0186:
            java.lang.String r13 = "moveTo"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x018f
            goto L_0x01a7
        L_0x018f:
            r13 = 2
            goto L_0x01a8
        L_0x0191:
            java.lang.String r13 = "requestFileSystem"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x019a
            goto L_0x01a7
        L_0x019a:
            r13 = 1
            goto L_0x01a8
        L_0x019c:
            java.lang.String r13 = "copyTo"
            boolean r13 = r0.equals(r13)
            if (r13 != 0) goto L_0x01a5
            goto L_0x01a7
        L_0x01a5:
            r13 = 0
            goto L_0x01a8
        L_0x01a7:
            r13 = -1
        L_0x01a8:
            java.lang.String r6 = "android_asset/"
            java.lang.String r4 = "/android_asset/"
            java.lang.String r5 = "apps/"
            java.lang.String r14 = "filePath"
            java.lang.String r15 = ""
            java.lang.String r12 = "/"
            r19 = 0
            switch(r13) {
                case 0: goto L_0x0bc3;
                case 1: goto L_0x0b56;
                case 2: goto L_0x0ab1;
                case 3: goto L_0x0a5f;
                case 4: goto L_0x0975;
                case 5: goto L_0x0965;
                case 6: goto L_0x0921;
                case 7: goto L_0x0802;
                case 8: goto L_0x07a7;
                case 9: goto L_0x071c;
                case 10: goto L_0x06c3;
                case 11: goto L_0x06b0;
                case 12: goto L_0x06a4;
                case 13: goto L_0x0651;
                case 14: goto L_0x0644;
                case 15: goto L_0x05d8;
                case 16: goto L_0x05cb;
                case 17: goto L_0x06b0;
                case 18: goto L_0x04ff;
                case 19: goto L_0x0478;
                case 20: goto L_0x03bc;
                case 21: goto L_0x034a;
                case 22: goto L_0x026e;
                case 23: goto L_0x0217;
                case 24: goto L_0x01bb;
                default: goto L_0x01b9;
            }
        L_0x01b9:
            goto L_0x0c74
        L_0x01bb:
            r6 = r1[r8]
            int r0 = r1.length
            r2 = 1
            if (r0 <= r2) goto L_0x01db
            r0 = r1[r2]
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r0 == 0) goto L_0x01db
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x01d7 }
            r1 = r1[r2]     // Catch:{ Exception -> 0x01d7 }
            r0.<init>(r1)     // Catch:{ Exception -> 0x01d7 }
            java.lang.String r1 = "multiple"
            boolean r0 = r0.optBoolean(r1, r8)     // Catch:{ Exception -> 0x01d7 }
            goto L_0x01dc
        L_0x01d7:
            r0 = move-exception
            r0.printStackTrace()
        L_0x01db:
            r0 = 0
        L_0x01dc:
            int r10 = io.dcloud.common.adapter.util.PermissionUtil.getRequestCode()
            io.dcloud.js.file.FileFeatureImpl$d r12 = new io.dcloud.js.file.FileFeatureImpl$d
            r1 = r12
            r2 = r20
            r3 = r10
            r4 = r11
            r5 = r21
            r1.<init>(r3, r4, r5, r6)
            io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onActivityResult
            r11.registerSysEventListener(r12, r1)
            android.content.Intent r1 = new android.content.Intent
            java.lang.String r2 = "android.intent.action.GET_CONTENT"
            r1.<init>(r2)
            java.lang.String r2 = "android.intent.extra.ALLOW_MULTIPLE"
            if (r0 != 0) goto L_0x0200
            r1.putExtra(r2, r8)
            goto L_0x0204
        L_0x0200:
            r3 = 1
            r1.putExtra(r2, r3)
        L_0x0204:
            java.lang.String r0 = "*/*"
            r1.setType(r0)
            java.lang.String r0 = "android.intent.category.OPENABLE"
            r1.addCategory(r0)
            android.app.Activity r0 = r21.getActivity()
            r0.startActivityForResult(r1, r10)
            goto L_0x0c74
        L_0x0217:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x021f
            goto L_0x0c74
        L_0x021f:
            r0 = r0[r8]
            android.content.Context r1 = r21.getContext()
            r2 = 1
            java.lang.String[] r4 = new java.lang.String[r2]
            r4[r8] = r0
            boolean r1 = io.dcloud.common.util.FileUtil.checkPathAccord(r1, r4)
            if (r1 != 0) goto L_0x0239
            java.lang.String r1 = io.dcloud.common.util.FileUtil.getPathForPublicType(r0)
            if (r1 == 0) goto L_0x0237
            goto L_0x0239
        L_0x0237:
            r1 = 0
            goto L_0x023a
        L_0x0239:
            r1 = 1
        L_0x023a:
            if (r1 != 0) goto L_0x0243
            r1 = 15
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0243:
            boolean r1 = r11.checkPrivateDir(r0)
            if (r1 == 0) goto L_0x024f
            if (r3 == 0) goto L_0x024f
        L_0x024b:
            r0 = r19
            r12 = 0
            goto L_0x025e
        L_0x024f:
            java.lang.String r1 = r11.convert2RelPath(r0)     // Catch:{ Exception -> 0x0259 }
            org.json.JSONArray r0 = io.dcloud.js.file.a.b(r0, r1)     // Catch:{ Exception -> 0x0259 }
            r12 = 1
            goto L_0x025e
        L_0x0259:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x024b
        L_0x025e:
            if (r12 == 0) goto L_0x0267
            int r1 = io.dcloud.common.util.JSUtil.OK
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONArray) r0, (int) r1, (boolean) r8)
            goto L_0x0c74
        L_0x0267:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x026e:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x0276
            goto L_0x0c74
        L_0x0276:
            r1 = r0[r8]     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            android.content.Context r2 = r21.getContext()     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            r3 = 1
            java.lang.String[] r4 = new java.lang.String[r3]     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            r4[r8] = r1     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            boolean r2 = io.dcloud.common.util.FileUtil.checkPathAccord(r2, r4)     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            if (r2 != 0) goto L_0x0294
            r2 = 15
            r9.a((int) r2, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r19)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r19)
            goto L_0x0c74
        L_0x0294:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            java.io.FileInputStream r11 = new java.io.FileInputStream     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            r11.<init>(r2)     // Catch:{ Exception -> 0x032c, all -> 0x0328 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r1.<init>()     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r1.append(r3)     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            java.lang.String r3 = r2.getName()     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r1.append(r3)     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r4.<init>()     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            java.lang.String r5 = r2.getParent()     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r4.append(r5)     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r4.append(r12)     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r4.append(r1)     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            java.lang.String r1 = r4.toString()     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            java.io.FileOutputStream r12 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r12.<init>(r3)     // Catch:{ Exception -> 0x0324, all -> 0x0322 }
            r1 = 1
            r4 = r0[r1]     // Catch:{ Exception -> 0x0320 }
            int r1 = java.lang.Integer.parseInt(r4)     // Catch:{ Exception -> 0x0320 }
            r4 = 2
            r4 = r0[r4]     // Catch:{ Exception -> 0x0320 }
            int r4 = java.lang.Integer.parseInt(r4)     // Catch:{ Exception -> 0x0320 }
            int r5 = r4 + r1
            r6 = 10240(0x2800, float:1.4349E-41)
            int r5 = java.lang.Math.min(r5, r6)     // Catch:{ Exception -> 0x0320 }
            byte[] r5 = new byte[r5]     // Catch:{ Exception -> 0x0320 }
            r6 = 0
        L_0x02ee:
            int r13 = r11.read(r5)     // Catch:{ Exception -> 0x0320 }
            r14 = -1
            if (r13 == r14) goto L_0x030c
            int r14 = r1 - r6
            int r15 = r13 - r4
            int r14 = java.lang.Math.min(r14, r15)     // Catch:{ Exception -> 0x0320 }
            if (r14 <= 0) goto L_0x0303
            r12.write(r5, r4, r14)     // Catch:{ Exception -> 0x0320 }
            int r6 = r6 + r14
        L_0x0303:
            if (r6 < r1) goto L_0x0306
            goto L_0x030c
        L_0x0306:
            if (r4 <= 0) goto L_0x030a
            int r4 = r4 - r13
            goto L_0x02ee
        L_0x030a:
            r4 = 0
            goto L_0x02ee
        L_0x030c:
            r2.delete()     // Catch:{ Exception -> 0x0320 }
            r3.renameTo(r2)     // Catch:{ Exception -> 0x0320 }
            r1 = 1
            r3 = r0[r1]     // Catch:{ Exception -> 0x0320 }
            int r4 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x0320 }
            r5 = 1
            r6 = 0
            r1 = r21
            r2 = r10
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0320 }
            goto L_0x0338
        L_0x0320:
            r0 = move-exception
            goto L_0x0330
        L_0x0322:
            r0 = move-exception
            goto L_0x0343
        L_0x0324:
            r0 = move-exception
            r12 = r19
            goto L_0x0330
        L_0x0328:
            r0 = move-exception
            r11 = r19
            goto L_0x0343
        L_0x032c:
            r0 = move-exception
            r11 = r19
            r12 = r11
        L_0x0330:
            r0.printStackTrace()     // Catch:{ all -> 0x0340 }
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)     // Catch:{ all -> 0x0340 }
        L_0x0338:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r11)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r12)
            goto L_0x0c74
        L_0x0340:
            r0 = move-exception
            r19 = r12
        L_0x0343:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r11)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r19)
            throw r0
        L_0x034a:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x0352
            goto L_0x0c74
        L_0x0352:
            r1 = r0[r8]
            android.content.Context r2 = r21.getContext()
            r4 = 1
            java.lang.String[] r5 = new java.lang.String[r4]
            r5[r8] = r1
            boolean r2 = io.dcloud.common.util.FileUtil.checkPathAccord(r2, r5)
            if (r2 != 0) goto L_0x0370
            android.content.Context r2 = r21.getContext()
            boolean r2 = io.dcloud.common.util.FileUtil.isFilePathForPublic(r2, r1)
            if (r2 == 0) goto L_0x036e
            goto L_0x0370
        L_0x036e:
            r2 = 0
            goto L_0x0371
        L_0x0370:
            r2 = 1
        L_0x0371:
            if (r2 != 0) goto L_0x037a
            r2 = 15
            r9.a((int) r2, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x037a:
            boolean r2 = r11.checkPrivateDir(r1)
            if (r2 == 0) goto L_0x0386
            if (r3 == 0) goto L_0x0386
        L_0x0382:
            r0 = r19
            r12 = 0
            goto L_0x03ac
        L_0x0386:
            int r2 = r0.length     // Catch:{ Exception -> 0x03a7 }
            r3 = 2
            if (r2 != r3) goto L_0x03a0
            r2 = 1
            r3 = r0[r2]     // Catch:{ Exception -> 0x03a7 }
            if (r3 == 0) goto L_0x03a0
            r3 = r0[r2]     // Catch:{ Exception -> 0x03a7 }
            java.lang.String r4 = "true"
            boolean r3 = r3.equalsIgnoreCase(r4)     // Catch:{ Exception -> 0x03a7 }
            if (r3 == 0) goto L_0x03a0
            r0 = r0[r2]     // Catch:{ Exception -> 0x03a7 }
            boolean r0 = java.lang.Boolean.parseBoolean(r0)     // Catch:{ Exception -> 0x03a7 }
            goto L_0x03a1
        L_0x03a0:
            r0 = 0
        L_0x03a1:
            org.json.JSONObject r0 = io.dcloud.js.file.a.a((java.lang.String) r1, (boolean) r0)     // Catch:{ Exception -> 0x03a7 }
            r12 = 1
            goto L_0x03ac
        L_0x03a7:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0382
        L_0x03ac:
            if (r12 == 0) goto L_0x03b5
            int r1 = io.dcloud.common.util.JSUtil.OK
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r1, (boolean) r8)
            goto L_0x0c74
        L_0x03b5:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x03bc:
            java.lang.String[] r1 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)     // Catch:{ JSONException -> 0x0471 }
            if (r1 != 0) goto L_0x03c4
            goto L_0x0c74
        L_0x03c4:
            r0 = r1[r8]     // Catch:{ JSONException -> 0x0471 }
            boolean r0 = r9.e(r0)     // Catch:{ JSONException -> 0x0471 }
            if (r0 == 0) goto L_0x03dc
            boolean r2 = r11.isOnAppRunningMode()     // Catch:{ JSONException -> 0x0471 }
            if (r2 == 0) goto L_0x03dc
            r2 = r1[r8]     // Catch:{ JSONException -> 0x0471 }
            boolean r2 = r11.checkPrivateDir(r2)     // Catch:{ JSONException -> 0x0471 }
            if (r2 == 0) goto L_0x03dc
            r2 = 1
            goto L_0x03dd
        L_0x03dc:
            r2 = 0
        L_0x03dd:
            java.lang.String r3 = r21.obtainFullUrl()     // Catch:{ JSONException -> 0x0471 }
            r4 = r1[r8]     // Catch:{ JSONException -> 0x0471 }
            java.lang.String r13 = r11.convert2AbsFullPath(r3, r4)     // Catch:{ JSONException -> 0x0471 }
            if (r2 == 0) goto L_0x040f
            org.json.JSONObject r0 = r9.a(r13)     // Catch:{ JSONException -> 0x0471 }
            java.lang.String r12 = "_www"
            r14 = 1
            java.lang.String r15 = "_www"
            java.lang.String r1 = "fsName"
            java.lang.String r16 = r0.optString(r1)     // Catch:{ JSONException -> 0x0471 }
            java.lang.String r1 = "type"
            int r17 = r0.optInt(r1)     // Catch:{ JSONException -> 0x0471 }
            java.lang.String r1 = "fsRoot"
            org.json.JSONObject r18 = r0.optJSONObject(r1)     // Catch:{ JSONException -> 0x0471 }
            org.json.JSONObject r0 = io.dcloud.js.file.a.a(r12, r13, r14, r15, r16, r17, r18)     // Catch:{ JSONException -> 0x0471 }
            int r1 = io.dcloud.common.util.JSUtil.OK     // Catch:{ JSONException -> 0x0471 }
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r1, (boolean) r8)     // Catch:{ JSONException -> 0x0471 }
            goto L_0x0c74
        L_0x040f:
            java.io.File r2 = new java.io.File     // Catch:{ JSONException -> 0x0471 }
            r2.<init>(r13)     // Catch:{ JSONException -> 0x0471 }
            boolean r3 = r2.exists()     // Catch:{ JSONException -> 0x0471 }
            if (r3 != 0) goto L_0x043c
            if (r0 == 0) goto L_0x043c
            java.io.File r0 = r2.getParentFile()     // Catch:{ Exception -> 0x0438 }
            boolean r4 = r0.exists()     // Catch:{ Exception -> 0x0438 }
            if (r4 != 0) goto L_0x0429
            r0.mkdirs()     // Catch:{ Exception -> 0x0438 }
        L_0x0429:
            java.lang.Object r0 = io.dcloud.common.adapter.io.DHFile.createFileHandler(r13)     // Catch:{ Exception -> 0x0438 }
            byte r0 = io.dcloud.common.adapter.io.DHFile.createNewFile(r0)     // Catch:{ Exception -> 0x0438 }
            r3 = 1
            if (r3 != r0) goto L_0x0436
            r12 = 1
            goto L_0x043d
        L_0x0436:
            r12 = 0
            goto L_0x043d
        L_0x0438:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ JSONException -> 0x0471 }
        L_0x043c:
            r12 = r3
        L_0x043d:
            if (r12 == 0) goto L_0x046a
            org.json.JSONObject r0 = r9.a(r13)     // Catch:{ JSONException -> 0x0471 }
            java.lang.String r12 = r2.getName()     // Catch:{ JSONException -> 0x0471 }
            boolean r14 = r2.isDirectory()     // Catch:{ JSONException -> 0x0471 }
            r15 = r1[r8]     // Catch:{ JSONException -> 0x0471 }
            java.lang.String r1 = "fsName"
            java.lang.String r16 = r0.optString(r1)     // Catch:{ JSONException -> 0x0471 }
            java.lang.String r1 = "type"
            int r17 = r0.optInt(r1)     // Catch:{ JSONException -> 0x0471 }
            java.lang.String r1 = "fsRoot"
            org.json.JSONObject r18 = r0.optJSONObject(r1)     // Catch:{ JSONException -> 0x0471 }
            org.json.JSONObject r0 = io.dcloud.js.file.a.a(r12, r13, r14, r15, r16, r17, r18)     // Catch:{ JSONException -> 0x0471 }
            int r1 = io.dcloud.common.util.JSUtil.OK     // Catch:{ JSONException -> 0x0471 }
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r1, (boolean) r8)     // Catch:{ JSONException -> 0x0471 }
            goto L_0x0c74
        L_0x046a:
            r1 = 14
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)     // Catch:{ JSONException -> 0x0471 }
            goto L_0x0c74
        L_0x0471:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0478:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x0480
            goto L_0x0c74
        L_0x0480:
            r1 = r0[r8]
            boolean r1 = io.dcloud.common.util.JSUtil.checkOperateDirErrorAndCallback(r7, r10, r1)
            if (r1 == 0) goto L_0x0489
            return r19
        L_0x0489:
            r0 = r0[r8]
            android.content.Context r1 = r21.getContext()
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]
            r2[r8] = r0
            boolean r1 = io.dcloud.common.util.FileUtil.checkPathAccord(r1, r2)
            if (r1 != 0) goto L_0x04a1
            r1 = 15
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x04a1:
            java.io.File r1 = new java.io.File
            r1.<init>(r0)
            boolean r0 = r1.isDirectory()
            if (r0 == 0) goto L_0x04eb
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x04e7 }
            java.lang.String r0 = java.lang.String.valueOf(r2)     // Catch:{ IOException -> 0x04e7 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x04e7 }
            r2.<init>()     // Catch:{ IOException -> 0x04e7 }
            java.lang.String r3 = r1.getAbsolutePath()     // Catch:{ IOException -> 0x04e7 }
            r2.append(r3)     // Catch:{ IOException -> 0x04e7 }
            r2.append(r12)     // Catch:{ IOException -> 0x04e7 }
            java.lang.String r2 = r2.toString()     // Catch:{ IOException -> 0x04e7 }
            io.dcloud.common.adapter.io.DHFile.rename(r2, r0)     // Catch:{ IOException -> 0x04e7 }
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x04e7 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x04e7 }
            r3.<init>()     // Catch:{ IOException -> 0x04e7 }
            java.lang.String r4 = r1.getParent()     // Catch:{ IOException -> 0x04e7 }
            r3.append(r4)     // Catch:{ IOException -> 0x04e7 }
            r3.append(r12)     // Catch:{ IOException -> 0x04e7 }
            r3.append(r0)     // Catch:{ IOException -> 0x04e7 }
            java.lang.String r0 = r3.toString()     // Catch:{ IOException -> 0x04e7 }
            r2.<init>(r0)     // Catch:{ IOException -> 0x04e7 }
            r1 = r2
            goto L_0x04eb
        L_0x04e7:
            r0 = move-exception
            r0.printStackTrace()
        L_0x04eb:
            boolean r0 = io.dcloud.common.adapter.io.DHFile.delete(r1)
            if (r0 == 0) goto L_0x04f8
            int r0 = io.dcloud.common.util.JSUtil.OK
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (java.lang.String) r15, (int) r0, (boolean) r8)
            goto L_0x0c74
        L_0x04f8:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x04ff:
            r0 = r1[r8]
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0514 }
            r13 = 1
            r1 = r1[r13]     // Catch:{ JSONException -> 0x0514 }
            r3.<init>(r1)     // Catch:{ JSONException -> 0x0514 }
            java.lang.String r1 = r3.optString(r14)     // Catch:{ JSONException -> 0x0514 }
            java.lang.String r13 = "digestAlgorithm"
            java.lang.String r2 = r3.optString(r13, r2)     // Catch:{ JSONException -> 0x0515 }
            goto L_0x0515
        L_0x0514:
            r1 = r15
        L_0x0515:
            r13 = r2
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r1)
            if (r2 != 0) goto L_0x0c74
            boolean r2 = io.dcloud.common.util.PdrUtil.isNetPath(r1)
            if (r2 != 0) goto L_0x0c74
            io.dcloud.common.DHInterface.IApp r2 = r21.obtainApp()
            java.lang.String r3 = r21.obtainFullUrl()
            java.lang.String r2 = r2.convert2AbsFullPath(r3, r1)
            android.content.Context r3 = r21.getContext()
            r22 = r13
            r14 = 1
            java.lang.String[] r13 = new java.lang.String[r14]
            r13[r8] = r1
            boolean r3 = io.dcloud.common.util.FileUtil.checkPathAccord(r3, r13)
            if (r3 != 0) goto L_0x054c
            android.content.Context r3 = r21.getContext()
            boolean r1 = io.dcloud.common.util.FileUtil.isFilePathForPublic(r3, r1)
            if (r1 == 0) goto L_0x054a
            goto L_0x054c
        L_0x054a:
            r1 = 0
            goto L_0x054d
        L_0x054c:
            r1 = 1
        L_0x054d:
            if (r1 != 0) goto L_0x0556
            r1 = 15
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0556:
            boolean r1 = r2.startsWith(r5)
            if (r1 == 0) goto L_0x056b
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r12)
            r1.append(r2)
            java.lang.String r2 = r1.toString()
        L_0x056b:
            boolean r1 = r2.startsWith(r4)
            if (r1 == 0) goto L_0x0578
            java.lang.String r1 = "/android_asset"
            java.lang.String r2 = r2.replace(r1, r15)
            goto L_0x0584
        L_0x0578:
            boolean r1 = r2.startsWith(r6)
            if (r1 == 0) goto L_0x0584
            java.lang.String r1 = "android_asset"
            java.lang.String r2 = r2.replace(r1, r15)
        L_0x0584:
            java.lang.String r1 = r11.checkPrivateDirAndCopy2Temp(r2)
            java.io.File r3 = new java.io.File
            r3.<init>(r1)
            boolean r1 = r3.exists()
            if (r1 != 0) goto L_0x05b5
            r1 = 2
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r2 = -4
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r1[r8] = r2
            java.lang.String r2 = io.dcloud.common.constant.DOMException.MSG_FILE_NOT_EXIST
            r3 = 1
            r1[r3] = r2
            java.lang.String r2 = "{code:%d,message:'%s'}"
            java.lang.String r3 = io.dcloud.common.util.StringUtil.format(r2, r1)
            int r4 = io.dcloud.common.util.JSUtil.ERROR
            r5 = 1
            r6 = 0
            r1 = r21
            r2 = r0
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)
            goto L_0x0c74
        L_0x05b5:
            io.dcloud.common.util.ThreadPool r8 = io.dcloud.common.util.ThreadPool.self()
            io.dcloud.js.file.FileFeatureImpl$c r10 = new io.dcloud.js.file.FileFeatureImpl$c
            r1 = r10
            r2 = r20
            r4 = r22
            r5 = r21
            r6 = r0
            r1.<init>(r3, r4, r5, r6)
            r8.addThreadTask(r10)
            goto L_0x0c74
        L_0x05cb:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x05d3
            goto L_0x0c74
        L_0x05d3:
            r9.a((io.dcloud.common.DHInterface.IWebview) r7, (io.dcloud.common.DHInterface.IApp) r11, (java.lang.String[]) r0, (java.lang.String) r10)
            goto L_0x0c74
        L_0x05d8:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x05e0
            goto L_0x0c74
        L_0x05e0:
            r0 = r0[r8]
            android.content.Context r1 = r21.getContext()
            r2 = 1
            java.lang.String[] r3 = new java.lang.String[r2]
            r3[r8] = r0
            boolean r1 = io.dcloud.common.util.FileUtil.checkPathAccord(r1, r3)
            if (r1 != 0) goto L_0x05fe
            android.content.Context r1 = r21.getContext()
            boolean r1 = io.dcloud.common.util.FileUtil.isFilePathForPublic(r1, r0)
            if (r1 == 0) goto L_0x05fc
            goto L_0x05fe
        L_0x05fc:
            r1 = 0
            goto L_0x05ff
        L_0x05fe:
            r1 = 1
        L_0x05ff:
            if (r1 != 0) goto L_0x0608
            r1 = 15
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0608:
            if (r0 == 0) goto L_0x0615
            boolean r1 = r9.e(r0)
            if (r1 == 0) goto L_0x0615
            r1 = 4
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            return r19
        L_0x0615:
            java.io.File r1 = new java.io.File
            r1.<init>(r0)
            boolean r0 = r1.exists()
            if (r0 == 0) goto L_0x063b
            java.lang.String r0 = r1.getParent()
            java.io.File r1 = r1.getParentFile()
            java.lang.String r1 = r1.getName()
            java.lang.String r2 = r11.convert2RelPath(r0)
            r3 = 1
            org.json.JSONObject r0 = io.dcloud.js.file.a.a(r1, r0, r2, r3)
            int r1 = io.dcloud.common.util.JSUtil.OK
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r1, (boolean) r8)
            r8 = 1
        L_0x063b:
            if (r8 != 0) goto L_0x0c74
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0644:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x064c
            goto L_0x0c74
        L_0x064c:
            r9.b(r7, r0, r10)
            goto L_0x0c74
        L_0x0651:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x0659
            goto L_0x0c74
        L_0x0659:
            r0 = r0[r8]
            boolean r1 = r11.checkPrivateDir(r0)
            if (r1 == 0) goto L_0x0667
            if (r3 == 0) goto L_0x0667
        L_0x0663:
            r0 = r19
            r12 = 0
            goto L_0x0694
        L_0x0667:
            byte[] r1 = io.dcloud.common.adapter.io.DHFile.readAll(r0)
            java.lang.String r0 = io.dcloud.common.util.PdrUtil.getMimeType(r0)     // Catch:{ Exception -> 0x068f }
            int r2 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceSdkVer     // Catch:{ Exception -> 0x068f }
            r3 = 8
            if (r2 < r3) goto L_0x067b
            r2 = 2
            java.lang.String r1 = android.util.Base64.encodeToString(r1, r2)     // Catch:{ Exception -> 0x068f }
            goto L_0x0680
        L_0x067b:
            r2 = 2
            java.lang.String r1 = io.dcloud.common.util.Base64.encode((byte[]) r1)     // Catch:{ Exception -> 0x068f }
        L_0x0680:
            java.lang.String r3 = "data:%s;base64,%s"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x068f }
            r2[r8] = r0     // Catch:{ Exception -> 0x068f }
            r4 = 1
            r2[r4] = r1     // Catch:{ Exception -> 0x068f }
            java.lang.String r0 = io.dcloud.common.util.StringUtil.format(r3, r2)     // Catch:{ Exception -> 0x068f }
            r12 = 1
            goto L_0x0694
        L_0x068f:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0663
        L_0x0694:
            if (r12 == 0) goto L_0x069d
            int r1 = io.dcloud.common.util.JSUtil.OK
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (java.lang.String) r0, (int) r1, (boolean) r8)
            goto L_0x0c74
        L_0x069d:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x06a4:
            r0 = r1[r8]
            java.lang.String r0 = r11.convert2RelPath(r0)
            r2 = 1
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r2)
            return r0
        L_0x06b0:
            r2 = 1
            r3 = r1[r8]
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ JSONException -> 0x06be }
            r1 = r1[r2]     // Catch:{ JSONException -> 0x06be }
            r4.<init>(r1)     // Catch:{ JSONException -> 0x06be }
            java.lang.String r15 = r4.optString(r14)     // Catch:{ JSONException -> 0x06be }
        L_0x06be:
            r9.a((java.lang.String) r15, (java.lang.String) r3, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r0)
            goto L_0x0c74
        L_0x06c3:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)     // Catch:{ Exception -> 0x0715 }
            if (r0 != 0) goto L_0x06cb
            goto L_0x0c74
        L_0x06cb:
            r1 = r0[r8]     // Catch:{ Exception -> 0x0715 }
            android.content.Context r2 = r21.getContext()     // Catch:{ Exception -> 0x0715 }
            r3 = 1
            java.lang.String[] r4 = new java.lang.String[r3]     // Catch:{ Exception -> 0x0715 }
            r4[r8] = r1     // Catch:{ Exception -> 0x0715 }
            boolean r2 = io.dcloud.common.util.FileUtil.checkPathAccord(r2, r4)     // Catch:{ Exception -> 0x0715 }
            if (r2 != 0) goto L_0x06e3
            r2 = 15
            r9.a((int) r2, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)     // Catch:{ Exception -> 0x0715 }
            goto L_0x0c74
        L_0x06e3:
            boolean r2 = r11.checkPrivateDir(r1)     // Catch:{ Exception -> 0x0715 }
            r3 = 2
            r3 = r0[r3]     // Catch:{ Exception -> 0x0715 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x0715 }
            int r3 = r3.intValue()     // Catch:{ Exception -> 0x0715 }
            r4 = 1
            r5 = r0[r4]     // Catch:{ Exception -> 0x0715 }
            if (r5 == 0) goto L_0x0710
            if (r2 == 0) goto L_0x06fa
            goto L_0x0710
        L_0x06fa:
            r0 = r0[r4]     // Catch:{ Exception -> 0x0715 }
            byte[] r0 = r0.getBytes()     // Catch:{ Exception -> 0x0715 }
            io.dcloud.common.adapter.io.DHFile.writeFile((byte[]) r0, (int) r3, (java.lang.String) r1)     // Catch:{ Exception -> 0x0715 }
            int r0 = r0.length     // Catch:{ Exception -> 0x0715 }
            double r3 = (double) r0     // Catch:{ Exception -> 0x0715 }
            int r5 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x0715 }
            r6 = 0
            r1 = r21
            r2 = r10
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r1, (java.lang.String) r2, (double) r3, (int) r5, (boolean) r6)     // Catch:{ Exception -> 0x0715 }
            goto L_0x0c74
        L_0x0710:
            r0 = 4
            r9.a((int) r0, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)     // Catch:{ Exception -> 0x0715 }
            return r19
        L_0x0715:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x071c:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x0724
            goto L_0x0c74
        L_0x0724:
            r1 = r0[r8]
            r2 = 1
            r3 = r0[r2]
            java.lang.String r1 = r11.convert2AbsFullPath(r1, r3)
            android.content.Context r3 = r21.getContext()
            java.lang.String[] r4 = new java.lang.String[r2]
            r4[r8] = r1
            boolean r2 = io.dcloud.common.util.FileUtil.checkPathAccord(r3, r4)
            if (r2 != 0) goto L_0x0742
            r2 = 15
            r9.a((int) r2, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0742:
            java.io.File r2 = new java.io.File
            r2.<init>(r1)
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x07a0 }
            r4 = 2
            r4 = r0[r4]     // Catch:{ Exception -> 0x07a0 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x07a0 }
            r4 = 1
            r0 = r0[r4]     // Catch:{ Exception -> 0x07a0 }
            java.lang.String r0 = "create"
            boolean r0 = r3.optBoolean(r0)     // Catch:{ Exception -> 0x07a0 }
            java.lang.String r4 = "exclusive"
            boolean r3 = r3.optBoolean(r4)     // Catch:{ Exception -> 0x07a0 }
            boolean r4 = r2.exists()     // Catch:{ Exception -> 0x07a0 }
            if (r4 != 0) goto L_0x0783
            if (r0 == 0) goto L_0x077d
            byte r0 = io.dcloud.common.adapter.io.DHFile.createNewFile(r2)     // Catch:{ Exception -> 0x07a0 }
            java.lang.String r2 = r2.getName()     // Catch:{ Exception -> 0x07a0 }
            r4 = -1
            if (r0 == r4) goto L_0x0777
            r4 = -2
            if (r0 != r4) goto L_0x0791
            if (r3 != 0) goto L_0x0777
            goto L_0x0791
        L_0x0777:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x07a0 }
            r0.<init>()     // Catch:{ Exception -> 0x07a0 }
            throw r0     // Catch:{ Exception -> 0x07a0 }
        L_0x077d:
            r1 = 14
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)     // Catch:{ Exception -> 0x07a0 }
            return r19
        L_0x0783:
            if (r0 == 0) goto L_0x078d
            if (r3 == 0) goto L_0x078d
            r0 = 12
            r9.a((int) r0, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)     // Catch:{ Exception -> 0x07a0 }
            return r19
        L_0x078d:
            java.lang.String r2 = r2.getName()     // Catch:{ Exception -> 0x07a0 }
        L_0x0791:
            java.lang.String r0 = r11.convert2RelPath(r1)     // Catch:{ Exception -> 0x07a0 }
            org.json.JSONObject r0 = io.dcloud.js.file.a.a(r2, r1, r0, r8)     // Catch:{ Exception -> 0x07a0 }
            int r1 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x07a0 }
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r1, (boolean) r8)     // Catch:{ Exception -> 0x07a0 }
            goto L_0x0c74
        L_0x07a0:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x07a7:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x07af
            goto L_0x0c74
        L_0x07af:
            r0 = r0[r8]
            android.content.Context r1 = r21.getContext()
            r2 = 1
            java.lang.String[] r4 = new java.lang.String[r2]
            r4[r8] = r0
            boolean r1 = io.dcloud.common.util.FileUtil.checkPathAccord(r1, r4)
            if (r1 != 0) goto L_0x07cd
            android.content.Context r1 = r21.getContext()
            boolean r1 = io.dcloud.common.util.FileUtil.isFilePathForPublic(r1, r0)
            if (r1 == 0) goto L_0x07cb
            goto L_0x07cd
        L_0x07cb:
            r1 = 0
            goto L_0x07ce
        L_0x07cd:
            r1 = 1
        L_0x07ce:
            if (r1 != 0) goto L_0x07d7
            r1 = 15
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x07d7:
            boolean r1 = r11.checkPrivateDir(r0)
            if (r1 == 0) goto L_0x07e3
            if (r3 == 0) goto L_0x07e3
        L_0x07df:
            r0 = r19
            r12 = 0
            goto L_0x07f2
        L_0x07e3:
            java.lang.String r1 = r9.d(r0)     // Catch:{ Exception -> 0x07ed }
            org.json.JSONObject r0 = io.dcloud.js.file.a.a((java.lang.String) r0, (java.lang.String) r1)     // Catch:{ Exception -> 0x07ed }
            r12 = 1
            goto L_0x07f2
        L_0x07ed:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x07df
        L_0x07f2:
            if (r12 == 0) goto L_0x07fb
            int r1 = io.dcloud.common.util.JSUtil.OK
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r1, (boolean) r8)
            goto L_0x0c74
        L_0x07fb:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0802:
            r2 = r1[r8]
            r3 = 1
            r0 = r1[r3]
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r3 != 0) goto L_0x0c74
            boolean r3 = io.dcloud.common.util.PdrUtil.isNetPath(r0)
            if (r3 == 0) goto L_0x08ad
            int r3 = r1.length     // Catch:{ Exception -> 0x089a }
            r4 = 2
            if (r3 <= r4) goto L_0x087e
            r3 = r1[r4]     // Catch:{ Exception -> 0x089a }
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r3)     // Catch:{ Exception -> 0x089a }
            if (r3 != 0) goto L_0x087e
            r1 = r1[r4]     // Catch:{ Exception -> 0x089a }
            boolean r3 = r1.endsWith(r12)     // Catch:{ Exception -> 0x089a }
            if (r3 != 0) goto L_0x0836
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x089a }
            r3.<init>()     // Catch:{ Exception -> 0x089a }
            r3.append(r1)     // Catch:{ Exception -> 0x089a }
            r3.append(r12)     // Catch:{ Exception -> 0x089a }
            java.lang.String r1 = r3.toString()     // Catch:{ Exception -> 0x089a }
        L_0x0836:
            java.lang.String r3 = "image/*"
            java.lang.String r3 = io.dcloud.common.util.PdrUtil.getDownloadFilename(r0, r3, r0)     // Catch:{ Exception -> 0x089a }
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x089a }
            if (r4 == 0) goto L_0x0862
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x089a }
            r3.<init>()     // Catch:{ Exception -> 0x089a }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x089a }
            r3.append(r4)     // Catch:{ Exception -> 0x089a }
            java.lang.String r4 = "_"
            r3.append(r4)     // Catch:{ Exception -> 0x089a }
            int r4 = r0.hashCode()     // Catch:{ Exception -> 0x089a }
            int r4 = java.lang.Math.abs(r4)     // Catch:{ Exception -> 0x089a }
            r3.append(r4)     // Catch:{ Exception -> 0x089a }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x089a }
        L_0x0862:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x089a }
            r4.<init>()     // Catch:{ Exception -> 0x089a }
            io.dcloud.common.DHInterface.IApp r5 = r21.obtainApp()     // Catch:{ Exception -> 0x089a }
            java.lang.String r6 = r21.obtainFullUrl()     // Catch:{ Exception -> 0x089a }
            java.lang.String r1 = r5.convert2AbsFullPath(r6, r1)     // Catch:{ Exception -> 0x089a }
            r4.append(r1)     // Catch:{ Exception -> 0x089a }
            r4.append(r3)     // Catch:{ Exception -> 0x089a }
            java.lang.String r1 = r4.toString()     // Catch:{ Exception -> 0x089a }
            goto L_0x0880
        L_0x087e:
            r1 = r19
        L_0x0880:
            android.content.Context r3 = r21.getContext()     // Catch:{ Exception -> 0x089a }
            com.bumptech.glide.RequestManager r3 = com.bumptech.glide.Glide.with((android.content.Context) r3)     // Catch:{ Exception -> 0x089a }
            com.bumptech.glide.RequestBuilder r3 = r3.asFile()     // Catch:{ Exception -> 0x089a }
            com.bumptech.glide.RequestBuilder r0 = r3.load((java.lang.String) r0)     // Catch:{ Exception -> 0x089a }
            io.dcloud.js.file.FileFeatureImpl$b r3 = new io.dcloud.js.file.FileFeatureImpl$b     // Catch:{ Exception -> 0x089a }
            r3.<init>(r1, r7, r2)     // Catch:{ Exception -> 0x089a }
            r0.into(r3)     // Catch:{ Exception -> 0x089a }
            goto L_0x0c74
        L_0x089a:
            java.lang.String r0 = "Failed to load resource"
            r1 = 13
            java.lang.String r3 = io.dcloud.common.constant.DOMException.toJSON((int) r1, (java.lang.String) r0)
            int r4 = io.dcloud.common.util.JSUtil.ERROR
            r5 = 1
            r6 = 0
            r1 = r21
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)
            goto L_0x0c74
        L_0x08ad:
            java.lang.String r1 = r21.obtainFullUrl()
            java.lang.String r0 = r11.convert2AbsFullPath(r1, r0)
            android.content.Context r1 = r21.getContext()
            r3 = 1
            java.lang.String[] r13 = new java.lang.String[r3]
            r13[r8] = r0
            boolean r1 = io.dcloud.common.util.FileUtil.checkPathAccord(r1, r13)
            if (r1 != 0) goto L_0x08ce
            android.content.Context r1 = r21.getContext()
            boolean r1 = io.dcloud.common.util.FileUtil.isFilePathForPublic(r1, r0)
            if (r1 == 0) goto L_0x08cf
        L_0x08ce:
            r8 = 1
        L_0x08cf:
            if (r8 != 0) goto L_0x08d8
            r1 = 15
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x08d8:
            boolean r1 = r0.startsWith(r5)
            if (r1 == 0) goto L_0x08ed
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r12)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
        L_0x08ed:
            boolean r1 = r0.startsWith(r4)
            if (r1 == 0) goto L_0x08fa
            java.lang.String r1 = "/android_asset"
            java.lang.String r0 = r0.replace(r1, r15)
            goto L_0x0906
        L_0x08fa:
            boolean r1 = r0.startsWith(r6)
            if (r1 == 0) goto L_0x0906
            java.lang.String r1 = "android_asset"
            java.lang.String r0 = r0.replace(r1, r15)
        L_0x0906:
            java.lang.String r0 = r11.checkPrivateDirAndCopy2Temp(r0)
            java.io.File r1 = new java.io.File
            r1.<init>(r0)
            boolean r1 = r1.exists()
            if (r1 != 0) goto L_0x091c
            r1 = 14
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x091c:
            r9.a((java.lang.String) r0, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r2)
            goto L_0x0c74
        L_0x0921:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x0929
            goto L_0x0c74
        L_0x0929:
            r0 = r0[r8]
            android.content.Context r1 = r21.getContext()
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]
            r2[r8] = r0
            boolean r1 = io.dcloud.common.util.FileUtil.checkPathAccord(r1, r2)
            if (r1 != 0) goto L_0x0941
            r1 = 15
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0941:
            boolean r1 = r11.checkPrivateDir(r0)
            if (r1 == 0) goto L_0x094c
            r1 = 4
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            return r19
        L_0x094c:
            java.io.File r1 = new java.io.File
            r1.<init>(r0)
            boolean r0 = r1.delete()
            if (r0 == 0) goto L_0x095e
            int r0 = io.dcloud.common.util.JSUtil.OK
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (java.lang.String) r15, (int) r0, (boolean) r8)
            goto L_0x0c74
        L_0x095e:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0965:
            r0 = r1[r8]
            java.lang.String r1 = r21.obtainFullUrl()
            java.lang.String r0 = r11.convert2AbsFullPath(r1, r0)
            r1 = 1
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r1)
            return r0
        L_0x0975:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x097d
            goto L_0x0c74
        L_0x097d:
            r1 = r0[r8]
            if (r1 == 0) goto L_0x099c
            r1 = r0[r8]
            boolean r1 = r1.endsWith(r12)
            if (r1 != 0) goto L_0x099c
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r2 = r0[r8]
            r1.append(r2)
            r1.append(r12)
            java.lang.String r1 = r1.toString()
            r0[r8] = r1
        L_0x099c:
            r1 = r0[r8]
            r2 = 1
            r3 = r0[r2]
            java.lang.String r1 = r11.convert2AbsFullPath(r1, r3)
            android.content.Context r3 = r21.getContext()
            java.lang.String[] r4 = new java.lang.String[r2]
            r4[r8] = r1
            boolean r3 = io.dcloud.common.util.FileUtil.checkPathAccord(r3, r4)
            if (r3 != 0) goto L_0x09ba
            r3 = 15
            r9.a((int) r3, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x09ba:
            r3 = r0[r8]
            if (r3 == 0) goto L_0x09da
            r3 = r0[r2]
            if (r3 == 0) goto L_0x09da
            r3 = r0[r2]
            java.lang.String r2 = "../"
            int r2 = r3.indexOf(r2)
            r3 = -1
            if (r2 == r3) goto L_0x09da
            r2 = r0[r8]
            boolean r2 = r9.e(r2)
            if (r2 == 0) goto L_0x09da
            r2 = 4
            r9.a((int) r2, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            return r19
        L_0x09da:
            boolean r2 = r1.endsWith(r12)
            if (r2 != 0) goto L_0x09ef
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            r2.append(r12)
            java.lang.String r1 = r2.toString()
        L_0x09ef:
            java.io.File r2 = new java.io.File
            r2.<init>(r1)
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x0a44 }
            r4 = 2
            r4 = r0[r4]     // Catch:{ Exception -> 0x0a44 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x0a44 }
            java.lang.String r4 = "create"
            boolean r4 = r3.optBoolean(r4)     // Catch:{ Exception -> 0x0a44 }
            java.lang.String r5 = "exclusive"
            boolean r3 = r3.optBoolean(r5)     // Catch:{ Exception -> 0x0a44 }
            boolean r2 = r2.exists()     // Catch:{ Exception -> 0x0a44 }
            if (r2 != 0) goto L_0x0a29
            if (r4 == 0) goto L_0x0a23
            byte r2 = io.dcloud.common.adapter.io.DHFile.createNewFile(r1)     // Catch:{ Exception -> 0x0a44 }
            r4 = -1
            if (r2 == r4) goto L_0x0a1d
            r4 = -2
            if (r2 != r4) goto L_0x0a32
            if (r3 != 0) goto L_0x0a1d
            goto L_0x0a32
        L_0x0a1d:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x0a44 }
            r0.<init>()     // Catch:{ Exception -> 0x0a44 }
            throw r0     // Catch:{ Exception -> 0x0a44 }
        L_0x0a23:
            r2 = 14
            r9.a((int) r2, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)     // Catch:{ Exception -> 0x0a44 }
            return r19
        L_0x0a29:
            if (r3 == 0) goto L_0x0a32
            if (r3 == 0) goto L_0x0a32
            r2 = 12
            r9.a((int) r2, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)     // Catch:{ Exception -> 0x0a44 }
        L_0x0a32:
            r2 = 1
            r0 = r0[r2]     // Catch:{ Exception -> 0x0a44 }
            java.lang.String r3 = r11.convert2RelPath(r1)     // Catch:{ Exception -> 0x0a44 }
            org.json.JSONObject r0 = io.dcloud.js.file.a.a(r0, r1, r3, r2)     // Catch:{ Exception -> 0x0a44 }
            int r2 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x0a44 }
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r2, (boolean) r8)     // Catch:{ Exception -> 0x0a44 }
            goto L_0x0c74
        L_0x0a44:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "Not Found "
            r0.append(r2)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            io.dcloud.common.adapter.util.Logger.d(r0)
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0a5f:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x0a67
            goto L_0x0c74
        L_0x0a67:
            r4 = r0[r8]
            android.content.Context r1 = r21.getContext()
            r2 = 1
            java.lang.String[] r5 = new java.lang.String[r2]
            r5[r8] = r4
            boolean r1 = io.dcloud.common.util.FileUtil.checkPathAccord(r1, r5)
            if (r1 != 0) goto L_0x0a7f
            r1 = 15
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0a7f:
            boolean r1 = r11.checkPrivateDir(r4)
            r6 = r0[r2]
            r2 = 2
            r2 = r0[r2]
            int r5 = io.dcloud.common.util.PdrUtil.parseInt(r2, r8)
            r2 = 3
            r0 = r0[r2]
            r2 = -1
            int r0 = io.dcloud.common.util.PdrUtil.parseInt(r0, r2)
            if (r1 == 0) goto L_0x0a9f
            if (r3 == 0) goto L_0x0a9f
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0a9f:
            io.dcloud.js.file.FileFeatureImpl$a r11 = new io.dcloud.js.file.FileFeatureImpl$a
            r1 = r11
            r2 = r20
            r3 = r4
            r4 = r0
            r7 = r21
            r8 = r10
            r1.<init>(r3, r4, r5, r6, r7, r8)
            io.dcloud.common.adapter.util.AsyncTaskHandler.executeThreadTask(r11)
            goto L_0x0c74
        L_0x0ab1:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r0 != 0) goto L_0x0ab9
            goto L_0x0c74
        L_0x0ab9:
            java.lang.String r1 = r21.obtainFullUrl()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r3 = 1
            r4 = r0[r3]
            r2.append(r4)
            r3 = 2
            r4 = r0[r3]
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            java.lang.String r1 = r11.convert2AbsFullPath(r1, r2)
            r2 = r0[r8]
            boolean r2 = r11.checkPrivateDir(r2)
            if (r2 != 0) goto L_0x0ae7
            boolean r2 = r11.checkPrivateDir(r1)
            if (r2 == 0) goto L_0x0ae5
            goto L_0x0ae7
        L_0x0ae5:
            r2 = 0
            goto L_0x0ae8
        L_0x0ae7:
            r2 = 1
        L_0x0ae8:
            if (r2 == 0) goto L_0x0af1
            r2 = 10
            r9.a((int) r2, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0af1:
            java.lang.String r2 = r21.obtainFullUrl()
            r3 = r0[r8]
            java.lang.String r2 = r11.convert2AbsFullPath(r2, r3)
            android.content.Context r3 = r21.getContext()
            r4 = 2
            java.lang.String[] r5 = new java.lang.String[r4]
            r5[r8] = r1
            r4 = 1
            r5[r4] = r2
            boolean r3 = io.dcloud.common.util.FileUtil.checkPathAccord(r3, r5)
            if (r3 != 0) goto L_0x0b14
            r3 = 15
            r9.a((int) r3, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0b14:
            java.io.File r3 = new java.io.File
            r3.<init>(r2)
            boolean r2 = r3.isDirectory()
            java.io.File r4 = new java.io.File
            r4.<init>(r1)
            boolean r5 = r4.exists()
            if (r5 != 0) goto L_0x0b3a
            java.io.File r5 = r4.getParentFile()
            boolean r6 = r5.exists()
            if (r6 != 0) goto L_0x0b35
            r5.mkdirs()
        L_0x0b35:
            boolean r3 = r3.renameTo(r4)
            goto L_0x0b3b
        L_0x0b3a:
            r3 = 0
        L_0x0b3b:
            if (r3 == 0) goto L_0x0b4f
            r3 = 2
            r0 = r0[r3]
            java.lang.String r3 = r11.convert2RelPath(r1)
            org.json.JSONObject r0 = io.dcloud.js.file.a.a(r0, r1, r3, r2)
            int r1 = io.dcloud.common.util.JSUtil.OK
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r1, (boolean) r8)
            goto L_0x0c74
        L_0x0b4f:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0b56:
            java.lang.String[] r0 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)     // Catch:{ Exception -> 0x0bb8 }
            if (r0 != 0) goto L_0x0b5e
            goto L_0x0c74
        L_0x0b5e:
            r0 = r0[r8]     // Catch:{ Exception -> 0x0bb8 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0bb8 }
            r1 = 1
            if (r0 == r1) goto L_0x0ba4
            r1 = 2
            if (r0 == r1) goto L_0x0b96
            r1 = 3
            if (r0 == r1) goto L_0x0b88
            r1 = 4
            if (r0 == r1) goto L_0x0b7a
            java.lang.String r0 = "PUBLIC_DEVICE_ROOT"
            java.lang.String r1 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceRootDir     // Catch:{ Exception -> 0x0bb8 }
            r2 = 5
            org.json.JSONObject r0 = io.dcloud.js.file.a.a((java.lang.String) r0, (int) r2, (java.lang.String) r1, (java.lang.String) r1, (java.lang.String) r1)     // Catch:{ Exception -> 0x0bb8 }
            goto L_0x0bb1
        L_0x0b7a:
            java.lang.String r0 = "PUBLIC_DOWNLOADS"
            java.lang.String r1 = io.dcloud.common.util.BaseInfo.REAL_PUBLIC_DOWNLOADS_DIR     // Catch:{ Exception -> 0x0bb8 }
            java.lang.String r2 = e     // Catch:{ Exception -> 0x0bb8 }
            java.lang.String r3 = "_downloads"
            r4 = 4
            org.json.JSONObject r0 = io.dcloud.js.file.a.a((java.lang.String) r0, (int) r4, (java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ Exception -> 0x0bb8 }
            goto L_0x0bb1
        L_0x0b88:
            java.lang.String r0 = "PUBLIC_DOCUMENTS"
            java.lang.String r1 = io.dcloud.common.util.BaseInfo.REAL_PUBLIC_DOCUMENTS_DIR     // Catch:{ Exception -> 0x0bb8 }
            java.lang.String r2 = d     // Catch:{ Exception -> 0x0bb8 }
            java.lang.String r3 = "_documents"
            r4 = 3
            org.json.JSONObject r0 = io.dcloud.js.file.a.a((java.lang.String) r0, (int) r4, (java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ Exception -> 0x0bb8 }
            goto L_0x0bb1
        L_0x0b96:
            java.lang.String r0 = "PRIVATE_DOCUMENTS"
            java.lang.String r1 = io.dcloud.common.util.BaseInfo.REAL_PRIVATE_DOC_DIR     // Catch:{ Exception -> 0x0bb8 }
            java.lang.String r2 = c     // Catch:{ Exception -> 0x0bb8 }
            java.lang.String r3 = "_doc"
            r4 = 2
            org.json.JSONObject r0 = io.dcloud.js.file.a.a((java.lang.String) r0, (int) r4, (java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ Exception -> 0x0bb8 }
            goto L_0x0bb1
        L_0x0ba4:
            java.lang.String r0 = "PRIVATE_WWW"
            java.lang.String r1 = io.dcloud.common.util.BaseInfo.REAL_PRIVATE_WWW_DIR     // Catch:{ Exception -> 0x0bb8 }
            java.lang.String r2 = a     // Catch:{ Exception -> 0x0bb8 }
            java.lang.String r3 = "_www"
            r4 = 1
            org.json.JSONObject r0 = io.dcloud.js.file.a.a((java.lang.String) r0, (int) r4, (java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ Exception -> 0x0bb8 }
        L_0x0bb1:
            int r1 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x0bb8 }
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r1, (boolean) r8)     // Catch:{ Exception -> 0x0bb8 }
            goto L_0x0c74
        L_0x0bb8:
            r0 = move-exception
            r0.printStackTrace()
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0bc3:
            java.lang.String[] r1 = r9.a((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String[]) r1, (java.lang.String) r10)
            if (r1 != 0) goto L_0x0bcb
            goto L_0x0c74
        L_0x0bcb:
            r2 = 1
            r0 = r1[r2]
            if (r0 == 0) goto L_0x0bee
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r3 = r1[r2]
            r0.append(r3)
            r3 = r1[r2]
            java.lang.String r4 = java.io.File.separator
            boolean r3 = r3.endsWith(r4)
            if (r3 == 0) goto L_0x0be5
            goto L_0x0be6
        L_0x0be5:
            r15 = r4
        L_0x0be6:
            r0.append(r15)
            java.lang.String r0 = r0.toString()
            goto L_0x0bf0
        L_0x0bee:
            r0 = r1[r2]
        L_0x0bf0:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r0)
            r3 = 2
            r0 = r1[r3]
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            r0 = r1[r8]
            android.content.Context r4 = r21.getContext()
            java.lang.String[] r5 = new java.lang.String[r3]
            r5[r8] = r2
            r3 = 1
            r5[r3] = r0
            boolean r3 = io.dcloud.common.util.FileUtil.checkPathAccord(r4, r5)
            if (r3 != 0) goto L_0x0c22
            android.content.Context r3 = r21.getContext()
            boolean r3 = io.dcloud.common.util.FileUtil.isFilePathForPublic(r3, r0)
            if (r3 == 0) goto L_0x0c20
            goto L_0x0c22
        L_0x0c20:
            r3 = 0
            goto L_0x0c23
        L_0x0c22:
            r3 = 1
        L_0x0c23:
            if (r3 != 0) goto L_0x0c2b
            r3 = 15
            r9.a((int) r3, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
            goto L_0x0c74
        L_0x0c2b:
            boolean r3 = r11.checkPrivateDir(r2)
            r4 = 1
            r3 = r3 ^ r4
            if (r3 == 0) goto L_0x0c52
            boolean r3 = io.dcloud.common.adapter.io.DHFile.isExist((java.lang.String) r2)     // Catch:{ IOException -> 0x0c4e }
            if (r3 != 0) goto L_0x0c52
            boolean r3 = io.dcloud.common.adapter.io.DHFile.isExist((java.lang.String) r0)     // Catch:{ IOException -> 0x0c4e }
            if (r3 == 0) goto L_0x0c46
            int r0 = io.dcloud.common.adapter.io.DHFile.copyFile(r0, r2)     // Catch:{ IOException -> 0x0c4e }
            if (r0 != r4) goto L_0x0c52
            goto L_0x0c4c
        L_0x0c46:
            boolean r0 = io.dcloud.common.adapter.io.DHFile.copyAssetsFile(r0, r2)     // Catch:{ IOException -> 0x0c4e }
            if (r0 == 0) goto L_0x0c52
        L_0x0c4c:
            r12 = 1
            goto L_0x0c53
        L_0x0c4e:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0c52:
            r12 = 0
        L_0x0c53:
            if (r12 == 0) goto L_0x0c6f
            r3 = 2
            r0 = r1[r3]
            java.lang.String r1 = r11.convert2RelPath(r2)
            java.io.File r3 = new java.io.File
            r3.<init>(r2)
            boolean r3 = r3.isDirectory()
            org.json.JSONObject r0 = io.dcloud.js.file.a.a(r0, r2, r1, r3)
            int r1 = io.dcloud.common.util.JSUtil.OK
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10, (org.json.JSONObject) r0, (int) r1, (boolean) r8)
            goto L_0x0c74
        L_0x0c6f:
            r1 = 10
            r9.a((int) r1, (io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r10)
        L_0x0c74:
            return r19
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.file.FileFeatureImpl.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public void init(AbsMgr absMgr, String str) {
    }

    private void a(IWebview iWebview, IApp iApp, String[] strArr, String str) {
        String str2 = strArr[0];
        if (!FileUtil.checkPathAccord(iWebview.getContext(), str2)) {
            a(15, iWebview, str);
            return;
        }
        boolean checkPrivateDir = iApp.checkPrivateDir(str2);
        int parseInt = PdrUtil.parseInt(strArr[1], 0);
        int parseInt2 = PdrUtil.parseInt(strArr[2], -1);
        if (!checkPrivateDir || !iApp.isOnAppRunningMode()) {
            AsyncTaskHandler.executeThreadTask(new f(str2, parseInt2, parseInt, iWebview, str));
        } else {
            a(10, iWebview, str);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(11:25|26|(1:28)(2:29|(1:31))|32|(24:34|35|(2:37|38)|40|(3:43|44|45)|(3:47|48|49)|(3:51|52|53)|54|56|57|58|(6:60|61|(1:63)(2:64|(1:66))|67|(6:69|70|71|72|(2:76|136)|77)|135)(2:79|80)|81|83|84|(2:86|(2:88|(1:90)(2:91|(8:93|(2:102|(2:104|(1:106)(1:107))(1:108))(1:109)|110|111|112|(1:114)|115|(3:117|118|119))))(2:94|(8:96|(0)(0)|110|111|112|(0)|115|(0))))(2:97|(8:99|(0)(0)|110|111|112|(0)|115|(0)))|100|(0)(0)|110|111|112|(0)|115|(0))|120|122|123|124|125|143) */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0114, code lost:
        if ("270".equals(r12) != false) goto L_0x0116;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:124:0x0259 */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x0202  */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x0211  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x0224 A[Catch:{ Exception -> 0x0269 }] */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x0235  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(java.lang.String r18, java.lang.String r19, io.dcloud.common.DHInterface.IWebview r20, java.lang.String r21) {
        /*
            r17 = this;
            java.lang.String r0 = "90"
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r18)
            if (r1 != 0) goto L_0x028e
            boolean r1 = io.dcloud.common.util.PdrUtil.isNetPath(r18)
            if (r1 != 0) goto L_0x028e
            io.dcloud.common.DHInterface.IApp r1 = r20.obtainApp()
            java.lang.String r2 = r20.obtainFullUrl()
            r3 = r18
            java.lang.String r1 = r1.convert2AbsFullPath(r2, r3)
            java.lang.String r2 = "apps/"
            boolean r2 = r1.startsWith(r2)
            if (r2 == 0) goto L_0x0035
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "/"
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
        L_0x0035:
            java.lang.String r2 = "/android_asset/"
            boolean r2 = r1.startsWith(r2)
            java.lang.String r3 = ""
            if (r2 == 0) goto L_0x0046
            java.lang.String r2 = "/android_asset"
            java.lang.String r1 = r1.replace(r2, r3)
            goto L_0x0054
        L_0x0046:
            java.lang.String r2 = "android_asset/"
            boolean r2 = r1.startsWith(r2)
            if (r2 == 0) goto L_0x0054
            java.lang.String r2 = "android_asset"
            java.lang.String r1 = r1.replace(r2, r3)
        L_0x0054:
            io.dcloud.common.DHInterface.IApp r2 = r20.obtainApp()
            java.lang.String r1 = r2.checkPrivateDirAndCopy2Temp(r1)
            android.content.Context r2 = r20.getContext()
            r3 = 1
            java.lang.String[] r4 = new java.lang.String[r3]
            r5 = 0
            r4[r5] = r1
            boolean r2 = io.dcloud.common.util.FileUtil.checkPathAccord(r2, r4)
            if (r2 != 0) goto L_0x0079
            android.content.Context r2 = r20.getContext()
            boolean r2 = io.dcloud.common.util.FileUtil.isFilePathForPublic(r2, r1)
            if (r2 == 0) goto L_0x0077
            goto L_0x0079
        L_0x0077:
            r2 = 0
            goto L_0x007a
        L_0x0079:
            r2 = 1
        L_0x007a:
            if (r2 != 0) goto L_0x0088
            r0 = 15
            r2 = r17
            r9 = r19
            r10 = r20
            r2.a((int) r0, (io.dcloud.common.DHInterface.IWebview) r10, (java.lang.String) r9)
            return
        L_0x0088:
            r2 = r17
            r9 = r19
            r10 = r20
            java.io.File r4 = new java.io.File
            r4.<init>(r1)
            boolean r4 = r4.exists()
            r6 = 2
            if (r4 != 0) goto L_0x00bb
            java.lang.Object[] r0 = new java.lang.Object[r6]
            r1 = -4
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r0[r5] = r1
            java.lang.String r1 = io.dcloud.common.constant.DOMException.MSG_FILE_NOT_EXIST
            r0[r3] = r1
            java.lang.String r1 = "{code:%d,message:'%s'}"
            java.lang.String r5 = io.dcloud.common.util.StringUtil.format(r1, r0)
            int r6 = io.dcloud.common.util.JSUtil.ERROR
            r7 = 1
            r8 = 0
            r3 = r20
            r4 = r19
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r3, r4, r5, r6, r7, r8)
            goto L_0x028e
        L_0x00bb:
            android.media.MediaMetadataRetriever r4 = new android.media.MediaMetadataRetriever     // Catch:{ Exception -> 0x0269 }
            r4.<init>()     // Catch:{ Exception -> 0x0269 }
            android.content.Context r7 = r20.getContext()     // Catch:{ Exception -> 0x0269 }
            boolean r7 = io.dcloud.common.util.FileUtil.checkPrivatePath(r7, r1)     // Catch:{ Exception -> 0x0269 }
            if (r7 == 0) goto L_0x00ce
            r4.setDataSource(r1)     // Catch:{ Exception -> 0x0269 }
            goto L_0x00df
        L_0x00ce:
            android.content.Context r7 = r20.getContext()     // Catch:{ Exception -> 0x0269 }
            android.net.Uri r7 = io.dcloud.common.util.FileUtil.getVideoFileUri(r7, r1)     // Catch:{ Exception -> 0x0269 }
            if (r7 == 0) goto L_0x00df
            android.content.Context r8 = r20.getContext()     // Catch:{ Exception -> 0x0269 }
            r4.setDataSource(r8, r7)     // Catch:{ Exception -> 0x0269 }
        L_0x00df:
            r7 = 9
            java.lang.String r7 = r4.extractMetadata(r7)     // Catch:{ Exception -> 0x0269 }
            r8 = 18
            java.lang.String r8 = r4.extractMetadata(r8)     // Catch:{ Exception -> 0x0269 }
            r11 = 19
            java.lang.String r11 = r4.extractMetadata(r11)     // Catch:{ Exception -> 0x0269 }
            r12 = 24
            java.lang.String r12 = r4.extractMetadata(r12)     // Catch:{ Exception -> 0x0269 }
            org.json.JSONObject r13 = new org.json.JSONObject     // Catch:{ Exception -> 0x0269 }
            r13.<init>()     // Catch:{ Exception -> 0x0269 }
            java.lang.String r14 = "getVideoInfo"
            r15 = r21
            boolean r14 = r15.equals(r14)     // Catch:{ Exception -> 0x0269 }
            r15 = 1148846080(0x447a0000, float:1000.0)
            if (r14 == 0) goto L_0x0244
            boolean r14 = r0.equals(r12)     // Catch:{ Exception -> 0x0269 }
            java.lang.String r5 = "270"
            if (r14 != 0) goto L_0x0116
            boolean r14 = r5.equals(r12)     // Catch:{ Exception -> 0x0269 }
            if (r14 == 0) goto L_0x011b
        L_0x0116:
            r16 = r11
            r11 = r8
            r8 = r16
        L_0x011b:
            if (r8 == 0) goto L_0x0138
            if (r11 == 0) goto L_0x0138
            java.lang.String r14 = "resolution"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0269 }
            r6.<init>()     // Catch:{ Exception -> 0x0269 }
            r6.append(r8)     // Catch:{ Exception -> 0x0269 }
            java.lang.String r3 = "*"
            r6.append(r3)     // Catch:{ Exception -> 0x0269 }
            r6.append(r11)     // Catch:{ Exception -> 0x0269 }
            java.lang.String r3 = r6.toString()     // Catch:{ Exception -> 0x0269 }
            r13.put(r14, r3)     // Catch:{ Exception -> 0x0269 }
        L_0x0138:
            if (r8 == 0) goto L_0x0145
            java.lang.String r3 = "width"
            float r6 = java.lang.Float.parseFloat(r8)     // Catch:{ Exception -> 0x0151 }
            double r8 = (double) r6     // Catch:{ Exception -> 0x0151 }
            r13.put(r3, r8)     // Catch:{ Exception -> 0x0151 }
        L_0x0145:
            if (r11 == 0) goto L_0x0151
            java.lang.String r3 = "height"
            float r6 = java.lang.Float.parseFloat(r11)     // Catch:{ Exception -> 0x0151 }
            double r8 = (double) r6     // Catch:{ Exception -> 0x0151 }
            r13.put(r3, r8)     // Catch:{ Exception -> 0x0151 }
        L_0x0151:
            java.lang.String r3 = "size"
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x0269 }
            r6.<init>(r1)     // Catch:{ Exception -> 0x0269 }
            long r8 = r6.length()     // Catch:{ Exception -> 0x0269 }
            r13.put(r3, r8)     // Catch:{ Exception -> 0x0269 }
            r3 = 32
            java.lang.String r3 = r4.extractMetadata(r3)     // Catch:{ Exception -> 0x0269 }
            boolean r6 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0269 }
            java.lang.String r8 = "fps"
            if (r6 == 0) goto L_0x01be
            android.media.MediaExtractor r3 = new android.media.MediaExtractor     // Catch:{ Exception -> 0x0269 }
            r3.<init>()     // Catch:{ Exception -> 0x0269 }
            android.content.Context r6 = r20.getContext()     // Catch:{ Exception -> 0x0269 }
            boolean r6 = io.dcloud.common.util.FileUtil.checkPrivatePath(r6, r1)     // Catch:{ Exception -> 0x0269 }
            if (r6 == 0) goto L_0x0180
            r3.setDataSource(r1)     // Catch:{ Exception -> 0x0269 }
            goto L_0x0192
        L_0x0180:
            android.content.Context r6 = r20.getContext()     // Catch:{ Exception -> 0x0269 }
            android.net.Uri r1 = io.dcloud.common.util.FileUtil.getVideoFileUri(r6, r1)     // Catch:{ Exception -> 0x0269 }
            if (r1 == 0) goto L_0x0192
            android.content.Context r6 = r20.getContext()     // Catch:{ Exception -> 0x0269 }
            r9 = 0
            r3.setDataSource(r6, r1, r9)     // Catch:{ Exception -> 0x0269 }
        L_0x0192:
            int r1 = r3.getTrackCount()     // Catch:{ Exception -> 0x0269 }
            r6 = 0
        L_0x0197:
            if (r6 >= r1) goto L_0x01d1
            android.media.MediaFormat r9 = r3.getTrackFormat(r6)     // Catch:{ Exception -> 0x0269 }
            java.lang.String r11 = "mime"
            java.lang.String r11 = r9.getString(r11)     // Catch:{ Exception -> 0x01bb }
            boolean r14 = android.text.TextUtils.isEmpty(r11)     // Catch:{ Exception -> 0x01bb }
            if (r14 != 0) goto L_0x01bb
            java.lang.String r14 = "video/"
            boolean r11 = r11.startsWith(r14)     // Catch:{ Exception -> 0x01bb }
            if (r11 == 0) goto L_0x01bb
            java.lang.String r11 = "frame-rate"
            int r9 = r9.getInteger(r11)     // Catch:{ Exception -> 0x01bb }
            r13.put(r8, r9)     // Catch:{ Exception -> 0x01bb }
        L_0x01bb:
            int r6 = r6 + 1
            goto L_0x0197
        L_0x01be:
            float r1 = java.lang.Float.parseFloat(r3)     // Catch:{ Exception -> 0x01d1 }
            float r3 = java.lang.Float.parseFloat(r7)     // Catch:{ Exception -> 0x01d1 }
            float r1 = r1 / r3
            float r1 = r1 * r15
            double r1 = (double) r1     // Catch:{ Exception -> 0x01d1 }
            double r1 = java.lang.Math.ceil(r1)     // Catch:{ Exception -> 0x01d1 }
            r13.put(r8, r1)     // Catch:{ Exception -> 0x01d1 }
        L_0x01d1:
            r1 = -1
            int r2 = r12.hashCode()     // Catch:{ Exception -> 0x0269 }
            r3 = 1815(0x717, float:2.543E-42)
            if (r2 == r3) goto L_0x01f7
            r0 = 48873(0xbee9, float:6.8486E-41)
            if (r2 == r0) goto L_0x01ed
            r0 = 49803(0xc28b, float:6.9789E-41)
            if (r2 == r0) goto L_0x01e5
            goto L_0x01ff
        L_0x01e5:
            boolean r0 = r12.equals(r5)     // Catch:{ Exception -> 0x0269 }
            if (r0 == 0) goto L_0x01ff
            r5 = 1
            goto L_0x0200
        L_0x01ed:
            java.lang.String r0 = "180"
            boolean r0 = r12.equals(r0)     // Catch:{ Exception -> 0x0269 }
            if (r0 == 0) goto L_0x01ff
            r5 = 2
            goto L_0x0200
        L_0x01f7:
            boolean r0 = r12.equals(r0)     // Catch:{ Exception -> 0x0269 }
            if (r0 == 0) goto L_0x01ff
            r5 = 0
            goto L_0x0200
        L_0x01ff:
            r5 = -1
        L_0x0200:
            if (r5 == 0) goto L_0x0211
            r0 = 1
            if (r5 == r0) goto L_0x020e
            r0 = 2
            if (r5 == r0) goto L_0x020b
            java.lang.String r0 = "up"
            goto L_0x0213
        L_0x020b:
            java.lang.String r0 = "down"
            goto L_0x0213
        L_0x020e:
            java.lang.String r0 = "left"
            goto L_0x0213
        L_0x0211:
            java.lang.String r0 = "right"
        L_0x0213:
            java.lang.String r1 = "orientation"
            r13.put(r1, r0)     // Catch:{ Exception -> 0x0269 }
            r0 = 12
            java.lang.String r0 = r4.extractMetadata(r0)     // Catch:{ Exception -> 0x0269 }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0269 }
            if (r1 != 0) goto L_0x0229
            java.lang.String r1 = "type"
            r13.put(r1, r0)     // Catch:{ Exception -> 0x0269 }
        L_0x0229:
            r0 = 20
            java.lang.String r0 = r4.extractMetadata(r0)     // Catch:{ Exception -> 0x0269 }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0269 }
            if (r1 != 0) goto L_0x0244
            java.lang.String r1 = "bitrate"
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0244 }
            int r0 = r0 / 1000
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Exception -> 0x0244 }
            r13.put(r1, r0)     // Catch:{ Exception -> 0x0244 }
        L_0x0244:
            java.lang.String r0 = "duration"
            float r1 = java.lang.Float.parseFloat(r7)     // Catch:{ Exception -> 0x0259 }
            float r1 = r1 / r15
            double r1 = (double) r1     // Catch:{ Exception -> 0x0259 }
            java.math.BigDecimal r1 = java.math.BigDecimal.valueOf(r1)     // Catch:{ Exception -> 0x0259 }
            r2 = 4
            r3 = 2
            java.math.BigDecimal r1 = r1.setScale(r3, r2)     // Catch:{ Exception -> 0x0259 }
            r13.put(r0, r1)     // Catch:{ Exception -> 0x0259 }
        L_0x0259:
            java.lang.String r5 = r13.toString()     // Catch:{ Exception -> 0x0269 }
            int r6 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x0269 }
            r7 = 1
            r8 = 0
            r3 = r20
            r4 = r19
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r3, r4, r5, r6, r7, r8)     // Catch:{ Exception -> 0x0269 }
            goto L_0x028e
        L_0x0269:
            r0 = move-exception
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>()
            java.lang.String r2 = "code"
            r3 = 13
            r1.put(r2, r3)     // Catch:{ JSONException -> 0x027f }
            java.lang.String r2 = "message"
            java.lang.String r0 = r0.getMessage()     // Catch:{ JSONException -> 0x027f }
            r1.put(r2, r0)     // Catch:{ JSONException -> 0x027f }
        L_0x027f:
            java.lang.String r5 = r1.toString()
            int r6 = io.dcloud.common.util.JSUtil.ERROR
            r7 = 1
            r8 = 0
            r3 = r20
            r4 = r19
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r3, r4, r5, r6, r7, r8)
        L_0x028e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.file.FileFeatureImpl.a(java.lang.String, java.lang.String, io.dcloud.common.DHInterface.IWebview, java.lang.String):void");
    }

    public static String b(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        if (fileExtensionFromUrl != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
        }
        return null;
    }

    private JSONObject a(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (str.startsWith(a)) {
            jSONObject.put("type", 1);
            jSONObject.put("fsName", "PRIVATE_WWW");
            jSONObject.put("fsRoot", a.a("PRIVATE_WWW", a, c(str), true));
        } else if (str.startsWith(c)) {
            jSONObject.put("type", 2);
            jSONObject.put("fsName", "PRIVATE_DOCUMENTS");
            jSONObject.put("fsRoot", a.a("PRIVATE_DOCUMENTS", c, c(str), true));
        } else if (str.startsWith(d)) {
            jSONObject.put("type", 3);
            jSONObject.put("fsName", "PUBLIC_DOCUMENTS");
            jSONObject.put("fsRoot", a.a("PUBLIC_DOCUMENTS", d, c(str), true));
        } else if (str.startsWith(e)) {
            jSONObject.put("type", 4);
            jSONObject.put("fsName", "PUBLIC_DOWNLOADS");
            jSONObject.put("fsRoot", a.a("PUBLIC_DOWNLOADS", e, c(str), true));
        } else if (str.startsWith(b)) {
            jSONObject.put("type", 1);
            jSONObject.put("fsName", "PRIVATE_WWW");
            jSONObject.put("fsRoot", a.a("PRIVATE_WWW", b, c(str), true));
        } else if (PdrUtil.isDeviceRootDir(str)) {
            jSONObject.put("type", 5);
            jSONObject.put("fsName", "PUBLIC_DEVICE_ROOT");
            jSONObject.put("fsRoot", a.a("PUBLIC_DEVICE_ROOT", DeviceInfo.sDeviceRootDir, c(str), true));
        }
        return jSONObject;
    }

    /* access modifiers changed from: private */
    public void a(int i, IWebview iWebview, String str) {
        Deprecated_JSUtil.execCallback(iWebview, str, a(iWebview.getContext(), i), JSUtil.ERROR, true, false);
    }

    private String a(Context context, int i) {
        switch (i) {
            case 1:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_file_not_found));
            case 2:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_without_authorization));
            case 3:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_common_cancel));
            case 4:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_file_not_read));
            case 5:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_coding_error));
            case 6:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_no_modification_allowed));
            case 7:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_invalid_state));
            case 8:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_grammar_mistakes));
            case 9:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_invalid_modification));
            case 10:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_perform_error));
            case 11:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_type_mismatch));
            case 12:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_path_exists));
            case 14:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_path_not_exist));
            case 15:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), DOMException.MSG_PATH_NOT_PRIVATE_ERROR);
            default:
                return StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), context.getString(R.string.dcloud_io_unknown_error));
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006d A[Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x011e A[Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x011f A[Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(java.lang.String r15, io.dcloud.common.DHInterface.IWebview r16, java.lang.String r17) {
        /*
            r14 = this;
            r0 = r15
            java.lang.String r1 = "/"
            java.lang.String r2 = "{code:%d,message:'%s'}"
            r3 = 13
            r4 = 0
            r5 = 2
            r6 = 1
            android.graphics.BitmapFactory$Options r7 = new android.graphics.BitmapFactory$Options     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r7.<init>()     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r7.inJustDecodeBounds = r6     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            android.content.Context r8 = r16.getContext()     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            boolean r8 = io.dcloud.common.util.FileUtil.checkPrivatePath(r8, r15)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r9 = "Orientation"
            r10 = 0
            if (r8 != 0) goto L_0x0041
            int r8 = android.os.Build.VERSION.SDK_INT     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r11 = 24
            if (r8 < r11) goto L_0x0041
            android.content.Context r8 = r16.getContext()     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.io.InputStream r8 = io.dcloud.common.util.FileUtil.getFileInputStream((android.content.Context) r8, (java.lang.String) r15)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r8 == 0) goto L_0x003f
            android.graphics.BitmapFactory.decodeStream(r8, r10, r7)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            android.media.ExifInterface r11 = new android.media.ExifInterface     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r11.<init>(r8)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r9 = r11.getAttribute(r9)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r8.close()     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            goto L_0x004d
        L_0x003f:
            r9 = r10
            goto L_0x004d
        L_0x0041:
            android.media.ExifInterface r8 = new android.media.ExifInterface     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r8.<init>(r15)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r9 = r8.getAttribute(r9)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            android.graphics.BitmapFactory.decodeFile(r15, r7)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
        L_0x004d:
            int r8 = r7.outWidth     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            int r11 = r7.outHeight     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r7 = r7.outMimeType     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            boolean r12 = io.dcloud.common.util.PdrUtil.isEmpty(r7)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r12 != 0) goto L_0x0067
            boolean r12 = r7.contains(r1)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r12 == 0) goto L_0x0067
            java.lang.String[] r1 = r7.split(r1)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            int r7 = r1.length     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            int r7 = r7 - r6
            r7 = r1[r7]     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
        L_0x0067:
            boolean r1 = android.text.TextUtils.isEmpty(r9)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r1 != 0) goto L_0x00eb
            r1 = -1
            int r10 = r9.hashCode()     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            switch(r10) {
                case 48: goto L_0x00c8;
                case 49: goto L_0x00be;
                case 50: goto L_0x00b4;
                case 51: goto L_0x00aa;
                case 52: goto L_0x00a0;
                case 53: goto L_0x0096;
                case 54: goto L_0x008c;
                case 55: goto L_0x0081;
                case 56: goto L_0x0076;
                default: goto L_0x0075;
            }     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
        L_0x0075:
            goto L_0x00d1
        L_0x0076:
            java.lang.String r10 = "8"
            boolean r9 = r9.equals(r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r9 == 0) goto L_0x00d1
            r1 = 9
            goto L_0x00d1
        L_0x0081:
            java.lang.String r10 = "7"
            boolean r9 = r9.equals(r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r9 == 0) goto L_0x00d1
            r1 = 8
            goto L_0x00d1
        L_0x008c:
            java.lang.String r10 = "6"
            boolean r9 = r9.equals(r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r9 == 0) goto L_0x00d1
            r1 = 7
            goto L_0x00d1
        L_0x0096:
            java.lang.String r10 = "5"
            boolean r9 = r9.equals(r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r9 == 0) goto L_0x00d1
            r1 = 6
            goto L_0x00d1
        L_0x00a0:
            java.lang.String r10 = "4"
            boolean r9 = r9.equals(r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r9 == 0) goto L_0x00d1
            r1 = 5
            goto L_0x00d1
        L_0x00aa:
            java.lang.String r10 = "3"
            boolean r9 = r9.equals(r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r9 == 0) goto L_0x00d1
            r1 = 4
            goto L_0x00d1
        L_0x00b4:
            java.lang.String r10 = "2"
            boolean r9 = r9.equals(r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r9 == 0) goto L_0x00d1
            r1 = 3
            goto L_0x00d1
        L_0x00be:
            java.lang.String r10 = "1"
            boolean r9 = r9.equals(r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r9 == 0) goto L_0x00d1
            r1 = 2
            goto L_0x00d1
        L_0x00c8:
            java.lang.String r10 = "0"
            boolean r9 = r9.equals(r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r9 == 0) goto L_0x00d1
            r1 = 1
        L_0x00d1:
            switch(r1) {
                case 3: goto L_0x00e9;
                case 4: goto L_0x00e6;
                case 5: goto L_0x00e3;
                case 6: goto L_0x00e0;
                case 7: goto L_0x00dd;
                case 8: goto L_0x00da;
                case 9: goto L_0x00d7;
                default: goto L_0x00d4;
            }
        L_0x00d4:
            java.lang.String r10 = "up"
            goto L_0x00eb
        L_0x00d7:
            java.lang.String r10 = "left"
            goto L_0x00eb
        L_0x00da:
            java.lang.String r10 = "right-mirrored"
            goto L_0x00eb
        L_0x00dd:
            java.lang.String r10 = "right"
            goto L_0x00eb
        L_0x00e0:
            java.lang.String r10 = "left-mirrored"
            goto L_0x00eb
        L_0x00e3:
            java.lang.String r10 = "down-mirrored"
            goto L_0x00eb
        L_0x00e6:
            java.lang.String r10 = "down"
            goto L_0x00eb
        L_0x00e9:
            java.lang.String r10 = "up-mirrored"
        L_0x00eb:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r1.<init>()     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r9 = "path"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r12.<init>()     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r13 = "file://"
            r12.append(r13)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r12.append(r15)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r0 = r12.toString()     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r1.put(r9, r0)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r0 = "width"
            r1.put(r0, r8)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r0 = "height"
            r1.put(r0, r11)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r0 = "orientation"
            r1.put(r0, r10)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r0 = "type"
            boolean r8 = io.dcloud.common.util.PdrUtil.isEmpty(r7)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            if (r8 == 0) goto L_0x011f
            goto L_0x0125
        L_0x011f:
            java.util.Locale r8 = java.util.Locale.ENGLISH     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            java.lang.String r7 = r7.toLowerCase(r8)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
        L_0x0125:
            r1.put(r0, r7)     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            int r0 = io.dcloud.common.util.JSUtil.OK     // Catch:{ IOException -> 0x0159, JSONException -> 0x0136 }
            r7 = r16
            r8 = r17
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r8, (org.json.JSONObject) r1, (int) r0, (boolean) r4)     // Catch:{ IOException -> 0x0134, JSONException -> 0x0132 }
            goto L_0x017b
        L_0x0132:
            r0 = move-exception
            goto L_0x013b
        L_0x0134:
            r0 = move-exception
            goto L_0x015e
        L_0x0136:
            r0 = move-exception
            r7 = r16
            r8 = r17
        L_0x013b:
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r1[r4] = r3
            java.lang.String r0 = r0.getMessage()
            r1[r6] = r0
            java.lang.String r9 = io.dcloud.common.util.StringUtil.format(r2, r1)
            int r10 = io.dcloud.common.util.JSUtil.ERROR
            r11 = 1
            r12 = 0
            r7 = r16
            r8 = r17
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r7, r8, r9, r10, r11, r12)
            goto L_0x017b
        L_0x0159:
            r0 = move-exception
            r7 = r16
            r8 = r17
        L_0x015e:
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r1[r4] = r3
            java.lang.String r0 = r0.getMessage()
            r1[r6] = r0
            java.lang.String r9 = io.dcloud.common.util.StringUtil.format(r2, r1)
            int r10 = io.dcloud.common.util.JSUtil.ERROR
            r11 = 1
            r12 = 0
            r7 = r16
            r8 = r17
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r7, r8, r9, r10, r11, r12)
        L_0x017b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.file.FileFeatureImpl.a(java.lang.String, io.dcloud.common.DHInterface.IWebview, java.lang.String):void");
    }

    private String[] a(IWebview iWebview, String[] strArr, String str) {
        if (strArr.length > 1 && !PdrUtil.isEmpty(strArr[1])) {
            try {
                return JSUtil.jsonArrayToStringArr(new JSONArray(strArr[1]));
            } catch (JSONException e2) {
                e2.printStackTrace();
                a(8, iWebview, str);
            }
        }
        return null;
    }
}
