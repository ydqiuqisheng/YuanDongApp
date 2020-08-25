package net.huansi.equipment.equipmentapp.sqlite_db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by 单中年 on 2017/2/24.
 * 盘点的记录
 */
@Entity
@SuppressWarnings("serial")
public class InventoryDetail  implements Serializable{
    @Id(autoincrement = true)
    private Long id;
    private long inventoryHdrIdInSQLite;//SQLite中盘点主表的Id
    private String area;//区域

    private String inventoryParentId;//设备盘点的头表(服务器)
    private String equipmentParentId;//设备主表的主键
    private String equipmentChildId;//设备从表的主键
//    private String equipmentCode;//EPCode
//    private String equipmentName;//设备名称
//    private String assetsCode;//资产编号
//    private String outFactoryCode;//出厂编号
//    private String costCenter;//成本中心
//    private String factory;//所在工厂
//    private String departmentCHTName;//使用单位中文名
//    private String departmentEnglishName;//使用单位英文名

    private String inFactoryDate;//进厂日期
    private String assetsCode;//资产编号
    private String outFactoryCode;//出厂编号
    private String depreciationStartingDate;//折旧日期
    private String costCenter;//成本中心
    private String declarationNum;//报关单号
    private String EPCode;//RFID

    private String factory;
    private String equipmentName;

    private int status=-2;//-2 未盘-1盘亏 0正常 1盘盈
    private String scanTime;//扫描时间
    @Generated(hash = 514004275)
    public InventoryDetail(Long id, long inventoryHdrIdInSQLite, String area,
            String inventoryParentId, String equipmentParentId,
            String equipmentChildId, String inFactoryDate, String assetsCode,
            String outFactoryCode, String depreciationStartingDate,
            String costCenter, String declarationNum, String EPCode, String factory,
            String equipmentName, int status, String scanTime) {
        this.id = id;
        this.inventoryHdrIdInSQLite = inventoryHdrIdInSQLite;
        this.area = area;
        this.inventoryParentId = inventoryParentId;
        this.equipmentParentId = equipmentParentId;
        this.equipmentChildId = equipmentChildId;
        this.inFactoryDate = inFactoryDate;
        this.assetsCode = assetsCode;
        this.outFactoryCode = outFactoryCode;
        this.depreciationStartingDate = depreciationStartingDate;
        this.costCenter = costCenter;
        this.declarationNum = declarationNum;
        this.EPCode = EPCode;
        this.factory = factory;
        this.equipmentName = equipmentName;
        this.status = status;
        this.scanTime = scanTime;
    }
    @Generated(hash = 516932111)
    public InventoryDetail() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getInventoryHdrIdInSQLite() {
        return this.inventoryHdrIdInSQLite;
    }
    public void setInventoryHdrIdInSQLite(long inventoryHdrIdInSQLite) {
        this.inventoryHdrIdInSQLite = inventoryHdrIdInSQLite;
    }
    public String getArea() {
        return this.area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getInventoryParentId() {
        return this.inventoryParentId;
    }
    public void setInventoryParentId(String inventoryParentId) {
        this.inventoryParentId = inventoryParentId;
    }
    public String getEquipmentParentId() {
        return this.equipmentParentId;
    }
    public void setEquipmentParentId(String equipmentParentId) {
        this.equipmentParentId = equipmentParentId;
    }
    public String getEquipmentChildId() {
        return this.equipmentChildId;
    }
    public void setEquipmentChildId(String equipmentChildId) {
        this.equipmentChildId = equipmentChildId;
    }
    public String getInFactoryDate() {
        return this.inFactoryDate;
    }
    public void setInFactoryDate(String inFactoryDate) {
        this.inFactoryDate = inFactoryDate;
    }
    public String getAssetsCode() {
        return this.assetsCode;
    }
    public void setAssetsCode(String assetsCode) {
        this.assetsCode = assetsCode;
    }
    public String getOutFactoryCode() {
        return this.outFactoryCode;
    }
    public void setOutFactoryCode(String outFactoryCode) {
        this.outFactoryCode = outFactoryCode;
    }
    public String getDepreciationStartingDate() {
        return this.depreciationStartingDate;
    }
    public void setDepreciationStartingDate(String depreciationStartingDate) {
        this.depreciationStartingDate = depreciationStartingDate;
    }
    public String getCostCenter() {
        return this.costCenter;
    }
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }
    public String getDeclarationNum() {
        return this.declarationNum;
    }
    public void setDeclarationNum(String declarationNum) {
        this.declarationNum = declarationNum;
    }
    public String getEPCode() {
        return this.EPCode;
    }
    public void setEPCode(String EPCode) {
        this.EPCode = EPCode;
    }
    public String getFactory() {
        return this.factory;
    }
    public void setFactory(String factory) {
        this.factory = factory;
    }
    public String getEquipmentName() {
        return this.equipmentName;
    }
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getScanTime() {
        return this.scanTime;
    }
    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

}
