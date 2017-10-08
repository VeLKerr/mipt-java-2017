package ru.mipt.java2017.homework.g696.feofanova.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * ExpressionHandler is an interface, that must do smth with numbers and operators, and give the
 * answer in the end.
 * @author Mary Feofanova
 * @since 25.09.17
 */
public interface ExpressionHandler {
  /**
   * Func that handles number
   *
   * @param number is a value to be pushed
   * @throws ParsingException if input expression was invalid
   */
  void pushNumber(double number) throws ParsingException;

  /**
   * Func that handles operation
   *
   * @param operator is an operator to be pushed
   * @throws ParsingException if input expression was invalid
   */
  void pushOperator(char operator) throws ParsingException;

  /**
   * Gives the answer
   *
   * @return correct answer
   * @throws ParsingException if input expression was invalid
   */
  double getAnswer() throws ParsingException;
}
