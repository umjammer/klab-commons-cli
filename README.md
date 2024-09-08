[![Release](https://jitpack.io/v/umjammer/klab-commons-cli.svg)](https://jitpack.io/#umjammer/klab-commons-cli)
[![Java CI](https://github.com/umjammer/klab-commons-cli/actions/workflows/maven.yml/badge.svg)](https://github.com/umjammer/klab-commons-cli/actions/workflows/maven.yml)
[![CodeQL](https://github.com/umjammer/klab-commons-cli/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/umjammer/klab-commons-cli/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-17-b07219)

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

        ︙

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

        ︙
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

        ︙
    }
```

## References

 * [apache-commons-cli](https://commons.apache.org/proper/commons-cli/)

## TODO

 * add group functionality?
 * add default value? (just set values into fields?)
 * class binding
 * rename project vavi-commomns-cli
 * BasicParser is deprecated, but Default parser doesn't pass the unit test #test08