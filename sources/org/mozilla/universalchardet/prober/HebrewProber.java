package org.mozilla.universalchardet.prober;

import io.dcloud.common.DHInterface.IApp;
import org.mozilla.universalchardet.Constants;
import org.mozilla.universalchardet.prober.CharsetProber;

public class HebrewProber extends CharsetProber {
    public static final int FINAL_KAF = 234;
    public static final int FINAL_MEM = 237;
    public static final int FINAL_NUN = 239;
    public static final int FINAL_PE = 243;
    public static final int FINAL_TSADI = 245;
    public static final int MIN_FINAL_CHAR_DISTANCE = 5;
    public static final float MIN_MODEL_DISTANCE = 0.01f;
    public static final int NORMAL_KAF = 235;
    public static final int NORMAL_MEM = 238;
    public static final int NORMAL_NUN = 240;
    public static final int NORMAL_PE = 244;
    public static final int NORMAL_TSADI = 246;
    public static final byte SPACE = 32;
    private byte beforePrev;
    private int finalCharLogicalScore;
    private int finalCharVisualScore;
    private CharsetProber logicalProber = null;
    private byte prev;
    private CharsetProber visualProber = null;

    public HebrewProber() {
        reset();
    }

    protected static boolean isFinal(byte b) {
        byte b2 = b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
        return b2 == 234 || b2 == 237 || b2 == 239 || b2 == 243 || b2 == 245;
    }

    protected static boolean isNonFinal(byte b) {
        byte b2 = b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
        return b2 == 235 || b2 == 238 || b2 == 240 || b2 == 244;
    }

    public String getCharSetName() {
        int i = this.finalCharLogicalScore - this.finalCharVisualScore;
        if (i >= 5) {
            return Constants.CHARSET_WINDOWS_1255;
        }
        if (i <= -5) {
            return Constants.CHARSET_ISO_8859_8;
        }
        float confidence = this.logicalProber.getConfidence() - this.visualProber.getConfidence();
        if (confidence > 0.01f) {
            return Constants.CHARSET_WINDOWS_1255;
        }
        if (confidence < -0.01f) {
            return Constants.CHARSET_ISO_8859_8;
        }
        if (i < 0) {
            return Constants.CHARSET_ISO_8859_8;
        }
        return Constants.CHARSET_WINDOWS_1255;
    }

    public float getConfidence() {
        return 0.0f;
    }

    public CharsetProber.ProbingState getState() {
        CharsetProber.ProbingState state = this.logicalProber.getState();
        CharsetProber.ProbingState probingState = CharsetProber.ProbingState.NOT_ME;
        if (state == probingState && this.visualProber.getState() == probingState) {
            return probingState;
        }
        return CharsetProber.ProbingState.DETECTING;
    }

    public CharsetProber.ProbingState handleData(byte[] bArr, int i, int i2) {
        CharsetProber.ProbingState state = getState();
        CharsetProber.ProbingState probingState = CharsetProber.ProbingState.NOT_ME;
        if (state == probingState) {
            return probingState;
        }
        int i3 = i2 + i;
        while (i < i3) {
            byte b = bArr[i];
            if (b == 32) {
                if (this.beforePrev != 32) {
                    if (isFinal(this.prev)) {
                        this.finalCharLogicalScore++;
                    } else if (isNonFinal(this.prev)) {
                        this.finalCharVisualScore++;
                    }
                }
            } else if (this.beforePrev == 32 && isFinal(this.prev) && b != 32) {
                this.finalCharVisualScore++;
            }
            this.beforePrev = this.prev;
            this.prev = b;
            i++;
        }
        return CharsetProber.ProbingState.DETECTING;
    }

    public void reset() {
        this.finalCharLogicalScore = 0;
        this.finalCharVisualScore = 0;
        this.prev = SPACE;
        this.beforePrev = SPACE;
    }

    public void setModalProbers(CharsetProber charsetProber, CharsetProber charsetProber2) {
        this.logicalProber = charsetProber;
        this.visualProber = charsetProber2;
    }

    public void setOption() {
    }
}
