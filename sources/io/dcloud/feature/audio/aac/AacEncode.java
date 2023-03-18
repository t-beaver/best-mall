package io.dcloud.feature.audio.aac;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Surface;
import androidtranscoder.format.MediaFormatExtraConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class AacEncode {
    private static AacEncode mInstance;
    MediaCodec.BufferInfo bufferInfo;
    private HashMap<Integer, Integer> freqidxs = new HashMap<>();
    ByteBuffer[] inputBuffers = null;
    private int mChannelCount;
    private int mSampleRate;
    private MediaCodec mediaCodec;
    ByteBuffer[] outputBuffers = null;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    long presentationTimeUs = 0;

    public AacEncode(int i, int i2) {
        try {
            this.mediaCodec = MediaCodec.createEncoderByType(MediaFormatExtraConstants.MIMETYPE_AUDIO_AAC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mSampleRate = i;
        this.mChannelCount = i2;
        initFreqidxs();
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(MediaFormatExtraConstants.MIMETYPE_AUDIO_AAC, i, i2);
        createAudioFormat.setString("mime", MediaFormatExtraConstants.MIMETYPE_AUDIO_AAC);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("bitrate", new int[]{64000, 96000, 128000}[1]);
        createAudioFormat.setInteger("max-input-size", 1048576);
        this.mediaCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mediaCodec.start();
        this.inputBuffers = this.mediaCodec.getInputBuffers();
        this.outputBuffers = this.mediaCodec.getOutputBuffers();
        this.bufferInfo = new MediaCodec.BufferInfo();
    }

    private void addADTStoPacket(byte[] bArr, int i) {
        int intValue = this.freqidxs.get(Integer.valueOf(this.mSampleRate)).intValue();
        int i2 = this.mChannelCount;
        bArr[0] = -1;
        bArr[1] = -7;
        bArr[2] = (byte) ((intValue << 2) + 64 + (i2 >> 2));
        bArr[3] = (byte) (((i2 & 3) << 6) + (i >> 11));
        bArr[4] = (byte) ((i & 2047) >> 3);
        bArr[5] = (byte) (((i & 7) << 5) + 31);
        bArr[6] = -4;
    }

    private long computePresentationTime(long j) {
        return ((j * 90000) * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / ((long) this.mSampleRate);
    }

    public static AacEncode getAacEncode(int i, int i2) {
        if (mInstance == null) {
            mInstance = new AacEncode(i, i2);
        }
        return mInstance;
    }

    public void close() {
        try {
            this.mediaCodec.stop();
            this.mediaCodec.release();
            this.outputStream.flush();
            this.outputStream.close();
            mInstance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initFreqidxs() {
        this.freqidxs.put(96000, 0);
        this.freqidxs.put(88200, 1);
        this.freqidxs.put(64000, 2);
        this.freqidxs.put(48000, 3);
        this.freqidxs.put(44100, 4);
        this.freqidxs.put(32000, 5);
        this.freqidxs.put(24000, 6);
        this.freqidxs.put(22050, 7);
        this.freqidxs.put(16000, 8);
        this.freqidxs.put(12000, 9);
        this.freqidxs.put(11025, 10);
        this.freqidxs.put(8000, 11);
        this.freqidxs.put(7350, 12);
    }

    public byte[] offerEncoder(byte[] bArr) throws Exception {
        int dequeueInputBuffer = this.mediaCodec.dequeueInputBuffer(-1);
        if (dequeueInputBuffer >= 0) {
            ByteBuffer byteBuffer = this.inputBuffers[dequeueInputBuffer];
            byteBuffer.clear();
            byteBuffer.put(bArr);
            byteBuffer.limit(bArr.length);
            this.mediaCodec.queueInputBuffer(dequeueInputBuffer, 0, bArr.length, computePresentationTime(this.presentationTimeUs), 0);
            this.presentationTimeUs++;
        }
        int dequeueOutputBuffer = this.mediaCodec.dequeueOutputBuffer(this.bufferInfo, 0);
        while (dequeueOutputBuffer >= 0) {
            MediaCodec.BufferInfo bufferInfo2 = this.bufferInfo;
            int i = bufferInfo2.size;
            int i2 = i + 7;
            ByteBuffer byteBuffer2 = this.outputBuffers[dequeueOutputBuffer];
            byteBuffer2.position(bufferInfo2.offset);
            byteBuffer2.limit(this.bufferInfo.offset + i);
            byte[] bArr2 = new byte[i2];
            addADTStoPacket(bArr2, i2);
            byteBuffer2.get(bArr2, 7, i);
            byteBuffer2.position(this.bufferInfo.offset);
            this.outputStream.write(bArr2);
            this.mediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
            dequeueOutputBuffer = this.mediaCodec.dequeueOutputBuffer(this.bufferInfo, 0);
        }
        byte[] byteArray = this.outputStream.toByteArray();
        this.outputStream.flush();
        this.outputStream.reset();
        return byteArray;
    }

    public static AacEncode getAacEncode() {
        return mInstance;
    }
}
