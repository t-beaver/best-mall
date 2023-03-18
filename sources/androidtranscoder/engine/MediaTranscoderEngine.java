package androidtranscoder.engine;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.os.Build;
import android.util.Log;
import androidtranscoder.engine.QueuedMuxer;
import androidtranscoder.format.MediaFormatStrategy;
import androidtranscoder.utils.ISO6709LocationParser;
import androidtranscoder.utils.MediaExtractorUtils;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Objects;

public class MediaTranscoderEngine {
    private static final long PROGRESS_INTERVAL_STEPS = 10;
    private static final double PROGRESS_UNKNOWN = -1.0d;
    private static final long SLEEP_TO_WAIT_TRACK_TRANSCODERS = 10;
    private static final String TAG = "MediaTranscoderEngine";
    /* access modifiers changed from: private */
    public TrackTranscoder mAudioTrackTranscoder;
    private long mDurationUs;
    private MediaExtractor mExtractor;
    private FileDescriptor mInputFileDescriptor;
    private MediaMuxer mMuxer;
    private volatile double mProgress;
    private ProgressCallback mProgressCallback;
    /* access modifiers changed from: private */
    public TrackTranscoder mVideoTrackTranscoder;

    public interface ProgressCallback {
        void onProgress(double d);
    }

    private void runPipelines() throws InterruptedException {
        double d;
        if (this.mDurationUs <= 0) {
            this.mProgress = PROGRESS_UNKNOWN;
            ProgressCallback progressCallback = this.mProgressCallback;
            if (progressCallback != null) {
                progressCallback.onProgress(PROGRESS_UNKNOWN);
            }
        }
        long j = 0;
        while (true) {
            if (!this.mVideoTrackTranscoder.isFinished() || !this.mAudioTrackTranscoder.isFinished()) {
                boolean z = this.mVideoTrackTranscoder.stepPipeline() || this.mAudioTrackTranscoder.stepPipeline();
                j++;
                if (this.mDurationUs > 0 && j % 10 == 0) {
                    double d2 = 1.0d;
                    if (this.mVideoTrackTranscoder.isFinished()) {
                        d = 1.0d;
                    } else {
                        double writtenPresentationTimeUs = (double) this.mVideoTrackTranscoder.getWrittenPresentationTimeUs();
                        double d3 = (double) this.mDurationUs;
                        Double.isNaN(writtenPresentationTimeUs);
                        Double.isNaN(d3);
                        d = Math.min(1.0d, writtenPresentationTimeUs / d3);
                    }
                    if (!this.mAudioTrackTranscoder.isFinished()) {
                        double writtenPresentationTimeUs2 = (double) this.mAudioTrackTranscoder.getWrittenPresentationTimeUs();
                        double d4 = (double) this.mDurationUs;
                        Double.isNaN(writtenPresentationTimeUs2);
                        Double.isNaN(d4);
                        d2 = Math.min(1.0d, writtenPresentationTimeUs2 / d4);
                    }
                    double d5 = (d + d2) / 2.0d;
                    this.mProgress = d5;
                    ProgressCallback progressCallback2 = this.mProgressCallback;
                    if (progressCallback2 != null) {
                        progressCallback2.onProgress(d5);
                    }
                }
                if (!z) {
                    Thread.sleep(10);
                }
            } else {
                return;
            }
        }
    }

