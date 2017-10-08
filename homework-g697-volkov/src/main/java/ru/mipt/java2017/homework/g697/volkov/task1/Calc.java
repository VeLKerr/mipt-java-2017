package ru.mipt.java2017.homework.g697.volkov.task1;

import ru.mipt.java2017.homework.base.task1.*;

import java.util.Stack;

public class Calc implements Calculator {

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null string!");
    }
    if (expression.isEmpty()) {
      throw new ParsingException("Empty string");
    }
    // Deleting unnecessary symbols
    expression = expression.replaceAll(" ", "");
    expression = expression.replaceAll("\t", "");
    expression = expression.replaceAll("\n", "");

    // Catching exceptions caused by incorrect expression
    try {
      String notation = getRPN(expression); // Forming Reverse Polish notation
      double result = countRPN(notation); // Counting it
      return result;
    } catch (Exception e) {
      throw new ParsingException(e.getMessage());
    }
  }

  // Uses in getRPN() to parse +,-,*,/
  private String parseOperations(Stack<Character> stack, char x, String notation)
      throws ParsingException {
    char symb = '!';
    while (!(symb == '#' || x == ' ')) {
      symb = stack.pop();

      switch (symb) {
        case '#': // # is terminal symbol: #some_expression#
          if (x != '#') {
            stack.push(symb);
            stack.push(x);
            x = ' ';
          }
          break;
        case '+':
        case '-':
          // All operations are processed according to algorithm
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
          // All operations are processed according to algorithm
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
    boolean lastSymbIsNumber = false;
    boolean isNegative = false;
    int n = 0;
    int i = 0;
    char prevSymb = '!';
    String number = "";
    char symb;
    while (i < exp.length()) {
      symb = exp.charAt(i);
      if (Character.isDigit(symb) || symb == '.') {
        number = "";
        while (Character.isDigit(symb) || symb == '.') {
          number += symb;
          lastSymbIsNumber = true;
          ++i;
          symb = exp.charAt(i);
        }
      } else {
        if (lastSymbIsNumber) {
          notation = notation.concat(number + ' ');
          if (isNegative) {
            notation = notation.concat("@ ");
          }
          n = 0;
          lastSymbIsNumber = false;
          isNegative = false;
        }

        if (symb == '-' && (prevSymb == '(' || prevSymb == '!' || prevSymb == '+'
            || prevSymb == '/' || prevSymb == '*')) {
          isNegative = !isNegative;
        } else {
          notation = parseOperations(stack, symb, notation);
        }
      }

      if (!lastSymbIsNumber) {
        prevSymb = symb;
        ++i;
      } else {
        prevSymb = exp.charAt(i - 1);
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

      double x1 = .0;
      double x2 = .0;
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