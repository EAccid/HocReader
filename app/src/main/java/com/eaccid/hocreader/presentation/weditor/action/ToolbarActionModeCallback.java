package com.eaccid.hocreader.presentation.weditor.action;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.weditor.adapter.SwipeOnLongPressRecyclerViewAdapter;

import android.support.v7.view.ActionMode;

public class ToolbarActionModeCallback implements ActionMode.Callback {

    private final String LOG_TAG = "ToolbarAMCallback";
    private final SwipeOnLongPressRecyclerViewAdapter recyclerView_adapter;
    private ToolbarActionModeListener actionModeListener;

    public ToolbarActionModeCallback(SwipeOnLongPressRecyclerViewAdapter recyclerView_adapter) {
        this.recyclerView_adapter = recyclerView_adapter;
    }

    public void setToolbarActionModeListener(ToolbarActionModeListener listener) {
        this.actionModeListener = listener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.edit_words_action_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.findItem(R.id.action_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.findItem(R.id.action_learn).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        actionModeListener.onActionItemClicked(mode, item);
        mode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Log.i(LOG_TAG, "Action mode is destroying...");
        recyclerView_adapter.removeSelection();
        if (actionModeListener != null)
            actionModeListener.onModeDestroyed(mode);
    }
}
