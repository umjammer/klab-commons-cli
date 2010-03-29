/*
 * Copyright (c) 2009 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;

import java.io.File;

import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * OptionsTest. 
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2009/05/31 nsano initial version <br>
 */
public class OptionsTest {

    @Options
    class Test1 {
        @Option(argName = "byte", description = "byte value", args = 1, option = "b", required = false)
        byte b;
        @Option(argName = "char", description = "char value", args = 1, option = "c", required = false)
        char c;
        @Option(argName = "short", description = "short value", args = 1, option = "s", required = false)
        short s;
        @Option(argName = "int", description = "int value", args = 1, option = "i", required = false)
        int i;
        @Option(argName = "long", description = "long value", args = 1, option = "l", required = false)
        long l;
        @Option(argName = "float", description = "float value", args = 1, option = "f", required = false)
        float f;
        @Option(argName = "double", description = "double value", args = 1, option = "d", required = false)
        double d;
        @Option(argName = "String", description = "string value", args = 1, option = "x", required = false)
        String string;
    }

    @Test
    public void test01() throws Exception {
        String[] args = { "-b", "65", "-c", "c", "-s", "1", "-i", "2", "-l", "3", "-f", "4.0", "-d", "5.0", "-x", "String" };
        Test1 test1 = new Test1();
        Options.Util.bind(args, test1);
        assertEquals(test1.b, 0x41);
        assertEquals(test1.c, 'c');
        assertEquals(test1.s, 1);
        assertEquals(test1.i, 2);
        assertEquals(test1.l, 3l);
        assertEquals("0", test1.f, 4.0f, 0);
        assertEquals("0", test1.d, 5.0, 0);
        assertEquals(test1.string, "String");
    }

    @Options(option = "org.apache.commons.cli.PosixParser")
    class Test2 {
        @Option(argName = "byte", description = "byte value", args = 1, option = "b", required = false)
        byte b;
        @Option(argName = "char", description = "char value", args = 1, option = "c", required = false)
        char c;
        @Option(argName = "short", description = "short value", args = 1, option = "s", required = false)
        short s;
        @Option(argName = "int", description = "int value", args = 1, option = "i", required = false)
        int i;
        @Option(argName = "long", description = "long value", args = 1, option = "l", required = false)
        long l;
        @Option(argName = "float", description = "float value", args = 1, option = "f", required = false)
        float f;
        @Option(argName = "double", description = "double value", args = 1, option = "d", required = false)
        double d;
        @Option(argName = "String", description = "string value", args = 1, option = "x", required = false)
        String string;
        @Option(argName = "Posix", description = "posix value", args = 1, option = "posix", required = false)
        String posix;
    }

    @Test
    public void test02() throws Exception {
        String[] args = { "-b", "65", "-c", "c", "-s", "1", "-i", "2", "-l", "3", "-f", "4.0", "-d", "5.0", "-x", "String", "--posix", "posix" };
        Test2 test2 = new Test2();
        Options.Util.bind(args, test2);
        assertEquals(0x41, test2.b);
        assertEquals('c', test2.c);
        assertEquals(1, test2.s);
        assertEquals(2, test2.i);
        assertEquals(3l, test2.l);
        assertEquals(null, 4.0f, test2.f, 0);
        assertEquals(null, 5.0, test2.d, 0);
        assertEquals("String", test2.string);
        assertEquals("posix", test2.posix);
    }

    @Options
    class Test3 {
        @Option(argName = "String", description = "string value", args = 1, option = "x", required = false)
        @Binded(binder = X_Binder.class)
        String string;
    }

    public static class X_Binder implements Binder<Test3> {
        public void bind(Test3 bean, String arg, Context context) {
            bean.string = arg.toUpperCase();
        }
    }

    @Test
    public void test03() throws Exception {
        String[] args = { "-x", "String" };
        Test3 test3 = new Test3();
        Options.Util.bind(args, test3);
        assertEquals("STRING", test3.string);
    }

    @Options
    @HelpOption(option = "?", helpHandler = HelpHandler.class)
    class Test4 {
        @Option(argName = "String", description = "string value", args = 1, option = "x", required = false)
        String string;
        boolean help;
    }

    public static class HelpHandler implements Options.ExceptionHandler {
        public void handleException(Context context) {
            context.printHelp();
            Test4.class.cast(context.getBean()).help = true;
        }
    }

    @Test
    public void test04() throws Exception {
        String[] args = { "-?" };
        Test4 test4 = new Test4();
        Options.Util.bind(args, test4);
        assertEquals(true, test4.help);
    }

    @Options
    static class Test5 {
        @Option(argName = "String", description = "string value", args = 1, option = "x", required = false)
        String string;
        @Argument(index = 0)
        String a0;
        @Argument(index = 1)
        @Binded(binder = A1_Binder.class)
        File a1;
        @Argument(index = 2)
        File a2;
    }

    public static class A1_Binder implements Binder<Test5> {
        public void bind(Test5 bean, String arg, Context context) {
            bean.a1= new File(arg);
        }
    }

    @Test
    public void test05() throws Exception {
        String[] args = { "-x", "aaa", "args0", "args1", "args2" };
        Test5 test5 = new Test5();
        Options.Util.bind(args, test5);
        assertEquals("aaa", test5.string);
        assertEquals("args0", test5.a0);
        assertEquals("args1", test5.a1.getName());
        assertEquals("args2", test5.a2.getName());
    }

    @Test
    @SuppressWarnings("static-access")
    public void test06() throws Exception {
        org.apache.commons.cli.Option option = OptionBuilder
          .hasArg(true)
          .withArgName("id for mbv file")
          .isRequired(false)
          .withDescription("set the mbv file id")
          .withLongOpt("id")
          .create();
System.err.println(option);                      
    }
    
    @Options
    class Test7 {
        @Option(argName = "String", description = "string value", args = 1, option = "x", required = false)
        @Binded(binder = X_Binder7.class)
        String string;
        @Option(argName = "a", description = "int value", args = 1, option = "a", required = false)
        @Binded(binder = X_Binder7.class)
        int a;
    }

    public static class X_Binder7 implements Binder<Test7> {
        public void bind(Test7 bean, String arg, Context context) {
            assertEquals(true, context.hasOption("x"));
            assertEquals(false, context.hasOption("y"));
            assertEquals(false, context.hasOption("yy"));
            assertEquals(false, context.hasOption("a"));
            bean.string = arg.toUpperCase();
        }
    }

    @Test
    public void test07() throws Exception {
        String[] args = { "-x", "String" };
        Test7 test7 = new Test7();
        Options.Util.bind(args, test7);
        assertEquals("STRING", test7.string);
    }
}

/* */
