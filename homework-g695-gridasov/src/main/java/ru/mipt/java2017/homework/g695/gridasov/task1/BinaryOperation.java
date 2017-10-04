/**
 * Created by ilya on 02.10.17.
 */

package ru.mipt.java2017.homework.g695.gridasov.task1;

public class BinaryOperation implements Operation {

  private Operation leftOperand;
  private Operation rightOperand;
  private char operator;

  BinaryOperation(Operation leftOperand, char operator, Operation rightOperand) {
    this.leftOperand = leftOperand;
    this.rightOperand = rightOperand;
    this.operator = operator;
  }

  public double evaluate() {
    double lv = leftOperand.evaluate();
    double rv = rightOperand.evaluate();
    if (operator == '+') {
      return lv + rv;
    } else if (operator == '-') {
      return lv - rv;
    } else if (operator == '*') {
      return lv * rv;
    } else if (operator == '/') {
      return lv / rv;
    }
    return 0;
  }
}
