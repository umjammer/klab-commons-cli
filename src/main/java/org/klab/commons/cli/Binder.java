/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;


/**
 * �t�B�[���h�ւ̕��G�ȑ�����������܂��B 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 070224 nsano initial version <br>
 */
public interface Binder {

    /** �R�}���h���C��������͂̏�Ԃ�\���N���X�ł��B */
    public abstract class Context {
        /** �w�肵�� {@link Option#option} �����݂��Ă��邩�ǂ����B */
        public abstract boolean hasOption(String option);
    }

    /** �����ő�����������܂��B */
    void bind(Object bean, String arg, Context context);
}

/* */
