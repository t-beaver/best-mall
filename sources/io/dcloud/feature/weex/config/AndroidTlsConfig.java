package io.dcloud.feature.weex.config;

import com.taobao.weex.el.parse.Operators;
import java.util.Arrays;
import java.util.Objects;

public class AndroidTlsConfig {
    private String[] ca;
    private String keystore;
    private String storePass;

    public String getKeystore() {
        return this.keystore;
    }

    public void setKeystore(String str) {
        if (str == null) {
            str = "";
        }
        this.keystore = str;
    }

    public String getStorePass() {
        return this.storePass;
    }

    public void setStorePass(String str) {
        if (str == null) {
            str = "";
        }
        this.storePass = str;
    }

    public String[] getCa() {
        return this.ca;
    }

    public void setCa(String[] strArr) {
        if (strArr == null) {
            strArr = new String[0];
        }
        this.ca = strArr;
    }

    public String toString() {
        return "AndroidTlsConfig{keystore='" + this.keystore + Operators.SINGLE_QUOTE + ", storePass='" + this.storePass + Operators.SINGLE_QUOTE + ", ca=" + Arrays.toString(this.ca) + Operators.BLOCK_END;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AndroidTlsConfig androidTlsConfig = (AndroidTlsConfig) obj;
        String arrays = Arrays.toString(this.ca);
        String arrays2 = Arrays.toString(androidTlsConfig.ca);
        if (!this.keystore.equals(androidTlsConfig.keystore) || !this.storePass.equals(androidTlsConfig.storePass) || !arrays.equals(arrays2)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (Objects.hash(new Object[]{this.keystore, this.storePass, Arrays.toString(this.ca)}) * 31) + Arrays.hashCode(this.ca);
    }
}
