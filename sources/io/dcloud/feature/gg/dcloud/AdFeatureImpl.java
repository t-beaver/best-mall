package io.dcloud.feature.gg.dcloud;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.common.util.UriUtil;
import com.taobao.weex.common.WXRequest;
import io.dcloud.WebAppActivity;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.NotificationUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.hostpicker.HostPicker;
import io.dcloud.e.c.g;
import io.dcloud.e.f.b;
import io.dcloud.feature.gg.AdSplashUtil;
import io.dcloud.feature.gg.dcloud.ADHandler;
import io.dcloud.feature.gg.dcloud.ADResult;
import io.dcloud.feature.gg.dcloud.AdFeatureImpl;
import io.dcloud.h.c.d.a;
import io.dcloud.sdk.core.entry.SplashConfig;
import io.dcloud.sdk.core.v2.splash.DCSplashAOLLoadListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class AdFeatureImpl {
    /* access modifiers changed from: private */
    public static volatile boolean isRequestSuccess = false;
    /* access modifiers changed from: private */
    public static boolean isSplashClose = false;
    /* access modifiers changed from: private */
    public static Handler mHandler = new MyHandler();
    /* access modifiers changed from: private */
    public static int retryCount = 0;
    /* access modifiers changed from: private */
    public static a splashAd;

    protected static class AdReceiver implements IADReceiver {
        private Object[] _args;
        /* access modifiers changed from: private */
        public String appid;
        /* access modifiers changed from: private */
        public Context context;

        public AdReceiver(Context context2, Object[] objArr, String str) {
            this.context = context2;
            this._args = objArr;
            this.appid = str;
        }

        public List<String> getActivities() {
            ArrayList arrayList = new ArrayList();
            try {
                ActivityInfo[] activityInfoArr = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 1).activities;
                if (activityInfoArr != null) {
                    for (ActivityInfo activityInfo : activityInfoArr) {
                        arrayList.add(activityInfo.name);
                    }
                }
                ServiceInfo[] serviceInfoArr = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 4).services;
                if (serviceInfoArr != null) {
                    for (ServiceInfo serviceInfo : serviceInfoArr) {
                        arrayList.add(serviceInfo.name);
                    }
                }
                ProviderInfo[] providerInfoArr = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 8).providers;
                if (providerInfoArr != null) {
                    for (ProviderInfo providerInfo : providerInfoArr) {
                        arrayList.add(providerInfo.name);
                    }
                }
                ActivityInfo[] activityInfoArr2 = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 2).receivers;
                if (activityInfoArr2 != null) {
                    for (ActivityInfo activityInfo2 : activityInfoArr2) {
                        arrayList.add(activityInfo2.name);
                    }
                }
            } catch (Exception unused) {
            }
            return arrayList;
        }

        public /* synthetic */ void lambda$onReceiver$0$AdFeatureImpl$AdReceiver() {
            Context context2 = this.context;
            if (context2 instanceof WebAppActivity) {
                ((WebAppActivity) context2).onCreateAdSplash(context2);
                ((WebAppActivity) this.context).initBackToFrontSplashAd();
            }
        }

        public void onError(String str, String str2) {
            int i;
            boolean unused = AdFeatureImpl.isRequestSuccess = false;
            SP.setsBundleData(ADHandler.AdTag, "uniad", "");
            Logger.p("request Fail", "type:" + str + ";message:" + str2);
            if (AdFeatureImpl.retryCount < 3) {
                AdFeatureImpl.access$508();
                Message message = new Message();
                message.what = 1;
                message.obj = new Runnable() {
                    public void run() {
                        io.dcloud.a.a(AdReceiver.this.context, AdReceiver.this.appid, "pull", "RETRY");
                    }
                };
                AdFeatureImpl.mHandler.sendMessageDelayed(message, (long) (AdFeatureImpl.retryCount * WXRequest.DEFAULT_TIMEOUT_MS));
            }
            if (this._args[2] == null) {
                try {
                    i = Integer.parseInt(str2);
                } catch (Exception unused2) {
                    i = -1;
                }
                Context context2 = this.context;
                if (i != -1) {
                    str2 = "http:" + str2;
                }
                AdFeatureImpl.setRequest(context2, "-8001", str2, (JSONArray) null);
            }
        }

        public void onReceiver(JSONObject jSONObject) {
            JSONObject jSONObject2;
            Logger.p("doForFeature", "success when request");
            if (AdFeatureImpl.isSplashClose && this._args[2] == null && ADHandler.SplashAdIsEnable(this.context).booleanValue()) {
                AdFeatureImpl.setRequest(this.context, "-8002", "广告关闭时未请求成功", (JSONArray) null);
            }
            boolean unused = AdFeatureImpl.isRequestSuccess = true;
            try {
                JSONObject optJSONObject = jSONObject.optJSONObject("data");
                HashMap hashMap = new HashMap();
                JSONObject jSONObject3 = new JSONObject();
                b bVar = SP.getsOrCreateBundle(this.context, ADHandler.AdTag);
                String str = "";
                if (optJSONObject != null) {
                    String optString = optJSONObject.has("uniad") ? optJSONObject.optString("uniad") : str;
                    if (optJSONObject.has("al")) {
                        bVar.b("al", optJSONObject.optString("al"));
                    }
                    if (optJSONObject.has("cad")) {
                        JSONObject optJSONObject2 = optJSONObject.optJSONObject("cad");
                        if (optJSONObject2 != null) {
                            List<String> activities = getActivities();
                            Iterator<String> keys = optJSONObject2.keys();
                            while (keys.hasNext()) {
                                String next = keys.next();
                                JSONObject jSONObject4 = optJSONObject2.getJSONObject(next);
                                JSONArray optJSONArray = jSONObject4.optJSONArray("mf-a");
                                if (optJSONArray != null) {
                                    if (optJSONArray.length() > 0) {
                                        jSONObject2 = optJSONObject2;
                                        int i = 0;
                                        while (true) {
                                            if (i >= optJSONArray.length()) {
                                                break;
                                            } else if (activities.contains(optJSONArray.getString(i))) {
                                                JSONObject jSONObject5 = new JSONObject();
                                                jSONObject5.put("r", "1");
                                                jSONObject3.put(next, jSONObject5);
                                                break;
                                            } else {
                                                i++;
                                            }
                                        }
                                        optJSONObject2 = jSONObject2;
                                    }
                                }
                                jSONObject2 = optJSONObject2;
                                JSONArray jSONArray = jSONObject4.getJSONArray("cls-a");
                                int i2 = 0;
                                while (i2 < jSONArray.length()) {
                                    try {
                                        Class.forName(jSONArray.getString(i2));
                                        JSONObject jSONObject6 = new JSONObject();
                                        jSONObject6.put("r", "1");
                                        jSONObject3.put(next, jSONObject6);
                                        break;
                                    } catch (Exception unused2) {
                                        i2++;
                                    }
                                }
                                optJSONObject2 = jSONObject2;
                            }
                            if (jSONObject3.length() > 0) {
                                str = jSONObject3.toString();
                            }
                            hashMap.put("cad", str);
                        }
                    } else {
                        hashMap.put("cad", str);
                    }
                    bVar.b("uniad", optString);
                    bVar.b("cgk", optString);
                } else {
                    bVar.b("uniad", str);
                    hashMap.put("cad", str);
                }
                AdSplashUtil.saveOperate(hashMap);
                if (jSONObject3.length() > 0) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("rad", jSONObject3.toString());
                    Context context2 = this.context;
                    ADHandler.pullRad(context2, hashMap2, new ADHandler.ADReceiver(context2), new ADResult.CADReceiver(this.context));
                }
                if (AdFeatureImpl.splashAd != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public final void run() {
                            AdFeatureImpl.AdReceiver.this.lambda$onReceiver$0$AdFeatureImpl$AdReceiver();
                        }
                    });
                    AdFeatureImpl.splashAd.v();
                }
            } catch (Exception unused3) {
            }
        }
    }

    private static class MyHandler extends Handler {
        private MyHandler() {
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                ((Runnable) message.obj).run();
            }
        }
    }

    static /* synthetic */ int access$508() {
        int i = retryCount;
        retryCount = i + 1;
        return i;
    }

    public static Object doForFeature(String str, Object obj) {
        a aVar;
        if ("onAppCreate".equals(str)) {
            Context context = (Context) obj;
            Logger.p("doForFeature", "AdFeatureImpl onAppCreate");
            String str2 = ADHandler.get("uniad");
            if (!TextUtils.isEmpty(str2)) {
                SP.setsBundleData(context, ADHandler.AdTag, "uniad", str2);
                SP.setsBundleData(context, ADHandler.AdTag, "cgk", str2);
                SP.removeBundleData(context, ADHandler.AdTag, "uniad");
                SP.removeBundleData(context, ADHandler.AdTag, "cgk");
            }
        } else if ("pull".equals(str) || "ba_pull".equals(str)) {
            Object[] objArr = (Object[]) obj;
            final Context context2 = (Context) objArr[0];
            String str3 = (String) objArr[1];
            if (str.equals("ba_pull") && g.b() && !io.dcloud.e.c.h.a.d(context2, str3)) {
                return null;
            }
            b.a(context2, ADHandler.AdTag);
            ADHandler.pull(context2, str3, false, (List<HostPicker.Host>) null, new ADHandler.ADReceiver(context2), new ADResult.CADReceiver(context2), new AdReceiver(context2, objArr, str3));
            if ((PdrUtil.isEmpty(objArr[2]) || !"RETRY".equals(objArr[2])) && !AppRuntime.hasPrivacyForNotShown(context2)) {
                splashAd = null;
                isSplashClose = false;
                isRequestSuccess = false;
                mHandler.removeMessages(1);
                retryCount = 0;
                SplashConfig build = new SplashConfig.Builder().width(0).height(0).build();
                a aVar2 = new a((Activity) context2);
                splashAd = aVar2;
                aVar2.a(build, (DCSplashAOLLoadListener) new DCSplashAOLLoadListener() {
                    static /* synthetic */ void lambda$onSplashAdLoad$0(Context context) {
                        if (context instanceof WebAppActivity) {
                            WebAppActivity webAppActivity = (WebAppActivity) context;
                            webAppActivity.onCreateAdSplash(context);
                            webAppActivity.initBackToFrontSplashAd();
                        }
                    }

                    public void onError(int i, String str, JSONArray jSONArray) {
                        Logger.e("doForFeature", "AdFeatureImpl load fail" + i + ":" + str);
                        if (i != -5000) {
                            AdFeatureImpl.setRequest(context2, "-8003", str, jSONArray);
                        }
                    }

                    public void onSplashAdLoad() {
                        Logger.e("doForFeature", "AdFeatureImpl load success");
                        if (AdFeatureImpl.isRequestSuccess) {
                            new Handler(Looper.getMainLooper()).post(new Runnable(context2) {
                                public final /* synthetic */ Context f$0;

                                {
                                    this.f$0 = r1;
                                }

                                public final void run() {
                                    AdFeatureImpl.AnonymousClass1.lambda$onSplashAdLoad$0(this.f$0);
                                }
                            });
                        }
                    }

                    public void pushAd(JSONObject jSONObject) {
                        pushAd(jSONObject, context2);
                    }

                    public void redBag(View view, FrameLayout.LayoutParams layoutParams) {
                        ((ViewGroup) ((Activity) context2).getWindow().getDecorView().findViewById(16908290)).addView(view, layoutParams);
                    }

                    private void pushAd(final JSONObject jSONObject, final Context context) {
                        String optString = jSONObject.optString(AbsoluteConst.JSON_KEY_ICON);
                        if (!TextUtils.isEmpty(optString)) {
                            Glide.with(context).asBitmap().load(optString).into(new CustomTarget<Bitmap>() {
                                public void onLoadCleared(Drawable drawable) {
                                }

                                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                    String optString = jSONObject.optString(AbsoluteConst.JSON_KEY_TITLE);
                                    String optString2 = jSONObject.optString(UriUtil.LOCAL_CONTENT_SCHEME);
                                    Intent intent = new Intent();
                                    intent.putExtra("dcloud.push.broswer", AbsoluteConst.TRUE);
                                    intent.setClassName(context, "io.dcloud.PandoraEntry");
                                    intent.putExtra("__json__", jSONObject.toString());
                                    intent.putExtra("appid", BaseInfo.sDefaultBootApp);
                                    intent.putExtra("adid", ADHandler.get(context, "adid"));
                                    String str = optString;
                                    Bitmap bitmap2 = bitmap;
                                    NotificationUtil.createCustomNotification(context, str, bitmap2, optString, optString2, jSONObject.hashCode(), PendingIntent.getActivity(context, intent.hashCode(), intent, 1073741824));
                                }
                            });
                        }
                    }
                }, false);
            }
            AdSplashUtil.setShowInterstitialAd(false);
        } else if ("save".equals(str)) {
            Object[] objArr2 = (Object[]) obj;
            Logger.p("doForFeature", "AdFeatureImpl save");
            AdSplashUtil.saveOperate((Context) objArr2[0], (String) objArr2[1], (HashMap) objArr2[2]);
        } else if ("formatUrl_wanka".equals(str)) {
            Object[] objArr3 = (Object[]) ((Object[]) obj)[2];
            return ADHandler.formatUrl((String) objArr3[0], (JSONObject) objArr3[1]);
        } else if ("handleArgs_wanka".equals(str)) {
            return ADHandler.getArgsJsonData((JSONObject) ((Object[]) obj)[2]);
        } else {
            if ("onWillCloseSplash".equals(str)) {
                isSplashClose = true;
                isRequestSuccess = false;
                Object[] objArr4 = (Object[]) obj;
                Context context3 = (Context) objArr4[0];
                if (context3 instanceof Activity) {
                    Activity activity = (Activity) context3;
                    if (!activity.isDestroyed() && !activity.isFinishing() && (aVar = splashAd) != null) {
                        aVar.a((View) objArr4[2]);
                    }
                }
                return null;
            } else if ("onCloseSplashNoAd".equals(str)) {
                isSplashClose = true;
                Context context4 = (Context) ((Object[]) obj)[0];
                if (context4 instanceof Activity) {
                    Activity activity2 = (Activity) context4;
                    if (!activity2.isDestroyed() && !activity2.isFinishing()) {
                        Logger.p("doForFeature", "AdFeatureImpl onCloseSplashNoAd");
                        if (!ADHandler.SplashAdIsEnable(context4).booleanValue()) {
                            return null;
                        }
                    }
                }
                return null;
            } else if ("onCreateAdSplash".equals(str)) {
                if (!isRequestSuccess || splashAd == null || isSplashClose) {
                    return null;
                }
                Object[] objArr5 = (Object[]) obj;
                Context context5 = (Context) objArr5[0];
                if (context5 instanceof Activity) {
                    Activity activity3 = (Activity) context5;
                    if (activity3.isDestroyed() || activity3.isFinishing() || !ADHandler.SplashAdIsEnable(context5).booleanValue()) {
                        return null;
                    }
                    ICallBack iCallBack = (ICallBack) objArr5[1];
                    Logger.p("doForFeature", "AdFeatureImpl onCreateAdSplash");
                    a aVar3 = splashAd;
                    if (aVar3 != null && aVar3.l()) {
                        return splashAd.a(activity3, "", iCallBack);
                    }
                }
                return null;
            } else if ("onAppAttachBaseContext".equals(str)) {
                Logger.p("doForFeature", "AdFeatureImpl onAppAttachBaseContext");
            } else if ("onSplashclosed".equals(str)) {
                a aVar4 = splashAd;
                if (aVar4 != null) {
                    aVar4.w();
                }
                splashAd = null;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static void setRequest(Context context, String str, String str2, JSONArray jSONArray) {
        if (!context.getPackageName().equals("io.dcloud.HBuilder")) {
            ADHandler.postSplashError(context, str, str2, jSONArray);
        }
    }
}
