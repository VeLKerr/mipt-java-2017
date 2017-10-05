package ru.mipt.java2017.homework.g696.bogomolov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author Mikhail Bogomolov
 * @since 04.10.2017
 */
public class SimpleCalculator implements Calculator {

  private ArrayList<String> tokenize(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Input string is null");
    }
    ArrayList<String> result = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    for (char ch : expression.toCharArray()) {
      if (Character.isDigit(ch) || ch == '.') {
        stringBuilder.append(ch);
      } else {
        if (stringBuilder.length() != 0) {
          result.add(stringBuilder.toString());
          stringBuilder = new StringBuilder();
        }
        if ("+-*/()".contains(ch + "")) {
          result.add(ch + "");
        } else if (!Character.isWhitespace(ch)) {
          throw new ParsingException("Input string contains invalid character '" + ch + "'");
        }
      }
    }
    if (stringBuilder.length() > 0) {
      result.add(stringBuilder.toString());
    }
    return result;
  }

  private ArrayList<Object> toReversePolishNotation(String expression) throws ParsingException {
    ArrayList<Object> result = new ArrayList<>();
    Stack<String> stack = new Stack<>();
    ArrayList<String> tokens = tokenize(expression);
    boolean canBeUnaryMinus = true;
    for (String token : tokens) {
      if (token.length() >= 2 || Character.isDigit(token.charAt(0))) {
        try {
          result.add(Double.parseDouble(token));
        } catch (NumberFormatException e) {
          throw new ParsingException("Wrong number: " + token + " contains multiple decimal dots");
        }
        canBeUnaryMinus = false;
      } else if (token.equals("(")) {
        stack.push(token);
        canBeUnaryMinus = true;
      } else if (token.equals(")")) {
        while (true) {
          if (stack.isEmpty()) {
            throw new ParsingException("Wrong parenthesis sequence");
          }
          if (stack.peek().equals("(")) {
            stack.pop();
            break;
          } else {
            result.add(stack.pop());
          }
        }
        canBeUnaryMinus = false;
      } else if (token.equals("-") && canBeUnaryMinus) {
        stack.push("±");
      } else if (canBeUnaryMinus) {
        throw new ParsingException("Multiple operators together");
      } else {
        while (!stack.isEmpty() && "+-*/".contains(stack.peek()) &&
          ("+-".contains(token) || "*/".contains(stack.peek()))) {
          result.add(stack.pop());
        }
        stack.push(token);
        canBeUnaryMinus = true;
      }
    }
    while (!stack.isEmpty()) {
      if (stack.peek().equals("(")) {
        throw new ParsingException("Wrong parenthesis sequence");
      }
      result.add(stack.pop());
    }
    return result;
  }


  public double calculate(String expression) throws ParsingException {
    ArrayList<Object> tokens = toReversePolishNotation(expression);
    Stack<Double> stack = new Stack<>();
    for (Object token : tokens) {
      if (token instanceof Double) {
        stack.add((Double) token);
      } else if (token.equals("±")) {
        double a;
        try {
          a = stack.pop();
          stack.push(-a);
        } catch (EmptyStackException e) {
          throw new ParsingException("Extra minus symbol in the end of the string");
        }
      } else {
        double a, b;
        try {
          a = stack.pop();
          b = stack.pop();
        } catch (EmptyStackException e) {
          throw new ParsingException("Extra operator symbol");
        }
        String operator = (String) token;
        switch (operator) {
          case "*":
            stack.add(a * b);
            break;
          case "+":
            stack.add(a + b);
            break;
          case "/":
            stack.add(b / a);
            break;
          case "-":
            stack.add(b - a);
        }
      }
    }
    if (stack.size() != 1) {
      throw new ParsingException("Invalid operators and numbers sequence");
    }
    return stack.remove(stack.size() - 1);
  }
}
