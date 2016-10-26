package com.eaccid.libtranslator.dictionary;

public interface Dictionary {

    boolean authorize(String login, String password);

    boolean isAuth();

    boolean addWord(String word, String textTranslation, String context);

}
