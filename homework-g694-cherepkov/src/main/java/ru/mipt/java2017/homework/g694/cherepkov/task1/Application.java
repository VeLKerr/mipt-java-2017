package ru.mipt.java2017.homework.g694.cherepkov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Scanner;

public class Application {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    MyCalculator calculator = new MyCalculator();
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
