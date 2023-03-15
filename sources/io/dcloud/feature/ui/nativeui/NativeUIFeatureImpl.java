package io.dcloud.feature.ui.nativeui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.module.WXModalUIModule;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.feature.nativeObj.photoview.LongClickEventManager;
import io.dcloud.feature.ui.nativeui.a;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NativeUIFeatureImpl implements IFeature {
    HashMap<String, c> a = null;
    HashMap<String, a> b = null;
    DatePickerDialog c = null;
    int d;
    int e;
    int f;
    TimePickerDialog g = null;
    int h;
    int i;
    final byte j = 0;
    final byte k = 1;
    final byte l = 2;
    AbsMgr m;

    class a implements DialogInterface.OnClickListener {
        final /* synthetic */ byte a;
        final /* synthetic */ EditText b;
        final /* synthetic */ IWebview c;
        final /* synthetic */ String d;
        final /* synthetic */ int e;
        final /* synthetic */ AlertDialog f;

        a(byte b2, EditText editText, IWebview iWebview, String str, int i, AlertDialog alertDialog) {
            this.a = b2;
            this.b = editText;
            this.c = iWebview;
            this.d = str;
            this.e = i;
            this.f = alertDialog;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (this.a == 2) {
                String jSONableString = JSONUtil.toJSONableString(this.b.getText().toString());
                IWebview iWebview = this.c;
                String str = this.d;
                Deprecated_JSUtil.execCallback(iWebview, str, "{index:" + this.e + ",message:" + jSONableString + Operators.BLOCK_END_STR, JSUtil.OK, true, false);
            }
            if (this.a == 1) {
                Deprecated_JSUtil.execCallback(this.c, this.d, String.valueOf(this.e), JSUtil.OK, true, false);
            }
            this.f.dismiss();
        }
    }

    class b implements DialogInterface.OnKeyListener {
        final /* synthetic */ byte a;
        final /* synthetic */ EditText b;
        final /* synthetic */ IWebview c;
        final /* synthetic */ String d;
        final /* synthetic */ AlertDialog e;

        b(byte b2, EditText editText, IWebview iWebview, String str, AlertDialog alertDialog) {
            this.a = b2;
            this.b = editText;
            this.c = iWebview;
            this.d = str;
            this.e = alertDialog;
        }

        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() != 1 || i != 4) {
                return false;
            }
            if (this.a == 2) {
                String jSONableString = JSONUtil.toJSONableString(this.b.getText().toString());
                IWebview iWebview = this.c;
                String str = this.d;
                Deprecated_JSUtil.execCallback(iWebview, str, "{index:-1,message:" + jSONableString + Operators.BLOCK_END_STR, JSUtil.OK, true, false);
            }
            if (this.a == 1) {
                Deprecated_JSUtil.execCallback(this.c, this.d, String.valueOf(-1), JSUtil.OK, true, false);
            }
            this.e.dismiss();
            return true;
        }
    }

    class c extends LongClickEventManager.OnLongClickListener {
        c(IWebview iWebview, String str) {
            super(iWebview, str);
        }

        public void onLongClickListener(JSONObject jSONObject) {
            Deprecated_JSUtil.execCallback(getPwebview(), getCallbackIds(), jSONObject.toString(), JSUtil.OK, true, true);
        }
    }

    class d implements a.b {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;
        final /* synthetic */ String c;

        d(IWebview iWebview, String str, String str2) {
            this.a = iWebview;
            this.b = str;
            this.c = str2;
        }

        public void initCancelText(TextView textView) {
        }

        public void initTextItem(int i, TextView textView, String str) {
        }

        public boolean onDismiss(int i) {
            HashMap<String, a> hashMap = NativeUIFeatureImpl.this.b;
            if (hashMap == null || !hashMap.containsKey(this.c)) {
                return false;
            }
            NativeUIFeatureImpl.this.b.remove(this.c);
            return false;
        }

        public void onItemClick(int i) {
            IWebview iWebview = this.a;
            String str = this.b;
            Deprecated_JSUtil.execCallback(iWebview, str, "" + i, JSUtil.OK, true, false);
        }
    }

    class e implements DatePickerDialog.OnDateSetListener {
        e() {
        }

        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            NativeUIFeatureImpl nativeUIFeatureImpl = NativeUIFeatureImpl.this;
            nativeUIFeatureImpl.d = i;
            nativeUIFeatureImpl.e = i2;
            nativeUIFeatureImpl.f = i3;
        }
    }

    class f extends DatePickerDialog {
        int a;
        int b;
        int c;
        final /* synthetic */ int d;
        final /* synthetic */ int e;
        final /* synthetic */ int f;
        final /* synthetic */ boolean g;
        final /* synthetic */ IWebview h;
        final /* synthetic */ String i;
        final /* synthetic */ NativeUIFeatureImpl j;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        f(NativeUIFeatureImpl nativeUIFeatureImpl, Context context, int i2, DatePickerDialog.OnDateSetListener onDateSetListener, int i3, int i4, int i5, int i6, int i7, int i8, boolean z, IWebview iWebview, String str) {
            super(context, i2, onDateSetListener, i3, i4, i5);
            int i9 = i6;
            int i10 = i7;
            int i11 = i8;
            this.j = nativeUIFeatureImpl;
            this.d = i9;
            this.e = i10;
            this.f = i11;
            this.g = z;
            this.h = iWebview;
            this.i = str;
            this.a = i9;
            this.b = i10;
            this.c = i11;
        }

        public void onClick(DialogInterface dialogInterface, int i2) {
            super.onClick(dialogInterface, i2);
            if (i2 == -2) {
                Deprecated_JSUtil.execCallback(this.h, this.i, DOMException.toJSON(-2, DOMException.MSG_USER_CANCEL), JSUtil.ERROR, true, false);
            } else if (i2 == -1) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                if (DeviceInfo.sDeviceSdkVer >= 21) {
                    NativeUIFeatureImpl nativeUIFeatureImpl = this.j;
                    gregorianCalendar.set(nativeUIFeatureImpl.d, nativeUIFeatureImpl.e, nativeUIFeatureImpl.f, 0, 0, 0);
                } else {
                    gregorianCalendar.set(this.a, this.b, this.c, 0, 0, 0);
                }
                Deprecated_JSUtil.execCallback(this.h, this.i, String.valueOf(gregorianCalendar.getTime().getTime()), JSUtil.OK, true, false);
            }
            this.j.a();
        }

        /* access modifiers changed from: protected */
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            getWindow().setSoftInputMode(2);
        }

        public void onDateChanged(DatePicker datePicker, int i2, int i3, int i4) {
            this.a = i2;
            this.b = i3;
            this.c = i4;
            if (!this.g && !DeviceInfo.sVersion_release.equals("4.0.3") && !DeviceInfo.sVersion_release.equals("4.0.4")) {
                super.onDateChanged(datePicker, i2, i3, i4);
            }
        }
    }

    class g implements DialogInterface.OnDismissListener {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        g(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public void onDismiss(DialogInterface dialogInterface) {
            if (NativeUIFeatureImpl.this.c != null) {
                Deprecated_JSUtil.execCallback(this.a, this.b, DOMException.toJSON(-2, DOMException.MSG_USER_CANCEL), JSUtil.ERROR, true, false);
                NativeUIFeatureImpl.this.a();
            }
        }
    }

    class h implements TimePickerDialog.OnTimeSetListener {
        h() {
        }

        public void onTimeSet(TimePicker timePicker, int i, int i2) {
            NativeUIFeatureImpl nativeUIFeatureImpl = NativeUIFeatureImpl.this;
            nativeUIFeatureImpl.h = i;
            nativeUIFeatureImpl.i = i2;
        }
    }

    class i extends TimePickerDialog {
        int a;
        int b;
        final /* synthetic */ int c;
        final /* synthetic */ int d;
        final /* synthetic */ boolean e;
        final /* synthetic */ IWebview f;
        final /* synthetic */ String g;
        final /* synthetic */ NativeUIFeatureImpl h;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        i(NativeUIFeatureImpl nativeUIFeatureImpl, Context context, int i, TimePickerDialog.OnTimeSetListener onTimeSetListener, int i2, int i3, boolean z, int i4, int i5, boolean z2, IWebview iWebview, String str) {
            super(context, i, onTimeSetListener, i2, i3, z);
            int i6 = i4;
            int i7 = i5;
            this.h = nativeUIFeatureImpl;
            this.c = i6;
            this.d = i7;
            this.e = z2;
            this.f = iWebview;
            this.g = str;
            this.a = i6;
            this.b = i7;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            super.onClick(dialogInterface, i);
            if (i == -2) {
                Deprecated_JSUtil.execCallback(this.f, this.g, DOMException.toJSON(-2, DOMException.MSG_USER_CANCEL), JSUtil.ERROR, true, false);
            } else if (i == -1) {
                Date date = new Date();
                if (DeviceInfo.sDeviceSdkVer >= 21) {
                    date.setHours(this.h.h);
                    date.setMinutes(this.h.i);
                } else {
                    date.setHours(this.a);
                    date.setMinutes(this.b);
                }
                Deprecated_JSUtil.execCallback(this.f, this.g, String.valueOf(date.getTime()), JSUtil.OK, true, false);
            }
            this.h.b();
        }

        /* access modifiers changed from: protected */
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            getWindow().setSoftInputMode(2);
        }

        public void onTimeChanged(TimePicker timePicker, int i, int i2) {
            this.a = i;
            this.b = i2;
            if (!this.e) {
                setTitle(i + ":" + i2);
            }
        }
    }

    class j implements DialogInterface.OnDismissListener {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        j(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public void onDismiss(DialogInterface dialogInterface) {
            if (NativeUIFeatureImpl.this.g != null) {
                Deprecated_JSUtil.execCallback(this.a, this.b, DOMException.toJSON(-2, DOMException.MSG_USER_CANCEL), JSUtil.ERROR, true, false);
                NativeUIFeatureImpl.this.b();
            }
        }
    }

    class k implements DialogInterface.OnClickListener {
        final /* synthetic */ AlertDialog a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;

        k(AlertDialog alertDialog, IWebview iWebview, String str) {
            this.a = alertDialog;
            this.b = iWebview;
            this.c = str;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            this.a.dismiss();
            Deprecated_JSUtil.execCallback(this.b, this.c, "{index:0}", JSUtil.OK, true, false);
        }
    }

    class l implements DialogInterface.OnKeyListener {
        final /* synthetic */ AlertDialog a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;

        l(AlertDialog alertDialog, IWebview iWebview, String str) {
            this.a = alertDialog;
            this.b = iWebview;
            this.c = str;
        }

        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() != 1 || i != 4) {
                return false;
            }
            this.a.dismiss();
            Deprecated_JSUtil.execCallback(this.b, this.c, "{index:-1}", JSUtil.OK, true, false);
            return true;
        }
    }

    public void dispose(String str) {
        if (PdrUtil.isEmpty(str)) {
            this.m = null;
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r18, java.lang.String r19, java.lang.String[] r20) {
        /*
            r17 = this;
            r10 = r17
            r8 = r18
            r0 = r19
            r1 = r20
            io.dcloud.common.DHInterface.IApp r2 = r18.obtainApp()
            int r3 = r1.length
            r4 = 1
            r5 = 0
            if (r3 < r4) goto L_0x0015
            r3 = r1[r5]
            r9 = r3
            goto L_0x0016
        L_0x0015:
            r9 = 0
        L_0x0016:
            int r3 = r1.length
            r6 = 2
            if (r3 < r6) goto L_0x0021
            r3 = r1[r4]
            org.json.JSONArray r3 = io.dcloud.common.util.JSONUtil.createJSONArray(r3)
            goto L_0x0022
        L_0x0021:
            r3 = 0
        L_0x0022:
            r19.hashCode()
            int r7 = r19.hashCode()
            java.lang.String r12 = "isTitlebarVisible"
            java.lang.String r13 = "setTitlebarVisible"
            java.lang.String r14 = "getTitlebarHeight"
            java.lang.String r15 = "showMenu"
            java.lang.String r11 = "hideMenu"
            r16 = 5
            switch(r7) {
                case -2071870705: goto L_0x0133;
                case -1774127679: goto L_0x012a;
                case -1771264837: goto L_0x011f;
                case -1727401845: goto L_0x0114;
                case -1383206285: goto L_0x0109;
                case -1055312317: goto L_0x00fe;
                case -979805852: goto L_0x00f3;
                case -739906705: goto L_0x00e8;
                case -739422578: goto L_0x00da;
                case -339042820: goto L_0x00ce;
                case -228775627: goto L_0x00c0;
                case -160355144: goto L_0x00b4;
                case -92053387: goto L_0x00a8;
                case -49471494: goto L_0x009a;
                case 92899676: goto L_0x008c;
                case 110532135: goto L_0x007e;
                case 235955885: goto L_0x0072;
                case 951117504: goto L_0x0064;
                case 988815851: goto L_0x0056;
                case 1572298953: goto L_0x0048;
                case 2141060722: goto L_0x003a;
                default: goto L_0x0038;
            }
        L_0x0038:
            goto L_0x013e
        L_0x003a:
            java.lang.String r7 = "WaitingView"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0044
            goto L_0x013e
        L_0x0044:
            r0 = 20
            goto L_0x013f
        L_0x0048:
            java.lang.String r7 = "actionSheet"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0052
            goto L_0x013e
        L_0x0052:
            r0 = 19
            goto L_0x013f
        L_0x0056:
            java.lang.String r7 = "WaitingView_close"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0060
            goto L_0x013e
        L_0x0060:
            r0 = 18
            goto L_0x013f
        L_0x0064:
            java.lang.String r7 = "confirm"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x006e
            goto L_0x013e
        L_0x006e:
            r0 = 17
            goto L_0x013f
        L_0x0072:
            boolean r0 = r0.equals(r12)
            if (r0 != 0) goto L_0x007a
            goto L_0x013e
        L_0x007a:
            r0 = 16
            goto L_0x013f
        L_0x007e:
            java.lang.String r7 = "toast"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0088
            goto L_0x013e
        L_0x0088:
            r0 = 15
            goto L_0x013f
        L_0x008c:
            java.lang.String r7 = "alert"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0096
            goto L_0x013e
        L_0x0096:
            r0 = 14
            goto L_0x013f
        L_0x009a:
            java.lang.String r7 = "_NativeObj_close"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x00a4
            goto L_0x013e
        L_0x00a4:
            r0 = 13
            goto L_0x013f
        L_0x00a8:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x00b0
            goto L_0x013e
        L_0x00b0:
            r0 = 12
            goto L_0x013f
        L_0x00b4:
            boolean r0 = r0.equals(r14)
            if (r0 != 0) goto L_0x00bc
            goto L_0x013e
        L_0x00bc:
            r0 = 11
            goto L_0x013f
        L_0x00c0:
            java.lang.String r7 = "closeWaiting"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x00ca
            goto L_0x013e
        L_0x00ca:
            r0 = 10
            goto L_0x013f
        L_0x00ce:
            boolean r0 = r0.equals(r15)
            if (r0 != 0) goto L_0x00d6
            goto L_0x013e
        L_0x00d6:
            r0 = 9
            goto L_0x013f
        L_0x00da:
            java.lang.String r7 = "pickTime"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x00e4
            goto L_0x013e
        L_0x00e4:
            r0 = 8
            goto L_0x013f
        L_0x00e8:
            java.lang.String r7 = "pickDate"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x00f1
            goto L_0x013e
        L_0x00f1:
            r0 = 7
            goto L_0x013f
        L_0x00f3:
            java.lang.String r7 = "prompt"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x00fc
            goto L_0x013e
        L_0x00fc:
            r0 = 6
            goto L_0x013f
        L_0x00fe:
            java.lang.String r7 = "WaitingView_setTitle"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0107
            goto L_0x013e
        L_0x0107:
            r0 = 5
            goto L_0x013f
        L_0x0109:
            java.lang.String r7 = "previewImage"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0112
            goto L_0x013e
        L_0x0112:
            r0 = 4
            goto L_0x013f
        L_0x0114:
            java.lang.String r7 = "closePreviewImage"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x011d
            goto L_0x013e
        L_0x011d:
            r0 = 3
            goto L_0x013f
        L_0x011f:
            java.lang.String r7 = "setUiStyle"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0128
            goto L_0x013e
        L_0x0128:
            r0 = 2
            goto L_0x013f
        L_0x012a:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x0131
            goto L_0x013e
        L_0x0131:
            r0 = 1
            goto L_0x013f
        L_0x0133:
            java.lang.String r7 = "closeToast"
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x013c
            goto L_0x013e
        L_0x013c:
            r0 = 0
            goto L_0x013f
        L_0x013e:
            r0 = -1
        L_0x013f:
            java.lang.String r7 = "io.dcloud.feature.nativeObj.photoview.PhotoActivity"
            java.lang.String r6 = "io.dcloud.appstream.actionbar.StreamAppActionBarUtil"
            switch(r0) {
                case 0: goto L_0x0542;
                case 1: goto L_0x050d;
                case 2: goto L_0x0503;
                case 3: goto L_0x04f9;
                case 4: goto L_0x0412;
                case 5: goto L_0x03fd;
                case 6: goto L_0x03c2;
                case 7: goto L_0x03b5;
                case 8: goto L_0x03a8;
                case 9: goto L_0x033b;
                case 10: goto L_0x0318;
                case 11: goto L_0x02ea;
                case 12: goto L_0x02af;
                case 13: goto L_0x0298;
                case 14: goto L_0x0272;
                case 15: goto L_0x0265;
                case 16: goto L_0x0228;
                case 17: goto L_0x01c2;
                case 18: goto L_0x01b1;
                case 19: goto L_0x0182;
                case 20: goto L_0x0148;
                default: goto L_0x0146;
            }
        L_0x0146:
            goto L_0x0549
        L_0x0148:
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)
            org.json.JSONObject r5 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r3, (int) r4)
            r1 = 2
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r1)
            android.app.Activity r1 = r18.getActivity()
            android.app.Activity r7 = io.dcloud.common.util.RuningAcitvityUtil.getTopRuningActivity(r1)
            boolean r1 = r7.isDestroyed()
            if (r1 != 0) goto L_0x0549
            io.dcloud.feature.ui.nativeui.c r11 = new io.dcloud.feature.ui.nativeui.c
            r1 = r11
            r2 = r17
            r3 = r18
            r4 = r0
            r1.<init>(r2, r3, r4, r5, r6, r7)
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.nativeui.c> r0 = r10.a
            if (r0 != 0) goto L_0x0179
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r10.a = r0
        L_0x0179:
            r11.g = r9
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.nativeui.c> r0 = r10.a
            r0.put(r9, r11)
            goto L_0x0549
        L_0x0182:
            r1 = 2
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r1)
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r3, (int) r5)
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r4)
            io.dcloud.feature.ui.nativeui.a r1 = r10.a((java.lang.String) r0, (org.json.JSONObject) r1, (java.lang.String) r2, (io.dcloud.common.DHInterface.IWebview) r8)
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r2 != 0) goto L_0x0549
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r1)
            if (r2 != 0) goto L_0x0549
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.nativeui.a> r2 = r10.b
            if (r2 != 0) goto L_0x01aa
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>()
            r10.b = r2
        L_0x01aa:
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.nativeui.a> r2 = r10.b
            r2.put(r0, r1)
            goto L_0x0549
        L_0x01b1:
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.nativeui.c> r0 = r10.a
            if (r0 == 0) goto L_0x0549
            java.lang.Object r0 = r0.remove(r9)
            io.dcloud.feature.ui.nativeui.c r0 = (io.dcloud.feature.ui.nativeui.c) r0
            if (r0 == 0) goto L_0x0549
            r0.a()
            goto L_0x0549
        L_0x01c2:
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)     // Catch:{ JSONException -> 0x0222 }
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r4)     // Catch:{ JSONException -> 0x0222 }
            r1 = 2
            java.lang.Object r1 = r3.get(r1)     // Catch:{ JSONException -> 0x0222 }
            boolean r2 = r1 instanceof org.json.JSONObject     // Catch:{ JSONException -> 0x0222 }
            if (r2 == 0) goto L_0x01e2
            r2 = 1
            r4 = r1
            org.json.JSONObject r4 = (org.json.JSONObject) r4     // Catch:{ JSONException -> 0x0222 }
            r1 = r17
            r3 = r0
            r5 = r18
            r6 = r9
            r1.a(r2, r3, r4, r5, r6)     // Catch:{ JSONException -> 0x0222 }
            goto L_0x0549
        L_0x01e2:
            boolean r2 = r1 instanceof java.lang.String     // Catch:{ JSONException -> 0x0222 }
            if (r2 == 0) goto L_0x0213
            r4 = r1
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ JSONException -> 0x0222 }
            r1 = 3
            org.json.JSONArray r1 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONArray) r3, (int) r1)     // Catch:{ JSONException -> 0x0222 }
            if (r1 == 0) goto L_0x0203
            int r2 = r1.length()     // Catch:{ JSONException -> 0x0222 }
            java.lang.String[] r3 = new java.lang.String[r2]     // Catch:{ JSONException -> 0x0222 }
        L_0x01f6:
            if (r5 >= r2) goto L_0x0201
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r1, (int) r5)     // Catch:{ JSONException -> 0x0222 }
            r3[r5] = r6     // Catch:{ JSONException -> 0x0222 }
            int r5 = r5 + 1
            goto L_0x01f6
        L_0x0201:
            r6 = r3
            goto L_0x0204
        L_0x0203:
            r6 = 0
        L_0x0204:
            r2 = 1
            r5 = 0
            r7 = 0
            r1 = r17
            r3 = r4
            r4 = r5
            r5 = r0
            r8 = r18
            r1.a(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch:{ JSONException -> 0x0222 }
            goto L_0x0549
        L_0x0213:
            r2 = 1
            r3 = 0
            r4 = 0
            r6 = 0
            r7 = 0
            r1 = r17
            r5 = r0
            r8 = r18
            r1.a(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch:{ JSONException -> 0x0222 }
            goto L_0x0549
        L_0x0222:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0549
        L_0x0228:
            r1 = 2
            java.lang.Class[] r0 = new java.lang.Class[r1]
            java.lang.Class<android.app.Activity> r2 = android.app.Activity.class
            r0[r5] = r2
            java.lang.Class<java.lang.String> r2 = java.lang.String.class
            r0[r4] = r2
            java.lang.Object[] r1 = new java.lang.Object[r1]
            android.app.Activity r2 = r18.getActivity()
            r1[r5] = r2
            io.dcloud.common.DHInterface.IApp r2 = r18.obtainApp()
            java.lang.String r2 = r2.obtainAppId()
            r1[r4] = r2
            r2 = 0
            java.lang.Object r0 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r6, r12, r2, r0, r1)
            boolean r1 = r0 instanceof java.lang.Boolean
            if (r1 == 0) goto L_0x025b
            java.lang.String r0 = r0.toString()
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            boolean r0 = r0.booleanValue()
            goto L_0x025c
        L_0x025b:
            r0 = 0
        L_0x025c:
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r5)
            return r0
        L_0x0265:
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r3, (int) r4)
            r10.a((io.dcloud.common.DHInterface.IApp) r2, (io.dcloud.common.DHInterface.IWebview) r8, (java.lang.String) r0, (org.json.JSONObject) r1)
            goto L_0x0549
        L_0x0272:
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r4)
            r1 = 2
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r1)
            r1 = 3
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r1)
            java.lang.String[] r7 = new java.lang.String[r4]
            r7[r5] = r1
            r2 = 0
            r4 = 0
            r11 = 0
            r1 = r17
            r3 = r6
            r5 = r0
            r6 = r7
            r7 = r11
            r8 = r18
            r1.a(r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x0549
        L_0x0298:
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.nativeui.a> r0 = r10.b
            if (r0 == 0) goto L_0x0549
            java.lang.Object r0 = r0.remove(r9)
            io.dcloud.feature.ui.nativeui.a r0 = (io.dcloud.feature.ui.nativeui.a) r0
            if (r0 == 0) goto L_0x0549
            r1 = -1
            r0.a((int) r1)
            io.dcloud.feature.ui.nativeui.a$b r0 = r0.b
            r0.onItemClick(r1)
            goto L_0x0549
        L_0x02af:
            boolean r0 = r3.getBoolean(r5)     // Catch:{ Exception -> 0x02b5 }
        L_0x02b3:
            r1 = 3
            goto L_0x02bc
        L_0x02b5:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
            r0 = 1
            goto L_0x02b3
        L_0x02bc:
            java.lang.Class[] r2 = new java.lang.Class[r1]
            java.lang.Class<android.app.Activity> r3 = android.app.Activity.class
            r2[r5] = r3
            java.lang.Class<java.lang.String> r3 = java.lang.String.class
            r2[r4] = r3
            java.lang.Class r3 = java.lang.Boolean.TYPE
            r7 = 2
            r2[r7] = r3
            java.lang.Object[] r1 = new java.lang.Object[r1]
            android.app.Activity r3 = r18.getActivity()
            r1[r5] = r3
            io.dcloud.common.DHInterface.IApp r3 = r18.obtainApp()
            java.lang.String r3 = r3.obtainAppId()
            r1[r4] = r3
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            r1[r7] = r0
            r3 = 0
            io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r6, r13, r3, r2, r1)
            r1 = r3
            goto L_0x054a
        L_0x02ea:
            r3 = 0
            java.lang.Class[] r0 = new java.lang.Class[r4]
            java.lang.Class<android.app.Activity> r1 = android.app.Activity.class
            r0[r5] = r1
            java.lang.Object[] r1 = new java.lang.Object[r4]
            android.app.Activity r2 = r18.getActivity()
            r1[r5] = r2
            java.lang.Object r0 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r6, r14, r3, r0, r1)
            boolean r1 = r0 instanceof java.lang.Integer
            if (r1 == 0) goto L_0x030e
            java.lang.String r0 = r0.toString()
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            int r0 = r0.intValue()
            goto L_0x030f
        L_0x030e:
            r0 = 0
        L_0x030f:
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r5)
            return r0
        L_0x0318:
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.nativeui.c> r0 = r10.a
            if (r0 == 0) goto L_0x0549
            java.util.Collection r0 = r0.values()
            java.util.Iterator r0 = r0.iterator()
        L_0x0324:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0334
            java.lang.Object r1 = r0.next()
            io.dcloud.feature.ui.nativeui.c r1 = (io.dcloud.feature.ui.nativeui.c) r1
            r1.a()
            goto L_0x0324
        L_0x0334:
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.nativeui.c> r0 = r10.a
            r0.clear()
            goto L_0x0549
        L_0x033b:
            r0 = 6
            java.lang.Class[] r1 = new java.lang.Class[r0]
            java.lang.Class<android.app.Activity> r0 = android.app.Activity.class
            r1[r5] = r0
            java.lang.Class<java.lang.String> r0 = java.lang.String.class
            r1[r4] = r0
            java.lang.Class<java.lang.String> r0 = java.lang.String.class
            r2 = 2
            r1[r2] = r0
            java.lang.Class<java.lang.String> r0 = java.lang.String.class
            r2 = 3
            r1[r2] = r0
            java.lang.Class<io.dcloud.common.DHInterface.IWebview> r0 = io.dcloud.common.DHInterface.IWebview.class
            r2 = 4
            r1[r2] = r0
            java.lang.Class<java.lang.String> r0 = java.lang.String.class
            r1[r16] = r0
            r0 = 6
            java.lang.Object[] r0 = new java.lang.Object[r0]
            android.app.Activity r2 = r18.getActivity()
            r0[r5] = r2
            io.dcloud.common.DHInterface.IApp r2 = r18.obtainApp()
            java.lang.String r2 = r2.obtainAppId()
            r0[r4] = r2
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)
            r7 = 2
            r0[r7] = r2
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r4)
            r9 = 3
            r0[r9] = r2
            r2 = 4
            r0[r2] = r8
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r7)
            r0[r16] = r2
            r2 = 0
            io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r6, r15, r2, r1, r0)
            android.app.Activity r0 = r18.getActivity()
            if (r0 == 0) goto L_0x0549
            android.app.Activity r0 = r18.getActivity()
            io.dcloud.common.DHInterface.IActivityHandler r0 = io.src.dcloud.adapter.DCloudAdapterUtil.getIActivityHandler(r0)
            if (r0 == 0) goto L_0x0549
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r4)
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r7)
            r0.sideBarShowMenu(r1, r2, r8, r3)
            goto L_0x0549
        L_0x03a8:
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r3, (int) r4)
            r10.b(r8, r0, r1)
            goto L_0x0549
        L_0x03b5:
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r3, (int) r4)
            r10.a(r8, r0, r1)
            goto L_0x0549
        L_0x03c2:
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r4)
            r1 = 2
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r1)
            r1 = 3
            java.lang.String r7 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r1)
            r1 = 4
            org.json.JSONArray r1 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONArray) r3, (int) r1)
            if (r1 == 0) goto L_0x03ee
            int r2 = r1.length()
            java.lang.String[] r3 = new java.lang.String[r2]
        L_0x03e1:
            if (r5 >= r2) goto L_0x03ec
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r1, (int) r5)
            r3[r5] = r6
            int r5 = r5 + 1
            goto L_0x03e1
        L_0x03ec:
            r6 = r3
            goto L_0x03ef
        L_0x03ee:
            r6 = 0
        L_0x03ef:
            r2 = 2
            r5 = 0
            r1 = r17
            r3 = r4
            r4 = r5
            r5 = r0
            r8 = r18
            r1.a(r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x0549
        L_0x03fd:
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.nativeui.c> r0 = r10.a
            if (r0 == 0) goto L_0x0549
            java.lang.Object r0 = r0.get(r9)
            io.dcloud.feature.ui.nativeui.c r0 = (io.dcloud.feature.ui.nativeui.c) r0
            if (r0 == 0) goto L_0x0549
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r3, (int) r5)
            r0.b((java.lang.String) r1)
            goto L_0x0549
        L_0x0412:
            org.json.JSONArray r1 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONArray) r3, (int) r5)
            org.json.JSONObject r6 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r3, (int) r4)
            if (r1 == 0) goto L_0x0549
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            r12 = 0
        L_0x0427:
            int r0 = r1.length()
            if (r12 >= r0) goto L_0x0459
            java.lang.String r0 = r1.getString(r12)     // Catch:{ JSONException -> 0x0452 }
            boolean r13 = io.dcloud.common.util.PdrUtil.isNetPath(r0)     // Catch:{ JSONException -> 0x0452 }
            if (r13 != 0) goto L_0x044a
            boolean r13 = io.dcloud.common.util.PdrUtil.isBase64ImagePath(r0)     // Catch:{ JSONException -> 0x0452 }
            if (r13 != 0) goto L_0x044a
            io.dcloud.common.DHInterface.IApp r13 = r18.obtainApp()     // Catch:{ JSONException -> 0x0452 }
            java.lang.String r14 = r18.obtainFullUrl()     // Catch:{ JSONException -> 0x0452 }
            java.lang.String r13 = r13.convert2AbsFullPath(r14, r0)     // Catch:{ JSONException -> 0x0452 }
            goto L_0x044b
        L_0x044a:
            r13 = r0
        L_0x044b:
            r9.add(r13)     // Catch:{ JSONException -> 0x0452 }
            r11.add(r0)     // Catch:{ JSONException -> 0x0452 }
            goto L_0x0456
        L_0x0452:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0456:
            int r12 = r12 + 1
            goto L_0x0427
        L_0x0459:
            android.content.Intent r0 = new android.content.Intent
            r0.<init>()
            android.app.Activity r1 = r2.getActivity()
            java.lang.String r1 = r1.getPackageName()
            r0.setClassName(r1, r7)
            java.lang.String r1 = "image_urlList"
            r0.putExtra(r1, r9)
            java.lang.String r1 = "original_image_urlArray"
            r0.putExtra(r1, r11)
            if (r6 == 0) goto L_0x04b7
            java.lang.String r1 = "current"
            int r1 = r6.optInt(r1, r5)
            java.lang.String r5 = "image_current_index"
            r0.putExtra(r5, r1)
            java.lang.String r1 = "background"
            boolean r5 = r6.has(r1)
            if (r5 == 0) goto L_0x0495
            java.lang.String r1 = r6.optString(r1)
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r1)
            java.lang.String r5 = "image_backgroud_color"
            r0.putExtra(r5, r1)
        L_0x0495:
            java.lang.String r1 = "loop"
            boolean r5 = r6.has(r1)
            if (r5 == 0) goto L_0x04a6
            boolean r1 = r6.optBoolean(r1)
            java.lang.String r5 = "image_loop"
            r0.putExtra(r5, r1)
        L_0x04a6:
            java.lang.String r1 = "indicator"
            boolean r5 = r6.has(r1)
            if (r5 == 0) goto L_0x04b7
            java.lang.String r1 = r6.optString(r1)
            java.lang.String r5 = "image_indicator"
            r0.putExtra(r5, r1)
        L_0x04b7:
            java.lang.String r1 = "image_photo"
            r0.putExtra(r1, r4)
            if (r3 == 0) goto L_0x04dc
            int r1 = r3.length()
            r4 = 2
            if (r1 <= r4) goto L_0x04dc
            java.lang.String r1 = r3.optString(r4)
            if (r1 == 0) goto L_0x04dc
            java.lang.String r3 = "preview_callback"
            r0.putExtra(r3, r1)
            io.dcloud.feature.nativeObj.photoview.LongClickEventManager r3 = io.dcloud.feature.nativeObj.photoview.LongClickEventManager.getInstance()
            io.dcloud.feature.ui.nativeui.NativeUIFeatureImpl$c r4 = new io.dcloud.feature.ui.nativeui.NativeUIFeatureImpl$c
            r4.<init>(r8, r1)
            r3.addOnlongClickListener(r1, r4)
        L_0x04dc:
            int r1 = r2.getRequestedOrientation()
            java.lang.String r3 = "screen_orientation"
            r0.putExtra(r3, r1)
            android.app.Activity r1 = r2.getActivity()
            r1.startActivity(r0)
            android.app.Activity r0 = r2.getActivity()
            r1 = 17432576(0x10a0000, float:2.5346597E-38)
            r2 = 17432577(0x10a0001, float:2.53466E-38)
            r0.overridePendingTransition(r1, r2)
            goto L_0x0549
        L_0x04f9:
            android.app.Activity r0 = io.dcloud.common.util.RuningAcitvityUtil.getActivity(r7)
            if (r0 == 0) goto L_0x0549
            r0.onBackPressed()
            goto L_0x0549
        L_0x0503:
            r0 = r1[r5]
            android.app.Activity r1 = r18.getActivity()
            io.dcloud.common.util.AppRuntime.setAppDarkMode(r1, r8, r0)
            goto L_0x0549
        L_0x050d:
            r1 = 2
            java.lang.Class[] r0 = new java.lang.Class[r1]
            java.lang.Class<android.app.Activity> r2 = android.app.Activity.class
            r0[r5] = r2
            java.lang.Class<java.lang.String> r2 = java.lang.String.class
            r0[r4] = r2
            java.lang.Object[] r1 = new java.lang.Object[r1]
            android.app.Activity r2 = r18.getActivity()
            r1[r5] = r2
            io.dcloud.common.DHInterface.IApp r2 = r18.obtainApp()
            java.lang.String r2 = r2.obtainAppId()
            r1[r4] = r2
            r2 = 0
            io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r6, r11, r2, r0, r1)
            android.app.Activity r0 = r18.getActivity()
            if (r0 == 0) goto L_0x0549
            android.app.Activity r0 = r18.getActivity()
            io.dcloud.common.DHInterface.IActivityHandler r0 = io.src.dcloud.adapter.DCloudAdapterUtil.getIActivityHandler(r0)
            if (r0 == 0) goto L_0x0549
            r0.sideBarHideMenu()
            goto L_0x0549
        L_0x0542:
            java.lang.String r0 = r2.obtainAppId()
            io.dcloud.feature.ui.nativeui.b.a((java.lang.String) r0)
        L_0x0549:
            r1 = 0
        L_0x054a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.nativeui.NativeUIFeatureImpl.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public void init(AbsMgr absMgr, String str) {
        this.m = absMgr;
    }

    /* access modifiers changed from: private */
    public void b() {
        this.g = null;
        this.h = 0;
        this.i = 0;
    }

    /* access modifiers changed from: package-private */
    public void a(String str) {
        this.a.remove(str);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v6, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ee A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00ef  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(io.dcloud.common.DHInterface.IApp r21, io.dcloud.common.DHInterface.IWebview r22, java.lang.String r23, org.json.JSONObject r24) {
        /*
            r20 = this;
            r0 = r23
            r1 = r24
            float r2 = r22.getScale()
            java.lang.String r3 = "inline"
            r4 = 0
            r5 = 80
            java.lang.String r7 = "block"
            r9 = 0
            if (r1 == 0) goto L_0x00d8
            java.lang.String r10 = "type"
            java.lang.String r10 = r1.optString(r10)
            java.lang.String r11 = "richtext"
            boolean r10 = android.text.TextUtils.equals(r11, r10)
            java.lang.String r11 = "style"
            boolean r12 = r1.isNull(r11)
            if (r12 != 0) goto L_0x0038
            java.lang.String r11 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r1, (java.lang.String) r11)
            java.util.Locale r12 = java.util.Locale.ENGLISH
            java.lang.String r11 = r11.toLowerCase(r12)
            boolean r11 = r7.equals(r11)
            if (r11 == 0) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r7 = r3
        L_0x0038:
            java.lang.String r11 = "icon"
            boolean r12 = r1.isNull(r11)
            if (r12 != 0) goto L_0x005e
            java.lang.String r11 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r1, (java.lang.String) r11)
            java.lang.String r12 = r22.obtainFullUrl()
            r13 = r21
            java.lang.String r11 = r13.convert2AbsFullPath(r12, r11)
            android.graphics.BitmapFactory$Options r12 = new android.graphics.BitmapFactory$Options
            r12.<init>()
            r12.inScaled = r9
            java.io.InputStream r11 = io.dcloud.common.adapter.util.PlatformUtil.getInputStream(r11)
            android.graphics.Bitmap r4 = android.graphics.BitmapFactory.decodeStream(r11, r4, r12)
            goto L_0x0060
        L_0x005e:
            r13 = r21
        L_0x0060:
            java.lang.String r11 = "duration"
            boolean r12 = r1.isNull(r11)
            if (r12 != 0) goto L_0x007c
            java.lang.String r11 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r1, (java.lang.String) r11)
            java.util.Locale r12 = java.util.Locale.ENGLISH
            java.lang.String r11 = r11.toLowerCase(r12)
            java.lang.String r12 = "long"
            boolean r11 = r12.equals(r11)
            if (r11 == 0) goto L_0x007c
            r11 = 1
            goto L_0x007d
        L_0x007c:
            r11 = 0
        L_0x007d:
            java.lang.String r12 = "align"
            boolean r14 = r1.isNull(r12)
            if (r14 != 0) goto L_0x009d
            java.lang.String r12 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r1, (java.lang.String) r12)
            java.lang.String r14 = "left"
            boolean r14 = r14.equals(r12)
            if (r14 == 0) goto L_0x0093
            r12 = 3
            goto L_0x009e
        L_0x0093:
            java.lang.String r14 = "right"
            boolean r12 = r14.equals(r12)
            if (r12 == 0) goto L_0x009d
            r12 = 5
            goto L_0x009e
        L_0x009d:
            r12 = 1
        L_0x009e:
            java.lang.String r14 = "verticalAlign"
            boolean r15 = r1.isNull(r14)
            if (r15 != 0) goto L_0x00c1
            java.lang.String r14 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r1, (java.lang.String) r14)
            java.lang.String r15 = "top"
            boolean r15 = r15.equals(r14)
            if (r15 == 0) goto L_0x00b6
            r5 = 48
            goto L_0x00c1
        L_0x00b6:
            java.lang.String r15 = "bottom"
            boolean r14 = r15.equals(r14)
            if (r14 == 0) goto L_0x00bf
            goto L_0x00c1
        L_0x00bf:
            r5 = 16
        L_0x00c1:
            java.lang.String r14 = "background"
            boolean r15 = r1.isNull(r14)
            if (r15 != 0) goto L_0x00dd
            java.lang.String r14 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r1, (java.lang.String) r14)
            boolean r15 = io.dcloud.common.util.PdrUtil.isEmpty(r14)
            if (r15 != 0) goto L_0x00dd
            int r14 = io.dcloud.common.util.PdrUtil.stringToColor(r14)
            goto L_0x00de
        L_0x00d8:
            r13 = r21
            r10 = 0
            r11 = 0
            r12 = 1
        L_0x00dd:
            r14 = 1
        L_0x00de:
            android.app.Activity r15 = r22.getActivity()
            android.app.Activity r15 = io.dcloud.common.util.RuningAcitvityUtil.getTopRuningActivity(r15)
            if (r15 == 0) goto L_0x00ef
            boolean r16 = r15.isFinishing()
            if (r16 == 0) goto L_0x00ef
            return
        L_0x00ef:
            if (r4 != 0) goto L_0x011c
            if (r10 == 0) goto L_0x00f4
            goto L_0x011c
        L_0x00f4:
            android.content.Context r1 = r15.getApplicationContext()
            com.dcloud.android.widget.toast.ToastCompat r0 = com.dcloud.android.widget.toast.ToastCompat.makeText((android.content.Context) r1, (java.lang.CharSequence) r0, (int) r11)
            r1 = r12 | r5
            int r2 = r0.getXOffset()
            int r3 = r0.getYOffset()
            r0.setGravity(r1, r2, r3)
            if (r14 > 0) goto L_0x0118
            android.view.View r1 = r0.getView()
            android.graphics.drawable.Drawable r1 = r1.getBackground()
            android.graphics.PorterDuff$Mode r2 = android.graphics.PorterDuff.Mode.SRC
            r1.setColorFilter(r14, r2)
        L_0x0118:
            r3 = r20
            goto L_0x02aa
        L_0x011c:
            if (r10 == 0) goto L_0x0128
            io.dcloud.feature.ui.nativeui.b r6 = new io.dcloud.feature.ui.nativeui.b
            java.lang.String r13 = r21.obtainAppId()
            r6.<init>(r15, r13)
            goto L_0x0131
        L_0x0128:
            com.dcloud.android.widget.toast.ToastCompat r6 = new com.dcloud.android.widget.toast.ToastCompat
            android.content.Context r13 = r15.getApplicationContext()
            r6.<init>(r13)
        L_0x0131:
            android.widget.LinearLayout r13 = new android.widget.LinearLayout
            r13.<init>(r15)
            r8 = 1092616192(0x41200000, float:10.0)
            int r8 = io.dcloud.common.adapter.util.CanvasHelper.dip2px(r15, r8)
            r9 = 1090519040(0x41000000, float:8.0)
            int r9 = io.dcloud.common.adapter.util.CanvasHelper.dip2px(r15, r9)
            r13.setPadding(r8, r9, r8, r9)
            r8 = 17
            r13.setGravity(r8)
            java.lang.String r8 = android.os.Build.BRAND
            r17 = r11
            java.lang.String r11 = "xiaomi"
            boolean r8 = r8.equalsIgnoreCase(r11)
            if (r8 == 0) goto L_0x019a
            r8 = 17301654(0x1080096, float:2.4979675E-38)
            if (r14 > 0) goto L_0x0171
            android.content.Context r9 = r22.getContext()
            android.content.res.Resources r9 = r9.getResources()
            android.graphics.drawable.Drawable r8 = r9.getDrawable(r8)
            android.graphics.PorterDuff$Mode r9 = android.graphics.PorterDuff.Mode.SRC
            r8.setColorFilter(r14, r9)
            r13.setBackground(r8)
            goto L_0x01b5
        L_0x0171:
            java.lang.String r9 = io.dcloud.common.constant.DataInterface.getSystemProperty()
            java.lang.String r11 = "v11"
            java.lang.String r14 = "v12"
            java.lang.String[] r11 = new java.lang.String[]{r11, r14}
            boolean r14 = io.dcloud.common.util.PdrUtil.isEmpty(r9)
            if (r14 != 0) goto L_0x0196
            java.util.Locale r14 = java.util.Locale.ENGLISH
            java.lang.String r9 = r9.toLowerCase(r14)
            int r9 = java.util.Arrays.binarySearch(r11, r9)
            r11 = -1
            if (r9 == r11) goto L_0x0196
            int r8 = io.dcloud.base.R.drawable.toast_bg
            r13.setBackgroundResource(r8)
            goto L_0x01b5
        L_0x0196:
            r13.setBackgroundResource(r8)
            goto L_0x01b5
        L_0x019a:
            android.graphics.drawable.GradientDrawable r8 = new android.graphics.drawable.GradientDrawable
            r8.<init>()
            float r9 = (float) r9
            r8.setCornerRadius(r9)
            r9 = 0
            r8.setShape(r9)
            if (r14 > 0) goto L_0x01ad
            r8.setColor(r14)
            goto L_0x01b2
        L_0x01ad:
            r9 = -1308622848(0xffffffffb2000000, float:-7.4505806E-9)
            r8.setColor(r9)
        L_0x01b2:
            r13.setBackground(r8)
        L_0x01b5:
            r8 = -2
            if (r4 == 0) goto L_0x022b
            android.widget.ImageView r9 = new android.widget.ImageView
            r9.<init>(r15)
            android.widget.LinearLayout$LayoutParams r11 = new android.widget.LinearLayout$LayoutParams
            r11.<init>(r8, r8)
            r14 = 17
            r11.gravity = r14
            java.lang.String r14 = "iconWidth"
            java.lang.String r14 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r1, (java.lang.String) r14)
            java.lang.String r8 = "iconHeight"
            java.lang.String r8 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r1, (java.lang.String) r8)
            boolean r18 = android.text.TextUtils.isEmpty(r14)
            r19 = r5
            java.lang.String r5 = "px"
            if (r18 != 0) goto L_0x01ec
            boolean r18 = r14.endsWith(r5)
            if (r18 == 0) goto L_0x01ec
            r18 = r12
            r12 = 0
            int r14 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r14, r12, r12, r2)
            r11.width = r14
            goto L_0x01ef
        L_0x01ec:
            r18 = r12
            r12 = 0
        L_0x01ef:
            boolean r14 = android.text.TextUtils.isEmpty(r8)
            if (r14 != 0) goto L_0x0201
            boolean r5 = r8.endsWith(r5)
            if (r5 == 0) goto L_0x0201
            int r2 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r8, r12, r12, r2)
            r11.height = r2
        L_0x0201:
            r2 = 1088421888(0x40e00000, float:7.0)
            int r2 = io.dcloud.common.adapter.util.CanvasHelper.dip2px(r15, r2)
            boolean r3 = r7.equals(r3)
            if (r3 == 0) goto L_0x0214
            r13.setOrientation(r12)
            r11.setMargins(r2, r2, r2, r2)
            goto L_0x0221
        L_0x0214:
            r3 = 1065353216(0x3f800000, float:1.0)
            int r3 = io.dcloud.common.adapter.util.CanvasHelper.dip2px(r15, r3)
            r5 = 1
            r13.setOrientation(r5)
            r11.setMargins(r2, r2, r2, r3)
        L_0x0221:
            r9.setLayoutParams(r11)
            r9.setImageBitmap(r4)
            r13.addView(r9)
            goto L_0x022f
        L_0x022b:
            r19 = r5
            r18 = r12
        L_0x022f:
            android.widget.LinearLayout$LayoutParams r2 = new android.widget.LinearLayout$LayoutParams
            r3 = -2
            r2.<init>(r3, r3)
            r3 = 17
            r2.gravity = r3
            if (r10 == 0) goto L_0x0271
            r3 = r20
            io.dcloud.common.DHInterface.AbsMgr r4 = r3.m
            io.dcloud.common.DHInterface.IMgr$MgrType r5 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r7 = 4
            java.lang.Object[] r8 = new java.lang.Object[r7]
            r9 = 0
            r8[r9] = r22
            java.lang.String r11 = "nativeobj"
            r12 = 1
            r8[r12] = r11
            java.lang.String r11 = "makeRichText"
            r14 = 2
            r8[r14] = r11
            java.lang.Object[] r7 = new java.lang.Object[r7]
            io.dcloud.common.DHInterface.IFrameView r11 = r22.obtainFrameView()
            r7[r9] = r11
            r7[r12] = r0
            java.lang.String r0 = "richTextStyle"
            org.json.JSONObject r0 = r1.optJSONObject(r0)
            r7[r14] = r0
            r0 = 3
            r7[r0] = r6
            r8[r0] = r7
            r0 = 10
            java.lang.Object r0 = r4.processEvent(r5, r0, r8)
            android.widget.TextView r0 = (android.widget.TextView) r0
            goto L_0x027c
        L_0x0271:
            r3 = r20
            android.widget.TextView r1 = new android.widget.TextView
            r1.<init>(r15)
            r1.setText(r0)
            r0 = r1
        L_0x027c:
            java.lang.String r1 = "#ffffffff"
            int r1 = android.graphics.Color.parseColor(r1)
            r0.setTextColor(r1)
            r0.setLayoutParams(r2)
            r13.addView(r0)
            if (r10 == 0) goto L_0x0294
            r1 = r6
            io.dcloud.feature.ui.nativeui.b r1 = (io.dcloud.feature.ui.nativeui.b) r1
            r1.a(r13, r0)
            goto L_0x0297
        L_0x0294:
            r6.setView(r13)
        L_0x0297:
            r0 = r18 | r19
            int r1 = r6.getXOffset()
            int r2 = r6.getYOffset()
            r6.setGravity(r0, r1, r2)
            r11 = r17
            r6.setDuration(r11)
            r0 = r6
        L_0x02aa:
            r0.show()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.nativeui.NativeUIFeatureImpl.a(io.dcloud.common.DHInterface.IApp, io.dcloud.common.DHInterface.IWebview, java.lang.String, org.json.JSONObject):void");
    }

    private void b(IWebview iWebview, String str, JSONObject jSONObject) {
        int i2;
        int i3;
        JSONObject jSONObject2 = jSONObject;
        if (this.g != null) {
            Deprecated_JSUtil.execCallback(iWebview, str, DOMException.toJSON(5, ""), JSUtil.ERROR, true, false);
            return;
        }
        Activity topRuningActivity = RuningAcitvityUtil.getTopRuningActivity(iWebview.getActivity());
        boolean z = jSONObject2 != null && !jSONObject2.isNull(AbsoluteConst.JSON_KEY_TITLE);
        boolean parseBoolean = PdrUtil.parseBoolean(JSONUtil.getString(jSONObject2, AbsoluteConst.JSON_KEY_IS24HOUR), true, false);
        String string = JSONUtil.getString(jSONObject2, Constants.Value.TIME);
        String string2 = JSONUtil.getString(jSONObject2, "__minutes");
        String string3 = JSONUtil.getString(jSONObject2, "__hours");
        if (PdrUtil.isEmpty(string3)) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            PdrUtil.isEmpty(string);
            if (parseBoolean) {
                i3 = gregorianCalendar.get(11);
            } else {
                i3 = gregorianCalendar.get(10);
            }
            i2 = gregorianCalendar.get(12);
        } else {
            i3 = Integer.parseInt(string3);
            i2 = Integer.parseInt(string2);
        }
        int i4 = i3;
        int i5 = i2;
        h hVar = new h();
        i iVar = r0;
        i iVar2 = new i(this, topRuningActivity, AppRuntime.getAppDarkMode(iWebview.getContext()) ? 4 : 0, hVar, i4, i5, parseBoolean, i4, i5, z, iWebview, str);
        if (z) {
            iVar.setTitle(JSONUtil.getString(jSONObject2, AbsoluteConst.JSON_KEY_TITLE));
        }
        i iVar3 = iVar;
        iVar3.setOnDismissListener(new j(iWebview, str));
        iVar3.show();
        this.g = iVar3;
    }

    private void a(IWebview iWebview, String str, JSONObject jSONObject) {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        JSONObject jSONObject2 = jSONObject;
        if (this.c != null) {
            Deprecated_JSUtil.execCallback(iWebview, str, DOMException.toJSON(5, ""), JSUtil.ERROR, true, false);
            return;
        }
        Activity topRuningActivity = RuningAcitvityUtil.getTopRuningActivity(iWebview.getActivity());
        int i7 = JSONUtil.getInt(jSONObject2, AbsoluteConst.JSON_KEY_STARTYEAR);
        int i8 = JSONUtil.getInt(jSONObject2, "startMonth");
        int i9 = JSONUtil.getInt(jSONObject2, "startDay");
        int i10 = JSONUtil.getInt(jSONObject2, AbsoluteConst.JSON_KEY_ENDYEAR);
        int i11 = JSONUtil.getInt(jSONObject2, "endMonth");
        int i12 = JSONUtil.getInt(jSONObject2, "endDay");
        int i13 = JSONUtil.getInt(jSONObject2, "setYear");
        int i14 = JSONUtil.getInt(jSONObject2, "setMonth");
        int i15 = JSONUtil.getInt(jSONObject2, "setDay");
        if (i13 == 0) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(new Date());
            int i16 = gregorianCalendar.get(1);
            int i17 = gregorianCalendar.get(2);
            i2 = gregorianCalendar.get(5);
            i4 = i16;
            i3 = i17;
        } else {
            i4 = i13;
            i3 = i14;
            i2 = i15;
        }
        boolean z = jSONObject2 != null && !jSONObject2.isNull(AbsoluteConst.JSON_KEY_TITLE);
        GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
        e eVar = new e();
        int i18 = AppRuntime.getAppDarkMode(iWebview.getContext()) ? 4 : 0;
        f fVar = r0;
        GregorianCalendar gregorianCalendar3 = gregorianCalendar2;
        String str2 = AbsoluteConst.JSON_KEY_TITLE;
        int i19 = i12;
        int i20 = i11;
        int i21 = i10;
        int i22 = i9;
        int i23 = i8;
        int i24 = i7;
        f fVar2 = new f(this, topRuningActivity, i18, eVar, i4, i3, i2, i4, i3, i2, z, iWebview, str);
        if (DeviceInfo.sDeviceSdkVer >= 11) {
            if (i24 > 1900) {
                gregorianCalendar3.set(1, i24);
                i6 = 2;
                gregorianCalendar3.set(2, i23);
                i5 = 5;
                gregorianCalendar3.set(5, i22);
                fVar.getDatePicker().setMinDate(gregorianCalendar3.getTimeInMillis());
            } else {
                i6 = 2;
                i5 = 5;
            }
            int i25 = i21;
            if (i25 > 1900 && i25 >= i24) {
                gregorianCalendar3.set(1, i25);
                gregorianCalendar3.set(i6, i20);
                gregorianCalendar3.set(i5, i19);
                fVar.getDatePicker().setMaxDate(gregorianCalendar3.getTimeInMillis());
            }
        }
        f fVar3 = fVar;
        fVar3.setOnDismissListener(new g(iWebview, str));
        if (z) {
            fVar3.setTitle(JSONUtil.getString(jSONObject, str2));
        }
        fVar3.show();
        this.c = fVar3;
    }

    /* access modifiers changed from: private */
    public void a() {
        this.c = null;
        this.d = 0;
        this.e = 0;
        this.f = 0;
    }

    private a a(String str, JSONObject jSONObject, String str2, IWebview iWebview) {
        a aVar;
        String optString = jSONObject.has(AbsoluteConst.JSON_KEY_TITLE) ? jSONObject.optString(AbsoluteConst.JSON_KEY_TITLE) : null;
        String optString2 = jSONObject.has(BindingXConstants.STATE_CANCEL) ? jSONObject.optString(BindingXConstants.STATE_CANCEL) : null;
        JSONArray optJSONArray = jSONObject.optJSONArray("buttons");
        if (optJSONArray == null || a(optJSONArray, iWebview.obtainApp(), str2, iWebview)) {
            return null;
        }
        d dVar = new d(iWebview, str2, str);
        Activity topRuningActivity = RuningAcitvityUtil.getTopRuningActivity(iWebview.getActivity());
        topRuningActivity.setTheme(NativeUIR.ACTS_STYLE_ActionSheetStyleIOS7);
        int i2 = 16973837;
        if (iWebview.obtainApp().isFullScreen()) {
            i2 = 16973838;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            aVar = new a(topRuningActivity);
        } else {
            aVar = new a(topRuningActivity, i2);
        }
        aVar.b(optString2);
        aVar.a(optString);
        aVar.a(optJSONArray);
        aVar.a((a.b) dVar);
        aVar.a(true);
        aVar.j();
        return aVar;
    }

    private void a(byte b2, String str, JSONObject jSONObject, IWebview iWebview, String str2) {
        String[] strArr;
        JSONObject jSONObject2 = jSONObject;
        String optString = jSONObject2.optString(AbsoluteConst.JSON_KEY_VERTICAL_ALIGN, "center");
        JSONArray optJSONArray = jSONObject2.optJSONArray("buttons");
        if (optJSONArray != null) {
            int length = optJSONArray.length();
            strArr = new String[length];
            for (int i2 = 0; i2 < length; i2++) {
                strArr[i2] = JSONUtil.getString(optJSONArray, i2);
            }
        } else {
            strArr = null;
        }
        String[] strArr2 = strArr;
        if (TextUtils.equals(optString, "center")) {
            a(b2, jSONObject2.optString(AbsoluteConst.JSON_KEY_TITLE), (String) null, str, strArr2, (String) null, iWebview, str2, 17);
        } else if (TextUtils.equals(optString, "top")) {
            a(b2, jSONObject2.optString(AbsoluteConst.JSON_KEY_TITLE), (String) null, str, strArr2, (String) null, iWebview, str2, 49);
        } else if (TextUtils.equals(optString, "bottom")) {
            a(b2, jSONObject2.optString(AbsoluteConst.JSON_KEY_TITLE), (String) null, str, strArr2, (String) null, iWebview, str2, 81);
        }
    }

    private void a(byte b2, String str, String str2, String str3, String[] strArr, String str4, IWebview iWebview, String str5) {
        a(b2, str, str2, str3, strArr, str4, iWebview, str5, 17);
    }

    private void a(byte b2, String str, String str2, String str3, String[] strArr, String str4, IWebview iWebview, String str5, int i2) {
        String[] strArr2;
        byte b3 = b2;
        String str6 = str;
        String str7 = str3;
        String str8 = str4;
        IWebview iWebview2 = iWebview;
        String str9 = str5;
        Activity topRuningActivity = RuningAcitvityUtil.getTopRuningActivity(iWebview.getActivity());
        int i3 = 0;
        if (b3 != 0) {
            DCloudAlertDialog initDialogTheme = DialogUtil.initDialogTheme(topRuningActivity, true);
            initDialogTheme.setMessage(str7);
            EditText editText = null;
            if (b3 == 2) {
                editText = new EditText(topRuningActivity);
                if (str8 != null) {
                    editText.setHint(str8);
                }
                initDialogTheme.setView(editText);
                Editable text = editText.getText();
                if (text instanceof Spannable) {
                    Selection.setSelection(text, text.length());
                }
            }
            EditText editText2 = editText;
            if (b3 == 1) {
                Window window = initDialogTheme.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.gravity = i2;
                window.setAttributes(attributes);
            }
            if (!PdrUtil.isEmpty(str)) {
                initDialogTheme.setTitle(str6);
            }
            initDialogTheme.setCanceledOnTouchOutside(false);
            initDialogTheme.setMessage(str7);
            if (strArr == null) {
                strArr2 = new String[]{AndroidResources.getString(17039370), AndroidResources.getString(17039360)};
            } else {
                strArr2 = strArr;
            }
            String[] strArr3 = strArr2;
            if (!a(b2, str3, strArr2, iWebview.obtainApp(), iWebview, str5)) {
                while (i3 < strArr3.length && i3 < 3) {
                    a aVar = r0;
                    int i4 = i3;
                    a aVar2 = new a(b2, editText2, iWebview, str5, i3, initDialogTheme);
                    if (i4 == 0) {
                        initDialogTheme.setButton(-1, strArr3[i4], aVar);
                    } else if (i4 == 1) {
                        initDialogTheme.setButton(-2, strArr3[i4], aVar);
                    } else if (i4 == 2) {
                        initDialogTheme.setButton(-3, strArr3[i4], aVar);
                    }
                    i3 = i4 + 1;
                }
                initDialogTheme.setOnKeyListener(new b(b2, editText2, iWebview, str5, initDialogTheme));
                initDialogTheme.show();
                if (b3 == 2 && !Build.FINGERPRINT.toLowerCase(Locale.ENGLISH).contains("flyme")) {
                    DeviceInfo.showIME(editText2);
                }
            }
        } else if (!a(str7, iWebview.obtainApp(), iWebview2, str9)) {
            DCloudAlertDialog initDialogTheme2 = DialogUtil.initDialogTheme(topRuningActivity, true);
            if (strArr != null && PdrUtil.isEmpty(strArr[0])) {
                strArr[0] = AndroidResources.getString(17039370);
            }
            if (!PdrUtil.isEmpty(str)) {
                initDialogTheme2.setTitle(str6);
            }
            initDialogTheme2.setCanceledOnTouchOutside(false);
            initDialogTheme2.setMessage(str7);
            initDialogTheme2.setButton(-1, strArr[0], new k(initDialogTheme2, iWebview2, str9));
            initDialogTheme2.setOnKeyListener(new l(initDialogTheme2, iWebview2, str9));
            initDialogTheme2.show();
        }
    }

    private boolean a(String str, IApp iApp, IWebview iWebview, String str2) {
        if (TextUtils.isEmpty(str) || !BaseInfo.ISAMU) {
            return false;
        }
        if (!str.contains("更新") && !str.contains("升级") && !str.contains("版本")) {
            return false;
        }
        try {
            Deprecated_JSUtil.execCallback(iWebview, str2, "{index:0}", JSUtil.OK, true, false);
            a(iApp);
            return true;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    private boolean a(byte b2, String str, String[] strArr, IApp iApp, IWebview iWebview, String str2) {
        byte b3 = b2;
        String[] strArr2 = strArr;
        IApp iApp2 = iApp;
        if (!TextUtils.isEmpty(str) && strArr2 != null && BaseInfo.ISAMU) {
            String str3 = str;
            int i2 = 0;
            while (i2 < strArr2.length && i2 < 3) {
                String str4 = strArr2[i2];
                if (str4.contains("更新") || str4.contains("升级") || ((str3.contains("更新") || str3.contains("升级") || str3.contains("版本")) && (str4.equals(WXModalUIModule.OK) || str4.equals("是") || str4.equals("确定")))) {
                    if (b3 == 2) {
                        str3 = "";
                        try {
                            String jSONableString = JSONUtil.toJSONableString(str3);
                            Deprecated_JSUtil.execCallback(iWebview, str2, "{index:" + i2 + ",message:" + jSONableString + Operators.BLOCK_END_STR, JSUtil.OK, true, false);
                            a(iApp2);
                            return true;
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    } else if (b3 == 1) {
                        Deprecated_JSUtil.execCallback(iWebview, str2, String.valueOf(i2), JSUtil.OK, true, false);
                        a(iApp2);
                        return true;
                    }
                }
                i2++;
            }
        }
        return false;
    }

    private boolean a(JSONArray jSONArray, IApp iApp, String str, IWebview iWebview) {
        if (jSONArray != null && BaseInfo.ISAMU) {
            int i2 = 0;
            while (i2 < jSONArray.length()) {
                try {
                    String string = jSONArray.getJSONObject(i2).getString(AbsoluteConst.JSON_KEY_TITLE);
                    if (string.contains("更新") || string.contains("升级") || string.contains("版本")) {
                        Deprecated_JSUtil.execCallback(iWebview, str, "" + (i2 + 1), JSUtil.OK, true, false);
                        a(iApp);
                        return true;
                    }
                    i2++;
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return false;
    }

    private void a(IApp iApp) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("type", "notify");
        jSONObject.put("appid", iApp.obtainOriginalAppId());
        jSONObject.put("version", iApp.obtainAppVersionName());
        Log.i(AbsoluteConst.HBUILDER_TAG, jSONObject.toString());
    }
}
