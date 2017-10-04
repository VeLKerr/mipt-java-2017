package ru.mipt.java2017.homework.g696.feofanova.task1;

/**
 * ShuntingYardTokenCalculator extends AbstractTokenCalculator.
 * It's a final class, that solves the task.
 * @author Mary Feofanova
 * @since 25.09.17
 */
public class ShuntingYardTokenCalculator extends AbstractTokenCalculator {
  /**
   * Define the fabric with certain handler
   *
   * @return ShuntingYardHandler object
   */
  public ExpressionHandler createExpressionHandler() {
    return new ShuntingYardHandler();
  }
}
