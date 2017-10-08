package ru.mipt.java2017.sm03;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrivialCalculatorProdTest {

    TrivialCalculator calculator;

    @Test
    public void correctTest() throws ExpressionParseException {
        String expression = "1*2*3*4";
        long expected = 24;
        long result = calculator.calculateProd(expression);
        assertEquals(expected, result);
    }

    @Test(expected = ExpressionParseException.class)
    public void incorrectTest() throws ExpressionParseException {
        String expression = "abrakadabra";
        calculator.calculateProd(expression);
    }

    @Before
    public void initialize() {
        calculator = new TrivialCalculator();
    }

    @After
    public void finalize() {
        calculator = null;
//        System.gc(); // Bad practice!!!!
    }
}
