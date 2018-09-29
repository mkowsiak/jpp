public class DebugVsReleaseDefine {
  // This time, instead of passing debug value via command line
  // we simply define it in source code
  // This is something like #define DEBUG true in C code

  // <#assign debug = "true">

  // Debugger is set to: ${debug}

  public static void main(String [] arg) {
    /*<#if debug == "true">*/ System.out.println("DEBUG message"); /*</#if>*/
    System.out.println("This code is always executed");
  }
}
