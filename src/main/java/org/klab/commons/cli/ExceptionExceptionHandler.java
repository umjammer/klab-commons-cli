/*
 * Copyright (c) 2008 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.klab.commons.cli.Options.ExceptionHandler;


/**
 * ExceptionExceptionHandler.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 080227 sano-n initial version <br>
 */
public class ExceptionExceptionHandler<T> implements ExceptionHandler<T> {
    /** */
    private static Logger logger = Logger.getLogger(ExceptionExceptionHandler.class.getName());

    /**
     * @throws IllegalStateException cause is e
     */
    public void handleException(Context<T> context) {
logger.log(Level.FINE, "ExceptionHandler", context.exception);
        context.printHelp();
        throw (RuntimeException) new IllegalStateException().initCause(context.exception);
    }
}

/* */
