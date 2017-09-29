package ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser;

class DiffNode extends BinaryOperatorNode {

    @Override
    public String toString() {
        return "(" + left.toString() + " - " + right.toString() + ")";
    }

    DiffNode(ParsedExpr left, ParsedExpr right) {
        super(left, right);
    }

    @Override
    public double eval() {
        return left.eval() - right.eval();
    }
}
