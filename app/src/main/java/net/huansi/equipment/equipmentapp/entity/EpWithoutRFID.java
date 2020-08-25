package net.huansi.equipment.equipmentapp.entity;

/**
 * Created by shanz on 2017/3/5.
 */

public class EpWithoutRFID extends WsData {

//b.ItemID as EquipmentDetailID,--设备ID
//    b.InFactoryDate,
//    b.AssetsCode,
//    b.OutFactoryCode,
//    b.DepreciationStartingDate,
//
//    b.DeclarationNum,
//    c.EquipmentName,
//    c.Brand,
//    c.Model
    public String EQUIPMENTDETAILID="";

    public String INFACTORYDATE="";//入厂日期
    public String ASSETSCODE="";//资产编号
    public String OUTFACTORYCODE="";//出厂编号
    public String DEPRECIATIONSTARTINGDATE="";//折旧日期
    public String DECLARATIONNUM="";//报关单号
    public String COSTCENTER="";//成本中心

    public String EQUIPMENTNAME="";
    public String BRAND="";
    public String MODEL="";
}
