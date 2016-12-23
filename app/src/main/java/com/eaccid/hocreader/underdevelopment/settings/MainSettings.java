package com.eaccid.hocreader.underdevelopment.settings;

import android.content.Context;
import android.provider.SearchRecentSuggestions;
import com.eaccid.hocreader.presentation.activity.main.serchadapter.SearchSuggestionsProvider;

public class MainSettings {

    public void clearBookSearchHistory(Context context) {
        // TODO 1. provide a confirmation dialog to verify that the user wants to delete their search history
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(context, SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
        suggestions.clearHistory();
    }
}
