package net.huansi.equipment.equipmentapp.activity.user_manage;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.EPRole;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static net.huansi.equipment.equipmentapp.constant.Constant.RoleActivityConstants.FACTORY_LIST_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RoleActivityConstants.GROUP_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by 单中年 on 2017/2/23.
 */

public class RoleAddActivity extends BaseActivity {
    @BindView(R.id.spRoleAddFactory)Spinner spRoleAddFactory;
    @BindView(R.id.spRoleAddGroup)Spinner spRoleAddGroup;
    @BindView(R.id.btnRoleAddOk) Button btnRoleAddOk;//确定按钮
    private LoadProgressDialog dialog;

    private List<String> mFactoryList;
    private List<String> mGroupList;//目前只支持

    private ArrayAdapter<String> mFactoryAdapter;
    private ArrayAdapter<String> mGroupAdapter;

    private EPRole role;

    @Override
    protected int getLayoutId() {
        return R.layout.role_add_activity;
    }

    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        dialog=new LoadProgressDialog(this);
        mGroupList=new ArrayList<>();
        mFactoryList=getIntent().getStringArrayListExtra(FACTORY_LIST_PARAM);
        if(mFactoryList==null) mFactoryList=new ArrayList<>();
        role= (EPRole) getIntent().getSerializableExtra(GROUP_DATA_PARAM);
        if(role==null) {
            role=new EPRole();
            setToolBarTitle("添加组别");
        }else {
            setToolBarTitle("修改组别");
        }
        mGroupList=new ArrayList<>();
        mGroupList.add("A");
        mGroupList.add("B");
        mGroupList.add("C");
        mGroupList.add("D");
        mGroupList.add("E");
        mGroupList.add("F");
        mGroupList.add("G");
        mGroupList.add("H");
        mGroupList.add("I");
        mGroupList.add("J");
        mGroupList.add("K");
        mFactoryAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mFactoryList);
        mGroupAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mGroupList);
        spRoleAddGroup.setAdapter(mGroupAdapter);
        spRoleAddFactory.setAdapter(mFactoryAdapter);
        int groupPosition=-1;
        for(int i=0;i<mGroupList.size();i++){
            if(role.SROLECODE.equals(mGroupList.get(i))){
                groupPosition=i;
                break;
            }
        }
        if(groupPosition!=-1) spRoleAddGroup.setSelection(groupPosition,true);

        int factoryPosition=-1;
        for(int i=0;i<mFactoryList.size();i++){
            if(role.SFACTORY.equals(mFactoryList.get(i))){
                factoryPosition=i;
                break;
            }
        }
        if(factoryPosition!=-1) spRoleAddFactory.setSelection(factoryPosition,true);
    }



    /**
     * 提交数据
     */
    @OnClick(R.id.btnRoleAddOk)
    void commit(){
        OthersUtil.showLoadDialog(dialog);
        RxjavaWebUtils.requestByGetJsonData(this,
                "spAppEPRoleAction",
                "sAdminUserNo=" + SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString() +
                        ",uRoleID="+role.IROLEID+
                        ",sRoleCode=" + mGroupList.get(spRoleAddGroup.getSelectedItemPosition()) +
                        ",sFactory=" + mFactoryList.get(spRoleAddFactory.getSelectedItemPosition()),
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
