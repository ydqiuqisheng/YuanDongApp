package net.huansi.equipment.equipmentapp.activity.cut_parts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.CutPartsTakeEntity;
import net.huansi.equipment.equipmentapp.entity.CutPartsTakeInfo;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CutPartsTakeActivity extends BaseActivity {
    private LoadProgressDialog dialog;
    private List<CutPartsTakeInfo> takeInfoList;
    private CutPartsTakeInfo takeInfo;
    private CutPartsTakeAdapter takeAdapter;
    private Map<Integer,Boolean> map;
    @BindView(R.id.TakeList)
    ListView lv_takeList;
    @BindView(R.id.et_TakeInfo)
    EditText et_GetInfo;
    @BindView(R.id.cpTake_car)
    TextView carNo;
    @BindView(R.id.cpTakeAway)
    Button takePieces;
    private int REQUEST_CODE=1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cut_parts_take;
    }

    @Override
    public void init() {
        setToolBarTitle("取片");
        dialog=new LoadProgressDialog(this);
        takeInfoList=new ArrayList<>();
        ZXingLibrary.initDisplayOpinion(getApplicationContext());
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (carNo.getText().toString().isEmpty()){
            carNo.setText(et_GetInfo.getText().toString().trim());
            et_GetInfo.getText().clear();
        }
        if (!carNo.getText().toString().isEmpty()){
            getTakeInfo(carNo.getText().toString().trim());
        }
        return super.onKeyUp(keyCode, event);
    }

    @OnClick(R.id.cpTake_car)
    void takeCar(){
        Intent intent = new Intent(CutPartsTakeActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


    //接收扫描二维码数据
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

                    getTakeInfo(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(CutPartsTakeActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
    private void getTakeInfo(final String frameCode) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CutPartsTakeActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_GetCutPiecesTransportDetail",
                                "FrameCode="+frameCode,
                                String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","takeJson="+json);
                CutPartsTakeEntity cutPartsTakeEntity = JSON.parseObject(json, CutPartsTakeEntity.class);
                List<CutPartsTakeEntity.DATABean> data = cutPartsTakeEntity.getDATA();
                //takeInfoList=new ArrayList<>();
                map=new HashMap<>();
                for (int i=0;i<data.size();i++){
                    takeInfo=new CutPartsTakeInfo();
                    takeInfo.ITEMID=data.get(i).getITEMID();
                    takeInfo.FEPOCODE=data.get(i).getFEPOCODE();
                    takeInfo.BEDNO=data.get(i).getBEDNO();
                    takeInfo.COMBNAME=data.get(i).getCOMBNAME();
                    takeInfo.QUANTITY=data.get(i).getQUANTITY();
                    takeInfo.LAYER=data.get(i).getLAYER();
                    takeInfoList.add(takeInfo);
                }
                for (int j=0;j<takeInfoList.size();j++){
                    map.put(j,false);
                }
                takeAdapter=new CutPartsTakeAdapter(takeInfoList,getApplicationContext());
                lv_takeList.setAdapter(takeAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorBind="+hsWebInfo.json);
            }
        });
    }

    private class CutPartsTakeAdapter extends HsBaseAdapter<CutPartsTakeInfo>{
        public CutPartsTakeAdapter(List<CutPartsTakeInfo> list, Context context) {
            super(list, context);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            Log.e("TAG","MAP="+map.toString());
            if (convertView==null) convertView = mInflater.inflate(R.layout.cut_parts_take_item, viewGroup, false);

            CheckBox choose = ViewHolder.get(convertView, R.id.cpTake_choose);
            TextView po = ViewHolder.get(convertView, R.id.cpTake_Po);
            TextView bedNo = ViewHolder.get(convertView, R.id.cpTake_BedNo);
            TextView comb = ViewHolder.get(convertView, R.id.cpTake_Comb);
            TextView qty = ViewHolder.get(convertView, R.id.cpTake_Qty);
            TextView layer = ViewHolder.get(convertView, R.id.cpTake_Layer);
            CutPartsTakeInfo cutPartsTakeInfo = mList.get(position);
            po.setText(cutPartsTakeInfo.FEPOCODE);
            bedNo.setText(cutPartsTakeInfo.BEDNO);
            comb.setText(cutPartsTakeInfo.COMBNAME);
            qty.setText(cutPartsTakeInfo.QUANTITY);
            layer.setText(cutPartsTakeInfo.LAYER);
            choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                    map.put(position,isCheck);
                    Log.e("TAG", "选中" + position + isCheck);
                    //Log.e("TAG", "内容" + choose.getText().toString());
                }
            });
            if (map.get(position)==true){
                choose.setChecked(true);
            }else {
                choose.setChecked(false);
            }
            return convertView;
        }
    }

    @OnClick(R.id.cpTakeAway)
    void takeAway(){
        for (int i=0;i<takeInfoList.size();i++){
            if (map.get(i)==true){
                final int finalI = i;
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CutPartsTakeActivity.this,"")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_TakeOutCutPiecesFromFrame",
                                        "ItemID="+takeInfoList.get(finalI).ITEMID,
                                        String.class.getName(),false,"组别获取成功");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        Log.e("TAG","upJson="+json);
                    }
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG","errorLog="+hsWebInfo.json);
                        OthersUtil.ToastMsg(getApplicationContext(),"解绑成功,可取片");
                    }
                });
            }

    }
    }
}
