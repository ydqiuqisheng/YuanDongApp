package net.huansi.equipment.equipmentapp.adapter;//package net.huansi.equipment.equipmentapp.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.entity.RepairList;
//import net.huansi.equipment.equipmentapp.util.ViewHolder;
//
//import java.util.List;
//
///**
// * Created by shanz on 2017/3/5.
// */
//
//public class RepairTemporaryEquipmentAdapter extends HsBaseAdapter<RepairList> {
//    public RepairTemporaryEquipmentAdapter(List<RepairList> list, Context context) {
//        super(list, context);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if(convertView==null) convertView=mInflater.inflate(R.layout.bind_card_main_item,parent,false);
//        TextView tvBindCardItemFactory= ViewHolder.get(convertView,R.id.tvBindCardItemFactory);
//        TextView tvBindCardItemAssetsCode= ViewHolder.get(convertView,R.id.tvBindCardItemAssetsCode);
//        TextView tvBindCardItemEquipmentName= ViewHolder.get(convertView,R.id.tvBindCardItemEquipmentName);
//        RepairList planList=mList.get(position);
//        tvBindCardItemFactory.setText(planList.FACTORY);
//        tvBindCardItemAssetsCode.setText(planList.ASSETSCODE);
//        tvBindCardItemEquipmentName.setText(planList.EQUIPMENTNAME);
//        return convertView;
//    }
//}
