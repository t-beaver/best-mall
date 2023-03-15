package io.dcloud.feature.uniapp.ui.action;

import android.view.View;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.dom.WXStyle;
import io.dcloud.feature.uniapp.dom.AbsAttr;
import io.dcloud.feature.uniapp.dom.AbsCSSShorthand;
import io.dcloud.feature.uniapp.dom.AbsEvent;
import io.dcloud.feature.uniapp.dom.AbsStyle;
import io.dcloud.feature.uniapp.utils.UniUtils;
import java.util.Map;
import java.util.Set;

public abstract class AbsComponentData<T extends View> {
    protected AbsAttr mAttributes;
    private AbsCSSShorthand mBorders;
    public String mComponentType;
    protected AbsEvent mEvents;
    private AbsCSSShorthand mMargins;
    private AbsCSSShorthand mPaddings;
    public String mParentRef;
    public String mRef;
    protected AbsStyle mStyles;
    protected long renderObjectPr = 0;

    public abstract AbsComponentData<T> clone() throws CloneNotSupportedException;

    public AbsComponentData(String str, String str2, String str3) {
        this.mRef = str;
        this.mComponentType = str2;
        this.mParentRef = str3;
    }

    public void addStyle(Map<String, Object> map) {
        addStyle(map, false);
    }

    public final void addStyle(Map<String, Object> map, boolean z) {
        if (map != null && !map.isEmpty()) {
            AbsStyle absStyle = this.mStyles;
            if (absStyle == null) {
                this.mStyles = new WXStyle(map);
            } else {
                absStyle.putAll(map, z);
            }
        }
    }

