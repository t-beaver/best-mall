package androidtranscoder.format;

import android.media.MediaFormat;

public interface MediaFormatStrategy {
    MediaFormat createAudioOutputFormat(MediaFormat mediaFormat);

    MediaFormat createVideoOutputFormat(MediaFormat mediaFormat);
}
