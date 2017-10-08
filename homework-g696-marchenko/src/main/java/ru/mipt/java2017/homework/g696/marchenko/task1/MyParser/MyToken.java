package ru.mipt.java2017.homework.g696.marchenko.task1.MyParser;

public class MyToken {

  public enum TokenType {
    LEFTB, RIGHTB, NUMBER, OPERATION
  }

  private final TokenType type;
  private final String value;

  MyToken(TokenType t, String v) {
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
