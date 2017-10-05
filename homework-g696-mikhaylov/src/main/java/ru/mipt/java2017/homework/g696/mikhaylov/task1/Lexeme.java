package ru.mipt.java2017.homework.g696.mikhaylov.task1;

class Lexeme {

  private LexemeType type;
  private String value;
  private Integer priority;

  LexemeType getType() {
    return type;
  }

  String getValue() {
    return value;
  }

  Integer getPriority() {
    return priority;
  }

  Lexeme(String v, LexemeType t) {
    type = t;
    value = v;
    priority = identifyPriority();
  }

  /**
   * @return вычисляет приоритет данной лексемы.
   */
  private int identifyPriority() {
    if (type == LexemeType.NUMBER || type == LexemeType.LEFT_BRACKET || type == LexemeType.RIGHT_BRACKET) {
      return PriorityType.VERY_LOW;
    }
    if (type == LexemeType.UNARY_OPERATION) {
      return PriorityType.HIGH;
    }
    if (value.equals("+") || value.equals("-")) {
      return PriorityType.LOW;
    }
    return PriorityType.MEDIUM;
  }
}
