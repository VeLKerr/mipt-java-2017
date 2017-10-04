package ru.mipt.java2017.homework.g697.moiseeva.task1;

  import org.junit.Assert;
  import org.junit.Test;
  import ru.mipt.java2017.homework.base.task1.Calculator;
  import ru.mipt.java2017.homework.base.task1.ParsingException;
  import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 *
 * @author Moiseeva Anastasia @alloky
 * @since 04.10.17
 *
 */

public class MyCalculatorTest extends  AbstractCalculatorTest{

  @Override
  protected Calculator calc() {
    return new MyCalculator();
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
  public void testGood() throws ParsingException {
    test("1", 1.0);
    test("3/(0+3-3)", Double.POSITIVE_INFINITY);
    test("(10 - 8 + 3*\n5)*((2*15)/(14 - 4) + (-2)\t) - 3*3*2.01", -1.09);
  }

  @Test(expected = ParsingException.class)
  public void testBad() throws ParsingException {
    tryFail(".");
    tryFail("\t\n()");
    tryFail("(()()((())())())");
    tryFail("2 + 3*4--1\n");
    tryFail("1+");
  }
}

