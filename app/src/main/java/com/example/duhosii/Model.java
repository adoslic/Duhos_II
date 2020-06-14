package com.example.duhosii;

public class Model {

    String naslov;
    String datum;

    public Model(String naslov, String datum) {
        this.naslov = naslov;
        this.datum = datum;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
