package net.huansi.equipment.equipmentapp.event;

/**
 * Created by shanz on 2017/3/5.
 */

public class AdapterToActivityEvent {
    public final static int DELETE_INDEX=10001;//删除

    public Class adapterClass;
    public Class activityClass;
    public int position;
    public int subPosition;
    public int index;

    public AdapterToActivityEvent(Class adapterClass, Class activityClass, int position, int subPosition, int index) {
        this.adapterClass = adapterClass;
        this.activityClass = activityClass;
        this.position = position;
        this.subPosition = subPosition;
        this.index = index;
    }
}
