package net.huansi.equipment.equipmentapp.event;

import net.huansi.equipment.equipmentapp.entity.EpWithRFID;

/**
 * Created by shanz on 2017/4/27.
 */

public class RFIDActionEvent {
    public static final int DELETE_INDEX=1;//服务器删除了RFID
    public int index;
    public Object object;

    public RFIDActionEvent(int index,Object object) {
        this.index = index;
        this.object=object;
    }
}
