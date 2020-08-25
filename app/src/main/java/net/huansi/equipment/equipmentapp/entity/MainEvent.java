package net.huansi.equipment.equipmentapp.entity;

/**
 * Created by 单中年 on 2017/2/28.
 */

public class MainEvent {
    public Class aClass;
    public Object object;
    public int index;

    public MainEvent(Class aClass, Object object) {
        this(aClass,object,0);
    }
    public MainEvent(Class aClass, Object object,int index) {
        this.aClass = aClass;
        this.object = object;
        this.index=index;
    }
}
