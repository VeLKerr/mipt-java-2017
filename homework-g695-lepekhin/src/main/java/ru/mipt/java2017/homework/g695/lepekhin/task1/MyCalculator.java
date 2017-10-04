package ru.mipt.java2017.homework.g695.lepekhin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayDeque;

public class MyCalculator implements Calculator {

  class Char {

    private char value;

    public char getValue() {
      return value;
    }

    public void setValue(char what) {
      value = what;
    }

    Char(char ch) {
      value = ch;
    }
  }

  static boolean isSpaceChar(char ch) {
    return ch == ' ' || ch == '\n' || ch == '\t';
  }

  static boolean isOperator(char ch) {
    return ch == '+' || ch == '-' || ch == '/' || ch == '*';
  }

  static int prior(char oper) {
    if (oper == '(') {
      return 0;
    }
    if (oper == '+' || oper == '-') {
      return 1;
    }
    if (oper == '*' || oper == '/') {
      return 2;
    }
    return 3;
  }

  static void action(ArrayDeque<Char> opers, ArrayDeque<Double> nums) throws ParsingException {
    if (opers.size() < 1) {
      throw new ParsingException("Lack of the operators");
    }
    if (prior(opers.getFirst().getValue()) == 3) {
      if (nums.size() < 1) {
        throw new ParsingException("Lack of the operands");
      }
      if (opers.getFirst().getValue() == 'm') {
        Double val = nums.pop();
        nums.push(-val);
      }
      opers.pop();
      return;
    }
    if (nums.size() < 2) {
      throw new ParsingException("Lack of the operands");
    }

    Double b = nums.pop();
    Double a = nums.pop();
    Char curOper = opers.pop();
    Double result = new Double(0.0);
    switch (curOper.getValue()) {
      case '+':
        result = a + b;
        break;
      case '-':
        result = a - b;
        break;
      case '*':
        result = a * b;
        break;
      case '/':
        result = a / b;
        break;
      default:
        throw new ParsingException("An error occured.");
    }
    nums.push(result);
  }

  public static void check(String expr) throws ParsingException {
    if (expr == null) {
      throw new ParsingException("null string");
      //throw new NullPointerException();
    }

    int openBalance = 0;
    char lastCh = '!';

    for (int i = 0; i < expr.length(); ++i) {
      char curCh = expr.charAt(i);
      if (isSpaceChar(curCh)) {
        continue;
      }
      if ((curCh == '.' || Character.isDigit(curCh)) && (lastCh == '.' || Character
          .isDigit(lastCh))) {
        if (i > 0 && isSpaceChar(expr.charAt(i - 1))) {
          throw new ParsingException("Numbers mustn't be divided by spaces");
        }
      }
      if (curCh != '(' && curCh != ')' && !isOperator(curCh) && !Character.isDigit(curCh)
          && curCh != '.') {
        throw new ParsingException("Invalid character");
      }
      if ((lastCh == '-' || lastCh == '+') && (curCh == '-' || curCh == '+')) {
        throw new ParsingException("Two operators (+, -) in a row");
      }
      if (lastCh == '.' && curCh == '.') {
        throw new ParsingException("Two points in a row");
      }
      if (expr.charAt(i) == '(') {
        ++openBalance;
      } else if (expr.charAt(i) == ')') {
        --openBalance;
      }
      if (openBalance < 0) {
        throw new ParsingException("Negative bracket balance");
      }
      lastCh = expr.charAt(i);
    }
    if (openBalance != 0) {
      throw new ParsingException("Final bracket balance is not equal to 0");
    }
  }

  public double calculate(String expr) throws ParsingException {
    double res = 0.0;
    try {
      check(expr);
      ArrayDeque<Char> opers = new ArrayDeque<Char>();
      ArrayDeque<Double> nums = new ArrayDeque<Double>();
      char lastCh = '!';
      for (int i = 0; i < expr.length(); ++i) {
        char curCh = expr.charAt(i);
        if (isSpaceChar(curCh)) {
          continue;
        }
        if (curCh == '.') {
          throw new ParsingException("Unexpected point");
        }
        if (isOperator(curCh) && !((curCh == '+' || curCh == '-') && (lastCh == '!'
            || lastCh == '(' || isOperator(lastCh)))) {
          while (!opers.isEmpty() && prior(opers.getFirst().getValue()) >= prior(curCh)) {
            action(opers, nums);
          }
          opers.push(new Char(curCh));
        } else if ((curCh == '+' || curCh == '-') && (lastCh == '!' || lastCh == '('
            || isOperator(lastCh))) {
          if (curCh == '-') {
            opers.push(new Char('m'));
          } else {
            opers.push(new Char('p'));
          }
        } else if (curCh == '(') {
          opers.push(new Char('('));
        } else if (curCh == ')') {
          while (opers.getFirst().getValue() != '(') {
            action(opers, nums);
          }
          Char kek = opers.pop();
        } else {
          int r = i;
          int ptcnt = 0;
          while (r + 1 < expr.length() && (Character.isDigit(expr.charAt(r + 1))
            || expr.charAt(r + 1) == '.')) {
            ++r;
            if (expr.charAt(r) == '.') {
              ++ptcnt;
            }
          }
          if (ptcnt > 1) {
            throw new ParsingException("More than 1 point in the double");
          }
          double curd = Double.parseDouble(expr.substring(i, r + 1));
          nums.push(curd);
          i = r;
          curCh = expr.charAt(r);
        }
        lastCh = curCh;
      }
      while (opers.size() > 0 || nums.size() > 1) {
        action(opers, nums);
      }
      if (nums.size() != 1) {
        throw new ParsingException("Inbalance in the amount of operators and operands");
      }
      res = nums.getLast();
    } catch (ParsingException parsingExpr) {
      throw parsingExpr;
    }
    return res;
  }
}
