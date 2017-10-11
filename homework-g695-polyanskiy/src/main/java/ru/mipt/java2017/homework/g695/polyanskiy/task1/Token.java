package ru.mipt.java2017.homework.g695.polyanskiy.task1;

/**
 * Created by maxim on 06.10.17.
 */
public class Token {
  private String value;
  private TokenEnums.Tokens type;

  Token(TokenEnums.Tokens typeNew, String valueNew) {
    value = valueNew;
    type = typeNew;
  }

  public String getValue() {
    return value;
  }

  public TokenEnums.Tokens getType() {
    return type;
  }
}
