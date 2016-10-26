package com.eaccid.bookreader.searchbooks;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider  {

    public final static String AUTHORITY = "com.eaccid.bookreader.searcher.SearchSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionsProvider() {
            setupSuggestions(AUTHORITY, MODE);
    }
}
