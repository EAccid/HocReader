package com.eaccid.hocreader.ins.impl;

import com.eaccid.hocreader.ins.TextManager;

public class TextManagerImpl implements TextManager {
    @Override
    public String capitalizeFirsChar(String text) {
        if (text == null || text.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder(text);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}
