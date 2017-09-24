package ru.mipt.java2017.homework.g696.feofanova.task1;

/**
 * ShuntingYardTokenCalculator extends AbstractTokenCalculator. It's a final class, that solves the
 * task.
 */
public class ShuntingYardTokenCalculator extends AbstractTokenCalculator {
    // define the fabric with certain handler.
    public ExpressionHandler createExpressionHandler() {
    return new ShuntingYardHandler();
  }
}
