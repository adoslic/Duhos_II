package com.example.duhosii;

class AlarmDates {
    String datum;
    String sati;
    String minute;

    public AlarmDates(String datum, String sati, String minute) {
        this.datum = datum;
        this.sati = sati;
        this.minute = minute;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getSati() {
        return sati;
    }

    public void setSati(String sati) {
        this.sati = sati;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
}
