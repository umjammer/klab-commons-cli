[![Release](https://jitpack.io/v/umjammer/klab-commons-cli.svg)](https://jitpack.io/#umjammer/klab-commons-cli)
[![Actions Status](https://github.com/umjammer/klab-commons-cli/workflows/Java%20CI/badge.svg)](https://github.com/umjammer/klab-commons-cli/actions)
[![CodeQL](https://github.com/umjammer/klab-commons-cli/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/umjammer/klab-commons-cli/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-8-b07219)
[![License](https://img.shields.io/badge/License-APACHE%20LICENSE%2C%20VERSION%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# klab-commons-cli

Command Line Interface Library with Annotation

This is the wrapper of [Apache Commons CLI](http://commons.apache.org/proper/commons-cli/).

## Usage

* Before

```Java
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


* After

```Java
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
