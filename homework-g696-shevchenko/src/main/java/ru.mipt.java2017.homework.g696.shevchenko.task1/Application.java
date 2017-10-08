package ru.mipt.java2017.homework.g696.shevchenko.task1;

//import ru.mipt.java2017.homework.base.task1.ParsingException;
//import Calc.java;
import java.io.IOException;
import java.util.Scanner;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class Application {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    String expression;

    System.out.print("Expression: ");
    expression = in.nextLine();

    try {
      Calc calculator = new Calc();
      System.out.println("Result: " + calculator.calculate(expression));
    } catch (ParsingException e) {
      System.out.print("Error: " + e.getMessage());
    }
  }

}
