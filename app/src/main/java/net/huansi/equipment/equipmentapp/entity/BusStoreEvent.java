package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class BusStoreEvent {
    public enum Type {
        /**
         * 获取汇总
         */
        SUM_SUCCESS,
        /**
         * 获取明细
         */
        DETAIL_SUCCESS,
    }

    public Type type;
    public List data;

    public BusStoreEvent(Type type, List data) {
        this.type = type;
        this.data = data;
    }
}
