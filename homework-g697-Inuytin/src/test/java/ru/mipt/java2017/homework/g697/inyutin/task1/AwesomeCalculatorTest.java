package ru.mipt.java2017.homework.g697.inyutin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.g697.inyutin.task1.task1.AwesomeCalculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 *
 * @author Dmitry A. Inyutin
 * @since 30.09.18
 *
 */

public class AwesomeCalculatorTest extends AbstractCalculatorTest {
  @Override
  protected Calculator calc() {
    AwesomeCalculator calc = new AwesomeCalculator();
    return calc;
  }
}
