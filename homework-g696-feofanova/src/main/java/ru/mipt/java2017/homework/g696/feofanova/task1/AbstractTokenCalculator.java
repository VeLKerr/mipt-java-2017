package ru.mipt.java2017.homework.g696.feofanova.task1;

import java.util.StringTokenizer;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * AbstractTokenCalculator implements interface Calculator from base. This class realises expression
 * parsing using StringTokenizer. Also it used interface ExpressionHandler, that calculate
 * expressions.
 * @author Mary Feofanova
 * @since 25.09.17
 */
public abstract class AbstractTokenCalculator implements Calculator {
  /**
   * Fabric of handlers
   *
   * @return ExpressionHandler object
   */
  public abstract ExpressionHandler createExpressionHandler();

  /**
   * Main method, that calculates something
   *
   * @param expression string with arithmetic expression that should be calculate
   * @return correct answer
   * @throws ParsingException if input expression was invalid
   */
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null expression");
    }

    try {
      // creates handler, that will calculate expression.

      ExpressionHandler handler = createExpressionHandler();

      // this calculator is using StringTokenizer to parse expression.
      // expression must be wrapped in braces for correct work of some handler.
      // we parse expression on operators, numbers and braces, and also we should skip
      // special symbols like \t or \n.
      StringTokenizer stringTokenizer =
          new StringTokenizer('(' + expression + ")", "\\+/*-()\t\n", true);
      double number;

      while (stringTokenizer.hasMoreTokens()) {
        String token = stringTokenizer.nextToken();

        // each token is a number or operator.
        // if it's possible to convert in double - it's a number.
        // we push it to handler.
        number = toDouble(token);
        if (number != -1) {
          handler.pushNumber(number);
        } else if (token.length() == 1) {
          // else it's an operator that should be pushed in our handler.
          char operator = token.charAt(0);
          handler.pushOperator(operator);
        } else {
          // else there is some unknown symbol.
          throw new ParsingException("Unknown symbol");
        }
      }

      // return answer, that we received from handler.
      return handler.getAnswer();

    } catch (ParsingException e) {
      throw new ParsingException("Invalid expression", e.getCause());
    }
  }

  // function, that converting token to double, or tells that it isn't a number.
  private double toDouble(String token) {
    try {
      return Double.parseDouble(token);
    } catch (NumberFormatException e) {
      return -1;
    }
  }
}

