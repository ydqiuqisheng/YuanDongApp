package net.huansi.equipment.equipmentapp.util;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.motorolasolutions.ASCII_SDK.ASCIIProcessor;
import com.motorolasolutions.ASCII_SDK.COMMAND_TYPE;
import com.motorolasolutions.ASCII_SDK.CONFIG_TYPE;
import com.motorolasolutions.ASCII_SDK.Command;
import com.motorolasolutions.ASCII_SDK.Command_GetAttrInfo;
import com.motorolasolutions.ASCII_SDK.Command_GetCapabilities;
import com.motorolasolutions.ASCII_SDK.Command_GetSupportedLinkprofiles;
import com.motorolasolutions.ASCII_SDK.Command_GetVersion;
import com.motorolasolutions.ASCII_SDK.Command_ProtocolConfig;
import com.motorolasolutions.ASCII_SDK.Command_SetAntennaConfiguration;
import com.motorolasolutions.ASCII_SDK.Command_SetAttr;
import com.motorolasolutions.ASCII_SDK.Command_SetStartTrigger;
import com.motorolasolutions.ASCII_SDK.Command_SetStopTrigger;
import com.motorolasolutions.ASCII_SDK.ENUM_TRIGGER_ID;
import com.motorolasolutions.ASCII_SDK.IMsg;
import com.motorolasolutions.ASCII_SDK.MetaData;

import net.huansi.equipment.equipmentapp.constant.Constant;
import net.huansi.equipment.equipmentapp.entity.ReaderDevice;
import net.huansi.equipment.equipmentapp.event.BlueToothEvent;
import net.huansi.equipment.equipmentapp.event.CommandEvent;
import net.huansi.equipment.equipmentapp.event.DialogEvent;
import net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent;

import org.greenrobot.eventbus.EventBus;

import static android.bluetooth.BluetoothDevice.BOND_BONDED;
import static net.huansi.equipment.equipmentapp.event.BlueToothEvent.DEVICE_CONNECTED_INDEX;
import static net.huansi.equipment.equipmentapp.event.BlueToothEvent.DEVICE_UNCONNECTED_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.CONFIGURATIONS_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.METADATA_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.NOTIFICATION_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RESPONSE_DATA_INDEX;

/**
 * Created by shanz on 2017/4/26.
 */

public class RFD8500DeviceUtils {
    public static void disconnectedReader(BluetoothDevice device) {//蓝牙未连接
        //消失Dialog
        EventBus.getDefault().post(new DialogEvent());
        ReaderDevice readerDevice = new ReaderDevice(device, device.getName(), device.getAddress(), null, null, false);
        updateConnectedReaderDevice(readerDevice, false);
        EPData.mConnectedDevice = null;
    }

    public static void updateConnectedReaderDevice(ReaderDevice device, boolean isConnected) {
        if (device == null) return;
        int pos = EPData.getActiveDeceiveList().indexOf(EPData.mConnectedDevice);
        if (pos < 0) return;
        if (isConnected) {
            EPData.getActiveDeceiveList().get(pos).setConnected(true);
        } else {
            EPData.getActiveDeceiveList().get(pos).setConnected(false);
        }
    }

    /**
     * 连接蓝牙设备
     * @param device
     */
    public static void connectToBluetoothDevice(BluetoothDevice device) {
        switch (device.getBondState()){
            //已连接
            case BOND_BONDED:
                EventBus.getDefault().post(new BlueToothEvent(DEVICE_CONNECTED_INDEX,device));
                break;
            //连接失败
            default:
                EventBus.getDefault().post(new BlueToothEvent(DEVICE_UNCONNECTED_INDEX,device));
                break;
        }
    }

    public static void sendCommand(Command cmd) {
        String cmdOut = ASCIIProcessor.getCommandString(cmd);
        EventBus.getDefault().post(new CommandEvent(cmdOut));
    }

