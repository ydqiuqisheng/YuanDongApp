package net.huansi.equipment.equipmentapp.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import net.huansi.equipment.equipmentapp.entity.ReaderDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by 单中年 on 2017/2/27.
 */

public class EPData {
//    public static boolean isBlueRunning=false;//蓝牙连接状态
    public static ReaderDevice mConnectedDevice;//连接的设备
    private static List<ReaderDevice> activeDeceiveList;
//    public static boolean isDeviceDisconnected=true;
//    public static boolean AUTO_DETECT_READERS=false;
    //variable to keep track of the bluetoothService
//    public static BluetoothService bluetoothService;
//    public static GenericReader genericReader;
//    public static Handler handler;
//    public static Hashtable<String, String> capabilities = new Hashtable<>();
//    public static  boolean is_connection_requested;
//    public static volatile boolean AUTO_RECONNECT_READERS;
//    public static boolean mIsInventoryRunning;
//    public static boolean isAccessCriteriaRead;
    // reader settings
//    public static Command_SetRegulatory regulatorySettings;

    public synchronized static List<ReaderDevice>  getActiveDeceiveList(){
        if(activeDeceiveList==null) activeDeceiveList=new ArrayList<>();
        return activeDeceiveList;
    }
//    public static BluetoothService getBluetoothService(Context context,Handler handler){
//        if(bluetoothService==null){
//            bluetoothService=new BluetoothService(context,handler);
//        }
//        return bluetoothService;
//    }
//
//    public static GenericReader getGenericReader(GenericReader.GenericReaderResponseParsedListener listener, BluetoothService bluetoothService) {
//        if(genericReader==null) {
//            genericReader=new GenericReader();
//            genericReader.attachActivity(listener, bluetoothService);
//        }
//        return genericReader;
//    }

}
