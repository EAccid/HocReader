package com.eaccid.bookreader.activity.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.db.entity.Book;
import com.eaccid.bookreader.db.entity.Word;
import com.eaccid.bookreader.dev.AppDatabaseManager;
import com.eaccid.bookreader.dev.settings.MainSettings;

public class MainActivity extends AppCompatActivity {

    MainBookListView bookListViewHandler;
    MainSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookListViewHandler = new MainBookListView(this);
        settings = new MainSettings(this);
        AppDatabaseManager.loadDatabaseManagerForAllActivities(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "books: " + AppDatabaseManager.getAllBooks().size() + "\nwords: " + AppDatabaseManager.getAllWords().size(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                int i = 1;
                for (Book book : AppDatabaseManager.getAllBooks()
                        ) {
                    System.out.println(i + ": " + book + "/n");
                    i++;
                }

                i = 1;
                for (Word word : AppDatabaseManager.getAllWords()
                        ) {
                    System.out.println(i + ": " + word + "/n");
                    i++;
                }

            }
        });

        bookListViewHandler.fillBookList();
        settings.setDefaultSettings();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabaseManager.releaseDatabaseManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        bookListViewHandler.setSearch(searchView);

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
                settings.clearBookSearchHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

