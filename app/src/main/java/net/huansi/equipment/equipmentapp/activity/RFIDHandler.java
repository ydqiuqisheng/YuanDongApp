//package net.huansi.equipment.equipmentapp.activity;//package com.zebra.scan_write.android.scanscanwrite;
//
//import android.bluetooth.BluetoothDevice;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.FragmentManager;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.motorolasolutions.ASCII_SDK.AcessOperation;
//import com.motorolasolutions.ASCII_SDK.Command_GetCapabilities;
//import com.motorolasolutions.ASCII_SDK.Command_GetVersion;
//import com.motorolasolutions.ASCII_SDK.Command_SetAttr;
//import com.motorolasolutions.ASCII_SDK.IMsg;
//import com.motorolasolutions.ASCII_SDK.Notification;
//import com.motorolasolutions.ASCII_SDK.Notification_StopOperation;
//import com.motorolasolutions.ASCII_SDK.RESPONSE_TYPE;
//import com.motorolasolutions.ASCII_SDK.ResponseMsg;
//import com.motorolasolutions.ASCII_SDK.Response_Capabilities;
//import com.motorolasolutions.ASCII_SDK.Response_TagData;
//import com.motorolasolutions.ASCII_SDK.Response_VersionInfo;
//
//import net.huansi.equipment.equipmentapp.activity.check_goods.CheckMainActivity;
//import net.huansi.equipment.equipmentapp.activity.check_goods.ScannerActivity;
//import net.huansi.equipment.equipmentapp.service.BluetoothService;
//
//
///**
// * Created by DHXW76 on 26-May-16.
// */
//public class RFIDHandler implements GenericReader.GenericReaderResponseParsedListener {
//
//
//    protected final GenericReader genericReader;
//    private final ScannerActivity mainActivity;
//    public BluetoothService bluetoothService;
//    public Boolean isConnected = null;
//    private final Handler mHandler = initializeHandler();
//    private String minPower;
//    private String maxPower;
//    private Boolean writesuccess = false;
//    private String STATUS_OK = "OK";
//
//    public RFIDHandler(ScannerActivity activity) {
//        mainActivity = activity;
//        genericReader = new GenericReader();
//        bluetoothService = new BluetoothService(this.mainActivity, mHandler);
//        genericReader.attachActivity(this, bluetoothService);
//        bluetoothService.setGenericReader(genericReader);
//    }
//
//    private Handler initializeHandler() {
//        return new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                BluetoothDevice device;
//                switch (msg.what) {
//                    case BluetoothService.MESSAGE_STATE_CHANGE:
//                        switch (msg.arg1) {
//                            case BluetoothService.STATE_CONNECTED:
//                                isConnected = true;
//                                break;
//                            case BluetoothService.STATE_CONNECTING:
//                                break;
//                            case BluetoothService.STATE_LISTEN:
//                            case BluetoothService.STATE_NONE:
//                                break;
//                        }
//                        break;
//                    case BluetoothService.MESSAGE_DEVICE_NAME:
//                        isConnected = true;
//                        break;
//                    case BluetoothService.MESSAGE_TOAST:
//                        break;
//                    case BluetoothService.MESSAGE_CONN_FAILED:
//                        isConnected = false;
//                        break;
//                    case BluetoothService.MESSAGE_DISCONNECTED:
//                    case BluetoothService.MESSAGE_CONN_LOST:
//                        break;
//                }
//            }
//        };
//    }
//
//
//    @Override
//    public void responseDataParsedFromGenericReader(ResponseMsg responseMsg) {
//        if (RESPONSE_TYPE.TAGDATA == responseMsg.getResponseType()) {
//            final Response_TagData response_tagData = (Response_TagData) responseMsg;
//            handleTagResponse(response_tagData);
//        } else if (RESPONSE_TYPE.VERSIONINFO == responseMsg.getResponseType()) {
//            Response_VersionInfo response_versionInfo = (Response_VersionInfo) responseMsg;
//            if (mainActivity.versionInfo.contains(response_versionInfo.Device))
//                mainActivity.versionInfo.remove(response_versionInfo.Device);
//            mainActivity.versionInfo.put(response_versionInfo.Device, response_versionInfo.Version);
//        } else if (RESPONSE_TYPE.CAPABILITIES == responseMsg.getResponseType()) {
//            Response_Capabilities response_capabilities = (Response_Capabilities) responseMsg;
//            if (response_capabilities.Name.equalsIgnoreCase("MIN_POWER"))
//                minPower = response_capabilities.Value;
//            if (response_capabilities.Name.equalsIgnoreCase("MAX_POWER"))
//                maxPower = response_capabilities.Value;
//        }
//    }
//
//    @Override
//    public void notificationFromGenericReader(final Notification notification) {
//        mainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (notification instanceof Notification_StopOperation) {
//                    if (!writesuccess)
//                    writesuccess = false;
//                }
//            }
//        });
//    }
//
//    @Override
//    public void configurationsFromReader(IMsg msg) {
//
//    }
//
//    private void handleTagResponse(final Response_TagData response_tagData) {
//        mainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                AcessOperation readAcessOperation = null;
//                if (response_tagData.tagAcessOprations != null)
//                    for (AcessOperation acessOperation : response_tagData.tagAcessOprations) {
//                        if (acessOperation.opration.equalsIgnoreCase("read") || acessOperation.opration.equalsIgnoreCase("write")) {
//                            readAcessOperation = acessOperation;
//                        }
//                    }
//                if (readAcessOperation != null) {
//                    if (readAcessOperation.operationStatus != null && !readAcessOperation.operationStatus.equalsIgnoreCase(STATUS_OK))
//                        Toast.makeText(mainActivity, readAcessOperation.operationStatus, Toast.LENGTH_SHORT).show();
//                    else {
//                        if (readAcessOperation.opration.equalsIgnoreCase("write")) {
//                            Toast.makeText(mainActivity, "WRITE SUCCESS", Toast.LENGTH_SHORT).show();
////                            mainActivity.ShowToast("SUCCESS", readAcessOperation.operationStatus);
////                            mainActivity.upc = null;
////                            mainActivity.epc = null;
////                            mainActivity.scanwrite.viewPager.setCurrentItem(1);
//                        }
//                    }
//                    writesuccess = true;
//                }
//            }
//        });
//    }
//
//    private void SetMode() {
//        Command_SetAttr attr = new Command_SetAttr();
//        attr.setattnum(1664);
//        attr.setatttype("B");
//        attr.setattvalue(1);
//        genericReader.sendCommand(attr);
//        genericReader.sendCommand(new Command_GetVersion());
//        genericReader.sendCommand(new Command_GetCapabilities());
//    }
//}
