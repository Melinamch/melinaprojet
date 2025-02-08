package melina.maouchi.javafxproject.models.entities;

import melina.maouchi.javafxproject.models.enums.OrderStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private Customer customer;
    private LocalDate orderDate;
    private OrderStatus status;
    private double totalAmount;


    public Order(){
    }


    public Order(int id, Customer customer, LocalDate orderDate, OrderStatus status, double totalAmount) {
        this.id = id;
        this.customer = customer;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem item) {
        item.setOrder(this);
        orderItems.add(item);
        calculateTotalAmount();
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        calculateTotalAmount();
    }

    public List<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        calculateTotalAmount();
    }

    public void calculateTotalAmount() {

    }
    public OrderItem getItems() {
        OrderItem item = new OrderItem();  // Crée un objet OrderItem
        // Configure l'objet OrderItem ici si nécessaire
        return item;  // Retourne l'objet
    }

}
