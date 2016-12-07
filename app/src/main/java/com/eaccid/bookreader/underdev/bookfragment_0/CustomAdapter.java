package com.eaccid.bookreader.underdev.bookfragment_0;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.eaccid.bookreader.R;
import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    private List<String> mPagesList;

    public CustomAdapter(List<String> mPagesList) {
        this.mPagesList = mPagesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.bookpage_item_fragment_0, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTextOnPageTextView().setText(mPagesList.get(position));
        holder.getPageNumberTextView().setText(position);
    }

    @Override
    public int getItemCount() {
        return mPagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textOnPageTextView;
        private final TextView pageNumberTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            textOnPageTextView = (TextView) itemView.findViewById(R.id.text_on_page);
            pageNumberTextView = (TextView) itemView.findViewById(R.id.page_number);
        }

        public TextView getTextOnPageTextView() {
            return textOnPageTextView;
        }

        public TextView getPageNumberTextView() {
            return pageNumberTextView;
        }
    }

}