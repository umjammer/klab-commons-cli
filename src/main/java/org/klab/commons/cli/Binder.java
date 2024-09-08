/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;


/**
 * Implements complex assignment a value into a field.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 070224 nsano initial version <br>
 */
public interface Binder<T> {

    /** A command line parser context */
    interface Context {
        /** Does the {@link Option#option} exists or not. */
        boolean hasOption(String option);

        /**
         * Prints help.
         * @since 1.3.3
         */
        void printHelp();
    }

    /**
     * Implement an assignment here.
     *
     * @throws ArrayIndexOutOfBoundsException
     */
    void bind(T bean, String[] args, Context context);
}
