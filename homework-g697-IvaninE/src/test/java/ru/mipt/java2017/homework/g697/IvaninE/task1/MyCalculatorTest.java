package ru.mipt.java2017.homework.g697.IvaninE.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class MyCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator_realization calc() {
    return new Calculator_realization();
  }
}
