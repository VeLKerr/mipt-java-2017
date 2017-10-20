package ru.mipt.java2017.seminars.seminar3;

import org.junit.*;

import static org.junit.Assert.assertEquals;

public class TrivialCalculatorTest {

    private TrivialCalculator calculator;

    @Before
    public void setup() {
        calculator = new TrivialCalculator();
    }

    @After
    public void treaddown() {
        calculator = null;
    }

    @Test
    @Ignore
    public void testSum() {
        assertEquals(5, calculator.calculate("2+3"));
        assertEquals(5, calculator.calculate("1+1+3"));
    }

    @Test
    @Ignore
    public void testDivide() {
        assertEquals(2, calculator.calculate("4/2"));
    }

    @Test
    @Ignore
    public void testDivideSum() {
        assertEquals(2, calculator.calculate("4/1+1"));
        assertEquals(2, calculator.calculate("2+2/2"));
        assertEquals(3, calculator.calculate("3+3/1+1"));
    }
}
