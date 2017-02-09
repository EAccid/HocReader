package com.eaccid.hocreader.presentation.book.ins;

import com.yalantis.contextmenu.lib.MenuObject;

public class MenuObjectWrapper extends MenuObject {
    private final MenuOption TAG;

    public MenuObjectWrapper(MenuOption tag, String title) {
        super(title);
        TAG = tag;
    }

    public MenuObjectWrapper(MenuOption tag) {
        TAG = tag;
    }

    public MenuOption getTag() {
        return TAG;
    }

    public enum MenuOption {
        CLOSE,
        GO_TO_PAGE,
        ADD_BOOKMARK,
        OPEN_LINGUALEO,
        OPEN_GOOGLE_TRANSLATOR,
        FONT_SIZE,
        SELECT_TEXT
    }
}

