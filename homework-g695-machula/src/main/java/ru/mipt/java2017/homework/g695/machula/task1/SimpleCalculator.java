package ru.mipt.java2017.homework.g695.machula.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * Calculator implementation
 *
 * @author Maxim V. Machula
 * @since 04.10.17
 */
public class SimpleCalculator implements Calculator {
  /**
   * @param expression arithmetic expression to calculate
   * @return value of expression if it is valid
   * @throws ParsingException exception in case of invalid expression with messege about error
   */
  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Expression is null");
    }
    return evalExpression(expression.replaceAll("\\s", ""), 0.0);
  }

  /**
   * expression = prevResult '+'/'-' term_1 '+'/'-' term_2 '+'/'-' ...  '+'/'-' term_n This function
   * tries to get first term of expression, and recursively call himself to calculate another part
   * of expression
   *
   * @param expression arithmetic expression without spaces that need to be evaluated
   * @param prevResult result of the previously evaluated terms
   * @return result of evaluating the expression
   * @throws ParsingException exception in case of invalid expression with messege about error
   */
  private double evalExpression(String expression, double prevResult) throws ParsingException {
    if (expression.length() == 0) {
      throw new ParsingException("Expression length equals zero");
    }
    if (expression.charAt(0) != '-' && expression.charAt(0) != '+') {
      return evalExpression("+" + expression, prevResult);
    }
    int posTermEnd = getPosTermEnd(expression);
    double result = evalTerm("*" + expression.substring(1, posTermEnd), 1);
    if (expression.charAt(0) == '-') {
      result = -result;
    }
    if (prevResult != 0.0) {
      result = prevResult + result;
    }
    if (posTermEnd == expression.length()) {
      return result;
    } else {
      return evalExpression(expression.substring(posTermEnd, expression.length()), result);
    }
  }

  /**
   * @param expression arithmetic expression without spaces
   * @return position of end of the first met term
   */
  private int getPosTermEnd(String expression) {
    for (int i = 1; i < expression.length(); ++i) {
      if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
        return i;
      }
      if (expression.charAt(i) == '/' || expression.charAt(i) == '*') {
        ++i;
      }
      if (expression.charAt(i) == '(') {
        int balance = 1;
        for (i = i + 1; balance > 0 && i < expression.length(); ++i) {
          if (expression.charAt(i) == ')') {
            --balance;
          }
          if (expression.charAt(i) == '(') {
            ++balance;
          }
        }
        --i;
      }
    }
    return expression.length();
  }

  /**
   * term = prevResult '*'/'/' value_1 '*'/'/' value_2 '*'/'/' ...  '*'/'/' value_n This function
   * tries to get first value of term, and recursively call himself to calculate another part of
   * term
   *
   * @param term term that need to be evaluated
   * @param prevResult result of the previously evaluated values
   * @return result of evaluating the term
   * @throws ParsingException exception in case of invalid expression with messege about error
   */
  private double evalTerm(String term, double prevResult) throws ParsingException {
    if (term.length() == 0) {
      throw new ParsingException("Term length equals zero");
    }
    int posValueEnd = getPosValueEnd(term);
    double result = evalValue(term.substring(1, posValueEnd));
    if (term.charAt(0) == '*') {
      result = prevResult * result;
    } else {
      result = prevResult / result;
    }
    if (posValueEnd == term.length()) {
      return result;
    } else {
      return evalTerm(term.substring(posValueEnd, term.length()), result);
    }
  }

  /**
   * @param term term that need to be divide into values
   * @return position of end of the first met value
   */
  private int getPosValueEnd(String term) {
    for (int i = 1; i < term.length(); ++i) {
      if (term.charAt(i) == '*' || term.charAt(i) == '/') {
        return i;
      }
      if (term.charAt(i) == '(') {
        int balance = 1;
        for (i = i + 1; balance > 0 && i < term.length(); ++i) {
          if (term.charAt(i) == ')') {
            --balance;
          }
          if (term.charAt(i) == '(') {
            ++balance;
          }
        }
        --i;
      }
    }
    return term.length();
  }

  /**
   * value = number or '('expression')'
   *
   * @param value value that need to be evaluated
   * @return result of evaluating the value
   * @throws ParsingException exception in case of invalid expression with messege about error
   */
  private double evalValue(String value) throws ParsingException {
    if (value.length() == 0) {
      throw new ParsingException("Value length equals zero");
    }
    if (value.charAt(0) == '(' && value.charAt(value.length() - 1) == ')') {
      return evalExpression(value.substring(1, value.length() - 1), 0);
    }
    if (value.charAt(0) == '+' || value.charAt(0) == '-') {
      return evalExpression(value, 0.0);
    }
    double result = 0;
    boolean afterDecimalPoint = false;
    double factor = 1;
    for (int i = 0; i < value.length(); ++i) {
      if (value.charAt(i) == '.') {
        if (afterDecimalPoint) {
          throw new ParsingException("Two decimal points in one number");
        }
        afterDecimalPoint = true;
      } else if (Character.isDigit(value.charAt(i))) {
        if (!afterDecimalPoint) {
          result = result * 10 + Character.getNumericValue(value.charAt(i));
        } else {
          factor *= 0.1;
          result = result + factor * Character.getNumericValue(value.charAt(i));
        }
      } else {
        throw new ParsingException("Unexpected Symbol");
      }
    }
    return result;
  }

}
