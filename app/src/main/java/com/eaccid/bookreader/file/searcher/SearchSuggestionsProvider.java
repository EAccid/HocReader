package com.eaccid.bookreader.file.searcher;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider  {

    public final static String AUTHORITY = "com.eaccid.bookreader.FilesSearcher.SearchSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionsProvider() {
            setupSuggestions(AUTHORITY, MODE);
    }
}
