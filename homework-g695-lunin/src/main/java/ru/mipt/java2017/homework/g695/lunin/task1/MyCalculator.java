package ru.mipt.java2017.homework.g695.lunin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

/**
 * @author Dmitry V.Lunin
 * @since 30.09.17
 */
public class MyCalculator implements Calculator {

  private interface CalculateItems {

  }

  private interface StackItems {

  }

  private class Number implements CalculateItems {

    private double val = 0;

    private Number(double a) {
      val = a;
    }

    private double getValue() {
      return val;
    }
  }

  private class Bracket implements StackItems {

  }

  private class Operator implements CalculateItems, StackItems {

    private int priotity;
    private char operation;

    private Operator(char c) {
      switch (c) {
        case '/':
          priotity = 1;
          operation = '/';
          break;
        case '*':
          priotity = 1;
          operation = '*';
          break;
        case '-':
          priotity = 2;
          operation = '-';
          break;
        case '+':
          priotity = 2;
          operation = '+';
          break;
        default:
          break;
      }
    }

    private Number resultValue(Number a, Number b) {
      switch (operation) {
        case '+':
          return new Number(a.getValue() + b.getValue());
        case '-':
          return new Number(a.getValue() - b.getValue());
        case '*':
          return new Number(a.getValue() * b.getValue());
        case '/':
          return new Number(a.getValue() / b.getValue());
        default:
          break;
      }
      return null;
    }
  }

  private String deleteWhitespace(String expression) throws ParsingException {
    StringBuilder currentBuf = new StringBuilder();
    int n = expression.length();
    char previous = ' ';
    boolean isSpace = false;
    for (int i = 0; i < n; ++i) {
      char c = expression.charAt(i);
      if (!Character.isWhitespace(c)) {
        if (Character.isDigit(previous) && Character.isDigit(c) && isSpace) {
          throw new ParsingException("Two numbers without any operator");
        }
        currentBuf.append(c);
        previous = c;
        isSpace = false;

      } else {
        isSpace = true;
      }
    }
    return currentBuf.toString();
  }

  private static int helper;
  private static boolean unary;
  private static boolean isPreviousNumber;
  private static double sign;

  private void solverForDigit(String expression, Stack<CalculateItems> calcItems, Integer i)
      throws ParsingException {
    int n = expression.length();
    char c = expression.charAt(i);
    boolean hasDot = false;
    helper = i;
    StringBuilder curr = new StringBuilder();
    for (; helper < n && (Character.isDigit(c) || c == '.'); ++helper) {
      curr.append(c);
      if (helper + 1 < n) {
        c = expression.charAt(helper + 1);
        if (c == '.') {
          if (hasDot) {
            throw new ParsingException("Problem with a dot");
          }
          hasDot = true;
        }
      }
    }
    --helper;
    String currExpression = curr.toString();
    if (currExpression.endsWith(".")) {
      throw new ParsingException("Problem with a dot");
    }
    double currNumber = Double.parseDouble(currExpression);
    calcItems.add(new Number(sign * currNumber));
    sign = 1;
    unary = false;
    isPreviousNumber = true;
  }

  private void solverForBrackets(char c, Stack<StackItems> stackItems,
      Stack<CalculateItems> calcItems) throws ParsingException {
    if (c == '(') {
      unary = true;
      stackItems.add(new Bracket());
      isPreviousNumber = false;
    } else if (c == ')') {
      if (!isPreviousNumber) {
        throw new ParsingException("Problem with ')'");
      }
      unary = false;
      while (!stackItems.empty() && !(stackItems.peek() instanceof Bracket)) {
        StackItems tmp = stackItems.peek();
        if (tmp instanceof Operator) {
          calcItems.add((Operator) tmp);
          stackItems.pop();
        } else {
          break;
        }
      }
      if (stackItems.empty()) {
        throw new ParsingException("Too many ')' ");
      } else {
        stackItems.pop();
      }
      isPreviousNumber = true;
    }
  }

  private void solverForOperator(char c, Stack<StackItems> stackItems,
      Stack<CalculateItems> calcItems) throws ParsingException {
    if (unary) {
      if (c == '*' || c == '/') {
        throw new ParsingException("Unary is * or /");
      } else if (c == '-') {
        sign *= -1;
      }
      unary = false;

    } else {
      if (!isPreviousNumber) {
        throw new ParsingException("Operator without previous number");
      }
      Operator curr = new Operator(c);
      while (!stackItems.empty()) {
        StackItems tmp = stackItems.peek();
        if (tmp instanceof Operator && ((Operator) tmp).priotity <= curr.priotity) {
          calcItems.add((Operator) tmp);
          stackItems.pop();
        } else {
          break;
        }
      }
      stackItems.push(curr);
      unary = true;

    }
    isPreviousNumber = false;
  }

  private Stack<CalculateItems> polishNotation(String expression) throws ParsingException {
    expression = deleteWhitespace(expression);
    Stack<StackItems> stackItems = new Stack<>();
    Stack<CalculateItems> calcItems = new Stack<>();
    int n = expression.length();
    sign = 1;
    unary = true;
    isPreviousNumber = false;
    for (int i = 0; i < n; ++i) {
      char c = expression.charAt(i);
      if (Character.isDigit(c)) {
        solverForDigit(expression, calcItems, i);
        i = helper;
      } else if (c == '.') {
        throw new ParsingException("Problem with a dot");
      } else if (c == '(' || c == ')') {
        solverForBrackets(c, stackItems, calcItems);
      } else if (c == '+' || c == '-' || c == '*' || c == '/') {
        solverForOperator(c, stackItems, calcItems);
      } else {
        throw new ParsingException("New strange symbol");
      }
    }
    while (!stackItems.empty()) {
      StackItems tmp = stackItems.pop();
      if (tmp instanceof Operator) {
        calcItems.add((Operator) tmp);
      } else {
        throw new ParsingException("Too many '('");
      }
    }
    if (calcItems.size() == 0) {
      throw new ParsingException("No numbers");
    }
    return calcItems;
  }


  private double answer(Stack<CalculateItems> polishNotation) throws ParsingException {
    Stack<Number> answer = new Stack<>();
    for (CalculateItems curr : polishNotation) {

      if (curr instanceof Number) {
        answer.push((Number) curr);
      } else {
        Number second = answer.pop();
        Number first = answer.pop();
        answer.push(((Operator) curr).resultValue(first, second));
      }
    }
    return answer.pop().getValue();
  }

  /**
   * Takes string with valid arithmetic expression. Returns result of execution of it.
   *
   * May contain decimal numbers, operators +, -, *, / and priority operators (, ). Any spaces could
   * be in the expression.
   *
   * @param expression string with arithmetic expression
   * @return the result of execution
   * @throws ParsingException the expression is not recognized
   */

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null expression");
    }
    Stack<CalculateItems> polishNotation = polishNotation(expression);
    return answer(polishNotation);
  }
}