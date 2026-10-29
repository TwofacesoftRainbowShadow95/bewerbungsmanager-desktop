module de.bewerbungsmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens de.bewerbungsmanager to javafx.fxml;

    exports de.bewerbungsmanager;
}
