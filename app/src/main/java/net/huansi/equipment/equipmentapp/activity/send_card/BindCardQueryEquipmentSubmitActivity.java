package net.huansi.equipment.equipmentapp.activity.send_card;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.motorolasolutions.ASCII_SDK.AcessOperation;
import com.motorolasolutions.ASCII_SDK.Command_Inventory;
import com.motorolasolutions.ASCII_SDK.Command_abort;
import com.motorolasolutions.ASCII_SDK.MetaData;
import com.motorolasolutions.ASCII_SDK.Notification;
import com.motorolasolutions.ASCII_SDK.Notification_TriggerEvent;
import com.motorolasolutions.ASCII_SDK.RESPONSE_TYPE;
import com.motorolasolutions.ASCII_SDK.ResponseMsg;
import com.motorolasolutions.ASCII_SDK.Response_TagData;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.QueryEPSubmitAdapter;
import net.huansi.equipment.equipmentapp.constant.Constant;
import net.huansi.equipment.equipmentapp.entity.EpWithRFID;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.MainEvent;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent;
import net.huansi.equipment.equipmentapp.event.RFIDActionEvent;
import net.huansi.equipment.equipmentapp.gen.CheckDataInSQLiteDao;
import net.huansi.equipment.equipmentapp.gen.InventoryDetailDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.sqlite_db.CheckDataInSQLite;
import net.huansi.equipment.equipmentapp.sqlite_db.InventoryDetail;
import net.huansi.equipment.equipmentapp.util.NetUtil;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RFD8500DeviceUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import rx.functions.Func1;

import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.RFID_HAS_BIND;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.RECEIVE_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.TRIGGER_OPERATION_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.METADATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.NOTIFICATION_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RECEIVED_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RESPONSE_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by shanz on 2017/4/27.
 * 查询已上传设备
 */

public class BindCardQueryEquipmentSubmitActivity extends BaseActivity{
    @BindView(R.id.etQueryEquipmentSubmitSearch) EditText etQueryEquipmentSubmitSearch;
    @BindView(R.id.lvQueryEquipmentSubmit) ListView lvQueryEquipmentSubmit;
    @BindView(R.id.rgQueryEquipmentSubmit) RadioGroup rgQueryEquipmentSubmit;
    @BindView(R.id.rbQueryEquipmentSubmitNetwork) RadioButton rbQueryEquipmentSubmitNetwork;//有网
    @BindView(R.id.rbQueryEquipmentSubmitNoNetwork) RadioButton rbQueryEquipmentSubmitNoNetwork;//无网

    private List<EpWithRFID> mList;
    private QueryEPSubmitAdapter mAdapter;

    private HashMap<String,String> RFIDMap;//已绑定的RFID信息

    private LoadProgressDialog dialog;
    private boolean isInventorying;
    private MetaData metaData;
    private  EditText etEPSubmitUpdateDialog;

