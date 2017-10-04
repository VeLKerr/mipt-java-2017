package ru.mipt.java2017.homework.g695.kalinochkin.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.base.task1.Calculator;

import java.util.ArrayList;
import java.util.Stack;

class MyCalculator implements Calculator {

  /**
   * Evaluates an expression.
   *
   * @param expression the expression to evaluate
   * @return the value of the expression
   * @throws ParsingException if the expression is invalid
   */
  public double calculate(String expression) throws ParsingException {
    if (expression == null || expression.length() == 0) {
      throw new ParsingException("Empty expression");
    }
    // tokens in postfix notation
    ArrayList<Token> tokens = reorder(tokenize(expression));
    Stack<Double> st = new Stack<>();
    for (Token t : tokens) {
      if (t.getType() == TokenType.NUMBER) {
        try {
          st.push(Double.parseDouble(t.getValue()));
        } catch (NumberFormatException e) {
          throw new ParsingException("Invalid number: " + t.getValue());
        }
      } else if (t.getType() == TokenType.UNARY_OP) {
        if (st.empty()) {
          throw new ParsingException("Not enough operands");
        }
        st.push(evalUnary(t.getValue(), st.pop()));
      } else if (t.getType() == TokenType.BINARY_OP) {
        if (st.size() < 2) {
          throw new ParsingException("Not enough operands");
        }
        double b = st.pop();
        double a = st.pop();
        st.push(evalBinary(t.getValue(), a, b));
      }
    }
    if (st.size() != 1) {
      throw new ParsingException("Invalid expression");
    }
    return st.pop();
  }

  /**
   * Returns the precedence of the operation. Higher value means higher priority.
   *
   * @param t token with the operation
   * @return precedence of the operation
   */
  protected int getPrecedence(Token t) {
    if (t.getType() == TokenType.UNARY_OP) {
      return 3;
    }
    if (t.getValue().equals("+") || t.getValue().equals("-")) {
      return 1;
    }
    return 2;
  }

  /**
   * Evaluates the unary operation.
   *
   * @param op operation to evaluate
   * @param a  argument of the operation
   * @return the result of evaluation
   */
  protected double evalUnary(String op, double a) {
    if (op.equals("+")) {
      return a;
    }
    if (op.equals("-")) {
      return -a;
    }
    return 0;
  }

  /**
   * Evaluates the binary operation.
   *
   * @param op operation to evaluate
   * @param a  first argumrnt of the operation
   * @param b  second argument of the operation
   * @return the result of evaluation
   */
  protected double evalBinary(String op, double a, double b) {
    if (op.equals("+")) {
      return a + b;
    }
    if (op.equals("-")) {
      return a - b;
    }
    if (op.equals("*")) {
      return a * b;
    }
    if (op.equals("/")) {
      return a / b;
    }
    return 0;
  }


  /**
   * Creates a token by the operation symbol
   *
   * @param c    operation symbol
   * @param last type of the previous token
   * @return operation token
   * @throws ParsingException if the operation is not allowed in this context
   */
  protected Token createOperation(char c, TokenType last) throws ParsingException {
    if (last == TokenType.LEFTPAR || last == TokenType.BINARY_OP) {
      if (c != '+' && c != '-') {
        throw new ParsingException("Invalid unary operation: " + c);
      }
      return new Token(TokenType.UNARY_OP, "" + c);
    } else if (last == TokenType.UNARY_OP) {
      throw new ParsingException("Multiple unary operations are not allowed");
    } else {
      return new Token(TokenType.BINARY_OP, "" + c);
    }
  }

  /**
   * Ensures that the condition is false. Works like assert but throws {@code ParsingException}
   *
   * @param condition a condition to check
   * @param message   exception message
   * @throws ParsingException if the condition is true
   */
  private void ensureFalse(boolean condition, String message) throws ParsingException {
    if (condition) {
      throw new ParsingException(message);
    }
  }

  /**
   * Converts the expression into a list of tokens.
   *
   * @param expression the expression to convert
   * @return list of tokens
   * @throws ParsingException if the expression could not be tokenized
   */
  private ArrayList<Token> tokenize(String expression) throws ParsingException {
    ArrayList<Token> tokens = new ArrayList<>();
    String number = ""; // accumulates current numeric literal
    TokenType last = TokenType.LEFTPAR; // the previous token
    int pars = 0; // number of unclosed parentheses
    for (char c : (expression + ' ').toCharArray()) {
      if (Character.isDigit(c) || c == '.') {
        ensureFalse(last == TokenType.NUMBER, "Operation expected, number found");
        number += c;
      } else {
        if (!number.isEmpty()) {
          tokens.add(new Token(TokenType.NUMBER, number));
          last = TokenType.NUMBER;
          number = "";
        }
        if (c == '+' || c == '-' || c == '*' || c == '/') {
          Token t = createOperation(c, last);
          tokens.add(t);
          last = t.getType();
        } else if (c == '(') {
          ensureFalse(last == TokenType.NUMBER || last == TokenType.RIGHTPAR, "Operation "
              + "expected, '(' found");
          tokens.add(new Token(TokenType.LEFTPAR, "("));
          last = TokenType.LEFTPAR;
          pars++;
        } else if (c == ')') {
          ensureFalse(last == TokenType.LEFTPAR || last == TokenType.BINARY_OP ||
              last == TokenType.UNARY_OP, "Number expected, ')' found");
          ensureFalse(pars <= 0, "Unpaired parentheses");
          tokens.add(new Token(TokenType.RIGHTPAR, ")"));
          last = TokenType.RIGHTPAR;
          pars--;
        } else {
          ensureFalse(!Character.isWhitespace(c), "Invalid character: " + c);
        }
      }
    }
    ensureFalse(pars != 0, "Unpaired parentheses");
    return tokens;
  }

  /**
   * Converts the list of tokens in natural order into postfix notation.
   *
   * @param tokens the list of tokens
   * @return the list of tokens in postfix notation
   */
  private ArrayList<Token> reorder(ArrayList<Token> tokens) {
    ArrayList<Token> result = new ArrayList<>();
    Stack<Token> stack = new Stack<>();
    for (Token t : tokens) {
      switch (t.getType()) {
        case NUMBER:
          stack.push(t);
          break;
        case UNARY_OP:
        case BINARY_OP:
          while (!stack.empty() && stack.peek().getType() != TokenType.LEFTPAR &&
                 getPrecedence(stack.peek()) >= getPrecedence(t)) {
            result.add(stack.pop());
          }
          // fallthrough
        case LEFTPAR:
          stack.push(t);
          break;
        case RIGHTPAR:
          while (!stack.empty() && stack.peek().getType() != TokenType.LEFTPAR) {
            result.add(stack.pop());
          }
          stack.pop();
        default:
      }
    }
    while (!stack.empty()) {
      result.add(stack.pop());
    }
    return result;
  }
}
