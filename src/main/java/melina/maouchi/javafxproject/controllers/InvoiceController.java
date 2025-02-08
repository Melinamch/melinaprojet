package melina.maouchi.javafxproject.controllers;


import javafx.scene.layout.VBox;
import melina.maouchi.javafxproject.HelloApplication;
import melina.maouchi.javafxproject.models.entities.Invoice;
import melina.maouchi.javafxproject.models.entities.Order;
import melina.maouchi.javafxproject.models.entities.OrderItem;
import melina.maouchi.javafxproject.repositories.impl.CustomerRepositoryImpl;
import melina.maouchi.javafxproject.repositories.impl.InvoiceRepositoryImpl;
import melina.maouchi.javafxproject.repositories.impl.OrderRepositoryImpl;
import melina.maouchi.javafxproject.repositories.impl.ProductRepositoryImpl;
import melina.maouchi.javafxproject.repositories.interfaces.CustomerRepository;
import melina.maouchi.javafxproject.repositories.interfaces.InvoiceRepository;
import melina.maouchi.javafxproject.repositories.interfaces.OrderRepository;
import melina.maouchi.javafxproject.repositories.interfaces.ProductRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.security.cert.PolicyNode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;



public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    public InvoiceController() {
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        ProductRepository productRepository = new ProductRepositoryImpl();
        this.orderRepository = new OrderRepositoryImpl(customerRepository, productRepository);
        this.invoiceRepository = new InvoiceRepositoryImpl(orderRepository);
    }


    @FXML
    private ComboBox<Order> orderComboBox;
    @FXML private TableView<Invoice> invoicesTable;
    @FXML private Label totalAmountLabel;
    @FXML private DatePicker invoiceDatePicker;

    @FXML private TableColumn<Invoice, Integer> idColumn;
    @FXML private TableColumn<Invoice, Integer> orderIdColumn;
    @FXML private TableColumn<Invoice, LocalDate> invoiceDateColumn;
    @FXML private TableColumn<Invoice, Double> totalAmountColumn;
    @FXML private VBox invoiceItemsVBox;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderIdColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getOrder().getId()));
        invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        loadOrders();
        loadInvoices();
    }

    private void loadOrders() {
        List<Order> orders = orderRepository.findAll();

        orderComboBox.setItems(FXCollections.observableArrayList(orders));

        orderComboBox.setConverter(new StringConverter<Order>() {
            @Override
            public String toString(Order order) {
                if (order == null) {
                    return "";
                }
                return "Order #" + order.getId() + " - " + order.getCustomer().getFirstName();
            }

            @Override
            public Order fromString(String string) {
                return null;
            }
        });
    }

    private void loadInvoices() {
        invoicesTable.setItems(
                FXCollections.observableArrayList(invoiceRepository.findAll())
        );
    }

    @FXML
    public void selectOrder() {
        Order selectedOrder = orderComboBox.getValue();
        if (selectedOrder != null) {
            totalAmountLabel.setText(String.format("%.2f", selectedOrder.getTotalAmount()));
        }
    }

    @FXML
    public void generateInvoice() {
        Order selectedOrder = orderComboBox.getValue();
        if (selectedOrder != null) {
            // Créer la facture
            Invoice invoice = new Invoice();
            invoice.setOrder(selectedOrder);
            invoice.setInvoiceDate(invoiceDatePicker.getValue());
            invoice.setTotalAmount(selectedOrder.getTotalAmount());

            // Ajouter la facture dans la base de données
            invoiceRepository.add(invoice);

            // Afficher les articles de la commande dans la vue de la facture
            displayOrderItems(selectedOrder);

            // Charger la liste des factures
            loadInvoices();
        }
    }
    private void displayOrderItems(Order order) {
        // Crée une TableView pour afficher les articles
        TableView<OrderItem> itemsTable = new TableView<>();

        // Définir les colonnes pour le nom de l'article, le prix et la quantité
        TableColumn<OrderItem, String> itemNameColumn = new TableColumn<>("Item");
        TableColumn<OrderItem, Double> itemPriceColumn = new TableColumn<>("Price");
        TableColumn<OrderItem, Integer> itemQuantityColumn = new TableColumn<>("Quantity");

        // Assurez-vous que les colonnes récupèrent les bonnes valeurs des objets OrderItem
        itemNameColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getProduct().getName()));
        itemPriceColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getProduct().getPrice()));
        itemQuantityColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getQuantity()));

        // Ajouter les colonnes à la TableView
        itemsTable.getColumns().addAll(itemNameColumn, itemPriceColumn, itemQuantityColumn);

        // Ajouter les articles de la commande à la table
        itemsTable.setItems(FXCollections.observableArrayList(order.getItems()));

        // Ajouter la TableView au VBox dans l'interface utilisateur
        invoiceItemsVBox.getChildren().add(itemsTable);  // Utilise le conteneur correct du FXML
    }
    @FXML
    public void updateInvoice() {
        Invoice selectedInvoice = invoicesTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            Order selectedOrder = orderComboBox.getValue();
            LocalDate selectedDate = invoiceDatePicker.getValue();

            if (selectedOrder != null) {
                selectedInvoice.setOrder(selectedOrder);
                selectedInvoice.setTotalAmount(selectedOrder.getTotalAmount());
            }
            if (selectedDate != null) {
                selectedInvoice.setInvoiceDate(selectedDate);
            }

            System.out.println("Selected Invoice: " + selectedInvoice.getId());
            invoiceRepository.update(selectedInvoice);

            loadInvoices();
        } else {
            showAlert("No Invoice Selected", "Please select an invoice to update.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void deleteInvoice() throws SQLException {
        Invoice selectedInvoice = invoicesTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            invoiceRepository.delete(selectedInvoice.getId());
            loadInvoices();
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