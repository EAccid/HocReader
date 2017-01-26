package com.eaccid.hocreader.data.remote.libtranslator.deprecated;

import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;

public interface Translator {

    @Deprecated
    boolean translate(String word);

    @Deprecated
    TextTranslation getTranslations();
}
