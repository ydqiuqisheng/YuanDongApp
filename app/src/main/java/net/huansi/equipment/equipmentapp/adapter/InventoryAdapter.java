package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.InventoryItem;

import java.util.List;

/**
 * Created by 单中年 on 2017/2/22.
 */

public class InventoryAdapter extends HsBaseAdapter<InventoryItem> {
    public InventoryAdapter(List<InventoryItem> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) view=mInflater.inflate(R.layout.inventory_detail_item,viewGroup,false);


        return view;
    }
}
