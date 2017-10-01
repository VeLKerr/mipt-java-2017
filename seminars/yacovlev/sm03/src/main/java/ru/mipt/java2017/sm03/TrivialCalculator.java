package ru.mipt.java2017.sm03;

// "1+2+3+4"
/*
Это многострочный комментарий
ывапвап
вапвап

 */

/** Этот класс реализует крутой калькулятор!
 * @author Вася Пупкин
 * @since 1.0
 */
public class TrivialCalculator implements SumCalculable, ProdCalculable {

    enum Operation { PLUS, ASTERISK;

        @Override
        public String toString() {
            return this==Operation.PLUS ? "\\+" : "\\*";
        }
    };

    /**
     * Вычисляет сумму ряда чисел в строке
     * @param expression Строка, содержащая числа и оператор "+"
     * @return Сумма чисел ряда
     * @throws ExpressionParseException
     *          Если в строке какая-та фигня
     */
    @Override
    public int calculateSum(String expression) throws ExpressionParseException {
        int numbers[] = extractNumbersFromString(expression, Operation.PLUS);
        int result = 0;
        for (int i = 0; i < numbers.length; i++) {
            result += numbers[i];
        }
        return result;
    }

    protected int[] extractNumbersFromString(String expression, Operation op) throws ExpressionParseException {
        String regex = op.toString();
        String tokens[] = expression.split(regex);
        int result[] = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            try {
                result[i] = Integer.parseInt(tokens[i]);
            }
            catch (NumberFormatException e) {
                throw new ExpressionParseException(expression, tokens[i]);
            }
        }
        return result;
    }


    @Override
    public long calculateProd(String expression) throws ExpressionParseException {
        int numbers[] = extractNumbersFromString(expression, Operation.ASTERISK);
        long result = 1;
        for (int i = 0; i < numbers.length; i++) {
            result *= numbers[i];
        }
        return result;
    }
}
