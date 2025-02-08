package melina.maouchi.javafxproject.controllers;

import melina.maouchi.javafxproject.HelloApplication;

import melina.maouchi.javafxproject.models.entities.*;
import melina.maouchi.javafxproject.models.enums.OrderStatus;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import melina.maouchi.javafxproject.repositories.impl.CustomerRepositoryImpl;
import melina.maouchi.javafxproject.repositories.impl.OrderRepositoryImpl;
import melina.maouchi.javafxproject.repositories.impl.ProductRepositoryImpl;
import melina.maouchi.javafxproject.repositories.interfaces.CustomerRepository;
import melina.maouchi.javafxproject.repositories.interfaces.OrderRepository;
import melina.maouchi.javafxproject.repositories.interfaces.ProductRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class OrderController {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;

    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<Product> productComboBox;
    @FXML private TextField quantityField;


    @FXML private TableView<OrderItem> orderItemsTable;
    @FXML private Label totalAmountLabel;

    @FXML private TableView<Order> savedOrdersTable;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, String> orderCustomerColumn;
    @FXML private TableColumn<Order, LocalDate> orderDateColumn;
    @FXML private TableColumn<Order, OrderStatus> orderStatusColumn;
    @FXML private TableColumn<Order, Double> orderTotalColumn;

    private Order currentOrder = new Order();

    public OrderController() {
        this.customerRepository = new CustomerRepositoryImpl();
        this.productRepository = new ProductRepositoryImpl();
        this.orderRepository = new OrderRepositoryImpl(customerRepository, productRepository);

    }


    @FXML
    public void initialize() {
        if (productsTable != null) setupProductsTable();
        if (customerComboBox != null) setupCustomerComboBox();
        if (productComboBox != null) setupProductComboBox();
        if (orderItemsTable != null) setupOrderItemsTable();
        if (savedOrdersTable != null) setupSavedOrdersTable();
        savedOrdersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showOrderDetails();
            }
        });

        loadInitialData();



    }
    private void setupProductsTable() {
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void setupCustomerComboBox() {
        customerComboBox.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer != null ? customer.getFirstName() + " " + customer.getLastName() : "";
            }

            @Override
            public Customer fromString(String string) {
                return null; // Not needed
            }
        });
    }

    private void setupProductComboBox() {
        productComboBox.setConverter(new StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product != null ? product.getName() : "";
            }

            @Override
            public Product fromString(String string) {
                return null; // Not needed
            }
        });
    }

    private void setupOrderItemsTable() {
        TableColumn<OrderItem, String> productColumn = new TableColumn<>("Product");
        productColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getName()));

        TableColumn<OrderItem, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<OrderItem, Double> priceColumn = new TableColumn<>("Total Price");
        priceColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().calculatePrice()));

        orderItemsTable.getColumns().setAll(productColumn, quantityColumn, priceColumn);
    }

    private void setupSavedOrdersTable() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        orderCustomerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCustomer() != null ?
                                cellData.getValue().getCustomer().getFirstName() + " " +
                                        cellData.getValue().getCustomer().getLastName() :
                                "N/A"
                ));

        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderTotalColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTotalAmount()));
    }

    private void loadInitialData() {
        // Load products
        List<Product> products = productRepository.findAll();
        productsTable.setItems(FXCollections.observableArrayList(products));

        // Load products into the combo box
        productComboBox.setItems(FXCollections.observableArrayList(products));

        // Load customers
        List<Customer> customers = customerRepository.findAll();
        customerComboBox.setItems(FXCollections.observableArrayList(customers));

        // Load saved orders
        refreshSavedOrdersTable();
    }

    @FXML
    public void addOrderItem() {
        try {
            Product selectedProduct = productComboBox.getValue();
            int quantity = Integer.parseInt(quantityField.getText());

            if (selectedProduct == null || quantity <= 0) {
                showAlert("Invalid Input", "Please select a product and enter valid quantity");
                return;
            }

            OrderItem orderItem = new OrderItem(selectedProduct, quantity);
            currentOrder.addOrderItem(orderItem);

            orderItemsTable.setItems(FXCollections.observableArrayList(currentOrder.getOrderItems()));
            totalAmountLabel.setText(String.format("$%.2f", currentOrder.getTotalAmount()));

            // Clear input fields
            productComboBox.getSelectionModel().clearSelection();
            quantityField.clear();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid numeric quantity");
        }
    }

    @FXML
    public void createOrder() {
        if (currentOrder.getOrderItems().isEmpty() || customerComboBox.getValue() == null) {
            showAlert("Incomplete Order", "Please select a customer and add at least one item");
            return;
        }

        try {
            currentOrder.setCustomer(customerComboBox.getValue());
            currentOrder.setOrderDate(LocalDate.now());
            currentOrder.setStatus(OrderStatus.PENDING);
            currentOrder.calculateTotalAmount();

            orderRepository.add(currentOrder);
            refreshSavedOrdersTable();
            resetCurrentOrder();

        } catch (Exception e) {
            showAlert("Error", "Failed to create order: " + e.getMessage());
        }
    }

    private void refreshSavedOrdersTable() {
        List<Order> orders = orderRepository.findAll();
        savedOrdersTable.setItems(FXCollections.observableArrayList(orders));
    }

    private void resetCurrentOrder() {
        currentOrder = new Order();
        orderItemsTable.getItems().clear();
        totalAmountLabel.setText("$0.00");
        customerComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    public void updateOrderStatus() {
        Order selectedOrder = savedOrdersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            selectedOrder.setStatus(OrderStatus.VALIDATED);
            orderRepository.update(selectedOrder);
            refreshSavedOrdersTable();

        } else {
            showAlert("No Selection", "Please select an order to update");
        }

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
/*
    @FXML
    public void updateOrder() {
        if (currentOrder.getOrderItems().isEmpty() || customerComboBox.getValue() == null) {
            showAlert("Incomplete Order", "Please select a customer and add at least one item");
            return;
        }

        try {
            currentOrder.setCustomer(customerComboBox.getValue());
            currentOrder.calculateTotalAmount();

            orderRepository.update(currentOrder);
            refreshSavedOrdersTable();
            resetCurrentOrder();

        } catch (Exception e) {
            showAlert("Error", "Failed to update order: " + e.getMessage());
        }
    }
     @FXML
    public void editOrder() {
        Order selectedOrder = savedOrdersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("No Selection", "Please select an order to edit");
            return;
        }}
*/
    @FXML
    public void deleteOrder() throws SQLException {
        Order selectedOrder = savedOrdersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orderRepository.delete(selectedOrder.getId());
            refreshSavedOrdersTable();
        } else {
            showAlert("No Selection", "Please select an order to delete");
        }
    }


    @FXML
    public void removeOrderItem(OrderItem item) {
        if (item != null) {
            currentOrder.removeOrderItem(item);
            orderItemsTable.setItems(FXCollections.observableArrayList(currentOrder.getOrderItems()));
            totalAmountLabel.setText(String.format("$%.2f", currentOrder.getTotalAmount()));
        } else {
            showAlert("No Item Selected", "Please select an item to remove");
        }
    }

    @FXML
    public void cancelOrder() {
        Order selectedOrder = savedOrdersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            selectedOrder.setStatus(OrderStatus.CANCELED);
            orderRepository.update(selectedOrder);
            refreshSavedOrdersTable();
        } else {
            showAlert("No Selection", "Please select an order to cancel");
        }
    }
    @FXML
    public void showOrderDetails() {
        Order selectedOrder = savedOrdersTable.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            List<OrderItem> orderItems = selectedOrder.getOrderItems();
            orderItemsTable.setItems(FXCollections.observableArrayList(orderItems));
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une commande pour voir ses détails.");
        }
    }




    @FXML
    public void goToProducts() {
        HelloApplication.navigateTo("products-view.fxml");
    }

    @FXML
    public void goToOrders() {
        HelloApplication.navigateTo("order-view.fxml");
    }

    @FXML
    public void goToCustomer() {
        HelloApplication.navigateTo("customer-view.fxml");
    }

    @FXML
    public void goToInvoices() {
        HelloApplication.navigateTo("invoice-view.fxml");
    }
}