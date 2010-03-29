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
 * {@link Option} �̃t�B�[���h�̐ݒ肾���ł͎w�肵����Ȃ�
 * ���G�ȑ�����s���t�B�[���h�ɑ΂��Đݒ肵�܂��B
 * ����̎����� {@link Binder} �C���^�[�t�F�[�X����������
 * �N���X�ōs���Ă��������B
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 070224 nsano initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Binded {

    /**
     * �t�B�[���h�ɒl������������N���X
     */
    Class<? extends Binder<?>> binder();

    /**
     * �����N���X�Ɉ˂��ėp�r�͈Ⴂ�܂�
     */
    String value() default "";

    /**
     * TODO �A�m�e�[�V���������\�b�h�w��̏ꍇ 
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
