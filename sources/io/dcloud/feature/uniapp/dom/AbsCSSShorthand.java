package io.dcloud.feature.uniapp.dom;

import android.text.TextUtils;
import java.lang.Enum;
import java.util.Arrays;

public abstract class AbsCSSShorthand<T extends Enum<? extends CSSProperty>> implements Cloneable {
    private float[] values;

    public enum CORNER implements CSSProperty {
        BORDER_TOP_LEFT,
        BORDER_TOP_RIGHT,
        BORDER_BOTTOM_RIGHT,
        BORDER_BOTTOM_LEFT,
        ALL
    }

    protected interface CSSProperty {
    }

    public enum EDGE implements CSSProperty {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        ALL
    }

    public enum TYPE {
        MARGIN,
        PADDING,
        BORDER
    }

    public AbsCSSShorthand(float[] fArr) {
        replace(fArr);
    }

    public AbsCSSShorthand() {
        this(false);
    }

    AbsCSSShorthand(boolean z) {
        float[] fArr = new float[Math.max(EDGE.values().length, CORNER.values().length)];
        this.values = fArr;
        if (z) {
            Arrays.fill(fArr, Float.NaN);
        }
    }

    public void set(Enum<? extends CSSProperty> enumR, float f) {
        setInternal(enumR, f);
    }

    public void set(CORNER corner, float f) {
        setInternal(corner, f);
    }

    public float get(Enum<? extends CSSProperty> enumR) {
        return getInternal(enumR);
    }

    public float get(CORNER corner) {
        return getInternal(corner);
    }

    public final void replace(float[] fArr) {
        this.values = fArr;
    }

    public AbsCSSShorthand clone() throws CloneNotSupportedException {
        return (AbsCSSShorthand) super.clone();
    }

    private void setInternal(Enum<? extends CSSProperty> enumR, float f) {
        if (enumR.name().equals(EDGE.ALL.name()) || enumR.name().equals(CORNER.ALL.name())) {
            Arrays.fill(this.values, f);
        } else {
            this.values[enumR.ordinal()] = f;
        }
    }

    private float getInternal(Enum<? extends CSSProperty> enumR) {
        if (enumR.name().equals(EDGE.ALL.name()) || enumR.name().equals(CORNER.ALL.name())) {
            return 0.0f;
        }
        return this.values[enumR.ordinal()];
    }

    public String toString() {
        return TextUtils.isEmpty(this.values.toString()) ? "" : Arrays.toString(this.values);
    }
}
