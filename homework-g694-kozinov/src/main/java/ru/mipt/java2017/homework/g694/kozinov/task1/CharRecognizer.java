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
    MULTI,
    DIVISION;
    public int getPriority() {
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

      default:
        return OperationKind.UNKNOWN;
    }
  }
}
