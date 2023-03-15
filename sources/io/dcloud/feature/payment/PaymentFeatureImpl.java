package io.dcloud.feature.payment;

import android.app.Application;
import android.content.Context;
import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;

public class PaymentFeatureImpl implements IFeature {
    String a;
    AbsMgr b;
    Context c;
    ArrayList<AbsPaymentChannel> d = null;

    private void a(IWebview iWebview) {
        HashMap hashMap = (HashMap) this.b.processEvent(IMgr.MgrType.FeatureMgr, 4, this.a);
        if (hashMap != null && !hashMap.isEmpty()) {
            for (String str : hashMap.keySet()) {
                try {
                    Object newInstance = Class.forName((String) hashMap.get(str)).newInstance();
                    if (newInstance instanceof AbsPaymentChannel) {
                        AbsPaymentChannel absPaymentChannel = (AbsPaymentChannel) newInstance;
                        absPaymentChannel.init(this.c);
                        absPaymentChannel.name = str;
                        absPaymentChannel.featureName = this.a;
                        if (absPaymentChannel.id == null) {
                            absPaymentChannel.id = str;
                        }
                        this.d.add(absPaymentChannel);
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                } catch (ClassNotFoundException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public void dispose(String str) {
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        AbsPaymentChannel a2;
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Payment-getUserInfo");
        if ("getChannels".equals(str)) {
            if (this.d.isEmpty()) {
                a(iWebview);
            }
            JSUtil.execCallback(iWebview, strArr[0], a(), JSUtil.OK, false);
            return null;
        } else if (AbsURIAdapter.REQUEST.equals(str)) {
            AbsPaymentChannel a3 = a(strArr[0]);
            String str2 = strArr[1];
            String str3 = strArr[2];
            if (a3 != null) {
                if (a3.id.equalsIgnoreCase("wxpay")) {
                    Application application = iWebview.getActivity().getApplication();
                    if (application instanceof DCloudApplication) {
                        ((DCloudApplication) application).stopB2FOnce();
                    }
                }
                a3.updateWebview(iWebview);
                a3.a(str2, str3);
                return null;
            }
            IWebview iWebview2 = iWebview;
            Deprecated_JSUtil.execCallback(iWebview2, str3, StringUtil.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(IPaymentListener.CODE_UNKNOWN), "not found channel"), JSUtil.ERROR, true, false);
            return null;
        } else if ("installService".equals(str)) {
            AbsPaymentChannel a4 = a(strArr[0]);
            if (a4 == null) {
                return null;
            }
            a4.updateWebview(iWebview);
            a4.installService();
            return null;
        } else if (!"isReadyToPay".equals(str) || (a2 = a(strArr[0])) == null) {
            return null;
        } else {
            a2.updateWebview(iWebview);
            a2.isReadyToPay(strArr[1], strArr[2]);
            return null;
        }
    }

    public void init(AbsMgr absMgr, String str) {
        this.a = str;
        this.b = absMgr;
        this.c = absMgr.getContext();
        this.d = new ArrayList<>(2);
    }

    private JSONArray a() {
        JSONArray jSONArray = new JSONArray();
        int size = this.d.size();
        for (int i = 0; i < size; i++) {
            jSONArray.put(this.d.get(i).toJSONObject());
        }
        return jSONArray;
    }

    private AbsPaymentChannel a(String str) {
        Iterator<AbsPaymentChannel> it = this.d.iterator();
        while (it.hasNext()) {
            AbsPaymentChannel next = it.next();
            if (next.id.equals(str)) {
                return next;
            }
        }
        return null;
    }
}
