package net.huansi.equipment.equipmentapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zebra.scannercontrol.DCSSDKDefs;
import com.zebra.scannercontrol.DCSScannerInfo;
import com.zebra.scannercontrol.IDcsSdkApiDelegate;
import com.zebra.scannercontrol.SDKHandler;
import com.zebra.scannercontrol.ScannerAppEngine;

import net.huansi.equipment.equipmentapp.activity.DeviceSettingActivity;
import net.huansi.equipment.equipmentapp.activity.check_goods.MyAsyncTask;
import net.huansi.equipment.equipmentapp.constant.Constant;
import net.huansi.equipment.equipmentapp.entity.ReaderDevice;
import net.huansi.equipment.equipmentapp.event.BlueToothEvent;
import net.huansi.equipment.equipmentapp.event.CommandEvent;
import net.huansi.equipment.equipmentapp.event.DialogEvent;
import net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent;
import net.huansi.equipment.equipmentapp.event.MessageEvent;
import net.huansi.equipment.equipmentapp.util.EPData;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RFD8500DeviceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static net.huansi.equipment.equipmentapp.event.BlueToothEvent.DEVICE_CONNECTED_INDEX;
import static net.huansi.equipment.equipmentapp.event.BlueToothEvent.DEVICE_UNCONNECTED_INDEX;
import static net.huansi.equipment.equipmentapp.event.GenericReaderResponseEvent.RECEIVED_DATA_INDEX;


/**
 * Created by 单中年 on 2017/1/17.
 */


public class AlwaysRunningService extends Service implements IDcsSdkApiDelegate ,ScannerAppEngine {
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_DISCONNECTED = 4;
    public static final int MESSAGE_STATE_CHANGE = 11;
    public static final int MESSAGE_DEVICE_NAME = 12;
    public static final int MESSAGE_DISCONNECTED = 14;
    public static final int MESSAGE_CONN_FAILED = 15;
    public static final int MESSAGE_CONN_LOST = 16;
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID CUSTOM_UUID = UUID.fromString("2ad8a392-0e49-e52c-a6d2-60834c012263");
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    private static final String RFD8500 = "RFD8500";

    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;
    private BluetoothAdapter bluetoothAdapter;

    protected ArrayList<DCSScannerInfo> mScannerInfoList = new ArrayList<DCSScannerInfo>();
    public static SDKHandler sdkHandler;
    int notifications_mask = 0;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BluetoothDevice device;
            switch (msg.what) {
                //状态改变
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        //已连接
                        case STATE_CONNECTED:
                            //发送一个事件进行取消dialog
                            EventBus.getDefault().post(new DialogEvent());
                            scannerConnect();
                            Log.e("TAG","1");
                            break;
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    device = msg.getData().getParcelable(Constant.DATA_BLUETOOTH_DEVICE);
                    ReaderDevice readerDevice = new ReaderDevice(device, device.getName(), device.getAddress(), null, null, true);
                    EPData.mConnectedDevice = readerDevice;
                    if (EPData.getActiveDeceiveList().indexOf(EPData.mConnectedDevice) < 0)
                        if(EPData.mConnectedDevice != null) disconnect(EPData.mConnectedDevice.getBluetoothDevice());
                    else {
                        RFD8500DeviceUtils.updateConnectedReaderDevice(EPData.mConnectedDevice, true);
                    }
                    Log.e("TAG","2");
                    break;
                case MESSAGE_CONN_FAILED:
                    RFD8500DeviceUtils.updateConnectedReaderDevice(EPData.mConnectedDevice, false);
                    Log.e("TAG","3");
                    break;
                case MESSAGE_DISCONNECTED:
                case MESSAGE_CONN_LOST:
                    device = msg.getData().getParcelable(Constant.DATA_BLUETOOTH_DEVICE);
                    if (device == null) return;
                    RFD8500DeviceUtils.disconnectedReader(device);
                    sdkHandler.dcssdkUnsubsribeForEvents(notifications_mask);
                    mScannerInfoList.clear();
                    Log.e("TAG","4");
                    break;
            }
        }
    };

    private void scannerConnect() {
        sdkHandler=new SDKHandler(this);
        initializeDcsSdkWithAppSettings();
        sdkHandler.dcssdkSetDelegate(this);
        sdkHandler.dcssdkEnableAvailableScannersDetection(true);
        sdkHandler.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_NORMAL);
        sdkHandler.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_SNAPI);
        sdkHandler.dcssdkGetAvailableScannersList(mScannerInfoList);
        sdkHandler.dcssdkGetActiveScannersList(mScannerInfoList);
        notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_APPEARANCE.value | DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_DISAPPEARANCE.value);
        notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_ESTABLISHMENT.value | DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_TERMINATION.value);
        notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_BARCODE.value);
        sdkHandler.dcssdkSubsribeForEvents(notifications_mask);
        Log.e("TAG","activeList="+mScannerInfoList);
        new MyAsyncTask(mScannerInfoList.get(DeviceSettingActivity.ScannerPosition).getScannerID()).execute();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OthersUtil.registerEvent(this);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        mAlarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlwaysRunningService.class);
        mPendingIntent = PendingIntent.getService(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Log.e("TAG","5");
        long now = System.currentTimeMillis();
        mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 1000*60, mPendingIntent);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;

