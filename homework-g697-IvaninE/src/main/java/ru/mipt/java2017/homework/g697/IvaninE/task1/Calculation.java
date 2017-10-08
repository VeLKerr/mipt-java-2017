package ru.mipt.java2017.homework.g697.IvaninE.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

public class Calculation {

  private TokenStream tokenStream = new TokenStream();

  public double evaluate(String expression) throws ParsingException {
    tokenStream.setParsingExpression(expression);
    checkBracesBalance(expression);
    double value = expressionRule();
    return value;
  }

  private static void checkBracesBalance(String expression) throws ParsingException {
    Stack<Character> openBraces = new Stack<>();
    for (int i = 0; i < expression.length(); i++) {
      if (expression.charAt(i) == '(') {
        openBraces.push(expression.charAt(i));
      } else if (expression.charAt(i) == ')') {
        if (openBraces.empty()) {
          throw new ParsingException("ERROR: There is too few braces");
        }
        openBraces.pop();
      }
    }
    if (!openBraces.empty()) {
      throw new ParsingException("ERROR: There is too much braces");
    }
  }

  // Случай уножения и деления
  private double termRule() throws ParsingException {
    double value = primaryExprRule();
    Token curToken = tokenStream.getCurrentToken();
    while (true) {
      switch (curToken.symbol()) {
        case '*':
          value *= primaryExprRule();
          curToken = tokenStream.getCurrentToken();
          break;
        case '/':
          value /= primaryExprRule();
          curToken = tokenStream.getCurrentToken();
          break;
        default:
          tokenStream.putBackToStream(curToken);
          return value;
      }
    }
  }

  // Случай чисел и скобок
  private double primaryExprRule() throws ParsingException {
    Token curToken = tokenStream.getCurrentToken();
    switch (curToken.symbol()) {
      case 'x':
        return curToken.value();

      case '-':
        return -primaryExprRule();

      case '(':
        double value = expressionRule();
        curToken = tokenStream.getCurrentToken();
        if (curToken.symbol() != ')') {
          throw new ParsingException("ERROR: bad parentheses balance");
        }
        return value;
      default:
        throw new ParsingException("ERROR: parenthesis is empty ");
    }
  }

  // Случай плюса и минуса
  private double expressionRule() throws ParsingException {
    double value = termRule();
    Token curToken = tokenStream.getCurrentToken();
    while (true) {
      switch (curToken.symbol()) {
        case '-':
          value -= termRule();
          curToken = tokenStream.getCurrentToken();
          break;
        case '+':
          value += termRule();
          curToken = tokenStream.getCurrentToken();
          break;
        default:
          tokenStream.putBackToStream(curToken);
          return value;
      }
    }
  }
}
