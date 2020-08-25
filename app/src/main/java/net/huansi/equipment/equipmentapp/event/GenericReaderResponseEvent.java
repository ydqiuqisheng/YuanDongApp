package net.huansi.equipment.equipmentapp.event;

import com.motorolasolutions.ASCII_SDK.IMsg;

/**
 * Created by shanz on 2017/4/27.
 */

public class GenericReaderResponseEvent {
    public static final  int RECEIVED_DATA_INDEX=-1;
    public static final int CONFIGURATIONS_INDEX=0;
    public static final int METADATA_INDEX=1;
    public static final int RESPONSE_DATA_INDEX=2;
    public static final int NOTIFICATION_INDEX=3;

    public int index;
    public IMsg msg;
    public String data;
    public Class aClass;


    public GenericReaderResponseEvent(int index, IMsg msg,Class aClass) {
        this.index = index;
        this.msg = msg;
        this.aClass=aClass;
    }

    public GenericReaderResponseEvent(int index, String data) {
        this.index = index;
        this.data = data;
    }
}
