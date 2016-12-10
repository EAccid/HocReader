package com.eaccid.bookreader.file.pagesplitter;

public class CharsetName {

    private static final String WINDOWS_1251 = "Windows-1251";
    private static final String ISO_8859_1 = "ISO-8859-1";
    private static final String UTF_8 = "UTF-8";

    public static String getCharset() {

        //TODO settings: Charset
//        Charset.isSupported()
//    } catch (UnsupportedEncodingException e) {
//        e.printStackTrace();
        return WINDOWS_1251;
    }
}