//        sdkHandler=new SDKHandler(this);
//        initializeDcsSdkWithAppSettings();
//        sdkHandler.dcssdkSetDelegate(this);
//        sdkHandler.dcssdkEnableAvailableScannersDetection(true);
//        sdkHandler.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_NORMAL);
//        sdkHandler.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_SNAPI);
//        sdkHandler.dcssdkGetAvailableScannersList(mScannerInfoList);
//        sdkHandler.dcssdkGetActiveScannersList(mScannerInfoList);
//        notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_APPEARANCE.value | DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_DISAPPEARANCE.value);
//        notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_ESTABLISHMENT.value | DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_TERMINATION.value);
//        notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_BARCODE.value);
//        sdkHandler.dcssdkSubsribeForEvents(notifications_mask);
//        Log.e("TAG","activeList="+mScannerInfoList);
//        new MyAsyncTask(mScannerInfoList.get(1).getScannerID()).execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return  START_STICKY;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void writeCommand(CommandEvent event){
        synchronized ("CommandEvent") {
            if (event.cmdOut.isEmpty()) return;
            write(event.cmdOut.getBytes());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void blueDeviceConnect(BlueToothEvent event){
        switch (event.index){
            case DEVICE_CONNECTED_INDEX:
                connect(event.device);

                Log.e("TAG","6");
                break;
            case DEVICE_UNCONNECTED_INDEX:
                EventBus.getDefault().post(new DialogEvent());
                Message msg = handler.obtainMessage(MESSAGE_CONN_FAILED);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.DATA_BLUETOOTH_DEVICE, event.device);
                msg.setData(bundle);
                handler.sendMessage(msg);
                Log.e("TAG","7");
                break;
        }
    }

    /**
     * 获取连接的设备
     */
    public void getAvailableDevices(Set<BluetoothDevice> devices) {
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            if (isRFIDReader(device)) devices.add(device);
        }
    }

    /**
     * 判断是否为RFD8500设备
     */
    public boolean isRFIDReader(BluetoothDevice device) {
        if (device.getName().startsWith(RFD8500)) return true;
        return false;
    }

    /**
     * 设置状态
     */
    private synchronized void setState(int state) {
        mState = state;
        handler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public synchronized void startConnect() {

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_LISTEN);
    }

    public synchronized void connect(BluetoothDevice device) {

        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mConnectThread = new ConnectThread(device,"ConnectThread");
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        try {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }

            if (mConnectedThread != null) {
                mConnectedThread.cancel();
                mConnectedThread = null;
            }
        } catch (Exception e) {
        }

        mConnectedThread = new ConnectedThread(socket, device);
        mConnectedThread.start();
        Log.e("TAG","8");
        Message msg = handler.obtainMessage(MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.DATA_BLUETOOTH_DEVICE, device);
        msg.setData(bundle);
        handler.sendMessage(msg);
        setState(STATE_CONNECTED);
    }

    public synchronized void stopThread() {

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_NONE);
    }

    public synchronized void disconnect(BluetoothDevice connectedDevice) {
        setState(STATE_DISCONNECTED);
        Message msg = handler.obtainMessage(MESSAGE_DISCONNECTED);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.DATA_BLUETOOTH_DEVICE,connectedDevice);
        msg.setData(bundle);
        handler.sendMessage(msg);
        stopThread();
        if (bluetoothAdapter.isEnabled()) startConnect();
    }


    public void write(byte[] out) {
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            mConnectedThread.write(out);
        }
    }

    private void connectionFailed() {
        setState(STATE_LISTEN);
        stopThread();
        if (bluetoothAdapter.isEnabled()) startConnect();
    }

    private void connectionLost(BluetoothDevice device) {
        setState(STATE_LISTEN);
        Message msg = handler.obtainMessage(MESSAGE_CONN_LOST);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.DATA_BLUETOOTH_DEVICE, device);
        msg.setData(bundle);
        handler.sendMessage(msg);
        mConnectedThread=null;
        if (bluetoothAdapter.isEnabled()) startConnect();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        private ConnectThread(BluetoothDevice device,String name) {
            setName(name);
            mmDevice = device;
            BluetoothSocket tmp = null;
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN
                        || Build.MODEL.equals("TC55")) tmp = device.createInsecureRfcommSocketToServiceRecord(CUSTOM_UUID);
                else tmp = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();
            Log.e("TAG","9");
            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                }
                connectionFailed();
                return;
            }

            synchronized (AlwaysRunningService.this) {
                mConnectThread = null;
            }
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;
        private final BluetoothDevice mDevice;
        private BufferedReader mmReader;
        private String data;

        public ConnectedThread(BluetoothSocket socket, BluetoothDevice device) {
            mmSocket = socket;
            mDevice = device;
            InputStream tmpIn;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                mmReader = new BufferedReader(new InputStreamReader(tmpIn));
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmOutStream = tmpOut;
        }

        public void run() {

            while (true) {
                try {
                    if (this.isInterrupted()) break;
                    if (mmReader != null) {
                        data = mmReader.readLine();
                        if (data != null) EventBus.getDefault().post(new GenericReaderResponseEvent(RECEIVED_DATA_INDEX,data));
                    }
                } catch (IOException e) {
                    if (mState != STATE_DISCONNECTED) connectionLost(mDevice);
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                if (mState != STATE_DISCONNECTED) connectionLost(mDevice);
            }
        }

        public void cancel() {
            try {
                this.interrupt();
                if (mmOutStream != null) {
                    mmOutStream.flush();
                    mmOutStream.close();
                }
                if (mmReader != null)
                    mmReader.close();
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OthersUtil.unregisterEvent(this);
        Intent intent=new Intent(this,AlwaysRunningService.class);
        startService(intent);
        Log.e("TAG","0="+intent);
    }


    @Override
    public void dcssdkEventScannerAppeared(DCSScannerInfo availableScanner) {
        mScannerInfoList.add(availableScanner);
    }

    @Override
    public void dcssdkEventScannerDisappeared(int i) {

    }

    @Override
    public void dcssdkEventCommunicationSessionEstablished(DCSScannerInfo activeScanner) {
        String inXML = "<inArgs><scannerID>" + activeScanner.getScannerID() + "</scannerID><cmdArgs><arg-xml><attrib_list><attribute><id>" +
                55 + "</id><datatype>F</datatype><value>" + 0 + "</value></attribute></attrib_list></arg-xml></cmdArgs></inArgs>";
        StringBuilder sb = new StringBuilder() ;
        DCSSDKDefs.DCSSDK_RESULT result = sdkHandler.dcssdkExecuteCommandOpCodeInXMLForScanner(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_SET, inXML,sb,activeScanner.getScannerID());
        Log.e("TAG","result=" + result);

    }

    @Override
    public void dcssdkEventCommunicationSessionTerminated(int i) {

    }

    @Override
    public void dcssdkEventBarcode(byte[] barcodeData, int barcodeType, int fromScannerID) {
        Log.e("TAG","barcodeData="+new String(barcodeData));
        EventBus.getDefault().post(new MessageEvent(new String(barcodeData)));
        //scannerCode=new String(barcodeData);
        //scannerList.add(new String(barcodeData));
    }

    @Override
    public void dcssdkEventImage(byte[] bytes, int i) {

    }

    @Override
    public void dcssdkEventVideo(byte[] bytes, int i) {

    }
    @Override
    public void initializeDcsSdkWithAppSettings() {

    }

    @Override
    public void showMessageBox(String s) {

    }

    @Override
    public int showBackgroundNotification(String s) {
        return 0;
    }

    @Override
    public int dismissBackgroundNotifications() {
        return 0;
    }

    @Override
    public boolean isInBackgroundMode(Context context) {
        return false;
    }

    @Override
    public void addDevListDelegate(IScannerAppEngineDevListDelegate iScannerAppEngineDevListDelegate) {

    }

    @Override
    public void addDevConnectionsDelegate(IScannerAppEngineDevConnectionsDelegate iScannerAppEngineDevConnectionsDelegate) {

    }

    @Override
    public void addDevEventsDelegate(IScannerAppEngineDevEventsDelegate iScannerAppEngineDevEventsDelegate) {

    }

    @Override
    public void removeDevListDelegate(IScannerAppEngineDevListDelegate iScannerAppEngineDevListDelegate) {

    }

    @Override
    public void removeDevConnectiosDelegate(IScannerAppEngineDevConnectionsDelegate iScannerAppEngineDevConnectionsDelegate) {

    }

    @Override
    public void removeDevEventsDelegate(IScannerAppEngineDevEventsDelegate iScannerAppEngineDevEventsDelegate) {

    }

    @Override
    public List<DCSScannerInfo> getActualScannersList() {
        return null;
    }

    @Override
    public DCSScannerInfo getScannerInfoByIdx(int i) {
        return null;
    }

    @Override
    public DCSScannerInfo getScannerByID(int i) {
        return null;
    }

    @Override
    public void raiseDeviceNotificationsIfNeeded() {

    }

    @Override
    public void updateScannersList() {

    }

    @Override
    public DCSSDKDefs.DCSSDK_RESULT connect(int i) {
        return null;
    }

    @Override
    public void disconnect(int i) {

    }

    @Override
    public DCSSDKDefs.DCSSDK_RESULT setAutoReconnectOption(int i, boolean b) {
        return null;
    }

    @Override
    public void enableScannersDetection(boolean b) {

    }

    @Override
    public void configureNotificationAvailable(boolean b) {

    }

    @Override
    public void configureNotificationActive(boolean b) {

    }

    @Override
    public void configureNotificationBarcode(boolean b) {

    }

    @Override
    public void configureNotificationImage(boolean b) {

    }

    @Override
    public void configureNotificationVideo(boolean b) {

    }

    @Override
    public void configureOperationalMode(DCSSDKDefs.DCSSDK_MODE dcssdk_mode) {

    }

    @Override
    public boolean executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE dcssdk_command_opcode, String s, StringBuilder stringBuilder, int i) {
        return false;
    }
}
