package net.huansi.equipment.equipmentapp.activity.bom_data;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.BomInfo;
import net.huansi.equipment.equipmentapp.entity.TokenInfo;
import net.huansi.equipment.equipmentapp.service.NikeBomService;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BomActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bom;
    }

    @Override
    public void init() {
        Intent intent = new Intent(this, NikeBomService.class);
        startService(intent);
    }



}
