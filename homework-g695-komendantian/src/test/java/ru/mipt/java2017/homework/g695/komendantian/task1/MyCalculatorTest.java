package ru.mipt.java2017.homework.g695.komendantian.task1;

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
  public void myTests() throws ParsingException {
    test("0.0 / 0 + 5", Double.NaN);
    test("9*9*9*9*9*9*9*9*9*9*9*9*9*9*9*9*9",
      9.0 * 9 * 9 * 9 * 9 * 9 * 9 * 9 * 9 * 9 * 9 * 9 * 9 * 9 * 9 * 9 * 9);
    test("-(-(-(-(-1))))", -1);
    test("+(-(+(-((1)))))", 1);
  }

  @Test(expected = ParsingException.class)
  public void tryWithoutNumbers() throws ParsingException {
    tryFail("-(-(-()))");
  }

  @Test(expected = ParsingException.class)
  public void tryEmpty() throws ParsingException {
    tryFail("");
  }

  @Test(expected = ParsingException.class)
  public void tryNull() throws ParsingException {
    tryFail("");
  }
}