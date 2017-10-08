package ru.mipt.java2017.homework.g694.kozinov.task1;

class CharRecognizer {
  public enum CharKind {
    IGNORE,
    UNKNOWN,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    NUMBER_PART,
    OPERATION
  }

  public enum OperationKind {
    UNKNOWN,
    PLUS,
    MINUS,
    UNARY_PLUS,
    UNARY_MINUS,
    MULTI,
    DIVISION,
    OPEN,
    CLOSE;

    public boolean isUnary() {
      return this == UNARY_PLUS || this == UNARY_MINUS;
    }

    public OperationKind makeUnary() {
      if (this == PLUS) {
        return UNARY_PLUS;
      }
      if (this == MINUS) {
        return UNARY_MINUS;
      }
      return this;
    }

    public int getPriority() {
      if (isUnary()) {
        return 4;
      }

      if (this == PLUS || this == MINUS) {
        return 1;
      }

      if (this == MULTI || this == DIVISION) {
        return 2;
      }

      return -1;
    }
  }

  static CharKind getCharKind(char c) {
    if (c == ' ' || c == '\t' || c == '\n') {
      return CharKind.IGNORE;
    }

    if (c == '(') {
      return CharKind.OPEN_BRACKET;
    }

    if (c == ')') {
      return CharKind.CLOSE_BRACKET;
    }

    if (Character.isDigit(c) || c == '.') {
      return CharKind.NUMBER_PART;
    }

    if (c == '+' || c == '-' || c == '*' || c == '/') {
      return CharKind.OPERATION;
    }

    return CharKind.UNKNOWN;
  }

  static OperationKind getOperationKind(char c) {
    switch (c) {
      case '+':
        return OperationKind.PLUS;

      case '-':
        return OperationKind.MINUS;

      case '*':
        return OperationKind.MULTI;

      case '/':
        return OperationKind.DIVISION;

      case '(':
        return OperationKind.OPEN;

      case ')':
        return OperationKind.CLOSE;

      default:
        return OperationKind.UNKNOWN;
    }
  }
}
