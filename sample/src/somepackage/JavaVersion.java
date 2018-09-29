package somepackage;

public class JavaVersion {

  public static void main(String [] arg) {
    /*<#if (version?eval >= 9) >*/
    Runtime.Version jvmversion = Runtime.version();
    System.out.println(  "major: "    + jvmversion.major() + " "
                       + "minor: "    + jvmversion.minor() + " "
                       + "security: " + jvmversion.security());  
    /*<#else>*/
    System.out.println(System.getProperty("java.version"));
    /*</#if>*/
  }

}
