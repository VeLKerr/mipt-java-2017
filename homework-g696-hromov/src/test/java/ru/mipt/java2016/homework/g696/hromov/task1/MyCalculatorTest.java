package ru.mipt.java2016.homework.g696.hromov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 * Created by igorhromov on 10.10.16.
 */
public class MyCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new MyCalculator();
  }
}
