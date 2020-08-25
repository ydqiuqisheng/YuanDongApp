package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zebra.scannercontrol.DCSScannerInfo;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.ReaderDevice;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 单中年 on 2017/2/27.
 */

public class DeviceSettingAdapter extends HsBaseAdapter<ReaderDevice> {

    public DeviceSettingAdapter(List<ReaderDevice> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null) view=mInflater.inflate(R.layout.device_setting_item,viewGroup,false);
        TextView tvDeviceSettingItemName= ViewHolder.get(view,R.id.tvDeviceSettingItemName);//名称
        TextView tvDeviceSettingItemMacAddress=ViewHolder.get(view,R.id.tvDeviceSettingItemMacAddress);//macaddress
        CheckBox cbDeviceSettingItem=ViewHolder.get(view,R.id.cbDeviceSettingItem);
        ReaderDevice device=mList.get(i);
        tvDeviceSettingItemName.setText(device.getName());
        tvDeviceSettingItemMacAddress.setText(device.getAddress());
        cbDeviceSettingItem.setChecked(device.isConnected());
        return view;
    }
}
