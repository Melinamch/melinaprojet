module melina.maouchi.javafxproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens melina.maouchi.javafxproject to javafx.fxml;
    opens melina.maouchi.javafxproject.controllers to javafx.fxml;

    exports melina.maouchi.javafxproject;
    exports melina.maouchi.javafxproject.controllers to javafx.fxml;
    opens melina.maouchi.javafxproject.models.entities to javafx.base;
    exports melina.maouchi.javafxproject.models.entities;
}