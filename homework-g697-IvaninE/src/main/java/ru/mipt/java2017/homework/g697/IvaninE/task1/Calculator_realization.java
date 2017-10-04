package ru.mipt.java2017.homework.g697.IvaninE.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

class CalculatorRealization implements Calculator {

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("ERROR: Where is the line?");

    }
    expression = expression.replaceAll("\\s+", "");
    if (expression.length() == 0) {
      throw new ParsingException("ERROR: This string is empty");
    }
    Calculation answer = new Calculation();
    return answer.evaluate(expression);
  }
}
