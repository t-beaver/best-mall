package io.dcloud.js.gallery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GalleryFeatureImpl implements IFeature {
    /* access modifiers changed from: private */
    public static String a = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/");
    /* access modifiers changed from: private */
    public static int b = 1001;
    /* access modifiers changed from: private */
    public static int c = 1002;
    /* access modifiers changed from: private */
    public static int d = 1003;
    /* access modifiers changed from: private */
    public static int e = 1004;
    private static IWebview f;
    AbsMgr g = null;
    /* access modifiers changed from: private */
    public ArrayList<BroadcastReceiver> h;

    class a extends PermissionUtil.Request {
        final /* synthetic */ String[] a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;

        a(String[] strArr, IWebview iWebview, String str) {
            this.a = strArr;
            this.b = iWebview;
            this.c = str;
        }

        public void onDenied(String str) {
            String json = DOMException.toJSON(12, DOMException.MSG_NO_PERMISSION);
            Deprecated_JSUtil.execCallback(this.b, this.a[0], json, JSUtil.ERROR, true, false);
        }

        public void onGranted(String str) {
            String[] strArr = this.a;
            boolean z = false;
            if (strArr.length >= 2) {
                String str2 = strArr[1];
                if (!PdrUtil.isEmpty(str2)) {
                    try {
                        z = new JSONObject(str2).optBoolean("multiple", false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (z) {
                GalleryFeatureImpl.this.a(this.b, this.c, this.a);
            } else {
                GalleryFeatureImpl.this.b(this.b, this.c, this.a);
            }
        }
    }

    class b extends PermissionUtil.StreamPermissionRequest {
        final /* synthetic */ String a;
        final /* synthetic */ IApp b;
        final /* synthetic */ IWebview c;
        final /* synthetic */ String d;
        final /* synthetic */ String[] e;

        class a extends CustomTarget<byte[]> {
            a() {
            }

            /* renamed from: a */
            public void onResourceReady(byte[] bArr, Transition<? super byte[]> transition) {
                String downloadFilename = PdrUtil.getDownloadFilename((String) null, "image/*", b.this.a);
                if (!downloadFilename.contains(Operators.DOT_STR) || FileUtil.getFileTypeForSuffix(downloadFilename.substring(downloadFilename.lastIndexOf(Operators.DOT_STR) + 1)) == null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                    String str = options.outMimeType;
                    if (!PdrUtil.isEmpty(str)) {
                        String[] split = str.split("/");
                        if (split.length > 1 && !PdrUtil.isEmpty(split[1]) && !split[1].contains("*")) {
                            downloadFilename = downloadFilename + "_" + System.currentTimeMillis() + Operators.DOT_STR + split[1];
                        }
                    }
                }
                String str2 = GalleryFeatureImpl.a + downloadFilename;
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                if (!FileUtil.needMediaStoreOpenFile(b.this.b.getActivity())) {
                    FileUtil.writeStream2File(byteArrayInputStream, new File(str2));
                } else {
                    Uri copyMediaFileToDCIM = FileUtil.copyMediaFileToDCIM(b.this.c.getContext(), byteArrayInputStream, downloadFilename);
                    if (copyMediaFileToDCIM != null) {
                        str2 = FileUtil.getPathFromUri(b.this.c.getContext(), copyMediaFileToDCIM);
                    } else {
                        String json = DOMException.toJSON(12, "SAVE ERROR");
                        b bVar = b.this;
                        Deprecated_JSUtil.execCallback(bVar.c, bVar.d, json, JSUtil.ERROR, true, false);
                    }
                }
                String convert2WebviewFullPath = b.this.b.convert2WebviewFullPath((String) null, str2);
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("file", convert2WebviewFullPath);
                    jSONObject.put(AbsoluteConst.XML_PATH, convert2WebviewFullPath);
                    b bVar2 = b.this;
                    JSUtil.execCallback(bVar2.c, bVar2.d, jSONObject, JSUtil.OK, false);
                    b.this.c.getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse(DeviceInfo.FILE_PROTOCOL + str2)));
                } catch (JSONException unused) {
                }
            }

            public void onLoadCleared(Drawable drawable) {
            }

            public void onLoadFailed(Drawable drawable) {
                String json = DOMException.toJSON(12, "UNKOWN ERROR");
                b bVar = b.this;
                Deprecated_JSUtil.execCallback(bVar.c, bVar.d, json, JSUtil.ERROR, true, false);
            }
        }

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        b(IApp iApp, String str, IApp iApp2, IWebview iWebview, String str2, String[] strArr) {
            super(iApp);
            this.a = str;
            this.b = iApp2;
            this.c = iWebview;
            this.d = str2;
            this.e = strArr;
        }

        public void onDenied(String str) {
            Deprecated_JSUtil.execCallback(this.c, this.d, DOMException.toJSON(12, DOMException.MSG_NO_PERMISSION), JSUtil.ERROR, true, false);
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x00c1 A[Catch:{ Exception -> 0x011e }] */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0106 A[Catch:{ Exception -> 0x011e }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onGranted(java.lang.String r8) {
            /*
                r7 = this;
                r8 = 12
                java.lang.String r0 = r7.a     // Catch:{ Exception -> 0x011e }
                if (r0 == 0) goto L_0x0118
                boolean r0 = io.dcloud.common.util.PdrUtil.isNetPath(r0)     // Catch:{ Exception -> 0x011e }
                if (r0 == 0) goto L_0x002c
                io.dcloud.common.DHInterface.IApp r0 = r7.b     // Catch:{ Exception -> 0x011e }
                android.app.Activity r0 = r0.getActivity()     // Catch:{ Exception -> 0x011e }
                com.bumptech.glide.RequestManager r0 = com.bumptech.glide.Glide.with((android.app.Activity) r0)     // Catch:{ Exception -> 0x011e }
                java.lang.Class<byte[]> r1 = byte[].class
                com.bumptech.glide.RequestBuilder r0 = r0.as(r1)     // Catch:{ Exception -> 0x011e }
                java.lang.String r1 = r7.a     // Catch:{ Exception -> 0x011e }
                com.bumptech.glide.RequestBuilder r0 = r0.load((java.lang.String) r1)     // Catch:{ Exception -> 0x011e }
                io.dcloud.js.gallery.GalleryFeatureImpl$b$a r1 = new io.dcloud.js.gallery.GalleryFeatureImpl$b$a     // Catch:{ Exception -> 0x011e }
                r1.<init>()     // Catch:{ Exception -> 0x011e }
                r0.into(r1)     // Catch:{ Exception -> 0x011e }
                goto L_0x0133
            L_0x002c:
                io.dcloud.common.DHInterface.IApp r0 = r7.b     // Catch:{ Exception -> 0x011e }
                io.dcloud.common.DHInterface.IWebview r1 = r7.c     // Catch:{ Exception -> 0x011e }
                java.lang.String r1 = r1.obtainFullUrl()     // Catch:{ Exception -> 0x011e }
                java.lang.String[] r2 = r7.e     // Catch:{ Exception -> 0x011e }
                r3 = 0
                r2 = r2[r3]     // Catch:{ Exception -> 0x011e }
                java.lang.String r0 = r0.convert2AbsFullPath(r1, r2)     // Catch:{ Exception -> 0x011e }
                io.dcloud.common.DHInterface.IApp r1 = r7.b     // Catch:{ Exception -> 0x011e }
                android.app.Activity r1 = r1.getActivity()     // Catch:{ Exception -> 0x011e }
                boolean r1 = io.dcloud.common.util.FileUtil.checkPrivatePath(r1, r0)     // Catch:{ Exception -> 0x011e }
                if (r1 != 0) goto L_0x0067
                io.dcloud.common.DHInterface.IApp r1 = r7.b     // Catch:{ Exception -> 0x011e }
                android.app.Activity r1 = r1.getActivity()     // Catch:{ Exception -> 0x011e }
                boolean r1 = io.dcloud.common.util.FileUtil.isFilePathForPublic(r1, r0)     // Catch:{ Exception -> 0x011e }
                if (r1 != 0) goto L_0x0067
                java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_PATH_NOT_PRIVATE_ERROR     // Catch:{ Exception -> 0x011e }
                java.lang.String r3 = io.dcloud.common.constant.DOMException.toJSON((int) r8, (java.lang.String) r0)     // Catch:{ Exception -> 0x011e }
                io.dcloud.common.DHInterface.IWebview r1 = r7.c     // Catch:{ Exception -> 0x011e }
                java.lang.String r2 = r7.d     // Catch:{ Exception -> 0x011e }
                int r4 = io.dcloud.common.util.JSUtil.ERROR     // Catch:{ Exception -> 0x011e }
                r5 = 1
                r6 = 0
                io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x011e }
                return
            L_0x0067:
                java.lang.String r1 = "/"
                int r1 = r0.lastIndexOf(r1)     // Catch:{ Exception -> 0x011e }
                r2 = 1
                int r1 = r1 + r2
                java.lang.String r1 = r0.substring(r1)     // Catch:{ Exception -> 0x011e }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x011e }
                r4.<init>()     // Catch:{ Exception -> 0x011e }
                java.lang.String r5 = io.dcloud.js.gallery.GalleryFeatureImpl.a     // Catch:{ Exception -> 0x011e }
                r4.append(r5)     // Catch:{ Exception -> 0x011e }
                r4.append(r1)     // Catch:{ Exception -> 0x011e }
                java.lang.String r1 = r4.toString()     // Catch:{ Exception -> 0x011e }
                int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x011e }
                r5 = 29
                if (r4 < r5) goto L_0x00b7
                io.dcloud.common.DHInterface.IApp r4 = r7.b     // Catch:{ Exception -> 0x011e }
                android.app.Activity r4 = r4.getActivity()     // Catch:{ Exception -> 0x011e }
                boolean r4 = io.dcloud.common.util.FileUtil.needMediaStoreOpenFile(r4)     // Catch:{ Exception -> 0x011e }
                if (r4 != 0) goto L_0x009f
                int r0 = io.dcloud.common.adapter.io.DHFile.copyFile(r0, r1)     // Catch:{ Exception -> 0x011e }
                if (r2 != r0) goto L_0x00be
                goto L_0x00bf
            L_0x009f:
                io.dcloud.common.DHInterface.IWebview r4 = r7.c     // Catch:{ Exception -> 0x011e }
                android.content.Context r4 = r4.getContext()     // Catch:{ Exception -> 0x011e }
                android.net.Uri r0 = io.dcloud.common.util.FileUtil.copyMediaFileToDCIM(r4, r0)     // Catch:{ Exception -> 0x011e }
                if (r0 == 0) goto L_0x00be
                io.dcloud.common.DHInterface.IWebview r1 = r7.c     // Catch:{ Exception -> 0x011e }
                android.content.Context r1 = r1.getContext()     // Catch:{ Exception -> 0x011e }
                java.lang.String r0 = io.dcloud.common.util.FileUtil.getPathFromUri(r1, r0)     // Catch:{ Exception -> 0x011e }
                r1 = r0
                goto L_0x00bf
            L_0x00b7:
                int r0 = io.dcloud.common.adapter.io.DHFile.copyFile(r0, r1)     // Catch:{ Exception -> 0x011e }
                if (r2 != r0) goto L_0x00be
                goto L_0x00bf
            L_0x00be:
                r2 = 0
            L_0x00bf:
                if (r2 == 0) goto L_0x0106
                io.dcloud.common.DHInterface.IApp r0 = r7.b     // Catch:{ Exception -> 0x011e }
                r2 = 0
                java.lang.String r0 = r0.convert2WebviewFullPath(r2, r1)     // Catch:{ Exception -> 0x011e }
                org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x011e }
                r2.<init>()     // Catch:{ Exception -> 0x011e }
                java.lang.String r4 = "file"
                r2.put(r4, r0)     // Catch:{ Exception -> 0x011e }
                java.lang.String r4 = "path"
                r2.put(r4, r0)     // Catch:{ Exception -> 0x011e }
                io.dcloud.common.DHInterface.IWebview r0 = r7.c     // Catch:{ Exception -> 0x011e }
                java.lang.String r4 = r7.d     // Catch:{ Exception -> 0x011e }
                int r5 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x011e }
                io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r0, (java.lang.String) r4, (org.json.JSONObject) r2, (int) r5, (boolean) r3)     // Catch:{ Exception -> 0x011e }
                android.content.Intent r0 = new android.content.Intent     // Catch:{ Exception -> 0x011e }
                java.lang.String r2 = "android.intent.action.MEDIA_SCANNER_SCAN_FILE"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x011e }
                r3.<init>()     // Catch:{ Exception -> 0x011e }
                java.lang.String r4 = "file://"
                r3.append(r4)     // Catch:{ Exception -> 0x011e }
                r3.append(r1)     // Catch:{ Exception -> 0x011e }
                java.lang.String r1 = r3.toString()     // Catch:{ Exception -> 0x011e }
                android.net.Uri r1 = android.net.Uri.parse(r1)     // Catch:{ Exception -> 0x011e }
                r0.<init>(r2, r1)     // Catch:{ Exception -> 0x011e }
                io.dcloud.common.DHInterface.IWebview r1 = r7.c     // Catch:{ Exception -> 0x011e }
                android.content.Context r1 = r1.getContext()     // Catch:{ Exception -> 0x011e }
                r1.sendBroadcast(r0)     // Catch:{ Exception -> 0x011e }
                goto L_0x0133
            L_0x0106:
                java.lang.String r0 = "UNKOWN ERROR3"
                java.lang.String r3 = io.dcloud.common.constant.DOMException.toJSON((int) r8, (java.lang.String) r0)     // Catch:{ Exception -> 0x011e }
                io.dcloud.common.DHInterface.IWebview r1 = r7.c     // Catch:{ Exception -> 0x011e }
                java.lang.String r2 = r7.d     // Catch:{ Exception -> 0x011e }
                int r4 = io.dcloud.common.util.JSUtil.ERROR     // Catch:{ Exception -> 0x011e }
                r5 = 1
                r6 = 0
                io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x011e }
                goto L_0x0133
            L_0x0118:
                java.io.IOException r0 = new java.io.IOException     // Catch:{ Exception -> 0x011e }
                r0.<init>()     // Catch:{ Exception -> 0x011e }
                throw r0     // Catch:{ Exception -> 0x011e }
            L_0x011e:
                r0 = move-exception
                r0.printStackTrace()
                java.lang.String r0 = "UNKOWN ERROR4"
                java.lang.String r3 = io.dcloud.common.constant.DOMException.toJSON((int) r8, (java.lang.String) r0)
                io.dcloud.common.DHInterface.IWebview r1 = r7.c
                java.lang.String r2 = r7.d
                int r4 = io.dcloud.common.util.JSUtil.ERROR
                r5 = 1
                r6 = 0
                io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)
            L_0x0133:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.gallery.GalleryFeatureImpl.b.onGranted(java.lang.String):void");
        }
    }

    class c implements ISysEventListener {
        final /* synthetic */ IApp a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;

        c(IApp iApp, IWebview iWebview, String str) {
            this.a = iApp;
            this.b = iWebview;
            this.c = str;
        }

        /* JADX WARNING: Removed duplicated region for block: B:39:0x00b1  */
        /* JADX WARNING: Removed duplicated region for block: B:40:0x00b3  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onExecute(io.dcloud.common.DHInterface.ISysEventListener.SysEventType r11, java.lang.Object r12) {
            /*
                r10 = this;
                java.lang.Object[] r12 = (java.lang.Object[]) r12
                r0 = 0
                r1 = r12[r0]
                java.lang.Integer r1 = (java.lang.Integer) r1
                int r1 = r1.intValue()
                r2 = 1
                r3 = r12[r2]
                java.lang.Integer r3 = (java.lang.Integer) r3
                r3.intValue()
                r3 = 2
                r12 = r12[r3]
                android.content.Intent r12 = (android.content.Intent) r12
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r3 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onActivityResult
                if (r11 != r3) goto L_0x00b8
                io.dcloud.common.DHInterface.IApp r11 = r10.a
                r11.unregisterSysEventListener(r10, r3)
                r11 = 0
                int r3 = io.dcloud.js.gallery.GalleryFeatureImpl.c     // Catch:{ Exception -> 0x00a6 }
                if (r1 != r3) goto L_0x0030
                java.lang.String r1 = "all_path"
                java.lang.String[] r1 = r12.getStringArrayExtra(r1)     // Catch:{ Exception -> 0x00a6 }
                goto L_0x00a4
            L_0x0030:
                int r3 = io.dcloud.js.gallery.GalleryFeatureImpl.d     // Catch:{ Exception -> 0x00a6 }
                if (r1 != r3) goto L_0x0092
                if (r12 == 0) goto L_0x00a6
                android.content.ClipData r1 = r12.getClipData()     // Catch:{ Exception -> 0x00a6 }
                if (r1 == 0) goto L_0x0060
                int r2 = r1.getItemCount()     // Catch:{ Exception -> 0x00a6 }
                java.lang.String[] r3 = new java.lang.String[r2]     // Catch:{ Exception -> 0x00a6 }
                r4 = 0
            L_0x0045:
                if (r4 >= r2) goto L_0x00a7
                android.content.ClipData$Item r5 = r1.getItemAt(r4)     // Catch:{ Exception -> 0x005e }
                android.net.Uri r5 = r5.getUri()     // Catch:{ Exception -> 0x005e }
                io.dcloud.common.DHInterface.IWebview r6 = r10.b     // Catch:{ Exception -> 0x005e }
                android.app.Activity r6 = r6.getActivity()     // Catch:{ Exception -> 0x005e }
                java.lang.String r5 = io.dcloud.common.adapter.util.ContentUriUtil.getImageAbsolutePath(r6, r5)     // Catch:{ Exception -> 0x005e }
                r3[r4] = r5     // Catch:{ Exception -> 0x005e }
                int r4 = r4 + 1
                goto L_0x0045
            L_0x005e:
                goto L_0x00a7
            L_0x0060:
                android.net.Uri r1 = r12.getData()     // Catch:{ Exception -> 0x00a6 }
                if (r1 == 0) goto L_0x00a6
                android.net.Uri r1 = r12.getData()     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r1 = r1.getPath()     // Catch:{ Exception -> 0x00a6 }
                boolean r1 = io.dcloud.common.util.PdrUtil.isDeviceRootDir(r1)     // Catch:{ Exception -> 0x00a6 }
                if (r1 == 0) goto L_0x007d
                android.net.Uri r1 = r12.getData()     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r1 = r1.getPath()     // Catch:{ Exception -> 0x00a6 }
                goto L_0x008b
            L_0x007d:
                io.dcloud.common.DHInterface.IWebview r1 = r10.b     // Catch:{ Exception -> 0x00a6 }
                android.app.Activity r1 = r1.getActivity()     // Catch:{ Exception -> 0x00a6 }
                android.net.Uri r3 = r12.getData()     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r1 = io.dcloud.common.adapter.util.ContentUriUtil.getImageAbsolutePath(r1, r3)     // Catch:{ Exception -> 0x00a6 }
            L_0x008b:
                if (r1 == 0) goto L_0x00a6
                java.lang.String[] r3 = new java.lang.String[r2]     // Catch:{ Exception -> 0x00a6 }
                r3[r0] = r1     // Catch:{ Exception -> 0x005e }
                goto L_0x00a7
            L_0x0092:
                int r2 = io.dcloud.js.gallery.GalleryFeatureImpl.e     // Catch:{ Exception -> 0x00a6 }
                if (r1 != r2) goto L_0x00a6
                java.lang.String r1 = "select_result"
                java.util.ArrayList r1 = r12.getParcelableArrayListExtra(r1)     // Catch:{ Exception -> 0x00a6 }
                io.dcloud.js.gallery.GalleryFeatureImpl r2 = io.dcloud.js.gallery.GalleryFeatureImpl.this     // Catch:{ Exception -> 0x00a6 }
                java.lang.String[] r1 = r2.mediasToJSONArray(r1)     // Catch:{ Exception -> 0x00a6 }
            L_0x00a4:
                r3 = r1
                goto L_0x00a7
            L_0x00a6:
                r3 = r11
            L_0x00a7:
                io.dcloud.js.gallery.GalleryFeatureImpl r4 = io.dcloud.js.gallery.GalleryFeatureImpl.this
                io.dcloud.common.DHInterface.IApp r5 = r10.a
                io.dcloud.common.DHInterface.IWebview r6 = r10.b
                java.lang.String r7 = r10.c
                if (r12 == 0) goto L_0x00b3
                r8 = r3
                goto L_0x00b4
            L_0x00b3:
                r8 = r11
            L_0x00b4:
                r9 = 1
                r4.a(r5, r6, r7, r8, r9)
            L_0x00b8:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.gallery.GalleryFeatureImpl.c.onExecute(io.dcloud.common.DHInterface.ISysEventListener$SysEventType, java.lang.Object):boolean");
        }
    }

    class d extends BroadcastReceiver {
        final /* synthetic */ Activity a;

        d(Activity activity) {
            this.a = activity;
        }

        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("_onMaxedId");
            if (TextUtils.isEmpty(stringExtra)) {
                if (GalleryFeatureImpl.this.h != null) {
                    GalleryFeatureImpl.this.h.remove(this);
                }
                LocalBroadcastManager.getInstance(this.a).unregisterReceiver(this);
                return;
            }
            GalleryFeatureImpl.onMaxed(this.a, stringExtra);
        }
    }

    class e implements ISysEventListener {
        final /* synthetic */ IApp a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;

        e(IApp iApp, IWebview iWebview, String str) {
            this.a = iApp;
            this.b = iWebview;
            this.c = str;
        }

        public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
            String[] mediasToJSONArray;
            String str;
            Uri data;
            Object[] objArr = (Object[]) obj;
            int intValue = ((Integer) objArr[0]).intValue();
            ((Integer) objArr[1]).intValue();
            Intent intent = (Intent) objArr[2];
            ISysEventListener.SysEventType sysEventType2 = ISysEventListener.SysEventType.onActivityResult;
            if (sysEventType == sysEventType2) {
                this.a.unregisterSysEventListener(this, sysEventType2);
                String[] strArr = null;
                if (intValue == GalleryFeatureImpl.b) {
                    if (intent == null || (data = intent.getData()) == null) {
                        str = null;
                    } else {
                        str = PdrUtil.isDeviceRootDir(data.getPath()) ? data.getPath() : data.toString();
                    }
                    GalleryFeatureImpl galleryFeatureImpl = GalleryFeatureImpl.this;
                    IApp iApp = this.a;
                    IWebview iWebview = this.b;
                    String str2 = this.c;
                    if (str != null) {
                        strArr = new String[]{str};
                    }
                    galleryFeatureImpl.a(iApp, iWebview, str2, strArr, false);
                } else if (intValue == GalleryFeatureImpl.e) {
                    String str3 = (intent == null || (mediasToJSONArray = GalleryFeatureImpl.this.mediasToJSONArray(intent.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT))) == null || mediasToJSONArray.length <= 0) ? null : mediasToJSONArray[0];
                    GalleryFeatureImpl galleryFeatureImpl2 = GalleryFeatureImpl.this;
                    IApp iApp2 = this.a;
                    IWebview iWebview2 = this.b;
                    String str4 = this.c;
                    if (str3 != null) {
                        strArr = new String[]{str3};
                    }
                    galleryFeatureImpl2.a(iApp2, iWebview2, str4, strArr, false);
                }
            }
            return false;
        }
    }

    public static void onMaxed(Context context, String str) {
        IWebview iWebview = f;
        if (iWebview != null) {
            JSUtil.execCallback(iWebview, str, "", JSUtil.OK, true);
            return;
        }
        Intent intent = new Intent("io.dcloud.streamapp.Gallery.onMax." + str);
        intent.putExtra("_onMaxedId", str);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public ArrayList<Media> JSONArrayToMedias(int i, JSONArray jSONArray) {
        ArrayList<Media> arrayList = null;
        int i2 = 0;
        while (i2 < jSONArray.length()) {
            try {
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                File file = new File(URI.create(jSONArray.getString(i2)));
                if (file.exists()) {
                    arrayList.add(new Media(file.getPath(), file.getName(), 0, i == 102 ? 3 : 1, file.length(), 0, ""));
                } else {
                    int i3 = i;
                }
                i2++;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return arrayList;
    }

    public void dispose(String str) {
        ArrayList<BroadcastReceiver> arrayList = this.h;
        if (!(arrayList == null || f == null)) {
            Iterator<BroadcastReceiver> it = arrayList.iterator();
            while (it.hasNext()) {
                LocalBroadcastManager.getInstance(f.getActivity()).unregisterReceiver(it.next());
            }
            this.h.clear();
        }
        f = null;
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        f = iWebview;
        Context context = iWebview.getContext();
        AppRuntime.checkPrivacyComplianceAndPrompt(context, "Gallery-" + str);
        if ("pick".equals(str)) {
            PermissionUtil.usePermission(iWebview.obtainApp().getActivity(), "gallery", PermissionUtil.PMS_STORAGE, 2, new a(strArr, iWebview, str));
            return null;
        } else if (!"save".equals(str)) {
            return null;
        } else {
            IApp obtainApp = iWebview.obtainFrameView().obtainApp();
            PermissionUtil.usePermission(obtainApp.getActivity(), "gallery", PermissionUtil.PMS_STORAGE, 2, new b(obtainApp, strArr[0], obtainApp, iWebview, strArr[1], strArr));
            return null;
        }
    }

    public Intent getMediaPickerIntent(Context context, String str, int i, JSONArray jSONArray, String str2, boolean z, String str3, JSONObject jSONObject, String str4, String str5, boolean z2) {
        int i2;
        Intent intent = new Intent();
        if (!str.contains("video") || !str.contains("image")) {
            i2 = str.contains("video") ? 102 : 100;
        } else {
            i2 = 101;
        }
        boolean contains = str.contains("__Single__");
        intent.setClassName(context, "com.dmcbig.mediapicker.PickerActivity");
        intent.putExtra(PickerConfig.SELECT_MODE, i2);
        intent.putExtra(PickerConfig.SINGLE_SELECT, contains);
        intent.putExtra(PickerConfig.COMPRESSED, z2);
        if (i > 0) {
            intent.putExtra(PickerConfig.MAX_SELECT_COUNT, i);
        }
        if (jSONArray != null) {
            intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, JSONArrayToMedias(i2, jSONArray));
        }
        if (!TextUtils.isEmpty(str3)) {
            intent.putExtra(PickerConfig.SELECTED_MAX_CALLBACK_ID, str3);
        }
        if (!TextUtils.isEmpty(str2)) {
            intent.putExtra(PickerConfig.DONE_BUTTON_TEXT, str2);
        }
        if (!TextUtils.isEmpty(str4)) {
            intent.putExtra(PickerConfig.SIZE_TYPE, str4);
        }
        if (!TextUtils.isEmpty(str5)) {
            intent.putExtra(PickerConfig.DOC_PATH, str5);
        }
        if (jSONObject != null && jSONObject.has("width") && jSONObject.has("height")) {
            Pattern compile = Pattern.compile("[^0-9]");
            try {
                int parseInt = Integer.parseInt(compile.matcher(jSONObject.optString("width")).replaceAll(""));
                int parseInt2 = Integer.parseInt(compile.matcher(jSONObject.optString("height")).replaceAll(""));
                if (parseInt > 0 && parseInt2 > 0) {
                    jSONObject.put("width", parseInt);
                    jSONObject.put("height", parseInt2);
                    intent.putExtra(PickerConfig.IMAGE_CROP, jSONObject.toString());
                    intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1);
                }
            } catch (Exception unused) {
            }
        }
        intent.putExtra(PickerConfig.IMAGE_EDITABLE, z);
        return intent;
    }

    public void init(AbsMgr absMgr, String str) {
        this.g = absMgr;
    }

    public String[] mediasToJSONArray(ArrayList<Parcelable> arrayList) {
        String[] strArr = null;
        for (int i = 0; i < arrayList.size(); i++) {
            Media media = (Media) arrayList.get(i);
            if (strArr == null) {
                strArr = new String[arrayList.size()];
            }
            strArr[i] = media.path;
        }
        return strArr;
    }

    /* access modifiers changed from: private */
    public void b(IWebview iWebview, String str, String[] strArr) {
        String str2;
        boolean z;
        String str3;
        JSONObject jSONObject;
        boolean z2;
        String str4;
        String str5;
        String[] strArr2 = strArr;
        try {
            String str6 = strArr2[0];
            try {
                IApp obtainApp = iWebview.obtainFrameView().obtainApp();
                try {
                    obtainApp.registerSysEventListener(new e(obtainApp, iWebview, str6), ISysEventListener.SysEventType.onActivityResult);
                    String str7 = "image/*";
                    if (!PdrUtil.isEmpty(strArr2[1])) {
                        JSONObject createJSONObject = JSONUtil.createJSONObject(strArr2[1]);
                        String string = JSONUtil.getString(createJSONObject, Constants.Name.FILTER);
                        if ("video".equals(string)) {
                            str7 = "video/*";
                        } else if ("none".equals(string)) {
                            str7 = "image/*|video/*";
                        }
                        str4 = createJSONObject.optString("confirmText");
                        boolean optBoolean = createJSONObject.optBoolean("editable", true);
                        JSONObject optJSONObject = createJSONObject.optJSONObject("crop");
                        String optString = createJSONObject.optString("sizeType");
                        z = createJSONObject.optBoolean("videoCompress", false);
                        jSONObject = optJSONObject;
                        str3 = optString;
                        z2 = optBoolean;
                    } else {
                        str4 = null;
                        jSONObject = null;
                        str3 = null;
                        z2 = true;
                        z = false;
                    }
                    new Intent("android.intent.action.PICK");
                    String str8 = str7 + "__Single__";
                    if (strArr2.length > 2) {
                        String str9 = strArr2[2];
                        a(iWebview.getActivity(), str9);
                        str5 = str9;
                    } else {
                        str5 = null;
                    }
                    iWebview.getActivity().startActivityForResult(getMediaPickerIntent(iWebview.getContext(), str8, 1, (JSONArray) null, str4, z2, str5, jSONObject, str3, iWebview.obtainApp().convert2LocalFullPath((String) null, BaseInfo.REL_PRIVATE_DOC_DIR), z), e);
                } catch (Exception e2) {
                    e = e2;
                    str2 = str6;
                    e.printStackTrace();
                    Deprecated_JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e.getMessage()), JSUtil.ERROR, true, false);
                }
            } catch (Exception e3) {
                e = e3;
                IWebview iWebview2 = iWebview;
                str2 = str6;
                e.printStackTrace();
                Deprecated_JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e.getMessage()), JSUtil.ERROR, true, false);
            }
        } catch (Exception e4) {
            e = e4;
            IWebview iWebview3 = iWebview;
            str2 = null;
            e.printStackTrace();
            Deprecated_JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e.getMessage()), JSUtil.ERROR, true, false);
        }
    }

    /* access modifiers changed from: private */
    public void a(IWebview iWebview, String str, String[] strArr) {
        String str2;
        String str3;
        IApp obtainApp;
        boolean z;
        String str4;
        JSONObject jSONObject;
        boolean z2;
        JSONArray jSONArray;
        String str5;
        int i;
        int i2;
        String str6;
        String str7;
        String[] strArr2 = strArr;
        try {
            String str8 = strArr2[0];
            try {
                obtainApp = iWebview.obtainFrameView().obtainApp();
            } catch (Exception e2) {
                e = e2;
                IWebview iWebview2 = iWebview;
                str3 = str8;
                str2 = str3;
                e.printStackTrace();
                Deprecated_JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e.getMessage()), JSUtil.ERROR, true, false);
            }
            try {
                obtainApp.registerSysEventListener(new c(obtainApp, iWebview, str8), ISysEventListener.SysEventType.onActivityResult);
                Intent intent = new Intent();
                String str9 = "image/*";
                int i3 = -1;
                if (!PdrUtil.isEmpty(strArr2[1])) {
                    try {
                        JSONObject createJSONObject = JSONUtil.createJSONObject(strArr2[1]);
                        String string = JSONUtil.getString(createJSONObject, Constants.Name.FILTER);
                        if ("video".equals(string)) {
                            str9 = "video/*";
                        } else if ("none".equals(string)) {
                            str9 = "image/*|video/*";
                        }
                        i3 = createJSONObject.optInt("maximum", -1);
                        jSONArray = createJSONObject.optJSONArray("selected");
                        String optString = createJSONObject.optString("confirmText");
                        z2 = createJSONObject.optBoolean("editable", true);
                        jSONObject = createJSONObject.optJSONObject("crop");
                        str4 = createJSONObject.optString("sizeType");
                        z = createJSONObject.optBoolean("videoCompress", false);
                        str5 = optString;
                    } catch (Exception e3) {
                        e = e3;
                        str2 = str8;
                        e.printStackTrace();
                        Deprecated_JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e.getMessage()), JSUtil.ERROR, true, false);
                    }
                } else {
                    str5 = null;
                    jSONArray = null;
                    z2 = true;
                    jSONObject = null;
                    str4 = null;
                    z = false;
                }
                intent.setType(str9);
                if (Build.VERSION.SDK_INT >= 19) {
                    intent.setAction("android.intent.action.OPEN_DOCUMENT");
                    intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                    intent.addCategory("android.intent.category.OPENABLE");
                    i = d;
                    try {
                        if (strArr2.length > 2) {
                            try {
                                str7 = strArr2[2];
                                a(iWebview.getActivity(), str7);
                            } catch (Exception e4) {
                                e = e4;
                                str3 = str8;
                            }
                        } else {
                            str7 = null;
                        }
                        str3 = str8;
                        try {
                            intent = getMediaPickerIntent(iWebview.getContext(), str9, i3, jSONArray, str5, z2, str7, jSONObject, str4, iWebview.obtainApp().convert2LocalFullPath((String) null, BaseInfo.REL_PRIVATE_DOC_DIR), z);
                            try {
                                i2 = e;
                            } catch (Exception e5) {
                                e = e5;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            intent = intent;
                            try {
                                e.printStackTrace();
                                iWebview.getActivity().startActivityForResult(intent, i);
                            } catch (Exception e7) {
                                e = e7;
                                str2 = str3;
                                e.printStackTrace();
                                Deprecated_JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e.getMessage()), JSUtil.ERROR, true, false);
                            }
                        }
                    } catch (Exception e8) {
                        e = e8;
                        str3 = str8;
                        Intent intent2 = intent;
                        e.printStackTrace();
                        iWebview.getActivity().startActivityForResult(intent, i);
                    }
                } else {
                    String str10 = str8;
                    Intent intent3 = intent;
                    if (strArr2.length > 2) {
                        str6 = strArr2[2];
                        intent3.putExtra("_onMaxedId", str6);
                        a(iWebview.getActivity(), str6);
                    } else {
                        str6 = null;
                    }
                    intent = getMediaPickerIntent(iWebview.getContext(), str9, i3, jSONArray, str5, z2, str6, jSONObject, str4, iWebview.obtainApp().convert2LocalFullPath((String) null, BaseInfo.REL_PRIVATE_DOC_DIR), z);
                    i2 = e;
                }
                i = i2;
                iWebview.getActivity().startActivityForResult(intent, i);
            } catch (Exception e9) {
                e = e9;
                str3 = str8;
                str2 = str3;
                e.printStackTrace();
                Deprecated_JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e.getMessage()), JSUtil.ERROR, true, false);
            }
        } catch (Exception e10) {
            e = e10;
            IWebview iWebview3 = iWebview;
            str2 = null;
            e.printStackTrace();
            Deprecated_JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e.getMessage()), JSUtil.ERROR, true, false);
        }
    }

    private void a(Activity activity, String str) {
        if ((activity instanceof IActivityHandler) && ((IActivityHandler) activity).isMultiProcessMode()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("io.dcloud.streamapp.Gallery.onMax." + str);
            d dVar = new d(activity);
            if (this.h == null) {
                this.h = new ArrayList<>();
            }
            this.h.add(dVar);
            LocalBroadcastManager.getInstance(activity).registerReceiver(dVar, intentFilter);
        }
    }

    /* access modifiers changed from: private */
    public void a(IApp iApp, IWebview iWebview, String str, String[] strArr, boolean z) {
        String str2;
        boolean z2;
        String str3;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("multiple", z);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        JSONArray jSONArray = new JSONArray();
        if (strArr == null || strArr.length <= 0) {
            str2 = "User cancelled";
            z2 = false;
        } else {
            for (String str4 : strArr) {
                if (str4.startsWith("content://")) {
                    str3 = FileUtil.getPathFromUri(iApp.getActivity(), Uri.parse(str4));
                    if (!TextUtils.isEmpty(str3) && !str3.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        str3 = DeviceInfo.FILE_PROTOCOL + str3;
                    }
                } else {
                    str3 = iApp.convert2WebviewFullPath((String) null, str4);
                }
                jSONArray.put(str3);
            }
            try {
                jSONObject.put("files", jSONArray);
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
            z2 = true;
            str2 = "pickImage path wrong";
        }
        if (z2) {
            JSUtil.execCallback(iWebview, str, jSONObject, JSUtil.OK, false);
            return;
        }
        Deprecated_JSUtil.execCallback(iWebview, str, DOMException.toJSON(12, str2), JSUtil.ERROR, true, false);
    }
}
