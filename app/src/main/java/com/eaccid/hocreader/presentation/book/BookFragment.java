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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.FragmentTags;
import com.eaccid.hocreader.presentation.book.ins.BookMenuParamsImpl;
import com.eaccid.hocreader.presentation.book.ins.MenuObjectWrapper;
import com.h6ah4i.android.widget.advrecyclerview.utils.CustomRecyclerViewUtils;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookFragment extends Fragment implements OnMenuItemClickListener, OnMenuItemLongClickListener, BookView {
    private BookPresenter mPresenter;
    private BookRecyclerViewAdapter mAdapter;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private List<MenuObject> menuObjects;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.menu_more_vert_grey)
    ImageView moreMenuImg;
    @BindView(R.id.progress_bar_layout)
    View progressBarLinearLayout;
    @BindView(R.id.book)
    View bookView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

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
        rootView.setTag(FragmentTags.BOOK_READER);
        ButterKnife.bind(this, rootView);
        TextView textView = (TextView) progressBarLinearLayout.findViewById(R.id.text_loading);
        textView.setText("Loading book");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        setAdapterToRecyclerView();
        moreMenuImg.setOnClickListener(view -> onMoreMenuClicked());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int page = 0;
        if (savedInstanceState != null) {
            boolean isSelectableText = savedInstanceState.getBoolean("is_selectable");
            setSelectableText(isSelectableText);
            page = savedInstanceState.getInt("page_number");
        }
        mPresenter.onViewCreated(page);
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
                mPresenter.onOpenLingualeoClicked();
                break;
            case OPEN_GOOGLE_TRANSLATOR:
                mPresenter.onOpenGoogleTranslatorClicked();
                break;
            case FONT_SIZE:
                Toast.makeText(clickedView.getContext(), "under development: " + mo.getTag(), Toast.LENGTH_SHORT).show();
                break;
            case SELECT_TEXT:
                mPresenter.onSelectTextMenuClicked();
                break;
            case CLOSE:
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
        outState.putInt("page_number", getCurrentPosition());
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
        snackbar.setAction(R.string.snack_bar_action_back, v -> mPresenter.onUndoClicked(nextPage, previousPage));
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
        //TODO add mAdapter.notifyDataSetChanged() to; temp
        setAdapterToRecyclerView();
    }

    @Override
    public void setSelectableText(boolean isSelectable) {
        mAdapter.setSelectableItemTextView(isSelectable);
        String modeText;
        if (isSelectable) {
            modeText = getString(R.string.selectable_mode);
            moreMenuImg.setImageResource(R.drawable.ic_done_all_24px);
        } else {
            modeText =  getString(R.string.reader_mode);
            moreMenuImg.setImageResource(R.drawable.ic_more_vert_gray_24px);
        }
        Toast.makeText(getContext(), modeText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isSelectableMode() {
        return mAdapter.isSelectableItemTextView();
    }

    @Override
    public void showProgressDialog() {
        progressBarLinearLayout.setVisibility(View.VISIBLE);
        bookView.setVisibility(View.GONE);
    }

    @Override
    public void dismissProgressDialog() {
        progressBarLinearLayout.setVisibility(View.GONE);
        bookView.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToLingualeoApp() {
        Intent leoIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.lingualeo.android");
        if (leoIntent != null) {
            startActivity(leoIntent);
        }
    }

    @Override
    public void navigateToGoogleTranslator() {
        Intent googleTranslatorIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.translate");
        if (googleTranslatorIntent != null) {
            startActivity(googleTranslatorIntent);
        }
    }

    private void onMoreMenuClicked() {
        mPresenter.onMoreMenuClicked();
    }

    private void setAdapterToRecyclerView() {
        mAdapter = mPresenter.createRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initMenuFragment() {
        MenuParams menuParams = new BookMenuParamsImpl().create(getContext());
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
        menuObjects = menuParams.getMenuObjects();
    }

}



