package com.eaccid.hocreader.provider.fromtext;

public class WordFromTextImpl implements WordFromText {

    private String text;
    private String sentence;
    private int pageNumber;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getSentence() {
        return sentence;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public String toString() {
        return "WordFromTextImpl{" +
                "text='" + text + '\'' +
                ", sentence='" + sentence + '\'' +
                '}';
    }
}
