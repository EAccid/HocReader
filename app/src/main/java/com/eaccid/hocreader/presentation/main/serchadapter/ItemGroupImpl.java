package com.eaccid.hocreader.presentation.main.serchadapter;

import java.util.List;

public class ItemGroupImpl implements ItemGroup<ItemChild> {
    private final String name;
    private final List<ItemChild> itemChildGroupList;

    public ItemGroupImpl(String name, List<ItemChild> itemChildGroupList) {
        this.name = name;
        this.itemChildGroupList = itemChildGroupList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<ItemChild> getItemObjectChildGroupList() {
        return itemChildGroupList;
    }
}

