package com.alibaba.android.bindingx.core.internal;

import android.view.animation.Interpolator;
import androidx.core.view.animation.PathInterpolatorCompat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;
import org.json.JSONException;

class TimingFunctions {
    /* access modifiers changed from: private */
    public static final InnerCache<BezierInterpolatorWrapper> cache = new InnerCache<>(4);
    private static Object cubicBezier = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            ArrayList<Object> arrayList2 = arrayList;
            double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
            double doubleValue5 = ((Double) arrayList2.get(4)).doubleValue();
            double doubleValue6 = ((Double) arrayList2.get(5)).doubleValue();
            double doubleValue7 = ((Double) arrayList2.get(6)).doubleValue();
            double doubleValue8 = ((Double) arrayList2.get(7)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4);
            if (min == doubleValue4) {
                return Double.valueOf(doubleValue2 + doubleValue3);
            }
            float f = (float) doubleValue5;
            float f2 = (float) doubleValue6;
            float f3 = (float) doubleValue7;
            float f4 = (float) doubleValue8;
            BezierInterpolatorWrapper access$000 = TimingFunctions.isCacheHit(f, f2, f3, f4);
            if (access$000 == null) {
                access$000 = new BezierInterpolatorWrapper(f, f2, f3, f4);
                TimingFunctions.cache.add(access$000);
            }
            double interpolation = (double) access$000.getInterpolation((float) (min / doubleValue4));
            Double.isNaN(interpolation);
            return Double.valueOf((doubleValue3 * interpolation) + doubleValue2);
        }
    };
    private static Object easeInBack = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
            return Double.valueOf((doubleValue3 * min * min * ((2.70158d * min) - 1.70158d)) + doubleValue2);
        }
    };
    private static Object easeInBounce = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            return Double.valueOf(TimingFunctions.easeInBounce(Math.min(doubleValue, doubleValue4), doubleValue2, doubleValue3, doubleValue4));
        }
    };
    private static Object easeInCirc = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
            return Double.valueOf(((-doubleValue3) * (Math.sqrt(1.0d - (min * min)) - 1.0d)) + doubleValue2);
        }
    };
    private static Object easeInCubic = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
            return Double.valueOf((doubleValue3 * min * min * min) + doubleValue2);
        }
    };
    private static Object easeInElastic = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double d;
            ArrayList<Object> arrayList2 = arrayList;
            double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4);
            if (min == 0.0d) {
                return Double.valueOf(doubleValue2);
            }
            double d2 = min / doubleValue4;
            if (d2 == 1.0d) {
                return Double.valueOf(doubleValue2 + doubleValue3);
            }
            double d3 = 0.3d * doubleValue4;
            if (doubleValue3 < Math.abs(doubleValue3)) {
                d = d3 / 4.0d;
            } else {
                d = (d3 / 6.283185307179586d) * Math.asin(doubleValue3 / doubleValue3);
            }
            double d4 = d2 - 1.0d;
            return Double.valueOf((-(doubleValue3 * Math.pow(2.0d, d4 * 10.0d) * Math.sin((((d4 * doubleValue4) - d) * 6.283185307179586d) / d3))) + doubleValue2);
        }
    };
    private static Object easeInExpo = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4);
            if (min != 0.0d) {
                doubleValue2 += doubleValue3 * Math.pow(2.0d, ((min / doubleValue4) - 1.0d) * 10.0d);
            }
            return Double.valueOf(doubleValue2);
        }
    };
    private static Object easeInOutBack = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
            if (min < 1.0d) {
                return Double.valueOf(((doubleValue3 / 2.0d) * min * min * ((3.5949095d * min) - 2.5949095d)) + doubleValue2);
            }
            double d = min - 2.0d;
            return Double.valueOf(((doubleValue3 / 2.0d) * ((d * d * ((3.5949095d * d) + 2.5949095d)) + 2.0d)) + doubleValue2);
        }
    };
    private static Object easeInOutBounce = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            ArrayList<Object> arrayList2 = arrayList;
            double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4);
            if (min < doubleValue4 / 2.0d) {
                return Double.valueOf((TimingFunctions.easeInBounce(min * 2.0d, 0.0d, doubleValue3, doubleValue4) * 0.5d) + doubleValue2);
            }
            return Double.valueOf((TimingFunctions.easeOutBounce((min * 2.0d) - doubleValue4, 0.0d, doubleValue3, doubleValue4) * 0.5d) + (doubleValue3 * 0.5d) + doubleValue2);
        }
    };
    private static Object easeInOutCirc = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
            if (min < 1.0d) {
                return Double.valueOf((((-doubleValue3) / 2.0d) * (Math.sqrt(1.0d - (min * min)) - 1.0d)) + doubleValue2);
            }
            double d = min - 2.0d;
            return Double.valueOf(((doubleValue3 / 2.0d) * (Math.sqrt(1.0d - (d * d)) + 1.0d)) + doubleValue2);
        }
    };
    private static Object easeInOutCubic = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
            if (min < 1.0d) {
                return Double.valueOf(((doubleValue3 / 2.0d) * min * min * min) + doubleValue2);
            }
            double d = min - 2.0d;
            return Double.valueOf(((doubleValue3 / 2.0d) * ((d * d * d) + 2.0d)) + doubleValue2);
        }
    };
    private static Object easeInOutElastic = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            double d;
            ArrayList<Object> arrayList2 = arrayList;
            double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4);
            if (min == 0.0d) {
                return Double.valueOf(doubleValue2);
            }
            double d2 = min / (doubleValue4 / 2.0d);
            if (d2 == 2.0d) {
                return Double.valueOf(doubleValue2 + doubleValue3);
            }
            double d3 = 0.44999999999999996d * doubleValue4;
            if (doubleValue3 < Math.abs(doubleValue3)) {
                d = d3 / 4.0d;
            } else {
                d = (d3 / 6.283185307179586d) * Math.asin(doubleValue3 / doubleValue3);
            }
            if (d2 < 1.0d) {
                double d4 = d2 - 1.0d;
                return Double.valueOf((doubleValue3 * Math.pow(2.0d, d4 * 10.0d) * Math.sin((((d4 * doubleValue4) - d) * 6.283185307179586d) / d3) * -0.5d) + doubleValue2);
            }
            double d5 = d2 - 1.0d;
            return Double.valueOf((Math.pow(2.0d, -10.0d * d5) * doubleValue3 * Math.sin((((d5 * doubleValue4) - d) * 6.283185307179586d) / d3) * 0.5d) + doubleValue3 + doubleValue2);
        }
    };
    private static Object easeInOutExpo = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4);
            if (min == 0.0d) {
                return Double.valueOf(doubleValue2);
            }
            if (min == doubleValue4) {
                return Double.valueOf(doubleValue2 + doubleValue3);
            }
            double d = min / (doubleValue4 / 2.0d);
            if (d < 1.0d) {
                return Double.valueOf(((doubleValue3 / 2.0d) * Math.pow(2.0d, (d - 1.0d) * 10.0d)) + doubleValue2);
            }
            return Double.valueOf(((doubleValue3 / 2.0d) * ((-Math.pow(2.0d, (d - 1.0d) * -10.0d)) + 2.0d)) + doubleValue2);
        }
    };
    private static Object easeInOutQuad = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
            if (min < 1.0d) {
                return Double.valueOf(((doubleValue3 / 2.0d) * min * min) + doubleValue2);
            }
            double d = min - 1.0d;
            return Double.valueOf((((-doubleValue3) / 2.0d) * ((d * (d - 2.0d)) - 1.0d)) + doubleValue2);
        }
    };
    private static Object easeInOutQuart = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
            if (min < 1.0d) {
                return Double.valueOf(((doubleValue3 / 2.0d) * min * min * min * min) + doubleValue2);
            }
            double d = min - 2.0d;
            return Double.valueOf((((-doubleValue3) / 2.0d) * ((((d * d) * d) * d) - 2.0d)) + doubleValue2);
        }
    };
    private static Object easeInOutQuint = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / (doubleValue4 / 2.0d);
            if (min < 1.0d) {
                return Double.valueOf(((doubleValue3 / 2.0d) * min * min * min * min * min) + doubleValue2);
            }
            double d = min - 2.0d;
            return Double.valueOf(((doubleValue3 / 2.0d) * ((d * d * d * d * d) + 2.0d)) + doubleValue2);
        }
    };
    private static Object easeInOutSine = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            return Double.valueOf((((-doubleValue3) / 2.0d) * (Math.cos((Math.min(doubleValue, doubleValue4) * 3.141592653589793d) / doubleValue4) - 1.0d)) + doubleValue2);
        }
    };
    private static Object easeInQuad = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
            return Double.valueOf((doubleValue3 * min * min) + doubleValue2);
        }
    };
    private static Object easeInQuart = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
            return Double.valueOf((doubleValue3 * min * min * min * min) + doubleValue2);
        }
    };
    private static Object easeInQuint = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
            return Double.valueOf((doubleValue3 * min * min * min * min * min) + doubleValue2);
        }
    };
    private static Object easeInSine = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            return Double.valueOf(((-doubleValue3) * Math.cos((Math.min(doubleValue, doubleValue4) / doubleValue4) * 1.5707963267948966d)) + doubleValue3 + doubleValue2);
        }
    };
    private static Object easeOutBack = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
            return Double.valueOf((doubleValue3 * ((min * min * ((2.70158d * min) + 1.70158d)) + 1.0d)) + doubleValue2);
        }
    };
    private static Object easeOutBounce = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            return Double.valueOf(TimingFunctions.easeOutBounce(Math.min(doubleValue, doubleValue4), doubleValue2, doubleValue3, doubleValue4));
        }
    };
    private static Object easeOutCirc = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
            return Double.valueOf((doubleValue3 * Math.sqrt(1.0d - (min * min))) + doubleValue2);
        }
    };
    private static Object easeOutCubic = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
            return Double.valueOf((doubleValue3 * ((min * min * min) + 1.0d)) + doubleValue2);
        }
    };
    private static Object easeOutElastic = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) {
            double d;
            ArrayList<Object> arrayList2 = arrayList;
            double doubleValue = ((Double) arrayList2.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList2.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList2.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList2.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4);
            if (min == 0.0d) {
                return Double.valueOf(doubleValue2);
            }
            double d2 = min / doubleValue4;
            if (d2 == 1.0d) {
                return Double.valueOf(doubleValue2 + doubleValue3);
            }
            double d3 = 0.3d * doubleValue4;
            if (doubleValue3 < Math.abs(doubleValue3)) {
                d = d3 / 4.0d;
            } else {
                d = (d3 / 6.283185307179586d) * Math.asin(doubleValue3 / doubleValue3);
            }
            return Double.valueOf((Math.pow(2.0d, d2 * -10.0d) * doubleValue3 * Math.sin((((d2 * doubleValue4) - d) * 6.283185307179586d) / d3)) + doubleValue3 + doubleValue2);
        }
    };
    private static Object easeOutExpo = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double d;
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4);
            if (min == doubleValue4) {
                d = doubleValue2 + doubleValue3;
            } else {
                d = doubleValue2 + (doubleValue3 * ((-Math.pow(2.0d, (min * -10.0d) / doubleValue4)) + 1.0d));
            }
            return Double.valueOf(d);
        }
    };
    private static Object easeOutQuad = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = Math.min(doubleValue, doubleValue4) / doubleValue4;
            return Double.valueOf(((-doubleValue3) * min * (min - 2.0d)) + doubleValue2);
        }
    };
    private static Object easeOutQuart = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
            return Double.valueOf(((-doubleValue3) * ((((min * min) * min) * min) - 1.0d)) + doubleValue2);
        }
    };
    private static Object easeOutQuint = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            double min = (Math.min(doubleValue, doubleValue4) / doubleValue4) - 1.0d;
            return Double.valueOf((doubleValue3 * ((min * min * min * min * min) + 1.0d)) + doubleValue2);
        }
    };
    private static Object easeOutSine = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            return Double.valueOf((doubleValue3 * Math.sin((Math.min(doubleValue, doubleValue4) / doubleValue4) * 1.5707963267948966d)) + doubleValue2);
        }
    };
    private static Object linear = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            double doubleValue = ((Double) arrayList.get(0)).doubleValue();
            double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
            double doubleValue3 = ((Double) arrayList.get(2)).doubleValue();
            double doubleValue4 = ((Double) arrayList.get(3)).doubleValue();
            return Double.valueOf((doubleValue3 * (Math.min(doubleValue, doubleValue4) / doubleValue4)) + doubleValue2);
        }
    };

    /* access modifiers changed from: private */
    public static double easeOutBounce(double d, double d2, double d3, double d4) {
        double d5;
        double d6;
        double d7;
        double d8 = d / d4;
        if (d8 < 0.36363636363636365d) {
            d7 = 7.5625d * d8 * d8;
        } else {
            if (d8 < 0.7272727272727273d) {
                double d9 = d8 - 0.5454545454545454d;
                d5 = 7.5625d * d9 * d9;
                d6 = 0.75d;
            } else if (d8 < 0.9090909090909091d) {
                double d10 = d8 - 0.8181818181818182d;
                d5 = 7.5625d * d10 * d10;
                d6 = 0.9375d;
            } else {
                double d11 = d8 - 0.9545454545454546d;
                d5 = 7.5625d * d11 * d11;
                d6 = 0.984375d;
            }
            d7 = d5 + d6;
        }
        return (d3 * d7) + d2;
    }

    private TimingFunctions() {
    }

    static void applyToScope(Map<String, Object> map) {
        map.put("linear", linear);
        map.put("easeInQuad", easeInQuad);
        map.put("easeOutQuad", easeOutQuad);
        map.put("easeInOutQuad", easeInOutQuad);
        map.put("easeInCubic", easeInCubic);
        map.put("easeOutCubic", easeOutCubic);
        map.put("easeInOutCubic", easeInOutCubic);
        map.put("easeInQuart", easeInQuart);
        map.put("easeOutQuart", easeOutQuart);
        map.put("easeInOutQuart", easeInOutQuart);
        map.put("easeInQuint", easeInQuint);
        map.put("easeOutQuint", easeOutQuint);
        map.put("easeInOutQuint", easeInOutQuint);
        map.put("easeInSine", easeInSine);
        map.put("easeOutSine", easeOutSine);
        map.put("easeInOutSine", easeInOutSine);
        map.put("easeInExpo", easeInExpo);
        map.put("easeOutExpo", easeOutExpo);
        map.put("easeInOutExpo", easeInOutExpo);
        map.put("easeInCirc", easeInCirc);
        map.put("easeOutCirc", easeOutCirc);
        map.put("easeInOutCirc", easeInOutCirc);
        map.put("easeInElastic", easeInElastic);
        map.put("easeOutElastic", easeOutElastic);
        map.put("easeInOutElastic", easeInOutElastic);
        map.put("easeInBack", easeInBack);
        map.put("easeOutBack", easeOutBack);
        map.put("easeInOutBack", easeInOutBack);
        map.put("easeInBounce", easeInBounce);
        map.put("easeOutBounce", easeOutBounce);
        map.put("easeInOutBounce", easeInOutBounce);
        map.put("cubicBezier", cubicBezier);
    }

    /* access modifiers changed from: private */
    public static BezierInterpolatorWrapper isCacheHit(float f, float f2, float f3, float f4) {
        for (BezierInterpolatorWrapper next : cache.getAll()) {
            if (Float.compare(next.x1, f) == 0 && Float.compare(next.x2, f3) == 0 && Float.compare(next.y1, f2) == 0 && Float.compare(next.y2, f4) == 0) {
                return next;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static double easeInBounce(double d, double d2, double d3, double d4) {
        return (d3 - easeOutBounce(d4 - d, 0.0d, d3, d4)) + d2;
    }

    private static class InnerCache<T> {
        private final ArrayDeque<T> deque;

        InnerCache(int i) {
            this.deque = new ArrayDeque<>(i);
        }

        /* access modifiers changed from: package-private */
        public void add(T t) {
            if (this.deque.size() >= 4) {
                this.deque.removeFirst();
                this.deque.addLast(t);
                return;
            }
            this.deque.addLast(t);
        }

        /* access modifiers changed from: package-private */
        public Deque<T> getAll() {
            return this.deque;
        }
    }

    private static class BezierInterpolatorWrapper implements Interpolator {
        private Interpolator mInnerInterpolator;
        float x1;
        float x2;
        float y1;
        float y2;

        BezierInterpolatorWrapper(float f, float f2, float f3, float f4) {
            this.x1 = f;
            this.y1 = f2;
            this.x2 = f3;
            this.y2 = f4;
            this.mInnerInterpolator = PathInterpolatorCompat.create(f, f2, f3, f4);
        }

        public float getInterpolation(float f) {
            return this.mInnerInterpolator.getInterpolation(f);
        }
    }
}
