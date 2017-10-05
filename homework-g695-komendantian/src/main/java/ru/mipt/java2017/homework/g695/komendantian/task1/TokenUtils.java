package ru.mipt.java2017.homework.g695.komendantian.task1;

public class TokenUtils {

  static final String UNARY_MINUS = "UNARY_MINUS";
  static final String[] CONTROL_SYMBOLS = "+-*/()".split("");
  static final String[] OPERATORS = "+-*/".split("");

  /**
   * Принимает строку. Проверяет, является ли она управляющим символом (арифметическим оператором
   * или круглой скобкой)
   *
   * @param token строка
   * @return булевое значение
   */

  protected static boolean isControlSymbol(String token) {
    for (String symbol : CONTROL_SYMBOLS) {
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

  protected static boolean isOperator(String token) {
    for (String operator : OPERATORS) {
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

  protected static int getPriority(String operator) {
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

  protected static boolean isNumber(String token) {
    try {
      Double.valueOf(token);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
