package com.eaccid.bookreader.search;

import java.util.ArrayList;

public class ItemObjectGroup {

    private String name;
    private ArrayList<ItemObjectChild> itemObjectChildGroupList;

    public ItemObjectGroup(String name, ArrayList<ItemObjectChild> itemObjectChildGroupList) {
        this.name = name;
        this.itemObjectChildGroupList = itemObjectChildGroupList;
    }

    public String getName() {
        return name;
    }


    public ArrayList<ItemObjectChild> getItemObjectChildGroupList() {
        return itemObjectChildGroupList;
    }
}

