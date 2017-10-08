package ru.mipt.java2017.homework.g694.kozinov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Vector;

class Parser {
  private Vector<String> lexemes;

  Parser() {
    lexemes = new Vector<>();
  }

  void parseString(String expression) throws ParsingException {
    int len = expression.length();
    int balance = 0;
    lexemes.clear();

    if (expression.isEmpty()) {
      throw new ParsingException("You write an empty string");
    }

    for (int i = 0; i < len; ++i) {
      char currentSymbol = expression.charAt(i);
      switch (CharRecognizer.getCharKind(currentSymbol)) {
        case IGNORE:
          break;
        case UNKNOWN:
          throw new ParsingException("Unknown symbol is " + currentSymbol);

        case NUMBER_PART:
          StringBuilder nextString = new StringBuilder();
          while (CharRecognizer.getCharKind(currentSymbol) ==
            CharRecognizer.CharKind.NUMBER_PART) {
            nextString.append(currentSymbol);
            ++i;
            if (i >= len) {
              break;
            }

            currentSymbol = expression.charAt(i);
          }
          --i;
          lexemes.add(nextString.toString());
          break;

        case OPEN_BRACKET:
          ++balance;
          lexemes.add("(");
          break;

        case CLOSE_BRACKET:
          --balance;
          if (balance < 0) {
            throw new ParsingException("Extra close bracket at position " + i);
          }

          lexemes.add(")");
          break;

        default:
          lexemes.add("" + currentSymbol);
      }
    }

    if (balance != 0) {
      throw new ParsingException("Number of close and open brackets are different");
    }
  }

  void checkLexemes() throws ParsingException {
    for (int i = 1; i < lexemes.size(); ++i) {
      CharRecognizer.OperationKind a = CharRecognizer.getOperationKind(lexemes.get(i - 1).charAt(0));
      CharRecognizer.OperationKind b = CharRecognizer.getOperationKind(lexemes.get(i).charAt(0));
      if (a.getPriority() < 0 || b.getPriority() < 0) {
        return;
      }

      if (a.getPriority() == b.getPriority()) {
        throw new ParsingException("Double unary operation must separate them with scobe");
      }
    }
  }

  String getAt(int i) {
    return lexemes.get(i);
  }

  int size() {
    return lexemes.size();
  }
}
