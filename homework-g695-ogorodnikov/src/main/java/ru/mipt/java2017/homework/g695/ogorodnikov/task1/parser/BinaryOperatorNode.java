package ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser;

abstract class BinaryOperatorNode extends ParsedExpr {
    protected ParsedExpr left;
    protected ParsedExpr right;

    BinaryOperatorNode(ParsedExpr left, ParsedExpr right) {
        this.left = left;
        this.right = right;
    }
}
