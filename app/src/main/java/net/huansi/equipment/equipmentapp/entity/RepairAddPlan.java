package net.huansi.equipment.equipmentapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by 单中年 on 2017/3/2.
 */

public class RepairAddPlan implements Parcelable{
    public Date startDate;
    public Date endDate;
    public boolean isAdd=true;//是否为刚刚新加的

    protected RepairAddPlan(Parcel in) {
    }

    public static final Creator<RepairAddPlan> CREATOR = new Creator<RepairAddPlan>() {
        @Override
        public RepairAddPlan createFromParcel(Parcel in) {
            return new RepairAddPlan(in);
        }

        @Override
        public RepairAddPlan[] newArray(int size) {
            return new RepairAddPlan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
