package melina.maouchi.javafxproject.controllers;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import melina.maouchi.javafxproject.repositories.impl.LoginRepository;
import melina.maouchi.javafxproject.HelloApplication;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final LoginRepository loginRepository = new LoginRepository();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        String role = loginRepository.authenticateUser(username, password);

        if (role != null) {
            showAlert("Succès", "Login successfully as " + role + " !");
            if ("admin".equals(role)) {
                HelloApplication.navigateTo("products-view.fxml");
            } else {
                HelloApplication.navigateTo("user-view.fxml");
            }
        } else {
            showAlert("Erreur", "Incorrect identifiers !");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
