package com.taobao.weex.ui.module;

import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;
import com.taobao.weex.WXSDKEngine;

public class WXModalUIModule extends WXSDKEngine.DestroyableModule {
    public static final String CANCEL = "Cancel";
    public static final String CANCEL_TITLE = "cancelTitle";
    public static final String DATA = "data";
    public static final String DEFAULT = "default";
    public static final String DURATION = "duration";
    public static final String GRAVITY = "gravity";
    public static final String MESSAGE = "message";
    public static final String OK = "OK";
    public static final String OK_TITLE = "okTitle";
    public static final String RESULT = "result";
    /* access modifiers changed from: private */
    public Dialog activeDialog;
    private Toast toast;

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005e  */
    @com.taobao.weex.annotation.JSMethod(uiThread = true)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void toast(com.alibaba.fastjson.JSONObject r7) {
        /*
            r6 = this;
            java.lang.String r0 = "gravity"
            java.lang.String r1 = "duration"
            com.taobao.weex.WXSDKInstance r2 = r6.mWXSDKInstance
            android.content.Context r2 = r2.getContext()
            if (r2 != 0) goto L_0x000d
            return
        L_0x000d:
            java.lang.String r2 = ""
            r3 = 17
            r4 = 0
            if (r7 == 0) goto L_0x004f
            java.lang.String r5 = "message"
            java.lang.String r2 = r7.getString(r5)     // Catch:{ Exception -> 0x0045 }
            boolean r5 = r7.containsKey(r1)     // Catch:{ Exception -> 0x0045 }
            if (r5 == 0) goto L_0x0029
            java.lang.Integer r1 = r7.getInteger(r1)     // Catch:{ Exception -> 0x0045 }
            int r1 = r1.intValue()     // Catch:{ Exception -> 0x0045 }
            goto L_0x002a
        L_0x0029:
            r1 = 0
        L_0x002a:
            boolean r5 = r7.containsKey(r0)     // Catch:{ Exception -> 0x0043 }
            if (r5 == 0) goto L_0x004c
            java.lang.String r7 = r7.getString(r0)     // Catch:{ Exception -> 0x0043 }
            boolean r0 = r7 instanceof java.lang.String     // Catch:{ Exception -> 0x0043 }
            if (r0 == 0) goto L_0x004c
            java.lang.String r0 = "bottom"
            boolean r7 = r7.equals(r0)     // Catch:{ Exception -> 0x0043 }
            if (r7 == 0) goto L_0x004c
            r7 = 80
            goto L_0x0052
        L_0x0043:
            r7 = move-exception
            goto L_0x0047
        L_0x0045:
            r7 = move-exception
            r1 = 0
        L_0x0047:
            java.lang.String r0 = "[WXModalUIModule] alert param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.Throwable) r7)
        L_0x004c:
            r7 = 17
            goto L_0x0052
        L_0x004f:
            r7 = 17
            r1 = 0
        L_0x0052:
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 == 0) goto L_0x005e
            java.lang.String r7 = "[WXModalUIModule] toast param parse is null "
            com.taobao.weex.utils.WXLogUtils.e(r7)
            return
        L_0x005e:
            r0 = 3
            if (r1 <= r0) goto L_0x0063
            r0 = 1
            goto L_0x0064
        L_0x0063:
            r0 = 0
        L_0x0064:
            android.widget.Toast r1 = r6.toast
            if (r1 != 0) goto L_0x0075
            com.taobao.weex.WXSDKInstance r1 = r6.mWXSDKInstance
            android.content.Context r1 = r1.getContext()
            com.dcloud.android.widget.toast.ToastCompat r0 = com.dcloud.android.widget.toast.ToastCompat.makeText((android.content.Context) r1, (java.lang.CharSequence) r2, (int) r0)
            r6.toast = r0
            goto L_0x007d
        L_0x0075:
            r1.setDuration(r0)
            android.widget.Toast r0 = r6.toast
            r0.setText(r2)
        L_0x007d:
            if (r3 != r7) goto L_0x0084
            android.widget.Toast r0 = r6.toast
            r0.setGravity(r7, r4, r4)
        L_0x0084:
            android.widget.Toast r7 = r6.toast
            r7.show()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.module.WXModalUIModule.toast(com.alibaba.fastjson.JSONObject):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0047  */
    @com.taobao.weex.annotation.JSMethod(uiThread = true)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void alert(com.alibaba.fastjson.JSONObject r5, final com.taobao.weex.bridge.JSCallback r6) {
        /*
            r4 = this;
            com.taobao.weex.WXSDKInstance r0 = r4.mWXSDKInstance
            android.content.Context r0 = r0.getContext()
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 == 0) goto L_0x005f
            java.lang.String r0 = "OK"
            java.lang.String r1 = ""
            if (r5 == 0) goto L_0x0028
            java.lang.String r2 = "message"
            java.lang.String r2 = r5.getString(r2)     // Catch:{ Exception -> 0x001f }
            java.lang.String r3 = "okTitle"
            java.lang.String r5 = r5.getString(r3)     // Catch:{ Exception -> 0x001d }
            goto L_0x002a
        L_0x001d:
            r5 = move-exception
            goto L_0x0021
        L_0x001f:
            r5 = move-exception
            r2 = r1
        L_0x0021:
            java.lang.String r3 = "[WXModalUIModule] alert param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r3, (java.lang.Throwable) r5)
            r5 = r0
            goto L_0x002a
        L_0x0028:
            r5 = r0
            r2 = r1
        L_0x002a:
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r1 = r2
        L_0x0032:
            android.app.AlertDialog$Builder r2 = new android.app.AlertDialog$Builder
            com.taobao.weex.WXSDKInstance r3 = r4.mWXSDKInstance
            android.content.Context r3 = r3.getContext()
            r2.<init>(r3)
            r2.setMessage(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 == 0) goto L_0x0047
            goto L_0x0048
        L_0x0047:
            r0 = r5
        L_0x0048:
            com.taobao.weex.ui.module.WXModalUIModule$1 r5 = new com.taobao.weex.ui.module.WXModalUIModule$1
            r5.<init>(r6, r0)
            r2.setPositiveButton(r0, r5)
            android.app.AlertDialog r5 = r2.create()
            r6 = 0
            r5.setCanceledOnTouchOutside(r6)
            r5.show()
            r4.tracking(r5)
            goto L_0x0064
        L_0x005f:
            java.lang.String r5 = "[WXModalUIModule] when call alert mWXSDKInstance.getContext() must instanceof Activity"
            com.taobao.weex.utils.WXLogUtils.e(r5)
        L_0x0064:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.module.WXModalUIModule.alert(com.alibaba.fastjson.JSONObject, com.taobao.weex.bridge.JSCallback):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x005c  */
    @com.taobao.weex.annotation.JSMethod(uiThread = true)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void confirm(com.alibaba.fastjson.JSONObject r7, final com.taobao.weex.bridge.JSCallback r8) {
        /*
            r6 = this;
            com.taobao.weex.WXSDKInstance r0 = r6.mWXSDKInstance
            android.content.Context r0 = r0.getContext()
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 == 0) goto L_0x007c
            java.lang.String r0 = "Cancel"
            java.lang.String r1 = "OK"
            java.lang.String r2 = ""
            if (r7 == 0) goto L_0x0034
            java.lang.String r3 = "message"
            java.lang.String r3 = r7.getString(r3)     // Catch:{ Exception -> 0x002a }
            java.lang.String r4 = "okTitle"
            java.lang.String r4 = r7.getString(r4)     // Catch:{ Exception -> 0x0027 }
            java.lang.String r5 = "cancelTitle"
            java.lang.String r7 = r7.getString(r5)     // Catch:{ Exception -> 0x0025 }
            goto L_0x0037
        L_0x0025:
            r7 = move-exception
            goto L_0x002d
        L_0x0027:
            r7 = move-exception
            r4 = r1
            goto L_0x002d
        L_0x002a:
            r7 = move-exception
            r4 = r1
            r3 = r2
        L_0x002d:
            java.lang.String r5 = "[WXModalUIModule] confirm param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r5, (java.lang.Throwable) r7)
            r7 = r0
            goto L_0x0037
        L_0x0034:
            r7 = r0
            r4 = r1
            r3 = r2
        L_0x0037:
            boolean r5 = android.text.TextUtils.isEmpty(r3)
            if (r5 == 0) goto L_0x003e
            goto L_0x003f
        L_0x003e:
            r2 = r3
        L_0x003f:
            android.app.AlertDialog$Builder r3 = new android.app.AlertDialog$Builder
            com.taobao.weex.WXSDKInstance r5 = r6.mWXSDKInstance
            android.content.Context r5 = r5.getContext()
            r3.<init>(r5)
            r3.setMessage(r2)
            boolean r2 = android.text.TextUtils.isEmpty(r4)
            if (r2 == 0) goto L_0x0054
            goto L_0x0055
        L_0x0054:
            r1 = r4
        L_0x0055:
            boolean r2 = android.text.TextUtils.isEmpty(r7)
            if (r2 == 0) goto L_0x005c
            goto L_0x005d
        L_0x005c:
            r0 = r7
        L_0x005d:
            com.taobao.weex.ui.module.WXModalUIModule$2 r7 = new com.taobao.weex.ui.module.WXModalUIModule$2
            r7.<init>(r8, r1)
            r3.setPositiveButton(r1, r7)
            com.taobao.weex.ui.module.WXModalUIModule$3 r7 = new com.taobao.weex.ui.module.WXModalUIModule$3
            r7.<init>(r8, r0)
            r3.setNegativeButton(r0, r7)
            android.app.AlertDialog r7 = r3.create()
            r8 = 0
            r7.setCanceledOnTouchOutside(r8)
            r7.show()
            r6.tracking(r7)
            goto L_0x0081
        L_0x007c:
            java.lang.String r7 = "[WXModalUIModule] when call confirm mWXSDKInstance.getContext() must instanceof Activity"
            com.taobao.weex.utils.WXLogUtils.e(r7)
        L_0x0081:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.module.WXModalUIModule.confirm(com.alibaba.fastjson.JSONObject, com.taobao.weex.bridge.JSCallback):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0079  */
    @com.taobao.weex.annotation.JSMethod(uiThread = true)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void prompt(com.alibaba.fastjson.JSONObject r8, final com.taobao.weex.bridge.JSCallback r9) {
        /*
            r7 = this;
            com.taobao.weex.WXSDKInstance r0 = r7.mWXSDKInstance
            android.content.Context r0 = r0.getContext()
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 == 0) goto L_0x009a
            java.lang.String r0 = "Cancel"
            java.lang.String r1 = "OK"
            java.lang.String r2 = ""
            if (r8 == 0) goto L_0x003f
            java.lang.String r3 = "message"
            java.lang.String r3 = r8.getString(r3)     // Catch:{ Exception -> 0x0034 }
            java.lang.String r4 = "okTitle"
            java.lang.String r4 = r8.getString(r4)     // Catch:{ Exception -> 0x0030 }
            java.lang.String r5 = "cancelTitle"
            java.lang.String r5 = r8.getString(r5)     // Catch:{ Exception -> 0x002d }
            java.lang.String r6 = "default"
            java.lang.String r8 = r8.getString(r6)     // Catch:{ Exception -> 0x002b }
            goto L_0x0043
        L_0x002b:
            r8 = move-exception
            goto L_0x0038
        L_0x002d:
            r8 = move-exception
            r5 = r0
            goto L_0x0038
        L_0x0030:
            r8 = move-exception
            r5 = r0
            r4 = r1
            goto L_0x0038
        L_0x0034:
            r8 = move-exception
            r5 = r0
            r4 = r1
            r3 = r2
        L_0x0038:
            java.lang.String r6 = "[WXModalUIModule] confirm param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r6, (java.lang.Throwable) r8)
            r8 = r2
            goto L_0x0043
        L_0x003f:
            r5 = r0
            r4 = r1
            r8 = r2
            r3 = r8
        L_0x0043:
            boolean r6 = android.text.TextUtils.isEmpty(r3)
            if (r6 == 0) goto L_0x004a
            goto L_0x004b
        L_0x004a:
            r2 = r3
        L_0x004b:
            android.app.AlertDialog$Builder r3 = new android.app.AlertDialog$Builder
            com.taobao.weex.WXSDKInstance r6 = r7.mWXSDKInstance
            android.content.Context r6 = r6.getContext()
            r3.<init>(r6)
            r3.setMessage(r2)
            android.widget.EditText r2 = new android.widget.EditText
            com.taobao.weex.WXSDKInstance r6 = r7.mWXSDKInstance
            android.content.Context r6 = r6.getContext()
            r2.<init>(r6)
            r2.setText(r8)
            r3.setView(r2)
            boolean r8 = android.text.TextUtils.isEmpty(r4)
            if (r8 == 0) goto L_0x0071
            goto L_0x0072
        L_0x0071:
            r1 = r4
        L_0x0072:
            boolean r8 = android.text.TextUtils.isEmpty(r5)
            if (r8 == 0) goto L_0x0079
            goto L_0x007a
        L_0x0079:
            r0 = r5
        L_0x007a:
            com.taobao.weex.ui.module.WXModalUIModule$5 r8 = new com.taobao.weex.ui.module.WXModalUIModule$5
            r8.<init>(r9, r1, r2)
            android.app.AlertDialog$Builder r8 = r3.setPositiveButton(r1, r8)
            com.taobao.weex.ui.module.WXModalUIModule$4 r1 = new com.taobao.weex.ui.module.WXModalUIModule$4
            r1.<init>(r9, r0, r2)
            r8.setNegativeButton(r0, r1)
            android.app.AlertDialog r8 = r3.create()
            r9 = 0
            r8.setCanceledOnTouchOutside(r9)
            r8.show()
            r7.tracking(r8)
            goto L_0x00a0
        L_0x009a:
            java.lang.String r8 = "when call prompt mWXSDKInstance.getContext() must instanceof Activity"
            com.taobao.weex.utils.WXLogUtils.e(r8)
        L_0x00a0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.module.WXModalUIModule.prompt(com.alibaba.fastjson.JSONObject, com.taobao.weex.bridge.JSCallback):void");
    }

    private void tracking(Dialog dialog) {
        this.activeDialog = dialog;
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                Dialog unused = WXModalUIModule.this.activeDialog = null;
            }
        });
    }

    public void destroy() {
        Dialog dialog = this.activeDialog;
        if (dialog != null && dialog.isShowing()) {
            this.activeDialog.dismiss();
        }
    }
}
