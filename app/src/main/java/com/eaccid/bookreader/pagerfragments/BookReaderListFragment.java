package com.eaccid.bookreader.pagerfragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.fragment_0.BookArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.eaccid.bookreader.activity.pager.PagerActivity.getPagesList;

public class BookReaderListFragment extends ListFragment {
    private int mNum;
    private List<String> pagesList;

    public static BookReaderListFragment newInstance(int num) {
        BookReaderListFragment f = new BookReaderListFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putStringArrayList("pagesList", (ArrayList<String>) getPagesList());
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        pagesList = getArguments() != null ? getArguments().getStringArrayList("pagesList") : new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bookreader_fragment_0, container, false);
        View tv = v.findViewById(R.id.text);
//        ((TextView) tv).setText("Fragment #" + mNum + ": book");
        ((TextView) tv).setText(AppDatabaseManager.getCurrentBookName() + ", pagers: " + pagesList.size());

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BookArrayAdapter bookArrayAdapter = new BookArrayAdapter(getContext(), R.id.text_on_page, pagesList);
        if (pagesList.size() > 0)
            setListAdapter(bookArrayAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ListView list = getListView();
        outState.putInt("firstVisiblePosition", list.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }



}