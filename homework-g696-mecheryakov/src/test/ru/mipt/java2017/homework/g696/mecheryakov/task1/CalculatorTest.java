package ru.mipt.java2017.homework.g696.mecheryakov.task1;

import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class CalculatorTest extends  AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new MyCalculator();
  }

  @Test
  public void testManyUnary() throws ParsingException {
    test("-(+(-(+(-(-5)))))", 5);
  }

  @Test(expected = ParsingException.class)
  public void testOnlyBraces() throws ParsingException {
    tryFail("(()))()((");
    tryFail("(())((()()))");
  }

  @Test
  public void testJustNumber() throws ParsingException {
    test("234", 234);
  }
}
