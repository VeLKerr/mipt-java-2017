package ru.mipt.java2017.homework.g696.zhukovskiy.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 *
 * @author Anatoly M. Zhukovskiy
 * @since 5.10.17
 *
 */


public class SimpleCalculatorTest extends AbstractCalculatorTest {
    @Override
    protected Calculator calc() { return new SimpleCalculator(); }
}
