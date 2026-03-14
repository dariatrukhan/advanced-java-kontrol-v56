public class OrderItem {
    private final String name;
    private final String category;
    private final Money price;

    public OrderItem(String name, String category, Money price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getCategory() { return category; }
    public Money getPrice() { return price; }
}

