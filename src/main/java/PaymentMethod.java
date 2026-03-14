public interface PaymentMethod {
    void pay(Money amount) throws PaymentException;
}

class CardPayment implements PaymentMethod {
    @Override
    public void pay(Money amount) {
        if (amount.amount() > 25000) {
            throw new PaymentException("Card limit exceeded: max 25,000");
        }
        System.out.println("Paid via Card: " + amount.amount());
    }
}

class PayPalPayment implements PaymentMethod {
    @Override
    public void pay(Money amount) {
        if (amount.amount() < 200) {
            throw new PaymentException("PayPal minimum: 200");
        }
        System.out.println("Paid via PayPal: " + amount.amount());
    }
}

 class BankTransferPayment implements PaymentMethod {
    @Override
    public void pay(Money amount) {
        double totalWithFee = amount.amount() * 1.015; // 1.5% commission
        System.out.println("Bank Transfer (incl 1.5% fee): " + totalWithFee);
    }
}
