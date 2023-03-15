package dc.squareup.okhttp3.internal.http;

import dc.squareup.okhttp3.Cookie;
import dc.squareup.okhttp3.CookieJar;
import dc.squareup.okhttp3.Headers;
import dc.squareup.okhttp3.Interceptor;
import dc.squareup.okhttp3.MediaType;
import dc.squareup.okhttp3.Request;
import dc.squareup.okhttp3.RequestBody;
import dc.squareup.okhttp3.Response;
import dc.squareup.okhttp3.internal.Util;
import dc.squareup.okhttp3.internal.Version;
import dc.squareup.okio.GzipSource;
import dc.squareup.okio.Okio;
import dc.squareup.okio.Source;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.net.NetWork;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class BridgeInterceptor implements Interceptor {
    private static final Comparator<String> FIELD_NAME_COMPARATOR = new Comparator<String>() {
        public int compare(String str, String str2) {
            if (str == str2) {
                return 0;
            }
            if (str == null) {
                return -1;
            }
            if (str2 == null) {
                return 1;
            }
            return String.CASE_INSENSITIVE_ORDER.compare(str, str2);
        }
    };
    private final CookieJar cookieJar;

    public BridgeInterceptor(CookieJar cookieJar2) {
        this.cookieJar = cookieJar2;
    }

    public static void addCookies(Request.Builder builder, Map<String, List<String>> map) {
        for (Map.Entry next : map.entrySet()) {
            String str = (String) next.getKey();
            if ((IWebview.COOKIE.equalsIgnoreCase(str) || "Cookie2".equalsIgnoreCase(str)) && !((List) next.getValue()).isEmpty()) {
                builder.addHeader(str, buildCookieHeader((List) next.getValue()));
            }
        }
    }

    public static String buildCookieHeader(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append("; ");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private String cookieHeader(List<Cookie> list) {
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append("; ");
            }
            Cookie cookie = list.get(i);
            sb.append(cookie.name());
            sb.append('=');
            sb.append(cookie.value());
        }
        return sb.toString();
    }

    public static Map<String, List<String>> toMultimap(Headers headers, String str) {
        TreeMap treeMap = new TreeMap(FIELD_NAME_COMPARATOR);
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            ArrayList arrayList = new ArrayList();
            List list = (List) treeMap.get(name);
            if (list != null) {
                arrayList.addAll(list);
            }
            arrayList.add(value);
            treeMap.put(name, Collections.unmodifiableList(arrayList));
        }
        if (str != null) {
            treeMap.put((Object) null, Collections.unmodifiableList(Collections.singletonList(str)));
        }
        return Collections.unmodifiableMap(treeMap);
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder newBuilder = request.newBuilder();
        RequestBody body = request.body();
        if (body != null) {
            MediaType contentType = body.contentType();
            if (contentType != null) {
                newBuilder.header(NetWork.CONTENT_TYPE, contentType.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                newBuilder.header("Content-Length", Long.toString(contentLength));
                newBuilder.removeHeader("Transfer-Encoding");
            } else {
                newBuilder.header("Transfer-Encoding", "chunked");
                newBuilder.removeHeader("Content-Length");
            }
        }
        boolean z = false;
        if (request.header("Host") == null) {
            newBuilder.header("Host", Util.hostHeader(request.url(), false));
        }
        if (request.header("Connection") == null) {
            newBuilder.header("Connection", "Keep-Alive");
        }
        if (request.header("Accept-Encoding") == null && request.header("Range") == null) {
            z = true;
            newBuilder.header("Accept-Encoding", "gzip");
        }
        String header = request.header("cookie");
        if (header == null) {
            CookieHandler cookieHandler = CookieHandler.getDefault();
            if (cookieHandler != null) {
                try {
                    addCookies(newBuilder, cookieHandler.get(request.url().uri(), toMultimap(request.headers(), (String) null)));
                } catch (Exception unused) {
                }
            }
        } else {
            newBuilder.header(IWebview.COOKIE, header);
        }
        if (request.header(IWebview.USER_AGENT) == null) {
            newBuilder.header(IWebview.USER_AGENT, Version.userAgent());
        }
        Response proceed = chain.proceed(newBuilder.build());
        try {
            CookieHandler cookieHandler2 = CookieHandler.getDefault();
            if (cookieHandler2 != null) {
                cookieHandler2.put(request.url().uri(), toMultimap(proceed.headers(), (String) null));
            }
        } catch (Exception unused2) {
        }
        HttpHeaders.receiveHeaders(this.cookieJar, request.url(), proceed.headers());
        Response.Builder request2 = proceed.newBuilder().request(request);
        if (z && "gzip".equalsIgnoreCase(proceed.header("Content-Encoding")) && HttpHeaders.hasBody(proceed)) {
            GzipSource gzipSource = new GzipSource(proceed.body().source());
            request2.headers(proceed.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build());
            request2.body(new RealResponseBody(proceed.header(NetWork.CONTENT_TYPE), -1, Okio.buffer((Source) gzipSource)));
        }
        return request2.build();
    }
}
