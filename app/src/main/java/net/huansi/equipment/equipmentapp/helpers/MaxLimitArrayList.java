package net.huansi.equipment.equipmentapp.helpers;


import net.huansi.equipment.equipmentapp.entity.InventoryListItem;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class to limit the number of items in the arraylist
 */
public class MaxLimitArrayList extends ArrayList<InventoryListItem> {
    private static final int MAX_ITEMS = 30000;

    @Override
    public synchronized boolean add(InventoryListItem inventoryListItem) {
        if (size() < MAX_ITEMS)
            return super.add(inventoryListItem);
        else {
            return false;
        }
    }

    @Override
    public synchronized void add(int index, InventoryListItem inventoryListItem) {
        if (size() < MAX_ITEMS)
            super.add(index, inventoryListItem);
        else {
        }
    }

    @Override
    public synchronized boolean addAll(Collection<? extends InventoryListItem> collection) {
        if (size() + collection.size() < MAX_ITEMS)
            return super.addAll(collection);
        else {
            return false;
        }
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends InventoryListItem> collection) {
        if (size() + collection.size() < MAX_ITEMS)
            return super.addAll(index, collection);
        else {
            return false;
        }
    }
}
