package com.eaccid.libtranslator.lingualeo.translator;

import com.eaccid.libtranslator.lingualeo.connection.LingualeoResponse;
import com.eaccid.libtranslator.translator.TextTranslation;
import java.util.List;

public class WordTranslation implements TextTranslation {

    LingualeoResponse lingualeoResponse;
    List<String> translates;
    String transcription;
    String soundUrl;
    String picUrl;
    String word;

    @Override
    public String getWord() {
        return word;
    }

    public WordTranslation (LingualeoResponse lingualeoResponse) {
        initLingualeoResponse(lingualeoResponse);
        loadTranslateData();
    }

    @Override
    public String getTranscription() {
        return transcription;
    }

    @Override
    public List<String> getTranslates() {
        return translates;
    }

    @Override
    public String getSoundUrl() {
        return soundUrl;
    }

    @Override
    public String getPicUrl() {
        return picUrl;
    }

    @Override
    public boolean isEmpty() {
        return lingualeoResponse.isEmpty();
    }

    @Override
    public String toString() {
        return lingualeoResponse.toString();
    }

    private void initLingualeoResponse(LingualeoResponse lingualeoResponse) {

        this.lingualeoResponse = lingualeoResponse == null ? new LingualeoResponse() : lingualeoResponse;

    }

    private void loadTranslateData() {

        word = lingualeoResponse.getString("word");
        translates = lingualeoResponse.getListString("value");
        transcription = lingualeoResponse.getString("transcription");
        soundUrl = lingualeoResponse.getString("sound_url");
        picUrl = lingualeoResponse.getString("pic_url");

    }

    //TODO delete; just for practicing reflection
    private String readJsonResponseFromLingualeoAsString() {
        return this.toString();
    }

}
