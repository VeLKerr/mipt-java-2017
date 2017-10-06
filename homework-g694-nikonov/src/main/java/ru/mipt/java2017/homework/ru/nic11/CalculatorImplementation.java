package ru.mipt.java2017.homework.ru.nic11;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;
import java.util.Stack;

public class CalculatorImplementation implements Calculator {
  boolean isOperator(char c) {
    return c == '+' || c == '-' || c == '*' || c == '/';
  }

  ArrayList<Token> tokenize(String s) throws ParsingException {
    ArrayList<Token> tokens = new ArrayList<>();
    StringBuilder num = new StringBuilder();
    int balance = 0;

    tokens.add(new Token(Token.Type.BRACE_OPEN, "("));
    s += " ";

    for (int i = 0; i < s.length(); ++i) {
      char c = s.charAt(i);

      if (Character.isDigit(c) || c == '.') {
        num.append(c);
        continue;
      }

      if (num.length() != 0) {
        tokens.add(new Token(Token.Type.NUMBER, num.toString()));
        num.delete(0, num.length());
      }

      Token prev = tokens.get(tokens.size() - 1);

      if (c == '(') {
        if (prev.type == Token.Type.BRACE_CLOSE || prev.type == Token.Type.NUMBER) {
          throw new ParsingException("Wrong usage of ( near " + i);
        }
        ++balance;

        tokens.add(new Token(Token.Type.BRACE_OPEN, "("));
      } else if (c == ')') {
        if (prev.type == Token.Type.BRACE_OPEN || prev.type == Token.Type.BINARY_OPERATOR
            || prev.type == Token.Type.UNARY_OPERATOR) {
          throw new ParsingException("Wrong usage of ) near " + i);
        }
        if (--balance < 0) {
          throw new ParsingException("Unexpected ) at " + i);
        }

        tokens.add(new Token(Token.Type.BRACE_CLOSE, ")"));
      } else if (isOperator(c)) {
        if (prev.type == Token.Type.BRACE_OPEN || prev.type == Token.Type.BINARY_OPERATOR) {
          if (c == '+' || c == '-') {
            tokens.add(new Token(Token.Type.UNARY_OPERATOR, c + ""));
          } else {
            throw new ParsingException(c + " is an invalid unary operator");
          }
        } else if (prev.type == Token.Type.UNARY_OPERATOR) {
          throw new ParsingException("Unary operator can't go after another one");
        } else {
          tokens.add(new Token(Token.Type.BINARY_OPERATOR, c + ""));
        }
      } else if (!Character.isWhitespace(c)) {
        throw new ParsingException("Unexpected character at " + i);
      }
    }

    if (balance != 0) {
      throw new ParsingException("Unmatched braces");
    }

    tokens.remove(0);
    return tokens;
  }

  int getPriority(Token t) {
    if (t.type == Token.Type.UNARY_OPERATOR) {
      return 3;
    }
    if (t.type == Token.Type.NUMBER || t.value.equals("*") || t.value.equals("/")) {
      return 2;
    }
    return 1;
  }

  ArrayList<Token> toReversePolishNotation(ArrayList<Token> tokens) throws ParsingException {
    ArrayList<Token> ans = new ArrayList<>();
    Stack<Token> s = new Stack<>();
    for (int i = 0; i < tokens.size(); ++i) {
      Token t = tokens.get(i);
      switch (t.type) {
        case NUMBER:
          s.push(t);
          break;
        case BRACE_OPEN:
          s.push(t);
          break;
        case BRACE_CLOSE:
          while (!s.empty() && s.peek().type != Token.Type.BRACE_OPEN) {
            ans.add(s.pop());
          }
          s.pop();
          break;
        case UNARY_OPERATOR:
        case BINARY_OPERATOR:
          while (!s.empty() && s.peek().type != Token.Type.BRACE_OPEN && getPriority(s.peek()) >= getPriority(t)) {
            ans.add(s.pop());
          }
          s.push(t);
          break;
        default:
          throw new ParsingException("Unknown operator type");
      }
    }
    while (!s.empty()) {
      ans.add(s.pop());
    }
    return ans;
  }

  double eval(String op, double a) {
    if (op.equals("-")) {
      return -a;
    }
    return a;
  }

  double eval(double a, String op, double b) throws ParsingException {
    if (op.equals("-")) {
      return a - b;
    }
    if (op.equals("*")) {
      return a * b;
    }
    if (op.equals("/")) {
      return a / b;
    }
    if (op.equals("+")) {
      return a + b;
    }
    throw new ParsingException("Invalid operator");
  }

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("null expression");
    }

    ArrayList<Token> tokens1 = tokenize(expression);
    ArrayList<Token> tokens2 = toReversePolishNotation(tokens1);

    if (tokens2.size() == 0) {
      throw new ParsingException("Empty expression");
    }

    Stack<Double> s = new Stack<>();
    for (Token t : tokens2) {
      switch (t.type) {
        case NUMBER:
          s.push(t.parseAsNumber());
          break;
        case UNARY_OPERATOR:
          s.push(eval(t.value, s.pop()));
          break;
        case BINARY_OPERATOR:
          double b = s.pop();
          double a = s.pop();
          s.push(eval(a, t.value, b));
          break;
        default:
          throw new ParsingException("Unknown");
      }
    }
    if (s.size() != 1) {
      throw new ParsingException("Unknown");
    }
    return s.pop();
  }
}
