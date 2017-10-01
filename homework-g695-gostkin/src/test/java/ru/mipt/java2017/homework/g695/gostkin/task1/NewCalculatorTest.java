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
  public void numberTests() throws ParsingException {
    test("(((3 + 2) / (1 + 1) + 3) -  2.5 * 2)", 0.5);
    test("1 / (1 - 1)", Double.POSITIVE_INFINITY);
    test("(1) + (1) + (-(1))", 1);
  }

  @Test(expected = ParsingException.class)
  public void exceptionTests() throws ParsingException {
    tryFail("Hola!");
    tryFail(".45");
    tryFail("((()(())))()");
    tryFail("5 + (2 + 3");
    tryFail("2-+3");
  }
}
