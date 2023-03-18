package org.mozilla.universalchardet.prober;

import java.util.Arrays;
import org.mozilla.universalchardet.Constants;
import org.mozilla.universalchardet.prober.CharsetProber;
import org.mozilla.universalchardet.prober.distributionanalysis.Big5DistributionAnalysis;
import org.mozilla.universalchardet.prober.statemachine.Big5SMModel;
import org.mozilla.universalchardet.prober.statemachine.CodingStateMachine;
import org.mozilla.universalchardet.prober.statemachine.SMModel;

public class Big5Prober extends CharsetProber {
    private static final SMModel smModel = new Big5SMModel();
    private CodingStateMachine codingSM = new CodingStateMachine(smModel);
    private Big5DistributionAnalysis distributionAnalyzer = new Big5DistributionAnalysis();
    private byte[] lastChar = new byte[2];
    private CharsetProber.ProbingState state;

    public Big5Prober() {
        reset();
    }

    public String getCharSetName() {
        return Constants.CHARSET_BIG5;
    }

    public float getConfidence() {
        return this.distributionAnalyzer.getConfidence();
    }

    public CharsetProber.ProbingState getState() {
        return this.state;
    }

    public CharsetProber.ProbingState handleData(byte[] bArr, int i, int i2) {
        int i3 = i2 + i;
        int i4 = i;
        while (true) {
            if (i4 >= i3) {
                break;
            }
            int nextState = this.codingSM.nextState(bArr[i4]);
            if (nextState == 1) {
                this.state = CharsetProber.ProbingState.NOT_ME;
                break;
            } else if (nextState == 2) {
                this.state = CharsetProber.ProbingState.FOUND_IT;
                break;
            } else {
                if (nextState == 0) {
                    int currentCharLen = this.codingSM.getCurrentCharLen();
                    if (i4 == i) {
                        byte[] bArr2 = this.lastChar;
                        bArr2[1] = bArr[i];
                        this.distributionAnalyzer.handleOneChar(bArr2, 0, currentCharLen);
                    } else {
                        this.distributionAnalyzer.handleOneChar(bArr, i4 - 1, currentCharLen);
                    }
                }
                i4++;
            }
        }
        this.lastChar[0] = bArr[i3 - 1];
        if (this.state == CharsetProber.ProbingState.DETECTING && this.distributionAnalyzer.gotEnoughData() && getConfidence() > 0.95f) {
            this.state = CharsetProber.ProbingState.FOUND_IT;
        }
        return this.state;
    }

    public void reset() {
        this.codingSM.reset();
        this.state = CharsetProber.ProbingState.DETECTING;
        this.distributionAnalyzer.reset();
        Arrays.fill(this.lastChar, (byte) 0);
    }

    public void setOption() {
    }
}
