package ru.mipt.java2017.homework.g697.IvaninE.task1;

import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class MyCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected CalculatorRealization calc() {
    return new CalculatorRealization();
  }
}
