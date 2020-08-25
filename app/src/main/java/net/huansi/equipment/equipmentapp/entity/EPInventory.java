package net.huansi.equipment.equipmentapp.entity;

/**
 * Created by 单中年 on 2017/2/24.
 * 设备盘点表
 */

public class EPInventory extends WsData {
//    @uInventoryGUID as sInventoryID,--设备盘点的头表
//    a.ItemID as iEquipmentDetailId,--设备从表的主键
//    b.EquipmentListID as iEquipmentId,--设备主表的主键
//    a.InFactoryDate,
//    a.OutFactoryCode,
//    a.DepreciationStartingDate,
//    a.AssetsCode,
//    a.CostCenter,
//    a.DeclarationNum,
//    a.EPCCode
    public String SINVENTORYID="";
    public String IEQUIPMENTDETAILID="";
    public String IEQUIPMENTID="";

    public String EQUIPMENTNAME="";//设备名称
    public String INFACTORYDATE="";//入厂日期
    public String ASSETSCODE="";//资产编号
    public String OUTFACTORYCODE="";//出厂编号
    public String DEPRECIATIONSTARTINGDATE="";//折旧日期
    public String COSTCENTER="";//成本中心
    public String DECLARATIONNUM="";//报关单号
    public String EPCCODE="";//报关单号
    public String FACTORY="";//所在工厂
//
//
//
//    public int status=-2;//-2未盘 -1盘亏 0正常 1盘盈



}
