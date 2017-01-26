package com.eaccid.hocreader.presentation.main.serchadapter;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider  {

    public final static String AUTHORITY = "SearchSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionsProvider() {
            setupSuggestions(AUTHORITY, MODE);
    }
}
