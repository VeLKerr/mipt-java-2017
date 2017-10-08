package ru.mipt.java2017.homework.g695.buchnev.task1;

import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class java2017CalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new SimpleCalculator();
  }

  @Test
  public void MyTests() throws ParsingException {
    test("2.5", 2.5);
    test("(111)", 111);
    test("(4+5)", 9);
    test("(4+5)/2", 4.5);
    test("1+2-(4+5)/2", -1.5);
    test("1+2-(4+5)/2.5", -0.6);
    test("(1+6/ 3) * (2 - 1)", 3);
    test("-1", -1);
    test("-(-1)", 1);
    test(" 7.0 \t/   3.5 ", 2.0);
    test(" (6.0  ) + \t( - 4) * (  0.0 +\n 5/2)", -4.0);
  }
}