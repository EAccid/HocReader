package com.eaccid.bookreader.wordview;

public class Word {

    private String text;

    public String getText() {
        return text;
    }

    public String getSentance() {
        return sentance;
    }

    private String sentance;

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
