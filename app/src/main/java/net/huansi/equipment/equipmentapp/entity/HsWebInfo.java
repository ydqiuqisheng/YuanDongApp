package net.huansi.equipment.equipmentapp.entity;

import java.io.Serializable;

/**
 * Created by 单中年 on 2016/12/27.
 */

public class HsWebInfo implements Serializable{
    public boolean success=true;
    public boolean flag=false;
    public String json;
    public HsError error=new HsError();
    public WsData wsData;
    public Object  object;

}
