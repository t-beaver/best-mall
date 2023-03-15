package io.dcloud.feature.uniapp.common;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
public @interface UniModuleAnno {
    @Deprecated
    boolean moduleMethod() default true;

    boolean runOnUIThread() default true;
}
