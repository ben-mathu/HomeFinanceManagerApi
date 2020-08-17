package com.miiguar.hfms.init;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bernard
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraint {
    String name() default "";

    String columnName();

    /**
     * Defines the name of the table being referenced
     */
    String tableName() default "";
}
