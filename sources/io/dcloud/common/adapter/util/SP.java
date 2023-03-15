package io.dcloud.common.adapter.util;

import android.content.Context;
import android.content.SharedPreferences;
import io.dcloud.e.f.b;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.util.HashMap;

public class SP {
    public static final String CHECK_PATH_STREAMAPP = "check_path_streamapp";
    public static final String DARK_MODE_BUNDLE_FORMAT = "dc_dark_mode_";
    public static final String DARK_MODE_KEY = "dark_mode";
    public static final String IS_CREATE_SHORTCUT = "_is_create_shortcut";
    public static final String K_COLLECTED = "_dpush_collected_";
    public static final String K_CREATED_SHORTCUT = "_created_shortcut";
    public static final String K_CREATE_SHORTCUT_NAME = "_create_shortcut_name";
    public static final String K_DEVICE_DPUSH_UUID = "_dpush_uuid_";
    public static final String K_LAST_POS = "_dpush_last_pos_";
    public static final String K_SHORT_CUT_ONE_TIPS = "short_cut_one_tips";
    public static final String K_SMART_UPDATE_NEED_UPDATE_ICON = "_smart_update_need_update_icon";
    public static final String K_SMART_UPDATE_PACKAGE_DOWNLOAD_SUCCESS = "_smart_update_packge_success";
    public static final String K_SMART_UPDATE_PARAMS = "_smart_update_need_update";
    public static final String K_STORAGES_SHORTCUT = "SHORTCUT";
    public static final String NEED_UPDATE_ICON = "_smart_update_need_update_icon";
    public static final String N_BASE = "pdr";
    public static final String N_DEVICE_INFO = "device_info";
    private static final String N_SMART_UPDATE = "_smart_update";
    public static final String N_STORAGES = "_storages";
    public static final String RECORD_RUN_SHORT_CUT = "record_run_short_cut";
    public static final String REPAIR_FIRST_SHORT_CUT = "repaid_first_short_cut";
    public static final String REPORT_UNI_VERIFY_GYUID = "report_uni_verify_GYUID";
    public static final String SMART_UPDATE = "pdr";
    public static final String STAREMAPP_ALIYUN_SHORT_CUT = "_staremapp_aliyun_short_cut";
    public static final String STAREMAPP_ALIYUN_SHORT_CUT_IS_FIRST_CREATED = "_staremapp_aliyun_short_cut_is_first_created";
    public static final String STAREMAPP_FIRST_SHORT_CUT = "_staremapp_first_short_cut";
    public static final String STAREMAPP_SHORTCUT_GUIDE_IS_FIRST_EMUI = "_staremapp_shortcut_guide_is_first_emui";
    public static final String STAREMAPP_SHORTCUT_GUIDE_IS_FIRST_FLYME = "_staremapp_shortcut_guide_is_first_flyme";
    public static final String STAREMAPP_SHORTCUT_GUIDE_IS_FIRST_MIUI = "_staremapp_shortcut_guide_is_first_miui";
    public static final String STAREMAPP_SHORTCUT_GUIDE_IS_FIRST_VIVO = "_staremapp_shortcut_guide_is_first_vivo";
    public static final String STAREMAPP_SHORTCUT_TIP_IS_FIRST = "_staremapp_shortcut_tip_is_first";
    public static final String STARTUP_DEVICE_ID = "_deviceId";
    public static final String STREAM_APP_NOT_FOUND_SPLASH_AT_SERVER = "_no_splash_at_server";
    public static final String UNIMP_CUSTOM_OAID_SAVE_KEY = "unimp_custom_oaid_save_key";
    public static final String UPDATE_PACKAGE_DOWNLOAD_SUCCESS = "_smart_update_packge_success";
    public static final String UPDATE_PARAMS = "_smart_update_need_update";
    public static final String UPDATE_SPLASH_AUTOCLOSE = "__update_splash_autoclose";
    public static final String UPDATE_SPLASH_AUTOCLOSE_W2A = "__update_splash_autoclose_w2a";
    public static final String UPDATE_SPLASH_DELAY = "__update_splash_delay";
    public static final String UPDATE_SPLASH_DELAY_W2A = "__update_splash_delay_w2a";
    public static final String UPDATE_SPLASH_IMG_PATH = "update_splash_img_path";
    public static final String WELCOME_SPLASH_SHOW = "__welcome_splash_show";
    private static HashMap<String, SharedPreferences> mBundles;

    public static void clearBundle(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }

    @Deprecated
    public static String getBundleData(String str, String str2) {
        return getBundleData(DeviceInfo.sApplicationContext, str, str2, false);
    }

    public static SharedPreferences getOrCreateBundle(String str, boolean z) {
        return getOrCreateBundle(DeviceInfo.sApplicationContext, str, z);
    }

