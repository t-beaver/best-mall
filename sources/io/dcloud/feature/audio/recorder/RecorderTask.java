package io.dcloud.feature.audio.recorder;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.Message;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.feature.audio.aac.AacEncode;
import io.dcloud.feature.audio.mp3.SimpleLame;
import io.dcloud.feature.audio.recorder.HighGradeRecorder;
import io.dcloud.feature.gg.dcloud.ADSim;
import java.io.File;
import java.io.FileNotFoundException;

public class RecorderTask extends Thread {
    public static final int DEFAULT_LAME_MP3_BIT_RATE = 128;
    private static final int DEFAULT_LAME_MP3_QUALITY = 5;
    private static final int FRAME_COUNT = 220;
    private static final String TAG = "RecorderTask";
    private AudioRecord audioRecord = null;
    int bufsize = -2;
    private final int[] configs = {16, 12};
    private long duration = 0;
    private final int[] formats = {2, 3};
    Handler handler;
    private byte[] mAacBuffer;
    /* access modifiers changed from: private */
    public double mDuration;
    /* access modifiers changed from: private */
    public HighGradeRecorder.Callback mDurationListener;
    private DataEncodeThread mEncodeThread;
    private String mFormat;
    private short[] mPCMBuffer;
    HighGradeRecorder mRecorder;
    private boolean mShouldRecord = false;
    private boolean mShouldRun = false;
    /* access modifiers changed from: private */
    public int maxDuration;
    private File outputFile;
    boolean reallyStart;
    private int[] sampleRates = {44100, 22050, 11025, 8000};
    private long startTime = 0;
    int waitingTime;

    public RecorderTask(File file, HighGradeRecorder highGradeRecorder, RecordOption recordOption) {
        this.outputFile = file;
        this.mRecorder = highGradeRecorder;
        this.handler = new Handler();
        this.mFormat = recordOption.mFormat;
        if (!recordOption.isRateDeft) {
            this.sampleRates = new int[]{recordOption.mSamplingRate, 44100, 22050, 11025, 8000};
        }
        if (highGradeRecorder.getRecorderState() == 1) {
            this.waitingTime = 1000;
        } else {
            this.waitingTime = ADSim.INTISPLSH;
        }
    }

    private double calVolume(short[] sArr, double d) {
        long j = 0;
        for (int i = 0; i < sArr.length; i++) {
            j += (long) (sArr[i] * sArr[i]);
        }
        double d2 = (double) j;
        Double.isNaN(d2);
        return Math.log10(d2 / d) * 10.0d;
    }

    private void cancel() {
        stopRecord();
    }

    private void createRecord(int i, int i2, int i3) {
        this.audioRecord = new AudioRecord(1, i, i2, i3, this.bufsize);
    }

