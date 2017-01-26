package com.eaccid.hocreader.presentation.book;

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
    private boolean isSelectableItemTextView;

    public BookRecyclerViewAdapter(List<Page<String>> mPagesList) {
        this.mPagesList = mPagesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.book_page_text_item_0, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Page<String> page = mPagesList.get(position);
        TextView textOnPage = holder.getTextOnPageTextView();

        textOnPage.setText(page.getDataFromPage());
        holder.getPageNumberTextView().setText(
                String.valueOf(position + 1) +
                        " - " +
                        String.valueOf(mPagesList.size())
        );
        if (isSelectableItemTextView) {
            textOnPage.setTextIsSelectable(true);
            textOnPage.setOnTouchListener(null);
            textOnPage.setCustomSelectionActionModeCallback(
                    new SelectableToTranslateActionMode(textOnPage)
            );
        } else {
            textOnPage.setTextIsSelectable(false);
            textOnPage.setCustomSelectionActionModeCallback(null);
            textOnPage.setOnTouchListener(new OnWordFromPageViewTouchListener(page.getPageNumber()));
        }
    }

    @Override
    public int getItemCount() {
        return mPagesList.size();
    }

    public void setSelectableItemTextView(boolean isSelectable) {
        isSelectableItemTextView = isSelectable;
    }

    public boolean isSelectableItemTextView() {
        return isSelectableItemTextView;
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