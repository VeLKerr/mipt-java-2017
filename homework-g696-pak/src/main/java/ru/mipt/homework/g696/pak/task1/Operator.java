package ru.mipt.homework.g696.pak.task1;

public class Operator implements ExpressionForCalculation {
    private String operator_;
    Operator(String operator){
        operator_ = String.copyValueOf(operator.toCharArray());
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    public Constant calculate(Constant value1, Constant value2){
        Constant value;
        if(operator_.equals("+"))
            value = new Constant(value1.calculate() + value2.calculate());
        else
        if(operator_.equals("-"))
            value = new Constant(value1.calculate() - value2.calculate());
        else
        if(operator_.equals("*"))
            value = new Constant(value1.calculate() * value2.calculate());
        else
            value = new Constant(value1.calculate() / value2.calculate());

        return value;
    }
}