    private void setupMetadata() throws IOException {
        String extractMetadata;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.mInputFileDescriptor);
        try {
            this.mMuxer.setOrientationHint(Integer.parseInt(mediaMetadataRetriever.extractMetadata(24)));
        } catch (NumberFormatException unused) {
        }
        if (Build.VERSION.SDK_INT >= 19 && (extractMetadata = mediaMetadataRetriever.extractMetadata(23)) != null) {
            float[] parse = new ISO6709LocationParser().parse(extractMetadata);
            if (parse != null) {
                this.mMuxer.setLocation(parse[0], parse[1]);
            } else {
                Log.d(TAG, "Failed to parse the location metadata: " + extractMetadata);
            }
        }
        try {
            this.mDurationUs = Long.parseLong(mediaMetadataRetriever.extractMetadata(9)) * 1000;
        } catch (NumberFormatException unused2) {
            this.mDurationUs = -1;
        }
        Log.d(TAG, "Duration (us): " + this.mDurationUs);
    }

    private void setupTrackTranscoders(MediaFormatStrategy mediaFormatStrategy) {
        MediaExtractorUtils.TrackResult firstVideoAndAudioTrack = MediaExtractorUtils.getFirstVideoAndAudioTrack(this.mExtractor);
        MediaFormat createVideoOutputFormat = mediaFormatStrategy.createVideoOutputFormat(firstVideoAndAudioTrack.mVideoTrackFormat);
        MediaFormat createAudioOutputFormat = mediaFormatStrategy.createAudioOutputFormat(firstVideoAndAudioTrack.mAudioTrackFormat);
        if (createVideoOutputFormat == null && createAudioOutputFormat == null) {
            throw new InvalidOutputFormatException("MediaFormatStrategy returned pass-through for both video and audio. No transcoding is necessary.");
        }
        QueuedMuxer queuedMuxer = new QueuedMuxer(this.mMuxer, new QueuedMuxer.Listener() {
            public void onDetermineOutputFormat() {
                MediaFormatValidator.validateVideoOutputFormat(MediaTranscoderEngine.this.mVideoTrackTranscoder.getDeterminedFormat());
                MediaFormatValidator.validateAudioOutputFormat(MediaTranscoderEngine.this.mAudioTrackTranscoder.getDeterminedFormat());
            }
        });
        if (createVideoOutputFormat == null) {
            this.mVideoTrackTranscoder = new PassThroughTrackTranscoder(this.mExtractor, firstVideoAndAudioTrack.mVideoTrackIndex, queuedMuxer, QueuedMuxer.SampleType.VIDEO);
        } else {
            this.mVideoTrackTranscoder = new VideoTrackTranscoder(this.mExtractor, firstVideoAndAudioTrack.mVideoTrackIndex, createVideoOutputFormat, queuedMuxer);
        }
        this.mVideoTrackTranscoder.setup();
        PassThroughTrackTranscoder passThroughTrackTranscoder = new PassThroughTrackTranscoder(this.mExtractor, firstVideoAndAudioTrack.mAudioTrackIndex, queuedMuxer, QueuedMuxer.SampleType.AUDIO);
        this.mAudioTrackTranscoder = passThroughTrackTranscoder;
        passThroughTrackTranscoder.setup();
        this.mExtractor.selectTrack(firstVideoAndAudioTrack.mVideoTrackIndex);
        this.mExtractor.selectTrack(firstVideoAndAudioTrack.mAudioTrackIndex);
    }

    public double getProgress() {
        return this.mProgress;
    }

    public ProgressCallback getProgressCallback() {
        return this.mProgressCallback;
    }

    public void setDataSource(FileDescriptor fileDescriptor) {
        this.mInputFileDescriptor = fileDescriptor;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.mProgressCallback = progressCallback;
    }

    public void transcodeVideo(String str, MediaFormatStrategy mediaFormatStrategy) throws IOException, InterruptedException {
        Objects.requireNonNull(str, "Output path cannot be null.");
        if (this.mInputFileDescriptor != null) {
            try {
                MediaExtractor mediaExtractor = new MediaExtractor();
                this.mExtractor = mediaExtractor;
                mediaExtractor.setDataSource(this.mInputFileDescriptor);
                this.mMuxer = new MediaMuxer(str, 0);
                setupMetadata();
                setupTrackTranscoders(mediaFormatStrategy);
                runPipelines();
                this.mMuxer.stop();
                try {
                    try {
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Failed to release muxer.", e);
                    }
                } catch (RuntimeException e2) {
                    throw new Error("Could not shutdown extractor, codecs and muxer pipeline.", e2);
                }
            } finally {
                try {
                    TrackTranscoder trackTranscoder = this.mVideoTrackTranscoder;
                    if (trackTranscoder != null) {
                        trackTranscoder.release();
                        this.mVideoTrackTranscoder = null;
                    }
                    TrackTranscoder trackTranscoder2 = this.mAudioTrackTranscoder;
                    if (trackTranscoder2 != null) {
                        trackTranscoder2.release();
                        this.mAudioTrackTranscoder = null;
                    }
                    MediaExtractor mediaExtractor2 = this.mExtractor;
                    if (mediaExtractor2 != null) {
                        mediaExtractor2.release();
                        this.mExtractor = null;
                    }
                    try {
                        MediaMuxer mediaMuxer = this.mMuxer;
                        if (mediaMuxer != null) {
                            mediaMuxer.release();
                            this.mMuxer = null;
                        }
                    } catch (RuntimeException e3) {
                        Log.e(TAG, "Failed to release muxer.", e3);
                    }
                } catch (RuntimeException e4) {
                    throw new Error("Could not shutdown extractor, codecs and muxer pipeline.", e4);
                }
            }
        } else {
            throw new IllegalStateException("Data source is not set.");
        }
    }
}
