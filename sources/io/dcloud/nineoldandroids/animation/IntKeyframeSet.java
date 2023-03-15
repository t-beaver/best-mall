package io.dcloud.nineoldandroids.animation;

import android.view.animation.Interpolator;
import io.dcloud.nineoldandroids.animation.Keyframe;
import java.util.ArrayList;

class IntKeyframeSet extends KeyframeSet {
    private int deltaValue;
    private boolean firstTime = true;
    private int firstValue;
    private int lastValue;

    public IntKeyframeSet(Keyframe.IntKeyframe... intKeyframeArr) {
        super(intKeyframeArr);
    }

    public int getIntValue(float f) {
        int i = this.mNumKeyframes;
        if (i == 2) {
            if (this.firstTime) {
                this.firstTime = false;
                this.firstValue = ((Keyframe.IntKeyframe) this.mKeyframes.get(0)).getIntValue();
                int intValue = ((Keyframe.IntKeyframe) this.mKeyframes.get(1)).getIntValue();
                this.lastValue = intValue;
                this.deltaValue = intValue - this.firstValue;
            }
            Interpolator interpolator = this.mInterpolator;
            if (interpolator != null) {
                f = interpolator.getInterpolation(f);
            }
            TypeEvaluator typeEvaluator = this.mEvaluator;
            if (typeEvaluator == null) {
                return this.firstValue + ((int) (f * ((float) this.deltaValue)));
            }
            return ((Number) typeEvaluator.evaluate(f, Integer.valueOf(this.firstValue), Integer.valueOf(this.lastValue))).intValue();
        } else if (f <= 0.0f) {
            Keyframe.IntKeyframe intKeyframe = (Keyframe.IntKeyframe) this.mKeyframes.get(0);
            Keyframe.IntKeyframe intKeyframe2 = (Keyframe.IntKeyframe) this.mKeyframes.get(1);
            int intValue2 = intKeyframe.getIntValue();
            int intValue3 = intKeyframe2.getIntValue();
            float fraction = intKeyframe.getFraction();
            float fraction2 = intKeyframe2.getFraction();
            Interpolator interpolator2 = intKeyframe2.getInterpolator();
            if (interpolator2 != null) {
                f = interpolator2.getInterpolation(f);
            }
            float f2 = (f - fraction) / (fraction2 - fraction);
            TypeEvaluator typeEvaluator2 = this.mEvaluator;
            if (typeEvaluator2 == null) {
                return intValue2 + ((int) (f2 * ((float) (intValue3 - intValue2))));
            }
            return ((Number) typeEvaluator2.evaluate(f2, Integer.valueOf(intValue2), Integer.valueOf(intValue3))).intValue();
        } else if (f >= 1.0f) {
            Keyframe.IntKeyframe intKeyframe3 = (Keyframe.IntKeyframe) this.mKeyframes.get(i - 2);
            Keyframe.IntKeyframe intKeyframe4 = (Keyframe.IntKeyframe) this.mKeyframes.get(this.mNumKeyframes - 1);
            int intValue4 = intKeyframe3.getIntValue();
            int intValue5 = intKeyframe4.getIntValue();
            float fraction3 = intKeyframe3.getFraction();
            float fraction4 = intKeyframe4.getFraction();
            Interpolator interpolator3 = intKeyframe4.getInterpolator();
            if (interpolator3 != null) {
                f = interpolator3.getInterpolation(f);
            }
            float f3 = (f - fraction3) / (fraction4 - fraction3);
            TypeEvaluator typeEvaluator3 = this.mEvaluator;
            if (typeEvaluator3 == null) {
                return intValue4 + ((int) (f3 * ((float) (intValue5 - intValue4))));
            }
            return ((Number) typeEvaluator3.evaluate(f3, Integer.valueOf(intValue4), Integer.valueOf(intValue5))).intValue();
        } else {
            Keyframe.IntKeyframe intKeyframe5 = (Keyframe.IntKeyframe) this.mKeyframes.get(0);
            int i2 = 1;
            while (true) {
                int i3 = this.mNumKeyframes;
                if (i2 >= i3) {
                    return ((Number) this.mKeyframes.get(i3 - 1).getValue()).intValue();
                }
                Keyframe.IntKeyframe intKeyframe6 = (Keyframe.IntKeyframe) this.mKeyframes.get(i2);
                if (f < intKeyframe6.getFraction()) {
                    Interpolator interpolator4 = intKeyframe6.getInterpolator();
                    if (interpolator4 != null) {
                        f = interpolator4.getInterpolation(f);
                    }
                    float fraction5 = (f - intKeyframe5.getFraction()) / (intKeyframe6.getFraction() - intKeyframe5.getFraction());
                    int intValue6 = intKeyframe5.getIntValue();
                    int intValue7 = intKeyframe6.getIntValue();
                    TypeEvaluator typeEvaluator4 = this.mEvaluator;
                    if (typeEvaluator4 == null) {
                        return intValue6 + ((int) (fraction5 * ((float) (intValue7 - intValue6))));
                    }
                    return ((Number) typeEvaluator4.evaluate(fraction5, Integer.valueOf(intValue6), Integer.valueOf(intValue7))).intValue();
                }
                i2++;
                intKeyframe5 = intKeyframe6;
            }
        }
    }

    public Object getValue(float f) {
        return Integer.valueOf(getIntValue(f));
    }

    public IntKeyframeSet clone() {
        ArrayList<Keyframe> arrayList = this.mKeyframes;
        int size = arrayList.size();
        Keyframe.IntKeyframe[] intKeyframeArr = new Keyframe.IntKeyframe[size];
        for (int i = 0; i < size; i++) {
            intKeyframeArr[i] = (Keyframe.IntKeyframe) arrayList.get(i).clone();
        }
        return new IntKeyframeSet(intKeyframeArr);
    }
}
