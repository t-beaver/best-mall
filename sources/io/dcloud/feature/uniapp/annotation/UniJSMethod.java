package io.dcloud.feature.uniapp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniJSMethod {
    public static final String NOT_SET = "_";

    String alias() default "_";

    boolean uiThread() default true;
}
