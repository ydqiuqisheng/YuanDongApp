package net.huansi.equipment.equipmentapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.activity.check_simple.CheckSimpleDepartmentActivity;
import net.huansi.equipment.equipmentapp.activity.check_simple.CheckSimpleMonitorActivity;
import net.huansi.equipment.equipmentapp.entity.BomInfo;
import net.huansi.equipment.equipmentapp.entity.CheckUser;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.TokenInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.receiver.AlarmReceiver;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ThreadPool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class NikeBomService extends Service{

    private ThreadPool threadPool;
    private static OkHttpClient client = new OkHttpClient();
    private static String token_json="";
    private static String bom_json="";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG","onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        threadPool=ThreadPool.getInstantiation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //執行獲取bom並call存儲存到數據庫
                initBomService();

            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int anHour = 60 * 60 * 1000;   // 这是一小时的毫秒数
        //int anHour = 60 * 1000;   // 这是6十秒的毫秒数

        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.e("TAG","hour="+hour);
        if (hour>7&&hour<18){
            Intent i = new Intent(this, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }

        return Service.START_STICKY;
    }

    private  void initBomService() {
        //post请求獲取token
        FormBody formBody = new FormBody.Builder()
                .add("client_id", "testfactory.gsm.bom")
                .add("client_secret", "Ttpx9N1Qpr-thAdjgWVpyMsOKdce0ECElm8XWoAdL7sHH6u0AIOQ0RgfjH59rQb_")
                .add("grant_type", "client_credentials")
                .build();

        final Request request = new Request.Builder().url("https://nike-qa.oktapreview.com/oauth2/ausa0mcornpZLi0C40h7/v1/token")
                .addHeader("Content-Type", "application/x-www-form-urlencoded").post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() >= 200 && response.code() < 300) {
                    token_json = response.body().string();
                    Log.e("TAG","tokenJson="+token_json);
                    TokenInfo tokenInfo = JSON.parseObject(token_json, TokenInfo.class);
                    String access_token = tokenInfo.getAccess_token().trim();
                    BomData(access_token);
                }
            }
        });
    }

    private   void BomData(String Token){

        //get请求獲取bom數據
        Request request1 = new Request.Builder().url("https://2lglxh787c.execute-api.us-east-1.amazonaws.com/dev/product/boms_edge/v1")
                .addHeader("x-api-key","4B4I0gZQvU3SAG4s3Qzhw5b1uahxu0sx8ZKch5zv")
                .addHeader("Authorization","Bearer " +Token)
                .build();
        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG","失敗原因"+e.getMessage());
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() >= 200 && response.code() < 300) {
                    bom_json=response.body().string();
                    Log.e("TAG","bomJson="+bom_json);
                    sendBom();

                    System.out.println(response.body().string());
                }
            }
        });
    }

    private void sendBom() {
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
                BomInfo bomInfo = JSON.parseObject(bom_json, BomInfo.class);
                List<BomInfo.ObjectsBean> objects = bomInfo.getObjects();
                HsWebInfo jsonData = null;
                HsWebInfo jsonData1 = null;
                String data = new SimpleDateFormat("yyyy-MM-dd HH").format(new Date()).toString();

                for (int i = 0; i < objects.size(); i++) {
                    jsonData = NewRxjavaWebUtils.getJsonData(getApplicationContext(), "CreateUpdateDelete_NikeBOM_MainBean",
                            "DataAction=" + "Insert" +
                                    ",id=" + objects.get(i).getId() +
                                    ",bomId=" + objects.get(i).getBomId() +
                                    ",CerateDate=" + data+
                                    ",prmryAbrv=" + objects.get(i).getPrimDevRegAbrv() +
                                    ",prmryColorCd=" + objects.get(i).getPrmryColorId() +
                                    ",plugColorwayCd=" + objects.get(i).getPlugColorwayCd() +
                                    ",parentFcty=" + objects.get(i).getParentFcty() +
                                    ",designRegCode=" + objects.get(i).getDesignRegCode() +
                                    ",mscCode=" + objects.get(i).getMscCode() +
                                    ",seasonYr=" + objects.get(i).getSeasonYr() +
                                    ",objectId=" + objects.get(i).getObjectId() +
                                    ",bomUpdateTimestamp=" + objects.get(i).getBomUpdateTimestamp() +
                                    ",developer=" + objects.get(i).getDeveloper().replace(",",";").toString() +
                                    ",bomUpdateUserid=" + objects.get(i).getBomUpdateUserid() +
                                    ",styleNbr=" + objects.get(i).getStyleNbr() +
                                    ",silhouetteCode=" + objects.get(i).getSilhouetteCode() +
                                    ",prmryDesc=" + objects.get(i).getPrmryDesc() +
                                    ",prmryColorId=" + objects.get(i).getPrmryColorId() +
                                    ",productId=" + objects.get(i).getProductId() +
                                    ",cycleYear=" + objects.get(i).getCycleYear() +
                                    ",colorwayCd=" + objects.get(i).getColorwayCd() +
                                    ",primDevRegAbrv=" + objects.get(i).getPrimDevRegAbrv() +
                                    ",colorwayId=" + objects.get(i).getColorwayId() +
                                    ",mscIdentifier=" + objects.get(i).getMscIdentifier() +
                                    ",bomStatus=" + objects.get(i).getBomStatus() +
                                    ",seasonCd=" + objects.get(i).getSeasonCd()+
                                    ",designRegAbrv=" + objects.get(i).getDesignRegAbrv()+
                                    ",styleNm=" + objects.get(i).getStyleNm()+
                                    ",silhouetteDesc=" + objects.get(i).getSilhouetteDesc()+
                                    ",mscLevel3=" + objects.get(i).getMscLevel3()+
                                    ",mscLevel2=" + objects.get(i).getMscLevel2()+
                                    ",primDevRegCode=" + objects.get(i).getPrimDevRegCode()+
                                    ",mscLevel1=" + objects.get(i).getMscLevel1()+
                                    ",developerUserId=" + objects.get(i).getDeveloperUserId()+
                                    ",factoryCode=" + objects.get(i).getFactoryCode()+
                                    ",resourceType=" + objects.get(i).getResourceType()
                            ,
                            String.class.getName(), false, "插入Bom主表成功");
                }
                for (int j = 0; j < objects.size(); j++) {
                    objects.get(j).getBomLineItems().get(0).setUse("");
                    for (int k=0;k<objects.get(j).getBomLineItems().size();k++){
                        List<BomInfo.ObjectsBean.BomLineItemsBean> bomLineItems = objects.get(j).getBomLineItems();
                        jsonData1 = NewRxjavaWebUtils.getJsonData(getApplicationContext(), "CreateUpdateDelete_NikeBOM_DetailBean",
                                "DataAction =" + "Insert"+
                                        ",DetailID=" + UUID.randomUUID().toString()+
                                        ",MainID=" + objects.get(j).getId().replace(",",";").toString()+
                                        ",createDate=" + data+
                                        ",pcxSuppliedMatlId=" + bomLineItems.get(k).getPcxSuppliedMatlId()+
                                        ",bomItmUpdateTimestamp=" + bomLineItems.get(k).getBomItmUpdateTimestamp().replace(",",";").toString()+
                                        ",itemType1=" + bomLineItems.get(k).getItemType1().replace(",",";").toString()+
                                        ",use=" + bomLineItems.get(k).getUse().replace(",",";").toString()+
                                        ",bomComponentId=" + bomLineItems.get(k).getBomComponentId()+
                                        ",itemNbr=" + bomLineItems.get(k).getItemNbr()+
                                        ",description=" +bomLineItems.get(k).getDescription().replace(",",";".toString())+
                                        ",is=" + bomLineItems.get(k).getIs().replace(",",";").toString()+
                                        ",it=" + bomLineItems.get(k).getIt().replace(",",";").toString()+
                                        ",vendLo=" + bomLineItems.get(k).getVendLo().replace(",",";").toString()+
                                        ",vendCd=" + bomLineItems.get(k).getVendCd().replace(",",";").toString()+
                                        ",bomRowNbr=" + bomLineItems.get(k).getBomRowNbr()+
                                        ",bomItmSetupTimestamp=" + bomLineItems.get(k).getBomItmSetupTimestamp().replace(",",";").toString()+
                                        ",vendNm=" + bomLineItems.get(k).getVendNm().replace(",",";").toString()+
                                        ",componentOrd=" + bomLineItems.get(k).getComponentOrd()+
                                        ",vendId=" + bomLineItems.get(k).getVendId()+
                                        ",bomItmId=" + bomLineItems.get(k).getBomItmId()
                                ,
                                String.class.getName(), false, "插入Bom从表成功");
                        Log.e("TAG","bom="+j+"item="+k);
                    }
                }
                OthersUtil.ToastMsg(getApplicationContext(),jsonData.json+jsonData1.json);
                Log.e("TAG", "yuanyin=" + jsonData.json);
                Log.e("TAG", "yuanyin1=" + jsonData1.json);
            }
        });
    }


}
