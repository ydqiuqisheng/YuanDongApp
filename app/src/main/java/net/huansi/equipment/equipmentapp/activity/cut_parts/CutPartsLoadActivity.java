package net.huansi.equipment.equipmentapp.activity.cut_parts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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
import net.huansi.equipment.equipmentapp.activity.logging_bill.LoggingBillActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.CutPartsLoadEntity;
import net.huansi.equipment.equipmentapp.entity.CutPartsLoadInfo;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.LogDtEntity;
import net.huansi.equipment.equipmentapp.entity.LogDtInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CutPartsLoadActivity extends BaseActivity{
    @BindView(R.id.cpLoad_carNo)
    TextView carNo;
    @BindView(R.id.cpLoad_billNo)
    TextView billNo;
    @BindView(R.id.cpLoad_location)
    TextView location;
    @BindView(R.id.LoadList)
    ListView loadListView;
    private List<String> locationList;
    private List<CutPartsLoadInfo> loadInfoList;
    private CutPartsLoadInfo loadInfo;
    private CutPartsLoadAdapter loadAdapter;
    private LoadProgressDialog dialog;
    private int REQUEST_CODE_CAR=0;
    private int REQUEST_CODE_BILL=1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cut_parts_load;
    }

    @Override
    public void init() {
        setToolBarTitle("装车");
        location.setText("无");
        dialog=new LoadProgressDialog(this);
        loadInfoList=new ArrayList<>();
        locationList=new ArrayList<>();
        locationList.add("无");
        locationList.add("上");
        locationList.add("中");
        locationList.add("下");
        ZXingLibrary.initDisplayOpinion(this);
    }
    @OnClick(R.id.cpLoad_carNo)
    void GetCarNo(){
        Intent intent = new Intent(CutPartsLoadActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CAR);
    }
    @OnClick(R.id.cpLoad_billNo)
    void GetBillNo(){
        Intent intent = new Intent(CutPartsLoadActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_BILL);
    }
    @OnClick(R.id.cpLoad_location)
    void InputLocation(){
        int size = locationList.size();
        final String[] array = locationList.toArray(new String[size]);
        AlertDialog alertDialog = new AlertDialog.Builder(CutPartsLoadActivity.this)
                .setTitle("选择一个尺码")
                .setIcon(R.drawable.app_icon)
                .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {
                        location.setText(array[which]);
                    }
                }).create();
        alertDialog.show();
    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (carNo.getText().toString().isEmpty()){
//            carNo.setText(et_loadInfo.getText().toString().trim());
//            et_loadInfo.getText().clear();
//        }else if(billNo.getText().toString().isEmpty()){
//
//            getLoadCarInfo(et_loadInfo.getText().toString().trim());
//            billNo.setText(et_loadInfo.getText().toString().trim());
//            et_loadInfo.getText().clear();
//        }
//        return super.onKeyUp(keyCode, event);
//    }

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
                    Toast.makeText(CutPartsLoadActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }

        }
        if (requestCode == REQUEST_CODE_BILL) {
            //处理扫描结果
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("TAG","扫描结果="+result);
                    billNo.setText(result);
                    getLoadCarInfo(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(CutPartsLoadActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void getLoadCarInfo(final String orderCode) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CutPartsLoadActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_GetCutPiecesDetailByPullOrderCode",
                                "OrderCode="+orderCode,
                                String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","loadJson="+json);
                CutPartsLoadEntity cutPartsLoadEntity = JSON.parseObject(json, CutPartsLoadEntity.class);
                List<CutPartsLoadEntity.DATABean> data = cutPartsLoadEntity.getDATA();
                for (int i=0;i<data.size();i++){
                    loadInfo=new CutPartsLoadInfo();
                    loadInfo.SBILLNO=data.get(i).getSBILLNO();
                    loadInfo.SSUBFEPOCODE=data.get(i).getSSUBFEPOCODE();
                    loadInfo.SBEDNO=data.get(i).getSBEDNO();
                    loadInfo.SCOMBNAME=data.get(i).getSCOMBNAME();
                    loadInfo.QTY=data.get(i).getQTY();
                    loadInfoList.add(loadInfo);
                }
                loadAdapter=new CutPartsLoadAdapter(loadInfoList,getApplicationContext());
                loadListView.setAdapter(loadAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog1="+hsWebInfo.json);
                OthersUtil.showTipsDialog(CutPartsLoadActivity.this,hsWebInfo.error.error);
                billNo.setText("");
            }
        });
    }

    @OnClick(R.id.loadInfoUpdate)
    void upDate(){
        if (carNo.getText().toString().isEmpty()){
            OthersUtil.showTipsDialog(CutPartsLoadActivity.this,"裁片车号还未扫描!");
            return;
        }else {
            OthersUtil.showDoubleChooseDialog(CutPartsLoadActivity.this, "确认提交?", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    LoadInfoUpDate();
                }
            });

        }
    }

    private void LoadInfoUpDate() {

        final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()).toString();
        for (int i=0;i<loadInfoList.size();i++){
            final int finalI = i;
            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CutPartsLoadActivity.this,"")
                    .map(new Func1<String, HsWebInfo>() {
                        @Override
                        public HsWebInfo call(String s) {
                            return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_InsertDataToCutPiecesTransportDetail",
                                    "ItemID="+ UUID.randomUUID().toString()+
                                            ",FrameCode="+carNo.getText().toString().trim()+
                                            ",FrameState="+"1"+
                                            ",Layer="+location.getText().toString().trim()+
                                            ",PullOrderCode="+billNo.getText().toString().trim()+
                                            ",FEPOCode="+loadInfoList.get(finalI).SSUBFEPOCODE+
                                            ",BedNo="+loadInfoList.get(finalI).SBEDNO+
                                            ",CombName="+loadInfoList.get(finalI).SCOMBNAME+
                                            ",Quantity="+loadInfoList.get(finalI).QTY+
                                            ",CreateDate="+date+
                                            ",StorageLocation="+"",
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
                    carNo.setText("");
                    billNo.setText("");
                    loadInfoList.clear();
                    loadAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class CutPartsLoadAdapter extends HsBaseAdapter<CutPartsLoadInfo>{

        public CutPartsLoadAdapter(List<CutPartsLoadInfo> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null) convertView=mInflater.inflate(R.layout.cut_parts_load_item,viewGroup,false);
            TextView billNO = ViewHolder.get(convertView, R.id.load_BillNo);
            TextView po = ViewHolder.get(convertView, R.id.load_Po);
            TextView bedNO = ViewHolder.get(convertView, R.id.load_BedNo);
            TextView comb = ViewHolder.get(convertView, R.id.load_Comb);
            TextView qty = ViewHolder.get(convertView, R.id.load_Qty);
            billNO.setText(mList.get(position).SBILLNO);
            po.setText(mList.get(position).SSUBFEPOCODE);
            bedNO.setText(mList.get(position).SBEDNO);
            comb.setText(mList.get(position).SCOMBNAME);
            qty.setText(mList.get(position).QTY);
            return convertView;
        }
    }
}
