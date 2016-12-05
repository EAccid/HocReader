package com.eaccid.bookreader.activity.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.activity.services.MemorizingAlarmReceiver;
import com.eaccid.bookreader.activity.services.MemorizingService;
import com.eaccid.bookreader.db.entity.Book;
import com.eaccid.bookreader.db.entity.Word;
import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.underdev.settings.MainSettings;
import com.eaccid.bookreader.db.WordFilter;

public class MainActivity extends AppCompatActivity {

    MainBookListView bookListViewHandler;
    MainSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookListViewHandler = new MainBookListView(this);
        settings = new MainSettings(this);
        AppDatabaseManager.loadDatabaseManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO delete from here / temp data

                AppDatabaseManager.setFilter(WordFilter.NONE);

                Snackbar.make(view, "books: " + AppDatabaseManager.getAllBooks().size() + "\nwords: "
                                + AppDatabaseManager.getAllWords(null, null).size(),
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();


                int i = 1;
                for (Book book : AppDatabaseManager.getAllBooks()
                        ) {
                    System.out.println(i + ": " + book + "/n");
                    i++;
                }

                i = 1;
                for (Word word : AppDatabaseManager.getAllWords(null, null)
                        ) {
                    System.out.println(i + ": " + word + "/n");
                    i++;
                }

            }
        });

        bookListViewHandler.fillBookList();
        settings.setDefaultSettings();

        Log.i("TestLC", "on create");

//        startService(new Intent(this, MemorizingService.class));
        scheduleAlarm();

    }

    //TODO: scheduleAlarm, cancelAlarm - > settings isCanceled
    public void scheduleAlarm() {

        Intent intent = new Intent(getApplicationContext(), MemorizingAlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, MemorizingAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final long NOTIFY_INTERVAL = 5 * 1000;

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, NOTIFY_INTERVAL,
                2 * 60 * 1000, pendingIntent);
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    public void cancelAlarm() {


        Intent intent = new Intent(getApplicationContext(), MemorizingAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MemorizingAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) {
            alarm.cancel(pIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabaseManager.releaseDatabaseManager();
        Log.i("TestLC", "on destroy");
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

    ///////

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TestLC", "on start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TestLC", "on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TestLC", "on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TestLC", "on stop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TestLC", "on restart");
    }
}


