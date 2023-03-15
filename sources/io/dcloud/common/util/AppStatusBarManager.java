package io.dcloud.common.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import io.dcloud.common.DHInterface.IActivityDelegate;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.core.ui.DCKeyboardManager;
import io.dcloud.common.ui.blur.DCBlurDraweeView;
import io.dcloud.e.b.f;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.json.JSONObject;

public class AppStatusBarManager {
    public boolean isFullScreen = false;
    public boolean isHandledWhiteScreen = false;
    public boolean isImmersive = false;
    public boolean isTemporaryFullScreen = true;
    private IApp mIApp;
    private int mStatusBarDefaultColor = 0;
    private f mWebAppInfo;

    public AppStatusBarManager(Activity activity, f fVar) {
        initStatusBarDefaultColor(activity);
        this.mWebAppInfo = fVar;
        this.mIApp = (IApp) fVar;
        initDirectImmersive();
    }

    private void diyContentFullScreenBug(Activity activity) {
        if ((activity instanceof IActivityDelegate ? ((IActivityDelegate) activity).obtainActivityContentView() : null) != null) {
            if (this.isFullScreen || this.isImmersive) {
                String metaValue = AndroidResources.getMetaValue("DCLOUD_INPUT_MODE");
                if (TextUtils.isEmpty(metaValue) || !metaValue.contains("adjustPan")) {
                    DCKeyboardManager.getInstance().setAdjust(true);
                    return;
                }
                return;
            }
            this.isTemporaryFullScreen = false;
            DCKeyboardManager.getInstance().setAdjust(false);
        }
    }

    /* access modifiers changed from: private */
    public View getRootView(Activity activity) {
        return activity.findViewById(16908290);
    }

    private void initDirectImmersive() {
        if ((this.mWebAppInfo.getActivity().getIntent().hasExtra(IntentConst.DIRECT_PAGE) && BaseInfo.isWap2AppAppid(this.mWebAppInfo.p)) && Build.VERSION.SDK_INT > 19) {
            JSONObject directStatusJson = getDirectStatusJson(this.mIApp);
            if (directStatusJson == null || !directStatusJson.has(AbsoluteConst.JSONKEY_STATUSBAR_IMMERSED)) {
                this.isImmersive = true;
            } else {
                this.isImmersive = directStatusJson.optBoolean(AbsoluteConst.JSONKEY_STATUSBAR_IMMERSED);
            }
            BaseInfo.isImmersive = this.isImmersive;
        }
    }

    private void initStatusBarDefaultColor(Activity activity) {
        if (activity != null && Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            int statusbarColorIndex = getStatusbarColorIndex();
            if (statusbarColorIndex > 0) {
                this.mStatusBarDefaultColor = window.getWindowStyle().getColor(statusbarColorIndex, 0);
            }
            if (this.mStatusBarDefaultColor == 0) {
                this.mStatusBarDefaultColor = Color.parseColor("#D4D4D4");
            }
        }
    }

    private void setMeizuStatusBarDarkIcon(Activity activity, boolean z) {
        if (activity != null) {
            try {
                WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt((Object) null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (i ^ -1) & i2);
                activity.getWindow().setAttributes(attributes);
            } catch (Exception unused) {
            }
        }
    }

    private void setMiuiStatusBarDarkMode(Activity activity, boolean z) {
        Class<?> cls = activity.getWindow().getClass();
        try {
            Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
            Class cls3 = Integer.TYPE;
            Method method = cls.getMethod("setExtraFlags", new Class[]{cls3, cls3});
            Window window = activity.getWindow();
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(z ? i : 0);
            objArr[1] = Integer.valueOf(i);
            method.invoke(window, objArr);
        } catch (Exception unused) {
        }
    }

