package com.eaccid.hocreader.provider.fromtext;

import java.io.Serializable;

public interface WordFromText extends Serializable {

    String getText();

    String getSentence();

    int getPageNumber();

    void setText(String text);

    void setSentence(String text);

    void setPageNumber(int page);

}
