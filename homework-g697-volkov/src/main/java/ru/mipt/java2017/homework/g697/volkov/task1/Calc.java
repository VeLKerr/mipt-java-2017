package ru.mipt.java2017.homework.g697.volkov.task1;

import ru.mipt.java2017.homework.base.task1.*;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calc implements Calculator {

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null string");
    }
    if (expression.isEmpty()) {
      throw new ParsingException("Empty string");
    }
    expression = expression.replaceAll(" ", "");
    expression = expression.replaceAll("\t", "");
    expression = expression.replaceAll("\n", "");

    try {
      String notation = getRPN(expression);
      double result = countRPN(notation);
      System.out.println(result);
      return result;
    } catch (Exception e) {
      throw new ParsingException(e.getMessage());
    }
  }

  // Uses in getRPN() to parse +,-,*,/
  private String parse_operations(Stack<Character> stack, char x, String notation)
    throws ParsingException {
    char symb = '!';
    while (!(symb == '#' || x == ' ')) {
      symb = stack.pop();

      switch (symb) {
        case '#':
          if (x != '#') {
            stack.push(symb);
            stack.push(x);
            x = ' ';
          }
          break;
        case '+':
        case '-':
          if (x == '*' || x == '/' || x == '(') {
            stack.push(symb);
            stack.push(x);
            x = ' ';
          } else {
            notation = notation + symb + ' ';
          }
          break;
        case '*':
        case '/':
          if (x == '(') {
            stack.push(symb);
            stack.push(x);
            x = ' ';
          } else {
            notation = notation + symb + ' ';
          }
          break;
        case '(':
          if (x != ')') {
            stack.push(symb);
            stack.push(x);
            x = ' ';
          } else {
            x = ' ';
          }
          break;
        default:
          throw new ParsingException("Invalid expression");
      }
    }
    return notation;
  }

  // Reverse Polish notation
  private String getRPN(String exp) throws ParsingException {
    Stack<Character> stack = new Stack<Character>();
    exp += '#';
    stack.push('#');
    String notation = "";
    boolean f = false;
    boolean flaggus = false;
    int n = 0;
    int i = 0;
    char prev_symb = '!';
    String number = "";
    char symb;
    while (i < exp.length()) {
      symb = exp.charAt(i);
      if (Character.isDigit(symb) || symb == '.') {
        //if (symb != '+' && symb != '-' && symb != '*' && symb != '/' && symb != '(' && symb != ')' && symb != '#') {
        number = "";
        while (Character.isDigit(symb) || symb == '.') {
          number += symb;
          f = true;
          ++i;
          symb = exp.charAt(i);
        }
      } else {
        if (f) {
          notation = notation.concat(number + ' ');
          if (flaggus) {
            notation = notation.concat("@ ");
          }
          n = 0;
          f = false;
          flaggus = false;
        }

        if (symb == '-' && (prev_symb == '(' || prev_symb == '!' || prev_symb == '+'
          || prev_symb == '/' || prev_symb == '*')) {
          flaggus = !flaggus;
        } else {
          notation = parse_operations(stack, symb, notation);
        }
      }

      if (!f) {
        prev_symb = symb;
        ++i;
      } else {
        prev_symb = exp.charAt(i - 1);
      }
    }

    if (stack.size() != 0) {
      throw new ParsingException("Invalid expression");
    }
    return notation;
  }

  private Double countRPN(String notation) throws ParsingException {
    Stack<Double> stack = new Stack<Double>();
    int i = 0;
    int len = notation.length();
    while (i < len) {
      String element = "";
      for (int j = i; notation.charAt(j) != ' ' && j < len; ++j) {
        element += notation.charAt(j);
      }

      //System.out.println("<" + element + ">");

      double x1 = .0, x2 = .0;
      if (element.equals("+")) {
        x2 = stack.pop();
        x1 = stack.pop();
        stack.push(x1 + x2);
      } else if (element.equals("-")) {
        x2 = stack.pop();
        x1 = stack.pop();
        stack.push(x1 - x2);
      } else if (element.equals("*")) {
        x2 = stack.pop();
        x1 = stack.pop();
        stack.push(x1 * x2);
      } else if (element.equals("/")) {
        x2 = stack.pop();
        x1 = stack.pop();
        stack.push(x1 / x2);
      } else if (element.equals("@")) {
        x1 = -stack.pop();
        stack.push(x1);
      } else {
        stack.push(Double.parseDouble(element));
      }

      i += element.length() + 1;
    }
    return stack.pop();
  }
}