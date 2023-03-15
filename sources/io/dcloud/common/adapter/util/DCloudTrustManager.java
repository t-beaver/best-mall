package io.dcloud.common.adapter.util;

import android.text.TextUtils;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

public class DCloudTrustManager {
    private DCloudTrustManager() {
    }

    public static SecureRandom createSecureRandom() {
        return new SecureRandom();
    }

    public static X509HostnameVerifier getHostnameVerifier(boolean z) {
        if (z || (!PdrUtil.isEquals(BaseInfo.untrustedca, "refuse") && !PdrUtil.isEquals(BaseInfo.untrustedca, "warning"))) {
            return SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        }
        return SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
    }

    public static javax.net.ssl.SSLSocketFactory getSSLSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
        return getSSLSocketFactory("TLSv1");
    }

    public static javax.net.ssl.SSLSocketFactory getSSLSocketFactory(String str) throws NoSuchAlgorithmException, KeyManagementException {
        Object newInstance;
        if (DCLoudApplicationImpl.self().getContext() != null) {
            String str2 = DCLoudApplicationImpl.self().getContext().getPackageName() + ".CustomTrustMgr";
            if (PlatformUtil.checkClass(str2) && (newInstance = PlatformUtil.newInstance(str2, (Class[]) null, (Object[]) null)) != null) {
                if (TextUtils.isEmpty(str)) {
                    str = "TLSv1";
                }
                SSLContext instance = SSLContext.getInstance(str);
                instance.init((KeyManager[]) null, new TrustManager[]{(TrustManager) newInstance}, createSecureRandom());
                return instance.getSocketFactory();
            }
        }
        return null;
    }
}
