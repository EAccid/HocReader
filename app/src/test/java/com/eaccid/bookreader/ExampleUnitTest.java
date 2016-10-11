package com.eaccid.bookreader;


import com.eaccid.translator.lingualeo.translator.LingualeoTranslator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        LingualeoTranslator translator = new LingualeoTranslator();
        translator.translate("mad");
        System.out.println(translator.getTranslations());




    }
}