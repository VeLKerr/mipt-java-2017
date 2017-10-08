package ru.mipt.java2017.homework.g696.marchenko.task1.MyParser;

public class UnPlusNode extends MyExpression {

  private MyExpression expression;

  UnPlusNode(MyExpression expr) {
    expression = expr;
  }

  @Override
  public double count() {
    return expression.count();
  }
}
