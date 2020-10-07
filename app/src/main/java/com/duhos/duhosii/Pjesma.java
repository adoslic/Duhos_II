package com.duhos.duhosii;

public class Pjesma {

    String naslov;
    String bend;
    String tekstPjesme;
    String link;

    public Pjesma(String naslov, String bend,String tekstPjesme,String link) {
        this.naslov = naslov;
        this.bend = bend;
        this.tekstPjesme=tekstPjesme;
        this.link=link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
