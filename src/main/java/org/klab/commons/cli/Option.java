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
 * �R�}���h���C��������������t�B�[���h�ɑ΂���
 * �ݒ肵�܂��B 
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (nsano)
 * @version 0.00 080225 nsano initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {

    /** �������������̐� */
    int args() default 0;

    /** �����̖��O */
    String argName() default "";

    /** �K�{�������ǂ��� */
    boolean required() default false;

    /** �����̐��� */
    String description() default "";

    /** �����̖��O�̏ȗ��` */
    String option();
}

/* */
