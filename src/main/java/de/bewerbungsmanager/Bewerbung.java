package de.bewerbungsmanager;

import java.util.ArrayList;

public class Bewerbung {
    private String unternehmen;
    private String stelle;
    private String status;
    private int erinnerungsIntervall;
    private ArrayList<String> dokumente = new ArrayList<>();

    public Bewerbung(String unternehmen, String stelle, String status, int erinnerungsIntervall) {
        this.unternehmen = unternehmen;
        this.stelle = stelle;
        this.status = status;
        this.erinnerungsIntervall = erinnerungsIntervall;
    }

    public String getUnternehmen() {
        return unternehmen;
    }

    public void setUnternehmen(String unternehmen) {
        this.unternehmen = unternehmen;
    }

    public String getStelle() {
        return stelle;
    }

    public void setStelle(String stelle) {
        this.stelle = stelle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getErinnerungsIntervall() {
        return erinnerungsIntervall;
    }

    public void setErinnerungsIntervall(int erinnerungsIntervall) {
        this.erinnerungsIntervall = erinnerungsIntervall;
    }

    public void dokumentHinzufuegen(String dateipfad) {
        dokumente.add(dateipfad);
    }

    public ArrayList<String> getDokumente() {
        return dokumente;
    }
}
