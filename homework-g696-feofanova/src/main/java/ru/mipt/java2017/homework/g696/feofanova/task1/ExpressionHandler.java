package ru.mipt.java2017.homework.g696.feofanova.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 *ExpressionHandler is an interface, that must do smth with numbers and operators,
 * and give the answer in the end.
 */

public interface ExpressionHandler {
  //func that handles number
  void pushNumber(double number) throws ParsingException;
  //func that handles operator
  void pushOperator(char operator) throws  ParsingException;

  //gives the answer
  double getAnswer() throws ParsingException;
}