package com.eaccid.bookreader.searchfiles;

import java.util.List;

public class ItemObjectGroup {

    private String name;
    private List<ItemObjectChild> itemObjectChildGroupList;

    public ItemObjectGroup(String name, List<ItemObjectChild> itemObjectChildGroupList) {
        this.name = name;
        this.itemObjectChildGroupList = itemObjectChildGroupList;
    }

    public String getName() {
        return name;
    }


    public List<ItemObjectChild> getItemObjectChildGroupList() {
        return itemObjectChildGroupList;
    }
}

