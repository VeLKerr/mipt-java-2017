package ru.mipt.java2017.homework.g696.zhukovskiy.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

class Evaluator {

  private Stack<Double> rpnStack;
  private String rpnExpression;

  /**
   * Initializes Evaluator and creates it's inner state
   *
   * @param rpnExpr expression in Reverse Polish Notation
   * @return {@code Evaluator} object
   */
  Evaluator(String rpnExpr) {
    rpnExpression = rpnExpr;
    rpnStack = new java.util.Stack<>();
  }

  /**
   * Indicates if given character is arithmetic operator
   *
   * @param currentChar character
   * @return {@code true} if character is operator {@code false} instead
   */
  private boolean symbolIsOperator(char currentChar) {
    return (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/' || currentChar == '$');
  }

  private boolean symbolIsDigit(char currentChar) {
    return (Character.isDigit(currentChar));
  }

  /**
   * Returns numeric value of the character at
   * the given position in rpnExpression
   *
   * @param position of the character
   * @return {@code int} numeric value
   */
  private int readDigitAt(int position) {
    return Character.getNumericValue(rpnExpression.charAt(position));
  }

  /**
   * Evaluates single operation with 2 items at the top
   * of a stack.
   *
   * @param operation the character of the operation
   */
  private void evalSingle(char operation) throws ParsingException {
    double first = rpnStack.pop();
    double second = rpnStack.pop();
    switch (operation) {
      case ('+'):
        rpnStack.push(second + first);
        break;
      case ('-'):
        rpnStack.push(second - first);
        break;
      case ('/'):
        rpnStack.push(second / first);
        break;
      case ('*'):
        rpnStack.push(second * first);
        break;
      default:
        throw new ParsingException("Broken expression (invalid operator)");
    }
  }

  /**
   * Evaluates a expression in it's inner state and
   * returns it's final value
   *
   * @return {@code double} value of expression
   * @throws ParsingException if given expression is not valid
   */
  double evaluateRPN() throws ParsingException {
    for (int i = 0; i < rpnExpression.length(); ++i) {
      char currentChar = rpnExpression.charAt(i);

      // Считываем числа и точки
      if (symbolIsDigit(currentChar)) {
        double result = Character.getNumericValue(currentChar);
        if (result == -1) {
          throw new ParsingException("Broken expression (digit starts with a .)");
        } else {
          while (Character.isDigit(rpnExpression.charAt(i + 1))) {
            ++i;
            result = result * 10 + readDigitAt(i);
          }
          if (rpnExpression.charAt(i + 1) == '.') {
            ++i;
            double partAfterDot = 1;
            while (Character.isDigit(rpnExpression.charAt(i + 1))) {
              ++i;
              partAfterDot *= 0.1;
              result = result + partAfterDot * readDigitAt(i);
            }
            if (rpnExpression.charAt(i + 1) == '.') {
              throw new ParsingException("Broken expression (too much . in a number)");
            }
            rpnStack.push(result);
          } else {
            rpnStack.push(result);
          }
        }
        continue;
      }

      // Обработка операторов типа +, -, *, /, $ (унарный минус, изменение знака выражения)
      if (symbolIsOperator(currentChar)) {
        if (currentChar == '$') {
          // Меняем знак верхнего числа в стеке
          if (!rpnStack.empty()) {
            double number = rpnStack.pop();
            rpnStack.push(-1 * number);
          } else {
            throw new ParsingException("Broken expression (no number after unary minus found)");
          }
          // Выполняем арифметическую операцию над двумя числами в стеке
        } else {
          if (rpnStack.size() >= 2) {
            evalSingle(currentChar);
          } else {
            throw new ParsingException("Broken expression (wrong usage of \"" + currentChar + ")");
          }
        }
      }
    }

    if (rpnStack.size() == 1) {
      return rpnStack.pop();
    } else {
      throw new ParsingException("Broken expression (stack isn't empty)");
    }
  }
}