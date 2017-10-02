package ru.mipt.homework.g696.pak.task1;
import sun.security.pkcs.ParsingException;

//parse numbers
public class PrimaryExpressionParser implements ExpressionParser {

    private int getDigit_(char digit) {
        return (digit - '0');
    }

    @Override
    public Calculable parse(String expression) throws ParsingException {
        double frictionalPart = 0, integerPart = 0;
        int iter = 0;
        int sign = 1;
        if (expression.charAt(0) == '-') {
            sign = -1;
            ++iter;
        }
        for (; iter < expression.length() && expression.charAt(iter) != '.'; ++iter) {
            integerPart = integerPart * 10 + getDigit_(expression.charAt(iter));
        }

        if (iter < expression.length() && expression.charAt(iter) == '.')
            ++iter;

        double e = 1;
        for (; iter < expression.length(); ++iter) {
            e *= 10;
            frictionalPart = frictionalPart * 10 + getDigit_(expression.charAt(iter));
        }
        return new Constant(sign * (integerPart + frictionalPart / e));
    }

}
