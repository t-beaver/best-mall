package org.mozilla.universalchardet;

import io.dcloud.common.DHInterface.IApp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.mozilla.universalchardet.prober.CharsetProber;
import org.mozilla.universalchardet.prober.EscCharsetProber;
import org.mozilla.universalchardet.prober.MBCSGroupProber;

public class UniversalDetector {
    public static final float MINIMUM_THRESHOLD = 0.2f;
    public static final float SHORTCUT_THRESHOLD = 0.95f;
    private String detectedCharset;
    private boolean done;
    private CharsetProber escCharsetProber;
    private boolean gotData;
    private InputState inputState;
    private byte lastChar;
    private CharsetListener listener;
    private CharsetProber[] probers;
    private boolean start;

    public enum InputState {
        PURE_ASCII,
        ESC_ASCII,
        HIGHBYTE
    }

    public UniversalDetector() {
        this((CharsetListener) null);
    }

    public static String detectCharset(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            byte[] bArr = new byte[4096];
            UniversalDetector universalDetector = new UniversalDetector((CharsetListener) null);
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read <= 0 || universalDetector.isDone()) {
                    universalDetector.dataEnd();
                    String detectedCharset2 = universalDetector.getDetectedCharset();
                    universalDetector.reset();
                } else {
                    universalDetector.handleData(bArr, 0, read);
                }
            }
            universalDetector.dataEnd();
            String detectedCharset22 = universalDetector.getDetectedCharset();
            universalDetector.reset();
            fileInputStream.close();
            return detectedCharset22;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public void dataEnd() {
        CharsetProber[] charsetProberArr;
        if (this.gotData) {
            String str = this.detectedCharset;
            if (str != null) {
                this.done = true;
                CharsetListener charsetListener = this.listener;
                if (charsetListener != null) {
                    charsetListener.report(str);
                }
            } else if (this.inputState == InputState.HIGHBYTE) {
                float f = 0.0f;
                int i = 0;
                int i2 = 0;
                while (true) {
                    charsetProberArr = this.probers;
                    if (i >= charsetProberArr.length) {
                        break;
                    }
                    float confidence = charsetProberArr[i].getConfidence();
                    if (confidence > f) {
                        i2 = i;
                        f = confidence;
                    }
                    i++;
                }
                if (f > 0.2f) {
                    String charSetName = charsetProberArr[i2].getCharSetName();
                    this.detectedCharset = charSetName;
                    CharsetListener charsetListener2 = this.listener;
                    if (charsetListener2 != null) {
                        charsetListener2.report(charSetName);
                    }
                }
            } else {
                InputState inputState2 = InputState.ESC_ASCII;
            }
        }
    }

    public String getDetectedCharset() {
        return this.detectedCharset;
    }

    public CharsetListener getListener() {
        return this.listener;
    }

    public void handleData(byte[] bArr) {
        handleData(bArr, 0, bArr.length);
    }

    public boolean isDone() {
        return this.done;
    }

    public void reset() {
        int i = 0;
        this.done = false;
        this.start = true;
        this.detectedCharset = null;
        this.gotData = false;
        this.inputState = InputState.PURE_ASCII;
        this.lastChar = 0;
        CharsetProber charsetProber = this.escCharsetProber;
        if (charsetProber != null) {
            charsetProber.reset();
        }
        while (true) {
            CharsetProber[] charsetProberArr = this.probers;
            if (i < charsetProberArr.length) {
                if (charsetProberArr[i] != null) {
                    charsetProberArr[i].reset();
                }
                i++;
            } else {
                return;
            }
        }
    }

    public void setListener(CharsetListener charsetListener) {
        this.listener = charsetListener;
    }

    public UniversalDetector(CharsetListener charsetListener) {
        this.listener = charsetListener;
        this.escCharsetProber = null;
        this.probers = new CharsetProber[1];
        reset();
    }

    public void handleData(byte[] bArr, int i, int i2) {
        if (!this.done) {
            if (i2 > 0) {
                this.gotData = true;
            }
            int i3 = 0;
            if (this.start) {
                this.start = false;
                if (i2 > 3) {
                    byte b = bArr[i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
                    byte b2 = bArr[i + 1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
                    byte b3 = bArr[i + 2] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
                    byte b4 = bArr[i + 3] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
                    if (b != 0) {
                        if (b != 239) {
                            if (b != 254) {
                                if (b == 255) {
                                    if (b2 == 254 && b3 == 0 && b4 == 0) {
                                        this.detectedCharset = Constants.CHARSET_UTF_32LE;
                                    } else if (b2 == 254) {
                                        this.detectedCharset = Constants.CHARSET_UTF_16LE;
                                    }
                                }
                            } else if (b2 == 255 && b3 == 0 && b4 == 0) {
                                this.detectedCharset = Constants.CHARSET_X_ISO_10646_UCS_4_3412;
                            } else if (b2 == 255) {
                                this.detectedCharset = Constants.CHARSET_UTF_16BE;
                            }
                        } else if (b2 == 187 && b3 == 191) {
                            this.detectedCharset = Constants.CHARSET_UTF_8;
                        }
                    } else if (b2 == 0 && b3 == 254 && b4 == 255) {
                        this.detectedCharset = Constants.CHARSET_UTF_32BE;
                    } else if (b2 == 0 && b3 == 255 && b4 == 254) {
                        this.detectedCharset = Constants.CHARSET_X_ISO_10646_UCS_4_2143;
                    }
                    if (this.detectedCharset != null) {
                        this.done = true;
                        return;
                    }
                }
            }
            int i4 = i + i2;
            for (int i5 = i; i5 < i4; i5++) {
                byte b5 = bArr[i5] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
                if ((b5 & 128) == 0 || b5 == 160) {
                    if (this.inputState == InputState.PURE_ASCII && (b5 == 27 || (b5 == 123 && this.lastChar == 126))) {
                        this.inputState = InputState.ESC_ASCII;
                    }
                    this.lastChar = bArr[i5];
                } else {
                    InputState inputState2 = this.inputState;
                    InputState inputState3 = InputState.HIGHBYTE;
                    if (inputState2 != inputState3) {
                        this.inputState = inputState3;
                        if (this.escCharsetProber != null) {
                            this.escCharsetProber = null;
                        }
                        CharsetProber[] charsetProberArr = this.probers;
                        if (charsetProberArr[0] == null) {
                            charsetProberArr[0] = new MBCSGroupProber();
                        }
                    }
                }
            }
            InputState inputState4 = this.inputState;
            if (inputState4 == InputState.ESC_ASCII) {
                if (this.escCharsetProber == null) {
                    this.escCharsetProber = new EscCharsetProber();
                }
                if (this.escCharsetProber.handleData(bArr, i, i2) == CharsetProber.ProbingState.FOUND_IT) {
                    this.done = true;
                    this.detectedCharset = this.escCharsetProber.getCharSetName();
                }
            } else if (inputState4 == InputState.HIGHBYTE) {
                while (true) {
                    CharsetProber[] charsetProberArr2 = this.probers;
                    if (i3 >= charsetProberArr2.length) {
                        return;
                    }
                    if (charsetProberArr2[i3].handleData(bArr, i, i2) == CharsetProber.ProbingState.FOUND_IT) {
                        this.done = true;
                        this.detectedCharset = this.probers[i3].getCharSetName();
                        return;
                    }
                    i3++;
                }
            }
        }
    }
}
