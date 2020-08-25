package net.huansi.equipment.equipmentapp.activity.user_manage;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.EPRole;
import net.huansi.equipment.equipmentapp.entity.EPUser;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static net.huansi.equipment.equipmentapp.constant.Constant.UserActivityConstants.USER_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by 单中年 on 2017/2/23.
 */

public class UserAddActivity extends BaseActivity {
    @BindView(R.id.spUserAddFactory)Spinner spUserAddFactory;//工厂
    @BindView(R.id.spUserAddGroup)Spinner spUserAddGroup;//组别
    @BindView(R.id.etUserAddUserUserNo) EditText etUserAddUserUserNo;//用户名
    @BindView(R.id.etUserAddUserUserName) EditText etUserAddUserUserName;//昵称
    @BindView(R.id.etUserAddUserPwd) EditText etUserAddUserPwd;//密码
    @BindView(R.id.etUserAddUserPwdAgain) EditText etUserAddUserPwdAgain;//再次输入密码

    private LoadProgressDialog dialog;

    private ArrayAdapter<String> mFactoryAdapter;
    private ArrayAdapter<String> mGroupAdapter;

    private List<String> mFactoryList;
    private List<String> mGroupNameList;

    private List<EPRole> mTotalGroupList;
    private List<EPRole> mShowGroupList;

    private EPUser user;

    private boolean isCreate=true;


    @Override
    protected int getLayoutId() {
        return R.layout.user_add_activity;
    }

    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        dialog=new LoadProgressDialog(this);
        user= (EPUser) getIntent().getSerializableExtra(USER_DATA_PARAM);
        if(user==null) {
            user=new EPUser();
            setToolBarTitle("添加用户");
        }else {
            setToolBarTitle("修改用户");
        }
        etUserAddUserUserNo.setText(user.SUSERNO);
        etUserAddUserUserName.setText(user.SUSERNAME);
        mFactoryList=new ArrayList<>();
        mGroupNameList=new ArrayList<>();
        mTotalGroupList=new ArrayList<>();
        mShowGroupList=new ArrayList<>();
        mFactoryAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mFactoryList);
        mGroupAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mGroupNameList);
        spUserAddFactory.setAdapter(mFactoryAdapter);
        spUserAddGroup.setAdapter(mGroupAdapter);
        OthersUtil.showLoadDialog(dialog);
        getRoleList();
    }

    /**
     * 获得角色的数据
     */
    private void getRoleList(){
        mTotalGroupList.clear();
        mFactoryList.clear();
        //获取角色信息
        RxjavaWebUtils.requestByGetJsonData(
                this,
                "spAppEPQueryAllRole",
                "",
                getApplicationContext(),
                dialog,
                EPRole.class.getName(),
                true,
                getResources().getString(R.string.webservice_no_query),
                new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        List<WsEntity>  entities=hsWebInfo.wsData.LISTWSDATA;
                        Map<String,String> factoryMap=new HashMap<>();
                        for(WsEntity wsEntity:entities){
                            EPRole group=(EPRole) wsEntity;
                            mTotalGroupList.add(group);
                            factoryMap.put(group.SFACTORY,group.SFACTORY);
                        }
                        Iterator<Map.Entry<String,String>> it=factoryMap.entrySet().iterator();
                        while (it.hasNext()){
                            mFactoryList.add(it.next().getKey());
                        }
                        spUserAddFactory.setAdapter(mFactoryAdapter);
                        int factoryPosition=-1;
                        for(int i=0;i<mFactoryList.size();i++){
                            if(user.SFACTORY.equals(mFactoryList.get(i))){
                                factoryPosition=i;
                                break;
                            }
                        }
                        if(factoryPosition!=-1) spUserAddFactory.setSelection(factoryPosition,true);
                    }
                });
    }

    @OnItemSelected(R.id.spUserAddFactory)
    void showFactory(int position){
        String factory=mFactoryList.get(position);
        mShowGroupList.clear();
        mGroupNameList.clear();
        for(int i=0;i<mTotalGroupList.size();i++){
            EPRole group=mTotalGroupList.get(i);
            if(factory.equals(group.SFACTORY)) {
                mShowGroupList.add(group);
                mGroupNameList.add(group.SROLENAME);
            }
        }
        spUserAddGroup.setAdapter(mGroupAdapter);
        if(isCreate){
            int groupPosition=-1;
            for(int i=0;i<mShowGroupList.size();i++){
                EPRole role=mShowGroupList.get(i);
                if(role.SROLECODE.equals(user.SROLECODE)){
                    groupPosition=i;
                    break;
                }
            }
            if(groupPosition!=-1){
                spUserAddGroup.setSelection(groupPosition,true);
                isCreate=false;
            }
        }
    }

    /**
     * 提交数据
     */
    @OnClick(R.id.btnUserAddOk)
    void commit(){
        String userNo=etUserAddUserUserNo.getText().toString();
        String userName=etUserAddUserUserName.getText().toString();
        String pwd=etUserAddUserPwd.getText().toString();
        String pwdAgain=etUserAddUserPwdAgain.getText().toString();

        if(userNo.isEmpty()){
            OthersUtil.showTipsDialog(this,"请输入用户名");
            return;
        }

        if(userName.isEmpty()){
            OthersUtil.showTipsDialog(this,"请输入昵称");
            return;
        }
        if(user.SUSERNO.isEmpty()) {
            if (pwd.isEmpty()) {
                OthersUtil.showTipsDialog(this, "请输入密码");
                return;
            }
            if (!pwdAgain.equals(pwd)) {
                OthersUtil.showTipsDialog(this, "两次密码不同，请重新输入");
                return;
            }
        }

        EPRole group=mShowGroupList.get(spUserAddGroup.getSelectedItemPosition());
        OthersUtil.showLoadDialog(dialog);
        RxjavaWebUtils.requestByGetJsonData(this,
                "spAppEPUserAction",
                "sAdminUserNo=" + SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString() +
                        ",uUserID="+user.IUSERID+
                        ",sUserNo=" + userNo +
                        ",sUserName=" + userName +
                        ",sPwd="+pwd+
                        ",uGroupID=" +group.IROLEID,
                getApplicationContext(),
                dialog,
                WsData.class.getName(),
                false,
                null,
                new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        OthersUtil.ToastMsg(getApplicationContext(),"操作成功！！");
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }
}
