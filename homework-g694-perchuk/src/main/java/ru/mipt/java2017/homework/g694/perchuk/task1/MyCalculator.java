package ru.mipt.java2017.homework.g694.perchuk.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * @author Perchuk Svyatoslav
 * @since 01.10.2017
 */
public class MyCalculator implements Calculator {

    public double calculate(String expression) throws ParsingException {
        if (expression == null) {
            throw new ParsingException("Null expression");
        }

        Evaluator evaluator = new Evaluator();
        Parser parser = new Parser(evaluator);
        parser.parse(expression);
        return evaluator.completeEvaluation();
    }
}
