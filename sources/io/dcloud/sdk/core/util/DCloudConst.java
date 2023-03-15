package io.dcloud.sdk.core.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DCloudConst {
    public static final int ORIENTATION_HORIZONTAL = 2;
    public static final int ORIENTATION_VERTICAL = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }
}
