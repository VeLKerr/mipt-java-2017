package ru.mipt.java2017.homework.g696.bobylev.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 *
 * @author Igor V. Bobylev
 * @since 03.10.17
 *
 */
public class ArithmeticCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new ArithmeticCalculator();
  }
}
