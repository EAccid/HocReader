package com.eaccid.hocreader.presentation.book;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.h6ah4i.android.widget.advrecyclerview.utils.CustomRecyclerViewUtils;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.List;

public class BookFragment extends Fragment implements
        OnMenuItemClickListener, OnMenuItemLongClickListener, BookView {

    private BookPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private BookRecyclerViewAdapter mAdapter;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private List<MenuObject> menuObjects;
    private static final String TAG = "BookFragment";

    public ImageView moreMenuImg;

    public static BookFragment newInstance() {
        BookFragment f = new BookFragment();
        return f;
    }

    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (mPresenter == null) mPresenter = new BookPresenter();
        mPresenter.attachView(this);
        initMenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        View rootView = inflater.inflate(R.layout.bookreader_rv_fragment_0, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = mPresenter.createRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        moreMenuImg = (ImageView) rootView.findViewById(R.id.menu_more_vert_grey);
        moreMenuImg.setOnClickListener(view -> onMoreMenuClicked());
        if (savedInstanceState != null) {
            boolean isSelectableText = savedInstanceState.getBoolean("is_selectable");
            setSelectableText(isSelectableText);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onViewCreated();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        MenuObjectWrapper mo = (MenuObjectWrapper) menuObjects.get(position);
        switch (mo.getTag()) {
            case GO_TO_PAGE:
                mPresenter.onGoToPageClicked();
                break;
            case ADD_BOOKMARK:
                Toast.makeText(clickedView.getContext(), "under development: " + mo.getTag(), Toast.LENGTH_SHORT).show();
                break;
            case OPEN_LINGUALEO:
                Intent leoIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.lingualeo.android");
                if (leoIntent != null) {
                    startActivity(leoIntent);
                }
                break;
            case OPEN_GOOGLE_TRANSLATOR:
                Intent googleTranslatorIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.translate");
                if (googleTranslatorIntent != null) {
                    startActivity(googleTranslatorIntent);
                }
                break;
            case FONT_SIZE:
                Toast.makeText(clickedView.getContext(), "under development: " + mo.getTag(), Toast.LENGTH_SHORT).show();
                break;
            case SELECT_TEXT:
                mPresenter.onSelectTextMenuClicked();
                break;
            default:
                throw new RuntimeException("There is no such menu item: position " + position);
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(clickedView.getContext(), "item on long clicked: " + menuObjects.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_selectable", isSelectableMode());
    }

    @Override
    public void showMoreMenu() {
        mMenuDialogFragment.show(getFragmentManager(), ContextMenuDialogFragment.TAG);
    }

    @Override
    public void showGoToPage() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.go_to_page_title)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .positiveText(android.R.string.ok)
                .input(R.string.input_page, 0, false, (dialog, input) -> {
                    mPresenter.goToPageClicked(input);
                })
                .negativeText(android.R.string.cancel)
                .show();
    }

    @Override
    public void showSnackbarBackToPage(int nextPage, int previousPage) {
        Snackbar snackbar = Snackbar.make(
                mRecyclerView,
                R.string.previous_page,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_action_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onUndoClicked(nextPage, previousPage);
            }
        });
        snackbar.show();
    }

    @Override
    public void scrollToListPosition(int position, int oldPosition) {
        mRecyclerView.scrollToPosition(position);
    }

    @Override
    public int getCurrentPosition() {
        return CustomRecyclerViewUtils.findFirstVisibleItemPosition(mRecyclerView, false);
    }

    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSelectableText(boolean isSelectable) {
        mAdapter.setSelectableItemTextView(isSelectable);
        mAdapter.notifyDataSetChanged();
        String modeText = ""; // todo from @string
        if (isSelectable) {
            modeText = "Tap twice to select";
            moreMenuImg.setImageResource(R.drawable.ic_done_all_24px);
        } else {
            modeText = "Reader mode";
            moreMenuImg.setImageResource(R.drawable.ic_more_vert_gray_24px);
        }
        Toast.makeText(getContext(), modeText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isSelectableMode() {
        return mAdapter.isSelectableItemTextView();
    }

    private void initMenuFragment() {
        MenuParams menuParams = new BookMenuParamsImpl().create(getContext());
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
        menuObjects = menuParams.getMenuObjects();
    }

    private void onMoreMenuClicked() {
        mPresenter.onMoreMenuClicked();
    }

}



