package ru.mipt.java2017.homework.g696.sobakina.task1;


import java.util.Stack;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
//import java.util.StringTokenizer;

/**
 * @author Sobakina Olga
 * @since 05.10.17
 */

// from valid input string get an answer -- result of calculations
public class MyJavaCalculator implements Calculator {

  static final String STRING_OF_OPERANDS = "\\(|\\)|\\*|\\/|\\-|\\+";

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
    return presumablyOperator.matches(STRING_OF_OPERANDS);
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
    Integer xPriority = -1;
    Integer yPriority = -1;
    if (isOperator(x) && isOperator(y)) {
      if ((x.equals("(")) || (x.equals(")"))) {
        xPriority = 1;
      } else if ((x.equals("-")) || (x.equals("+"))) {
        xPriority = 2;
      } else if ((x.equals("*")) || (x.equals("/"))) {
        xPriority = 3;
      }

      if ((y.equals("(")) || (y.equals(")"))) {
        yPriority = 1;
      } else if ((y.equals("-")) || (y.equals("+"))) {
        yPriority = 2;
      } else if ((y.equals("*")) || (y.equals("/"))) {
        yPriority = 3;
      }
      return xPriority >= yPriority;
    } else {
      throw new ParsingException("not operators");
    }
  }

  // pop 2 operands and 1 operator and return the result of received simple expression
  private void accessoryCalculation(Stack<Double> q, Stack<String> w) throws ParsingException {

    if (q.size() >= 2) {
      double a = q.pop();
      double b = q.pop();
      double result;
      String operator = w.pop();

      if (operator.matches("\\+")) {
        result = a + b;
        q.push(result);
      } else if (operator.matches("\\-")) {
        result = b - a;
        q.push(result);
      } else if (operator.matches("\\*")) {
        result = a * b;
        q.push(result);
      } else if (operator.matches("\\/")) {
        result = b / a;
        q.push(result);
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
    expression = expression.replaceAll(STRING_OF_OPERANDS, " $0 ");

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
    Stack<String> w = new Stack<String>(); // stack of operators
    Stack<Double> q = new Stack<Double>(); // stack of operands - numbers

    for (int i = 0; i < expressionModified.length; i++) {
      if (isOperand(expressionModified[i])) {
        q.push(transformOperand(expressionModified[i]));
      } else if (isOperator(expressionModified[i])) {
        if (expressionModified[i].matches("\\(") || expressionModified[i].matches("\\)")) {
          if (expressionModified[i].matches("\\)")) {
            while (!(w.empty() || (w.peek().matches("\\(")))) {
              accessoryCalculation(q, w);
            }
            if (w.empty()) {
              throw new ParsingException("don't have open-bracket.");
            } else {
              w.pop();
            }
          } else {
            w.push(expressionModified[i]);
          }
        } else {
          while (!w.empty() && firstPriorityIsBigger(w.peek(), expressionModified[i])) {
            accessoryCalculation(q, w);
          }
          w.push(expressionModified[i]);
        }
      } else {
        throw new ParsingException("not operand and not operator was met.");
      }
    }
    if (q.isEmpty()) {
      throw new ParsingException("no operands in stack.");
    }

    double answer = q.pop();

    if (q.isEmpty()) {
      return answer;
    } else {
      throw new ParsingException("not finished. still some operands are in stack.");
    }
  }

}
