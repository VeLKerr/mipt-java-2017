package ru.mipt.java2017.homework.g964.derut.taskN1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Scanner;

/**
 * Created by Danila Derut g694
 */
public class Main {

  public static void main(String[] args) {
    MyCalculator myCalculator = new MyCalculator();
    Scanner in = new Scanner(System.in);
    try {
      System.out.println(myCalculator.calculate(in.nextLine()));
    } catch (ParsingException e) {
      e.printStackTrace();
    }
  }
}
