package ru.mipt.java2017.homework.g695.gazizullin.task1;


import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.g695.gazizullin.task1.SimpleCalculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class java2017CalculatorTest extends AbstractCalculatorTest {

    @Override
    protected Calculator calc() {
        return new SimpleCalculator();
    }

    @Test
    public void TestOnlyPlusExpessionWithNormalSpaces () throws ParsingException {
        test("5 + 3 + 1.0 + 778", 787.0);
    }

    @Test
    public void TestOnlyPlusExpessionWithBigSpaces () throws ParsingException {
        test("5    + 333 +    1.0   +    778", 1117.0);
    }

    @Test
    public void TestOnlyPlusExpessionWithoutSpaces () throws ParsingException {
        test("7+3+1.0+778", 789.0);
    }

    @Test
    public void TestOnlyMinusExpessionWithNormalSpaces () throws ParsingException {
        test("5 - 3 - 1.0 - 778", -777.0);
    }

    @Test
    public void TestOnlyMinusExpessionWithBigSpaces () throws ParsingException {
        test("5    - 333 -    1.0   -    778  ", -1107.0);
    }

    @Test
    public void TestOnlyMinusExpessionWithoutSpaces () throws ParsingException {
        test("7-3-1.0-778", -775.0);
    }

    @Test
    public void TestUnaryMinus () throws ParsingException {
        test("-7 + 3", -4);
    }

    @Test
    public void TestUnaryPlus1 () throws ParsingException {
        test("7 + (+3)", 10);
    }

    @Test
    public void TestUnaryPlus2 () throws ParsingException {
        test("7 + ( + (3 + 876 + 972 + (+(3)) ) )", 1861);
    }

    @Test(expected = ParsingException.class)
    public void TestUnaryPlusFail () throws ParsingException {
        tryFail("7 + (++3)");
    }

    @Test
    public void TestPriority() throws ParsingException {
        test("2 + 3 * 2 + 3 / (0 + 4 /2 + 7 * 3 - 21)", 9.5);
    }

    @Test
    public void TestBigTestOfUnary () throws ParsingException {
        test("875 / - 5  + 34", -141);
        test("875 / +5 - 34", 141);
        test(" - 875 / - 5.0", 175);
        test("875 / -(0.0 + ( + 3 ) + 58) + 12", 26.34426229508197);
        test("875 / + (0.0 + ( + 3 ) + 58) + 7", 21.34426229508197);

    }

    @Test
    public void TestNullMinusNull () throws ParsingException {
        test("0 - 0.0", -0.0);
    }

    @Test
    public void TestInfinity() throws ParsingException {
        test("234 / 0", Double.POSITIVE_INFINITY);
        test("875 / -0.0", Double.NEGATIVE_INFINITY);
    }

}
