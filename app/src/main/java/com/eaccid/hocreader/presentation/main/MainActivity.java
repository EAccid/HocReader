package com.eaccid.hocreader.presentation.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import android.widget.ExpandableListView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.main.ins.directories.DirectoryChooser;
import com.eaccid.hocreader.presentation.main.ins.PermissionRequest;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemGroup;
import com.eaccid.hocreader.presentation.main.serchadapter.SearchAdapter;
import com.eaccid.hocreader.presentation.main.serchadapter.SearchSuggestionsProvider;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.settings.SettingsActivity;
import com.eaccid.hocreader.presentation.training.TrainingActivity;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//TODO: provide searching handler in separate class
public class MainActivity extends AppCompatActivity implements MainView<ItemGroup>,
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
    private SubMenu customizedMenu;

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
        customizedMenu = navigationView.getMenu().findItem(R.id.directories).getSubMenu();
        resetCustomMenu();
        if (mPresenter == null) mPresenter = new MainPresenter();
        mPresenter.attachView(this);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        mPresenter.onFabClicked();
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
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getBaseContext(),
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
        });
        searchView.setOnCloseListener(() -> {
            provideBooksSearching("");
            mPresenter.onCloseSearchView();
            return true;
        });
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                provideBooksSearching("");
                mPresenter.onCloseSearchView();
                return true;
            }
        });
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
            case R.id.action_choose_directory:
                mPresenter.onDirectoryChosen();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            provideBooksSearching(query);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                mPresenter.onSettingsMenuSelected();
                break;
            case R.id.alldir:
                mPresenter.onAllDirectoryMenuSelected();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        if (mPresenter.onNavigationItemSelected(id))
            setCheckedMenuItem(id);
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
        progressDialog = new ProgressDialog(expandableListView.getContext(),
                R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == DirectoryChooser.FILE_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> paths = intent.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);
            if (paths != null) {
                mPresenter.OnDirectoryChooserResult(paths);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionRequest.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (permissionGranted(grantResults)) {
                    mPresenter.readExternalStorageGranted();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showPermissionExplanation(String message, int permission) {
        Snackbar.make(expandableListView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("GRANT", v -> mPresenter.requestPermission(permission))
                .show();
    }

    private boolean permissionGranted(int[] grantResults) {
        return grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * menu customization
     */

    public void resetCustomMenu() {
        customizedMenu.clear();
        customizedMenu.setGroupCheckable(customizedMenu.getItem().getGroupId(), true, true);
        customizedMenu.add(customizedMenu.getItem().getGroupId(), R.id.alldir, 0, R.string.all_directories)
                .setIcon(R.drawable.ic_folder_special_black_24px)
                .setCheckable(true);
    }

    public void addCustomMenuItem(int id, String name) {
        if (customizedMenu.findItem(id) != null)
            customizedMenu.removeItem(id);
        customizedMenu
                .add(customizedMenu.getItem().getGroupId(), id, id, name)
                .setIcon(R.drawable.ic_folder_black_24px)
                .setCheckable(true);
    }

    public void setCheckedMenuItem(int id) {
        customizedMenu.findItem(id).setChecked(true);
    }

    /**
     * Router
     */

    public void navigateToTraining() {
        Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
        startActivity(intent);
    }

    public void navigateToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}


