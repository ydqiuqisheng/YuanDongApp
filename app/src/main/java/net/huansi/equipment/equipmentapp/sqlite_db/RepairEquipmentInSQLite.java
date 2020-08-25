package net.huansi.equipment.equipmentapp.sqlite_db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by shanz on 2017/7/5.
 */
@Entity
public class RepairEquipmentInSQLite {
    @Id(autoincrement = true)
    private Long id;
    private String equipmentDetailID;
    private String factory;
    private String assetsCode;
    private String epcCode;
    private String costCenter;
    private String outFactoryCode;
    private String equipmentName;
    private String model;
    @Generated(hash = 258097214)
    public RepairEquipmentInSQLite(Long id, String equipmentDetailID,
            String factory, String assetsCode, String epcCode, String costCenter,
            String outFactoryCode, String equipmentName, String model) {
        this.id = id;
        this.equipmentDetailID = equipmentDetailID;
        this.factory = factory;
        this.assetsCode = assetsCode;
        this.epcCode = epcCode;
        this.costCenter = costCenter;
        this.outFactoryCode = outFactoryCode;
        this.equipmentName = equipmentName;
        this.model = model;
    }
    @Generated(hash = 1208936083)
    public RepairEquipmentInSQLite() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEquipmentDetailID() {
        return this.equipmentDetailID;
    }
    public void setEquipmentDetailID(String equipmentDetailID) {
        this.equipmentDetailID = equipmentDetailID;
    }
    public String getFactory() {
        return this.factory;
    }
    public void setFactory(String factory) {
        this.factory = factory;
    }
    public String getAssetsCode() {
        return this.assetsCode;
    }
    public void setAssetsCode(String assetsCode) {
        this.assetsCode = assetsCode;
    }
    public String getEpcCode() {
        return this.epcCode;
    }
    public void setEpcCode(String epcCode) {
        this.epcCode = epcCode;
    }
    public String getCostCenter() {
        return this.costCenter;
    }
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }
    public String getOutFactoryCode() {
        return this.outFactoryCode;
    }
    public void setOutFactoryCode(String outFactoryCode) {
        this.outFactoryCode = outFactoryCode;
    }
    public String getEquipmentName() {
        return this.equipmentName;
    }
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
    public String getModel() {
        return this.model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    
   /* b.Factory,
    b.AssetsCode,
    b.EPCCode,
    b.CostCenter,
    b.OutFactoryCode,
    b.ItemID as EquipmentDetailID,--设备ID
    c.EquipmentName,
    c.Model*/
}
