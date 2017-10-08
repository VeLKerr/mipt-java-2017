package ru.mipt.java2017.homework.g696.marchenko.task1.MyParser;

public class NumberNode extends MyExpression {

  private double number;

  NumberNode(double num) {
    number = num;
  }

  @Override
  public double count() {
    return number;
  }
}
