package net.huansi.equipment.equipmentapp.receiver;//package net.huansi.equipment.equipmentapp.receiver;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import net.huansi.equipment.equipmentapp.entity.ReaderDevice;
//import net.huansi.equipment.equipmentapp.event.DeviceEvent;
//import net.huansi.equipment.equipmentapp.util.EPData;
//import net.huansi.equipment.equipmentapp.util.OthersUtil;
//
//import org.greenrobot.eventbus.EventBus;
//
//import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;
//import static android.bluetooth.BluetoothAdapter.STATE_OFF;
//import static android.bluetooth.BluetoothAdapter.STATE_ON;
//import static android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED;
//import static android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED;
//import static android.bluetooth.BluetoothDevice.BOND_BONDED;
//import static android.bluetooth.BluetoothDevice.BOND_NONE;
//
///**
// * Created by 单中年 on 2017/2/27.
// */
//
//public class EPReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        switch (action) {
//            //蓝牙状态改变的时候
//            case ACTION_STATE_CHANGED:
//                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
//                        BluetoothAdapter.ERROR);
//                changeByBlueState(state);
//                break;
//            //蓝牙连接设备状态的改变
//            case ACTION_BOND_STATE_CHANGED:
//                changeByDeviceBond(intent);
//                break;
//            //设备取消绑定
//            case ACTION_ACL_DISCONNECTED:
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (device == null || EPData.mConnectedDevice == null) return;
//                if (device.getAddress().equalsIgnoreCase(EPData.mConnectedDevice.getAddress())) {
//                    EPData.isDeviceDisconnected = true;
//                    EPData.mConnectedDevice=null;
//                }
//        }
//    }
//
//    /**
//     * 根据蓝牙连接状态改变静态值
//     */
//    private void changeByBlueState(int state){
//        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
//        EPData.getActiveDeceiveList().clear();
//        switch (state){
//            case STATE_ON:
//                EPData.isBlueRunning=true;
//                for(BluetoothDevice bluetoothDevice:bluetoothAdapter.getBondedDevices()){
//                    if(OthersUtil.isRFIDReader(bluetoothDevice)){
//                        EPData.getActiveDeceiveList().add(new ReaderDevice(bluetoothDevice,bluetoothDevice.getName(),bluetoothDevice.getAddress(),null,null,false));
//                    }
//                }
//                EventBus.getDefault().post(new DeviceEvent());
//                break;
//            case STATE_OFF:
//                EPData.mConnectedDevice=null;
//                EPData.isDeviceDisconnected=true;
//                EPData.isBlueRunning=false;
//                EventBus.getDefault().post(new DeviceEvent());
//                break;
//        }
//
//    }
//
//    /**
//     * 根据设备蓝牙连接的状态
//     */
//    private void changeByDeviceBond(Intent intent) {
//        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//        ReaderDevice readerDevice = new ReaderDevice(device, device.getName(), device.getAddress(), null, null, false);
//        int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
//        switch (bondState) {
//            case BOND_BONDED:
//                if (!OthersUtil.isRFIDReader(device)) return;
//                if (EPData.getActiveDeceiveList().contains(readerDevice)) return;
//                EPData.getActiveDeceiveList().add(readerDevice);
//                EventBus.getDefault().post(new DeviceEvent());
//                break;
//            case BOND_NONE:
//                if (!EPData.getActiveDeceiveList().contains(readerDevice)) return;
//                EPData.getActiveDeceiveList().remove(readerDevice);
//                if(EPData.mConnectedDevice!=null&&
//                        readerDevice.getAddress().equals(EPData.mConnectedDevice.getAddress())){
//                    EPData.mConnectedDevice=null;
//                    EPData.isDeviceDisconnected=true;
//                }
//                EventBus.getDefault().post(new DeviceEvent());
//                break;
//        }
//
//    }
//}
