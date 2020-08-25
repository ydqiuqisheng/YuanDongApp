package net.huansi.equipment.equipmentapp.activity.send_card;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import net.huansi.equipment.equipmentapp.constant.Constant;
import net.huansi.equipment.equipmentapp.entity.MainEvent;
import net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent;
import net.huansi.equipment.equipmentapp.gen.EquipmentWithoutRFIDInSQLiteDao;
import net.huansi.equipment.equipmentapp.sqlite_db.EquipmentWithoutRFIDInSQLite;
import net.huansi.equipment.equipmentapp.util.DrawableCache;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RFD8500DeviceUtils;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.EQUIPMENT_INFO_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.POSITION_IN_ALL_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.POSITION_IN_SHOW_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.RETURN_DATA_KEY;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.RFID_HAS_BIND;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.RECEIVE_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryDetailActivityConstants.TRIGGER_OPERATION_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.METADATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.NOTIFICATION_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RECEIVED_DATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RESPONSE_DATA_INDEX;

/**
 * Created by shanz on 2017/3/5.
 */

public class BindMoreCardActivity extends BaseActivity{
    @BindView(R.id.lvBindCardDetail) ListView lvBindCardDetail;
    @BindView(R.id.btnBindCardDetailStartStop) Button btnBindCardDetailStartStop;
    @BindView(R.id.bindCardDetailInventoryingLayout) LinearLayout bindCardDetailInventoryingLayout;
    @BindView(R.id.etBindCardDetail)EditText etBindCardDetail;//使用简易的RFID读卡器进行扫描
    private Map<String,String> showRFIDMap;//检查数组中是否存在扫到的RFID卡

    private ArrayAdapter<String> mAdapter;
    private List<String> rFIDInfoList;
    private boolean isInventorying;

//    private EquipmentWithoutRFIDInSQLite equipment;
    private LoadProgressDialog dialog;
    private Vibrator vibrator;

//    private HashMap<String,String> bindRFIDMap;//已经绑定的RFID


