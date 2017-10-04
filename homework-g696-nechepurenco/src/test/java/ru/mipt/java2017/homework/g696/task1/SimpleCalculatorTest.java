package ru.mipt.java2017.homework.g696.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.g696.nechepurenco.task1.SimpleCalculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;
public class SimpleCalculatorTest extends AbstractCalculatorTest {
    @Override
    protected Calculator calc() { return new SimpleCalculator(); }
}
