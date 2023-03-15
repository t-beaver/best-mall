package com.taobao.weex.http;

import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.http.Options;
import com.taobao.weex.ui.component.WXBasicComponentType;
import com.taobao.weex.utils.WXLogUtils;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.net.NetWork;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class WXStreamModule extends WXModule {
    static final Pattern CHARSET_PATTERN = Pattern.compile("charset=([a-z0-9-]+)");
    public static final String STATUS = "status";
    public static final String STATUS_TEXT = "statusText";
    public static HashMap<String, CertDTO> certMap = new HashMap<>();
    final IWXHttpAdapter mAdapter;

    private interface ResponseCallback {
        void onResponse(WXResponse wXResponse, Map<String, String> map);
    }

    public WXStreamModule() {
        this((IWXHttpAdapter) null);
    }

    public WXStreamModule(IWXHttpAdapter iWXHttpAdapter) {
        this.mAdapter = iWXHttpAdapter;
    }

    @JSMethod(uiThread = false)
    @Deprecated
    public void sendHttp(JSONObject jSONObject, final String str) {
        String string = jSONObject.getString("method");
        String string2 = jSONObject.getString("url");
        JSONObject jSONObject2 = jSONObject.getJSONObject(WXBasicComponentType.HEADER);
        String string3 = jSONObject.getString("body");
        int intValue = jSONObject.getIntValue("timeout");
        boolean booleanValue = jSONObject.getBooleanValue("sslVerify");
        boolean booleanValue2 = jSONObject.getBooleanValue("firstIpv4");
        JSONObject jSONObject3 = jSONObject.getJSONObject("tls");
        if (string != null) {
            string = string.toUpperCase();
        }
        Options.Builder builder = new Options.Builder();
        if (!"GET".equals(string) && !"POST".equals(string) && !"PUT".equals(string) && !"DELETE".equals(string) && !"HEAD".equals(string) && !"PATCH".equals(string)) {
            string = "GET";
        }
        Options.Builder firstIpv4 = builder.setMethod(string).setUrl(string2).setBody(string3).setTimeout(intValue).setSslVerify(booleanValue).setAndroidTlsConfig(jSONObject3).setFirstIpv4(booleanValue2);
        extractHeaders(jSONObject2, firstIpv4);
        final Options createOptions = firstIpv4.createOptions();
        sendRequest(firstIpv4.createOptions(), new ResponseCallback() {
            public void onResponse(WXResponse wXResponse, Map<String, String> map) {
                String str;
                if (str != null && WXStreamModule.this.mWXSDKInstance != null) {
                    WXBridgeManager instance = WXBridgeManager.getInstance();
                    String instanceId = WXStreamModule.this.mWXSDKInstance.getInstanceId();
                    String str2 = str;
                    if (wXResponse == null || wXResponse.originalData == null) {
                        str = "{}";
                    } else {
                        str = WXStreamModule.readAsString(wXResponse.originalData, map != null ? WXStreamModule.getHeader(map, NetWork.CONTENT_TYPE) : "", createOptions.getType());
                    }
                    instance.callback(instanceId, str2, str);
                }
            }
        }, (JSCallback) null, this.mWXSDKInstance.getInstanceId(), this.mWXSDKInstance.getBundleUrl());
    }

    @JSMethod(uiThread = false)
    public void fetch(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        fetch(jSONObject, jSCallback, jSCallback2, this.mWXSDKInstance.getInstanceId(), this.mWXSDKInstance.getBundleUrl());
    }

    @JSMethod(uiThread = false)
    public void fetchWithArrayBuffer(JSONObject jSONObject, JSONObject jSONObject2, JSCallback jSCallback, JSCallback jSCallback2) {
        if (jSONObject != null) {
            String string = jSONObject.getString("@type");
            if (!TextUtils.isEmpty(string) && "binary".equalsIgnoreCase(string)) {
                String string2 = jSONObject.getString("base64");
                if (!TextUtils.isEmpty(string2)) {
                    jSONObject2.put("inputType", (Object) "base64");
                    jSONObject2.put("body", (Object) string2);
                    fetch(jSONObject2, jSCallback, jSCallback2, this.mWXSDKInstance.getInstanceId(), this.mWXSDKInstance.getBundleUrl());
                } else if (jSCallback != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("ok", false);
                    hashMap.put(STATUS_TEXT, Status.ERR_INVALID_REQUEST);
                    jSCallback.invoke(hashMap);
                }
            } else if (jSCallback != null) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("ok", false);
                hashMap2.put(STATUS_TEXT, Status.ERR_INVALID_REQUEST);
                jSCallback.invoke(hashMap2);
            }
        } else if (jSCallback != null) {
            HashMap hashMap3 = new HashMap();
            hashMap3.put("ok", false);
            hashMap3.put(STATUS_TEXT, Status.ERR_INVALID_REQUEST);
            jSCallback.invoke(hashMap3);
        }
    }

    @JSMethod(uiThread = false)
    public void configMTLS(JSONArray jSONArray, JSCallback jSCallback) {
        if (jSCallback != null) {
            if (jSONArray == null || jSONArray.isEmpty()) {
                jSCallback.invoke(CertJSResponse.obtainFail(-1, DOMException.MSG_PARAMETER_ERROR));
                return;
            }
            certMap.clear();
            for (int i = 0; i < jSONArray.size(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                if (jSONObject.containsKey("host")) {
                    CertDTO certDTO = new CertDTO();
                    String string = jSONObject.getString("host");
                    certDTO.host = string;
                    certDTO.client = jSONObject.getString("client");
                    certDTO.clientPassword = jSONObject.getString("clientPassword");
                    certDTO.server = (String[]) jSONObject.getJSONArray("server").toArray(new String[0]);
                    certMap.put(string, certDTO);
                }
            }
            jSCallback.invoke(CertJSResponse.obtainSuccess());
        }
    }

    public void fetch(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2, String str, String str2) {
        JSONObject jSONObject2 = jSONObject;
        final JSCallback jSCallback3 = jSCallback;
        if (!(jSONObject2 == null || jSONObject2.getString("url") == null)) {
            String string = jSONObject2.getString("method");
            String string2 = jSONObject2.getString("url");
            JSONObject jSONObject3 = jSONObject2.getJSONObject("headers");
            String string3 = jSONObject2.getString("body");
            String string4 = jSONObject2.getString("type");
            String string5 = jSONObject2.getString("inputType");
            int intValue = jSONObject2.getIntValue("timeout");
            JSONObject jSONObject4 = jSONObject2.getJSONObject("tls");
            boolean booleanValue = jSONObject2.getBooleanValue("sslVerify");
            boolean booleanValue2 = jSONObject2.getBooleanValue("firstIpv4");
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (!(sDKInstance == null || sDKInstance.getStreamNetworkHandler() == null)) {
                String fetchLocal = sDKInstance.getStreamNetworkHandler().fetchLocal(string2);
                if (!TextUtils.isEmpty(fetchLocal)) {
                    string2 = fetchLocal;
                }
            }
            if (string != null) {
                string = string.toUpperCase();
            }
            Options.Builder builder = new Options.Builder();
            if (!"GET".equals(string) && !"POST".equals(string) && !"PUT".equals(string) && !"DELETE".equals(string) && !"HEAD".equals(string) && !"PATCH".equals(string)) {
                string = "GET";
            }
            Options.Builder firstIpv4 = builder.setMethod(string).setUrl(string2).setBody(string3).setType(string4).setInputTypes(string5).setTimeout(intValue).setSslVerify(booleanValue).setAndroidTlsConfig(jSONObject4).setFirstIpv4(booleanValue2);
            extractHeaders(jSONObject3, firstIpv4);
            final Options createOptions = firstIpv4.createOptions();
            sendRequest(createOptions, new ResponseCallback() {
                public void onResponse(WXResponse wXResponse, Map<String, String> map) {
                    if (jSCallback3 != null) {
                        HashMap hashMap = new HashMap();
                        if (wXResponse == null || "-1".equals(wXResponse.statusCode)) {
                            hashMap.put("status", -1);
                            hashMap.put(WXStreamModule.STATUS_TEXT, Status.ERR_CONNECT_FAILED);
                            if (wXResponse != null) {
                                hashMap.put("errorMsg", wXResponse.errorMsg);
                            } else {
                                hashMap.put("errorMsg", "response 为空");
                            }
                        } else {
                            int parseInt = Integer.parseInt(wXResponse.statusCode);
                            hashMap.put("status", Integer.valueOf(parseInt));
                            hashMap.put("ok", Boolean.valueOf(parseInt >= 200 && parseInt <= 299));
                            if (wXResponse.originalData == null) {
                                hashMap.put("data", wXResponse.errorMsg);
                            } else {
                                try {
                                    hashMap.put("data", WXStreamModule.this.parseData(WXStreamModule.readAsString(wXResponse.originalData, map != null ? WXStreamModule.getHeader(map, NetWork.CONTENT_TYPE) : "", createOptions.getType()), createOptions.getType()));
                                } catch (JSONException e) {
                                    WXLogUtils.e("", (Throwable) e);
                                    hashMap.put("ok", false);
                                    hashMap.put("data", "{'err':'Data parse failed!'}");
                                }
                            }
                            hashMap.put(WXStreamModule.STATUS_TEXT, Status.getStatusText(wXResponse.statusCode));
                        }
                        hashMap.put("headers", map);
                        jSCallback3.invoke(hashMap);
                    }
                }
            }, jSCallback2, str, str2);
        } else if (jSCallback3 != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("ok", false);
            hashMap.put(STATUS_TEXT, Status.ERR_INVALID_REQUEST);
            jSCallback3.invoke(hashMap);
        }
    }

    /* access modifiers changed from: package-private */
    public Object parseData(String str, Options.Type type) throws JSONException {
        if (type == Options.Type.json) {
            return JSONObject.parse(str);
        }
        if (type != Options.Type.jsonp) {
            return str;
        }
        if (str == null || str.isEmpty()) {
            return new JSONObject();
        }
        int indexOf = str.indexOf(Operators.BRACKET_START_STR) + 1;
        int lastIndexOf = str.lastIndexOf(Operators.BRACKET_END_STR);
        if (indexOf == 0 || indexOf >= lastIndexOf || lastIndexOf <= 0) {
            return new JSONObject();
        }
        return JSONObject.parse(str.substring(indexOf, lastIndexOf));
    }

    static String getHeader(Map<String, String> map, String str) {
        if (map == null || str == null) {
            return null;
        }
        if (map.containsKey(str)) {
            return map.get(str);
        }
        return map.get(str.toLowerCase(Locale.ENGLISH));
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0026 A[Catch:{ Exception -> 0x002c }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0020 A[Catch:{ Exception -> 0x002c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.String readAsString(byte[] r2, java.lang.String r3, com.taobao.weex.http.Options.Type r4) {
        /*
            if (r3 == 0) goto L_0x001a
            java.util.regex.Pattern r0 = CHARSET_PATTERN
            java.util.Locale r1 = java.util.Locale.ENGLISH
            java.lang.String r3 = r3.toLowerCase(r1)
            java.util.regex.Matcher r3 = r0.matcher(r3)
            boolean r0 = r3.find()
            if (r0 == 0) goto L_0x001a
            r0 = 1
            java.lang.String r3 = r3.group(r0)
            goto L_0x001c
        L_0x001a:
            java.lang.String r3 = "utf-8"
        L_0x001c:
            com.taobao.weex.http.Options$Type r0 = com.taobao.weex.http.Options.Type.base64     // Catch:{ Exception -> 0x002c }
            if (r4 != r0) goto L_0x0026
            r3 = 2
            java.lang.String r2 = android.util.Base64.encodeToString(r2, r3)     // Catch:{ Exception -> 0x002c }
            return r2
        L_0x0026:
            java.lang.String r4 = new java.lang.String     // Catch:{ Exception -> 0x002c }
            r4.<init>(r2, r3)     // Catch:{ Exception -> 0x002c }
            return r4
        L_0x002c:
            r3 = move-exception
            java.lang.String r4 = ""
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r4, (java.lang.Throwable) r3)
            java.lang.String r3 = new java.lang.String
            r3.<init>(r2)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.http.WXStreamModule.readAsString(byte[], java.lang.String, com.taobao.weex.http.Options$Type):java.lang.String");
    }

    private void extractHeaders(JSONObject jSONObject, Options.Builder builder) {
        String assembleUserAgent = WXHttpUtil.assembleUserAgent(WXEnvironment.getApplication(), WXEnvironment.getConfig());
        if (jSONObject != null) {
            for (String next : jSONObject.keySet()) {
                if (next.equals(WXHttpUtil.KEY_USER_AGENT)) {
                    assembleUserAgent = jSONObject.getString(next);
                } else {
                    builder.putHeader(next, jSONObject.getString(next));
                }
            }
        }
        builder.putHeader(WXHttpUtil.KEY_USER_AGENT, assembleUserAgent);
    }

    private void sendRequest(Options options, ResponseCallback responseCallback, JSCallback jSCallback, String str, String str2) {
        WXRequest wXRequest = new WXRequest();
        wXRequest.method = options.getMethod();
        wXRequest.url = WXSDKManager.getInstance().getURIAdapter().rewrite(str2, AbsURIAdapter.REQUEST, Uri.parse(options.getUrl())).toString();
        wXRequest.body = options.getBody();
        wXRequest.timeoutMs = options.getTimeout();
        wXRequest.instanceId = str;
        wXRequest.sslVerify = options.getSslVerify();
        wXRequest.isFirstIpv4 = options.isFirstIpv4();
        wXRequest.tls = options.getTlsConfig();
        if ("BASE64".equalsIgnoreCase(options.getInputType())) {
            wXRequest.inputType = "BASE64";
        } else {
            wXRequest.inputType = "";
        }
        if (options.getHeaders() != null) {
            if (wXRequest.paramMap == null) {
                wXRequest.paramMap = options.getHeaders();
            } else {
                wXRequest.paramMap.putAll(options.getHeaders());
            }
        }
        IWXHttpAdapter iWXHttpAdapter = this.mAdapter;
        if (iWXHttpAdapter == null) {
            iWXHttpAdapter = WXSDKManager.getInstance().getIWXHttpAdapter();
        }
        if (iWXHttpAdapter != null) {
            iWXHttpAdapter.sendRequest(wXRequest, new StreamHttpListener(responseCallback, jSCallback));
        } else {
            WXLogUtils.e("WXStreamModule", "No HttpAdapter found,request failed.");
        }
    }

    private static class StreamHttpListener implements IWXHttpAdapter.OnHttpListener {
        private ResponseCallback mCallback;
        private JSCallback mProgressCallback;
        private Map<String, String> mRespHeaders;
        private Map<String, Object> mResponse;

        public void onHttpUploadProgress(int i) {
        }

        private StreamHttpListener(ResponseCallback responseCallback, JSCallback jSCallback) {
            this.mResponse = new HashMap();
            this.mCallback = responseCallback;
            this.mProgressCallback = jSCallback;
        }

        public void onHttpStart() {
            if (this.mProgressCallback != null) {
                this.mResponse.put("readyState", 1);
                this.mResponse.put("length", 0);
                this.mProgressCallback.invokeAndKeepAlive(new HashMap(this.mResponse));
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v13, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: java.lang.String} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onHeadersReceived(int r5, java.util.Map<java.lang.String, java.util.List<java.lang.String>> r6) {
            /*
                r4 = this;
                java.util.Map<java.lang.String, java.lang.Object> r0 = r4.mResponse
                r1 = 2
                java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
                java.lang.String r2 = "readyState"
                r0.put(r2, r1)
                java.util.Map<java.lang.String, java.lang.Object> r0 = r4.mResponse
                java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
                java.lang.String r1 = "status"
                r0.put(r1, r5)
                java.util.HashMap r5 = new java.util.HashMap
                r5.<init>()
                if (r6 == 0) goto L_0x0089
                java.util.Set r6 = r6.entrySet()
                java.util.Iterator r6 = r6.iterator()
            L_0x0026:
                boolean r0 = r6.hasNext()
                if (r0 == 0) goto L_0x0089
                java.lang.Object r0 = r6.next()
                java.util.Map$Entry r0 = (java.util.Map.Entry) r0
                java.lang.Object r1 = r0.getValue()
                java.util.List r1 = (java.util.List) r1
                int r1 = r1.size()
                if (r1 != 0) goto L_0x003f
                goto L_0x0026
            L_0x003f:
                java.lang.Object r1 = r0.getValue()
                java.util.List r1 = (java.util.List) r1
                int r1 = r1.size()
                r2 = 1
                java.lang.String r3 = "_"
                if (r1 != r2) goto L_0x006d
                java.lang.Object r1 = r0.getKey()
                if (r1 != 0) goto L_0x0055
                goto L_0x005c
            L_0x0055:
                java.lang.Object r1 = r0.getKey()
                r3 = r1
                java.lang.String r3 = (java.lang.String) r3
            L_0x005c:
                java.lang.Object r0 = r0.getValue()
                java.util.List r0 = (java.util.List) r0
                r1 = 0
                java.lang.Object r0 = r0.get(r1)
                java.lang.String r0 = (java.lang.String) r0
                r5.put(r3, r0)
                goto L_0x0026
            L_0x006d:
                java.lang.Object r1 = r0.getKey()
                if (r1 != 0) goto L_0x0074
                goto L_0x007b
            L_0x0074:
                java.lang.Object r1 = r0.getKey()
                r3 = r1
                java.lang.String r3 = (java.lang.String) r3
            L_0x007b:
                java.lang.Object r0 = r0.getValue()
                java.util.List r0 = (java.util.List) r0
                java.lang.String r0 = r0.toString()
                r5.put(r3, r0)
                goto L_0x0026
            L_0x0089:
                java.util.Map<java.lang.String, java.lang.Object> r6 = r4.mResponse
                java.lang.String r0 = "headers"
                r6.put(r0, r5)
                r4.mRespHeaders = r5
                com.taobao.weex.bridge.JSCallback r5 = r4.mProgressCallback
                if (r5 == 0) goto L_0x00a0
                java.util.HashMap r6 = new java.util.HashMap
                java.util.Map<java.lang.String, java.lang.Object> r0 = r4.mResponse
                r6.<init>(r0)
                r5.invokeAndKeepAlive(r6)
            L_0x00a0:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.http.WXStreamModule.StreamHttpListener.onHeadersReceived(int, java.util.Map):void");
        }

        public void onHttpResponseProgress(int i) {
            this.mResponse.put("length", Integer.valueOf(i));
            JSCallback jSCallback = this.mProgressCallback;
            if (jSCallback != null) {
                jSCallback.invokeAndKeepAlive(new HashMap(this.mResponse));
            }
        }

        public void onHttpFinish(WXResponse wXResponse) {
            ResponseCallback responseCallback = this.mCallback;
            if (responseCallback != null) {
                responseCallback.onResponse(wXResponse, this.mRespHeaders);
            }
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("WXStreamModule", (wXResponse == null || wXResponse.originalData == null) ? "response data is NUll!" : new String(wXResponse.originalData));
            }
        }
    }
}
