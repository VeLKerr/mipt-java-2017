package ru.mipt.java2017.sm03;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrivialCalculatorSumTest {

    TrivialCalculator calculator;

    @Test
    public void correctTest() throws ExpressionParseException {
        String expression = "1+2+3+4";
        int expected = 10;
        int result = calculator.calculateSum(expression);
        assertEquals(expected, result);
    }

    @Test(expected = ExpressionParseException.class)
    public void incorrectTest() throws ExpressionParseException {
        String expression = "один+два+три+четыре";
        calculator.calculateSum(expression);
    }

    @Before
    public void initialize() {
        calculator = new TrivialCalculator();
    }

    @After
    public void finalize() {
        calculator = null;
        System.gc(); // Bad practice!!!!
    }
}
