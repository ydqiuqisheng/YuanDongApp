package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.sqlite_db.InventoryDetail;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by 单中年 on 2017/2/26.
 */

public class InventoryDetailAdapter extends HsBaseAdapter<InventoryDetail> {
    public InventoryDetailAdapter(List<InventoryDetail> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) view=mInflater.inflate(R.layout.inventory_detail_item,viewGroup,false);

        TextView tvInventoryDetailItemInFactoryDate=ViewHolder.get(view,R.id.tvInventoryDetailItemInFactoryDate);
        TextView tvInventoryDetailItemDepreciationDate=ViewHolder.get(view,R.id.tvInventoryDetailItemDepreciationDate);
        TextView tvInventoryDetailItemAssetsCode=ViewHolder.get(view,R.id.tvInventoryDetailItemAssetsCode);
        TextView tvInventoryDetailItemOufFactoryCode=ViewHolder.get(view,R.id.tvInventoryDetailItemOufFactoryCode);
        TextView tvInventoryDetailItemCostCenter=ViewHolder.get(view,R.id.tvInventoryDetailItemCostCenter);
        TextView tvInventoryDetailItemDeclareNum=ViewHolder.get(view,R.id.tvInventoryDetailItemDeclareNum);
        TextView tvInventoryItemCompanyIn=ViewHolder.get(view,R.id.tvInventoryItemCompanyIn);
        TextView tvInventoryItemStatus=ViewHolder.get(view,R.id.tvInventoryItemStatus);
        TextView tvInventoryDetailItemEquipmentName=ViewHolder.get(view,R.id.tvInventoryDetailItemEquipmentName);

        InventoryDetail inventoryDetail =mList.get(i);
        tvInventoryDetailItemInFactoryDate.setText(inventoryDetail.getInFactoryDate());
        tvInventoryDetailItemDepreciationDate.setText(inventoryDetail.getDepreciationStartingDate());
        tvInventoryDetailItemAssetsCode.setText(inventoryDetail.getAssetsCode());
        tvInventoryDetailItemOufFactoryCode.setText(inventoryDetail.getOutFactoryCode());
        tvInventoryDetailItemCostCenter.setText(inventoryDetail.getCostCenter());
        tvInventoryDetailItemDeclareNum.setText(inventoryDetail.getDeclarationNum());
        tvInventoryItemCompanyIn.setText(inventoryDetail.getFactory());
        tvInventoryDetailItemEquipmentName.setText(inventoryDetail.getEquipmentName());
        switch (inventoryDetail.getStatus()){
            case -2:
                tvInventoryItemStatus.setText("未盘");
                break;
            case -1:
                tvInventoryItemStatus.setText("盘亏");
                break;
            case 0:
                tvInventoryItemStatus.setText("已盘");
                break;
            case 1:
                tvInventoryItemStatus.setText("盘盈");
                break;
        }

        return view;
    }
}
