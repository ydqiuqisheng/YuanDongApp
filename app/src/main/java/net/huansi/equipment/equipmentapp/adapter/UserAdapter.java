package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.EPUser;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by 单中年 on 2017/2/23.
 */

public class UserAdapter extends HsBaseAdapter<EPUser> {

    public UserAdapter(List<EPUser> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) view=mInflater.inflate(R.layout.user_item,viewGroup,false);
        TextView tvUserItemUser= ViewHolder.get(view,R.id.tvUserItemUser);//用户名
        TextView tvUserItemRole=ViewHolder.get(view,R.id.tvUserItemRole);//角色
        EPUser user=mList.get(i);
        tvUserItemUser.setText(user.SUSERNO);
        tvUserItemRole.setText(user.SROLENAME);
        return view;
    }
}
