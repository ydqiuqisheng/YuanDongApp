package net.huansi.equipment.equipmentapp.activity.check_goods;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.util.ArraySet;
import android.util.JsonReader;
import android.util.Log;

import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.zxing.common.StringUtils;
import com.motorolasolutions.ASCII_SDK.AcessOperation;
import com.motorolasolutions.ASCII_SDK.Command_Inventory;
import com.motorolasolutions.ASCII_SDK.Command_abort;
import com.motorolasolutions.ASCII_SDK.MetaData;
import com.motorolasolutions.ASCII_SDK.Notification;
import com.motorolasolutions.ASCII_SDK.Notification_TriggerEvent;
import com.motorolasolutions.ASCII_SDK.RESPONSE_TYPE;
import com.motorolasolutions.ASCII_SDK.ResponseMsg;
import com.motorolasolutions.ASCII_SDK.Response_TagData;
import com.twelvemonkeys.lang.StringUtil;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;

import net.huansi.equipment.equipmentapp.activity.merge_goods.ScannerActivity;
import net.huansi.equipment.equipmentapp.activity.send_card.BindCardDetailActivity;
import net.huansi.equipment.equipmentapp.adapter.MaterialListAdapter;
import net.huansi.equipment.equipmentapp.constant.Constant;
import net.huansi.equipment.equipmentapp.entity.DetailDataShowItem;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.MainEvent;
import net.huansi.equipment.equipmentapp.entity.MaterialData;
import net.huansi.equipment.equipmentapp.entity.MaterialList;
import net.huansi.equipment.equipmentapp.entity.NumberHolder;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent;
import net.huansi.equipment.equipmentapp.event.MessageEvent;
import net.huansi.equipment.equipmentapp.gen.MaterialDataInSQLiteDao;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.sqlite_db.MaterialDataInSQLite;
import net.huansi.equipment.equipmentapp.util.DrawableCache;
import net.huansi.equipment.equipmentapp.util.GetSnUtils;
import net.huansi.equipment.equipmentapp.util.HexString;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RFD8500DeviceUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


import static android.R.attr.focusable;
import static android.R.attr.y;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.RFID_HAS_BIND;
import static net.huansi.equipment.equipmentapp.constant.Constant.DB_NAME;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.RECEIVE_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.TRIGGER_OPERATION_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.CONFIGURATIONS_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.METADATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.NOTIFICATION_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RECEIVED_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RESPONSE_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;
import static net.huansi.equipment.equipmentapp.util.SPHelper.getLocalData;

/**
 * Created by zhou.mi on 2018/1/11.
 */

public class CheckMainActivity extends BaseActivity implements MaterialListAdapter.CallBack  {
    @BindView(R.id.lvMaterialList)
    ListView lvMaterialList;
    @BindView(R.id.btnBindCardDetailStartStop) Button btnBindCardDetailStartStop;
    @BindView(R.id.bindCardDetailInventoryingLayout)
    LinearLayout bindCardDetailInventoryingLayout;
    @BindView(R.id.spArea) Spinner spArea;
    @BindView(R.id.spGallery) Spinner spGallery;
    @BindView(R.id.etShelf) EditText etShelf;
    @BindView(R.id.etRemark) EditText etRemark;
    @BindView(R.id.totalBolt) TextView totalBolt;
    @BindView(R.id.etTime) EditText etTime;
    private List<String> areaList=new ArrayList<>();
    private List<String> galleryList=new ArrayList<>();
    private ArrayAdapter<String> mAreaAdapter;
    private ArrayAdapter<String> mGalleryAdapter;
    private List<MaterialList> mList;
    private List<DetailDataShowItem> scanShowNONOList=new ArrayList<>();//未传未扫
    private List<DetailDataShowItem> scanShowNowPSList=new ArrayList<>();//本次扫描
    private List<String> scanShowCheckList=new ArrayList<>();//已检查到布匹列表
    private MaterialListAdapter mAdapter;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private LoadProgressDialog dialog;
    private PopupWindow pw=new PopupWindow();
    //private LineAdapter lineAdapter;
    private ScanNoNoAdapter scanNoNoAdapter;
    private ScanNowPsAdapter scanNowPsAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> rFIDInfoList;//正常扫到的所有条码集合
    private List<String> manualRFIDInfoList=new ArrayList<>();//标记手动提交的条码集合
    private boolean isInventorying;
    private HashMap<String,String> bindRFIDMap;//已经绑定的RFID
    private Map<String,String> showRFIDMap;//检查数组中是否存在扫到的RFID卡
    private MetaData metaData;
    private List<String> SNList=new ArrayList<>();
    private MaterialList materialLists;
    private List<String> FEPOList=new ArrayList<String>();
    private List<String> IsMatch=new ArrayList<String>();
    private List<String> UnMatchErpSn=new ArrayList<>();
            //List<String> testList = new ArrayList<String>();
    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        setToolBarTitle("RFID验货");
        SharedPreferences message = getSharedPreferences("message", MODE_PRIVATE);
        int area = message.getInt("Area", 1);
        int gallery = message.getInt("Gallery", 1);
        areaList.add("A");
        areaList.add("B");
        areaList.add("C");
        areaList.add("D");
        areaList.add("E");
        areaList.add("F");
        mAreaAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.string_item,R.id.text,areaList);
        spArea.setAdapter(mAreaAdapter);
        spArea.setSelection(area,true);
        for (int i=1;i<=11;i++){
            galleryList.add(Integer.toString(i));
        }
        mGalleryAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.string_item,R.id.text,galleryList);
        spGallery.setAdapter(mGalleryAdapter);
        spGallery.setSelection(gallery,true);
        rFIDInfoList=new ArrayList<>();
        mList=new ArrayList<>();
        showRFIDMap=new HashMap<>();
        mAdapter=new MaterialListAdapter(mList, getApplicationContext(), this);
        lvMaterialList.setAdapter(mAdapter);
        dialog=new LoadProgressDialog(this);
        bindRFIDMap= (HashMap<String, String>) getIntent().getSerializableExtra(RFID_HAS_BIND);
        if(bindRFIDMap==null) bindRFIDMap=new HashMap<>();
        OthersUtil.registerEvent(this);
        RFD8500DeviceUtils.setPowerLevel((short) 180);
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()).toString();
        if (etTime.getText().toString().isEmpty()){
            etTime.setText(data);
        }
