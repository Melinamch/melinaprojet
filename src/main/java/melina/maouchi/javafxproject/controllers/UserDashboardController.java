package melina.maouchi.javafxproject.controllers;

import melina.maouchi.javafxproject.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UserDashboardController {

    @FXML
    private void handleViewProducts(ActionEvent event) {
        HelloApplication.navigateTo("products-view.fxml");
    }

    @FXML
    private void handleViewOrders(ActionEvent event) {
        HelloApplication.navigateTo("orders-view.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        HelloApplication.navigateTo("login-view.fxml");
    }
}
