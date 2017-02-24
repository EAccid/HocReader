package com.eaccid.hocreader.presentation.pager;

import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.presentation.book.ins.OnWordFromPageViewTouchListener;
import com.eaccid.hocreader.presentation.translation.WordTranslationDialogFragment;
import com.eaccid.hocreader.provider.file.pagesplitter.CharactersDefinerForFullScreenTextView;
import com.eaccid.hocreader.provider.fromtext.WordFromText;

public interface PagerView extends BaseView, CharactersDefinerForFullScreenTextView.PageView,
        OnWordFromPageViewTouchListener.OnWordFromTextClickListener,
        WordTranslationDialogFragment.OnWordTranslationClickListener {
    void showTranslationDialog(WordFromText wordFromText);
}
