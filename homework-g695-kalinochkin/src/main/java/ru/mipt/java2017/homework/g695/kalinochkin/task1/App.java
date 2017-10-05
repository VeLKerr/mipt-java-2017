package ru.mipt.java2017.homework.g695.kalinochkin.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Scanner;

public class App {

  public static void main(String[] args) {
    MyCalculator calc = new MyCalculator();
    String expression;
    if (args.length >= 1) {
      expression = args[0];
    } else {
      System.out.print("Enter an expression: ");
      Scanner scanner = new Scanner(System.in);
      expression = scanner.nextLine();
    }
    try {
      double result = calc.calculate(expression);
      System.out.print("Result: ");
      System.out.println(result);
    } catch (ParsingException e) {
      // We don't want to see ugly stack traces if an exception is thrown
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
