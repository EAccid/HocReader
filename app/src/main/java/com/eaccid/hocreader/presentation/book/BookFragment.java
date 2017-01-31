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
import com.eaccid.hocreader.presentation.BaseView;
import com.h6ah4i.android.widget.advrecyclerview.utils.CustomRecyclerViewUtils;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment implements
        OnMenuItemClickListener, OnMenuItemLongClickListener, BaseView {

    private BookPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private BookRecyclerViewAdapter mAdapter;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private final List<MenuObject> menuObjects = new ArrayList<>();
    private final String TAG = "BookFragment";
    private ImageView moreMenuImg;

    public static BookFragment newInstance() {
        return new BookFragment();
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
        boolean isSelectableMode = false;
        if (savedInstanceState != null)
            isSelectableMode = savedInstanceState.getBoolean("is_selectable");
        View rootView = inflater.inflate(R.layout.bookreader_rv_fragment_0, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = mPresenter.createRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        moreMenuImg = (ImageView) rootView.findViewById(R.id.menu_more_vert_grey);
        moreMenuImg.setOnClickListener(view -> onMoreMenuClicked());
        setSelectableText(isSelectableMode);
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
                showGoToPageFragment(clickedView);
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

    public void showMoreMenu() {
        mMenuDialogFragment.show(getFragmentManager(), ContextMenuDialogFragment.TAG);
    }

    private void showGoToPageFragment(View clickedView) {
        new MaterialDialog.Builder(clickedView.getContext())
                .title(R.string.go_to_page_title)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .positiveText(android.R.string.ok)
                .input(R.string.input_page, 0, false,
                        (dialog, input) -> mPresenter.goToPageClicked(input)
                )
                .negativeText(android.R.string.cancel)
                .show();
    }

    public void showSnackbarBackToLastOpenedPage(int currentPage, int previousPage) {
        Snackbar snackbar = Snackbar.make(
                mRecyclerView,
                R.string.previous_page,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_action_back,
                v -> scrollToListPosition(currentPage, previousPage)
        );
        snackbar.show();
    }

    public void scrollToListPosition(int position, int oldPosition) {
        mRecyclerView.scrollToPosition(position);
    }

    public int getCurrentPosition() {
        return CustomRecyclerViewUtils.findFirstVisibleItemPosition(mRecyclerView, false);
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public void setSelectableText(boolean isSelectable) {
        mAdapter.setSelectableItemTextView(isSelectable);
        mAdapter.notifyDataSetChanged();
        String modeText; // todo from @string
        if (isSelectable) {
            modeText = "Tap twice to select";
            moreMenuImg.setImageResource(R.drawable.ic_done_all_24px);
        } else {
            modeText = "Reader mode";
            moreMenuImg.setImageResource(R.drawable.ic_more_vert_gray_24px);
        }
        Toast.makeText(getContext(), modeText, Toast.LENGTH_SHORT).show();
    }

    public boolean isSelectableMode() {
        return mAdapter.isSelectableItemTextView();
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        menuParams.setAnimationDuration(10);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {

        MenuObject close = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.CLOSE);
        close.setResource(R.drawable.ic_arrow_back_blue_24px);

        MenuObject aster = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.GO_TO_PAGE, "go to page");
        aster.setResource(R.drawable.ic_find_in_page_blue_24px);

        MenuObject bookmark = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.ADD_BOOKMARK, "add bookmark");
        bookmark.setResource(R.drawable.ic_bookmark_border_blue_24px);

        MenuObject leoTraining = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.OPEN_LINGUALEO, "open Lingualeo");
        leoTraining.setResource(R.drawable.ic_pets_orange_24px);

        MenuObject gTranslator = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.OPEN_GOOGLE_TRANSLATOR, "open Google Translator");
        gTranslator.setResource(R.drawable.ic_g_translate_blue_24px);

        MenuObject fontSize = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.FONT_SIZE, "font size");
        fontSize.setResource(R.drawable.ic_format_size_blue_24px);

        MenuObject selectText = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.SELECT_TEXT, "select to translate");
        selectText.setResource(R.drawable.ic_mode_edit_blue_24px);

        menuObjects.add(close);
        menuObjects.add(aster);
        menuObjects.add(bookmark);
        menuObjects.add(leoTraining);
        menuObjects.add(gTranslator);
        menuObjects.add(fontSize);
        menuObjects.add(selectText);

        return menuObjects;
    }

    private void onMoreMenuClicked() {
        mPresenter.onMoreMenuClicked();
    }

}



