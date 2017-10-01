package ru.mipt.java2017.homework.g695.gostkin.task1;

import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 * @author Eugene M. Gostkin
 * @since 22.09.17
 */

public class NewCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected ru.mipt.java2017.homework.base.task1.Calculator calc() {
    return new NewCalculator();
  }
}
