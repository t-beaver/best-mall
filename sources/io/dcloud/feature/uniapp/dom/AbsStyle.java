package io.dcloud.feature.uniapp.dom;

import android.text.Layout;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.binding.ELUtils;
import com.taobao.weex.ui.component.WXTextDecoration;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class AbsStyle implements Map<String, Object>, Cloneable {
    public static final int UNSET = -1;
    private static final long serialVersionUID = 611132641365274134L;
    protected ArrayMap<String, Object> mBindingStyle;
    protected Map<String, Object> mPesudoResetStyleMap;
    protected Map<String, Map<String, Object>> mPesudoStyleMap;
    protected Map<String, Object> mStyles;

    public abstract AbsStyle clone();

    public AbsStyle() {
        this.mStyles = new ArrayMap();
    }

    public AbsStyle(Map<String, Object> map) {
        this.mStyles = map;
        processPesudoClasses(map);
    }

    public AbsStyle(Map<String, Object> map, boolean z) {
        this();
        putAll(map, z);
    }

    public String getBlur() {
        if (get(Constants.Name.FILTER) == null) {
            return null;
        }
        return get(Constants.Name.FILTER).toString().trim();
    }

    public static WXTextDecoration getTextDecoration(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get(Constants.Name.TEXT_DECORATION)) == null) {
            return WXTextDecoration.NONE;
        }
        String obj2 = obj.toString();
        obj2.hashCode();
        char c = 65535;
        switch (obj2.hashCode()) {
            case -1171789332:
                if (obj2.equals("line-through")) {
                    c = 0;
                    break;
                }
                break;
            case -1026963764:
                if (obj2.equals("underline")) {
                    c = 1;
                    break;
                }
                break;
            case 3387192:
                if (obj2.equals("none")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return WXTextDecoration.LINETHROUGH;
            case 1:
                return WXTextDecoration.UNDERLINE;
            case 2:
                return WXTextDecoration.NONE;
            default:
                return WXTextDecoration.INVALID;
        }
    }

    public static String getTextColor(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get("color")) == null) {
            return "";
        }
        return obj.toString();
    }

    public static int getFontWeight(Map<String, Object> map) {
        Object obj;
        if (!(map == null || (obj = map.get(Constants.Name.FONT_WEIGHT)) == null)) {
            String obj2 = obj.toString();
            obj2.hashCode();
            char c = 65535;
            switch (obj2.hashCode()) {
                case 53430:
                    if (obj2.equals("600")) {
                        c = 0;
                        break;
                    }
                    break;
                case 54391:
                    if (obj2.equals("700")) {
                        c = 1;
                        break;
                    }
                    break;
                case 55352:
                    if (obj2.equals("800")) {
                        c = 2;
                        break;
                    }
                    break;
                case 56313:
                    if (obj2.equals("900")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3029637:
                    if (obj2.equals(Constants.Value.BOLD)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    return 1;
            }
        }
        return 0;
    }

    public static int getFontStyle(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get(Constants.Name.FONT_STYLE)) == null || !obj.toString().equals(Constants.Value.ITALIC)) {
            return 0;
        }
        return 2;
    }

    public static int getFontSize(Map<String, Object> map, int i, float f) {
        float realPxByWidth;
        if (map == null) {
            realPxByWidth = WXViewUtils.getRealPxByWidth((float) i, f);
        } else {
            int i2 = WXUtils.getInt(map.get(Constants.Name.FONT_SIZE));
            if (i2 > 0) {
                i = i2;
            }
            realPxByWidth = WXViewUtils.getRealPxByWidth((float) i, f);
        }
        return (int) realPxByWidth;
    }

    public static String getFontFamily(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get(Constants.Name.FONT_FAMILY)) == null) {
            return null;
        }
        return obj.toString();
    }

    public static Layout.Alignment getTextAlignment(Map<String, Object> map) {
        return getTextAlignment(map, false);
    }

    public static Layout.Alignment getTextAlignment(Map<String, Object> map, boolean z) {
        Layout.Alignment alignment = z ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
        String str = (String) map.get(Constants.Name.TEXT_ALIGN);
        if (TextUtils.equals("left", str)) {
            return Layout.Alignment.ALIGN_NORMAL;
        }
        if (TextUtils.equals("center", str)) {
            return Layout.Alignment.ALIGN_CENTER;
        }
        return TextUtils.equals("right", str) ? Layout.Alignment.ALIGN_OPPOSITE : alignment;
    }

    public static TextUtils.TruncateAt getTextOverflow(Map<String, Object> map) {
        if (TextUtils.equals(Constants.Name.ELLIPSIS, (String) map.get(Constants.Name.TEXT_OVERFLOW))) {
            return TextUtils.TruncateAt.END;
        }
        return null;
    }

    public static int getLines(Map<String, Object> map) {
        return WXUtils.getInt(map.get(Constants.Name.LINES));
    }

    public static int getLineHeight(Map<String, Object> map, float f) {
        int i;
        if (map != null && (i = WXUtils.getInt(map.get(Constants.Name.LINE_HEIGHT))) > 0) {
            return (int) WXViewUtils.getRealPxByWidth((float) i, f);
        }
        return -1;
    }

    public float getBorderRadius() {
        float f = WXUtils.getFloat(get(Constants.Name.BORDER_RADIUS));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public String getBorderColor() {
        Object obj = get(Constants.Name.BORDER_COLOR);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public String getBorderStyle() {
        Object obj = get(Constants.Name.BORDER_STYLE);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public boolean isSticky() {
        Object obj = get("position");
        if (obj == null) {
            return false;
        }
        return obj.toString().equals("sticky");
    }

    public boolean isFixed() {
        Object obj = get("position");
        if (obj == null) {
            return false;
        }
        return obj.toString().equals(Constants.Value.FIXED);
    }

    public float getLeft() {
        float f = WXUtils.getFloat(get("left"));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public float getRight() {
        float f = WXUtils.getFloat(get("right"));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public float getTop() {
        float f = WXUtils.getFloat(get("top"));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public float getBottom() {
        float f = WXUtils.getFloat(get("bottom"));
        if (WXUtils.isUndefined(f)) {
            return Float.NaN;
        }
        return f;
    }

    public String getBackgroundColor() {
        Object obj = get("backgroundColor");
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public int getTimeFontSize(int i) {
        int i2 = WXUtils.getInt(get("timeFontSize"));
        return i2 <= 0 ? i : i2;
    }

    public float getOpacity() {
        Object obj = get("opacity");
        if (obj == null) {
            return 1.0f;
        }
        return WXUtils.getFloat(obj);
    }

    public String getOverflow() {
        Object obj = get(Constants.Name.OVERFLOW);
        if (obj == null) {
            return "visible";
        }
        return obj.toString();
    }

    public boolean equals(Object obj) {
        return this.mStyles.equals(obj);
    }

    public int hashCode() {
        return this.mStyles.hashCode();
    }

    public void clear() {
        this.mStyles.clear();
    }

    public boolean containsKey(Object obj) {
        return this.mStyles.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return this.mStyles.containsValue(obj);
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return this.mStyles.entrySet();
    }

    public Object get(Object obj) {
        return this.mStyles.get(obj);
    }

    public boolean isEmpty() {
        return this.mStyles.isEmpty();
    }

    public Set<String> keySet() {
        return this.mStyles.keySet();
    }

    public Object put(String str, Object obj) {
        if (addBindingStyleIfStatement(str, obj)) {
            return null;
        }
        return this.mStyles.put(str, obj);
    }

    public void putAll(Map<? extends String, ?> map) {
        this.mStyles.putAll(map);
    }

    public void putAll(Map<? extends String, ?> map, boolean z) {
        this.mStyles.putAll(map);
        if (!z) {
            processPesudoClasses(map);
        }
    }

    public void updateStyle(Map<? extends String, ?> map, boolean z) {
        parseBindingStylesStatements(map);
        putAll(map, z);
    }

    public Map<String, Object> getPesudoResetStyles() {
        if (this.mPesudoResetStyleMap == null) {
            this.mPesudoResetStyleMap = new ArrayMap();
        }
        return this.mPesudoResetStyleMap;
    }

    public Map<String, Map<String, Object>> getPesudoStyles() {
        if (this.mPesudoStyleMap == null) {
            this.mPesudoStyleMap = new ArrayMap();
        }
        return this.mPesudoStyleMap;
    }

    /* access modifiers changed from: package-private */
    public <T extends String, V> void processPesudoClasses(Map<T, V> map) {
        ArrayMap arrayMap = null;
        for (Map.Entry next : map.entrySet()) {
            String str = (String) next.getKey();
            int indexOf = str.indexOf(":");
            if (indexOf > 0) {
                initPesudoMapsIfNeed(map);
                String substring = str.substring(indexOf);
                if (substring.equals(Constants.PSEUDO.ENABLED)) {
                    String substring2 = str.substring(0, indexOf);
                    if (arrayMap == null) {
                        arrayMap = new ArrayMap();
                    }
                    arrayMap.put(substring2, next.getValue());
                    this.mPesudoResetStyleMap.put(substring2, next.getValue());
                } else {
                    String replace = substring.replace(Constants.PSEUDO.ENABLED, "");
                    Map map2 = this.mPesudoStyleMap.get(replace);
                    if (map2 == null) {
                        map2 = new ArrayMap();
                        this.mPesudoStyleMap.put(replace, map2);
                    }
                    map2.put(str.substring(0, indexOf), next.getValue());
                }
            }
        }
        if (arrayMap != null && !arrayMap.isEmpty()) {
            this.mStyles.putAll(arrayMap);
        }
    }

    public Object remove(Object obj) {
        return this.mStyles.remove(obj);
    }

    public int size() {
        return this.mStyles.size();
    }

    public Collection<Object> values() {
        return this.mStyles.values();
    }

    private void initPesudoMapsIfNeed(Map<? extends String, ?> map) {
        if (this.mPesudoStyleMap == null) {
            this.mPesudoStyleMap = new ArrayMap();
        }
        if (this.mPesudoResetStyleMap == null) {
            this.mPesudoResetStyleMap = new ArrayMap();
        }
        if (this.mPesudoResetStyleMap.isEmpty()) {
            this.mPesudoResetStyleMap.putAll(map);
        }
    }

    public void parseStatements() {
        Map<String, Object> map = this.mStyles;
        if (map != null) {
            this.mStyles = parseBindingStylesStatements(map);
        }
    }

    private Map<String, Object> parseBindingStylesStatements(Map map) {
        if (!(map == null || map.size() == 0)) {
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (addBindingStyleIfStatement((String) entry.getKey(), entry.getValue())) {
                    Map<String, Map<String, Object>> map2 = this.mPesudoStyleMap;
                    if (map2 != null) {
                        map2.remove(entry.getKey());
                    }
                    Map<String, Object> map3 = this.mPesudoResetStyleMap;
                    if (map3 != null) {
                        map3.remove(entry.getKey());
                    }
                    it.remove();
                }
            }
        }
        return map;
    }

    private boolean addBindingStyleIfStatement(String str, Object obj) {
        if (!ELUtils.isBinding(obj)) {
            return false;
        }
        if (this.mBindingStyle == null) {
            this.mBindingStyle = new ArrayMap<>();
        }
        this.mBindingStyle.put(str, ELUtils.bindingBlock(obj));
        return true;
    }

    public ArrayMap<String, Object> getBindingStyle() {
        return this.mBindingStyle;
    }

    public String toString() {
        return this.mStyles.toString();
    }
}
