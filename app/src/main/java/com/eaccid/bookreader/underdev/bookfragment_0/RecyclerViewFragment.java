package com.eaccid.bookreader.underdev.bookfragment_0;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.activity.pager.PagerActivity;
import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.file.FileToPagesListReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class RecyclerViewFragment extends Fragment {

    private List<String> mPagesList = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private static final String TAG = "RecyclerViewFragment";

    public static RecyclerViewFragment newInstance() {
        RecyclerViewFragment f = new RecyclerViewFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        loadDataSet();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recycler_view_fragment_0, container, false);
        rootView.setTag(TAG);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new CustomAdapter(mPagesList);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(mLayoutManager);


        return rootView;
    }

    private void loadDataSet() {
        String filePath = AppDatabaseManager.getCurrentBookPath();
        FileToPagesListReader fileToPagesListReader = new FileToPagesListReader(getContext(), filePath);

        fileToPagesListReader.loadRxPages();

//      mPagesList = PagerActivity.getPagesList();
    }

}




