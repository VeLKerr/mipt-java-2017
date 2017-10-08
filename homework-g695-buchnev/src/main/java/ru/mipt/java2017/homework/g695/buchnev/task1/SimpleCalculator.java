package ru.mipt.java2017.homework.g695.buchnev.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.*;

public class SimpleCalculator implements Calculator {

  private String[] tokens;
  private double[] numbers;
  private int countnumbers;
  private Stack stackoperations;
  private Stack stacknumbers;

  SimpleCalculator() {
    stackoperations = new Stack();
    stacknumbers = new Stack();
  }

  @Override
  public double calculate(String rawExpression) throws ParsingException {
    if (rawExpression == null) {
      throw new ParsingException("Null pointer!");
    }

    rawExpression = rawExpression.replaceAll("\n", "");
    rawExpression = rawExpression.replaceAll("\t", "");
    rawExpression = rawExpression.replaceAll(" ", "");
    int length = 0;
    char[] e = new char[rawExpression.length() - spacesCount(rawExpression)];
    for (int i = 0; i < rawExpression.length(); ++i) {
      if (rawExpression.charAt(i) != ' ') {
        e[length++] = rawExpression.charAt(i);
      }
    }
    String expression = String.valueOf(e);
    String message = checkExpression(expression);

    if (!message.equals("Ok")) {
      throw new ParsingException(message);
    }

    tokens = expression.split("[\\+, \\-, \\*, \\/, \\(, \\)]");
    numbers = new double[tokens.length];
    countnumbers = 0;
    for (int i = 0; i < tokens.length; ++i) {
      if (tokens[i].length() > 0) {
        try {
          numbers[countnumbers++] = Double.parseDouble(tokens[i]);
        } catch (NumberFormatException exc) {
          throw new ParsingException("Can't parse to double " + tokens[i] + "!", exc.getCause());
        }
      }
    }

    int curnumber = 0;
    boolean isunary = true;
    for (int i = 0; i < length; ++i) {
      char curoperation = expression.charAt(i);
      if (curoperation == '(') {
        stackoperations.push(curoperation);
        isunary = true;
      } else if (curoperation == ')') {
        while (!stackoperations.peek().equals('(')) {
          process((Character) stackoperations.pop());
        }
        stackoperations.pop();
        isunary = false;
      } else if (isOperation(curoperation)) {
        boolean iscurunary = false;
        if (isunary) {
          if (curoperation != '-' && curoperation != '+') {
            throw new ParsingException("Bad operation!");
          }
          iscurunary = true;
          curoperation = (curoperation == '-' ? '^' : '#');
        }

        while (!stackoperations.empty() &&
          (!iscurunary && priority((Character) stackoperations.peek()) >= priority(curoperation) ||
            iscurunary && priority((Character) stackoperations.peek()) > priority(curoperation))) {
          process((Character) stackoperations.pop());
        }
        stackoperations.push(curoperation);
        isunary = (curoperation == '/' ? true : false);
      } else {
        if (curnumber == numbers.length) {
          throw new ParsingException("Bad expression!");
        }

        stacknumbers.push(numbers[curnumber++]);
        while (i < expression.length() && !isOperation(expression.charAt(i))) {
          ++i;
        }
        --i;
        isunary = false;
      }
    }
    while (!stackoperations.empty()) {
      process((Character) stackoperations.pop());
    }
    if (stacknumbers.size() != 1) {
      throw new ParsingException("Bad expression!");
    }
    return (Double) stacknumbers.pop();
  }

  int spacesCount(String expression) {
    int result = 0;
    for (int i = 0; i < expression.length(); ++i) {
      if (expression.charAt(i) == ' ' && expression.charAt(i) != '\t' && expression.charAt(i) != '\n') {
        ++result;
      }
    }
    return result;
  }

  private String checkExpression(String expression) {
    if (expression.equals("")) {
      return "Empty expression!";
    }
    if (!expression.matches("[\\d.()+\\s\\-*/]*")) {
      return "Expression contains forbidden symbols!";
    }
    int balance = 0;
    for (int i = 0; i < expression.length(); ++i) {
      if (expression.charAt(i) == '(') {
        ++balance;
      }
      if (expression.charAt(i) == ')') {
        --balance;
      }
      if (balance < 0) {
        return "Brackets don't form a right bracket sequence!";
      }
    }
    if (balance != 0) {
      return "Brackets don't form a right bracket sequence!";
    }
    return "Ok";
  }

  private int priority(char c) {
    int prior = -1;
    switch (c) {
      case '^':
        prior = 3;
        break;
      case '#':
        prior = 3;
        break;
      case '*':
        prior = 2;
        break;
      case '/':
        prior = 2;
        break;
      case '-':
        prior = 1;
        break;
      case '+':
        prior = 1;
        break;
      default:
        break;
    }
    return prior;
  }

  private boolean isOperation(char c) {
    return c == '-' || c == '+' || c == '*' || c == '/' || c == '^' || c == '#' || c == '('
      || c == ')';
  }

  private void process(char operation) throws ParsingException {
    if (operation == '^' || operation == '#') {
      if (stacknumbers.empty()) {
        throw new ParsingException("Bad stack size!");
      }
      double x = (Double) stacknumbers.pop();
      stacknumbers.push((operation == '^' ? -x : x));
      return;
    }
    if (stacknumbers.size() < 2) {
      throw new ParsingException("Bad stack size!");
    }
    double r = (Double) stacknumbers.pop();
    double l = (Double) stacknumbers.pop();
    switch (operation) {
      case '+':
        stacknumbers.push(l + r);
        break;
      case '-':
        stacknumbers.push(l - r);
        break;
      case '*':
        stacknumbers.push(l * r);
        break;
      case '/':
        stacknumbers.push(l / r);
        break;
      default:
        break;
    }
  }
}