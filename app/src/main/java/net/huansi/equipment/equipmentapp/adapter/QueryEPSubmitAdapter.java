package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.EpWithRFID;
import net.huansi.equipment.equipmentapp.sqlite_db.EquipmentWithoutRFIDInSQLite;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by shanz on 2017/3/5.
 */

public class QueryEPSubmitAdapter extends HsBaseAdapter<EpWithRFID> {
    public QueryEPSubmitAdapter(List<EpWithRFID> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) convertView=mInflater.inflate(R.layout.ep_submit_item,parent,false);
        TextView tvEPSubmitItemName= ViewHolder.get(convertView,R.id.tvEPSubmitItemName);//设备名称
        TextView tvEPSubmitItemEPCode=ViewHolder.get(convertView,R.id.tvEPSubmitItemEPCode);
        TextView tvEPSubmitItemInFactoryDate= ViewHolder.get(convertView,R.id.tvEPSubmitItemInFactoryDate);//进厂日期
        TextView tvEPSubmitItemDepreciationDate= ViewHolder.get(convertView,R.id.tvEPSubmitItemDepreciationDate);//折旧日期
        TextView tvEPSubmitItemAssetsCode=ViewHolder.get(convertView,R.id.tvEPSubmitItemAssetsCode);//资产编号
        TextView tvEPSubmitItemOufFactoryCode=ViewHolder.get(convertView,R.id.tvEPSubmitItemOufFactoryCode);//出厂编号
        TextView tvEPSubmitItemCostCenter= ViewHolder.get(convertView,R.id.tvEPSubmitItemCostCenter);//成本中心
        TextView tvEPSubmitItemDeclareNum= ViewHolder.get(convertView,R.id.tvEPSubmitItemDeclareNum);//报关单号

        EpWithRFID epWithRFID=mList.get(position);
        tvEPSubmitItemName.setText(epWithRFID.EQUIPMENTNAME);
        tvEPSubmitItemEPCode.setText(epWithRFID.EPCCODE);
        tvEPSubmitItemInFactoryDate.setText(epWithRFID.INFACTORYDATE);
        tvEPSubmitItemDepreciationDate.setText(epWithRFID.DEPRECIATIONSTARTINGDATE);
        tvEPSubmitItemAssetsCode.setText(epWithRFID.ASSETSCODE);
        tvEPSubmitItemOufFactoryCode.setText(epWithRFID.OUTFACTORYCODE);
        tvEPSubmitItemCostCenter.setText(epWithRFID.COSTCENTER);
        tvEPSubmitItemDeclareNum.setText(epWithRFID.DECLARATIONNUM);
        return convertView;
    }
}
