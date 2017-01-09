package com.eaccid.hocreader.presentation.fragment.weditor;

import android.util.Log;
import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.provider.db.WordProvider;
import com.eaccid.hocreader.provider.db.WordListInteractor;
import com.eaccid.hocreader.presentation.BasePresenter;

import javax.inject.Inject;

public class WordEditorPresenter implements BasePresenter<WordsEditorFragment> {
    private final String logTAG = "PagerPresenter";
    private WordsEditorFragment mView;

    @Inject
    WordListInteractor wordListInteractor;

    @Override
    public void attachView(WordsEditorFragment wordsEditorFragment) {
        App.getWordListComponent().inject(this);
        mView = wordsEditorFragment;
        Log.i(logTAG, "WordsEditorFragment has been attached.");
    }

    @Override
    public void detachView() {
        Log.i(logTAG, "WordsEditorFragment has been detached.");
        mView = null;
    }

    public void onRefreshRecyclerView() {
        wordListInteractor.updateSessionDataList();
    }

    public void onItemRemoved(int position) {
        mView.showRemovedSnackBar(position);
    }

    public void onUndoClick() {
        int position = wordListInteractor.undoLastRemoval();
        if (position >= 0) {
            mView.notifyItemInserted(position);
        }
    }

    public void onItemClicked(int position) {
        WordProvider data = (WordProvider) wordListInteractor.getItem(position);
        if (data.isPinned()) {
            data.setPinned(false);
            mView.notifyItemChanged(position);
        }
    }
}
