package ru.mipt.java2017.homework.g694.kozinov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.Scanner;

public class Application {
  public static void main(String[] args) {
    while (true) {
      Scanner in = new Scanner(System.in);
      String expression;
      MyCalculator calc = new MyCalculator();
      System.out.print("Expression: ");
      expression = in.nextLine();
      try {
        System.out.println("Result: " + calc.calculate(expression));
      } catch (ParsingException e) {
        System.out.print("Error: " + e.getMessage());
      }
    }
  }
}
