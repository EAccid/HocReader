package com.eaccid.bookreader.pagerfragments;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eaccid.bookreader.R;
import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.fragment_0.BookArrayAdapter;
import com.eaccid.bookreader.fragment_0.MenuObjectWrapper;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.eaccid.bookreader.activity.pager.PagerActivity.getPagesList;

public class BookReaderListFragment extends ListFragment implements OnMenuItemClickListener, OnMenuItemLongClickListener {

    private List<String> pagesList;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private List<MenuObject> menuObjects = new ArrayList<>();
    private BookArrayAdapter bookArrayAdapter;

    public static BookReaderListFragment newInstance(int num) {
        BookReaderListFragment f = new BookReaderListFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putStringArrayList("pagesList", (ArrayList<String>) getPagesList());
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagesList = getArguments() != null ? getArguments().getStringArrayList("pagesList") : new ArrayList<>();
        fragmentManager = getFragmentManager();
        initMenuFragment();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bookreader_fragment_0, container, false);
        ImageView iv = (ImageView) v.findViewById(R.id.menu_more_vert_grey);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookArrayAdapter = new BookArrayAdapter(getContext(), R.id.text_on_page, pagesList);

        if (pagesList.size() > 0)
            setListAdapter(bookArrayAdapter);

        if (savedInstanceState != null)
            getListView().setSelection(savedInstanceState.getInt("firstVisiblePosition"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ListView list = getListView();
        outState.putInt("firstVisiblePosition", list.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }


    /////////////////////////////////////////    /////////////////////////////////////////

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

        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        MenuObject close = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.CLOSE);
        close.setResource(R.drawable.ic_arrow_back_blue_24px);

        MenuObject aster = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.GO_TO_PAGE, "go to page");
        aster.setResource(R.drawable.ic_find_in_page_blue_24px);

        MenuObject bookmark = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.ADD_BOOKMARK, "add bookmark");
        bookmark.setResource(R.drawable.ic_star_yellow_24px);

        MenuObject leoTraining = new MenuObjectWrapper(MenuObjectWrapper.MenuOption.OPEN_LINGUALEO, "open Lingualeo");
        leoTraining.setResource(R.drawable.ic_pets_leo_training_24px);

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

    @Override
    public void onMenuItemClick(View clickedView, int position) {

        MenuObjectWrapper mo = (MenuObjectWrapper) menuObjects.get(position);

        switch (mo.getTag()) {
            case GO_TO_PAGE:
                goToPage(clickedView);
                break;
            case ADD_BOOKMARK:
                Toast.makeText(clickedView.getContext(), "under development: " + mo.getTag(), Toast.LENGTH_SHORT).show();
                break;
            case OPEN_LINGUALEO:
                Toast.makeText(clickedView.getContext(), "under development: " + mo.getTag(), Toast.LENGTH_SHORT).show();
                break;
            case OPEN_GOOGLE_TRANSLATOR:
                Toast.makeText(clickedView.getContext(), "under development: " + mo.getTag(), Toast.LENGTH_SHORT).show();
                break;
            case FONT_SIZE:
                Toast.makeText(clickedView.getContext(), "under development: " + mo.getTag(), Toast.LENGTH_SHORT).show();
                break;
            case SELECT_TEXT:
                Toast.makeText(clickedView.getContext(), "under development: " + mo.getTag(), Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new RuntimeException("There is no such menu item: position " + position);
        }

    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(clickedView.getContext(), "item on long clicked: " + menuObjects.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void goToPage(View clickedView) {

        int currentItemPosition = AppDatabaseManager.getCurrentPage() - 1;

        new MaterialDialog.Builder(clickedView.getContext())
                .title(R.string.go_to_page_title)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .positiveText(android.R.string.ok)
                .input(R.string.input_page, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        goToListAdapterPosition(Integer.parseInt(input.toString()) - 1);
                        showSnackBackToLastOpenedPage(currentItemPosition);
                    }
                })
                .negativeText(android.R.string.cancel)
                .show();
    }

    private void showSnackBackToLastOpenedPage(int pageNumber) {

        Snackbar snackbar = Snackbar.make(
                getListView(),
                R.string.previous_page,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_action_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListAdapterPosition(pageNumber);
            }
        });
        snackbar.show();
    }

    private void goToListAdapterPosition(int position) {
        getListView().setSelection(position);
    }


}