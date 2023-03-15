package io.dcloud.feature.pdr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.TextUtils;
import com.taobao.weex.common.Constants;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ThreadPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;

public class a {

    /* renamed from: io.dcloud.feature.pdr.a$a  reason: collision with other inner class name */
    class C0041a implements Runnable {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String[] b;

        C0041a(IWebview iWebview, String[] strArr) {
            this.a = iWebview;
            this.b = strArr;
        }

        public void run() {
            a.a(this.a, this.b);
        }
    }

    static class b {
        float a;
        float b;
        float c;
        float d;
        float e;
        float f;

        public b(String str, String str2, String str3, String str4, float f2, float f3) {
            this.e = f2;
            this.f = f3;
            this.a = a.a(str, f3, 0.0f);
            float a2 = a.a(str2, this.e, 0.0f);
            this.b = a2;
            float f4 = this.e;
            this.c = a.a(str3, f4, f4 - a2);
            float f5 = this.f;
            float a3 = a.a(str4, f5, f5 - this.a);
            this.d = a3;
            float f6 = this.c;
            float f7 = this.b;
            float f8 = this.e;
            if (f6 + f7 > f8) {
                this.c = f8 - f7;
            }
            float f9 = this.a;
            float f10 = this.f;
            if (a3 + f9 > f10) {
                this.d = f10 - f9;
            }
        }

