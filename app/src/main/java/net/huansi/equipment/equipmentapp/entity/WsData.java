package net.huansi.equipment.equipmentapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quanm on 2015-11-17.
 */
public class WsData extends WsEntity implements Serializable {
    public String SSTATUS;
    public String SMESSAGE;
    public List<WsEntity> LISTWSDATA;

    public WsData() {
        this.LISTWSDATA = new ArrayList<>();
    }

}
