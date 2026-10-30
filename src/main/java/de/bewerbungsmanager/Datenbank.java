package de.bewerbungsmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Datenbank {

    public static Connection verbinden() throws SQLException, java.io.IOException {
        Path datenOrdner = Path.of(System.getProperty("user.home"), "BewerbungsManager");
        Files.createDirectories(datenOrdner);
        Path datenbankDatei = datenOrdner.resolve("bewerbungsmanager.db");
        String url = "jdbc:sqlite:" + datenbankDatei;
        return DriverManager.getConnection(url);
    }

    public static void verbindungTesten() {
        try (Connection verbindung = verbinden()) {
            System.out.println("SQLite-Verbindung erfolgreich!");
            System.out.println("Datenbank ist geöffnet.");
        } catch (Exception e) {
            System.out.println("Verbindung fehlgeschlagen!");
            e.printStackTrace();
        }
    }

}
