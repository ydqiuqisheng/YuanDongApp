//package net.huansi.equipment.equipmentapp.adapter;//package net.huansi.equipment.equipmentapp.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.entity.EPUser;
//import net.huansi.equipment.equipmentapp.util.DeviceUtil;
//import net.huansi.equipment.equipmentapp.util.ViewHolder;
//
//import java.util.List;
//
///**
// * Created by Administrator on 2016/3/24 0024.
// */
//public class UserRoleUserFragmentUserAdapter extends HsBaseAdapter<EPUser> {
//
//    public UserRoleUserFragmentUserAdapter(List<EPUser> list, Context context) {
//        super(list, context);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if(convertView==null){
//            convertView= mInflater.inflate(R.layout.user_role_user_role_fragment_top_item,parent,false);
//        }
//        TextView tvUserRoleUserFragmentUserItem= ViewHolder.get(convertView,R.id.tvUserRoleUserFragmentUserItem);
//        EPUser epUser=mList.get(position);
//        tvUserRoleUserFragmentUserItem.setText(epUser.SUSERNAME);
//        int screenWidth=DeviceUtil.getScreenWidth((Activity) mContext);
//        tvUserRoleUserFragmentUserItem.setWidth(screenWidth/4-10);
//        tvUserRoleUserFragmentUserItem.setHeight((int)((screenWidth/4-10)/2.5));
//        if(epUser.isChoose){
//            tvUserRoleUserFragmentUserItem.setBackground(mContext.getResources().getDrawable(R.drawable.blue_shape_selected));
//        }else {
//            tvUserRoleUserFragmentUserItem.setBackground(mContext.getResources().getDrawable(R.drawable.blue_shape_normal));
//        }
//        return convertView;
//    }
//}
