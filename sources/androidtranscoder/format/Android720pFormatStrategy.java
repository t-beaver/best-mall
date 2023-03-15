package androidtranscoder.format;

import android.media.MediaFormat;

class Android720pFormatStrategy implements MediaFormatStrategy {
    public static final int AUDIO_BITRATE_AS_IS = -1;
    public static final int AUDIO_CHANNELS_AS_IS = -1;
    private static final int DEFAULT_VIDEO_BITRATE = 8000000;
    private static final int LONGER_LENGTH = 1280;
    private static final int SHORTER_LENGTH = 720;
    private static final String TAG = "720pFormatStrategy";
    private int compressLevel = 1;
    private final int mAudioBitrate = 128000;
    private final int mAudioChannels = 1;
    private int mVideoBitrate;
    private double resolution = 1.0d;

    public Android720pFormatStrategy(int i, double d) {
        this.compressLevel = i;
        this.resolution = d;
    }

    public MediaFormat createAudioOutputFormat(MediaFormat mediaFormat) {
        if (this.mAudioBitrate == -1 || this.mAudioChannels == -1) {
            return null;
        }
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(MediaFormatExtraConstants.MIMETYPE_AUDIO_AAC, mediaFormat.getInteger("sample-rate"), this.mAudioChannels);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("bitrate", this.mAudioBitrate);
        return createAudioFormat;
    }

    public MediaFormat createVideoOutputFormat(MediaFormat mediaFormat) {
        int i;
        int i2;
        double d;
        double d2;
        int integer = mediaFormat.getInteger("width");
        int integer2 = mediaFormat.getInteger("height");
        double d3 = this.resolution;
        if (d3 == 1.0d) {
            int i3 = this.compressLevel;
            if (i3 == 1) {
                d = (double) integer;
                d2 = 0.8d;
            } else if (i3 == 2) {
                d = (double) integer;
                d2 = 0.5d;
            } else {
                d = (double) integer;
                d2 = 0.3d;
            }
            Double.isNaN(d);
            i2 = (int) (d * d2);
            double d4 = (double) integer2;
            Double.isNaN(d4);
            i = (int) (d4 * d2);
        } else {
            double d5 = (double) integer;
            Double.isNaN(d5);
            i2 = (int) (d5 * d3);
            double d6 = (double) integer2;
            Double.isNaN(d6);
            i = (int) (d6 * d3);
        }
        if (i2 % 2 > 0) {
            i2++;
        }
        if (i % 2 > 0) {
            i++;
        }
        this.mVideoBitrate = i2 * i;
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(MediaFormatExtraConstants.MIMETYPE_VIDEO_AVC, i2, i);
        createVideoFormat.setInteger("bitrate", this.mVideoBitrate);
        createVideoFormat.setInteger("frame-rate", 25);
        createVideoFormat.setInteger("i-frame-interval", 3);
        createVideoFormat.setInteger("color-format", 2130708361);
        return createVideoFormat;
    }
}
