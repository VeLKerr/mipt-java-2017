/**
 * Created by ilya on 02.10.17.
 */

package ru.mipt.java2017.homework.g695.gridasov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

class MyCalculator implements Calculator {

  private AbstractParser parser;

  MyCalculator() {
    parser = new MainParser();
  }

  public double calculate(String expression) throws ParsingException {
    Operation operation = parser.parse(expression);
    return operation.evaluate();
  }
}
