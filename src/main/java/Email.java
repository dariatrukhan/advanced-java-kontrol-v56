public class Email {
    private final String orderId;
    private final String amount;

    public Email(String orderId, String amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order " + orderId + " processed for " + amount + " UAH";
    }
}
