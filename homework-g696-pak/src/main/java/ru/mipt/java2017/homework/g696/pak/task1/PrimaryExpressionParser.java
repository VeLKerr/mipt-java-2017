package ru.mipt.java2017.homework.g696.pak.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

//parse numbers
public class PrimaryExpressionParser implements ExpressionParser {

  private int getDigit(char digit) {
    return (digit - '0');
  }

  @Override
  public Calculable parse(String expression) throws ParsingException {
    double frictionalPart = 0;
    double integerPart = 0;
    int iter = 0;
    int sign = 1;
    if (expression.charAt(0) == '-') {
      sign = -1;
      ++iter;
    }
    for (; iter < expression.length() && expression.charAt(iter) != '.'; ++iter) {
      integerPart = integerPart * 10 + getDigit(expression.charAt(iter));
    }

    if (iter < expression.length() && expression.charAt(iter) == '.') {
      ++iter;
    }

    double e = 1;
    for (; iter < expression.length(); ++iter) {
      e *= 10;
      frictionalPart = frictionalPart * 10 + getDigit(expression.charAt(iter));
    }
    return new Constant(sign * (integerPart + frictionalPart / e));
  }

}
