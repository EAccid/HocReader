package com.eaccid.hocreader.provider.fromtext;

public class StringFromTextManagerImpl implements StringFromTextManager {
    @Override
    public String capitalizeFirsChar(String text) {
        StringBuilder sb = new StringBuilder(text);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}
