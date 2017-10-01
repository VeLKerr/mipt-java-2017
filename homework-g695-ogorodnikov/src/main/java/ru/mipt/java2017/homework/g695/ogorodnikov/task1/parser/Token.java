package ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser;

class Token {

  private final TokenType type;
  private final String val;

  Token(TokenType type, String val) {
    this.type = type;
    this.val = val;
  }

  public String getVal() {
    return val;
  }

  public TokenType getType() {
    return type;
  }

  public enum TokenType {
    NUMBER, LBRACE, RBRACE, OP, INVALID
  }
}
