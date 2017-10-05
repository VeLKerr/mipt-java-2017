package ru.mipt.java2017.homework.g696.mikhaylov.task1;

class MyCalculatorFunctions {

  double unaryOperations(String operation, double number) {
    if (operation.equals("+")) {
      return number;
    }
    if (operation.equals("-")) {
      return -number;
    }
    return 0;
  }

  double binaryOperations(String operation, double first, double second) {
    if (operation.equals("*")) {
      return first * second;
    }
    if (operation.equals("/")) {
      return first / second;
    }
    if (operation.equals("+")) {
      return first + second;
    }
    if (operation.equals("-")) {
      return first - second;
    }
    return 0;
  }
}
