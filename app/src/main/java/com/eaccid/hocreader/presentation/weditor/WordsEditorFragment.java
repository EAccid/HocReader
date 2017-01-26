package com.eaccid.hocreader.presentation.weditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.BaseView;

import com.eaccid.hocreader.presentation.weditor.action.ToolbarActionModeCallback;
import com.eaccid.hocreader.presentation.weditor.action.ToolbarActionModeListener;
import com.eaccid.hocreader.presentation.weditor.adapter.SwipeOnLongPressRecyclerViewAdapter;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.CustomRecyclerViewUtils;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

public class WordsEditorFragment extends Fragment implements BaseView, Toolbar.OnMenuItemClickListener, ToolbarActionModeListener {

    private WordEditorPresenter mPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public SwipeOnLongPressRecyclerViewAdapter mAdapter;
    public RecyclerView.Adapter mWrappedAdapter;
    public RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;
    private ActionMode mActionMode;

    public WordsEditorFragment() {
    }

    public static WordsEditorFragment newInstance() {
        WordsEditorFragment f = new WordsEditorFragment();
        return f;
    }

    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mPresenter == null) mPresenter = new WordEditorPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.editor_rv_fragment_1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorSecondaryText);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorLightAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this::onRefreshRecyclerView);
        Toolbar mToolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.edit_words_main_menu);
        mToolbar.setOnMenuItemClickListener(this);
        mPresenter.onViewCreated();
    }

    private void initRecyclerView() {
        //noinspection ConstantConditions
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);

        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();
        final SwipeOnLongPressRecyclerViewAdapter myItemAdapter = new SwipeOnLongPressRecyclerViewAdapter();
        myItemAdapter.setEventListener(new SwipeOnLongPressRecyclerViewAdapter.EventListener() {
            @Override
            public void onItemRemoved(int position) {
                onItemRemove(position);
            }

            @Override
            public void onItemPinned(int position) {
                onItemPin(position);
            }

            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                onItemViewClick(v, pinned);
            }
        });

        mAdapter = myItemAdapter;
        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mAdapter);
        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        animator.setSupportsChangeAnimations(false);
        animator.setRemoveDuration(300);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }
        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }
        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }
        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;
        super.onDestroyView();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_recycler_view:
                int position = CustomRecyclerViewUtils.findFirstVisibleItemPosition(mRecyclerView, false);
                if (position != RecyclerView.NO_POSITION) {
                    startActionMode();
                    onListItemSelect(position);
                }
                break;
            case R.id.action_clear_all_words:
                mPresenter.onDeleteAllWords();
                break;
            default:
        }
        return false;
    }

    private void onItemRemove(int position) {
        mPresenter.onItemRemoved(position);
    }

    public void onItemPin(int position) {
    }

    public void showRemovedSnackBar(int position) {
        Snackbar snackbar = Snackbar.make(
                getView().findViewById(R.id.container),
                R.string.snack_bar_text_item_removed,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onUndoClick();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(getView().getContext(), R.color.snackbar_action_color_done));
        snackbar.show();
    }

    private void onItemViewClick(View v, boolean pinned) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        if (position != RecyclerView.NO_POSITION) {
            if (mActionMode != null) {
                onListItemSelect(position);
            }
            mPresenter.onItemClicked(position);
        }
    }

    private void onRefreshRecyclerView() {
        mPresenter.onRefreshRecyclerView();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public void notifyItemInserted(int position) {
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(position);
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Handle action mode
     */

    @Override
    public void onModeDestroyed(ActionMode mode) {
        releaseActionMode();
    }

    @Override

    public void onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                mPresenter.removeItems(mAdapter.getSelectedIds());
                break;
            case R.id.action_copy:
                Toast.makeText(getContext(), "'Copy': under development", Toast.LENGTH_SHORT).show();
                mPresenter.copyItems(mAdapter.getSelectedIds());
                mode.finish();
                break;
            case R.id.action_learn:
                mPresenter.setToLearnItems(mAdapter.getSelectedIds());
                mode.finish();
                break;
        }
    }

    public void releaseActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void startActionMode() {
        ToolbarActionModeCallback actionModeCallback = new ToolbarActionModeCallback(mAdapter);
        actionModeCallback.setToolbarActionModeListener(this);
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
    }

    private void onListItemSelect(int position) {
        mAdapter.toggleSelection(position);
        boolean hasCheckedItems = mAdapter.getSelectedCount() > 0;
        if (!hasCheckedItems) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(String.valueOf(mAdapter.getSelectedCount()) + " selected");
        }
    }

}