    @Override
    @SuppressWarnings("unchecked")
    public void init() {
        OthersUtil.registerEvent(this);
        OthersUtil.hideInputFirst(this);
        dialog=new LoadProgressDialog(this);
        mList=new ArrayList<>();
        RFIDMap= (HashMap<String, String>) getIntent().getSerializableExtra(RFID_HAS_BIND);
        mAdapter=new QueryEPSubmitAdapter(mList,getApplicationContext());
        lvQueryEquipmentSubmit.setAdapter(mAdapter);
        etQueryEquipmentSubmitSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        if(event.getAction()==KeyEvent.ACTION_DOWN) searchEquipment();
                        return true;
                }
                return false;
            }
        });
        RFD8500DeviceUtils.setPowerLevel((short) 80);
        rgQueryEquipmentSubmit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    //无网
                    case R.id.rbQueryEquipmentSubmitNoNetwork:
                        getSubTitle().setText("离线操作");
                        showDownDataDialog(false);
                        break;
                    case R.id.rbQueryEquipmentSubmitNetwork:
                        getSubTitle().setText("");
                        break;
                }
            }
        });
        //没有网络就直接跳到无网络状态
        if(!NetUtil.isNetworkAvailable(getApplicationContext())){
            rbQueryEquipmentSubmitNoNetwork.setChecked(true);
        }
        getSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(BindCardQueryEquipmentSubmitActivity.this);
                builder.setItems(new String[]{"下载/更新离线数据", "清除下载/更新的离线数据", "上传修改后的离线数据"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        showDownDataDialog(true);
                                        break;
                                    case 1:
                                        clearDownData();
                                        break;
                                    case 2:
                                        submitDataWithNoNetwork(false);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }


    /**
     * 显示下载数据的弹框
     * @Param isNeedUpdateDialog true 本地有的话，提示更新 false 本地有不提示更新
     */
    private void showDownDataDialog(final boolean isNeedUpdateDialog){
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(BindCardQueryEquipmentSubmitActivity.this, "")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                CheckDataInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getCheckDataInSQLiteDao();
                                HsWebInfo hsWebInfo=new HsWebInfo();
                                hsWebInfo.object=dao.queryBuilder()
                                        .where(CheckDataInSQLiteDao.Properties.UserNo
                                                .eq(SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY,String.class.getName(),"")))
                                        .list();
                                return hsWebInfo;
                            }

                        })
                , getApplicationContext(), dialog, new SimpleHsWeb() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void success(HsWebInfo hsWebInfo) {
                        List<CheckDataInSQLite> list= (List<CheckDataInSQLite>) hsWebInfo.object;
                        String networkStr=NetUtil.isNetworkAvailable(getApplicationContext())?"":"网络已断开！请寻找一个网络覆盖处！";
                        //本地SQLite中没有数据
                        if(list==null||list.isEmpty()){
                            OthersUtil.showDoubleChooseDialog(BindCardQueryEquipmentSubmitActivity.this,
                                    networkStr+"需要您下载校验的资产数据,请确认！",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            rbQueryEquipmentSubmitNetwork.setChecked(true);
                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            downOrReplaceCheckData();
                                        }
                                    });
                        }else {
                            if(isNeedUpdateDialog)
                            OthersUtil.showChooseDialog(BindCardQueryEquipmentSubmitActivity.this,
                                    networkStr+"本地有校验的资产数据,您需要替换为最新的吗？",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            downOrReplaceCheckData();
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * 替换或者下载校验的数据
     */
    private void downOrReplaceCheckData(){
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                //删除本地校验数据
                .map(new Func1<String, Void>() {
                    @Override
                    public Void call(String s) {
                        CheckDataInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getCheckDataInSQLiteDao();
                        List<CheckDataInSQLite> list=dao.queryBuilder()
                                .where(CheckDataInSQLiteDao.Properties.UserNo
                                        .eq(SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY,String.class.getName(),"")))
                                .list();
                        if(list==null) return null;
                        for (CheckDataInSQLite checkDataInSQLite:list){
                            dao.delete(checkDataInSQLite);
                        }
                        return null;
                    }
                })
                //下载新的数据
                .map(new Func1<Void, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(Void v) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                "spappQueryEPBind",
                                "sSearch=" ,
                                EpWithRFID.class.getName(),
                                true,"下载失败！！");
                    }
                })
                //保存数据
                .map(new Func1<HsWebInfo, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                        if(!hsWebInfo.success) return hsWebInfo;
                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                        CheckDataInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getCheckDataInSQLiteDao();
                        List<CheckDataInSQLite> checkDataInSQLiteList=new ArrayList<>();
                        for(WsEntity entity:entities){
                            CheckDataInSQLite checkDataInSQLite=new CheckDataInSQLite();
                            EpWithRFID epWithRFID= (EpWithRFID) entity;
                            checkDataInSQLite.setAssetsCode(epWithRFID.ASSETSCODE);
                            checkDataInSQLite.setBrand(epWithRFID.BRAND);
                            checkDataInSQLite.setCostCenter(epWithRFID.COSTCENTER);
                            checkDataInSQLite.setDeclarationNum(epWithRFID.DECLARATIONNUM);
                            checkDataInSQLite.setDepreciationStartingDate(epWithRFID.DEPRECIATIONSTARTINGDATE);
                            checkDataInSQLite.setEquipmentDetailID(epWithRFID.EQUIPMENTDETAILID);
                            checkDataInSQLite.setEquipmentName(epWithRFID.EQUIPMENTNAME);
                            checkDataInSQLite.setInFactoryDate(epWithRFID.INFACTORYDATE);
                            checkDataInSQLite.setModel(epWithRFID.MODEL);
                            checkDataInSQLite.setOutFactoryCode(epWithRFID.OUTFACTORYCODE);
                            checkDataInSQLite.setRFID(epWithRFID.EPCCODE);
                            checkDataInSQLite.setUserNo(SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString());
                            checkDataInSQLiteList.add(checkDataInSQLite);
                        }
                        dao.insertOrReplaceInTx(checkDataInSQLiteList);
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                OthersUtil.ToastMsg(getApplicationContext(),"下载成功！！");
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                super.error(hsWebInfo);
                OthersUtil.showTipsDialog(BindCardQueryEquipmentSubmitActivity.this,hsWebInfo.error.error);
                rbQueryEquipmentSubmitNetwork.setChecked(true);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bind_card_query_equipment_submit_activity;
    }

    /**
     * 清除数据
     */
    private void clearDownData(){
        OthersUtil.showChooseDialog(this, "离线数据可能有您修改但未上传的，确认清除已下载的离线数据？",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                OthersUtil.showLoadDialog(dialog);
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(BindCardQueryEquipmentSubmitActivity.this, "")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                subClearDownData();
                                return new HsWebInfo();
                            }
                        })
                        , getApplicationContext(), dialog, new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        OthersUtil.ToastMsg(getApplicationContext(),"清除成功！！");
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        super.error(hsWebInfo);
                        OthersUtil.showTipsDialog(BindCardQueryEquipmentSubmitActivity.this,hsWebInfo.error.error);
                    }
                });
            }
        });
    }

    /**
     * 查询
     */
    @OnClick(R.id.btnQueryEquipmentSubmitSearch)
    void search(){
        searchEquipment();
    }

    @OnItemLongClick(R.id.lvQueryEquipmentSubmit)
    boolean updateOrDelete(final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setItems(new CharSequence[]{"修改绑定的EPCCode", "删除绑定的EPCCode"},
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    //修改
                    case 0:
                        updateRFID(mList.get(position));
                        break;
                    //删除
                    case 1:
                        deleteRFID(mList.get(position));
                        break;
                }
            }
        })
                .show();
        return true;
    }

    /**
     * 查询已绑定设备的RFID
     */
    private void searchEquipment(){
        String search=etQueryEquipmentSubmitSearch.getText().toString().trim();
        if(search.isEmpty()){
            OthersUtil.ToastMsg(getApplicationContext(),"请输入内容后进行查询！！");
            return;
        }
        mList.clear();
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,search)
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        //有网的情况下
                        if(rbQueryEquipmentSubmitNetwork.isChecked()){
                            return  NewRxjavaWebUtils.getJsonData(getApplicationContext()
                                    ,"spappQueryEPBind",
                                    "sSearch=" + s,
                                    EpWithRFID.class.getName(),
                                    true, "查询不到已绑定的设备！！");
                        }else {
                            HsWebInfo hsWebInfo=new HsWebInfo();
                            CheckDataInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getCheckDataInSQLiteDao();
                            List<CheckDataInSQLite> checkDataInSQLiteList=dao.queryBuilder()
                                    .where(CheckDataInSQLiteDao.Properties.UserNo
                                    .eq(SPHelper.getLocalData(getApplicationContext(),
                                            USER_NO_KEY,String.class.getName(),"").toString()))
                                    .where(CheckDataInSQLiteDao.Properties.Status.notEq(2))
                                    .list();
                            //说明没有下载验证数据
                            if(checkDataInSQLiteList==null||checkDataInSQLiteList.isEmpty()){
                                hsWebInfo.success=false;
                                hsWebInfo.error.error="请先下载校验的资产数据，再查询";
                                return hsWebInfo;
                            }
                            List<CheckDataInSQLite> returnList=new ArrayList<>();
                            for(int i=0;i<checkDataInSQLiteList.size();i++){
                                CheckDataInSQLite checkDataInSQLite=checkDataInSQLiteList.get(i);
                                if(checkDataInSQLite.getAssetsCode().toLowerCase().contains(s.toLowerCase().trim())||
                                        checkDataInSQLite.getOutFactoryCode().toLowerCase().contains(s.toLowerCase().trim())||
                                        checkDataInSQLite.getEquipmentName().toLowerCase().contains(s.toLowerCase().trim())||
                                        checkDataInSQLite.getRFID().toLowerCase().contains(s.toLowerCase().trim())){
                                    returnList.add(checkDataInSQLite);
                                }
                            }
                            if(returnList.isEmpty()){
                                hsWebInfo.success=false;
                                hsWebInfo.error.error="查询不到已绑定的设备！！";
                                return  hsWebInfo;
                            }
                            hsWebInfo.object=returnList;
                            return  hsWebInfo;
                        }
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            @SuppressWarnings("unchecked")
            public void success(HsWebInfo hsWebInfo) {
                if (rbQueryEquipmentSubmitNetwork.isChecked()) {
                    List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                    for (int i = 0; i < entities.size(); i++) {
                        mList.add((EpWithRFID) entities.get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                List<CheckDataInSQLite> checkDataInSQLiteList = (List<CheckDataInSQLite>) hsWebInfo.object;
                for (int i = 0; i < checkDataInSQLiteList.size(); i++) {
                    CheckDataInSQLite checkDataInSQLite = checkDataInSQLiteList.get(i);
                    EpWithRFID epWithRFID = new EpWithRFID();
                    epWithRFID.ASSETSCODE = checkDataInSQLite.getAssetsCode();
                    epWithRFID.BRAND = checkDataInSQLite.getBrand();
                    epWithRFID.COSTCENTER = checkDataInSQLite.getCostCenter();
                    epWithRFID.DECLARATIONNUM = checkDataInSQLite.getDeclarationNum();
                    epWithRFID.DEPRECIATIONSTARTINGDATE = checkDataInSQLite.getDepreciationStartingDate();
                    epWithRFID.EQUIPMENTDETAILID = checkDataInSQLite.getEquipmentDetailID();
                    epWithRFID.EPCCODE = checkDataInSQLite.getRFID();
                    epWithRFID.INFACTORYDATE = checkDataInSQLite.getInFactoryDate();
                    epWithRFID.EQUIPMENTNAME = checkDataInSQLite.getEquipmentName();
                    epWithRFID.MODEL = checkDataInSQLite.getModel();
                    epWithRFID.OUTFACTORYCODE = checkDataInSQLite.getOutFactoryCode();
                    mList.add(epWithRFID);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                super.error(hsWebInfo);
                OthersUtil.ToastMsg(getApplicationContext(),hsWebInfo.error.error);
                mAdapter.notifyDataSetChanged();

            }
        });
//        RxjavaWebUtils.requestByGetJsonData(this,
//                "spappQueryEPBind",
//                "sSearch=" + search,
//                getApplicationContext(),
//                dialog,
//                EpWithRFID.class.getName(),
//                true, "查询不到已绑定的设备！！",
//                new SimpleHsWeb() {
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        OthersUtil.dismissLoadDialog(dialog);
//                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
//                        for(int i=0;i<entities.size();i++){
//                            mList.add((EpWithRFID) entities.get(i));
//                        }
//                        mAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void error(HsWebInfo hsWebInfo) {
//                        super.error(hsWebInfo);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                });
    }

    /**
     * 修改RFID
     */
    private void updateRFID(final EpWithRFID epWithRFID){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.ep_submit_update_dialog,null);
        etEPSubmitUpdateDialog=(EditText) view.findViewById(R.id.etEPSubmitUpdateDialog);
        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String RFID=etEPSubmitUpdateDialog.getText().toString().trim();
                        if(RFID.isEmpty()){
                            OthersUtil.dialogNotDismissClickOut(dialogInterface);
                            OthersUtil.ToastMsg(getApplicationContext(),"请输入/扫描EPCCode号");
                            return;
                        }
                        if(RFIDMap.get(RFID)!=null){
                            OthersUtil.dialogNotDismissClickOut(dialogInterface);
                            OthersUtil.showTipsDialog(BindCardQueryEquipmentSubmitActivity.this,"已绑定此EPCCode，但未上传");
                            return;
                        }
                        OthersUtil.dialogDismiss(dialogInterface);
                        etEPSubmitUpdateDialog=null;
                        if(rbQueryEquipmentSubmitNetwork.isChecked()) updateByServer(epWithRFID,RFID);
                        else updateBySQLite(epWithRFID,RFID);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        OthersUtil.dialogDismiss(dialogInterface);
                        etEPSubmitUpdateDialog=null;
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * 服务器进行修改
     */
    private void updateByServer(final EpWithRFID epWithRFID, final String RFID){
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,
                        "iType=0" +
                        ",iEquipmentDetailID=" + epWithRFID.EQUIPMENTDETAILID +
                        ",sUserNo="+SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString()+
                        ",RFID=" + RFID)
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                "spappEPWithRFIDAction",
                                s,
                                WsData.class.getName(),
                                false,
                                "修改失败！！");
                    }
                })
                .map(new Func1<HsWebInfo, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                        if(!hsWebInfo.success) return hsWebInfo;
                        //修改盘点的设备（主要是之前未盘完的设备）
                        InventoryDetailDao inventoryDetailDao = OthersUtil.getGreenDaoSession(getApplicationContext())
                                .getInventoryDetailDao();
                        Query<InventoryDetail> query = inventoryDetailDao.queryBuilder()
                                .where(InventoryDetailDao.Properties.AssetsCode.eq(epWithRFID.ASSETSCODE))
                                .build();
                        if (query == null) return hsWebInfo;
                        List<InventoryDetail> list=query.list();
                        if(list==null) return hsWebInfo;
                        for(int i=0;i<list.size();i++){
                            InventoryDetail detail=list.get(i);
                            if(!detail.getAssetsCode().equals(epWithRFID.ASSETSCODE))continue;
                            detail.setEPCode(RFID);
                            inventoryDetailDao.insertOrReplaceInTx(detail);
                        }
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                searchEquipment();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                OthersUtil.showTipsDialog(BindCardQueryEquipmentSubmitActivity.this,hsWebInfo.error.error);
            }
        });
    }

    /**
     * SQLite进行修改
     */
    private void updateBySQLite(EpWithRFID epWithRFID, final String RFID){
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, epWithRFID)
                .map(new Func1<EpWithRFID, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(EpWithRFID e) {
                        HsWebInfo hsWebInfo=new HsWebInfo();
                        CheckDataInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getCheckDataInSQLiteDao();
                        //先查询出RFID 是否绑定过
                        List<CheckDataInSQLite> checkRFIDList=dao.queryBuilder().where(CheckDataInSQLiteDao.Properties.UserNo
                                .eq(SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString()))
                                .where(CheckDataInSQLiteDao.Properties.RFID.eq(RFID))
                                .list();

                        List<CheckDataInSQLite> list=dao.queryBuilder().where(CheckDataInSQLiteDao.Properties.UserNo
                                .eq(SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString()))
                                .where(CheckDataInSQLiteDao.Properties.AssetsCode.eq(e.ASSETSCODE))
                                .list();
                        if(checkRFIDList!=null&&!checkRFIDList.isEmpty()){
                            hsWebInfo.success=false;
                            hsWebInfo.error.error="EPCCode:"+RFID+"在资产号："+checkRFIDList.get(0).getAssetsCode()+"上已绑定过！！";
                            return hsWebInfo;
                        }

                        if(list==null||list.isEmpty()){
                            hsWebInfo.success=false;
                            hsWebInfo.error.error="修改失败！！";
                            return hsWebInfo;
                        }
                        CheckDataInSQLite checkDataInSQLite=list.get(0);
                        checkDataInSQLite.setRFID(RFID);
                        checkDataInSQLite.setStatus(1);
                        dao.updateInTx(checkDataInSQLite);
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                searchEquipment();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                OthersUtil.showTipsDialog(BindCardQueryEquipmentSubmitActivity.this,hsWebInfo.error.error);
            }
        });
    }

    /**
     * 删除RFID
     */
    private void deleteRFID(final EpWithRFID epWithRFID){
        OthersUtil.showChooseDialog(this, "确认删除？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                OthersUtil.showLoadDialog(dialog);
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(BindCardQueryEquipmentSubmitActivity.this, epWithRFID)
                        .map(new Func1<EpWithRFID, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(EpWithRFID e) {
                                //在线校验
                                HsWebInfo hsWebInfo;
                                if(rbQueryEquipmentSubmitNetwork.isChecked()){
                                    hsWebInfo=NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                            "spappEPWithRFIDAction",
                                            "iType=1" +
                                            ",iEquipmentDetailID=" + e.EQUIPMENTDETAILID+
                                            ",sUserNo="+SPHelper.getLocalData(getApplicationContext(),
                                                    USER_NO_KEY,String.class.getName(),"").toString(),
                                            WsData.class.getName(),
                                            false,
                                            "删除EPCCode失败！！");
                                }else {
                                    hsWebInfo=new HsWebInfo();
                                    CheckDataInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getCheckDataInSQLiteDao();
                                    List<CheckDataInSQLite> list=dao.queryBuilder().where(CheckDataInSQLiteDao.Properties.UserNo
                                            .eq(SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString()))
                                            .where(CheckDataInSQLiteDao.Properties.AssetsCode.eq(e.ASSETSCODE))
                                            .list();
                                    if(list==null||list.isEmpty()){
                                        hsWebInfo.success=false;
                                        hsWebInfo.error.error="删除EPCCode失败！！";
                                        return hsWebInfo;
                                    }
                                    CheckDataInSQLite checkDataInSQLite=list.get(0);
                                    checkDataInSQLite.setStatus(2);
                                    checkDataInSQLite.setRFID("");
                                    dao.updateInTx(checkDataInSQLite);
                                }
                                hsWebInfo.object=e;
                                return hsWebInfo;
                            }
                        }), getApplicationContext(), dialog, new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        if(rbQueryEquipmentSubmitNetwork.isChecked())
                        EventBus.getDefault().post(new RFIDActionEvent(RFIDActionEvent.DELETE_INDEX,hsWebInfo.object));
                        searchEquipment();
                    }
                });
