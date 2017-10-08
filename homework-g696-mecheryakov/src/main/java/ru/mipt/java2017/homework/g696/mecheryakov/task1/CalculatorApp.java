package ru.mipt.java2017.homework.g696.mecheryakov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.Scanner;

public class CalculatorApp {
  public static void main(String[] args) throws ParsingException {
    MyCalculator calc = new MyCalculator();
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    System.out.println(calc.calculate(input));
//    System.out.println(Double.NEGATIVE_INFINITY);
//    String s = "4+5";
//    System.out.println(s.charAt(0) == '4');
//    Object f = 7.6;
//    System.out.println(f instanceof Double);
  }
}
