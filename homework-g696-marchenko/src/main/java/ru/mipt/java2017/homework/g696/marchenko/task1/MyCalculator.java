package ru.mipt.java2017.homework.g696.marchenko.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.g696.marchenko.task1.MyParser.MyExpression;

public class MyCalculator implements Calculator {

  @Override
  public double calculate(String expression) throws ParsingException {
    return MyExpression.parse(expression).count();
  }
}
