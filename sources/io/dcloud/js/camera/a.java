package io.dcloud.js.camera;

import android.hardware.Camera;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

class a {
    protected static int a = 501;
    protected static int b = 502;
    protected static int c = 5011;
    List<Camera.Size> d = null;
    List<Integer> e = null;
    List<Camera.Size> f = null;
    int g;

    /* renamed from: io.dcloud.js.camera.a$a  reason: collision with other inner class name */
    static class C0070a {
        String a;
        String b;
        String c;
        int d;
        boolean e = true;
        int f = 0;
        JSONObject g = null;
        boolean h = false;

        C0070a() {
        }

        public String a() {
            return this.a;
        }

        public int b() {
            return this.f;
        }
    }

    a(int i) {
        this.g = i;
    }

    private String c() {
        List<Camera.Size> list = this.f;
        return list != null ? b(list) : "[]";
    }

    private String[] d() {
        List<Integer> list = this.e;
        String[] a2 = list != null ? a(list) : null;
        return a2 == null ? new String[]{"['jpg']", "['mp4']"} : a2;
    }

    private String e() {
        List<Camera.Size> list = this.d;
        return (list == null || DeviceInfo.sDeviceSdkVer < 11) ? "[]" : b(list);
    }

    /* access modifiers changed from: protected */
    public String a() {
        String[] d2 = d();
        return StringUtil.format("(function(){return{supportedImageResolutions : %s,supportedVideoResolutions : %s,supportedImageFormats : %s,supportedVideoFormats : %s};})();", c(), e(), d2[0], d2[1]);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x002a A[Catch:{ Exception -> 0x005c }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0034 A[Catch:{ Exception -> 0x005c }] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x004e A[Catch:{ Exception -> 0x005c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void b() {
        /*
            r3 = this;
            int r0 = r3.g     // Catch:{ Exception -> 0x005c }
            r1 = 2
            if (r0 != r1) goto L_0x0027
            int r0 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceSdkVer     // Catch:{ Exception -> 0x005c }
            r1 = 9
            if (r0 < r1) goto L_0x0027
            r0 = 0
        L_0x000c:
            int r1 = android.hardware.Camera.getNumberOfCameras()     // Catch:{ Exception -> 0x005c }
            if (r0 >= r1) goto L_0x0027
            android.hardware.Camera$CameraInfo r1 = new android.hardware.Camera$CameraInfo     // Catch:{ Exception -> 0x005c }
            r1.<init>()     // Catch:{ Exception -> 0x005c }
            android.hardware.Camera.getCameraInfo(r0, r1)     // Catch:{ Exception -> 0x005c }
            int r1 = r1.facing     // Catch:{ Exception -> 0x005c }
            r2 = 1
            if (r1 != r2) goto L_0x0024
            android.hardware.Camera r0 = android.hardware.Camera.open(r0)     // Catch:{ Exception -> 0x005c }
            goto L_0x0028
        L_0x0024:
            int r0 = r0 + 1
            goto L_0x000c
        L_0x0027:
            r0 = 0
        L_0x0028:
            if (r0 != 0) goto L_0x002e
            android.hardware.Camera r0 = android.hardware.Camera.open()     // Catch:{ Exception -> 0x005c }
        L_0x002e:
            int r1 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceSdkVer     // Catch:{ Exception -> 0x005c }
            r2 = 11
            if (r1 < r2) goto L_0x003e
            android.hardware.Camera$Parameters r1 = r0.getParameters()     // Catch:{ Exception -> 0x005c }
            java.util.List r1 = r1.getSupportedVideoSizes()     // Catch:{ Exception -> 0x005c }
            r3.d = r1     // Catch:{ Exception -> 0x005c }
        L_0x003e:
            android.hardware.Camera$Parameters r1 = r0.getParameters()     // Catch:{ Exception -> 0x005c }
            java.util.List r1 = r1.getSupportedPictureSizes()     // Catch:{ Exception -> 0x005c }
            r3.f = r1     // Catch:{ Exception -> 0x005c }
            int r1 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceSdkVer     // Catch:{ Exception -> 0x005c }
            r2 = 8
            if (r1 < r2) goto L_0x0058
            android.hardware.Camera$Parameters r1 = r0.getParameters()     // Catch:{ Exception -> 0x005c }
            java.util.List r1 = r1.getSupportedPictureFormats()     // Catch:{ Exception -> 0x005c }
            r3.e = r1     // Catch:{ Exception -> 0x005c }
        L_0x0058:
            r0.release()     // Catch:{ Exception -> 0x005c }
            goto L_0x0060
        L_0x005c:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0060:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.camera.a.b():void");
    }

    private String[] a(List<Integer> list) {
        return new String[]{"['jpg']", "['mp4']"};
    }

    static C0070a a(String str, boolean z) {
        C0070a aVar = new C0070a();
        if (str != null) {
            JSONObject jSONObject = null;
            try {
                jSONObject = new JSONObject(str);
            } catch (JSONException unused) {
            }
            aVar.b = JSONUtil.getString(jSONObject, "resolution");
            String string = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_FILENAME);
            JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_FORMAT);
            String str2 = z ? "jpg" : "mp4";
            aVar.c = str2;
            aVar.a = PdrUtil.getDefaultPrivateDocPath(string, str2);
            aVar.d = JSONUtil.getInt(jSONObject, "index");
            if (jSONObject != null && jSONObject.has("optimize")) {
                aVar.e = JSONUtil.getBoolean(jSONObject, "optimize");
            }
            if (jSONObject != null && jSONObject.has("videoMaximumDuration")) {
                aVar.f = JSONUtil.getInt(jSONObject, "videoMaximumDuration");
            }
            if (jSONObject != null && jSONObject.has("crop")) {
                aVar.g = jSONObject.optJSONObject("crop");
            }
            if (!z) {
                if (jSONObject != null && jSONObject.has("videoCompress")) {
                    aVar.h = jSONObject.optBoolean("videoCompress", false);
                }
            } else if (jSONObject != null && jSONObject.has("sizeType")) {
                String optString = jSONObject.optString("sizeType");
                if (!optString.contains(Constants.Value.ORIGINAL) || !optString.contains("compressed")) {
                    aVar.h = !optString.contains(Constants.Value.ORIGINAL);
                } else {
                    aVar.h = true;
                }
            }
        }
        return aVar;
    }

    private String b(List<Camera.Size> list) {
        int size = list.size();
        if (size <= 1) {
            return "[]";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Operators.ARRAY_START_STR);
        for (int i = 0; i < size; i++) {
            stringBuffer.append("'" + list.get(i).width + "*" + list.get(i).height + "'");
            if (i != size - 1) {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append(Operators.ARRAY_END_STR);
        return stringBuffer.toString();
    }
}
