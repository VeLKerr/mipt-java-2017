package ru.mipt.java2017.homework.g695.lunin.task1;

import org.junit.Assert;
import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 * @author Dmitry V. Lunin
 * @since 30.09.17
 */
public class MyCalculatorTest extends AbstractCalculatorTest {

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
    public void goodTests() throws ParsingException {
        test("42 / (42 - 42)", Double.POSITIVE_INFINITY);
        test("(((5 + 6) / 2 + 7) -  3.5 * 3)", 2);
        test("(345 - 44) * ((42 * 3) / ((42 - 5) + (-6)) - -5) + (34 + -45) * 7 - 2.02",
            2649.3993548);
    }

    @Test(expected = ParsingException.class)
    public void badTests() throws ParsingException {
        tryFail("No expression, only letters.");
        tryFail(".");
        tryFail("(())()((()))");
        tryFail("7 + 5 - 2 :)");
        tryFail("(7 + 5 - 2) (666)");
        tryFail("(7 + 5 - 2) + ((666)");
        tryFail("(7 + 5 - 2) + (666))");
        tryFail("+(2++3)");
    }
}
