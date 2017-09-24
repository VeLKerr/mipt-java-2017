package ru.mipt.java2017.homework.g696.feofanova.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class ShuntingYardTokenCalculatorTest extends AbstractCalculatorTest {
  protected Calculator calc() {
    return new ShuntingYardTokenCalculator();
  }
}
