package ru.mipt.java2017.homework.g697.RaskinIlya.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 * @author Ilya I. Raskin
 * @since 02.10.17
 */


public class ForActCalculatorTest extends AbstractCalculatorTest {
    @Override
    protected Calculator calc() { return new ForActCalculator(); }
}
