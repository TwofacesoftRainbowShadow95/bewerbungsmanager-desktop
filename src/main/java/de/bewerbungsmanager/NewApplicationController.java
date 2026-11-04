package de.bewerbungsmanager;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NewApplicationController {

    @FXML
    private TextField unternehmenField;

    @FXML
    private TextField stelleField;

    @FXML
    private ComboBox<String> statusBox;

    @FXML
    private TextField erinnerungsIntervallField;

    private ApplicationsController applicationsController;

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

    public void setApplicationsController(ApplicationsController controller) {
        this.applicationsController = controller;
    }

    @FXML
    private void abbrechen() {
        unternehmenField.getScene().getWindow().hide();
    }

    @FXML
    private void speichern() {
        String unternehmen = unternehmenField.getText().trim();
        String stelle = stelleField.getText().trim();
        String status = statusBox.getValue();
        String erinnerungsText = erinnerungsIntervallField.getText().trim();

        if (unternehmen.isEmpty() || stelle.isEmpty() || erinnerungsText.isEmpty() || status == null) {
            Alert meldung = new Alert(AlertType.WARNING);
            meldung.setTitle("Fehlende Angaben");
            meldung.setHeaderText("Bitte alle Pflichtfelder ausfüllen");
            meldung.setContentText("Unternehmen, Stelle, Status und Erinnerungsintervall werden benötigt.");
            meldung.showAndWait();
            return;
        }

        int erinnerungsIntervall;

        try {
            erinnerungsIntervall = Integer.parseInt(erinnerungsText);

            if (erinnerungsIntervall < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert meldung = new Alert(AlertType.WARNING);
            meldung.setTitle("Ungültiges Erinnerungsintervall");
            meldung.setHeaderText("Bitte gib eine gültige Zahl ein.");
            meldung.setContentText("Das Erinnerungsintervall muss eine Zahl ab 0 sein.");
            meldung.showAndWait();
            return;
        }

        Bewerbung neueBewerbung = new Bewerbung(unternehmen, stelle, status, erinnerungsIntervall);

        Datenbank.speichern(neueBewerbung);

        applicationsController.bewerbungHinzufuegen(neueBewerbung);
        unternehmenField.getScene().getWindow().hide();
    }
}
