package org.mozilla.universalchardet.prober;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.mozilla.universalchardet.prober.CharsetProber;
import org.mozilla.universalchardet.prober.sequence.HebrewModel;
import org.mozilla.universalchardet.prober.sequence.Ibm855Model;
import org.mozilla.universalchardet.prober.sequence.Ibm866Model;
import org.mozilla.universalchardet.prober.sequence.Koi8rModel;
import org.mozilla.universalchardet.prober.sequence.Latin5BulgarianModel;
import org.mozilla.universalchardet.prober.sequence.Latin5Model;
import org.mozilla.universalchardet.prober.sequence.Latin7Model;
import org.mozilla.universalchardet.prober.sequence.MacCyrillicModel;
import org.mozilla.universalchardet.prober.sequence.ThaiModel;
import org.mozilla.universalchardet.prober.sequence.Win1251BulgarianModel;
import org.mozilla.universalchardet.prober.sequence.Win1251Model;
import org.mozilla.universalchardet.prober.sequence.Win1253Model;

public class SBCSGroupProber extends CharsetProber {
    private int activeNum;
    private CharsetProber bestGuess;
    private List<CharsetProber> probers;
    private CharsetProber.ProbingState state;

    public SBCSGroupProber() {
        ArrayList arrayList = new ArrayList();
        this.probers = arrayList;
        arrayList.add(new SingleByteCharsetProber(new Win1251Model()));
        this.probers.add(new SingleByteCharsetProber(new Koi8rModel()));
        this.probers.add(new SingleByteCharsetProber(new Latin5Model()));
        this.probers.add(new SingleByteCharsetProber(new MacCyrillicModel()));
        this.probers.add(new SingleByteCharsetProber(new Ibm866Model()));
        this.probers.add(new SingleByteCharsetProber(new Ibm855Model()));
        this.probers.add(new SingleByteCharsetProber(new Latin7Model()));
        this.probers.add(new SingleByteCharsetProber(new Win1253Model()));
        this.probers.add(new SingleByteCharsetProber(new Latin5BulgarianModel()));
        this.probers.add(new SingleByteCharsetProber(new Win1251BulgarianModel()));
        this.probers.add(new SingleByteCharsetProber(new ThaiModel()));
        HebrewModel hebrewModel = new HebrewModel();
        HebrewProber hebrewProber = new HebrewProber();
        SingleByteCharsetProber singleByteCharsetProber = new SingleByteCharsetProber(hebrewModel, false, hebrewProber);
        SingleByteCharsetProber singleByteCharsetProber2 = new SingleByteCharsetProber(hebrewModel, true, hebrewProber);
        hebrewProber.setModalProbers(singleByteCharsetProber, singleByteCharsetProber2);
        this.probers.add(hebrewProber);
        this.probers.add(singleByteCharsetProber);
        this.probers.add(singleByteCharsetProber2);
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
        ByteBuffer filterWithoutEnglishLetters = filterWithoutEnglishLetters(bArr, i, i2);
        if (filterWithoutEnglishLetters.position() != 0) {
            Iterator<CharsetProber> it = this.probers.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CharsetProber next = it.next();
                if (next.isActive()) {
                    CharsetProber.ProbingState handleData = next.handleData(filterWithoutEnglishLetters.array(), 0, filterWithoutEnglishLetters.position());
                    CharsetProber.ProbingState probingState = CharsetProber.ProbingState.FOUND_IT;
                    if (handleData == probingState) {
                        this.bestGuess = next;
                        this.state = probingState;
                        break;
                    }
                    CharsetProber.ProbingState probingState2 = CharsetProber.ProbingState.NOT_ME;
                    if (handleData == probingState2) {
                        next.setActive(false);
                        int i3 = this.activeNum - 1;
                        this.activeNum = i3;
                        if (i3 <= 0) {
                            this.state = probingState2;
                            break;
                        }
                    } else {
                        continue;
                    }
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
