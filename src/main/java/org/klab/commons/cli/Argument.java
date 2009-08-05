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
 * �R�}���h���C�������� {@link Option} �ȊO�̍���
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2009/05/31 nsano initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {

    /** ���� */
    int index();

    /**
     * TODO �A�m�e�[�V���������\�b�h�w��̏ꍇ 
     */
    static class Util {

        /** */
        public static int getIndex(Field field) {
            Argument argument = field.getAnnotation(Argument.class);
            return argument.index();
        }
    }
}

/* */