    private void setTranslucentStatus(final Activity activity, boolean z) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (z) {
            attributes.flags |= 67108864;
        } else {
            attributes.flags &= -67108865;
        }
        window.setAttributes(attributes);
        window.getDecorView().post(new Runnable() {
            public void run() {
                View access$000 = AppStatusBarManager.this.getRootView(activity);
                if (access$000.getParent() instanceof LinearLayout) {
                    access$000.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                } else if (access$000.getParent() instanceof FrameLayout) {
                    access$000.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                }
            }
        });
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean checkImmersedStatusBar(android.content.Context r5, boolean r6) {
        /*
            r4 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 0
            r2 = 19
            if (r0 >= r2) goto L_0x0008
            return r1
        L_0x0008:
            android.content.pm.PackageManager r2 = r5.getPackageManager()     // Catch:{ NameNotFoundException -> 0x0023 }
            java.lang.String r5 = r5.getPackageName()     // Catch:{ NameNotFoundException -> 0x0023 }
            r3 = 128(0x80, float:1.794E-43)
            android.content.pm.ApplicationInfo r5 = r2.getApplicationInfo(r5, r3)     // Catch:{ NameNotFoundException -> 0x0023 }
            if (r5 == 0) goto L_0x0027
            android.os.Bundle r5 = r5.metaData     // Catch:{ NameNotFoundException -> 0x0023 }
            if (r5 == 0) goto L_0x0027
            java.lang.String r2 = "immersed.status.bar"
            boolean r5 = r5.getBoolean(r2, r1)     // Catch:{ NameNotFoundException -> 0x0023 }
            goto L_0x0028
        L_0x0023:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0027:
            r5 = 0
        L_0x0028:
            if (r5 != 0) goto L_0x002b
            goto L_0x002c
        L_0x002b:
            r6 = r5
        L_0x002c:
            io.dcloud.e.b.f r5 = r4.mWebAppInfo
            java.lang.String r5 = r5.n
            java.lang.String r2 = "suggestedDevice"
            boolean r5 = r5.equals(r2)
            r2 = 1
            if (r5 == 0) goto L_0x005b
            if (r6 == 0) goto L_0x005b
            java.lang.String r5 = io.dcloud.common.adapter.util.DeviceInfo.sBrand
            java.lang.String r6 = io.dcloud.common.adapter.util.MobilePhoneModel.XIAOMI
            boolean r5 = r5.equalsIgnoreCase(r6)
            if (r5 != 0) goto L_0x004f
            java.lang.String r5 = io.dcloud.common.adapter.util.DeviceInfo.sBrand
            java.lang.String r6 = io.dcloud.common.adapter.util.MobilePhoneModel.MEIZU
            boolean r5 = r5.contentEquals(r6)
            if (r5 == 0) goto L_0x0053
        L_0x004f:
            r5 = 21
            if (r0 >= r5) goto L_0x005a
        L_0x0053:
            r5 = 23
            if (r0 < r5) goto L_0x0058
            goto L_0x005a
        L_0x0058:
            r6 = 0
            goto L_0x005b
        L_0x005a:
            r6 = 1
        L_0x005b:
            r4.isImmersive = r6
            if (r6 != 0) goto L_0x00a1
            io.dcloud.e.b.f r5 = r4.mWebAppInfo
            android.app.Activity r5 = r5.getActivity()
            android.content.Intent r5 = r5.getIntent()
            java.lang.String r0 = "direct_page"
            boolean r5 = r5.hasExtra(r0)
            if (r5 == 0) goto L_0x007c
            io.dcloud.e.b.f r5 = r4.mWebAppInfo
            java.lang.String r5 = r5.p
            boolean r5 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r5)
            if (r5 == 0) goto L_0x007c
            r1 = 1
        L_0x007c:
            if (r1 == 0) goto L_0x00a1
            io.dcloud.common.DHInterface.IApp r5 = r4.mIApp
            boolean r5 = r5.manifestBeParsed()
            if (r5 != 0) goto L_0x00a1
            io.dcloud.common.DHInterface.IApp r5 = r4.mIApp
            org.json.JSONObject r5 = r4.getDirectStatusJson(r5)
            if (r5 == 0) goto L_0x009d
            java.lang.String r6 = "immersed"
            boolean r0 = r5.has(r6)
            if (r0 == 0) goto L_0x009d
            boolean r5 = r5.optBoolean(r6)
            r4.isImmersive = r5
            goto L_0x009f
        L_0x009d:
            r4.isImmersive = r2
        L_0x009f:
            boolean r6 = r4.isImmersive
        L_0x00a1:
            boolean r5 = r4.isImmersive
            io.dcloud.common.util.BaseInfo.isImmersive = r5
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.AppStatusBarManager.checkImmersedStatusBar(android.content.Context, boolean):boolean");
    }

    public JSONObject getDirectStatusJson(IApp iApp) {
        JSONObject obtainThridInfo = iApp.obtainThridInfo(IApp.ConfigProperty.ThridInfo.DirectPageJsonData);
        if (obtainThridInfo == null || !obtainThridInfo.has(AbsoluteConst.JSONKEY_STATUSBAR)) {
            JSONObject obtainThridInfo2 = iApp.obtainThridInfo(IApp.ConfigProperty.ThridInfo.SitemapJsonData);
            if (obtainThridInfo2 == null || !obtainThridInfo2.has(AbsoluteConst.JSONKEY_STATUSBAR)) {
                return null;
            }
            return obtainThridInfo2.optJSONObject(AbsoluteConst.JSONKEY_STATUSBAR);
        }
        JSONObject optJSONObject = obtainThridInfo.optJSONObject(AbsoluteConst.JSONKEY_STATUSBAR);
        if (optJSONObject != null) {
            return optJSONObject;
        }
        return null;
    }

    public int getStatusBarDefaultColor() {
        return this.mStatusBarDefaultColor;
    }

    public int getStatusbarColorIndex() {
        try {
            return Integer.parseInt(PlatformUtil.invokeFieldValue((String) null, "Window_statusBarColor", PlatformUtil.newInstance(Base64.decode2String("Y29tLmFuZHJvaWQuaW50ZXJuYWwuUiRzdHlsZWFibGU="), (Class[]) null, (Object[]) null)).toString());
        } catch (Exception unused) {
            return -1;
        }
    }

    public boolean isFullScreenOrImmersive() {
        return this.isImmersive || this.isFullScreen;
    }

    public void setFullScreen(Activity activity, boolean z) {
        Window window = activity.getWindow();
        this.isFullScreen = z;
        if (z) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags |= 1024;
            if (Build.VERSION.SDK_INT >= 28) {
                attributes.layoutInDisplayCutoutMode = 1;
            }
            window.setAttributes(attributes);
        } else {
            WindowManager.LayoutParams attributes2 = window.getAttributes();
            attributes2.flags &= -1025;
            if (Build.VERSION.SDK_INT >= 28) {
                attributes2.layoutInDisplayCutoutMode = 0;
            }
            window.setAttributes(attributes2);
        }
        diyContentFullScreenBug(activity);
    }

    public void setImmersive(final Activity activity, boolean z) {
        if (activity != null) {
            int i = Build.VERSION.SDK_INT;
            if (i == 19 || ((DeviceInfo.sBrand.equalsIgnoreCase(MobilePhoneModel.SONY) && i >= 21) || (DeviceInfo.sBrand.equalsIgnoreCase(MobilePhoneModel.QiKU) && i >= 21))) {
                this.isImmersive = z;
                setTranslucentStatus(activity, z);
                diyContentFullScreenBug(activity);
            } else if (i >= 21) {
                this.isImmersive = z;
                Window window = activity.getWindow();
                int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
                window.clearFlags(1024);
                if (z) {
                    window.clearFlags(67108864);
                    window.addFlags(Integer.MIN_VALUE);
                    window.getDecorView().setSystemUiVisibility(systemUiVisibility | 1280);
                    window.setStatusBarColor(0);
                } else {
                    window.getDecorView().setSystemUiVisibility(systemUiVisibility & -1281);
                    window.setStatusBarColor(this.mStatusBarDefaultColor);
                }
                window.getDecorView().post(new Runnable() {
                    public void run() {
                        View access$000 = AppStatusBarManager.this.getRootView(activity);
                        if (access$000.getParent() instanceof LinearLayout) {
                            access$000.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                        } else if (access$000.getParent() instanceof FrameLayout) {
                            access$000.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                        }
                    }
                });
                diyContentFullScreenBug(activity);
            }
        }
    }

    public void setStatusBarColor(Activity activity, int i) {
        if (PdrUtil.checkStatusbarColor(i) && Build.VERSION.SDK_INT >= 21 && activity != null && !this.isImmersive) {
            Window window = activity.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(i);
        }
    }

    public void setStatusBarMode(Activity activity, String str) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 21 && activity != null) {
            if (PdrUtil.isEmpty(str)) {
                str = "nono";
            }
            boolean equalsIgnoreCase = str.equalsIgnoreCase(DCBlurDraweeView.DARK);
            Window window = activity.getWindow();
            String str2 = Build.BRAND;
            if (str2.equalsIgnoreCase(MobilePhoneModel.GOOGLE)) {
                str2 = Build.MANUFACTURER;
            }
            if (str2.equals(MobilePhoneModel.XIAOMI)) {
                setMiuiStatusBarDarkMode(activity, equalsIgnoreCase);
            } else if (str2.equals(MobilePhoneModel.MEIZU)) {
                setMeizuStatusBarDarkIcon(activity, equalsIgnoreCase);
            }
            if (i >= 23) {
                int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
                int i2 = 8192;
                try {
                    Class<?> cls = Class.forName("android.view.View");
                    i2 = cls.getField("SYSTEM_UI_FLAG_LIGHT_STATUS_BAR").getInt(cls);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                window.getDecorView().setSystemUiVisibility(equalsIgnoreCase ? systemUiVisibility | i2 : systemUiVisibility & (i2 ^ -1));
            }
        }
    }
}
