package com.alibaba.android.bindingx.core.internal;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.text.TextUtils;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.taobao.weex.common.Constants;
import io.dcloud.common.util.ExifInterface;
import io.dcloud.common.util.JSUtil;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONException;

class JSMath {
    public static Object E = Double.valueOf(2.718281828459045d);
    private static Object PI = Double.valueOf(3.141592653589793d);
    private static Object abs = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.abs(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object acos = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.acos(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object asArray = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            return arrayList;
        }
    };
    private static Object asin = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.asin(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object atan = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.atan(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object atan2 = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.atan2(((Double) arrayList.get(0)).doubleValue(), ((Double) arrayList.get(1)).doubleValue()));
        }
    };
    private static Object cbrt = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.cbrt(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object ceil = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.ceil(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object cos = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.cos(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object evaluateColor = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            int access$000 = JSMath.parseColor((String) arrayList.get(0));
            int access$0002 = JSMath.parseColor((String) arrayList.get(1));
            return JSMath.sArgbEvaluator.evaluate((float) Math.min(1.0d, Math.max(0.0d, ((Double) arrayList.get(2)).doubleValue())), Integer.valueOf(access$000), Integer.valueOf(access$0002));
        }
    };
    private static Object exp = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.exp(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object floor = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.floor(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object log = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.log(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object matrix = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            if (arrayList == null || arrayList.size() < 6) {
                return null;
            }
            return arrayList;
        }
    };
    private static Object max = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            if (arrayList == null) {
                return null;
            }
            if (arrayList.size() < 1) {
                return null;
            }
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            int size = arrayList.size();
            for (int i = 1; i < size; i++) {
                double doubleValue2 = ((Double) arrayList.get(i)).doubleValue();
                if (doubleValue2 > doubleValue) {
                    doubleValue = doubleValue2;
                }
            }
            return Double.valueOf(doubleValue);
        }
    };
    private static Object min = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            if (arrayList == null) {
                return null;
            }
            if (arrayList.size() < 1) {
                return null;
            }
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            int size = arrayList.size();
            for (int i = 1; i < size; i++) {
                double doubleValue2 = ((Double) arrayList.get(i)).doubleValue();
                if (doubleValue2 < doubleValue) {
                    doubleValue = doubleValue2;
                }
            }
            return Double.valueOf(doubleValue);
        }
    };
    private static Object pow = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.pow(((Double) arrayList.get(0)).doubleValue(), ((Double) arrayList.get(1)).doubleValue()));
        }
    };
    private static Object rgb = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            if (arrayList == null || arrayList.size() < 3) {
                return null;
            }
            return Integer.valueOf(Color.rgb((int) ((Double) arrayList.get(0)).doubleValue(), (int) ((Double) arrayList.get(1)).doubleValue(), (int) ((Double) arrayList.get(2)).doubleValue()));
        }
    };
    private static Object rgba = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            if (arrayList == null || arrayList.size() < 4) {
                return null;
            }
            return Integer.valueOf(Color.argb((int) (((Double) arrayList.get(3)).doubleValue() * 255.0d), (int) ((Double) arrayList.get(0)).doubleValue(), (int) ((Double) arrayList.get(1)).doubleValue(), (int) ((Double) arrayList.get(2)).doubleValue()));
        }
    };
    private static Object round = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Long.valueOf(Math.round(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    /* access modifiers changed from: private */
    public static ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
    private static Object scale = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            if (arrayList == null || arrayList.size() < 2) {
                return null;
            }
            return arrayList;
        }
    };
    private static Object sign = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            if (doubleValue > 0.0d) {
                return 1;
            }
            if (doubleValue == 0.0d) {
                return 0;
            }
            if (doubleValue < 0.0d) {
                return -1;
            }
            return Double.valueOf(Double.NaN);
        }
    };
    private static Object sin = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.sin(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object sqrt = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.sqrt(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object tan = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            return Double.valueOf(Math.tan(((Double) arrayList.get(0)).doubleValue()));
        }
    };
    private static Object translate = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            if (arrayList == null || arrayList.size() < 2) {
                return null;
            }
            return arrayList;
        }
    };

    private JSMath() {
    }

    /* access modifiers changed from: private */
    public static int parseColor(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.startsWith("'") || str.startsWith(JSUtil.QUOTE)) {
                str = str.substring(1, str.length() - 1);
            }
            int parseColor = Color.parseColor(str);
            return Color.argb(255, Color.red(parseColor), Color.green(parseColor), Color.blue(parseColor));
        }
        throw new IllegalArgumentException("Unknown color");
    }

    static void applyXYToScope(Map<String, Object> map, double d, double d2, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator) {
        map.put(Constants.Name.X, Double.valueOf(iDeviceResolutionTranslator.nativeToWeb(d, new Object[0])));
        map.put(Constants.Name.Y, Double.valueOf(iDeviceResolutionTranslator.nativeToWeb(d2, new Object[0])));
        map.put("internal_x", Double.valueOf(d));
        map.put("internal_y", Double.valueOf(d2));
    }

    static void applyOrientationValuesToScope(Map<String, Object> map, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        Map<String, Object> map2 = map;
        map.put("alpha", Double.valueOf(d));
        map.put("beta", Double.valueOf(d2));
        map.put("gamma", Double.valueOf(d3));
        map.put("dalpha", Double.valueOf(d - d4));
        map.put("dbeta", Double.valueOf(d2 - d5));
        map.put("dgamma", Double.valueOf(d3 - d6));
        map.put(Constants.Name.X, Double.valueOf(d7));
        map.put(Constants.Name.Y, Double.valueOf(d8));
        map.put("z", Double.valueOf(d9));
    }

    static void applyTimingValuesToScope(Map<String, Object> map, double d) {
        map.put("t", Double.valueOf(d));
    }

    static void applyScrollValuesToScope(Map<String, Object> map, double d, double d2, double d3, double d4, double d5, double d6, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator) {
        Map<String, Object> map2 = map;
        PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
        double d7 = d;
        map.put(Constants.Name.X, Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d, new Object[0])));
        double d8 = d2;
        map.put(Constants.Name.Y, Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d2, new Object[0])));
        map.put("dx", Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d3, new Object[0])));
        map.put(Constants.Name.DISTANCE_Y, Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d4, new Object[0])));
        map.put("tdx", Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d5, new Object[0])));
        map.put("tdy", Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d6, new Object[0])));
        map.put("internal_x", Double.valueOf(d));
        map.put("internal_y", Double.valueOf(d2));
    }

    static void applyToScope(Map<String, Object> map) {
        map.put("sin", sin);
        map.put("cos", cos);
        map.put("tan", tan);
        map.put("asin", asin);
        map.put("acos", acos);
        map.put("atan", atan);
        map.put("atan2", atan2);
        map.put("pow", pow);
        map.put("exp", exp);
        map.put("sqrt", sqrt);
        map.put("cbrt", cbrt);
        map.put("log", log);
        map.put("abs", abs);
        map.put("sign", sign);
        map.put("ceil", ceil);
        map.put("floor", floor);
        map.put(AbsoluteConst.JSON_KEY_ROUND, round);
        map.put("max", max);
        map.put(Constants.Name.MIN, min);
        map.put("PI", PI);
        map.put(ExifInterface.LONGITUDE_EAST, E);
        map.put("translate", translate);
        map.put("scale", scale);
        map.put("matrix", matrix);
        map.put("rgb", rgb);
        map.put("rgba", rgba);
        map.put("evaluateColor", evaluateColor);
        map.put("asArray", asArray);
    }
}
