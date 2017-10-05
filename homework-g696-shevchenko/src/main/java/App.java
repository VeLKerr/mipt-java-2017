package ru.mipt.java2017.homework.g696.shevchenko.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import Calc.java;
import java.util.Scanner;

public class Application {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    Calc calculator = new Calc();
    String expression;

    System.out.print("Expression: ");
    expression = in.nextLine();

    try {
      System.out.println("Result: " + calculator.calculate(expression));
    } catch (ParsingException e) {
      System.out.print("Error: " + e.getMessage());
    }
  }

}
