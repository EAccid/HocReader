package com.eaccid.translator.lingualeo.translator;

import com.eaccid.translator.lingualeo.connection.LingualeoResponse;
import com.eaccid.translator.translator.TextTranslation;

import java.io.Serializable;
import java.util.List;

public class WordTranslation implements TextTranslation, Serializable {

    LingualeoResponse lingualeoResponse;
    List<String> translates;
    String transcription;
    String soundUrl;
    String picUrl;

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