//                RxjavaWebUtils.requestByGetJsonData(BindCardQueryEquipmentSubmitActivity.this,
//                                "spappEPWithRFIDAction",
//                                "iType=1" +
//                                ",iEquipmentDetailID=" + epWithRFID.EQUIPMENTDETAILID,
//                        getApplicationContext(),
//                        dialog,
//                        WsData.class.getName(),
//                        false,
//                        "删除RFID失败！！",
//                        new SimpleHsWeb() {
//                            @Override
//                            public void success(HsWebInfo hsWebInfo) {
//
//                            }
//                        });
            }
        });
    }

    /**
     * 来自设备的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fromBluetooth(MainEvent event){
        if(event.aClass!=BindCardQueryEquipmentSubmitActivity.class) return;
        switch (event.index){
            case RECEIVE_DATA_INDEX:
                receiveData((Response_TagData) event.object);
                break;
            case TRIGGER_OPERATION_INDEX:
                pressOrReleaseTrigger((Notification) event.object);
                break;
        }
    }

    /**
     * 接收到数据的信号
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void readerResponseData(GenericReaderResponseEvent event){
        synchronized (this) {
            switch (event.index) {
                case RECEIVED_DATA_INDEX:
                    RFD8500DeviceUtils.dataReceivedFromBluetooth(event.data, metaData,BindCardQueryEquipmentSubmitActivity.class);
                    break;
                case METADATA_INDEX:
                    if(event.aClass!=BindCardQueryEquipmentSubmitActivity.class) return;
                    metaData = (MetaData) event.msg;
                    break;
                case RESPONSE_DATA_INDEX:
                    if(event.aClass!=BindCardQueryEquipmentSubmitActivity.class) return;
                    responseDataParsedFromGenericReader((ResponseMsg) event.msg);
                    break;
                case NOTIFICATION_INDEX:
                    if(event.aClass!=BindCardQueryEquipmentSubmitActivity.class) return;
                    notificationFromGenericReader((Notification) event.msg);
                    break;
            }
        }
    }


    private void startInventory(){
        //进行盘点（一直在运行）
        Command_Inventory inventoryCommand = new Command_Inventory();
        RFD8500DeviceUtils.sendCommand(inventoryCommand);
    }

    /**
     * 停止盘点
     */
    private void stopInventory(){
        RFD8500DeviceUtils.sendCommand(new Command_abort());
    }


    /**
     * 获得数据
     * @param data
     */
    private synchronized void receiveData(Response_TagData data){
        String epCode=data.EPCId+"";
//        OthersUtil.dismissLoadDialog(dialog);
//        if(RFIDMap.get(epCode)==null){
//
//        }
        if(etEPSubmitUpdateDialog!=null){
            etEPSubmitUpdateDialog.setText(epCode);
        }else {
            etQueryEquipmentSubmitSearch.setText(epCode);
        }
    }



    /**
     * 按下或者松开按键
     */
    private void pressOrReleaseTrigger(Notification notification){
        if (notification instanceof Notification_TriggerEvent) {
            Notification_TriggerEvent triggerEvent = (Notification_TriggerEvent) notification;
            switch (triggerEvent.TriggerValue) {
                case TRIGGER_PRESS:
                    if(!isInventorying){
                        isInventorying=true;
                    }
                    break;
                case TRIGGER_RELEASE:
                    if(isInventorying){
                        isInventorying=false;
                    }
                    break;
            }
            changedByScan();
        }
    }

    private void responseDataParsedFromGenericReader(ResponseMsg responseMsg) {
        if (RESPONSE_TYPE.TAGDATA == responseMsg.getResponseType()) {
            Response_TagData response_tagData = (Response_TagData) responseMsg;
            String operationStatus = null;
            if (response_tagData.tagAcessOprations != null) {
                for (AcessOperation acessOperation : response_tagData.tagAcessOprations) {
                    if (acessOperation.opration.equalsIgnoreCase("read")) {
                        operationStatus = acessOperation.operationStatus;
                    }
                }
            }
            if (operationStatus == null || operationStatus.equalsIgnoreCase(Constant.STATUS_OK)) {
                EventBus.getDefault().post(new MainEvent(BindCardQueryEquipmentSubmitActivity.class,response_tagData,RECEIVE_DATA_INDEX));
            }
        }
    }

    private void notificationFromGenericReader(Notification notification) {
        EventBus.getDefault().post(new MainEvent(BindCardQueryEquipmentSubmitActivity.class,notification,TRIGGER_OPERATION_INDEX));
    }


    /**
     * 根据扫描的状态改变控件的状态
     */
    private void changedByScan(){
        if(isInventorying){
            startInventory();
        }else {
            stopInventory();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OthersUtil.unregisterEvent(this);
    }

    @Override
    public void onBackPressed() {
        submitDataWithNoNetwork(true);
    }


    /**
     * 上传离线数据
     */
    private void submitDataWithNoNetwork(final boolean isBack){
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        HsWebInfo hsWebInfo=new HsWebInfo();
                        CheckDataInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getCheckDataInSQLiteDao();
                        hsWebInfo.object=dao.queryBuilder().where(CheckDataInSQLiteDao.Properties.UserNo
                                .eq(SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString()))
                                .where(CheckDataInSQLiteDao.Properties.Status.in(1,2))
                                .list();
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            @SuppressWarnings("unchecked")
            public void success(HsWebInfo hsWebInfo) {
                final List<CheckDataInSQLite> list= (List<CheckDataInSQLite>) hsWebInfo.object;
                //没有修改和删除的资产
                if(list==null||list.isEmpty()){
                    if(isBack) BindCardQueryEquipmentSubmitActivity.super.onBackPressed();
                    else OthersUtil.showTipsDialog(BindCardQueryEquipmentSubmitActivity.this,"您还未修改/删除任何资产，请核对！！");
                }else {
                    OthersUtil.showDoubleChooseDialog(BindCardQueryEquipmentSubmitActivity.this,
                            "您有离线状态下修改/删除、但未上传的资产数据，需要上传吗？",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(isBack) BindCardQueryEquipmentSubmitActivity.super.onBackPressed();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //上传离线修改/删除的数据
                                    subSubmitDataWithNoNetwork(list);
                                }
                            });
                }
            }
        });
    }

    /**
     * 上传离线修改/删除的数据
     */
    private void subSubmitDataWithNoNetwork(final List<CheckDataInSQLite> checkDataInSQLiteList){
        StringBuilder sb=new StringBuilder();
        for (CheckDataInSQLite checkDataInSQLite:checkDataInSQLiteList){
            if(checkDataInSQLite.getStatus()==0)continue;
            sb.append("exec")
                    .append(" spappEPWithRFIDAction ")
                    .append("@iType=").append((checkDataInSQLite.getStatus()-1))
                    .append(",@iEquipmentDetailID='").append(checkDataInSQLite.getEquipmentDetailID()).append("'")
                    .append(",@sUserNo='").append(SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString()).append("'")
                    .append(",@RFID='").append(checkDataInSQLite.getStatus()==1?checkDataInSQLite.getRFID():"").append("'")
                    .append("; ");

        }
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, sb.toString())
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),s,"",
                                WsData.class.getName(),false,"上传失败！！");
                    }
                })
                //清空离线下载的数据
                .map(new Func1<HsWebInfo, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                        if(!hsWebInfo.success) return hsWebInfo;
                        subClearDownData();
                        return hsWebInfo;
                    }
                })
               , getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                OthersUtil.ToastMsg(getApplicationContext(),"上传成功！！");
                for(CheckDataInSQLite checkDataInSQLite:checkDataInSQLiteList){
                    if(checkDataInSQLite.getStatus()!=2) continue;
                    EpWithRFID epWithRFID=new EpWithRFID();
                    epWithRFID.ASSETSCODE = checkDataInSQLite.getAssetsCode();
                    epWithRFID.BRAND = checkDataInSQLite.getBrand();
                    epWithRFID.COSTCENTER = checkDataInSQLite.getCostCenter();
                    epWithRFID.DECLARATIONNUM = checkDataInSQLite.getDeclarationNum();
                    epWithRFID.DEPRECIATIONSTARTINGDATE = checkDataInSQLite.getDepreciationStartingDate();
                    epWithRFID.EQUIPMENTDETAILID = checkDataInSQLite.getEquipmentDetailID();
                    epWithRFID.EPCCODE = checkDataInSQLite.getRFID();
                    epWithRFID.INFACTORYDATE = checkDataInSQLite.getInFactoryDate();
                    epWithRFID.EQUIPMENTNAME = checkDataInSQLite.getEquipmentName();
                    epWithRFID.MODEL = checkDataInSQLite.getModel();
                    epWithRFID.OUTFACTORYCODE = checkDataInSQLite.getOutFactoryCode();
                    EventBus.getDefault().post(new RFIDActionEvent(RFIDActionEvent.DELETE_INDEX,epWithRFID));
                }

                BindCardQueryEquipmentSubmitActivity.super.onBackPressed();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                super.error(hsWebInfo);
                OthersUtil.showTipsDialog(BindCardQueryEquipmentSubmitActivity.this,hsWebInfo.error.error);
            }
        });
    }

    /**
     * 清空离线数据
     */
    private void subClearDownData(){
        CheckDataInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getCheckDataInSQLiteDao();
        List<CheckDataInSQLite> list=dao.queryBuilder().where(CheckDataInSQLiteDao.Properties.UserNo
                .eq(SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString()))
                .list();
        dao.deleteInTx(list);
    }
}
