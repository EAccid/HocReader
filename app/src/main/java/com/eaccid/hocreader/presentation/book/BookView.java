package com.eaccid.hocreader.presentation.book;

import com.eaccid.hocreader.presentation.BaseView;

public interface BookView extends BaseView, BookReaderRouter {
    void showMoreMenu();

    void showGoToPage();

    void showSnackbarBackToPage(int nextPage, int previousPage);

    void scrollToListPosition(int position, int oldPosition);

    int getCurrentPosition();

    void notifyDataSetChanged();

    void setSelectableText(boolean isSelectable);

    boolean isSelectableMode();
}
