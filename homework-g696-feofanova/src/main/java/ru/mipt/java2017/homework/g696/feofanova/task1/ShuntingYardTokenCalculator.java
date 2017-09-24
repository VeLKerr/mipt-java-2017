package ru.mipt.java2017.homework.g696.feofanova.task1;

public class ShuntingYardTokenCalculator extends AbstractTokenCalculator {
  public ExpressionHandler createExpressionHandler() {
    return new ShuntingYardHandler();
  }
}
