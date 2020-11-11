package com.duhos.duhosii.models;

public class Song {

    String naslov;
    String bend;
    String tekstPjesme;
    String link;
    String youtubeLink;

    public Song(String naslov, String bend, String tekstPjesme, String link, String youtubeLink) {
        this.naslov = naslov;
        this.bend = bend;
        this.tekstPjesme=tekstPjesme;
        this.link=link;
        this.youtubeLink=youtubeLink;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }
    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
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
