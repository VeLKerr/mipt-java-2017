package ru.mipt.java2017.homework.g696.nechepurenco.task1;

class Token {
  private int type = -1;
  private char operand = '!';
  private double number = 0;

  Token(char _operand) {
    operand = _operand;
    type = 2;
  }

  Token(double value) {
    number = value;
    type = 1;
  }

  boolean equal(char givenOperand) {
        return (type == 2 && operand == givenOperand);
  }

  int getType() {
    return type;
  }

  char getOperand() {
    return operand;
  }

  double getNumber() {
    return number;
  }
}
