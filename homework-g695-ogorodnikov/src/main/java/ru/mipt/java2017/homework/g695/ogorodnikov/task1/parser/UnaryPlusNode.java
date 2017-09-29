package ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser;

class UnaryPlusNode extends ParsedExpr {
    private ParsedExpr expr;

    UnaryPlusNode(ParsedExpr expr) {
        this.expr = expr;
    }

    @Override
    public double eval() {
        return expr.eval();
    }

    @Override
    public String toString() {
        return "+(" + expr.toString() + ")";
    }
}
