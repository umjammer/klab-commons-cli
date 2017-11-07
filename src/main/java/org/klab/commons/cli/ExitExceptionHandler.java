/*
 * Copyright (c) 2008 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.klab.commons.cli.Options.ExceptionHandler;


/**
 * ExitExceptionHandler.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 080227 sano-n initial version <br>
 */
public class ExitExceptionHandler<T> implements ExceptionHandler<T> {
    /** */
    private static Log logger = LogFactory.getLog(ExitExceptionHandler.class);

    /* */
    public void handleException(Context<T> context) {
logger.debug("ExceptionHandler", context.exception);
        context.printHelp();
        System.exit(1);
    }
}

/* */
