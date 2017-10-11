package ru.mipt.java2017.homework.g695.polyanskiy.task1;

import ru.mipt.java2017.homework.base.task1.*;


import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

import static ru.mipt.java2017.homework.g695.polyanskiy.task1.MyParser.parsingSpace;


/**
 * Created by maxim on 06.10.17.
 */

public class MyCalculator implements Calculator {

  public static boolean isOperation(char c) {
    return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
  }

  private int getPriority(Token operation) {
    if (operation.getType() == TokenEnums.Tokens.UNARY) {
      return 4;
    }
    if (Objects.equals(operation.getValue(), "+")  ||  Objects.equals(operation.getValue(), "-")) {
      return 1;
    }
    if (Objects.equals(operation.getValue(), "*")  ||  Objects.equals(operation.getValue(), "/")) {
      return 2;
    }
    return -1;
  }

  private void processOperation(Stack<Double> st, Token operation) throws ParsingException {
    if (operation.getType() == TokenEnums.Tokens.UNARY) {
      Double l = st.peek();
      st.pop();
      if (Objects.equals(operation.getValue(), "+")) {
        st.push(l);
      }
      if (Objects.equals(operation.getValue(), "-")) {
        st.push(-l);
      }
    } else {
      try {
        Double r = st.peek();
        st.pop();
        Double l = st.peek();
        st.pop();
        switch (operation.getValue()) {
          case "+":
            st.push(l + r);
            break;
          case "-":
            st.push(l - r);
            break;
          case "*":
            st.push(l * r);
            break;
          case "/":
            st.push(l / r);
            break;
          default:
            throw new ParsingException("bad operations");
        }
      } catch (Exception e) {
        throw new ParsingException("bad operations");
      }
    }
  }

  public double calculate(String expression) throws ParsingException {
    ArrayList<Token> tokens = parsingSpace(expression);
    Stack<Double> stackVar = new Stack<>();
    Stack<Token> op = new Stack<>();
    for (int i = 0; i < tokens.size(); ++i) {
      if (tokens.get(i).getType() == TokenEnums.Tokens.LEFTBR) {
        op.push(tokens.get(i));
      } else if (tokens.get(i).getType() == TokenEnums.Tokens.RIGHTBR) {
        if (op.isEmpty()) {
          throw new ParsingException("no operations  ");
        }
        while (op.peek().getType() != TokenEnums.Tokens.LEFTBR) {
          processOperation(stackVar, op.peek());
          op.pop();
        }
        op.pop();
      } else if (tokens.get(i).getType() == TokenEnums.Tokens.BINARY ||
          tokens.get(i).getType() == TokenEnums.Tokens.UNARY) {
        Token curOperation = tokens.get(i);
        while (!op.empty() && getPriority(op.peek()) >= getPriority(curOperation)) {
          processOperation(stackVar, op.peek());
          op.pop();
        }
        op.push(curOperation);
      } else {
        Double curVariable = Double.parseDouble(tokens.get(i).getValue());
        stackVar.push(curVariable);
      }
    }
    while (!op.isEmpty()) {
      processOperation(stackVar, op.peek());
      op.pop();
    }
    return stackVar.peek();
  }
}
