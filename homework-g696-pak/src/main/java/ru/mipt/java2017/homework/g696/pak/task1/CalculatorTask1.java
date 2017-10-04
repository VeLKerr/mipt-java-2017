package ru.mipt.java2017.homework.g696.pak.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class CalculatorTask1 implements Calculator {

  private Parser parser;

  CalculatorTask1() {
    parser = new Parser();
  }

  public double calculate(String expression) throws ParsingException {
    return parser.parse(expression).calculate();
  }
}
