package ru.mipt.java2017.homework.g694.chizh.task1;

import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;


public class MyCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new MyCalculator();
  }

  @Test
  public void testBracketedMinusAfterDivision() throws ParsingException {
    test("4.5 / (-0.0)", Double.NEGATIVE_INFINITY);
  }

  @Test(expected = ParsingException.class)
  public void testMinusAfterDivision() throws ParsingException {
    tryFail("4.5 / -0.0");
  }
}