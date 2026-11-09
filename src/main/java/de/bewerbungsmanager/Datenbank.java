package de.bewerbungsmanager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public static void tabellenErstellen() {

        try (Connection verbindung = verbinden(); Statement statement = verbindung.createStatement()) {
            String sql = """
                    CREATE TABLE IF NOT EXISTS bewerbungen (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    unternehmen TEXT NOT NULL,
                    stelle TEXT NOT NULL,
                    status TEXT,
                    erinnerungs_intervall INTEGER,
                    kontakt TEXT,
                    notizen TEXT)""";

            statement.execute(sql);
            System.out.println("Tabelle bewerbungen wurde erstellt!");

        } catch (Exception e) {
            System.out.println("Fehler beim Erstellen der Tabelle!");
            e.printStackTrace();
        }
    }

    public static void speichern(Bewerbung bewerbung) {
        String sql = """
                INSERT INTO bewerbungen
                (unternehmen, stelle, status, erinnerungs_intervall)
                VALUES(?, ?, ?, ?)
                """;

        try (Connection verbindung = verbinden();
                PreparedStatement statement = verbindung.prepareStatement(sql)) {

            statement.setString(1, bewerbung.getUnternehmen());
            statement.setString(2, bewerbung.getStelle());
            statement.setString(3, bewerbung.getStatus());
            statement.setInt(4, bewerbung.getErinnerungsIntervall());

            statement.executeUpdate();

            try (Statement idAbfrage = verbindung.createStatement();
                    ResultSet ergebnis = idAbfrage.executeQuery("SELECT last_insert_rowid()")) {

                if (ergebnis.next()) {
                    int neueId = ergebnis.getInt(1);
                    bewerbung.setId(neueId);
                }
            }

            System.out.println("Bewerbung wurde gespeichert!");

        } catch (Exception e) {
            System.out.println("Fehler beim speichern der Bewerbung!");
            e.printStackTrace();
        }
    }

    public static void aktualisieren(Bewerbung bewerbung) {
        String sql = """
                UPDATE bewerbungen
                SET unternehmen = ?,
                    stelle = ?,
                    status = ?,
                    erinnerungs_intervall = ?
                WHERE id = ?
                """;

        try (Connection verbindung = verbinden();
                PreparedStatement statement = verbindung.prepareStatement(sql)) {

            statement.setString(1, bewerbung.getUnternehmen());
            statement.setString(2, bewerbung.getStelle());
            statement.setString(3, bewerbung.getStatus());
            statement.setInt(4, bewerbung.getErinnerungsIntervall());
            statement.setInt(5, bewerbung.getId());

            statement.executeUpdate();

            System.out.println("Bewerbung wurde aktualisiert!");

        } catch (Exception e) {
            System.out.println("Fehler beim Aktualisieren der Bewerbung!");
            e.printStackTrace();
        }
    }

    public static boolean loeschen(int id) {
        String sql = "DELETE FROM bewerbungen WHERE id = ?";

        try (Connection verbindung = verbinden();
                PreparedStatement statement = verbindung.prepareStatement(sql)) {
            statement.setInt(1, id);
            int betroffeneZeilen = statement.executeUpdate();
            return betroffeneZeilen > 0;

        } catch (Exception e) {
            System.out.println("Fehler beim Löschen der Bewerbung!");
            e.printStackTrace();
            return false;
        }

    }

    public static List<Bewerbung> laden() {
        List<Bewerbung> bewerbungen = new ArrayList<>();

        String sql = "SELECT * FROM bewerbungen";

        try (Connection verbindung = verbinden();
                PreparedStatement statement = verbindung.prepareStatement(sql);
                ResultSet ergebnis = statement.executeQuery()) {

            while (ergebnis.next()) { // next gehört zu dem Java-Paket SQL
                int id = ergebnis.getInt("id");
                String unternehmen = ergebnis.getString("unternehmen");
                String stelle = ergebnis.getString("stelle");
                String status = ergebnis.getString("status");
                int intervall = ergebnis.getInt("erinnerungs_intervall");

                Bewerbung neueBewerbung = new Bewerbung(id, unternehmen, stelle, status, intervall); // hier werden
                // Java-Objekte erstellt (für die graphische Oberfläche zur Darstellung der
                // Objekte der SQLite?) für die ArrayList als Ausgabe.

                bewerbungen.add(neueBewerbung);
            }

        } catch (Exception e) {
            System.out.println("Fehler beim Laden der Bewerbungen!");
            e.printStackTrace();
        }

        return bewerbungen;
    }

}
