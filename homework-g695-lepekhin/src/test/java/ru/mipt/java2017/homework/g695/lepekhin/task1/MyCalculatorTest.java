package ru.mipt.java2017.homework.g695.lepekhin.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;
import org.junit.Assert;
import org.junit.Test;

public class MyCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new MyCalculator();
  }

  @Test
  public void ManyMinuses() throws ParsingException {
    test("-(-(-(-(-(3.14)))))", -3.14);
    test("-(+(-(+(-(+(-(776.58)))))))", 776.58);
  }

  @Test
  public void Difficult() throws ParsingException {
    test("-(7 / 3.5 + 4 * (5 + 3 / (96 - 95)) - (-(16.2+11.8 - (-2.0))))", -64.0);
  }

  @Test
  public void ZeroToZero() throws ParsingException {
    tryFail("0/(64 -(7 / 3.5 + 4 * (5 + 3 / (96 - 95)) - (-(16.2+11.8 - (-2.0)))))");
  }

  @Test
  public void TwoMinuses() throws ParsingException {
    test("-5*-(+6)", 30);
  }

  @Test
  public void Chain() throws ParsingException {
    test("7/7*7/7*7/7*7/7", 1);
  }
}
