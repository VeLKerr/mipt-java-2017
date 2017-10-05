package ru.mipt.java2017.homework.g697.vorobev;

import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class MySimpleCalculatorTest extends AbstractCalculatorTest {
  @Override
  protected Calculator calc() {
    return new MySimpleCalculator();
  }

  /**
   * Two operators in a row are not allowed, added brackets
   *
   * @throws ParsingException
   */
  @Test
  @Override
  public void testDivisionByZero() throws ParsingException {
    test("4.5 / 0", Double.POSITIVE_INFINITY);
    test("4.5 / (-0.0)", Double.NEGATIVE_INFINITY);
  }
}