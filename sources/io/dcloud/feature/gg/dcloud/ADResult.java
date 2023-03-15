package io.dcloud.feature.gg.dcloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Process;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import io.dcloud.EntryProxy;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.ui.c;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.feature.gg.AdSplashUtil;
import io.dcloud.feature.gg.dcloud.ADHandler;
import io.dcloud.feature.gg.dcloud.AdFeatureImpl;
import io.dcloud.feature.ui.nativeui.b;
import io.dcloud.h.c.a;
import io.dcloud.h.c.c.a.b.d;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ADResult {
    private String d;
    private d handler = ((d) a.d().a());
    private IADReceiver[] receivers;

    public static class CADReceiver implements IADReceiver {
        private Context context;

        public CADReceiver(Context context2) {
            this.context = context2;
        }

        public void checkPromptData(JSONArray jSONArray) {
            try {
                if (jSONArray.length() > 0) {
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        if (jSONObject != null && jSONObject.length() > 0) {
                            if (jSONObject.optString("action").equals("prompt")) {
                                final String optString = jSONObject.optString("onclose");
                                AlertDialog create = new AlertDialog.Builder(this.context).setTitle(jSONObject.optString(AbsoluteConst.JSON_KEY_TITLE)).setMessage(jSONObject.optString("message")).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (optString.equals(BindingXConstants.STATE_EXIT)) {
                                            Process.killProcess(Process.myPid());
                                        } else {
                                            dialogInterface.dismiss();
                                        }
                                    }
                                }).create();
                                create.setCanceledOnTouchOutside(false);
                                create.show();
                            } else if (jSONObject.optString("action").equals("toast")) {
                                b bVar = new b((Activity) this.context, "");
                                TextView textView = new TextView(this.context);
                                textView.setAutoLinkMask(1);
                                textView.setClickable(true);
                                textView.setText(c.a(this.context).a(jSONObject.optString("message")));
                                LinearLayout linearLayout = new LinearLayout(this.context);
                                linearLayout.addView(textView);
                                bVar.a(linearLayout, textView);
                                bVar.setDuration(1);
                                bVar.setGravity(80, bVar.getXOffset(), bVar.getYOffset());
                                int dip2px = CanvasHelper.dip2px(this.context, 10.0f);
                                int dip2px2 = CanvasHelper.dip2px(this.context, 8.0f);
                                linearLayout.setPadding(dip2px, dip2px2, dip2px, dip2px2);
                                GradientDrawable gradientDrawable = new GradientDrawable();
                                gradientDrawable.setCornerRadius((float) dip2px2);
                                gradientDrawable.setShape(0);
                                gradientDrawable.setColor(-1308622848);
                                linearLayout.setBackground(gradientDrawable);
                                textView.setGravity(17);
                                textView.setTextColor(Color.parseColor("#ffffffff"));
                                bVar.show();
                            }
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }

        public void onError(String str, String str2) {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
            r3 = r3.optJSONArray("data");
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceiver(org.json.JSONObject r3) {
            /*
                r2 = this;
                java.lang.String r0 = "data"
                boolean r1 = r3.has(r0)
                if (r1 == 0) goto L_0x0020
                org.json.JSONArray r3 = r3.optJSONArray(r0)
                if (r3 == 0) goto L_0x0020
                int r0 = r3.length()
                if (r0 <= 0) goto L_0x0020
                android.content.Context r0 = r2.context
                android.app.Activity r0 = (android.app.Activity) r0
                io.dcloud.feature.gg.dcloud.ADResult$CADReceiver$1 r1 = new io.dcloud.feature.gg.dcloud.ADResult$CADReceiver$1
                r1.<init>(r3)
                r0.runOnUiThread(r1)
            L_0x0020:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gg.dcloud.ADResult.CADReceiver.onReceiver(org.json.JSONObject):void");
        }
    }

    public ADResult(IADReceiver... iADReceiverArr) {
        this.receivers = iADReceiverArr;
    }

    private String gd() {
        IApp iApp = (IApp) EntryProxy.getInstnace().getCoreHandler().dispatchEvent(IMgr.MgrType.AppMgr, 28, BaseInfo.sDefaultBootApp);
        if (iApp != null) {
            Activity activity = iApp.getActivity();
            this.d = new JSONObject(this.handler.b(activity)).toString();
            this.receivers[0] = new ADHandler.ADReceiver(activity);
            this.receivers[1] = new CADReceiver(activity);
            this.receivers[2] = new AdFeatureImpl.AdReceiver(activity, new Object[]{null, null, ""}, iApp.obtainAppId());
        }
        return this.d;
    }

    public void cad(String str) {
        if (this.receivers != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                int optInt = jSONObject.optInt("ret", -1);
                if (optInt == 0) {
                    for (IADReceiver iADReceiver : this.receivers) {
                        if ((iADReceiver instanceof CADReceiver) || (iADReceiver instanceof ADHandler.ADReceiver)) {
                            iADReceiver.onReceiver(jSONObject);
                        }
                    }
                    return;
                }
                for (IADReceiver iADReceiver2 : this.receivers) {
                    if ((iADReceiver2 instanceof CADReceiver) || (iADReceiver2 instanceof ADHandler.ADReceiver)) {
                        iADReceiver2.onError(String.valueOf(optInt), jSONObject.optString("desc"));
                    }
                }
            } catch (JSONException e) {
                for (IADReceiver iADReceiver3 : this.receivers) {
                    if ((iADReceiver3 instanceof CADReceiver) || (iADReceiver3 instanceof ADHandler.ADReceiver)) {
                        iADReceiver3.onError("Exception", e.getMessage());
                    }
                }
            }
        }
    }

    public void dc(String str, int i, int i2) {
        IADReceiver[] iADReceiverArr = this.receivers;
        if (iADReceiverArr != null) {
            int i3 = 0;
            if (i != 0) {
                int length = iADReceiverArr.length;
                while (i3 < length) {
                    IADReceiver iADReceiver = iADReceiverArr[i3];
                    if (!(iADReceiver instanceof CADReceiver)) {
                        iADReceiver.onError("NotFountDataError", DOMException.MSG_NETWORK_ERROR);
                    }
                    i3++;
                }
                d dVar = this.handler;
                if (dVar != null) {
                    dVar.a(-5007, DOMException.MSG_NETWORK_ERROR);
                }
            } else if (i2 != 200) {
                int length2 = iADReceiverArr.length;
                while (i3 < length2) {
                    IADReceiver iADReceiver2 = iADReceiverArr[i3];
                    if (!(iADReceiver2 instanceof CADReceiver)) {
                        iADReceiver2.onError("NotFountDataError", String.valueOf(i2));
                    }
                    i3++;
                }
                d dVar2 = this.handler;
                if (dVar2 != null) {
                    dVar2.a(-5007, String.valueOf(i2));
                }
            } else {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    int optInt = jSONObject.optInt("ret", -1);
                    if (optInt == 0) {
                        for (IADReceiver iADReceiver3 : this.receivers) {
                            if (!(iADReceiver3 instanceof CADReceiver)) {
                                AdSplashUtil.decodeChannel(jSONObject.optJSONObject("data"));
                                iADReceiver3.onReceiver(jSONObject);
                            }
                        }
                        d dVar3 = this.handler;
                        if (dVar3 != null) {
                            dVar3.a(jSONObject);
                            return;
                        }
                        return;
                    }
                    for (IADReceiver iADReceiver4 : this.receivers) {
                        if (!(iADReceiver4 instanceof CADReceiver)) {
                            iADReceiver4.onError(String.valueOf(optInt), jSONObject.optString("desc"));
                        }
                    }
                    d dVar4 = this.handler;
                    if (dVar4 != null) {
                        dVar4.a(-5007, String.valueOf(optInt) + ":" + jSONObject.optString("desc"));
                    }
                } catch (Exception e) {
                    IADReceiver[] iADReceiverArr2 = this.receivers;
                    int length3 = iADReceiverArr2.length;
                    while (i3 < length3) {
                        IADReceiver iADReceiver5 = iADReceiverArr2[i3];
                        if (!(iADReceiver5 instanceof CADReceiver)) {
                            iADReceiver5.onError("Exception", e.getMessage());
                        }
                        i3++;
                    }
                    d dVar5 = this.handler;
                    if (dVar5 != null) {
                        dVar5.a(-5007, e.getMessage());
                    }
                }
            }
        }
    }
}
