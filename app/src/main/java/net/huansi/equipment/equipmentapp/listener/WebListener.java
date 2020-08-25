package net.huansi.equipment.equipmentapp.listener;


import net.huansi.equipment.equipmentapp.entity.HsWebInfo;

/**
 * Created by 单中年 on 2017/1/13.
 */

public interface WebListener {
    void success(HsWebInfo hsWebInfo);
    void error(HsWebInfo hsWebInfo);
}
