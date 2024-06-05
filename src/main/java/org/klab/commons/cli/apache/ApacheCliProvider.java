/*
 * Copyright (c) 2009 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli.apache;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.klab.commons.cli.Binder;
import org.klab.commons.cli.Bound;
import org.klab.commons.cli.HelpOption;
import org.klab.commons.cli.spi.CliProvider;

import vavi.beans.BeanUtil;


/**
 * ApacheCliProvider.
 * <p>
 * {@link #option} は CommandLineParser のクラス名を設定します。
 * </p>
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2009/05/30 sano-n initial version <br>
 */
public class ApacheCliProvider extends CliProvider {

    /** */
    private static Logger logger = Logger.getLogger(ApacheCliProvider.class.getName());

    /* */
    @SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
    public <T> void bind(String[] sourceArgs, T destBean) {

        //
        CommandLineParser commandLineParser;
        try {
            if (option.isEmpty()) {
                option = "org.apache.commons.cli.BasicParser";
            }
            commandLineParser = (CommandLineParser) Class.forName(option).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        //
        Map<Field, Option> optionFields = new LinkedHashMap<>();
        Map<Field, Option> boundFields = new HashMap<>();

        final Options options = new Options();

        //
        org.klab.commons.cli.Options.ExceptionHandler<?> helpHandler = null;
        String helpOption = null;
        org.klab.commons.cli.HelpOption helpOptionAnnotation = Util.getHelpOption(destBean);
        if (helpOptionAnnotation != null) {
            helpHandler = HelpOption.Util.getExceptionHandler(helpOptionAnnotation);
            helpOption = helpOptionAnnotation.option();
            Option option;
            if (helpOptionAnnotation.description().length() > 0) {
                option = new Option(helpOptionAnnotation.option(), helpOptionAnnotation.description());
            } else {
                option = Option.builder(helpOptionAnnotation.option()).build();
            }
            options.addOption(option);
        }

        //
        for (Field field : Util.getOptionFields(destBean)) {
//logger.debug("field: " + field.getName());
            org.klab.commons.cli.Option optionAnnotation = field.getAnnotation(org.klab.commons.cli.Option.class);

            //
            Option option;
            if (optionAnnotation.description().length() > 0) {
                if (DefaultParser.class.isInstance(commandLineParser) && optionAnnotation.option().length() > 1) {
                    option = Option.builder().longOpt(optionAnnotation.option()).desc(optionAnnotation.description()).build();
                } else {
                    option = Option.builder(optionAnnotation.option()).desc(optionAnnotation.description()).build();
                }
            } else {
                if (DefaultParser.class.isInstance(commandLineParser) && optionAnnotation.option().length() > 1) {
                    option = Option.builder().longOpt(optionAnnotation.option()).build();
                } else {
                    option = Option.builder(optionAnnotation.option()).build();
                }
            }
            if (optionAnnotation.args() != 0) {
                option.setArgs(optionAnnotation.args());
            }
            if (optionAnnotation.argName().length() > 0) {
                option.setArgName(optionAnnotation.argName());
            }
            if (optionAnnotation.valueSeparator() != '-') {
                option.setValueSeparator(optionAnnotation.valueSeparator());
            }
            option.setRequired(optionAnnotation.required());

            options.addOption(option);

            //
            if (Bound.Util.isBound(field)) {
                boundFields.put(field, option);
            } else {
                optionFields.put(field, option);
            }
//logger.debug("@Option: " + field.getName() + ", " + option.getOpt());
        }

        optionFields.putAll(boundFields);
//logger.debug("★★★★ options: " + optionFields.size());

        CommandLine commandLine = null;
        try {
            commandLine = commandLineParser.parse(options, sourceArgs);
        } catch (ParseException e) {
            exceptionHandler.handleException(new org.klab.commons.cli.Options.ExceptionHandler.Context(e, destBean) {
                public void printHelp() {
                    new HelpFormatter().printHelp(bean.getClass().getSimpleName(), options, true);
                }
            });
            return;
        }

        if (helpOption != null && commandLine.hasOption(helpOption)) {
            helpHandler.handleException(new org.klab.commons.cli.Options.ExceptionHandler.Context(null, destBean) {
                public void printHelp() {
                    new HelpFormatter().printHelp(bean.getClass().getSimpleName(), options, true);
                }
            });
        }

        final CommandLine commandLineForBinder = commandLine;
        Binder.Context binderContext = new Binder.Context() {
            public boolean hasOption(String option) {
                return commandLineForBinder.hasOption(option);
            }
        };

        for (Field field : optionFields.keySet()) {
            Option option = optionFields.get(field);
//logger.debug("@Option: " + field.getName() + ", " + option.getOpt() + ", " + option.getLongOpt());
            String opt;
            if (DefaultParser.class.isInstance(commandLineParser) && option.getOpt() == null) {
                opt = option.getLongOpt();
            } else {
                opt = option.getOpt();
            }
            if (commandLine.hasOption(opt)) {
                if (Bound.Util.isBound(field)) {
                    Binder<T> binder = Bound.Util.getBinder(field);
                    if (option.getArgs() > 1) {
                        binder.bind(destBean, commandLine.getOptionValues(opt), binderContext);
                    } else {
                        binder.bind(destBean, new String[] { commandLine.getOptionValue(opt) }, binderContext);
                    }
                } else {
                    Class<?> fieldClass = field.getType();
                    String value = commandLine.getOptionValue(opt); // TODO check args
                    defaultBinder.bind(destBean, field, fieldClass, value, value);
                }
logger.fine(option.getArgName() + "[" + opt + "]: " + BeanUtil.getFieldValue(field, destBean));
            }
        }

        //
        for (Field field : destBean.getClass().getDeclaredFields()) {
logger.finer("field: " + field.getName());
            org.klab.commons.cli.Argument argumentAnnotation = field.getAnnotation(org.klab.commons.cli.Argument.class);
            if (argumentAnnotation == null) {
logger.finer("not @Argument: " + field.getName());
                continue;
            }

            int index = org.klab.commons.cli.Argument.Util.getIndex(field);
            boolean requied = org.klab.commons.cli.Argument.Util.isRequred(field);
            try {
                if (Bound.Util.isBound(field)) {
                    Binder<T> binder = Bound.Util.getBinder(field);
logger.finer("args[" + index + "]: " + commandLine.getArgs()[index]);
                    binder.bind(destBean, new String[] { commandLine.getArgs()[index] }, binderContext);
                } else {
                    Class<?> fieldClass = field.getType();
                    String value = commandLine.getArgs()[index];
                    defaultBinder.bind(destBean, field, fieldClass, value, value);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                if (requied) {
                    helpHandler.handleException(new org.klab.commons.cli.Options.ExceptionHandler.Context(e, destBean) {
                        public void printHelp() {
                            new HelpFormatter().printHelp(bean.getClass().getSimpleName(), options, true);
                        }
                    });
                } else {
logger.fine("args[" + index + "]: not required, ignored");
                }
            }
        }
    }
}
