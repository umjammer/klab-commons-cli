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
 * �R�}���h���C��������ݒ肵���� POJO �ɑ΂��Đݒ肵�܂��B
 * �R�}���h���C����������͂���v���o�C�_�[�N���X��
 * ��O�������s���N���X�A������s���N���X��ݒ肵�܂��B 
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (nsano)
 * @version 0.00 080225 nsano initial version <br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Options {

    /** �v���o�C�_�[�N���X */
    Class<? extends CliProvider> cliProvider() default org.klab.commons.cli.apache.ApacheCliProvider.class;

    /** ��O�����N���X */
    Class<? extends ExceptionHandler> exceptionHandler() default ExitExceptionHandler.class;

    /** ��������N���X */
    Class<? extends DefaultBinder> defaultBinder() default AdvancedBinder.class;

    /** {@link CliProvider} �Ɉ˂��ėp�r�͌��܂�܂��B */
    String option() default "org.apache.commons.cli.BasicParser";

    /** �R�}���h���C����͒��̗�O�������L�q����N���X�ł��B */
    public interface ExceptionHandler {

        /** �R�}���h���C����͒��̗�O�̏���ێ�����N���X�ł��B */
        public abstract class Context {
            /** */
            public Context(Exception exception, Object bean) {
                this.exception = exception;
                this.bean = bean;
            }

            /** �R�}���h���C����͒��̗�O */
            protected Exception exception;

            /** {@link Options} ��ݒ肵�� bean */
            protected Object bean;

            /** */
            public Exception getException() {
                return exception;
            }

            /** */
            public Object getBean() {
                return bean;
            }

            /** �R�}���h���C�������̃w���v��\�����܂��B */
            public abstract void printHelp();
        }

        /** ��O�������s���܂��B */
        void handleException(Context context);
    }

    /** �R�}���h���C���������������郆�[�e�B���e�B�ł��B */
    class Util {

        /** */
        public static CliProvider getCliProvider(Object bean) {
            try {
                Options options = bean.getClass().getAnnotation(Options.class);
                CliProvider commandLineParser = options.cliProvider().newInstance();
                return commandLineParser;
            } catch (Exception e) {
                throw (RuntimeException) new IllegalStateException().initCause(e);
            }
        }

        /** */
        public static ExceptionHandler getExceptionHandler(Object bean) {
            try {
                Options options = bean.getClass().getAnnotation(Options.class);
                ExceptionHandler exceptionHandler = options.exceptionHandler().newInstance();
                return exceptionHandler;
            } catch (Exception e) {
                throw (RuntimeException) new IllegalStateException().initCause(e);
            }
        }

        /** */
        public static DefaultBinder getDefaultBinder(Object bean) {
            try {
                Options options = bean.getClass().getAnnotation(Options.class);
                DefaultBinder defaultBinder = options.defaultBinder().newInstance();
                return defaultBinder;
            } catch (Exception e) {
                throw (RuntimeException) new IllegalStateException().initCause(e);
            }
        }

        /** */
        public static String getOption(Object bean) {
            Options options = bean.getClass().getAnnotation(Options.class);
            return options.option();
        }

        /**
         * POJO destBean �ɃR�}���h���C������ sourceArgs ��ݒ肵�܂��B
         * @return UTF-8 URL encoded 
         */
        public static void bind(String[] sourceArgs, Object destBean) {
            //
            Options optionsAnnotation = destBean.getClass().getAnnotation(Options.class);
            if (optionsAnnotation == null) {
                throw new IllegalArgumentException("bean is not annotated with @Options");
            }
            CliProvider cliProvider = Options.Util.getCliProvider(destBean);
            ExceptionHandler exceptionHandler = Options.Util.getExceptionHandler(destBean);
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
