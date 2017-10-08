package ru.mipt.java2017.homework.g695.loginov.task1;

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
  public void checkManyUnaryOperators() throws ParsingException {
    test("1 +++--+ + 10", 11);
    test("10 - ++ - 10 + -- 20 / - --1", 0);
  }

  @Test(expected = ParsingException.class)
  public void testOnlyBraces() throws ParsingException {
    calc().calculate("(()()()(()))");
  }
}