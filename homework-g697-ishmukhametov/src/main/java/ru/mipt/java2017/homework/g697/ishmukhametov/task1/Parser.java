package ru.mipt.java2017.homework.g697.ishmukhametov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.Stack;

public class Parser {
  private String rebuildedExpression = "";
  static final String NUMBERS = "^[0-9.]";

  String getRez() {
    return rebuildedExpression;
  }

  public void rebuild(String expression) throws ParsingException {

    Integer pos;
    Character currentPrev;
    Character prevAtRebuilded;

    if (expression == null) {
      throw new ParsingException("Empty expression", new NullPointerException());
    }

    expression = expression.replace(" ", "")
    .replace("\n", "")
    .replace("\t", "");
    expression = '#' + expression + '#';

    Stack<Character> temp = new Stack<>();
    temp.add('#');
    pos = 1;
    currentPrev = '#';
    prevAtRebuilded = currentPrev;

    while (!temp.isEmpty()) {
      char symbol = expression.charAt(pos);

      if ("*/".indexOf(symbol) != -1) {
        fstPriorityHandler(symbol, pos, currentPrev, prevAtRebuilded,  temp);
        continue;
      }
      if ("+-".indexOf(symbol) != -1) {
        sndPriorityHandler(symbol, pos, currentPrev, prevAtRebuilded, temp);
        continue;
      }
      if (symbol == '(') {
        temp.add(symbol);
        currentPrev = symbol;
        pos++;
        continue;
      }
      if (symbol == ')') {
        closeBrace(pos, currentPrev, prevAtRebuilded, temp);
        continue;
      }
      if (symbol == '#') {
        endSymbol(pos, currentPrev, prevAtRebuilded, temp);
        continue;
      }
      if (NUMBERS.indexOf(symbol) != -1) {
        rebuildedExpression += symbol;
        prevAtRebuilded = symbol;
        currentPrev = symbol;
        pos++;
      } else {
        throw new ParsingException("Invalid symbol");
      }
    }
    if (rebuildedExpression.equals("")) {
      throw new ParsingException("Empty expression");
    }
    String tmp = "";
    for (int i = 0; i < rebuildedExpression.length(); i++) {
      if ("+-".indexOf(rebuildedExpression.charAt(i)) == -1) {
        while (temp.size() != 1 && !temp.isEmpty()) {
          char a = temp.pop();
          char b = temp.pop();
          if (a == b) {
            temp.add('+');
          } else {
            temp.add('-');
          }
        }
        if (temp.size() == 1) {
          tmp += temp.pop();
        }
        tmp += rebuildedExpression.charAt(i);
      } else {
        temp.add(rebuildedExpression.charAt(i));
      }
    }
    rebuildedExpression = tmp;
  }

  private void fstPriorityHandler(char symbol, Integer pos, Character currentPrev,
                                  Character prevAtRebuilded, Stack<Character> temp) throws ParsingException {
    if ("(+-/*#".indexOf(currentPrev) != -1) {
      throw new ParsingException("Operators");
    }
    if (NUMBERS.indexOf(prevAtRebuilded) != -1) {
      rebuildedExpression += " ";
      prevAtRebuilded = ' ';
    }
    if (temp.peek() == '*' || temp.peek() == '/') {
      prevAtRebuilded = ' ';
      rebuildedExpression += temp.pop() + " ";
    } else {
      temp.add(symbol);
      currentPrev = symbol;
      pos++;
    }
  }

  private void sndPriorityHandler(char symbol, Integer pos, Character currentPrev,
                                  Character prevAtRebuilded, Stack<Character> temp) throws ParsingException {
    if ("+-*/".indexOf(currentPrev) != -1) {
      throw new ParsingException("Operators");
    }
    if (NUMBERS.indexOf(prevAtRebuilded) != -1) {
      rebuildedExpression += " ";
      prevAtRebuilded = ' ';
    }
    if ("(#".indexOf(currentPrev) != -1) {
      currentPrev = symbol;
      prevAtRebuilded = symbol;
      rebuildedExpression += symbol;
      pos++;
      return;
    }
    if (temp.peek() == '#' || temp.peek() == '(') {
      temp.add(symbol);
      currentPrev = symbol;
      pos++;
    } else {
      prevAtRebuilded = ' ';
      rebuildedExpression += temp.pop() + " ";
    }
  }

  private void closeBrace(Integer pos, Character currentPrev,
                          Character prevAtRebuilded, Stack<Character> temp) throws ParsingException {
    if (currentPrev == '(') {
      throw new ParsingException("Empty braces");
    }
    if (temp.peek() == '#') {
      throw new ParsingException("Wrong number of braces");
    }
    if (temp.peek() == '(') {
      currentPrev = ')';
      pos++;
      temp.pop();
    } else {
      if (NUMBERS.indexOf(prevAtRebuilded) != -1) {
        rebuildedExpression = rebuildedExpression.concat(" ");
      }
      prevAtRebuilded = ' ';
      rebuildedExpression = rebuildedExpression.concat(temp.pop() + " ");
    }
  }

  private void endSymbol(Integer pos, Character currentPrev,
                         Character prevAtRebuilded, Stack<Character> temp) throws ParsingException {
    if (temp.peek() == '(') {
      throw new ParsingException("Wrong number of braces");
    }
    if (temp.peek() == '#') {
      temp.pop();
    } else {
      if (NUMBERS.indexOf(prevAtRebuilded) != -1) {
        rebuildedExpression = rebuildedExpression.concat(" ");
      }
      prevAtRebuilded = ' ';
      rebuildedExpression = rebuildedExpression.concat(temp.pop() + " ");
    }
  }
}