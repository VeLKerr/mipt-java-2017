package ru.mipt.java2017.homework.g695.dyachenko.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

public class SimpleMathCalculator implements Calculator {
  private ParsedTreeLine buildTree(String expression) throws ParsingException {

    byte sign = 0;
    byte operator = 0;
    byte state = 0;
    // state:
    // 0 - не ждём бинарный
    // 1 - ждём бинарный
    // 2 - внутри числа

    int numpos = 0;

    Stack<ParsedTreeLine> levels = new Stack<ParsedTreeLine>();
    ParsedTreeLine cline = new ParsedTreeLine();

    int len = expression.length();

    for (int i = 0; i <= len; i++) {

      char c = (i == len ? '\0' : expression.charAt(i));

      if (state == 2) {
        if ("0123456789.".indexOf(c) != -1) {
          // number inner - just skip
          continue;
        }
        // number end
        try {
          double num = Double.parseDouble(expression.substring(numpos, i));

          cline.addItem(new ParsedTreeItem(operator, sign, num));

        } catch (NumberFormatException e) {
          throw new ParsingException("Invalid number format");
        }

        state = 1;
        sign = 0;
        operator = 0;
      }

      if ("0123456789.".indexOf(c) != -1) {
        // number start

        if (state == 1 && sign == 0 && operator == 0) {
          throw new ParsingException("Unexpected number");
        }

        state = 2;
        numpos = i;

      } else if (c == '-' || c == '+') {
        // - and + operators

        if (sign != 0) {
          throw new ParsingException("Too many operators");
        }
        sign = (byte) (c == '+' ? 1 : -1);

      } else if (c == '*' || c == '/') {
        // * and / operators

        if (state == 0) {
          throw new ParsingException("Unexpected binary operator");
        }

        if (sign != 0 || operator != 0) {
          throw new ParsingException("Too many operators");
        }
        operator = (byte) (c == '*' ? 2 : 3);

      } else if (c == '(') {
        // new line start

        if (state == 1 && sign == 0 && operator == 0) {
          throw new ParsingException("Unexpected opening bracket");
        }

        ParsedTreeLine newline = new ParsedTreeLine();
        cline.addItem(new ParsedTreeItem(operator, sign, newline));
        levels.push(cline);

        cline = newline;
        state = 0;
        sign = 0;
        operator = 0;

      } else if (c == ')' || c == '\0') {
        // current line end

        if (state == 0) {
          throw new ParsingException("Value is required");
        }
        if (sign != 0 || operator != 0) {
          throw new ParsingException("Operator is unfinished");
        }
        if (c == '\0') {
          if (!levels.empty()) {
            throw new ParsingException("Unexpected end of input");
          }
        } else {
          if (levels.empty()) {
            throw new ParsingException("Unexpected closing bracket");
          }
          cline = levels.pop();
          state = 1;
          sign = 0;
          operator = 0;
        }

      } else if (" \n\r\t".indexOf(c) == -1) {
        // unknown character
        throw new ParsingException("Unexpected character: '" + c + "'");
      }
    }
    return cline;
  }

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null expression");
    }
    ParsedTreeLine root = buildTree(expression);
    return root.evaluateValue();
  }
}
