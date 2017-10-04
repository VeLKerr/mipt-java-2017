package ru.mipt.java2017.homework.g695.gostkin.task1;

import java.util.NoSuchElementException;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

/**
 * Calculator implementation
 *
 * @author Eugene M. Gostkin
 * @since 22.09.17
 */

public class NewCalculator implements Calculator {

  enum Operator {
    LeftBrace,
    RightBrace,
    Plus,
    Minus,
    Multiply,
    Divide,
    UnaryMinus,
    UnaryPlus,
    Space,
    Digit,
    Point,
    Null
  }

  private boolean isPreUnary(Operator operator) {
    switch (operator) {
      case Plus:
      case Minus:
      case UnaryPlus:
      case UnaryMinus:
        return true;
      default:
        return false;
    }
  }

  private boolean isUnary(Operator operator) {
    switch (operator) {
      case UnaryPlus:
      case UnaryMinus:
        return true;
      default:
        return false;
    }
  }

  private Operator makeOperator(char symbol) throws ParsingException {
    if (Character.isDigit(symbol)) {
      return Operator.Digit;
    }

    switch (symbol) {
      case '(':
        return Operator.LeftBrace;
      case ')':
        return Operator.RightBrace;
      case '+':
        return Operator.Plus;
      case '-':
        return Operator.Minus;
      case '*':
        return Operator.Multiply;
      case '/':
        return Operator.Divide;
      case ' ':
      case '\t':
        return Operator.Space;
      case '.':
        return Operator.Point;
      default:
        throw new ParsingException(String.format("Operator \"%c\"is not recognized.", symbol));
    }
  }

  private int getOperatorPriority(Operator operator) throws ParsingException {
    switch (operator) {
      case Plus:
      case Minus:
        return 1;
      case Multiply:
      case Divide:
        return 2;
      case UnaryPlus:
      case UnaryMinus:
        return 3;
      default:
        return -1;
    }
  }

  private void processOperator(Stack<Double> stack, Operator operator) throws ParsingException {
    if (isUnary(operator)) {
      double left = stack.pop();
      switch (operator) {
        case UnaryMinus:
          stack.push(-left);
          break;
        case UnaryPlus:
          stack.push(left);
          break;
        default:
          throw new ParsingException(String.format("Operator \"%s\" is not unary.",
            operator.toString()));
      }
    } else {
      double right = stack.pop();
      double left = stack.pop();

      switch (operator) {
        case Plus:
          stack.push(left + right);
          break;
        case Minus:
          stack.push(left - right);
          break;
        case Multiply:
          stack.push(left * right);
          break;
        case Divide:
          stack.push(left / right);
          break;
        default:
          throw new ParsingException(String.format("Operator \"%s\" is not recognized.",
            operator.toString()));
      }
    }
  }

  /**
   * Parses digit starting at index.
   *
   * @param expression string with arithmetic expression
   * @param stack stack with numbers
   * @param index index of operator
   * @return returns last value of index
   * @throws ParsingException exception when the expression is not valid
   */
  private int parseDigit(String expression, Stack<Double> stack,
      int index) throws ParsingException {
    StringBuilder operand = new StringBuilder();
    String result;
    boolean hasPoint = false;
    while (index < expression.length() && (Character.isDigit(expression.charAt(index)) ||
      expression.charAt(index) == '.')) {
      if (expression.charAt(index) == '.') {
        if (!hasPoint) {
          hasPoint = true;
        } else {
          throw new ParsingException("Too many points in one number.");
        }
      }
      operand.append(expression.charAt(index++));
    }
    --index;
    result = operand.toString();
    stack.push(Double.parseDouble(result));
    return index;
  }

  /**
   * Parses Operator operator at index.
   *
   * @param expression string with arithmetic expression
   * @param stack stack with numbers
   * @param operators stack with operators
   * @param operator current operator
   * @param wasUnary condition: if the last operator was unary
   * @param index index of operator
   * @return returns if current operator is unary
   * @throws ParsingException exception when the expression is not valid
   */
  private boolean parseOperator(String expression, Stack<Double> stack, Stack<Operator> operators,
      Operator operator, boolean wasUnary, int index) throws ParsingException {
    if (index + 1 == expression.length()) {
      throw new ParsingException("Invalid operators order.");
    }

    Operator prevOperator;
    if (index == 0) {
      prevOperator = Operator.Null;
    } else {
      prevOperator = makeOperator(expression.charAt(index - 1));
    }

    boolean unary = false;

    if (prevOperator == Operator.Null ||
        prevOperator == Operator.Plus || prevOperator == Operator.Minus ||
        prevOperator == Operator.Multiply || prevOperator == Operator.Divide ||
        prevOperator == Operator.LeftBrace) {
      if (isPreUnary(operator)) {
        if (operator == Operator.Plus) {
          operator = Operator.UnaryPlus;
        } else if (operator == Operator.Minus) {
          operator = Operator.UnaryMinus;
        } else {
          throw new ParsingException(String.format("Operator \"%s\" is not unary.",
            operator.toString()));
        }

        unary = true;

        if (wasUnary) {
          throw new ParsingException("Two unary operators are applied to one number.");
        }

        wasUnary = true;
      } else {
        throw new ParsingException("Non-unary operator is used like unary.");
      }
    }

    while (!operators.empty() &&
      (!unary && getOperatorPriority(operators.lastElement()) >=
        getOperatorPriority(operator) || unary &&
        getOperatorPriority(operators.lastElement()) >
          getOperatorPriority(operator))) {
      processOperator(stack, operators.pop());
    }

    operators.push(operator);

    return wasUnary;
  }

  /**
   * Takes String with valid math expression. Returns result of its execution.
   *
   * May contain real numbers, operators +, -, *, / and braces (, ). Any spaces could be in the
   * expression.
   *
   * @param expression string with arithmetic expression
   * @return the result of its execution
   * @throws ParsingException exception when the expression is not valid
   */

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("No expression.");
    }

    boolean wasUnary = false;

    expression = expression.replaceAll("[\\s]", "");

    Stack<Double> stack = new Stack<Double>();
    Stack<Operator> operators = new Stack<Operator>();

    for (int i = 0; i < expression.length(); ++i) {
      char operatorRaw = expression.charAt(i);
      Operator operator = makeOperator(operatorRaw);

      if (operator != Operator.Space) {
        if (operator == Operator.LeftBrace) {
          operators.push(Operator.LeftBrace);
          wasUnary = false;
        } else if (operator == Operator.RightBrace) {
          try {
            while (operators.lastElement() != Operator.LeftBrace) {
              processOperator(stack, operators.pop());
            }
          } catch (NoSuchElementException exception) {
            throw new ParsingException("Unbalanced braces.");
          }
          operators.pop();
          wasUnary = false;
        } else if (operator != Operator.Digit) {
          wasUnary = parseOperator(expression, stack, operators, operator, wasUnary, i);
        } else {
          i = parseDigit(expression, stack, i);
          wasUnary = false;
        }
      }
    }
    while (!operators.empty()) {
      processOperator(stack, operators.pop());
    }

    if (!stack.empty()) {
      return stack.lastElement();
    } else {
      throw new ParsingException("Expression has no result");
    }
  }
}