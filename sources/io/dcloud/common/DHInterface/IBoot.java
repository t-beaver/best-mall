package io.dcloud.common.DHInterface;

import android.content.Context;
import android.os.Bundle;

public interface IBoot extends ISysEventListener {
    void onPause();

    void onRestart(Context context);

    void onResume();

    void onStart(Context context, Bundle bundle, String[] strArr);

    void onStop();
}
