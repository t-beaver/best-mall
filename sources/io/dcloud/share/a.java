package io.dcloud.share;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.core.content.FileProvider;
import androidx.webkit.internal.AssetHelper;
import com.facebook.common.util.UriUtil;
import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class a {
    private AbsMgr a;
    private HashMap<String, IFShareApi> b = new HashMap<>();
    private HashMap<String, ShareAuthorizeView> c;
    public ArrayList<Uri> d;
    private HashMap<String, String> e;

    protected a(AbsMgr absMgr, String str) {
        this.a = absMgr;
        this.e = (HashMap) this.a.processEvent(IMgr.MgrType.FeatureMgr, 4, str);
        this.d = new ArrayList<>();
    }

    public String a(IWebview iWebview, String str, String[] strArr) {
        Context context = iWebview.getContext();
        AppRuntime.checkPrivacyComplianceAndPrompt(context, "Share-" + str);
        str.hashCode();
        str.hashCode();
        char c2 = 65535;
        switch (str.hashCode()) {
            case -1748133766:
                if (str.equals("launchMiniProgram")) {
                    c2 = 0;
                    break;
                }
                break;
            case -1352294148:
                if (str.equals("create")) {
                    c2 = 1;
                    break;
                }
                break;
            case -1268789356:
                if (str.equals("forbid")) {
                    c2 = 2;
                    break;
                }
                break;
            case -854558288:
                if (str.equals("setVisible")) {
                    c2 = 3;
                    break;
                }
                break;
            case -837857836:
                if (str.equals("getServices")) {
                    c2 = 4;
                    break;
                }
                break;
            case 3327206:
                if (str.equals("load")) {
                    c2 = 5;
                    break;
                }
                break;
            case 3526536:
                if (str.equals("send")) {
                    c2 = 6;
                    break;
                }
                break;
            case 18269245:
                if (str.equals("sendWithSystem")) {
                    c2 = 7;
                    break;
                }
                break;
            case 996243013:
                if (str.equals("openCustomerServiceChat")) {
                    c2 = 8;
                    break;
                }
                break;
            case 1475610601:
                if (str.equals(DOMException.MSG_SHARE_AUTHORIZE_ERROR)) {
                    c2 = 9;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
                String str2 = strArr[0];
                IFShareApi iFShareApi = this.b.get(strArr[1]);
                if (iFShareApi instanceof IWeiXinFShareApi) {
                    ((IWeiXinFShareApi) iFShareApi).launchMiniProgram(iWebview, strArr[2], str2);
                    return null;
                }
                IWebview iWebview2 = iWebview;
                Deprecated_JSUtil.execCallback(iWebview2, str2, StringUtil.format(DOMException.JSON_ERROR_INFO, -3, DOMException.MSG_NOT_SUPPORT), JSUtil.ERROR, true, false);
                return null;
            case 1:
                ShareAuthorizeView shareAuthorizeView = new ShareAuthorizeView(iWebview, strArr[1]);
                if (strArr[2] == null || !strArr[1].equals(AbsoluteConst.FALSE)) {
                    float scale = iWebview.getScale();
                    iWebview.addFrameItem(shareAuthorizeView, AdaFrameItem.LayoutParamsUtil.createLayoutParams((int) (((float) Integer.parseInt(strArr[3])) * scale), (int) (((float) Integer.parseInt(strArr[4])) * scale), (int) (((float) Integer.parseInt(strArr[5])) * scale), (int) (((float) Integer.parseInt(strArr[6])) * scale)));
                }
                this.c.put(strArr[0], shareAuthorizeView);
                return null;
            case 2:
                this.b.get(strArr[0]).forbid(iWebview);
                return null;
            case 3:
                ShareAuthorizeView shareAuthorizeView2 = this.c.get(strArr[0]);
                if (Boolean.parseBoolean(strArr[1])) {
                    shareAuthorizeView2.setVisibility(0);
                    return null;
                }
                shareAuthorizeView2.setVisibility(8);
                return null;
            case 4:
                Deprecated_JSUtil.execCallback(iWebview, strArr[0], a(iWebview), 1, true, false);
                return null;
            case 5:
                this.c.get(strArr[0]).load(this, strArr[1]);
                return null;
            case 6:
                IFShareApi iFShareApi2 = this.b.get(strArr[1]);
                Application application = iWebview.getActivity().getApplication();
                if (application instanceof DCloudApplication) {
                    ((DCloudApplication) application).stopB2FOnce();
                }
                iFShareApi2.send(iWebview, strArr[0], strArr[2]);
                return null;
            case 7:
                a(iWebview, strArr[0], strArr[1]);
                return null;
            case 8:
                String str3 = strArr[0];
                IFShareApi iFShareApi3 = this.b.get(strArr[1]);
                if (iFShareApi3 instanceof IWeiXinFShareApi) {
                    ((IWeiXinFShareApi) iFShareApi3).openCustomerServiceChat(iWebview, strArr[2], str3);
                    return null;
                }
                IWebview iWebview3 = iWebview;
                Deprecated_JSUtil.execCallback(iWebview3, str3, StringUtil.format(DOMException.JSON_ERROR_INFO, -3, DOMException.MSG_NOT_SUPPORT), JSUtil.ERROR, true, false);
                return null;
            case 9:
                this.b.get(strArr[1]).authorize(iWebview, strArr[0], strArr[2]);
                return null;
            default:
                return null;
        }
    }

    public String a(String str) {
        if (PdrUtil.isEquals(str, "sinaweibo")) {
            return this.e.get("sina");
        }
        if (PdrUtil.isEquals(str, "tencentweibo")) {
            return this.e.get("tencent");
        }
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v14, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v7, resolved type: io.dcloud.share.IFShareApi} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(io.dcloud.common.DHInterface.IWebview r10) {
        /*
            r9 = this;
            java.lang.String r0 = "ShareApiManager getServices "
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            java.lang.String r2 = "["
            r1.append(r2)
            java.util.HashMap<java.lang.String, java.lang.String> r2 = r9.e
            if (r2 == 0) goto L_0x00ad
            boolean r2 = r2.isEmpty()
            if (r2 != 0) goto L_0x00ad
            java.util.HashMap<java.lang.String, java.lang.String> r2 = r9.e
            java.util.Set r2 = r2.keySet()
            java.util.Iterator r2 = r2.iterator()
            r3 = 0
            r4 = 0
        L_0x0022:
            boolean r5 = r2.hasNext()
            if (r5 == 0) goto L_0x00ad
            java.lang.Object r5 = r2.next()     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            java.util.HashMap<java.lang.String, io.dcloud.share.IFShareApi> r6 = r9.b     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            java.lang.Object r6 = r6.get(r5)     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            io.dcloud.share.IFShareApi r6 = (io.dcloud.share.IFShareApi) r6     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            if (r6 != 0) goto L_0x005d
            java.util.HashMap<java.lang.String, java.lang.String> r6 = r9.e     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            java.lang.Object r5 = r6.get(r5)     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            java.lang.Class r3 = java.lang.Class.forName(r5)     // Catch:{ NotFoundException -> 0x005b, Exception -> 0x0059 }
            java.lang.Object r3 = r3.newInstance()     // Catch:{ NotFoundException -> 0x005b, Exception -> 0x0059 }
            r6 = r3
            io.dcloud.share.IFShareApi r6 = (io.dcloud.share.IFShareApi) r6     // Catch:{ NotFoundException -> 0x005b, Exception -> 0x0059 }
            r6.initConfig()     // Catch:{ NotFoundException -> 0x005b, Exception -> 0x0059 }
            java.util.HashMap<java.lang.String, io.dcloud.share.IFShareApi> r3 = r9.b     // Catch:{ NotFoundException -> 0x005b, Exception -> 0x0059 }
            java.lang.String r7 = r6.getId()     // Catch:{ NotFoundException -> 0x005b, Exception -> 0x0059 }
            r3.put(r7, r6)     // Catch:{ NotFoundException -> 0x005b, Exception -> 0x0059 }
            r3 = r5
            goto L_0x005d
        L_0x0059:
            r3 = move-exception
            goto L_0x0076
        L_0x005b:
            r3 = r5
            goto L_0x0092
        L_0x005d:
            java.lang.String r5 = r6.getJsonObject(r10)     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            r1.append(r5)     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            java.util.HashMap<java.lang.String, io.dcloud.share.IFShareApi> r5 = r9.b     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            int r5 = r5.size()     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            if (r4 == r5) goto L_0x00a9
            java.lang.String r5 = ","
            r1.append(r5)     // Catch:{ NotFoundException -> 0x0092, Exception -> 0x0072 }
            goto L_0x00a9
        L_0x0072:
            r5 = move-exception
            r8 = r5
            r5 = r3
            r3 = r8
        L_0x0076:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r6.append(r0)
            r6.append(r5)
            java.lang.String r7 = " Exception ="
            r6.append(r7)
            r6.append(r3)
            java.lang.String r3 = r6.toString()
            io.dcloud.common.adapter.util.Logger.e(r3)
            r3 = r5
            goto L_0x00a9
        L_0x0092:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r0)
            r5.append(r3)
            java.lang.String r6 = " is Not found!"
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            io.dcloud.common.adapter.util.Logger.e(r5)
        L_0x00a9:
            int r4 = r4 + 1
            goto L_0x0022
        L_0x00ad:
            java.lang.String r10 = "]"
            r1.append(r10)
            java.lang.String r10 = r1.toString()
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.share.a.a(io.dcloud.common.DHInterface.IWebview):java.lang.String");
    }

    public void a(IWebview iWebview, String str, String str2) {
        Uri uri;
        try {
            JSONObject jSONObject = new JSONObject(str2);
            String optString = jSONObject.optString(UriUtil.LOCAL_CONTENT_SCHEME);
            String optString2 = jSONObject.optString(AbsoluteConst.JSON_KEY_TITLE);
            String optString3 = jSONObject.optString("href");
            if (!TextUtils.isEmpty(optString3)) {
                optString = optString + "  " + optString3;
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("pictures");
            Intent intent = new Intent();
            if (!PdrUtil.isEmpty(optJSONArray)) {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < optJSONArray.length(); i++) {
                    String convert2AbsFullPath = iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), optJSONArray.optString(i));
                    if (convert2AbsFullPath.startsWith(DeviceInfo.sPrivateDir)) {
                        String str3 = DeviceInfo.sPrivateExternalDir + convert2AbsFullPath.substring(DeviceInfo.sPrivateDir.length());
                        if (DHFile.copyFile(convert2AbsFullPath, str3, true, false) == 1) {
                            convert2AbsFullPath = str3;
                        }
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        uri = a(iWebview.getContext(), new File(convert2AbsFullPath), intent);
                    } else {
                        uri = Uri.fromFile(new File(convert2AbsFullPath));
                    }
                    arrayList.add(uri);
                }
                a(intent, optString, optString2, arrayList);
            } else {
                a(intent, optString, optString2, (ArrayList<Uri>) null);
            }
            Intent createChooser = Intent.createChooser(intent, "");
            createChooser.addFlags(1);
            iWebview.getActivity().startActivity(createChooser);
            Deprecated_JSUtil.execCallback(iWebview, str, "", JSUtil.OK, false, false);
        } catch (Exception unused) {
            Deprecated_JSUtil.execCallback(iWebview, str, StringUtil.format(DOMException.JSON_ERROR_INFO, -99, DOMException.MSG_UNKNOWN_ERROR), JSUtil.ERROR, true, false);
        }
    }

    private Intent a(Intent intent, String str, String str2, ArrayList<Uri> arrayList) {
        if (!PdrUtil.isEmpty(str)) {
            intent.putExtra("android.intent.extra.TEXT", str);
        }
        if (!PdrUtil.isEmpty(str2)) {
            intent.putExtra("android.intent.extra.SUBJECT", str2);
        }
        if (PdrUtil.isEmpty(arrayList) || arrayList.size() <= 0) {
            intent.setAction("android.intent.action.SEND");
            intent.setType(AssetHelper.DEFAULT_MIME_TYPE);
        } else {
            intent.setType("image/*");
            if (arrayList.size() > 1) {
                intent.setAction("android.intent.action.SEND_MULTIPLE");
                intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
            } else {
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.STREAM", arrayList.get(0));
            }
        }
        return intent;
    }

    public Uri a(Context context, File file, Intent intent) {
        String absolutePath = file.getAbsolutePath();
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=? ", new String[]{absolutePath}, (String) null);
        Uri uri = null;
        if (query != null) {
            if (query.moveToFirst()) {
                int i = query.getInt(query.getColumnIndex("_id"));
                Uri parse = Uri.parse("content://media/external/images/media");
                uri = Uri.withAppendedPath(parse, "" + i);
            }
            query.close();
        }
        if (uri == null) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".dc.fileprovider", file);
        }
        if (uri != null) {
            return uri;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", absolutePath);
        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public void a() {
        HashMap<String, IFShareApi> hashMap = this.b;
        if (hashMap != null) {
            for (Map.Entry<String, IFShareApi> value : hashMap.entrySet()) {
                ((IFShareApi) value.getValue()).dispose();
            }
        }
        HashMap<String, ShareAuthorizeView> hashMap2 = this.c;
        if (hashMap2 != null) {
            for (Map.Entry<String, ShareAuthorizeView> value2 : hashMap2.entrySet()) {
                ((ShareAuthorizeView) value2.getValue()).dispose();
            }
        }
        HashMap<String, String> hashMap3 = this.e;
        if (hashMap3 != null) {
            hashMap3.clear();
        }
        HashMap<String, IFShareApi> hashMap4 = this.b;
        if (hashMap4 != null) {
            hashMap4.clear();
        }
        HashMap<String, ShareAuthorizeView> hashMap5 = this.c;
        if (hashMap5 != null) {
            hashMap5.clear();
        }
    }
}
