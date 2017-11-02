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
public @interface Bound {

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
        public static boolean isBound(Field field) {
            return field.getAnnotation(Bound.class) != null;
        }

        /**
         * 
         * @param field @{@link Bound} annotated field.
         * @throws NullPointerException when field is not annotated by {@link Bound}
         */
        public static <T> Binder<T> getBinder(Field field) {
            try {
                Bound bound = field.getAnnotation(Bound.class);
                @SuppressWarnings("unchecked")
                Binder<T> binder = (Binder<T>) bound.binder().newInstance();
                return binder; 
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}

/* */
