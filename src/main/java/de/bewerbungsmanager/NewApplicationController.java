package de.bewerbungsmanager;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;

public class NewApplicationController {

    @FXML
    private TextField unternehmenField;

    @FXML
    private TextField stelleField;

    @FXML
    private ComboBox<String> statusBox;

    @FXML
    private TextField erinnerungsIntervallField;

    @FXML
    private void initialize() {
        statusBox.getItems().addAll(
                "Noch nicht versendet",
                "Bewerbung versendet",
                "Rückmeldung ausstehend",
                "Vorstellungsgespräch",
                "Absage",
                "Zusage");

        statusBox.setValue("Noch nicht versendet");
    }
}
