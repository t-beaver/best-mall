package io.dcloud.feature.device;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import com.taobao.weex.WXEnvironment;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.e.d.a;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceFeatureImpl implements IFeature, ISysEventListener {
    static int a = 255;
    private PowerManager.WakeLock b = null;
    private boolean c = false;
    private Context d;
    int e = -1;

    class a extends PermissionUtil.StreamPermissionRequest {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String[] b;
        final /* synthetic */ boolean c;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        a(IApp iApp, IWebview iWebview, String[] strArr, boolean z) {
            super(iApp);
            this.a = iWebview;
            this.b = strArr;
            this.c = z;
        }

        public void onDenied(String str) {
        }

        public void onGranted(String str) {
            DeviceFeatureImpl.this.a(this.a, this.b[0], this.c);
        }
    }

    class b extends PermissionUtil.Request {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        b(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public void onDenied(String str) {
            Deprecated_JSUtil.execCallback(this.a, this.b, "{'imei':'','imsi':[],'uuid':'" + TelephonyUtil.getIMEI(this.a.getContext(), false, true) + "'}", JSUtil.ERROR, true, false);
        }

        public void onGranted(String str) {
            DeviceInfo.updateIMEI();
            DeviceInfo.getUpdateIMSI();
            Deprecated_JSUtil.execCallback(this.a, this.b, StringUtil.format("{'imei':'%s','imsi':['%s'],'uuid':'%s'}", DeviceInfo.sIMEI, DeviceInfo.sIMSI, TextUtils.isEmpty(DeviceInfo.sIMEI) ? TelephonyUtil.getIMEI(this.a.getContext(), false, true) : DeviceInfo.sIMEI), JSUtil.OK, true, false);
        }
    }

    class c implements a.b {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;
        final /* synthetic */ String c;

        c(IWebview iWebview, String str, String str2) {
            this.a = iWebview;
            this.b = str;
            this.c = str2;
        }

        public void a(String str, boolean z) {
            if (z) {
                DeviceFeatureImpl.this.a(this.a, this.b, this.c, str);
            } else {
                Deprecated_JSUtil.execCallback(this.a, this.c, DOMException.toJSON(401, "not support"), JSUtil.ERROR, true, false);
            }
        }
    }

    class d implements Runnable {
        final /* synthetic */ IWebview a;
        final /* synthetic */ float b;

        d(IWebview iWebview, float f) {
            this.a = iWebview;
            this.b = f;
        }

        public void run() {
            DeviceFeatureImpl.this.a(this.a, this.b);
        }
    }

    static {
        try {
            Resources system = Resources.getSystem();
            int identifier = system.getIdentifier("config_screenBrightnessSettingMaximum", "integer", WXEnvironment.OS);
            if (identifier != 0) {
                a = system.getInteger(identifier);
            }
        } catch (Exception unused) {
            a = 255;
        }
    }

    private void b(IWebview iWebview, float f) {
        iWebview.obtainWindowView().post(new d(iWebview, f));
    }

    public void dispose(String str) {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0226, code lost:
        if (r0 <= 0) goto L_0x022d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r16, java.lang.String r17, java.lang.String[] r18) {
        /*
            r15 = this;
            r7 = r15
            r0 = r16
            r1 = r17
            r17.hashCode()
            int r2 = r17.hashCode()
            r3 = 4
            r4 = 5
            r5 = 3
            r8 = 2
            r6 = 8
            r9 = 1
            r10 = 0
            switch(r2) {
                case -2016181423: goto L_0x0163;
                case -1768495376: goto L_0x0158;
                case -1498445182: goto L_0x014d;
                case -1492959986: goto L_0x0142;
                case -1386751524: goto L_0x0137;
                case -1107875961: goto L_0x012c;
                case -503804903: goto L_0x0121;
                case -75727471: goto L_0x0116;
                case -75444956: goto L_0x0108;
                case -75310397: goto L_0x00fa;
                case -75101860: goto L_0x00ec;
                case -39062556: goto L_0x00de;
                case -39017699: goto L_0x00d0;
                case 3019822: goto L_0x00c2;
                case 3083120: goto L_0x00b4;
                case 350413895: goto L_0x00a6;
                case 376261968: goto L_0x0098;
                case 451310959: goto L_0x0089;
                case 578223421: goto L_0x007b;
                case 596855788: goto L_0x006d;
                case 608383606: goto L_0x005f;
                case 670514716: goto L_0x0051;
                case 885131792: goto L_0x0043;
                case 1124545107: goto L_0x0035;
                case 1351280895: goto L_0x0027;
                case 1497595813: goto L_0x0019;
                default: goto L_0x0017;
            }
        L_0x0017:
            goto L_0x016e
        L_0x0019:
            java.lang.String r2 = "lockOrientation"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0023
            goto L_0x016e
        L_0x0023:
            r2 = 25
            goto L_0x016f
        L_0x0027:
            java.lang.String r2 = "s.resolutionWidth"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0031
            goto L_0x016e
        L_0x0031:
            r2 = 24
            goto L_0x016f
        L_0x0035:
            java.lang.String r2 = "setBrightness"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x003f
            goto L_0x016e
        L_0x003f:
            r2 = 23
            goto L_0x016f
        L_0x0043:
            java.lang.String r2 = "getVolume"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x004d
            goto L_0x016e
        L_0x004d:
            r2 = 22
            goto L_0x016f
        L_0x0051:
            java.lang.String r2 = "setVolume"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x005b
            goto L_0x016e
        L_0x005b:
            r2 = 21
            goto L_0x016f
        L_0x005f:
            java.lang.String r2 = "isSetProxy"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0069
            goto L_0x016e
        L_0x0069:
            r2 = 20
            goto L_0x016f
        L_0x006d:
            java.lang.String r2 = "unlockOrientation"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0077
            goto L_0x016e
        L_0x0077:
            r2 = 19
            goto L_0x016f
        L_0x007b:
            java.lang.String r2 = "d.resolutionHeight"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0085
            goto L_0x016e
        L_0x0085:
            r2 = 18
            goto L_0x016f
        L_0x0089:
            java.lang.String r2 = "vibrate"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0094
            goto L_0x016e
        L_0x0094:
            r2 = 17
            goto L_0x016f
        L_0x0098:
            java.lang.String r2 = "__isWakelockNative__"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x00a2
            goto L_0x016e
        L_0x00a2:
            r2 = 16
            goto L_0x016f
        L_0x00a6:
            java.lang.String r2 = "getBrightness"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x00b0
            goto L_0x016e
        L_0x00b0:
            r2 = 15
            goto L_0x016f
        L_0x00b4:
            java.lang.String r2 = "dial"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x00be
            goto L_0x016e
        L_0x00be:
            r2 = 14
            goto L_0x016f
        L_0x00c2:
            java.lang.String r2 = "beep"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x00cc
            goto L_0x016e
        L_0x00cc:
            r2 = 13
            goto L_0x016f
        L_0x00d0:
            java.lang.String r2 = "getCurrentType"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x00da
            goto L_0x016e
        L_0x00da:
            r2 = 12
            goto L_0x016f
        L_0x00de:
            java.lang.String r2 = "getCurrentSize"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x00e8
            goto L_0x016e
        L_0x00e8:
            r2 = 11
            goto L_0x016f
        L_0x00ec:
            java.lang.String r2 = "getVAID"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x00f6
            goto L_0x016e
        L_0x00f6:
            r2 = 10
            goto L_0x016f
        L_0x00fa:
            java.lang.String r2 = "getOAID"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0104
            goto L_0x016e
        L_0x0104:
            r2 = 9
            goto L_0x016f
        L_0x0108:
            java.lang.String r2 = "getInfo"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0112
            goto L_0x016e
        L_0x0112:
            r2 = 8
            goto L_0x016f
        L_0x0116:
            java.lang.String r2 = "getAAID"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x011f
            goto L_0x016e
        L_0x011f:
            r2 = 7
            goto L_0x016f
        L_0x0121:
            java.lang.String r2 = "isWakelock"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x012a
            goto L_0x016e
        L_0x012a:
            r2 = 6
            goto L_0x016f
        L_0x012c:
            java.lang.String r2 = "getDeviceId"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0135
            goto L_0x016e
        L_0x0135:
            r2 = 5
            goto L_0x016f
        L_0x0137:
            java.lang.String r2 = "getCurrentAPN"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0140
            goto L_0x016e
        L_0x0140:
            r2 = 4
            goto L_0x016f
        L_0x0142:
            java.lang.String r2 = "s.resolutionHeight"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x014b
            goto L_0x016e
        L_0x014b:
            r2 = 3
            goto L_0x016f
        L_0x014d:
            java.lang.String r2 = "getDCloudID"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0156
            goto L_0x016e
        L_0x0156:
            r2 = 2
            goto L_0x016f
        L_0x0158:
            java.lang.String r2 = "d.resolutionWidth"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x0161
            goto L_0x016e
        L_0x0161:
            r2 = 1
            goto L_0x016f
        L_0x0163:
            java.lang.String r2 = "setWakelock"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x016c
            goto L_0x016e
        L_0x016c:
            r2 = 0
            goto L_0x016f
        L_0x016e:
            r2 = -1
        L_0x016f:
            java.lang.String r11 = "audio"
            java.lang.String r12 = "Device"
            java.lang.String r14 = "Device-"
            r13 = 0
            switch(r2) {
                case 0: goto L_0x0433;
                case 1: goto L_0x041c;
                case 2: goto L_0x03f9;
                case 3: goto L_0x03e2;
                case 4: goto L_0x03b0;
                case 5: goto L_0x03f9;
                case 6: goto L_0x03a1;
                case 7: goto L_0x030c;
                case 8: goto L_0x02e4;
                case 9: goto L_0x030c;
                case 10: goto L_0x030c;
                case 11: goto L_0x02d3;
                case 12: goto L_0x02ce;
                case 13: goto L_0x02a3;
                case 14: goto L_0x026d;
                case 15: goto L_0x0257;
                case 16: goto L_0x023f;
                case 17: goto L_0x021c;
                case 18: goto L_0x0205;
                case 19: goto L_0x01fc;
                case 20: goto L_0x01ef;
                case 21: goto L_0x01c4;
                case 22: goto L_0x01a8;
                case 23: goto L_0x019d;
                case 24: goto L_0x0186;
                case 25: goto L_0x017b;
                default: goto L_0x0179;
            }
        L_0x0179:
            goto L_0x0446
        L_0x017b:
            r1 = r18[r10]
            io.dcloud.common.DHInterface.IApp r0 = r16.obtainApp()
            r0.setRequestedOrientation((java.lang.String) r1)
            goto L_0x0446
        L_0x0186:
            io.dcloud.common.DHInterface.IApp r1 = r16.obtainApp()
            float r0 = r16.getScale()
            int r1 = r1.getInt(r10)
            float r1 = (float) r1
            float r1 = r1 / r0
            java.lang.String r0 = java.lang.String.valueOf(r1)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r10)
            return r0
        L_0x019d:
            r1 = r18[r10]
            float r1 = java.lang.Float.parseFloat(r1)
            r15.b(r0, r1)
            goto L_0x0446
        L_0x01a8:
            android.content.Context r0 = r16.getContext()
            java.lang.Object r0 = r0.getSystemService(r11)
            android.media.AudioManager r0 = (android.media.AudioManager) r0
            int r0 = r0.getStreamVolume(r5)
            float r0 = (float) r0
            int r1 = r7.e
            float r1 = (float) r1
            float r0 = r0 / r1
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r10)
            return r0
        L_0x01c4:
            r1 = r18[r10]
            float r1 = java.lang.Float.parseFloat(r1)
            android.content.Context r0 = r16.getContext()
            java.lang.Object r0 = r0.getSystemService(r11)
            android.media.AudioManager r0 = (android.media.AudioManager) r0
            int r1 = r15.a((float) r1)
            r0.setStreamVolume(r3, r1, r6)
            r0.setStreamVolume(r6, r1, r6)
            r0.setStreamVolume(r5, r1, r6)
            r0.setStreamVolume(r4, r1, r6)
            r0.setStreamVolume(r8, r1, r6)
            r0.setStreamVolume(r9, r1, r6)
            r0.setStreamVolume(r10, r1, r6)
            goto L_0x0446
        L_0x01ef:
            android.content.Context r0 = r16.getContext()
            boolean r0 = io.dcloud.common.util.NetworkTypeUtil.isWifiProxy(r0)
            java.lang.String r0 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r0)
            return r0
        L_0x01fc:
            io.dcloud.common.DHInterface.IApp r0 = r16.obtainApp()
            r0.setRequestedOrientation((java.lang.String) r13)
            goto L_0x0446
        L_0x0205:
            io.dcloud.common.DHInterface.IApp r1 = r16.obtainApp()
            float r0 = r16.getScale()
            int r1 = r1.getInt(r9)
            float r1 = (float) r1
            float r1 = r1 / r0
            java.lang.String r0 = java.lang.String.valueOf(r1)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r10)
            return r0
        L_0x021c:
            r0 = r18[r10]     // Catch:{ NumberFormatException -> 0x0229 }
            long r0 = java.lang.Long.parseLong(r0)     // Catch:{ NumberFormatException -> 0x0229 }
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 > 0) goto L_0x022f
            goto L_0x022d
        L_0x0229:
            r0 = move-exception
            r0.printStackTrace()
        L_0x022d:
            r0 = 500(0x1f4, double:2.47E-321)
        L_0x022f:
            android.content.Context r2 = r7.d
            java.lang.String r3 = "vibrator"
            java.lang.Object r2 = r2.getSystemService(r3)
            android.os.Vibrator r2 = (android.os.Vibrator) r2
            r2.vibrate(r0)
            goto L_0x0446
        L_0x023f:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            android.os.PowerManager$WakeLock r1 = r7.b
            boolean r1 = r1.isHeld()
            r0.append(r1)
            java.lang.String r1 = ""
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            return r0
        L_0x0257:
            android.app.Activity r0 = r16.getActivity()
            int r0 = r15.a((android.app.Activity) r0)
            float r0 = (float) r0
            int r1 = a
            float r1 = (float) r1
            float r0 = r0 / r1
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r10)
            return r0
        L_0x026d:
            android.content.Context r2 = r16.getContext()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r14)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r2, r1)
            r1 = r18[r9]
            boolean r6 = io.dcloud.common.util.PdrUtil.parseBoolean(r1, r9, r10)
            android.app.Activity r9 = r16.getActivity()
            io.dcloud.feature.device.DeviceFeatureImpl$a r10 = new io.dcloud.feature.device.DeviceFeatureImpl$a
            io.dcloud.common.DHInterface.IApp r3 = r16.obtainApp()
            r1 = r10
            r2 = r15
            r4 = r16
            r5 = r18
            r1.<init>(r3, r4, r5, r6)
            java.lang.String r0 = "PHONE"
            io.dcloud.common.adapter.util.PermissionUtil.usePermission(r9, r12, r0, r8, r10)
            goto L_0x0446
        L_0x02a3:
            android.media.ToneGenerator r1 = new android.media.ToneGenerator
            r0 = 100
            r1.<init>(r4, r0)
            r0 = r18[r10]     // Catch:{ NumberFormatException -> 0x02b5 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x02b5 }
            if (r0 > 0) goto L_0x02b3
            goto L_0x02b9
        L_0x02b3:
            r9 = r0
            goto L_0x02b9
        L_0x02b5:
            r0 = move-exception
            r0.printStackTrace()
        L_0x02b9:
            if (r10 >= r9) goto L_0x0446
            r0 = 88
            r1.startTone(r0)
            r2 = 500(0x1f4, double:2.47E-321)
            java.lang.Thread.sleep(r2)     // Catch:{ InterruptedException -> 0x02c6 }
            goto L_0x02cb
        L_0x02c6:
            r0 = move-exception
            r4 = r0
            r4.printStackTrace()
        L_0x02cb:
            int r10 = r10 + 1
            goto L_0x02b9
        L_0x02ce:
            java.lang.String r0 = io.dcloud.common.adapter.util.DeviceInfo.getNetWorkType()
            return r0
        L_0x02d3:
            io.dcloud.common.DHInterface.IApp r1 = r16.obtainApp()
            float r0 = r16.getScale()
            java.lang.String r0 = r15.a((io.dcloud.common.DHInterface.IApp) r1, (float) r0)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r10)
            return r0
        L_0x02e4:
            r2 = r18[r10]
            android.content.Context r3 = r16.getContext()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r14)
            r4.append(r1)
            java.lang.String r1 = r4.toString()
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r3, r1)
            android.app.Activity r1 = r16.getActivity()
            io.dcloud.feature.device.DeviceFeatureImpl$b r3 = new io.dcloud.feature.device.DeviceFeatureImpl$b
            r3.<init>(r0, r2)
            java.lang.String r0 = "android.permission.READ_PHONE_STATE"
            io.dcloud.common.adapter.util.PermissionUtil.usePermission(r1, r12, r0, r8, r3)
            goto L_0x0446
        L_0x030c:
            android.content.Context r2 = r16.getContext()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r14)
            r3.append(r1)
            java.lang.String r3 = r3.toString()
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r2, r3)
            r2 = r18[r10]
            boolean r3 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()
            java.lang.String r4 = "not support"
            r5 = 401(0x191, float:5.62E-43)
            if (r3 == 0) goto L_0x0345
            java.lang.String r3 = io.dcloud.feature.internal.sdk.SDK.customOAID
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x0345
            java.lang.String r3 = io.dcloud.common.constant.DOMException.toJSON((int) r5, (java.lang.String) r4)
            int r4 = io.dcloud.common.util.JSUtil.ERROR
            r5 = 1
            r6 = 0
            r1 = r16
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)
            goto L_0x0446
        L_0x0345:
            java.lang.String r3 = io.dcloud.common.adapter.util.DeviceInfo.oaids
            if (r3 == 0) goto L_0x035a
            java.lang.String r6 = "||"
            boolean r3 = r3.equalsIgnoreCase(r6)
            if (r3 == 0) goto L_0x0353
            goto L_0x035a
        L_0x0353:
            java.lang.String r3 = io.dcloud.common.adapter.util.DeviceInfo.oaids
            r15.a(r0, r1, r2, r3)
            goto L_0x0446
        L_0x035a:
            java.lang.String r3 = "com.bun.miitmdid.core.JLibrary"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch:{ Exception -> 0x037d }
            java.lang.String r6 = "InitEntry"
            java.lang.Class[] r8 = new java.lang.Class[r9]     // Catch:{ Exception -> 0x037d }
            java.lang.Class<android.content.Context> r11 = android.content.Context.class
            r8[r10] = r11     // Catch:{ Exception -> 0x037d }
            java.lang.reflect.Method r3 = r3.getDeclaredMethod(r6, r8)     // Catch:{ Exception -> 0x037d }
            if (r3 == 0) goto L_0x037e
            r3.setAccessible(r9)     // Catch:{ Exception -> 0x037d }
            java.lang.Object[] r6 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x037d }
            android.content.Context r8 = r16.getContext()     // Catch:{ Exception -> 0x037d }
            r6[r10] = r8     // Catch:{ Exception -> 0x037d }
            r3.invoke(r13, r6)     // Catch:{ Exception -> 0x037d }
            goto L_0x037e
        L_0x037d:
        L_0x037e:
            io.dcloud.e.d.a r3 = new io.dcloud.e.d.a
            io.dcloud.feature.device.DeviceFeatureImpl$c r6 = new io.dcloud.feature.device.DeviceFeatureImpl$c
            r6.<init>(r0, r1, r2)
            r3.<init>(r6)
            android.app.Activity r1 = r16.getActivity()
            boolean r1 = r3.b(r1)
            if (r1 != 0) goto L_0x0446
            java.lang.String r3 = io.dcloud.common.constant.DOMException.toJSON((int) r5, (java.lang.String) r4)
            int r4 = io.dcloud.common.util.JSUtil.ERROR
            r5 = 1
            r6 = 0
            r1 = r16
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)
            goto L_0x0446
        L_0x03a1:
            android.os.PowerManager$WakeLock r0 = r7.b
            boolean r0 = r0.isHeld()
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r10)
            return r0
        L_0x03b0:
            java.lang.String r0 = io.dcloud.common.adapter.util.DeviceInfo.getCurrentAPN()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x03bb
            return r13
        L_0x03bb:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "{name:"
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = "}"
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x03dd }
            r1.<init>(r0)     // Catch:{ JSONException -> 0x03dd }
            java.lang.String r0 = io.dcloud.common.util.JSUtil.wrapJsVar((org.json.JSONObject) r1)     // Catch:{ JSONException -> 0x03dd }
            return r0
        L_0x03dd:
            r0 = move-exception
            r0.printStackTrace()
            return r13
        L_0x03e2:
            io.dcloud.common.DHInterface.IApp r1 = r16.obtainApp()
            float r0 = r16.getScale()
            int r1 = r1.getInt(r8)
            float r1 = (float) r1
            float r1 = r1 / r0
            java.lang.String r0 = java.lang.String.valueOf(r1)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r10)
            return r0
        L_0x03f9:
            android.content.Context r2 = r16.getContext()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r14)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r2, r1)
            android.content.Context r0 = r16.getContext()
            java.lang.String r0 = io.dcloud.common.util.AppRuntime.getDCloudDeviceID(r0)
            java.lang.String r0 = io.dcloud.common.util.JSUtil.wrapJsVar((java.lang.String) r0)
            return r0
        L_0x041c:
            io.dcloud.common.DHInterface.IApp r1 = r16.obtainApp()
            float r0 = r16.getScale()
            int r1 = r1.getInt(r10)
            float r1 = (float) r1
            float r1 = r1 / r0
            java.lang.String r0 = java.lang.String.valueOf(r1)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r10)
            return r0
        L_0x0433:
            r0 = r18[r10]
            boolean r0 = io.dcloud.common.util.PdrUtil.parseBoolean(r0, r10, r10)
            if (r0 == 0) goto L_0x0441
            android.os.PowerManager$WakeLock r0 = r7.b
            r0.acquire()
            goto L_0x0446
        L_0x0441:
            android.os.PowerManager$WakeLock r0 = r7.b
            r0.release()
        L_0x0446:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.device.DeviceFeatureImpl.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public void init(AbsMgr absMgr, String str) {
        Context context = absMgr.getContext();
        this.d = context;
        PowerManager.WakeLock newWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(10, "My Lock");
        this.b = newWakeLock;
        newWakeLock.setReferenceCounted(false);
        this.e = ((AudioManager) absMgr.getContext().getSystemService("audio")).getStreamMaxVolume(3);
    }

    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        return false;
    }

    private String a(IApp iApp, float f) {
        int i = iApp.getInt(0);
        int i2 = iApp.getInt(2);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("width", i);
            jSONObject.put("height", i2);
            jSONObject.put("resolutionWidth", (double) (((float) i) / f));
            jSONObject.put("resolutionHeight", (double) (((float) i2) / f));
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jSONObject.toString();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0092 A[Catch:{ Exception -> 0x00b7 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(io.dcloud.common.DHInterface.IWebview r7, java.lang.String r8, java.lang.String r9, java.lang.String r10) {
        /*
            r6 = this;
            java.lang.String r0 = "\\|"
            java.lang.String[] r10 = r10.split(r0)
            int r0 = r8.hashCode()     // Catch:{ Exception -> 0x00b7 }
            r1 = -75727471(0xfffffffffb7c7d91, float:-1.3110056E36)
            r2 = 1
            r3 = 2
            r4 = 0
            if (r0 == r1) goto L_0x0031
            r1 = -75310397(0xfffffffffb82dac3, float:-1.3588712E36)
            if (r0 == r1) goto L_0x0027
            r1 = -75101860(0xfffffffffb86095c, float:-1.3919152E36)
            if (r0 == r1) goto L_0x001d
            goto L_0x003b
        L_0x001d:
            java.lang.String r0 = "getVAID"
            boolean r8 = r8.equals(r0)     // Catch:{ Exception -> 0x00b7 }
            if (r8 == 0) goto L_0x003b
            r8 = 1
            goto L_0x003c
        L_0x0027:
            java.lang.String r0 = "getOAID"
            boolean r8 = r8.equals(r0)     // Catch:{ Exception -> 0x00b7 }
            if (r8 == 0) goto L_0x003b
            r8 = 0
            goto L_0x003c
        L_0x0031:
            java.lang.String r0 = "getAAID"
            boolean r8 = r8.equals(r0)     // Catch:{ Exception -> 0x00b7 }
            if (r8 == 0) goto L_0x003b
            r8 = 2
            goto L_0x003c
        L_0x003b:
            r8 = -1
        L_0x003c:
            java.lang.String r0 = "'}"
            java.lang.String r1 = ""
            if (r8 == 0) goto L_0x0092
            if (r8 == r2) goto L_0x006d
            if (r8 == r3) goto L_0x0048
            goto L_0x00cb
        L_0x0048:
            int r8 = r10.length     // Catch:{ Exception -> 0x00b7 }
            if (r8 <= r3) goto L_0x004d
            r1 = r10[r3]     // Catch:{ Exception -> 0x00b7 }
        L_0x004d:
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ Exception -> 0x00b7 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b7 }
            r10.<init>()     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r2 = "{'aaid':'"
            r10.append(r2)     // Catch:{ Exception -> 0x00b7 }
            r10.append(r1)     // Catch:{ Exception -> 0x00b7 }
            r10.append(r0)     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x00b7 }
            r8.<init>(r10)     // Catch:{ Exception -> 0x00b7 }
            int r10 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x00b7 }
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r9, (org.json.JSONObject) r8, (int) r10, (boolean) r4)     // Catch:{ Exception -> 0x00b7 }
            goto L_0x00cb
        L_0x006d:
            int r8 = r10.length     // Catch:{ Exception -> 0x00b7 }
            if (r8 <= r2) goto L_0x0072
            r1 = r10[r2]     // Catch:{ Exception -> 0x00b7 }
        L_0x0072:
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ Exception -> 0x00b7 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b7 }
            r10.<init>()     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r2 = "{'vaid':'"
            r10.append(r2)     // Catch:{ Exception -> 0x00b7 }
            r10.append(r1)     // Catch:{ Exception -> 0x00b7 }
            r10.append(r0)     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x00b7 }
            r8.<init>(r10)     // Catch:{ Exception -> 0x00b7 }
            int r10 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x00b7 }
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r9, (org.json.JSONObject) r8, (int) r10, (boolean) r4)     // Catch:{ Exception -> 0x00b7 }
            goto L_0x00cb
        L_0x0092:
            int r8 = r10.length     // Catch:{ Exception -> 0x00b7 }
            if (r8 <= 0) goto L_0x0097
            r1 = r10[r4]     // Catch:{ Exception -> 0x00b7 }
        L_0x0097:
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ Exception -> 0x00b7 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b7 }
            r10.<init>()     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r2 = "{'oaid':'"
            r10.append(r2)     // Catch:{ Exception -> 0x00b7 }
            r10.append(r1)     // Catch:{ Exception -> 0x00b7 }
            r10.append(r0)     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x00b7 }
            r8.<init>(r10)     // Catch:{ Exception -> 0x00b7 }
            int r10 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x00b7 }
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r7, (java.lang.String) r9, (org.json.JSONObject) r8, (int) r10, (boolean) r4)     // Catch:{ Exception -> 0x00b7 }
            goto L_0x00cb
        L_0x00b7:
            r8 = move-exception
            java.lang.String r8 = r8.getMessage()
            r10 = 401(0x191, float:5.62E-43)
            java.lang.String r2 = io.dcloud.common.constant.DOMException.toJSON((int) r10, (java.lang.String) r8)
            int r3 = io.dcloud.common.util.JSUtil.ERROR
            r4 = 1
            r5 = 0
            r0 = r7
            r1 = r9
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r0, r1, r2, r3, r4, r5)
        L_0x00cb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.device.DeviceFeatureImpl.a(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String, java.lang.String):void");
    }

    private int a(Activity activity) {
        int i;
        float f = activity.getWindow().getAttributes().screenBrightness;
        if (f < 0.0f) {
            ContentResolver contentResolver = activity.getContentResolver();
            try {
                int integer = activity.getResources().getInteger(activity.getResources().getIdentifier("config_screenBrightnessSettingMaximum", "integer", WXEnvironment.OS));
                int i2 = Settings.System.getInt(contentResolver, "screen_brightness", 125);
                if (integer > 255) {
                    f = ((float) i2) / ((float) integer);
                    if (f <= 0.0f) {
                        return 125;
                    }
                    i = a;
                } else if (i2 >= 0) {
                    return i2;
                } else {
                    return 125;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                return 125;
            }
        } else {
            i = a;
        }
        return (int) (f * ((float) i));
    }

    /* access modifiers changed from: private */
    public void a(IWebview iWebview, float f) {
        Window window = iWebview.getActivity().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        int i = (f > 1.0f ? 1 : (f == 1.0f ? 0 : -1));
        if (i > 0 || f <= 0.0f) {
            attributes.screenBrightness = -1.0f;
        } else {
            attributes.screenBrightness = f;
        }
        if (f == -1.0f) {
            attributes.screenBrightness = -1.0f;
            window.setAttributes(attributes);
        } else if (i <= 0 && f > 0.0f) {
            attributes.screenBrightness = f;
            window.setAttributes(attributes);
        }
    }

    private int a(float f) {
        if (f > 1.0f || f < 0.0f) {
            return 0;
        }
        return (int) (f * ((float) this.e));
    }

    /* access modifiers changed from: protected */
    public void a(IWebview iWebview, String str, boolean z) {
        iWebview.getActivity().startActivity(new Intent(!z ? "android.intent.action.CALL" : "android.intent.action.DIAL", Uri.parse("tel:" + str)));
    }
}
