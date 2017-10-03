package ru.mipt.java2017.homework.g696.nechepurenco.task1;

/**
 *Realisation of calculator using two stacks
 *
 * @autor Ivan Nechepurenc
 * @since 03.10.17
 */

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class SimpleCalculator implements Calculator {
  /**
   * @param expression string which we want to calculate
   * @return double value of expression if it is correct
   * @throws ParsingException if expression is incorrect
   */
  public double calculate(String expression) throws ParsingException {
    TwoStacksSolver sp = new TwoStacksSolver(expression);
    sp.parse();
    return sp.getAnswer();
  }
}
