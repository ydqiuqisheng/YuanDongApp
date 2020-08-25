package net.huansi.equipment.equipmentapp.entity;

/**
 * Created by 单中年 on 2017/2/28.
 */

public class RepairList extends WsData {
   /* b.Factory,
    b.AssetsCode,
    b.EPCCode,
    b.CostCenter,
    b.OutFactoryCode,
    b.ItemID as EquipmentDetailID,--设备ID
    c.EquipmentName,
    c.Model*/
    public String EQUIPMENTDETAILID="";//设备ID
    public String FACTORY="";//工厂
    public String ASSETSCODE="";//资产编号
    public String EPCCODE="";//EPCode
    public String COSTCENTER="";//成本中心
    public String OUTFACTORYCODE="";//出厂编码
    public String EQUIPMENTNAME="";//设备名称
    public String MODEL="";//设备型号
    public String EPSTATUS="";//维修状态
}
