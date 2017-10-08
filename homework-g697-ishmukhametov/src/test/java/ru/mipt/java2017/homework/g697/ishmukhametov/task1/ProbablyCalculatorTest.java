package ru.mipt.java2017.homework.g697.ishmukhametov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 *
 * @author Ivanin Vitaly @alloky
 * @since 30.09.17
 *
 */

public class ProbablyCalculatorTest extends  AbstractCalculatorTest{

  @Override
  protected Calculator calc() {
    return new ProbablyCalculator();
  }

}
