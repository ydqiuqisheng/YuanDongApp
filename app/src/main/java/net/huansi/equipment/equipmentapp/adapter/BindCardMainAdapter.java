package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.sqlite_db.EquipmentWithoutRFIDInSQLite;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by shanz on 2017/3/5.
 */

public class BindCardMainAdapter extends HsBaseAdapter<EquipmentWithoutRFIDInSQLite> {
    public BindCardMainAdapter(List<EquipmentWithoutRFIDInSQLite> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) convertView=mInflater.inflate(R.layout.bind_card_main_item,parent,false);

        TextView tvBindCardItemInFactoryDate= ViewHolder.get(convertView,R.id.tvBindCardItemInFactoryDate);//进厂日期
        TextView tvBindCardItemDepreciationDate= ViewHolder.get(convertView,R.id.tvBindCardItemDepreciationDate);//折旧日期
        TextView tvBindCardItemAssetsCode=ViewHolder.get(convertView,R.id.tvBindCardItemAssetsCode);//资产编号
        TextView tvBindCardItemOufFactoryCode=ViewHolder.get(convertView,R.id.tvBindCardItemOufFactoryCode);//出厂编号
        TextView tvBindCardItemCostCenter= ViewHolder.get(convertView,R.id.tvBindCardItemCostCenter);//成本中心
        TextView tvBindCardItemDeclareNum= ViewHolder.get(convertView,R.id.tvBindCardItemDeclareNum);//报关单号
        TextView tvBindCardItemStatus=ViewHolder.get(convertView,R.id.tvBindCardItemStatus);
        LinearLayout bindCardItemEPCodeLayout=ViewHolder.get(convertView,R.id.bindCardItemEPCodeLayout);//RFID的layout
        TextView tvBindCardItemEPCode=ViewHolder.get(convertView,R.id.tvBindCardItemEPCode);
        TextView tvBindCardItemName=ViewHolder.get(convertView,R.id.tvBindCardItemName);

        EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite=mList.get(position);

        tvBindCardItemInFactoryDate.setText(equipmentWithoutRFIDInSQLite.getInFactoryDate());
        tvBindCardItemDepreciationDate.setText(equipmentWithoutRFIDInSQLite.getDepreciationStartingDate());
        tvBindCardItemOufFactoryCode.setText(equipmentWithoutRFIDInSQLite.getOutFactoryCode());
        tvBindCardItemCostCenter.setText(equipmentWithoutRFIDInSQLite.getCostCenter());
        tvBindCardItemDeclareNum.setText(equipmentWithoutRFIDInSQLite.getDeclarationNum());
        tvBindCardItemAssetsCode.setText(equipmentWithoutRFIDInSQLite.getAssetsCode());
        tvBindCardItemName.setText(equipmentWithoutRFIDInSQLite.getEquipmentName());

        switch (equipmentWithoutRFIDInSQLite.getIsBindRFID()){
            case 0:
                tvBindCardItemStatus.setTextColor(Color.parseColor("#EDCED7"));
                tvBindCardItemStatus.setText("未绑定");
                bindCardItemEPCodeLayout.setVisibility(View.GONE);

                break;
            case 1:
                tvBindCardItemStatus.setTextColor(Color.parseColor("#D28499"));
                tvBindCardItemStatus.setText("已绑定");
                bindCardItemEPCodeLayout.setVisibility(View.VISIBLE);
                tvBindCardItemEPCode.setText(equipmentWithoutRFIDInSQLite.getRFID());
                break;
        }
        return convertView;
    }
}
