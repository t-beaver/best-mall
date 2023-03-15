package androidtranscoder;

import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import androidtranscoder.MediaTranscoder;
import androidtranscoder.format.MediaFormatStrategyPresets;
import com.taobao.weex.common.Constants;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoCompressor {
    private static VideoCompressor instance;

    private VideoCompressor() {
    }

    public static void callBackError(IWebview iWebview, String str, String str2, int i) {
        Deprecated_JSUtil.execCallback(iWebview, str, DOMException.toJSON(i, str2), JSUtil.ERROR, true, false);
    }

    public static void callBackSuccess(IWebview iWebview, String str, String str2) {
        Deprecated_JSUtil.execCallback(iWebview, str, str2, JSUtil.OK, true, false);
    }

    public static VideoCompressor getInstance() {
        if (instance == null) {
            synchronized (VideoCompressor.class) {
                if (instance == null) {
                    instance = new VideoCompressor();
                }
            }
        }
        return instance;
    }

    public void compressVideo(final IWebview iWebview, String[] strArr) {
        final String str;
        int i = 1;
        final String str2 = strArr[1];
        try {
            JSONObject jSONObject = new JSONObject(strArr[0]);
            String optString = jSONObject.optString(AbsoluteConst.JSON_KEY_FILENAME);
            if (TextUtils.isEmpty(optString)) {
                String obtainAppTempPath = iWebview.obtainApp().obtainAppTempPath();
                File file = new File(obtainAppTempPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                str = obtainAppTempPath + "/compress_video_" + SystemClock.elapsedRealtime() + ".mp4";
            } else if (optString.endsWith("/")) {
                String convert2WebviewFullPath = iWebview.obtainFrameView().obtainApp().convert2WebviewFullPath(iWebview.obtainFullUrl(), optString);
                if (convert2WebviewFullPath.startsWith("file:")) {
                    convert2WebviewFullPath = Uri.parse(convert2WebviewFullPath).getPath();
                }
                File file2 = new File(convert2WebviewFullPath);
                if (!file2.exists()) {
                    file2.mkdirs();
                } else if (file2.exists() && !file2.isDirectory()) {
                    file2.delete();
                    file2.mkdirs();
                }
                str = file2.getPath() + "/compress_video_" + SystemClock.elapsedRealtime() + ".mp4";
            } else {
                String convert2WebviewFullPath2 = iWebview.obtainFrameView().obtainApp().convert2WebviewFullPath(iWebview.obtainFullUrl(), optString);
                if (convert2WebviewFullPath2.startsWith("file:")) {
                    convert2WebviewFullPath2 = Uri.parse(convert2WebviewFullPath2).getPath();
                }
                File file3 = new File(convert2WebviewFullPath2);
                if (!file3.getParentFile().exists()) {
                    file3.getParentFile().mkdirs();
                }
                str = file3.getPath();
            }
            String convert2AbsFullPath = iWebview.obtainApp().convert2AbsFullPath(jSONObject.optString("src"));
            String optString2 = jSONObject.optString(Constants.Name.QUALITY);
            double d = 1.0d;
            double optDouble = jSONObject.optDouble("resolution", 1.0d);
            if (optDouble > 0.0d && optDouble < 1.0d) {
                d = optDouble;
            }
            if ("low".equalsIgnoreCase(optString2)) {
                i = 3;
            } else if ("medium".equalsIgnoreCase(optString2)) {
                i = 2;
            } else {
                boolean equalsIgnoreCase = "high".equalsIgnoreCase(optString2);
            }
            try {
                MediaTranscoder.getInstance().transcodeVideo(convert2AbsFullPath, str, MediaFormatStrategyPresets.createAndroid720pStrategy(i, d), (MediaTranscoder.Listener) new MediaTranscoder.Listener() {
                    public void onTranscodeCanceled() {
                        VideoCompressor.callBackError(iWebview, str2, DOMException.MSG_USER_CANCEL, -2);
                    }

                    public void onTranscodeCompleted() {
                        File file = new File(str);
                        if (file.exists()) {
                            com.alibaba.fastjson.JSONObject jSONObject = new com.alibaba.fastjson.JSONObject();
                            jSONObject.put("tempFilePath", (Object) Uri.fromFile(file).toString());
                            jSONObject.put(AbsoluteConst.JSON_KEY_SIZE, (Object) Long.valueOf(file.length()));
                            VideoCompressor.callBackSuccess(iWebview, str2, jSONObject.toJSONString());
                            return;
                        }
                        VideoCompressor.callBackError(iWebview, str2, DOMException.MSG_IO_ERROR, -5);
                    }

                    public void onTranscodeFailed(Exception exc) {
                        VideoCompressor.callBackError(iWebview, str2, exc.getMessage(), -99);
                    }

                    public void onTranscodeProgress(double d) {
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                callBackError(iWebview, str2, e.getMessage(), -99);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            callBackError(iWebview, str2, DOMException.MSG_PARAMETER_ERROR, -1);
        }
    }
}
