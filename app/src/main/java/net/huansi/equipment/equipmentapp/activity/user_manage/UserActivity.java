package net.huansi.equipment.equipmentapp.activity.user_manage;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.UserAdapter;
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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

import static net.huansi.equipment.equipmentapp.constant.Constant.RoleActivityConstants.GROUP_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RoleActivityConstants.UPDATE_ROLE_REQUEST_CODE;
import static net.huansi.equipment.equipmentapp.constant.Constant.UserActivityConstants.ADD_USER_REQUEST_CODE;
import static net.huansi.equipment.equipmentapp.constant.Constant.UserActivityConstants.UPDATE_USER_REQUEST_CODE;
import static net.huansi.equipment.equipmentapp.constant.Constant.UserActivityConstants.USER_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by shanz on 2017/3/10.
 */

public class UserActivity extends BaseActivity {
    @BindView(R.id.lvUser) ListView lvUser;
    @BindView(R.id.etUserSearch)EditText etUserSearch;//搜索框

    private List<EPUser> mList;
    private List<EPUser> mShowList;
    private UserAdapter mAdapter;
    private LoadProgressDialog dialog;

    private boolean isCreate=true;

    @Override
    protected int getLayoutId() {
        return R.layout.user_activity;
    }

    @Override
    public void init() {
        setToolBarTitle("用户");
        OthersUtil.hideInputFirst(this);
        mList=new ArrayList<>();
        mShowList=new ArrayList<>();
        dialog=new LoadProgressDialog(this);
        mAdapter=new UserAdapter(mShowList,getApplicationContext());
        lvUser.setAdapter(mAdapter);
        etUserSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        OthersUtil.showLoadDialog(dialog);
        getUser();
    }

    /**
     * 显示筛选的数据
     */
    private void showFilter(){
        mShowList.clear();
        String search=etUserSearch.getText().toString().trim();
        for(int i=0;i<mList.size();i++){
            EPUser user=mList.get(i);

            if(user.SROLECODE.contains(search)||
                    user.SROLENAME.contains(search)||
                    user.SUSERNO.contains(search)||
                    user.SUSERNAME.contains(search)){
                mShowList.add(user);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 更新这个组别
     */
    @OnItemClick(R.id.lvUser)
    void update(int position){
        Intent intent=new Intent(this,UserAddActivity.class);
        intent.putExtra(USER_DATA_PARAM,mShowList.get(position));
        startActivityForResult(intent,UPDATE_USER_REQUEST_CODE);
    }

    /**
     * 删除
     */
    @OnItemLongClick(R.id.lvUser)
    boolean delete(final int position){
        OthersUtil.showChooseDialog(this, "确认删除这个用户？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                OthersUtil.showLoadDialog(dialog);
                RxjavaWebUtils.requestByGetJsonData(UserActivity.this,
                        "spAppEPUserAction",
                        "sAdminUserNo=" + SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString() +
                                ",uUserID=" + mShowList.get(position).IUSERID +
                                ",bDel=1", getApplicationContext(),
                        dialog,
                        WsData.class.getName(),
                        false,
                        "删除失败",
                        new SimpleHsWeb() {
                            @Override
                            public void success(HsWebInfo hsWebInfo) {
                                getUser();
                            }
                        });
            }
        });
        return true;
    }

    /**
     * 获得用户
     */
    private void getUser(){
        mList.clear();
        mShowList.clear();
        RxjavaWebUtils.requestByGetJsonData(this,
                "spAppEPQueryUser",
                "",
                getApplicationContext(),
                dialog,
                EPUser.class.getName(),
                true,
                "用户还不存在，请添加！！",
                new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                        Log.e("TAG","用户数据="+hsWebInfo.json);
                        for(int i=0;i<entities.size();i++){
                            Log.e("TAG",entities.get(i).toString());
                            Log.e("TAG",mList.toString());
                            mList.add((EPUser) entities.get(i));
                        }
                        if(isCreate) {
                            mShowList.addAll(mList);
                            mAdapter.notifyDataSetChanged();
                            isCreate=false;
                        }else {
                            showFilter();
                        }
                    }
                });
    }

    @OnClick(R.id.btnUserAdd)
    void addUser(){
        Intent intent=new Intent(this,UserAddActivity.class);
        startActivityForResult(intent,ADD_USER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;
        switch (requestCode){
            case ADD_USER_REQUEST_CODE:
            case UPDATE_USER_REQUEST_CODE:
                OthersUtil.showLoadDialog(dialog);
                getUser();
                break;
        }
    }
}
