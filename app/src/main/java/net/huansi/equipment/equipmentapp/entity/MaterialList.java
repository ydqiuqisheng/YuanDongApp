package net.huansi.equipment.equipmentapp.entity;

/**
 * Created by zhou.mi on 2018/1/11.
 */

public class MaterialList extends WsData {

    public String SN="";
    public String FEPOQUANTITY="";
    public Float QUANTITYTOTAL=0.00f;
    public Float QUANTITYCHECKED=0.00f;
    //public Float QUANTITYUNCHECK=0.00f;
    public int PNOTOTAL=0;
    public int PNOCHECKED=0;
    //public int PNOUNCHECK=0;
    public float QUANTITYSCAN=0.00f;
    public int PNOSCAN=0;
    public String ISMATCHERPPO="";
    public String EXEMPTION="";//是否免检
    public int NONONUM=0;//未传待扫


    public MaterialList() {
    }


}
