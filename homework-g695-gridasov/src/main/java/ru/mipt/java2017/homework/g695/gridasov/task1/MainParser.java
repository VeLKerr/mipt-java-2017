/**
 * Created by ilya on 02.10.17.
 */

package ru.mipt.java2017.homework.g695.gridasov.task1;

public class MainParser implements AbstractParser {

    public static final int OPERATOR_NOT_EXIST = -111;

    public Operation parse(String expression) throws ParsingException {
        if (expression == null) {
            throw new ParsingException("get null expression");
        }
        String expressionWithoutSpaces = deleteSpaces(expression);
        return getOperationTree(expressionWithoutSpaces);
    }

    private String deleteSpaces(String s) {
        StringBuilder stringBuilder = new StringBuilder(s);
        for (int i = 0; i < stringBuilder.length(); ++i) {
            char c = stringBuilder.charAt(i);
            while ((c == ' ' || c == '\t' || c == '\n') && i < stringBuilder.length()) {
                stringBuilder.deleteCharAt(i);
                if (i == stringBuilder.length()) {
                    break;
                }
                c = stringBuilder.charAt(i);
            }
        }
        return stringBuilder.toString();
    }

    private Operation getOperationTree(String expression) {
        try {
            double x = Double.parseDouble(expression);
            return new Number(x);
        } catch (NumberFormatException e) {
            int ind = findLastOperator(expression);
            if (ind == OPERATOR_NOT_EXIST) { // expr = '(' + smth + ')'
                if (expression.charAt(0) != '(' || expression.charAt(expression.length() - 1) != ')') {
                    throw new ParsingException("bad number");
                }
                return getOperationTree(expression.substring(1, expression.length() - 1));
            }
            char c = expression.charAt(ind);
            if (ind == 0) { // this is unary operation
                if (c != '+' && c != '-') {
                    throw new ParsingException("bad unary operation");
                }
                return new UnaryOperation(c, getOperationTree(expression.substring(1, expression.length())));
            } else { // this is binary operation
                String left_expression = expression.substring(0, ind);
                String right_expression = expression.substring(ind + 1, expression.length());
                return new BinaryOperation(getOperationTree(left_expression), c, getOperationTree(right_expression));
            }
        }
    }

    private int findLastOperator(String expression) throws ParsingException {
        // 0 = '+', 1 = '-', 2 = '*', 3 = '/'
        if (expression.length() == 0) {
            throw new ParsingException("Wrong expression");
        }
        int[] index = new int[4];
        for (int j = 0; j < 4; ++j) {
            index[j] = OPERATOR_NOT_EXIST;
        }
        int depth = 0;
        for (int i = 0; i < expression.length(); ++i) {
            char c = expression.charAt(i);
            if (isUnknown(c)) {
                throw new ParsingException("unknown char");
            }
            if (c == '(') {
                ++depth;
            } else if (c == ')') {
                --depth;
            } else if (depth == 0) {
                if (c == '+') {
                    if (index[2] == i - 1 || index[3] == i - 1) { // 2\-5
                        continue;
                    }
                    index[0] = i;
                } else if (c == '-') {
                    if (index[2] == i - 1 || index[3] == i - 1) { // 2\-5
                        continue;
                    }
                    index[1] = i;
                } else if (c == '*') {
                    index[2] = i;
                } else if (c == '/') {
                    index[3] = i;
                }
            }
            if (depth < 0) {
                throw new ParsingException("Wrong '(' and ')' expression");
            }
        }
        if (depth != 0) {
            throw new ParsingException("Wrong '(' and ')' expression");

        }
        return getLastOperatorIndex(index);
    }

    private boolean isUnknown(char c) {
        return !(isOperator(c) || isDigit(c) || isHelpSymbol(c));
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean isHelpSymbol(char c) {
        return c == '.' || c == '(' || c == ')';
    }

    private int getLastOperatorIndex(int[] index) {
        for (int i = 0; i < 4; ++i) {
            if (index[i] != OPERATOR_NOT_EXIST) {
                return index[i];
            }
        }
        return OPERATOR_NOT_EXIST;
    }
}
