package io.dcloud.g;

import android.app.Activity;
import io.dcloud.common.DHInterface.IReflectAble;
import java.util.HashMap;

interface c extends IReflectAble {
    Object a(String str);

    Object a(String str, Activity activity);

    HashMap a();
}
