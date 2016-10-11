package com.eaccid.bookreader.file;

enum FileExtensions {
    TXT, PDF;

    public String getExtension() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return name();
    }
}
