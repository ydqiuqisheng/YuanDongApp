package net.huansi.equipment.equipmentapp.activity.repair;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;

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
import net.huansi.equipment.equipmentapp.adapter.RepairListAdapter;
import net.huansi.equipment.equipmentapp.constant.Constant;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.MainEvent;
import net.huansi.equipment.equipmentapp.entity.RepairList;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent;
import net.huansi.equipment.equipmentapp.gen.RepairBaseDataInSQLiteDao;
import net.huansi.equipment.equipmentapp.gen.RepairEquipmentInSQLiteDao;
import net.huansi.equipment.equipmentapp.gen.RepairHdrInSQLiteDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairBaseDataInSQLite;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairEquipmentInSQLite;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairHdrInSQLite;
import net.huansi.equipment.equipmentapp.util.NetUtil;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RFD8500DeviceUtils;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.RECEIVE_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.TRIGGER_OPERATION_INDEX;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.PLAN_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.SQLITE_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.METADATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.NOTIFICATION_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RECEIVED_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RESPONSE_DATA_INDEX;

/**
 * Created by 单中年 on 2017/2/28.
 */

public class RepairListActivity extends BaseActivity implements OnKeyListener{
    @BindView(R.id.lvRepairList) ListView lvRepairList;
    @BindView(R.id.etRepairSearch) EditText etRepairSearch;

    private LoadProgressDialog dialog;
    private List<RepairList> mList;
//    private List<List<RepairPlanList>> mChildList;
//    private List<RepairPlanList> mSourceList;//源数据
    private RepairListAdapter mAdapter;

    private boolean isInventorying;
    private MetaData metaData;

    @Override
    protected int getLayoutId() {
        return R.layout.repair_plan_list_activity;
    }

    @Override
    public void init() {
        OthersUtil.registerEvent(this);
        setToolBarTitle("搜索维修");
        OthersUtil.hideInputFirst(this);
        mList=new ArrayList<>();
        dialog=new LoadProgressDialog(this);
        mAdapter=new RepairListAdapter(mList,getApplicationContext());
        lvRepairList.setAdapter(mAdapter);
        etRepairSearch.setOnKeyListener(this);
        RFD8500DeviceUtils.setPowerLevel((short) 130);
    }

    /**
     * 根据工厂搜索计划
     */
    @OnClick(R.id.btnRepairSearch)
    void searchPlanByFactory(){
        String search=etRepairSearch.getText().toString().trim();
        searchEquipmentByRepair(search);
    }

