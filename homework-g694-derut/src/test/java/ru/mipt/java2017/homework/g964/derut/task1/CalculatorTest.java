
package ru.mipt.java2017.homework.g964.derut.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

import ru.mipt.java2017.homework.g964.derut.taskN1.MyCalculator;


public class CalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new MyCalculator();
  }
}
