package com.taobao.weex.ui.animation;

import android.animation.PropertyValuesHolder;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Property;
import android.view.View;
import androidx.collection.ArrayMap;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WXAnimationBean {
    public static final String CUBIC_BEZIER = "cubic-bezier";
    public static final String EASE_IN = "ease-in";
    public static final String EASE_IN_OUT = "ease-in-out";
    public static final String EASE_OUT = "ease-out";
    public static final String LINEAR = "linear";
    public static final int NUM_CUBIC_PARAM = 4;
    public long delay;
    public long duration;
    public boolean needLayout;
    public Style styles;
    public String timingFunction;

    public static class Style {
        public static final String BACKGROUND_COLOR = "backgroundColor";
        public static final String BOTTOM = "bottom";
        public static final String CENTER = "center";
        private static final String DEG = "deg";
        private static final String FULL = "100%";
        private static final String HALF = "50%";
        public static final String HEIGHT = "height";
        public static final String LEFT = "left";
        private static final String PX = "px";
        public static final String RIGHT = "right";
        public static final String TOP = "top";
        public static final String WIDTH = "width";
        public static final String WX_ROTATE = "rotate";
        public static final String WX_ROTATE_X = "rotateX";
        public static final String WX_ROTATE_Y = "rotateY";
        public static final String WX_SCALE = "scale";
        public static final String WX_SCALE_X = "scaleX";
        public static final String WX_SCALE_Y = "scaleY";
        public static final String WX_TRANSLATE = "translate";
        public static final String WX_TRANSLATE_X = "translateX";
        public static final String WX_TRANSLATE_Y = "translateY";
        private static final String ZERO = "0%";
        private static Map<Property<View, Float>, Float> defaultMap = new ArrayMap();
        public static Map<String, List<Property<View, Float>>> wxToAndroidMap;
        public String backgroundColor;
        private float cameraDistance = Float.MAX_VALUE;
        public String height;
        private List<PropertyValuesHolder> holders = new LinkedList();
        public String opacity;
        private Pair<Float, Float> pivot;
        public String transform;
        private Map<Property<View, Float>, Float> transformMap = new LinkedHashMap();
        public String transformOrigin;
        public String width;

        static {
            wxToAndroidMap = new ArrayMap();
            wxToAndroidMap.put("translate", Arrays.asList(new Property[]{View.TRANSLATION_X, View.TRANSLATION_Y}));
            wxToAndroidMap.put("translateX", Collections.singletonList(View.TRANSLATION_X));
            wxToAndroidMap.put("translateY", Collections.singletonList(View.TRANSLATION_Y));
            wxToAndroidMap.put("rotate", Collections.singletonList(View.ROTATION));
            wxToAndroidMap.put("rotateX", Collections.singletonList(View.ROTATION_X));
            wxToAndroidMap.put("rotateY", Collections.singletonList(View.ROTATION_Y));
            wxToAndroidMap.put("scale", Arrays.asList(new Property[]{View.SCALE_X, View.SCALE_Y}));
            wxToAndroidMap.put("scaleX", Collections.singletonList(View.SCALE_X));
            wxToAndroidMap.put("scaleY", Collections.singletonList(View.SCALE_Y));
            wxToAndroidMap.put(Constants.Name.PERSPECTIVE, Collections.singletonList(CameraDistanceProperty.getInstance()));
            wxToAndroidMap = Collections.unmodifiableMap(wxToAndroidMap);
            Map<Property<View, Float>, Float> map = defaultMap;
            Property property = View.TRANSLATION_X;
            Float valueOf = Float.valueOf(0.0f);
            map.put(property, valueOf);
            defaultMap.put(View.TRANSLATION_Y, valueOf);
            Map<Property<View, Float>, Float> map2 = defaultMap;
            Property property2 = View.SCALE_X;
            Float valueOf2 = Float.valueOf(1.0f);
            map2.put(property2, valueOf2);
            defaultMap.put(View.SCALE_Y, valueOf2);
            defaultMap.put(View.ROTATION, valueOf);
            defaultMap.put(View.ROTATION_X, valueOf);
            defaultMap.put(View.ROTATION_Y, valueOf);
        }

        private static Pair<Float, Float> parsePivot(String str, int i, int i2, float f) {
            int indexOf;
            if (TextUtils.isEmpty(str) || (indexOf = str.indexOf(32)) == -1) {
                return null;
            }
            int i3 = indexOf;
            while (i3 < str.length() && str.charAt(i3) == ' ') {
                i3++;
            }
            if (i3 >= str.length() || str.charAt(i3) == ' ') {
                return null;
            }
            ArrayList arrayList = new ArrayList(2);
            arrayList.add(str.substring(0, indexOf).trim());
            arrayList.add(str.substring(i3, str.length()).trim());
            return parsePivot((List<String>) arrayList, i, i2, f);
        }

        private static Pair<Float, Float> parsePivot(List<String> list, int i, int i2, float f) {
            return new Pair<>(Float.valueOf(parsePivotX(list.get(0), i, f)), Float.valueOf(parsePivotY(list.get(1), i2, f)));
        }

        private static float parsePivotX(String str, int i, float f) {
            if ("left".equals(str)) {
                str = ZERO;
            } else if ("right".equals(str)) {
                str = FULL;
            } else if ("center".equals(str)) {
                str = HALF;
            }
            return parsePercentOrPx(str, i, f);
        }

        private static float parsePivotY(String str, int i, float f) {
            if ("top".equals(str)) {
                str = ZERO;
            } else if ("bottom".equals(str)) {
                str = FULL;
            } else if ("center".equals(str)) {
                str = HALF;
            }
            return parsePercentOrPx(str, i, f);
        }

        private static float parsePercentOrPx(String str, int i, float f) {
            int lastIndexOf = str.lastIndexOf(37);
            if (lastIndexOf != -1) {
                return parsePercent(str.substring(0, lastIndexOf), i, 1.0f);
            }
            int lastIndexOf2 = str.lastIndexOf(PX);
            if (lastIndexOf2 != -1) {
                return WXViewUtils.getRealPxByWidth(WXUtils.fastGetFloat(str.substring(0, lastIndexOf2), 1.0f), f);
            }
            return WXViewUtils.getRealPxByWidth(WXUtils.fastGetFloat(str, 1.0f), f);
        }

        private static float parsePercent(String str, int i, float f) {
            return (WXUtils.fastGetFloat(str, f) / 100.0f) * ((float) i);
        }

        private void resetToDefaultIfAbsent() {
            for (Map.Entry next : defaultMap.entrySet()) {
                if (!this.transformMap.containsKey(next.getKey())) {
                    this.transformMap.put((Property) next.getKey(), (Float) next.getValue());
                }
            }
        }

        public Pair<Float, Float> getPivot() {
            return this.pivot;
        }

        public void init(String str, String str2, int i, int i2, float f, WXSDKInstance wXSDKInstance) {
            this.pivot = parsePivot(str, i, i2, f);
            this.transformMap.putAll(TransformParser.parseTransForm(wXSDKInstance.getInstanceId(), str2, i, i2, f));
            resetToDefaultIfAbsent();
            if (this.transformMap.containsKey(CameraDistanceProperty.getInstance())) {
                this.cameraDistance = this.transformMap.remove(CameraDistanceProperty.getInstance()).floatValue();
            }
            initHolders();
        }

        public void init(Map<Property<View, Float>, Pair<Float, Float>> map) {
            for (Map.Entry next : map.entrySet()) {
                this.holders.add(PropertyValuesHolder.ofFloat((Property) next.getKey(), new float[]{((Float) ((Pair) next.getValue()).first).floatValue(), ((Float) ((Pair) next.getValue()).second).floatValue()}));
            }
        }

        private void initHolders() {
            for (Map.Entry next : this.transformMap.entrySet()) {
                this.holders.add(PropertyValuesHolder.ofFloat((Property) next.getKey(), new float[]{((Float) next.getValue()).floatValue()}));
            }
            if (!TextUtils.isEmpty(this.opacity)) {
                this.holders.add(PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{WXUtils.fastGetFloat(this.opacity, 3.0f)}));
            }
        }

        public List<PropertyValuesHolder> getHolders() {
            return this.holders;
        }

        public float getCameraDistance() {
            return this.cameraDistance;
        }
    }
}
