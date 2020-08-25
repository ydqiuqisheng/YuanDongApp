package net.huansi.equipment.equipmentapp.activity.user_manage;//package net.huansi.equipment.equipmentapp.activity.user_manage;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentTransaction;
//import android.view.View;
//import android.widget.TextView;
//
//import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.activity.BaseActivity;
//import net.huansi.equipment.equipmentapp.adapter.UserRoleUserFragmentRoleAdapter;
//import net.huansi.equipment.equipmentapp.entity.EPRole;
//import net.huansi.equipment.equipmentapp.entity.EPUser;
//import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
//import net.huansi.equipment.equipmentapp.entity.WsEntity;
//import net.huansi.equipment.equipmentapp.event.ActivityToFragmentEvent;
//import net.huansi.equipment.equipmentapp.fragment.UserRoleRoleFragment;
//import net.huansi.equipment.equipmentapp.fragment.UserRoleUserFragment;
//import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
//import net.huansi.equipment.equipmentapp.util.OthersUtil;
//import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
//import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserRoleActivityConstants.ADD_ROLE_QUEST_CODE;
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserRoleActivityConstants.ADD_USER_QUEST_CODE;
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserRoleActivityConstants.FRAGMENT_ROLE_LIST_PARAM;
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserRoleActivityConstants.FRAGMENT_USER_LIST_PARAM;
//
///**
// * Created by 单中年 on 2017/2/23.
// */
//
//public class UserRoleActivity extends BaseActivity {
//    @BindView(R.id.userRoleTopRoleLine) View userRoleTopRoleLine;//角色下面的红线
//    @BindView(R.id.tvUserRoleTopRole) TextView tvUserRoleTopRole;//角色的文字
//    @BindView(R.id.tvUserRoleTopUser) TextView tvUserRoleTopUser;//用户的文字
//    @BindView(R.id.userRoleTopUserLine) View userRoleTopUserLine;//用户下面的红线
//
//
//    private UserRoleUserFragment userFragment;
//    private UserRoleRoleFragment roleFragment;
//    private List<EPUser> mUserList;
//    private List<EPRole> mRoleList;
//
//    private LoadProgressDialog dialog;
//
//    private boolean isInit=true;//是否是刚开始的初始化
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.user_role_activity;
//    }
//
//    @Override
//    public void init() {
//        TextView tvSubTitle=getSubTitle();
//        tvSubTitle.setText("添加");
//        tvSubTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(UserRoleActivity.this);
//                builder.setItems(new String[]{"添加用户", "添加角色"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent=new Intent();
//                        switch (i){
//                            case 0:
//                                intent.setClass(UserRoleActivity.this,UserAddActivity.class);
//                                startActivityForResult(intent,ADD_USER_QUEST_CODE);
//                                break;
//                            case 1:
//                                intent.setClass(UserRoleActivity.this,RoleAddActivity.class);
//                                startActivityForResult(intent,ADD_ROLE_QUEST_CODE);
//                                break;
//                        }
//                    }
//                })
//                        .show();
//            }
//        });
//        dialog=new LoadProgressDialog(this);
//        mUserList=new ArrayList<>();
//        mRoleList=new ArrayList<>();
//        OthersUtil.showLoadDialog(dialog);
//        getUserData();
//    }
//
//    @Override
//    public void back() {
//
//    }
//
//    /**
//     * 显示用户
//     */
//    @OnClick(R.id.tvUserRoleTopUser)
//    void showUser(){
//        tvUserRoleTopUser.setTextColor(getResources().getColor(R.color.red));
//        tvUserRoleTopRole.setTextColor(getResources().getColor(R.color.black));
//        userRoleTopUserLine.setVisibility(View.VISIBLE);
//        userRoleTopRoleLine.setVisibility(View.INVISIBLE);
//        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//        transaction.hide(roleFragment);
//        transaction.show(userFragment);
//        transaction.commitAllowingStateLoss();
//    }
//
//    /**
//     * 显示角色
//     */
//    @OnClick(R.id.tvUserRoleTopRole)
//    void showRole(){
//        tvUserRoleTopRole.setTextColor(getResources().getColor(R.color.red));
//        tvUserRoleTopUser.setTextColor(getResources().getColor(R.color.black));
//        userRoleTopRoleLine.setVisibility(View.VISIBLE);
//        userRoleTopUserLine.setVisibility(View.INVISIBLE);
//        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//        transaction.hide(userFragment);
//        transaction.show(roleFragment);
//        transaction.commitAllowingStateLoss();
//    }
//
//    /**
//     * 获得用户的数据
//     */
//    private void getUserData() {
//        mUserList.clear();
//        mRoleList.clear();
//        //获得用户信息
//        RxjavaWebUtils.requestByGetJsonData(this,
//                "spAppEPQueryUser",
//                "",
//                getApplicationContext(),
//                dialog,
//                EPUser.class.getName(),
//                true,
//                getResources().getString(R.string.webservice_no_query),
//                new SimpleHsWeb() {
//
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
//                        for (WsEntity wsEntity : entities) {
//                            mUserList.add((EPUser) wsEntity);
//                        }
//                        //获取角色信息
//                        RxjavaWebUtils.requestByGetJsonData(UserRoleActivity.this,
//                                "spAppEPQueryAllRole",
//                                "",
//                                getApplicationContext(),
//                                dialog,
//                                EPRole.class.getName(),
//                                true,
//                                getResources().getString(R.string.webservice_no_query),
//                                new SimpleHsWeb() {
//                                    @Override
//                                    public void success(HsWebInfo hsWebInfo) {
//                                        OthersUtil.dismissLoadDialog(dialog);
//                                        List<WsEntity>  entities=hsWebInfo.wsData.LISTWSDATA;
//                                        for(WsEntity wsEntity:entities){
//                                            EPRole  epRole=(EPRole) wsEntity;
//                                            mRoleList.add(epRole);
//                                        }
//                                        if(isInit) {
//                                            initFragment();
//                                            isInit=false;
//                                        }else {
//                                            EventBus.getDefault().post
//                                                    (new ActivityToFragmentEvent
//                                                            (UserRoleActivity.class,
//                                                                    UserRoleRoleFragment.class,mUserList,mRoleList));
//                                            EventBus.getDefault().post
//                                                    (new ActivityToFragmentEvent
//                                                            (UserRoleActivity.class,
//                                                                    UserRoleUserFragment.class,mUserList,mRoleList));
//                                        }
//                                    }
//                                });
//                    }
//                });
//    }
//
//    private void initFragment(){
//        roleFragment = new UserRoleRoleFragment();
//        userFragment = new UserRoleUserFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(FRAGMENT_USER_LIST_PARAM, (Serializable) mUserList);
//        bundle.putSerializable(FRAGMENT_ROLE_LIST_PARAM, (Serializable) mRoleList);
//        userFragment.setArguments(bundle);
//        roleFragment.setArguments(bundle);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.userRoleLayout, userFragment);
//        transaction.add(R.id.userRoleLayout, roleFragment);
//        transaction.commitAllowingStateLoss();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode!=RESULT_OK) return;
//        switch (requestCode){
//            //添加用户
//            case ADD_USER_QUEST_CODE:
//            //添加角色
//            case ADD_ROLE_QUEST_CODE:
//                getUserData();
//
//                break;
//        }
//    }
//}
