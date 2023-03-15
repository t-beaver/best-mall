package io.dcloud.feature.audio.recorder;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import io.dcloud.feature.audio.aac.AacEncode;
import io.dcloud.feature.audio.mp3.SimpleLame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DataEncodeThread extends Thread implements AudioRecord.OnRecordPositionUpdateListener {
    public static final int PROCESS_STOP = 1;
    private byte[] mBuffer;
    private FileOutputStream mFileOutputStream;
    private String mFormat;
    private StopHandler mHandler;
    private CountDownLatch mHandlerInitLatch = new CountDownLatch(1);
    private List<Task> mTasks = Collections.synchronizedList(new ArrayList());

    static class StopHandler extends Handler {
        WeakReference<DataEncodeThread> encodeThread;

        public StopHandler(DataEncodeThread dataEncodeThread) {
            this.encodeThread = new WeakReference<>(dataEncodeThread);
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                DataEncodeThread dataEncodeThread = (DataEncodeThread) this.encodeThread.get();
                do {
                } while (dataEncodeThread.processData() > 0);
                removeCallbacksAndMessages((Object) null);
                dataEncodeThread.flushAndRelease();
                getLooper().quit();
            }
            super.handleMessage(message);
        }
    }

    public DataEncodeThread(File file, int i, String str) throws FileNotFoundException {
        this.mFileOutputStream = new FileOutputStream(file);
        double d = (double) (i * 2);
        Double.isNaN(d);
        this.mBuffer = new byte[((int) ((d * 1.25d) + 7200.0d))];
        this.mFormat = str;
    }

    /* access modifiers changed from: private */
    public void flushAndRelease() {
        int i;
        if (this.mFormat.equalsIgnoreCase("aac")) {
            try {
                byte[] offerEncoder = AacEncode.getAacEncode().offerEncoder(this.mBuffer);
                this.mBuffer = offerEncoder;
                i = offerEncoder.length;
            } catch (Exception e) {
                e.printStackTrace();
                i = 0;
            }
        } else {
            i = SimpleLame.flush(this.mBuffer);
        }
        if (i > 0) {
            try {
                this.mFileOutputStream.write(this.mBuffer, 0, i);
                FileOutputStream fileOutputStream = this.mFileOutputStream;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        this.mFileOutputStream = null;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                FileOutputStream fileOutputStream2 = this.mFileOutputStream;
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                        this.mFileOutputStream = null;
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                FileOutputStream fileOutputStream3 = this.mFileOutputStream;
                if (fileOutputStream3 != null) {
                    try {
                        fileOutputStream3.close();
                        this.mFileOutputStream = null;
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                throw th;
            }
        }
        if (this.mFormat.equalsIgnoreCase("aac")) {
            AacEncode.getAacEncode().close();
        } else {
            SimpleLame.close();
        }
    }

    /* access modifiers changed from: private */
    public int processData() {
        short[] sArr;
        int i;
        if (this.mTasks.size() <= 0) {
            return 0;
        }
        Task remove = this.mTasks.remove(0);
        short[] data = remove.getData();
        remove.getRightData();
        int readSize = remove.getReadSize();
        if (remove.getRightData() == null || remove.getRightData().length <= 0) {
            sArr = remove.getData();
        } else {
            sArr = remove.getRightData();
        }
        if (this.mFormat.equalsIgnoreCase("aac")) {
            try {
                byte[] offerEncoder = AacEncode.getAacEncode().offerEncoder(remove.getByteData());
                this.mBuffer = offerEncoder;
                i = offerEncoder.length;
            } catch (Exception e) {
                e.printStackTrace();
                i = 0;
            }
        } else {
            i = SimpleLame.encode(data, sArr, readSize, this.mBuffer);
        }
        if (i > 0) {
            try {
                this.mFileOutputStream.write(this.mBuffer, 0, i);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return readSize;
    }

    public void addTask(short[] sArr, int i) {
        this.mTasks.add(new Task(sArr, i));
    }

    public Handler getHandler() {
        try {
            this.mHandlerInitLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.mHandler;
    }

    public void onMarkerReached(AudioRecord audioRecord) {
    }

    public void onPeriodicNotification(AudioRecord audioRecord) {
        processData();
    }

    public void run() {
        Looper.prepare();
        this.mHandler = new StopHandler(this);
        this.mHandlerInitLatch.countDown();
        Looper.loop();
    }

    public void addTask(short[] sArr, short[] sArr2, int i) {
        this.mTasks.add(new Task(sArr, sArr2, i));
    }

    private class Task {
        private byte[] byteRawData;
        private short[] rawData;
        private int readSize;
        private short[] rightData;

        public Task(short[] sArr, int i) {
            this.rawData = (short[]) sArr.clone();
            this.readSize = i;
        }

        public byte[] getByteData() {
            return this.byteRawData;
        }

        public short[] getData() {
            return this.rawData;
        }

        public int getReadSize() {
            return this.readSize;
        }

        public short[] getRightData() {
            return this.rightData;
        }

        public Task(byte[] bArr, int i) {
            this.byteRawData = (byte[]) bArr.clone();
            this.readSize = i;
        }

        public Task(short[] sArr, short[] sArr2, int i) {
            this.rawData = (short[]) sArr.clone();
            this.rightData = (short[]) sArr2.clone();
            this.readSize = i;
        }
    }

    public void addTask(byte[] bArr, int i) {
        this.mTasks.add(new Task(bArr, i));
    }
}
