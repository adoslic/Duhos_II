package com.duhos.duhosii.models;

public class Question {

    String pitanje;
    String odgovor;

    public Question(String pitanje, String odgovor) {
        this.pitanje = pitanje;
        this.odgovor = odgovor;
    }

    public String getPitanje() {
        return pitanje;
    }

    public void setPitanje(String pitanje) {
        this.pitanje = pitanje;
    }

    public String getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(String odgovor) {
        this.odgovor = odgovor;
    }
}
