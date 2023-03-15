package androidtranscoder.engine;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class QueuedMuxer {
    private static final int BUFFER_SIZE = 65536;
    private static final String TAG = "QueuedMuxer";
    private MediaFormat mAudioFormat;
    private int mAudioTrackIndex;
    private ByteBuffer mByteBuffer;
    private final Listener mListener;
    private final MediaMuxer mMuxer;
    private final List<SampleInfo> mSampleInfoList = new ArrayList();
    private boolean mStarted;
    private MediaFormat mVideoFormat;
    private int mVideoTrackIndex;

    /* renamed from: androidtranscoder.engine.QueuedMuxer$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidtranscoder$engine$QueuedMuxer$SampleType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                androidtranscoder.engine.QueuedMuxer$SampleType[] r0 = androidtranscoder.engine.QueuedMuxer.SampleType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$androidtranscoder$engine$QueuedMuxer$SampleType = r0
                androidtranscoder.engine.QueuedMuxer$SampleType r1 = androidtranscoder.engine.QueuedMuxer.SampleType.VIDEO     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$androidtranscoder$engine$QueuedMuxer$SampleType     // Catch:{ NoSuchFieldError -> 0x001d }
                androidtranscoder.engine.QueuedMuxer$SampleType r1 = androidtranscoder.engine.QueuedMuxer.SampleType.AUDIO     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidtranscoder.engine.QueuedMuxer.AnonymousClass1.<clinit>():void");
        }
    }

    public interface Listener {
        void onDetermineOutputFormat();
    }

    private static class SampleInfo {
        private final int mFlags;
        private final long mPresentationTimeUs;
        /* access modifiers changed from: private */
        public final SampleType mSampleType;
        /* access modifiers changed from: private */
        public final int mSize;

        /* synthetic */ SampleInfo(SampleType sampleType, int i, MediaCodec.BufferInfo bufferInfo, AnonymousClass1 r4) {
            this(sampleType, i, bufferInfo);
        }

        /* access modifiers changed from: private */
        public void writeToBufferInfo(MediaCodec.BufferInfo bufferInfo, int i) {
            bufferInfo.set(i, this.mSize, this.mPresentationTimeUs, this.mFlags);
        }

        private SampleInfo(SampleType sampleType, int i, MediaCodec.BufferInfo bufferInfo) {
            this.mSampleType = sampleType;
            this.mSize = i;
            this.mPresentationTimeUs = bufferInfo.presentationTimeUs;
            this.mFlags = bufferInfo.flags;
        }
    }

    public enum SampleType {
        VIDEO,
        AUDIO
    }

    public QueuedMuxer(MediaMuxer mediaMuxer, Listener listener) {
        this.mMuxer = mediaMuxer;
        this.mListener = listener;
    }

    private int getTrackIndexForSampleType(SampleType sampleType) {
        int i = AnonymousClass1.$SwitchMap$androidtranscoder$engine$QueuedMuxer$SampleType[sampleType.ordinal()];
        if (i == 1) {
            return this.mVideoTrackIndex;
        }
        if (i == 2) {
            return this.mAudioTrackIndex;
        }
        throw new AssertionError();
    }

    private void onSetOutputFormat() {
        if (this.mVideoFormat != null && this.mAudioFormat != null) {
            this.mListener.onDetermineOutputFormat();
            this.mVideoTrackIndex = this.mMuxer.addTrack(this.mVideoFormat);
            Log.v(TAG, "Added track #" + this.mVideoTrackIndex + " with " + this.mVideoFormat.getString("mime") + " to muxer");
            this.mAudioTrackIndex = this.mMuxer.addTrack(this.mAudioFormat);
            Log.v(TAG, "Added track #" + this.mAudioTrackIndex + " with " + this.mAudioFormat.getString("mime") + " to muxer");
            this.mMuxer.start();
            this.mStarted = true;
            int i = 0;
            if (this.mByteBuffer == null) {
                this.mByteBuffer = ByteBuffer.allocate(0);
            }
            this.mByteBuffer.flip();
            Log.v(TAG, "Output format determined, writing " + this.mSampleInfoList.size() + " samples / " + this.mByteBuffer.limit() + " bytes to muxer.");
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            for (SampleInfo next : this.mSampleInfoList) {
                next.writeToBufferInfo(bufferInfo, i);
                this.mMuxer.writeSampleData(getTrackIndexForSampleType(next.mSampleType), this.mByteBuffer, bufferInfo);
                i += next.mSize;
            }
            this.mSampleInfoList.clear();
            this.mByteBuffer = null;
        }
    }

    public void setOutputFormat(SampleType sampleType, MediaFormat mediaFormat) {
        int i = AnonymousClass1.$SwitchMap$androidtranscoder$engine$QueuedMuxer$SampleType[sampleType.ordinal()];
        if (i == 1) {
            this.mVideoFormat = mediaFormat;
        } else if (i == 2) {
            this.mAudioFormat = mediaFormat;
        } else {
            throw new AssertionError();
        }
        onSetOutputFormat();
    }

    public void writeSampleData(SampleType sampleType, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (this.mStarted) {
            this.mMuxer.writeSampleData(getTrackIndexForSampleType(sampleType), byteBuffer, bufferInfo);
            return;
        }
        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
        byteBuffer.position(bufferInfo.offset);
        if (this.mByteBuffer == null) {
            this.mByteBuffer = ByteBuffer.allocateDirect(65536).order(ByteOrder.nativeOrder());
        }
        this.mByteBuffer.put(byteBuffer);
        this.mSampleInfoList.add(new SampleInfo(sampleType, bufferInfo.size, bufferInfo, (AnonymousClass1) null));
    }
}