        public boolean a() {
            return this.a <= this.f && this.b <= this.e;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:25|26|(2:27|28)) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        io.dcloud.common.adapter.util.Logger.d("CompressImage", "获取bitmap 内存溢出第一次  bitmap路径" + r3.a);
        r7.inSampleSize = r7.inSampleSize * 2;
        io.dcloud.common.adapter.util.Logger.d("CompressImage", "获取bitmap 内存溢出第一次 第二次获取bitmmap  bitmap路径" + r3.a);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r9 = a(r20.getContext(), r3.a, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00a4, code lost:
        r9 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0209, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0061 */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x01a1 A[Catch:{ JSONException -> 0x020a }] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x01e8 A[Catch:{ JSONException -> 0x020a }] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0205 A[Catch:{ JSONException -> 0x020a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void a(io.dcloud.common.DHInterface.IWebview r20, java.lang.String[] r21) {
        /*
            r1 = r20
            java.lang.Class<io.dcloud.feature.pdr.a> r2 = io.dcloud.feature.pdr.a.class
            monitor-enter(r2)
            r0 = 0
            r3 = r21[r0]     // Catch:{ all -> 0x0215 }
            r4 = 1
            r5 = r21[r4]     // Catch:{ all -> 0x0215 }
            r6 = -1
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ JSONException -> 0x020a }
            r7.<init>(r3)     // Catch:{ JSONException -> 0x020a }
            io.dcloud.feature.pdr.a$c r3 = new io.dcloud.feature.pdr.a$c     // Catch:{ all -> 0x0215 }
            r3.<init>()     // Catch:{ all -> 0x0215 }
            boolean r7 = r3.a((org.json.JSONObject) r7, (io.dcloud.common.DHInterface.IWebview) r1, (java.lang.String) r5)     // Catch:{ all -> 0x0215 }
            if (r7 != 0) goto L_0x001e
            monitor-exit(r2)
            return
        L_0x001e:
            android.graphics.BitmapFactory$Options r7 = new android.graphics.BitmapFactory$Options     // Catch:{ all -> 0x0215 }
            r7.<init>()     // Catch:{ all -> 0x0215 }
            r7.inJustDecodeBounds = r0     // Catch:{ all -> 0x0215 }
            int r8 = r3.e     // Catch:{ all -> 0x0215 }
            if (r8 >= 0) goto L_0x002d
            r8 = 50
            r3.e = r8     // Catch:{ all -> 0x0215 }
        L_0x002d:
            long r8 = r3.m     // Catch:{ all -> 0x0215 }
            r10 = 1500000(0x16e360, double:7.410985E-318)
            r12 = 2
            int r13 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r13 <= 0) goto L_0x003a
            r7.inSampleSize = r12     // Catch:{ all -> 0x0215 }
            goto L_0x003c
        L_0x003a:
            r7.inSampleSize = r4     // Catch:{ all -> 0x0215 }
        L_0x003c:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0215 }
            r8.<init>()     // Catch:{ all -> 0x0215 }
            java.lang.String r9 = "文件获取完毕 初始化bitmap 获取文件大小"
            r8.append(r9)     // Catch:{ all -> 0x0215 }
            long r9 = r3.m     // Catch:{ all -> 0x0215 }
            r8.append(r9)     // Catch:{ all -> 0x0215 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0215 }
            java.lang.String r9 = "CompressImage"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r9, (java.lang.String) r8)     // Catch:{ all -> 0x0215 }
            r8 = 0
            android.content.Context r9 = r20.getContext()     // Catch:{ OutOfMemoryError -> 0x0061 }
            java.lang.String r10 = r3.a     // Catch:{ OutOfMemoryError -> 0x0061 }
            android.graphics.Bitmap r9 = a((android.content.Context) r9, (java.lang.String) r10, (android.graphics.BitmapFactory.Options) r7)     // Catch:{ OutOfMemoryError -> 0x0061 }
            goto L_0x00a5
        L_0x0061:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x0215 }
            r9.<init>()     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = "获取bitmap 内存溢出第一次  bitmap路径"
            r9.append(r10)     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = r3.a     // Catch:{ all -> 0x0215 }
            r9.append(r10)     // Catch:{ all -> 0x0215 }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = "CompressImage"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r10, (java.lang.String) r9)     // Catch:{ all -> 0x0215 }
            int r9 = r7.inSampleSize     // Catch:{ all -> 0x0215 }
            int r9 = r9 * 2
            r7.inSampleSize = r9     // Catch:{ all -> 0x0215 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x0215 }
            r9.<init>()     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = "获取bitmap 内存溢出第一次 第二次获取bitmmap  bitmap路径"
            r9.append(r10)     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = r3.a     // Catch:{ all -> 0x0215 }
            r9.append(r10)     // Catch:{ all -> 0x0215 }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = "CompressImage"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r10, (java.lang.String) r9)     // Catch:{ all -> 0x0215 }
            android.content.Context r9 = r20.getContext()     // Catch:{ IOException -> 0x00a4 }
            java.lang.String r10 = r3.a     // Catch:{ IOException -> 0x00a4 }
            android.graphics.Bitmap r9 = a((android.content.Context) r9, (java.lang.String) r10, (android.graphics.BitmapFactory.Options) r7)     // Catch:{ IOException -> 0x00a4 }
            goto L_0x00a5
        L_0x00a4:
            r9 = r8
        L_0x00a5:
            if (r9 != 0) goto L_0x00ae
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_PARAMETER_ERROR     // Catch:{ all -> 0x0215 }
            a((io.dcloud.common.DHInterface.IWebview) r1, (java.lang.String) r5, (java.lang.String) r0, (int) r6)     // Catch:{ all -> 0x0215 }
            monitor-exit(r2)
            return
        L_0x00ae:
            boolean r6 = r3.l     // Catch:{ all -> 0x0215 }
            if (r6 == 0) goto L_0x00eb
            float r6 = r3.f     // Catch:{ all -> 0x0215 }
            r10 = 0
            int r6 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r6 <= 0) goto L_0x00eb
            float r6 = r3.g     // Catch:{ all -> 0x0215 }
            int r6 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r6 <= 0) goto L_0x00eb
            android.graphics.Matrix r6 = new android.graphics.Matrix     // Catch:{ all -> 0x0215 }
            r6.<init>()     // Catch:{ all -> 0x0215 }
            float r10 = r3.f     // Catch:{ all -> 0x0215 }
            int r11 = r9.getWidth()     // Catch:{ all -> 0x0215 }
            float r11 = (float) r11     // Catch:{ all -> 0x0215 }
            float r10 = r10 / r11
            float r11 = r3.g     // Catch:{ all -> 0x0215 }
            int r13 = r9.getHeight()     // Catch:{ all -> 0x0215 }
            float r13 = (float) r13     // Catch:{ all -> 0x0215 }
            float r11 = r11 / r13
            r6.setScale(r10, r11)     // Catch:{ all -> 0x0215 }
            int r16 = r9.getWidth()     // Catch:{ all -> 0x0215 }
            int r17 = r9.getHeight()     // Catch:{ all -> 0x0215 }
            r14 = 0
            r15 = 0
            r19 = 0
            r13 = r9
            r18 = r6
            android.graphics.Bitmap r6 = android.graphics.Bitmap.createBitmap(r13, r14, r15, r16, r17, r18, r19)     // Catch:{ all -> 0x0215 }
            goto L_0x00ec
        L_0x00eb:
            r6 = r9
        L_0x00ec:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0215 }
            r10.<init>()     // Catch:{ all -> 0x0215 }
            java.lang.String r11 = "缩放完毕"
            r10.append(r11)     // Catch:{ all -> 0x0215 }
            java.lang.String r11 = r3.a     // Catch:{ all -> 0x0215 }
            r10.append(r11)     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0215 }
            java.lang.String r11 = "CompressImage"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r11, (java.lang.String) r10)     // Catch:{ all -> 0x0215 }
            int r10 = r3.h     // Catch:{ all -> 0x0215 }
            if (r10 <= 0) goto L_0x0114
            android.graphics.Matrix r8 = new android.graphics.Matrix     // Catch:{ all -> 0x0215 }
            r8.<init>()     // Catch:{ all -> 0x0215 }
            int r10 = r3.h     // Catch:{ all -> 0x0215 }
            float r10 = (float) r10     // Catch:{ all -> 0x0215 }
            r8.postRotate(r10)     // Catch:{ all -> 0x0215 }
        L_0x0114:
            r18 = r8
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0215 }
            r8.<init>()     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = "旋转完毕"
            r8.append(r10)     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = r3.a     // Catch:{ all -> 0x0215 }
            r8.append(r10)     // Catch:{ all -> 0x0215 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = "CompressImage"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r10, (java.lang.String) r8)     // Catch:{ all -> 0x0215 }
            io.dcloud.feature.pdr.a$b r8 = r3.k     // Catch:{ all -> 0x0215 }
            if (r8 == 0) goto L_0x0157
            float r10 = r8.a     // Catch:{ all -> 0x0215 }
            int r7 = r7.inSampleSize     // Catch:{ all -> 0x0215 }
            float r7 = (float) r7     // Catch:{ all -> 0x0215 }
            float r10 = r10 / r7
            int r15 = (int) r10     // Catch:{ all -> 0x0215 }
            float r10 = r8.b     // Catch:{ all -> 0x0215 }
            float r10 = r10 / r7
            int r14 = (int) r10     // Catch:{ all -> 0x0215 }
            float r10 = r8.c     // Catch:{ all -> 0x0215 }
            float r10 = r10 / r7
            int r10 = (int) r10     // Catch:{ all -> 0x0215 }
            float r8 = r8.d     // Catch:{ all -> 0x0215 }
            float r8 = r8 / r7
            int r7 = (int) r8     // Catch:{ all -> 0x0215 }
            r19 = 0
            r13 = r6
            r16 = r10
            r17 = r7
            android.graphics.Bitmap r7 = android.graphics.Bitmap.createBitmap(r13, r14, r15, r16, r17, r18, r19)     // Catch:{ all -> 0x0215 }
            if (r7 == r6) goto L_0x016f
            r6.recycle()     // Catch:{ all -> 0x0215 }
            goto L_0x016f
        L_0x0157:
            if (r18 == 0) goto L_0x0170
            int r16 = r6.getWidth()     // Catch:{ all -> 0x0215 }
            int r17 = r6.getHeight()     // Catch:{ all -> 0x0215 }
            r14 = 0
            r15 = 0
            r19 = 0
            r13 = r6
            android.graphics.Bitmap r7 = android.graphics.Bitmap.createBitmap(r13, r14, r15, r16, r17, r18, r19)     // Catch:{ all -> 0x0215 }
            if (r7 == r6) goto L_0x016f
            r6.recycle()     // Catch:{ all -> 0x0215 }
        L_0x016f:
            r6 = r7
        L_0x0170:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0215 }
            r7.<init>()     // Catch:{ all -> 0x0215 }
            java.lang.String r8 = "裁剪完毕"
            r7.append(r8)     // Catch:{ all -> 0x0215 }
            java.lang.String r8 = r3.a     // Catch:{ all -> 0x0215 }
            r7.append(r8)     // Catch:{ all -> 0x0215 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0215 }
            java.lang.String r8 = "CompressImage"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r8, (java.lang.String) r7)     // Catch:{ all -> 0x0215 }
            int r7 = r6.getWidth()     // Catch:{ all -> 0x0215 }
            int r8 = r6.getHeight()     // Catch:{ all -> 0x0215 }
            java.lang.String r10 = r3.b     // Catch:{ all -> 0x0215 }
            boolean r11 = r3.d     // Catch:{ all -> 0x0215 }
            int r13 = r3.e     // Catch:{ all -> 0x0215 }
            long r10 = a((java.lang.String) r10, (android.graphics.Bitmap) r6, (boolean) r11, (int) r13)     // Catch:{ all -> 0x0215 }
            r13 = 0
            int r15 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r15 <= 0) goto L_0x01e8
            java.lang.String r6 = r3.a     // Catch:{ all -> 0x0215 }
            java.lang.String r13 = r3.b     // Catch:{ all -> 0x0215 }
            a((java.lang.String) r6, (java.lang.String) r13)     // Catch:{ all -> 0x0215 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0215 }
            r6.<init>()     // Catch:{ all -> 0x0215 }
            java.lang.String r13 = "开始保存图片"
            r6.append(r13)     // Catch:{ all -> 0x0215 }
            java.lang.String r13 = r3.a     // Catch:{ all -> 0x0215 }
            r6.append(r13)     // Catch:{ all -> 0x0215 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0215 }
            java.lang.String r13 = "CompressImage"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r13, (java.lang.String) r6)     // Catch:{ all -> 0x0215 }
            java.util.Locale r6 = java.util.Locale.ENGLISH     // Catch:{ all -> 0x0215 }
            r13 = 4
            java.lang.Object[] r13 = new java.lang.Object[r13]     // Catch:{ all -> 0x0215 }
            java.lang.String r3 = r3.b     // Catch:{ all -> 0x0215 }
            r13[r0] = r3     // Catch:{ all -> 0x0215 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r7)     // Catch:{ all -> 0x0215 }
            r13[r4] = r0     // Catch:{ all -> 0x0215 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x0215 }
            r13[r12] = r0     // Catch:{ all -> 0x0215 }
            java.lang.Long r0 = java.lang.Long.valueOf(r10)     // Catch:{ all -> 0x0215 }
            r3 = 3
            r13[r3] = r0     // Catch:{ all -> 0x0215 }
            java.lang.String r0 = "{path:'file://%s', w:%d, h:%d, size:%d}"
            java.lang.String r0 = java.lang.String.format(r6, r0, r13)     // Catch:{ all -> 0x0215 }
            a((io.dcloud.common.DHInterface.IWebview) r1, (java.lang.String) r5, (java.lang.String) r0)     // Catch:{ all -> 0x0215 }
            goto L_0x01ff
        L_0x01e8:
            boolean r0 = r6.isRecycled()     // Catch:{ all -> 0x0215 }
            if (r0 != 0) goto L_0x01f1
            r6.recycle()     // Catch:{ all -> 0x0215 }
        L_0x01f1:
            android.content.Context r0 = r20.getContext()     // Catch:{ all -> 0x0215 }
            int r3 = io.dcloud.base.R.string.dcloud_common_zip_image_output_failed     // Catch:{ all -> 0x0215 }
            java.lang.String r0 = r0.getString(r3)     // Catch:{ all -> 0x0215 }
            r3 = -5
            a((io.dcloud.common.DHInterface.IWebview) r1, (java.lang.String) r5, (java.lang.String) r0, (int) r3)     // Catch:{ all -> 0x0215 }
        L_0x01ff:
            boolean r0 = r9.isRecycled()     // Catch:{ all -> 0x0215 }
            if (r0 != 0) goto L_0x0208
            r9.recycle()     // Catch:{ all -> 0x0215 }
        L_0x0208:
            monitor-exit(r2)
            return
        L_0x020a:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0215 }
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_PARAMETER_ERROR     // Catch:{ all -> 0x0215 }
            a((io.dcloud.common.DHInterface.IWebview) r1, (java.lang.String) r5, (java.lang.String) r0, (int) r6)     // Catch:{ all -> 0x0215 }
            monitor-exit(r2)
            return
        L_0x0215:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.pdr.a.a(io.dcloud.common.DHInterface.IWebview, java.lang.String[]):void");
    }

    public static void b(IWebview iWebview, String[] strArr) {
        ThreadPool.self().addThreadTask(new C0041a(iWebview, strArr));
    }

    public static Bitmap.CompressFormat c(String str) {
        if (str.contains(".jpg") || str.contains(".jpeg")) {
            return Bitmap.CompressFormat.JPEG;
        }
        return Bitmap.CompressFormat.PNG;
    }

    public static boolean b(String str) {
        try {
            File file = new File(str);
            if (!file.exists() || file.length() < 5) {
                return false;
            }
            return true;
        } catch (Exception unused) {
        }
    }

    static class c {
        String a;
        String b;
        String c;
        boolean d;
        int e;
        float f;
        float g;
        int h;
        int i;
        int j;
        b k;
        boolean l = false;
        long m;

        c() {
        }

        public boolean a(JSONObject jSONObject, IWebview iWebview, String str) {
            JSONObject jSONObject2 = jSONObject;
            IWebview iWebview2 = iWebview;
            String str2 = str;
            this.a = jSONObject2.optString("src");
            this.b = jSONObject2.optString("dst");
            if (!a(iWebview2, str2)) {
                return false;
            }
            this.d = jSONObject2.optBoolean("overwrite", false);
            this.c = jSONObject2.optString(AbsoluteConst.JSON_KEY_FORMAT);
            this.e = jSONObject2.optInt(Constants.Name.QUALITY, -1);
            a(iWebview.getContext(), jSONObject2.optString("width", "auto"), jSONObject2.optString("height", "auto"));
            this.h = jSONObject2.optInt("rotate", -1);
            JSONObject optJSONObject = jSONObject2.optJSONObject("clip");
            if (optJSONObject == null) {
                return true;
            }
            b bVar = new b(optJSONObject.optString("top"), optJSONObject.optString("left"), optJSONObject.optString("width"), optJSONObject.optString("height"), this.f, this.g);
            this.k = bVar;
            if (bVar.a()) {
                return true;
            }
            a.a(iWebview2, str2, DOMException.MSG_PARAMETER_ERROR, -1);
            return false;
        }

        public boolean a(IWebview iWebview, String str) {
            if (TextUtils.isEmpty(this.a) || TextUtils.isEmpty(this.b)) {
                a.a(iWebview, str, DOMException.MSG_PARAMETER_ERROR, -1);
                return false;
            }
            String convert2AbsFullPath = iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), this.a);
            this.a = convert2AbsFullPath;
            if (!a.b(convert2AbsFullPath)) {
                a.a(iWebview, str, DOMException.MSG_FILE_NOT_EXIST, -4);
                return false;
            }
            this.b = iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), this.b);
            return true;
        }

        public void a(Context context, String str, String str2) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try {
                a.a(context, this.a, options);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            int i2 = options.outWidth;
            this.i = i2;
            int i3 = options.outHeight;
            this.j = i3;
            if (i3 > 0 && i2 > 0) {
                this.m = new File(this.a).length();
                if (str.equals("auto") && str2.endsWith("auto")) {
                    this.l = false;
                    this.f = (float) this.i;
                    this.g = (float) this.j;
                } else if (str.equals("auto")) {
                    this.l = true;
                    float f2 = (float) this.j;
                    float parseFloat = PdrUtil.parseFloat(str2, f2, f2);
                    this.g = parseFloat;
                    this.f = (((float) this.i) * parseFloat) / ((float) this.j);
                } else if (str2.equals("auto")) {
                    this.l = true;
                    float f3 = (float) this.i;
                    float parseFloat2 = PdrUtil.parseFloat(str, f3, f3);
                    this.f = parseFloat2;
                    this.g = (((float) this.j) * parseFloat2) / ((float) this.i);
                } else {
                    this.l = true;
                    float f4 = (float) this.i;
                    this.f = PdrUtil.parseFloat(str, f4, f4);
                    float f5 = (float) this.j;
                    this.g = PdrUtil.parseFloat(str2, f5, f5);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002e, code lost:
        r4 = r4.toString();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(java.lang.String r7, java.lang.String r8) {
        /*
            android.media.ExifInterface r0 = new android.media.ExifInterface     // Catch:{ Exception -> 0x0041 }
            r0.<init>(r7)     // Catch:{ Exception -> 0x0041 }
            android.media.ExifInterface r7 = new android.media.ExifInterface     // Catch:{ Exception -> 0x0041 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x0041 }
            java.lang.Class<android.media.ExifInterface> r8 = android.media.ExifInterface.class
            java.lang.reflect.Field[] r1 = r8.getFields()     // Catch:{ Exception -> 0x0041 }
            int r2 = r1.length     // Catch:{ Exception -> 0x0041 }
            r3 = 0
        L_0x0012:
            if (r3 >= r2) goto L_0x003e
            r4 = r1[r3]     // Catch:{ Exception -> 0x0041 }
            java.lang.String r5 = r4.getName()     // Catch:{ Exception -> 0x0041 }
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x0041 }
            if (r6 != 0) goto L_0x003b
            java.lang.String r6 = "TAG"
            boolean r5 = r5.startsWith(r6)     // Catch:{ Exception -> 0x0041 }
            if (r5 == 0) goto L_0x003b
            java.lang.Object r4 = r4.get(r8)     // Catch:{ Exception -> 0x0041 }
            if (r4 == 0) goto L_0x003b
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0041 }
            java.lang.String r5 = r0.getAttribute(r4)     // Catch:{ Exception -> 0x0041 }
            if (r5 == 0) goto L_0x003b
            r7.setAttribute(r4, r5)     // Catch:{ Exception -> 0x0041 }
        L_0x003b:
            int r3 = r3 + 1
            goto L_0x0012
        L_0x003e:
            r7.saveAttributes()     // Catch:{ Exception -> 0x0041 }
        L_0x0041:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.pdr.a.a(java.lang.String, java.lang.String):void");
    }

    public static Bitmap a(Context context, String str, BitmapFactory.Options options) throws IOException {
        if (FileUtil.checkPrivatePath(context, str)) {
            return BitmapFactory.decodeFile(str, options);
        }
        InputStream fileInputStream = FileUtil.getFileInputStream(context, str);
        if (fileInputStream == null) {
            return null;
        }
        Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream, (Rect) null, options);
        fileInputStream.close();
        return decodeStream;
    }

    public static void a(IWebview iWebview, String str, String str2, int i) {
        Deprecated_JSUtil.execCallback(iWebview, str, DOMException.toJSON(i, str2), JSUtil.ERROR, true, false);
    }

    public static void a(IWebview iWebview, String str, String str2) {
        Deprecated_JSUtil.execCallback(iWebview, str, str2, JSUtil.OK, true, false);
    }

    public static long a(String str, Bitmap bitmap, boolean z, int i) {
        File file = new File(str);
        if (file.exists()) {
            if (file.length() < 1) {
                file.delete();
            } else if (!z) {
                return -1;
            } else {
                file.delete();
            }
        } else if (a(str)) {
            file = new File(str);
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(c(str), i, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return file.length();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e2) {
            e2.printStackTrace();
            return -1;
        }
    }

    public static boolean a(String str) {
        int lastIndexOf;
        if (!(TextUtils.isEmpty(str) || (lastIndexOf = str.lastIndexOf("/")) == -1 || lastIndexOf == 0)) {
            try {
                File file = new File(str.substring(0, lastIndexOf));
                if (file.exists()) {
                    return true;
                }
                file.mkdirs();
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static float a(String str, float f, float f2) {
        if (str.equals("auto")) {
            return f2;
        }
        return PdrUtil.parseFloat(str, f, f2);
    }
}
