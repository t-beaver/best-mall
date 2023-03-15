package com.taobao.weex.utils;

import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import java.io.File;

public class FontDO {
    public static final int STATE_FAILED = 3;
    public static final int STATE_INIT = 0;
    public static final int STATE_INVALID = -1;
    public static final int STATE_LOADING = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int TYPE_BASE64 = 5;
    public static final int TYPE_FILE = 2;
    public static final int TYPE_LOCAL = 3;
    public static final int TYPE_NATIVE = 4;
    public static final int TYPE_NETWORK = 1;
    public static final int TYPE_UNKNOWN = 0;
    private String mFilePath;
    private final String mFontFamilyName;
    private int mState = -1;
    private int mType = 1;
    private Typeface mTypeface;
    private String mUrl = "";

    public FontDO(String str, String str2, WXSDKInstance wXSDKInstance) {
        this.mFontFamilyName = str;
        parseSrc(str2, wXSDKInstance);
    }

    public FontDO(String str, Typeface typeface) {
        this.mFontFamilyName = str;
        this.mTypeface = typeface;
        this.mType = 4;
        this.mState = 2;
    }

    public String getFontFamilyName() {
        return this.mFontFamilyName;
    }

    private void parseSrc(String str, WXSDKInstance wXSDKInstance) {
        String trim = str != null ? str.trim() : "";
        if (!(wXSDKInstance == null || wXSDKInstance.getCustomFontNetworkHandler() == null)) {
            String fetchLocal = wXSDKInstance.getCustomFontNetworkHandler().fetchLocal(trim);
            if (!TextUtils.isEmpty(fetchLocal)) {
                trim = fetchLocal;
            }
        }
        if (trim.isEmpty()) {
            this.mState = -1;
            WXLogUtils.e("TypefaceUtil", "font src is empty.");
            return;
        }
        if (trim.matches("^url\\((('.*')|(\".*\"))\\)$")) {
            Uri parse = Uri.parse(trim.substring(5, trim.length() - 2));
            if (wXSDKInstance != null) {
                parse = wXSDKInstance.rewriteUri(parse, AbsURIAdapter.FONT);
            }
            this.mUrl = parse.toString();
            try {
                String scheme = parse.getScheme();
                if (!"http".equals(scheme)) {
                    if (!"https".equals(scheme)) {
                        if ("file".equals(scheme)) {
                            this.mType = 2;
                            this.mUrl = parse.getEncodedSchemeSpecificPart();
                        } else if (Constants.Scheme.LOCAL.equals(scheme)) {
                            this.mType = 3;
                        } else if ("data".equals(scheme)) {
                            long currentTimeMillis = System.currentTimeMillis();
                            String[] split = this.mUrl.split(",");
                            if (split != null && split.length == 2) {
                                String str2 = split[0];
                                if (!TextUtils.isEmpty(str2) && str2.endsWith("base64")) {
                                    String str3 = split[1];
                                    if (!TextUtils.isEmpty(str3)) {
                                        String md5 = WXFileUtils.md5(str3);
                                        File file = new File(WXEnvironment.getApplication().getCacheDir(), TypefaceUtil.FONT_CACHE_DIR_NAME);
                                        if (!file.exists()) {
                                            file.mkdirs();
                                        }
                                        File file2 = new File(file, md5);
                                        if (!file2.exists()) {
                                            file2.createNewFile();
                                            WXFileUtils.saveFile(file2.getPath(), Base64.decode(str3, 0), WXEnvironment.getApplication());
                                        }
                                        this.mUrl = file2.getPath();
                                        this.mType = 5;
                                        WXLogUtils.d("TypefaceUtil", "Parse base64 font cost " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
                                    }
                                }
                            }
                        } else {
                            WXLogUtils.e("TypefaceUtil", "Unknown scheme for font url: " + this.mUrl);
                            this.mType = 0;
                        }
                        this.mState = 0;
                    }
                }
                this.mType = 1;
                this.mState = 0;
            } catch (Exception e) {
                this.mType = -1;
                WXLogUtils.e("TypefaceUtil", "URI.create(mUrl) failed mUrl: " + this.mUrl + "\n" + WXLogUtils.getStackTrace(e));
            }
        } else {
            this.mUrl = trim;
            this.mState = -1;
        }
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("TypefaceUtil", "src:" + trim + ", mUrl:" + this.mUrl + ", mType:" + this.mType);
        }
    }

    public String getUrl() {
        return this.mUrl;
    }

    public int getType() {
        return this.mType;
    }

    public Typeface getTypeface() {
        return this.mTypeface;
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
    }
}
