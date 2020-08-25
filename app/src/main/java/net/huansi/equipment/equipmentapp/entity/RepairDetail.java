package net.huansi.equipment.equipmentapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 单中年 on 2017/3/1.
 */

public class RepairDetail implements Serializable{
    public BaseData project;
    public String method="";
    public BaseData result;
    public String repairUserNo;
    public String remark="";
}
