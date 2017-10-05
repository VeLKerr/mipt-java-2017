package ru.mipt.java2017.homework.g697.vorobev;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

public class MySimpleCalculator implements Calculator {
  private String text = null;

  @Override
  public double calculate(String str) throws ParsingException {
    if (str != null) {
      text = str;
    } else {
      throw new ParsingException("Null string");
    }

    preprocess();
    StringTokenizer st = new StringTokenizer(text, "+-/*() \t\r\f\n", true);
    Stack<Double> numbers = new Stack<Double>();
    Stack<Character> functions = new Stack<Character>();

    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (iSDouble(token)) {
        numbers.push(token.contentEquals("o") ? (-0.0) : Double.parseDouble(token));
      } else if (isFunc(token) || token.contentEquals("(")) {
        while (canPop(token.charAt(0), functions)) {
          popFunction(functions, numbers);
        }

        functions.push(token.charAt(0));
      } else if (token.contentEquals(")")) {
        try {
          while (functions.peek() != '(') {
            popFunction(functions, numbers);
          }

          functions.pop();
        } catch (EmptyStackException e) {
          throw new ParsingException("Wrong brackets sequence");
        }
      } else if (isSpace(token)) {
      } else {
        throw new ParsingException("Unexpected symbol", new Throwable(token));
      }
    }

    if (!functions.empty() || numbers.size() != 1) {
      throw new ParsingException("Wrong number of operators or operands");
    }

    return numbers.pop();
  }

  private void preprocess() throws ParsingException {
    if (text.indexOf('o') != -1) {
      throw new ParsingException("Unexpected symbol", new Throwable("o"));
    }

    StringBuilder sb = new StringBuilder();
    sb.append("(");
    sb.append(text);
    sb.append(")");
    text = sb.toString();
    text = text.replaceAll("([^\\d.])0+.?0+(\\D)", "$10$2");
    text = text.replaceAll("\\(-\\s*0(\\D)", "(o$1");
    text = text.replaceAll("\\(\\s*-", "(0-");
  }

  boolean isFunc(String token) {
    if (token.contentEquals("+")
      || token.contentEquals("-")
      || token.contentEquals("*")
      || token.contentEquals("/")) {
      return true;
    }

    return false;
  }

  boolean iSDouble(String token) {
    if (token.contentEquals("o")) {
      return true;
    }

    return (token.matches("^\\d+.\\d+$")
      || token.matches("^\\d+$"));
  }

  boolean isSpace(String token) {
    return token.matches("^\\s+$");
  }

  int getPriority(char ch) {
    switch (ch) {
      case '+':
      case '-':
        return 2;
      case '*':
      case '/':
        return 1;
      case '(':
        return -1;
    }

    return 0;
  }

  boolean canPop(char operator, Stack<Character> functions) {
    if (functions.empty()) {
      return false;
    }

    int operatorPriority = getPriority(operator);
    int nextOperatorPriority = getPriority(functions.peek());
    return operatorPriority >= 0 && nextOperatorPriority >= 0 && operatorPriority >= nextOperatorPriority;
  }

  void popFunction(Stack<Character> functions, Stack<Double> numbers) throws ParsingException {
    char op = functions.pop();
    Double leftOperand;
    Double rightOperand;
    try {
      rightOperand = new Double(numbers.pop());
      leftOperand = new Double(numbers.pop());
    } catch (EmptyStackException e) {
      throw new ParsingException("Wrong number of operands or operators");
    }

    switch (op) {
      case '+':
        numbers.push(leftOperand + rightOperand);
        break;
      case '-':
        numbers.push(leftOperand - rightOperand);
        break;
      case '*':
        numbers.push(leftOperand * rightOperand);
        break;
      case '/':
        numbers.push(leftOperand / rightOperand);
    }
  }
}

