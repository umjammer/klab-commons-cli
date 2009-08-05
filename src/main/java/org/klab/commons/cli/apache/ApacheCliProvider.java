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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.klab.commons.cli.Binded;
import org.klab.commons.cli.Binder;
import org.klab.commons.cli.HelpOption;
import org.klab.commons.cli.spi.CliProvider;

import vavi.beans.BeanUtil;


/**
 * ApacheCliProvider. 
 * <p>
 * {@link #option} ÇÕ CommandLineParser ÇÃÉNÉâÉXñºÇê›íËÇµÇ‹Ç∑ÅB
 * </p>
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2009/05/30 nsano initial version <br>
 */
public class ApacheCliProvider extends CliProvider {

    /** */
    private static Log logger = LogFactory.getLog(ApacheCliProvider.class); 

    /* */
    @SuppressWarnings("static-access")
    public void bind(String[] sourceArgs, Object destBean) {

        //
        CommandLineParser commandLineParser;
        try {
            commandLineParser = (CommandLineParser) Class.forName(option).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        //
        Map<Field, Option> optionFields = new LinkedHashMap<Field, Option>();
        Map<Field, Option> bindedFields = new HashMap<Field, Option>();

        final Options options = new Options();

        //
        org.klab.commons.cli.Options.ExceptionHandler helpHandler = null;
        String helpOption = null;
        org.klab.commons.cli.HelpOption helpOptionAnnotation = destBean.getClass().getAnnotation(org.klab.commons.cli.HelpOption.class);
        if (helpOptionAnnotation != null) {
            helpHandler = HelpOption.Util.getExceptionHandler(destBean);
            helpOption = helpOptionAnnotation.option();
            Option option;
            if (helpOptionAnnotation.description().length() > 0) {
                option = new Option(helpOptionAnnotation.option(), helpOptionAnnotation.description());
            } else {
                option = OptionBuilder.create(helpOptionAnnotation.option());
            }
            options.addOption(option);
        }
        
        //
        for (Field field : destBean.getClass().getDeclaredFields()) {
//logger.debug("field: " + field.getName());
            org.klab.commons.cli.Option optionAnnotation = field.getAnnotation(org.klab.commons.cli.Option.class);
            if (optionAnnotation == null) {
//logger.debug("not @Option: " + field.getName());
                continue;
            }

            //
            Option option;
            if (optionAnnotation.description().length() > 0) {
                if (PosixParser.class.isInstance(commandLineParser) && optionAnnotation.option().length() > 1) {
                    option = OptionBuilder.withLongOpt(optionAnnotation.option()).withDescription(optionAnnotation.description()).create();
                } else {
                    option = new Option(optionAnnotation.option(), optionAnnotation.description());
                }
            } else {
                if (PosixParser.class.isInstance(commandLineParser) && optionAnnotation.option().length() > 1) {
                    option = OptionBuilder.withLongOpt(optionAnnotation.option()).create();
                } else {
                    option = OptionBuilder.create(optionAnnotation.option());
                }
            }
            if (optionAnnotation.args() != 0) {
                option.setArgs(optionAnnotation.args());
            }
            if (optionAnnotation.argName().length() > 0) {
                option.setArgName(optionAnnotation.argName());
            }
            option.setRequired(optionAnnotation.required());

            options.addOption(option);

            //
            if (Binded.Util.isBinded(field)) {
                bindedFields.put(field, option);
            } else {
                optionFields.put(field, option);
            }
//logger.debug("@Option: " + field.getName() + ", " + option.getOpt());
        }

        optionFields.putAll(bindedFields);
//logger.debug("ÅöÅöÅöÅö options: " + optionFields.size());

        CommandLine commandLine = null;
        try {
            commandLine = commandLineParser.parse(options, sourceArgs);
        } catch(ParseException e) {
            exceptionHandler.handleException(new org.klab.commons.cli.Options.ExceptionHandler.Context(e, destBean) {
                public void printHelp() {
                    new HelpFormatter().printHelp(bean.getClass().getSimpleName(), options, true);
                }
            });
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
            if (PosixParser.class.isInstance(commandLineParser) && option.getOpt() == null) {
                opt = option.getLongOpt();
            } else {
                opt = option.getOpt();
            }
            if (commandLine.hasOption(opt)) {
                if (Binded.Util.isBinded(field)) {
                    Binder binder = Binded.Util.getBinder(field);
                    binder.bind(destBean, commandLine.getOptionValue(opt), binderContext);
                } else {
                    Class<?> fieldClass = field.getType();
                    String value = commandLine.getOptionValue(opt); // TODO check args
                    defaultBinder.bind(destBean, field, fieldClass, value, value);
                }
logger.debug(option.getArgName() + "[" + opt + "]: " + BeanUtil.getFieldValue(field, destBean));
            }
        }

        //
        for (Field field : destBean.getClass().getDeclaredFields()) {
//logger.debug("field: " + field.getName());
            org.klab.commons.cli.Argument argumentAnnotation = field.getAnnotation(org.klab.commons.cli.Argument.class);
            if (argumentAnnotation == null) {
//logger.debug("not @Argument: " + field.getName());
                continue;
            }

            int index = org.klab.commons.cli.Argument.Util.getIndex(field);
            if (Binded.Util.isBinded(field)) {
                Binder binder = Binded.Util.getBinder(field);
//logger.debug("args[" + index + "]: " + commandLine.getArgs()[index]);
                binder.bind(destBean, commandLine.getArgs()[index], binderContext);
            } else {
                Class<?> fieldClass = field.getType();
                String value = commandLine.getArgs()[index];
                defaultBinder.bind(destBean, field, fieldClass, value, value);
            }
        }
    }
}

/* */
