package androidtranscoder.engine;

import android.media.MediaFormat;

public interface TrackTranscoder {
    MediaFormat getDeterminedFormat();

    long getWrittenPresentationTimeUs();

    boolean isFinished();

    void release();

    void setup();

    boolean stepPipeline();
}
