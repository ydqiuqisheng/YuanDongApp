package net.huansi.equipment.equipmentapp.imp;


import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.OthersUtil;

/**
 * Created by 单中年 on 2017/1/16.
 */

public abstract class SimpleHsWeb implements WebListener {

    @Override
    public void error(HsWebInfo hsWebInfo) {
    }
}
