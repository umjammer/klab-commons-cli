/*
 * Copyright (c) 2009 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;

import java.io.File;

import org.apache.commons.cli.OptionBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * OptionsTest.
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2009/05/31 sano-n initial version <br>
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
        assertEquals(0x41, test1.b);
        assertEquals('c', test1.c);
        assertEquals(1, test1.s);
        assertEquals(2, test1.i);
        assertEquals(3l, test1.l);
        assertEquals(4.0f, test1.f, 0.1f, "0");
        assertEquals(5.0, test1.d, 0.1, "0");
        assertEquals("String", test1.string);
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
        assertEquals(4.0f, test2.f, 0.1f, (String) null);
        assertEquals(5.0, test2.d, 0.1f, (String) null);
        assertEquals("String", test2.string);
        assertEquals("posix", test2.posix);
    }

    @Options
    class Test3 {
        @Option(argName = "String", description = "string value", args = 1, option = "x", required = false)
        @Bound(binder = X_Binder.class)
        String string;
    }

    public static class X_Binder implements Binder<Test3> {
        public void bind(Test3 bean, String[] args, Context context) {
            bean.string = args[0].toUpperCase();
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

    public static class HelpHandler implements Options.ExceptionHandler<Test4> {
        public void handleException(Context<Test4> context) {
            context.printHelp();
            context.getBean().help = true;
        }
    }

    @Test
    public void test04() throws Exception {
        String[] args = { "-?" };
        Test4 test4 = new Test4();
        Options.Util.bind(args, test4);
        assertTrue(test4.help);
    }

    @Options
    static class Test5 {
        @Option(argName = "String", description = "string value", args = 1, option = "x", required = false)
        String string;
        @Argument(index = 0)
        String a0;
        @Argument(index = 1)
        @Bound(binder = A1_Binder.class)
        File a1;
        @Argument(index = 2)
        File a2;
    }

    public static class A1_Binder implements Binder<Test5> {
        public void bind(Test5 bean, String[] args, Context context) {
            bean.a1 = new File(args[0]);
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
        @Bound(binder = X_Binder7.class)
        String string;
        @Option(argName = "a", description = "int value", args = 1, option = "a", required = false)
        @Bound(binder = X_Binder7.class)
        int a;
    }

    public static class X_Binder7 implements Binder<Test7> {
        public void bind(Test7 bean, String[] args, Context context) {
            assertEquals(true, context.hasOption("x"));
            assertEquals(false, context.hasOption("y"));
            assertEquals(false, context.hasOption("yy"));
            assertEquals(false, context.hasOption("a"));
            bean.string = args[0].toUpperCase();
        }
    }

    @Test
    public void test07() throws Exception {
        String[] args = { "-x", "String" };
        Test7 test7 = new Test7();
        Options.Util.bind(args, test7);
        assertEquals("STRING", test7.string);
    }

    @Options
    class Test8 {
        @Option(argName = "sorter", description = "sort options", args = 4, option = "s", required = false)
        @Bound(binder = S_Binder8.class)
        String arg1;
        String arg2;
        String arg3;
        String arg4;
        @Argument(index = 0)
        String arg;
    }

    public static class S_Binder8 implements Binder<Test8> {
        public void bind(Test8 bean, String[] args, Context context) {
            assertEquals(true, context.hasOption("s"));
            assertTrue(args.length <= 4);
//for (String arg : args) {
// System.err.println(arg);
//}
            bean.arg1 = args.length > 0 ? args[0] : null;
            bean.arg2 = args.length > 1 ? args[1] : null;
            bean.arg3 = args.length > 2 ? args[2] : null;
            bean.arg4 = args.length > 3 ? args[3] : null;
        }
    }

    @Test
    public void test08() throws Exception {
        String[] args = { "-s", "/x/path", "desc", "datetime", "$1 xpath('/y/path/text()')" };
        Test8 test8 = new Test8();
        Options.Util.bind(args, test8);
        assertEquals("/x/path", test8.arg1);
        assertEquals("desc", test8.arg2);
        assertEquals("datetime", test8.arg3);
        assertEquals("$1 xpath('/y/path/text()')", test8.arg4);

        String[] args2 = { "-s", "/x/path", "desc" };
        Options.Util.bind(args2, test8);
        assertEquals("/x/path", test8.arg1);
        assertEquals("desc", test8.arg2);
        assertEquals(null, test8.arg3);
        assertEquals(null, test8.arg4);

        String[] args3 = { "-s", "/x/path", "desc", "3", "4", "5" };
        Options.Util.bind(args3, test8);
        assertEquals("/x/path", test8.arg1);
        assertEquals("desc", test8.arg2);
        assertEquals("3", test8.arg3);
        assertEquals("4", test8.arg4);
        assertEquals("5", test8.arg);
    }

    @Options
    @HelpOption(option = "?", description="print this help", helpHandler = ExceptionHandler9.class)
    class Test9 {
        @Option(argName = "sorter", description = "sort options", args = 4, option = "s", required = false)
        @Bound(binder = S_Binder9.class)
        String arg1;
        String arg2;
        String arg3;
        String arg4;
        @Argument(index = 0, required = true)
        String arg;
        boolean help;
    }

    public static class S_Binder9 implements Binder<Test9> {
        public void bind(Test9 bean, String[] args, Context context) {
            assertEquals(true, context.hasOption("s"));
            assertTrue(args.length <= 4);
            bean.arg1 = args.length > 0 ? args[0] : null;
            bean.arg2 = args.length > 1 ? args[1] : null;
            bean.arg3 = args.length > 2 ? args[2] : null;
            bean.arg4 = args.length > 3 ? args[3] : null;
        }
    }

    public static class ExceptionHandler9 implements Options.ExceptionHandler<Test9> {
        public void handleException(Context<Test9> context) {
            context.printHelp();
            context.getBean().help = true;
        }
    }

    @Test
    public void test09() throws Exception {
        String[] args = { "-s", "/x/path", "desc", "3", "4" };
        Test9 test9 = new Test9();
        Options.Util.bind(args, test9);
        assertTrue(test9.help);
    }

    @Options(exceptionHandler = ExceptionHandler10.class) // default exceptionHandler will call System.exit(1)
    class Test10 {
        @Option(argName = "boolean", description = "boolean value", option = "b")
        boolean b;
    }

    /** avoiding System.exit() */
    public static class ExceptionHandler10 implements Options.ExceptionHandler<Test10> {
        public void handleException(Context<Test10> context) {
            context.printHelp();
        }
    }

    @Test
    public void test10() throws Exception {
        String[] args = { "-b" };
        Test10 test10 = new Test10();
        Options.Util.bind(args, test10);
        assertTrue(test10.b);

        String[] args2 = { "-a" };
        test10 = new Test10();
        Options.Util.bind(args2, test10);
        assertTrue(!test10.b);
    }

    class Test11Super {
        @Option(argName = "String2", description = "string value2", args = 1, option = "y", required = false)
        String string2;
        @Option(argName = "b", description = "int value2", args = 1, option = "b", required = false)
        int b;
    }

    @Options(exceptionHandler = ExceptionHandler10.class) // TODO default exceptionHandler will call System.exit(1)
    class Test11 extends Test11Super {
        @Option(argName = "String", description = "string value", args = 1, option = "x", required = false)
        String string;
        @Option(argName = "a", description = "int value", args = 1, option = "a", required = false)
        int a;
    }

    @DisplayName("super class options")
    @Test
    public void test11() throws Exception {
        String[] args = { "-a", "1", "-b", "2", "-x", "xxx", "-y", "yyy" };
        Test11 test11 = new Test11();
        Options.Util.bind(args, test11);
        assertEquals(1, test11.a);
        assertEquals("xxx", test11.string);
        assertEquals(2, test11.b);
        assertEquals(test11.string2, "yyy");
    }
}

/* */
