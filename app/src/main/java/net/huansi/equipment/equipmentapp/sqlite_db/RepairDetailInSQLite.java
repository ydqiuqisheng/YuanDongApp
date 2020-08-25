package net.huansi.equipment.equipmentapp.sqlite_db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by 单中年 on 2017/3/1.
 */
@Entity
@SuppressWarnings("serial")
public class RepairDetailInSQLite implements Serializable{
    @Id(autoincrement = true)
    private Long id;

    private long hdrId;//SQLite主表的id
    private String projectName;//维修项目
    private String projectID;
    private String method;//维修方法
    private String resultName;//维修结果
    private String resultID;//维修结果
    private String repairUserNo;//维修人员
    private String remark="";
    @Generated(hash = 36960459)
    public RepairDetailInSQLite(Long id, long hdrId, String projectName,
            String projectID, String method, String resultName, String resultID,
            String repairUserNo, String remark) {
        this.id = id;
        this.hdrId = hdrId;
        this.projectName = projectName;
        this.projectID = projectID;
        this.method = method;
        this.resultName = resultName;
        this.resultID = resultID;
        this.repairUserNo = repairUserNo;
        this.remark = remark;
    }
    @Generated(hash = 1810806260)
    public RepairDetailInSQLite() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getHdrId() {
        return this.hdrId;
    }
    public void setHdrId(long hdrId) {
        this.hdrId = hdrId;
    }
    public String getProjectName() {
        return this.projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectID() {
        return this.projectID;
    }
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
    public String getMethod() {
        return this.method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getResultName() {
        return this.resultName;
    }
    public void setResultName(String resultName) {
        this.resultName = resultName;
    }
    public String getResultID() {
        return this.resultID;
    }
    public void setResultID(String resultID) {
        this.resultID = resultID;
    }
    public String getRepairUserNo() {
        return this.repairUserNo;
    }
    public void setRepairUserNo(String repairUserNo) {
        this.repairUserNo = repairUserNo;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
   
}
