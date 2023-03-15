package io.dcloud.feature.uniapp.dom;

import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.facebook.common.util.UriUtil;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXImageSharpen;
import com.taobao.weex.dom.WXImageQuality;
import com.taobao.weex.dom.binding.ELUtils;
import com.taobao.weex.dom.binding.WXStatement;
import com.taobao.weex.el.parse.Parser;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public abstract class AbsAttr implements Map<String, Object>, Cloneable {
    private static final long serialVersionUID = -2619357510079360946L;
    private Map<String, Object> attr;
    private ArrayMap<String, Object> mBindingAttrs;
    private WXStatement mStatement;
    private Map<String, Object> writeAttr;

    public abstract AbsAttr clone();

    public Map<String, Object> getAttr() {
        return this.attr;
    }

    public AbsAttr() {
        this.attr = new HashMap();
    }

    public AbsAttr(Map<String, Object> map) {
        this.attr = map;
    }

    public AbsAttr(Map<String, Object> map, int i) {
        this.attr = map;
    }

    public static String getPrefix(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get(Constants.Name.PREFIX)) == null) {
            return null;
        }
        return obj.toString();
    }

    public static String getSuffix(Map<String, Object> map) {
        Object obj;
        if (map == null || (obj = map.get(Constants.Name.SUFFIX)) == null) {
            return null;
        }
        return obj.toString();
    }

    public static String getValue(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Object obj = map.get("value");
        if (obj == null && (obj = map.get(UriUtil.LOCAL_CONTENT_SCHEME)) == null) {
            return null;
        }
        return obj.toString();
    }

    public WXImageQuality getImageQuality() {
        String str = Constants.Name.QUALITY;
        if (!containsKey(str)) {
            str = Constants.Name.IMAGE_QUALITY;
        }
        Object obj = get(str);
        WXImageQuality wXImageQuality = WXImageQuality.AUTO;
        if (obj == null) {
            return wXImageQuality;
        }
        String obj2 = obj.toString();
        if (TextUtils.isEmpty(obj2)) {
            return wXImageQuality;
        }
        try {
            return WXImageQuality.valueOf(obj2.toUpperCase(Locale.US));
        } catch (IllegalArgumentException unused) {
            WXLogUtils.e("Image", "Invalid value image quality. Only low, normal, high, original are valid");
            return wXImageQuality;
        }
    }

    public WXImageSharpen getImageSharpen() {
        Object obj = get(Constants.Name.SHARPEN);
        if (obj == null) {
            obj = get(Constants.Name.IMAGE_SHARPEN);
        }
        if (obj == null) {
            return WXImageSharpen.UNSHARPEN;
        }
        return obj.toString().equals(Constants.Name.SHARPEN) ? WXImageSharpen.SHARPEN : WXImageSharpen.UNSHARPEN;
    }

    public String getImageSrc() {
        Object obj = get("src");
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public boolean canRecycled() {
        Object obj = get(Constants.Name.RECYCLE);
        if (obj == null) {
            return true;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] recycle:", (Throwable) e);
            return true;
        }
    }

    public boolean showIndicators() {
        Object obj = get(Constants.Name.SHOW_INDICATORS);
        if (obj == null) {
            return true;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] showIndicators:", (Throwable) e);
            return true;
        }
    }

    public boolean autoPlay() {
        Object obj = get(Constants.Name.AUTO_PLAY);
        if (obj == null) {
            return false;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] autoPlay:", (Throwable) e);
            return false;
        }
    }

    public String getScope() {
        Object obj = get(Constants.Name.SCOPE);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public String getLoadMoreRetry() {
        Object obj = get(Constants.Name.LOADMORERETRY);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public String getLoadMoreOffset() {
        Object obj = get("loadmoreoffset");
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public String optString(String str) {
        if (!containsKey(str)) {
            return "";
        }
        Object obj = get(str);
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj != null ? String.valueOf(obj) : "";
    }

    public boolean getIsRecycleImage() {
        Object obj = get(Constants.Name.RECYCLE_IMAGE);
        if (obj == null) {
            return true;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] recycleImage:", (Throwable) e);
            return false;
        }
    }

    public String getScrollDirection() {
        Object obj = get(Constants.Name.SCROLL_DIRECTION);
        if (obj == null) {
            return "vertical";
        }
        return obj.toString();
    }

    public int getOrientation() {
        String scrollDirection = getScrollDirection();
        if (!TextUtils.isEmpty(scrollDirection) && scrollDirection.equals(Constants.Value.HORIZONTAL)) {
            return 0;
        }
        Object obj = get("orientation");
        if (obj == null || !Constants.Value.HORIZONTAL.equals(obj.toString())) {
            return 1;
        }
        return 0;
    }

    public float getElevation(float f) {
        Object obj = get(Constants.Name.ELEVATION);
        if (obj == null) {
            return Float.NaN;
        }
        String obj2 = obj.toString();
        if (!TextUtils.isEmpty(obj2)) {
            return WXViewUtils.getRealSubPxByWidth(WXUtils.getFloat(obj2), f);
        }
        return 0.0f;
    }

    public float getColumnWidth() {
        Object obj = get(Constants.Name.COLUMN_WIDTH);
        if (obj == null) {
            return -1.0f;
        }
        String valueOf = String.valueOf(obj);
        if ("auto".equals(valueOf)) {
            return -1.0f;
        }
        try {
            float parseFloat = Float.parseFloat(valueOf);
            if (parseFloat > 0.0f) {
                return parseFloat;
            }
            return 0.0f;
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] getColumnWidth:", (Throwable) e);
            return -1.0f;
        }
    }

    public int getColumnCount() {
        Object obj = get(Constants.Name.COLUMN_COUNT);
        if (obj == null) {
            return -1;
        }
        String valueOf = String.valueOf(obj);
        if ("auto".equals(valueOf)) {
            return -1;
        }
        try {
            int parseInt = Integer.parseInt(valueOf);
            if (parseInt > 0) {
                return parseInt;
            }
            return -1;
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] getColumnCount:", (Throwable) e);
            return -1;
        }
    }

    public float getColumnGap() {
        Object obj = get(Constants.Name.COLUMN_GAP);
        if (obj == null) {
            return 32.0f;
        }
        String valueOf = String.valueOf(obj);
        if ("normal".equals(valueOf)) {
            return 32.0f;
        }
        try {
            float parseFloat = Float.parseFloat(valueOf);
            if (parseFloat >= 0.0f) {
                return parseFloat;
            }
            return -1.0f;
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] getColumnGap:", (Throwable) e);
            return 32.0f;
        }
    }

    public int getLayoutType() {
        Object obj = get(Constants.Name.LAYOUT);
        if (obj == null) {
            return 1;
        }
        try {
            String valueOf = String.valueOf(obj);
            char c = 65535;
            int hashCode = valueOf.hashCode();
            if (hashCode != 3181382) {
                if (hashCode == 674874986) {
                    if (valueOf.equals(Constants.Value.MULTI_COLUMN)) {
                        c = 0;
                    }
                }
            } else if (valueOf.equals(Constants.Value.GRID)) {
                c = 1;
            }
            if (c == 0) {
                return 3;
            }
            if (c != 1) {
                return 1;
            }
            return 2;
        } catch (Exception e) {
            WXLogUtils.e("[WXAttr] getLayoutType:", (Throwable) e);
            return 1;
        }
    }

    public boolean equals(Object obj) {
        return this.attr.equals(obj);
    }

    public int hashCode() {
        return this.attr.hashCode();
    }

    public void clear() {
        this.attr.clear();
    }

    public boolean containsKey(Object obj) {
        return this.attr.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return this.attr.containsValue(obj);
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return this.attr.entrySet();
    }

    public Object get(Object obj) {
        Object obj2;
        Map<String, Object> map = this.writeAttr;
        if (map == null || (obj2 = map.get(obj)) == null) {
            return this.attr.get(obj);
        }
        return obj2;
    }

    public boolean isEmpty() {
        return this.attr.isEmpty();
    }

    public Set<String> keySet() {
        return this.attr.keySet();
    }

    public Object put(String str, Object obj) {
        if (addBindingAttrIfStatement(str, obj)) {
            return null;
        }
        return this.attr.put(str, obj);
    }

    public void putAll(Map<? extends String, ?> map) {
        if (this.writeAttr == null) {
            this.writeAttr = new ArrayMap();
        }
        this.writeAttr.putAll(map);
    }

    public Object remove(Object obj) {
        return this.attr.remove(obj);
    }

    public int size() {
        return this.attr.size();
    }

    public Collection<Object> values() {
        return this.attr.values();
    }

    public ArrayMap<String, Object> getBindingAttrs() {
        return this.mBindingAttrs;
    }

    public WXStatement getStatement() {
        return this.mStatement;
    }

    public void setBindingAttrs(ArrayMap<String, Object> arrayMap) {
        this.mBindingAttrs = arrayMap;
    }

    public void setStatement(WXStatement wXStatement) {
        this.mStatement = wXStatement;
    }

    public void parseStatements() {
        Map<String, Object> map = this.attr;
        if (map != null) {
            this.attr = filterStatementsFromAttrs(map);
        }
    }

    private Map<String, Object> filterStatementsFromAttrs(Map map) {
        if (!(map == null || map.size() == 0)) {
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (ELUtils.COMPONENT_PROPS.equals(entry.getKey())) {
                    entry.setValue(ELUtils.bindingBlock(entry.getValue()));
                } else if (addBindingAttrIfStatement((String) entry.getKey(), entry.getValue())) {
                    it.remove();
                }
            }
        }
        return map;
    }

    private boolean addBindingAttrIfStatement(String str, Object obj) {
        for (String equals : ELUtils.EXCLUDES_BINDING) {
            if (str.equals(equals)) {
                return false;
            }
        }
        if (ELUtils.isBinding(obj)) {
            if (this.mBindingAttrs == null) {
                this.mBindingAttrs = new ArrayMap<>();
            }
            this.mBindingAttrs.put(str, ELUtils.bindingBlock(obj));
            return true;
        } else if (WXStatement.WX_IF.equals(str)) {
            if (this.mStatement == null) {
                this.mStatement = new WXStatement();
            }
            if (obj != null) {
                this.mStatement.put(str, Parser.parse(obj.toString()));
            }
            return true;
        } else {
            if (WXStatement.WX_FOR.equals(str)) {
                if (this.mStatement == null) {
                    this.mStatement = new WXStatement();
                }
                Object vforBlock = ELUtils.vforBlock(obj);
                if (vforBlock != null) {
                    this.mStatement.put(str, vforBlock);
                    return true;
                }
            }
            if (WXStatement.WX_ONCE.equals(str)) {
                if (this.mStatement == null) {
                    this.mStatement = new WXStatement();
                }
                this.mStatement.put(str, true);
            }
            return false;
        }
    }

    public void skipFilterPutAll(Map<String, Object> map) {
        this.attr.putAll(map);
    }

    public void mergeAttr() {
        Map<String, Object> map = this.writeAttr;
        if (map != null) {
            this.attr.putAll(map);
            this.writeAttr = null;
        }
    }

    public String toString() {
        return this.attr.toString();
    }
}
