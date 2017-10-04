package ru.mipt.java2017.homework.g695.kalinochkin.task1;

class Token {

  private TokenType type;
  private String value;

  Token(TokenType t, String v) {
    type = t;
    value = v;
  }

  public TokenType getType() {
    return type;
  }

  public String getValue() {
    return value;
  }
}
