package io.dcloud.net;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.net.NetWorkLoop;
import io.dcloud.common.util.net.RequestData;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class XMLHttpRequestMgr {
    private NetWorkLoop mNetWorkLoop;
    public HashMap<String, ArrayList<XMLHttpRequest>> mXMLHttpRequests;

    public XMLHttpRequestMgr() {
        this.mXMLHttpRequests = null;
        this.mNetWorkLoop = new NetWorkLoop();
        this.mXMLHttpRequests = new HashMap<>();
        this.mNetWorkLoop.startThreadPool();
    }

    private XMLHttpRequest findXMLHttpRequest(String str, String str2) {
        ArrayList arrayList = this.mXMLHttpRequests.get(str);
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                XMLHttpRequest xMLHttpRequest = (XMLHttpRequest) arrayList.get(i);
                if (str2.equals(xMLHttpRequest.mUUID)) {
                    return xMLHttpRequest;
                }
            }
        }
        return null;
    }

    private void pushXMLHttpRequest(String str, XMLHttpRequest xMLHttpRequest) {
        ArrayList arrayList = this.mXMLHttpRequests.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.mXMLHttpRequests.put(str, arrayList);
        }
        arrayList.add(xMLHttpRequest);
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        String obtainAppId = iWebview.obtainFrameView().obtainApp().obtainAppId();
        if ("send".equals(str)) {
            XMLHttpRequest findXMLHttpRequest = findXMLHttpRequest(obtainAppId, strArr[0]);
            findXMLHttpRequest.setCallbackId(strArr[1]);
            String str2 = strArr[2];
            RequestData requestData = findXMLHttpRequest.getRequestData();
            if (!requestData.getReqmethod().equalsIgnoreCase("get")) {
                requestData.addBody(str2);
            }
            try {
                JSONObject jSONObject = new JSONObject(strArr[3]);
                JSONArray names = jSONObject.names();
                if (names != null && names.length() > 0) {
                    for (int i = 0; i < names.length(); i++) {
                        String optString = names.optString(i);
                        requestData.addHeader(optString, jSONObject.optString(optString));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.mNetWorkLoop.addNetWork(findXMLHttpRequest.getNetWork());
            return null;
        } else if ("open".equals(str)) {
            String str3 = strArr[0];
            String str4 = strArr[1];
            String str5 = strArr[2];
            String str6 = strArr[3];
            String str7 = strArr[4];
            XMLHttpRequest xMLHttpRequest = new XMLHttpRequest(str3, str5, str4, iWebview);
            RequestData requestData2 = xMLHttpRequest.getRequestData();
            requestData2.mTimeout = PdrUtil.parseInt(strArr[5], requestData2.mTimeout);
            requestData2.addHeader(str6, str7);
            pushXMLHttpRequest(obtainAppId, xMLHttpRequest);
            return null;
        } else if ("overrideMimeType".equals(str)) {
            XMLHttpRequest findXMLHttpRequest2 = findXMLHttpRequest(obtainAppId, strArr[0]);
            if (findXMLHttpRequest2 == null) {
                return null;
            }
            findXMLHttpRequest2.getRequestData().mOverrideMimeType = strArr[1];
            return null;
        } else if (!"abort".equals(str)) {
            return null;
        } else {
            this.mNetWorkLoop.removeNetWork(findXMLHttpRequest(obtainAppId, strArr[0]).getNetWork());
            return null;
        }
    }
}
