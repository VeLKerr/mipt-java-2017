package ru.mipt.java2017.homework.g696.feofanova.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/** ShuntingYardTokenCalculatorTest extends AbstractCalculatorTest. Tests my calculator. */
public class ShuntingYardTokenCalculatorTest extends AbstractCalculatorTest {
    // define the fabric with certain calculator.
    protected Calculator calc() {
    return new ShuntingYardTokenCalculator();
  }
}
