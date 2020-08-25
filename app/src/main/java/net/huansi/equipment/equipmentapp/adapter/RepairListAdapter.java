package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.RepairList;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by 单中年 on 2017/3/3.
 * 维修计划的安排选择设备
 */

public class RepairListAdapter extends HsBaseAdapter<RepairList>{
    public RepairListAdapter(List<RepairList> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) view=mInflater.inflate(R.layout.repair_plan_list_parent_item,viewGroup,false);
        TextView tvRepairPlanListParentItemFactory= ViewHolder.get(view,R.id.tvRepairPlanListParentItemFactory);//工厂
        TextView tvRepairPlanListParentItemEquipmentName= ViewHolder.get(view,R.id.tvRepairPlanListParentItemEquipmentName);//设备名
        TextView tvRepairPlanListParentItemEPCode= ViewHolder.get(view,R.id.tvRepairPlanListParentItemEPCode);//EPCode
        TextView tvRepairPlanListParentItemCostCenter= ViewHolder.get(view,R.id.tvRepairPlanListParentItemCostCenter);//成本中心
        TextView tvRepairPlanListParentItemAssetsCode= ViewHolder.get(view,R.id.tvRepairPlanListParentItemAssetsCode);//资产编号
        TextView tvRepairPlanListParentItemOutFactoryCode= ViewHolder.get(view,R.id.tvRepairPlanListParentItemOutFactoryCode);//出厂编号
        RepairList list=mList.get(i);
        tvRepairPlanListParentItemFactory.setText(list.FACTORY);
        tvRepairPlanListParentItemEquipmentName.setText(list.EQUIPMENTNAME);
        tvRepairPlanListParentItemEPCode.setText(list.EPCCODE);
        tvRepairPlanListParentItemCostCenter.setText(list.COSTCENTER);
        tvRepairPlanListParentItemAssetsCode.setText(list.ASSETSCODE);
        tvRepairPlanListParentItemOutFactoryCode.setText(list.OUTFACTORYCODE);
        return view;
    }
}
