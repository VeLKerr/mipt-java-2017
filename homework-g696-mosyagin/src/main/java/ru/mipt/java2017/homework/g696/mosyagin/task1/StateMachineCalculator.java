package ru.mipt.java2017.homework.g696.mosyagin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * Calculator implementation that uses state machines.
 *
 * @author Mosyagin Mikhail
 * @since 24.09.17
 */
public class StateMachineCalculator implements Calculator {
    @Override
    public double calculate(String expression) throws ParsingException {
        if (expression == null) {
            throw new ParsingException("Cannot parse null string");
        }

        StateMachine parser = new StateMachine();
        for (int index = 0; index < expression.length(); index++) {
            parser.transit(expression.charAt(index));
        }
        parser.transit(')');

        if (!parser.hasFinished()) {
            throw new ParsingException("Cannot find the end of the expression");
        }

        return parser.getResult().evaluateExpression();
    }
}
