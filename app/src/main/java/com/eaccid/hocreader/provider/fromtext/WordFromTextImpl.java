package com.eaccid.hocreader.provider.fromtext;

class WordFromTextImpl implements WordFromText {
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

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public String toString() {
        return "WordFromTextImpl{" +
                "text='" + text + '\'' +
                ", sentence='" + sentence + '\'' +
                '}';
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    @Override
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

}
