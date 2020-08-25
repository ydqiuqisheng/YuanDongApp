package net.huansi.equipment.equipmentapp.imp;

import net.huansi.equipment.equipmentapp.listener.HsListener;

/**
 * Created by 单中年 on 2017/2/17.
 */

public class SimpleHs<SUC,ERR> implements HsListener<SUC,ERR> {
    @Override
    public void success(SUC t) {

    }

    @Override
    public void error(ERR err) {

    }
}
