package ru.mipt.java2017.homework.g696.shavrina.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.*;


public class MyCalculator implements Calculator {

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Expression is incorrect: empty");
    }
    expression = expression.replaceAll("\\s+", "");

    return operation(toPostfix(expression));
  }

  private static String operations = "+-*/";
  private static String numbers = ".0123456789";

  private static int getPriority(Character s) {
    switch (s) {
      case '(':
        return 1;

      case '*':
      case '/':
        return 3;

      case '+':
      case '-':
        return 2;

      case '~':
        return 4;

      default:
        return 0;

    }
  }

  private String toPostfix(String expression) throws ParsingException {
    if (expression.length() == 0) {
      throw new ParsingException("Expression is incorrect: empty");
    }

    Character previous = '^';
    Character current;
    int counter1 = 0;
    int counter2 = 0;

    Stack<Character> stack = new Stack<Character>();
    StringBuilder postfix = new StringBuilder();

    for (int i = 0; i < expression.length(); ++i) {
      current = expression.charAt(i);

      if (numbers.indexOf(current) != -1) {
        postfix.append(current);
        if (numbers.indexOf(previous) != -1) {
          previous = current;
        } else {
          counter1++;
          if (counter1 - counter2 > 1) {
            throw new ParsingException("Expression is incorrect");
          }
          previous = current;
        }
      } else if (operations.indexOf(current) != -1) {
        counter2++;
        if (current == '-' && operations.indexOf(previous) != -1) {
          postfix.append(' ');
          postfix.append(' ');
          stack.push('~');
          continue;
        }

        while (!stack.empty() && (getPriority(current) <= getPriority(stack.lastElement()))) {
          postfix.append(' ').append(stack.pop()).append(' ');
        }

        if (previous == '(') {
          if (current != '-') {
            throw new ParsingException("Expression is incorrect: wrong symbol after ( is taken ");
          }
          postfix.append('0').append(' ');
        }

        previous = current;
        postfix.append(' ');
        stack.push(current);
      } else if (current == '(') {
        //counter3++;
        previous = current;
        postfix.append(' ');
        stack.push(current);
      } else if (current == ')') {
        //counter3--;
        while (!stack.empty() && (stack.lastElement() != '(')) {
          postfix.append(' ').append(stack.pop()).append(' ');
        }

        boolean balanceChecker = false;

        if (!stack.empty() && stack.lastElement() == '(') {
          balanceChecker = true;
        }

        if (stack.empty() && !balanceChecker) {
          throw new ParsingException("Expression is incorrect: brackets are wrong");
        }

        previous = current;
        stack.pop();
      } else {
        throw new ParsingException("Expression is incorrect: inapropriate symbol is used");
      }
    }
    while (!stack.empty()) {
      postfix.append(' ').append(stack.lastElement()).append(' ');
      stack.pop();
    }
    if (postfix.toString().contains(")") || postfix.toString().contains("(") ||
        postfix.length() == 0) {
      throw new ParsingException("Expression is incorrect: brackets are wrong");
    }
    //System.out.println(postfix);
    return postfix.toString();
  }

  private double operation(String expression) throws ParsingException {
    Stack<Double> stack = new Stack<Double>();
    StringBuilder curNumber = new StringBuilder();

    for (int i = 0; i < expression.length(); ++i) {
      Character current = expression.charAt(i);

      if (numbers.indexOf(current) != -1) {
        curNumber.append(current);
        continue;
      }

      if (operations.indexOf(current) != -1) {
        if (stack.empty()) {
          throw new ParsingException("Expression is incorrect");
        } else if (stack.size() == 1) {
          stack.push(-stack.pop());
        } else {
          Double number1 = stack.pop();
          Double number2 = stack.pop();
          stack.push(oneOperation(current, number1, number2));
        }
      } else if (i > 0 && current == ' ' && numbers.indexOf(expression.charAt(i - 1)) != -1) {
        try {
          stack.push(Double.parseDouble(curNumber.toString()));
        } catch (NumberFormatException e) {
          throw new ParsingException("Expression is incorrect: wrong number");
        }
        curNumber.delete(0, curNumber.length()); // delete
      } else if (current == '~') {
        stack.push(-stack.pop());
      }
    }

    if (stack.empty()) {
      throw new ParsingException("Expression is incorrect");
    }

    //System.out.println(stack.lastElement());
    return stack.lastElement();
  }

  private double oneOperation(Character x, double b, double a) throws ParsingException {
    switch (x) {
      case '+':
        return a + b;

      case '-':
        return a - b;

      case '*':
        return a * b;

      case '/':
        return a / b;

      default:
        throw new ParsingException("Expression is incorrect");
    }
  }
}