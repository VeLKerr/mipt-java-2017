package ru.mipt.java2017.homework.ru.nic11;

import ru.mipt.java2017.homework.base.task1.ParsingException;

public class Token {
  public static enum Type {
    BRACE_OPEN, BRACE_CLOSE,
    UNARY_OPERATOR, BINARY_OPERATOR,
    NUMBER,
  }

  public final Type type;
  public final String value;

  Token(Type type, String value) {
    this.type = type;
    this.value = value;
  }

  public double parseAsNumber() throws ParsingException {
    if (type != Type.NUMBER) throw new ParsingException("Not a number");
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      throw new ParsingException("Not a number");
    }
  }
}
