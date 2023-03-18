package io.dcloud.common.util.hostpicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.f.a;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

public class HostPicker {
    private static HostPicker instance = new HostPicker();
    private final String SP_FILE_NAME = "UNIAPP_HostPicker_0817";
    private final String SP_LAST_SUIT_HOST_NAME = "SP_LAST_SUIT_HOST_NAME_0817";

    public static class Host implements Comparable<Host>, Cloneable {
        String hostUrl;
        PriorityEnum priority = PriorityEnum.NORMAL;

        public enum PriorityEnum {
            NORMAL(0),
            FIRST(1),
            BACKUP(-1);
            
            int val;

            private PriorityEnum(int i) {
                this.val = 0;
                this.val = i;
            }
        }

        public Host(String str, PriorityEnum priorityEnum) {
            this.hostUrl = str;
            this.priority = priorityEnum;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Host)) {
                return false;
            }
            Host host = (Host) obj;
            if (TextUtils.isEmpty(host.hostUrl)) {
                return false;
            }
            return host.hostUrl.equals(this.hostUrl);
        }

        public String getHostUrl() {
            return this.hostUrl;
        }

        public PriorityEnum getPriority() {
            return this.priority;
        }

        public String getRealHost() {
            String str = "";
            if (TextUtils.isEmpty(this.hostUrl)) {
                return str;
            }
            try {
                str = new String(Base64.decode(this.hostUrl.getBytes("UTF-8"), 2), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return a.b(str);
        }

        public int hashCode() {
            return this.hostUrl.hashCode();
        }

        public boolean isFormatRightful() {
            return !TextUtils.isEmpty(this.hostUrl);
        }

        public String toString() {
            return "Host{hostUrl='" + this.hostUrl + Operators.SINGLE_QUOTE + ", priority=" + this.priority + Operators.BLOCK_END;
        }

        /* access modifiers changed from: protected */
        public Host clone() {
            return new Host(this.hostUrl, this.priority);
        }

        public int compareTo(Host host) {
            if (host == null) {
                return 1;
            }
            return host.priority.val - this.priority.val;
        }
    }

    public interface HostPickCallback {
        boolean doRequest(Host host);

        void onNoOnePicked();

        void onOneSelected(Host host);
    }

    private HostPicker() {
    }

    public static HostPicker getInstance() {
        return instance;
    }

    private void initHostsForRequest(Context context, List<Host> list, String str) {
        String str2 = "SP_LAST_SUIT_HOST_NAME_0817" + str;
        SharedPreferences sharedPreferences = context.getSharedPreferences("UNIAPP_HostPicker_0817", 0);
        String string = sharedPreferences.getString(str2, "");
        for (Host next : list) {
            if (!next.isFormatRightful()) {
                throw new RuntimeException("error format host");
            } else if (!TextUtils.isEmpty(string) && next.priority != Host.PriorityEnum.BACKUP) {
                if (string.equals(next.hostUrl)) {
                    next.priority = Host.PriorityEnum.FIRST;
                } else {
                    next.priority = Host.PriorityEnum.NORMAL;
                }
            }
        }
        sharedPreferences.edit().remove(str2).apply();
    }

    public void pickSuitHost(Context context, List<Host> list, String str, HostPickCallback hostPickCallback) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("call initHosts first");
        }
        initHostsForRequest(context, list, str);
        Collections.sort(list);
        for (Host next : list) {
            if (hostPickCallback.doRequest(next)) {
                if (next.priority != Host.PriorityEnum.BACKUP) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("UNIAPP_HostPicker_0817", 0);
                    sharedPreferences.edit().putString("SP_LAST_SUIT_HOST_NAME_0817" + str, next.hostUrl).apply();
                }
                hostPickCallback.onOneSelected(next);
                return;
            }
        }
        hostPickCallback.onNoOnePicked();
    }
}
