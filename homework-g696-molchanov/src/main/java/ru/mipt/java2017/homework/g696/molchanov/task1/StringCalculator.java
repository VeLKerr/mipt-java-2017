package ru.mipt.java2017.homework.g696.molchanov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;


/**
 * Created by mike on 10/6/17.
 */
public class StringCalculator implements Calculator{

  private Stack<Double> numbers;
  private Stack<Character> operations;

  @Override
  public double calculate(String expression) throws ParsingException {
    numbers = new Stack<>();
    operations = new Stack<>();

    try {
      if (expression == null) {
        throw new ParsingException("Incorrect expression");
      }
      if (!checkForConsequentNumbers(expression)) {
        throw new ParsingException("Incorrect expression");
      }
      expression = deleteSpaces(expression);
      if (!checkIncorrectExpression(expression)) {
        throw new ParsingException("Incorrect expression");
      }
      return toRPH(expression);
    } finally {
      numbers = null;
      operations = null;
    }
  }

  private boolean checkForConsequentNumbers(String expression) {
    boolean opBetween = true; //Operator must be between any two numbers
    boolean notString = true;
    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      if (c == '*' || c == '/' || c == '+' || c == '-') {
        opBetween = true;
        notString = true;
      } else if (Character.isDigit(c) || c == '.') {
        if (notString && !opBetween) {
          return false;
        }
        notString = false;
        opBetween = false;
      } else {
        notString = true;
      }
    }
    return true;
  }

  private int priority(char c) {
    if (c == '+' || c == '-') {
      return 1;
    } else if (c == '*' || c == '/') {
      return 2;
    } else if (c == 'M') {
      return 3;
    } else {
      return -1;
    }
  }

  private void operatorCalculation(char c) {
    double a;
    if (c == 'M') {
      a = numbers.pop();
      numbers.push(-a);
      return;
    }

    a = numbers.pop();
    double b = numbers.pop();

    if (c == '+') {
      numbers.push(a + b);
    } else if (c == '-') {
      numbers.push(b - a);
    } else if (c == '*') {
      numbers.push(b * a);
    } else if (c == '/') {
      numbers.push(b / a);
    }
  }

  private double toRPH(String expression) throws ParsingException {
    /** before the unary minus is either an operation, or '(' */
    boolean isUnary = true;

    for (int i = 0; i < expression.length(); ++i) {
      char c = expression.charAt(i);

      if (c == '(') {
        isUnary = true;
        operations.push(c);
      } else if (c == ')') {
        /** calculate value in brackets */
        while (operations.peek() != '(') {
          operatorCalculation(operations.peek());
          operations.pop();
        }

        /** after ')' cannot be unary minus */
        isUnary = false;
        operations.pop();

      } else if (c == '+' || c == '-' || c == '*' || c == '/') {
        if (isUnary && c == '-') {
          c = 'M';
        }

        /** first we perform operations with a higher priority */
        while (!operations.isEmpty() && ((c != 'M' &&
          priority(operations.peek()) >= priority(c)) || (c == 'M'
          && priority(operations.peek()) > priority(c)))) {
          operatorCalculation(operations.peek());
          operations.pop();
        }

        operations.push(c);
        isUnary = true;
      } else {
        String operand = "";

        /** find the decimal number and add it to the number vector */
        while (i < expression.length() &&
          (Character.isDigit(expression.charAt(i))
            || expression.charAt(i) == '.')) {
          operand += expression.charAt(i);
          i++;
        }
        i--;
        numbers.push(Double.parseDouble(operand));
        /** after the number can not stand unary minus */
        isUnary = false;
      }
    }

    /** perform the remaining operations on the resulting numbers from the numbers */
    while (!operations.isEmpty()) {
      operatorCalculation(operations.peek());
      operations.pop();
    }
    if (numbers.size() != 1) {
      throw new ParsingException("Invalid expression.");
    }
    return numbers.peek();
  }

  private String deleteSpaces(String expression) {
    String returnExpression = "";
    for (int i = 0; i < expression.length(); ++i) {
      if (expression.charAt(i) != ' ' && expression.charAt(i) != '\t' && expression.charAt(i) != '\n') {
        returnExpression += Character.toString(expression.charAt(i));
      }
    }
    return returnExpression;
  }

  private boolean checkIncorrectExpression(String expres) {
    int bracketResult = 0;

    /** the expression is nonempty
        the first place is not binary operations
        in the last place either a digit or ')' */
    if (expres.length() == 0 || expres.charAt(0) == '*'
      || expres.charAt(0) == '/' || expres.charAt(0) == '+'
      || !(Character.isDigit(expres.charAt(expres.length() - 1))
      || expres.charAt(expres.length() - 1) == ')')) {
      return false;
    }

    for (int i = 0; i < expres.length(); ++i) {
      if (expres.charAt(i) == '(') {
        bracketResult += 1;
      }
      if (expres.charAt(i) == ')') {
        bracketResult -= 1;
      }

      /** after the operator there is no binary operator (that is, not *, /, +) */
      if (expres.charAt(i) == '-' || expres.charAt(i) == '+'
        || expres.charAt(i) == '/' || expres.charAt(i) == '*') {
        if (i + 1 >= expres.length() || expres.charAt(i + 1) == '+'
          || expres.charAt(i + 1) == '/' || expres.charAt(i + 1) == '*') {
          return false;
        }
      }

      /** check for invalid characters */
      if (!(Character.isDigit(expres.charAt(i)) || expres.charAt(i) == '.'
        || expres.charAt(i) == '(' || expres.charAt(i) == ')'
        || expres.charAt(i) == '+' || expres.charAt(i) == '-' ||
        expres.charAt(i) == '*' || expres.charAt(i) == '/')) {
        return false;
      }

      /** check for a non-negative bracketed result */
      if (bracketResult < 0) {
        return false;
      }

      /** *, /, + are not binary operators, that is, they can not stand after '('
          also the empty brackets are considered to be a non-explicit expression */
      if (expres.charAt(i) == '(') {
        if (i + 1 >= expres.length() || (expres.charAt(i + 1) == '+'
          || expres.charAt(i + 1) == '*' ||
          expres.charAt(i + 1) == '/' || expres.charAt(i + 1) == ')')) {
          return false;
        }
      }
    }
    if (bracketResult != 0) {
      return false;
    }

    /** check for correctness of the decimal expression (in each number not more than one '.') */
    int dot = 0;
    int i = 0;
    while (i < expres.length() && dot < 2) {
      if (expres.charAt(i) == '+' || expres.charAt(i) == '-'
        || expres.charAt(i) == '/' || expres.charAt(i) == '*') {
        dot  = 0;
      }
      if (expres.charAt(i) == '.') {
        dot += 1;
      }
      i++;
    }
    return dot < 2;
  }
}
