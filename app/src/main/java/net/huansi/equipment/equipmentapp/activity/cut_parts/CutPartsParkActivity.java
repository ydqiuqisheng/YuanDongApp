package net.huansi.equipment.equipmentapp.activity.cut_parts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.CutPartsLoadEntity;
import net.huansi.equipment.equipmentapp.entity.CutPartsLoadInfo;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CutPartsParkActivity extends BaseActivity {
    @BindView(R.id.cpPark_carNo)
    TextView carNo;
    @BindView(R.id.cpPark_Area)
    TextView Area;
    @BindView(R.id.et_ParkInfo)
    EditText parkInfo;
    private LoadProgressDialog dialog;
    private int REQUEST_CODE_CAR=0;
    private int REQUEST_CODE_AREA=1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cut_parts_park;
    }

    @Override
    public void init() {
        setToolBarTitle("停车");
        dialog=new LoadProgressDialog(this);
        ZXingLibrary.initDisplayOpinion(getApplicationContext());
    }
    @OnClick(R.id.cpPark_carNo)
    void carNo(){
        Intent intent = new Intent(CutPartsParkActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CAR);
    }
    @OnClick(R.id.cpPark_Area)
    void area(){
        Intent intent = new Intent(CutPartsParkActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AREA);
    }

    @OnClick(R.id.cutParts_bind)
    void cutParts_bind(){
        if (carNo.getText().toString().isEmpty()||Area.getText().toString().isEmpty()){
            OthersUtil.ToastMsg(getApplicationContext(),"空值无法绑定");
            return;
        }
        OthersUtil.showDoubleChooseDialog(CutPartsParkActivity.this, "确认绑定?",null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CutPartsParkActivity.this,"")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_BindingStorageLocationAndFrameCode",
                                        "FrameCode="+carNo.getText().toString().trim()+
                                                ",StorageLocation="+Area.getText().toString().trim(),
                                        String.class.getName(),false,"组别获取成功");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        Log.e("TAG","bindJson="+json);
                    }
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG","errorBind="+hsWebInfo.json);
                        OthersUtil.ToastMsg(CutPartsParkActivity.this,"绑定成功");
                        carNo.setText("");
                        Area.setText("");
                    }
                });
            }
        });


    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (carNo.getText().toString().isEmpty()){
            carNo.setText(parkInfo.getText().toString().trim());
            parkInfo.getText().clear();
        }else if (Area.getText().toString().isEmpty()){
            Area.setText(parkInfo.getText().toString().trim());
            parkInfo.getText().clear();
        }
        return super.onKeyUp(keyCode, event);
    }

    //接收扫描二维码数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAR) {
            //处理扫描结果
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("TAG","扫描结果="+result);
                    carNo.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(CutPartsParkActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }

        }
        if (requestCode == REQUEST_CODE_AREA) {
            //处理扫描结果
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("TAG","扫描结果="+result);
                    Area.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(CutPartsParkActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
