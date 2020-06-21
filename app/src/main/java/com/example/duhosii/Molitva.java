package com.example.duhosii;

public class Molitva {

    String naslov;
    String datum;
    String tekst;

    public Molitva(String naslov, String datum, String tekst) {
        this.naslov = naslov;
        this.datum = datum;
        this.tekst = tekst;
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

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
}
