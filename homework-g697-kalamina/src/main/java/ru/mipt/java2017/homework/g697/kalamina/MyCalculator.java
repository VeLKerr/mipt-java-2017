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
    if (nullexp(str)) {
      throw new ParsingException("Expression is null!");
    }
    str = removespaces(str);
    if (empty(str)) {
      throw new ParsingException("Expression consists only spaces.");
    } else if (wrongbalance(str)) {
      throw new ParsingException("Wrong bracket balance.");
    } else if (invalidsymbols(str)) {
      throw new ParsingException("There are some invalid symbols in expression.");
    } else if (incorrectnum(str)) {
      throw new ParsingException("Expression contains incorrect numbers.");
    }
    inftopost(str);
    return docalc();
  }

  private String removespaces(String str) {
    str = str.replaceAll("\\s+", "");
    return str;
  }

  private boolean nullexp(String str) {
    return str == null;
  }

  private boolean empty(String str) {
    return str.length() == 0 || str == "()";
  }

  private boolean wrongbalance(String str) {
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

  private boolean invalidsymbols(String str) {
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (operator(ch) || Character.isDigit(ch) || ch == '.' || delimiter(ch) || bracket(ch)) {
        continue;
      }
      return true;
    }
    return false;
  }

  private boolean incorrectnum(String str) {
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

  private boolean bracket(char ch) {
    return ch == '(' || ch == ')';
  }


  private boolean delimiter(char ch) {
    return ch == ' ';
  }

  private boolean operator(char ch) {
    return ch == '*' || ch == '/' || ch == '+' || ch == '-';
  }

  private int priority(char ch) {
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

  private double calc(double ln, char ch, double rn) throws ParsingException {
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


  private void stackcalc() throws ParsingException {
    char operation = operations.lastElement();
    double r = numbers.lastElement();
    numbers.pop();
    if (operation == 'u') {
      numbers.add(-r);
      return;
    }
    double l = numbers.lastElement();
    numbers.pop();
    numbers.push(calc(l, operation, r));
  }


  private void inftopost(String str) throws ParsingException {
    boolean unar = true;
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (delimiter(ch)) {
        continue;
      }
      if (ch == '(') {
        operations.add('(');
        unar = true;
        continue;
      }
      if (ch == ')') {
        while (operations.lastElement() != '(') {
          stackcalc();
          operations.removeElementAt(operations.size() - 1);
        }
        operations.removeElementAt(operations.size() - 1);
        unar = false;
        continue;
      }
      if (operator(ch)) {
        if (unar) {
          if (ch == '*' || ch == '/' || ch == '+') {
            throw new ParsingException("Invalid unary operator.");
          }
          ch = 'u';
        }
        while (!operations.isEmpty() && (ch != 'u' && priority(operations.lastElement())
          >= priority(ch) || ch == 'u' && priority(operations.lastElement()) > priority(ch))) {
          stackcalc();
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
      stackcalc();
      operations.removeElementAt(operations.size() - 1);
    }
    return numbers.lastElement();
  }
}