    public static  void sendCommand(COMMAND_TYPE command_type, CONFIG_TYPE config_type) {
        String cmdOut = ASCIIProcessor.getCommandConfig(command_type, config_type);
        EventBus.getDefault().post(new CommandEvent(cmdOut));
    }

    /**
     * 获取信息并且发送事件
     * @param msg
     */
    private static void getReplyDetails(IMsg msg,Class aClass) {
        if (msg == null) return;
        switch (msg.getMsgType()) {
            case COMMAND:
                EventBus.getDefault().post(new GenericReaderResponseEvent(CONFIGURATIONS_INDEX,msg,aClass));
                break;
            case METADATA:
                EventBus.getDefault().post(new GenericReaderResponseEvent(METADATA_INDEX,msg,aClass));
//                return (MetaData) msg;
            case RESPONSE_MSG:
                EventBus.getDefault().post(new GenericReaderResponseEvent(RESPONSE_DATA_INDEX,msg,aClass));
                break;
            case NOTIFICATION:
                EventBus.getDefault().post(new GenericReaderResponseEvent(NOTIFICATION_INDEX,msg,aClass));
//                if (genericListener != null)
//                    genericListener.notificationFromGenericReader((Notification) msg);
                break;
        }
    }
    public static void dataReceivedFromBluetooth(String data,MetaData metaData,Class aClass) {
        if (null == data || data.isEmpty())  return;
        try {
            IMsg msg = ASCIIProcessor.getReplyMsg(data,metaData);
            getReplyDetails(msg,aClass);
        } catch (Exception e) {}
    }

        /**
     * 设置信号等级
     * @param powerLevel
     */
    public static void setPowerLevel(short powerLevel) {
        Command_SetAntennaConfiguration antennaCommand = new Command_SetAntennaConfiguration();
        antennaCommand.setPower(powerLevel);
        sendCommand(antennaCommand);
        sendCommand(COMMAND_TYPE.COMMAND_SETANTENNACONFIGURATION, CONFIG_TYPE.CURRENT);
    }

    /**
     * 设置trigger按钮
     */
    public static void triggerSetting() {
        Command_SetStartTrigger startTrigger = new Command_SetStartTrigger();
        startTrigger.setNoRepeat(false);
        startTrigger.setRepeat(true);
        startTrigger.setIgnoreHandHeldTrigger(false);
        startTrigger.setStartOnHandHeldTrigger(true);
        startTrigger.setStartDelay(0);
        startTrigger.setTriggerType(ENUM_TRIGGER_ID.TRIGGER_PRESS);
        sendCommand(startTrigger);
        sendCommand(COMMAND_TYPE.COMMAND_SETSTARTTRIGGER, CONFIG_TYPE.CURRENT);


        Command_SetStopTrigger stopTrigger = new Command_SetStopTrigger();
        stopTrigger.setStopOnHandHeldTrigger(true);
        stopTrigger.setIgnoreHandHeldTrigger(false);
        stopTrigger.setTriggerType(ENUM_TRIGGER_ID.TRIGGER_RELEASE);
        stopTrigger.setEnableStopOntagcount(false);
        stopTrigger.setDisableStopOntagcount(true);
        stopTrigger.setStopTagCount(0);
        stopTrigger.setEnableStopOnTimeout(true);
        stopTrigger.setDisableStopOnTimeout(false);
        stopTrigger.setStopTimeout(0);
        stopTrigger.setEnableStopOnInventoryCount(false);
        stopTrigger.setDisableStoponInventoryCount(true);
        stopTrigger.setStopInventoryCount(0);
        sendCommand(stopTrigger);
        sendCommand(COMMAND_TYPE.COMMAND_SETSTOPTRIGGER, CONFIG_TYPE.CURRENT);
    }

