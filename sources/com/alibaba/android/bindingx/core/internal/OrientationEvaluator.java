package com.alibaba.android.bindingx.core.internal;

class OrientationEvaluator {
    private final Euler EULER = new Euler();
    private final Quaternion Q0 = new Quaternion();
    private final Quaternion Q1 = new Quaternion(-Math.sqrt(0.5d), 0.0d, 0.0d, Math.sqrt(0.5d));
    private final Vector3 ZEE = new Vector3(0.0d, 0.0d, 1.0d);
    private Double constraintAlpha = null;
    private double constraintAlphaOffset = 0.0d;
    private Double constraintBeta = null;
    private double constraintBetaOffset = 0.0d;
    private Double constraintGamma = null;
    private double constraintGammaOffset = 0.0d;
    private Quaternion quaternion = new Quaternion(0.0d, 0.0d, 0.0d, 1.0d);

    OrientationEvaluator(Double d, Double d2, Double d3) {
        this.constraintAlpha = d;
        this.constraintBeta = d2;
        this.constraintGamma = d3;
    }

    /* access modifiers changed from: package-private */
    public Quaternion calculate(double d, double d2, double d3, double d4) {
        Double d5 = this.constraintAlpha;
        double radians = Math.toRadians(d5 != null ? d5.doubleValue() : d4 + this.constraintAlphaOffset);
        Double d6 = this.constraintBeta;
        double radians2 = Math.toRadians(d6 != null ? d6.doubleValue() : this.constraintBetaOffset + d2);
        Double d7 = this.constraintGamma;
        setObjectQuaternion(this.quaternion, radians, radians2, Math.toRadians(d7 != null ? d7.doubleValue() : d3 + this.constraintGammaOffset), 0.0d);
        return this.quaternion;
    }

    private void setObjectQuaternion(Quaternion quaternion2, double d, double d2, double d3, double d4) {
        Quaternion quaternion3 = quaternion2;
        this.EULER.setValue(d2, d, -d3, "YXZ");
        quaternion2.setFromEuler(this.EULER);
        quaternion2.multiply(this.Q1);
        quaternion2.multiply(this.Q0.setFromAxisAngle(this.ZEE, -d4));
    }
}
