package melina.maouchi.javafxproject.models.entities;

public class OrderItem {
    private int id;
    private Order order;
    private Product product;
    private int quantity;
    private double price;

    public OrderItem() {}

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice() * quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        updatePrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updatePrice();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private void updatePrice() {
        if (product != null) {
            this.price = product.getPrice() * quantity;
        }
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "product=" + product.getName() +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    public double calculatePrice() {
        return product.getPrice() * quantity;
    }
}
