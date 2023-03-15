package org.mozilla.universalchardet.prober;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.mozilla.universalchardet.prober.CharsetProber;

public class MBCSGroupProber extends CharsetProber {
    private int activeNum;
    private CharsetProber bestGuess;
    private List<CharsetProber> probers;
    private CharsetProber.ProbingState state;

    public MBCSGroupProber() {
        ArrayList arrayList = new ArrayList();
        this.probers = arrayList;
        arrayList.add(new UTF8Prober());
        this.probers.add(new Big5Prober());
        reset();
    }

    public String getCharSetName() {
        if (this.bestGuess == null) {
            getConfidence();
            if (this.bestGuess == null) {
                this.bestGuess = this.probers.get(0);
            }
        }
        return this.bestGuess.getCharSetName();
    }

    public float getConfidence() {
        CharsetProber.ProbingState probingState = this.state;
        if (probingState == CharsetProber.ProbingState.FOUND_IT) {
            return 0.99f;
        }
        if (probingState == CharsetProber.ProbingState.NOT_ME) {
            return 0.01f;
        }
        float f = 0.0f;
        for (CharsetProber next : this.probers) {
            if (next.isActive()) {
                float confidence = next.getConfidence();
                if (f < confidence) {
                    this.bestGuess = next;
                    f = confidence;
                }
            }
        }
        return f;
    }

    public CharsetProber.ProbingState getState() {
        return this.state;
    }

    public CharsetProber.ProbingState handleData(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        int i3 = i2 + i;
        boolean z = true;
        int i4 = 0;
        while (i < i3) {
            if ((bArr[i] & 128) != 0) {
                bArr2[i4] = bArr[i];
                i4++;
                z = true;
            } else if (z) {
                bArr2[i4] = bArr[i];
                i4++;
                z = false;
            }
            i++;
        }
        Iterator<CharsetProber> it = this.probers.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            CharsetProber next = it.next();
            if (next.isActive()) {
                CharsetProber.ProbingState handleData = next.handleData(bArr2, 0, i4);
                CharsetProber.ProbingState probingState = CharsetProber.ProbingState.FOUND_IT;
                if (handleData == probingState) {
                    this.bestGuess = next;
                    this.state = probingState;
                    break;
                }
                CharsetProber.ProbingState probingState2 = CharsetProber.ProbingState.NOT_ME;
                if (handleData == probingState2) {
                    next.setActive(false);
                    int i5 = this.activeNum - 1;
                    this.activeNum = i5;
                    if (i5 <= 0) {
                        this.state = probingState2;
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        return this.state;
    }

    public void reset() {
        this.activeNum = 0;
        for (CharsetProber next : this.probers) {
            next.reset();
            next.setActive(true);
            this.activeNum++;
        }
        this.bestGuess = null;
        this.state = CharsetProber.ProbingState.DETECTING;
    }

    public void setOption() {
    }
}
