//package net.huansi.equipment.equipmentapp.adapter;//package net.huansi.equipment.equipmentapp.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.entity.EPRole;
//import net.huansi.equipment.equipmentapp.util.ViewHolder;
//
//import java.util.List;
//
///**
// * Created by 单中年 on 2017/2/23.
// */
//
//public class UserManageRoleAdapter extends HsBaseAdapter<EPRole> {
//    public UserManageRoleAdapter(List<EPRole> list, Context context) {
//        super(list, context);
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        if(view==null) view=mInflater.inflate(R.layout.user_manage_role_item,viewGroup,false);
//        TextView tvUserManageRoleItemRole= ViewHolder.get(view,R.id.tvUserManageRoleItemRole);//角色
//        ImageView imvUserManageRoleItemTick=ViewHolder.get(view,R.id.imvUserManageRoleItemTick);//打勾
//        EPRole role=mList.get(i);
//        tvUserManageRoleItemRole.setText(role.SROLENAME+(role.SROLECODE.isEmpty()?"":("/"+role.SROLECODE)));
//        imvUserManageRoleItemTick.setVisibility(role.isChoose?View.VISIBLE:View.INVISIBLE);
//        return view;
//    }
//}
