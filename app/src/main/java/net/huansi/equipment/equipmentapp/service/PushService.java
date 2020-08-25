package net.huansi.equipment.equipmentapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.repair.RepairMainActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.CallRepairUtil;
import net.huansi.equipment.equipmentapp.util.GetMacUtil;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.List;

import rx.Notification;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhou.mi on 2018/4/2.
 */

public class PushService extends Service {
    public NotificationManager manager;
    public int notification_id;
    public String MAC="02:00:00:00:00:00";//指定Mac地址的pad才可以接收到推送
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        startForeground(notification_id,new android.app.Notification());
        String mac = GetMacUtil.getAdresseMAC(getApplication());
        MAC=mac;
        super.onCreate();
        Log.e("TAG","create");
    }

    private void initNotifiManager(int ID,String text) {

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent targetIntent = new Intent(this, RepairMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, targetIntent, 0);
        Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("您有新的叫修请及时修理")
                .setContentText(text)
                .setTicker("有新消息啦")
                .setWhen(System.currentTimeMillis())
                .setColor(Color.RED)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(false)
                .setSound(ringUri)
                .setDefaults(NotificationCompat.FLAG_SHOW_LIGHTS)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.remind_icon);
        android.app.Notification notification = mBuilder.build();
        notification.flags |= NotificationCompat.FLAG_AUTO_CANCEL;
        manager.notify(ID,notification);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("TAG","Start");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        Log.e("TAG","startCommand");
        new PollingThread().start();
        return Service.START_STICKY;
    }

    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     * @Author Ryan
     * @Create 2013-7-13 上午10:18:34
     */

    class PollingThread extends Thread {
        @Override
        public void run() {
            Log.e("TAG","Mac="+MAC);
            Log.e("TAG","Polling...");
            HsWebInfo jsonData = NewRxjavaWebUtils.getJsonData(getApplicationContext(), "spAppEPQueryCallRepairRecord_ForPush", "Mac="+MAC, String.class.getName()
                    , false, "开始数据获取");
            Log.e("TAG","Success="+jsonData.json);
            String json = jsonData.json;
            if (json=="请在网络通畅的情况下使用！！！"||json==""){
                return;
            }
            CallRepairUtil callRepairUtil = JSON.parseObject(json, CallRepairUtil.class);
            if (callRepairUtil==null){
                return;
            }else {
                List<CallRepairUtil.DATABean> data = callRepairUtil.getDATA();
                for (CallRepairUtil.DATABean item:data){
                    initNotifiManager(Integer.parseInt(item.getID()),item.getCALLREPAIRDATE());
                }
            }
            Log.e("TAG","New message!");

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
