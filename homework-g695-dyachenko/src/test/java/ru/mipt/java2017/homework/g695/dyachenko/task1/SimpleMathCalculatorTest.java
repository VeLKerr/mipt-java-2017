package ru.mipt.java2017.homework.g695.dyachenko.task1;

import org.junit.Assert;
import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class SimpleMathCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new SimpleMathCalculator();
  }

  protected void testNaN(String expression) throws ParsingException {
    double actual = calc().calculate(expression);
    Assert.assertTrue(Double.isNaN(actual));
  }

  @Test
  public void TestSingleLine() throws ParsingException {
    test("-1 +1 + 1   -  1 + 1", 1.0);
    test("7 * 4 / 2 - 3 - 2 / 1", 9.0);
    test("+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1", 26.0);
  }

  @Test
  public void TestMoreExpressions() throws ParsingException {
    test("-2 + 4.134 * (1 + (7.1 - (21 * 5.5)) - (2 * -2)) / 23 / (3.3 - 4.4) + 0.5", 15.39547826086956);
    test("((6 - 13.7) / 101 - 12 + 11 * (2 / 3) * 65.47 + 1 / (0.145 * (-1 / -7)) + 38) * 0.84 - 7", 458.6228845339706);
  }

  @Test
  public void TestOperatorsTogether() throws ParsingException {
    test("7 * +4 / -2 - (2*-3) - 2 / -1/-2 + 1 * + 1", -8.0);
  }

  @Test
  public void TestDeepBrackets() throws ParsingException {
    test("(((((26)))-((12)))*(199-(1)))", 2772.0);
    test("((((((((-2))))))))", -2.0);
    test("-(-(-((-(-(-(-(-2))))))))", 2.0);
  }

  @Test
  public void TestOver100500() throws ParsingException {
    test("100500 * 100500 * 100500", 1015075125000000.0);
    test("100500 * 100500 * 100500 + 1", 1015075125000001.0);
    test("1015075125000000 + 11", 1015075125000011.0);
    test("1913410137981354 + 3", 1913410137981357.0);
    test("1913410137981354 + (3 + (632648932974912 * 10))", 8239899467730477.0);
    test("1000 * 10000000 * 10000 * 10 * 10 * 100000000000", 1e+27);
    test("1 / ((0.001 * 0.0001) * 0.0001 * 0.5 * (1 / 10) / 1000 * 0.1) / 1000000000000000", 20.0);
  }

  @Test
  public void TestZeroMadness() throws ParsingException {
    test("000000.000000", 0.0);
    test("000000.000070", 0.00007);
    test("- 000000.000000", -0.0);
    test("0000071 - 11.0000000000000000002", 60.0);
    
    StringBuilder aVeryLongNumber = new StringBuilder();
    aVeryLongNumber.append('-');
    for (int i = 0; i < 9000; i++) {
        aVeryLongNumber.append('0');
    }
    aVeryLongNumber.append("7421.053");
    for (int i = 0; i < 9000; i++) {
        aVeryLongNumber.append('0');
    }
    test(aVeryLongNumber.toString(), -7421.053);
  }

  @Test
  public void TestExtremelyDeepBrackets() throws ParsingException {
    StringBuilder aVeryDeepExpression = new StringBuilder();
    for (int i = 0; i < 500; i++) {
        aVeryDeepExpression.append("(1 + ");
    }
    aVeryDeepExpression.append("2.8772");
    for (int i = 0; i < 500; i++) {
        aVeryDeepExpression.append(" + 2)");
    }
    test(aVeryDeepExpression.toString(), 1502.8772);
  }

  @Test
  public void TestNaN() throws ParsingException {
    testNaN("0.0 / 0.0");
    testNaN("-0.0 / 0.0");
    testNaN("0.0 / -0.0");
    testNaN("25 - 9 * -(-(-0.0 / 0.0) * 2 - 1)");
    testNaN("(0.0 / 0.0) - (0.0 / 0.0)");
    testNaN("(5.0 / (1-1)) + (-9.0 / (1-1))");
    testNaN("(5.0 / (1-1)) + (9.0 / -(1-1))");
    testNaN("(5.0 / (1-1)) * (2 - 3 + 1)");
  }

  @Test(expected = ParsingException.class)
  public void TestTwoNumbers() throws ParsingException {
    tryFail("12 11 - 5");
  }

  @Test(expected = ParsingException.class)
  public void TestEnoughOperators() throws ParsingException {
    tryFail("12-- 5");
  }

  @Test(expected = ParsingException.class)
  public void TestNumberBracketSpace() throws ParsingException {
    tryFail("5 (4 + 1)");
  }

  @Test(expected = ParsingException.class)
  public void TestBracketNumberSpace() throws ParsingException {
    tryFail("(81 - 3) 51");
  }

  @Test(expected = ParsingException.class)
  public void TestNumberBracketClose() throws ParsingException {
    tryFail("5(2)");
  }

  @Test(expected = ParsingException.class)
  public void TestBracketNumberClose() throws ParsingException {
    tryFail("(1)11");
  }

  @Test(expected = ParsingException.class)
  public void TestVK() throws ParsingException {
    tryFail("0))0))0)))0))))))0)))))))))0)0)))))000)");
  }

  @Test(expected = ParsingException.class)
  public void TestVKReversed() throws ParsingException {
    tryFail("((((((((((9((9((9((9(9((9((999(9((((9(9");
  }
}
