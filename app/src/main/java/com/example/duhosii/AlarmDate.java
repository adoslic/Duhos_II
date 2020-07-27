package com.example.duhosii;

import io.realm.RealmObject;

public class AlarmDate extends RealmObject {
    String datum;
    String vrijeme;

    public AlarmDate() {

    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }
}
