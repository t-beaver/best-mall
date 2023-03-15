package io.dcloud.feature.uniapp.ui.shadow;

import java.util.List;

public class UniBoxShadowData {
    int contentHeight;
    int contentWidth;
    List<UniBoxShadowOptions> insetShadows = null;
    String mStyle;
    int normalLeft;
    int normalMaxHeight;
    int normalMaxWidth;
    List<UniBoxShadowOptions> normalShadows = null;
    int normalTop;
    float quality;
    float[] radii;

    public UniBoxShadowData(List<UniBoxShadowOptions> list, List<UniBoxShadowOptions> list2, float[] fArr, float f) {
        this.normalShadows = list;
        this.insetShadows = list2;
        this.quality = f;
        this.radii = fArr;
    }

    public void setNormalMaxWidth(int i) {
        this.normalMaxWidth = i;
    }

    public void setNormalMaxHeight(int i) {
        this.normalMaxHeight = i;
    }

    public int getNormalMaxWidth() {
        return this.normalMaxWidth;
    }

    public int getNormalMaxHeight() {
        return this.normalMaxHeight;
    }

    public int getNormalLeft() {
        return this.normalLeft;
    }

    public void setNormalLeft(int i) {
        this.normalLeft = i;
    }

    public int getNormalTop() {
        return this.normalTop;
    }

    public void setNormalTop(int i) {
        this.normalTop = i;
    }

    public int getCanvasWidth() {
        return (int) (((float) getNormalMaxWidth()) * this.quality);
    }

    public int getCanvasHeight() {
        return (int) (((float) getNormalMaxHeight()) * this.quality);
    }

    public List<UniBoxShadowOptions> getNormalShadows() {
        return this.normalShadows;
    }

    public List<UniBoxShadowOptions> getInsetShadows() {
        return this.insetShadows;
    }

    public float getQuality() {
        return this.quality;
    }

    public int getContentWidth() {
        return this.contentWidth;
    }

    public void setContentWidth(int i) {
        this.contentWidth = i;
    }

    public int getContentHeight() {
        return this.contentHeight;
    }

    public void setContentHeight(int i) {
        this.contentHeight = i;
    }

    public float[] getRadii() {
        return this.radii;
    }

    public String getStyle() {
        return this.mStyle;
    }

    public void setStyle(String str) {
        this.mStyle = str;
    }

    public boolean equalsUniBoxShadowData(String str, int i, int i2, float[] fArr) {
        if (getStyle().equals(str) && getContentWidth() == i && getContentHeight() == i2 && getRadii()[0] == fArr[0] && getRadii()[2] == fArr[2] && getRadii()[4] == fArr[4] && getRadii()[6] == fArr[6]) {
            return true;
        }
        return false;
    }
}
