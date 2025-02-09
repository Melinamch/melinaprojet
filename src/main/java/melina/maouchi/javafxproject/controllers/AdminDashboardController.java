package melina.maouchi.javafxproject.controllers;

import melina.maouchi.javafxproject.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminDashboardController {

    @FXML
    private void handleManageUsers(ActionEvent event) {
        HelloApplication.navigateTo("customer-view.fxml");
    }

    @FXML
    private void handleManageProducts(ActionEvent event) {
        HelloApplication.navigateTo("products-view.fxml");
    }

    @FXML
    private void handleManageOrders(ActionEvent event) {
        HelloApplication.navigateTo("order-orders.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        HelloApplication.navigateTo("invoice-view.fxml");
    }
}
