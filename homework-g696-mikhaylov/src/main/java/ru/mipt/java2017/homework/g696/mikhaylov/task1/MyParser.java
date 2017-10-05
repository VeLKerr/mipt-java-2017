package ru.mipt.java2017.homework.g696.mikhaylov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;
import java.util.Stack;

class MyParser {

  private String expression;

  ArrayList<Lexeme> parse(String exp) throws ParsingException {
    TrivialChecker checker = new TrivialChecker();
    expression = checker.check(exp);
    ArrayList<Lexeme> result = reverseNotation(parseToLexems());
    if (result.isEmpty()) {
      throw new ParsingException("Empty expression");
    }
    return result;
  }

  private Lexeme createOperation(char c, LexemeType lastLexeme) throws ParsingException {
    if (lastLexeme == LexemeType.LEFT_BRACKET || lastLexeme == LexemeType.BINARY_OPERATION) {
      if (c != '+' && c != '-') {
        throw new ParsingException("Invalid unary operation: " + c);
      }
      return new Lexeme("" + c, LexemeType.UNARY_OPERATION);
    } else if (lastLexeme == LexemeType.UNARY_OPERATION) {
      throw new ParsingException("Multiple unary operations are not allowed");
    } else {
      return new Lexeme("" + c, LexemeType.BINARY_OPERATION);
    }
  }

  private ArrayList<Lexeme> parseToLexems() throws ParsingException {
    ArrayList<Lexeme> tokens = new ArrayList<>();
    StringBuilder number = new StringBuilder();
    LexemeType last = LexemeType.LEFT_BRACKET;
    for (char c : (expression + ' ').toCharArray()) {
      if (Character.isDigit(c) || c == '.') {
        number.append(c);
      } else {
        if (number.length() > 0) {
          tokens.add(new Lexeme(number.toString(), LexemeType.NUMBER));
          last = LexemeType.NUMBER;
          number = new StringBuilder();
        }
        if (c == '+' || c == '-' || c == '*' || c == '/') {
          Lexeme t = createOperation(c, last);
          tokens.add(t);
          last = t.getType();
        } else if (c == '(') {
          tokens.add(new Lexeme("(", LexemeType.LEFT_BRACKET));
          last = LexemeType.LEFT_BRACKET;
        } else if (c == ')') {
          tokens.add(new Lexeme(")", LexemeType.RIGHT_BRACKET));
          last = LexemeType.RIGHT_BRACKET;
        }
      }
    }
    return tokens;
  }

  private ArrayList<Lexeme> reverseNotation(ArrayList<Lexeme> tokens) throws ParsingException {
    ArrayList<Lexeme> result = new ArrayList<>();
    Stack<Lexeme> stack = new Stack<>();
    for (Lexeme t : tokens) {
      switch (t.getType()) {
        case NUMBER:
          result.add(t);
          break;
        case UNARY_OPERATION:
        case BINARY_OPERATION:
          while (!stack.empty() &&
            stack.peek().getType() != LexemeType.LEFT_BRACKET &&
            stack.peek().getPriority() >= t.getPriority()) {
            result.add(stack.pop());
          }
        case LEFT_BRACKET:
          stack.push(t);
          break;
        case RIGHT_BRACKET:
          while (!stack.empty() && LexemeType.LEFT_BRACKET != stack.peek().getType()) {
            result.add(stack.pop());
          }
          stack.pop();
        default:
          break;
      }
    }
    while (!stack.empty()) {
      result.add(stack.pop());
    }
    return result;
  }

}
