package com.eaccid.hocreader.presentation.training;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.training.carouseladapter.WordCarouselRecyclerViewAdapter;
import com.eaccid.hocreader.provider.db.words.WordCursorProvider;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordTrainingFragment extends Fragment {

    public static WordTrainingFragment newInstance(boolean isFilterByBook) {
        WordTrainingFragment f = new WordTrainingFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_filter_by_book", isFilterByBook);
        f.setArguments(bundle);
        return f;
    }

    @BindView(R.id.expandable_layout)
    ExpandableLayout expandable_layout;
    @BindView(R.id.show_hint)
    ImageButton showHint;
    @BindView(R.id.ripple_view)
    RippleView rippleView;
    @BindView(R.id.expandable_text)
    TextView expandable_text;
    @BindView(R.id.new_text)
    EditText new_text;
    @BindView(R.id.hint)
    LinearLayout hint;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_list_fragment_2, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        WordCarouselRecyclerViewAdapter adapter = new WordCarouselRecyclerViewAdapter();
        WordCursorProvider wordCursorProvider = new WordCursorProvider();
        adapter = (WordCarouselRecyclerViewAdapter) wordCursorProvider.createAdapterWithCursor(adapter,
                getArguments().getBoolean("is_filter_by_book"));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        new_text.setText("Translation under development");
        new_text.setTextColor(Color.BLUE);
    }

    @OnClick(R.id.show_hint)
    public void OnShowHintClick() {
        hint.setVisibility(View.GONE);
        expandable_layout.expand();
    }

    @OnClick(R.id.expandable_text)
    public void OnExpandableTextClick() {
        expandable_layout.collapse();
        expandable_layout.setOnExpansionUpdateListener(expansionFraction -> {
            if (expansionFraction == 0)
                hint.setVisibility(View.VISIBLE);
        });
    }
}