package org.owsiak.preprocessor;

import freemarker.template.*;
import java.util.*;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class JavaPreprocessor {

  private static final Logger logger = LogManager.getLogger(JavaPreprocessor.class);
  public static String version = "2.7";

  public static void main(String[] args) throws Exception {

    if(args == null || args.length == 0) {
      logger.info("You have to specify arguments. Try to use -h."); 
      System.exit(1);
    }

    JavaPreprocessor pp = new JavaPreprocessor();
    CLIParser cli = new CLIParser();

    try {
      cli.parseArguments( args );
    } catch(Exception ex) {
      logger.error(ex.getMessage());
      System.exit(1);
    }
    
    try {
      pp.preprocessFile( cli.getFileName(), 
                         cli.getSourceDir(), 
                         cli.getDestDir(), 
                         cli.getProperties() );
    } catch(Exception ex) {
      logger.error(ex.getMessage());
      System.exit(1);
    }
  }

  private void preprocessFile( String fileName, 
                               String sourceDir, 
                               String destDir, 
                               Properties properties) 
                 throws Exception {

    Path   srcFilePath = Paths.get(fileName);
    String srcFileName = srcFilePath.getFileName().toString();
    String dirName     = srcFilePath.getParent() == null ? "" : srcFilePath.getParent().toString();

    Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
    cfg.setDirectoryForTemplateLoading(new File(  sourceDir 
                                                + File.separator 
                                                + dirName));
    Template temp = cfg.getTemplate(srcFileName);
      
    Path outputPath = Paths.get(  destDir 
                                + File.separator 
                                + dirName);

    Files.createDirectories(outputPath);

    Writer out = new FileWriter(  destDir 
                                + File.separator 
                                + dirName 
                                + File.separator 
                                + srcFileName);

    temp.process(properties, out);
  }

}
