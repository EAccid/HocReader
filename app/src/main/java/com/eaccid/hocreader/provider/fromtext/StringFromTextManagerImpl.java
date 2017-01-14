package com.eaccid.hocreader.provider.fromtext;

public class StringFromTextManagerImpl implements StringFromTextManager {
    @Override
    public String capitalizeFirsChar(String text) {
        if (text == null || text.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder(text);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}
