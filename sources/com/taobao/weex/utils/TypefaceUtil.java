package com.taobao.weex.utils;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.font.FontAdapter;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypefaceUtil {
    public static final String ACTION_TYPE_FACE_AVAILABLE = "type_face_available";
    public static final String FONT_CACHE_DIR_NAME = "font-family";
    private static final String TAG = "TypefaceUtil";
    /* access modifiers changed from: private */
    public static final Map<String, FontDO> sCacheMap = new HashMap();

    public static void putFontDO(FontDO fontDO) {
        if (fontDO != null && !TextUtils.isEmpty(fontDO.getFontFamilyName())) {
            sCacheMap.put(fontDO.getFontFamilyName(), fontDO);
        }
    }

    public static void registerNativeFont(Map<String, Typeface> map) {
        if (map != null && map.size() > 0) {
            for (Map.Entry next : map.entrySet()) {
                putFontDO(new FontDO((String) next.getKey(), (Typeface) next.getValue()));
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d(TAG, "register new typeface: " + ((String) next.getKey()));
                }
            }
        }
    }

    public static FontDO getFontDO(String str) {
        return sCacheMap.get(str);
    }

    public static void removeFontDO(String str) {
        sCacheMap.remove(str);
    }

    public static void applyFontStyle(Paint paint, int i, int i2, String str) {
        int i3;
        Typeface typeface = paint.getTypeface();
        int i4 = 0;
        if (typeface == null) {
            i3 = 0;
        } else {
            i3 = typeface.getStyle();
        }
        int i5 = 1;
        if (i2 != 1 && ((i3 & 1) == 0 || i2 != -1)) {
            i5 = 0;
        }
        if (i == 2 || ((i3 & 2) != 0 && i == -1)) {
            i5 |= 2;
        }
        if (str != null) {
            typeface = getOrCreateTypeface(str, i5);
        }
        if (typeface != null) {
            paint.setTypeface(Typeface.create(typeface, i5));
        } else {
            paint.setTypeface(Typeface.defaultFromStyle(i5));
        }
        if (i >= 0) {
            if (paint.getTypeface() != null) {
                i4 = paint.getTypeface().getStyle();
            }
            paint.setTextSkewX(((i & (i4 ^ -1)) & 2) != 0 ? -0.2f : 0.0f);
        }
    }

    public static Typeface getOrCreateTypeface(String str, int i) {
        FontDO fontDO = sCacheMap.get(str);
        if (fontDO == null || fontDO.getTypeface() == null) {
            return Typeface.create(str, i);
        }
        return fontDO.getTypeface();
    }

    private static void loadFromAsset(FontDO fontDO, String str) {
        try {
            Typeface createFromAsset = Typeface.createFromAsset(WXEnvironment.getApplication().getAssets(), str);
            if (createFromAsset != null) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d(TAG, "load asset file success");
                }
                fontDO.setState(2);
                fontDO.setTypeface(createFromAsset);
                return;
            }
            WXLogUtils.e(TAG, "Font asset file not found " + fontDO.getUrl());
        } catch (Exception e) {
            WXLogUtils.e(TAG, e.toString());
        }
    }

    public static void loadTypeface(FontDO fontDO, boolean z) {
        if (fontDO != null && fontDO.getTypeface() == null && (fontDO.getState() == 3 || fontDO.getState() == 0)) {
            fontDO.setState(1);
            if (fontDO.getType() == 3) {
                loadFromAsset(fontDO, Uri.parse(fontDO.getUrl()).getPath().substring(1));
            } else if (fontDO.getType() == 1) {
                String url = fontDO.getUrl();
                String fontFamilyName = fontDO.getFontFamilyName();
                String md5 = WXFileUtils.md5(url);
                File file = new File(getFontCacheDir());
                if (!file.exists()) {
                    file.mkdirs();
                }
                String str = file.getAbsolutePath() + File.separator + md5;
                if (!loadLocalFontFile(str, fontFamilyName, false)) {
                    downloadFontByNetwork(url, str, fontFamilyName);
                }
            } else if ((fontDO.getType() == 2 || fontDO.getType() == 5) && !loadLocalFontFile(fontDO.getUrl(), fontDO.getFontFamilyName(), false)) {
                fontDO.setState(3);
            }
        } else if (z) {
            notifyFontAvailable(false, fontDO);
        }
    }

    private static void downloadFontByNetwork(final String str, final String str2, final String str3) {
        IWXHttpAdapter iWXHttpAdapter = WXSDKManager.getInstance().getIWXHttpAdapter();
        if (iWXHttpAdapter == null) {
            WXLogUtils.e(TAG, "downloadFontByNetwork() IWXHttpAdapter == null");
            return;
        }
        WXRequest wXRequest = new WXRequest();
        wXRequest.url = str;
        wXRequest.method = "GET";
        iWXHttpAdapter.sendRequest(wXRequest, new IWXHttpAdapter.OnHttpListener() {
            public void onHeadersReceived(int i, Map<String, List<String>> map) {
            }

            public void onHttpResponseProgress(int i) {
            }

            public void onHttpUploadProgress(int i) {
            }

            public void onHttpStart() {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d(TypefaceUtil.TAG, "downloadFontByNetwork begin url:" + str);
                }
            }

            /* JADX WARNING: Removed duplicated region for block: B:15:0x0043  */
            /* JADX WARNING: Removed duplicated region for block: B:16:0x004d  */
            /* JADX WARNING: Removed duplicated region for block: B:22:0x0068  */
            /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onHttpFinish(com.taobao.weex.common.WXResponse r5) {
                /*
                    r4 = this;
                    java.lang.String r0 = r5.statusCode
                    boolean r0 = android.text.TextUtils.isEmpty(r0)
                    java.lang.String r1 = "TypefaceUtil"
                    r2 = 0
                    if (r0 != 0) goto L_0x0028
                    java.lang.String r0 = r5.statusCode     // Catch:{ NumberFormatException -> 0x0012 }
                    int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x0012 }
                    goto L_0x0029
                L_0x0012:
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    r0.<init>()
                    java.lang.String r3 = "IWXHttpAdapter onHttpFinish statusCode:"
                    r0.append(r3)
                    java.lang.String r3 = r5.statusCode
                    r0.append(r3)
                    java.lang.String r0 = r0.toString()
                    com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.String) r0)
                L_0x0028:
                    r0 = 0
                L_0x0029:
                    r3 = 200(0xc8, float:2.8E-43)
                    if (r0 < r3) goto L_0x0058
                    r3 = 299(0x12b, float:4.19E-43)
                    if (r0 > r3) goto L_0x0058
                    byte[] r0 = r5.originalData
                    if (r0 == 0) goto L_0x0058
                    java.lang.String r0 = r4
                    byte[] r5 = r5.originalData
                    android.app.Application r2 = com.taobao.weex.WXEnvironment.getApplication()
                    boolean r2 = com.taobao.weex.utils.WXFileUtils.saveFile(r0, r5, r2)
                    if (r2 == 0) goto L_0x004d
                    java.lang.String r5 = r4
                    java.lang.String r0 = r5
                    r1 = 1
                    boolean r2 = com.taobao.weex.utils.TypefaceUtil.loadLocalFontFile(r5, r0, r1)
                    goto L_0x0058
                L_0x004d:
                    boolean r5 = com.taobao.weex.WXEnvironment.isApkDebugable()
                    if (r5 == 0) goto L_0x0058
                    java.lang.String r5 = "downloadFontByNetwork() onHttpFinish success, but save file failed."
                    com.taobao.weex.utils.WXLogUtils.d((java.lang.String) r1, (java.lang.String) r5)
                L_0x0058:
                    if (r2 != 0) goto L_0x006c
                    java.util.Map r5 = com.taobao.weex.utils.TypefaceUtil.sCacheMap
                    java.lang.String r0 = r5
                    java.lang.Object r5 = r5.get(r0)
                    com.taobao.weex.utils.FontDO r5 = (com.taobao.weex.utils.FontDO) r5
                    if (r5 == 0) goto L_0x006c
                    r0 = 3
                    r5.setState(r0)
                L_0x006c:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.TypefaceUtil.AnonymousClass1.onHttpFinish(com.taobao.weex.common.WXResponse):void");
            }
        });
    }

    /* access modifiers changed from: private */
    public static boolean loadLocalFontFile(String str, String str2, boolean z) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                if (!new File(str).exists()) {
                    return false;
                }
                Typeface createFromFile = Typeface.createFromFile(str);
                if (createFromFile != null) {
                    final FontDO fontDO = sCacheMap.get(str2);
                    if (fontDO != null) {
                        fontDO.setState(2);
                        fontDO.setTypeface(createFromFile);
                        fontDO.setFilePath(str);
                        if (WXEnvironment.isApkDebugable()) {
                            WXLogUtils.d(TAG, "load local font file success");
                        }
                        if (z) {
                            WXSDKManager.getInstance().getWXRenderManager().postOnUiThread((Runnable) new Runnable() {
                                public void run() {
                                    TypefaceUtil.notifyFontAvailable(true, FontDO.this);
                                }
                            }, 100);
                        } else {
                            notifyFontAvailable(true, fontDO);
                        }
                        return true;
                    }
                } else {
                    WXLogUtils.e(TAG, "load local font file failed, can't create font.");
                }
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.toString());
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static void notifyFontAvailable(boolean z, FontDO fontDO) {
        if (z) {
            Intent intent = new Intent(ACTION_TYPE_FACE_AVAILABLE);
            intent.putExtra(Constants.Name.FONT_FAMILY, fontDO.getFontFamilyName());
            intent.putExtra("filePath", fontDO.getFilePath());
            intent.putExtra("fontUrl", fontDO.getUrl());
            LocalBroadcastManager.getInstance(WXEnvironment.getApplication()).sendBroadcast(intent);
        }
        FontAdapter fontAdapter = WXSDKManager.getInstance().getFontAdapter();
        if (fontAdapter != null) {
            fontAdapter.onFontLoad(fontDO.getFontFamilyName(), fontDO.getUrl(), fontDO.getFilePath());
        }
    }

    private static String getFontCacheDir() {
        return WXEnvironment.getApplication().getCacheDir() + "/" + FONT_CACHE_DIR_NAME;
    }
}
