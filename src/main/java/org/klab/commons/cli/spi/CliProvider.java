/*
 * Copyright (c) 2009 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli.spi;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import org.klab.commons.cli.Option;
import org.klab.commons.cli.Options;
import org.klab.commons.cli.Options.ExceptionHandler;

import vavi.beans.DefaultBinder;


/**
 * CliProvider.
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2009/05/30 sano-n initial version <br>
 */
public abstract class CliProvider {

    /** */
    protected ExceptionHandler<?> exceptionHandler;

    /** */
    public void setExceptionHandler(ExceptionHandler<?> exceptionHandler) {
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
     * TODO exception handling
     *
     * @param sourceArgs
     * @param destBeans
     */
    public abstract <T> void bind(String[] sourceArgs, T destBeans);

    /** */
    public static class Util {

        private static CliProvider defaultProvider;

        static {
            ServiceLoader<CliProvider> loader = ServiceLoader.load(CliProvider.class);
            defaultProvider = loader.iterator().next();
//System.err.println("default provider: " + CliProvider.Util.defaultService());
        }

        private Util() {
        }

        /**
         * @return {@link Option} annotated fields
         */
        public static Set<Field> getOptionFields(Object bean) {
            //
            Options propsEntity = bean.getClass().getAnnotation(Options.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @Options");
            }

            //
            Set<Field> optionFields = new HashSet<>();

            Class<?> clazz = bean.getClass();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    Option property = field.getAnnotation(Option.class);
                    if (property != null) {
                        optionFields.add(field);
                    }
                }
                clazz = clazz.getSuperclass();
            }

            return optionFields;
        }

        /** */
        public static CliProvider defaultService() {
            return defaultProvider;
        }
    }
}

/* */
