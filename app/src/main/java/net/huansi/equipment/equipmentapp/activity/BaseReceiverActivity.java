package net.huansi.equipment.equipmentapp.activity;//package net.huansi.equipment.equipmentapp.activity;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//
//import com.motorolasolutions.ASCII_SDK.COMMAND_TYPE;
//import com.motorolasolutions.ASCII_SDK.CONFIG_TYPE;
//import com.motorolasolutions.ASCII_SDK.Command;
//import com.motorolasolutions.ASCII_SDK.Command_GetAttrInfo;
//import com.motorolasolutions.ASCII_SDK.Command_ProtocolConfig;
//import com.motorolasolutions.ASCII_SDK.Command_SetAntennaConfiguration;
//import com.motorolasolutions.ASCII_SDK.Command_SetAttr;
//
//import net.huansi.equipment.equipmentapp.constant.Constants;
//import net.huansi.equipment.equipmentapp.entity.ReaderDevice;
//import net.huansi.equipment.equipmentapp.helpers.GenericReader;
//import net.huansi.equipment.equipmentapp.helpers.GenericReader.GenericReaderResponseParsedListener;
//import net.huansi.equipment.equipmentapp.service.BluetoothService;
//import net.huansi.equipment.equipmentapp.util.EPData;
//import net.huansi.equipment.equipmentapp.util.OthersUtil;
//import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;
//
//import java.util.HashSet;
//
//import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;
//import static android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED;
//import static android.bluetooth.BluetoothDevice.BOND_BONDED;
//import static android.bluetooth.BluetoothDevice.BOND_NONE;
//
//
//public abstract class BaseReceiverActivity extends BaseActivity
//        implements GenericReaderResponseParsedListener {
////    private static ArrayList<BluetoothDeviceFoundHandler> bluetoothDeviceFoundHandlers = new ArrayList<>();
//    private static boolean is_disconnection_requested = false;
//    private static int autoreconnect_count = 1;
//    protected final Handler mHandler = initializeHandler();
//    protected LoadProgressDialog progressDialog;
//    protected BluetoothService bluetoothService;
//
//    protected final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            BluetoothAdapter.getDefaultAdapter().getBondedDevices();
//            String action = intent.getAction();
//            switch (action) {
//                case ACTION_STATE_CHANGED:
//                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
//                            BluetoothAdapter.ERROR);
//                    if (state == BluetoothAdapter.STATE_ON) {
//                        initializeService();
//                        if (bluetoothService != null)
//                            bluetoothService.start();
//                    } else if (state == BluetoothAdapter.STATE_OFF) {
//                        EPData.isBlueRunning = false;
//                    }
//                    break;
//                case ACTION_BOND_STATE_CHANGED:
//                    // Get the BluetoothDevice object from the Intent
//                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    ReaderDevice readerDevice = new ReaderDevice(device, device.getName(), device.getAddress(), null, null, false);
//                    int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
//                    switch (bondState){
//                        case BOND_BONDED:
//                            if (bluetoothService.isRFIDReader(device)) {
//                                if (!EPData.getActiveDeceiveList().contains(readerDevice)) {
//                                    EPData.getActiveDeceiveList().add(readerDevice);
//                                    if (bluetoothDeviceFoundHandlers != null && bluetoothDeviceFoundHandlers.size() > 0) {
//                                        for (BluetoothDeviceFoundHandler bluetoothDeviceFoundHandler : bluetoothDeviceFoundHandlers)
//                                            bluetoothDeviceFoundHandler.bluetoothDevicePaired(readerDevice);
//                                    }
//                                }
//                            }
//                            break;
//                        case BOND_NONE:
//                            if (EPData.getActiveDeceiveList().contains(readerDevice)) {
//                                EPData.getActiveDeceiveList().remove(readerDevice);
//                                //disconnect if it is was connected device
//                                if (isConnected() && EPData.mConnectedDevice != null &&
//                                        device.getAddress().equalsIgnoreCase(EPData.mConnectedDevice.getAddress()))
//                                    disconnectFromBluetoothDevice();
//                            }
//                            break;
//                    }
//                    break;
//            }
//        }
//    };
//    protected GenericReader genericReader;
//    //BluetoothAdapter
//    protected BluetoothAdapter mBluetoothAdapter;
//
//    /**
//     * callback of actvity, which will call while creating of the activity
//     *
//     * @param savedInstanceState
//     */
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        filter.addAction(Constants.ACTION_READER_CONN_FAILED);
//        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        registerReceiver(mReceiver, filter);
//
//
//        // Get local Bluetooth adapter
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        // If the adapter is null, then Bluetooth is not supported
//        if (mBluetoothAdapter == null) {
//            finish();
//        } else {
//            bluetoothService = EPData.getBluetoothService(this, mHandler);
//            genericReader = EPData.getGenericReader(this, bluetoothService);
//            defaultSetting();
//            setVoice(0);
//            setPowerLevel((short) 270);
//        }
//
//    }
//
//
//
//    /**
//     * call back of activity,which will call when brought into front
//     */
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // Performing this check in onResume() covers the case in which BT was
//        // not enabled during onStart(), so we were paused to enable it...
//        bluetoothService = EPData.getBluetoothService(this,mHandler);
//        genericReader = EPData.getGenericReader(this,bluetoothService);
//    }
//
//
//    /**
//     * call back of activity,which will call when activity went to background
//     */
//    @Override
//    protected void onStop() {
//        super.onStop();
//        // If we're already discovering, stop it
//        if (mBluetoothAdapter.isDiscovering()) {
//            mBluetoothAdapter.cancelDiscovery();
//        }
//    }
//
//    /**
//     * Method to link BluetoothService and GenericReader together
//     */
//    protected void initializeService() {
//
//        // Initialize the BluetoothService to perform bluetooth connections
//        if (bluetoothService == null)
//            bluetoothService = new BluetoothService(mHandler);
//
//        if (genericReader == null) {
//            genericReader = new GenericReader();
//            genericReader.attachActivity(this, bluetoothService);
//        }
//
//        bluetoothService.setGenericReader(genericReader);
//
//        //Save the instance in EPData so that it is available throughout the app
//        EPData.bluetoothService = bluetoothService;
//        EPData.genericReader = genericReader;
//        EPData.handler = mHandler;
//        loadAvailableReaders();
//    }
//
////    /**
////     * method to know whether bluetooth is enabled or not
////     *
////     * @return - true if bluetooth enabled
////     * - false if bluetooth disabled
////     */
////    public boolean isBluetoothEnabled() {
////        return mBluetoothAdapter.isEnabled();
////    }
//
//    /**
//     * Method to load available readers into readers list collection from bluetooth service
//     */
//    public void loadAvailableReaders() {
//        EPData.getActiveDeceiveList().clear();
//        HashSet<BluetoothDevice> availableReaders = new HashSet<>();
//        bluetoothService.getAvailableDevices(availableReaders);
//        for (BluetoothDevice device : availableReaders) {
//            EPData.getActiveDeceiveList().add(new ReaderDevice(device, device.getName(), device.getAddress(), null, null, false));
//        }
//    }
//
//
////
////    /**
////     * Called by fragments wishing to connect to a bluetooth device
////     *
////     * @param device - Bluetooth device to which we want to connect
////     */
////    public void connectToBluetoothDevice(BluetoothDevice device) {
////        if (bluetoothService != null && device.getBondState() == BOND_BONDED) {
////            try {
////                while (true) {
////                    if (EPData.mConnectedDevice != null) Thread.sleep(500);
////                    else {
////                        break;
////                    }
////                }
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////            bluetoothService.connect(device);
////        } else {
////            OthersUtil.dismissLoadDialog(progressDialog);
////            Message msg = mHandler.obtainMessage(BluetoothService.MESSAGE_CONN_FAILED);
////            Bundle bundle = new Bundle();
////            bundle.putParcelable(Constants.DATA_BLUETOOTH_DEVICE, device);
////            msg.setData(bundle);
////            mHandler.sendMessage(msg);
////        }
////    }
//
//    /**
//     * Called by fragments wishing to disconnect from bluetooth device
//     */
//    public void disconnectFromBluetoothDevice() {
//        if (bluetoothService != null) {
//            is_disconnection_requested = true;
//            if(EPData.mConnectedDevice != null)
//                bluetoothService.disconnect(EPData.mConnectedDevice.getBluetoothDevice());
//        }
//    }
//
//    /**
//     * Method to know if reader is connected
//     */
//    public boolean isConnected() {
//        return bluetoothService.isConnected();
//    }
//
//
//    /**
//     * Method to send a command to the underlying bluetooth layers
//     *
//     * @param cmd - Command to be sent
//     */
//    public void sendCommand(Command cmd) {
//        genericReader.sendCommand(cmd);
//    }
//
//    /**
//     * Method to send a string command to the underlying bluetooth layers
//     *
//     * @param command_type - command type
//     * @param config_type  - configuration type
//     */
//    public void sendCommand(COMMAND_TYPE command_type, CONFIG_TYPE config_type) {
//        genericReader.sendCommand(command_type, config_type);
//    }
//
////    /**
////     * method which will update the reader settings in EPData with configuration received from reader
////     *
////     * @param msg - configuration
////     */
////
//
////    /**
////     * Method to change operation status and ui in app on recieving abort status
////     */
////    private void operationHasAborted() {
////        //retrieve get tags if inventory in batch mode got aborted
////        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CONTENT_FRAGMENT);
////        if (EPData.isBatchModeInventoryRunning != null && EPData.isBatchModeInventoryRunning) {
////            if (isInventoryAborted) {
////                EPData.isBatchModeInventoryRunning = false;
////                isInventoryAborted = true;
////                EPData.isGettingTags = true;
////                sendCommand(new Command_GetTags());
////            }
////        }
////
////        if (EPData.mIsInventoryRunning) {
////            if (isInventoryAborted) {
////                EPData.mIsInventoryRunning = false;
////                isInventoryAborted = false;
////                isTriggerRepeat = null;
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        if (fragment instanceof InventoryFragment)
////                            ((InventoryFragment) fragment).resetInventoryDetail();
////                        else if (fragment instanceof RapidReadFragment)
////                            ((RapidReadFragment) fragment).resetInventoryDetail();
////                        //export Data to the file
////                        if (EPData.EXPORT_DATA)
////                            if (EPData.tagsReadInventory != null && !EPData.tagsReadInventory.isEmpty()) {
////                                new DataExportTask(getEPDataContext(), EPData.tagsReadInventory, EPData.mConnectedDevice.getName(), EPData.TOTAL_TAGS, EPData.UNIQUE_TAGS, EPData.mRRStartedTime).execute();
////                            }
////                    }
////                });
////            }
////        } else if (EPData.isLocatingTag) {
////            if (isLocationingAborted) {
////                EPData.isLocatingTag = false;
////                isLocationingAborted = false;
////                if (fragment instanceof LocationingFragment)
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            ((LocationingFragment) fragment).resetLocationingDetails(false);
////                        }
////                    });
////            }
////        }
////    }
//
////    /**
////     * Method which will called once notification received from reader.
////     * update the operation status in the EPData based on notification type
////     *
////     * @param notification - notification received from reader
////     */
////    @Override
////    public void notificationFromGenericReader(Notification notification) {
////        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CONTENT_FRAGMENT);
////        if (notification instanceof Notification_StartOperation) {
////            if (!EPData.isAccessCriteriaRead && !EPData.isLocatingTag) {
////                // if (!(EPData.setStartTriggerSettings.getRepeat() && Inventorytimer.getInstance().isTimerRunning())) {
////                EPData.mIsInventoryRunning = true;
////                Inventorytimer.getInstance().startTimer();
////                // }
////            }
////        } else if (notification instanceof Notification_StopOperation) {
////            if (progressDialog != null && progressDialog.isShowing())
////                progressDialog.dismiss();
////            if (EPData.isAccessCriteriaRead && accessTagCount == 0) {
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        Toast.makeText(getEPDataContext(), R.string.err_access_op_failed, Toast.LENGTH_SHORT).show();
////                    }
////                });
////
////            }
////            accessTagCount = 0;
////            EPData.isAccessCriteriaRead = false;
////
////            if (EPData.mIsInventoryRunning) {
////                Inventorytimer.getInstance().stopTimer();
////            } else if (EPData.isGettingTags) {
////                EPData.isGettingTags = false;
////                sendCommand(new Command_PurgeTags());
////                //retrieve reader settings if not received
////                if (!isReaderDefaultSettingsReceived) {
////                    sendCommand(new Command_GetCapabilities());
////                    sendCommand(new Command_GetAllSupportedRegions());
////                    sendCommand(COMMAND_TYPE.COMMAND_SETREGULATORY, CONFIG_TYPE.CURRENT);
////                    getReaderDefaultSetting();
////                }
////                if (EPData.EXPORT_DATA)
////                    if (EPData.tagsReadInventory != null && !EPData.tagsReadInventory.isEmpty()) {
////                        runOnUiThread(new Runnable() {
////                            @Override
////                            public void run() {
////                                new DataExportTask(getEPDataContext(), EPData.tagsReadInventory, EPData.mConnectedDevice.getName(), EPData.TOTAL_TAGS, EPData.UNIQUE_TAGS, EPData.mRRStartedTime).execute();
////                            }
////                        });
////                    }
////            }
////
////            if(!((EPData.setStartTriggerSettings != null && EPData.setStartTriggerSettings.getRepeat()) || (isTriggerRepeat != null && isTriggerRepeat))) {
////                if (EPData.mIsInventoryRunning)
////                    isInventoryAborted= true;
////                else if(EPData.isLocatingTag)
////                    isLocationingAborted = true;
////                operationHasAborted();
////            }
////
////        } else if (notification instanceof Notification_OperEndSummary) {
////            // re calculate read rate at end
////            //check the started time to avoid showing of abnormal read rate in app if app didn't received start notification
////            if (EPData.mRRStartedTime == 0)
////                EPData.TAG_READ_RATE = 0;
////            else
////                EPData.TAG_READ_RATE = (int) (EPData.TOTAL_TAGS / (EPData.mRRStartedTime / (float) 1000));
////            if (fragment instanceof RapidReadFragment)
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        ((RapidReadFragment) fragment).updateInventoryDetails();
////                    }
////                });
////        } else if (notification instanceof Notification_TriggerEvent) {
////            Notification_TriggerEvent triggerEvent = (Notification_TriggerEvent) notification;
////            if (fragment instanceof TriggerEventHandler) {
////                if (triggerEvent.TriggerValue == ENUM_TRIGGER_ID.TRIGGER_PRESS && (EPData.settings_startTrigger.equalsIgnoreCase(Constants.IMMEDIATE) || (isTriggerRepeat != null && !isTriggerRepeat)))
////                    ((TriggerEventHandler) fragment).triggerPressEventRecieved();
////                else if (triggerEvent.TriggerValue == ENUM_TRIGGER_ID.TRIGGER_RELEASE && (EPData.settings_stopTrigger.equalsIgnoreCase(Constants.IMMEDIATE) || (isTriggerRepeat != null && !isTriggerRepeat)))
////                    ((TriggerEventHandler) fragment).triggerReleaseEventRecieved();
////            }
////        } else if (notification instanceof Notification_BatteryEvent) {
////            final Notification_BatteryEvent batteryEvent = (Notification_BatteryEvent) notification;
////            EPData.batteryEvent = batteryEvent;
////            setActionBarBatteryStatus(batteryEvent.Level);
////            if (fragment instanceof BatteryNotificationHandler) {
////                ((BatteryNotificationHandler) fragment).deviceStatusReceived(batteryEvent.Level, batteryEvent.Charging, batteryEvent.Cause);
////            } else {
////                if (EPData.NOTIFY_BATTERY_STATUS && batteryEvent.Cause!=null) {
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            if (batteryEvent.Cause.trim().equalsIgnoreCase(Constants.MESSAGE_BATTERY_CRITICAL))
////                                sendNotification(ACTION_READER_BATTERY_CRITICAL, getString(R.string.battery_status__critical_message));
////                            else if (batteryEvent.Cause.trim().equalsIgnoreCase(Constants.MESSAGE_BATTERY_LOW))
////                                sendNotification(ACTION_READER_BATTERY_CRITICAL, getString(R.string.battery_status_low_message));
////                        }
////                    });
////                }
////            }
////        } else if (notification instanceof Notification_BatchModeEvent) {
////            startTimer();
////            EPData.isBatchModeInventoryRunning = true;
////            clearInventoryData();
////            EPData.mIsInventoryRunning = true;
////            EPData.memoryBankId = 0;
////            if (((Notification_BatchModeEvent) notification).Repeat == 0)
////                isTriggerRepeat = false;
////            else
////                isTriggerRepeat = true;
////            runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    if (fragment instanceof BatchModeEventHandler)
////                        ((BatchModeEventHandler) fragment).batchModeEventReceived();
////                    if (fragment instanceof ReadersListFragment) {
////                        ((ReadersListFragment) fragment).cancelProgressDialog();
////                        if( EPData.mConnectedDevice.getModel()== null) {
////                            EPData.mConnectedDevice.setModel(getString(R.string.batch_mode_running_title));
////                            EPData.mConnectedDevice.setSerial(getString(R.string.batch_mode_running_title));
////                            ((ReadersListFragment) fragment).capabilitiesRecievedforDevice();
////                        }
////                    }
////                }
////            });
////        }
////    }
////
////    /**
////     * method lear inventory data like total tags, unique tags, read rate etc..
////     */
////    public void clearInventoryData() {
////        EPData.TOTAL_TAGS = 0;
////        EPData.mRRStartedTime = 0;
////        EPData.UNIQUE_TAGS = 0;
////        EPData.TAG_READ_RATE = 0;
////        if (EPData.tagIDs != null)
////            EPData.tagIDs.clear();
////        if (EPData.tagsReadInventory.size() > 0)
////            EPData.tagsReadInventory.clear();
////        if (EPData.tagsReadInventory.size() > 0)
////            EPData.tagsReadInventory.clear();
////        if (EPData.inventoryList != null && EPData.inventoryList.size() > 0)
////            EPData.inventoryList.clear();
////    }
//
//    /**
//     * Method to initialize the handler
//     *
//     * @return - The Handler to handle the message like device connected or connection failed
//     */
//    private Handler initializeHandler() {
//        if (EPData.handler != null)
//            return EPData.handler;
//        else {
//            return new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
////                    BluetoothDevice device;
////                    switch (msg.what) {
////                        case BluetoothService.MESSAGE_STATE_CHANGE:
////                            switch (msg.arg1) {
////                                case BluetoothService.STATE_CONNECTED:
////                                    //发送一个事件进行取消dialog
////                                    EventBus.getDefault().post(new DeviceEvent());
//////                                    Log.i("BluetoothService","STATE_CONNECTED");
////                                    break;
////                                case BluetoothService.STATE_CONNECTING:
//////
////                                    break;
////                                case BluetoothService.STATE_LISTEN:
//////                                    Log.i("BluetoothService","STATE_LISTEN");
////                                case BluetoothService.STATE_NONE:
//////                                    Log.i("BluetoothService","STATE_NONE");
////                                    break;
////                            }
////                            break;
////                        case BluetoothService.MESSAGE_DEVICE_NAME:
////                            // save the connected device's name
////                            device = msg.getData().getParcelable(Constants.DATA_BLUETOOTH_DEVICE);
////                            ReaderDevice readerDevice = new ReaderDevice(device, device.getName(), device.getAddress(), null, null, true);
////                            EPData.mConnectedDevice = readerDevice;
////                            if (bluetoothDeviceFoundHandlers != null && bluetoothDeviceFoundHandlers.size() > 0) {
////                                for (BluetoothDeviceFoundHandler bluetoothDeviceFoundHandler : bluetoothDeviceFoundHandlers)
////                                    bluetoothDeviceFoundHandler.bluetoothDeviceConnected(readerDevice);
////                            }
////                            //disconnect from device if device removed from readers list(device got unpaired)
////                            if (EPData.getActiveDeceiveList().indexOf(EPData.mConnectedDevice) < 0)
////                                disconnectFromBluetoothDevice();
////                            else {
////                                updateConnectedDeviceDetails(EPData.mConnectedDevice, true);
////                            }
////
//////                            if (isBluetoothEnabled()) {
//////                                if (isConnected()) {
//////                                    readerDevice.setPassword(getReaderPassword(readerDevice.getAddress()));
//////                                    if (readerDevice.getPassword() != null) {
//////                                        Command_Connect command_connect = new Command_Connect();
//////                                        command_connect.setpassword(readerDevice.getPassword());
//////                                        sendCommand(command_connect);
//////                                    } else
//////                                        sendCommand(new Command_Connect());
//////                                }
//////                            }
////                            break;
////                        case BluetoothService.MESSAGE_CONN_FAILED:
////                            //TO handle reconnection.
////                            //Toast.makeText(getEPDataContext(), "Connection Failed!! was received", Toast.LENGTH_SHORT).show();
////                            BluetoothDevice bluetoothDevice = (BluetoothDevice) msg.getData().getParcelable(Constants.DATA_BLUETOOTH_DEVICE);
////                            updateConnectedDeviceDetails(EPData.mConnectedDevice, false);
////                            if (bluetoothDeviceFoundHandlers != null && bluetoothDeviceFoundHandlers.size() > 0) {
////                                for (BluetoothDeviceFoundHandler bluetoothDeviceFoundHandler : bluetoothDeviceFoundHandlers)
////                                    bluetoothDeviceFoundHandler.bluetoothDeviceConnFailed(new ReaderDevice(bluetoothDevice, bluetoothDevice.getName(), bluetoothDevice.getAddress(), null, null, false));
////                            }
////                            if (!is_disconnection_requested
////                                    && EPData.AUTO_RECONNECT_READERS && autoreconnect_count <= Constants.AUTO_RECONNECTION_COUNT && EPData.mConnectedDevice != null && bluetoothDevice.getName().equalsIgnoreCase(EPData.mConnectedDevice.getName())) {
////                                if (isBluetoothEnabled()) {
////                                    autoreconnect_count++;
////                                    connectToBluetoothDevice(bluetoothDevice);
////                                } else {
//////                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_bluetooth_disabled), Toast.LENGTH_SHORT).show();
////                                    if (autoreconnect_count <= Constants.AUTO_RECONNECTION_COUNT) {
////                                        EPData.mConnectedDevice = null;
////                                        clearSettings();
////                                    }
////                                }
////                            } else {
////                                autoreconnect_count = 0;
////                                is_disconnection_requested = false;
////                                EPData.mConnectedDevice = null;
////                                clearSettings();
////
////                            }
////                            break;
////                        case BluetoothService.MESSAGE_DISCONNECTED:
////                        case BluetoothService.MESSAGE_CONN_LOST:
////                            device = msg.getData().getParcelable(Constants.DATA_BLUETOOTH_DEVICE);
////                            if (device == null) return;
////                            readerDisconnected(device);
//////                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//////                                    disconnectTask = new UpdateDisconnectedStatusTask(device);
//////                                    disconnectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//////                                }
//////                                else{
//////                                    disconnectTask = new UpdateDisconnectedStatusTask(device);
//////                                    disconnectTask.execute();
//////                                }
////
////                            break;
////                    }
//                }
//            };
//        }
//    }
//
//    /**
//     * method to get connect password for the reader
//     *
//     * @param address - device BT address
//     * @return connect password of the reader
//     */
//    private String getReaderPassword(String address) {
//        SharedPreferences sharedPreferences = getSharedPreferences(Constants.READER_PASSWORDS, 0);
//        return sharedPreferences.getString(address, null);
//    }
//
////    /**
////     * Method to send the notification
////     *
////     * @param action - intent action
////     * @param data   - notification message
////     */
////    public void sendNotification(String action, String data) {
////        if (EPData.isActivityVisible()) {
////            if (action.equalsIgnoreCase(Constants.ACTION_READER_BATTERY_CRITICAL) || action.equalsIgnoreCase(Constants.ACTION_READER_BATTERY_LOW)) {
////                new CustomToast(BaseReceiverActivity.this, R.layout.toast_layout, data).show();
////            } else {
////                Toast.makeText(getEPDataContext(), data, Toast.LENGTH_SHORT).show();
////            }
////        } else {
////            Intent i = new Intent(BaseReceiverActivity.this, NotificationsService.class);
////            i.putExtra(Constants.INTENT_ACTION, action);
////            i.putExtra(Constants.INTENT_DATA, data);
////            startService(i);
////        }
////    }
//
////    /**
////     * Method to send the notification
////     *
////     * @param action - intent action
////     * @param data   - Bluetooth device
////     */
////    public void sendNotification(String action, Parcelable data) {
////        Intent i = new Intent(BaseReceiverActivity.this, NotificationsService.class);
////        i.setAction(Constants.ACTION_READER_CONN_FAILED);
////        i.putExtra(Constants.INTENT_ACTION, action);
////        i.putExtra(Constants.DATA_BLUETOOTH_DEVICE, data);
////        startService(i);
////    }
//
////    /**
////     * Method for registering the classes for device events like paired,unpaired, connected and disconnected.
////     * The registered classes will get notified when device event occurs.
////     *
////     * @param bluetoothDeviceFoundHandler - handler class to register with base receiver activity
////     */
////    public void addBluetoothDeviceFoundHandler(BluetoothDeviceFoundHandler bluetoothDeviceFoundHandler) {
////        this.bluetoothDeviceFoundHandlers.add(bluetoothDeviceFoundHandler);
////    }
//
////    /**
////     * method to initiate async task to retrieve reader settings
////     */
////    private void getReaderSettings() {
////
////        try {
////            if (isBluetoothEnabled()) {
////                if (isConnected()) {
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
////                                new RetrieveReaderSettingsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////                            else
////                                new RetrieveReaderSettingsTask().execute();
////                        }
////                    });
////                } else {
////                    Toast.makeText(getEPDataContext(), getResources().getString(R.string.error_disconnected), Toast.LENGTH_SHORT).show();
////                }
////            } else
////                Toast.makeText(getEPDataContext(), getResources().getString(R.string.error_bluetooth_disabled), Toast.LENGTH_SHORT).show();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////    }
//
////    /**
////     * method to send connect command request to reader
////     * after connect button clicked on connect password dialog
////     *
////     * @param password - reader password
////     */
////    public void connectClicked(String password) {
////        EPData.mConnectedDevice.setPassword(password);
////        if (isBluetoothEnabled()) {
////            if (isConnected()) {
////                Command_Connect command_connect = new Command_Connect();
////                command_connect.setpassword(password);
////                sendCommand(command_connect);
////            } else
////                Toast.makeText(getEPDataContext(), getResources().getString(R.string.error_disconnected), Toast.LENGTH_SHORT).show();
////        } else
////            Toast.makeText(getEPDataContext(), getResources().getString(R.string.error_bluetooth_disabled), Toast.LENGTH_SHORT).show();
////    }
////
////    /**
////     * method which will exe cute after cancel button clicked on connect pwd dialog
////     */
////    public void cancelClicked() {
////        disconnectFromBluetoothDevice();
////    }
//
////    /**
////     * method which is used send retrieve the reader default settings
////     */
////    public void getReaderDefaultSetting() {
////
////        Command_ProtocolConfig command_protocolConfig = new Command_ProtocolConfig();
////        command_protocolConfig.setEchoOff(true);
////        command_protocolConfig.setIncOperEndSummaryNotify(true);
////        command_protocolConfig.setIncStartOperationNotify(true);
////        command_protocolConfig.setIncStopOperationNotify(true);
////        command_protocolConfig.setInctriggereventnotify(true);
////        command_protocolConfig.setIncBatteryEventNotify(true);
////        sendCommand(command_protocolConfig);
////        sendCommand(new Command_GetVersion());
////        sendCommand(new Command_GetCapabilities());
////        sendCommand(new Command_GetSupportedLinkprofiles());
////
////        Command_GetAttrInfo command_getAttrInfo = new Command_GetAttrInfo();
////        command_getAttrInfo.setattnum(Constants.BEEPER_ATTR_NUM);
////        sendCommand(command_getAttrInfo);
////
////        command_getAttrInfo = new Command_GetAttrInfo();
////        command_getAttrInfo.setattnum(Constants.BATCH_MODE_ATTR_NUM);
////        sendCommand(command_getAttrInfo);
////
////        sendCommand(COMMAND_TYPE.COMMAND_SETREPORTCONFIG, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETSELECTRECORDS, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETANTENNACONFIGURATION, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETQUERYPARAMS, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETSTARTTRIGGER, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETSTOPTRIGGER, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETDYNAMICPOWER, CONFIG_TYPE.CURRENT);
//////        isReaderDefaultSettingsReceived = true;
////    }
//
//    /**
//     * method to clear reader's settings on disconnection
//     */
//    protected void clearSettings() {
////        EPData.antennaLinkProfileIndex = -1;
////        if (EPData.linkedProfiles != null)
////            EPData.linkedProfiles.clear();
////        EPData.antennaPowerLevel = "";
//////        EPData.tari = "";
////        if (EPData.capabilities != null)
////            EPData.capabilities.clear();
////        EPData.dynamicPowerSettings = null;
////        EPData.setQueryParamsSettings = null;
////        EPData.settings_startTrigger = "";
////        EPData.settings_stopTrigger = "";
////        EPData.setStartTriggerSettings = null;
////        EPData.setStopTriggerSettings = null;
////        EPData.reportConfigSettings = null;
////        EPData.regulatorySettings = null;
////        if (EPData.supportedRegions != null)
////            EPData.supportedRegions.clear();
////        EPData.beeperVolume = -1;
////        EPData.preFilters = null;
////        if (EPData.versionInfo != null)
////            EPData.versionInfo.clear();
////        EPData.isBatchModeInventoryRunning = null;
////        EPData.batteryEvent = null;
//    }
//
////    public static Timer t;
//
////    /**
////     * method to start a timer task to get device battery status per every 6 sec
////     */
////    protected void startTimer() {
////        if (t == null) {
////            TimerTask task = new TimerTask() {
////                @Override
////                public void run() {
////                    try {
////                        Command_GetDeviceInfo deviceInfo = new Command_GetDeviceInfo();
////                        deviceInfo.setbattery(true);
////                        sendCommand(deviceInfo);
////                    } catch (Exception e) {
////                        stopTimer();
////                    }
////                }
////            };
////            t = new Timer();
////            t.scheduleAtFixedRate(task, 0, 60000);
////        }
////    }
//
////    /**
////     * method to stop timer
////     */
////    protected void stopTimer() {
////        if (t != null) {
////            t.cancel();
////            t.purge();
////        }
////        t = null;
////    }
////
////    /**
////     * Interface to be implemented by the Fragments to handle the tag Response
////     */
////    public interface ResponseTagHandler {
////        /**
////         * method to handle tag data
////         *
////         * @param inventoryListItem - tag data
////         * @param isAddedToList     - true if tag is added to the list.
////         *                          - false if tag is updated in the list.
////         */
////        void handleTagResponse(InventoryListItem inventoryListItem, boolean isAddedToList);
////    }
////
////    /**
////     * Interface to be implemented by the Fragments to handle the status Response
////     */
////    public interface ResponseStatusHandler {
////        /**
////         * method to handle status response received from reader
////         *
////         * @param statusData - status of the requested operation
////         */
////        void handleStatusResponse(Response_Status statusData);
////    }
////
////    /**
////     * Interface to be implemented by the Fragments to handle the regulatory config Response
////     */
////    public interface ResponseRegulatoryConfigHandler {
////        /**
////         * method to handle regulatory response
////         *
////         * @param regulatoryConfig - regulatory settings of the reader
////         */
////        void handleRegulatoryConfigResponse(Response_RegulatoryConfig regulatoryConfig);
////    }
//
//    /**
//     * Interface to be implemented by fragments to be notified when a new device is discovered via bluetooth
//     */
//    public interface BluetoothDeviceFoundHandler {
//        /**
//         * This method will be called when reader device got paired.
//         *
//         * @param device - paired reader device
//         */
//        void bluetoothDevicePaired(ReaderDevice device);
//
//        /**
//         * This method will be called when reader device got connected.
//         *
//         * @param device - connected reader device
//         */
//        void bluetoothDeviceConnected(ReaderDevice device);
//
//        /**
//         * This method will be called when reader device got disconnected.
//         *
//         * @param device - disconnected reader device
//         */
//        void bluetoothDeviceDisConnected(ReaderDevice device);
//
//        /**
//         * This method will be called when connection process with reader device got failed.
//         *
//         * @param device - reader device
//         */
//        void bluetoothDeviceConnFailed(ReaderDevice device);
//
//        /**
//         * This method will be called when reader device got unpaired.
//         *
//         * @param device - unpaired reader device
//         */
//        void bluetoothDeviceUnPaired(ReaderDevice device);
//    }
//
////    /**
////     * Interface to be implemented by the Fragments to handle proximity details of the locationing tag
////     */
////    public interface ResponseLocateTagHandler {
////        /**
////         * method to handle locate tag response
////         */
////        void handleLocateTagResponse();
////    }
////
////    /**
////     * Interface to be implemented by the Fragments to handle access operation response
////     */
////    public interface AccessOperationResponseTagHandler {
////        /**
////         * method to handle access operation tag response
////         *
////         * @param tagData access operation tag data
////         */
////        void handleTagResponse(Response_TagData tagData);
////    }
//
////    /**
////     * Interface to be implemented by the Fragments to handle physical trigger events of the reader
////     */
////    public interface TriggerEventHandler {
////        /**
////         * method to handle trigger press event
////         */
////        void triggerPressEventRecieved();
////
////        /**
////         * method to handle trigger release event
////         */
////        void triggerReleaseEventRecieved();
////    }
//
//    /**
//     * Interface to be implemented by the Fragments to handle battery notifications
////     */
////    public interface BatteryNotificationHandler {
////        /**
////         * method to handle device battery status data
////         *
////         * @param level    - battery level
////         * @param charging - specifies whether device is charging or not
////         * @param cause    - reason for receiving battery notification
////         */
////        void deviceStatusReceived(int level, boolean charging, String cause);
////    }
//
////    /**
////     * Interface to be implemented by the Fragments to handle batch mode events
////     */
////    public interface BatchModeEventHandler {
////        /**
////         * method to handle batch mode event
////         */
////        void batchModeEventReceived();
////    }
//
////    /**
////     * Async Task, which will handle tag data response from reader. This task is used to check whether tag is in inventory list or not.
////     * If tag is not in the list then it will add the tag data to inventory list. If tag is there in inventory list then it will update the tag details in inventory list.
////     */
////    private class ResponseHandlerTask extends AsyncTask<Void, Void, Boolean> {
////
////        private Response_TagData response_tagData;
////        private InventoryListItem inventoryItem;
////        private InventoryListItem oldObject;
////        private String memoryBank;
////        private String memoryBankData;
////
////        ResponseHandlerTask(Response_TagData response_tagData) {
////            this.response_tagData = response_tagData;
////        }
////
////        @Override
////        protected Boolean doInBackground(Void... voids) {
////            boolean added = false;
////            try {
////                if (EPData.inventoryList.containsKey(response_tagData.EPCId)) {
////                    inventoryItem = new InventoryListItem(response_tagData.EPCId, 1, null, null,null, null, null, null);
////                    int index = EPData.inventoryList.get(response_tagData.EPCId);
////                    if (index >= 0) {
////                        EPData.TOTAL_TAGS++;
////                        //Tag is already present. Update the fields and increment the count
////                        if (response_tagData.tagAcessOprations != null)
////                            for (AcessOperation acessOperation : response_tagData.tagAcessOprations) {
////                                if (acessOperation.opration.equalsIgnoreCase("read")) {
////                                    memoryBank = acessOperation.memoryBank;
////                                    memoryBankData = acessOperation.memoryBankData;
////                                }
////                            }
////
////                        oldObject = EPData.tagsReadInventory.get(index);
////                        oldObject.incrementCount();
////                        if (oldObject.getMemoryBankData() != null && !oldObject.getMemoryBankData().equalsIgnoreCase(memoryBankData))
////                            oldObject.setMemoryBankData(memoryBankData);
////                        //oldObject.setEPCId(inventoryItem.getEPCId());
////
////                       oldObject.setPC(response_tagData.PC);
////                       oldObject.setPhase(response_tagData.Phase);
////                       oldObject.setChannelIndex(response_tagData.ChannelIndex);
////                       oldObject.setRSSI(response_tagData.RSSI);
////                    }
////                } else {
////                    //Tag is encountered for the first time. Add it.
////                    if (EPData.inventoryMode == 0 || (EPData.inventoryMode == 1 && EPData.UNIQUE_TAGS <= Constants.UNIQUE_TAG_LIMIT)) {
////                        int tagSeenCount = 0;
////                        if(response_tagData.TagSeenCount != null)
////                            tagSeenCount = Integer.parseInt(response_tagData.TagSeenCount);
////                        if (tagSeenCount != 0) {
////                            EPData.TOTAL_TAGS += tagSeenCount;
////                            inventoryItem = new InventoryListItem(response_tagData.EPCId, tagSeenCount,  null, null,null, null, null, null);
////                        } else {
////                            EPData.TOTAL_TAGS++;
////                            inventoryItem = new InventoryListItem(response_tagData.EPCId, 1, null, null,  null, null,null, null);
////                        }
////                        added = EPData.tagsReadInventory.add(inventoryItem);
////                        if (added) {
////                            EPData.inventoryList.put(response_tagData.EPCId, EPData.UNIQUE_TAGS);
////                            if (response_tagData.tagAcessOprations != null)
////                                for (AcessOperation acessOperation : response_tagData.tagAcessOprations) {
////                                    if (acessOperation.opration.equalsIgnoreCase("read")) {
////                                        memoryBank = acessOperation.memoryBank;
////                                        memoryBankData = acessOperation.memoryBankData;
////                                    }
////                                }
////                            oldObject = EPData.tagsReadInventory.get(EPData.UNIQUE_TAGS);
////                            oldObject.setMemoryBankData(memoryBankData);
////                            oldObject.setMemoryBank(memoryBank);
////                            oldObject.setPC(response_tagData.PC);
////                            oldObject.setPhase(response_tagData.Phase);
////                            oldObject.setChannelIndex(response_tagData.ChannelIndex);
////                            oldObject.setRSSI(response_tagData.RSSI);
////                            EPData.UNIQUE_TAGS++;
////                        }
////                    }
////
////                }
////            } catch (IndexOutOfBoundsException e) {
////                oldObject = null;
////                added = false;
////            } catch (Exception e) {
////                oldObject = null;
////                added = false;
////            }
////            response_tagData = null;
////            inventoryItem = null;
////            memoryBank = null;
////            memoryBankData = null;
////            return added;
////        }
////
////        @Override
////        protected void onPostExecute(Boolean result) {
////            cancel(true);
////            if (oldObject != null && fragment instanceof ResponseTagHandler)
////                ((ResponseTagHandler) fragment).handleTagResponse(oldObject, result);
////            oldObject = null;
////        }
////
////    }
//
//
//    private void defaultSetting(){
//
//        Command_ProtocolConfig command_protocolConfig = new Command_ProtocolConfig();
//        command_protocolConfig.setInctriggereventnotify(true);
//        sendCommand(command_protocolConfig);
////        sendCommand(new Command_GetVersion());
////        sendCommand(new Command_GetCapabilities());
////        sendCommand(new Command_GetSupportedLinkprofiles());
//
////        Command_GetAttrInfo command_getAttrInfo = new Command_GetAttrInfo();
////        command_getAttrInfo.setattnum(Constants.BEEPER_ATTR_NUM);
////        sendCommand(command_getAttrInfo);
//
////        command_getAttrInfo = new Command_GetAttrInfo();
////        command_getAttrInfo.setattnum(Constants.BATCH_MODE_ATTR_NUM);
////        sendCommand(command_getAttrInfo);
//
////        sendCommand(COMMAND_TYPE.COMMAND_SETREPORTCONFIG, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETSELECTRECORDS, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETANTENNACONFIGURATION, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETQUERYPARAMS, CONFIG_TYPE.CURRENT);
//        sendCommand(COMMAND_TYPE.COMMAND_SETSTARTTRIGGER, CONFIG_TYPE.CURRENT);
//        sendCommand(COMMAND_TYPE.COMMAND_SETSTOPTRIGGER, CONFIG_TYPE.CURRENT);
////        sendCommand(COMMAND_TYPE.COMMAND_SETDYNAMICPOWER, CONFIG_TYPE.CURRENT);
//    }
//
//    /**
//     * 设置声音
//     * @param voiceIndex
//     */
//    public void setVoice(int voiceIndex){
//        Command_SetAttr command_setAttr = new Command_SetAttr();
//        command_setAttr.setattnum(Constants.BEEPER_ATTR_NUM);
//        command_setAttr.setatttype("B");
//        command_setAttr.setattvalue(voiceIndex);
//        sendCommand(command_setAttr);
//
//        Command_GetAttrInfo command_getAttrInfo = new Command_GetAttrInfo();
//        command_getAttrInfo.setattnum(Constants.BEEPER_ATTR_NUM);
//        sendCommand(command_getAttrInfo);
//    }
//
//    /**
//     * 设置信号等级
//     * @param powerLevel
//     */
//    public void setPowerLevel(short powerLevel) {
//        Command_SetAntennaConfiguration antennaCommand = new Command_SetAntennaConfiguration();
//        antennaCommand.setPower(powerLevel);
//
//        //Send the command
//        sendCommand(antennaCommand);
//        sendCommand(COMMAND_TYPE.COMMAND_SETANTENNACONFIGURATION, CONFIG_TYPE.CURRENT);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(mReceiver);
//    }
//}