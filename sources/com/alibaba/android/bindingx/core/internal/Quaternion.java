package com.alibaba.android.bindingx.core.internal;

import com.taobao.weex.el.parse.Operators;

class Quaternion {
    double w;
    double x;
    double y;
    double z;

    Quaternion() {
    }

    Quaternion(double d, double d2, double d3, double d4) {
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.w = d4;
    }

    /* access modifiers changed from: package-private */
    public Quaternion setFromEuler(Euler euler) {
        Euler euler2 = euler;
        if (euler2 == null || !euler2.isEuler) {
            return null;
        }
        double cos = Math.cos(euler2.x / 2.0d);
        double cos2 = Math.cos(euler2.y / 2.0d);
        double cos3 = Math.cos(euler2.z / 2.0d);
        double sin = Math.sin(euler2.x / 2.0d);
        double sin2 = Math.sin(euler2.y / 2.0d);
        double sin3 = Math.sin(euler2.z / 2.0d);
        String str = euler2.order;
        if ("XYZ".equals(str)) {
            double d = sin * cos2;
            double d2 = cos * sin2;
            this.x = (d * cos3) + (d2 * sin3);
            this.y = (d2 * cos3) - (d * sin3);
            double d3 = cos * cos2;
            double d4 = sin * sin2;
            this.z = (d3 * sin3) + (d4 * cos3);
            this.w = (d3 * cos3) - (d4 * sin3);
        } else {
            double d5 = sin;
            if ("YXZ".equals(str)) {
                double d6 = d5 * cos2;
                double d7 = cos * sin2;
                this.x = (d6 * cos3) + (d7 * sin3);
                this.y = (d7 * cos3) - (d6 * sin3);
                double d8 = cos * cos2;
                double d9 = d5 * sin2;
                this.z = (d8 * sin3) - (d9 * cos3);
                this.w = (d8 * cos3) + (d9 * sin3);
            } else if ("ZXY".equals(str)) {
                double d10 = d5 * cos2;
                double d11 = cos * sin2;
                this.x = (d10 * cos3) - (d11 * sin3);
                this.y = (d11 * cos3) + (d10 * sin3);
                double d12 = cos * cos2;
                double d13 = d5 * sin2;
                this.z = (d12 * sin3) + (d13 * cos3);
                this.w = (d12 * cos3) - (d13 * sin3);
            } else if ("ZYX".equals(str)) {
                double d14 = d5 * cos2;
                double d15 = cos * sin2;
                this.x = (d14 * cos3) - (d15 * sin3);
                this.y = (d15 * cos3) + (d14 * sin3);
                double d16 = cos * cos2;
                double d17 = d5 * sin2;
                this.z = (d16 * sin3) - (d17 * cos3);
                this.w = (d16 * cos3) + (d17 * sin3);
            } else if ("YZX".equals(str)) {
                double d18 = d5 * cos2;
                double d19 = cos * sin2;
                this.x = (d18 * cos3) + (d19 * sin3);
                this.y = (d19 * cos3) + (d18 * sin3);
                double d20 = cos * cos2;
                double d21 = d5 * sin2;
                this.z = (d20 * sin3) - (d21 * cos3);
                this.w = (d20 * cos3) - (d21 * sin3);
            } else if ("XZY".equals(str)) {
                double d22 = d5 * cos2;
                double d23 = cos * sin2;
                this.x = (d22 * cos3) - (d23 * sin3);
                this.y = (d23 * cos3) - (d22 * sin3);
                double d24 = cos * cos2;
                double d25 = d5 * sin2;
                this.z = (d24 * sin3) + (d25 * cos3);
                this.w = (d24 * cos3) + (d25 * sin3);
            }
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public Quaternion setFromAxisAngle(Vector3 vector3, double d) {
        double d2 = d / 2.0d;
        double sin = Math.sin(d2);
        this.x = vector3.x * sin;
        this.y = vector3.y * sin;
        this.z = vector3.z * sin;
        this.w = Math.cos(d2);
        return this;
    }

    /* access modifiers changed from: package-private */
    public Quaternion multiply(Quaternion quaternion) {
        return multiplyQuaternions(this, quaternion);
    }

    /* access modifiers changed from: package-private */
    public Quaternion multiplyQuaternions(Quaternion quaternion, Quaternion quaternion2) {
        Quaternion quaternion3 = quaternion;
        Quaternion quaternion4 = quaternion2;
        double d = quaternion3.x;
        double d2 = quaternion3.y;
        double d3 = quaternion3.z;
        double d4 = quaternion3.w;
        double d5 = quaternion4.x;
        double d6 = quaternion4.y;
        double d7 = quaternion4.z;
        double d8 = d3;
        double d9 = quaternion4.w;
        double d10 = d7;
        double d11 = d10;
        this.x = (((d * d9) + (d4 * d5)) + (d2 * d7)) - (d8 * d6);
        this.y = (((d2 * d9) + (d4 * d6)) + (d8 * d5)) - (d * d11);
        this.z = (((d8 * d9) + (d4 * d11)) + (d * d6)) - (d2 * d5);
        this.w = (((d4 * d9) - (d * d5)) - (d2 * d6)) - (d8 * d11);
        return this;
    }

    public String toString() {
        return "Quaternion{x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", w=" + this.w + Operators.BLOCK_END;
    }
}
