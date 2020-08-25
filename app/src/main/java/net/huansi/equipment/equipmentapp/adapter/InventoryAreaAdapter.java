package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.InventoryArea;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by shanz on 2017/7/6.
 */

public class InventoryAreaAdapter extends HsBaseAdapter<InventoryArea> {
    public InventoryAreaAdapter(List<InventoryArea> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) convertView=mInflater.inflate(R.layout.inventory_area_item,parent,false);
        TextView tvInventoryAreaItemArea= ViewHolder.get(convertView,R.id.tvInventoryAreaItemArea);//区域
        TextView tvInventoryAreaItemAlready=ViewHolder.get(convertView,R.id.tvInventoryAreaItemAlready);//已盘
        TextView tvInventoryAreaItemOverage=ViewHolder.get(convertView,R.id.tvInventoryAreaItemOverage);//盘盈
        InventoryArea area=mList.get(position);
        tvInventoryAreaItemArea.setText(area.name);
        tvInventoryAreaItemAlready.setText(String.valueOf(area.already));
        tvInventoryAreaItemOverage.setText(String.valueOf(area.overage));
        return convertView;
    }
}
