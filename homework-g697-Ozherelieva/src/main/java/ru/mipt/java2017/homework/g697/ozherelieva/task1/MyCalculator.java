package ru.mipt.java2017.homework.g697.ozherelieva.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import static ru.mipt.java2017.homework.g697.ozherelieva.task1.Parser.*;

import java.util.Stack;

/**
 * @author Ozherelieva Sofia
 * @since 04.10.17
 *
 * Get valid string expression.
 * Expression can contains any number of space symbols, and '\n', '\t'.
 * Return the result of calculations.
 *
 * Works with numbers of type double, brackets,
 * binary operators:
 * '+', '-', '/', '*',
 * unary operators:
 * '-'.
 *
 *
 */

class MyCalculator implements Calculator {

  /**
   *
   * @param expression the string expression.
   * @return calculation result type of double.
   * @throws ParsingException
   */

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Expression is null");
    }
    StringBuilder new_expression = expressionPreparation(expression);
    return countPostfix(fromInfixToPostfix(new_expression));
  }

  private static int getPriority(char operator) throws ParsingException {
    switch (operator) {
      case '+':
        return 1;
      case '-':
        return 1;
      case '*':
        return 2;
      case '/':
        return 2;
      case '(':
        return 0;
      case ')':
        return 0;
      case UNARYMINUS:
        return 3;
      default:
        throw new ParsingException("Invalid operator");
    }
  }

  private static double operator(char operator, double num2, double num1) throws ParsingException {
    switch (operator) {
      case '+':
        return num1 + num2;
      case '-':
        return num1 - num2;
      case '*':
        return num1 * num2;
      case '/':
        return num1 / num2;
      default:
        throw new ParsingException("Invalid operator");
    }
  }

  private static StringBuilder fromInfixToPostfix(StringBuilder expression) throws ParsingException {
    StringBuilder postfix = new StringBuilder();
    Stack<String> operators = new Stack<>();
    String token = "";
    int i = 0;
    while (i < expression.length()) {
      while (expression.charAt(i) != ' ') {
        token += expression.charAt(i);
        i++;
      }
      if (token.length() > 1 || Character.isDigit(token.charAt(0))) {
        postfix.append(token + " ");
      } else {
        if (!operators.empty()) {
          int currentPriority = getPriority(token.charAt(0));
          while (currentPriority <= getPriority(operators.peek().charAt(0))
            && token.charAt(0) != '(') {
            if (operators.peek().charAt(0) != '(') {
              postfix.append(operators.peek() + " ");
            } else {
              operators.pop();
              break;
            }
            operators.pop();
            if (operators.empty()) {
              break;
            }
          }
        }
        if (token.charAt(0) != ')') {
          operators.add(token);
        }
      }
      token = "";
      i++;
    }
    while (!operators.empty()) {
      postfix.append(operators.peek() + " ");
      operators.pop();
    }
    return postfix;
  }

  private static Double countPostfix(StringBuilder postfix) throws ParsingException {
    Double result;
    Stack<Double> operation = new Stack<>();
    int i = 0;
    String token = "";
    while (i < postfix.length()) {
      while (postfix.charAt(i) != ' ') {
        token += postfix.charAt(i);
        i++;
      }
      if (Character.isDigit(token.charAt(0))) {
        operation.push(Double.parseDouble(token));
      } else {
        if (token.charAt(0) == UNARYMINUS) {
          Double value = (-1) * operation.pop();
          operation.push(value);
        } else {
          Double value = operator(token.charAt(0), operation.pop(), operation.pop());
          operation.push(value);
        }
      }
      token = "";
      i++;
    }
    result = operation.pop();
    return result;
  }
}