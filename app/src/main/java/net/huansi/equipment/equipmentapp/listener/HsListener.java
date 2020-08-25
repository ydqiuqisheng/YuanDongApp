package net.huansi.equipment.equipmentapp.listener;

/**
 * Created by 单中年 on 2017/2/17.
 */

public interface HsListener<SUC,ERR> {
    void success(SUC t);
    void error(ERR err);
}
