package com.eaccid.translator.lingualeo.translator;


import com.eaccid.translator.lingualeo.connection.RequestHandler;
import com.eaccid.translator.lingualeo.connection.RequestParameters;
import com.eaccid.translator.translator.Translator;

public class LingualeoTranslator implements Translator {

    WordTranslation translation;

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
        if (translation == null) throw new NullPointerException("WordFromText has not been translated");
        return translation;
    }

}
