package ru.mipt.java2017.homework.g697.yakushkin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Objects;

/**
 * @author Yakushkin Alexander
 * @since 03.10.17
 *
 * Get valid string expression.
 * Expression may contain any number of space symbols, and '\n', '\t'.
 * Return the result of calculations.
 *
 * Works with numbers of type double, brackets,
 * binary operators:
 * '+', '-', '/', '*',
 * unary operators:
 * '-'.
 */


public class MyCalculator implements Calculator {
  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null string");
    }
    expression = expression.replaceAll("\n", "").replaceAll(" ", "").replaceAll("\t", "");


    // Check if bracket sequence correct
    int balance = 0;
    for (int i = 0; i < expression.length(); ++i) {
      char symbol = expression.charAt(i);
      if (!((symbol >= '0' && symbol <= '9')
        || symbol == '(' || symbol == ')' || symbol == '-' || symbol == '+' || symbol == '/' ||
        symbol == '*' || symbol == '.')) {
        throw new ParsingException("Incorrect symbol");
      }
      if (symbol == '(') {
        ++balance;
      }
      if (symbol == ')') {
        --balance;
      }
      if (balance < 0) {
        throw new ParsingException("Incorrect bracket sequence");
      }


      char lastSymbol;
      if (i > 0) {
        lastSymbol = expression.charAt(i - 1);
      } else {
        continue;
      }
      if (ifStrngIncorrect(lastSymbol, symbol)) {
        throw new ParsingException("Incorrect");
      }
    }

    if (balance != 0) {
      throw new ParsingException("Incorrect bracket sequence");
    }
    return getInt(expression);
  }


  /**
   * Check if it possible to put next element after last
   *
   * @param (last, next) two elements going one by one
   * @return true if string incorrect
   */

  private boolean ifStrngIncorrect(char last, char next) {
    // Two operations can`t go one by one
    if ((last == '+' || last == '-' || last == '*' || last == '/' || last == '.') &&
      (next == '+' || next == '.' || next == '*' || next == '/')) {
      return true;
    }

    // After '(' can`t be any operation, except '-'
    if (last == '(' && (next == '+' || next == '*' || next == '/' || next == '.')) {
      return true;
    }

    // Before ')' can`t be any operation
    if (next == ')' &&
      (last == '+' || last == '-' || last == '*' || last == '/' || last == '.')) {
      return true;
    }

    // Before '(' must be operation
    if (next == '(' && ((last >= '0' && last <= '9') || last == '.')) {
      return true;
    }

    // After ')' must be operation
    if (last == ')' && ((next >= '0' && next <= '9') || next == '.' || next == '(')) {
      return true;
    }

    return false;
  }

  private double getInt(String expression) throws ParsingException {
    int balance = 0;
    boolean isPlus = false;
    double sum = 0.0;
    int lastAct = 0;
    if (Objects.equals(expression, "")) {
      throw new ParsingException("Empty string");
    }
    for (int i = 0; i < expression.length(); ++i) {
      if (i == 0 && expression.charAt(i) == '-') {
        continue;
      }
      if (expression.charAt(i) == '(') {
        ++balance;
      }
      if (expression.charAt(i) == ')') {
        --balance;
      }
      if (balance != 0) {
        continue;
      }
      if (expression.charAt(i) == '+') {
        sum += getInt(expression.substring(lastAct, i));
        lastAct = i + 1;
        isPlus = true;
      }
      if (expression.charAt(i) == '-' && i > 0 &&
        (expression.charAt(i - 1) >= '0' && expression.charAt(i - 1) <= '9')) {
        sum += getInt(expression.substring(lastAct, i));
        lastAct = i;
        isPlus = true;
      }
    }
    if (isPlus) {
      return sum + getInt(expression.substring(lastAct, expression.length()));
    }
    boolean isMult = false;
    double res = 1.0;
    char lastMult = '*';
    for (int i = 0; i < expression.length(); ++i) {
      if (i == 0 && expression.charAt(i) == '-') {
        continue;
      }
      if (expression.charAt(i) == '(') {
        ++balance;
      }
      if (expression.charAt(i) == ')') {
        --balance;
      }
      if (balance != 0) {
        continue;
      }
      if (expression.charAt(i) == '*') {
        if (lastMult == '*') {
          res *= getInt(expression.substring(lastAct, i));
        } else {
          res /= getInt(expression.substring(lastAct, i));
        }
        lastAct = i + 1;
        lastMult = '*';
        isMult = true;
      }
      if (expression.charAt(i) == '/') {
        if (lastMult == '*') {
          res *= getInt(expression.substring(lastAct, i));
        } else {
          res /= getInt(expression.substring(lastAct, i));
        }
        lastAct = i + 1;
        lastMult = '/';
        isMult = true;
      }
    }
    if (isMult) {
      if (lastMult == '*') {
        res *= getInt(expression.substring(lastAct, expression.length()));
      } else {
        res /= getInt(expression.substring(lastAct, expression.length()));
      }
      return res;
    }
    if (expression.charAt(0) == '(') {
      return getInt(expression.substring(1, expression.length() - 1));
    }
    if (expression.charAt(0) == '-') {
      return -1.0 * getInt(expression.substring(1, expression.length()));
    }
    int cntPoint = 0;
    for (int i = 0; i < expression.length(); ++i) {
      char ch = expression.charAt(i);
      if (!(Character.isDigit(ch) || ch == '.' || ch == '-')) {
        throw new ParsingException("Incorrect");
      }
      if (ch == '.') {
        ++cntPoint;
      }
    }
    if (cntPoint > 1) {
      throw new ParsingException("Number with more one points");
    }
    return Double.parseDouble(expression);
  }
}