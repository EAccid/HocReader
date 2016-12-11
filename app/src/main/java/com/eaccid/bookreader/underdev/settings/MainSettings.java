package com.eaccid.bookreader.underdev.settings;

import android.app.Activity;
import android.provider.SearchRecentSuggestions;

import com.eaccid.bookreader.searchfiles.SearchSuggestionsProvider;

public class MainSettings {

    private Activity context;

    public MainSettings(Activity context) {
        this.context = context;
    }

    public void setDefaultSettings() {

        LingualeoAuthSettings lingualeoAuthSettings = new LingualeoAuthSettings(context);
        lingualeoAuthSettings.setUp();

    }

    public void clearBookSearchHistory() {
        // TODO 1. provide a confirmation dialog to verify that the user wants to delete their search history
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(context, SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
        suggestions.clearHistory();
    }
}
