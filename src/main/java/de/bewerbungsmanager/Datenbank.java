package de.bewerbungsmanager;

import java.sql.Connection;
import java.sql.DriverManager;

public class Datenbank {

    private static final String URL = "jdbc:sqlite:bewerbungsmanager.db";

    public static Connection verbinden() throws java.sql.SQLException {
        return DriverManager.getConnection(URL);
    }

}
