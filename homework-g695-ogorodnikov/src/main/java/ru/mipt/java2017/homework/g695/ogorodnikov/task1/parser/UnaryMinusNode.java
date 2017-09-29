package ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser;

class UnaryMinusNode extends ParsedExpr {
    private ParsedExpr expr;

    UnaryMinusNode(ParsedExpr expr) {
        this.expr = expr;
    }

    @Override
    public double eval() {
        return -expr.eval();
    }

    @Override
    public String toString() {
        return "-(" + expr.toString() + ")";
    }
}
