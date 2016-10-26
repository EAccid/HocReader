package com.eaccid.bookreader.apagersfragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.adapter.BookArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.eaccid.bookreader.activity.PagerActivity.getPagesList;

public class BookReaderListFragment extends ListFragment {
    private int mNum;
    private List<String> pagesList;
    private int curentBookPage = 0;

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

        View v = inflater.inflate(R.layout.fragment_0_bookreader, container, false);
        View tv = v.findViewById(R.id.text);

//        //TODO separate fragment mNum
//        switch (mNum) {
//            case 0:
                ((TextView) tv).setText("Fragment #" + mNum + ": book");
//
//                    ((TextView) tv).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            Intent intent = new Intent(getContext(), SwipeOnLongPressExampleActivity.class);
//                            getContext().startActivity(intent);
//
//                        }
//                    });

//                break;
//            case 1:
//                ((TextView) tv).setText("Fragment #" + mNum + ": added words from page");
//
//
//
//
//
//                break;
//            case 2:
//                ((TextView) tv).setText("Fragment #" + mNum + ": word training");
//
//                break;
//            default:
//                ((TextView) tv).setText("Fragment #" + mNum);
//                Toast.makeText(getContext(), "smth goes wrong!", Toast.LENGTH_SHORT).show();
//                break;
//        }

        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//
//        switch (mNum) {
//            case 0:
                BookArrayAdapter bookArrayAdapter = new BookArrayAdapter(getContext(), R.id.text_on_page, pagesList);
                if (pagesList.size() > 0)
                    setListAdapter(bookArrayAdapter);
//                break;
//            case 1:
//                break;
//            case 2:
//                break;
//            default:
//                Toast.makeText(getContext(), "smth goes wrong!", Toast.LENGTH_SHORT).show();
//                break;
//        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ListView list = getListView();
        outState.putInt("firstVisiblePosition", list.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }

}