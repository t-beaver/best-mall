package com.alibaba.android.bindingx.core.internal;

class Vector3 {
    double x;
    double y;
    double z;

    Vector3(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    /* access modifiers changed from: package-private */
    public void set(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    /* access modifiers changed from: package-private */
    public Vector3 applyQuaternion(Quaternion quaternion) {
        Quaternion quaternion2 = quaternion;
        double d = this.x;
        double d2 = this.y;
        double d3 = this.z;
        double d4 = quaternion2.x;
        double d5 = quaternion2.y;
        double d6 = quaternion2.z;
        double d7 = quaternion2.w;
        double d8 = ((d7 * d) + (d5 * d3)) - (d6 * d2);
        double d9 = ((d7 * d2) + (d6 * d)) - (d4 * d3);
        double d10 = ((d7 * d3) + (d4 * d2)) - (d5 * d);
        double d11 = -d4;
        double d12 = ((d * d11) - (d2 * d5)) - (d3 * d6);
        double d13 = -d6;
        double d14 = -d5;
        this.x = (((d8 * d7) + (d12 * d11)) + (d9 * d13)) - (d10 * d14);
        this.y = (((d9 * d7) + (d12 * d14)) + (d10 * d11)) - (d8 * d13);
        this.z = (((d10 * d7) + (d12 * d13)) + (d8 * d14)) - (d9 * d11);
        return this;
    }
}
