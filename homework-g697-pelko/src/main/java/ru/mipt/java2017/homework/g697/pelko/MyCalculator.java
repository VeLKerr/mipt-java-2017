package ru.mipt.java2017.homework.g697.pelko;
/**
 * Created by Pelko Andrew
 * MIPT DIHT student
 * on 06.10.2017
 */

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import static java.lang.Character.isDigit;
import java.util.Stack;

public class MyCalculator implements Calculator {

  private Stack<Double> numbers = new Stack<>();
  private Stack<Character> func = new Stack<>();

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null expression");
    }
    return answer(expression);
  }

  private int priority(char operation) throws ParsingException {
    switch (operation) {
      case '+':
        return 2;
      case '-':
        return 2;
      case '*':
        return 3;
      case '/':
        return 3;
      case '(':
        return 1;
      default:
        throw new ParsingException("Wrong operator in priority");
    }
  }

  private double docalculation(Double x, char operation, Double y) throws ParsingException {
    switch (operation) {
      case '+':
        return y + x;
      case '-':
        return y - x;
      case '*':
        return y * x;
      case '/':
        return y / x;
      default:
        throw new ParsingException("Incorrect operation symbol");
    }
  }

  private boolean characterproccessing(char currentsymbol) throws ParsingException {
    if (currentsymbol == ')') {
      if (numbers.size() == 0) {
        throw new ParsingException("Not enough operands!");
      }
      while (func.peek() != '(') {
        if (numbers.size() >= 2) {
          numbers.push(docalculation(numbers.pop(), func.pop(), numbers.pop()));
        } else {
          throw new ParsingException("Not enough operands!");
        }
      }
      func.pop();
      return true;
    } else if (currentsymbol == '(' || priority(currentsymbol) > priority(func.peek())) {
      func.push(currentsymbol);
      return true;
    } else if (priority(currentsymbol) <= priority(func.peek())) {    //выталкивание
      if (numbers.size() < 2) {
        throw new ParsingException("Not enough operands!");
      }
      numbers.push(docalculation(numbers.pop(), func.pop(), numbers.pop()));
      func.push(currentsymbol);
      char top;
      top = func.pop();
      while (priority(top) <= priority(func.peek()) && numbers.size() > 1) {
        numbers.push(docalculation(numbers.pop(), func.pop(), numbers.pop()));
      }
      func.push(top);
      return true;
    }
    return false;
  }

  private boolean isfunc(char c) {
    return (c == '-' || c == '+' || c == '/' || c == '*' || c == '(');
  }

  private int makenum(int begin, String s, int sign) throws ParsingException {
    int currentpos = begin;
    int count = 0;
    currentpos++;
    while (s.charAt(currentpos) == '.' || isDigit(s.charAt(currentpos))) {
      if (s.charAt(currentpos) == '.') {
        if (++count > 1) {
          throw new ParsingException("Too much dots!");
        }
      }
      currentpos++;
    }
    if (sign == -1 && Double.parseDouble(s.substring(begin, currentpos).toString()) == 0) {
      numbers.push(-0.0);
    } else {
      numbers.push(Double.parseDouble(s.substring(begin, currentpos).toString()) * sign);
    }
    return currentpos;
  }

  private double answer(String expression) throws ParsingException {
    func.clear();
    numbers.clear();
    String number = "";
    func.push('(');
    String s = '(' + expression.replaceAll("\\s+", "") + ')';
    int summ = 0;
    int i = 1;
    while (s.length() > i) {
      char symbol;
      if (isfunc(s.charAt(i - 1)) && (s.charAt(i) == '-') && (i + 1 < s.length())) {
        if (s.charAt(i - 1) == '/') {
          symbol = s.charAt(i);
          i++;
          if (symbol == '-') {
            i = makenum(i, s, -1);
          } else {
            i = makenum(i, s, 1);
          }
        } else {
          numbers.push(0.0);
        }
      }
      if (summ < 0) {
        throw new ParsingException("Bracket balance is incorrect!");
      }
      if (s.charAt(i) == '(') {
        summ++;
      }
      if (s.charAt(i) == ')' && i != s.length() - 1) {
        summ--;
      }
      if (isDigit(s.charAt(i))) {
        i = makenum(i, s, 1);
        continue;
      }
      if (characterproccessing(s.charAt(i))) {
        i++;
      }
    }

    if (numbers.size() > 1 || func.size() > 0) {
      throw new ParsingException("There are many braces in the expression!");
    }
    return numbers.pop();
  }
}
