package de.bewerbungsmanager;

import java.util.ArrayList;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;

public class ApplicationsController {

    @FXML
    private TableView<Bewerbung> applicationsTable;

    @FXML
    private ListView<String> dokumentenListe;

    @FXML
    private TableColumn<Bewerbung, String> unternehmenColumn;

    @FXML
    private TableColumn<Bewerbung, String> stelleColumn;

    @FXML
    private TableColumn<Bewerbung, String> statusColumn;

    @FXML
    private TableColumn<Bewerbung, Integer> erinnerungColumn;

    private ObservableList<Bewerbung> bewerbungen = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        bewerbungen.add(new Bewerbung("Muster GmbH", "Fachinformatiker/in", "versendet", 3));
        bewerbungen.add(new Bewerbung("Beispiel AG", "Anwendungsentwicklung", "Vorstellungsgespräch", 1));
        bewerbungen.add(new Bewerbung("Demo IT", "Softwareentwicklung", "Rückmeldung ausstehend", 5));

        applicationsTable.setItems(bewerbungen);

        unternehmenColumn.setCellValueFactory(new PropertyValueFactory<>("unternehmen"));

        stelleColumn.setCellValueFactory(new PropertyValueFactory<>("stelle"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        erinnerungColumn.setCellValueFactory(new PropertyValueFactory<>("erinnerungsIntervall"));

        applicationsTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, alteBewerbung, neueBewerbung) -> {
                    dokumentenListe.getItems().clear();
                    System.out.println("Dokumentenliste verbunden: " + dokumentenListe);
                    System.out.println("Listeneinträge nach dem Leeren: " + dokumentenListe.getItems());

                    if (neueBewerbung != null) {
                        for (String dateipfad : neueBewerbung.getDokumente()) {
                            dokumentenListe.getItems().add(new java.io.File(dateipfad).getName());
                        }
                    }
                });
    }

    @FXML
    private void backToHome() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void neueBewerbung() {
        Alert meldung = new Alert(Alert.AlertType.INFORMATION);
        meldung.setTitle("Neue Bewerbung");
        meldung.setHeaderText("Bewerbung anlegen");
        meldung.setContentText("Hier entsteht später das Formular für neue Bewerbungen");
        meldung.showAndWait();
    }

    @FXML
    private void dokumentHinzufuegen() {
        Bewerbung ausgewaehlteBewerbung = applicationsTable.getSelectionModel().getSelectedItem();

        if (ausgewaehlteBewerbung == null) {
            System.out.println("Bitte zuerst eine Bewerbung auswählen!");
            return;
        }

        System.out.println("Ausgewählt: " + ausgewaehlteBewerbung.getUnternehmen());

        FileChooser dateiauswahl = new FileChooser();

        dateiauswahl.setTitle("Dokument auswählen");

        File ausgewaehlteDatei = dateiauswahl.showOpenDialog(applicationsTable.getScene().getWindow());

        if (ausgewaehlteDatei == null) {
            return;
        }
        System.out.println("Datei ausgewählt" + ausgewaehlteDatei.getAbsolutePath());
        ausgewaehlteBewerbung.dokumentHinzufuegen(ausgewaehlteDatei.getAbsolutePath());
        dokumentenListe.getItems().add(new java.io.File(ausgewaehlteDatei.getAbsolutePath()).getName());
        System.out.println("Einträge in der Liste: " + dokumentenListe.getItems());
    }
}
