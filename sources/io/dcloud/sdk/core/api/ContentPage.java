package io.dcloud.sdk.core.api;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import io.dcloud.sdk.core.api.AOLLoader;
import java.io.Serializable;

public interface ContentPage extends Serializable {

    public static class ContentPageItem {
        private String duration;
        private String errorCode;
        private String errorMsg;
        private String id;
        private String type;

        public String getDuration() {
            return this.duration;
        }

        public String getErrorCode() {
            return this.errorCode;
        }

        public String getErrorMsg() {
            return this.errorMsg;
        }

        public String getId() {
            return this.id;
        }

        public String getType() {
            return this.type;
        }

        public void setDuration(String str) {
            this.duration = str;
        }

        public void setErrorCode(String str) {
            this.errorCode = str;
        }

        public void setErrorMsg(String str) {
            this.errorMsg = str;
        }

        public void setId(String str) {
            this.id = str;
        }

        public void setType(String str) {
            this.type = str;
        }
    }

    Fragment getContentPage();

    String getType();

    void setContentPageVideoListener(AOLLoader.ContentPageVideoListener contentPageVideoListener);

    void show(Activity activity);
}
