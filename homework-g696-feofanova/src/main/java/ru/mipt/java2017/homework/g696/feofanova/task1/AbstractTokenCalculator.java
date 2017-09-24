package ru.mipt.java2017.homework.g696.feofanova.task1;

import java.util.StringTokenizer;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public abstract class AbstractTokenCalculator implements Calculator {
  public abstract ExpressionHandler createExpressionHandler();

  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null expression");
    }

    try {
      ExpressionHandler handler = createExpressionHandler();

      StringTokenizer stringTokenizer = new StringTokenizer(expression + ")",
          "\\+/*-()\t\n", true);
      double number;

      while (stringTokenizer.hasMoreTokens()) {
        String token = stringTokenizer.nextToken();

        if ((number = toDouble(token)) != -1) {
          handler.pushNumber(number);
        } else if (token.length() == 1) {
          char operand = token.charAt(0);
          handler.pushOperand(operand);
        } else {
          throw new ParsingException("Invalid symbol");
        }
      }

      return handler.getAnswer();

    } catch (ParsingException e) {
      throw new ParsingException("Invalid expression", e.getCause());
    }
  }

  private double toDouble(String token) {
    try {
      return Double.parseDouble(token);
    } catch (NumberFormatException e) {
      return -1;
    }
  }
}
