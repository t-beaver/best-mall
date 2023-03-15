package io.dcloud.feature.pdr;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.common.util.UriUtil;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.PandoraEntry;
import io.dcloud.PdrR;
import io.dcloud.WebviewActivity;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.message.ActionBus;
import io.dcloud.common.DHInterface.message.action.BadgeSyncAction;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.ui.Info.AndroidPrivacyResponse;
import io.dcloud.common.ui.b;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class RuntimeFeatureImpl implements IFeature, MessageHandler.IMessages {
    final String a = PandoraEntry.class.getName();
    AbsMgr b = null;

    class a extends Thread {
        final /* synthetic */ String[] a;
        final /* synthetic */ String b;
        final /* synthetic */ IWebview c;

        a(String[] strArr, String str, IWebview iWebview) {
            this.a = strArr;
            this.b = str;
            this.c = iWebview;
        }

        public void run() {
            String[] strArr = this.a;
            String str = strArr[1];
            Object[] objArr = (Object[]) RuntimeFeatureImpl.this.b.processEvent(IMgr.MgrType.AppMgr, 4, new Object[]{this.b, !PdrUtil.isEmpty(strArr[2]) ? JSONUtil.createJSONObject(this.a[2]) : null, this.c});
            boolean booleanValue = Boolean.valueOf(String.valueOf(objArr[0])).booleanValue();
            String valueOf = String.valueOf(objArr[1]);
            if (booleanValue) {
                Deprecated_JSUtil.execCallback(this.c, str, valueOf, JSUtil.ERROR, true, false);
            } else {
                Deprecated_JSUtil.execCallback(this.c, str, valueOf, JSUtil.OK, true, false);
            }
        }
    }

    class b implements b.C0023b {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;
        final /* synthetic */ Activity c;

        b(IWebview iWebview, String str, Activity activity) {
            this.a = iWebview;
            this.b = str;
            this.c = activity;
        }

        public void a(String str) {
            JSUtil.execCallback(this.a, this.b, new JSONObject(), JSUtil.ERROR, false);
        }

        public void b(AndroidPrivacyResponse androidPrivacyResponse) {
            if (TextUtils.isEmpty(androidPrivacyResponse.second.message)) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("code", -1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSUtil.execCallback(this.a, this.b, jSONObject, JSUtil.OK, false);
                return;
            }
            io.dcloud.common.ui.b.a().a(this.c, new a(), true, true);
        }

        class a implements b.C0023b {
            a() {
            }

            public void a(String str) {
                JSONObject jSONObject = new JSONObject();
                b bVar = b.this;
                JSUtil.execCallback(bVar.a, bVar.b, jSONObject, JSUtil.ERROR, false);
            }

            public void b(AndroidPrivacyResponse androidPrivacyResponse) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("code", -1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                b bVar = b.this;
                JSUtil.execCallback(bVar.a, bVar.b, jSONObject, JSUtil.OK, false);
            }

            public void a() {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("code", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                b bVar = b.this;
                JSUtil.execCallback(bVar.a, bVar.b, jSONObject, JSUtil.OK, false);
            }

            public void a(AndroidPrivacyResponse androidPrivacyResponse) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("code", 2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                b bVar = b.this;
                JSUtil.execCallback(bVar.a, bVar.b, jSONObject, JSUtil.OK, false);
            }
        }

        public void a() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("code", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSUtil.execCallback(this.a, this.b, jSONObject, JSUtil.OK, false);
        }

        public void a(AndroidPrivacyResponse androidPrivacyResponse) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("code", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSUtil.execCallback(this.a, this.b, jSONObject, JSUtil.OK, false);
        }
    }

    class c implements ICallBack {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        c(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public Object onCallBack(int i, Object obj) {
            if (i == 1) {
                return null;
            }
            Deprecated_JSUtil.excCallbackError(this.a, this.b, String.valueOf(obj), true);
            return null;
        }
    }

    class d implements ICallBack {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        d(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public Object onCallBack(int i, Object obj) {
            if (i == 1) {
                Deprecated_JSUtil.execCallback(this.a, this.b, "{}", JSUtil.OK, true, false);
                return null;
            }
            Deprecated_JSUtil.excCallbackError(this.a, this.b, String.valueOf(obj), true);
            return null;
        }
    }

    private void a(IWebview iWebview, String str, String str2) {
        String valueOf = (TextUtils.isEmpty(str) || str.equals(WXInstanceApm.VALUE_ERROR_CODE_DEFAULT)) ? "" : String.valueOf(Math.max(0, Integer.valueOf(str).intValue()));
        try {
            String str3 = Build.MANUFACTURER;
            if (str3.equalsIgnoreCase("Xiaomi")) {
                b(iWebview, valueOf, str2);
            } else if (str3.equalsIgnoreCase("samsung")) {
                c(iWebview, valueOf);
            } else {
                Locale locale = Locale.ENGLISH;
                if (str3.toLowerCase(locale).contains("sony")) {
                    d(iWebview, valueOf);
                } else if (str3.toLowerCase(locale).contains("huawei")) {
                    e(iWebview, valueOf);
                } else if (str3.toLowerCase(locale).contains("vivo")) {
                    g(iWebview, valueOf);
                } else if (str3.toLowerCase(locale).contains("oppo")) {
                    f(iWebview, valueOf);
                } else if (str3.toLowerCase(locale).contains("honor")) {
                    b(iWebview, valueOf);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void b(IWebview iWebview, String str, String str2) {
        JSONObject jSONObject;
        Notification.Builder builder;
        try {
            jSONObject = new JSONObject(str2);
        } catch (JSONException unused) {
            jSONObject = new JSONObject();
        }
        NotificationManager notificationManager = (NotificationManager) iWebview.getContext().getSystemService("notification");
        Notification notification = null;
        boolean z = false;
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                builder = new Notification.Builder(iWebview.getContext(), "LOCAL_BADGE_NUM");
            } else {
                builder = new Notification.Builder(iWebview.getContext());
            }
            builder.setContentText(PdrUtil.isEmpty(jSONObject.optString(UriUtil.LOCAL_CONTENT_SCHEME)) ? StringUtil.format(iWebview.getContext().getString(R.string.dcloud_common_msg_unread_prompt), str) : jSONObject.optString(UriUtil.LOCAL_CONTENT_SCHEME));
            builder.setAutoCancel(true);
            int i = PdrR.getInt(iWebview.getContext(), "drawable", "push");
            if (i <= 0) {
                builder.setSmallIcon(iWebview.getContext().getApplicationInfo().icon);
            } else {
                builder.setSmallIcon(i);
            }
            builder.setDefaults(4);
            String packageName = iWebview.getActivity().getPackageName();
            PackageManager packageManager = iWebview.getActivity().getPackageManager();
            Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(packageName);
            builder.setContentTitle(PdrUtil.isEmpty(jSONObject.optString(AbsoluteConst.JSON_KEY_TITLE)) ? AndroidResources.mApplicationInfo.applicationInfo.loadLabel(packageManager) : jSONObject.optString(AbsoluteConst.JSON_KEY_TITLE));
            builder.setContentIntent(PendingIntent.getActivity(iWebview.getActivity(), 10019, launchIntentForPackage, 1073741824));
            builder.setAutoCancel(true);
            notification = builder.build();
            notification.flags = 16;
            Object obj = notification.getClass().getDeclaredField("extraNotification").get(notification);
            obj.getClass().getDeclaredMethod("setMessageCount", new Class[]{Integer.TYPE}).invoke(obj, new Object[]{Integer.valueOf(Integer.parseInt(str))});
            notificationManager.notify(101010, notification);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            intent.putExtra("android.intent.extra.update_application_component_name", this.a);
            intent.putExtra("android.intent.extra.update_application_message_text", str);
            iWebview.getContext().sendBroadcast(intent);
            return;
        } catch (Throwable th) {
            th = th;
        }
        if (notification != null && z) {
            notificationManager.notify(101010, notification);
        }
        throw th;
    }

    private void c(IWebview iWebview, String str) {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", Integer.parseInt(str));
        intent.putExtra("badge_count_package_name", iWebview.getContext().getPackageName());
        intent.putExtra("badge_count_class_name", this.a);
        iWebview.getContext().sendBroadcast(intent);
    }

    private void d(IWebview iWebview, String str) {
        boolean z = false;
        if (iWebview.getContext().getPackageManager().resolveContentProvider("com.sonymobile.home.resourceprovider", 0) != null) {
            if (Integer.parseInt(str) != 0) {
                z = true;
            }
            Intent intent = new Intent();
            intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", z);
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", this.a);
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", str);
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", iWebview.getContext().getPackageName());
            iWebview.getContext().sendBroadcast(intent);
            return;
        }
        Intent intent2 = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent2.putExtra("badge_count", str);
        intent2.putExtra("badge_count_package_name", iWebview.getContext().getPackageName());
        intent2.putExtra("badge_count_class_name", this.a);
        iWebview.getActivity().sendBroadcast(intent2);
    }

    private void e(IWebview iWebview, String str) {
        int i = 0;
        int parseInt = !str.equals("") ? Integer.parseInt(str) : 0;
        if (parseInt >= 0) {
            i = parseInt;
        }
        Bundle bundle = new Bundle();
        bundle.putString("package", iWebview.getContext().getPackageName());
        bundle.putString("class", iWebview.getContext().getPackageManager().getLaunchIntentForPackage(iWebview.getContext().getPackageName()).getComponent().getClassName());
        bundle.putInt("badgenumber", i);
        iWebview.getContext().getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", (String) null, bundle);
        ActionBus.getInstance().sendToBus(BadgeSyncAction.obtain(BadgeSyncAction.ENUM_ACTION_TYPE.SYNC_NUM).setSyncNum(i));
    }

    private void f(IWebview iWebview, String str) {
        int parseInt = Integer.parseInt(str);
        if (parseInt == 0) {
            parseInt = -1;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("app_badge_count", parseInt);
        iWebview.getContext().getContentResolver().call(Uri.parse("content://com.android.badge/badge"), "setAppBadgeCount", (String) null, bundle);
    }

    private void g(IWebview iWebview, String str) {
        try {
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", iWebview.getContext().getPackageName());
            intent.putExtra("className", iWebview.getContext().getPackageManager().getLaunchIntentForPackage(iWebview.getContext().getPackageName()).getComponent().getClassName());
            intent.putExtra("notificationNum", Integer.parseInt(str));
            iWebview.getContext().sendBroadcast(intent);
        } catch (Exception unused) {
        }
    }

    public void dispose(String str) {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0158  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r17, java.lang.String r18, java.lang.String[] r19) {
        /*
            r16 = this;
            r1 = r17
            r0 = r18
            java.lang.String r2 = "pname"
            r18.hashCode()
            r18.hashCode()
            int r3 = r18.hashCode()
            r4 = 3
            r5 = 2
            r6 = -1
            r7 = 0
            r8 = 1
            switch(r3) {
                case -1498445150: goto L_0x0075;
                case -1467480879: goto L_0x006a;
                case -748538349: goto L_0x005f;
                case 26235367: goto L_0x0054;
                case 208575698: goto L_0x0049;
                case 955720070: goto L_0x003e;
                case 1108535365: goto L_0x0033;
                case 1222725322: goto L_0x0028;
                case 1717055292: goto L_0x001b;
                default: goto L_0x0018;
            }
        L_0x0018:
            r3 = -1
            goto L_0x007f
        L_0x001b:
            java.lang.String r3 = "agreePrivacy"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0024
            goto L_0x0018
        L_0x0024:
            r3 = 8
            goto L_0x007f
        L_0x0028:
            java.lang.String r3 = "disagreePrivacy"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0031
            goto L_0x0018
        L_0x0031:
            r3 = 7
            goto L_0x007f
        L_0x0033:
            java.lang.String r3 = "downloadBlob"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x003c
            goto L_0x0018
        L_0x003c:
            r3 = 6
            goto L_0x007f
        L_0x003e:
            java.lang.String r3 = "isAgreePrivacy"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0047
            goto L_0x0018
        L_0x0047:
            r3 = 5
            goto L_0x007f
        L_0x0049:
            java.lang.String r3 = "isStreamValid"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0052
            goto L_0x0018
        L_0x0052:
            r3 = 4
            goto L_0x007f
        L_0x0054:
            java.lang.String r3 = "processDirectPage"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x005d
            goto L_0x0018
        L_0x005d:
            r3 = 3
            goto L_0x007f
        L_0x005f:
            java.lang.String r3 = "isCustomLaunchPath"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0068
            goto L_0x0018
        L_0x0068:
            r3 = 2
            goto L_0x007f
        L_0x006a:
            java.lang.String r3 = "isApplicationExist"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0073
            goto L_0x0018
        L_0x0073:
            r3 = 1
            goto L_0x007f
        L_0x0075:
            java.lang.String r3 = "getDCloudId"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x007e
            goto L_0x0018
        L_0x007e:
            r3 = 0
        L_0x007f:
            java.lang.String r9 = "true"
            java.lang.String r10 = "scok"
            java.lang.String r11 = "false"
            java.lang.String r12 = "pdr"
            java.lang.String r13 = "1"
            r14 = 0
            switch(r3) {
                case 0: goto L_0x023c;
                case 1: goto L_0x020a;
                case 2: goto L_0x01f7;
                case 3: goto L_0x01d9;
                case 4: goto L_0x01c9;
                case 5: goto L_0x0169;
                case 6: goto L_0x00b3;
                case 7: goto L_0x00a5;
                case 8: goto L_0x009b;
                default: goto L_0x008d;
            }
        L_0x008d:
            java.lang.Object[] r2 = new java.lang.Object[r4]
            r2[r7] = r1
            r2[r8] = r0
            r2[r5] = r19
            r15 = r16
            io.dcloud.common.adapter.util.MessageHandler.sendMessage(r15, r2)
            goto L_0x00b0
        L_0x009b:
            r15 = r16
            android.content.Context r0 = r17.getContext()
            io.dcloud.common.adapter.util.SP.setBundleData(r0, r12, r10, r13)
            goto L_0x00b0
        L_0x00a5:
            r15 = r16
            android.content.Context r0 = r17.getContext()
            java.lang.String r1 = "0"
            io.dcloud.common.adapter.util.SP.setBundleData(r0, r12, r10, r1)
        L_0x00b0:
            r9 = r14
            goto L_0x025f
        L_0x00b3:
            r15 = r16
            r0 = r19[r7]     // Catch:{ Exception -> 0x00be }
            r2 = r19[r5]     // Catch:{ Exception -> 0x00bc }
            r3 = r19[r8]     // Catch:{ Exception -> 0x00c0 }
            goto L_0x00c1
        L_0x00bc:
            r2 = r14
            goto L_0x00c0
        L_0x00be:
            r0 = r14
            r2 = r0
        L_0x00c0:
            r3 = r14
        L_0x00c1:
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r4 != 0) goto L_0x0158
            java.lang.String r4 = ","
            int r4 = r0.indexOf(r4)     // Catch:{ Exception -> 0x00d3 }
            int r4 = r4 + r8
            java.lang.String r0 = r0.substring(r4)     // Catch:{ Exception -> 0x00d3 }
            goto L_0x00d4
        L_0x00d3:
        L_0x00d4:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceRootDir
            r4.append(r5)
            java.lang.String r5 = "/Download/"
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            java.io.File r5 = new java.io.File
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r4)
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r3)
            if (r4 == 0) goto L_0x00ff
            long r3 = java.lang.System.currentTimeMillis()
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
        L_0x00ff:
            r8.append(r3)
            java.lang.String r3 = r8.toString()
            r5.<init>(r3)
            byte[] r0 = android.util.Base64.decode(r0, r7)
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x013e }
            r3.<init>(r5, r7)     // Catch:{ Exception -> 0x013e }
            r3.write(r0)     // Catch:{ Exception -> 0x013e }
            r3.flush()     // Catch:{ Exception -> 0x013e }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "{'code':0,'message':'"
            r0.append(r3)
            java.lang.String r3 = r5.getPath()
            r0.append(r3)
            java.lang.String r3 = "'}"
            r0.append(r3)
            java.lang.String r3 = r0.toString()
            int r4 = io.dcloud.common.util.JSUtil.ERROR
            r5 = 1
            r6 = 0
            r1 = r17
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)
            goto L_0x00b0
        L_0x013e:
            r0 = move-exception
            org.json.JSONObject r3 = new org.json.JSONObject
            r3.<init>()
            java.lang.String r4 = "code"
            r3.put(r4, r6)     // Catch:{ JSONException -> 0x0152 }
            java.lang.String r4 = "message"
            java.lang.String r0 = r0.toString()     // Catch:{ JSONException -> 0x0152 }
            r3.put(r4, r0)     // Catch:{ JSONException -> 0x0152 }
        L_0x0152:
            int r0 = io.dcloud.common.util.JSUtil.ERROR
            io.dcloud.common.util.JSUtil.execCallback((io.dcloud.common.DHInterface.IWebview) r1, (java.lang.String) r2, (org.json.JSONObject) r3, (int) r0, (boolean) r7)
            return r14
        L_0x0158:
            java.lang.String r0 = "blob error"
            java.lang.String r3 = io.dcloud.common.constant.DOMException.toJSON((int) r6, (java.lang.String) r0)
            int r4 = io.dcloud.common.util.JSUtil.ERROR
            r5 = 1
            r6 = 0
            r1 = r17
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r1, r2, r3, r4, r5, r6)
            goto L_0x00b0
        L_0x0169:
            r15 = r16
            android.content.Context r0 = r17.getContext()
            java.lang.String r0 = io.dcloud.common.adapter.util.SP.getBundleData((android.content.Context) r0, (java.lang.String) r12, (java.lang.String) r10)
            java.lang.String r1 = "DCLOUD_PRIVACY_PROMPT"
            java.lang.String r1 = io.dcloud.common.adapter.util.AndroidResources.getMetaValue(r1)
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r1)
            if (r2 != 0) goto L_0x01b6
            java.lang.String r2 = "template"
            boolean r2 = r1.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x0194
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r1 != 0) goto L_0x01c3
            boolean r0 = r0.equals(r13)
            if (r0 == 0) goto L_0x01c3
            goto L_0x01c2
        L_0x0194:
            java.lang.String r2 = "custom"
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x01a9
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r1 != 0) goto L_0x01c3
            boolean r0 = r0.equals(r13)
            if (r0 == 0) goto L_0x01c3
            goto L_0x01c2
        L_0x01a9:
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r1 != 0) goto L_0x01c2
            boolean r0 = r0.equals(r13)
            if (r0 == 0) goto L_0x01c3
            goto L_0x01c2
        L_0x01b6:
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r1 != 0) goto L_0x01c2
            boolean r0 = r0.equals(r13)
            if (r0 == 0) goto L_0x01c3
        L_0x01c2:
            r7 = 1
        L_0x01c3:
            java.lang.String r9 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r7)
            goto L_0x025f
        L_0x01c9:
            r15 = r16
            boolean r0 = io.dcloud.common.util.BaseInfo.existsStreamEnv()
            if (r0 == 0) goto L_0x0208
            boolean r0 = io.dcloud.common.util.BaseInfo.existsLibso()
            if (r0 == 0) goto L_0x0208
            goto L_0x025f
        L_0x01d9:
            r15 = r16
            io.dcloud.common.DHInterface.IApp r0 = r17.obtainApp()
            java.lang.String r0 = r0.getDirectPage()
            io.dcloud.common.DHInterface.IApp r1 = r17.obtainApp()
            r1.setDirectPage(r14)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x01f2
            java.lang.String r0 = ""
        L_0x01f2:
            java.lang.String r0 = io.dcloud.common.util.JSUtil.wrapJsVar((java.lang.String) r0)
            return r0
        L_0x01f7:
            r15 = r16
            io.dcloud.common.DHInterface.IFrameView r0 = r17.obtainFrameView()
            io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()
            boolean r0 = r0.checkIsCustomPath()
            if (r0 == 0) goto L_0x0208
            goto L_0x025f
        L_0x0208:
            r9 = r11
            goto L_0x025f
        L_0x020a:
            r15 = r16
            r0 = r19[r7]
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 == 0) goto L_0x0215
            return r11
        L_0x0215:
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0237 }
            r3.<init>(r0)     // Catch:{ JSONException -> 0x0237 }
            boolean r0 = r3.has(r2)     // Catch:{ JSONException -> 0x0237 }
            if (r0 != 0) goto L_0x0221
            return r11
        L_0x0221:
            java.lang.String r0 = r3.getString(r2)     // Catch:{ JSONException -> 0x0237 }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ JSONException -> 0x0237 }
            if (r2 == 0) goto L_0x022c
            return r11
        L_0x022c:
            android.content.Context r1 = r17.getContext()     // Catch:{ JSONException -> 0x0237 }
            boolean r0 = io.dcloud.common.util.LoadAppUtils.isAppLoad(r1, r0)     // Catch:{ JSONException -> 0x0237 }
            if (r0 == 0) goto L_0x0208
            return r9
        L_0x0237:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0208
        L_0x023c:
            r15 = r16
            android.content.Context r0 = r17.getContext()
            java.lang.String r2 = "_deviceId"
            java.lang.String r0 = io.dcloud.common.adapter.util.SP.getBundleData((android.content.Context) r0, (java.lang.String) r12, (java.lang.String) r2)
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r2 != 0) goto L_0x0253
            java.lang.String r9 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r8)
            goto L_0x025f
        L_0x0253:
            android.content.Context r0 = r17.getContext()
            java.lang.String r0 = io.dcloud.common.util.TelephonyUtil.getIMEI(r0, r8, r8)
            java.lang.String r9 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r8)
        L_0x025f:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.pdr.RuntimeFeatureImpl.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public void init(AbsMgr absMgr, String str) {
        this.b = absMgr;
        if (Build.VERSION.SDK_INT >= 26 && Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            NotificationManager notificationManager = (NotificationManager) absMgr.getContext().getSystemService("notification");
            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup("LOCAL_BADGE_SETTING", "badge"));
            NotificationChannel notificationChannel = new NotificationChannel("LOCAL_BADGE_NUM", absMgr.getContext().getString(R.string.dcloud_nf_desktop_icon_corner), 3);
            notificationChannel.enableLights(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void a(IWebview iWebview, String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                Intent intent = new Intent();
                intent.setClass(iWebview.getActivity(), WebviewActivity.class);
                intent.putExtra("url", str);
                intent.setData(Uri.parse(str));
                intent.setAction("android.intent.action.VIEW");
                intent.setFlags(268435456);
                iWebview.getContext().startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void a(String str, IApp iApp) {
        if (!TextUtils.isEmpty(str) && BaseInfo.ISAMU) {
            int length = str.length();
            if ((length - str.indexOf(".apk")) - 4 == 0 || (length - str.indexOf(".wgt")) - 4 == 0 || (length - str.indexOf(".wgtu")) - 5 == 0) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("type", "install");
                    jSONObject.put("file", str);
                    jSONObject.put("appid", iApp.obtainOriginalAppId());
                    jSONObject.put("version", iApp.obtainAppVersionName());
                    Log.i(AbsoluteConst.HBUILDER_TAG, jSONObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void b(IWebview iWebview, String str) {
        int i = 0;
        int parseInt = !str.equals("") ? Integer.parseInt(str) : 0;
        if (parseInt >= 0) {
            i = parseInt;
        }
        Bundle bundle = new Bundle();
        bundle.putString("package", iWebview.getContext().getPackageName());
        bundle.putString("class", iWebview.getContext().getPackageManager().getLaunchIntentForPackage(iWebview.getContext().getPackageName()).getComponent().getClassName());
        bundle.putInt("badgenumber", i);
        iWebview.getContext().getContentResolver().call(Uri.parse("content://com.hihonor.android.launcher.settings/badge/"), "change_badge", (String) null, bundle);
        ActionBus.getInstance().sendToBus(BadgeSyncAction.obtain(BadgeSyncAction.ENUM_ACTION_TYPE.SYNC_NUM).setSyncNum(i));
    }

    private void b(String str, IApp iApp) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "openurl");
            jSONObject.put("url", str);
            jSONObject.put("appid", iApp.obtainOriginalAppId());
            jSONObject.put("version", iApp.obtainAppVersionName());
            Log.i(AbsoluteConst.HBUILDER_TAG, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0256, code lost:
        if (r10.equals(io.dcloud.common.util.CustomPath.CUSTOM_PATH_DOC) != false) goto L_0x025a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0259, code lost:
        r3 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x025a, code lost:
        switch(r3) {
            case 0: goto L_0x0270;
            case 1: goto L_0x026d;
            case 2: goto L_0x026a;
            case 3: goto L_0x0267;
            case 4: goto L_0x0264;
            case 5: goto L_0x0261;
            case 6: goto L_0x025e;
            default: goto L_0x025d;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x025e, code lost:
        r10 = "application/pdf";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x0261, code lost:
        r10 = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0264, code lost:
        r10 = "application/vnd.ms-powerpoint";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0267, code lost:
        r10 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x026a, code lost:
        r10 = "application/vnd.ms-excel";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x026d, code lost:
        r10 = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x0270, code lost:
        r10 = "application/msword";
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void execute(java.lang.Object r17) {
        /*
            r16 = this;
            r1 = r16
            java.lang.String r0 = "newTask"
            r2 = r17
            java.lang.Object[] r2 = (java.lang.Object[]) r2
            r3 = 0
            r4 = r2[r3]
            r5 = r4
            io.dcloud.common.DHInterface.IWebview r5 = (io.dcloud.common.DHInterface.IWebview) r5
            r4 = 1
            r6 = r2[r4]
            java.lang.String r6 = java.lang.String.valueOf(r6)
            r7 = 2
            r2 = r2[r7]
            java.lang.String[] r2 = (java.lang.String[]) r2
            io.dcloud.common.DHInterface.IFrameView r8 = r5.obtainFrameView()
            io.dcloud.common.DHInterface.IApp r8 = r8.obtainApp()
            r6.hashCode()
            int r9 = r6.hashCode()
            r11 = 4
            r12 = -1
            r13 = 5
            r14 = 3
            switch(r9) {
                case -1263204667: goto L_0x00a7;
                case -1263202134: goto L_0x009c;
                case -1150858339: goto L_0x0091;
                case -944934523: goto L_0x0086;
                case -505062682: goto L_0x007b;
                case 3482191: goto L_0x0070;
                case 545494794: goto L_0x0065;
                case 1084758859: goto L_0x005a;
                case 1097506319: goto L_0x004d;
                case 1247092467: goto L_0x0040;
                case 1957569947: goto L_0x0033;
                default: goto L_0x0030;
            }
        L_0x0030:
            r6 = -1
            goto L_0x00b1
        L_0x0033:
            java.lang.String r9 = "install"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x003c
            goto L_0x0030
        L_0x003c:
            r6 = 10
            goto L_0x00b1
        L_0x0040:
            java.lang.String r9 = "showPrivacyDialog"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x0049
            goto L_0x0030
        L_0x0049:
            r6 = 9
            goto L_0x00b1
        L_0x004d:
            java.lang.String r9 = "restart"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x0056
            goto L_0x0030
        L_0x0056:
            r6 = 8
            goto L_0x00b1
        L_0x005a:
            java.lang.String r9 = "getProperty"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x0063
            goto L_0x0030
        L_0x0063:
            r6 = 7
            goto L_0x00b1
        L_0x0065:
            java.lang.String r9 = "setBadgeNumber"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x006e
            goto L_0x0030
        L_0x006e:
            r6 = 6
            goto L_0x00b1
        L_0x0070:
            java.lang.String r9 = "quit"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x0079
            goto L_0x0030
        L_0x0079:
            r6 = 5
            goto L_0x00b1
        L_0x007b:
            java.lang.String r9 = "openFile"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x0084
            goto L_0x0030
        L_0x0084:
            r6 = 4
            goto L_0x00b1
        L_0x0086:
            java.lang.String r9 = "openDocument"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x008f
            goto L_0x0030
        L_0x008f:
            r6 = 3
            goto L_0x00b1
        L_0x0091:
            java.lang.String r9 = "launchApplication"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x009a
            goto L_0x0030
        L_0x009a:
            r6 = 2
            goto L_0x00b1
        L_0x009c:
            java.lang.String r9 = "openWeb"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x00a5
            goto L_0x0030
        L_0x00a5:
            r6 = 1
            goto L_0x00b1
        L_0x00a7:
            java.lang.String r9 = "openURL"
            boolean r6 = r6.equals(r9)
            if (r6 != 0) goto L_0x00b0
            goto L_0x0030
        L_0x00b0:
            r6 = 0
        L_0x00b1:
            r9 = -4
            java.lang.String r15 = "pname"
            r10 = 0
            switch(r6) {
                case 0: goto L_0x0334;
                case 1: goto L_0x032e;
                case 2: goto L_0x02a5;
                case 3: goto L_0x01eb;
                case 4: goto L_0x01a2;
                case 5: goto L_0x0197;
                case 6: goto L_0x017f;
                case 7: goto L_0x014d;
                case 8: goto L_0x013d;
                case 9: goto L_0x00da;
                case 10: goto L_0x00ba;
                default: goto L_0x00b8;
            }
        L_0x00b8:
            goto L_0x0376
        L_0x00ba:
            android.content.Context r0 = r5.getContext()
            java.lang.String r4 = "Runtime-install"
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r0, r4)
            java.lang.String r0 = r5.obtainFullUrl()
            r3 = r2[r3]
            java.lang.String r0 = r8.convert2AbsFullPath(r0, r3)
            r1.a((java.lang.String) r0, (io.dcloud.common.DHInterface.IApp) r8)
            io.dcloud.feature.pdr.RuntimeFeatureImpl$a r3 = new io.dcloud.feature.pdr.RuntimeFeatureImpl$a
            r3.<init>(r2, r0, r5)
            r3.start()
            goto L_0x0376
        L_0x00da:
            r6 = r2[r3]
            r0 = r2[r4]
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00e4 }
            r2.<init>(r0)     // Catch:{ JSONException -> 0x00e4 }
            goto L_0x00e9
        L_0x00e4:
            r0 = move-exception
            r0.printStackTrace()
            r2 = r10
        L_0x00e9:
            io.dcloud.common.ui.b r0 = io.dcloud.common.ui.b.a()
            android.content.Context r7 = r5.getContext()
            r0.d(r7)
            if (r2 != 0) goto L_0x0102
            io.dcloud.common.ui.b r0 = io.dcloud.common.ui.b.a()
            android.content.Context r2 = r5.getContext()
            r0.b(r2, r10)
            goto L_0x0129
        L_0x0102:
            java.lang.String r0 = "config"
            org.json.JSONObject r0 = r2.optJSONObject(r0)
            if (r0 != 0) goto L_0x0116
            io.dcloud.common.ui.b r0 = io.dcloud.common.ui.b.a()
            android.content.Context r2 = r5.getContext()
            r0.b(r2, r10)
            goto L_0x0129
        L_0x0116:
            java.lang.String r0 = r2.toString()
            com.alibaba.fastjson.JSONObject r0 = com.alibaba.fastjson.JSON.parseObject(r0)
            io.dcloud.common.ui.b r2 = io.dcloud.common.ui.b.a()
            android.content.Context r7 = r5.getContext()
            r2.b(r7, r0)
        L_0x0129:
            android.content.Context r0 = r5.getContext()
            android.app.Activity r0 = (android.app.Activity) r0
            io.dcloud.common.ui.b r2 = io.dcloud.common.ui.b.a()
            io.dcloud.feature.pdr.RuntimeFeatureImpl$b r7 = new io.dcloud.feature.pdr.RuntimeFeatureImpl$b
            r7.<init>(r5, r6, r0)
            r2.a(r0, r7, r3, r4)
            goto L_0x0376
        L_0x013d:
            java.lang.String r0 = r8.obtainAppId()
            r8.clearRuntimeArgs()
            io.dcloud.common.DHInterface.AbsMgr r2 = r1.b
            io.dcloud.common.DHInterface.IMgr$MgrType r3 = io.dcloud.common.DHInterface.IMgr.MgrType.AppMgr
            r2.processEvent(r3, r14, r0)
            goto L_0x0376
        L_0x014d:
            r0 = r2[r3]
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r3 == 0) goto L_0x0161
            io.dcloud.common.DHInterface.IFrameView r0 = r5.obtainFrameView()
            io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()
            java.lang.String r0 = r0.obtainAppId()
        L_0x0161:
            r2 = r2[r4]
            io.dcloud.common.DHInterface.AbsMgr r3 = r1.b
            io.dcloud.common.DHInterface.IMgr$MgrType r6 = io.dcloud.common.DHInterface.IMgr.MgrType.AppMgr
            java.lang.Object r0 = r3.processEvent(r6, r13, r0)
            java.lang.String r0 = java.lang.String.valueOf(r0)
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r3 != 0) goto L_0x017a
            io.dcloud.common.util.Deprecated_JSUtil.excCallbackSuccess(r5, r2, r0, r4)
            goto L_0x0376
        L_0x017a:
            io.dcloud.common.util.Deprecated_JSUtil.excCallbackError(r5, r2, r10)
            goto L_0x0376
        L_0x017f:
            int r0 = r2.length
            if (r0 <= r4) goto L_0x018d
            r0 = r2[r4]
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r0 != 0) goto L_0x018d
            r0 = r2[r4]
            goto L_0x0190
        L_0x018d:
            java.lang.String r0 = "{}"
        L_0x0190:
            r2 = r2[r3]
            r1.a(r5, r2, r0)
            goto L_0x0376
        L_0x0197:
            io.dcloud.common.DHInterface.AbsMgr r0 = r1.b
            io.dcloud.common.DHInterface.IMgr$MgrType r2 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            r3 = 20
            r0.processEvent(r2, r3, r8)
            goto L_0x0376
        L_0x01a2:
            r0 = r2[r3]
            java.lang.String r3 = r8.checkPrivateDirAndCopy2Temp(r0)
            r6 = r2[r7]
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x01b6 }
            r2 = r2[r4]     // Catch:{ Exception -> 0x01b6 }
            r0.<init>(r2)     // Catch:{ Exception -> 0x01b6 }
            java.lang.String r0 = r0.optString(r15)     // Catch:{ Exception -> 0x01b6 }
            goto L_0x01bb
        L_0x01b6:
            r0 = move-exception
            r0.printStackTrace()
            r0 = r10
        L_0x01bb:
            java.lang.String r2 = r5.obtainFullUrl()
            java.lang.String r2 = r8.convert2AbsFullPath(r2, r3)
            java.io.File r3 = new java.io.File
            r3.<init>(r2)
            boolean r3 = r3.isFile()
            if (r3 == 0) goto L_0x01dc
            android.app.Activity r3 = r8.getActivity()
            io.dcloud.feature.pdr.RuntimeFeatureImpl$c r4 = new io.dcloud.feature.pdr.RuntimeFeatureImpl$c
            r4.<init>(r5, r6)
            io.dcloud.common.adapter.util.PlatformUtil.openFileBySystem(r3, r2, r0, r10, r4)
            goto L_0x0376
        L_0x01dc:
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_FILE_NOT_EXIST
            java.lang.String r7 = io.dcloud.common.constant.DOMException.toJSON((int) r9, (java.lang.String) r0)
            int r8 = io.dcloud.common.util.JSUtil.ERROR
            r9 = 1
            r10 = 0
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r5, r6, r7, r8, r9, r10)
            goto L_0x0376
        L_0x01eb:
            r0 = r2[r3]
            java.lang.String r0 = r8.checkPrivateDirAndCopy2Temp(r0)
            r6 = r2[r7]
            org.json.JSONObject r10 = new org.json.JSONObject     // Catch:{ Exception -> 0x0273 }
            r2 = r2[r4]     // Catch:{ Exception -> 0x0273 }
            r10.<init>(r2)     // Catch:{ Exception -> 0x0273 }
            java.lang.String r2 = r10.optString(r15)     // Catch:{ Exception -> 0x0273 }
            java.lang.String r15 = "fileType"
            java.lang.String r10 = r10.optString(r15)     // Catch:{ Exception -> 0x0274 }
            java.util.Locale r15 = java.util.Locale.ENGLISH     // Catch:{ Exception -> 0x0274 }
            java.lang.String r10 = r10.toLowerCase(r15)     // Catch:{ Exception -> 0x0274 }
            int r15 = r10.hashCode()     // Catch:{ Exception -> 0x0274 }
            switch(r15) {
                case 99640: goto L_0x0250;
                case 110834: goto L_0x0246;
                case 111220: goto L_0x023c;
                case 118783: goto L_0x0231;
                case 3088960: goto L_0x0227;
                case 3447940: goto L_0x021d;
                case 3682393: goto L_0x0212;
                default: goto L_0x0211;
            }     // Catch:{ Exception -> 0x0274 }
        L_0x0211:
            goto L_0x0259
        L_0x0212:
            java.lang.String r3 = "xlsx"
            boolean r3 = r10.equals(r3)     // Catch:{ Exception -> 0x0274 }
            if (r3 == 0) goto L_0x0259
            r3 = 3
            goto L_0x025a
        L_0x021d:
            java.lang.String r3 = "pptx"
            boolean r3 = r10.equals(r3)     // Catch:{ Exception -> 0x0274 }
            if (r3 == 0) goto L_0x0259
            r3 = 5
            goto L_0x025a
        L_0x0227:
            java.lang.String r3 = "docx"
            boolean r3 = r10.equals(r3)     // Catch:{ Exception -> 0x0274 }
            if (r3 == 0) goto L_0x0259
            r3 = 1
            goto L_0x025a
        L_0x0231:
            java.lang.String r3 = "xls"
            boolean r3 = r10.equals(r3)     // Catch:{ Exception -> 0x0274 }
            if (r3 == 0) goto L_0x0259
            r3 = 2
            goto L_0x025a
        L_0x023c:
            java.lang.String r3 = "ppt"
            boolean r3 = r10.equals(r3)     // Catch:{ Exception -> 0x0274 }
            if (r3 == 0) goto L_0x0259
            r3 = 4
            goto L_0x025a
        L_0x0246:
            java.lang.String r3 = "pdf"
            boolean r3 = r10.equals(r3)     // Catch:{ Exception -> 0x0274 }
            if (r3 == 0) goto L_0x0259
            r3 = 6
            goto L_0x025a
        L_0x0250:
            java.lang.String r4 = "doc"
            boolean r4 = r10.equals(r4)     // Catch:{ Exception -> 0x0274 }
            if (r4 == 0) goto L_0x0259
            goto L_0x025a
        L_0x0259:
            r3 = -1
        L_0x025a:
            switch(r3) {
                case 0: goto L_0x0270;
                case 1: goto L_0x026d;
                case 2: goto L_0x026a;
                case 3: goto L_0x0267;
                case 4: goto L_0x0264;
                case 5: goto L_0x0261;
                case 6: goto L_0x025e;
                default: goto L_0x025d;
            }
        L_0x025d:
            goto L_0x0274
        L_0x025e:
            java.lang.String r10 = "application/pdf"
            goto L_0x0275
        L_0x0261:
            java.lang.String r10 = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            goto L_0x0275
        L_0x0264:
            java.lang.String r10 = "application/vnd.ms-powerpoint"
            goto L_0x0275
        L_0x0267:
            java.lang.String r10 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            goto L_0x0275
        L_0x026a:
            java.lang.String r10 = "application/vnd.ms-excel"
            goto L_0x0275
        L_0x026d:
            java.lang.String r10 = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            goto L_0x0275
        L_0x0270:
            java.lang.String r10 = "application/msword"
            goto L_0x0275
        L_0x0273:
            r2 = 0
        L_0x0274:
            r10 = 0
        L_0x0275:
            java.lang.String r3 = r5.obtainFullUrl()
            java.lang.String r0 = r8.convert2AbsFullPath(r3, r0)
            java.io.File r3 = new java.io.File
            r3.<init>(r0)
            boolean r3 = r3.isFile()
            if (r3 == 0) goto L_0x0296
            android.app.Activity r3 = r8.getActivity()
            io.dcloud.feature.pdr.RuntimeFeatureImpl$d r4 = new io.dcloud.feature.pdr.RuntimeFeatureImpl$d
            r4.<init>(r5, r6)
            io.dcloud.common.adapter.util.PlatformUtil.openFileBySystem(r3, r0, r2, r10, r4)
            goto L_0x0376
        L_0x0296:
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_FILE_NOT_EXIST
            java.lang.String r7 = io.dcloud.common.constant.DOMException.toJSON((int) r9, (java.lang.String) r0)
            int r8 = io.dcloud.common.util.JSUtil.ERROR
            r9 = 1
            r10 = 0
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r5, r6, r7, r8, r9, r10)
            goto L_0x0376
        L_0x02a5:
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch:{ Exception -> 0x0315 }
            r7 = r2[r3]     // Catch:{ Exception -> 0x0315 }
            r6.<init>(r7)     // Catch:{ Exception -> 0x0315 }
            org.json.JSONArray r7 = r6.names()     // Catch:{ Exception -> 0x0315 }
            java.util.HashMap r8 = new java.util.HashMap     // Catch:{ Exception -> 0x0315 }
            r8.<init>()     // Catch:{ Exception -> 0x0315 }
            r9 = 0
            r10 = 0
            r11 = 1
        L_0x02b8:
            int r12 = r7.length()     // Catch:{ Exception -> 0x0315 }
            if (r3 >= r12) goto L_0x030d
            java.lang.String r12 = r7.getString(r3)     // Catch:{ Exception -> 0x0315 }
            boolean r13 = r12.equals(r15)     // Catch:{ Exception -> 0x0315 }
            if (r13 == 0) goto L_0x02cd
            java.lang.String r10 = r6.getString(r12)     // Catch:{ Exception -> 0x0315 }
            goto L_0x0309
        L_0x02cd:
            java.lang.String r13 = "action"
            boolean r13 = r12.equals(r13)     // Catch:{ Exception -> 0x0315 }
            if (r13 == 0) goto L_0x02da
            java.lang.String r9 = r6.getString(r12)     // Catch:{ Exception -> 0x0315 }
            goto L_0x0309
        L_0x02da:
            java.lang.String r13 = "extra"
            boolean r13 = r12.equals(r13)     // Catch:{ Exception -> 0x0315 }
            if (r13 == 0) goto L_0x02ff
            org.json.JSONObject r12 = r6.getJSONObject(r12)     // Catch:{ Exception -> 0x0315 }
            java.util.Iterator r13 = r12.keys()     // Catch:{ Exception -> 0x0315 }
        L_0x02ea:
            boolean r14 = r13.hasNext()     // Catch:{ Exception -> 0x0315 }
            if (r14 == 0) goto L_0x0309
            java.lang.Object r14 = r13.next()     // Catch:{ Exception -> 0x0315 }
            java.lang.String r14 = (java.lang.String) r14     // Catch:{ Exception -> 0x0315 }
            java.lang.Object r4 = r12.get(r14)     // Catch:{ Exception -> 0x0315 }
            r8.put(r14, r4)     // Catch:{ Exception -> 0x0315 }
            r4 = 1
            goto L_0x02ea
        L_0x02ff:
            boolean r4 = r12.equals(r0)     // Catch:{ Exception -> 0x0315 }
            if (r4 == 0) goto L_0x0309
            boolean r11 = r6.getBoolean(r0)     // Catch:{ Exception -> 0x0315 }
        L_0x0309:
            int r3 = r3 + 1
            r4 = 1
            goto L_0x02b8
        L_0x030d:
            android.app.Activity r0 = r5.getActivity()     // Catch:{ Exception -> 0x0315 }
            io.dcloud.common.adapter.util.PlatformUtil.launchApplication(r0, r10, r9, r8, r11)     // Catch:{ Exception -> 0x0315 }
            goto L_0x0376
        L_0x0315:
            r0 = move-exception
            r0.printStackTrace()
            r3 = 1
            r6 = r2[r3]
            java.lang.String r0 = r0.getMessage()
            r2 = -99
            java.lang.String r7 = io.dcloud.common.constant.DOMException.toJSON((int) r2, (java.lang.String) r0)
            int r8 = io.dcloud.common.util.JSUtil.OK
            r9 = 1
            r10 = 0
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r5, r6, r7, r8, r9, r10)
            goto L_0x0376
        L_0x032e:
            r0 = r2[r3]
            r1.a((io.dcloud.common.DHInterface.IWebview) r5, (java.lang.String) r0)
            goto L_0x0376
        L_0x0334:
            r0 = r2[r3]     // Catch:{ Exception -> 0x0368 }
            boolean r4 = io.dcloud.common.util.PdrUtil.isDeviceRootDir(r0)     // Catch:{ Exception -> 0x0368 }
            if (r4 != 0) goto L_0x0351
            boolean r4 = io.dcloud.common.util.PdrUtil.isNetPath(r0)     // Catch:{ Exception -> 0x0368 }
            if (r4 != 0) goto L_0x0351
            java.lang.String r4 = "file://"
            boolean r4 = r0.startsWith(r4)     // Catch:{ Exception -> 0x0368 }
            if (r4 != 0) goto L_0x0351
            boolean r0 = r8.checkSchemeWhite(r0)     // Catch:{ Exception -> 0x0368 }
            if (r0 != 0) goto L_0x0351
            return
        L_0x0351:
            r0 = r2[r3]     // Catch:{ Exception -> 0x0368 }
            r1.b((java.lang.String) r0, (io.dcloud.common.DHInterface.IApp) r8)     // Catch:{ Exception -> 0x0368 }
            android.app.Activity r0 = r5.getActivity()     // Catch:{ Exception -> 0x0368 }
            r3 = r2[r3]     // Catch:{ Exception -> 0x0368 }
            java.lang.Object r4 = io.dcloud.common.util.PdrUtil.getObject(r2, r7)     // Catch:{ Exception -> 0x0368 }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Exception -> 0x0368 }
            io.dcloud.common.adapter.util.PlatformUtil.openURL(r0, r3, r4)     // Catch:{ Exception -> 0x0368 }
            goto L_0x0376
        L_0x0368:
            r0 = move-exception
            r0.printStackTrace()
            r3 = 1
            r2 = r2[r3]
            java.lang.String r0 = r0.getMessage()
            io.dcloud.common.util.Deprecated_JSUtil.excCallbackError(r5, r2, r0)
        L_0x0376:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.pdr.RuntimeFeatureImpl.execute(java.lang.Object):void");
    }
}
