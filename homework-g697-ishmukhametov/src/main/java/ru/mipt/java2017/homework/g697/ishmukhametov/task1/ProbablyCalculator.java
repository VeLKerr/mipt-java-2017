package ru.mipt.java2017.homework.g697.ishmukhametov.task1;

import ru.mipt.java2017.homework.base.task1.*;
import java.util.Stack;

public final class ProbablyCalculator implements Calculator {

  static final String NUMBERS = "0123456789";

  @Override
  public double calculate(String expression) throws ParsingException {
    Parser p = new Parser();
    p.rebuild(expression);
    String rebuildedExpression = p.getRez();
    String[] data = rebuildedExpression.split(" ");
    Stack<Double> calcOrder = new Stack<>();
    for (int i = 0; i < data.length; i++) {
      if (NUMBERS.indexOf(data[i].charAt(0)) != -1 ||
        data[i].length() >= 2) {
        double t;
        try {
          t = Double.parseDouble(data[i]);
        } catch (Exception e) {
          throw new ParsingException("Problem with decimal", e);
        }
        calcOrder.add(t);
      } else {
        double b = calcOrder.pop();
        double a = calcOrder.pop();
        switch (data[i]) {
          case "+":
            calcOrder.add(a + b);
            break;
          case "-":
            calcOrder.add(a - b);
            break;
          case "*":
            calcOrder.add(a * b);
            break;
          case "/":
            calcOrder.add(a / b);
            break;
          default:
            throw new ParsingException("Unexpected error");
        }
      }
    }
    return calcOrder.pop();
  }
}