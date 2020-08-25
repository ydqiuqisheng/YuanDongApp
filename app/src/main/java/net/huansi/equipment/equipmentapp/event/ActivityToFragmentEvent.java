package net.huansi.equipment.equipmentapp.event;

/**
 * Created by 单中年 on 2017/2/24.
 */

public class ActivityToFragmentEvent {
    public Class activityClass;
    public Class fragmentClass;
    public Object o1;
    public Object o2;

    public ActivityToFragmentEvent(Class activityClass, Class fragmentClass, Object o1,Object o2) {
        this.activityClass = activityClass;
        this.fragmentClass = fragmentClass;
        this.o1 = o1;
        this.o2=o2;
    }
    public ActivityToFragmentEvent(Class activityClass, Class fragmentClass, Object o1) {
        this(activityClass,fragmentClass,o1,null);
    }
}