    @Deprecated
    public static String getsBundleData(String str, String str2) {
        return getsBundleData(DeviceInfo.sApplicationContext, str, str2);
    }

    @Deprecated
    public static b getsOrCreateBundle(String str) {
        return getsOrCreateBundle(DeviceInfo.sApplicationContext, str);
    }

    public static boolean hasChanged(Context context, String str, boolean z) {
        SharedPreferences orCreateBundle = getOrCreateBundle(context, str, z);
        if (orCreateBundle instanceof SharedPreferencesExt) {
            return ((SharedPreferencesExt) orCreateBundle).hasChaged();
        }
        return true;
    }

    public static void removeBundleData(SharedPreferences sharedPreferences, String str) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(str);
        edit.commit();
    }

    public static void setBundleData(SharedPreferences sharedPreferences, String str, String str2) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(str, str2);
        edit.commit();
    }

    @Deprecated
    public static void setsBundleData(String str, String str2, String str3) {
        setsBundleData(DeviceInfo.sApplicationContext, str, str2, str3);
    }

    public static String getBundleData(Context context, String str, String str2) {
        return getBundleData(context, str, str2, false);
    }

    public static synchronized SharedPreferences getOrCreateBundle(Context context, String str, boolean z) {
        SharedPreferences sharedPreferences;
        synchronized (SP.class) {
            if (context == null) {
                try {
                    Context context2 = DeviceInfo.sApplicationContext;
                    if (context2 != null) {
                        context = context2;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (mBundles == null) {
                mBundles = new HashMap<>(1);
            }
            sharedPreferences = mBundles.get(str);
            if (sharedPreferences == null) {
                SharedPreferences sharedPreferences2 = DCloudAdapterUtil.getSharedPreferences(context, str, 0);
                if (sharedPreferences2 == null) {
                    sharedPreferences = z ? new SharedPreferencesExt(context, str) : context.getSharedPreferences(str, 0);
                } else {
                    sharedPreferences = sharedPreferences2;
                }
                mBundles.put(str, sharedPreferences);
            }
        }
        return sharedPreferences;
    }

    public static String getsBundleData(Context context, String str, String str2) {
        return b.a(context, str).a(str2, "");
    }

    public static synchronized b getsOrCreateBundle(Context context, String str) {
        b a;
        synchronized (SP.class) {
            a = b.a(context, str);
        }
        return a;
    }

    public static void setsBundleData(Context context, String str, String str2, String str3) {
        b.a(context, str).b(str2, str3);
    }

    @Deprecated
    public static String getBundleData(String str, String str2, boolean z) {
        return getBundleData(DeviceInfo.sApplicationContext, str, str2, z);
    }

    @Deprecated
    public static void clearBundle(String str) {
        clearBundle(DeviceInfo.sApplicationContext, str);
    }

    public static String getBundleData(Context context, String str, String str2, boolean z) {
        return getOrCreateBundle(context, str, z).getString(str2, "");
    }

    public static void removeBundleData(Context context, String str, String str2, boolean z) {
        SharedPreferences.Editor edit = getOrCreateBundle(context, str, z).edit();
        edit.remove(str2);
        edit.commit();
    }

    public static void setBundleData(Context context, String str, String str2, String str3, boolean z) {
        Context context2;
        if (context == null && (context2 = DeviceInfo.sApplicationContext) != null) {
            context = context2;
        }
        SharedPreferences.Editor edit = getOrCreateBundle(context, str, z).edit();
        edit.putString(str2, str3);
        edit.commit();
    }

    public static void clearBundle(Context context, String str) {
        SharedPreferences.Editor edit = getOrCreateBundle(context, str).edit();
        edit.clear();
        edit.commit();
    }

    public static String getBundleData(SharedPreferences sharedPreferences, String str) {
        return sharedPreferences.getString(str, "");
    }

    @Deprecated
    public static void removeBundleData(String str, String str2) {
        removeBundleData(DeviceInfo.sApplicationContext, str, str2, false);
    }

    public static void removeBundleData(Context context, String str, String str2) {
        removeBundleData(context, str, str2, false);
    }

    @Deprecated
    public static void setBundleData(String str, String str2, String str3) {
        setBundleData(DeviceInfo.sApplicationContext, str, str2, str3, false);
    }

    public static void setBundleData(Context context, String str, String str2, String str3) {
        setBundleData(context, str, str2, str3, false);
    }

    @Deprecated
    public static SharedPreferences getOrCreateBundle(String str) {
        return getOrCreateBundle(DeviceInfo.sApplicationContext, str, false);
    }

    public static SharedPreferences getOrCreateBundle(Context context, String str) {
        return getOrCreateBundle(context, str, false);
    }
}
