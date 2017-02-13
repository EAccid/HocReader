package com.eaccid.hocreader.presentation.main.serchadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaccid.hocreader.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater layoutInflater;
    private final ArrayList<ItemGroup> itemGroupList;
    private final ArrayList<ItemGroup> originalList;

    public SearchAdapter(Context context, List<ItemGroup> itemGroupList) {
        this.itemGroupList = new ArrayList<>();
        this.itemGroupList.addAll(itemGroupList);
        this.originalList = new ArrayList<>();
        this.originalList.addAll(itemGroupList);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return itemGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ItemGroup itemGroup = itemGroupList.get(groupPosition);
        return itemGroup.getItemObjectChildGroupList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itemGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ItemGroup itemGroup = itemGroupList.get(groupPosition);
        return itemGroup.getItemObjectChildGroupList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ItemGroup itemGroup = (ItemGroup) getGroup(groupPosition);
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.book_group_item, null);
        TextView textViewGroup = (TextView) convertView.findViewById(R.id.group_text_view);
        textViewGroup.setText(itemGroup.getName());
        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemChild itemChild = (ItemChild) getChild(groupPosition, childPosition);
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.book_child_item, null);
        FrameLayout childContent = (FrameLayout) convertView.findViewById(R.id.chide_content);
        childContent.setOnClickListener(new OnChildItemClickListener(itemChild.getFile()));
        ImageView childIcon = (ImageView) convertView.findViewById(R.id.child_icon);
        childIcon.setImageResource(itemChild.getIcon());
        TextView textViewChild = (TextView) convertView.findViewById(R.id.child_text);
        textViewChild.setText(itemChild.getText());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void filterDataInList(String query) {
        //example: com.example.user.searchviewexpandablelistview
        query = query.toLowerCase();
        itemGroupList.clear();
        if (query.isEmpty()) {
            itemGroupList.addAll(originalList);
        } else {
            for (ItemGroup parentRow : originalList) {
                @SuppressWarnings("unchecked")
                List<ItemChild> childList = parentRow.getItemObjectChildGroupList();
                List<ItemChild> newList = new ArrayList<>();

                for (ItemChild childRow : childList) {
                    if (childRow.getText().toLowerCase().contains(query)) {
                        newList.add(childRow);
                    }
                }
                if (newList.size() > 0) {
                    ItemGroup itemGroup = new ItemGroupImpl(parentRow.getName(), newList);
                    itemGroupList.add(itemGroup);
                }
            }
        }
        notifyDataSetChanged();
    }


}
