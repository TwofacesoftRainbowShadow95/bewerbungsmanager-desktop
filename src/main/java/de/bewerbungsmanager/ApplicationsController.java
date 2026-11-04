package de.bewerbungsmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

    @FXML
    private TableColumn<Bewerbung, Void> aktionenColumn;

    private ObservableList<Bewerbung> bewerbungen = FXCollections.observableArrayList();

    public void bewerbungHinzufuegen(Bewerbung bewerbung) {
        bewerbungen.add(bewerbung);
    }

    private ApplicationsController applicationsController;

    public void setApplicationsController(ApplicationsController controller) {
        this.applicationsController = controller;
    }

    @FXML
    private void initialize() {
        bewerbungen.addAll(Datenbank.laden());

        applicationsTable.setItems(bewerbungen);

        unternehmenColumn.setCellValueFactory(new PropertyValueFactory<>("unternehmen"));

        stelleColumn.setCellValueFactory(new PropertyValueFactory<>("stelle"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        erinnerungColumn.setCellValueFactory(new PropertyValueFactory<>("erinnerungsIntervall"));

        aktionenColumn.setCellFactory(spalte -> new TableCell<Bewerbung, Void>() {

            private final Button loeschenButton = new Button("X");

            {
                loeschenButton.setStyle("-fx-text-fill: red;" + "-fx-background-color: transparent;"
                        + "-fx-font-size: 16px" + "-fx-font-weight: bold;" + "-fx-cursor: hand;");
                loeschenButton.setOnAction(event -> {
                    Bewerbung bewerbung = getTableView().getItems().get(getIndex());
                    bewerbungEntfernen(bewerbung);
                });
            }

            @Override
            protected void updateItem(Void item, boolean leer) {
                super.updateItem(item, leer);

                if (leer || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                Bewerbung ausgewaehlteBewerbung = getTableView().getSelectionModel().getSelectedItem();

                if (getTableRow().getItem() == ausgewaehlteBewerbung) {
                    setGraphic(loeschenButton);
                } else {
                    setGraphic(null);
                }
            }
        });

        applicationsTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, alteBewerbung, neueBewerbung) -> {
                    applicationsTable.refresh();
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
    private void bewerbungEntfernen(Bewerbung bewerbung) {
        Alert meldung = new Alert(AlertType.CONFIRMATION);

        meldung.setTitle("Bewerbung entfernen");
        meldung.setHeaderText("Möchtest du diese Bewerbung wirklich entfernen?");
        meldung.setContentText("Unternehmen: " + bewerbung.getUnternehmen() + "\nstelle: " + bewerbung.getStelle());

        java.util.Optional<javafx.scene.control.ButtonType> ergebnis = meldung.showAndWait();

        if (ergebnis.isPresent() && ergebnis.get() == javafx.scene.control.ButtonType.OK) {

            boolean geloescht = Datenbank.loeschen(bewerbung.getId());

            if (geloescht) {
                bewerbungen.remove(bewerbung);
                dokumentenListe.getItems().clear();
                applicationsTable.getSelectionModel().clearSelection();

                System.out.println("Bewerbung wurde entfernt.");
            } else {
                System.out.println("Bewerbung konnte nicht gelöscht werden!");
            }
        }
    }

    @FXML
    private void neueBewerbung() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("new-application.fxml"));

        Parent root = loader.load();

        NewApplicationController controller = loader.getController();
        controller.setApplicationsController(this);

        Stage fenster = new Stage();
        fenster.setTitle("Neue Bewerbung anlegen");
        fenster.setScene(new Scene(root));
        fenster.show();
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
