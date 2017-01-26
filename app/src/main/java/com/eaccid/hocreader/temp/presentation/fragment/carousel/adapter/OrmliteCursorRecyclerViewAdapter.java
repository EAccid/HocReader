package com.eaccid.hocreader.temp.presentation.fragment.carousel.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.stmt.PreparedQuery;
import java.sql.SQLException;

public abstract class OrmliteCursorRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends CursorRecyclerViewAdapter<VH> {
    protected PreparedQuery<T> preparedQuery;

    public OrmliteCursorRecyclerViewAdapter(Context context){
        super(null);
    }

    public abstract void onBindViewHolder(VH holder, T t);

    public final void onBindViewHolder(VH viewHolder, Cursor cursor){
        try {
            onBindViewHolder(viewHolder, this.cursorToObject(cursor));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final void changeCursor(Cursor cursor) {
        throw new UnsupportedOperationException("Please use OrmLiteCursorAdapter.changeCursor(Cursor,PreparedQuery) instead");
    }

    public void changeCursor(Cursor cursor, PreparedQuery<T> preparedQuery) {
        this.setPreparedQuery(preparedQuery);
        super.changeCursor(cursor);
    }

    public void setPreparedQuery(PreparedQuery<T> preparedQuery) {
        this.preparedQuery = preparedQuery;
    }

    public T getTypedItem(int position) {
        try {
            return this.cursorToObject((Cursor)getItem(position));
        } catch (SQLException var3) {
            throw new RuntimeException(var3);
        }
    }

    protected T cursorToObject(Cursor cursor) throws SQLException {
        return this.preparedQuery.mapRow(new AndroidDatabaseResults(cursor, null, false));
    }
}
