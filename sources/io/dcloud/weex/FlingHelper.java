package io.dcloud.weex;

import android.content.Context;
import android.view.ViewConfiguration;

public class FlingHelper {
    private static float DECELERATION_RATE = ((float) (Math.log(0.78d) / Math.log(0.9d)));
    private static float mFlingFriction = ViewConfiguration.getScrollFriction();
    private static float mPhysicalCoeff;

    public FlingHelper(Context context) {
        mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
    }

    private double getSplineDeceleration(int i) {
        return Math.log((double) ((((float) Math.abs(i)) * 0.35f) / (mFlingFriction * mPhysicalCoeff)));
    }

    private double getSplineDecelerationByDistance(double d) {
        double d2 = (double) DECELERATION_RATE;
        Double.isNaN(d2);
        double d3 = (double) (mFlingFriction * mPhysicalCoeff);
        Double.isNaN(d3);
        double log = (d2 - 1.0d) * Math.log(d / d3);
        double d4 = (double) DECELERATION_RATE;
        Double.isNaN(d4);
        return log / d4;
    }

    public double getSplineFlingDistance(int i) {
        double splineDeceleration = getSplineDeceleration(i);
        float f = DECELERATION_RATE;
        double d = (double) f;
        double d2 = (double) f;
        Double.isNaN(d2);
        Double.isNaN(d);
        double exp = Math.exp(splineDeceleration * (d / (d2 - 1.0d)));
        double d3 = (double) (mFlingFriction * mPhysicalCoeff);
        Double.isNaN(d3);
        return exp * d3;
    }

    public int getVelocityByDistance(double d) {
        double exp = Math.exp(getSplineDecelerationByDistance(d));
        double d2 = (double) mFlingFriction;
        Double.isNaN(d2);
        double d3 = exp * d2;
        double d4 = (double) mPhysicalCoeff;
        Double.isNaN(d4);
        return Math.abs((int) ((d3 * d4) / 0.3499999940395355d));
    }
}
