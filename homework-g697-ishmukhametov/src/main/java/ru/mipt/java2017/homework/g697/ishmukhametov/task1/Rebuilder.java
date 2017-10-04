package ru.mipt.java2017.homework.g697.ishmukhametov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.Stack;

/*
*
* Rebuilder of our expression
* Rebuilded expression doesn't have:
*   Unnecessary spaces
*   Braces
*
 */

public class Rebuilder {

  private String rebuildedExpression = "";
  static final String NUMBERS = "0123456789.";
  private Integer pos;
  private Character currentPrev;
  private Character prevAtRebuilded;

  String getRez() {
    return rebuildedExpression;
  }

  public void rebuild(String expression) throws ParsingException {
    /*If expression is null, then we have NullPointerException*/
    if (expression == null) {
      throw new ParsingException("Empty expression", new NullPointerException());
    }

    /* Removing of unnecessary spaces
    and adding start and terminal states
    */
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
      /* Case study */
      if ("*/".indexOf(symbol) != -1) {
        fstPriorityHandler(symbol, temp);
        continue;
      }

      if ("+-".indexOf(symbol) != -1) {
        sndPriorityHandler(symbol, expression.charAt(pos + 1), temp);
        continue;
      }

      if (symbol == '(') {
        temp.add(symbol);
        currentPrev = symbol;
        pos++;
        continue;
      }

      if (symbol == ')') {
        closeBracesHandler(temp);
        continue;
      }

      if (symbol == '#') {
        terminalStateHandler(temp);
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

    /* Check if we have expression which consisted of braces and spaces*/
    if (rebuildedExpression.equals("")) {
      throw new ParsingException("Empty expression");
    }

    /* Else we should remove constractions
      of pluses and minuses before numbers
    */
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

  /* Handler of * and / */
  private void fstPriorityHandler(char symbol, Stack<Character> temp) throws ParsingException {
    /* None of the operators can be before them */
    if ("(+-/*#".indexOf(currentPrev) != -1) {
      throw new ParsingException("Operators");
    }
    /* If previous symbolwa*/
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

  /* Handler of + and -
  Have problems if 5 + + 6 or 5 - - 6.
  Can be solved by changing ++ and -- on **
  */
  private void sndPriorityHandler(char symbol, char nextSymbol,
                                  Stack<Character> temp) throws ParsingException {
    /*Not so easy as * and / because of + - and - + cases */
    if ("+-*/".indexOf(currentPrev) != -1) {
      if (NUMBERS.indexOf(nextSymbol) == -1 &&
          nextSymbol != '(' || currentPrev.equals(symbol)) {
        throw new ParsingException("Operators");
      } else {
        currentPrev = symbol;
        prevAtRebuilded = symbol;
        rebuildedExpression += symbol;
        pos++;
        return;
      }
    }
    if (NUMBERS.indexOf(prevAtRebuilded) != -1) {
      rebuildedExpression += " ";
      prevAtRebuilded = ' ';
    }
    /*If number has minus or plus */
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

  /* Handler of ) */
  private void closeBracesHandler(Stack<Character> temp) throws ParsingException {
    /* If we don't have anything in braces it is unforgivable */
    if (currentPrev == '(') {
      throw new ParsingException("Empty braces");
    }
    /* If we have more close braces when it is error */
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

  /* Terminal state handler */
  private void terminalStateHandler(Stack<Character> temp) throws ParsingException {
    /* If we have more open braces it is error */
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