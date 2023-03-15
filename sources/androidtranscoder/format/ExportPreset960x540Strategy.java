package androidtranscoder.format;

import android.media.MediaFormat;
import android.util.Log;
import io.dcloud.common.util.StringUtil;

class ExportPreset960x540Strategy implements MediaFormatStrategy {
    private static final String TAG = "ExportPreset960x540Strategy";

    ExportPreset960x540Strategy() {
    }

    public MediaFormat createAudioOutputFormat(MediaFormat mediaFormat) {
        return null;
    }

    public MediaFormat createVideoOutputFormat(MediaFormat mediaFormat) {
        int integer = mediaFormat.getInteger("width");
        int integer2 = mediaFormat.getInteger("height");
        MediaFormat exportPreset960x540 = MediaFormatPresets.getExportPreset960x540(integer, integer2);
        Log.d(TAG, StringUtil.format("inputFormat: %dx%d => outputFormat: %dx%d", Integer.valueOf(integer), Integer.valueOf(integer2), Integer.valueOf(exportPreset960x540.getInteger("width")), Integer.valueOf(exportPreset960x540.getInteger("height"))));
        return exportPreset960x540;
    }
}
