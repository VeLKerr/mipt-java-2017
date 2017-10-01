package ru.mipt.java2017.homework.g695.skakovsky.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 *
 * @author Uriy
 * @ 01.01.17
 *
 */


public class UraCalculatorTest extends AbstractCalculatorTest {

    @Override
    protected Calculator calc() {
        return new UraCalculator();
    }

}
