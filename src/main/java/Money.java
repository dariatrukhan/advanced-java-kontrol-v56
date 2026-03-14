public record Money(double amount, String currency) {
    public Money {
        if (amount < 0) throw new IllegalArgumentException("Cannot be negative");
    }

    public Money multiply(double factor) {
        return new Money(this.amount * factor, this.currency);
    }

    public Money subtract(double sub) {
        return new Money(this.amount - sub, this.currency);
    }
}

