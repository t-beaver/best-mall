package io.dcloud.feature.ui.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import com.dcloud.android.widget.toast.ToastCompat;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ShortCutUtil;
import io.dcloud.common.util.ShortcutCreateUtil;
import io.dcloud.common.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class NavigatorUIFeatureImpl implements IFeature {
    AbsMgr a;

    class a extends PermissionUtil.StreamPermissionRequest {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String[] b;
        final /* synthetic */ IApp c;
        final /* synthetic */ String d;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        a(IApp iApp, IWebview iWebview, String[] strArr, IApp iApp2, String str) {
            super(iApp);
            this.a = iWebview;
            this.b = strArr;
            this.c = iApp2;
            this.d = str;
        }

        public void onDenied(String str) {
        }

        public void onGranted(String str) {
            boolean unused = NavigatorUIFeatureImpl.this.a(this.a, this.b, this.c, this.d);
        }
    }

    class b implements ISysEventListener {
        final /* synthetic */ int a;
        final /* synthetic */ IApp b;
        final /* synthetic */ IWebview c;
        final /* synthetic */ String d;
        final /* synthetic */ String e;

        b(int i, IApp iApp, IWebview iWebview, String str, String str2) {
            this.a = i;
            this.b = iApp;
            this.c = iWebview;
            this.d = str;
            this.e = str2;
        }

        public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
            int i;
            Object[] objArr = (Object[]) obj;
            int intValue = ((Integer) objArr[0]).intValue();
            String[] strArr = (String[]) objArr[1];
            int[] iArr = (int[]) objArr[2];
            ISysEventListener.SysEventType sysEventType2 = ISysEventListener.SysEventType.onRequestPermissionsResult;
            if (sysEventType2 == sysEventType && intValue == this.a) {
                this.b.unregisterSysEventListener(this, sysEventType2);
                if (iArr.length > 0) {
                    i = iArr[0];
                } else {
                    i = this.c.obtainApp().checkSelfPermission(this.d, this.c.obtainApp().obtainAppName());
                }
                String convert5PlusValue = PermissionUtil.convert5PlusValue(i);
                Deprecated_JSUtil.execCallback(this.c, this.e, StringUtil.format("{result:'%s'}", convert5PlusValue), JSUtil.OK, true, false);
            }
            return true;
        }
    }

    class c implements Runnable {
        final /* synthetic */ Context a;
        final /* synthetic */ String b;
        final /* synthetic */ IWebview c;
        final /* synthetic */ String d;

        c(Context context, String str, IWebview iWebview, String str2) {
            this.a = context;
            this.b = str;
            this.c = iWebview;
            this.d = str2;
        }

        public void run() {
            try {
                JSUtil.execCallback(this.c, this.d, new JSONObject(StringUtil.format(DOMException.JSON_SHORTCUT_SUCCESS_INFO, ShortCutUtil.SHORT_CUT_EXISTING.equals(ShortCutUtil.requestShortCutForCommit(this.a, this.b)) ? AbsoluteConst.TRUE : AbsoluteConst.FALSE)), JSUtil.OK, false);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void b(Context context, IWebview iWebview, String str, String str2) {
        String str3;
        String requestShortCut = ShortCutUtil.requestShortCut(context, str2);
        if (ShortCutUtil.SHORT_CUT_EXISTING.equals(requestShortCut)) {
            str3 = StringUtil.format(DOMException.JSON_SHORTCUT_RESULT_INFO, "existing");
        } else if (ShortCutUtil.SHORT_CUT_NONE.equals(requestShortCut)) {
            str3 = StringUtil.format(DOMException.JSON_SHORTCUT_RESULT_INFO, "none");
        } else if (ShortCutUtil.NOPERMISSIONS.equals(requestShortCut)) {
            str3 = StringUtil.format(DOMException.JSON_SHORTCUT_RESULT_INFO, ShortCutUtil.NOPERMISSIONS);
        } else {
            str3 = StringUtil.format(DOMException.JSON_SHORTCUT_RESULT_INFO, "unknown");
        }
        try {
            JSUtil.execCallback(iWebview, str, new JSONObject(str3), JSUtil.OK, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void dispose(String str) {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r17, java.lang.String r18, java.lang.String[] r19) {
        /*
            r16 = this;
            r8 = r16
            r5 = r17
            r0 = r18
            r6 = r19
            java.lang.String r1 = "delay_w2a"
            java.lang.String r2 = "autoclose_w2a"
            java.lang.String r3 = "delay"
            java.lang.String r4 = "autoclose"
            io.dcloud.common.DHInterface.IApp r9 = r17.obtainApp()
            java.lang.String r7 = r9.obtainAppId()
            r18.hashCode()
            int r10 = r18.hashCode()
            switch(r10) {
                case -2079769446: goto L_0x01b3;
                case -1980692731: goto L_0x01a8;
                case -1921914628: goto L_0x019d;
                case -1763010304: goto L_0x0192;
                case -1294581845: goto L_0x0187;
                case -1250806682: goto L_0x017c;
                case -1180327431: goto L_0x0171;
                case -831443264: goto L_0x0166;
                case -802912774: goto L_0x0158;
                case -583672202: goto L_0x014a;
                case -452882469: goto L_0x013c;
                case -108255335: goto L_0x012e;
                case 126640486: goto L_0x0120;
                case 204345677: goto L_0x0112;
                case 301825860: goto L_0x0104;
                case 341257562: goto L_0x00f6;
                case 580068706: goto L_0x00e8;
                case 586449341: goto L_0x00da;
                case 586897223: goto L_0x00cc;
                case 686218487: goto L_0x00be;
                case 746581438: goto L_0x00b0;
                case 839078392: goto L_0x00a2;
                case 1063979522: goto L_0x0094;
                case 1094478863: goto L_0x0086;
                case 1204872973: goto L_0x0078;
                case 1217359681: goto L_0x006a;
                case 1365206181: goto L_0x005c;
                case 1841443122: goto L_0x004e;
                case 1850818488: goto L_0x0040;
                case 1984754993: goto L_0x0032;
                case 2104007794: goto L_0x0024;
                default: goto L_0x0022;
            }
        L_0x0022:
            goto L_0x01be
        L_0x0024:
            java.lang.String r10 = "setStatusBarStyle"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x002e
            goto L_0x01be
        L_0x002e:
            r10 = 30
            goto L_0x01bf
        L_0x0032:
            java.lang.String r10 = "setLogs"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x003c
            goto L_0x01be
        L_0x003c:
            r10 = 29
            goto L_0x01bf
        L_0x0040:
            java.lang.String r10 = "setUserAgent"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x004a
            goto L_0x01be
        L_0x004a:
            r10 = 28
            goto L_0x01bf
        L_0x004e:
            java.lang.String r10 = "getStatusbarHeight"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0058
            goto L_0x01be
        L_0x0058:
            r10 = 27
            goto L_0x01bf
        L_0x005c:
            java.lang.String r10 = "isFullScreen"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0066
            goto L_0x01be
        L_0x0066:
            r10 = 26
            goto L_0x01bf
        L_0x006a:
            java.lang.String r10 = "removeAllCookie"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0074
            goto L_0x01be
        L_0x0074:
            r10 = 25
            goto L_0x01bf
        L_0x0078:
            java.lang.String r10 = "setStatusBarBackground"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0082
            goto L_0x01be
        L_0x0082:
            r10 = 24
            goto L_0x01bf
        L_0x0086:
            java.lang.String r10 = "hasNotchInScreen"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0090
            goto L_0x01be
        L_0x0090:
            r10 = 23
            goto L_0x01bf
        L_0x0094:
            java.lang.String r10 = "getSignature"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x009e
            goto L_0x01be
        L_0x009e:
            r10 = 22
            goto L_0x01bf
        L_0x00a2:
            java.lang.String r10 = "isBackground"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x00ac
            goto L_0x01be
        L_0x00ac:
            r10 = 21
            goto L_0x01bf
        L_0x00b0:
            java.lang.String r10 = "requestPermission"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x00ba
            goto L_0x01be
        L_0x00ba:
            r10 = 20
            goto L_0x01bf
        L_0x00be:
            java.lang.String r10 = "checkPermission"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x00c8
            goto L_0x01be
        L_0x00c8:
            r10 = 19
            goto L_0x01bf
        L_0x00cc:
            java.lang.String r10 = "getUiStyle"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x00d6
            goto L_0x01be
        L_0x00d6:
            r10 = 18
            goto L_0x01bf
        L_0x00da:
            java.lang.String r10 = "setFullscreen"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x00e4
            goto L_0x01be
        L_0x00e4:
            r10 = 17
            goto L_0x01bf
        L_0x00e8:
            java.lang.String r10 = "createShortcut"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x00f2
            goto L_0x01be
        L_0x00f2:
            r10 = 16
            goto L_0x01bf
        L_0x00f6:
            java.lang.String r10 = "getCookie"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0100
            goto L_0x01be
        L_0x0100:
            r10 = 15
            goto L_0x01bf
        L_0x0104:
            java.lang.String r10 = "getUserAgent"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x010e
            goto L_0x01be
        L_0x010e:
            r10 = 14
            goto L_0x01bf
        L_0x0112:
            java.lang.String r10 = "hasSplashscreen"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x011c
            goto L_0x01be
        L_0x011c:
            r10 = 13
            goto L_0x01bf
        L_0x0120:
            java.lang.String r10 = "setCookie"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x012a
            goto L_0x01be
        L_0x012a:
            r10 = 12
            goto L_0x01bf
        L_0x012e:
            java.lang.String r10 = "getStatusBarBackground"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0138
            goto L_0x01be
        L_0x0138:
            r10 = 11
            goto L_0x01bf
        L_0x013c:
            java.lang.String r10 = "isImmersedStatusbar"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0146
            goto L_0x01be
        L_0x0146:
            r10 = 10
            goto L_0x01bf
        L_0x014a:
            java.lang.String r10 = "removeSessionCookie"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0154
            goto L_0x01be
        L_0x0154:
            r10 = 9
            goto L_0x01bf
        L_0x0158:
            java.lang.String r10 = "isSimulator"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0162
            goto L_0x01be
        L_0x0162:
            r10 = 8
            goto L_0x01bf
        L_0x0166:
            java.lang.String r10 = "showSystemNavigation"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x016f
            goto L_0x01be
        L_0x016f:
            r10 = 7
            goto L_0x01bf
        L_0x0171:
            java.lang.String r10 = "isLogs"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x017a
            goto L_0x01be
        L_0x017a:
            r10 = 6
            goto L_0x01bf
        L_0x017c:
            java.lang.String r10 = "getStatusBarStyle"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0185
            goto L_0x01be
        L_0x0185:
            r10 = 5
            goto L_0x01bf
        L_0x0187:
            java.lang.String r10 = "closeSplashscreen"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x0190
            goto L_0x01be
        L_0x0190:
            r10 = 4
            goto L_0x01bf
        L_0x0192:
            java.lang.String r10 = "hasShortcut"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x019b
            goto L_0x01be
        L_0x019b:
            r10 = 3
            goto L_0x01bf
        L_0x019d:
            java.lang.String r10 = "updateSplashscreen"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x01a6
            goto L_0x01be
        L_0x01a6:
            r10 = 2
            goto L_0x01bf
        L_0x01a8:
            java.lang.String r10 = "hideSystemNavigation"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x01b1
            goto L_0x01be
        L_0x01b1:
            r10 = 1
            goto L_0x01bf
        L_0x01b3:
            java.lang.String r10 = "getOrientation"
            boolean r10 = r0.equals(r10)
            if (r10 != 0) goto L_0x01bc
            goto L_0x01be
        L_0x01bc:
            r10 = 0
            goto L_0x01bf
        L_0x01be:
            r10 = -1
        L_0x01bf:
            java.lang.String r11 = "h5plus"
            java.lang.String r14 = "useragent"
            java.lang.String r13 = "funSetUA"
            java.lang.String r15 = "status_bar_mode"
            java.lang.String r12 = "Navigator-"
            switch(r10) {
                case 0: goto L_0x05fa;
                case 1: goto L_0x05d1;
                case 2: goto L_0x04e9;
                case 3: goto L_0x04ad;
                case 4: goto L_0x0453;
                case 5: goto L_0x0449;
                case 6: goto L_0x043f;
                case 7: goto L_0x041d;
                case 8: goto L_0x03f5;
                case 9: goto L_0x03e9;
                case 10: goto L_0x03c9;
                case 11: goto L_0x03ad;
                case 12: goto L_0x03a2;
                case 13: goto L_0x0392;
                case 14: goto L_0x0321;
                case 15: goto L_0x0318;
                case 16: goto L_0x02e6;
                case 17: goto L_0x02d6;
                case 18: goto L_0x02c1;
                case 19: goto L_0x02b7;
                case 20: goto L_0x028c;
                case 21: goto L_0x027c;
                case 22: goto L_0x026e;
                case 23: goto L_0x0260;
                case 24: goto L_0x0234;
                case 25: goto L_0x0228;
                case 26: goto L_0x021e;
                case 27: goto L_0x0209;
                case 28: goto L_0x01f1;
                case 29: goto L_0x01e1;
                case 30: goto L_0x01ce;
                default: goto L_0x01cc;
            }
        L_0x01cc:
            goto L_0x04aa
        L_0x01ce:
            r10 = 0
            r0 = r6[r10]
            r9.setConfigProperty(r15, r0)
            io.dcloud.common.util.AppStatusBarManager r1 = r9.obtainStatusBarMgr()
            android.app.Activity r2 = r9.getActivity()
            r1.setStatusBarMode(r2, r0)
            goto L_0x04aa
        L_0x01e1:
            r10 = 0
            r0 = r6[r10]
            java.lang.String r0 = java.lang.String.valueOf(r0)
            boolean r0 = io.dcloud.common.util.PdrUtil.parseBoolean(r0, r10, r10)
            io.dcloud.common.adapter.util.Logger.setOpen(r0)
            goto L_0x04aa
        L_0x01f1:
            r10 = 0
            r0 = r6[r10]
            r1 = 1
            r1 = r6[r1]
            r9.setConfigProperty(r14, r0)
            java.lang.String r2 = "true"
            r9.setConfigProperty(r13, r2)
            r9.setConfigProperty(r11, r1)
            java.lang.String r1 = "User-Agent"
            r5.setWebviewProperty(r1, r0)
            goto L_0x04aa
        L_0x0209:
            android.app.Activity r0 = r17.getActivity()
            io.dcloud.common.adapter.util.DeviceInfo.updateStatusBarHeight(r0)
            int r0 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            float r0 = (float) r0
            float r1 = r17.getScale()
            float r0 = r0 / r1
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((float) r0)
            goto L_0x062a
        L_0x021e:
            boolean r0 = r9.isFullScreen()
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r0)
            goto L_0x062a
        L_0x0228:
            r17.removeAllCookie()     // Catch:{ Exception -> 0x022d }
            goto L_0x04aa
        L_0x022d:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
            goto L_0x04aa
        L_0x0234:
            r0 = 0
            r1 = r6[r0]
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto L_0x04aa
            int r1 = android.graphics.Color.parseColor(r1)     // Catch:{ Exception -> 0x0242 }
            goto L_0x0246
        L_0x0242:
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r1)
        L_0x0246:
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 21
            if (r2 < r3) goto L_0x04aa
            r0 = r6[r0]
            java.lang.String r2 = "StatusBarBackground"
            r9.setConfigProperty(r2, r0)
            io.dcloud.common.util.AppStatusBarManager r0 = r9.obtainStatusBarMgr()
            android.app.Activity r2 = r9.getActivity()
            r0.setStatusBarColor(r2, r1)
            goto L_0x04aa
        L_0x0260:
            android.app.Activity r0 = r17.getActivity()
            boolean r0 = io.dcloud.feature.ui.navigator.QueryNotchTool.hasNotchInScreen(r0)
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r0)
            goto L_0x062a
        L_0x026e:
            android.content.Context r0 = r17.getContext()
            java.lang.String r0 = io.dcloud.common.util.LoadAppUtils.getAppSignatureSHA1(r0)
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((java.lang.String) r0)
            goto L_0x062a
        L_0x027c:
            byte r0 = r9.obtainAppStatus()
            r1 = 2
            if (r0 != r1) goto L_0x0285
            r15 = 1
            goto L_0x0286
        L_0x0285:
            r15 = 0
        L_0x0286:
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r15)
            goto L_0x062a
        L_0x028c:
            r0 = 0
            r1 = r6[r0]
            r0 = 1
            r7 = r6[r0]
            int r0 = io.dcloud.common.adapter.util.PermissionUtil.getRequestCode()
            java.lang.String r10 = io.dcloud.common.adapter.util.PermissionUtil.convertNativePermission(r1)
            io.dcloud.feature.ui.navigator.NavigatorUIFeatureImpl$b r11 = new io.dcloud.feature.ui.navigator.NavigatorUIFeatureImpl$b
            r1 = r11
            r2 = r16
            r3 = r0
            r4 = r9
            r5 = r17
            r6 = r10
            r1.<init>(r3, r4, r5, r6, r7)
            io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onRequestPermissionsResult
            r9.registerSysEventListener(r11, r1)
            r1 = 1
            java.lang.String[] r1 = new java.lang.String[r1]
            r2 = 0
            r1[r2] = r10
            r9.requestPermissions(r1, r0)
            goto L_0x04aa
        L_0x02b7:
            java.lang.String r0 = io.dcloud.common.adapter.util.PermissionUtil.checkPermission(r5, r6)
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((java.lang.String) r0)
            goto L_0x062a
        L_0x02c1:
            android.content.Context r0 = r17.getContext()
            boolean r0 = io.dcloud.common.util.AppRuntime.getAppDarkMode(r0)
            if (r0 == 0) goto L_0x02ce
            java.lang.String r0 = "dark"
            goto L_0x02d0
        L_0x02ce:
            java.lang.String r0 = "light"
        L_0x02d0:
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((java.lang.String) r0)
            goto L_0x062a
        L_0x02d6:
            r0 = 0
            r1 = r6[r0]
            java.lang.String r1 = java.lang.String.valueOf(r1)
            boolean r0 = io.dcloud.common.util.PdrUtil.parseBoolean(r1, r0, r0)
            r9.setFullScreen(r0)
            goto L_0x04aa
        L_0x02e6:
            android.content.Context r1 = r17.getContext()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r12)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r1, r0)
            android.app.Activity r0 = r17.getActivity()
            io.dcloud.feature.ui.navigator.NavigatorUIFeatureImpl$a r10 = new io.dcloud.feature.ui.navigator.NavigatorUIFeatureImpl$a
            r1 = r10
            r2 = r16
            r3 = r9
            r4 = r17
            r5 = r19
            r6 = r9
            r1.<init>(r3, r4, r5, r6, r7)
            java.lang.String r1 = "Navigator"
            java.lang.String r2 = "SHORTCUT"
            r3 = 2
            io.dcloud.common.adapter.util.PermissionUtil.usePermission(r0, r1, r2, r3, r10)
            goto L_0x04aa
        L_0x0318:
            r0 = 0
            r0 = r6[r0]
            java.lang.String r12 = r5.getCookie(r0)
            goto L_0x062a
        L_0x0321:
            java.lang.String r0 = r9.obtainConfigProperty(r13)
            boolean r0 = java.lang.Boolean.parseBoolean(r0)
            io.dcloud.common.DHInterface.IApp r1 = r17.obtainApp()
            java.lang.String r1 = r1.obtainConfigProperty(r14)
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x0339
            java.lang.String r1 = ""
        L_0x0339:
            if (r0 != 0) goto L_0x062b
            java.lang.String r0 = "concatenate"
            java.lang.String r0 = r9.obtainConfigProperty(r0)
            boolean r0 = java.lang.Boolean.parseBoolean(r0)
            java.lang.String r2 = r9.obtainConfigProperty(r11)
            boolean r2 = java.lang.Boolean.parseBoolean(r2)
            if (r0 == 0) goto L_0x0365
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = io.dcloud.common.util.BaseInfo.sDefWebViewUserAgent
            r0.append(r3)
            java.lang.String r3 = " "
            r0.append(r3)
            r0.append(r1)
            java.lang.String r1 = r0.toString()
        L_0x0365:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r1)
            if (r0 == 0) goto L_0x037d
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r1)
            java.lang.String r1 = io.dcloud.common.util.BaseInfo.sDefWebViewUserAgent
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r1 = r0
        L_0x037d:
            if (r2 == 0) goto L_0x062b
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r1)
            java.lang.String r1 = " Html5Plus/1.0"
            r0.append(r1)
            java.lang.String r12 = r0.toString()
            goto L_0x062a
        L_0x0392:
            io.dcloud.common.DHInterface.IWebAppRootView r0 = r9.obtainWebAppRootView()
            boolean r0 = r0.didCloseSplash()
            r1 = 1
            r0 = r0 ^ r1
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r0)
            goto L_0x062a
        L_0x03a2:
            r0 = 0
            r1 = 1
            r0 = r6[r0]
            r1 = r6[r1]
            r5.setCookie(r0, r1)
            goto L_0x04aa
        L_0x03ad:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 21
            if (r0 < r1) goto L_0x04aa
            android.app.Activity r0 = r9.getActivity()
            android.view.Window r0 = r0.getWindow()
            int r0 = r0.getStatusBarColor()
            java.lang.String r0 = io.dcloud.common.util.PdrUtil.toHexFromColor(r0)
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((java.lang.String) r0)
            goto L_0x062a
        L_0x03c9:
            java.lang.String r0 = "immersed"
            java.lang.String r0 = r9.obtainConfigProperty(r0)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            boolean r0 = r0.booleanValue()
            io.dcloud.common.util.AppStatusBarManager r1 = r9.obtainStatusBarMgr()
            android.app.Activity r2 = r17.getActivity()
            boolean r0 = r1.checkImmersedStatusBar(r2, r0)
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r0)
            goto L_0x062a
        L_0x03e9:
            r17.removeSessionCookie()     // Catch:{ Exception -> 0x03ee }
            goto L_0x04aa
        L_0x03ee:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
            goto L_0x04aa
        L_0x03f5:
            android.content.Context r1 = r17.getContext()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r12)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r1, r0)
            io.dcloud.common.util.emulator.EmulatorCheckUtil r0 = io.dcloud.common.util.emulator.EmulatorCheckUtil.getSingleInstance()
            android.content.Context r1 = r17.getContext()
            boolean r0 = r0.emulatorCheck(r1)
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r0)
            goto L_0x062a
        L_0x041d:
            android.app.Activity r0 = r9.getActivity()
            android.view.Window r0 = r0.getWindow()
            android.view.View r1 = r0.getDecorView()
            int r1 = r1.getSystemUiVisibility()
            r1 = r1 & -515(0xfffffffffffffdfd, float:NaN)
            android.view.View r0 = r0.getDecorView()
            r0.setSystemUiVisibility(r1)
            io.dcloud.common.DHInterface.IApp r0 = r17.obtainApp()
            r1 = 0
            r0.setHideNavBarState(r1)
            goto L_0x04aa
        L_0x043f:
            boolean r0 = io.dcloud.common.adapter.util.Logger.isOpen()
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r0)
            goto L_0x062a
        L_0x0449:
            java.lang.String r0 = r9.obtainConfigProperty(r15)
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((java.lang.String) r0)
            goto L_0x062a
        L_0x0453:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "appid="
            r0.append(r1)
            r0.append(r7)
            java.lang.String r1 = " closeSplashscreen"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r2 = "Main_Path"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r2, (java.lang.String) r0)
            java.lang.String r0 = io.dcloud.common.util.TestUtil.START_STREAM_APP
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "closeSplashscreen appid="
            r2.append(r3)
            r2.append(r7)
            java.lang.String r2 = r2.toString()
            io.dcloud.common.util.TestUtil.print(r0, r2)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "javascript webapp task begin success appid="
            r0.append(r2)
            r0.append(r7)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "download_manager"
            io.dcloud.common.adapter.util.Logger.i(r1, r0)
            io.dcloud.common.DHInterface.AbsMgr r0 = r8.a
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            io.dcloud.common.DHInterface.IFrameView r2 = r17.obtainFrameView()
            r3 = 11
            r0.processEvent(r1, r3, r2)
        L_0x04aa:
            r11 = 0
            goto L_0x0629
        L_0x04ad:
            android.content.Context r1 = r17.getContext()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r12)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r1, r0)
            r0 = 0
            r0 = r6[r0]
            r1 = 1
            r1 = r6[r1]
            io.dcloud.common.DHInterface.IApp r2 = r17.obtainApp()
            java.lang.String r2 = r2.obtainAppName()
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x04dd }
            r3.<init>(r0)     // Catch:{ JSONException -> 0x04dd }
            java.lang.String r0 = "name"
            java.lang.String r2 = r3.optString(r0, r2)     // Catch:{ JSONException -> 0x04dd }
            goto L_0x04e1
        L_0x04dd:
            r0 = move-exception
            r0.printStackTrace()
        L_0x04e1:
            android.content.Context r0 = r17.getContext()
            r8.b(r0, r5, r1, r2)
            goto L_0x04aa
        L_0x04e9:
            r0 = 0
            r6 = r6[r0]
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x05cb }
            r0.<init>(r6)     // Catch:{ JSONException -> 0x05cb }
            android.content.Context r6 = r17.getContext()     // Catch:{ JSONException -> 0x05cb }
            java.lang.String r7 = "pdr"
            android.content.SharedPreferences r6 = io.dcloud.common.adapter.util.SP.getOrCreateBundle((android.content.Context) r6, (java.lang.String) r7)     // Catch:{ JSONException -> 0x05cb }
            android.content.SharedPreferences$Editor r6 = r6.edit()     // Catch:{ JSONException -> 0x05cb }
            java.lang.String r7 = r9.obtainAppId()     // Catch:{ JSONException -> 0x05cb }
            java.lang.String r10 = "image"
            r11 = 0
            java.lang.String r10 = r0.optString(r10, r11)     // Catch:{ JSONException -> 0x05c9 }
            boolean r12 = android.text.TextUtils.isEmpty(r10)     // Catch:{ JSONException -> 0x05c9 }
            if (r12 != 0) goto L_0x0547
            java.lang.String r5 = r17.obtainFullUrl()     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r5 = r9.convert2AbsFullPath(r5, r10)     // Catch:{ JSONException -> 0x05c9 }
            boolean r10 = io.dcloud.common.util.PdrUtil.isDeviceRootDir(r5)     // Catch:{ JSONException -> 0x05c9 }
            if (r10 == 0) goto L_0x0542
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x05c9 }
            r10.<init>()     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r12 = io.dcloud.common.constant.StringConst.STREAMAPP_KEY_ROOTPATH     // Catch:{ JSONException -> 0x05c9 }
            r10.append(r12)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r12 = "splash/"
            r10.append(r12)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r9 = r9.obtainAppId()     // Catch:{ JSONException -> 0x05c9 }
            r10.append(r9)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r9 = ".png"
            r10.append(r9)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r9 = r10.toString()     // Catch:{ JSONException -> 0x05c9 }
            r10 = 1
            r12 = 0
            io.dcloud.common.adapter.io.DHFile.copyFile(r5, r9, r10, r12)     // Catch:{ JSONException -> 0x05c9 }
        L_0x0542:
            java.lang.String r9 = "update_splash_img_path"
            r6.putString(r9, r5)     // Catch:{ JSONException -> 0x05c9 }
        L_0x0547:
            boolean r5 = r0.isNull(r4)     // Catch:{ JSONException -> 0x05c9 }
            if (r5 != 0) goto L_0x0565
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x05c9 }
            r5.<init>()     // Catch:{ JSONException -> 0x05c9 }
            r5.append(r7)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r9 = "__update_splash_autoclose"
            r5.append(r9)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r5 = r5.toString()     // Catch:{ JSONException -> 0x05c9 }
            boolean r4 = r0.optBoolean(r4)     // Catch:{ JSONException -> 0x05c9 }
            r6.putBoolean(r5, r4)     // Catch:{ JSONException -> 0x05c9 }
        L_0x0565:
            boolean r4 = r0.isNull(r3)     // Catch:{ JSONException -> 0x05c9 }
            if (r4 != 0) goto L_0x0583
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x05c9 }
            r4.<init>()     // Catch:{ JSONException -> 0x05c9 }
            r4.append(r7)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r5 = "__update_splash_delay"
            r4.append(r5)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r4 = r4.toString()     // Catch:{ JSONException -> 0x05c9 }
            int r3 = r0.optInt(r3)     // Catch:{ JSONException -> 0x05c9 }
            r6.putInt(r4, r3)     // Catch:{ JSONException -> 0x05c9 }
        L_0x0583:
            boolean r3 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r7)     // Catch:{ JSONException -> 0x05c9 }
            if (r3 == 0) goto L_0x05c5
            boolean r3 = r0.isNull(r2)     // Catch:{ JSONException -> 0x05c9 }
            if (r3 != 0) goto L_0x05a7
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x05c9 }
            r3.<init>()     // Catch:{ JSONException -> 0x05c9 }
            r3.append(r7)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r4 = "__update_splash_autoclose_w2a"
            r3.append(r4)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r3 = r3.toString()     // Catch:{ JSONException -> 0x05c9 }
            boolean r2 = r0.optBoolean(r2)     // Catch:{ JSONException -> 0x05c9 }
            r6.putBoolean(r3, r2)     // Catch:{ JSONException -> 0x05c9 }
        L_0x05a7:
            boolean r2 = r0.isNull(r1)     // Catch:{ JSONException -> 0x05c9 }
            if (r2 != 0) goto L_0x05c5
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x05c9 }
            r2.<init>()     // Catch:{ JSONException -> 0x05c9 }
            r2.append(r7)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r3 = "__update_splash_delay_w2a"
            r2.append(r3)     // Catch:{ JSONException -> 0x05c9 }
            java.lang.String r2 = r2.toString()     // Catch:{ JSONException -> 0x05c9 }
            int r0 = r0.optInt(r1)     // Catch:{ JSONException -> 0x05c9 }
            r6.putInt(r2, r0)     // Catch:{ JSONException -> 0x05c9 }
        L_0x05c5:
            r6.commit()     // Catch:{ JSONException -> 0x05c9 }
            goto L_0x0629
        L_0x05c9:
            r0 = move-exception
            goto L_0x05cd
        L_0x05cb:
            r0 = move-exception
            r11 = 0
        L_0x05cd:
            r0.printStackTrace()
            goto L_0x0629
        L_0x05d1:
            r11 = 0
            android.app.Activity r0 = r9.getActivity()
            android.view.Window r0 = r0.getWindow()
            android.view.View r1 = r0.getDecorView()
            int r1 = r1.getSystemUiVisibility()
            android.view.View r0 = r0.getDecorView()
            r1 = r1 | 256(0x100, float:3.59E-43)
            r1 = r1 | 512(0x200, float:7.175E-43)
            r2 = 2
            r1 = r1 | r2
            r1 = r1 | 4096(0x1000, float:5.74E-42)
            r0.setSystemUiVisibility(r1)
            io.dcloud.common.DHInterface.IApp r0 = r17.obtainApp()
            r1 = 1
            r0.setHideNavBarState(r1)
            goto L_0x0629
        L_0x05fa:
            r1 = 1
            r12 = 0
            android.app.Activity r0 = r17.getActivity()     // Catch:{ Exception -> 0x0623 }
            android.view.WindowManager r0 = r0.getWindowManager()     // Catch:{ Exception -> 0x0623 }
            android.view.Display r0 = r0.getDefaultDisplay()     // Catch:{ Exception -> 0x0623 }
            int r0 = r0.getRotation()     // Catch:{ Exception -> 0x0623 }
            if (r0 == r1) goto L_0x061b
            r1 = 2
            if (r0 == r1) goto L_0x0618
            r1 = 3
            if (r0 == r1) goto L_0x0615
            goto L_0x061d
        L_0x0615:
            r12 = -90
            goto L_0x061d
        L_0x0618:
            r12 = 180(0xb4, float:2.52E-43)
            goto L_0x061d
        L_0x061b:
            r12 = 90
        L_0x061d:
            float r0 = (float) r12     // Catch:{ Exception -> 0x0623 }
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((float) r0)     // Catch:{ Exception -> 0x0623 }
            goto L_0x062a
        L_0x0623:
            r0 = 0
            java.lang.String r12 = io.dcloud.common.util.JSUtil.wrapJsVar((float) r0)
            goto L_0x062a
        L_0x0629:
            r12 = r11
        L_0x062a:
            r1 = r12
        L_0x062b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.navigator.NavigatorUIFeatureImpl.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public void init(AbsMgr absMgr, String str) {
        this.a = absMgr;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a1 A[Catch:{ Exception -> 0x00d5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00af  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00bd A[Catch:{ Exception -> 0x00d3 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a(io.dcloud.common.DHInterface.IWebview r17, java.lang.String[] r18, io.dcloud.common.DHInterface.IApp r19, java.lang.String r20) {
        /*
            r16 = this;
            r1 = r19
            java.lang.String r0 = "toast"
            java.lang.String r2 = ""
            r3 = 0
            r4 = r18[r3]
            r5 = 1
            r15 = r18[r5]
            android.content.Context r6 = r17.getContext()
            int r7 = io.dcloud.base.R.string.dcloud_short_cut_created
            java.lang.String r6 = r6.getString(r7)
            java.lang.Object[] r7 = new java.lang.Object[r5]
            java.lang.String r8 = r19.obtainAppName()
            r7[r3] = r8
            java.lang.String r6 = io.dcloud.common.util.StringUtil.format(r6, r7)
            r7 = 0
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ JSONException -> 0x008c }
            r8.<init>(r4)     // Catch:{ JSONException -> 0x008c }
            java.lang.String r4 = "force"
            boolean r4 = r8.optBoolean(r4, r5)     // Catch:{ JSONException -> 0x008c }
            if (r4 == 0) goto L_0x0049
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0086 }
            r9.<init>()     // Catch:{ JSONException -> 0x0086 }
            r9.append(r6)     // Catch:{ JSONException -> 0x0086 }
            android.app.Activity r10 = r17.getActivity()     // Catch:{ JSONException -> 0x0086 }
            int r11 = io.dcloud.base.R.string.dcloud_short_cut_created_removed_manually     // Catch:{ JSONException -> 0x0086 }
            java.lang.String r10 = r10.getString(r11)     // Catch:{ JSONException -> 0x0086 }
            r9.append(r10)     // Catch:{ JSONException -> 0x0086 }
            java.lang.String r6 = r9.toString()     // Catch:{ JSONException -> 0x0086 }
        L_0x0049:
            java.lang.String r9 = "name"
            java.lang.String r9 = r8.optString(r9)     // Catch:{ JSONException -> 0x0086 }
            java.lang.String r10 = "icon"
            java.lang.String r10 = r8.optString(r10)     // Catch:{ JSONException -> 0x007f }
            java.lang.String r11 = "classname"
            java.lang.String r2 = r8.optString(r11)     // Catch:{ JSONException -> 0x007b }
            boolean r11 = r8.has(r0)     // Catch:{ JSONException -> 0x007b }
            if (r11 == 0) goto L_0x0066
            java.lang.String r0 = r8.optString(r0)     // Catch:{ JSONException -> 0x007b }
            r6 = r0
        L_0x0066:
            java.lang.String r0 = "extra"
            org.json.JSONObject r11 = r8.optJSONObject(r0)     // Catch:{ JSONException -> 0x007b }
            java.lang.String r0 = "check"
            boolean r5 = r8.optBoolean(r0, r5)     // Catch:{ JSONException -> 0x0077 }
            r13 = r4
            r14 = r5
            r8 = r9
            r12 = r11
            goto L_0x009a
        L_0x0077:
            r0 = move-exception
            r8 = r0
            r0 = r2
            goto L_0x0084
        L_0x007b:
            r0 = move-exception
            r8 = r0
            r0 = r2
            goto L_0x0083
        L_0x007f:
            r0 = move-exception
            r8 = r0
            r0 = r2
            r10 = r0
        L_0x0083:
            r11 = r7
        L_0x0084:
            r2 = r9
            goto L_0x0092
        L_0x0086:
            r0 = move-exception
            r8 = r0
            r0 = r2
            r10 = r0
            r11 = r7
            goto L_0x0092
        L_0x008c:
            r0 = move-exception
            r8 = r0
            r0 = r2
            r10 = r0
            r11 = r7
            r4 = 1
        L_0x0092:
            r8.printStackTrace()
            r8 = r2
            r13 = r4
            r12 = r11
            r14 = 1
            r2 = r0
        L_0x009a:
            r11 = r6
            boolean r0 = android.text.TextUtils.isEmpty(r10)     // Catch:{ Exception -> 0x00d5 }
            if (r0 != 0) goto L_0x00ad
            java.lang.String r0 = r17.obtainFullUrl()     // Catch:{ Exception -> 0x00d5 }
            java.lang.String r0 = r1.convert2AbsFullPath(r0, r10)     // Catch:{ Exception -> 0x00d5 }
            android.graphics.Bitmap r7 = android.graphics.BitmapFactory.decodeFile(r0)     // Catch:{ Exception -> 0x00d5 }
        L_0x00ad:
            if (r7 != 0) goto L_0x00bd
            r4 = r16
            android.graphics.Bitmap r0 = r4.a(r1)     // Catch:{ Exception -> 0x00b7 }
            r7 = r0
            goto L_0x00bf
        L_0x00b7:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()     // Catch:{ Exception -> 0x00d3 }
            goto L_0x00bf
        L_0x00bd:
            r4 = r16
        L_0x00bf:
            if (r7 != 0) goto L_0x00db
            if (r7 != 0) goto L_0x00db
            android.content.Context r0 = r17.getContext()     // Catch:{ Exception -> 0x00d3 }
            android.content.res.Resources r0 = r0.getResources()     // Catch:{ Exception -> 0x00d3 }
            int r1 = io.dcloud.PdrR.DRAWABLE_ICON     // Catch:{ Exception -> 0x00d3 }
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeResource(r0, r1)     // Catch:{ Exception -> 0x00d3 }
            r9 = r0
            goto L_0x00dc
        L_0x00d3:
            r0 = move-exception
            goto L_0x00d8
        L_0x00d5:
            r0 = move-exception
            r4 = r16
        L_0x00d8:
            r0.printStackTrace()
        L_0x00db:
            r9 = r7
        L_0x00dc:
            r6 = r16
            r7 = r17
            r10 = r2
            r6.a(r7, r8, r9, r10, r11, r12, r13, r14, r15)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.navigator.NavigatorUIFeatureImpl.a(io.dcloud.common.DHInterface.IWebview, java.lang.String[], io.dcloud.common.DHInterface.IApp, java.lang.String):boolean");
    }

    private String b(IApp iApp) {
        Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
        return obtainWebAppIntent != null ? obtainWebAppIntent.getStringExtra(IntentConst.WEBAPP_ACTIVITY_APPICON) : "";
    }

    private void a(Context context, IWebview iWebview, String str, String str2) {
        MessageHandler.postDelayed(new c(context, str2, iWebview, str), (long) (Build.VERSION.SDK_INT >= 25 ? 1500 : 500));
    }

    private Bitmap a(IApp iApp) {
        String b2 = b(iApp);
        if (b2 != null) {
            return BitmapFactory.decodeFile(b2);
        }
        return null;
    }

    private void a(IWebview iWebview, String str, Bitmap bitmap, String str2, String str3, JSONObject jSONObject, boolean z, boolean z2, String str4) {
        Intent obtainWebAppIntent;
        String str5 = str3;
        IApp obtainApp = iWebview.obtainApp();
        String obtainAppId = obtainApp.obtainAppId();
        Activity activity = iWebview.getActivity();
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(iWebview.getContext(), "pdr");
        String obtainAppName = PdrUtil.isEmpty(str) ? obtainApp.obtainAppName() : str;
        boolean z3 = orCreateBundle.getBoolean(obtainAppId + SP.K_CREATED_SHORTCUT, false);
        String stringExtra = (!TextUtils.isEmpty(str2) || (obtainWebAppIntent = iWebview.obtainApp().obtainWebAppIntent()) == null) ? str2 : obtainWebAppIntent.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME);
        if (Build.VERSION.SDK_INT >= 25) {
            if ((!ShortCutUtil.hasShortcut(activity, obtainAppName) || z) && ShortCutUtil.createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap, stringExtra, jSONObject, true) && !TextUtils.isEmpty(str3)) {
                ToastCompat.makeText(activity.getApplicationContext(), (CharSequence) str5, 1).show();
            }
        } else if (ShortcutCreateUtil.isDuplicateLauncher(activity)) {
            if (ShortCutUtil.createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap, stringExtra, jSONObject, true) && !TextUtils.isEmpty(str3) && ShortcutCreateUtil.needToast(activity)) {
                ToastCompat.makeText(activity.getApplicationContext(), (CharSequence) str5, 1).show();
            }
        } else if (!ShortCutUtil.hasShortcut(activity, obtainAppName)) {
            if (z) {
                if (!TextUtils.isEmpty(str3) && ShortcutCreateUtil.needToast(activity)) {
                    ToastCompat.makeText(activity.getApplicationContext(), (CharSequence) str5, 1).show();
                }
                ShortCutUtil.createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap, stringExtra, jSONObject, true);
            } else if (!z3) {
                if (ShortCutUtil.createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap, stringExtra, jSONObject, true) && !TextUtils.isEmpty(str3) && ShortcutCreateUtil.needToast(activity)) {
                    ToastCompat.makeText(activity.getApplicationContext(), (CharSequence) str5, 1).show();
                }
            } else {
                return;
            }
        }
        IWebview iWebview2 = iWebview;
        a(iWebview.getContext(), iWebview, str4, obtainAppName);
    }
}
