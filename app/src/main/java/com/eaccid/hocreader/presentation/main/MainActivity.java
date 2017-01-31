package com.eaccid.hocreader.presentation.main;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemGroup;
import com.eaccid.hocreader.presentation.main.serchadapter.SearchAdapter;
import com.eaccid.hocreader.presentation.main.serchadapter.SearchSuggestionsProvider;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.settings.SettingsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//TODO: provide searching handler in separate class
public class MainActivity extends AppCompatActivity implements MainView<ItemGroup>,
        SearchView.OnQueryTextListener,
        SearchView.OnCloseListener,
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.expandableListView_main)
    ExpandableListView expandableListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private static MainPresenter mPresenter;
    private ProgressDialog progressDialog;
    private SearchAdapter searchAdapter;

    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle drawerListener = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerListener);
        drawerListener.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if (mPresenter == null) mPresenter = new MainPresenter();
        progressDialog = new ProgressDialog(expandableListView.getContext(),
                R.style.AppTheme_Dialog);
        mPresenter.attachView(this);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        mPresenter.onFabClicked();
    }

    @Override
    public boolean onClose() {
        provideBooksSearching("");
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
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_clearSearchHistory:
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
                suggestions.clearHistory();
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
        provideBooksSearching(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        provideBooksSearching(newText);
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
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setBooksData(List<ItemGroup> itemGroupList) {
        searchAdapter = new SearchAdapter(this, itemGroupList);
        expandableListView.setAdapter(searchAdapter);
        expandListViewGroup();
    }

    @Override
    public void provideBooksSearching(String searchText) {
        searchAdapter.filterDataInList(searchText);
        expandListViewGroup();
    }

    private void expandListViewGroup() {
        int groupCount = searchAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
    }

    @Override
    public void showProgressDialog() {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}