    private void init() {
        int i = 2;
        if (this.audioRecord.getAudioFormat() != 2) {
            i = 1;
        }
        int i2 = this.bufsize / i;
        int i3 = i2 % FRAME_COUNT;
        if (i3 != 0) {
            this.bufsize = (i2 + (220 - i3)) * i;
        }
        int i4 = this.bufsize;
        this.mPCMBuffer = new short[i4];
        this.mAacBuffer = new byte[i4];
        if (this.mFormat.equalsIgnoreCase("aac")) {
            AacEncode.getAacEncode(this.audioRecord.getSampleRate(), this.audioRecord.getChannelCount());
        } else {
            SimpleLame.init(this.audioRecord.getSampleRate(), this.audioRecord.getChannelCount(), this.audioRecord.getSampleRate(), 128, 5);
        }
        try {
            if (!this.outputFile.exists()) {
                File parentFile = this.outputFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                this.outputFile.createNewFile();
            }
            DataEncodeThread dataEncodeThread = new DataEncodeThread(this.outputFile, this.bufsize, this.mFormat);
            this.mEncodeThread = dataEncodeThread;
            dataEncodeThread.start();
            AudioRecord audioRecord2 = this.audioRecord;
            DataEncodeThread dataEncodeThread2 = this.mEncodeThread;
            audioRecord2.setRecordPositionUpdateListener(dataEncodeThread2, dataEncodeThread2.getHandler());
            this.audioRecord.setPositionNotificationPeriod(FRAME_COUNT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private boolean isFound() {
        boolean z = false;
        int i = 0;
        while (!z) {
            int[] iArr = this.formats;
            if (i >= iArr.length) {
                break;
            }
            int i2 = iArr[i];
            int i3 = 0;
            while (!z) {
                int[] iArr2 = this.sampleRates;
                if (i3 >= iArr2.length) {
                    break;
                }
                int i4 = iArr2[i3];
                int i5 = 0;
                while (true) {
                    if (z) {
                        break;
                    }
                    int[] iArr3 = this.configs;
                    if (i5 >= iArr3.length) {
                        break;
                    }
                    int i6 = iArr3[i5];
                    Logger.e(TAG, "Trying to create AudioRecord use: " + i2 + "/" + i6 + "/" + i4);
                    this.bufsize = AudioRecord.getMinBufferSize(i4, i6, i2);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Bufsize: ");
                    sb.append(this.bufsize);
                    Logger.e(TAG, sb.toString());
                    int i7 = this.bufsize;
                    if (-2 == i7) {
                        Logger.i(TAG, "invaild params!");
                    } else if (-1 == i7) {
                        Logger.i(TAG, "Unable to query hardware!");
                    } else {
                        try {
                            createRecord(i4, i6, i2);
                            if (this.audioRecord.getState() == 1) {
                                z = true;
                                break;
                            }
                        } catch (IllegalStateException unused) {
                            Logger.i(TAG, "Failed to set up recorder!");
                            this.audioRecord = null;
                        }
                    }
                    i5++;
                }
                i3++;
            }
            i++;
        }
        return z;
    }

    private int mapFormat(int i) {
        if (i != 2) {
            return i != 3 ? 0 : 8;
        }
        return 16;
    }

    public int getDuration() {
        return (int) this.mDuration;
    }

    public boolean isRecording() {
        return this.mShouldRecord;
    }

    public void pauseRecord() {
        this.mShouldRecord = false;
    }

    public void resumeRecord() {
        this.mShouldRecord = true;
    }

    public void run() {
        super.run();
        if (!isFound()) {
            Logger.e(TAG, "Sample rate, channel config or format not supported!");
            return;
        }
        init();
        this.mShouldRun = true;
        int sampleRate = ((this.audioRecord.getSampleRate() * mapFormat(this.audioRecord.getAudioFormat())) / 8) * this.audioRecord.getChannelCount();
        this.mDuration = 0.0d;
        boolean z = false;
        while (this.mShouldRun) {
            boolean z2 = this.mShouldRecord;
            if (z2 != z) {
                if (z2) {
                    this.startTime = System.currentTimeMillis();
                    try {
                        this.audioRecord.startRecording();
                        if (this.mDuration == 0.0d) {
                            this.reallyStart = true;
                            RecorderUtil.postTaskSafely(new Runnable() {
                                public void run() {
                                    RecorderTask.this.mRecorder.onstart();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    this.audioRecord.stop();
                }
                z = this.mShouldRecord;
            }
            if (this.mShouldRecord) {
                if (this.mFormat.equalsIgnoreCase("aac")) {
                    int read = this.audioRecord.read(this.mAacBuffer, 0, this.bufsize);
                    if (read > 0) {
                        this.mEncodeThread.addTask(this.mAacBuffer, read);
                    }
                } else {
                    int read2 = this.audioRecord.read(this.mPCMBuffer, 0, this.bufsize);
                    if (read2 > 0) {
                        double d = (double) read2;
                        Double.isNaN(d);
                        double d2 = (double) sampleRate;
                        Double.isNaN(d2);
                        double d3 = ((1000.0d * d) * 2.0d) / d2;
                        final double calVolume = calVolume(this.mPCMBuffer, d);
                        this.mDuration += d3;
                        if (this.mDurationListener != null) {
                            RecorderUtil.postTaskSafely(new Runnable() {
                                public void run() {
                                    RecorderTask.this.mDurationListener.onRecording(RecorderTask.this.mDuration, calVolume);
                                    if (RecorderTask.this.maxDuration > 0 && RecorderTask.this.mDuration >= ((double) RecorderTask.this.maxDuration)) {
                                        RecorderTask.this.mRecorder.stop(2);
                                        RecorderTask.this.mDurationListener.onMaxDurationReached();
                                    }
                                }
                            });
                        }
                        AudioRecord audioRecord2 = this.audioRecord;
                        if (audioRecord2 == null || audioRecord2.getChannelCount() != 1) {
                            AudioRecord audioRecord3 = this.audioRecord;
                            if (audioRecord3 != null && audioRecord3.getChannelCount() == 2) {
                                int i = read2 / 2;
                                short[] sArr = new short[i];
                                short[] sArr2 = new short[i];
                                for (int i2 = 0; i2 < i; i2 += 2) {
                                    short[] sArr3 = this.mPCMBuffer;
                                    int i3 = i2 * 2;
                                    sArr[i2] = sArr3[i3];
                                    int i4 = i3 + 1;
                                    if (i4 < read2) {
                                        sArr[i2 + 1] = sArr3[i4];
                                    }
                                    int i5 = i3 + 2;
                                    if (i5 < read2) {
                                        sArr2[i2] = sArr3[i5];
                                    }
                                    int i6 = i3 + 3;
                                    if (i6 < read2) {
                                        sArr2[i2 + 1] = sArr3[i6];
                                    }
                                }
                                this.mEncodeThread.addTask(sArr, sArr2, i);
                            }
                        } else {
                            this.mEncodeThread.addTask(this.mPCMBuffer, read2);
                        }
                    }
                }
            }
        }
    }

    public void setCallback(HighGradeRecorder.Callback callback) {
        this.mDurationListener = callback;
    }

    public void setMaxDuration(int i) {
        this.maxDuration = i;
    }

    public void startRecording() {
        this.mShouldRecord = true;
    }

    public void stopRecord() {
        this.mShouldRecord = false;
        this.mShouldRun = false;
        AudioRecord audioRecord2 = this.audioRecord;
        if (audioRecord2 != null) {
            audioRecord2.stop();
            this.audioRecord.release();
            this.audioRecord = null;
        }
        Message.obtain(this.mEncodeThread.getHandler(), 1).sendToTarget();
    }
}
