package com.example.duhosii;

public class Medij {

    String naslov;
    String sadrzaj;
    String medij;
    String link;

    public Medij(String naslov, String sadrzaj,String medij, String link) {
        this.naslov = naslov;
        this.sadrzaj = sadrzaj;
        this.medij = medij;
        this.link =link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public String getMedij() {
        return medij;
    }

    public void setMedij(String medij) {
        this.medij = medij;
    }
}
