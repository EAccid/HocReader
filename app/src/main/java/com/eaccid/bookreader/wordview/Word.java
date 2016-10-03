package com.eaccid.bookreader.wordview;

public class Word {

    String text;
    String sentance;

    public void setText(String text) {
        this.text = text;
    }

    public void setSentance(String sentance) {
        this.sentance = sentance;
    }

    @Override
    public String toString() {
        return "Word{" +
                "text='" + text + '\'' +
                ", sentance='" + sentance + '\'' +
                '}';
    }
}
