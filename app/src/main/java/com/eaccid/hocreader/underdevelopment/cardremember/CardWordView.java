package com.eaccid.hocreader.underdevelopment.cardremember;

import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.provider.db.words.WordItem;

public interface CardWordView extends BaseView {
    void setDataToView(WordItem wordItem);
}
