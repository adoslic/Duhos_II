package com.example.duhosii;

public class Pjesma {

    String naslov;
    String bend;
    String tekstPjesme;

    public Pjesma(String naslov, String bend,String tekstPjesme) {
        this.naslov = naslov;
        this.bend = bend;
        this.tekstPjesme=tekstPjesme;
    }
    public String getTekstPjesme() { return tekstPjesme; }
    public void setTekstPjesme(String tekstPjesme) { this.tekstPjesme = tekstPjesme; }
    public String getNaslov() {
        return naslov;
    }
    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }
    public String getBend() {
        return bend;
    }
    public void setBend(String bend) {
        this.bend = bend;
    }
}
