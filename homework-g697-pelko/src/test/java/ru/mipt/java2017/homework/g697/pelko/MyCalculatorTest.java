package ru.mipt.java2017.homework.g697.pelko.task1;
/**
 * Created by Pelko Andrew
 * MIPT DIHT student
 * on 06.10.2017
 */

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class MyCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new MyCalculator();
  }
}
