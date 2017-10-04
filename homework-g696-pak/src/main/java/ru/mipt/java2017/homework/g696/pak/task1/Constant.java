package ru.mipt.java2017.homework.g696.pak.task1;

public class Constant implements ExpressionForCalculation, Calculable {
  private double constant;

  Constant(double constant) {
    this.constant = constant;
  }

  @Override
  public double calculate() {
    return constant;
  }

  @Override
  public boolean isConstant() {
    return true;
  }
}
