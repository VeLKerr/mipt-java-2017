package ru.mipt.java2017.homework.g696.Serov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

/**
 * @author Yaroslav Serov
 * @since 5.10.2017
 */


public class MyCalculator implements Calculator {

  private static boolean isCorrectCharacter(char chr) throws ParsingException {
    if (chr == ' ') {
      return true;
    }
    if (chr == '.') {
      return true;
    }
    if (chr == '\n') {
      return true;
    }
    if (chr == '\t') {
      return true;
    }
    if (chr == '0') {
      return true;
    }
    if (chr == '1') {
      return true;
    }
    if (chr == '2') {
      return true;
    }
    if (chr == '3') {
      return true;
    }
    if (chr == '4') {
      return true;
    }
    if (chr == '5') {
      return true;
    }
    if (chr == '6') {
      return true;
    }
    if (chr == '7') {
      return true;
    }
    if (chr == '8') {
      return true;
    }
    if (chr == '9') {
      return true;
    }
    if (chr == '(') {
      return true;
    }
    if (chr == ')') {
      return true;
    }
    if (chr == '+') {
      return true;
    }
    if (chr == '-') {
      return true;
    }
    if (chr == '*') {
      return true;
    }
    if (chr == '/') {
      return true;
    }
    return false;
  }

  private static boolean isNumeric(char chr) throws ParsingException {
    return ((chr >= '0' && chr <= '9') || chr == '.');
  }

  private static boolean isOperator(char chr) throws ParsingException {
    return (chr == '+' || chr == '-' || chr == '*' || chr == '/' || chr == '$');
  }

  private static int getPriority(char chr) throws ParsingException {
    if (chr == '+') {
      return 1;
    }
    if (chr == '-') {
      return 1;
    }
    if (chr == '*') {
      return 2;
    }
    if (chr == '/') {
      return 2;
    }
    if (chr == '$') {
      return 3;
    }
    return 0;
  }

  private static String lol(String expression) throws ParsingException {
    Stack<Character> stack = new Stack<>();
    StringBuilder out = new StringBuilder("");
    boolean isOpeningBraceOpenedBefore = false;
    boolean wasnumber = false;
    boolean unary = true;
    int badnumber = 0;
    for (char chr : expression.toCharArray()) {
      if (!isCorrectCharacter(chr)) {
        throw new ParsingException("Invalid expression.");
      }
      if (badnumber == 1 && chr == ' ') {
        badnumber++;
      }
      if (chr == ' ' || chr == '\n' || chr == '\t') {
        continue;
      }
      if (isNumeric(chr)) {
        unary = false;
        if (!wasnumber) {
          out.append(" ").append(chr);
          wasnumber = true;
          badnumber = 0;
        } else {
          out.append(chr);
        }
        if (chr == '.') {
          badnumber++;
        }
        if (badnumber >= 2) {
          throw new ParsingException("Invalid expression.");
        }
        continue;
      }
      wasnumber = false;

      if (chr == '(') {
        unary = true;
        stack.push(chr);
        continue;
      }
      if (chr == ')') {
        unary = false;
        while (!stack.empty()) {
          char chrnow = stack.pop();
          if (chrnow == '(') {
            isOpeningBraceOpenedBefore = true;
            break;
          } else {
            out.append(" ").append(chrnow);
          }
        }
        if (!isOpeningBraceOpenedBefore) {
          throw new ParsingException("Invalid expression.");
        }
        isOpeningBraceOpenedBefore = false;
      }
      if (isOperator(chr)) {
        if (unary) {
          unary = false;
          if (chr == '-') {
            stack.push('$');
          }
          if (chr == '*' || chr == '/') {
            throw new ParsingException("Invalid expression.");
          }

        } else {
          unary = true;
          while (!stack.empty() && isOperator(stack.peek()) &&
            getPriority(chr) <= getPriority(stack.peek())) {
            char chrnow = stack.pop();
            out.append(" ").append(chrnow);
          }
          stack.push(chr);
        }
      }
    }
    while (!stack.empty()) {
      char chrnow = stack.pop();
      if (!isOperator(chrnow)) {
        throw new ParsingException("Invalid expression.");
      }
      out.append(" ").append(chrnow);
    }
    return out.toString();
  }

  private static double calculating(double a, double b, char operator) {
    double c = 0.;
    if (operator == '+') {
      c = b + a;
    }
    if (operator == '-') {
      c = b - a;
    }
    if (operator == '*') {
      c = b * a;
    }
    if (operator == '/') {
      c = b / a;
    }
    return c;
  }

  private static double calculating(String expression) throws ParsingException {
    Stack<Double> stack = new Stack<>();
    String[] parts = expression.split(" ");
    if (parts.length == 1) {
      throw new ParsingException("Invalid expression.");
    }
    for (int i = 1; i < parts.length; i++) {
      String str = parts[i];
      if (!isOperator(str.charAt(0))) {
        double num = Double.parseDouble(str);
        stack.push(num);
      } else {
        if (str.length() == 1 && stack.size() >= 2 && str.charAt(0) != '$') {
          double a = stack.pop();
          double b = stack.pop();
          double c = calculating(a, b, str.charAt(0));
          stack.push(c);
        } else {
          if (str.length() == 1 && stack.size() >= 1 && str.charAt(0) == '$') {
            double c = stack.pop();
            stack.push(-c);
          } else {
            throw new ParsingException("Invalid expression.");
          }
        }
      }
    }

    double res = stack.pop();
    if (!stack.empty()) {
      throw new ParsingException("Invalid expression.");
    }
    return res;
  }

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null || expression.equals("")) {
      throw new ParsingException("Expression is empty.");
    }
    return calculating(lol(expression));
  }
}