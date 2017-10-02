package ru.mipt.java2017.homework.g695.kalinochkin.task1;

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
  public void testNaN() throws ParsingException {
    test("1/0 + (-1/0)", Double.NaN);
    test("1/0 * 0", Double.NaN);
    test("1/0 / (1/0)", Double.NaN);
  }

  @Test(expected = ParsingException.class)
  public void testSpaceBetweenNumbers() throws ParsingException {
    tryFail("1 2 3");
  }

  @Test
  public void testShortForm() throws ParsingException {
    test("-.1+1", 0.9);
    test("5.+1", 6.0);
  }

  @Test(expected = ParsingException.class)
  public void testTooShortForm() throws ParsingException {
    tryFail("1+.+2");
  }
}
