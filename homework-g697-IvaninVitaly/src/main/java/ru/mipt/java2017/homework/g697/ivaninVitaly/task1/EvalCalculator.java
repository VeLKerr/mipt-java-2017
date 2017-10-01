package ru.mipt.java2017.homework.g697.ivaninVitaly.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;
import java.util.regex.*;

/**
 * Калькулятор на стеке. Реализует интерфейс {@link Calculator}
 *
 * Правильно построеные выражение (ппв) - expression, построим рекурсивно.
 *
 *  число с точкой - ппв
 *
 * Назовём ппв знаковым если первый его символ - плюс или минус.
 *
 * Если a, b - беззнаковые ппв, то (qam), arb - ппв, где r - строка,
 * содержащая ровно один оператор и произвольное число пробельных символов,
 * q, m - строки, содержащие произвольное число пробельных символов.
 *
 * Выражением в скобках назовём ппв, в начале и конце которого с точнстью до
 * пробелов стоят необходимые скобки.
 *
 * Если a - беззнаковое ппв, являющееся числом или выражением в скобках, то
 * +a , -a, (+a), (-a) - ппв.
 *
 * Калькулятор считает ппв посредством вызова {@link EvalCalculator#calculate(String exception)}.
 * Если входная строка не является ппв - будет выкинуто исключение - {@link ParsingException}
 *
 *
 * @author Ivanin Vitaly @alloky
 * @since 30.09.17
 */

public class EvalCalculator implements Calculator {

  private Pattern pattern;
  private Stack<ECalcLevel> st;

  /**
   * В конструкторе инициализируется регулярное выражение для ввода токенами ( числа, скобки и операторы )
   * и пустой стэк.
   */
  public EvalCalculator(){
    pattern = Pattern.compile("(([-+*\\/()]|\\s)|([1-9][0-9]*(\\.[0-9]*)?)|([0][0-9]*(\\.[0-9]*)?))");
    st = new Stack<ECalcLevel>();
  }

  /**
   * @param expression строка - для выполнения без исключения должна быть ппв.
   * @return значение ппв.
   * @throws ParsingException сообщение об ошибке с текстом на английском.
   */
  public double calculate(String expression) throws ParsingException {

    if(expression == null){
      throw new ParsingException("Null pointer: expression.");
    }

    Matcher matcher = pattern.matcher(expression);
    int correctLen = 0;
    ECalcLevel current = new ECalcLevel();

    while (matcher.find()) {
//            System.out.println(matcher.group(1));
      correctLen += matcher.group(1).length();
      if(Character.isWhitespace(matcher.group(1).charAt(0))){
        continue;
      }
      if(matcher.group(1).length() == 1) {
        switch (matcher.group(1).charAt(0)){
          case '+':
            current.processOperator(ECalcLevel.operatorType.PLUS);
            break;
          case '-':
            current.processOperator(ECalcLevel.operatorType.MINUS);
            break;
          case '*':
            current.processOperator(ECalcLevel.operatorType.MUL);
            break;
          case '/':
            current.processOperator(ECalcLevel.operatorType.DIV);
            break;
          case '(':
            st.push(current);
            current = new ECalcLevel();
            break;
          case ')':
            if(st.isEmpty()){
              throw new ParsingException("Is not balanced parentheses");
            }
            ECalcLevel tmp = current;
            current = st.pop();
            current.processNumber(tmp.eval());
            break;
          default:
            double curNum;
            try {
              curNum = Double.parseDouble(matcher.group(1));
            } catch (Exception e){
              throw new ParsingException("Unable to parse number.");
            }
            current.processNumber(curNum);
        }
      } else {
        if(matcher.group(1).charAt(0) == '0' && matcher.group(1).charAt(1) != '.'){
          throw new ParsingException("Numbers shouldn't start with zero.");
        }
        double curNum;
        try {
          curNum = Double.parseDouble(matcher.group(1));
        } catch (Exception e){
          throw new ParsingException("Unable to parse number.");
        }
        current.processNumber(curNum);
      }
    }
    if(st.size() != 0){
      throw new ParsingException("Is not balanced parentheses");
    }
    double ret;
    if (correctLen == expression.length()){
      ret = current.eval();
    } else {
      throw new ParsingException("Invalid token found.");
    }
    st.clear();
    return ret;
  }
}