//        String epCode="";
//        String trimCode = epCode.replaceAll("(00)+$", "");//截掉末尾补位的“00”
//        Log.e("TAG","trimCode="+trimCode);
  //      rFIDInfoList.add("A1710312990026");
//        rFIDInfoList.add("A1803091550002");
//        rFIDInfoList.add("A1805175620006");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.check_main_activity;
    }
    /**
     * 开始或者结束盘点
     */
    @OnClick(R.id.btnBindCardDetailStartStop)
    void startOrStopInventory(){
        isInventorying=!isInventorying;
        changedByScan();
    }
    /**
     * 根据扫描的状态改变控件的状态
     */
    private void changedByScan(){
        if(isInventorying){
            btnBindCardDetailStartStop.setText("结束扫描");
            btnBindCardDetailStartStop.setBackground(DrawableCache.getInstance().
                    getDrawable(R.drawable.btn_green_circle_selector,getApplicationContext()));
            bindCardDetailInventoryingLayout.setVisibility(View.VISIBLE);
            startInventory();
        }else {
            btnBindCardDetailStartStop.setText("开始扫描");
            btnBindCardDetailStartStop.setBackground(DrawableCache.getInstance().
                    getDrawable(R.drawable.btn_blue_circle_selector,getApplicationContext()));
            bindCardDetailInventoryingLayout.setVisibility(View.GONE);
            stopInventory();
        }
    }
    private void startInventory(){
        //进行盘点（一直在运行）
        Log.e("TAG","mi");
        Command_Inventory inventoryCommand = new Command_Inventory();
        RFD8500DeviceUtils.sendCommand(inventoryCommand);
    }
    /**
     * 停止盘点
     */
    private void stopInventory(){
        //进行盘点（一直在运行）
//        String cmdOut = ASCIIProcessor.getCommandString();
//        bluetoothService.write(cmdOut.getBytes());
        Log.e("TAG","im");
        RFD8500DeviceUtils.sendCommand(new Command_abort());
        OthersUtil.showChooseDialog(this, "条码获取完毕，是否显示详细?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSN();
            }
        });

    }

    private synchronized void getSN() {
        Log.e("TAG","jack="+rFIDInfoList);
        final String joiner = Joiner.on("/").join(rFIDInfoList);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CheckMainActivity.this, hsWebInfo)
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                        "spGetSNByBarcodeList",
                                        "Barcode=" + joiner,
                                        String.class.getName(),
                                        false,
                                        "helloWorld");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                , CheckMainActivity.this, dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "success1="+hsWebInfo.json);
                        String json = hsWebInfo.json;
                        if (json==null){
                            Log.e("TAG","JSON="+json);
                            OthersUtil.showTipsDialog(CheckMainActivity.this,"获取不到数据！！");
                            return;
                        }
                        GetSnUtils SnUtils = JSON.parseObject(json, GetSnUtils.class);
                        List<GetSnUtils.DATABean> data = SnUtils.getDATA();
                        SNList.clear();
                        for (int i=0;i<data.size();i++){
                            if (!data.get(i).getSN().toString().equalsIgnoreCase("")&&!SNList.contains(data.get(i).getSN().toString())){
                                SNList.add(data.get(i).getSN().toString());
                            }
                        }
                        getData();
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
//
                    }

                });

    }

    private synchronized void getData() {
        String s = "";
        for (int i = 0; i < SNList.size(); i++) {
            s = s + SNList.get(i) + "/";
        }
        String needUp = null;
        if (s.length() != 0) {
            needUp = s.substring(0, s.length() - 1);
        }
        Log.e("TAG", "needUp=" + needUp);
        if (needUp==null||needUp.toString().equalsIgnoreCase("")) {
            OthersUtil.showTipsDialog(this, "资料未导入或查询不到数据!!");
            return;
        } else {
            final String finalNeedUp = needUp;
            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CheckMainActivity.this, hsWebInfo)
                    .map(new Func1<HsWebInfo, HsWebInfo>() {
                        @Override
                        public HsWebInfo call(HsWebInfo s) {
                            HsWebInfo hsWebInfo = NewRxjavaWebUtils.getJsonData(getApplicationContext(), "spGetOther_MaterialStockInBySNList",
                                    "SN=" + finalNeedUp, MaterialData.class.getName(), true, "fucking");
                            if (!hsWebInfo.success) return hsWebInfo;
                            return hsWebInfo;
                        }
                    }), getApplicationContext(), dialog, new WebListener() {
                @Override
                public void success(HsWebInfo hsWebInfo) {
                    Log.e("TAG", "s=" + hsWebInfo.json);
                    List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                    List<MaterialDataInSQLite> materialDataInSQLiteList = new ArrayList<>();
                    Log.e("TAG", "en=" + entities.size());
                    totalBolt.setText("总匹数" + entities.size());
                    for (WsEntity entity : entities) {
                        MaterialData materialData = (MaterialData) entity;
                        MaterialDataInSQLite materialDataInSQLite = new MaterialDataInSQLite();
                        materialDataInSQLite.setCustomerName(materialData.CUSTOMERNAME);
                        materialDataInSQLite.setBarcode(materialData.BARCODE);
                        materialDataInSQLite.setSn(materialData.SN);
                        materialDataInSQLite.setMaterialCode(materialData.MATERIALCODE);
                        materialDataInSQLite.setMaterialName(materialData.MATERIALNAME);
                        materialDataInSQLite.setMaterialID(materialData.MATERIALID);
                        materialDataInSQLite.setColorCode(materialData.COLORCODE);
                        materialDataInSQLite.setColorName(materialData.COLORNAME);
                        materialDataInSQLite.setQuantity(materialData.QUANTITY);
                        materialDataInSQLite.setQuantityPs(materialData.QUANTITYPS);
                        materialDataInSQLite.setQuantityPsRaw(materialData.QUANTITYPSRAW);
                        materialDataInSQLite.setVatno(materialData.VATNO);
                        materialDataInSQLite.setPno(materialData.PNO);
                        materialDataInSQLite.setCheckStatus(materialData.CHECKSTATUS);
                        materialDataInSQLite.setBarcodeTtlNum(materialData.BARCODETTLNUM);
                        materialDataInSQLite.setCheckedttNum(materialData.CHECKEDTTLNUM);
                        materialDataInSQLite.setUnCheckedNum(materialData.UNCHECKEDNUM);
                        materialDataInSQLite.setExemption(materialData.EXEMPTION);
                        materialDataInSQLite.setIsMatchErpPo(materialData.ISMATCHERPPO);
                        materialDataInSQLite.setFepoQuantity(materialData.FEPOQUANTITY);
                        materialDataInSQLiteList.add(materialDataInSQLite);
                    }
                    Log.e("TAG", "ma=" + materialDataInSQLiteList);
                    MaterialDataInSQLiteDao materialDataInSQLiteDao = OthersUtil.getGreenDaoSession(getApplicationContext()).getMaterialDataInSQLiteDao();
                    materialDataInSQLiteDao.deleteAll();
                    materialDataInSQLiteDao.detachAll();
                    materialDataInSQLiteDao.insertOrReplaceInTx(materialDataInSQLiteList);
                    Log.e("TAG", "materialDataInSQLiteDaoList=" + materialDataInSQLiteDao.queryBuilder().list().size());
                    RefreshmList();
                }

                @Override
                public void error(HsWebInfo hsWebInfo) {
                    Log.e("TAG", "ss=" + hsWebInfo.json);
                }
            });
        }


    }
    //封装刷新主页面方法
    private synchronized void RefreshmList() {
        mList.clear();
        Log.e("TAG","SNLIST="+SNList.toString());
        for (String SnItem:SNList) {
            materialLists = new MaterialList();
            //同SN下所有条码集合
            List<MaterialDataInSQLite> MaterialItem1 = OthersUtil.getGreenDaoSession(getApplicationContext()).getMaterialDataInSQLiteDao().queryBuilder().where(MaterialDataInSQLiteDao.Properties.Sn.eq(SnItem)).list();
            materialLists.PNOTOTAL = MaterialItem1.size();
            Log.e("TAG", "MaterialItem1=" + MaterialItem1.size());

            IsMatch.clear();
            FEPOList.clear();
            for (MaterialDataInSQLite item : MaterialItem1) {
                materialLists.EXEMPTION=item.getExemption();
                materialLists.SN = item.getSn();//
                IsMatch.add(item.getIsMatchErpPo());
                if (!FEPOList.contains(item.getFepoQuantity())) {
                    FEPOList.add(item.getFepoQuantity());
                }
                materialLists.QUANTITYTOTAL = materialLists.QUANTITYTOTAL + Float.parseFloat(item.getQuantityPs());
            }

            //已传条码集合
            List<MaterialDataInSQLite> MaterialItem3 = OthersUtil.getGreenDaoSession(getApplicationContext()).getMaterialDataInSQLiteDao().queryBuilder()
                    .where(MaterialDataInSQLiteDao.Properties.Sn.eq(SnItem),MaterialDataInSQLiteDao.Properties.CheckStatus.eq("Checked")).build().forCurrentThread().listLazy();
            Log.e("TAG", "MaterialItem3=" + MaterialItem3.size());
            materialLists.PNOCHECKED = MaterialItem3.size();
            for (MaterialDataInSQLite item : MaterialItem3) {
                materialLists.QUANTITYCHECKED = materialLists.QUANTITYCHECKED + Float.parseFloat(item.getQuantityPs());
            }

            //本次扫描的数据集合  NowScanList
            List<String> repairBackList =new ArrayList<>();
            MaterialDataInSQLiteDao materialDataInSQLiteDao = OthersUtil.getGreenDaoSession(getApplicationContext()).getMaterialDataInSQLiteDao();
            for (String item:rFIDInfoList){
                MaterialDataInSQLite unique = materialDataInSQLiteDao.queryBuilder().where(MaterialDataInSQLiteDao.Properties.Barcode.eq(item)).unique();
                if (unique==null){
                   repairBackList.add(item);
                }else {
                    if (unique.getCheckStatus().equalsIgnoreCase("")){
                        unique.setCheckStatus("Scan");
                        materialDataInSQLiteDao.update(unique);
                    }
                }

            }
            rFIDInfoList.removeAll(repairBackList);
            QueryBuilder<MaterialDataInSQLite> qb =  materialDataInSQLiteDao.queryBuilder();
            qb.where(MaterialDataInSQLiteDao.Properties.Sn.eq(SnItem));
            qb.where(MaterialDataInSQLiteDao.Properties.CheckStatus.eq("Scan"));
            List<MaterialDataInSQLite> NowScanList = qb.list();
            Log.e("TAG", "NowScanData=" + NowScanList);
            for (MaterialDataInSQLite item : NowScanList) {
                materialLists.QUANTITYSCAN = materialLists.QUANTITYSCAN + Float.parseFloat(item.getQuantityPs());//本次扫描码长
            }

            //获取未传待扫数据集合 nonoList
            QueryBuilder<MaterialDataInSQLite> nonoQb =  materialDataInSQLiteDao.queryBuilder();
                    nonoQb.where(MaterialDataInSQLiteDao.Properties.Sn.eq(SnItem));
                    nonoQb.where(MaterialDataInSQLiteDao.Properties.CheckStatus.eq(""));
            List<MaterialDataInSQLite> nonoList = nonoQb.list();


            if (IsMatch.contains("N")) {
                materialLists.ISMATCHERPPO = "否";
            } else {
                materialLists.ISMATCHERPPO = "是";
            }
            materialLists.FEPOQUANTITY = FEPOList.toString();
            materialLists.PNOSCAN = NowScanList.size();
            materialLists.NONONUM = nonoList.size();//未传待扫
            if (materialLists.PNOSCAN!=0){
                mList.add(materialLists);
            }
        }

        mAdapter.notifyDataSetChanged();
        for (int i=0;i<mList.size();i++){
            if (mList.get(i).ISMATCHERPPO.equalsIgnoreCase("否")){
                if (!UnMatchErpSn.contains(mList.get(i).SN)){
                    UnMatchErpSn.add(mList.get(i).SN);
                }
            }
        }
    }


    /**
     * 来自设备的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fromBluetooth(MainEvent event){
        if(event.aClass!=BindCardDetailActivity.class) return;
        switch (event.index){
            case RECEIVE_DATA_INDEX:
                receiveData((Response_TagData) event.object);
                break;
            case TRIGGER_OPERATION_INDEX:
                pressOrReleaseTrigger((Notification) event.object);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageEvent event){
        Log.e("TAG","det数据="+event.message);
        etShelf.getText().clear();
        etShelf.setText(event.message);
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
                    RFD8500DeviceUtils.dataReceivedFromBluetooth(event.data, metaData,BindCardDetailActivity.class);
                    break;
                case CONFIGURATIONS_INDEX:
                    break;
                case METADATA_INDEX:
                    if(event.aClass!=BindCardDetailActivity.class) return;
                    metaData = (MetaData) event.msg;
                    break;
                case RESPONSE_DATA_INDEX:
                    if(event.aClass!=BindCardDetailActivity.class) return;
                    responseDataParsedFromGenericReader((ResponseMsg) event.msg);
                    break;
                case NOTIFICATION_INDEX:
                    if(event.aClass!=BindCardDetailActivity.class) return;
                    notificationFromGenericReader((Notification) event.msg);
                    break;
            }
        }
    }

    /**
     * 获得RFID数据
     * @param data
     */
    private synchronized void receiveData(Response_TagData data){
        String epCode=data.EPCId+"";
        Log.e("TAG","epCode="+epCode);
        OthersUtil.dismissLoadDialog(dialog);
        if(showRFIDMap.get(epCode)==null&&bindRFIDMap.get(epCode)==null){
            String firstCode = epCode.substring(0, 1);
            if (epCode.length()==24&&(firstCode.equalsIgnoreCase("1")||firstCode.equalsIgnoreCase("2"))){
                //越南
                String substring = epCode.substring(0, 15);
                if (!isMessyCode(substring)){
                    rFIDInfoList.add(substring);
                }
            }else {//苏州，台湾
                String barCode = HexString.hexStringToString(epCode).trim();
                String replace1 = barCode.replace(" ", "");
                String replace = replace1.replace("@", "");
                Log.e("TAG","barCode="+barCode);
                Log.e("TAG","replace="+replace);
                boolean b = !isMessyCode(replace);
                Log.e("TAG","boolean="+b);
                if (!isMessyCode(replace)){
                    rFIDInfoList.add(replace);
                }
            }
            
            btnBindCardDetailStartStop.setText("结束扫描"+"("+rFIDInfoList.size()+")");
            showRFIDMap.put(epCode,epCode);
        }
    }

    private boolean isVnMessyCode(String source) {
        boolean flag = false;
        String[] arr = { "\"", "?", "\u0001", "'", "&" ,"�","@"," " };
        for (int i=0;i<arr.length;i++){
            if (source.contains(arr[i])){
                flag = true;
            }
        }
        return flag;
    }
    //判断是否为乱码，如果是返回true
    private boolean isMessyCode(String source) {
        boolean flag = false;
        char ws[] = new char[] { '"', '?', ' ', '\'', '&' };
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            for (int j = 0; j < ws.length; j++) {
                char v = ws[j];
                if (c == v) {
                    flag = true;
                }
            }
            if ((int) c == 0xfffd) {
                flag = true;
            }
        }
        return flag;
    }
    @OnClick(R.id.IsMatchErp)
    void IsMatchErp(){
        OthersUtil.showTipsDialog(this,"不匹配ERP的SN:"+UnMatchErpSn.toString());
    }
    @OnClick(R.id.btnUpDate)
     synchronized void upDateData() {
            final List<String> IsMatchErp=new ArrayList<>();

            for (int i=0;i<mList.size();i++){
                IsMatchErp.add(mList.get(i).ISMATCHERPPO);
            }
            if (IsMatchErp.contains("否")){
                OthersUtil.showTipsDialog(this,"不匹配ERP无法提交,请联系业务部门");
            }else {
                if (rFIDInfoList.isEmpty()) {
                   Log.e("TAG","空");
                    return;
                }
                final String join = Joiner.on("/").join(rFIDInfoList);
                Log.e("TAG","list="+rFIDInfoList);
                Log.e("TAG","joiner="+join);
                final String submitter = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();

                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CheckMainActivity.this, hsWebInfo)
                                .map(new Func1<HsWebInfo, HsWebInfo>() {
                                    @Override
                                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                                        Log.e("TAG", "upupDate");
                                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                                "spSubmitOther_MaterialStockInByBarcode",
                                                "Barcode=" + join +
                                                        ",Submitter=" + submitter +
                                                        ",isChecked=" + null +
                                                        ",Area=" + spArea.getSelectedItem().toString() +
                                                        ",Location=" + spGallery.getSelectedItem().toString() +
                                                        ",Shelf=" + etShelf.getText().toString() +
                                                        ",GoodsRemarks=" + etRemark.getText().toString() +
                                                        ",Remarks=" + "快速提交" + ",CheckDate=" + etTime.getText().toString(),
                                                String.class.getName(),
                                                false,
                                                "helloWorld");
                                    }
                                })
                        , CheckMainActivity.this, dialog, new WebListener() {
                            @Override
                            public void success(HsWebInfo hsWebInfo) {
                                Log.e("TAG", "success1=" + hsWebInfo.json);
                                IsMatchErp.clear();
                                OthersUtil.showTipsDialog(CheckMainActivity.this,"自动提交成功！");
                                if (!manualRFIDInfoList.isEmpty()){
                                    Log.e("TAG","执行");
                                    modifyRemark();
                                }
                                scanShowNONOList.clear();
                                scanShowNowPSList.clear();
                                mList.clear();
                                SNList.clear();
                                rFIDInfoList.clear();
                                manualRFIDInfoList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void error(HsWebInfo hsWebInfo) {
                                Log.e("TAG", hsWebInfo.error.error);

                            }
                        });

            }

    }


   private void modifyRemark(){
        //  修改手动提交的一些布匹备注
        final String submitter = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
        final String joiner = Joiner.on("/").join(manualRFIDInfoList);
            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CheckMainActivity.this, hsWebInfo)
                            .map(new Func1<HsWebInfo, HsWebInfo>() {
                                @Override
                                public HsWebInfo call(HsWebInfo hsWebInfo) {
                                    Log.e("TAG", "handupDate");
                                    return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                            "spSubmitOther_MaterialStockInByBarcode",
                                            "Barcode=" + joiner +
                                                    ",Submitter=" + submitter +
                                                    ",isChecked=" + null +
                                                    ",Area=" + spArea.getSelectedItem().toString() +
                                                    ",Location=" + spGallery.getSelectedItem().toString() +
                                                    ",Shelf=" + etShelf.getText().toString() +
                                                    ",GoodsRemarks=" + etRemark.getText().toString() +
                                                    ",Remarks=" + "手动提交" + ",CheckDate=" + etTime.getText().toString(),
                                            String.class.getName(),
                                            false,
                                            "helloWWWorld");
                                }
                            })
                    , CheckMainActivity.this, dialog, new WebListener() {
                        @Override
                        public void success(HsWebInfo hsWebInfo) {
                            Log.e("TAG", "success1=" + hsWebInfo.json);
                            OthersUtil.showTipsDialog(CheckMainActivity.this,"手动提交成功！");
                        }

                        @Override
                        public void error(HsWebInfo hsWebInfo) {
                            Log.e("TAG", hsWebInfo.error.error);
                        }
                    });


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
                EventBus.getDefault().post(new MainEvent(BindCardDetailActivity.class,response_tagData,RECEIVE_DATA_INDEX));
            }
        }
    }
    private void notificationFromGenericReader(Notification notification) {
        EventBus.getDefault().post(new MainEvent(BindCardDetailActivity.class,notification,TRIGGER_OPERATION_INDEX));
    }


    @OnClick(R.id.tvTime)
    void selectTime(){

        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(CheckMainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.e("TAG","month="+month);
                String sMonth = null;
                if (month < 9) {
                    month=month+1;
                    sMonth = "0" + month;
                }else {
                    month=month+1;
                    sMonth=""+month;
                }
                String sDay = null;
                if (dayOfMonth < 10) {
                    sDay = "0" + dayOfMonth;
                }else {
                    sDay=""+dayOfMonth;
                }
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                String sHour=null;
                if (hour<10){
                    sHour="0"+hour;
                }else {
                    sHour=""+hour;
                }
                String sMinute=null;
                if (minute<10){
                    sMinute="0"+minute;
                }else {
                    sMinute=""+minute;
                }
                etTime.setText(year + "-" + sMonth + "-" +sDay +" "+sHour+":"+sMinute);

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    @Override
    public void click(View view) {
        ListView mListView = null;

        switch ((Integer) view.getTag()){
            case 1:
                int checkedPosition = (int) view.getTag(R.id.tag_checked);
                final String SN = mList.get((Integer) view.getTag(R.id.tag_checked)).SN;
                mListView = initCheckedListView(SN);
                break;
            case 2:
                int scanPosition = (int) view.getTag(R.id.tag_scan);
                mListView=initNowPSListView(scanPosition);
                break;
            case 3:
                int nonoPosition = (int) view.getTag(R.id.tag_nono);
                mListView=initNoNoListView(nonoPosition);
                break;
        }
        pw = new PopupWindow(mListView,this.getWindowManager().getDefaultDisplay().getWidth()-40, 1000);
        pw.setFocusable(true);
        //设置点击pop外部可以被关闭
        pw.setOutsideTouchable(true);
        //设置一个pop的背景
        pw.setBackgroundDrawable(new BitmapDrawable());
        // 设置popupwindow显示位置
        pw.showAtLocation(view, 1, 5, -5);
    }

    private ListView initNoNoListView(int position) {
        ListView mListView = new ListView(this);
        mListView.setBackgroundResource(R.drawable.shadow);
        mListView.setVerticalScrollBarEnabled(false);
        Log.e("TAG","TAG="+position);
        QueryBuilder<MaterialDataInSQLite> qb = OthersUtil.getGreenDaoSession(getApplicationContext()).getMaterialDataInSQLiteDao().queryBuilder();
        qb.where(MaterialDataInSQLiteDao.Properties.Sn.eq(mList.get(position).SN));
        qb.where(MaterialDataInSQLiteDao.Properties.CheckStatus.eq(""));
        List<MaterialDataInSQLite> needShowList = qb.list();
        for (MaterialDataInSQLite item:needShowList){
            DetailDataShowItem showItem=new DetailDataShowItem();
            showItem.BARCODE=item.getBarcode();
            showItem.COLORNAME=item.getColorName();
            showItem.MATERIALCODE=item.getMaterialCode();
            showItem.PNO=item.getPno();
            showItem.VATNO=item.getVatno();
            showItem.QUANTITYPS=item.getQuantityPs();
            scanShowNONOList.add(showItem);
        }
        scanNoNoAdapter=new ScanNoNoAdapter();
        mListView.setAdapter(scanNoNoAdapter);
        return mListView;
    }

    private ListView initNowPSListView(int position) {
        ListView mListView = new ListView(this);
        mListView.setBackgroundResource(R.drawable.shadow);
        mListView.setVerticalScrollBarEnabled(false);
        Log.e("TAG","TAG="+position);
        QueryBuilder<MaterialDataInSQLite> qb = OthersUtil.getGreenDaoSession(getApplicationContext()).getMaterialDataInSQLiteDao().queryBuilder();
        qb.where(MaterialDataInSQLiteDao.Properties.Sn.eq(mList.get(position).SN));
        qb.where(MaterialDataInSQLiteDao.Properties.CheckStatus.eq("Scan"));
        List<MaterialDataInSQLite> needShowList = qb.list();
        Log.e("TAG", "NowScanData=" + needShowList);
        for (MaterialDataInSQLite item:needShowList){
            DetailDataShowItem showItem=new DetailDataShowItem();
            showItem.SN=item.getSn();
            showItem.BARCODE=item.getBarcode();
            showItem.COLORNAME=item.getColorName();
            showItem.MATERIALCODE=item.getMaterialCode();
            showItem.PNO=item.getPno();
            showItem.VATNO=item.getVatno();
            showItem.QUANTITYPS=item.getQuantityPs();
            scanShowNowPSList.add(showItem);
        }
        scanNowPsAdapter=new ScanNowPsAdapter();
        mListView.setAdapter(scanNowPsAdapter);
        return mListView;
    }

    private ListView initCheckedListView(final String sn) {
        final ListView mListView = new ListView(this);
        mListView.setBackgroundResource(R.drawable.shadow);
        mListView.setVerticalScrollBarEnabled(false);
        arrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item_item,R.id.white_text,scanShowCheckList);
        QueryBuilder<MaterialDataInSQLite> qb = OthersUtil.getGreenDaoSession(getApplicationContext()).getMaterialDataInSQLiteDao().queryBuilder();
        qb.where(MaterialDataInSQLiteDao.Properties.Sn.eq(sn));
        qb.where(MaterialDataInSQLiteDao.Properties.CheckStatus.eq("Checked"));
        List<MaterialDataInSQLite> needShowList = qb.list();
        Log.e("TAG", "NowScanData=" + needShowList);
        scanShowCheckList.clear();
        for (MaterialDataInSQLite item:needShowList){
            Log.e("TAG","thanku");
            scanShowCheckList.add("物料:"+item.getMaterialCode()+"("+item.getColorName()+")"+"   "+"缸号:"+item.getVatno()+"   "+"匹号:"+item.getPno()
                    +"   "+"码长:"+item.getQuantityPs()+"   "+"{"+item.getBarcode()+"}");
        }

        mListView.setAdapter(arrayAdapter);
        return mListView;
    }

    private class ScanNoNoAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return scanShowNONOList.size();
        }

        @Override
        public Object getItem(int position) {
            return scanShowNONOList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            NumberHolder mHolder=null;
            if (convertView==null){
                convertView=View.inflate(CheckMainActivity.this,R.layout.listview_scan_add_item,null);
                mHolder=new NumberHolder();
                mHolder.tvUnCheckList= (TextView) convertView.findViewById(R.id.tv_listView_item_NoNoScan);
                mHolder.ibDelete=(Button) convertView.findViewById(R.id.ib_listView_item_Add);
                convertView.setTag(mHolder);
            }else {
                mHolder= (NumberHolder) convertView.getTag();
            }
            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    scanShowNONOList.clear();
                    Log.e("TAG","scanShowNONOList="+scanShowNONOList.size());
                }
            });
            mHolder.tvUnCheckList.setText("物料:"+scanShowNONOList.get(position).MATERIALCODE+"("+scanShowNONOList.get(position).COLORNAME+")"+"   "+"缸号:"+scanShowNONOList.get(position).VATNO+"   "+"匹号:"+scanShowNONOList.get(position).PNO
                    +"   "+"码长:"+scanShowNONOList.get(position).QUANTITYPS+"   "+"{"+scanShowNONOList.get(position).BARCODE+"}");
            mHolder.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!manualRFIDInfoList.contains(scanShowNONOList.get(position).BARCODE)){
                        manualRFIDInfoList.add(scanShowNONOList.get(position).BARCODE);
                    }
                    rFIDInfoList.add(scanShowNONOList.get(position).BARCODE);
                    scanShowNONOList.remove(position);
                    scanNoNoAdapter.notifyDataSetChanged();
                    getSN();

