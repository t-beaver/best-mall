package io.dcloud.nineoldandroids.util;

public abstract class FloatProperty<T> extends Property<T, Float> {
    public FloatProperty(String str) {
        super(Float.class, str);
    }

    public abstract void setValue(T t, float f);

    public final void set(T t, Float f) {
        setValue(t, f.floatValue());
    }
}
