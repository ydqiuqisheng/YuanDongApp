package net.huansi.equipment.equipmentapp.activity.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import net.huansi.equipment.equipmentapp.activity.MailActivity;
import net.huansi.equipment.equipmentapp.adapter.InventoryDetailAdapter;
import net.huansi.equipment.equipmentapp.constant.Constant;
import net.huansi.equipment.equipmentapp.entity.EPInventory;
import net.huansi.equipment.equipmentapp.entity.EpWithRFID;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.InventoryArea;
import net.huansi.equipment.equipmentapp.entity.MainEvent;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent;
import net.huansi.equipment.equipmentapp.gen.DaoSession;
import net.huansi.equipment.equipmentapp.gen.InventoryDao;
import net.huansi.equipment.equipmentapp.gen.InventoryDetailDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.sqlite_db.Inventory;
import net.huansi.equipment.equipmentapp.sqlite_db.InventoryDetail;
import net.huansi.equipment.equipmentapp.util.DeviceUtil;
import net.huansi.equipment.equipmentapp.util.DrawableCache;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.PopupUtil;
import net.huansi.equipment.equipmentapp.util.RFD8500DeviceUtils;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryAreaListActivityConstants.RETURN_AREA_DATA;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.AREA_REQUEST_CODE;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.RECEIVE_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.TRIGGER_OPERATION_INDEX;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryMainActivityConstants.INVENTORY_ID_IN_SQLITE_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.MailActivityConstants.MAIL_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.METADATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.NOTIFICATION_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RECEIVED_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RESPONSE_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.util.SPHelper.INVENTORY_POWER_LEVEL;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by 单中年 on 2017/2/26.
 * 盘点的详细界面
 */

public class InventoryDetailActivity extends BaseActivity implements OnClickListener{
    @BindView(R.id.lvInventoryDetail) ListView lvInventoryDetail;
    @BindView(R.id.inventoryDetailInventoryingLayout) LinearLayout inventoryDetailInventoryingLayout;//正在盘点的圈圈
    @BindView(R.id.btnInventoryDetailStartStop) Button btnInventoryDetailStartStop;//开始或者结束盘点的按钮
    @BindView(R.id.tvInventoryDetailStatusQty) TextView tvInventoryDetailStatusQty;//状态的统计   未盘：0   盘亏：0   已盘：0    盘盈：0
    @BindView(R.id.cbInventoryDetailShowOverage) CheckBox cbInventoryDetailShowOverage;//显示盘盈
    @BindView(R.id.cbInventoryDetailShowNormal) CheckBox cbInventoryDetailShowNormal;//显示已盘
    @BindView(R.id.cbInventoryDetailShowNoInventory) CheckBox cbInventoryDetailShowNoInventory;//显示未盘
    @BindView(R.id.cbInventoryDetailShowLoss) CheckBox cbInventoryDetailShowLoss;//显示盘亏
    @BindView(R.id.etInventoryDetailSearch) EditText etInventoryDetailSearch;
    @BindView(R.id.cbInventoryDetailRemedy) CheckBox cbInventoryDetailRemedy;//补盘
//    @BindView(R.id.inventoryDetailBottomLayout) LinearLayout inventoryDetailBottomLayout;//下面的盘点按钮的layout


    private Button btnInventoryOverageScanPopStartStop;//盘盈的开始结束盘点按钮
    private LinearLayout inventoryOverageScanPopStopLayout;//盘盈的加载的进度条
    private TextView tvInventoryOverageScanPopScanResult;//盘盈的扫描结果
    private TextView tvInventoryOverageScanPopAssetsCode;//盘盈的资产编号
    private TextView tvInventoryOverageScanPopEPCode;//盘盈的EPCode

    private boolean isOverageInventorying=false;
    private PopupWindow popOverage;

    private InventoryDetailAdapter mAdapter;
    private List<InventoryDetail> mList;
    private List<InventoryDetail> mShowList;

    private boolean isInventorying=false;//是不是正在盘点中
    private Map<String,Integer> inventoryDetailMap;//key=EPCode value=position
    private Map<String,String> alreadyOverageInventoryMap;//整个厂已盘和盘盈的map key=EPCode value=EPCode

    private LoadProgressDialog dialog;

//    private long inventoryId;
    private Inventory inventoryHdrInSQLite;

    private boolean noInventoryChecked=false;//未盘
    private boolean lossChecked=false;//盘亏
    private boolean normalChecked=false;//已盘
    private boolean overageChecked=false;//盘盈


    private String epCodeByOverageOrLoss="";//盘盈或者盘亏的RFID信息

