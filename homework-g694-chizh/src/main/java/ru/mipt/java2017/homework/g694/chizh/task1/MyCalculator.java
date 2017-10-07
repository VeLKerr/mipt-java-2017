package ru.mipt.java2017.homework.g694.chizh.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class MyCalculator implements Calculator {

  /**
   * String contains allowed symbols in expression
   */
  private static final String ALLOWED_SYMBOLS_REGEX = "[0-9.()\\-\\+\\*/\\s]*";

  /**
   * Current position of pointer to the beginning of processed expression
   */
  private int currentPosition;

  /**
   * Expression string
   */
  private String exp;

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null string");
    }
    currentPosition = 0;
    exp = expression;
    if (!exp.matches(ALLOWED_SYMBOLS_REGEX)) {
      throw new ParsingException("Unexpected symbol was found");
    }
    double result = parseExpression();
    if (currentPosition != exp.length()) {
      throw new ParsingException("Incorrect expression");
    }
    return result;
  }

  /**
   * Accepts a character and returns the result of checking whether this is a sign
   *
   * @param c a character
   * @return result of checking
   */
  private boolean isSign(char c) {
    return (c == '+' || c == '-' || c == '*' || c == '/');
  }

  /**
   * Accepts a character and returns the result of checking whether this is a digit or a point
   *
   * @param c a character
   * @return result of checking
   */
  private boolean isNumSymbol(char c) {
    return (c >= '0' && c <= '9' || c == '.');
  }

  /**
   * Accepts string with expression and checks whether spaces divide parts of any number
   *
   * @return result of cheking
   */
  private boolean checkSpace() {
    String[] parts = exp.split("\\s+");
    for (int i = 1; i < parts.length; ++i) {
      if (parts[i].isEmpty() || parts[i - 1].isEmpty()) {
        continue;
      }
      if (isNumSymbol(parts[i].charAt(0)) && isNumSymbol(
          parts[i - 1].charAt(parts[i - 1].length() - 1))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks whether two signs are next to each other
   *
   * @return result of checking
   */
  private boolean checkSigns() {
    for (int i = 1; i < exp.length(); ++i) {
      if (isSign(exp.charAt(i)) && isSign(exp.charAt(i - 1))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Parses the expression
   *
   * @return a double number - the result of parsed expression
   * @throws ParsingException in case of an incorrect expression was entered
   */
  private double parseExpression() throws ParsingException {
    if (currentPosition >= exp.length()) {
      throw new ParsingException("Incorrect expression");
    }
    if (checkSpace()) {
      exp = exp.replaceAll("\\s+", "");
      if (checkSigns()) {
        return parseAddAndSubt();
      } else {
        throw new ParsingException("Incorrect sign order");
      }
    } else {
      throw new ParsingException("Incorrect space syntax");
    }
  }

  /**
   * Parses the expression for addition and subtraction
   *
   * @return a double number - the result of parsed expression
   * @throws ParsingException in case of an incorrect expression was entered
   */
  private double parseAddAndSubt() throws ParsingException {
    if (currentPosition >= exp.length()) {
      throw new ParsingException("Incorrect expression");
    }
    double result = parseMultAndDiv();
    while (currentPosition < exp.length() && (exp.charAt(currentPosition) == '+' || exp.charAt(
        currentPosition) == '-')) {
      boolean sign;
      sign = exp.charAt(currentPosition) == '+';
      ++currentPosition;
      double number = parseMultAndDiv();
      if (sign) {
        result += number;
      } else {
        result -= number;
      }
    }
    return result;
  }

  /**
   * Parses the expression for multiplication and division
   *
   * @return a double number - the result of parsed expression
   * @throws ParsingException in case of an incorrect expression was entered
   */
  private double parseMultAndDiv() throws ParsingException {
    if (currentPosition >= exp.length()) {
      throw new ParsingException("Incorrect expression");
    }
    double result = parseNum();
    while (currentPosition < exp.length() && (exp.charAt(currentPosition) == '*' || exp.charAt(
        currentPosition) == '/')) {
      boolean sign;
      sign = exp.charAt(currentPosition) == '*';
      ++currentPosition;
      double number = parseNum();
      if (sign) {
        result *= number;
      } else {
        result /= number;
      }
    }
    return result;
  }

  /**
   * Parses the number
   *
   * @return a double number
   * @throws ParsingException in case of an incorrect expression was entered
   */
  private double parseNum() throws ParsingException {
    if (currentPosition >= exp.length()) {
      throw new ParsingException("Incorrect expression");
    }
    double number = 0;
    if (exp.charAt(currentPosition) == '(') {
      ++currentPosition;
      number = parseExpression();
      if (currentPosition >= exp.length() || exp.charAt(currentPosition) != ')') {
        throw new ParsingException("Incorrect syntax: ')' expected");
      }
      ++currentPosition;
      return number;
    } else if (exp.charAt(currentPosition) == '-') {
      ++currentPosition;
      number = parseNum();
      number *= -1;
      return number;
    } else if (exp.charAt(currentPosition) == '+') {
      ++currentPosition;
      number = parseNum();
      return number;
    }
    int position = currentPosition;
    while (position < exp.length() && isNumSymbol(exp.charAt(position))) {
      ++position;
    }
    try {
      number = Double.parseDouble(exp.substring(currentPosition, position));
    } catch (NumberFormatException err) {
      throw new ParsingException("Incorrect number was found");
    }
    currentPosition = position;
    return number;
  }
}
