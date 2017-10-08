package ru.mipt.java2017.homework.g696.bogomolov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 * @author Mikhail Bogomolov
 * @since 04.10.2017
 */
public class SimpleCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new SimpleCalculator();
  }
}
