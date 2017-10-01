package ru.mipt.java2017.homework.g696.mosyagin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 * Tests for {@code StateMachineCalculator}.
 *
 * @author Mikhail Mosyagin
 */
public class StateMachineCalculatorTest extends AbstractCalculatorTest {

  /**
   * Factory method that creates a {@code StateMachineCalculator}.
   *
   * @return a {@code StateMachineCalculator} to be tested
   */
  @Override
  protected Calculator calc() {
    return new StateMachineCalculator();
  }
}
