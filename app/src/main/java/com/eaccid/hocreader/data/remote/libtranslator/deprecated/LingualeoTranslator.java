package com.eaccid.hocreader.data.remote.libtranslator.deprecated;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.RequestHandler;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.RequestParameters;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.translator.WordTranslation;

public class LingualeoTranslator implements Translator {

    private WordTranslation translation;

    @Override
    public boolean translate(String word) {
        RequestParameters requestParameters = new RequestParameters();
        requestParameters.addParameter("word", word);
        RequestHandler requestHandler = RequestHandler.newUnauthorizedRequestWithParameters("http://lingualeo.com/api/gettranslates", requestParameters);
        requestHandler.handleRequest();
        translation = new WordTranslation(requestHandler.getResponse());
        return !translation.isEmpty();
    }

    @Override
    public WordTranslation getTranslations() throws NullPointerException {
        if (translation == null) throw new NullPointerException("WordFromTextImpl has not been translated");
        return translation;
    }

}
