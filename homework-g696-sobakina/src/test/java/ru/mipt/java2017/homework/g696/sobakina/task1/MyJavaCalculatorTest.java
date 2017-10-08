package ru.mipt.java2017.homework.g696.sobakina.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;


/**
 *
 * @author Sobakina Olga
 * @since 05.10.17
 *
 */


public class MyJavaCalculatorTest extends AbstractCalculatorTest {
  @Override
  protected Calculator calc() { return new MyJavaCalculator(); }
}