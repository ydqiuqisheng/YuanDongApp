package net.huansi.equipment.equipmentapp.entity;

/**
 * Created by 单中年 on 2017/3/1.
 */

public class EPInfo extends WsData{
//    a.ItemID as iEquipmentDetailId,--设备从表的主键
//    b.EquipmentListID as iEquipmentId,--设备主表的主键
//    a.EPCCode as EquipmentCode,--EPCode
//    b.EquipmentName,--设备名称
//    a.AssetsCode,--资产编号
//    a.OutFactoryCode,--出厂编号
//    b.Model,--设备型号
//    a.CostCenter,--成本中心
//    c.CHTName as departmentCHTName,--使用单位中文名
//    c.EnglishName as departmentEnglishName,--使用单位英文名
//    a.Factory--在工厂

    public String IEQUIPMENTDETAILID;
    public String IEQUIPMENTID;
    public String EQUIPMENTCODE;
    public String EQUIPMENTNAME;
    public String ASSETSCODE;
    public String OUTFACTORYCODE;
    public String MODEL;
    public String COSTCENTER;
    public String DEPARTMENTCHTNAME;
    public String DEPARTMENTENGLISHNAME;
    public String FACTORY;



}
