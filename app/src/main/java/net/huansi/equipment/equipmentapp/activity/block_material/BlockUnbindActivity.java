package net.huansi.equipment.equipmentapp.activity.block_material;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.BlockBindEntity;
import net.huansi.equipment.equipmentapp.entity.BlockUnbindEntity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.blockBindInfo;
import net.huansi.equipment.equipmentapp.entity.blockUnbindInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SewLineUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BlockUnbindActivity extends BaseActivity{
    @BindView(R.id.block_pacNo)
    TextView pacNo;
    @BindView(R.id.Block_unbindInfo_lv)
    ListView unBindInfo_lv;
    @BindView(R.id.et_unblockInfo)
    EditText unblockInfo;
    @BindView(R.id.unBind_queryGroup)
    Spinner group;
    private List<BlockUnbindEntity> unBindEntities;
    private BlockUnbindEntity unBindInfo;
    private BlockUnbindAdapter blockUnbindAdapter;
    private LoadProgressDialog dialog;
    private boolean isoncl=true;
    private int REQUEST_CODE_PAC=1;
    private List<String> untieCodes;//解绑时需要传的条码号集合
    private List<String>  groupList;
    private ArrayAdapter groupAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_block_unbind;
    }

    @Override
    public void init() {
        setToolBarTitle("解绑");
        dialog=new LoadProgressDialog(this);
        unBindEntities=new ArrayList<>();
        untieCodes=new ArrayList<>();
        ZXingLibrary.initDisplayOpinion(this);
        initGroup();
    }

    private void initGroup() {
        groupList=new ArrayList();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppEPSewLine","",String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","groupJson="+json);
                SewLineUtil sewLineUtil = JSON.parseObject(json, SewLineUtil.class);
                List<SewLineUtil.DATABean> data = sewLineUtil.getDATA();
                for (int i=0;i<data.size();i++){
                    groupList.add(i,data.get(i).getDEPTNAME());
                }
                groupAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.string_item,R.id.text,groupList);
                group.setAdapter(groupAdapter);
                //group.setSelection(groupList.size()-1);
                group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Log.e("TAG","selectedPosition="+group.getSelectedItem().toString());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error");
            }
        });
    }


    @OnClick(R.id.block_pacNo)
    void scanPacNo(){
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PAC);
    }

    //接收扫描二维码数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAC) {
            //处理扫描结果
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("TAG","扫描结果="+result);
                    pacNo.setText(result);
                    showDtInfo(result.trim());
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (pacNo.getText().toString().isEmpty()){
            String s = unblockInfo.getText().toString().trim();
            pacNo.setText(s);
            unblockInfo.getText().clear();
            showDtInfo(s);
        }
        return super.onKeyUp(keyCode, event);
    }

    private void showDtInfo(final String barcode) {
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(BlockUnbindActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_GetSplitsScaningDetail",
                                "BarCode="+barcode
                                ,String.class.getName(),false,"上传拼块成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG",json);
                OthersUtil.dismissLoadDialog(dialog);
                blockUnbindInfo unbindInfo = JSON.parseObject(json, blockUnbindInfo.class);
                List<blockUnbindInfo.DATABean> data = unbindInfo.getDATA();
                pacNo.setText("");
                for (int i=0;i<data.size();i++){
                    unBindInfo=new BlockUnbindEntity();
                    unBindInfo.BARCODE=data.get(i).getBARCODE();
                    untieCodes.add(unBindInfo.BARCODE);
                    unBindInfo.PACKAGENO=data.get(i).getPACKAGENO();
                    unBindInfo.FEPOCODE=data.get(i).getFEPOCODE();
                    unBindInfo.COMBNAME=data.get(i).getCOMBNAME();
                    unBindInfo.SIZENAME=data.get(i).getSIZENAME();
                    unBindInfo.QUANTITY=data.get(i).getQUANTITY();
                    unBindInfo.FRAMECODESTR=data.get(i).getFRAMECODESTR();
                    unBindEntities.add(unBindInfo);
               }

                blockUnbindAdapter=new BlockUnbindAdapter(unBindEntities,getContext());
                unBindInfo_lv.setAdapter(blockUnbindAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
                OthersUtil.ToastMsg(getContext(),"未查到此包号");
            }
        });
    }

    @OnClick(R.id.blockInfoUnbind)
    void upState(){
        Set set = new HashSet();
        List<String> NewList = new ArrayList<>();
        set.addAll(untieCodes);
        NewList.addAll(set);
        int size = NewList.size();
        String barcodes="";
        for (int i=0;i<size;i++){
            barcodes=barcodes+NewList.get(i).toString().trim()+"/";
        }
        int position = group.getSelectedItemPosition();
        String s = groupList.get(position);

        final String ss = s.substring((s.indexOf("/"))+1);
        Log.e("TAG","班组："+ss);

        final String finalBarcodes = barcodes;
        OthersUtil.showDoubleChooseDialog(this, "确认提交?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                OthersUtil.showLoadDialog(dialog);
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(BlockUnbindActivity.this,"")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_TakeOutSplitsFromFrame",
                                        "BarCode="+ finalBarcodes+
                                                ",PostName="+ss
                                        ,String.class.getName(),false,"解绑拼块成功");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        Log.e("TAG",json);
                        OthersUtil.dismissLoadDialog(dialog);
                    }
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG","errorLog2="+hsWebInfo.json);
                    }
                });
            }
        });
        untieCodes.clear();//上传完成以后清空
        blockUnbindAdapter.notifyDataSetChanged();
    }



    private class BlockUnbindAdapter extends HsBaseAdapter<BlockUnbindEntity>{

        public BlockUnbindAdapter(List<BlockUnbindEntity> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null) convertView=mInflater.inflate(R.layout.activity_block_unbind_item,viewGroup,false);
            TextView barcode = ViewHolder.get(convertView, R.id.unBlock_barcode);
            TextView pacNo = ViewHolder.get(convertView, R.id.unBlock_pacNo);
            TextView frameNo = ViewHolder.get(convertView, R.id.unBlock_frame);
            TextView fepo = ViewHolder.get(convertView, R.id.unBlock_fepo);
            TextView comb = ViewHolder.get(convertView, R.id.unBlock_comb);
            TextView size = ViewHolder.get(convertView, R.id.unBlock_size);
            TextView qty = ViewHolder.get(convertView, R.id.unBlock_qty);
            barcode.setText(mList.get(position).BARCODE);
            pacNo.setText(mList.get(position).PACKAGENO);
            frameNo.setText(mList.get(position).FRAMECODESTR);
            fepo.setText(mList.get(position).FEPOCODE);
            comb.setText(mList.get(position).COMBNAME);
            size.setText(mList.get(position).SIZENAME);
            qty.setText(mList.get(position).QUANTITY);
            return convertView;
        }
    }
}
