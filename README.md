klab-commons-cli
================

Command Line Interface Library with Annotation

This is wrapper of Apache Commons CLI.

# Usage

## Before

```
        Options options = new Options();

        options.addOption(OptionBuilder
            .hasArg(true)
            .withArgName("output path")
            .isRequired(true)
            .withDescription("set the output path")
            .create('d')
        );

        :

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            new HelpFormatter().printHelp("Foo", options, true);
            System.exit(-1);
        }

        Foo foo = new Foo();

        if (cmd.hasOption('d')) {
            foo.outDir = cmd.getOptionValue('d');
        }

        :
```


## After

```
@Options
public class Foo {

    @Option(argName = "output path", option = "d", args = 1, required = true, description = "set the output path")
    private String outDir;

    :

    public static void main(String[] args) throws Exception {
        Foo foo = new Foo();

        Options.Util.bind(args, foo);

        :
    }
```

# Depends

* http://code.google.com/p/vavi-commons/source/browse/#svn/trunk/vavi-commons
