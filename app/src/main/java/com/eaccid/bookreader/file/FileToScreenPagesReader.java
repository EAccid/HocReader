package com.eaccid.bookreader.file;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import rx.Observable;


public class FileToScreenPagesReader {

    //TODO: inject
    private CharactersDefinerForFullScreenTextView parameters;

    public FileToScreenPagesReader(Activity activity) {
        parameters = new CharactersDefinerForFullScreenTextView(activity);
    }

    //TODO refactor: split on methods / make readable
    public void readListFromFile(String filePath, List<String> list) {

        BufferedReader bufferedReader = null;
        try {
            String line;
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), CharsetName.getCharset()));

            String lineFromFile = "";
            boolean isEndOfPage;
            StringBuilder tb = new StringBuilder();
            int charsOnPage = parameters.getCharactersOnPage();

            int i = 0;

            while ((line = bufferedReader.readLine()) != null) {
                lineFromFile = lineFromFile + line;

                if ((lineFromFile = lineFromFile.trim()).isEmpty()) continue;

                lineFromFile = lineFromFile + "\n\n";
                i++;

                int currentCharNumber = lineFromFile.length();
                int remained = Math.max(charsOnPage - tb.length() - (parameters.getCharactersOnLine() * i), 0);
                int charToAppend = Math.min(currentCharNumber, remained);

                String toAppend = lineFromFile.substring(0, charToAppend);
                tb.append(toAppend);

                isEndOfPage = (remained == 0);

                if (isEndOfPage) {

                    String charToDel = tb.substring(tb.lastIndexOf(" "), tb.length());
                    tb.delete(tb.length() - charToDel.length(), tb.length());

                    list.add(tb.toString());

                    lineFromFile = charToDel + lineFromFile;


                    tb = new StringBuilder();
                    i = 0;

                } else {
                    lineFromFile = lineFromFile.substring(charToAppend, lineFromFile.length());
                }

            }
            System.out.println("END");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}