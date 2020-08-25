package net.huansi.equipment.equipmentapp.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.motorolasolutions.ASCII_SDK.COMMAND_TYPE;
import com.motorolasolutions.ASCII_SDK.CONFIG_TYPE;
import com.motorolasolutions.ASCII_SDK.Command_SetRegulatory;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.adapter.DeviceSettingAdapter;
import net.huansi.equipment.equipmentapp.entity.ReaderDevice;
import net.huansi.equipment.equipmentapp.event.DialogEvent;
import net.huansi.equipment.equipmentapp.util.EPData;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RFD8500DeviceUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

import static net.huansi.equipment.equipmentapp.util.SPHelper.INVENTORY_POWER_LEVEL;

/**
 * Created by 单中年 on 2017/2/27.
 */

public class DeviceSettingActivity extends BaseActivity   {
    @BindView(R.id.lvBlueSetting) ListView lvBlueSetting;
    @BindView(R.id.spBlueSettingStyle)Spinner spBlueSettingStyle;//盘点的方式

    private ArrayAdapter<String> mStyleAdapter;
    private List<String> mStyleList;

    private DeviceSettingAdapter mAdapter;

    private ReaderDevice chooseDevice;
    private boolean isCreate=true;

    private LoadProgressDialog dialog;
    public static int ScannerPosition;


    @Override
    protected int getLayoutId() {
        return R.layout.device_setting_activity;
    }

    @Override
    public void init() {
        setToolBarTitle("设备连接设置");
        mStyleList=new ArrayList<>();
        mStyleList.add("全部盘点");
        mStyleList.add("单个盘点");
        mStyleAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mStyleList);
        spBlueSettingStyle.setAdapter(mStyleAdapter);
        mAdapter=new DeviceSettingAdapter(EPData.getActiveDeceiveList(),getApplicationContext());
        lvBlueSetting.setAdapter(mAdapter);
        updateData();
        OthersUtil.registerEvent(this);
        dialog=new LoadProgressDialog(this);
        showInventoryStyle();

    }



    /**
     * 更新数据
     */
    private void updateData(){
        for (int i = 0; i < EPData.getActiveDeceiveList().size(); i++) {
            ReaderDevice device=EPData.getActiveDeceiveList().get(i);
            if(EPData.mConnectedDevice==null) {
                device.setConnected(false);
                continue;
            }
            if(device.getAddress().equals(EPData.mConnectedDevice.getAddress()))
                device.setConnected(true);
            else
                device.setConnected(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 显示盘点方式
     */
    private void showInventoryStyle(){
        float savePowerLevel=(Float) (SPHelper.getLocalData(getApplicationContext(),
                                        INVENTORY_POWER_LEVEL,Float.class.getName(),300F));
        mStyleAdapter.notifyDataSetChanged();
       if(savePowerLevel==300){
           spBlueSettingStyle.setSelection(0, true);
       }else {
           spBlueSettingStyle.setSelection(1, true);
       }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dismissDialog(DialogEvent event){
        OthersUtil.dismissLoadDialog(dialog);
        EPData.mConnectedDevice=chooseDevice;
        mAdapter.notifyDataSetChanged();
//        //设置trigger按钮
//        if(EPData.mConnectedDevice!=null){
//            RFD8500DeviceUtils.triggerSetting();
//        }
        Command_SetRegulatory applyCommand = new Command_SetRegulatory();
        //Set the param values
        applyCommand.setregion("ALB");
        applyCommand.sethoppingon(true);
        applyCommand.setenabledchannels("865700,866300,866900,867500");
        //Send the regulatory command

        RFD8500DeviceUtils.sendCommand(applyCommand);
        RFD8500DeviceUtils.sendCommand(COMMAND_TYPE.COMMAND_SETREGULATORY, CONFIG_TYPE.CURRENT);
        RFD8500DeviceUtils.setVoice(0);
        RFD8500DeviceUtils.defaultSetting();
    }

    /**
     * 选择设备
     */
    @OnItemClick(R.id.lvBlueSetting)
    void chooseDevice(int position){
        connectToScanner(position);
        int lastChoosePosition=-1;
        if(EPData.mConnectedDevice!=null){
            for(int i=0;i<EPData.getActiveDeceiveList().size();i++){
                ReaderDevice device=EPData.getActiveDeceiveList().get(i);
                if(device.getAddress().equals(EPData.mConnectedDevice.getAddress())){
                    lastChoosePosition=i;
                }
            }
        }
        OthersUtil.showLoadDialog(dialog);

        //两个位置一样，说明要取消这个连接
        if(lastChoosePosition==position){
            EPData.getActiveDeceiveList().get(lastChoosePosition).setConnected(false);
            RFD8500DeviceUtils.disconnectedReader(EPData.getActiveDeceiveList().get(lastChoosePosition).getBluetoothDevice());
            chooseDevice=null;
        }else {
            ReaderDevice chooseDevice=EPData.getActiveDeceiveList().get(position);
            chooseDevice.setConnected(true);
            this.chooseDevice=chooseDevice;
            EPData.getActiveDeceiveList().set(position,chooseDevice);
            //连接设备
            RFD8500DeviceUtils.connectToBluetoothDevice(chooseDevice.getBluetoothDevice());
            if(lastChoosePosition!=-1) {
                ReaderDevice lastChooseDevice=EPData.getActiveDeceiveList().get(lastChoosePosition);
                lastChooseDevice.setConnected(false);
                EPData.getActiveDeceiveList().set(lastChoosePosition,lastChooseDevice);
            }
        }
    }

    private void connectToScanner(int position) {
        ScannerPosition=position;
        Log.d("TAG","position="+position);
//        new MyAsyncTask(mScannerInfoList.get(position).getScannerID()).execute();
    }

    /**
     * 根据距离获取信号强度
     */
    @OnItemSelected(R.id.spBlueSettingStyle)
    void getPowerLevel(int position) {
        if(isCreate){
            isCreate=false;
            return;
        }
        try {
            //0.2m 50
            //0.5m 100
            //1m 200
            //2.3m 250
            //4m 270
            //4.5m 300
            String style=mStyleList.get(position).trim();
            float powerLevel=300F;
            switch (style){
                case "单个盘点":
                    powerLevel=80F;
                    break;
                case "全部盘点":
                    powerLevel=300F;
                    break;
            }
            SPHelper.saveLocalData(getApplicationContext(),INVENTORY_POWER_LEVEL,powerLevel,Float.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OthersUtil.unregisterEvent(this);
    }

}
