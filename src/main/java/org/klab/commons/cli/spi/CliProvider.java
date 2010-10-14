/*
 * Copyright (c) 2009 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli.spi;

import org.klab.commons.cli.Options.ExceptionHandler;

import vavi.beans.DefaultBinder;


/**
 * CliProvider. 
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2009/05/30 nsano initial version <br>
 */
public abstract class CliProvider {

    /** */
    protected ExceptionHandler exceptionHandler;
    
    /** */
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    /** */
    protected DefaultBinder defaultBinder;

    /** */
    public void setDefaultBinder(DefaultBinder defaultBinder) {
        this.defaultBinder = defaultBinder;
    }

    /** �l�X�ȗp�r�Ɏg�p����܂� */
    protected String option; 

    /** */
    public void setOption(String option) {
        this.option = option;
    }

    /**
     * �R�}���h���C�������̕�����z�� sourceArgs �� destBeans �̃t�B�[���h�̒l�Ƃ���
     * �ݒ肵�܂��B
     * 
     * TODO exception handling
     * 
     * @param sourceArgs
     * @param destBeans
     */
    public abstract <T> void bind(String[] sourceArgs, T destBeans);
}

/* */
