package com.duhos.duhosii.models;

import io.realm.RealmObject;

public class AlarmDate extends RealmObject {
    String datum;
    String vrijeme;
    String naslov;
    int alarmID;

    public AlarmDate() { }

    public String getDatum() {
        return datum;
    }

    public String getNaslov() { return naslov; }

    public void setNaslov(String naslov) { this.naslov = naslov; }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public void setAlarmID(int alarmID) { this.alarmID = alarmID; }

    public int getAlarmID() {
        return alarmID;
    }
}
