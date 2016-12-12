package com.eaccid.hocreader.refactoring.fragment2;

import android.support.annotation.Nullable;
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
import com.eaccid.hocreader.data.local.AppDatabaseManager;

public class WordsCarouselTrainingFragment extends Fragment {
    private AppDatabaseManager wordManager;

    public static WordsCarouselTrainingFragment newInstance() {
        return new WordsCarouselTrainingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordManager = new AppDatabaseManager();
        wordManager.loadDatabaseManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_list_fragment_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        DrawerRecyclerViewAdapter adapter = new DrawerRecyclerViewAdapter(getContext());

        WordCursorBinder wordCursorBinder = new WordCursorBinder(getContext(), true);
        adapter = (DrawerRecyclerViewAdapter) wordCursorBinder.createAdapterWithCursor(adapter, wordManager);

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());

    }

}