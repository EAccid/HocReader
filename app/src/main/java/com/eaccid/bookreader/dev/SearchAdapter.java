package com.eaccid.bookreader.dev;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaccid.bookreader.activity.PagerActivity;
import com.eaccid.bookreader.R;
import com.eaccid.bookreader.search.ItemObjectChild;
import com.eaccid.bookreader.search.ItemObjectGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseExpandableListAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<ItemObjectGroup> itemObjectGroupList;
    private ArrayList<ItemObjectGroup> originalList;

    public SearchAdapter(Context context, List<ItemObjectGroup> itemObjectGroupList) {
        this.itemObjectGroupList = new ArrayList<>();
        this.itemObjectGroupList.addAll(itemObjectGroupList);
        this.originalList = new ArrayList<>();
        this.originalList.addAll(itemObjectGroupList);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return itemObjectGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ItemObjectGroup itemObjectGroup = itemObjectGroupList.get(groupPosition);
        return itemObjectGroup.getItemObjectChildGroupList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itemObjectGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ItemObjectGroup itemObjectGroup = itemObjectGroupList.get(groupPosition);
        return itemObjectGroup.getItemObjectChildGroupList().get(childPosition);
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ItemObjectGroup itemObjectGroup = (ItemObjectGroup) getGroup(groupPosition);
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.group_list_item, null);
        TextView textViewGroup = (TextView) convertView.findViewById(R.id.group_text_view);
        textViewGroup.setText(itemObjectGroup.getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemObjectChild itemObjectChild = (ItemObjectChild) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.child_list_item, null);
        }

        ImageView childIcon = (ImageView) convertView.findViewById(R.id.child_icon);
        childIcon.setImageResource(itemObjectChild.getIcon());

        TextView textViewChild = (TextView) convertView.findViewById(R.id.child_text);
        textViewChild.setText(itemObjectChild.getText());

        textViewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PagerActivity.class);
                intent.putExtra("fileName", itemObjectChild.getFile().getName());
                intent.putExtra("filePath", itemObjectChild.getFile().getPath());
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void filterData(String query) {
        //example: com.example.user.searchviewexpandablelistview
        query = query.toLowerCase();
        itemObjectGroupList.clear();

        if (query.isEmpty()) {
            itemObjectGroupList.addAll(originalList);
        } else {
            for (ItemObjectGroup parentRow : originalList) {
                List<ItemObjectChild> childList = parentRow.getItemObjectChildGroupList();
                List<ItemObjectChild> newList = new ArrayList<>();

                for (ItemObjectChild childRow : childList) {
                    if (childRow.getText().toLowerCase().contains(query)) {
                        newList.add(childRow);
                    }
                }
                if (newList.size() > 0) {
                    ItemObjectGroup itemObjectGroup = new ItemObjectGroup(parentRow.getName(), newList);
                    itemObjectGroupList.add(itemObjectGroup);
                }
            }
        }
        notifyDataSetChanged();
    }
}
