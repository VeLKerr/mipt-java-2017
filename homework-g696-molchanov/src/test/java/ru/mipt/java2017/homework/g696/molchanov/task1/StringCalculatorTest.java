package ru.mipt.java2017.homework.g696.molchanov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 * Created by mike on 10/6/17.
 */

public class StringCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new StringCalculator();
  }
}