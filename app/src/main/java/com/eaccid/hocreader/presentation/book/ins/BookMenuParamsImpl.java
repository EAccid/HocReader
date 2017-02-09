package com.eaccid.hocreader.presentation.book.ins;

import android.content.Context;

import com.eaccid.hocreader.R;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import java.util.ArrayList;
import java.util.List;


public class BookMenuParamsImpl implements BookMenuParams {

    @Override
    public MenuParams create(Context context) {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) context.getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(createAndGetMenuObjects());
        menuParams.setClosableOutside(true);
        menuParams.setAnimationDuration(10);
        return menuParams;
    }

    private List<MenuObject> createAndGetMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();
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

}
