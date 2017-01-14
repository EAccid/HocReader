package com.eaccid.hocreader.presentation.fragment.carousel;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.BaseView;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordsCarouselFragment extends Fragment implements BaseView {

    public static WordsCarouselFragment newInstance() {
        return new WordsCarouselFragment();
    }

    private WordCarouselPresenter mPresenter;
    @BindView(R.id.expandable_layout)
    ExpandableLayout expandable_layout;

    boolean isCollapset = true;

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

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setHasFixedSize(true);

        WordCarouselRecyclerViewAdapter adapter = mPresenter.createWordCarouselRecyclerViewAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());

//        String temp = "I clasp the flask between my hands even though the warmth from the tea has long since leached into the frozen air. My muscles are clenched tight against the cold. If a pack of wild dogs were to appear at this moment, the odds of scaling a tree before they attacked are not in my favor.  I should get up, move around, and work the stiffness from my limbs. But instead I sit, as motionless as the rock beneath me, while the dawn begins to lighten the woods. I can't fight the sun. I can only watch helplessly as it drags me into a day that I've been dreading for months. By noon they will all be at my new house in the Victor's Village. ";
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCollapset) {
                    expandable_layout.expand();
                } else {
                    expandable_layout.collapse();
                }
                isCollapset = !isCollapset;

            }
        });


    }

}