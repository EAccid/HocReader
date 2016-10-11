package com.eaccid.translator.translator;

import java.util.List;

public interface TextTranslation {
    String getTranscription();

    List<String> getTranslates();

    String getSoundUrl();

    String getPicUrl();

    boolean isEmpty();

    @Override
    String toString();
}
