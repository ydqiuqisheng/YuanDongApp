package net.huansi.equipment.equipmentapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.huansi.equipment.equipmentapp.service.NikeBomService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NikeBomService.class);
        context.startService(i);
    }
}
