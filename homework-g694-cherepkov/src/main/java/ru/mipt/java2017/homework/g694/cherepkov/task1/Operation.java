package ru.mipt.java2017.homework.g694.cherepkov.task1;

import java.util.Arrays;
import java.util.List;

/**
 * Class to store operations used in arithmetic expression
 */
public class Operation {

  private static final List<Character> availableOperations = Arrays.asList('+', '-', '*', '/');

  private Character sign;
  private boolean isUnary;

  /**
   * Constructor
   *
   * @param sign sign of operation
   * @param isUnary true if unary, false otherwise
   */
  Operation(char sign, boolean isUnary) {
    this.sign = sign;
    this.isUnary = isUnary;
  }

  /**
   * Checks whether operation exists
   *
   * @param sign sign of operation to check
   * @return true if exists, false otherwise
   */
  static boolean isOperation(char sign) {
    return availableOperations.contains(sign);
  }

  /**
   * Checks whether operation may be unary
   *
   * @param sign sign of operation to check
   * @return true if may be unary, false otherwise
   */
  static boolean canBeUnary(char sign) {
    return sign == '+' || sign == '-';
  }

  /**
   *
   * @param sign sign of operation to set
   */
  public void setSign(Character sign) {
    this.sign = sign;
  }

  /**
   *
   * @param unary true to switch operation to unary, false to switch operation to binary
   */
  public void setUnary(boolean unary) {
    isUnary = unary;
  }

  /**
   *
   * @return sign of operation
   */
  Character getSign() {
    return sign;
  }

  /**
   *
   * @return true if operation is unary, false if binary
   */
  boolean isUnary() {
    return isUnary;
  }

  /**
   * Returns the priority for the operation
   * Table of priorities (from highest ones):
   *  -(unary), +(unary)
   *  *, /
   *  +(binary), -(binary)
   *
   * @return 3 if unary, 2 if * or /, 1 if + or -, 0 otherwise
   */
  byte getPriority() {
    if (isUnary) {
      return 3;
    }
    if (sign == '*' || sign == '/') {
      return 2;
    }
    if (sign == '+' || sign == '-') {
      return 1;
    }
    return 0;
  }

}