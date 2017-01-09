package com.eaccid.hocreader.injection.component;

import com.eaccid.hocreader.injection.WordListScope;
import com.eaccid.hocreader.injection.module.WordListModule;
import com.eaccid.hocreader.presentation.activity.pager.PagerPresenter;
import com.eaccid.hocreader.presentation.fragment.weditor.SwipeOnLongPressRecyclerViewAdapter;
import com.eaccid.hocreader.presentation.fragment.weditor.WordEditorPresenter;
import com.eaccid.hocreader.provider.db.WordListInteractor;

import dagger.Subcomponent;

@Subcomponent(modules = WordListModule.class)
@WordListScope
public interface WordListComponent {
    void inject(PagerPresenter pagerPresenter);

    void inject(WordEditorPresenter wordEditorPresenter);

    void inject(SwipeOnLongPressRecyclerViewAdapter swipeOnLongPressRecyclerViewAdapter);

    WordListInteractor wordWordListInteractor();


}