    public final void addAttr(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            AbsAttr absAttr = this.mAttributes;
            if (absAttr == null) {
                this.mAttributes = new WXAttr(map, 0);
            } else {
                absAttr.putAll(map);
            }
        }
    }

    public final void addEvent(Set<String> set) {
        if (set != null && !set.isEmpty()) {
            if (this.mEvents == null) {
                this.mEvents = new WXEvent();
            }
            this.mEvents.addAll(set);
        }
    }

    public final void addShorthand(float[] fArr, AbsCSSShorthand.TYPE type) {
        if (fArr == null) {
            fArr = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        }
        if (fArr.length == 4) {
            int i = AnonymousClass1.$SwitchMap$io$dcloud$feature$uniapp$dom$AbsCSSShorthand$TYPE[type.ordinal()];
            if (i == 1) {
                AbsCSSShorthand absCSSShorthand = this.mMargins;
                if (absCSSShorthand == null) {
                    this.mMargins = new CSSShorthand(fArr);
                } else {
                    absCSSShorthand.replace(fArr);
                }
            } else if (i == 2) {
                AbsCSSShorthand absCSSShorthand2 = this.mPaddings;
                if (absCSSShorthand2 == null) {
                    this.mPaddings = new CSSShorthand(fArr);
                } else {
                    absCSSShorthand2.replace(fArr);
                }
            } else if (i == 3) {
                AbsCSSShorthand absCSSShorthand3 = this.mBorders;
                if (absCSSShorthand3 == null) {
                    this.mBorders = new CSSShorthand(fArr);
                } else {
                    absCSSShorthand3.replace(fArr);
                }
            }
        }
    }

    /* renamed from: io.dcloud.feature.uniapp.ui.action.AbsComponentData$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$feature$uniapp$dom$AbsCSSShorthand$TYPE;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                io.dcloud.feature.uniapp.dom.AbsCSSShorthand$TYPE[] r0 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.TYPE.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$io$dcloud$feature$uniapp$dom$AbsCSSShorthand$TYPE = r0
                io.dcloud.feature.uniapp.dom.AbsCSSShorthand$TYPE r1 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.TYPE.MARGIN     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$io$dcloud$feature$uniapp$dom$AbsCSSShorthand$TYPE     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.feature.uniapp.dom.AbsCSSShorthand$TYPE r1 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.TYPE.PADDING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$io$dcloud$feature$uniapp$dom$AbsCSSShorthand$TYPE     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.feature.uniapp.dom.AbsCSSShorthand$TYPE r1 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.TYPE.BORDER     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.uniapp.ui.action.AbsComponentData.AnonymousClass1.<clinit>():void");
        }
    }

    public final void addShorthand(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> key : map.entrySet()) {
                String str = (String) key.getKey();
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case -1971292586:
                        if (str.equals(Constants.Name.BORDER_RIGHT_WIDTH)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1501175880:
                        if (str.equals(Constants.Name.PADDING_LEFT)) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1452542531:
                        if (str.equals(Constants.Name.BORDER_TOP_WIDTH)) {
                            c = 2;
                            break;
                        }
                        break;
                    case -1290574193:
                        if (str.equals(Constants.Name.BORDER_BOTTOM_WIDTH)) {
                            c = 3;
                            break;
                        }
                        break;
                    case -1081309778:
                        if (str.equals("margin")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -1044792121:
                        if (str.equals(Constants.Name.MARGIN_TOP)) {
                            c = 5;
                            break;
                        }
                        break;
                    case -806339567:
                        if (str.equals("padding")) {
                            c = 6;
                            break;
                        }
                        break;
                    case -289173127:
                        if (str.equals(Constants.Name.MARGIN_BOTTOM)) {
                            c = 7;
                            break;
                        }
                        break;
                    case -223992013:
                        if (str.equals(Constants.Name.BORDER_LEFT_WIDTH)) {
                            c = 8;
                            break;
                        }
                        break;
                    case 90130308:
                        if (str.equals(Constants.Name.PADDING_TOP)) {
                            c = 9;
                            break;
                        }
                        break;
                    case 202355100:
                        if (str.equals(Constants.Name.PADDING_BOTTOM)) {
                            c = 10;
                            break;
                        }
                        break;
                    case 713848971:
                        if (str.equals(Constants.Name.PADDING_RIGHT)) {
                            c = 11;
                            break;
                        }
                        break;
                    case 741115130:
                        if (str.equals(Constants.Name.BORDER_WIDTH)) {
                            c = 12;
                            break;
                        }
                        break;
                    case 975087886:
                        if (str.equals(Constants.Name.MARGIN_RIGHT)) {
                            c = 13;
                            break;
                        }
                        break;
                    case 1970934485:
                        if (str.equals(Constants.Name.MARGIN_LEFT)) {
                            c = 14;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        addBorder(CSSShorthand.EDGE.RIGHT, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 1:
                        addPadding(CSSShorthand.EDGE.LEFT, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 2:
                        addBorder(CSSShorthand.EDGE.TOP, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 3:
                        addBorder(CSSShorthand.EDGE.BOTTOM, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 4:
                        addMargin(CSSShorthand.EDGE.ALL, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 5:
                        addMargin(CSSShorthand.EDGE.TOP, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 6:
                        addPadding(CSSShorthand.EDGE.ALL, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 7:
                        addMargin(CSSShorthand.EDGE.BOTTOM, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 8:
                        addBorder(CSSShorthand.EDGE.LEFT, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 9:
                        addPadding(CSSShorthand.EDGE.TOP, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 10:
                        addPadding(CSSShorthand.EDGE.BOTTOM, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 11:
                        addPadding(CSSShorthand.EDGE.RIGHT, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 12:
                        addBorder(CSSShorthand.EDGE.ALL, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 13:
                        addMargin(CSSShorthand.EDGE.RIGHT, UniUtils.fastGetFloat(map.get(str)));
                        break;
                    case 14:
                        addMargin(CSSShorthand.EDGE.LEFT, UniUtils.fastGetFloat(map.get(str)));
                        break;
                }
            }
        }
    }

    private void addMargin(CSSShorthand.EDGE edge, float f) {
        if (this.mMargins == null) {
            this.mMargins = new CSSShorthand();
        }
        this.mMargins.set((Enum<? extends AbsCSSShorthand.CSSProperty>) edge, f);
    }

    private void addPadding(CSSShorthand.EDGE edge, float f) {
        if (this.mPaddings == null) {
            this.mPaddings = new CSSShorthand();
        }
        this.mPaddings.set((Enum<? extends AbsCSSShorthand.CSSProperty>) edge, f);
    }

    private void addBorder(CSSShorthand.EDGE edge, float f) {
        if (this.mBorders == null) {
            this.mBorders = new CSSShorthand();
        }
        this.mBorders.set((Enum<? extends AbsCSSShorthand.CSSProperty>) edge, f);
    }

    public AbsStyle getStyles() {
        if (this.mStyles == null) {
            this.mStyles = new WXStyle();
        }
        return this.mStyles;
    }

    public AbsAttr getAttrs() {
        if (this.mAttributes == null) {
            this.mAttributes = new WXAttr();
        }
        return this.mAttributes;
    }

    public AbsEvent getEvents() {
        if (this.mEvents == null) {
            this.mEvents = new WXEvent();
        }
        return this.mEvents;
    }

    public AbsCSSShorthand getMargin() {
        if (this.mMargins == null) {
            this.mMargins = new CSSShorthand();
        }
        return this.mMargins;
    }

    public AbsCSSShorthand getPadding() {
        if (this.mPaddings == null) {
            this.mPaddings = new CSSShorthand();
        }
        return this.mPaddings;
    }

    public AbsCSSShorthand getBorder() {
        if (this.mBorders == null) {
            this.mBorders = new CSSShorthand();
        }
        return this.mBorders;
    }

    public final void setMargins(CSSShorthand cSSShorthand) {
        this.mMargins = cSSShorthand;
    }

    public final void setPaddings(CSSShorthand cSSShorthand) {
        this.mPaddings = cSSShorthand;
    }

    public final void setBorders(CSSShorthand cSSShorthand) {
        this.mBorders = cSSShorthand;
    }

    public long getRenderObjectPr() {
        return this.renderObjectPr;
    }

    public boolean isRenderPtrEmpty() {
        return this.renderObjectPr == 0;
    }

    public synchronized void setRenderObjectPr(long j) {
        long j2 = this.renderObjectPr;
        if (j2 != j) {
            if (j2 == 0) {
                this.renderObjectPr = j;
            } else {
                throw new RuntimeException("RenderObjectPr has " + j + " old renderObjectPtr " + this.renderObjectPr);
            }
        }
    }
}
