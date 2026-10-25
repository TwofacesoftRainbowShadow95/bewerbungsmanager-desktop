module de.bewerbungsmanager {
    requires javafx.controls;
    requires javafx.fxml;

    opens de.bewerbungsmanager to javafx.fxml;

    exports de.bewerbungsmanager;
}
