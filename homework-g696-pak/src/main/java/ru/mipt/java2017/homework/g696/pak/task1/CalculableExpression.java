package ru.mipt.java2017.homework.g696.pak.task1;

import sun.security.pkcs.ParsingException;

import java.util.*;

public class CalculableExpression implements Calculable {

    private LinkedList<ExpressionForCalculation> expression_;

    public CalculableExpression(LinkedList<ExpressionForCalculation> expression) {
        expression_ = (LinkedList<ExpressionForCalculation>) expression.clone();
    }

    @Override
    public double calculate() throws ParsingException {
        LinkedList<ExpressionForCalculation> expression =
            (LinkedList<ExpressionForCalculation>) expression_.clone();
        ListIterator<ExpressionForCalculation> iter = expression.listIterator();
        while (iter.hasNext()) {
            ExpressionForCalculation expressionForCalculation = iter.next();
            if (!expressionForCalculation.isConstant()) {
                iter.remove();
                if (!iter.hasPrevious())
                    throw new ParsingException();
                ExpressionForCalculation tmp = iter.previous();
                if (!tmp.isConstant())
                    throw new ParsingException();

                Constant value1 = (Constant) tmp;
                iter.remove();

                if (!iter.hasPrevious())
                    throw new ParsingException();

                tmp = iter.previous();
                if (!tmp.isConstant())
                    throw new ParsingException();

                Constant value2 = (Constant) tmp;

                iter.remove();

                Constant value = ((Operator) expressionForCalculation).calculate(value2, value1);
                iter.add(value);
            }
        }
        if (expression.size() != 1 || !expression.get(0).isConstant())
            throw new ParsingException();
        return ((Constant) iter.previous()).calculate();
    }
}
