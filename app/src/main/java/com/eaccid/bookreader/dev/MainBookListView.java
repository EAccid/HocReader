package com.eaccid.bookreader.dev;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.provider.SearchRecentSuggestions;
import android.support.v7.widget.SearchView;
import android.webkit.MimeTypeMap;
import android.widget.ExpandableListView;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.activity.SearchAdapter;
import com.eaccid.bookreader.file.FileOnDeviceFinder;
import com.eaccid.bookreader.search.ItemObjectChild;
import com.eaccid.bookreader.search.ItemObjectGroup;
import com.eaccid.bookreader.search.SearchSuggestionsProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainBookListView implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private Activity context;
    private SearchAdapter searchAdapter;
    private ExpandableListView expandableListView;

    public MainBookListView(Activity context) {
        this.context = context;
        expandableListView = (ExpandableListView) context.findViewById(R.id.expandableListView_main);
    }

    private void fillExpandableListView() {

        List<ItemObjectGroup> itemObjectGroupList = new ArrayList<>();
        List<String> readableFiles = new ArrayList<>();

        FileOnDeviceFinder fileOnDeviceFinder = new FileOnDeviceFinder();
        ArrayList<File> foundFiles = fileOnDeviceFinder.findFiles();


        List<ItemObjectChild> childObjectItemTXT = new ArrayList<>();
        List<ItemObjectChild> childObjectItemPDF = new ArrayList<>();

        for (File file : foundFiles) {
            String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
            if (ext.equalsIgnoreCase("txt")) {
                childObjectItemTXT.add(new ItemObjectChild(R.mipmap.generic_icon, file.getName(), file));
                readableFiles.add(file.getPath());
            } else {
                childObjectItemPDF.add(new ItemObjectChild(R.mipmap.generic_icon, file.getName(), file));
            }
        }
        ItemObjectGroup itemObjectGroupTXT = new ItemObjectGroup("TXT", childObjectItemTXT);
        itemObjectGroupList.add(itemObjectGroupTXT);
        ItemObjectGroup itemObjectGroupPDF = new ItemObjectGroup("PDF", childObjectItemPDF);
        itemObjectGroupList.add(itemObjectGroupPDF);

        searchAdapter = new SearchAdapter(context, itemObjectGroupList);
        expandableListView.setAdapter(searchAdapter);

        expandListViewGroup();
        AppDatabaseManager.refreshBooks(readableFiles);

    }

    private void expandListViewGroup() {
        int groupCount = searchAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
    }

    public void refreshBookList(String searchText) {
        searchAdapter.filterData(searchText);
        expandListViewGroup();
    }

    public void fillBookList() {


        fillExpandableListView();

    }

    public void setSearch(SearchView searchView) {
        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(context.getComponentName()));

        // Do not iconify the widget, expand it by default
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        //Call this to try to give focus to a specific view or to one of its descendants
        searchView.requestFocus();
    }

    @Override
    public boolean onClose() {
        searchAdapter.filterData("");
        expandListViewGroup();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(context,
                SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
        suggestions.saveRecentQuery(query, null);

        refreshBookList(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        refreshBookList(newText);
        return false;
    }

}
