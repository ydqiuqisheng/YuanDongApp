package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.EPRole;
import net.huansi.equipment.equipmentapp.entity.EPUser;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by 单中年 on 2017/2/23.
 */

public class RoleAdapter extends HsBaseAdapter<EPRole> {

    public RoleAdapter(List<EPRole> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) view=mInflater.inflate(R.layout.role_item,viewGroup,false);
        TextView tvRoleItemRole= ViewHolder.get(view,R.id.tvRoleItemRole);//组别
        TextView tvRoleItemFactory=ViewHolder.get(view,R.id.tvRoleItemFactory);//工厂
        EPRole role=mList.get(i);
        tvRoleItemRole.setText(role.SROLENAME);
        tvRoleItemFactory.setText(role.SFACTORY);
        return view;
    }
}
