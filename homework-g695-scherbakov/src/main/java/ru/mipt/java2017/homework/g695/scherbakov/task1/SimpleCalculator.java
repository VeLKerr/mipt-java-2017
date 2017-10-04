package ru.mipt.java2017.homework.g695.scherbakov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class SimpleCalculator implements Calculator {
  /**
   * @param expression expression with numbers and operators as a string
   * @return a list of operands and operators of type OperatorOrNumber as they come in the string
   * @throws ParsingException if the expression is not syntactically valid and cannot be computed
   */
  private List<OperatorOrNumber> parseExpression(String expression) throws ParsingException {
    List<OperatorOrNumber> result = new LinkedList<>();

    int brackets = 0; // to check if brackets are balanced
    OperatorOrNumber last = null;

    try {
      Reader reader = new StringReader(expression);
      int symbol;

      do {
        symbol = reader.read();

        // Parse floating point number
        if (Character.isDigit(symbol)) {
          double number = 0;

          while (Character.isDigit(symbol)) {
            number *= 10;
            number += Character.getNumericValue(symbol);
            symbol = reader.read();
          }

          if (symbol == '.') {
            double base = 1;
            symbol = reader.read();

            while (Character.isDigit(symbol)) {
              base /= 10;
              number += base * Character.getNumericValue(symbol);
              symbol = reader.read();
            }
          }

          last = new OperatorOrNumber(number);
          result.add(last);
        }

        switch (symbol) {

          // Unary operators come after '(' or '/' or '*' or in beginning
          // Binary only after ')' or a number
          // ')' only after a number or ')'
          // '(' only after operator or in beginning

          case '+':
            if (last == null || last.operator == Operator.LEFT_BRACE ||
              last.operator == Operator.DIVIDE || last.operator == Operator.MULTIPLY) {
              last = new OperatorOrNumber(Operator.UNARY_PLUS);
              result.add(last);
            } else if (last.isNumber || last.operator == Operator.RIGHT_BRACE) {
              last = new OperatorOrNumber(Operator.PLUS);
              result.add(last);
            } else {
              throw new ParsingException("Invalid placement of plus operator");
            }
            break;

          case '-':
            if (last == null || last.operator == Operator.LEFT_BRACE ||
              last.operator == Operator.DIVIDE || last.operator == Operator.MULTIPLY) {
              last = new OperatorOrNumber(Operator.UNARY_MINUS);
              result.add(last);
            } else if (last.isNumber || last.operator == Operator.RIGHT_BRACE) {
              last = new OperatorOrNumber(Operator.MINUS);
              result.add(last);
            } else {
              throw new ParsingException("Invalid placement of minus operator");
            }
            break;

          case '*':
            if (last != null && (last.operator == Operator.RIGHT_BRACE || last.isNumber)) {
              last = new OperatorOrNumber(Operator.MULTIPLY);
              result.add(last);
            } else {
              throw new ParsingException("Invalid placement of multiplying operator");
            }
            break;

          case '/':
            if (last != null && (last.operator == Operator.RIGHT_BRACE || last.isNumber)) {
              last = new OperatorOrNumber(Operator.DIVIDE);
              result.add(last);
            } else {
              throw new ParsingException("Invalid placement of division operator");
            }
            break;

          case '(':
            if (last == null || (!last.isNumber && last.operator != Operator.LEFT_BRACE &&
              last.operator != Operator.RIGHT_BRACE)) {
              last = new OperatorOrNumber(Operator.LEFT_BRACE);
              result.add(last);
            } else {
              throw new ParsingException("Invalid placement of brackets");
            }
            brackets++;
            break;

          case ')':
            if (last != null && (last.isNumber || last.operator == Operator.RIGHT_BRACE)) {
              last = new OperatorOrNumber(Operator.RIGHT_BRACE);
              result.add(last);
            } else {
              throw new ParsingException("Invalid placement of brackets");
            }
            brackets--;
            break;

          default:
            if (symbol == -1 || Character.isWhitespace(symbol)) {
              break;
            } else {
              throw new ParsingException("Unexpected character: " + Character.getName(symbol));
            }
        }

        if (brackets < 0) {
          throw new ParsingException("Unbalanced brackets");
        }

      } while (symbol != -1);

    } catch (Exception exception) {
      throw new ParsingException(exception.getMessage());
    }

    // Expression must be not empty and end with number or ')'
    if (last == null || !(last.isNumber || last.operator == Operator.RIGHT_BRACE)) {
      throw new ParsingException("Unexpected ending of expression");
    }

    // Brackets balance should be 0
    if (brackets != 0) {
      throw new ParsingException("Unbalanced brackets");
    }

    return result;
  }

  /**
   * @param expression - expression using infix notation containing numbers and allowed operators
   * @return same expression written using postfix notation
   */
  private Queue<OperatorOrNumber> toRPN(List<OperatorOrNumber> expression) {
    Queue<OperatorOrNumber> result = new LinkedList<>();
    Stack<Operator> stack = new Stack<>();

    for (OperatorOrNumber element : expression) {
      if (element.isNumber) {
        result.add(element);
      } else if (element.operator == Operator.LEFT_BRACE) {
        stack.push(element.operator);
      } else if (element.operator == Operator.RIGHT_BRACE) {
        while (stack.peek() != Operator.LEFT_BRACE) {
          result.add(new OperatorOrNumber(stack.pop()));
        }
        stack.pop(); //remove left brace
      } else {
        while (!stack.isEmpty() && element.operator.priority <= stack.peek().priority) {
          result.add(new OperatorOrNumber(stack.pop()));
        }
        stack.push(element.operator);
      }
    }

    while (!stack.empty()) {
      if (stack.peek() != Operator.LEFT_BRACE) {
        result.add(new OperatorOrNumber(stack.pop()));
      }
    }

    return result;
  }

  /**
   * @param expression: a valid (!) expression of operators and numbers in reverse polish notation
   * @return result of calculating the expression
   */
  private double processRPN(Queue<OperatorOrNumber> expression) {
    Stack<Double> stack = new Stack<>();
    while (!expression.isEmpty()) {
      if (expression.peek().isNumber) {
        stack.push(expression.remove().number);
      } else {
        Operator operator = expression.remove().operator;
        double operands[] = new double[operator.numberOfArgs];

        for (int i = 0; i < operator.numberOfArgs; i++) {
          operands[i] = stack.pop();
        }

        stack.push(operator.apply(operands));
      }
    }
    return stack.pop();
  }

  @Override
  public double calculate(String expression) throws ParsingException {
    return processRPN(toRPN(parseExpression(expression)));
  }

  private enum Operator {
    PLUS('+', 1, 2),
    MINUS('-', 1, 2),
    MULTIPLY('*', 2, 2),
    DIVIDE('/', 2, 2),
    LEFT_BRACE('(', 0, 0),
    RIGHT_BRACE(')', 0, 0),
    UNARY_PLUS('+', 3, 1),
    UNARY_MINUS('-', 3, 1);

    public final char symbol;
    public final int priority;
    public final int numberOfArgs;

    Operator(char symbol, int priority, int numberOfArgs) {
      this.symbol = symbol;
      this.priority = priority;
      this.numberOfArgs = numberOfArgs;
    }

    /**
     * @param operands N numbers in reverse order
     * @return result of applying operator with ordness N to them
     */
    public double apply(double[] operands) {
      if (this == UNARY_PLUS) {
        return operands[0];
      } else if (this == UNARY_MINUS) {
        return -operands[0];
      } else if (this == PLUS) {
        return operands[1] + operands[0];
      } else if (this == MINUS) {
        return operands[1] - operands[0];
      } else if (this == MULTIPLY) {
        return operands[1] * operands[0];
      } else if (this == DIVIDE) {
        return operands[1] / operands[0];
      } else {
        return 0;
      }
    }
  }

  private class OperatorOrNumber {
    public final Operator operator;
    public final double number;
    public final boolean isNumber;

    OperatorOrNumber(Operator operator) {
      this.operator = operator;
      this.number = Double.NaN;
      this.isNumber = false;
    }

    OperatorOrNumber(double number) {
      this.operator = null;
      this.number = number;
      this.isNumber = true;
    }

    @Override
    public String toString() {
      if (this.isNumber) {
        return Double.toString(this.number);
      } else {
        return Character.toString(this.operator.symbol);
      }
    }
  }
}