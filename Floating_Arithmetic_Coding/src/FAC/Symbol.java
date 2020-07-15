package FAC;

public class Symbol{
    private char symbol;
    private double low_range;
    private double high_range;

    public Symbol(char symbol, double low_range, double high_range){
        this.symbol = symbol;
        this.low_range = low_range;
        this.high_range = high_range;
    }

    public char getSymbol() {
        return symbol;
    }

    public double getLow_range() {
        return low_range;
    }

    public double getHigh_range() {
        return high_range;
    }

    @Override
    public String toString() {
        return (symbol + " -> " + low_range + " : " + high_range);
    }
}
