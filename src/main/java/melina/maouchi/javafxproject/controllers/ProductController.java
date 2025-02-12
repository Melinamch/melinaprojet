package melina.maouchi.javafxproject.controllers;

import melina.maouchi.javafxproject.HelloApplication;
import melina.maouchi.javafxproject.models.entities.Product;
import melina.maouchi.javafxproject.repositories.interfaces.ProductRepository;
import melina.maouchi.javafxproject.repositories.impl.ProductRepositoryImpl;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class ProductController {
    private final ProductRepository productRepository;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField stockField;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Product, String> name;
    @FXML
    private TableColumn<Product, Double> price;
    @FXML
    private TableColumn<Product, String> category;
    @FXML
    private TableColumn<Product, Integer> stock;

    public ProductController() {
        this.productRepository = new ProductRepositoryImpl();
    }

    @FXML
    public void initialize() {
        if (productTable == null || name == null || price == null || category == null || stock == null) {
            System.err.println("FXML injection failed. Check fx:id attributes in FXML file.");
            return;
        }

        categoryComboBox.getItems().addAll("Electronics", "Clothing", "Furniture", "Automotive", "Accessories", "beauty","Games");

        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        price.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        category.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        stock.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStock()));

        loadProducts();

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void populateFields(Product product) {
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        categoryComboBox.setValue(product.getCategory());
        stockField.setText(String.valueOf(product.getStock()));
    }



    private void loadProducts() {
        try {
            ObservableList<Product> products = FXCollections.observableArrayList(productRepository.findAll());
            productTable.setItems(products);
        } catch (Exception e) {
            System.err.println("Error loading products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void addProduct() {
        try {
            Product newProduct = new Product();
            newProduct.setName(nameField.getText());
            newProduct.setPrice(Double.parseDouble(priceField.getText()));
            newProduct.setCategory(categoryComboBox.getValue());
            newProduct.setStock(Integer.parseInt(stockField.getText()));

            productRepository.add(newProduct);
            loadProducts();
            clearFields();
        } catch (Exception e) {
            System.err.println("Error adding product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteProduct() {
        try {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                productRepository.delete(selectedProduct.getId());
                loadProducts();
            }
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void updateProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            selectedProduct.setName(nameField.getText());
            selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
            selectedProduct.setCategory(categoryComboBox.getValue());
            selectedProduct.setStock(Integer.parseInt(stockField.getText()));

            productRepository.update(selectedProduct);
            productTable.refresh();
        }
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
        categoryComboBox.setValue(null);
        stockField.clear();
    }

    @FXML
    public void onSearch() {
        String query = searchField.getText().trim();
        ObservableList<Product> results = FXCollections.observableArrayList(productRepository.searchProducts(query));
        productTable.setItems(results);
    }
    @FXML
    private void handleLogout() {
        try {

            // Utiliser la méthode navigateTo pour charger la page de connexion
            HelloApplication.navigateTo("login-view.fxml");

        } catch (Exception e) {
            throw new RuntimeException(e);
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