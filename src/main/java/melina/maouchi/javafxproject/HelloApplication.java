package melina.maouchi.javafxproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("JavaFX Application");
        navigateTo("login-view.fxml");  // Charger la vue de connexion au démarrage
        stage.show();
    }

    public static void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            if (fxmlLoader.getLocation() == null) {
                throw new IOException("FXML file not found: " + fxmlFile);
            }
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            primaryStage.setScene(scene);

            String title = fxmlFile.replace(".fxml", "").replace("-", " ");
            setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load view");
            alert.setContentText("The requested view could not be loaded: " + fxmlFile);
            alert.showAndWait();
        }
    }

    public static void setTitle(String title) {
        primaryStage.setTitle("JavaFX Application - " + title);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}

