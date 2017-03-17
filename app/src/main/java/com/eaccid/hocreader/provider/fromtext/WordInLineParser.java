package com.eaccid.hocreader.provider.fromtext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO refactor parser
public class WordInLineParser {
    private String text;
    private int startOfLine;
    private int offset;
    private int endOfLine;
    private int charStart = 0;
    private String word = "";
    private String sentence = "";

    public WordInLineParser(String text, int startOfLine, int offset, int endOfLine) {
        this.text = text;
        this.startOfLine = startOfLine;
        this.offset = offset;
        this.endOfLine = endOfLine;
    }

    public void parse() {
        word = getWordFromLineAndSetCharStart();
        sentence = getSentenceFromText();
    }

    public String getWord() {
        return word;
    }

    public int getWordStart() {
        return charStart;
    }


    public int getWordEnd() {
        return  getWordStart() + word.length();
    }

    public String getSentence() {
        return sentence;
    }

    private String getWordFromLineAndSetCharStart() {
        String firstPartOfWord = getFirstPartOfWordInLine(text.subSequence(startOfLine, offset));
        String lastPartOfWord = getLastPartOfWordInLine(text.subSequence(offset, endOfLine));
        charStart = offset - firstPartOfWord.length();
        return firstPartOfWord + lastPartOfWord;
    }

    private String getSentenceFromText() {
        return getMatchingResult(text.replace("\n\n", " ").trim(),
                Pattern.compile("(?<=\\.)(\\w|\\s|,|'|`|-|–|\")+" + (text.subSequence(startOfLine, endOfLine)) + ".+?(\\.)"));
    }

    private String getFirstPartOfWordInLine(CharSequence sublineBeforeClickedChar) {
        return getMatchingResult(sublineBeforeClickedChar, Pattern.compile("(\\w+)$"));
    }

    private String getLastPartOfWordInLine(CharSequence sublineAfterClickedChar) {
        return getMatchingResult(sublineAfterClickedChar, Pattern.compile("^([\\w\\-]+)"));
    }

    private String getMatchingResult(CharSequence line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        try {
            if (matcher.find())
                return matcher.group(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}