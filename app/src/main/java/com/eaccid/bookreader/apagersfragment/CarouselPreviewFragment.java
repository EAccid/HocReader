package com.eaccid.bookreader.apagersfragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.eaccid.bookreader.R;
import com.eaccid.bookreader.recycleviewadapter.DrawerRecyclerViewAdapter;
import com.eaccid.bookreader.dev.AppDatabaseManager;
import com.j256.ormlite.android.apptools.OrmLiteCursorLoader;
import com.j256.ormlite.stmt.PreparedQuery;


public class CarouselPreviewFragment extends Fragment {


    public static CarouselPreviewFragment newInstance() {
        return new CarouselPreviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        DrawerRecyclerViewAdapter adapter = new DrawerRecyclerViewAdapter(getContext());

//        Cursor cursor = AppDatabaseManager.getWordCursor();
        PreparedQuery pq = AppDatabaseManager.getWordPreparedQuery();

        OrmLiteCursorLoader liteCursorLoader = new OrmLiteCursorLoader(getContext(), AppDatabaseManager.getWordDao(),pq);
        Cursor cursor = liteCursorLoader.loadInBackground();

        adapter.changeCursor(cursor, pq);


//        adapter.notifyDataSetChanged();



        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());

    }


}