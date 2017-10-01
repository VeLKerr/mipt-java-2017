package ru.mipt.java2017.homework.g695.gostkin.task1;

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
    Point
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
        throw new ParsingException(String.format("Operator \"%s\" does not have priority.",
            operator.toString()));
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
      }
    }
  }

  @Override
  public double calculate(String expression) throws ParsingException {
    boolean unary = true;

    Stack<Double> stack = new Stack<Double>();
    Stack<Operator> operators = new Stack<Operator>();

    for (int i = 0; i < expression.length(); ++i) {
      char operatorRaw = expression.charAt(i);
      Operator operator = makeOperator(operatorRaw);

      if (operator != Operator.Space) {
        if (operator == Operator.LeftBrace) {
          operators.push(Operator.LeftBrace);
          unary = true;
        } else if (operator == Operator.RightBrace) {
          while (operators.lastElement() != Operator.LeftBrace) {
            processOperator(stack, operators.pop());
          }

          operators.pop();
          unary = false;
        } else if (operator != Operator.Digit) {
          boolean unaryFinal = unary && isUnary(operator);

          while (!operators.empty() &&
              (!unaryFinal && getOperatorPriority(operators.lastElement()) >=
                  getOperatorPriority(operator)
                  || unaryFinal && getOperatorPriority(operators.lastElement()) >
                  getOperatorPriority(operator))
              ) {
            processOperator(stack, operators.pop());
          }

          operators.push(operator);
          unary = true;
        } else {
          StringBuilder operand = new StringBuilder();
          String result;
          while (i < expression.length() && (Character.isDigit(expression.charAt(i)) ||
              expression.charAt(i) == '.')) {
            operand.append(expression.charAt(i++));
          }
          --i;
          result = operand.toString();
          stack.push(Double.parseDouble(result));
          unary = false;
        }
      }
    }
    while (!operators.empty()) {
      processOperator(stack, operators.pop());
    }
    return stack.lastElement();
  }
}