//                    MaterialDataInSQLiteDao materialDataInSQLiteDao = OthersUtil.getGreenDaoSession(getApplicationContext()).getMaterialDataInSQLiteDao();
//                    MaterialDataInSQLite unique = materialDataInSQLiteDao.queryBuilder().
//                            where(MaterialDataInSQLiteDao.Properties.Barcode.eq(scanShowNONOList.get(position).BARCODE)).unique();
//                    unique.setCheckStatus("Scan");
//                    materialDataInSQLiteDao.update(unique);
//                    RefreshmList();
                }
            });
            return convertView;
        }
    }

    private class ScanNowPsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return scanShowNowPSList.size();
        }

        @Override
        public Object getItem(int position) {
            return scanShowNowPSList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            NumberHolder mHolder=null;
            if (convertView==null){
                convertView=View.inflate(CheckMainActivity.this,R.layout.listview_scan_remove_item,null);
                mHolder=new NumberHolder();
                mHolder.tvUnCheckList= (TextView) convertView.findViewById(R.id.tv_listView_item_NowScan);
                mHolder.ibDelete=(Button) convertView.findViewById(R.id.ib_listView_item_Remove);
                convertView.setTag(mHolder);
            }else {
                mHolder= (NumberHolder) convertView.getTag();
            }
            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    scanShowNowPSList.clear();
                    Log.e("TAG","scanShowNowPSList="+scanShowNowPSList.size());
                }
            });
            mHolder.tvUnCheckList.setText("物料:"+scanShowNowPSList.get(position).MATERIALCODE+"("+scanShowNowPSList.get(position).COLORNAME+")"+"   "+"缸号:"+scanShowNowPSList.get(position).VATNO+"   "+"匹号:"+scanShowNowPSList.get(position).PNO
                    +"   "+"码长:"+scanShowNowPSList.get(position).QUANTITYPS+"   "+"{"+scanShowNowPSList.get(position).BARCODE+"}");
            mHolder.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (manualRFIDInfoList.contains(scanShowNowPSList.get(position).BARCODE)){
                        manualRFIDInfoList.remove(scanShowNowPSList.get(position).BARCODE);
                    }
                    rFIDInfoList.remove(scanShowNowPSList.get(position).BARCODE);
                    scanShowNowPSList.remove(position);
                    scanNowPsAdapter.notifyDataSetChanged();
                    getSN();

//                    MaterialDataInSQLiteDao materialDataInSQLiteDao = OthersUtil.getGreenDaoSession(getApplicationContext()).getMaterialDataInSQLiteDao();
//                    MaterialDataInSQLite unique = materialDataInSQLiteDao.queryBuilder().
//                            where(MaterialDataInSQLiteDao.Properties.Barcode.eq(scanShowNONOList.get(position).BARCODE)).unique();
//                    unique.setCheckStatus("");
//                    materialDataInSQLiteDao.update(unique);
                    //RefreshmList();
                }
            });
            return convertView;
        }
    }
    @Override
    protected void onDestroy() {
        rFIDInfoList.clear();
        UnMatchErpSn.clear();
        SNList.clear();
        mList.clear();
        SharedPreferences message = getSharedPreferences("message", MODE_PRIVATE);
        SharedPreferences.Editor editor = message.edit();
        editor.putInt("Area",spArea.getSelectedItemPosition());
        editor.putInt("Gallery",spGallery.getSelectedItemPosition());
        Log.e("TAG","位置:"+spArea.getSelectedItemPosition()+spGallery.getSelectedItemPosition());
        editor.commit();
        super.onDestroy();
    }
}
