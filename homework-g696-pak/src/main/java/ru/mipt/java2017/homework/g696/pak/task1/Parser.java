package ru.mipt.java2017.homework.g696.pak.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.*;

public class Parser implements ExpressionParser {

  private PrimaryExpressionParser primaryExpressionParser;

  private boolean isSeparationSign(char symbol) {
    return symbol == ' ' || symbol == '\t' || symbol == '\n';
  }

  private boolean isDigit(char symbol) {
    return (symbol >= '0' && symbol <= '9');
  }

  private boolean isBracket(char symbol) {
    return (symbol == ')' || symbol == '(');
  }

  private boolean isOperator(char symbol) {
    return symbol == '+' || symbol == '-' || symbol == '/' || symbol == '*';
  }

  //check if valid except amount of operators and operands
  private boolean checkIfValid(String expression) throws ParsingException {
    int balance = 0;
    int amountOfDots = 0;
    if (expression == null || expression.length() == 0) {
      throw new ParsingException("");
    }
    char symbol = expression.charAt(0);

    if (symbol == '(') {
      ++balance;
    }
    if (symbol == ')') {
      throw new ParsingException("");
    }

    if (!(isBracket(symbol) || isDigit(symbol) || symbol == '+' || symbol == '-' || isSeparationSign(symbol))) {
      throw new ParsingException("");
    }

    for (int iter = 1; iter < expression.length() - 1; ++iter) {
      symbol = expression.charAt(iter);

      if (!(isBracket(symbol) || isDigit(symbol) || isOperator(symbol) || isSeparationSign(symbol)
           || symbol == '.')) {
        throw new ParsingException("");
      }

      if (expression.charAt(iter) == '.') {
        ++amountOfDots;
      }

      if (isOperator(symbol) || isBracket(symbol)) {
        amountOfDots = 0;
      }

      if (amountOfDots > 1) {
        throw new ParsingException("");
      }

      if (symbol == '.' && !(isDigit(expression.charAt(iter + 1)) && isDigit(
           expression.charAt(iter - 1)))) {
        throw new ParsingException("");
      }

      if (symbol == '(') {
        balance += 1;
      }
      if (symbol == ')') {
        balance -= 1;
      }
      if (balance < 0) {
        throw new ParsingException("");
      }

    }

    symbol = expression.charAt(expression.length() - 1);

    if (symbol == ')') {
      --balance;
    }
    if (symbol == '(' || balance != 0) {
      throw new ParsingException("");
    }
    if (!(isBracket(symbol) || isDigit(symbol) || isSeparationSign(symbol))) {
      throw new ParsingException("");
    }

    return true;
  }

  Parser() {
    primaryExpressionParser = new PrimaryExpressionParser();
  }

  @Override
  public Calculable parse(String expression) throws ParsingException {
    if (!checkIfValid(expression)) {
      throw new ParsingException(""); // check if expression is valid
    }
    Stack<String> stack = new Stack<String>();
    LinkedList<ExpressionForCalculation> queue = new LinkedList<ExpressionForCalculation>();
    boolean isUnaryOperationAvailable = true;

    for (int iter = 0; iter < expression.length(); ++iter) { //parsing the expression

      char symbol = expression.charAt(iter);
      if (symbol == '(') {
        stack.push("(");
        isUnaryOperationAvailable = true;
      } else if (symbol == ')') {
        while (!stack.peek().equals("(")) {
          queue.addLast(new Operator(stack.pop()));
        }

        isUnaryOperationAvailable = false;
        stack.pop();

      } else if (symbol == '+' || symbol == '-') {

        if (isUnaryOperationAvailable) {
          if (symbol == '-') {
            stack.push("*");
            queue.addLast(new Constant(-1));
          }
        } else {
          while (!stack.empty() && (stack.peek().equals("+") || stack.peek().equals("-")
            || stack.peek().equals("*") || stack.peek().equals("/"))) {
            queue.addLast(new Operator(stack.pop()));
          }
          stack.push(symbol + "");
        }

        isUnaryOperationAvailable = false;
      } else if (symbol == '*' || symbol == '/') {
        while (!stack.empty() && (stack.peek().equals("*") || stack.peek().equals("/"))) {
          queue.addLast(new Operator(stack.pop()));
        }
        stack.push(symbol + "");
        isUnaryOperationAvailable = true;
      } else {
        while (iter < expression.length() && !(isDigit(symbol) || isBracket(symbol) || isOperator(
          symbol))) {
          ++iter;
          if (iter < expression.length()) {
            symbol = expression.charAt(iter);
          }
        }

        StringBuffer s = new StringBuffer();
        while (iter < expression.length() && symbol >= '0' && symbol <= '9') {
          s.append(symbol);
          ++iter;
          if (iter < expression.length()) {
            symbol = expression.charAt(iter);
          }
        }

        if (iter < expression.length() && symbol == '.') {
          s.append('.');
          ++iter;
          if (iter < expression.length()) {
            symbol = expression.charAt(iter);
          }
          while (iter < expression.length() && symbol >= '0'
            && symbol <= '9') {
            s.append(symbol);
            ++iter;
            if (iter < expression.length()) {
              symbol = expression.charAt(iter);
            }
          }
        }

        --iter;
        if (s.length() > 0) {
          queue.addLast((Constant) primaryExpressionParser.parse(s.toString()));
          isUnaryOperationAvailable = false;
        }
      }
    }
    while (!stack.empty()) {
      queue.addLast(new Operator(stack.pop()));
    }

    return new CalculableExpression(queue);
  }
}
