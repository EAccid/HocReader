package com.eaccid.hocreader.presentation.book.ins;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.provider.fromtext.ins.TextViewManagetImpl;

public class SelectionToTranslateActionMode implements ActionMode.Callback {
    private final TextView textView;
    private final int GT_ITEM_ID = 16;

    public SelectionToTranslateActionMode(TextView tv) {
        textView = tv;
        textView.setTextIsSelectable(true);
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        menu
                .add(0, GT_ITEM_ID, 1, "Google Translate")
                .setIcon(R.drawable.ic_g_translate_menu_24px)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case GT_ITEM_ID:
                boolean isTranslator = new GoogleTranslatorOnDevice()
                        .openAndTranslate(
                                textView.getContext(),
                                new TextViewManagetImpl().getSelectedText(textView)
                        );
                if (!isTranslator)
                    Toast.makeText(textView.getContext(), "There is no installed Google Translate",
                            Toast.LENGTH_SHORT).show();
                actionMode.finish();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
