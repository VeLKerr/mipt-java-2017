package ru.mipt.java2017.homework.g694.veselev.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;


/**
 *
 * @author Aleksandr N. Veselev
 * @since 01.10.2017
 *
 */


public class JEvalCalculatorTest extends AbstractCalculatorTest {
  @Override
  protected Calculator calc() { return new JEvalCalculator(); }
}
