package com.eaccid.hocreader.underdevelopment;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.injection.ApplicationContext;
import com.eaccid.hocreader.presentation.fragment.weditor.adapter.SwipeOnLongPressRecyclerViewAdapter;
import com.eaccid.hocreader.provider.db.WordListInteractor;

import android.support.v7.view.ActionMode;

import javax.inject.Inject;

public class ToolbarActionMode implements ActionMode.Callback {

    private SwipeOnLongPressRecyclerViewAdapter recyclerView_adapter;

    @Inject
    @ApplicationContext
    Context context;
    @Inject
    WordListInteractor wordListInteractor;

    public ToolbarActionMode(SwipeOnLongPressRecyclerViewAdapter recyclerView_adapter) {
        this.recyclerView_adapter = recyclerView_adapter;
        App.plusWordListComponent().inject(this);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.edit_words_action_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_learn).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();

                break;
            case R.id.action_copy:
                Toast.makeText(context, "copy", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.action_forward:
//                break;
            case R.id.action_learn:
                Toast.makeText(context, "learn", Toast.LENGTH_SHORT).show();
                mode.finish();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerView_adapter.removeSelection();
        //Set action mode null
    }
}
