package de.bewerbungsmanager;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("settings");
    }

    @FXML
    private void showApplications() throws IOException {
        App.setRoot("applications");
    }
}
