package ru.mipt.java2017.homework.g694.perchuk.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class MyCalculator implements Calculator {

    public double calculate(String expression) throws ParsingException {
        if (expression == null) {
            throw new ParsingException("Null expression");
        }
        /*try {
            Evaluator evaluator = buildEvaluator();
            String result = evaluator.evaluate(expression);
            return Double.parseDouble(result);
        } catch (EvaluationException e) {
            throw new ParsingException("Invalid expression", e.getCause());
        }*/
        return 1.0;
    }
}
