package io.dcloud.feature.weex.config;

import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.http.CertDTO;
import io.dcloud.feature.weex.config.MimeInfoParser;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class UserCustomTrustManager {
    private static HashMap<CertDTO, SSLSocketFactory> cacheCertSSLFactory = new HashMap<>();
    private static HashMap<AndroidTlsConfig, SSLSocketFactory> cacheSSLFactory = new HashMap<>();

    public static SSLSocketFactory getSSLSocketFactory(AndroidTlsConfig androidTlsConfig, WXSDKInstance wXSDKInstance) {
        SSLSocketFactory sSLSocketFactory;
        if (androidTlsConfig == null) {
            return null;
        }
        if (cacheSSLFactory.containsKey(androidTlsConfig) && (sSLSocketFactory = cacheSSLFactory.get(androidTlsConfig)) != null) {
            return sSLSocketFactory;
        }
        if (wXSDKInstance == null) {
            return null;
        }
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            KeyStore instance2 = KeyStore.getInstance("PKCS12");
            KeyManagerFactory instance3 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            if ((!TextUtils.isEmpty(androidTlsConfig.getKeystore())) && (true ^ TextUtils.isEmpty(androidTlsConfig.getStorePass()))) {
                MimeInfoParser.MimeInfo obtainMimeInfo = MimeInfoParser.getInstance().obtainMimeInfo(androidTlsConfig.getKeystore());
                if (obtainMimeInfo == null) {
                    return null;
                }
                instance2.load(obtainMimeInfo.getDataBytes(wXSDKInstance), androidTlsConfig.getStorePass().toCharArray());
                instance3.init(instance2, androidTlsConfig.getStorePass().toCharArray());
            } else {
                instance3 = null;
            }
            CertificateFactory instance4 = CertificateFactory.getInstance("X.509");
            KeyStore instance5 = KeyStore.getInstance("PKCS12");
            for (int i = 0; i < androidTlsConfig.getCa().length; i++) {
                MimeInfoParser.MimeInfo obtainMimeInfo2 = MimeInfoParser.getInstance().obtainMimeInfo(androidTlsConfig.getCa()[i]);
                if (obtainMimeInfo2 != null) {
                    InputStream dataBytes = obtainMimeInfo2.getDataBytes(wXSDKInstance);
                    instance5.load((KeyStore.LoadStoreParameter) null);
                    instance5.setCertificateEntry(Integer.toString(i), instance4.generateCertificate(dataBytes));
                }
            }
            TrustManagerFactory instance6 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance6.init(instance5);
            if (instance3 == null) {
                instance.init((KeyManager[]) null, instance6.getTrustManagers(), new SecureRandom());
            } else {
                instance.init(instance3.getKeyManagers(), instance6.getTrustManagers(), new SecureRandom());
            }
            SSLSocketFactory socketFactory = instance.getSocketFactory();
            if (cacheSSLFactory.size() > 3) {
                cacheSSLFactory.clear();
            }
            cacheSSLFactory.put(androidTlsConfig, socketFactory);
            return socketFactory;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return null;
        } catch (KeyStoreException e3) {
            e3.printStackTrace();
            return null;
        } catch (CertificateException e4) {
            e4.printStackTrace();
            return null;
        } catch (IOException e5) {
            e5.printStackTrace();
            return null;
        } catch (UnrecoverableKeyException e6) {
            e6.printStackTrace();
            return null;
        }
    }

    public static SSLSocketFactory getSSLSocketFactory(CertDTO certDTO, WXSDKInstance wXSDKInstance) {
        InputStream inputStream;
        InputStream inputStream2;
        SSLSocketFactory sSLSocketFactory;
        if (certDTO == null) {
            return null;
        }
        if (cacheCertSSLFactory.containsKey(certDTO) && (sSLSocketFactory = cacheCertSSLFactory.get(certDTO)) != null) {
            return sSLSocketFactory;
        }
        if (wXSDKInstance == null) {
            return null;
        }
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            KeyStore instance2 = KeyStore.getInstance("PKCS12");
            KeyManagerFactory instance3 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            if ((!TextUtils.isEmpty(certDTO.client)) && (true ^ TextUtils.isEmpty(certDTO.clientPassword))) {
                MimeInfoParser.MimeInfo obtainMimeInfo = MimeInfoParser.getInstance().obtainMimeInfo(certDTO.client);
                if (obtainMimeInfo != null) {
                    inputStream2 = obtainMimeInfo.getDataBytes(wXSDKInstance);
                } else {
                    inputStream2 = MimeInfoParser.getFilePathStream(wXSDKInstance, certDTO.client);
                }
                if (inputStream2 != null) {
                    instance2.load(inputStream2, certDTO.getClientPassword().toCharArray());
                    instance3.init(instance2, certDTO.getClientPassword().toCharArray());
                }
            } else {
                instance3 = null;
            }
            CertificateFactory instance4 = CertificateFactory.getInstance("X.509");
            KeyStore instance5 = KeyStore.getInstance("PKCS12");
            for (int i = 0; i < certDTO.getServer().length; i++) {
                MimeInfoParser.MimeInfo obtainMimeInfo2 = MimeInfoParser.getInstance().obtainMimeInfo(certDTO.getServer()[i]);
                if (obtainMimeInfo2 != null) {
                    inputStream = obtainMimeInfo2.getDataBytes(wXSDKInstance);
                } else {
                    inputStream = MimeInfoParser.getFilePathStream(wXSDKInstance, certDTO.getServer()[i]);
                }
                if (inputStream != null) {
                    instance5.load((KeyStore.LoadStoreParameter) null);
                    instance5.setCertificateEntry(Integer.toString(i), instance4.generateCertificate(inputStream));
                }
            }
            TrustManagerFactory instance6 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance6.init(instance5);
            if (instance3 == null) {
                instance.init((KeyManager[]) null, instance6.getTrustManagers(), new SecureRandom());
            } else {
                instance.init(instance3.getKeyManagers(), instance6.getTrustManagers(), new SecureRandom());
            }
            SSLSocketFactory socketFactory = instance.getSocketFactory();
            if (cacheCertSSLFactory.size() > 3) {
                cacheCertSSLFactory.clear();
            }
            cacheCertSSLFactory.put(certDTO, socketFactory);
            return socketFactory;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return null;
        } catch (KeyStoreException e3) {
            e3.printStackTrace();
            return null;
        } catch (CertificateException e4) {
            e4.printStackTrace();
            return null;
        } catch (IOException e5) {
            e5.printStackTrace();
            return null;
        } catch (UnrecoverableKeyException e6) {
            e6.printStackTrace();
            return null;
        }
    }
}
