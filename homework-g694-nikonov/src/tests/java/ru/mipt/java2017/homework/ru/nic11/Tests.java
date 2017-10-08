package ru.mipt.java2017.homework.ru.nic11;

import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class Tests extends AbstractCalculatorTest {
  @Override
  protected Calculator calc() {
    return new CalculatorImplementation();
  }

  @Test
  public void testChain() throws ParsingException {
    test("7/7*7/7*7/7*7/7", 1);
    test("1*2*3*4*5/1/3/4/2/5", 1);
    test("1/2/3/4/5*2*3*4*5", 1);
  }

  @Test
  public void test1e9() throws ParsingException {
    test("(1 +9)*(2+8)*(3+7)*(4+ 6) * (5 + 5)*(6+4)*(7+3)*(8+2)*(9+1)", 1e9);
    test("2/1*2*1/4*10000*(100*2*100)*5", 1e9);
  }

  @Test
  public void testZeroByZero() throws ParsingException {
    tryFail("(0+2)/17 + (+(+14) - 14) / (4 - 17 + 13)");
    tryFail("(2-2)/(3*3--(-9))");
  }
}
