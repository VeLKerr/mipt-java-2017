package ru.mipt.java2017.homework.g696.antonova.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.*;

public class MyCalculator implements Calculator {

  static boolean is_operator(char c) {
    if (c == '+' || c == '-' || c == '/' || c == '*') {
      return true;
    } else {
      return false;
    }
  }

  static boolean is_num(char c) {
    return (Character.isDigit(c) || c == '.');
  }

  static int get_prior(char c) {
    if (c == '+' || c == '-') {
      return 0;
    } else if (c == '/' || c == '*') {
      return 1;
    } else {
      return -1;
    }
  }

  static String del_spaces(String str) {
    return ((str.replace(" ", "")).replace("\t", "")).replace("\n", "");
  }

  static void execute(LinkedList<Double> numbers, MyOperator operator) throws ParsingException {
    double first = numbers.removeLast();
    if (operator.isunary) {
      if (operator.op == '-') {
        first *= -1;
      }
      numbers.add(first);
    } else {
      double second = numbers.removeLast();
      switch (operator.op) {
        case '+':
          numbers.add(second + first);
          break;
        case '-':
          numbers.add(second - first);
          break;
        case '*':
          numbers.add(second * first);
          break;
        case '/':
          numbers.add(second / first);
          break;
        default:
          throw new ParsingException("Invalid expression");

      }
    }
  }

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("null expression");
    } else {
      expression = del_spaces(expression);
      if (expression.equals("")) {
        throw new ParsingException("null expression");
      }
      try {
        LinkedList<Double> numbers = new LinkedList<Double>();
        LinkedList<MyOperator> operators = new LinkedList<MyOperator>();
        for (int i = 0; i < expression.length(); ++i) {
          char curr = expression.charAt(i);
          if (is_operator(curr)) {
            MyOperator newop = new MyOperator();
            newop.op = curr;
            if (curr == '-' || curr == '+') {
              if (i == 0 || expression.charAt(i - 1) == '(') {
                newop.isunary = true;
              } else if (is_operator(expression.charAt(i - 1))) {
                throw new ParsingException("Extra operator");
              }
            } else if ((curr == '/' || curr == '*') && expression.charAt(i + 1) == '-') {
              MyOperator newop2 = new MyOperator();
              newop2.op = '-';
              newop2.isunary = true;
              operators.add(newop2);
              i += 1;
            } else if (i == 0 || expression.charAt(i - 1) == '(' || is_operator(
              expression.charAt(i + 1))) {
              throw new ParsingException("Extra operator");
            }
            while (!operators.isEmpty() && get_prior((operators.getLast()).op) >= get_prior(curr)) {
              execute(numbers, operators.removeLast());
            }
            operators.add(newop);
          } else if (curr == '(') {
            if ((i + 1) == expression.length() || expression.charAt(i + 1) == ')') {
              throw new ParsingException("Extra braces");
            }
            MyOperator newop1 = new MyOperator();
            newop1.op = '(';
            operators.add(newop1);
          } else if (curr == ')') {
            while (!(operators.isEmpty()) && (operators.getLast()).op != '(') {
              execute(numbers, operators.removeLast());
            }
            if (operators.isEmpty()) {
              throw new ParsingException("Extra ) symbol");
            } else {
              operators.removeLast();
            }
          } else if (is_num(curr)) {
            String newnum = "";
            boolean dot = false;
            while (i < expression.length() && is_num(expression.charAt(i))) {
              if (expression.charAt(i) == '.') {
                if (newnum.equals("") || dot) {
                  throw new ParsingException("Extra dot");
                } else {
                  dot = true;
                }
              }
              newnum += expression.charAt(i++);
            }
            --i;
            numbers.add(Double.parseDouble(newnum));
          } else {
            throw new ParsingException("Wrong expression");
          }
        }
        while (!operators.isEmpty()) {
          execute(numbers, operators.removeLast());
        }
        return numbers.get(0);
      } catch (ParsingException e) {
        throw new ParsingException("Invalid expression", e.getCause());
      }
    }
  }
}


