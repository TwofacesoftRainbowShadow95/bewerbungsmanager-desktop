package de.bewerbungsmanager;

import java.io.IOException;
import javafx.fxml.FXML;

public class SettingsController {
    @FXML
    private void backToHome() throws IOException {
        App.setRoot("primary");
    }
}
