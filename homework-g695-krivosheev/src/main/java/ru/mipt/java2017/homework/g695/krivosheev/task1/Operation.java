package ru.mipt.java2017.homework.g695.krivosheev.task1;

import java.util.Arrays;
import java.util.List;

/**
 * Класс, инкапсулирующий работу с арифметическими операциями
 */
public class Operation {

  private static final List<Character> OPERATIONS = Arrays.asList('+', '-', '*', '/');

  private Character sign;
  private boolean isUnary;

  /**
   * Конструктор
   *
   * @param sign sign of operation
   * @param isUnary true if unary, false otherwise
   */
  Operation(char sign, boolean isUnary) {
    this.sign = sign;
    this.isUnary = isUnary;
  }

  /**
   * Проверка символа на принадлежность к операциям
   *
   * @param sign sign of operation to check
   * @return true if exists, false otherwise
   */
  static boolean isOperation(char sign) {
    return OPERATIONS.contains(sign);
  }

  /**
   * Унарность операции
   *
   * @param sign sign of operation to check
   * @return true if may be unary, false otherwise
   */
  static boolean canBeUnary(char sign) {
    return sign == '+' || sign == '-';
  }

  /**
   * "Сеттер" для знака
   * @param sign sign of operation to set
   */
  public void setSign(Character sign) {
    this.sign = sign;
  }

  /**
   * "Сеттер" для унарности
   * @param unary true to switch operation to unary, false to switch operation to binary
   */
  public void setUnary(boolean unary) {
    isUnary = unary;
  }

  /**
   * "Геттер" для знака :)
   * @return sign of operation
   */
  Character getSign() {
    return sign;
  }

  /**
   * "Геттер" для унарности 
   * @return true if operation is unary, false if binary
   */
  boolean isUnary() {
    return isUnary;
  }

  /**
   * Получение стандартного арифметического приоритета операций
   * Чем больше число, тем выше приоритет
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

