package com.eaccid.bookreader.wordgetter;

import java.io.Serializable;

public class WordFromText implements Serializable{

    private String text;
    private String sentence;
    private int pageNumber;

    public String getText() {
        return text;
    }

    public String getSentence() {
        return sentence;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public String toString() {
        return "WordFromText{" +
                "text='" + text + '\'' +
                ", sentence='" + sentence + '\'' +
                '}';
    }
}
