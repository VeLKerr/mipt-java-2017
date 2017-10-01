package ru.mipt.java2017.homework.g695.gostkin.task1;

import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Eugene M. Gostkin
 * @since 22.09.17
 */
public class NewCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected ru.mipt.java2017.homework.base.task1.Calculator calc() {

    return new NewCalculator();
  }

  protected void test(String expression, double expected) throws ParsingException {
    String errorMessage = String
      .format("Bad result for expression '%s', %.2f expected", expression, expected);
    double actual = calc().calculate(expression);
    Assert.assertEquals(errorMessage, expected, actual, 1e-6);
  }

  protected void tryFail(String expression) throws ParsingException {
    calc().calculate(expression);
  }

  @Test
  public void numberTestMiddle() throws ParsingException {
    test("(((3 + 2) / (1 + 1) + 3) -  2.5 * 2)", 0.5);
  }

  @Test
  public void numberTestZeroDivision() throws ParsingException {
    test("1 / (1 - 1)", Double.POSITIVE_INFINITY);
  }

  @Test
  public void numberTestUnary() throws ParsingException {
    test("(1) + (1) + (-(1))", 1);
  }

  @Test(expected = ParsingException.class)
  public void exceptionTestLetters() throws ParsingException {
    tryFail("Hola!");
  }

  @Test(expected = ParsingException.class)
  public void exceptionTestDotNumber() throws ParsingException {
    tryFail(".45");
  }

  @Test(expected = ParsingException.class)
  public void exceptionTestBraces() throws ParsingException {
    tryFail("((()(())))()");
  }

  @Test(expected = ParsingException.class)
  public void exceptionTestInvalidBrace() throws ParsingException {
    tryFail("5 + (2 + 3");
  }

  @Test(expected = ParsingException.class)
  public void exceptionTestUnary() throws ParsingException {
    tryFail("2+++3");
  }
}
