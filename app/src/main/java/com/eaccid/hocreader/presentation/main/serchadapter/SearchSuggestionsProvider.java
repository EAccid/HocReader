package com.eaccid.hocreader.presentation.main.serchadapter;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.eaccid.hocreader.presentation.main.serchadapter.SearchSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public SearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
