package melina.maouchi.javafxproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class UserDashboardController {

    @FXML
    private TableView<?> productsTable;
    @FXML
    private TableColumn<?, ?> productIdColumn;
    @FXML
    private TableColumn<?, ?> productNameColumn;
    @FXML
    private TableColumn<?, ?> productPriceColumn;

    @FXML
    private ComboBox<String> customerComboBox;
    @FXML
    private ComboBox<String> productComboBox;
    @FXML
    private TextField quantityField;

    @FXML
    private TableView<?> orderItemsTable;
    @FXML
    private TableColumn<?, ?> orderProductColumn;
    @FXML
    private TableColumn<?, ?> orderQuantityColumn;
    @FXML
    private TableColumn<?, ?> orderPriceColumn;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private TableView<?> savedOrdersTable;
    @FXML
    private TableColumn<?, ?> orderIdColumn;
    @FXML
    private TableColumn<?, ?> orderCustomerColumn;
    @FXML
    private TableColumn<?, ?> orderDateColumn;
    @FXML
    private TableColumn<?, ?> orderStatusColumn;
    @FXML
    private TableColumn<?, ?> orderTotalColumn;

    @FXML
    private TableView<?> productTable;
    @FXML
    private TableColumn<?, ?> nameColumn;
    @FXML
    private TableColumn<?, ?> priceColumn;
    @FXML
    private TableColumn<?, ?> categoryColumn;
    @FXML
    private TableColumn<?, ?> stockColumn;

    private ObservableList<String> customers = FXCollections.observableArrayList("Customer 1", "Customer 2");
    private ObservableList<String> products = FXCollections.observableArrayList("Product 1", "Product 2");

    @FXML
    public void initialize() {
        customerComboBox.setItems(customers);
        productComboBox.setItems(products);
    }

    @FXML
    private void addOrderItem(ActionEvent event) {
        String product = productComboBox.getValue();
        String quantity = quantityField.getText();
        if (product != null && !quantity.isEmpty()) {
            System.out.println("Added " + quantity + " of " + product + " to order.");
        }
    }

    @FXML
    private void createOrder(ActionEvent event) {
        System.out.println("Order Created");
    }

    @FXML
    private void goToProducts(ActionEvent event) {
        System.out.println("Navigating to Products");
    }

    @FXML
    private void goToOrders(ActionEvent event) {
        System.out.println("Navigating to Orders");
    }

}
