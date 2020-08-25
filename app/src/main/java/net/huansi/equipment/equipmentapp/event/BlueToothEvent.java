package net.huansi.equipment.equipmentapp.event;

import android.bluetooth.BluetoothDevice;

/**
 * Created by shanz on 2017/4/26.
 */

public class BlueToothEvent {
    public static final int DEVICE_CONNECTED_INDEX=101;//已连接
    public static final int DEVICE_UNCONNECTED_INDEX=102;//未连接
    public int index;
    public BluetoothDevice device;

    public BlueToothEvent(int index,BluetoothDevice device) {
        this.index = index;
        this.device=device;
    }
}
