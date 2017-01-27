package com.eaccid.hocreader.presentation.weditor;

import com.eaccid.hocreader.presentation.BaseView;

public interface WordsEditorView extends BaseView {
    void notifyItemChanged(int position);

    void notifyItemInserted(int position);

    void notifyDataSetChanged();

    void showToast(String text);
}
