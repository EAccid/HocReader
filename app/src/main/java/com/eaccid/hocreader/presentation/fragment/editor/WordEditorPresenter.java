package com.eaccid.hocreader.presentation.fragment.editor;
import android.util.Log;
import com.eaccid.hocreader.presentation.activity.pager.PagerActivity;
import com.eaccid.hocreader.presentation.activity.pager.PagerPresenter;
import com.eaccid.hocreader.provider.DataListProvider;
import com.eaccid.hocreader.provider.WordListProvider;
import com.eaccid.hocreader.presentation.BasePresenter;

public class WordEditorPresenter implements BasePresenter<WordsEditorFragment> {
    private final String logTAG = "PagerPresenter";
    private WordsEditorFragment mView;
    private WordListProvider dataProvider; //TODO Inject

    @Override
    public void attachView(WordsEditorFragment wordsEditorFragment) {
        mView = wordsEditorFragment;
        Log.i(logTAG, mView.getClass().getName() + "' has been attached.");
        setDataProvider();
    }

    @Override
    public void detachView() {
        Log.i(logTAG, mView.getClass().getName() + "' has been detached.");
        mView = null;
    }

    public void setDataProvider() {
        PagerPresenter pp = ((PagerActivity) mView.getActivity()).getPresenter();
        dataProvider = pp.getDataProvider();
    }

    public void onRefreshRecyclerView() {
        dataProvider.updateSessionDataList();
    }

    public SwipeOnLongPressRecyclerViewAdapter createSwipeOnLongPressAdapter() {
        return new SwipeOnLongPressRecyclerViewAdapter(dataProvider);
    }

    public void onItemRemoved(int position) {
        mView.showRemovedSnackBar(position);
    }

    public void onUndoClick() {
        int position = dataProvider.undoLastRemoval();
        if (position >= 0) {
            mView.notifyItemInserted(position);
        }
    }

    public void onItemClicked(int position) {
        DataListProvider.ItemDataProvider data = dataProvider.getItem(position);
        if (data.isPinned()) {
            data.setPinned(false);
            mView.notifyItemChanged(position);
        }
    }

}
