package net.huansi.equipment.equipmentapp.activity.user_manage;//package net.huansi.equipment.equipmentapp.activity.user_manage;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.activity.BaseActivity;
//import net.huansi.equipment.equipmentapp.adapter.UserManageRoleAdapter;
//import net.huansi.equipment.equipmentapp.adapter.UserManageUserAdapter;
//import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
//import net.huansi.equipment.equipmentapp.entity.EPRole;
//import net.huansi.equipment.equipmentapp.entity.EPUser;
//import net.huansi.equipment.equipmentapp.entity.WsEntity;
//import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
//import net.huansi.equipment.equipmentapp.util.OthersUtil;
//import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
//import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnItemClick;
//
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserManageActivityConstants.IS_USER_PARAM;
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserManageActivityConstants.LIST_PARAM;
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserManageActivityConstants.MANAGE_USER_ROLE_QUEST_CODE;
//
///**
// * Created by 单中年 on 2017/2/22.
// */
//
//public class UserManageActivity extends BaseActivity {
//    @BindView(R.id.lvUserManageUser) ListView lvUserManageUser;//用户 左边
//    @BindView(R.id.lvUserManageRole) ListView lvUserManageRole;//角色 右边
//
//    private List<EPUser> mUserList;
//    private List<EPRole> mRoleList;
//
//    private UserManageUserAdapter mUserAdapter;
//    private UserManageRoleAdapter mRoleAdapter;
//
//    private LoadProgressDialog dialog;
//
//    private int lastUserChoosePosition=-1;//上一个选择用户的那个位置
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.user_manage_activity;
//    }
//
//
//    @Override
//    public void init() {
//        dialog=new LoadProgressDialog(this);
//        getToolbarTitle().setText("用户角色管理");
//        TextView tvSubTitle=getSubTitle();
//        tvSubTitle.setText("管理");
//        tvSubTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(UserManageActivity.this);
//                builder.setItems(new String[]{"用户管理", "角色管理"},
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent  intent=new Intent(UserManageActivity.this,UserOrRoleManageActivity.class);
//                                switch (i){
//                                    case 0:
//                                        intent.putExtra(LIST_PARAM, (Serializable) mUserList);
//                                        intent.putExtra(IS_USER_PARAM,true);
//                                        break;
//                                    case 1:
//                                        intent.putExtra(LIST_PARAM, (Serializable) mRoleList);
//                                        intent.putExtra(IS_USER_PARAM,false);
//                                        break;
//                                }
//                                startActivityForResult(intent,MANAGE_USER_ROLE_QUEST_CODE);
//                            }
//                        })
//                        .show();
//            }
//        });
//        mUserList=new ArrayList<>();
//        mRoleList=new ArrayList<>();
//        mUserAdapter=new UserManageUserAdapter(mUserList,getApplicationContext());
//        mRoleAdapter=new UserManageRoleAdapter(mRoleList,getApplicationContext());
//        lvUserManageRole.setAdapter(mRoleAdapter);
//        lvUserManageUser.setAdapter(mUserAdapter);
//        OthersUtil.showLoadDialog(dialog);
//        getData();
//    }
//
//
//    /**
//     * 根据用户查看角色选择的情况
//     */
//    @OnItemClick(R.id.lvUserManageUser)
//    void showRoleByUser(int position){
//        EPUser user=mUserList.get(position);
//        user.isChoose=true;
//        mUserList.set(position,user);
//        if(lastUserChoosePosition!=-1&&mUserList.size()>lastUserChoosePosition){
//            mUserList.get(lastUserChoosePosition).isChoose=false;
//        }
//        lastUserChoosePosition=position;
//        for(int i=0;i<mRoleList.size();i++){
//            EPRole role=mRoleList.get(i);
//            if(role.SROLECODE.equals(user.SROLECODE)){
//                role.isChoose=false;
//            }else {
//                role.isChoose=true;
//            }
//            mRoleList.set(i,role);
//        }
//        mRoleAdapter.notifyDataSetChanged();
//        mUserAdapter.notifyDataSetChanged();
//    }
//
//    /**
//     * 获得数据
//     */
//    private void getData(){
//        mUserList.clear();
//        mRoleList.clear();
//        lastUserChoosePosition=-1;
//        //获得用户信息
//        RxjavaWebUtils.requestByGetJsonData("spAppEPQueryUser",
//                "",
//                getApplicationContext(),
//                dialog,
//                EPUser.class.getName(),
//                true,
//                getResources().getString(R.string.webservice_no_query),
//                new SimpleHsWeb() {
//                    @Override
//                    public void error(HsWebInfo hsWebInfo) {
//                        super.error(hsWebInfo);
//                        OthersUtil.showLoadDialog(dialog);
//                        getRoleData();
//                    }
//
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        List<WsEntity>  entities=hsWebInfo.wsData.LISTWSDATA;
//                        for(WsEntity wsEntity:entities){
//                            mUserList.add((EPUser) wsEntity);
//                        }
//                        getRoleData();
//                    }
//                });
//    }
//
//    /**
//     * 获取角色信息
//     */
//    private void getRoleData(){
//        //获取角色信息
//        RxjavaWebUtils.requestByGetJsonData("spAppEPQueryRole",
//                "",
//                getApplicationContext(),
//                dialog,
//                EPRole.class.getName(),
//                true,
//                null,
//                new SimpleHsWeb() {
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        OthersUtil.dismissLoadDialog(dialog);
//                        List<WsEntity>  entities=hsWebInfo.wsData.LISTWSDATA;
//                        for(WsEntity wsEntity:entities){
//                            mRoleList.add((EPRole) wsEntity);
//                        }
//                        lastUserChoosePosition=0;
//                        EPUser manageUser=mUserList.get(0);
//                        manageUser.isChoose=true;
//                        mUserList.set(0,manageUser);
//                        for(int i=0;i<mRoleList.size();i++){
//                            EPRole role=mRoleList.get(i);
//                            if(role.SROLECODE.equals(manageUser.SROLECODE)){
//                                role.isChoose=false;
//                            }else {
//                                role.isChoose=true;
//                            }
//                            mRoleList.set(i,role);
//                        }
//                        mRoleAdapter.notifyDataSetChanged();
//                        mUserAdapter.notifyDataSetChanged();
//                    }
//                });
//    }
//
//}
