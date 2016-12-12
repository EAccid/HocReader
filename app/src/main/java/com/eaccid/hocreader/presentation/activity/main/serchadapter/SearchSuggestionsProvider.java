package com.eaccid.hocreader.presentation.activity.main.serchadapter;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider  {

    public final static String AUTHORITY = "com.eaccid.hocreader.presentation.activity.main.serchadapter.SearchSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionsProvider() {
            setupSuggestions(AUTHORITY, MODE);
    }
}
