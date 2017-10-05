package ru.mipt.java2017.homework.g696.pak.task1;

import org.junit.Test;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class TestCalc extends AbstractCalculatorTest  {

  @Override
  protected Calculator calc() {
    return new CalculatorTask1();
  }

  @Test(expected = ParsingException.class)
  public void divDivTest() throws ParsingException {
    tryFail("1//5");
  }

  @Test(expected = ParsingException.class)
  public void minusMinusTest() throws ParsingException {
    tryFail("1--5");
  }

  @Test(expected = ParsingException.class)
  public void MultMultTest() throws ParsingException {
    tryFail("1**5");
  }

  @Test
  public void testLargeExepression() throws ParsingException {
    test("1.1+5 -(-4.2)/5 * (5-4*-1)+(4*1/2) - 44 - ((-5)/5 *6+(6*8-4))",
      1.1 + 5 - (-4.2) / 5 * (5 - 4 * -1) + (4 * 1 / 2) - 44 - ((-5) / 5 * 6 + (6 * 8 - 4)));
  }

  @Test
  public void testInf() throws ParsingException {
    test("1/0.0 + 4", Double.POSITIVE_INFINITY);
    test("1/-1/(-5 - (-5))      -(-(-100))", Double.NEGATIVE_INFINITY);
  }
}
