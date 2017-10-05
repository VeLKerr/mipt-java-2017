package ru.mipt.java2017.homework.g697.vorobev;

import com.sun.xml.internal.ws.server.ServerRtException;
import jdk.nashorn.internal.codegen.DumpBytecode;
import ru.mipt.java2017.homework.g697.vorobev.MySimpleCalculator;

import java.util.SortedMap;

/**
 * Hello world!
 */
public class App {


  public static void main(String[] args) {
//      String text = "(-0.0 + (-3) - 3 + ((  -2)) + 0.00)";
//      text = text.replaceAll("0+.?0+(\\D+)", "0$1");
//      text = text.replaceAll("\\(\\s*-\\s*0([^\\d.])", "(o$1");
//      text = text.replaceAll("\\(\\s*-", "(0-");
//      System.out.println(text);
//      System.out.println(new Double(-0.0));

    String text = "6.0 + (-4) * ((0.0 + 5/2)";
    MySimpleCalculator c = new MySimpleCalculator();
    try
    {
      System.out.println( c.calculate(text));
    }
    catch (Exception e)
    {
      System.out.println("EXCEPTION!!!");
    }
  }


}
