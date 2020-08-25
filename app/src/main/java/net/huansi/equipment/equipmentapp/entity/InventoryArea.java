package net.huansi.equipment.equipmentapp.entity;

import java.io.Serializable;

/**
 * Created by shanz on 2017/7/6.
 */

public class InventoryArea implements Serializable{
    public String name;//名称
    public int already;//已盘
    public int overage;//盘盈

    public InventoryArea(String name, int already, int overage) {
        this.name = name;
        this.already = already;
        this.overage = overage;
    }
    public InventoryArea(){}
}
