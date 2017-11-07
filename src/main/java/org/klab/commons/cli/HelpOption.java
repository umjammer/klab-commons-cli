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

import org.klab.commons.cli.Options.ExceptionHandler;


/**
 * Option.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 080225 sano-n initial version <br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HelpOption {

    /** */
    String argName() default "";

    /** */
    String description() default "";

    /** */
    String option();

    /** */
    @SuppressWarnings("rawtypes")
    Class<? extends ExceptionHandler> helpHandler() default ExitExceptionHandler.class;

    /** */
    class Util {

        /** */
        public static ExceptionHandler<?> getExceptionHandler(Object bean) {
            try {
                HelpOption option = bean.getClass().getAnnotation(HelpOption.class);
                ExceptionHandler<?> exceptionHandler = option.helpHandler().newInstance();
                return exceptionHandler;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}

/* */