    /**
     * 查询维修的设备
     */
    private synchronized void searchEquipmentByRepair(String search){
        if(search.trim().isEmpty()){
            return;
        }
        OthersUtil.showLoadDialog(dialog);
        mList.clear();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, search)
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        //有网络就在线查询
                        if(NetUtil.isNetworkAvailable(getApplicationContext())){
                            return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                            "spAppEPQueryRepairPlanList",
                                    "sSearch=" + s,
                                    RepairList.class.getName(),
                                    true,
                                    "没有您要查询的维修/维护设备");
                            //离线查询
                        }else {
                            HsWebInfo hsWebInfo=new HsWebInfo();
                            RepairEquipmentInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getRepairEquipmentInSQLiteDao();
                            List<RepairEquipmentInSQLite> repairEquipmentInSQLiteList=dao.loadAll();
                            List<WsEntity> entities=new ArrayList<>();
                            for(RepairEquipmentInSQLite repairEquipmentInSQLite:repairEquipmentInSQLiteList){
                                if(repairEquipmentInSQLite.getEpcCode().equalsIgnoreCase(s)||
                                        repairEquipmentInSQLite.getAssetsCode().equalsIgnoreCase(s)||
                                        repairEquipmentInSQLite.getOutFactoryCode().equalsIgnoreCase(s)){
                                    RepairList repairList=new RepairList();
                                    repairList.ASSETSCODE=repairEquipmentInSQLite.getAssetsCode();
                                    repairList.COSTCENTER=repairEquipmentInSQLite.getCostCenter();
                                    repairList.EPCCODE=repairEquipmentInSQLite.getEpcCode();
                                    repairList.EQUIPMENTDETAILID=repairEquipmentInSQLite.getEquipmentDetailID();
                                    repairList.EQUIPMENTNAME=repairEquipmentInSQLite.getEquipmentName();
                                    repairList.FACTORY=repairEquipmentInSQLite.getFactory();
                                    repairList.MODEL=repairEquipmentInSQLite.getModel();
                                    repairList.OUTFACTORYCODE=repairEquipmentInSQLite.getOutFactoryCode();
                                    entities.add(repairList);
                                }
                            }
                            if(entities.isEmpty()){
                                hsWebInfo.success=false;
                                hsWebInfo.error.error="没有您要查询的维修/维护设备";
                                return hsWebInfo;
                            }

                            hsWebInfo.wsData=new WsData();
                            hsWebInfo.wsData.LISTWSDATA=entities;
                            Log.e("TAG","entities1="+entities);
                            return hsWebInfo;
                        }
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                Log.e("TAG","entities2="+entities.get(0));
                for(int i=0;i<entities.size();i++){
                    mList.add((RepairList) entities.get(i));
                }
                Log.e("TAG","我的list="+mList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                super.error(hsWebInfo);
                OthersUtil.showTipsDialog(RepairListActivity.this,hsWebInfo.error.error);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 维修详情界面
     */
    @OnItemClick(R.id.lvRepairList)
    void toRepairDetail(int position){
        final RepairList list=mList.get(position);
        OthersUtil.showLoadDialog(dialog);
        Observable.just(list)
                .compose(this.<RepairList>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Func1<RepairList, RepairHdrInSQLite>() {
                    @Override
                    public RepairHdrInSQLite call(RepairList repairList) {
                        RepairHdrInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext())
                                .getRepairHdrInSQLiteDao();
                        Query<RepairHdrInSQLite> query=dao.queryBuilder()
                                .where(RepairHdrInSQLiteDao.Properties.EquipmentId.eq(repairList.EQUIPMENTDETAILID))
                                .where(RepairHdrInSQLiteDao.Properties.SubmitStatus.eq(0))
                                .orderDesc(RepairHdrInSQLiteDao.Properties.Id)
                                .build();
                        if(query==null) return null;
                        List<RepairHdrInSQLite> list=query.list();
                        if(list==null||list.isEmpty()) return null;
                        return list.get(0);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RepairHdrInSQLite>() {
                    @Override
                    public void call(final RepairHdrInSQLite repairHdrInSQLite) {
                        OthersUtil.dismissLoadDialog(dialog);
                        if(repairHdrInSQLite!=null) {
                           OthersUtil.showDoubleChooseDialog(RepairListActivity.this,
                                   "此资产设备上次未修完，是否继续之前的维修进行处理？",
                                   new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           Intent intent = new Intent(RepairListActivity.this, RepairDetailActivity.class);
                                           intent.putExtra(PLAN_PARAM, list);
                                           startActivity(intent);
                                       }
                                   },
                                   //继续上一次维修
                                    new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   Intent intent=new Intent(RepairListActivity.this,RepairDetailActivity.class);
                                                   intent.putExtra(SQLITE_DATA_PARAM,repairHdrInSQLite);
                                                   startActivity(intent);
                                               }
                                           });
                        }else {
                            Intent intent = new Intent(RepairListActivity.this, RepairDetailActivity.class);
                            intent.putExtra(PLAN_PARAM, list);
                            startActivity(intent);
                        }
                    }
                });
    }


    /**
     * 来自设备的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fromBluetooth(MainEvent event){
        if(event.aClass!=RepairListActivity.class) return;
        switch (event.index){
            case RECEIVE_DATA_INDEX:
                receiveData((Response_TagData) event.object);
                break;
            case TRIGGER_OPERATION_INDEX:
                pressOrReleaseTrigger((Notification) event.object);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void readerResponseData(GenericReaderResponseEvent event){
        synchronized (this) {
            switch (event.index) {
                case RECEIVED_DATA_INDEX:
                    RFD8500DeviceUtils.dataReceivedFromBluetooth(event.data, metaData,RepairListActivity.class);
                    break;
                case METADATA_INDEX:
                    if(event.aClass!=RepairListActivity.class) return;
                    metaData = (MetaData) event.msg;
                    break;
                case RESPONSE_DATA_INDEX:
                    if(event.aClass!=RepairListActivity.class) return;
                    responseDataParsedFromGenericReader((ResponseMsg) event.msg);
                    break;
                case NOTIFICATION_INDEX:
                    if(event.aClass!=RepairListActivity.class) return;
                    notificationFromGenericReader((Notification) event.msg);
                    break;
            }
        }
    }

    /**
     * 获得数据
     * @param data
     */
    private synchronized void receiveData(Response_TagData data){
        String epCode=data.EPCId+"";
        etRepairSearch.setText(epCode.trim());
    }

    /**
     * 按下或者松开按键
     */
    public void pressOrReleaseTrigger(Notification notification){
        if (notification instanceof Notification_TriggerEvent) {
            Notification_TriggerEvent triggerEvent = (Notification_TriggerEvent) notification;
            switch (triggerEvent.TriggerValue) {
                case TRIGGER_PRESS:
                    if (!isInventorying) {
                        isInventorying = true;
                    }
                    startInventory();
                    break;
                case TRIGGER_RELEASE:
                    if (isInventorying) {
                        isInventorying = false;

                    }
                    stopInventory();
                    break;
            }
        }
    }


    /**
     * 此方法在异步执行的
     * @param responseMsg
     */
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
                EventBus.getDefault().post(new MainEvent(RepairListActivity.class,response_tagData,RECEIVE_DATA_INDEX));
            }
        }
    }

    private void notificationFromGenericReader(Notification notification) {
        EventBus.getDefault().post(new MainEvent(RepairListActivity.class,notification,TRIGGER_OPERATION_INDEX));
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
        //进行盘点（一直在运行）
        RFD8500DeviceUtils.sendCommand(new Command_abort());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopInventory();
        RFD8500DeviceUtils.setVoice(0);
        OthersUtil.unregisterEvent(this);
    }

    @Override
    public synchronized boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if(dialog.isShowing()) return false;
                if(event.getAction()==KeyEvent.ACTION_UP) {
                    String rfid = etRepairSearch.getText().toString().trim();
                    etRepairSearch.getText().clear();
                    searchEquipmentByRepair(rfid);
                }
                break;
        }
        return false;
    }
}
