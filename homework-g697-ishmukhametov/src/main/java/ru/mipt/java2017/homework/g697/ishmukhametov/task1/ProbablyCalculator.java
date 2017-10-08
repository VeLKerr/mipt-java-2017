package ru.mipt.java2017.homework.g697.ishmukhametov.task1;

import ru.mipt.java2017.homework.base.task1.*;
import java.util.Stack;

/*
*
* Calculator based on the reverse Polish entry
* Expression have to be rebuilded by rebuilder
*
 */

public final class ProbablyCalculator implements Calculator {

  static final String NUMBERS = "0123456789";
  static final String OPERATORS = "+-*/";

  @Override
  public double calculate(String expression) throws ParsingException {

    Rebuilder p = new Rebuilder();
    p.rebuild(expression);
    String rebuildedExpression = p.getRez();

    String[] data = rebuildedExpression.split(" ");
    Stack<Double> calcOrder = new Stack<>();

    for (int i = 0; i < data.length; i++) {
      /* If first symbol is number or length of element
      more than one symbol then it is number for sure.
      Or we have incorrect number
      */
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
        /* if symbol is operator then it will be calculated */
        if (OPERATORS.indexOf(data[i]) != -1) {
          Double b = calcOrder.pop();
          Double a = calcOrder.pop();
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
              /* We have something neither number, nor our operator.
              And it is strange
              */
              throw new ParsingException("Unexpected error");
          }
        } else {
          /* We have something neither number, nor our operator.
          And it is strange
          */
          throw new ParsingException("Unexpected error");
        }
      }
    }
    return calcOrder.pop();
  }
}