/*
 * Copyright (c) 2009 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli.spi;

import org.klab.commons.cli.DefaultBinder;
import org.klab.commons.cli.Options.ExceptionHandler;


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

    /** 様々な用途に使用されます */
    protected String option; 

    /** */
    public void setOption(String option) {
        this.option = option;
    }

    /**
     * コマンドライン引数の文字列配列 sourceArgs を destBeans のフィールドの値として
     * 設定します。
     * 
     * @param sourceArgs
     * @param destBeans
     */
    public abstract void bind(String[] sourceArgs, Object destBeans);
}

/* */
