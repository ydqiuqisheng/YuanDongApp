package net.huansi.equipment.equipmentapp.sqlite_db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Objects;

/**
 * Created by zhou.mi on 2018/3/8.
 */
@Entity
public class MaterialDataInSQLite {

    @Id(autoincrement = true)
    private Long id;
    private String barcode;
    private String customerName;
    private String sn;//SN号码
    private String fepoQuantity;//FEPO
    private String materialCode;
    private String materialName;
    private String materialID;
    private String colorCode;
    private String colorName;
    private String quantity;
    private String quantityPs;
    private String quantityPsRaw;
    private String vatno;
    private String pno;
    private String checkStatus;
    private String barcodeTtlNum;
    private String checkedttNum;
    private String unCheckedNum;
    private String exemption;
    private String isMatchErpPo;
    @Generated(hash = 1167153034)
    public MaterialDataInSQLite(Long id, String barcode, String customerName, String sn, String fepoQuantity, String materialCode, String materialName,
            String materialID, String colorCode, String colorName, String quantity, String quantityPs, String quantityPsRaw, String vatno, String pno,
            String checkStatus, String barcodeTtlNum, String checkedttNum, String unCheckedNum, String exemption, String isMatchErpPo) {
        this.id = id;
        this.barcode = barcode;
        this.customerName = customerName;
        this.sn = sn;
        this.fepoQuantity = fepoQuantity;
        this.materialCode = materialCode;
        this.materialName = materialName;
        this.materialID = materialID;
        this.colorCode = colorCode;
        this.colorName = colorName;
        this.quantity = quantity;
        this.quantityPs = quantityPs;
        this.quantityPsRaw = quantityPsRaw;
        this.vatno = vatno;
        this.pno = pno;
        this.checkStatus = checkStatus;
        this.barcodeTtlNum = barcodeTtlNum;
        this.checkedttNum = checkedttNum;
        this.unCheckedNum = unCheckedNum;
        this.exemption = exemption;
        this.isMatchErpPo = isMatchErpPo;
    }
    @Generated(hash = 714227621)
    public MaterialDataInSQLite() {
    }

    public String getBarcode() {
        return this.barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getCustomerName() {
        return this.customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getSn() {
        return this.sn;
    }
    public void setSn(String sn) {
        this.sn = sn;
    }
    public String getFepoQuantity() {
        return this.fepoQuantity;
    }
    public void setFepoQuantity(String fepoQuantity) {
        this.fepoQuantity = fepoQuantity;
    }
    public String getMaterialCode() {
        return this.materialCode;
    }
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
    public String getMaterialName() {
        return this.materialName;
    }
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public String getMaterialID() {
        return this.materialID;
    }
    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }
    public String getColorCode() {
        return this.colorCode;
    }
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
    public String getColorName() {
        return this.colorName;
    }
    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
    public String getQuantity() {
        return this.quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getQuantityPs() {
        return this.quantityPs;
    }
    public void setQuantityPs(String quantityPs) {
        this.quantityPs = quantityPs;
    }
    public String getQuantityPsRaw() {
        return this.quantityPsRaw;
    }
    public void setQuantityPsRaw(String quantityPsRaw) {
        this.quantityPsRaw = quantityPsRaw;
    }
    public String getVatno() {
        return this.vatno;
    }
    public void setVatno(String vatno) {
        this.vatno = vatno;
    }
    public String getPno() {
        return this.pno;
    }
    public void setPno(String pno) {
        this.pno = pno;
    }
    public String getCheckStatus() {
        return this.checkStatus;
    }
    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }
    public String getBarcodeTtlNum() {
        return this.barcodeTtlNum;
    }
    public void setBarcodeTtlNum(String barcodeTtlNum) {
        this.barcodeTtlNum = barcodeTtlNum;
    }
    public String getCheckedttNum() {
        return this.checkedttNum;
    }
    public void setCheckedttNum(String checkedttNum) {
        this.checkedttNum = checkedttNum;
    }
    public String getUnCheckedNum() {
        return this.unCheckedNum;
    }
    public void setUnCheckedNum(String unCheckedNum) {
        this.unCheckedNum = unCheckedNum;
    }
    public String getIsMatchErpPo() {
        return this.isMatchErpPo;
    }
    public void setIsMatchErpPo(String isMatchErpPo) {
        this.isMatchErpPo = isMatchErpPo;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getExemption() {
        return this.exemption;
    }
    public void setExemption(String exemption) {
        this.exemption = exemption;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (getClass()!= obj.getClass()){
//            MaterialDataInSQLite o=(MaterialDataInSQLite)obj;
//            return this.id==o.id
//            &&this.barcode==o.barcode
//            &&this.customerName==o.customerName
//            &&this.sn==o.sn
//            &&this.fepoQuantity==o.fepoQuantity
//            &&this.materialCode==o.materialCode
//            &&this.materialID==o.materialID
//            &&this.materialName==o.materialName
//            &&this.colorCode==o.colorName
//            &&this.quantity==o.quantity
//            &&this.quantityPs==o.quantityPs
//            &&this.quantityPsRaw==o.quantityPsRaw
//            &&this.vatno==o.vatno
//            &&this.pno==o.pno
//            &&this.checkStatus==o.checkStatus
//            &&this.barcodeTtlNum==o.barcodeTtlNum
//            &&this.checkedttNum==o.checkedttNum
//            &&this.unCheckedNum==o.unCheckedNum
//            &&this.isMatchErpPo==o.isMatchErpPo;
//        }
//        return false;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id,barcode,customerName,sn,fepoQuantity,materialCode,materialID,materialName
//                ,colorCode,colorName,quantity,quantityPs,quantityPsRaw,vatno,pno,checkStatus,barcodeTtlNum,checkedttNum,unCheckedNum,isMatchErpPo);
//    }
}
