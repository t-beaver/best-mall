package com.dmcbig.mediapicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.FileProvider;
import com.dcloud.android.widget.toast.ToastCompat;
import com.dmcbig.mediapicker.entity.Media;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TakePhotoActivity extends Activity {
    Uri NuriForFile;
    File mTmpFile = null;

    private File createImageFile() throws IOException {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return File.createTempFile("JPEG_" + format + "_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        int i3 = i;
        super.onActivityResult(i, i2, intent);
        ArrayList arrayList = new ArrayList();
        if (i3 == 100 || (i3 == 101 && i2 == -1 && this.mTmpFile.length() > 0)) {
            arrayList.add(new Media(this.mTmpFile.getPath(), this.mTmpFile.getName(), 0, 1, this.mTmpFile.length(), 0, ""));
        }
        Intent intent2 = new Intent();
        intent2.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, arrayList);
        setResult(PickerConfig.RESULT_CODE, intent2);
        finish();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        try {
            this.mTmpFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".dmc", this.mTmpFile);
            this.NuriForFile = uriForFile;
            intent.putExtra("output", uriForFile);
            startActivityForResult(intent, 100);
            return;
        }
        File file = this.mTmpFile;
        if (file == null || !file.exists()) {
            ToastCompat.makeText((Context) this, (CharSequence) "take error", 0).show();
            finish();
            return;
        }
        intent.putExtra("output", Uri.fromFile(this.mTmpFile));
        startActivityForResult(intent, 101);
    }
}
