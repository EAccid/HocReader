package com.eaccid.bookreader.fragment_1;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.provider.DataProvider;
import com.eaccid.bookreader.provider.WordDatabaseDataProvider;

public class SwipeOnWordRecyclerViewAdapter extends SwipeOnLongPressRecyclerViewAdapter{

    public SwipeOnWordRecyclerViewAdapter(WordDatabaseDataProvider dataProvider) {
        super(dataProvider);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DataProvider.ItemDataProvider item = getProvider().getItem(position);
        if (item.isLastAdded()) {
            int bgResId = R.drawable.bg_item_session_state;
            holder.mContainer.setBackgroundResource(bgResId);
        }
    }
}
