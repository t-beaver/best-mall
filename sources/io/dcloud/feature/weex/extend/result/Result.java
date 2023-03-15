package io.dcloud.feature.weex.extend.result;

import android.util.Pair;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXImage;

public abstract class Result {
    public IError cause;
    public JSONObject data;
    public int errCode;
    public String errMsg;
    public String errSubject;

    public interface IError {
        JSONObject toJsonObject();
    }

    public static class SourceError implements IError {
        public static final int EMPTY_CODE = -999999;
        public SourceError cause;
        public int code;
        public String message;
        public String subject;

        public SourceError(String str, int i, String str2, SourceError sourceError) {
            this.subject = str;
            this.code = i;
            this.message = str2;
            this.cause = sourceError;
        }

        public JSONObject toJsonObject() {
            JSONObject jSONObject = new JSONObject();
            String str = this.subject;
            if (str != null) {
                jSONObject.put("subject", (Object) str);
            }
            int i = this.code;
            if (i != -999999) {
                jSONObject.put("code", (Object) Integer.valueOf(i));
            }
            String str2 = this.message;
            if (str2 != null) {
                jSONObject.put("message", (Object) str2);
            }
            SourceError sourceError = this.cause;
            if (sourceError != null) {
                jSONObject.put("cause", (Object) sourceError.toJsonObject());
            }
            return jSONObject;
        }
    }

    public static class AggregateError implements IError {
        public String[] errors;
        public String message;
        public String name = "AggregateError";

        public AggregateError(String str, String[] strArr, String str2) {
            this.name = str;
            this.errors = strArr;
            this.message = str2;
        }

        public JSONObject toJsonObject() {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", (Object) this.name);
            if (this.errors != null) {
                StringBuilder sb = new StringBuilder(Operators.ARRAY_START_STR);
                for (String append : this.errors) {
                    sb.append(append);
                }
                sb.append(Operators.ARRAY_END_STR);
                jSONObject.put("errors", (Object) sb);
            }
            String str = this.message;
            if (str != null) {
                jSONObject.put("message", (Object) str);
            }
            return jSONObject;
        }
    }

    public Result(String str, int i, String str2) {
        this.errSubject = str;
        this.errCode = i;
        this.errMsg = str2;
    }

    @SafeVarargs
    public static JSONObject boxCallBackResult(boolean z, Pair<String, Object>... pairArr) {
        JSONObject jSONObject = new JSONObject();
        if (z) {
            jSONObject.put("type", (Object) WXImage.SUCCEED);
        } else {
            jSONObject.put("type", (Object) Constants.Event.FAIL);
        }
        for (Pair<String, Object> pair : pairArr) {
            if (pair != null) {
                jSONObject.put((String) pair.first, pair.second);
            }
        }
        return jSONObject;
    }

    public static JSONObject boxSuccessResult(Object obj) {
        return boxCallBackResult(true, new Pair("data", obj));
    }

    public static JSONObject boxFailResult(Result result) {
        Pair pair = new Pair("errSubject", result.errSubject);
        Pair pair2 = new Pair(IWXUserTrackAdapter.MONITOR_ERROR_CODE, Integer.valueOf(result.errCode));
        Pair pair3 = new Pair(IWXUserTrackAdapter.MONITOR_ERROR_MSG, result.errMsg);
        Pair pair4 = null;
        Pair pair5 = result.cause != null ? new Pair("cause", result.cause.toJsonObject()) : null;
        if (result.data != null) {
            pair4 = new Pair("data", result.data);
        }
        return boxCallBackResult(false, pair, pair2, pair3, pair5, pair4);
    }
}
