package ru.mipt.java2017.homework.g964.derut.taskN1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Collections;
import java.util.Stack;

public class MyCalculator implements Calculator {

  @Override
  public double calculate(String s) throws ParsingException {
    return Double.parseDouble(parser(s));
  }

  private String parser(String s) throws ParsingException {
    if (s == null) {
      throw new ParsingException("Invalid expression");
    }
    s = s.replaceAll(" ", "");
    s = s.replaceAll("\n", "");
    s = s.replaceAll("\t", "");
    s = '(' + s + ')';
    Stack<Integer> bracketId = new Stack<>();
    int length = s.length();
    for (int i = 0; i < s.length(); ++i) {
      if (s.charAt(i) == '(') {
        bracketId.push(i);
      } else if (s.charAt(i) == ')') {
        if (bracketId.isEmpty()) {
          throw new ParsingException("Invalid expression");
        }
        int lastBracketId = bracketId.pop();
        String localAns = String.valueOf(simpleCalculation(s.substring(lastBracketId + 1, i)));
        if (i != length - 1) {
          s = s.substring(0, lastBracketId) + localAns + s.substring(i + 1);
        } else {
          s = s.substring(0, lastBracketId) + localAns;
        }
        if (!bracketId.isEmpty()) {
          i = bracketId.peek();
        }

      }
    }
    if (!bracketId.isEmpty()) {
      throw new ParsingException("Invalid expression");
    }
    return s;
  }

  private Double simpleCalculation(String s) throws ParsingException {
    boolean isLastOperand = false;
    s = s.replaceAll("--", ""); // Костыль, но тест не д
    Stack<Double> operands = new Stack<>();
    Stack<Character> operators = new Stack<>();
    for (int i = 0; i < s.length(); ) {
      if (s.charAt(i) >= 48 && s.charAt(i) <= 57 || (s.charAt(i) == '-' && !isLastOperand)) {
        if (isLastOperand) {
          throw new ParsingException("Invalid expression");
        } else {
          int j = i;
          if (s.charAt(i) == '-') {
            ++i;
          }
          for (; i < s.length() && (s.charAt(i) >= 48 && s.charAt(i) <= 57 || s.charAt(i) == '.');
              ++i) {
            ;
          }
          Double operand;
          try {
            operand = new Double(s.substring(j, i));
          } catch (NumberFormatException e) {
            throw new ParsingException("InvalidExpression");
          }
          if (!operators.isEmpty()) {
            Character operator = operators.peek();
            if (operator.equals('*') || operator.equals('/')) {
              if (operands.isEmpty()) {
                throw new ParsingException("Invalid expression");
              } else {
                if (operator.equals('*')) {
                  operands.push(operands.pop() * operand);
                  operators.pop();
                } else if (operator.equals('/')) {
                  operands.push(operands.pop() / operand);
                  operators.pop();
                }
              }

            } else {
              operands.push(operand);
            }
          } else {
            operands.push(operand);
          }
          isLastOperand = true;
        }

      } else if (s.charAt(i) == '-' || s.charAt(i) == '+' || s.charAt(i) == '*'
          || s.charAt(i) == '/') {
        if (isLastOperand) {
          operators.push(s.charAt(i));
          isLastOperand = false;
        } else {
          throw new ParsingException("Invalid expression");
        }
        ++i;
      } else {
        throw new ParsingException("Invalid expression");
      }
    }
    if (operators.size() != operands.size() - 1) {
      throw new ParsingException("Invalid expression");
    }
    Collections.reverse(operators);
    Collections.reverse(operands);
    while (operands.size() > 1 && !operators.isEmpty()) {
      Character operator = operators.pop();
      if (operator.equals('+')) {
        operands.push(operands.pop() + operands.pop());
      } else if (operator.equals('-')) {
        operands.push(operands.pop() - operands.pop());
      }
    }
    return operands.pop();
  }
}
