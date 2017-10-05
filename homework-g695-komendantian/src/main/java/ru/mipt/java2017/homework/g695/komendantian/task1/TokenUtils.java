package ru.mipt.java2017.homework.g695.komendantian.task1;

public final class TokenUtils {

  static protected final String UNARY_MINUS = "UNARY_MINUS";
  static protected final String[] controlSymbols = "+-*/()".split("");
  static protected final String[] operators = "+-*/".split("");

  /**
   * Принимает строку. Проверяет, является ли она управляющим символом (арифметическим оператором
   * или круглой скобкой)
   *
   * @param token строка
   * @return булевое значение
   */

  static protected boolean isControlSymbol(String token) {
    for (String symbol : controlSymbols) {
      if (symbol.equals(token)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Принимает строку. Проверяет, является ли она арифметическим оператором
   *
   * @param token строка
   * @return булевое значение
   */

  static protected boolean isOperator(String token) {
    for (String operator : operators) {
      if (operator.equals(token)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Принимает оператор или открывающуюся скобку. Возвращает ее приоритет при разборе выражения.
   *
   * @param operator строка
   * @return булевое значение
   */

  static protected int getPriority(String operator) {
    if (operator.equals("+") || operator.equals("-")) {
      return 1;
    } else if (operator.equals("(")) {
      return 0;
    } else {
      return 2;
    }
  }

  /**
   * Принимает строку. Проверяет, является ли она валидной записью числа.
   *
   * @param token строка
   * @return булевое значение
   */

  static protected boolean isNumber(String token) {
    try {
      Double.valueOf(token);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
