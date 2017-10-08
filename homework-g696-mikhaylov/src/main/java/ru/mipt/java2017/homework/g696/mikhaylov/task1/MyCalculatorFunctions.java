package ru.mipt.java2017.homework.g696.mikhaylov.task1;

/**
 * Класс всех функций для калькулятора. В калькуляторе как могут присутствовать совсем
 * нетривиальные функции, например, тригонометрические.
 */

class MyCalculatorFunctions {

  double unaryOperations(Lexeme operation, double number) {
    if (operation.getValue().equals("+")) {
      return number;
    }
    if (operation.getValue().equals("-")) {
      return -number;
    }
    return 0;
  }

  double binaryOperations(Lexeme operation, double first, double second) {
    if (operation.getValue().equals("*")) {
      return first * second;
    }
    if (operation.getValue().equals("/")) {
      return first / second;
    }
    if (operation.getValue().equals("+")) {
      return first + second;
    }
    if (operation.getValue().equals("-")) {
      return first - second;
    }
    return 0;
  }
}
