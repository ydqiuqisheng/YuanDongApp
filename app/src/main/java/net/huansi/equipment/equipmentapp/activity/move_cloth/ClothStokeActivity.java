package net.huansi.equipment.equipmentapp.activity.move_cloth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.ClothMoveRecords;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.SimpleEntity;
import net.huansi.equipment.equipmentapp.entity.SimpleGetLocation;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ClothStokeActivity extends BaseActivity {
    private int REQUEST_CODE=1;
    private LoadProgressDialog dialog;
    private List<String> UUIDList=new ArrayList<>();
    @BindView(R.id.simple_location)
    EditText simple_location;
    @BindView(R.id.simple_ctn_no)
    EditText simple_ctn_no;
    @BindView(R.id.lv_simple_in)
    ListView lv_simple_in;
    private List<String> locationList=new ArrayList<>();
    private List<String> simpleList=new ArrayList<>();
    private ArrayAdapter<String> mLocationAdapter;
    private ArrayAdapter<String> mSimpleAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cloth_stoke;
    }

    @Override
    public void init() {
        setToolBarTitle("样衣入库");
        ZXingLibrary.initDisplayOpinion(this);
        dialog=new LoadProgressDialog(this);
//        locationList.add("东北");
//        locationList.add("西北");
//        locationList.add("东南");
//        locationList.add("西南");
//        mLocationAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,locationList);
//        simple_location.setAdapter(mLocationAdapter);
    }

    @OnClick(R.id.bt_simple_upDate)
    void simpleUpDate(){
        if (UUIDList.isEmpty()){
            return;
        }else {
            for (int i=0;i<UUIDList.size();i++){
                Log.e("TAG","uuID="+UUIDList.get(i).toString());
                Log.e("TAG","QCLocation="+simple_location.getText().toString());
                Log.e("TAG","BoxNo="+simple_ctn_no.getText().toString());
                final int finalI = i;
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_QCSubmitLocation",
                                                "QCLocation="+simple_location.getText().toString()+
                                                ",BoxNo="+simple_ctn_no.getText().toString()+
                                                ",uuID="+UUIDList.get(finalI).toString(),String.class.getName(),false,"组别获取成功");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        OthersUtil.ToastMsg(ClothStokeActivity.this,"入库成功");
                        Log.e("TAG","upJson="+json);
                    }
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG","error="+hsWebInfo.json);
                        OthersUtil.showTipsDialog(ClothStokeActivity.this,"成功入库");
                    }
                });
            }
        }

    }
    //调起二维码扫描
    @OnClick(R.id.simple_uuid)
    void startScan(){
        Intent intent = new Intent(ClothStokeActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    //接收扫描结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("TAG","扫描结果="+result);
                    UUIDList.add(result);
                    getSimpleInfo(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(ClothStokeActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void getSimpleInfo(final String result) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GteAPPSampleRegister ",
                                "uuID="+result,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","takeJson="+json);
                SimpleGetLocation simpleEntity = JSON.parseObject(json, SimpleGetLocation.class);
                List<SimpleGetLocation.DATABean> data = simpleEntity.getDATA();
                simpleList.add("款号:"+data.get(0).getCOMPANYSTYLE()+ "  季节:"+data.get(0).getSEASON()+"  位置:"+data.get(0).getQCLOCATION()+
                        "  箱号:"+data.get(0).getBOXNO()+"  色号:"+data.get(0).getCOMB()+"  sNo:"+data.get(0).getSTYLENO()+"  单号:"+data.get(0).getBILLNO());
               mSimpleAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item_item,R.id.white_text,simpleList);
               lv_simple_in.setAdapter(mSimpleAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
                OthersUtil.showTipsDialog(ClothStokeActivity.this,"未找到该样衣相关信息");
            }
        });
    }
}
