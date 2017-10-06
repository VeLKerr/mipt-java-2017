package ru.mipt.java2017.homework.g697.kalamina;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Vector;
import java.util.Stack;

/**
 * Created by Elvira Kalamina
 * on 06/10/2017.
 */
public class MyCalculator implements Calculator {
  private Stack<Double> numbers = new Stack<>();
  private Vector<Character> operations = new Vector<>();

  @Override
  public double calculate(String str) throws ParsingException {
    if (NullExp(str)) {
      throw new ParsingException("Expression is null!");
    }
    str = RemoveSpaces(str);
    if (Empty(str)) {
      throw new ParsingException("Expression consists only spaces.");
    } else if (WrongBalance(str)) {
      throw new ParsingException("Wrong bracket balance.");
    } else if (InvalidSymbols(str)) {
      throw new ParsingException("There are some invalid symbols in expression.");
    } else if (IncorrectNum(str)) {
      throw new ParsingException("Expression contains incorrect numbers.");
    }
    infTopost(str);
    return docalc();
  }

  private String RemoveSpaces(String str) {
    str = str.replaceAll("\\s+", "");
    return str;
  }

  private boolean NullExp(String str) {
    return str == null;
  }

  private boolean Empty(String str) {
    return str.length() == 0 || str == "()";
  }

  private boolean WrongBalance(String str) {
    int count = 0;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == '(') {
        count++;
      } else if (str.charAt(i) == ')') {
        count--;
      }
      if (count < 0) {
        return true;
      }
    }
    return count != 0;
  }

  private boolean InvalidSymbols(String str) {
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (Operator(ch) || Character.isDigit(ch) || ch == '.' || Delimiter(ch) || Bracket(ch)) {
        continue;
      }
      return true;
    }
    return false;
  }

  private boolean IncorrectNum(String str) {
    boolean hasdot = false;
    String word = "";
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (Character.isDigit(ch)) {
        word += Character.toString(ch);
      } else if (ch == '.') {
        word += Character.toString(ch);
        if (hasdot) {
          return true;
        }
        hasdot = true;
      } else {
        if (word != "" && word.charAt(word.length() - 1) == '.') {
          return true;
        }
        word = "";
        hasdot = false;
      }
    }
    return false;
  }

  private boolean Bracket(char ch) {
    return ch == '(' || ch == ')';
  }


  private boolean Delimiter(char ch) {
    return ch == ' ';
  }

  private boolean Operator(char ch) {
    return ch == '*' || ch == '/' || ch == '+' || ch == '-';
  }

  private int Priority(char ch) {
    switch (ch) {
      case '+':
        return 1;
      case '-':
        return 1;
      case '*':
        return 2;
      case '/':
        return 2;
      case 'u':
        return 3;
      default:
        return -1;
    }
  }

  private double Calc(double ln, char ch, double rn) throws ParsingException {
    switch (ch) {
      case '+':
        return ln + rn;
      case '-':
        return ln - rn;
      case '*':
        return ln * rn;
      case '/':
        return ln / rn;
      default:
        throw new ParsingException("Invalid operation symbol.");
    }
  }


  private void Stackcalc() throws ParsingException {
    char operation = operations.lastElement();
    double r = numbers.lastElement();
    numbers.pop();
    if (operation == 'u') {
      numbers.add(-r);
      return;
    }
    double l = numbers.lastElement();
    numbers.pop();
    numbers.push(Calc(l, operation, r));
  }


  private void infTopost(String str) throws ParsingException {
    boolean unar = true;
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (Delimiter(ch)) {
        continue;
      }
      if (ch == '(') {
        operations.add('(');
        unar = true;
        continue;
      }
      if (ch == ')') {
        while (operations.lastElement() != '(') {
          Stackcalc();
          operations.removeElementAt(operations.size() - 1);
        }
        operations.removeElementAt(operations.size() - 1);
        unar = false;
        continue;
      }
      if (Operator(ch)) {
        if (unar) {
          if (ch == '*' || ch == '/' || ch == '+') {
            throw new ParsingException("Invalid unary operator.");
          }
          ch = 'u';
        }
        while (!operations.isEmpty() && (ch != 'u' && Priority(operations.lastElement())
          >= Priority(ch) || ch == 'u' && Priority(operations.lastElement()) > Priority(ch))) {
          Stackcalc();
          operations.removeElementAt(operations.size() - 1);
        }
        operations.add(ch);
        unar = true;
        continue;
      }

      String word = "";
      while (i < str.length() && (Character.isDigit(str.charAt(i))
        || str.charAt(i) == '.')) {
        word += Character.toString(str.charAt(i));
        i++;
      }
      numbers.add(Double.parseDouble(word));
      unar = false;
      i--;
    }
  }

  private double docalc() throws ParsingException {
    while (!operations.isEmpty()) {
      Stackcalc();
      operations.removeElementAt(operations.size() - 1);
    }
    return numbers.lastElement();
  }
}