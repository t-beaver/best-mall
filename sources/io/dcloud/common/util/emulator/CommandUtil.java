package io.dcloud.common.util.emulator;

public class CommandUtil {

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final CommandUtil INSTANCE = new CommandUtil();

        private SingletonHolder() {
        }
    }

    public static final CommandUtil getSingleInstance() {
        return SingletonHolder.INSTANCE;
    }

    public String getProperty(String str) {
        try {
            Object invoke = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class}).invoke((Object) null, new Object[]{str});
            if (invoke != null) {
                return (String) invoke;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    private CommandUtil() {
    }
}
