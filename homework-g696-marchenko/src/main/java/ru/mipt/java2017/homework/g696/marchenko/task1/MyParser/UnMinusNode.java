package ru.mipt.java2017.homework.g696.marchenko.task1.MyParser;

public class UnMinusNode extends MyExpression {

  private MyExpression expression;

  UnMinusNode(MyExpression expr) {
    expression = expr;
  }

  @Override
  public double count() {
    return -expression.count();
  }
}
