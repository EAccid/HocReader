package com.eaccid.bookreader.fragment_1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.adapter.AdapterPathSegment;
import com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.adapter.UnwrapPositionResult;

import java.util.List;

class ItemFilteringAdapter extends SimpleWrapperAdapter {
    private boolean mFilteringEnabled = true;

    public ItemFilteringAdapter(@NonNull RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public int getItemCount() {
        if (isFilteringEnabled()) {
            return super.getItemCount() / 2;
        } else {
            return super.getItemCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isFilteringEnabled()) {
            return super.getItemViewType(position * 2);
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public long getItemId(int position) {
        if (isFilteringEnabled()) {
            return super.getItemId(position * 2);
        } else {
            return super.getItemId(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (isFilteringEnabled()) {
            super.onBindViewHolder(holder, position * 2, payloads);
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void unwrapPosition(@NonNull UnwrapPositionResult dest, int position) {
        if (isFilteringEnabled()) {
            super.unwrapPosition(dest, position * 2);
        } else {
            super.unwrapPosition(dest, position);
        }
    }

    @Override
    public int wrapPosition(@NonNull AdapterPathSegment pathSegment, int position) {
        if (isFilteringEnabled()) {
            if (pathSegment.adapter == getWrappedAdapter()) {
                return super.wrapPosition(pathSegment, position / 2);
            } else {
                return RecyclerView.NO_POSITION;
            }
        } else {
            return super.wrapPosition(pathSegment, position);
        }
    }

    @Override
    protected void onHandleWrappedAdapterChanged() {
        super.onHandleWrappedAdapterChanged();
    }

    @Override
    protected void onHandleWrappedAdapterItemRangeChanged(int positionStart, int itemCount) {
        if (isFilteringEnabled()) {
            final int localStart = positionStart / 2;
            final int localEnd = (positionStart + itemCount) / 2;

            if (localStart != localEnd) {
                super.onHandleWrappedAdapterItemRangeChanged(localStart, (localEnd - localStart));
            }
        } else {
            super.onHandleWrappedAdapterItemRangeChanged(positionStart, itemCount);
        }
    }

    @Override
    protected void onHandleWrappedAdapterItemRangeChanged(int positionStart, int itemCount, Object payload) {
        if (isFilteringEnabled()) {
            final int localStart = positionStart / 2;
            final int localEnd = (positionStart + itemCount) / 2;

            if (localStart != localEnd) {
                super.onHandleWrappedAdapterItemRangeChanged(localStart, (localEnd - localStart), payload);
            }
        } else {
            super.onHandleWrappedAdapterItemRangeChanged(positionStart, itemCount, payload);
        }
    }

    @Override
    protected void onHandleWrappedAdapterItemRangeInserted(int positionStart, int itemCount) {
        if (isFilteringEnabled()) {
            // NOTE: You cannot call onHandleWrappedAdapterItemRangeInserted() here,
            // because filtered result may be changed entirely.
            notifyDataSetChanged();
        } else {
            super.onHandleWrappedAdapterItemRangeInserted(positionStart, itemCount);
        }
    }

    @Override
    protected void onHandleWrappedAdapterItemRangeRemoved(int positionStart, int itemCount) {
        if (isFilteringEnabled()) {
            // NOTE: You cannot call onHandleWrappedAdapterItemRangeRemoved() here,
            // because filtered result may be changed entirely.
            notifyDataSetChanged();
        } else {
            super.onHandleWrappedAdapterItemRangeRemoved(positionStart, itemCount);
        }
    }

    @Override
    protected void onHandleWrappedAdapterRangeMoved(int fromPosition, int toPosition, int itemCount) {
        // NOTE: You cannot call onHandleWrappedAdapterRangeMoved() here,
        // because filtered result may be changed entirely.
        notifyDataSetChanged();
    }

    public boolean isFilteringEnabled() {
        return mFilteringEnabled;
    }

    public void setFilteringEnabled(boolean enabled) {
        if (mFilteringEnabled == enabled) {
            return;
        }

        mFilteringEnabled = enabled;
        notifyDataSetChanged();
    }
}