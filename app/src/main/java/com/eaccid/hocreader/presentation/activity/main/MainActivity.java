package com.eaccid.hocreader.presentation.activity.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.activity.settings.SettingsActivity;
import com.eaccid.hocreader.presentation.activity.main.serchadapter.ItemObjectGroup;
import com.eaccid.hocreader.presentation.activity.main.serchadapter.SearchAdapter;
import com.eaccid.hocreader.presentation.activity.main.serchadapter.SearchSuggestionsProvider;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.BaseView;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseView,
        SearchView.OnQueryTextListener,
        SearchView.OnCloseListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static MainPresenter mPresenter;
    private ExpandableListView expandableListView;
    private SearchAdapter searchAdapter;

    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (mPresenter == null) mPresenter = new MainPresenter();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onFabButtonClickListener();
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerListener = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerListener);
        drawerListener.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPresenter.attachView(this);

        PreferenceManager.setDefaultValues(this.getApplicationContext(), R.xml.preferences, false);
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
        setSearchViewParameters(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        reloadExpandableListView(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        reloadExpandableListView(newText);
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showTestFab(String text) {
        Snackbar.make(getCurrentFocus(), text,
                Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public void setItemsToExpandableListView(List<ItemObjectGroup> itemObjectGroupList) {
        searchAdapter = new SearchAdapter(this, itemObjectGroupList);
        expandableListView.setAdapter(searchAdapter);
        expandListViewGroup();
    }

    private void reloadExpandableListView(String searchText) {
        searchAdapter.filterData(searchText);
        expandListViewGroup();
    }

    private void expandListViewGroup() {
        int groupCount = searchAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
    }

    private void setSearchViewParameters(SearchView searchView) {
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();
    }

}

