package net.huansi.equipment.equipmentapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 单中年 on 2017/2/22.
 */

public class InventoryItem implements Parcelable {
    public String epCode="";
    public String equipmentName="";//设备名称
    public int propertyId=0;//资产编号
    public int outOfCompanyId=0;//出厂编号
    public String costCenter="";//成本中心
    public String companyByUse="";//使用单位
    public String companyIn="";//所在位置
    public int status=0;//盘点状态 -1盘亏 0正常 1盘盈

    protected InventoryItem(Parcel in) {
        epCode = in.readString();
        equipmentName = in.readString();
        propertyId = in.readInt();
        outOfCompanyId = in.readInt();
        costCenter = in.readString();
        companyByUse = in.readString();
        companyIn = in.readString();
        status = in.readInt();
    }

    public static final Creator<InventoryItem> CREATOR = new Creator<InventoryItem>() {
        @Override
        public InventoryItem createFromParcel(Parcel in) {
            return new InventoryItem(in);
        }

        @Override
        public InventoryItem[] newArray(int size) {
            return new InventoryItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(epCode);
        parcel.writeString(equipmentName);
        parcel.writeInt(propertyId);
        parcel.writeInt(outOfCompanyId);
        parcel.writeString(costCenter);
        parcel.writeString(companyByUse);
        parcel.writeString(companyIn);
        parcel.writeInt(status);
    }
}
