package ru.mipt.java2017.homework.g696.mosyagin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * Calculator implementation that uses state machines.
 *
 * @author Mosyagin Mikhail
 */
public class StateMachineCalculator implements Calculator {

  /**
   * Evaluates a valid infix mathematical expression.
   * Expression should only contain decimal number, '+', '-', '*', '/' operators,
   * brackets and white spaces.
   *
   * @param expression the expression string
   * @return the result of the expression
   * @throws ParsingException if unable to parse the expression
   */
  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Cannot parse null string");
    }

    ParsingMachine parser = new ParsingMachine();
    for (int index = 0; index < expression.length(); index++) {
      parser.transit(expression.charAt(index));
    }
    parser.transit(')');

    if (!parser.hasFinished()) {
      throw new ParsingException("Cannot find the end of the expression");
    }

    return parser.getResult().evaluateExpression();
  }
}
