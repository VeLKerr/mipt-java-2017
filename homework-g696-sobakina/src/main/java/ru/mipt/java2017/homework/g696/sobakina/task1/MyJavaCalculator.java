package ru.mipt.java2017.homework.g696.sobakina.task1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.lang.Throwable;
import java.util.StringTokenizer;

/**
 * @author Sobakina Olga
 * @since 05.10.17
 */

// from valid input string get an answer -- result of calculations
public class MyJavaCalculator implements Calculator
{
  class Pair<String, Integer> {
    String object1;
    Integer object2;

    Pair(String one, Integer two) {
      object1 = one;
      object2 = two;
    }
    public String getFirst() {
      return object1;
    }
    public Integer getSecond() {
      return object2;
    }
  }

  Pair<String, Integer> openbracket = new Pair<String, Integer>("(", 1);
  Pair<String, Integer> closebracket = new Pair<String, Integer>(")", 1);
  Pair<String, Integer> minus = new Pair<String, Integer>("-", 2);
  Pair<String, Integer> plus = new Pair<String, Integer>("+", 2);
  Pair<String, Integer> multiply = new Pair<String, Integer>("*", 3);
  Pair<String, Integer> divide = new Pair<String, Integer>("/", 3);

  static final String operators = "\\(|\\)|\\*|\\/|\\-|\\+";

  // check if our input string is a double number
  public static boolean isOperand(String presumablyOperand) {
    try {
      Double.parseDouble(presumablyOperand);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  // check if our input string is an operator
  public static boolean isOperator(String presumablyOperator) {
    return presumablyOperator.matches(operators);
  }

  // transform from String to Double if possible
  public static double transformOperand(String operand) throws ParsingException {
    try {
      return Double.parseDouble(operand);
    } catch (NumberFormatException e) {
      throw new ParsingException("impossible to transform into double.", e.getCause());
    }
  }

  // compares the priorities of two given operators
  public static boolean firstPriorityIsBigger(String x, String y) throws ParsingException {
    Integer xPriority = -1, yPriority = -1;
    if (isOperator(x) && isOperator(y)) {
      if ((x.equals("(")) || (x.equals(")"))) {
        xPriority = 1;
      }
      else if ((x.equals("-")) || (x.equals("+"))) {
        xPriority = 2;
      }
      else if ((x.equals("*")) || (x.equals("/"))) {
        xPriority = 3;
      }

      if ((y.equals("(")) || (y.equals(")"))) {
        yPriority = 1;
      }
      else if ((y.equals("-")) || (y.equals("+"))) {
        yPriority = 2;
      }
      else if ((y.equals("*")) || (y.equals("/"))) {
        yPriority = 3;
      }
      return xPriority >= yPriority;
    }
    else {
      throw new ParsingException("not operators");
    }
  }

  // pop 2 operands and 1 operator and return the result of received simple expression
  private void accessoryCalculation(Stack<Double> Q, Stack<String> W)
    throws ParsingException {
    if (Q.size() >= 2) {
      double a = Q.pop();
      double b = Q.pop();
      double result;
      String operator = W.pop();

      if (operator.matches("\\+")) {
        result = a + b;
        Q.push(result);
      } else if (operator.matches("\\-")) {
        result = b - a;
        Q.push(result);
      } else if (operator.matches("\\*")) {
        result = a * b;
        Q.push(result);
      } else if (operator.matches("\\/")) {
        result = b / a;
        Q.push(result);
      } else {
        throw new ParsingException("valid operator is not given.");
      }
    } else {
      throw new ParsingException("2 operands aren't given.");
    }
  }

  // finally the main void, calculates all expression
  public double calculate(String expression) throws ParsingException {
    // first we simplify (for a program) our string.
    // 1) we add brackets
    expression = "(" + expression + ")";
    // 2) if we see some operator, surround it with spaces
    expression = expression.replaceAll(operators," $0 ");

    // 3) if we have unary operations in our input string,
    // we make it binary by adding 0 at the beginning of the string
    expression = expression.replaceAll("([\\/|\\*])[\\s]*([\\+|\\-])[\\s]*(\\()", "$1 ( $2 ");
    expression = expression.replaceAll("(\\()[\\s]*([\\+|\\-])[\\s]*", "$1 -0.0 $2 ");
    expression = expression.replaceAll("([\\/|\\*])[\\s]*([\\+|\\-])[\\s]*(\\d*\\.?\\d*)",
      "$1 ( -0.0 $2 $3 )");

    // 4) we remove unnecessary spaces
    String[] expressionModified = expression.trim().split("[\\s]+");

    // simplification is finished
    // now we start the algorithm (read more in detail here: https://habrahabr.ru/post/50196/)
    Stack<String> W = new Stack<String>(); // stack of operators
    Stack<Double> Q = new Stack<Double>(); // stack of operands - numbers

    for (int i = 0; i < expressionModified.length; i++) {
      if (isOperand(expressionModified[i])) {
        Q.push(transformOperand(expressionModified[i]));
      }
      else if (isOperator(expressionModified[i])) {
        if (expressionModified[i].matches("\\(") ||
            expressionModified[i].matches("\\)")) {
          if (expressionModified[i].matches("\\)")) {
            while (!(W.empty() || (W.peek().matches("\\(")))) {
              accessoryCalculation(Q, W);
            }
            if (W.empty()) {
              throw new ParsingException("don't have open-bracket.");
            } else {
              W.pop();
            }
          } else {
            W.push(expressionModified[i]);
          }
        } else {
          while (!W.empty() && firstPriorityIsBigger(W.peek(), expressionModified[i])) {
            accessoryCalculation(Q, W);
          }
          W.push(expressionModified[i]);
        }
      } else {
        throw new ParsingException("not operand and not operator was met.");
      }
    }
    if (Q.isEmpty()) {
      throw new ParsingException("no operands in stack.");
    }

    double answer = Q.pop();

    if (Q.isEmpty()) {
      return answer;
    } else {
      throw new ParsingException("not finished. still some operands are in stack.");
    }
  }

}

