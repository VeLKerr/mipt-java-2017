package ru.mipt.homework.g696.pak.task1;

public class Constant implements ExpressionForCalculation, Calculable{
    private double constant_;
    Constant(double constant){
        constant_ = constant;
    }

    @Override
    public double calculate() {
        return constant_;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
