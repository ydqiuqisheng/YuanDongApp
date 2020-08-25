package net.huansi.equipment.equipmentapp.activity.cut_parts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.CutPartsQueryEntity;
import net.huansi.equipment.equipmentapp.entity.CutPartsQueryInfo;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CutPartsQueryActivity extends BaseActivity {
    @BindView(R.id.cpQuery_Po)
    EditText queryPo;
    @BindView(R.id.cpQuery_bedNo)
    EditText queryBed;
    @BindView(R.id.cpQuery_group)
    EditText queryGroup;
    @BindView(R.id.QueryList)
    ListView queryList;
    @BindView(R.id.et_QueryCode)
    EditText queryCode;
    private CutPartsQueryAdapter queryAdapter;
    private List<CutPartsQueryInfo> queryInfoList;
    private CutPartsQueryInfo queryInfo;
    private LoadProgressDialog dialog;
    private int REQUEST_CODE=1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cut_parts_query;
    }

    @Override
    public void init() {
        setToolBarTitle("查询");
        dialog=new LoadProgressDialog(this);
        queryInfoList=new ArrayList<>();
        ZXingLibrary.initDisplayOpinion(getApplicationContext());
        queryAdapter=new CutPartsQueryAdapter(queryInfoList,getApplicationContext());
        queryList.setAdapter(queryAdapter);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (queryCode.getText().toString().isEmpty()){
            Log.e("TAG","kong");
        }else {
            getInfoByCode(queryCode.getText().toString());
            Log.e("TAG","tag"+queryCode.getText().toString());
        }

        return super.onKeyUp(keyCode, event);
    }


    @OnClick(R.id.cpQueryByPo)
    void ByPo(){
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CutPartsQueryActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_ReportCutPiecesTransport",
                                "FEPO="+queryPo.getText().toString().trim()+
                                        ",BedNo="+queryBed.getText().toString().trim()+
                                ",Post="+queryGroup.getText().toString().trim(),
                                String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","queryJson="+json);
                CutPartsQueryEntity cutPartsQueryEntity = JSON.parseObject(json, CutPartsQueryEntity.class);
                List<CutPartsQueryEntity.DATABean> data = cutPartsQueryEntity.getDATA();
                queryInfoList.clear();
                for (int i=0;i<data.size();i++){
                    queryInfo=new CutPartsQueryInfo();
                    queryInfo.FEPOCODE=data.get(i).getFEPOCODE();
                    queryInfo.BEDNO=data.get(i).getBEDNO();
                    queryInfo.COMBNAME=data.get(i).getCOMBNAME();
                    queryInfo.QUANTITY=data.get(i).getQUANTITY();
                    queryInfo.STORAGELOCATION=data.get(i).getSTORAGELOCATION();
                    queryInfo.POSTNAME=data.get(i).getPOSTNAME();
                    queryInfo.LAYER=data.get(i).getLAYER();
                    queryInfo.RECEIVEQTY=data.get(i).getRECEIVEQTY();
                    queryInfoList.add(queryInfo);
                }
                queryAdapter.notifyDataSetChanged();
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorBind="+hsWebInfo.json);
                queryAdapter.notifyDataSetChanged();
                OthersUtil.ToastMsg(getApplicationContext(),"没有符合条件的结果");
            }
        });
    }
    @OnClick(R.id.cpQueryByBarcode)
    void ByCode(){
        Intent intent = new Intent(CutPartsQueryActivity.this, CaptureActivity.class);
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
                    getInfoByCode(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(CutPartsQueryActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void getInfoByCode(final String result) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CutPartsQueryActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_ReportCutPiecesTransportByBarCode",
                                "BarCode="+result,
                                String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","QueryJson="+json);
                queryCode.getText().clear();
                CutPartsQueryEntity cutPartsQueryEntity = JSON.parseObject(json, CutPartsQueryEntity.class);
                List<CutPartsQueryEntity.DATABean> data = cutPartsQueryEntity.getDATA();
                queryInfoList.clear();
                for (int i=0;i<data.size();i++){
                    queryInfo=new CutPartsQueryInfo();
                    queryInfo.FEPOCODE=data.get(i).getFEPOCODE();
                    queryInfo.BEDNO=data.get(i).getBEDNO();
                    queryInfo.COMBNAME=data.get(i).getCOMBNAME();
                    queryInfo.QUANTITY=data.get(i).getQUANTITY();
                    queryInfo.STORAGELOCATION=data.get(i).getSTORAGELOCATION();
                    queryInfo.POSTNAME=data.get(i).getPOSTNAME();
                    queryInfo.LAYER=data.get(i).getLAYER();
                    queryInfo.RECEIVEQTY=data.get(i).getRECEIVEQTY();
                    queryInfoList.add(queryInfo);
                }
                queryAdapter.notifyDataSetChanged();
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorBind="+hsWebInfo.json);
            }
        });
    }

    private class CutPartsQueryAdapter extends HsBaseAdapter<CutPartsQueryInfo>{
        public CutPartsQueryAdapter(List<CutPartsQueryInfo> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null) convertView=mInflater.inflate(R.layout.cut_parts_query_item,viewGroup,false);
            TextView po = ViewHolder.get(convertView, R.id.cpQuery_Po);
            TextView bed = ViewHolder.get(convertView, R.id.cpQuery_bedNo);
            TextView comb = ViewHolder.get(convertView, R.id.cpQuery_Comb);
            TextView qty = ViewHolder.get(convertView, R.id.cpQuery_Qty);
            TextView location = ViewHolder.get(convertView, R.id.cpQuery_location);
            TextView group = ViewHolder.get(convertView, R.id.cpQuery_group);
            TextView layer = ViewHolder.get(convertView, R.id.cpQuery_Layer);
            TextView returnQty = ViewHolder.get(convertView, R.id.cpQuery_return);
            CutPartsQueryInfo cpInfo = mList.get(position);
            po.setText(cpInfo.FEPOCODE);
            bed.setText(cpInfo.BEDNO);
            comb.setText(cpInfo.COMBNAME);
            qty.setText(cpInfo.QUANTITY);
            location.setText(cpInfo.STORAGELOCATION);
            group.setText(cpInfo.POSTNAME);
            layer.setText(cpInfo.LAYER);
            returnQty.setText(cpInfo.RECEIVEQTY);
            return convertView;
        }
    }
}
