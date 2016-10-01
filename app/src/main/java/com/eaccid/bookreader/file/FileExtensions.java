package com.eaccid.bookreader.file;

/**
 * Created by AA on 01.10.2016.
 */
public enum FileExtensions {
    TXT, PDF;

    public String getExtension() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return name();
    }
}
