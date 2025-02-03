package melina.maouchi.javafxproject.models.entities;

import java.time.LocalDate;

public class Invoice {
    private int id;
    private Order order;
    private LocalDate invoiceDate;
    private double totalAmount;

    public Invoice(){

    }

    public Invoice(int id, Order order, LocalDate invoiceDate, double totalAmount) {
        this.id = id;
        this.order = order;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
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

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}