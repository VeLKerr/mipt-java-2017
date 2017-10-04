package ru.mipt.java2017.homework.g696.zhukovskiy.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * One-pass recursive parser calculator realization
 *
 * @author Anatoly M. Zhukovskiy
 * @since 5.10.17
 */
public class SimpleCalculator implements Calculator {

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null expression");
    } else {
      Parser LocalParser = new Parser(expression);
      String rpnExpression = LocalParser.parseRPN();
      Evaluator Evaluator = new Evaluator(rpnExpression);
      return Evaluator.evaluateRPN();
    }
  }
}