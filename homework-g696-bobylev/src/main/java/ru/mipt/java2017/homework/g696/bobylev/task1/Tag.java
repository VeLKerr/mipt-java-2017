package ru.mipt.java2017.homework.g696.bobylev.task1;

enum Tag {
  NUMBER,
  LPAREN,
  RPAREN,
  END_OF_TEXT,
  ADD,
  SUB,
  MUL,
  DIV;

  public String toString() {
    switch (this) {
      case NUMBER:
        return "number";
      case LPAREN:
        return "'('";
      case RPAREN:
        return "')'";
      case END_OF_TEXT:
        return "end of text";
      case ADD:
        return "+";
      case SUB:
        return "-";
      case MUL:
        return "*";
      case DIV:
        return "|";
      default:
        throw new RuntimeException("unreachable code");
    }
  }
}