    private InventoryDetail overageOrLossDetail;

    private MetaData metaData;
//    private Vibrator vibrator;
    private String area="";

    @Override
    protected int getLayoutId() {
        return R.layout.inventory_detail_activity;
    }


    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        dialog=new LoadProgressDialog(this);
        setToolBarTitle("设备盘点");
        TextView tvSubmit=getSubTitle();
        tvSubmit.setText("更多");
//        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        tvSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(InventoryDetailActivity.this);
                builder.setItems(new String[]{"上传数据"/*, "同步数据"*/,"新增盘点的区域","盘点区域列表"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        submitData();
                                        break;
//                                    case 1:
//                                        OthersUtil.showChooseDialog(InventoryDetailActivity.this,
//                                                "同步数据可能需要几分钟，确认同步？", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        synchronizeData(true,null,-1);
//                                                    }
//                                                });
//                                        break;
                                    //新增盘点区域
                                    case 1:
                                        inputAreaDialog(false);
                                        break;
                                    case 2:
                                        Intent intent=new Intent(InventoryDetailActivity.this,InventoryAreaListActivity.class);
                                        intent.putExtra(INVENTORY_ID_IN_SQLITE_PARAM,inventoryHdrInSQLite);
                                        startActivityForResult(intent,AREA_REQUEST_CODE);
                                        break;
                                }
                            }
                        })
                        .show();



            }
        });
        etInventoryDetailSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showList();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        OthersUtil.registerEvent(this);
        inventoryDetailMap=new HashMap<>();
        mList=new LinkedList<>();
        mShowList=new ArrayList<>();
        alreadyOverageInventoryMap=new HashMap<>();
        mAdapter=new InventoryDetailAdapter(mShowList,getApplicationContext());
        lvInventoryDetail.setAdapter(mAdapter);
        OthersUtil.showLoadDialog(dialog);
        initPopByOverage();
        inventoryHdrInSQLite= (Inventory) getIntent().getSerializableExtra(INVENTORY_ID_IN_SQLITE_PARAM);//盘点主表的id
        cbInventoryDetailShowNoInventory.setChecked(true);
        noInventoryChecked=true;
        cbInventoryDetailShowLoss.setChecked(true);
        lossChecked=true;
        //设置信号
        float powerLevel= (float) SPHelper.getLocalData(getApplicationContext(),INVENTORY_POWER_LEVEL,Float.class.getName(),300f);
        RFD8500DeviceUtils.setPowerLevel((short) powerLevel);
        initData();
    }

    /**
     * 显示未盘
     */
    @OnCheckedChanged(R.id.cbInventoryDetailShowNoInventory)
    void showNoInventory(boolean isChecked){
        noInventoryChecked=isChecked;
        showList();
    }

    /**
     * 显示盘亏
     */
    @OnCheckedChanged(R.id.cbInventoryDetailShowLoss)
    void showLoss(boolean isChecked){
        lossChecked=isChecked;
        showList();
    }

    /**
     * 显示已盘
     */
    @OnCheckedChanged(R.id.cbInventoryDetailShowNormal)
    void showNormal(boolean isChecked){
        normalChecked=isChecked;
        showList();
    }

    /**
     * 显示盘盈
     */
    @OnCheckedChanged(R.id.cbInventoryDetailShowOverage)
    void showOverage(boolean isChecked){
        overageChecked=isChecked;
        showList();
    }


    /**
     * 显示列表
     */
    private void showList(){
        mShowList.clear();
        String search=etInventoryDetailSearch.getText().toString();
        for(int i=0;i<mList.size();i++){
            InventoryDetail detail=mList.get(i);
            if(!noInventoryChecked&&!lossChecked&&!normalChecked&&!overageChecked
                    && (detail.getAssetsCode().contains(search)||
                    detail.getOutFactoryCode().contains(search))){
                mShowList.add(detail);
                continue;
            }
            if(noInventoryChecked&&detail.getStatus()==-2
                    && (detail.getAssetsCode().contains(search)||
                    detail.getOutFactoryCode().contains(search))){
                mShowList.add(detail);
                continue;
            }
            if(lossChecked&&detail.getStatus()==-1
                    && (detail.getAssetsCode().contains(search)||
                    detail.getOutFactoryCode().contains(search))){
                mShowList.add(detail);
                continue;
            }
            if(normalChecked&&detail.getStatus()==0
                    && (detail.getAssetsCode().contains(search)||
                    detail.getOutFactoryCode().contains(search))){
                mShowList.add(detail);
                continue;
            }
            if(overageChecked&&detail.getStatus()==1
                    && (detail.getAssetsCode().contains(search)||
                    detail.getOutFactoryCode().contains(search))){
                mShowList.add(detail);
            }
        }
        Collections.sort(mShowList, new Comparator<InventoryDetail>() {
            @Override
            public int compare(InventoryDetail o1, InventoryDetail o2) {
                if(o1.getStatus()>o2.getStatus()) return 1;
                if(o1.getStatus()<o2.getStatus()) return -1;
                return 0;
            }
        });
        mAdapter.notifyDataSetChanged();
    }





    /**
     * 上传数据
     */
    private void submitData(){
        boolean flag=true;//true 没有盘亏
        for(int i=0;i<mList.size();i++){
            InventoryDetail detail=mList.get(i);
            if(detail.getStatus()==-1||detail.getStatus()==-2){
                flag=false;
                break;
            }
        }
        if(!flag){
            OthersUtil.showChooseDialog(InventoryDetailActivity.this, "您还有未盘的设备,上传则将未盘设备标识盘亏，确认上传吗？",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int which) {
                            subSubmitData();
                        }
                    });
        }else {
            OthersUtil.showChooseDialog(InventoryDetailActivity.this, "确认上传数据吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                    subSubmitData();
                }
            });
        }
    }


    /**
     * 上传数据
     */
    private void subSubmitData(){
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                //上传头表
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                "spAppEPSubmitInventoryHdr",
                                "APPEquipmentInventoryHdrID=" + mList.get(0).getInventoryParentId() +
                                        ",sUserNo=" + SPHelper.getLocalData(getApplicationContext(),
                                        USER_NO_KEY, String.class.getName(), "").toString(),
                                WsData.class.getName(),false,"上传失败！");
                    }
                })
                //上传明细
                .map(new Func1<HsWebInfo, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                        if(!hsWebInfo.success) return hsWebInfo;
                        //先获取到数据
                        DaoSession daoSession=OthersUtil.getGreenDaoSession(getApplicationContext());
                        InventoryDetailDao detailDao=daoSession.getInventoryDetailDao();
                        InventoryDao inventoryDao=daoSession.getInventoryDao();
                        List<InventoryDetail> inventoryDetails=detailDao.queryBuilder()
                                .where(InventoryDetailDao.Properties.InventoryHdrIdInSQLite.eq(inventoryHdrInSQLite.getId()))
                                .list();
                        if(inventoryDetails==null) inventoryDetails=new ArrayList<>();
                        StringBuilder sbSql=new StringBuilder();

                        for (InventoryDetail detail:inventoryDetails) {
                            sbSql.append("EXEC spAppEPSubmitInventoryDtl ")
                                    .append("@EquipmentInventoryItemID='").append(detail.getInventoryParentId()).append("'")
                                    .append(",@EquipmentListDetailItemID='").append(detail.getEquipmentChildId()).append("'")
                                    .append(",@InventoryStatus='").append( ((detail.getStatus() == -1||detail.getStatus()==-2) ?
                                                                                "盘亏" :
                                                                         (detail.getStatus() == 1 ? "盘盈" : "已盘"))).append("'")
                                    .append(";");
                        }
                        hsWebInfo=NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                sbSql.toString(),
                                "",
                                WsData.class.getName(),false,
                                "上传失败！！");
                        if(!hsWebInfo.success) return hsWebInfo;
                        //删除这些盘点记录
                        inventoryDao.deleteByKey(inventoryHdrInSQLite.getId());
                        detailDao.deleteInTx(inventoryDetails);
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                OthersUtil.ToastMsg(getApplicationContext(), "上传成功！！");
                finish();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                OthersUtil.showTipsDialog(InventoryDetailActivity.this,hsWebInfo.error.error);

            }
        });
    }

    /**
     * 盘盈的搜索popupWindow
     */
    private void initPopByOverage() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.inventory_overage_scan_pop, null);
        btnInventoryOverageScanPopStartStop = (Button) view.findViewById(R.id.btnInventoryOverageScanPopStartStop);
        inventoryOverageScanPopStopLayout = (LinearLayout) view.findViewById(R.id.inventoryOverageScanPopStopLayout);
        tvInventoryOverageScanPopScanResult = (TextView) view.findViewById(R.id.tvInventoryOverageScanPopScanResult);
        tvInventoryOverageScanPopAssetsCode = (TextView) view.findViewById(R.id.tvInventoryOverageScanPopAssetsCode);
        tvInventoryOverageScanPopEPCode = (TextView) view.findViewById(R.id.tvInventoryOverageScanPopEPCode);//发送邮件
        Button btnInventoryOverageScanPopCancel= (Button) view.findViewById(R.id.btnInventoryOverageScanPopCancel);//弹框的取消
        Button btnInventoryOverageScanPopMail= (Button) view.findViewById(R.id.btnInventoryOverageScanPopMail);
        btnInventoryOverageScanPopStartStop.setOnClickListener(this);
        btnInventoryOverageScanPopCancel.setOnClickListener(this);
        btnInventoryOverageScanPopMail.setOnClickListener(this);
        popOverage = new PopupWindow(view, (int) (DeviceUtil.getScreenWidth(this) * 0.8), (int) (DeviceUtil.getScreenHeight(this) * 0.8));
        popOverage.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popOverage.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void startInventory(){
        //进行盘点（一直在运行）
        Command_Inventory inventoryCommand = new Command_Inventory();
        RFD8500DeviceUtils.sendCommand(inventoryCommand);
        cbInventoryDetailRemedy.setEnabled(false);
        cbInventoryDetailRemedy.setAlpha(0.5f);
    }

    /**
     * 停止盘点
     */
    private void stopInventory(){
        RFD8500DeviceUtils.sendCommand(new Command_abort());
        cbInventoryDetailRemedy.setEnabled(true);
        cbInventoryDetailRemedy.setAlpha(1);
    }


    /**
     * 来自设备的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fromBluetooth(MainEvent event) {
        if (event.aClass != InventoryDetailActivity.class) return;
        switch (event.index) {
            case RECEIVE_DATA_INDEX:
                receiveData((Response_TagData) event.object);
                break;
            case TRIGGER_OPERATION_INDEX:
                pressOrReleaseTrigger((Notification) event.object);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void readerResponseData(GenericReaderResponseEvent event){
        synchronized ("readerResponseData") {
            switch (event.index) {
                case RECEIVED_DATA_INDEX:
                    RFD8500DeviceUtils.dataReceivedFromBluetooth(event.data, metaData,InventoryDetailActivity.class);
                    break;
                case METADATA_INDEX:
                    if(event.aClass!=InventoryDetailActivity.class) return;
                    metaData = (MetaData) event.msg;
                    break;
                case RESPONSE_DATA_INDEX:
                    if(event.aClass!=InventoryDetailActivity.class) return;
                    if(event.msg instanceof ResponseMsg) responseDataParsedFromGenericReader((ResponseMsg) event.msg);
                    break;
                case NOTIFICATION_INDEX:
                    if(event.aClass!=InventoryDetailActivity.class) return;
                    notificationFromGenericReader((Notification) event.msg);
                    break;
            }
        }
    }



    /**
     * 获得数据
     * @param data
     */
    private void receiveData(Response_TagData data) {
        String epCode = data.EPCId + "";
            //盘查盘盈盘亏的设备
        if (isOverageInventorying) {
            searchByOverage(epCode);
        } else {
            if(alreadyOverageInventoryMap.get(epCode)!=null) return;//全部已盘+盘盈中存在这个EPCCCode，那么就直接return
            //待盘点（未盘+已盘+盘盈）的设备中有扫描到的
            if (inventoryDetailMap.get(epCode) != null) {
                int position = inventoryDetailMap.get(epCode);
                if(position>mList.size()-1) return;
                InventoryDetail inventoryDetail = mList.get(position);
                switch (inventoryDetail.getStatus()){
                    //-2 未盘-1盘亏
                    case -1:
                    case -2:
                        if(cbInventoryDetailRemedy.isChecked()) {
                            OthersUtil.broadVoice(getApplicationContext(), R.raw.scaned);
                        }
                        inventoryDetail.setStatus(0);//0正常
                        saveToSQLite(inventoryDetail, false, position);
                        break;
                }
            //盘盈的设备需要插入
            } else {
                InventoryDetail addDetail = new InventoryDetail();
                addDetail.setStatus(1);
                addDetail.setEPCode(epCode);
                addDetail.setAssetsCode("");
                addDetail.setCostCenter("");
                addDetail.setDeclarationNum("");
                addDetail.setDepreciationStartingDate("");
                addDetail.setEquipmentChildId("");
                addDetail.setEquipmentName("");
                addDetail.setEquipmentParentId("");
                addDetail.setFactory("");
                addDetail.setInFactoryDate("");
                addDetail.setOutFactoryCode("");
                addDetail.setInventoryHdrIdInSQLite(inventoryHdrInSQLite.getId());
                saveToSQLite(addDetail, true, -1);
            }
        }
    }

    /**
     * 盘查盘盈的设备
     */
    private void searchByOverage(String EPCId){
        String epCode=tvInventoryOverageScanPopEPCode.getText().toString().trim();
        if(epCode.equals(EPCId)){
            OthersUtil.broadVoice(getApplicationContext(),R.raw.scaned);
            tvInventoryOverageScanPopScanResult.setText("已盘查到");
            Integer position=inventoryDetailMap.get(epCodeByOverageOrLoss);
            if(position==null||position>mList.size()-1) return;
            InventoryDetail detail=mList.get(position);
            if(detail.getStatus()==-2||detail.getStatus()==-1)detail.setStatus(0);
            saveToSQLite(detail,false,position);
        }

    }

    /**
     * 保存数据到SQLite中
     * @param detail 保存的明细
     * @param isAdd  是否保存成功
     * @param position 保存的位置
     */
    private void saveToSQLite(final InventoryDetail detail, final boolean isAdd, final int position){
        detail.setArea(area);
        Observable.just(detail)
                .compose(InventoryDetailActivity.this.<InventoryDetail>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Func1<InventoryDetail, InventoryDetail>() {
                    @Override
                    public InventoryDetail call(InventoryDetail inventoryDetail) {
                        InventoryDetailDao detailDao = OthersUtil.getGreenDaoSession(getApplicationContext())
                                .getInventoryDetailDao();
                        long id = detailDao.insertOrReplace(inventoryDetail);
                        return detailDao.load(id);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<InventoryDetail>() {
                    @Override
                    public void call(InventoryDetail inventoryDetail) {
                        OthersUtil.dismissLoadDialog(dialog);
                        if (inventoryDetail == null) return;
                        //不管盘盈还是已盘，都需要插进去
                        alreadyOverageInventoryMap.put(inventoryDetail.getEPCode(),inventoryDetail.getEPCode());
                        if(isAdd) {
                            mList.add(inventoryDetail);
                            inventoryDetailMap.put(inventoryDetail.getEPCode(),mList.size()-1);
                        }else {
                            mList.set(position,inventoryDetail);
                        }
                        showStatusQty();
                        showList();
                    }
                });
    }

    /**
     * 按下或者松开按键
     */
    public void pressOrReleaseTrigger(Notification notification){
        if (notification instanceof Notification_TriggerEvent) {
            Notification_TriggerEvent triggerEvent = (Notification_TriggerEvent) notification;
            switch (triggerEvent.TriggerValue) {
                case TRIGGER_PRESS:
                    if(popOverage.isShowing()){
                        if (!isOverageInventorying) {
                            isOverageInventorying = true;
                            inventoryByOverageOrLoss();
                        }
                    }else {
                        if (!isInventorying) {
                            isInventorying = true;
                            inventory();
                        }
                    }
                    break;
                case TRIGGER_RELEASE:
                    if(popOverage.isShowing()){
                        if (isOverageInventorying) {
                            isOverageInventorying = false;
                            inventoryByOverageOrLoss();
                        }
                    }else {
                        if (isInventorying) {
                            isInventorying = false;
                            inventory();
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 开始或者结束盘点
     */
    @OnClick(R.id.btnInventoryDetailStartStop)
    void startOrStopInventory(){
        isInventorying=!isInventorying;
        inventory();
    }

    /**
     * 盘点的显示以及扫描
     */
    private void inventory(){
        if(isInventorying){
            //补盘的时候需要开启低音
            if(cbInventoryDetailRemedy.isChecked()){
                RFD8500DeviceUtils.setVoice(3);
            }else {
                RFD8500DeviceUtils.setVoice(0);
            }
            startInventory();
            btnInventoryDetailStartStop.setText("结束盘点");
            btnInventoryDetailStartStop.setBackground(DrawableCache.getInstance().
                    getDrawable(R.drawable.btn_green_circle_selector,getApplicationContext()));
            inventoryDetailInventoryingLayout.setVisibility(View.VISIBLE);
        }else {
            RFD8500DeviceUtils.setVoice(0);
            stopInventory();
            btnInventoryDetailStartStop.setText("开始盘点");
            btnInventoryDetailStartStop.setBackground(DrawableCache.getInstance().
                    getDrawable(R.drawable.btn_blue_circle_selector,getApplicationContext()));
            inventoryDetailInventoryingLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 盘盈或者盘亏的盘查
     */
    private void inventoryByOverageOrLoss() {
        if (isOverageInventorying) {
            //静音
            RFD8500DeviceUtils.setVoice(3);
            startInventory();
            btnInventoryOverageScanPopStartStop.setText("结束盘查");
            btnInventoryOverageScanPopStartStop.setBackground(DrawableCache.getInstance().
                    getDrawable(R.drawable.btn_green_circle_selector, getApplicationContext()));
            inventoryOverageScanPopStopLayout.setVisibility(View.VISIBLE);
        }else {
            //高音
            RFD8500DeviceUtils.setVoice(0);
            stopInventory();
            btnInventoryOverageScanPopStartStop.setText("开始盘查");
            btnInventoryOverageScanPopStartStop.setBackground(DrawableCache.getInstance().
                    getDrawable(R.drawable.btn_blue_circle_selector, getApplicationContext()));
            inventoryOverageScanPopStopLayout.setVisibility(View.GONE);
        }
    }


    /**
     * 因为盘盈进行寻找设备
     */
    @OnItemLongClick(R.id.lvInventoryDetail)
    boolean searchDeviceForOverage(final int position){
        InventoryDetail detail=mShowList.get(position);
        switch (detail.getStatus()){
            case -2:
            case -1:
                longClickByStatus_1_2(detail);
                break;
            case 0:
                longClickByStatus0(detail);
                break;
            case 1:
                longClickByStatus1(detail);
                break ;
        }
        return true;
    }

    /**
     * 未盘以及盘亏的长按
     */
    private void longClickByStatus_1_2(final InventoryDetail detail){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setItems(new String[]{"查找盘亏/未盘"/*,"同步数据"*/}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        overageOrLossDetail=detail;
                        isInventorying=false;
                        inventoryByOverageOrLoss();
                        epCodeByOverageOrLoss=detail.getEPCode();
                        tvInventoryOverageScanPopEPCode.setText(detail.getEPCode());
                        tvInventoryOverageScanPopAssetsCode.setText(detail.getAssetsCode());
                        PopupUtil.showPopNoCancel(popOverage, InventoryDetailActivity.this, true, -1, new PopupUtil.PopDismissListener() {
                            @Override
                            public void dismiss() {
                                cancelOverageOrLossPop();
                                overageOrLossDetail=null;
                            }
                        });
                        RFD8500DeviceUtils.setPowerLevel((short) 300);
                        popOverage.showAtLocation(btnInventoryDetailStartStop, Gravity.CENTER,0,0);
                        break;
                }

            }
        })
                .show();
    }

    /**
     * 已盘的长按
     */
    private void longClickByStatus0( InventoryDetail detail){
    }

    /**
     * 盘盈的长按
     */
    private void longClickByStatus1(final InventoryDetail detail){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setItems(new String[]{"显示详细", "查找盘盈"/*,"同步数据"*/}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case 0:
                        OthersUtil.showLoadDialog(dialog);
                        RxjavaWebUtils.requestByGetJsonData(InventoryDetailActivity.this,
                                "spAppEPQueryEquipmentByEPCode",
                                "sEPCode=" + detail.getEPCode(), getApplicationContext(),
                                dialog,
                                EPInventory.class.getName(),
                                true,
                                "",
                                new SimpleHsWeb() {
                                    @Override
                                    public void success(HsWebInfo hsWebInfo) {
                                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                                        if (entities.isEmpty()) return;
                                        EPInventory epInventory = (EPInventory) entities.get(0);
                                        epInventory.SINVENTORYID = mList.get(0).getInventoryParentId();
                                        InventoryDetail addDetail = OthersUtil.exchangeByDownInventory(epInventory,
                                                inventoryHdrInSQLite.getId(), 1);
                                        Integer p=inventoryDetailMap.get(detail.getEPCode());
                                        if(p==null||p<0) return;
                                        saveToSQLite(addDetail, false,p);
                                    }

                                    @Override
                                    public void error(HsWebInfo hsWebInfo) {
                                        super.error(hsWebInfo);
                                        OthersUtil.showTipsDialog(InventoryDetailActivity.this,"服务器无数据");
                                    }
                                });
                        break;
                    case 1:
                        overageOrLossDetail = detail;
                        isInventorying = false;
                        inventoryByOverageOrLoss();
                        epCodeByOverageOrLoss = detail.getEPCode();
                        tvInventoryOverageScanPopEPCode.setText(detail.getEPCode());
                        tvInventoryOverageScanPopAssetsCode.setText(detail.getAssetsCode());

                        PopupUtil.showPopNoCancel(popOverage, InventoryDetailActivity.this, true, -1, new PopupUtil.PopDismissListener() {
                            @Override
                            public void dismiss() {
                                cancelOverageOrLossPop();
                                overageOrLossDetail=null;
                            }
                        });
                        RFD8500DeviceUtils.setPowerLevel((short) 300);
                        popOverage.showAtLocation(btnInventoryDetailStartStop, Gravity.CENTER, 0, 0);
                        break;
                }
            }
        })
                .show();



    }



    /**
     * 显示数量
     */
    private void showStatusQty(){
        int noInventory=0;
        int inventoryLoss=0;
        int normal=0;
        int inventoryOverage=0;
        for(int i=0;i<mList.size();i++){
            InventoryDetail detail=mList.get(i);
            switch (detail.getStatus()){
                case -2:
                    noInventory++;
                    break;
                case -1:
                    inventoryLoss++;
                    break;
                case 0:
                    normal++;
                    break;
                case 1:
                    inventoryOverage++;
                    break;
            }
        }
        tvInventoryDetailStatusQty.setText("全部："+mList.size()+"   未盘："+noInventory+"   盘亏："+inventoryLoss+"   已盘："+normal+"    盘盈："+inventoryOverage);
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
                EventBus.getDefault().post(new MainEvent(InventoryDetailActivity.class,response_tagData,RECEIVE_DATA_INDEX));
            }
        }

    }

    private void notificationFromGenericReader(Notification notification) {
        EventBus.getDefault().post(new MainEvent(InventoryDetailActivity.class,notification,TRIGGER_OPERATION_INDEX));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopInventory();
        RFD8500DeviceUtils.setVoice(0);
        OthersUtil.unregisterEvent(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInventoryOverageScanPopStartStop:
                isOverageInventorying=!isOverageInventorying;
                inventoryByOverageOrLoss();
                break;
            case R.id.btnInventoryOverageScanPopCancel:
                if(isOverageInventorying){
                    OthersUtil.showTipsDialog(this,"请先取消盘查");
                }else {
                    //设置信号
                    float powerLevel= (float) SPHelper.getLocalData(getApplicationContext(),INVENTORY_POWER_LEVEL,Float.class.getName(),300f);
                    RFD8500DeviceUtils.setPowerLevel((short) powerLevel);
                    popOverage.dismiss();
                }
                break;
            //发送邮件
            case R.id.btnInventoryOverageScanPopMail:
                Intent intent=new Intent(this, MailActivity.class);
                intent.putExtra(MAIL_DATA_PARAM,overageOrLossDetail);
                startActivity(intent);
                break;
        }
    }

    /**
     * 取消盘盈或者盘亏的弹框
     */
    private void cancelOverageOrLossPop(){
        isOverageInventorying=false;
        inventoryByOverageOrLoss();
        RFD8500DeviceUtils.setVoice(0);
        //设置信号
        float powerLevel= (float) SPHelper.getLocalData(getApplicationContext(),INVENTORY_POWER_LEVEL,Float.class.getName(),300f);
        RFD8500DeviceUtils.setPowerLevel((short) powerLevel);
        epCodeByOverageOrLoss="";
        tvInventoryOverageScanPopScanResult.setText("未盘查到");
    }
    @Override
    public void back() {
        if(popOverage.isShowing()){
            if(isOverageInventorying){
                OthersUtil.showTipsDialog(this,"请先取消盘查");
            }
        }else {
            super.back();
        }
    }


    /**
     * 初始化数据
     */
    private void initData(){
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        InventoryDetailDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getInventoryDetailDao();
                        String lastArea="";
                        //获取上一次的区域，不需要输入区域
                        List<InventoryDetail> detailList=dao.queryBuilder()
                                .where(InventoryDetailDao.Properties.InventoryHdrIdInSQLite.eq(inventoryHdrInSQLite.getId()))
                                .where(InventoryDetailDao.Properties.Area.isNotNull())
                                .where(InventoryDetailDao.Properties.Area.notEq(""))
                                .orderDesc(InventoryDetailDao.Properties.Id)
                                .limit(1)
                                .list();
                        if(detailList!=null&&!detailList.isEmpty()) lastArea=detailList.get(0).getArea();
                        HsWebInfo hsWebInfo=new HsWebInfo();
                        hsWebInfo.object=lastArea;
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                InventoryDetailActivity.this.area=hsWebInfo.object.toString();
                if(InventoryDetailActivity.this.area.isEmpty())inputAreaDialog(true);
                else showByArea();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                inputAreaDialog(true);
            }
        });
    }

    /**
     * 输入区域的dialog
     * @param isNotDismissByNotInput true=>不输入就不允许关闭
     */
    private void inputAreaDialog(final boolean isNotDismissByNotInput){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View areaDialogView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.area_input_dialog,null);
        final EditText editText= (EditText) areaDialogView.findViewById(R.id.etInventoryAreaDialog);
        editText.setHint("请输入您要盘点的区域");
        editText.setTextColor(Color.BLACK);
        builder.setView(areaDialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String area=editText.getText().toString().trim();
                        if(area.isEmpty()){
                            OthersUtil.dialogNotDismissClickOut(dialogInterface);
                            OthersUtil.ToastMsg(getApplicationContext(),"请输入您要盘点的区域");
                            return;
                        }
                        OthersUtil.dialogDismiss(dialogInterface);
                        dialogInterface.dismiss();
                        InventoryDetailActivity.this.area=area;
                        showByArea();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(isNotDismissByNotInput){
                            OthersUtil.dialogNotDismissClickOut(dialogInterface);
                        }else {
                            OthersUtil.dialogDismiss(dialogInterface);
                            dialogInterface.dismiss();
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }


    /**
     * 按区域显示盘点数据
     */
    @SuppressWarnings("unchecked")
    private void showByArea(){
        if(area==null||area.isEmpty()) return;
        setToolBarTitle("设备盘点（"+area+"）");
        OthersUtil.showLoadDialog(dialog);
        mList.clear();
        mShowList.clear();
        inventoryDetailMap.clear();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        HsWebInfo hsWebInfo=new HsWebInfo();
                        DaoSession daoSession=OthersUtil.getGreenDaoSession(getApplicationContext());
                        InventoryDetailDao detailDao=daoSession.getInventoryDetailDao();
                        //查出已盘和盘盈
                        List<InventoryDetail> detailList=detailDao.queryBuilder()
                                .where(InventoryDetailDao.Properties.Area.eq(area))
                                .where(InventoryDetailDao.Properties.InventoryHdrIdInSQLite.eq(inventoryHdrInSQLite.getId()))
                                .list();
                        if(detailList==null) detailList=new ArrayList<>();

                        //未盘的信息-2 未盘-1盘亏
                        List<InventoryDetail> noInventoryDetailList=detailDao.queryBuilder()
                                .where(InventoryDetailDao.Properties.Status.in(-1,-2))
                                .where(InventoryDetailDao.Properties.InventoryHdrIdInSQLite.eq(inventoryHdrInSQLite.getId()))
                                .list();
                        if(noInventoryDetailList!=null) detailList.addAll(noInventoryDetailList);
                        if(detailList.isEmpty()){
                            hsWebInfo.success=false;
                            return hsWebInfo;
                        }
                        //获取已盘和盘盈的设备 以防重复盘点
                        List<InventoryDetail> alreadyOverageDetailList=detailDao.queryBuilder()
                                .where(InventoryDetailDao.Properties.Status.in(0,1))
                                .where(InventoryDetailDao.Properties.InventoryHdrIdInSQLite.eq(inventoryHdrInSQLite.getId()))
                                .list();
                        if(alreadyOverageDetailList==null) alreadyOverageDetailList=new ArrayList<>();
                        Map<String,Object> map=new HashMap<>();
                        map.put("alreadyOverageDetailList",alreadyOverageDetailList);
                        map.put("detailList",detailList);
                        hsWebInfo.object=map;
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                Map<String,Object> map= (Map<String, Object>) hsWebInfo.object;
                List<InventoryDetail> alreadyOverageDetailList= (List<InventoryDetail>) map.get("alreadyOverageDetailList");//用来填充inventoryDetailMap
                List<InventoryDetail> detailList= (List<InventoryDetail>) map.get("detailList");
                mList.addAll(detailList);
                for (int i = 0; i < alreadyOverageDetailList.size(); i++) {
                    InventoryDetail inventoryDetail = alreadyOverageDetailList.get(i);
                    alreadyOverageInventoryMap.put(inventoryDetail.getEPCode(), inventoryDetail.getEPCode());
                }
                for (int i = 0; i < detailList.size(); i++) {
                    InventoryDetail inventoryDetail = detailList.get(i);
                    inventoryDetailMap.put(inventoryDetail.getEPCode(),i);
                }
                showList();
                showStatusQty();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;
        switch (requestCode){
            //返回的区域
            case AREA_REQUEST_CODE:
                InventoryArea inventoryArea= (InventoryArea) data.getSerializableExtra(RETURN_AREA_DATA);
                if(inventoryArea==null) return;
                area=inventoryArea.name;
                showByArea();
                break;
        }
    }
}
