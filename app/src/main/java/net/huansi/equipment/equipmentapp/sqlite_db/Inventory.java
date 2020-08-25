package net.huansi.equipment.equipmentapp.sqlite_db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by 单中年 on 2017/2/26.
 */
@Entity
@SuppressWarnings("serial")
public class Inventory implements Serializable{
    @Id(autoincrement = true)
    private Long id;//通过主表ID查询其他（工厂，创建者...）
    private String factory;//工厂
    private String createUserNo;//创建者
    private String inventoryUserNo;//上次盘点者
    private String createTime;//创建时间

    @Generated(hash = 82084513)
    public Inventory(Long id, String factory, String createUserNo,
            String inventoryUserNo, String createTime) {
        this.id = id;
        this.factory = factory;
        this.createUserNo = createUserNo;
        this.inventoryUserNo = inventoryUserNo;
        this.createTime = createTime;
    }
    @Generated(hash = 375708430)
    public Inventory() {
    }
   
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFactory() {
        return this.factory;
    }
    public void setFactory(String factory) {
        this.factory = factory;
    }
    public String getCreateUserNo() {
        return this.createUserNo;
    }
    public void setCreateUserNo(String createUserNo) {
        this.createUserNo = createUserNo;
    }
    public String getInventoryUserNo() {
        return this.inventoryUserNo;
    }
    public void setInventoryUserNo(String inventoryUserNo) {
        this.inventoryUserNo = inventoryUserNo;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
