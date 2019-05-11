/*
 * Copyright (c) 2009 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;


/**
 * コマンドライン引数で {@link Option} 以外の項目
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2009/05/31 sano-n initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {

    /** 順番 */
    int index();

    /** 必須かどうか */
    boolean required() default false;

    /**
     * TODO アノテーションがメソッド指定の場合
     */
    class Util {

        private Util() {
        }

        /** */
        public static int getIndex(Field field) {
            Argument argument = field.getAnnotation(Argument.class);
            return argument.index();
        }

        /** */
        public static boolean isRequred(Field field) {
            Argument argument = field.getAnnotation(Argument.class);
            return argument.required();
        }
    }
}

/* */
