package ru.mipt.java2017.homework.g697.RaskinIlya.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Calculator which can work with double numbers and for basic operations: +, - , *, /.
 * Expression for calculator can also include brackets and + and - as unary operators.
 * All space symbols are allowed.
 *
 * @author Ilya I. Raskin
 * @since 02.10.17
 */

public class ForActCalculator implements Calculator {
  public ForActCalculator()
  {
    revPolishEntry = new LinkedList<>();
  }

  public double calculate(String expression) throws ParsingException
  {
    if (expression == null) {
      throw new ParsingException("Error. Nothing to calculate.");
    }
    parsing(expression);
    return counting();
  }

  private LinkedList<String> revPolishEntry;

  private void parsing(String expression) throws ParsingException {
    Stack<Character> st = new Stack<>();
    boolean b = false, b1 = true, sign = true, bot = false;
    //b - if now we are reading a number, b1 - next symbol can be unary operator,
    //sign - the sign of next number, bot - if there has already been a bot in the number,
    String number = "";
    for (int i = 0; i < expression.length(); ++i) {
      char c = expression.charAt(i);
      if (c >= '0' && c <= '9') { //If we are reading a number.
        if (!sign) {              //Check if the number is negative.
          number = "-";
          sign = true;
        }
        b = true;   //Now we are reading a number.
        b1 = false;
        number += c;
      } else if (c == '.') { //If we are reading a dot.
          if (!bot) {
            if (number.equals("")) {
              throw new ParsingException("Error. There must be an integer part of number.");
            }
            number += '.';
            bot = true;
          } else {           //There can not be more than one dot in a number.
            throw new ParsingException("Error. Two bots in one number.");
          }
      } else {
        if (b) {      //If we have just finished to read a number.
          b = false;
          bot = false;
          revPolishEntry.add(number);
          number = "";
        }
        if (c == '(') {
          b1 = true;
          st.push(c);                   //Add the bracket to the stack.
        } else {
          if (c == '*' || c == '/' || c == '+' || c == '-') {
            if (b1 && c == '-') {                              //If '-' is an unary operator.
              sign = !sign;
              b1 = false;
            } else if (b1 && c == '+') {                  //If '+' is an unary operator.
              b1 = false;
            } else {                         //If we have a binary operator.
              if (c == '+' || c == '-') {
                b1 = false;
                while (!st.empty() && st.peek() != '(') {
                  revPolishEntry.add(st.peek().toString());
                  st.pop();
                }
              } else {
                b1 = true;
                while (!st.empty() && (st.peek() == '*' || st.peek() == '/')) {
                  revPolishEntry.add(st.peek().toString());
                  st.pop();
                }
              }
              st.push(c);         //Add the operation to the stack.
            }
          } else if (c == ')') {
            b1 = false;
            while (st.peek() != '(') {         //Pop operations from the stack while wouldn't find a bracket.
              revPolishEntry.add(st.peek().toString());
              st.pop();
              if (st.empty()) {
                throw new ParsingException("Error. The balance of brackets is broken.");
              }
            }
            st.pop();
          } else if (!Character.isWhitespace(c)){
            throw new ParsingException("Error. Unknown symbol.");
          }
        }
      }
    }
    if (b) {                         //Check if there were a number at the and of expression.
      revPolishEntry.add(number);
    }
    while (!st.empty()) {                                 //Empty the stack.
      revPolishEntry.add(st.peek().toString());
      st.pop();
    }
    if (revPolishEntry.size() == 0) {
      throw new ParsingException("Error. Nothing to calculate.");
    }
  }

  private double counting() throws ParsingException {
    Stack<Double> numbers = new Stack<>();
    while (!revPolishEntry.isEmpty()) {
      try {
        numbers.push(Double.parseDouble(revPolishEntry.get(0)));  //The next object is number.
      }
      catch(NumberFormatException exception) {                      //The next object is operation.
        if (numbers.empty()) {
          throw new ParsingException("Error. Incorrect expression.");
        }
        double a = numbers.peek();
        numbers.pop();
        if (numbers.empty()) {
          throw new ParsingException("Error. Incorrect expression.");
        }
        double b = numbers.peek();
        numbers.pop();
        if (revPolishEntry.get(0).equals("*")) {
          numbers.push(b * a);
        } else if (revPolishEntry.get(0).equals("/")) {
          numbers.push(b / a);
        } else if (revPolishEntry.get(0).equals("+")) {
          numbers.push(b + a);
        } else {
          numbers.push(b - a);
        }
      }
      revPolishEntry.pop();
      }
    return numbers.peek();
  }
}