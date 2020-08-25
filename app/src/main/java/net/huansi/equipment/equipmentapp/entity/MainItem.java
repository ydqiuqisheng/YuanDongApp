package net.huansi.equipment.equipmentapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 单中年 on 2017/2/22.
 */

public class MainItem{
    public int drawableId=0;
    public String name="";
    public String code="";

    public MainItem(int drawableId, String name, String code) {
        this.drawableId = drawableId;
        this.name = name;
        this.code = code;
    }
}
