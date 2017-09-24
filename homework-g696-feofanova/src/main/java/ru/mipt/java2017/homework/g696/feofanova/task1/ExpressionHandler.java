package ru.mipt.java2017.homework.g696.feofanova.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

public interface ExpressionHandler {
  void pushNumber(double number) throws ParsingException;
  void pushOperand(char operand) throws  ParsingException;

  double getAnswer() throws ParsingException;
}
