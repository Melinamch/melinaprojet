package melina.maouchi.javafxproject.controllers;

import melina.maouchi.javafxproject.HelloApplication;
import melina.maouchi.javafxproject.models.entities.Customer;
import melina.maouchi.javafxproject.repositories.impl.CustomerRepositoryImpl;
import melina.maouchi.javafxproject.repositories.interfaces.CustomerRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class CustomerController {
    private final CustomerRepository customerRepository;

    @FXML private TableView<Customer> customerTable;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    public CustomerController() {
        this.customerRepository = new CustomerRepositoryImpl();
    }

    @FXML
    public void initialize() {
        customerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectCustomer()
        );

        loadCustomers();
        setupTableColumns();
    }

    private void setupTableColumns() {
        TableColumn<Customer, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));

        TableColumn<Customer, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhone()));

        customerTable.getColumns().setAll(firstNameCol, lastNameCol, emailCol, phoneCol);
    }

    private void loadCustomers() {
        ObservableList<Customer> customers =
                FXCollections.observableArrayList(customerRepository.findAll());
        customerTable.setItems(customers);
    }

    @FXML
    public void addCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(firstNameField.getText());
        newCustomer.setLastName(lastNameField.getText());
        newCustomer.setEmail(emailField.getText());
        newCustomer.setPhone(phoneField.getText());

        customerRepository.add(newCustomer);
        loadCustomers();
        clearFields();
    }

    @FXML
    public void updateCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            selectedCustomer.setFirstName(firstNameField.getText());
            selectedCustomer.setLastName(lastNameField.getText());
            selectedCustomer.setEmail(emailField.getText());
            selectedCustomer.setPhone(phoneField.getText());

            customerRepository.update(selectedCustomer);
            loadCustomers();
        }
    }

    @FXML
    public void deleteCustomer() throws SQLException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            customerRepository.delete(selectedCustomer.getId());
            loadCustomers();
        }
    }

    @FXML
    public void selectCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            firstNameField.setText(selectedCustomer.getFirstName());
            lastNameField.setText(selectedCustomer.getLastName());
            emailField.setText(selectedCustomer.getEmail());
            phoneField.setText(selectedCustomer.getPhone());
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
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