package ru.mipt.java2017.homework.g694.nachinkin.task1;


import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.Scanner;

public class Main {

  public static void main(String ... args) {
    Scanner input = new Scanner(System.in);

    String expression = input.nextLine();

    NachinkinCalculator calculator = new NachinkinCalculator();

    try {
      System.out.println(calculator.calculate(expression));
    } catch (ParsingException e) {
      System.out.print("Error: " + e.getMessage());
    }

  }
}
