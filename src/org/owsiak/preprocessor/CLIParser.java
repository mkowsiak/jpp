package org.owsiak.preprocessor;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.*;
import java.util.*;

public class CLIParser {

  private static final Logger logger = LogManager.getLogger(CLIParser.class);

  // Storage for the CLI arguments
  private String sourceDir = null;
  private String destDir   = null;
  private String fileName  = null;
  private Properties properties = null;

  public void parseArguments(String [] args) throws Exception {

    // we have to make extra parsing to get help switch
    // the issue here is that we have arguments that are required
    // and they contradict with -h and --help
    boolean hasHelp = checkForHelp( args );

    Options options  = new Options();

    Option help      = Option.builder( "h" )
                             .longOpt( "help" )
                             .desc( "print this message" )
                             .build();

    Option version   = Option.builder( "v" )
                             .longOpt( "version" )
                             .desc( "print the version information and exit" )
                             .build();

    Option srcDir    = Option.builder( "s" )
                             .argName( "path" )
                             .hasArg()
                             .required(true)
                             .longOpt( "sourcepath" )
                             .desc( "directory where source file is located" )
                             .build();

    Option outDir    = Option.builder( "d" )
                             .argName( "path" )
                             .hasArg()
                             .required(true)
                             .longOpt( "destination" )
                             .desc( "directory where parsed file should be stored" )
                             .build();

    Option property  = Option.builder( "D" )
                             .hasArgs()
                             .numberOfArgs(2)
                             .argName( "property=value" )
                             .valueSeparator()
                             .desc( "use value for given property" )
                             .build();

    Option file      = Option.builder( "f" )
                             .argName( "filename" )
                             .hasArg()
                             .required(true)
                             .longOpt( "file" )
                             .desc(  "file name to preprocess\n" 
                                   + "it can be file name with package name:"
                                   + " e.g. somepackage/SomeClass.java")
                             .build();

    options.addOption( help );
    options.addOption( version );
    options.addOption( property );
    options.addOption( srcDir );
    options.addOption( outDir );
    options.addOption( file );

    if(hasHelp) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.setWidth(120);
      formatter.setDescPadding(5);
      formatter.setLeftPadding(3);
      formatter.setOptionComparator(null);
      String header =  "\n" 
                     + "Preprocess Java source code for debug/release compilation\n\n";
      String footer =  "\n"
                     + "sample: \n"
                     + "  jpp.sh -Ddebug=true -s sample/src -d sample/preprocessed -f Test.java";

      StringWriter out = new StringWriter();
      PrintWriter pw = new PrintWriter(out);

      formatter.printHelp(pw, 80, "jpp.sh", header, options, 2, 2, footer, true);

      pw.flush();

      logger.info(out.toString());

      System.exit(1);
    }

    CommandLineParser parser = new DefaultParser();
    CommandLine cmdline = parser.parse(options, args);
    
    properties = cmdline.getOptionProperties( "D" );

    if (cmdline.hasOption("s")) {
      sourceDir = cmdline.getOptionValue( "s" );
    }

    if (cmdline.hasOption("d")) {
      destDir = cmdline.getOptionValue( "d" );
    }

    if (cmdline.hasOption("f")) {
      fileName = cmdline.getOptionValue( "f" );
    }

    if(   fileName == null 
       || fileName.length() == 0
       || sourceDir == null
       || sourceDir.length() == 0
       || destDir == null
       || destDir.length() == 0) {
      logger.info("Make sure to pass all required arguments. Try to use -h.");
      System.exit(1);
    }
  }

  // I need this one to cheat the command line args parser.
  // I am looking here for -h or --help and if I find it, I won't parse
  // arguments later. I will just show help screen.
  private boolean checkForHelp(String[] args) throws Exception { 
    boolean hasHelp = false;
    Options options = new Options();

    Option help    = Option.builder( "h" )
                           .longOpt( "help" )
                           .build();

    Option version = Option.builder( "v" )
                           .longOpt( "version" )
                           .build();
    
    options.addOption(help);
    options.addOption(version);

    // we need that to overcome issue related to unknown args
    List<String> reduced = new ArrayList<>();
    for (String arg : args) {
      if (options.hasOption(arg)) {
        reduced.add(arg);
      }
    }

    CommandLineParser parser = new DefaultParser();
    CommandLine cmdline = parser.parse(options, reduced.toArray(new String[reduced.size()]));

    if (cmdline.hasOption("h")) {
      hasHelp = true;
    }

    // we can show version together with other stuff
    if (cmdline.hasOption("v")) {
      logger.info("java preprocessor version " + JavaPreprocessor.version );
      System.exit(1);
    }

    return hasHelp;
  }

  public String getSourceDir() {
    return sourceDir;
  }

  public String getDestDir() {
    return destDir;
  }

  public String getFileName() {
    return fileName;
  }

  public Properties getProperties() {
    return properties;
  }

}

