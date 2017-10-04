package ru.mipt.java2017.homework.g694.yusupov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    MyCalc calc = new MyCalc();

    System.out.println("Enter expression");
    String exp = in.nextLine();

    try {
      double res = calc.calculate(exp);
      System.out.println("result:");
      System.out.println(Double.toString(res));
    } catch (ParsingException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
