package com.dcloud.android.graphics;

public class Region extends android.graphics.Region {
    private int HOLD_SCREEN_COUNT;
    int fillScreenCounter;

    public Region() {
        this(1);
    }

    public void count() {
        this.fillScreenCounter++;
    }

    public boolean fillWholeScreen() {
        return this.fillScreenCounter >= this.HOLD_SCREEN_COUNT;
    }

    public int getFillScreenCounter() {
        return this.fillScreenCounter;
    }

    public Region(int i) {
        this.HOLD_SCREEN_COUNT = 2;
        this.fillScreenCounter = 1;
        this.HOLD_SCREEN_COUNT = i;
    }
}
