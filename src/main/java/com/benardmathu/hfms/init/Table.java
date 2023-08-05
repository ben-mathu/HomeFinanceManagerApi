package com.benardmathu.hfms.init;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a table and its attributes, e.g table name
 * or constraint
 * @author bernard
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    String tableName() default "";

    Constraint[] constraint() default {};
}
