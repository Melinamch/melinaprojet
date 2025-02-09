package melina.maouchi.javafxproject.controllers;

import melina.maouchi.javafxproject.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import melina.maouchi.javafxproject.config.DatabaseConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    // Méthode pour se connecter
    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Connexion à la base de données (assurez-vous d'avoir une méthode pour récupérer la connexion)
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users WHERE name = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");

                if (role.equals("admin")) {
                    // Charger l'interface admin
                    HelloApplication.navigateTo("admin-dashboard.fxml");
                } else if (role.equals("user")) {
                    // Charger l'interface utilisateur
                    HelloApplication.navigateTo("user-dashboard.fxml");
                }
            } else {
                showAlert("Erreur de connexion", "Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la connexion.");
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
