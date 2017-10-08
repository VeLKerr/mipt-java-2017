package ru.mipt.java2017.homework.g695.lobov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.g695.lobov.task1.AmazingCalculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;


public class AmazingCalculatorTest extends AbstractCalculatorTest{
  @Override
  protected Calculator calc() {
    AmazingCalculator calc = new AmazingCalculator();
    return calc;
  }
}
