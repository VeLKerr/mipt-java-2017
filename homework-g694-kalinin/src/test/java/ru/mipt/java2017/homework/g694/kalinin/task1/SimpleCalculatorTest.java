package ru.mipt.java2017.homework.g694.kalinin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;


/**
 *
 * @author Stepan A. Kalinin
 * @since 21.09.17
 *
 */


public class SimpleCalculatorTest extends AbstractCalculatorTest {
    @Override
    protected Calculator calc() { return new SimpleCalculator(); }
}
