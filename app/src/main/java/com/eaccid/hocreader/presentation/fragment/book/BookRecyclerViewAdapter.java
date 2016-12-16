package com.eaccid.hocreader.presentation.fragment.book;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.provider.file.pagesplitter.Page;

import java.util.List;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {
    private static final String logTAG = "BookAdapter";
    private List<Page<String>> mPagesList;

    public BookRecyclerViewAdapter(List<Page<String>> mPagesList) {
        this.mPagesList = mPagesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.book_page_text_default, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Page<String> page = mPagesList.get(position);
        holder.getTextOnPageTextView().setText(page.getDataFromPage());
        holder.getPageNumberTextView().setText(
                String.valueOf(position + 1) +
                        " - " +
                        String.valueOf(mPagesList.size())
        );
        holder.getTextOnPageTextView().setOnTouchListener(new OnWordFromPageViewTouchListener(page.getPageNumber()));
        holder.getTextOnPageTextView().setCustomSelectionActionModeCallback(new SelectableActionMode());

    }

    @Override
    public int getItemCount() {
        return mPagesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textOnPageTextView;
        private final TextView pageNumberTextView;

        ViewHolder(View itemView) {
            super(itemView);
            textOnPageTextView = (TextView) itemView.findViewById(R.id.text_on_page);
            pageNumberTextView = (TextView) itemView.findViewById(R.id.page_number);
        }

        TextView getTextOnPageTextView() {
            return textOnPageTextView;
        }

        TextView getPageNumberTextView() {
            return pageNumberTextView;
        }
    }

}