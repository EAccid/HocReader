package com.eaccid.hocreader.presentation.book;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.eaccid.hocreader.R;

public class SelectableToTranslateActionMode implements ActionMode.Callback {

    private TextView textView;

    public SelectableToTranslateActionMode(TextView tv) {
        textView = tv;
        textView.setTextIsSelectable(true); //todo false selectable
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        menu.add(0, 0, 0, "Lingualeo").setIcon(R.drawable.ic_pets_menu_24px).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 1, 0, "Google translator").setIcon(R.drawable.ic_g_translate_menu_24px).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case
//        textView -> get selected text
//        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }

}
