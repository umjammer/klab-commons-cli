/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
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
 * {@link Option} のフィールドの設定だけでは指定しきれない
 * 複雑な代入を行うフィールドに対して設定します。
 * 代入の実装は {@link Binder} インターフェースを実装した
 * クラスで行ってください。
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 070224 nsano initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Binded {

    /**
     * フィールドに値を代入する実装クラス
     */
    Class<? extends Binder<?>> binder();

    /**
     * 実装クラスに依って用途は違います
     */
    String value() default "";

    /**
     * TODO アノテーションがメソッド指定の場合 
     */
    static class Util {

        /** */
        public static boolean isBinded(Field field) {
            return field.getAnnotation(Binded.class) != null;
        }

        /**
         * 
         * @param field @{@link Binded} annotated field.
         * @throws NullPointerException when field is not annotated by {@link Binded}
         */
        public static <T> Binder<T> getBinder(Field field) {
            try {
                Binded binded = field.getAnnotation(Binded.class);
                @SuppressWarnings("unchecked")
                Binder<T> binder = (Binder<T>) binded.binder().newInstance();
                return binder; 
            } catch (Exception e) {
                throw (RuntimeException) new IllegalStateException().initCause(e);
            }
        }
    }
}

/* */
