package net.huansi.equipment.equipmentapp.sqlite_db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by shanz on 2017/6/27.
 * 无网情况下的校验的数据
 */
@Entity
public class CheckDataInSQLite {
    @Id(autoincrement = true)
    private Long id;
    private String equipmentDetailID;

    private String inFactoryDate;//进厂日期
    private String assetsCode;//资产编号
    private String outFactoryCode;//出厂编号
    private String depreciationStartingDate;//折旧日期
    private String costCenter;//成本中心
    private String declarationNum;//报关单号

    private String equipmentName;
    private String brand;
    private String model;
    private String RFID;

    private String userNo;
    private int status=0;//0原状 1修改 2删除
    @Generated(hash = 1815149125)
    public CheckDataInSQLite(Long id, String equipmentDetailID,
            String inFactoryDate, String assetsCode, String outFactoryCode,
            String depreciationStartingDate, String costCenter,
            String declarationNum, String equipmentName, String brand, String model,
            String RFID, String userNo, int status) {
        this.id = id;
        this.equipmentDetailID = equipmentDetailID;
        this.inFactoryDate = inFactoryDate;
        this.assetsCode = assetsCode;
        this.outFactoryCode = outFactoryCode;
        this.depreciationStartingDate = depreciationStartingDate;
        this.costCenter = costCenter;
        this.declarationNum = declarationNum;
        this.equipmentName = equipmentName;
        this.brand = brand;
        this.model = model;
        this.RFID = RFID;
        this.userNo = userNo;
        this.status = status;
    }
    @Generated(hash = 1745950688)
    public CheckDataInSQLite() {
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
    public String getEquipmentName() {
        return this.equipmentName;
    }
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
    public String getBrand() {
        return this.brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return this.model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getRFID() {
        return this.RFID;
    }
    public void setRFID(String RFID) {
        this.RFID = RFID;
    }
    public String getUserNo() {
        return this.userNo;
    }
    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
}
