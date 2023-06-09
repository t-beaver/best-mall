package com.taobao.weex.http;

import com.taobao.weex.ui.module.WXModalUIModule;
import java.util.HashMap;
import java.util.Map;

public class Status {
    public static final String ERR_CONNECT_FAILED = "ERR_CONNECT_FAILED";
    public static final String ERR_INVALID_REQUEST = "ERR_INVALID_REQUEST";
    public static final String UNKNOWN_STATUS = "unknown status";
    private static Map<String, String> statusMap;

    static {
        HashMap hashMap = new HashMap();
        statusMap = hashMap;
        hashMap.put("100", "Continue");
        statusMap.put("101", "Switching Protocol");
        statusMap.put("200", WXModalUIModule.OK);
        statusMap.put("201", "Created");
        statusMap.put("202", "Accepted");
        statusMap.put("203", "Non-Authoritative Information");
        statusMap.put("204", "No Content");
        statusMap.put("205", "Reset Content");
        statusMap.put("206", "Partial Content");
        statusMap.put("300", "Multiple Choice");
        statusMap.put("301", "Moved Permanently");
        statusMap.put("302", "Found");
        statusMap.put("303", "See Other");
        statusMap.put("304", "Not Modified");
        statusMap.put("305", "Use Proxy");
        statusMap.put("306", "unused");
        statusMap.put("307", "Temporary Redirect");
        statusMap.put("308", "Permanent Redirect");
        statusMap.put("400", "Bad Request");
        statusMap.put("401", "Unauthorized");
        statusMap.put("402", "Payment Required");
        statusMap.put("403", "Forbidden");
        statusMap.put("404", "Not Found");
        statusMap.put("405", "Method Not Allowed");
        statusMap.put("406", "Not Acceptable");
        statusMap.put("407", "Proxy Authentication Required");
        statusMap.put("408", "Request Timeout");
        statusMap.put("409", "Conflict");
        statusMap.put("410", "Gone");
        statusMap.put("411", "Length Required");
        statusMap.put("412", "Precondition Failed");
        statusMap.put("413", "Payload Too Large");
        statusMap.put("414", "URI Too Long");
        statusMap.put("415", "Unsupported Media Type");
        statusMap.put("416", "Requested Range Not Satisfiable");
        statusMap.put("417", "Expectation Failed");
        statusMap.put("418", "I'm a teapot");
        statusMap.put("421", "Misdirected Request");
        statusMap.put("426", "Upgrade Required");
        statusMap.put("428", "Precondition Required");
        statusMap.put("429", "Too Many Requests");
        statusMap.put("431", "Request Header Fields Too Large");
        statusMap.put("500", "Internal Server Error");
        statusMap.put("501", "Not Implemented");
        statusMap.put("502", "Bad Gateway");
        statusMap.put("503", "Service Unavailable");
        statusMap.put("504", "Gateway Timeout");
        statusMap.put("505", "HTTP Version Not Supported");
        statusMap.put("506", "Variant Also Negotiates");
        statusMap.put("507", "Variant Also Negotiates");
        statusMap.put("511", "Network Authentication Required");
    }

    public static String getStatusText(String str) {
        if (!statusMap.containsKey(str)) {
            return UNKNOWN_STATUS;
        }
        return statusMap.get(str);
    }
}
