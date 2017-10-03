package ru.mipt.java2017.homework.g697.ivaninVitaly.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;


/**
 * Вспомогательный класс для {@link EvalCalculator}
 * По сути предстовляет из себя подобие конечного автомата с подсчётом.
 * @author Ivanin Vitaly @alloky
 * @since 30.09.17
 */
public class ECalcLevel {
  private double leftSum, rightSum;
  private boolean leftInitialized, rightInitialized;

  protected enum operatorType {
    PLUS,
    MINUS,
    MUL,
    DIV,
    INVALID
  }
  protected enum expectation {
    NUMBER,
    OPERATOR,
    SIGN
  }
  private operatorType currentLeftOperator;
  private operatorType currentRightOperator;
  private double preSign;

  /**
   * Переменная отвечает за ожидаемый тип токена
   * Меняется в функциях {@link ECalcLevel#processOperator(operatorType)} и
   * {@link ECalcLevel#processNumber(double)}
   */
  private expectation currentExpectation;


  /**
   * Конструктор без параметров.
   * Инициализирует начальные значения.
   */
  public ECalcLevel(){
    leftSum = 1;
    rightSum = 1;
    preSign = 1;
    leftInitialized = false;
    rightInitialized = false;
    currentLeftOperator = operatorType.INVALID;
    currentRightOperator = operatorType.INVALID;
    currentExpectation = expectation.SIGN;
  }


  /**
   * @param operator новоприщедщий оператор
   * @throws ParsingException сообщение об ошибке с текстом на английском.
   */
  public void processOperator(operatorType operator) throws ParsingException {
    if (currentExpectation == expectation.NUMBER) {
      throw new ParsingException("Invalid expression.");
    }
    if(currentExpectation == expectation.OPERATOR){
      if(currentLeftOperator == operatorType.INVALID){
        currentLeftOperator = operator;
      } else if (currentRightOperator == operatorType.INVALID) {
        currentRightOperator = operator;
      }
      currentExpectation = expectation.SIGN;
    }  else {
      if (operator == operatorType.MINUS){
        preSign *= -1;
      } else {
        if(operator == operatorType.MUL || operator == operatorType.DIV){
          throw new ParsingException("Invalid unary operator");
        }
      }
      currentExpectation = expectation.NUMBER;
    }
  }

  /**
   * @param right новоприщедщее число
   * @throws ParsingException сообщение об ошибке с текстом на английском.
   */
  public void processNumber (double right) throws ParsingException {
    if(currentExpectation == expectation.OPERATOR ){
      throw new ParsingException("Invalid expression.");
    }

    right *= preSign;
    preSign = 1;

    if(!leftInitialized){
      leftSum = right;
      leftInitialized = true;
    }
    else {
      if(!rightInitialized){
        rightSum = right;
        rightInitialized = true;
      } else {
        if( currentLeftOperator == operatorType.MUL){
          leftSum *=rightSum;
          rightSum = right;
          currentLeftOperator = currentRightOperator;
          currentRightOperator = operatorType.INVALID;
        } else if (currentLeftOperator == operatorType.DIV){
          leftSum /= rightSum;
          rightSum = right;
          currentLeftOperator = currentRightOperator;
          currentRightOperator = operatorType.INVALID;

        } else {
          if(currentRightOperator == operatorType.MUL){
            rightSum *= right;
            currentRightOperator = operatorType.INVALID;
          } else if(currentRightOperator == operatorType.DIV){
            rightSum /= right;
            currentRightOperator = operatorType.INVALID;
          } else {
            if(currentLeftOperator == operatorType.MINUS){
              leftSum = leftSum - rightSum;
            } else {
              leftSum = leftSum + rightSum;
            }
            rightSum = right;
            currentLeftOperator = currentRightOperator;
            currentRightOperator = operatorType.INVALID;
          }
        }
      }
    }
    currentExpectation = expectation.OPERATOR;
  }


  /**
   * @throws ParsingException сообщение об ошибке с текстом на английском.
   * @return double подсчитанное значение на текущем уровне в скобочной последовательности
   */
  public double eval() throws ParsingException{
    if(currentRightOperator == operatorType.INVALID){
      if(currentLeftOperator == operatorType.INVALID){
        if(!leftInitialized){
          throw new ParsingException("Invalid expression.");
        } else {
          return leftSum;
        }
      } else {
        if(!rightInitialized) {
          throw new ParsingException("Invalid expression.");
        } else {
          switch (currentLeftOperator){
            case PLUS:
              return leftSum + rightSum;
            case MINUS:
              return leftSum - rightSum;
            case DIV:
              return leftSum / rightSum;
            case MUL:
              return leftSum*rightSum;
          }
        }
      }
    } else {
      throw new ParsingException("Invalid expression.");
    }
    return 0;
  }

}
