package net.huansi.equipment.equipmentapp.activity.user_manage;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.RoleAdapter;
import net.huansi.equipment.equipmentapp.entity.EPRole;
import net.huansi.equipment.equipmentapp.entity.Factory;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;

import static net.huansi.equipment.equipmentapp.constant.Constant.RoleActivityConstants.ADD_ROLE_REQUEST_CODE;
import static net.huansi.equipment.equipmentapp.constant.Constant.RoleActivityConstants.FACTORY_LIST_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RoleActivityConstants.GROUP_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RoleActivityConstants.UPDATE_ROLE_REQUEST_CODE;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by shanz on 2017/3/10.
 */

public class RoleActivity extends BaseActivity {
    @BindView(R.id.lvRole)ListView lvRole;
    @BindView(R.id.spRoleFactory)Spinner spRoleFactory;
//    @BindView(R.id.btnRoleAdd)Button btnRoleAdd;//添加角色

    private List<EPRole> mTotalList;
    private List<EPRole> mShowList;
    private ArrayList<String> mFactoryList;
    private ArrayAdapter<String> mFactoryAdapter;

    private RoleAdapter mAdapter;
    private LoadProgressDialog dialog;

    private boolean isCreate=true;
    @Override
    protected int getLayoutId() {
        return R.layout.role_activity;
    }

    @Override
    public void init() {
        setToolBarTitle("组别管理");
        TextView tvUser=getSubTitle();
        tvUser.setText("用户管理");
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RoleActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });
        mTotalList=new ArrayList<>();
        mShowList=new ArrayList<>();
        dialog=new LoadProgressDialog(this);
        mFactoryList=new ArrayList<>();
        mAdapter=new RoleAdapter(mShowList,getApplicationContext());
        lvRole.setAdapter(mAdapter);
        mFactoryAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mFactoryList);
        OthersUtil.showLoadDialog(dialog);
        getGroup();
    }

    /**
     * 获得组别
     */
    private void getGroup(){
        mShowList.clear();
        mTotalList.clear();
        RxjavaWebUtils.requestByGetJsonData(this,
                "spAppEPQueryAllRole",
                "",
                getApplicationContext(),
                dialog,
                EPRole.class.getName(),
                true,
                "不存在组别信息，请添加！！",
                new SimpleHsWeb() {
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        super.error(hsWebInfo);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                        for(int i=0;i<entities.size();i++){
                            EPRole group= (EPRole) entities.get(i);
                            mTotalList.add(group);
                        }
                        mShowList.addAll(mTotalList);
                        mAdapter.notifyDataSetChanged();
                        if(isCreate) {
                            getFactory();
                        }else {
                            OthersUtil.dismissLoadDialog(dialog);
                            showFilter(mFactoryList.get(spRoleFactory.getSelectedItemPosition()));
                        }
                    }
                });
    }


    /**
     * 获得工厂
     */
    private void getFactory(){
        mFactoryList.clear();
        RxjavaWebUtils.requestByGetJsonData(this,
                "spAppEPDownAllFactory",
                "",
                getApplicationContext(),
                dialog,
                Factory.class.getName(),
                true,
                "不存在工厂信息，请检查！！",
                new SimpleHsWeb() {
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        super.error(hsWebInfo);
                        spRoleFactory.setAdapter(mFactoryAdapter);
                    }

                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                        for(int i=0;i<entities.size();i++){
                            Factory factory= (Factory) entities.get(i);
                            if(factory.FACTORY.isEmpty()) continue;
                            mFactoryList.add(factory.FACTORY);
                        }
                        spRoleFactory.setAdapter(mFactoryAdapter);
                        isCreate=false;
                    }
                });
    }

    /**
     * 显示组别
     */
    @OnItemSelected(R.id.spRoleFactory)
    void showGroup(int position){
        showFilter(mFactoryList.get(position));
    }

    /**
     * 显示筛选的数据
     */
    private void showFilter(String factory){
        mShowList.clear();
        for(int i=0;i<mTotalList.size();i++){
            EPRole role=mTotalList.get(i);
            if(role.SFACTORY.equals(factory)){
                mShowList.add(role);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 更新这个组别
     */
    @OnItemClick(R.id.lvRole)
    void update(int position){
        Intent intent=new Intent(this,RoleAddActivity.class);
        intent.putExtra(FACTORY_LIST_PARAM, mFactoryList);
        intent.putExtra(GROUP_DATA_PARAM,mShowList.get(position));
        startActivityForResult(intent,UPDATE_ROLE_REQUEST_CODE);
    }

    /**
     * 删除
     */
    @OnItemLongClick(R.id.lvRole)
    boolean delete(final int position){
        OthersUtil.showChooseDialog(this, "确认删除这个组别？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                OthersUtil.showLoadDialog(dialog);
                RxjavaWebUtils.requestByGetJsonData(RoleActivity.this,
                        "spAppEPRoleAction",
                        "sAdminUserNo=" + SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString() +
                                ",uRoleID=" + mShowList.get(position).IROLEID +
                                ",bDel=1", getApplicationContext(),
                        dialog,
                        WsData.class.getName(),
                        false,
                        "删除失败",
                        new SimpleHsWeb() {
                            @Override
                            public void success(HsWebInfo hsWebInfo) {
                                getGroup();
                            }
                        });
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;
        switch (requestCode){
            case ADD_ROLE_REQUEST_CODE:
            case UPDATE_ROLE_REQUEST_CODE:
                OthersUtil.showLoadDialog(dialog);
                getGroup();
                break;
        }
    }

    /**
     * 添加组别
     */
    @OnClick(R.id.btnRoleAdd)
    void addRole(){
        Intent intent=new Intent(this,RoleAddActivity.class);
        intent.putExtra(FACTORY_LIST_PARAM, mFactoryList);
        startActivityForResult(intent,ADD_ROLE_REQUEST_CODE);
    }
}
