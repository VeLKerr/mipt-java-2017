package ru.mipt.java2017.homework.g696.mikhaylov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

class TrivialChecker {

  private String expression;
  private static final String ALLOWED_CHARACTERS_REGEX = "[^.()*/0-9+-]";

  /**
   * Проводит тривиальные тесты, такие как проверка на пустоту, допустимые символы или скобочный баланс
   *
   * @param exp для проверки
   * @return выражение без пробелов, переводов строки и так далее
   * @throws ParsingException если выражение не проходит проверки
   */
  String check(String exp) throws ParsingException {
    expression = exp;
    checkExpressionIsNotNull();
    celeteSpacesAndOthers();
    checkNotEmpty();
    checkCharacters();
    checkBracketBalance();
    return expression;
  }

  private void celeteSpacesAndOthers() {
    expression = expression.replaceAll("\\n", "");
    expression = expression.replaceAll("\\t", "");
    expression = expression.replaceAll("\\s", "");
  }

  private void checkCharacters() throws ParsingException {
    String buffer = expression.replaceAll(ALLOWED_CHARACTERS_REGEX, "");
    if (buffer.length() != expression.length()) {
      throw new ParsingException("Invalid characters");
    }
  }

  private void checkNotEmpty() throws ParsingException {
    if (expression.isEmpty()) {
      throw new ParsingException("String is empty");
    }
  }

  private void checkBracketBalance() throws ParsingException {
    int balance = 0;
    for (char c : expression.toCharArray()) {
      if (c == '(') {
        balance++;
      }
      if (c == ')') {
        balance--;
      }
      if (balance < 0) {
        throw new ParsingException("Bad bracket balance");
      }
    }
    if (balance != 0) {
      throw new ParsingException("Bad bracket balance");
    }
  }

  private void checkExpressionIsNotNull() throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Expression is null");
    }
  }
}
