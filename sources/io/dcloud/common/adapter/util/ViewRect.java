package io.dcloud.common.adapter.util;

import android.os.Build;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

public class ViewRect {
    public static final int DEFAULT_MARGIN = 0;
    public static byte DOCK_BOTTOM = 6;
    public static byte DOCK_LEFT = 3;
    public static byte DOCK_RIGHT = 4;
    public static byte DOCK_TOP = 5;
    public static byte POSITION_ABSOLUTE = 1;
    public static byte POSITION_DOCK = 2;
    public static byte POSITION_STATIC;
    public boolean allowUpdate = true;
    public int anim_left;
    public int anim_top;
    public int bottom;
    private boolean doUpdate = false;
    public int height;
    public boolean isNotHeightFullScreen = false;
    public boolean isStatusbar = false;
    public boolean isStatusbarDodifyHeight = false;
    public int left;
    private byte mDock = DOCK_TOP;
    private ViewRect mFrameParentViewRect = null;
    public JSONObject mJsonViewOption = JSONUtil.createJSONObject("{}");
    private ViewRect mParentViewRect = null;
    private byte mPosition = POSITION_STATIC;
    private ArrayList<ViewRect> mRelViewRectDockSet = null;
    public String mStatusbarColor;
    public float mWebviewScale = 1.0f;
    public String margin = String.valueOf(0);
    public int right;
    public int top;
    public int width;

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0040 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0154  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void layoutDockViewRect(io.dcloud.common.adapter.util.ViewRect r16, io.dcloud.common.adapter.util.ViewRect r17, boolean r18) {
        /*
            r0 = r16
            r1 = r17
            io.dcloud.common.adapter.util.ViewRect r2 = r1.mParentViewRect
            org.json.JSONObject r3 = r1.mJsonViewOption
            java.lang.String r4 = "position"
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r4)
            boolean r5 = io.dcloud.common.util.PdrUtil.isEmpty(r4)
            java.lang.String r6 = "dock"
            r7 = 0
            r8 = 1
            if (r5 != 0) goto L_0x003d
            java.lang.String r5 = "absolute"
            boolean r5 = r5.equals(r4)
            if (r5 == 0) goto L_0x0025
            byte r4 = POSITION_ABSOLUTE
            r1.mPosition = r4
            goto L_0x003d
        L_0x0025:
            boolean r5 = r6.equals(r4)
            if (r5 == 0) goto L_0x0031
            byte r4 = POSITION_DOCK
            r1.mPosition = r4
            r4 = 1
            goto L_0x003e
        L_0x0031:
            java.lang.String r5 = "static"
            boolean r4 = r5.equals(r4)
            if (r4 == 0) goto L_0x003d
            byte r4 = POSITION_STATIC
            r1.mPosition = r4
        L_0x003d:
            r4 = 0
        L_0x003e:
            if (r4 != 0) goto L_0x0041
            return
        L_0x0041:
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r6)
            boolean r5 = io.dcloud.common.util.PdrUtil.isEmpty(r4)
            java.lang.String r6 = "top"
            java.lang.String r9 = "left"
            if (r5 != 0) goto L_0x007e
            java.lang.String r5 = "bottom"
            boolean r5 = r5.equals(r4)
            if (r5 == 0) goto L_0x005c
            byte r4 = DOCK_BOTTOM
            r1.mDock = r4
            goto L_0x007e
        L_0x005c:
            boolean r5 = r6.equals(r4)
            if (r5 == 0) goto L_0x0067
            byte r4 = DOCK_TOP
            r1.mDock = r4
            goto L_0x007e
        L_0x0067:
            boolean r5 = r9.equals(r4)
            if (r5 == 0) goto L_0x0072
            byte r4 = DOCK_LEFT
            r1.mDock = r4
            goto L_0x007e
        L_0x0072:
            java.lang.String r5 = "right"
            boolean r4 = r5.equals(r4)
            if (r4 == 0) goto L_0x007e
            byte r4 = DOCK_RIGHT
            r1.mDock = r4
        L_0x007e:
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r9)
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r6)
            java.lang.String r6 = "width"
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r6)
            java.lang.String r9 = "height"
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r9)
            boolean r9 = io.dcloud.common.util.PdrUtil.isEmpty(r4)
            r9 = r9 ^ r8
            boolean r10 = io.dcloud.common.util.PdrUtil.isEmpty(r5)
            r10 = r10 ^ r8
            boolean r11 = io.dcloud.common.util.PdrUtil.isEmpty(r6)
            r11 = r11 ^ r8
            boolean r12 = io.dcloud.common.util.PdrUtil.isEmpty(r3)
            r8 = r8 ^ r12
            int r12 = r2.width
            int r13 = r0.width
            float r14 = r2.mWebviewScale
            int r6 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r6, r12, r13, r14)
            r1.width = r6
            int r6 = r2.height
            int r12 = r0.height
            float r13 = r2.mWebviewScale
            int r3 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r3, r6, r12, r13)
            r1.height = r3
            int r6 = r1.width
            if (r6 >= 0) goto L_0x00c7
            io.dcloud.common.adapter.util.ViewRect r6 = r1.mParentViewRect
            int r6 = r6.width
        L_0x00c7:
            r1.width = r6
            if (r3 >= 0) goto L_0x00cf
            io.dcloud.common.adapter.util.ViewRect r3 = r1.mParentViewRect
            int r3 = r3.height
        L_0x00cf:
            r1.height = r3
            int r3 = r0.top
            int r6 = r0.left
            int r12 = r0.width
            int r13 = r0.height
            int r14 = r2.width
            float r15 = r2.mWebviewScale
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r4, r14, r6, r15)
            int r14 = r2.height
            int r15 = r0.top
            float r2 = r2.mWebviewScale
            int r2 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r5, r14, r15, r2)
            byte r5 = r1.mDock
            byte r14 = DOCK_BOTTOM
            if (r5 != r14) goto L_0x0110
            if (r10 == 0) goto L_0x0102
            if (r8 == 0) goto L_0x00f6
            goto L_0x0102
        L_0x00f6:
            int r5 = r0.top
            int r5 = r2 - r5
            r0.height = r5
            int r5 = r1.height
            int r5 = r5 - r2
            r1.height = r5
            goto L_0x010e
        L_0x0102:
            int r2 = r0.height
            int r5 = r1.height
            int r2 = r2 - r5
            r0.height = r2
            int r5 = r0.top
            int r7 = r2 + r5
            r2 = r7
        L_0x010e:
            r7 = r4
            goto L_0x0152
        L_0x0110:
            byte r8 = DOCK_RIGHT
            if (r5 != r8) goto L_0x0131
            if (r9 == 0) goto L_0x0125
            if (r11 == 0) goto L_0x0119
            goto L_0x0125
        L_0x0119:
            int r5 = r0.left
            int r5 = r4 - r5
            r0.width = r5
            int r5 = r1.width
            int r5 = r5 - r4
            r1.width = r5
            goto L_0x010e
        L_0x0125:
            int r4 = r0.width
            int r5 = r1.width
            int r4 = r4 - r5
            r0.width = r4
            int r5 = r0.left
            int r7 = r4 + r5
            goto L_0x0152
        L_0x0131:
            byte r8 = DOCK_LEFT
            if (r5 != r8) goto L_0x0141
            int r4 = r1.width
            int r5 = r4 + 0
            r0.left = r5
            int r5 = r0.width
            int r5 = r5 - r4
            r0.width = r5
            goto L_0x0152
        L_0x0141:
            byte r8 = DOCK_TOP
            if (r5 != r8) goto L_0x010e
            int r2 = r1.height
            int r5 = r2 + 0
            r0.top = r5
            int r5 = r0.height
            int r5 = r5 - r2
            r0.height = r5
            r7 = r4
            r2 = 0
        L_0x0152:
            if (r18 != 0) goto L_0x015c
            r0.left = r6
            r0.top = r3
            r0.width = r12
            r0.height = r13
        L_0x015c:
            r1.left = r7
            r1.top = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.ViewRect.layoutDockViewRect(io.dcloud.common.adapter.util.ViewRect, io.dcloud.common.adapter.util.ViewRect, boolean):void");
    }

    private void layoutWithRelViewRect() {
        ArrayList<ViewRect> arrayList = this.mRelViewRectDockSet;
        if (arrayList != null) {
            Iterator<ViewRect> it = arrayList.iterator();
            while (it.hasNext()) {
                layoutDockViewRect(this, it.next());
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001e, code lost:
        if (r1.mJsonViewOption.getString(r2).indexOf("%") < 0) goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void checkValueIsPercentage(java.lang.String r2, int r3, int r4, boolean r5, boolean r6) {
        /*
            r1 = this;
            org.json.JSONObject r0 = r1.mJsonViewOption     // Catch:{ JSONException -> 0x0047 }
            boolean r0 = r0.isNull(r2)     // Catch:{ JSONException -> 0x0047 }
            if (r0 == 0) goto L_0x000a
            if (r5 == 0) goto L_0x004b
        L_0x000a:
            org.json.JSONObject r5 = r1.mJsonViewOption     // Catch:{ JSONException -> 0x0047 }
            boolean r5 = r5.isNull(r2)     // Catch:{ JSONException -> 0x0047 }
            java.lang.String r0 = "%"
            if (r5 != 0) goto L_0x0020
            org.json.JSONObject r5 = r1.mJsonViewOption     // Catch:{ JSONException -> 0x0047 }
            java.lang.String r5 = r5.getString(r2)     // Catch:{ JSONException -> 0x0047 }
            int r5 = r5.indexOf(r0)     // Catch:{ JSONException -> 0x0047 }
            if (r5 >= 0) goto L_0x0022
        L_0x0020:
            if (r6 == 0) goto L_0x003c
        L_0x0022:
            if (r4 <= 0) goto L_0x004b
            org.json.JSONObject r5 = r1.mJsonViewOption     // Catch:{ JSONException -> 0x0047 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0047 }
            r6.<init>()     // Catch:{ JSONException -> 0x0047 }
            int r3 = r3 * 100
            int r3 = r3 / r4
            r6.append(r3)     // Catch:{ JSONException -> 0x0047 }
            r6.append(r0)     // Catch:{ JSONException -> 0x0047 }
            java.lang.String r3 = r6.toString()     // Catch:{ JSONException -> 0x0047 }
            r5.put(r2, r3)     // Catch:{ JSONException -> 0x0047 }
            goto L_0x004b
        L_0x003c:
            org.json.JSONObject r4 = r1.mJsonViewOption     // Catch:{ JSONException -> 0x0047 }
            float r3 = (float) r3     // Catch:{ JSONException -> 0x0047 }
            float r5 = r1.mWebviewScale     // Catch:{ JSONException -> 0x0047 }
            float r3 = r3 / r5
            double r5 = (double) r3     // Catch:{ JSONException -> 0x0047 }
            r4.put(r2, r5)     // Catch:{ JSONException -> 0x0047 }
            goto L_0x004b
        L_0x0047:
            r2 = move-exception
            r2.printStackTrace()
        L_0x004b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.ViewRect.checkValueIsPercentage(java.lang.String, int, int, boolean, boolean):void");
    }

    public void commitUpdate2JSONObject(boolean z, boolean z2) {
        ViewRect viewRect = this.mFrameParentViewRect;
        int i = viewRect != null ? viewRect.width : this.mParentViewRect.width;
        if (viewRect == null) {
            viewRect = this.mParentViewRect;
        }
        int i2 = viewRect.height;
        boolean z3 = z;
        boolean z4 = z2;
        checkValueIsPercentage("left", this.left, i, z3, z4);
        checkValueIsPercentage("top", this.top, i2, z3, z4);
        checkValueIsPercentage("width", this.width, i, z3, z4);
        checkValueIsPercentage("height", this.height, i2, z3, z4);
        checkValueIsPercentage("right", this.right, i, z3, z4);
        checkValueIsPercentage("bottom", this.bottom, i2, z3, z4);
    }

    public void delRelViewRect(ViewRect viewRect) {
        ArrayList<ViewRect> arrayList = this.mRelViewRectDockSet;
        if (arrayList != null) {
            arrayList.remove(viewRect);
        }
    }

    public ViewRect getParentViewRect() {
        return this.mParentViewRect;
    }

    public boolean hasHeightAbsolutevalue() {
        JSONObject jSONObject = this.mJsonViewOption;
        if (jSONObject != null && jSONObject.has("height") && !this.mJsonViewOption.isNull("height")) {
            return true ^ JSONUtil.getString(this.mJsonViewOption, "height").endsWith("%");
        }
        if (!this.mJsonViewOption.has("bottom") || !this.mJsonViewOption.has("top")) {
            return false;
        }
        return true;
    }

    public boolean hasRectChanged(ViewRect viewRect, ViewRect viewRect2) {
        return (viewRect.left == viewRect2.left && viewRect.top == viewRect2.top && viewRect.height == viewRect2.height && viewRect.width == viewRect2.width) ? false : true;
    }

    public boolean isBottomAbsolute() {
        JSONObject jSONObject = this.mJsonViewOption;
        return jSONObject != null && jSONObject.has("bottom");
    }

    public boolean isHeightAbsolute() {
        JSONObject jSONObject = this.mJsonViewOption;
        if (jSONObject == null) {
            return true;
        }
        if (!jSONObject.has("height") || this.mJsonViewOption.isNull("height")) {
            if (this.mJsonViewOption.has("bottom") && this.mJsonViewOption.has("top")) {
                return true;
            }
            if (this.mJsonViewOption.has("height") || this.mJsonViewOption.isNull("height")) {
            }
        } else if (!JSONUtil.getString(this.mJsonViewOption, "height").equals("100%")) {
            return true;
        }
        return false;
    }

    public void onScreenChanged(int i, int i2) {
        updateViewData(this.mJsonViewOption, i, i2);
    }

    public void putRelViewRect(ViewRect viewRect) {
        if (this.mRelViewRectDockSet == null) {
            this.mRelViewRectDockSet = new ArrayList<>();
        }
        this.mRelViewRectDockSet.add(viewRect);
    }

    public void setFrameParentViewRect(ViewRect viewRect) {
        this.mFrameParentViewRect = viewRect;
        this.mFrameParentViewRect = null;
    }

    public void setParentViewRect(ViewRect viewRect) {
        this.mParentViewRect = viewRect;
    }

    public void setUpdateAction(boolean z) {
        this.doUpdate = z;
    }

    public boolean updateViewData(JSONObject jSONObject, int i, int i2) {
        return updateViewData(jSONObject, i, i2, this.mWebviewScale);
    }

    public void onScreenChanged() {
        updateViewData(this.mJsonViewOption);
    }

    public boolean updateViewData(JSONObject jSONObject, int i, int i2, float f) {
        int i3;
        boolean z;
        int i4;
        boolean z2;
        boolean z3;
        JSONObject jSONObject2 = jSONObject;
        float f2 = f;
        JSONObject jSONObject3 = this.mJsonViewOption;
        if (jSONObject3 == null) {
            return false;
        }
        if (jSONObject3 != null) {
            JSONUtil.combinJSONObject(jSONObject3, jSONObject2);
            jSONObject2 = this.mJsonViewOption;
        } else {
            this.mJsonViewOption = jSONObject2;
        }
        int i5 = i2 < 0 ? this.height + this.bottom + this.top : i2;
        int i6 = i < 0 ? this.width : i;
        int i7 = this.left;
        int i8 = this.top;
        int i9 = this.width;
        int i10 = this.height;
        if (jSONObject2.has(AbsoluteConst.JSONKEY_STATUSBAR) && BaseInfo.isImmersive && Build.VERSION.SDK_INT >= 19) {
            this.isStatusbar = true;
            JSONObject optJSONObject = jSONObject2.optJSONObject(AbsoluteConst.JSONKEY_STATUSBAR);
            if (optJSONObject != null && optJSONObject.has("background")) {
                this.mStatusbarColor = optJSONObject.optString("background", this.mStatusbarColor);
            }
        }
        boolean z4 = this.doUpdate || !jSONObject2.isNull("left");
        boolean z5 = this.doUpdate || !jSONObject2.isNull("right");
        boolean z6 = this.doUpdate || !jSONObject2.isNull("top");
        int i11 = i9;
        if (this.doUpdate || !jSONObject2.isNull("width")) {
            i3 = i10;
            z = true;
        } else {
            i3 = i10;
            z = false;
        }
        int i12 = i8;
        if (this.doUpdate || !jSONObject2.isNull("height")) {
            i4 = i7;
            z2 = true;
        } else {
            i4 = i7;
            z2 = false;
        }
        boolean z7 = z6;
        boolean z8 = this.doUpdate || !jSONObject2.isNull("bottom");
        this.left = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "left"), i6, 0, f2);
        this.top = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "top"), i5, 0, f2);
        this.width = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "width"), i6, z ? this.width : i6, f2);
        int convertToScreenInt = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "height"), i5, z2 ? this.height : i5, f2);
        this.height = convertToScreenInt;
        if (z2 && convertToScreenInt < i5) {
            this.isNotHeightFullScreen = true;
            if (this.isStatusbar) {
                this.isStatusbarDodifyHeight = true;
            }
        }
        this.right = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "right"), i6, this.right, f2);
        this.bottom = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "bottom"), i5, this.bottom, f2);
        if (!jSONObject2.isNull("margin")) {
            String string = JSONUtil.getString(jSONObject2, "margin");
            this.margin = string;
            z3 = PdrUtil.isEquals("auto", string);
        } else {
            z3 = false;
        }
        if (!z4) {
            if (!z && z5) {
                this.left = -this.right;
            } else if (z && !z5 && z3) {
                this.left = (i6 - this.width) / 2;
            } else if (z && z5) {
                this.left = (i6 - this.width) - this.right;
            }
        } else if (!z && z5) {
            this.width = (i6 - this.left) - this.right;
        }
        if (!z7) {
            if (!z2 && z8) {
                this.top = -this.bottom;
            } else if (z2 && !z8 && z3) {
                this.top = (i5 - this.height) / 2;
            } else if (z2 && z8) {
                this.top = (i5 - this.height) - this.bottom;
            }
        } else if (!z2 && z8) {
            this.height = (i5 - this.top) - this.bottom;
        }
        layoutWithRelViewRect();
        layoutDockViewRect(this.mParentViewRect, this, false);
        int i13 = this.left;
        boolean z9 = (i4 == i13 && i12 == this.top && i3 == this.height && i11 == this.width) ? false : true;
        this.anim_left = i13;
        this.anim_top = this.top;
        return z9;
    }

    public void commitUpdate2JSONObject() {
        commitUpdate2JSONObject(false, false);
    }

    public void updateViewData(ViewRect viewRect) {
        this.mWebviewScale = viewRect.mWebviewScale;
        this.left = viewRect.left;
        this.top = viewRect.top;
        this.width = viewRect.width;
        this.height = viewRect.height;
        this.right = viewRect.right;
        this.bottom = viewRect.bottom;
        updateViewData(viewRect.mJsonViewOption);
    }

    public static void layoutDockViewRect(ViewRect viewRect, ViewRect viewRect2) {
        layoutDockViewRect(viewRect, viewRect2, true);
    }

    public boolean updateViewData(JSONObject jSONObject) {
        ViewRect viewRect = this.mParentViewRect;
        if (viewRect == null) {
            return false;
        }
        return updateViewData(jSONObject, viewRect.width, viewRect.height);
    }
}
