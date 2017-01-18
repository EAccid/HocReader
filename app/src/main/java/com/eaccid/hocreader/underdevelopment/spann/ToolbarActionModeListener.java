package com.eaccid.hocreader.underdevelopment.spann;

import android.support.v7.view.ActionMode;
import android.view.MenuItem;

public interface ToolbarActionModeListener {

    void onModeDestroyed(ActionMode mode);

    void onActionItemClicked(ActionMode mode, MenuItem item);

}
