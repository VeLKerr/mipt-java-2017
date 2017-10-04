/**
 * Created by ilya on 04.10.17.
 */
public class Number implements Operation {
    public Number(double value) {
        this.value = value;
    }
    public Number(String epxression) {
        try {
            this.value = Double.parseDouble(epxression);
        } catch (NumberFormatException e) {
            throw new ParsingException("Bad number in expression");
        }
    }
    private double value;

    public double evaluate() {
        return value;
    }
}
