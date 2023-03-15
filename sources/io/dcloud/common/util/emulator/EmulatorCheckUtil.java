package io.dcloud.common.util.emulator;

import android.content.Context;
import android.hardware.SensorManager;
import android.text.TextUtils;
import com.taobao.weex.WXEnvironment;
import java.io.File;
import java.util.Locale;

public class EmulatorCheckUtil {
    public static final int RESULT_EMULATOR = 1;
    public static final int RESULT_MAYBE_EMULATOR = 0;
    public static final int RESULT_UNKNOWN = 2;
    private static String[] known_pkgNames = {"sdcard/Android/data/com.bluestacks.home", "sdcard/Android/data/com.bluestacks.settings", "sdcard/Android/data/com.microvirt.guide", "sdcard/Android/data/com.microvirt.launcher2"};

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final EmulatorCheckUtil INSTANCE = new EmulatorCheckUtil();

        private SingletonHolder() {
        }
    }

    private CheckResult checkFeaturesByBaseBand() {
        String property = getProperty("gsm.version.baseband");
        if (property == null) {
            return new CheckResult(0, (String) null);
        }
        return new CheckResult(property.contains("1.0.0.0") ? 1 : 2, property);
    }

    private CheckResult checkFeaturesByBoard() {
        String property = getProperty("ro.product.board");
        if (property == null) {
            return new CheckResult(0, (String) null);
        }
        String lowerCase = property.toLowerCase(Locale.ENGLISH);
        return new CheckResult((!lowerCase.contains(WXEnvironment.OS) && !lowerCase.contains("goldfish")) ? 2 : 1, property);
    }

    private CheckResult checkFeaturesByFlavor() {
        String property = getProperty("ro.build.flavor");
        if (property == null) {
            return new CheckResult(0, (String) null);
        }
        String lowerCase = property.toLowerCase(Locale.ENGLISH);
        return new CheckResult((!lowerCase.contains("vbox") && !lowerCase.contains("sdk_gphone")) ? 2 : 1, property);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0072, code lost:
        if (r2.equals("cancro") == false) goto L_0x0026;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private io.dcloud.common.util.emulator.CheckResult checkFeaturesByHardware() {
        /*
            r7 = this;
            java.lang.String r0 = "ro.hardware"
            java.lang.String r0 = r7.getProperty(r0)
            r1 = 0
            if (r0 != 0) goto L_0x0010
            io.dcloud.common.util.emulator.CheckResult r0 = new io.dcloud.common.util.emulator.CheckResult
            r2 = 0
            r0.<init>(r1, r2)
            return r0
        L_0x0010:
            java.util.Locale r2 = java.util.Locale.ENGLISH
            java.lang.String r2 = r0.toLowerCase(r2)
            r2.hashCode()
            r2.hashCode()
            r3 = -1
            int r4 = r2.hashCode()
            r5 = 2
            r6 = 1
            switch(r4) {
                case -1367724016: goto L_0x006c;
                case -822798509: goto L_0x0060;
                case 109271: goto L_0x0055;
                case 3570999: goto L_0x004a;
                case 3613077: goto L_0x003e;
                case 100361430: goto L_0x0033;
                case 937844646: goto L_0x0028;
                default: goto L_0x0026;
            }
        L_0x0026:
            r1 = -1
            goto L_0x0075
        L_0x0028:
            java.lang.String r1 = "android_x86"
            boolean r1 = r2.equals(r1)
            if (r1 != 0) goto L_0x0031
            goto L_0x0026
        L_0x0031:
            r1 = 6
            goto L_0x0075
        L_0x0033:
            java.lang.String r1 = "intel"
            boolean r1 = r2.equals(r1)
            if (r1 != 0) goto L_0x003c
            goto L_0x0026
        L_0x003c:
            r1 = 5
            goto L_0x0075
        L_0x003e:
            java.lang.String r1 = "vbox"
            boolean r1 = r2.equals(r1)
            if (r1 != 0) goto L_0x0048
            goto L_0x0026
        L_0x0048:
            r1 = 4
            goto L_0x0075
        L_0x004a:
            java.lang.String r1 = "ttvm"
            boolean r1 = r2.equals(r1)
            if (r1 != 0) goto L_0x0053
            goto L_0x0026
        L_0x0053:
            r1 = 3
            goto L_0x0075
        L_0x0055:
            java.lang.String r1 = "nox"
            boolean r1 = r2.equals(r1)
            if (r1 != 0) goto L_0x005e
            goto L_0x0026
        L_0x005e:
            r1 = 2
            goto L_0x0075
        L_0x0060:
            java.lang.String r1 = "vbox86"
            boolean r1 = r2.equals(r1)
            if (r1 != 0) goto L_0x006a
            goto L_0x0026
        L_0x006a:
            r1 = 1
            goto L_0x0075
        L_0x006c:
            java.lang.String r4 = "cancro"
            boolean r2 = r2.equals(r4)
            if (r2 != 0) goto L_0x0075
            goto L_0x0026
        L_0x0075:
            switch(r1) {
                case 0: goto L_0x0079;
                case 1: goto L_0x0079;
                case 2: goto L_0x0079;
                case 3: goto L_0x0079;
                case 4: goto L_0x0079;
                case 5: goto L_0x0079;
                case 6: goto L_0x0079;
                default: goto L_0x0078;
            }
        L_0x0078:
            goto L_0x007a
        L_0x0079:
            r5 = 1
        L_0x007a:
            io.dcloud.common.util.emulator.CheckResult r1 = new io.dcloud.common.util.emulator.CheckResult
            r1.<init>(r5, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.emulator.EmulatorCheckUtil.checkFeaturesByHardware():io.dcloud.common.util.emulator.CheckResult");
    }

    private CheckResult checkFeaturesByManufacturer() {
        String property = getProperty("ro.product.manufacturer");
        if (property == null) {
            return new CheckResult(0, (String) null);
        }
        String lowerCase = property.toLowerCase(Locale.ENGLISH);
        return new CheckResult((!lowerCase.contains("genymotion") && !lowerCase.contains("netease")) ? 2 : 1, property);
    }

    private CheckResult checkFeaturesByModel() {
        String property = getProperty("ro.product.model");
        if (property == null) {
            return new CheckResult(0, (String) null);
        }
        String lowerCase = property.toLowerCase(Locale.ENGLISH);
        return new CheckResult((!lowerCase.contains("google_sdk") && !lowerCase.contains("emulator") && !lowerCase.contains("android sdk built for x86")) ? 2 : 1, property);
    }

    private CheckResult checkFeaturesByPlatform() {
        String property = getProperty("ro.board.platform");
        if (property == null) {
            return new CheckResult(0, (String) null);
        }
        return new CheckResult(property.toLowerCase(Locale.ENGLISH).contains(WXEnvironment.OS) ? 1 : 2, property);
    }

    public static CheckResult checkPkgNameForEmulator() {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 2;
        while (true) {
            String[] strArr = known_pkgNames;
            if (i2 >= strArr.length) {
                break;
            }
            if (new File(strArr[i2]).exists()) {
                i3++;
            } else {
                i4 = 0;
            }
            if (i3 > 2) {
                break;
            }
            i2++;
        }
        if (i3 != 1) {
            i = i3 != 2 ? i4 : 1;
        }
        return new CheckResult(i, "PkgName");
    }

    private String getProperty(String str) {
        String property = CommandUtil.getSingleInstance().getProperty(str);
        if (TextUtils.isEmpty(property)) {
            return null;
        }
        return property;
    }

    private int getSensorNumber(Context context) {
        return ((SensorManager) context.getSystemService("sensor")).getSensorList(-1).size();
    }

    public static final EmulatorCheckUtil getSingleInstance() {
        return SingletonHolder.INSTANCE;
    }

    private int getUserAppNum(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        return str.split("package:").length;
    }

    private boolean hasLightSensor(Context context) {
        return ((SensorManager) context.getSystemService("sensor")).getDefaultSensor(5) != null;
    }

    private boolean supportBluetooth(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.bluetooth");
    }

    private boolean supportCameraFlash(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

    public boolean emulatorCheck(Context context) {
        int i;
        if (context != null) {
            int i2 = checkFeaturesByHardware().result;
            if (i2 == 0) {
                i = 1;
            } else if (i2 == 1) {
                return true;
            } else {
                i = 0;
            }
            int i3 = checkPkgNameForEmulator().result;
            if (i3 == 0) {
                i++;
            } else if (i3 == 1) {
                return true;
            }
            int i4 = checkFeaturesByFlavor().result;
            if (i4 == 0) {
                i++;
            } else if (i4 == 1) {
                return true;
            }
            int i5 = checkFeaturesByModel().result;
            if (i5 == 0) {
                i++;
            } else if (i5 == 1) {
                return true;
            }
            int i6 = checkFeaturesByManufacturer().result;
            if (i6 == 0) {
                i++;
            } else if (i6 == 1) {
                return true;
            }
            int i7 = checkFeaturesByBoard().result;
            if (i7 == 0) {
                i++;
            } else if (i7 == 1) {
                return true;
            }
            int i8 = checkFeaturesByPlatform().result;
            if (i8 == 0) {
                i++;
            } else if (i8 == 1) {
                return true;
            }
            int i9 = checkFeaturesByBaseBand().result;
            if (i9 == 0) {
                i += 2;
            } else if (i9 == 1) {
                return true;
            }
            if (getSensorNumber(context) <= 7) {
                i++;
            }
            if (!supportCameraFlash(context)) {
                i++;
            }
            if (!supportBluetooth(context)) {
                i++;
            }
            if (!hasLightSensor(context)) {
                i++;
            }
            if (i > 3) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("context must not be null");
    }

    private EmulatorCheckUtil() {
    }
}
