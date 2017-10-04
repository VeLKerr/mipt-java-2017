package ru.mipt.java2017.homework.g697.ishmukhametov.task1;

import ru.mipt.java2017.homework.base.task1.*;
import java.util.Stack;

public final class ProbablyCalculator implements Calculator {
  private final String NUMBERS = "0123456789";

  @Override
  public double calculate(String expression) throws ParsingException {
    Parser p = new Parser();
    p.Parse(expression);
    String reformated_expression = p.getRez();
    String[] data = reformated_expression.split(" ");
    Stack<Double> CalcOrder = new Stack<>();
    for (int i = 0; i < data.length; i++) {
      if (NUMBERS.indexOf(data[i].charAt(0)) != -1 ||
        data[i].length() >= 2) {
        double t;
        try {
          t = Double.parseDouble(data[i]);
        } catch (Exception e) {
          throw new ParsingException("Problem with decimal", e);
        }
        CalcOrder.add(t);
      } else {
        double b = CalcOrder.pop();
        double a = CalcOrder.pop();
        switch (data[i]) {
          case "+":
            CalcOrder.add(a + b);
            break;
          case "-":
            CalcOrder.add(a - b);
            break;
          case "*":
            CalcOrder.add(a * b);
            break;
          case "/":
            CalcOrder.add(a / b);
            break;
        }
      }
    }
    return CalcOrder.pop();
  }
}