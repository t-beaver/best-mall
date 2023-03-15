package io.dcloud.common.cs;

import io.dcloud.common.DHInterface.DAI;

public class DA implements DAI {
    private static DAI mInstance;

    private native void arn(String str, Object obj);

    private native void atcn(String str, Object obj);

    private native void scn();

    public static DAI getInstance() {
        if (mInstance == null) {
            mInstance = new DA();
        }
        return mInstance;
    }

    public void sc() {
        scn();
    }

    public void ar(String str, Object obj) {
        arn(str, obj);
    }

    public void act(String str, Object obj) {
        atcn(str, obj);
    }
}