        /**
     * 设置声音
     * @param voiceIndex
     */
    public static void setVoice(int voiceIndex){
        Command_SetAttr command_setAttr = new Command_SetAttr();
        command_setAttr.setattnum(Constant.BEEPER_ATTR_NUM);
        command_setAttr.setatttype("B");
        command_setAttr.setattvalue(voiceIndex);
        sendCommand(command_setAttr);

        Command_GetAttrInfo command_getAttrInfo = new Command_GetAttrInfo();
        command_getAttrInfo.setattnum(Constant.BEEPER_ATTR_NUM);
        sendCommand(command_getAttrInfo);
    }


    public static void defaultSetting(){
        Command_ProtocolConfig command_protocolConfig = new Command_ProtocolConfig();
        command_protocolConfig.setEchoOff(true);
        command_protocolConfig.setIncOperEndSummaryNotify(true);
        command_protocolConfig.setIncStartOperationNotify(true);
        command_protocolConfig.setIncStopOperationNotify(true);
        command_protocolConfig.setInctriggereventnotify(true);
        command_protocolConfig.setIncBatteryEventNotify(true);
        sendCommand(command_protocolConfig);
        sendCommand(new Command_GetVersion());
        sendCommand(new Command_GetCapabilities());
        sendCommand(new Command_GetSupportedLinkprofiles());

        Command_GetAttrInfo command_getAttrInfo = new Command_GetAttrInfo();
        command_getAttrInfo.setattnum(140);
        sendCommand(command_getAttrInfo);

        command_getAttrInfo = new Command_GetAttrInfo();
        command_getAttrInfo.setattnum(1500);
        sendCommand(command_getAttrInfo);

        sendCommand(COMMAND_TYPE.COMMAND_SETREPORTCONFIG, CONFIG_TYPE.CURRENT);
        sendCommand(COMMAND_TYPE.COMMAND_SETSELECTRECORDS, CONFIG_TYPE.CURRENT);
        sendCommand(COMMAND_TYPE.COMMAND_SETQUERYPARAMS, CONFIG_TYPE.CURRENT);
        sendCommand(COMMAND_TYPE.COMMAND_SETSTARTTRIGGER, CONFIG_TYPE.CURRENT);
        sendCommand(COMMAND_TYPE.COMMAND_SETSTOPTRIGGER, CONFIG_TYPE.CURRENT);
        sendCommand(COMMAND_TYPE.COMMAND_SETDYNAMICPOWER, CONFIG_TYPE.CURRENT);
//        triggerSetting();
//        sendCommand(new Command_GetVersion());
//        sendCommand(new Command_GetCapabilities());
//        sendCommand(new Command_GetSupportedLinkprofiles());

//        Command_GetAttrInfo command_getAttrInfo = new Command_GetAttrInfo();
//        command_getAttrInfo.setattnum(Constants.BEEPER_ATTR_NUM);
//        sendCommand(command_getAttrInfo);

//        command_getAttrInfo = new Command_GetAttrInfo();
//        command_getAttrInfo.setattnum(Constants.BATCH_MODE_ATTR_NUM);
//        sendCommand(command_getAttrInfo);

//        sendCommand(COMMAND_TYPE.COMMAND_SETREPORTCONFIG, CONFIG_TYPE.CURRENT);
//        sendCommand(COMMAND_TYPE.COMMAND_SETSELECTRECORDS, CONFIG_TYPE.CURRENT);
//        sendCommand(COMMAND_TYPE.COMMAND_SETANTENNACONFIGURATION, CONFIG_TYPE.CURRENT);
//        sendCommand(COMMAND_TYPE.COMMAND_SETQUERYPARAMS, CONFIG_TYPE.CURRENT);
//        sendCommand(COMMAND_TYPE.COMMAND_SETSTARTTRIGGER, CONFIG_TYPE.CURRENT);
//        sendCommand(COMMAND_TYPE.COMMAND_SETSTOPTRIGGER, CONFIG_TYPE.CURRENT);
//        sendCommand(COMMAND_TYPE.COMMAND_SETDYNAMICPOWER, CONFIG_TYPE.CURRENT);
    }
}
