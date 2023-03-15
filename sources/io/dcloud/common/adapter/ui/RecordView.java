package io.dcloud.common.adapter.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.PdrR;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.FeatureMessageDispatcher;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.AssistInputUtil;
import io.dcloud.common.util.Base64;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.PdrUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.json.JSONArray;

public class RecordView implements View.OnClickListener, Handler.Callback {
    private static final int ASSIST_ARROW_LONG_CLICK_INTERVAL_TIMES = 100;
    private static final int ASSIST_ARROW_LONG_CLICK_TIMER_DELAYED = 500;
    private static final int HANDLER_WHAT_LONG_CLICK = 1;
    private static final int HANDLER_WHAT_LONG_CLICK_TIMER = 0;
    public static final int TYPE_ADDRESS = 4;
    public static final int TYPE_COMPANY = 5;
    public static final int TYPE_EMAIL = 2;
    public static final int TYPE_ID = 7;
    public static final int TYPE_NICK = 3;
    public static final int TYPE_PHONE = 1;
    public static final int TYPE_TAX = 6;
    public static final int TYPE_UNKNOW = -1;
    private static final int XORNUMBER = 5;
    int Height;
    private boolean isLongClick;
    int mAnchorY;
    String mAppid;
    private Handler mHandler;
    ViewGroup mMainView;
    RecordData mRecordData;
    boolean mShowed;

    class RecordData {
        RecordItem[] mRecordItems = new RecordItem[2];
        int mRecordType;

        RecordData() {
        }

