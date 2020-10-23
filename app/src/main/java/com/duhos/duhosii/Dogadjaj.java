package com.duhos.duhosii;

class Dogadjaj {

    String naslov;
    String opis;
    String datum;
    String lokacija;
    String vrijeme;
    public Dogadjaj(String naslov, String opis, String datum, String vrijeme,String lokacija) {
        this.naslov=naslov;
        this.opis=opis;
        this.datum=datum;
        this.lokacija=lokacija;
        this.vrijeme=vrijeme;

    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
