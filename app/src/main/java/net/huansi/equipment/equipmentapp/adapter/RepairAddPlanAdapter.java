package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.RepairAddPlan;
import net.huansi.equipment.equipmentapp.util.TimeUtils;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by 单中年 on 2017/3/2.
 */

public class RepairAddPlanAdapter extends HsBaseAdapter<RepairAddPlan> {
    public RepairAddPlanAdapter(List<RepairAddPlan> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) view=mInflater.inflate(R.layout.repair_plan_list_dhild_item,viewGroup,false);
        RepairAddPlan plan=mList.get(i);
        TextView tvStartTime= ViewHolder.get(view,R.id.tvRepairPlanListChildItemStartTime);//开始时间
        TextView tvEndTime=ViewHolder.get(view,R.id.tvRepairPlanListChildItemEndTime);//结束时间
        if(!plan.isAdd){
           view.setAlpha(0.5f);
        }else {
            view.setAlpha(1);
        }
        tvStartTime.setText(TimeUtils.getTime(plan.startDate,"-"));
        tvEndTime.setText(TimeUtils.getTime(plan.endDate,"-"));
        return view;
    }
}
