package io.dcloud.feature.weex.config;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import com.taobao.weex.WXSDKInstance;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;

public class MimeInfoParser {
    final String DATA_FLAG = "data:";
    final String ENCODE_FORMAT_FLAG = ",";
    final String FILE_FORMAT_FLAG = ";";
    final String FILE_TYPE_FLAG = "/";
    HashSet<String> registerEncodeFormatSet = new HashSet<>();
    HashSet<String> registerFileFormatSet = new HashSet<>();
    HashSet<String> registerFileTypeSet = new HashSet<>();

    public static MimeInfoParser getInstance() {
        return new MimeInfoParser();
    }

    private MimeInfoParser() {
        this.registerFileTypeSet.add("keystore");
        this.registerFileTypeSet.add("cert");
        this.registerFileFormatSet.add("p12");
        this.registerFileFormatSet.add("pem");
        this.registerEncodeFormatSet.add("filepath");
        this.registerEncodeFormatSet.add("base64");
        this.registerEncodeFormatSet.add("text");
    }

    public static InputStream getFilePathStream(WXSDKInstance wXSDKInstance, String str) {
        if (wXSDKInstance != null) {
            try {
                File file = new File(new URI(wXSDKInstance.getURIAdapter().rewrite(wXSDKInstance, "file", Uri.parse(str)).toString()));
                if (file.exists()) {
                    return new FileInputStream(file);
                }
                File file2 = new File(UniPathParser.getAndroidPath(str));
                if (file2.exists()) {
                    return new FileInputStream(file2);
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static class MimeInfo {
        /* access modifiers changed from: private */
        public String dataContent;
        /* access modifiers changed from: private */
        public String encodeFormat;
        /* access modifiers changed from: private */
        public String fileFormat;
        /* access modifiers changed from: private */
        public String fileType;

        public InputStream getDataBytes(WXSDKInstance wXSDKInstance) {
            if ("base64".equals(this.encodeFormat)) {
                return new ByteArrayInputStream(Base64.decode(this.dataContent, 2));
            }
            if ("text".equals(this.encodeFormat)) {
                return new ByteArrayInputStream(this.dataContent.getBytes());
            }
            if (!"filepath".equals(this.encodeFormat) || wXSDKInstance == null) {
                return null;
            }
            try {
                return new FileInputStream(new File(new URI(wXSDKInstance.getURIAdapter().rewrite(wXSDKInstance, "file", Uri.parse(this.dataContent)).toString())));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: private */
        public boolean isValid(MimeInfoParser mimeInfoParser) {
            if (TextUtils.isEmpty(this.fileType) || TextUtils.isEmpty(this.fileFormat) || TextUtils.isEmpty(this.encodeFormat) || TextUtils.isEmpty(this.dataContent) || !mimeInfoParser.registerFileTypeSet.contains(this.fileType) || !mimeInfoParser.registerFileFormatSet.contains(this.fileFormat) || !mimeInfoParser.registerEncodeFormatSet.contains(this.encodeFormat)) {
                return false;
            }
            return true;
        }

        public String getFileType() {
            return this.fileType;
        }

        public void setFileType(String str) {
            this.fileType = str;
        }

        public String getFileFormat() {
            return this.fileFormat;
        }

        public void setFileFormat(String str) {
            this.fileFormat = str;
        }

        public String getEncodeFormat() {
            return this.encodeFormat;
        }

        public void setEncodeFormat(String str) {
            this.encodeFormat = str;
        }

        public String getDataContent() {
            return this.dataContent;
        }

        public void setDataContent(String str) {
            this.dataContent = str;
        }
    }

    public MimeInfo obtainMimeInfo(String str) {
        MimeInfo parseMimeInfo = parseMimeInfo(str);
        if (parseMimeInfo == null || parseMimeInfo.isValid(this)) {
            return parseMimeInfo;
        }
        return null;
    }

    private MimeInfo parseMimeInfo(String str) {
        MimeInfo mimeInfo = new MimeInfo();
        if (!str.startsWith("data:") || !str.contains("/") || !str.contains(";") || !str.contains(",")) {
            return null;
        }
        String substring = str.substring(5);
        String substring2 = substring.substring(0, substring.indexOf("/"));
        String unused = mimeInfo.fileType = substring2;
        String substring3 = substring.substring((substring2 + "/").length());
        String substring4 = substring3.substring(0, substring3.indexOf(";"));
        String unused2 = mimeInfo.fileFormat = substring4;
        String substring5 = substring3.substring((substring4 + ";").length());
        String substring6 = substring5.substring(0, substring5.indexOf(","));
        String unused3 = mimeInfo.encodeFormat = substring6;
        String unused4 = mimeInfo.dataContent = substring5.substring((substring6 + ",").length());
        return mimeInfo;
    }
}
