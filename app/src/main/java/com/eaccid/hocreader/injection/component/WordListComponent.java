package com.eaccid.hocreader.injection.component;

import com.eaccid.hocreader.injection.WordListScope;
import com.eaccid.hocreader.injection.module.DataProviderModule;
import com.eaccid.hocreader.presentation.pager.PagerPresenter;
import com.eaccid.hocreader.presentation.weditor.adapter.SwipeOnLongPressRecyclerViewAdapter;
import com.eaccid.hocreader.presentation.weditor.WordEditorPresenter;

import dagger.Subcomponent;

@Subcomponent(modules = DataProviderModule.class)
@WordListScope
public interface WordListComponent {
    void inject(PagerPresenter pagerPresenter);

    void inject(SwipeOnLongPressRecyclerViewAdapter swipeOnLongPressRecyclerViewAdapter);

    void inject(WordEditorPresenter wordEditorPresenter);
}
