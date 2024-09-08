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

import org.klab.commons.cli.spi.CliProvider;

import vavi.beans.DefaultBinder;


/**
 * Set this annotation to a POJO that you want to assign values into.
 * Set a command line parser provider and an exception handlar class
 * and binder (do complex assignment) class also.
 * <p>
 * Usage:
 * <pre><code>
 *     @Options
 *     class Pojo {
 *       @Option
 *       String style;
 *     }
 *
 *     static void main(String[] args) {
 *         Options.Util.bind(pojo, args);
 *          :
 *     }
 * </code></pre>
 * </p>
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 080225 sano-n initial version <br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Options {

    /**
     * Specifies a provider class. If not specified, the default provider will be used.
     * The default provider class is written in the file <code>META-INF/services/org.commons.cli.spi.CliProveder</code>
     */
    Class<? extends CliProvider> cliProvider() default org.klab.commons.cli.spi.CliProvider.class;

    /** the class that handles exceptions */
    Class<? extends ExceptionHandler> exceptionHandler() default ExitExceptionHandler.class;

    /** the class that binds field values */
    Class<? extends DefaultBinder> defaultBinder() default AdvancedBinder.class;

    /** Usage depends on {@link CliProvider}. */
    String option() default "";

    /** This class represents an exception handler */
    interface ExceptionHandler<T> {

        /** A context for an exception handler */
        abstract class Context<T> {

            /** Creates a context for an exception handler. */
            public Context(Exception exception, T bean) {
                this.exception = exception;
                this.bean = bean;
            }

            /** An exception that occurs during parsing command line. */
            protected Exception exception;

            /** A bean annotated {@link Options} */
            protected T bean;

            /** Gets an exception */
            public Exception getException() {
                return exception;
            }

            /** A target bean (POJO) */
            public T getBean() {
                return bean;
            }

            /** Prints help. */
            public abstract void printHelp();
        }

        /** Handles an exception. */
        void handleException(Context<T> context);
    }

    /**
     * Utilities for @Options.
     *
     * @see #bind(String[], Object)
     */
    class Util {

        private Util() {
        }

        /**
         * @see Options#cliProvider()
         */
        public static CliProvider getCliProvider(Object bean) {
            try {
                Options options = bean.getClass().getAnnotation(Options.class);
                Class<? extends CliProvider> provider = options.cliProvider();
                if (provider.equals(org.klab.commons.cli.spi.CliProvider.class)) {
                    return CliProvider.Util.defaultService();
                } else {
                    return provider.getDeclaredConstructor().newInstance();
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * @see Options#exceptionHandler()
         */
        public static ExceptionHandler<?> getExceptionHandler(Object bean) {
            try {
                Options options = bean.getClass().getAnnotation(Options.class);
                ExceptionHandler<?> exceptionHandler = options.exceptionHandler().getDeclaredConstructor().newInstance();
                return exceptionHandler;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * @see Options#defaultBinder()
         */
        public static DefaultBinder getDefaultBinder(Object bean) {
            try {
                Options options = bean.getClass().getAnnotation(Options.class);
                DefaultBinder defaultBinder = options.defaultBinder().getDeclaredConstructor().newInstance();
                return defaultBinder;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * @see Options#option()
         * @return value is depends on {@link Options#cliProvider()}
         */
        public static String getOption(Object bean) {
            Options options = bean.getClass().getAnnotation(Options.class);
            return options.option();
        }

        /**
         * Inject values from sourceArgs into POJO destBean.
         */
        public static void bind(String[] sourceArgs, Object destBean) {
            //
            Options optionsAnnotation = destBean.getClass().getAnnotation(Options.class);
            if (optionsAnnotation == null) {
                throw new IllegalArgumentException("bean is not annotated with @Options");
            }

            CliProvider cliProvider = Options.Util.getCliProvider(destBean);
            ExceptionHandler<?> exceptionHandler = Options.Util.getExceptionHandler(destBean);
            DefaultBinder defaultBinder = Options.Util.getDefaultBinder(destBean);
            String option = Options.Util.getOption(destBean);

            cliProvider.setExceptionHandler(exceptionHandler);
            cliProvider.setDefaultBinder(defaultBinder);
            cliProvider.setOption(option);
            cliProvider.bind(sourceArgs, destBean);
        }
    }
}
