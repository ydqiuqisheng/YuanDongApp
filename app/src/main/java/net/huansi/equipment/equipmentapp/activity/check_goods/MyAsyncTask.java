package net.huansi.equipment.equipmentapp.activity.check_goods;

import android.os.AsyncTask;
import android.util.Log;

import com.zebra.scannercontrol.DCSSDKDefs;

import net.huansi.equipment.equipmentapp.activity.awake_goods.AwakeMainActivity;
import net.huansi.equipment.equipmentapp.service.AlwaysRunningService;

/**
 * Created by zhou.mi on 2018/1/29.
 */
//异步开启扫描功能
public class MyAsyncTask extends AsyncTask<Void,Integer,Boolean>{
    private int scannerId;
    public MyAsyncTask(int scannerId){
        this.scannerId=scannerId;
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        DCSSDKDefs.DCSSDK_RESULT result = DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_FAILURE;

        if (AlwaysRunningService.sdkHandler!=null){
            result = AlwaysRunningService.sdkHandler.dcssdkEstablishCommunicationSession(scannerId);
        }
        if(result == DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_SUCCESS){
            Log.e("TAG","success");
            return true;
        }
        else if(result == DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_FAILURE) {
            Log.e("TAG","failure");
            return false;
        }
        return false;
    }
}
