package ru.mipt.java2017.homework.g696.mecheryakov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class MyCalculator implements Calculator {

  /**
   * Calculates an expression.
   *
   * @param expression the expression to calculate
   * @return the value of the expression
   * @throws ParsingException if the expression is invalid
   */

  public double calculate(String expression) throws ParsingException {
    ArrayList<Object> reversePolishNotation = getReversePolishNotation(checkAndAddZeroesForUnary(expression));
    Stack toCalculate = new Stack();
    for (Object s : reversePolishNotation) {
      char symbol = s.toString().charAt(0);
      if (isOperator(symbol)) {
        Double x;
        Double y;
        try {
          x = (Double) toCalculate.pop();
          y = (Double) toCalculate.pop();
        } catch (EmptyStackException e) {
          throw new ParsingException("not supported unary operator");
        }
        if (symbol == '+') {
          toCalculate.push(y + x);
        } else if (symbol == '-') {
          if (x == 0.0 && y == 0.0) {
            toCalculate.push(-0.0);
          } else {
            toCalculate.push(y - x);
          }
        } else if (symbol == '*') {
          toCalculate.push(x * y);
        } else {
          toCalculate.push(y / x);
        }
      } else {
        toCalculate.push(s);
      }
    }
    return (Double) toCalculate.pop();
  }

  /**
   * Transforms an expression to Reverse Polish Notation and Checks for some invalid syntax
   *
   *
   * @param exp ArrayList of operators, braces and numbers
   * @return Reverse Polish Notation
   * @throws ParsingException if the expression is invalid
   */

  private ArrayList<Object> getReversePolishNotation(ArrayList<Object> exp) throws ParsingException {
    ArrayList<Object> reversePolishNotation = new ArrayList<Object>();
    Stack operatorsAndBrackets = new Stack();
    operatorsAndBrackets.push(exp.get(0));
    int index = 1;
    while (index < exp.size()) {
      if (exp.get(index) instanceof Integer || exp.get(index) instanceof Double) {
        reversePolishNotation.add((Double) exp.get(index));
        ++index;
      } else if ((char) operatorsAndBrackets.peek() == 'T') {
        if ((char) exp.get(index) == ')') {
          throw new ParsingException("not a bracket sequence");
        } else if ((char) exp.get(index) == 'T') {
          break;
        } else {
          operatorsAndBrackets.push(exp.get(index));
          ++index;
        }
      } else if ((char) operatorsAndBrackets.peek() == '+' || (char) operatorsAndBrackets.peek() == '-') {
        if ((char) exp.get(index) == '*' || (char) exp.get(index) == '/' || (char) exp.get(index) == '(') {
          operatorsAndBrackets.push(exp.get(index));
          ++index;
        } else {
          reversePolishNotation.add(operatorsAndBrackets.pop());
        }
      } else if ((char) operatorsAndBrackets.peek() == '*' || (char) operatorsAndBrackets.peek() == '/') {
        if ((char) exp.get(index) == '(') {
          operatorsAndBrackets.push(exp.get(index));
          ++index;
        } else {
          reversePolishNotation.add(operatorsAndBrackets.pop());
        }
      } else if ((char) operatorsAndBrackets.peek() == '(') {
        if ((char) exp.get(index) == 'T') {
          throw new ParsingException("not a brackets sequence");
        } else if ((char) exp.get(index) == ')') {
          operatorsAndBrackets.pop();
          ++index;
        } else {
          operatorsAndBrackets.push(exp.get(index));
          ++index;
        }
      }
    }
    return reversePolishNotation;
  }

  /**
   * Checks for some invalid syntax and transforms supported unary operators to binary ones.
   *
   * @param expression the expression to Check and Transform
   * @return changed expression
   * @throws ParsingException if the expression is invalid
   */

  private ArrayList<Object> checkAndAddZeroesForUnary(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("null");
    }
    ArrayList<Object> exp = new ArrayList<Object>();
    exp.add('T'); // beginning of expression
    char previous = '\0'; // '0' => number, '_' => unary minus, the rest => exact match { / => /, ( => (, ... }
    int index = 0;
    boolean onlyBraces = true;
    while (index < expression.length()) {
      onlyBraces = onlyBraces && (expression.charAt(index) == '(' || expression.charAt(index) == ')');
      //if number get whole number
      int endIndex = index + 1;
      if (isDigit(expression.charAt(index))) {
        if (previous == '0' || previous == ')') {
          throw new ParsingException("number or ) before number");
        }
        while (endIndex < expression.length()
          && (isDigit(expression.charAt(endIndex)) || expression.charAt(endIndex) == '.')) {
          ++endIndex;
        }
        try {
          Double number = (Double) Double.parseDouble(expression.substring(index, endIndex));
          if (previous == '_') {
            exp.add('(');
            exp.add(0.0);
            exp.add('-');
            exp.add(number);
            exp.add(')');
            previous = ')';
          } else {
            exp.add(number);
            previous = '0';
          }
        } catch (NumberFormatException e) {
          throw new ParsingException("number format is invalid");
        }
      } else if (expression.charAt(index) == '(') {
        exp.add('(');
        previous = '(';
      } else if (expression.charAt(index) == ')') {
        if (isOperator(previous) || previous == '(') {
          throw new ParsingException("operator or ( before )");
        }

        exp.add(')');
        previous = ')';
      } else if (expression.charAt(index) == '+' || expression.charAt(index) == '-') {
        if (previous == ')' || previous == '0') { // binary
          exp.add(expression.charAt(index));
          previous = expression.charAt(index);
        } else if (previous == '\0') { // beginning of expression
          // + 5 ... => 0 + 5 ... or same with -
          exp.add(0.0);
          exp.add(expression.charAt(index));
          previous = expression.charAt(index);
        } else if (previous == expression.charAt(index)) { // ++ or --
          throw new ParsingException("unsupported unary");
        } else { // { ( or + or * or / before - } or { ( or - or * or / before + }
          //  / - ... => / (0 - ...) ; same with other operators
          exp.add('(');
          exp.add(0.0);
          exp.add(expression.charAt(index));
          int toInsertClosingBracket = index + 1;
          for (int bracketsBalance = 0; toInsertClosingBracket < expression.length(); ++toInsertClosingBracket) {
            if (expression.charAt(toInsertClosingBracket) == '(') {
              ++bracketsBalance;
            } else if (expression.charAt(toInsertClosingBracket) == ')') {
              --bracketsBalance;
            } else if (isSpaceSymbol(expression.charAt(toInsertClosingBracket))
                        || isDigit(expression.charAt(toInsertClosingBracket))
                        || expression.charAt(toInsertClosingBracket) == '.') {
              continue;
            }
            if (bracketsBalance <= 0) {
              break;
            }
          }
          if (toInsertClosingBracket == index + 1) {
            ++toInsertClosingBracket;
          }
          if (toInsertClosingBracket >= expression.length()) {
            expression = expression + ')';
          } else {
            expression = expression.substring(0, toInsertClosingBracket) + ')'
              + expression.substring(toInsertClosingBracket, expression.length());
          }
          previous = expression.charAt(index);
        }
      } else if (isOperator(expression.charAt(index))) { // /, *
        if (isOperator(previous) || previous == '(') {
          throw new ParsingException("operator or ( before / or *");
        }

        exp.add(expression.charAt(index));
        previous = expression.charAt(index);
      } else if (!isSpaceSymbol(expression.charAt(index))) {
        throw new ParsingException("invalid symbol");
      }
      index = endIndex;
    }
    exp.add('T'); // end of expression
    if (onlyBraces || exp.size() == 2) {
      throw new ParsingException("empty or only space symbols and braces");
    }
    return exp;
  }

  private boolean isDigit(char symbol) {
    return symbol <= '9' && symbol >= '0';
  }

  private boolean isOperator(char symbol) {
    return symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/' || symbol == '_';
  }

  private boolean isSpaceSymbol(char symbol) {
    return symbol == ' ' || symbol == '\t' || symbol == '\n';
  }
}
