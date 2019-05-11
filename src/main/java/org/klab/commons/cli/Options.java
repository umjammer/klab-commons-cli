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
 * コマンドライン引数を設定したい POJO に対して設定します。
 * コマンドライン引数を解析するプロバイダークラスと
 * 例外処理を行うクラス、代入を行うクラスを設定します。
 *
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

    /** 例外処理クラス */
    @SuppressWarnings("rawtypes")
    Class<? extends ExceptionHandler> exceptionHandler() default ExitExceptionHandler.class;

    /** 代入処理クラス */
    Class<? extends DefaultBinder> defaultBinder() default AdvancedBinder.class;

    /** {@link CliProvider} に依って用途は決まります。 */
    String option() default "";

    /** コマンドライン解析中の例外処理を記述するクラスです。 */
    public interface ExceptionHandler<T> {

        /** コマンドライン解析中の例外の情報を保持するクラスです。 */
        public abstract class Context<T> {
            /** */
            public Context(Exception exception, T bean) {
                this.exception = exception;
                this.bean = bean;
            }

            /** コマンドライン解析中の例外 */
            protected Exception exception;

            /** {@link Options} を設定した bean */
            protected T bean;

            /** */
            public Exception getException() {
                return exception;
            }

            /** */
            public T getBean() {
                return bean;
            }

            /** コマンドライン引数のヘルプを表示します。 */
            public abstract void printHelp();
        }

        /** 例外処理を行います。 */
        void handleException(Context<T> context);
    }

    /** コマンドライン引数を処理するユーティリティです。 */
    class Util {

        private Util() {
        }

        /**
         *
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

        /** */
        public static ExceptionHandler<?> getExceptionHandler(Object bean) {
            try {
                Options options = bean.getClass().getAnnotation(Options.class);
                ExceptionHandler<?> exceptionHandler = options.exceptionHandler().getDeclaredConstructor().newInstance();
                return exceptionHandler;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /** */
        public static DefaultBinder getDefaultBinder(Object bean) {
            try {
                Options options = bean.getClass().getAnnotation(Options.class);
                DefaultBinder defaultBinder = options.defaultBinder().getDeclaredConstructor().newInstance();
                return defaultBinder;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /** */
        public static String getOption(Object bean) {
            Options options = bean.getClass().getAnnotation(Options.class);
            return options.option();
        }

        /**
         * POJO destBean にコマンドライン引数 sourceArgs を設定します。
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

/* */
