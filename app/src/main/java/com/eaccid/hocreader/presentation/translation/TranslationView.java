package com.eaccid.hocreader.presentation.translation;

import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.presentation.Notifiable;

import java.util.List;

public interface TranslationView extends BaseView, Notifiable {
    void showContextWord(String title);

    void showBaseWord(String text);

    void showWordTranscription(String text);

    void showSpeaker(boolean isSpeaking);

    void showTranslations(List<String> list);
}
