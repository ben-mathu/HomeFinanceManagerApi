package com.miiguar.hfms.init;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bernard
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String columnName() default "";

    int characterLength() default 12;

    boolean unique() default false;

    boolean notNull() default true;
}
