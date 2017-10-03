package ru.mipt.java2017.homework.g694.nachinkin.task1;


import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Ilya A. Nachinkin
 * @since 03.10.17
 */
public class NachinkinCalculator implements Calculator {

  private static final  String DIGITS = "0123456789";
  private static ArrayList<String> arrayLexeme = new ArrayList<String>();


  /**
   * turn substring of exception without spaces into ArrayList;
   * each operand, operator is separate object in the array;
   * @param str is due string
   */
  private static void formatic(String str)  throws ParsingException {
    String digits = "0123456789.";
    boolean digitsIn = false;
    int leftBoard = 0;
    int countDot = 0;

    for (int i = 0; i < str.length(); i++) {

      if (digits.indexOf(str.charAt(i)) == -1) {
        countDot = 0;
        if (digitsIn) {
          digitsIn = false;

          arrayLexeme.add(str.substring(leftBoard, i));
        }
        arrayLexeme.add(str.substring(i, i + 1));
      } else {
        if (str.charAt(i) == '.') {
          if (countDot > 0) {
            arrayLexeme.clear();
            throw new ParsingException("wrong number");
          } else {
            countDot++;
          }

        }
        if (!digitsIn) {
          digitsIn = true;

          leftBoard = i;
        }
      }

    }
    if (digitsIn) {
      arrayLexeme.add(str.substring(leftBoard, str.length()));
    }

  }


  /**
   * turn string into ArrayList  without spaces;
   * each operand, operator is separate object in the array;
   * @param expression
   * @throws ParsingException
   */
  private static void stringToArray(String expression) throws ParsingException {
    boolean spaceIn = false;
    int leftBoard = 0;
    String acceptableSymbs = "+-*/.()0123456789\t\n "; //all acceptable symbols in the expression


    for (int i = 0; i < expression.length(); i++) {
      if (acceptableSymbs.indexOf(expression.charAt(i)) == -1) {
        throw new ParsingException("Wrong symbol in exception");
      }

    }

    expression = expression.replaceAll("\\s", "");
    formatic(expression);
  }

  /**
   * @param  expression - arithmetic
   * @return  result of calculations
   * @throws ParsingException
   */
  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null expression");
    }
    stringToArray(expression);
    turnToRPN();

    Stack<Double> stack = new Stack<Double>();
    String operands = "+-*/#~";

    for (String i : arrayLexeme) {

      if (operands.indexOf(i.charAt(0)) == -1) { //if it's number
        stack.push(new Double(i));
      } else {
        if (i.equals("#") || i.equals("~")) {
          stack.push(calcUnary(stack.pop(), i));
        } else {
          Double a = stack.pop();
          Double b = stack.pop();
          stack.push(calcBinary(b, a, i));
        }

      }
    }
    arrayLexeme.clear();
    return stack.pop();
  }

  /**
   * calc a simple binary expression
   *@param a is the first number
   *@param b is the second number
   *@param operand is due operand
   *@return result of calculations
   *@throws ParsingException
   */
  private static Double calcBinary(Double a, Double b, String operand) throws ParsingException {

    if (operand.equals("+")) {
      return  a + b;
    }

    if (operand.equals("-")) {
      return a - b;
    }

    if (operand.equals("*")) {
      return a * b;
    }

    if (operand.equals("/")) {
      return a / b;
    }
    arrayLexeme.clear();
    throw new ParsingException("Wrong binary operand");
  }

  /**
   * calc a simple unary expression
   *
   *@param a is the first number
   *@param operand is due operand
   *@return result of calculations
   *@throws ParsingException
   */
  private static Double calcUnary(Double a, String operand)  throws ParsingException {
    if (operand.equals("#")) {
      return a;
    }

    if (operand.equals("~")) {
      return (-1) * a;
    }

    arrayLexeme.clear();
    throw new ParsingException("Wrong unary operand");
  }

  /**
   * turn the parsed expression into Reverse Polish notation by changing the arrayLexeme
   *
   * @throws ParsingException
   */
  private static void turnToRPN() throws ParsingException {
    String symbsBeforeUnar = "*/( "; //set of symbols which can appear before unary sign
    String prevLexeme = " "; //safes previous lexeme to determine operands for unary

    Stack<String> stack = new Stack<String>();

    ArrayList<String> helperArray = new ArrayList<String>();

    helperArray.addAll(0, arrayLexeme);
    arrayLexeme.clear();

    for (int i = 0; i < helperArray.size(); i++) {
      String var = helperArray.get(i);

      if (DIGITS.indexOf(var.charAt(0)) >= 0) { //checks if element of helperArray is number
        arrayLexeme.add(var);

      } else {
        if (var.equals("(")) {
          stack.push("(");
        }

        if (var.equals(")"))  {
          String ch = "";
          while (!stack.empty()) {
            ch = stack.pop();
            if (!ch.equals("(")) {
              arrayLexeme.add(ch);
            } else {
              break;
            }

          }

          if (stack.empty() && !ch.equals("(")) { //if bracket balance is wrong
            arrayLexeme.clear();
            throw new ParsingException("Wrong bracket balance in the expression");
          }
        }

                /*
                 *здесь надо проверить на унарность операции
                 */
        if (var.equals("+") || var.equals("-")) {

          if (prevLexeme.charAt(0) == '+' || prevLexeme.charAt(0) == '-') {
            throw new ParsingException("incorrect expression");
          }

          if (symbsBeforeUnar.indexOf(prevLexeme.charAt(0)) >= 0) { //if it is unary sign

            String ch = "";

            while (!stack.empty()) {
              ch = stack.pop();
              if (ch.equals("#") || ch.equals("~")) {
                arrayLexeme.add(ch);
              } else {
                break;
              }

            }
            if (!ch.equals("#") && !ch.equals("~") && !ch.equals("")) {
              stack.push(ch);
            }

            if (var.equals("+")) {
              stack.push("#"); //sign of unary plus
            } else {
              stack.push("~"); //sign of unary minus
            }

          } else {
            String ch = "";
            while (!stack.empty()) {
              ch = stack.pop();
              if (!ch.equals("(")) {
                arrayLexeme.add(ch);
              } else {
                break;
              }

            }

            if (ch.equals("(")) { //return "(" in stack if we need it
              stack.push(ch);
            }

            stack.push(var);
          }
        }

        if (var.equals("*") || var.equals("/")) {
          String ch = "";

          while (!stack.empty()) {
            ch = stack.pop();
            if (!ch.equals("+") && !ch.equals("-") && !ch.equals("(")) {
              arrayLexeme.add(ch);
            } else {
              break;
            }

          }
          /*
           *return suitable symbol if we need it
           */
          if (ch.equals("+") || ch.equals("-") || ch.equals(")")) {
            stack.push(ch);
          }
          stack.push(var);
        }
      }

      prevLexeme = var;
    }

    while (!stack.empty()) {
      String s = stack.pop();
      arrayLexeme.add(s);

      if (s.equals("(")) {
        arrayLexeme.clear();
        throw new ParsingException("Wrong bracket balance in the expression");
      }
    }
    if (arrayLexeme.isEmpty()) {
      throw new ParsingException("Empty expression");
    }
  }

}
