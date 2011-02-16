/*
 * Copyright (c) 2008 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * コマンドライン引数を代入するフィールドに対して
 * 設定します。 
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 080225 sano-n initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {

    /** 引数が取る引数の数 */
    int args() default 0;

    /** 引数の名前 */
    String argName() default "";

    /** 必須引数かどうか */
    boolean required() default false;

    /** 引数の説明 */
    String description() default "";

    /** 引数の名前の省略形 */
    String option();
}

/* */
