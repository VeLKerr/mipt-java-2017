package ru.mipt.java2017.homework.g696.mecheryakov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class MyCalculator implements Calculator {

  public double calculate(String expression) throws ParsingException {
    //    try {
//      exp[4];
//      exp = checkAndAddZeroesForUnary(expression);
//      for (Object s : exp) {
//        System.out.print(s);
//      }
//    } catch (ParsingException e) {
//      System.out.println(e.toString());
//    }
    ArrayList<Object> reversePolishNotation = getReversePolishNotation(checkAndAddZeroesForUnary(expression));
    Stack toCalculate = new Stack();
    for (Object s : reversePolishNotation) {
      char symbol = s.toString().charAt(0);
      if (isOperator(symbol)) {
        double x;
        double y;
        Object x1;
        Object y1;
        try {
          x1 = toCalculate.pop();
          y1 = toCalculate.pop();
        } catch (EmptyStackException e) {
          throw new ParsingException("not supported unary operator");
        }
        if (x1 instanceof Integer) {
          x = (double) ((Integer) x1).intValue();
        } else {
          x = (double) x1;
        }
        if (y1 instanceof Integer) {
          y = (double) ((Integer) y1).intValue();
        } else {
          y = (double) y1;
        }
//        System.out.print("first: ");
//        System.out.print(y);
//        System.out.print(" ");
//        System.out.println(Double.doubleToLongBits(y));
//        System.out.print("second: ");
//        System.out.print(y);
//        System.out.print(" ");
//        System.out.println(Double.doubleToLongBits(y));
//        System.out.println();
        if (symbol == '+') {
          toCalculate.push(y + x);
        } else if (symbol == '-') {
          if (x == 0.0 && y == 0.0) {
            toCalculate.push(-0.0);
          } else {
            toCalculate.push(y - x);
          }
//          if (Double.doubleToLongBits(x) == 0x8000000000000000L) {
//            System.out.println("Negative zero");
//          }
//          if (Double.doubleToLongBits(y) == 0x8000000000000000L) {
//            System.out.println("another Negative zero");
//          }
        } else if (symbol == '*') {
          toCalculate.push(x * y);
        } else {
          toCalculate.push(y / x);
        }
      } else {
        toCalculate.push(s);
      }
    }
    //Double toBeTruncated = (Double)toCalculate.pop();

    //Double truncatedDouble = BigDecimal.valueOf(toBeTruncated).setScale(2, RoundingMode.HALF_UP).doubleValue();
    return (Double) toCalculate.pop();
  }


  private ArrayList<Object> getReversePolishNotation(ArrayList<Object> exp) throws ParsingException {
    ArrayList<Object> reversePolishNotation = new ArrayList<Object>();
    Stack operatorsAndBrackets = new Stack();
    operatorsAndBrackets.push(exp.get(0));
    int index = 1;
    while (index < exp.size()) {
      if (exp.get(index) instanceof Integer || exp.get(index) instanceof Double) {
        reversePolishNotation.add(exp.get(index));
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

  private ArrayList<Object> checkAndAddZeroesForUnary(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("null");
    }
    ArrayList<Object> exp = new ArrayList<Object>();
    exp.add('T'); // beginning of expression
    char previous = '\0'; // '0' => number, '_' => unary minus, the rest => exact match { / => /, ( => (, ... }
    int index = 0;
    while (index < expression.length()) {
      if (index == 14) {
        int l = 0;
      }
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
          double number = Double.parseDouble(expression.substring(index, endIndex));
          if (previous == '_') {
            exp.add('(');
            exp.add(0);
            exp.add('-');
            if (number % 1 == 0.0) {
              exp.add((int) number);
            } else {
              exp.add(number);
            }
            exp.add(')');
            previous = ')';
          } else {
            if (number % 1 == 0.0) {
              exp.add((int) number);
            } else {
              exp.add(number);
            }
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
          exp.add(0);
          exp.add(expression.charAt(index));
          previous = expression.charAt(index);
        } else if (previous == expression.charAt(index)) { // ++ or --
          throw new ParsingException("unsupported unary");
        } else { // { ( or + or * or / before - } or { ( or - or * or / before + }
          //  / - ... => / (0 - ...) ; same with other operators
          exp.add('(');
          exp.add(0);
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
    if (exp.size() == 2) {
      throw new ParsingException("empty or only space symbols");
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
