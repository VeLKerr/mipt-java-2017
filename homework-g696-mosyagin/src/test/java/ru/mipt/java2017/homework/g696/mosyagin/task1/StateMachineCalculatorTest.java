package ru.mipt.java2017.homework.g696.mosyagin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

/**
 * @author Mikhail Mosyagin
 * @since 24.09.17
 */
public class StateMachineCalculatorTest extends AbstractCalculatorTest {
    @Override
    protected Calculator calc() {
        return new StateMachineCalculator();
    }
}
