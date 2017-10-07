package ru.mipt.java2017.homework.g696.mosyagin.task1;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * Provides methods to build a postfix exprssion from an infix expression
 * represented as a sequence of tokens and evaluate it.
 *
 * @author Mikhail Mosyagin
 * @see Token
 */
public class PolishNotation {

  private ArrayList<Token> polishNotation;
  private Stack<Token> operations;
  private boolean evaluated;
  private double result;

  /**
   * Creates empty reverse Polish notation expression.
   */
  PolishNotation() {
    polishNotation = new ArrayList<Token>();
    operations = new Stack<Token>();
    evaluated = false;
  }

  /**
   * Updates expression depending on the token. Tokens should
   * be pushed in the same order as they appear in the infix expression.
   * <p>
   * This method cannot be used after the expression has been evaluated.
   *
   * @param token the token to be added
   * @throws ParsingException If used after the expression has been evaluated
   * @see Token
   */
  public void pushToken(Token token) throws ParsingException {
    if (evaluated) {
      throw new ParsingException("Cannot push new tokens after the expression has been evaluated");
    }

    if (token.getType() == TokenType.NUMBER) {
      polishNotation.add(token);
    } else {
      // the token is either an operation or the end of the expression
      while (!operations.empty()
          && getOperationPriority(operations.peek()) >= getOperationPriority(token)) {
        polishNotation.add(operations.pop());
      }
      operations.push(token);
    }
  }

  /**
   * Evaluates the expression.
   * <p>
   * The result is cached, so the actual evaluation process is run
   * only once. This also means that it is impossible to change the
   * expression after evaluation.
   *
   * @return the result of the expression
   */
  public double evaluateExpression() throws ParsingException {
    if (evaluated) {
      return result;
    }

    pushToken(new Token(';'));

    Stack<Double> results = new Stack<Double>();

    for (Token token : polishNotation) {
      try {
        switch (token.getType()) {
          case UNARY_PLUS:
            break;
          case UNARY_MINUS:
            results.push(-results.pop());
            break;
          case BINARY_PLUS:
            results.push(results.pop() + results.pop());
            break;
          case BINARY_MINUS:
            results.push(-results.pop() + results.pop());
            break;
          case MULTIPLICATION:
            results.push(results.pop() * results.pop());
            break;
          case DIVISION:
            results.push(1 / results.pop() * results.pop());
            break;
          case NUMBER:
            results.push(token.getValue());
            break;
          default:
            throw new ParsingException("Unexpected token in Polish notation");
        }
      } catch (EmptyStackException exception) {
        throw new ParsingException("Not enough operands for operation");
      }
    }

    if (results.size() != 1) {
      throw new ParsingException("Failed to evaluate expression");
    }
    result = results.pop();
    evaluated = true;

    return result;
  }

  private int getOperationPriority(Token operation) throws ParsingException {
    switch (operation.getType()) {
      case END_OF_EXPRESSION:
        return Integer.MIN_VALUE;
      case BINARY_MINUS:
      case BINARY_PLUS:
        return 0;
      case DIVISION:
      case MULTIPLICATION:
        return 1;
      case UNARY_MINUS:
      case UNARY_PLUS:
        return 2;
      default:
        throw new ParsingException("Unknown operation " + operation);
    }
  }
}
