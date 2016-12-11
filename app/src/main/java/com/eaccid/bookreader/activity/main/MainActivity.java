package com.eaccid.bookreader.activity.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ExpandableListView;
import com.eaccid.bookreader.R;
import com.eaccid.bookreader.activity.services.MemorizingAlarmReceiver;
import com.eaccid.bookreader.searchfiles.ItemObjectChild;
import com.eaccid.bookreader.searchfiles.ItemObjectGroup;
import com.eaccid.bookreader.searchfiles.SearchAdapter;
import com.eaccid.bookreader.searchfiles.SearchSuggestionsProvider;
import com.eaccid.hocreader.view.BaseView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseView, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static MainPresenter mPresenter;
    private ExpandableListView expandableListView;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_main);

        if (mPresenter == null)
            mPresenter = new MainPresenter();
        mPresenter.attachView(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onFabButtonClickListener();
            }
        });

        mPresenter.loadFiles();
        mPresenter.loadSettings();

        scheduleAlarm();

    }


    @Override
    public boolean onClose() {
        searchAdapter.filterData("");
        expandListViewGroup();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        setSearch(searchView);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                return true;
            case R.id.action_clearSearchHistory:
                mPresenter.clearBookSearchHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
        suggestions.saveRecentQuery(query, null);
        refreshBookList(query);
        return false;
    }

    private void refreshBookList(String searchText) {
        searchAdapter.filterData(searchText);
        expandListViewGroup();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        refreshBookList(newText);
        return false;
    }

    public void showTempDataFromDB(String text) {
        Snackbar.make(getCurrentFocus(), text,
                Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public List<String> fillExpandableListView(List<File> files) {

        List<ItemObjectGroup> itemObjectGroupList = new ArrayList<>();
        List<String> readableFiles = new ArrayList<>();

        List<ItemObjectChild> childObjectItemTXT = new ArrayList<>();
        List<ItemObjectChild> childObjectItemPDF = new ArrayList<>();

        for (File file : files) {
            String ext1 = MimeTypeMap.getFileExtensionFromUrl(file.getName());
            int lastDot = file.getName().lastIndexOf('.');
            String ext2 = "";
            if (lastDot != -1)
                ext2 = file.getName().substring(lastDot + 1, file.getName().length());
            if (ext1.equalsIgnoreCase("txt") || ext2.equalsIgnoreCase("txt")) {
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

        searchAdapter = new SearchAdapter(this, itemObjectGroupList);
        expandableListView.setAdapter(searchAdapter);

        expandListViewGroup();

        return readableFiles;

    }

    private void scheduleAlarm() {
        //TODO: scheduleAlarm, cancelAlarm - > settings isCanceled

        Intent intent = new Intent(getApplicationContext(), MemorizingAlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, MemorizingAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_HOUR,
                AlarmManager.INTERVAL_HOUR, pendingIntent);
    }

    private void setSearch(SearchView searchView) {
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();
    }

    private void expandListViewGroup() {
        int groupCount = searchAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
    }

}

