package dc.squareup.okhttp3.internal.tls;

import com.taobao.weex.el.parse.Operators;
import dc.squareup.okhttp3.internal.Util;
import io.dcloud.common.util.PdrUtil;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public final class OkHostnameVerifier implements HostnameVerifier {
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();

    private OkHostnameVerifier() {
    }

    public static List<String> allSubjectAltNames(X509Certificate x509Certificate) {
        List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        List<String> subjectAltNames2 = getSubjectAltNames(x509Certificate, 2);
        ArrayList arrayList = new ArrayList(subjectAltNames.size() + subjectAltNames2.size());
        arrayList.addAll(subjectAltNames);
        arrayList.addAll(subjectAltNames2);
        return arrayList;
    }

    private static List<String> getSubjectAltNames(X509Certificate x509Certificate, int i) {
        String str;
        ArrayList arrayList = new ArrayList();
        try {
            Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames == null) {
                return Collections.emptyList();
            }
            for (List next : subjectAlternativeNames) {
                if (next != null) {
                    if (next.size() >= 2) {
                        Integer num = (Integer) next.get(0);
                        if (num != null) {
                            if (num.intValue() == i && (str = (String) next.get(1)) != null) {
                                arrayList.add(str);
                            }
                        }
                    }
                }
            }
            return arrayList;
        } catch (CertificateParsingException unused) {
            return Collections.emptyList();
        }
    }

    private boolean verifyHostname(String str, X509Certificate x509Certificate) {
        String lowerCase = str.toLowerCase(Locale.US);
        for (String verifyHostname : getSubjectAltNames(x509Certificate, 2)) {
            if (verifyHostname(lowerCase, verifyHostname)) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyIpAddress(String str, X509Certificate x509Certificate) {
        List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        int size = subjectAltNames.size();
        for (int i = 0; i < size; i++) {
            if (str.equalsIgnoreCase(subjectAltNames.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean verify(String str, SSLSession sSLSession) {
        try {
            return verify(str, (X509Certificate) sSLSession.getPeerCertificates()[0]);
        } catch (SSLException unused) {
            return false;
        }
    }

    public boolean verify(String str, X509Certificate x509Certificate) {
        if (Util.verifyAsIpAddress(str)) {
            return verifyIpAddress(str, x509Certificate);
        }
        return verifyHostname(str, x509Certificate);
    }

    public boolean verifyHostname(String str, String str2) {
        if (str != null && str.length() != 0 && !str.startsWith(Operators.DOT_STR) && !str.endsWith(PdrUtil.FILE_PATH_ENTRY_BACK) && str2 != null && str2.length() != 0 && !str2.startsWith(Operators.DOT_STR) && !str2.endsWith(PdrUtil.FILE_PATH_ENTRY_BACK)) {
            if (!str.endsWith(Operators.DOT_STR)) {
                str = str + Operators.DOT;
            }
            if (!str2.endsWith(Operators.DOT_STR)) {
                str2 = str2 + Operators.DOT;
            }
            String lowerCase = str2.toLowerCase(Locale.US);
            if (!lowerCase.contains("*")) {
                return str.equals(lowerCase);
            }
            if (!lowerCase.startsWith("*.") || lowerCase.indexOf(42, 1) != -1 || str.length() < lowerCase.length() || "*.".equals(lowerCase)) {
                return false;
            }
            String substring = lowerCase.substring(1);
            if (!str.endsWith(substring)) {
                return false;
            }
            int length = str.length() - substring.length();
            if (length <= 0 || str.lastIndexOf(46, length - 1) == -1) {
                return true;
            }
            return false;
        }
        return false;
    }
}
