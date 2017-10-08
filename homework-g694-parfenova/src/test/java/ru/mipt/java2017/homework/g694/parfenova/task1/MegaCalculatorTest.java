package ru.mipt.java2017.homework.g694.parfenova.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class MegaCalculatorTest extends AbstractCalculatorTest {
  protected Calculator calc() {
    return new MegaCalculator();
  }
}