        /* access modifiers changed from: package-private */
        public void checkType(int i) {
            if (i != 4) {
                if ((i != this.mRecordType || this.mRecordItems[1] == null || hasChanged(i)) && i != -1) {
                    this.mRecordType = i;
                    this.mRecordItems = new RecordItem[2];
                    String access$300 = RecordView.getRecordDatas(DeviceInfo.sApplicationContext, (String) null, i);
                    if (!TextUtils.isEmpty(access$300)) {
                        RecordView recordView = RecordView.this;
                        recordView.log("AssistantInput", "RecordView checkType load recordType=" + i + ";value=" + access$300);
                        String[] split = access$300.split("&");
                        for (int i2 = 0; i2 < split.length; i2++) {
                            String[] split2 = split[i2].split(Operators.SUB);
                            if (!TextUtils.isEmpty(split2[0])) {
                                try {
                                    this.mRecordItems[i2] = new RecordItem(URLDecoder.decode(split2[0], "utf-8"), System.currentTimeMillis());
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean hasChanged(int i) {
            return SP.hasChanged(DeviceInfo.sApplicationContext, "assis_input", true);
        }

        /* access modifiers changed from: package-private */
        public void record(String str, int i) {
            if (i != 4 && Utils.needRecord(i)) {
                RecordItem recordItem = new RecordItem(str, System.currentTimeMillis());
                checkType(i);
                StringBuffer stringBuffer = new StringBuffer();
                if (i == 7 || i == 5 || i == 6) {
                    RecordItem[] recordItemArr = this.mRecordItems;
                    recordItemArr[0] = recordItem;
                    try {
                        stringBuffer.append(URLEncoder.encode(recordItemArr[0].mContent, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    RecordItem[] recordItemArr2 = this.mRecordItems;
                    if (recordItemArr2[0] != null && !TextUtils.equals(str, recordItemArr2[0].mContent)) {
                        RecordItem[] recordItemArr3 = this.mRecordItems;
                        recordItemArr3[1] = recordItemArr3[0];
                    }
                    RecordItem[] recordItemArr4 = this.mRecordItems;
                    recordItemArr4[0] = recordItem;
                    if (!TextUtils.isEmpty(recordItemArr4[0].mContent)) {
                        try {
                            stringBuffer.append(URLEncoder.encode(this.mRecordItems[0].mContent, "utf-8"));
                            RecordItem[] recordItemArr5 = this.mRecordItems;
                            if (recordItemArr5[1] != null && !TextUtils.isEmpty(recordItemArr5[1].mContent)) {
                                stringBuffer.append("&");
                                stringBuffer.append(URLEncoder.encode(this.mRecordItems[1].mContent, "utf-8"));
                            }
                        } catch (UnsupportedEncodingException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                RecordView.setRcordDatas(DeviceInfo.sApplicationContext, (String) null, i, stringBuffer.toString());
                RecordView recordView = RecordView.this;
                recordView.log("AssistantInput", "RecordView record recordType=" + i + ";value=" + stringBuffer.toString());
            }
        }
    }

    class RecordItem {
        String mContent;
        long mTime;

        RecordItem(String str, long j) {
            this.mContent = str;
            this.mTime = j;
        }

        public String toString() {
            return this.mContent + Operators.SUB + this.mTime;
        }
    }

    static class Utils {
        Utils() {
        }

        static int convertInt(String str) {
            if (TextUtils.equals(str, "nick")) {
                return 3;
            }
            if (TextUtils.equals(str, "address")) {
                return 4;
            }
            if (TextUtils.equals(str, Constants.Value.TEL)) {
                return 1;
            }
            if (TextUtils.equals(str, "email")) {
                return 2;
            }
            if (TextUtils.equals(str, "none")) {
                return -1;
            }
            if ("company".equals(str)) {
                return 5;
            }
            if ("tax".equals(str)) {
                return 6;
            }
            if ("id".equals(str)) {
                return 7;
            }
            return -1;
        }

        /* JADX WARNING: Removed duplicated region for block: B:24:0x003b A[ORIG_RETURN, RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static boolean needRecord(int r2) {
            /*
                r0 = 1
                if (r2 != r0) goto L_0x000a
                boolean r2 = io.dcloud.common.util.AssistInputUtil.useAssistSettingPhone()
                if (r2 == 0) goto L_0x003c
                goto L_0x003b
            L_0x000a:
                r1 = 5
                if (r2 != r1) goto L_0x0014
                boolean r2 = io.dcloud.common.util.AssistInputUtil.useAssistSettingCompany()
                if (r2 == 0) goto L_0x003c
                goto L_0x003b
            L_0x0014:
                r1 = 6
                if (r2 != r1) goto L_0x001e
                boolean r2 = io.dcloud.common.util.AssistInputUtil.useAssistSettingTax()
                if (r2 == 0) goto L_0x003c
                goto L_0x003b
            L_0x001e:
                r1 = 7
                if (r2 != r1) goto L_0x0028
                boolean r2 = io.dcloud.common.util.AssistInputUtil.useAssistSettingId()
                if (r2 == 0) goto L_0x003c
                goto L_0x003b
            L_0x0028:
                r1 = 3
                if (r2 != r1) goto L_0x0032
                boolean r2 = io.dcloud.common.util.AssistInputUtil.useAssistSettingName()
                if (r2 == 0) goto L_0x003c
                goto L_0x003b
            L_0x0032:
                r1 = 2
                if (r2 != r1) goto L_0x003c
                boolean r2 = io.dcloud.common.util.AssistInputUtil.useAssistSettingEmail()
                if (r2 == 0) goto L_0x003c
            L_0x003b:
                r0 = 0
            L_0x003c:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.RecordView.Utils.needRecord(int):boolean");
        }
    }

    RecordView(Context context, ViewGroup viewGroup, String str) {
        this.mShowed = false;
        this.mMainView = null;
        this.mHandler = null;
        this.isLongClick = false;
        this.mRecordData = null;
        this.mHandler = new Handler(this);
        this.mAppid = str;
        this.Height = dp2px(context, 46.0f);
        this.mRecordData = new RecordData();
        FrameLayout frameLayout = new FrameLayout(context);
        this.mMainView = frameLayout;
        frameLayout.addView(initView2(context, frameLayout));
        frameLayout.addView(initView1(context, frameLayout));
        viewGroup.addView(frameLayout, new FrameLayout.LayoutParams(-1, this.Height));
    }

    private boolean checkLocationPermission(Activity activity) {
        return PermissionUtil.checkLocationPermission(activity);
    }

    private boolean checkLocationService(Activity activity) {
        return PermissionUtil.checkLocationService(activity);
    }

    static int dp2px(Context context, float f) {
        return (int) TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static String getAssisBundleData(String str) {
        return getAssisBundleData(DeviceInfo.sApplicationContext, str);
    }

    public static String getRecordDatas(String str, String str2) {
        return getRecordDatas0(DeviceInfo.sApplicationContext, str, str2);
    }

    private static String getRecordDatas0(Context context, String str, String str2) {
        if ("address_home".equals(str2)) {
            return AssistInputUtil.getHomeAddress(AdaWebview.sCustomeizedInputConnection.mWebview.getContext());
        }
        if ("address_work".equals(str2)) {
            return AssistInputUtil.getWorkAddress(AdaWebview.sCustomeizedInputConnection.mWebview.getContext());
        }
        return getRecordDatas(context, str, Utils.convertInt(str2));
    }

    private TextView getTextView(View view, String str) {
        View view2 = getView(view, str);
        if (view2 instanceof TextView) {
            return (TextView) view2;
        }
        return null;
    }

    private View getView(View view, String str) {
        return view.findViewById(PdrR.getInt(view.getContext(), "id", str));
    }

    /* access modifiers changed from: private */
    public void handleAssistInputPreviousOrNextButtOnTouch(MotionEvent motionEvent, int i) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.isLongClick = false;
            Message obtain = Message.obtain();
            obtain.what = 0;
            obtain.obj = Integer.valueOf(i);
            this.mHandler.sendMessageDelayed(obtain, 500);
        } else if (action == 1) {
            this.mHandler.removeMessages(0);
            this.mHandler.removeMessages(1);
            if (!this.isLongClick) {
                AdaWebview.sCustomeizedInputConnection.update(i);
            }
            this.isLongClick = false;
        }
    }

    private void initArrowView(View view) {
        if (view != null) {
            View view2 = getView(view, "dcloud_record_arrow_left_layout");
            if (view2 != null) {
                view2.setClickable(true);
                view2.setLongClickable(true);
                view2.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        RecordView.this.handleAssistInputPreviousOrNextButtOnTouch(motionEvent, -1);
                        return true;
                    }
                });
            }
            View view3 = getView(view, "dcloud_record_arrow_right_layout");
            if (view3 != null) {
                view3.setClickable(true);
                view3.setLongClickable(true);
                view3.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        RecordView.this.handleAssistInputPreviousOrNextButtOnTouch(motionEvent, 1);
                        return true;
                    }
                });
            }
        }
    }

    private ViewGroup initView1(Context context, ViewGroup viewGroup) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(PdrR.getInt(context, Constants.Name.LAYOUT, "dcloud_record_default"), (ViewGroup) null);
        getView(relativeLayout, "dcloud_record_scroll_view").setHorizontalScrollBarEnabled(false);
        getView(relativeLayout, "dcloud_record_view_1").setOnClickListener(this);
        getView(relativeLayout, "dcloud_record_view_2").setOnClickListener(this);
        initArrowView(relativeLayout);
        return relativeLayout;
    }

    private ViewGroup initView2(Context context, ViewGroup viewGroup) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(PdrR.getInt(context, Constants.Name.LAYOUT, "dcloud_record_address"), (ViewGroup) null);
        getView(relativeLayout, "dcloud_record_scroll_view").setHorizontalScrollBarEnabled(false);
        getView(relativeLayout, "dcloud_record_address_view_1").setOnClickListener(this);
        getView(relativeLayout, "dcloud_record_address_view_2").setOnClickListener(this);
        getView(relativeLayout, "dcloud_record_address_view_3").setOnClickListener(this);
        initArrowView(relativeLayout);
        return relativeLayout;
    }

    /* access modifiers changed from: private */
    public void log(String str, String str2) {
        Logger.e(str, str2 + ";this=" + this);
    }

    /* access modifiers changed from: private */
    public void requestCurrentLocation(final TextView textView) {
        final Activity activity = AdaWebview.sCustomeizedInputConnection.mWebview.getActivity();
        if (!checkLocationPermission(activity)) {
            showConfrim(activity, activity.getString(R.string.dcloud_geo_open_permissions), new String[]{activity.getString(R.string.dcloud_common_set_up), activity.getString(R.string.dcloud_common_cancel)}, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == -2) {
                        AdaWebview.sCustomeizedInputConnection.mWebview.obtainApp().registerSysEventListener(new ISysEventListener() {
                            public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                                IApp obtainApp = AdaWebview.sCustomeizedInputConnection.mWebview.obtainApp();
                                ISysEventListener.SysEventType sysEventType2 = ISysEventListener.SysEventType.onResume;
                                obtainApp.unregisterSysEventListener(this, sysEventType2);
                                if (sysEventType != sysEventType2) {
                                    return false;
                                }
                                AnonymousClass5 r2 = AnonymousClass5.this;
                                RecordView.this.requestCurrentLocation(textView);
                                return false;
                            }
                        }, ISysEventListener.SysEventType.onResume);
                        try {
                            PermissionUtil.goSafeCenter(activity);
                        } catch (ActivityNotFoundException e) {
                            Logger.e("AssistantInput", "checkLocationService ActivityNotFoundException =" + e);
                            e.printStackTrace();
                        } catch (Exception e2) {
                            Logger.e("AssistantInput", "Exception =" + e2);
                        }
                    }
                }
            });
        } else if (checkLocationService(activity)) {
            requestCurrentLocation0(textView);
        } else {
            showConfrim(activity, activity.getString(R.string.dcloud_geo_open_service), new String[]{activity.getString(R.string.dcloud_common_set_up), activity.getString(R.string.dcloud_common_cancel)}, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == -2) {
                        AdaWebview.sCustomeizedInputConnection.mWebview.obtainApp().registerSysEventListener(new ISysEventListener() {
                            public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                                IApp obtainApp = AdaWebview.sCustomeizedInputConnection.mWebview.obtainApp();
                                ISysEventListener.SysEventType sysEventType2 = ISysEventListener.SysEventType.onResume;
                                obtainApp.unregisterSysEventListener(this, sysEventType2);
                                if (sysEventType != sysEventType2) {
                                    return false;
                                }
                                AnonymousClass4 r2 = AnonymousClass4.this;
                                RecordView.this.requestCurrentLocation(textView);
                                return false;
                            }
                        }, ISysEventListener.SysEventType.onResume);
                        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                        try {
                            activity.startActivity(intent);
                            RecordView recordView = RecordView.this;
                            recordView.log("AssistantInput", "checkLocationService successful " + Build.MODEL + "intent=" + intent);
                        } catch (ActivityNotFoundException e) {
                            Logger.e("AssistantInput", "checkLocationService ActivityNotFoundException =" + e);
                            e.printStackTrace();
                        } catch (Exception e2) {
                            Logger.e("Permission", "Exception =" + e2);
                        }
                    }
                }
            });
        }
    }

    private void requestCurrentLocation0(final TextView textView) {
        textView.setText(R.string.dcloud_geo_loading);
        AnonymousClass6 r0 = new FeatureMessageDispatcher.StrongMessageListener("record_address") {
            public void onReceiver(Object obj) {
                if (obj instanceof String) {
                    AdaWebview.sCustomeizedInputConnection.setText((String) obj);
                    textView.setText(R.string.dcloud_geo_current_address);
                    return;
                }
                textView.setText(R.string.dcloud_geo_current_address);
            }
        };
        FeatureMessageDispatcher.registerListener(r0);
        IWebview iWebview = AdaWebview.sCustomeizedInputConnection.mWebview;
        AbsMgr obtainWindowMgr = iWebview.obtainFrameView().obtainWindowMgr();
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(AnonymousClass6.class.getName() + "_" + r0.hashCode());
        jSONArray.put(r0.hashCode());
        jSONArray.put(true);
        jSONArray.put("null");
        jSONArray.put("");
        jSONArray.put("null");
        jSONArray.put("null");
        jSONArray.put("null");
        obtainWindowMgr.processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{iWebview, IFeature.F_GEOLOCATION, "getCurrentPosition", jSONArray});
    }

    public static void setAssisBundleData(Context context, String str, String str2) {
        SP.setBundleData(context, "assis_input", str, str2, true);
    }

    /* access modifiers changed from: private */
    public static void setRcordDatas(Context context, String str, int i, String str2) {
        if (i != -1) {
            String encodeString = Base64.encodeString(str2, true, 5);
            if (PdrUtil.isEmpty(encodeString)) {
                encodeString = "";
            }
            setAssisBundleData(context, "_input_text" + i, encodeString);
        }
    }

    private static void setRcordDatas0(Context context, String str, String str2, String str3) {
        setRcordDatas(context, str, Utils.convertInt(str2), str3);
    }

    private void showConfrim(Activity activity, String str, String[] strArr, DialogInterface.OnClickListener onClickListener) {
        DCloudAlertDialog initDialogTheme = DialogUtil.initDialogTheme(activity, true);
        initDialogTheme.setCanceledOnTouchOutside(false);
        initDialogTheme.setMessage(str);
        initDialogTheme.setButton(-2, strArr[0], onClickListener);
        initDialogTheme.setButton(-1, strArr[1], onClickListener);
        initDialogTheme.show();
    }

    /* access modifiers changed from: package-private */
    public void conceal() {
    }

    /* access modifiers changed from: package-private */
    public void display() {
    }

    public synchronized void dispose() {
        this.isLongClick = false;
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages((Object) null);
        }
        this.mHandler = null;
    }

    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 0) {
            this.isLongClick = true;
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = message.obj;
            this.mHandler.sendMessageDelayed(obtain, 100);
            return false;
        } else if (i != 1 || !this.isLongClick) {
            return false;
        } else {
            Object obj = message.obj;
            if (obj instanceof Integer) {
                AdaWebview.sCustomeizedInputConnection.update(((Integer) obj).intValue());
            }
            Message obtain2 = Message.obtain();
            obtain2.what = 1;
            obtain2.obj = message.obj;
            this.mHandler.sendMessageDelayed(obtain2, 100);
            return false;
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        Context context = view.getContext();
        if (id == PdrR.getInt(context, "id", "dcloud_record_address_view_1")) {
            requestCurrentLocation((TextView) view);
        } else if (id == PdrR.getInt(context, "id", "dcloud_record_address_view_2")) {
            AdaWebview.sCustomeizedInputConnection.setText(getRecordDatas(this.mMainView.getContext(), this.mAppid, "address_home"));
        } else if (id == PdrR.getInt(context, "id", "dcloud_record_address_view_3")) {
            AdaWebview.sCustomeizedInputConnection.setText(getRecordDatas(this.mMainView.getContext(), this.mAppid, "address_work"));
        } else {
            AdaWebview.sCustomeizedInputConnection.setText(((TextView) view).getText().toString());
        }
    }

    /* access modifiers changed from: package-private */
    public void record(String str, int i) {
        if (i != -1) {
            this.mRecordData.record(str, i);
        }
    }

    /* access modifiers changed from: package-private */
    public void update(final int i, final int i2) {
        this.mMainView.post(new Runnable() {
            public void run() {
                RecordView.this.update0(i, i2);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void update0(int i, int i2) {
        boolean z;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mMainView.getLayoutParams();
        layoutParams.topMargin = i - this.Height;
        layoutParams.bottomMargin = i;
        this.mMainView.setLayoutParams(layoutParams);
        this.mAnchorY = i;
        boolean z2 = true;
        int i3 = 0;
        boolean z3 = i2 != -1;
        this.mMainView.setVisibility(z3 ? 0 : 8);
        this.mMainView.bringToFront();
        this.mRecordData.checkType(i2);
        View childAt = this.mMainView.getChildAt(0);
        View childAt2 = this.mMainView.getChildAt(1);
        if (i2 == 4) {
            childAt.setVisibility(0);
            childAt2.setVisibility(8);
            getView(childAt, "dcloud_record_address_view_1").setVisibility(0);
            getTextView(childAt, "dcloud_record_address_view_1").setText(R.string.dcloud_current_address);
            String recordDatas = getRecordDatas(this.mMainView.getContext(), this.mAppid, "address_home");
            getView(childAt, "dcloud_record_line_1").setVisibility(TextUtils.isEmpty(recordDatas) ? 8 : 0);
            getView(childAt, "dcloud_record_address_view_2").setVisibility(TextUtils.isEmpty(recordDatas) ? 8 : 0);
            String recordDatas2 = getRecordDatas(this.mMainView.getContext(), this.mAppid, "address_work");
            getView(childAt, "dcloud_record_line_2").setVisibility(TextUtils.isEmpty(recordDatas2) ? 8 : 0);
            View view = getView(childAt, "dcloud_record_address_view_3");
            if (TextUtils.isEmpty(recordDatas2)) {
                i3 = 8;
            }
            view.setVisibility(i3);
        } else {
            childAt2.setVisibility(0);
            childAt.setVisibility(8);
            if (this.mRecordData.mRecordItems[0] != null) {
                getView(childAt2, "dcloud_record_view_1").setVisibility(z3 ? 0 : 8);
                getTextView(childAt2, "dcloud_record_view_1").setText(this.mRecordData.mRecordItems[0].mContent);
                z = true;
            } else {
                this.mMainView.setVisibility(8);
                z = false;
            }
            if (this.mRecordData.mRecordItems[1] != null) {
                getView(childAt2, "dcloud_record_line_1").setVisibility(z3 ? 0 : 8);
                View view2 = getView(childAt2, "dcloud_record_view_2");
                if (!z3) {
                    i3 = 8;
                }
                view2.setVisibility(i3);
                getTextView(childAt2, "dcloud_record_view_2").setText(this.mRecordData.mRecordItems[1].mContent);
            } else {
                getView(childAt2, "dcloud_record_line_1").setVisibility(8);
                getView(childAt2, "dcloud_record_view_2").setVisibility(8);
            }
            z2 = z;
        }
        if (!z3 || !z2) {
            conceal();
        } else {
            display();
        }
    }

    public static String getAssisBundleData(Context context, String str) {
        return SP.getBundleData(context, "assis_input", str, true);
    }

    public static String getRecordDatas(Context context, String str, String str2) {
        return getRecordDatas0(context, str, str2);
    }

    /* access modifiers changed from: private */
    public static String getRecordDatas(Context context, String str, int i) {
        if (i == -1) {
            return null;
        }
        return Base64.decodeString(getAssisBundleData(context, "_input_text" + i), true, 5);
    }

    public static void setRcordDatas(Context context, String str, String str2, String str3) {
        setRcordDatas0(context, str, str2, str3);
    }
}