    private MetaData metaData;
    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        setToolBarTitle("扫描多绑");
        rFIDInfoList=new ArrayList<>();
        mAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,rFIDInfoList);
        lvBindCardDetail.setAdapter(mAdapter);
        showRFIDMap=new HashMap<>();
        initRFID();
        dialog=new LoadProgressDialog(this);
        OthersUtil.registerEvent(this);
        RFD8500DeviceUtils.setVoice(3);
        RFD8500DeviceUtils.setPowerLevel((short) 300);
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }
    
    private void initRFID(){
        String s=
        "2016040608701A0110400150"+
        " 2016040608701A0110400150"+
        " 2016040608701A0110400154"+
        " 2016040608701A0110400154"+
        " 2016040608701A011040038E"+
        " 2016040608701A011040038E"+
        " 2016040608701A01106002DC"+
        " 2016040608701A01106002DC"+
        " 2016040608701A0110600346"+
        " 2016040608701A0110600346"+
        " 2016040608701A01106003D1"+
        " 2016040608701A01106003D1"+
        " 2016040608701A01106003D7"+
        " 2016040608701A01106003D7"+
        " 2016040608701A01106003FC"+
        " 2016040608701A01106003FC"+
        " 2016040608701A011060049D"+
        " 2016040608701A011060049D"+
        " 2016040608701A01106004A4"+
        " 2016040608701A01106004A4"+
        " 2016040708701A011040005C"+
        " 2016040708701A011040005C"+
        " 2016040708701A0110400073"+
        " 2016040708701A0110400073"+
        " 2016040708701A01104000F2"+
        " 2016040708701A01104000F2"+
        " 2016040708701A01104000F6"+
        " 2016040708701A01104000F6"+
        " 2016040708701A0110400140"+
        " 2016040708701A0110400140"+
        " 2016040708701A0110400166"+
        " 2016040708701A0110400166"+
        " 2016040708701A011040017F"+
        " 2016040708701A011040017F"+
        " 2016040708701A01104001AD"+
        " 2016040708701A01104001AD"+
        " 2016040708701A011040026A"+
        " 2016040708701A011040026A"+
        " 2016040708701A0110400296"+
        " 2016040708701A0110400296"+
        " 2016040708701A01104002AD"+
        " 2016040708701A01104002AD"+
        " 2016040708701A01104002AE"+
        " 2016040708701A01104002AE"+
        " 2016040708701A0110400328"+
        " 2016040708701A0110400328"+
        " 2016040708701A01104005AE"+
        " 2016040708701A01104005AE"+
        " 2016040708701A01104005B6"+
        " 2016040708701A01104005B6"+
        " 2016040708701A01104005BC"+
        " 2016040708701A01104005BC"+
        " 2016040708701A01106000C3"+
        " 2016040708701A01106000C3"+
        " 2016040708701A01106001B8"+
        " 2016040708701A01106001B8"+
        " 2016040708701A01106001C6"+
        " 2016040708701A01106001C6"+
        " 2016040708701A0110600633"+
        " 2016040708701A0110600633"+
        " 2016040708701A0110600711"+
        " 2016040708701A0110600711"+
        " 2016040708701A0110600739"+
        " 2016040708701A0110600739"+
        " 2016040908701A01106006FB"+
        " 2016040908701A01106006FB"+
        " 2016040908701A011060094E"+
        " 2016040908701A011060094E"+
        " 2016040908701A0110600959"+
        " 2016040908701A0110600959"+
        " 2016040908701A01106009D8"+
        " 2016040908701A01106009D8"+
        " 2016040908701A01106009DA"+
        " 2016040908701A01106009DA"+
        " 2016041108701A01106000B8"+
        " 2016041108701A01106000B8"+
        " 2016041108701A0110600123"+
        " 2016041108701A0110600123"+
        " 2016041108701A0110600203"+
        " 2016041108701A0110600203"+
        " 2016041108701A0110600618"+
        " 2016041108701A0110600618"+
        " 2016041108701A0110600644"+
        " 2016041108701A0110600644";
        String arr[]=s.split(" ");
        for(String str:arr){
            showRFIDMap.put(str,str);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.bind_card_detail_activity;
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
            btnBindCardDetailStartStop.setText("结束");
            btnBindCardDetailStartStop.setBackground(DrawableCache.getInstance().
                    getDrawable(R.drawable.btn_green_circle_selector,getApplicationContext()));
            bindCardDetailInventoryingLayout.setVisibility(View.VISIBLE);
            startInventory();
        }else {
            btnBindCardDetailStartStop.setText("开始");
            btnBindCardDetailStartStop.setBackground(DrawableCache.getInstance().
                    getDrawable(R.drawable.btn_blue_circle_selector,getApplicationContext()));
            bindCardDetailInventoryingLayout.setVisibility(View.GONE);
            stopInventory();
        }
    }

    private void startInventory(){
        //进行盘点（一直在运行）
        Command_Inventory inventoryCommand = new Command_Inventory();
        RFD8500DeviceUtils.sendCommand(inventoryCommand);
//        String cmdOut = ASCIIProcessor.getCommandString(inventoryCommand);
//        bluetoothService.write(cmdOut.getBytes());
    }

    /**
     * 停止盘点
     */
    private void stopInventory(){
        //进行盘点（一直在运行）
//        String cmdOut = ASCIIProcessor.getCommandString();
//        bluetoothService.write(cmdOut.getBytes());
        RFD8500DeviceUtils.sendCommand(new Command_abort());
    }



    /**
     * 来自设备的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fromBluetooth(MainEvent event){
        if(event.aClass!=BindMoreCardActivity.class) return;
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
                    RFD8500DeviceUtils.dataReceivedFromBluetooth(event.data, metaData,BindMoreCardActivity.class);
                    break;
                case METADATA_INDEX:
                    if(event.aClass!=BindMoreCardActivity.class) return;
                    metaData = (MetaData) event.msg;
                    break;
                case RESPONSE_DATA_INDEX:
                    if(event.aClass!=BindMoreCardActivity.class) return;
                    responseDataParsedFromGenericReader((ResponseMsg) event.msg);
                    break;
                case NOTIFICATION_INDEX:
                    if(event.aClass!=BindMoreCardActivity.class) return;
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
        OthersUtil.dismissLoadDialog(dialog);
        if(showRFIDMap.get(epCode)!=null){
            OthersUtil.broadVoice(getApplicationContext(),R.raw.scaned);
            vibrator.vibrate(1000);
            rFIDInfoList.clear();
            rFIDInfoList.add(epCode);
            mAdapter.notifyDataSetChanged();
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
                EventBus.getDefault().post(new MainEvent(BindMoreCardActivity.class,response_tagData,RECEIVE_DATA_INDEX));
            }
        }
    }

    private void notificationFromGenericReader(Notification notification) {
        EventBus.getDefault().post(new MainEvent(BindMoreCardActivity.class,notification,TRIGGER_OPERATION_INDEX));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopInventory();
        RFD8500DeviceUtils.setVoice(0);
        OthersUtil.unregisterEvent(this);
    }

//    @Override
//    public synchronized boolean onKey(View v, int keyCode, KeyEvent event) {
//       switch (keyCode){
//           case KeyEvent.KEYCODE_ENTER:
//           case KeyEvent.KEYCODE_NUMPAD_ENTER:
//               if(event.getAction()==KeyEvent.ACTION_UP) {
//                   String rfid = etBindCardDetail.getText().toString().trim();
//                   if (showRFIDMap.get(rfid) == null) {
//                       rFIDInfoList.add(rfid);
//                       showRFIDMap.put(rfid, rfid);
//                   }
//                   mAdapter.notifyDataSetChanged();
//                   etBindCardDetail.getText().clear();
//               }
//               break;
//       }
//        return false;
//    }
}
