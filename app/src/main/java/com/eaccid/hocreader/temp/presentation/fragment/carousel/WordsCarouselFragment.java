package com.eaccid.hocreader.temp.presentation.fragment.carousel;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.temp.presentation.fragment.carousel.adapter.WordCarouselRecyclerViewAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordsCarouselFragment extends Fragment implements BaseView {

    public static WordsCarouselFragment newInstance(boolean isFilterByBook) {
        WordsCarouselFragment f = new WordsCarouselFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_filter_by_book", isFilterByBook);
        f.setArguments(bundle);
        return f;
    }

    private WordCarouselPresenter mPresenter;
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
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter == null) mPresenter = new WordCarouselPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

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
        WordCarouselRecyclerViewAdapter adapter = mPresenter.createWordCarouselRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {
            new_text.setText("Translation under development");
            new_text.setTextColor(Color.BLUE);
        });
        showHint.setOnClickListener(v -> {
            hint.setVisibility(View.GONE);
            expandable_layout.expand();
        });
        expandable_text.setOnClickListener(v -> {
            expandable_layout.collapse();
            expandable_layout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
                @Override
                public void onExpansionUpdate(float expansionFraction) {
                    if (expansionFraction == 0)
                        hint.setVisibility(View.VISIBLE);
                }
            });
        });
    }
}