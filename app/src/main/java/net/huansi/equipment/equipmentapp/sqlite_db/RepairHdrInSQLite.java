package net.huansi.equipment.equipmentapp.sqlite_db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by 单中年 on 2017/3/1.
 */
@Entity
@SuppressWarnings("serial")
public class RepairHdrInSQLite implements Serializable {
    @Id(autoincrement = true)
    private Long id;

    private String repairPlanHdrId;//服务器维修主表的Id
    private String equipmentId;//设备的id
    private String equipmentName;//设备名称
    private String costCenter;//成本中心
    private String EPCode;//设备编号
    private String outOfCode;//出厂编号
    private String equipmentModel;//设备型号
    private String assetsCode;//资产编号
    private String remark;//备注
    private Long createTime;
    private Long repairTime;
    private String callRepairItemId;//叫修主键
    private String callRepairRecord;//叫修记录
    private String callRepairGroup;//叫修组别
    private String callRepairGroupId;//组别ID
    private int submitStatus;//是否已经上传了  0=未上传 1已上传
    private String pathList="";
    @Generated(hash = 1016024142)
    public RepairHdrInSQLite(Long id, String repairPlanHdrId, String equipmentId,
            String equipmentName, String costCenter, String EPCode,
            String outOfCode, String equipmentModel, String assetsCode,
            String remark, Long createTime, Long repairTime,
            String callRepairItemId, String callRepairRecord,
            String callRepairGroup, String callRepairGroupId, int submitStatus,
            String pathList) {
        this.id = id;
        this.repairPlanHdrId = repairPlanHdrId;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.costCenter = costCenter;
        this.EPCode = EPCode;
        this.outOfCode = outOfCode;
        this.equipmentModel = equipmentModel;
        this.assetsCode = assetsCode;
        this.remark = remark;
        this.createTime = createTime;
        this.repairTime = repairTime;
        this.callRepairItemId = callRepairItemId;
        this.callRepairRecord = callRepairRecord;
        this.callRepairGroup = callRepairGroup;
        this.callRepairGroupId = callRepairGroupId;
        this.submitStatus = submitStatus;
        this.pathList = pathList;
    }
    @Generated(hash = 1796893552)
    public RepairHdrInSQLite() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRepairPlanHdrId() {
        return this.repairPlanHdrId;
    }
    public void setRepairPlanHdrId(String repairPlanHdrId) {
        this.repairPlanHdrId = repairPlanHdrId;
    }
    public String getEquipmentId() {
        return this.equipmentId;
    }
    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }
    public String getEquipmentName() {
        return this.equipmentName;
    }
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
    public String getCostCenter() {
        return this.costCenter;
    }
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }
    public String getEPCode() {
        return this.EPCode;
    }
    public void setEPCode(String EPCode) {
        this.EPCode = EPCode;
    }
    public String getOutOfCode() {
        return this.outOfCode;
    }
    public void setOutOfCode(String outOfCode) {
        this.outOfCode = outOfCode;
    }
    public String getEquipmentModel() {
        return this.equipmentModel;
    }
    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }
    public String getAssetsCode() {
        return this.assetsCode;
    }
    public void setAssetsCode(String assetsCode) {
        this.assetsCode = assetsCode;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public int getSubmitStatus() {
        return this.submitStatus;
    }
    public void setSubmitStatus(int submitStatus) {
        this.submitStatus = submitStatus;
    }
    public String getPathList() {
        return this.pathList;
    }
    public void setPathList(String pathList) {
        this.pathList = pathList;
    }
    public Long getRepairTime() {
        return this.repairTime;
    }
    public void setRepairTime(Long repairTime) {
        this.repairTime = repairTime;
    }
    public String getCallRepairItemId() {
        return this.callRepairItemId;
    }
    public void setCallRepairItemId(String callRepairItemId) {
        this.callRepairItemId = callRepairItemId;
    }
    public String getCallRepairRecord() {
        return this.callRepairRecord;
    }
    public void setCallRepairRecord(String callRepairRecord) {
        this.callRepairRecord = callRepairRecord;
    }
    public String getCallRepairGroup() {
        return this.callRepairGroup;
    }
    public void setCallRepairGroup(String callRepairGroup) {
        this.callRepairGroup = callRepairGroup;
    }
    public String getCallRepairGroupId() {
        return this.callRepairGroupId;
    }
    public void setCallRepairGroupId(String callRepairGroupId) {
        this.callRepairGroupId = callRepairGroupId;
    }
    
  
}
