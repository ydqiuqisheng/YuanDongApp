//package net.huansi.equipment.equipmentapp.adapter;//package net.huansi.equipment.equipmentapp.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.entity.EPRole;
//import net.huansi.equipment.equipmentapp.entity.EPUser;
//import net.huansi.equipment.equipmentapp.util.ViewHolder;
//
//import java.util.List;
//
///**
// * Created by 单中年 on 2017/2/23.
// */
//
//public class UserRoleRoleFragmentUserAdapter extends HsBaseAdapter<EPUser> {
//    public UserRoleRoleFragmentUserAdapter(List<EPUser> list, Context context) {
//        super(list, context);
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        if(view==null) view=mInflater.inflate(R.layout.user_role_user_role_fragment_bottom_item,viewGroup,false);
//        CheckBox cbUserRoleUserFragmentRoleItem= ViewHolder.get(view,R.id.cbUserRoleUserFragmentRoleItem);
//        EPUser epUser=mList.get(i);
//        cbUserRoleUserFragmentRoleItem.setChecked(epUser.isChoose);
//        cbUserRoleUserFragmentRoleItem.setText(epUser.SUSERNAME);
//        return view;
//    }
//}
