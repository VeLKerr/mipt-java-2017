package ru.mipt.java2017.homework.g697.ozherelieva.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 * @author Ozherelieva Sofia
 * @since 04.10.17
 */

public class MyCalculatorTest extends  AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new MyCalculator();
  }

}
