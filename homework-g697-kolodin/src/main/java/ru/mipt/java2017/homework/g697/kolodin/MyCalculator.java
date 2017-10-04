package ru.mipt.java2017.homework.g697.kolodin;

import ru.mipt.java2017.homework.base.task1.*;

public class MyCalculator implements Calculator {

  public MyCalculator() {
  }

  /**
   * We store the current result and rest of the string in this class
   */
  private class Result {

    private double value;
    private String rest;

    Result(double value, String rest) {
      this.value = value;
      this.rest = rest;
    }
  }

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("A null expression");
    }
    expression = expression.replaceAll("\\s+", "");
    if (expression.length() == 0) {
      throw new ParsingException("An empty string");
    }
    Result result = addOrSubtract(expression);
    if (!result.rest.isEmpty()) {
      throw new ParsingException("Can't full parse");
    }
    return result.value;
  }

  /**
   * Parses sum or difference
   *
   * @return An exapmle of Result, which consists current result and rest of the string
   */
  private Result addOrSubtract(String expression) throws ParsingException {
    Result current = mulDiv(expression);
    while (current.rest.length() > 0 && (current.rest.charAt(0) == '+'
        || current.rest.charAt(0) == '-')) {

      char sign = current.rest.charAt(0);
      String next = current.rest.substring(1);
      double acc = current.value;
      current = mulDiv(next);
      if (sign == '+') {
        acc += current.value;
      } else {
        acc -= current.value;
      }
      current.value = acc;
    }
    return new Result(current.value, current.rest);
  }

  /**
   * Parses brackets, also checks the quantity of unary operands
   *
   * @return An exapmle of Result, which consists current result and rest of the string
   */
  private Result brackets(String expression, boolean unaryOperand) throws ParsingException {
    char zeroChar = expression.charAt(0);
    if (zeroChar == '(') {
      Result r = addOrSubtract(expression.substring(1));
      if (!r.rest.isEmpty() && r.rest.charAt(0) == ')') {
        r.rest = r.rest.substring(1);
      } else {
        throw new ParsingException("No close bracket");
      }
      return r;
    }
    if (!unaryOperand && (expression.charAt(0) == '+' || expression.charAt(0) == '-')) {
      throw new ParsingException("Extra unary plus or minus");
    }
    if (expression.charAt(0) == '-') {
      Result result = brackets(expression.substring(1), false);
      return new Result(-result.value, result.rest);
    }
    if (expression.charAt(0) == '+') {
      Result result = brackets(expression.substring(1), false);
      return new Result(result.value, result.rest);
    }
    return num(expression);
  }

  /**
   * Parses a number
   *
   * @return An exapmle of Result, which consists current result and rest of the string
   */
  private Result num(String expression) throws ParsingException {
    int i = 0;
    int dotCounter = 0;
    boolean isNegative = false;
    if (expression.charAt(0) == '-') {
      isNegative = true;
      expression = expression.substring(1);
    }
    while (i < expression.length() && (Character.isDigit(expression.charAt(i))
        || expression.charAt(i) == '.')) {

      if (expression.charAt(i) == '.' && ++dotCounter > 1) {
        throw new ParsingException("Invalid number: " + expression.substring(0, i + 1));
      }
      ++i;
    }
    if (i == 0) {
      throw new ParsingException("Can't get valid number in: " + expression);
    }
    double doublePart = Double.parseDouble(expression.substring(0, i));
    if (isNegative) {
      doublePart = -doublePart;
    }
    String resPart = expression.substring(i);
    return new Result(doublePart, resPart);
  }

  /**
   * Parses composition and division
   *
   * @return An exapmle of Result, which consists current result and rest of the string
   */
  private Result mulDiv(String expression) throws ParsingException {
    Result current = brackets(expression, true);
    double acc = current.value;
    while (true) {

      if (current.rest.length() == 0) {
        return current;
      }
      char sign = current.rest.charAt(0);
      if (sign != '*' && sign != '/') {
        return current;
      }
      String next = current.rest.substring(1);
      Result right = brackets(next, true);
      if (sign == '*') {
        acc *= right.value;
      } else {
        acc /= right.value;
      }
      current = new Result(acc, right.rest);
    }
  }
}