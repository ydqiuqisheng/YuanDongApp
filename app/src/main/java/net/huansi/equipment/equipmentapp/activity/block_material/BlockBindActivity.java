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
import net.huansi.equipment.equipmentapp.activity.cut_parts.CutPartsLoadActivity;
import net.huansi.equipment.equipmentapp.activity.move_cloth.ClothPoActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.BlockBindEntity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.PrintSamplePo;
import net.huansi.equipment.equipmentapp.entity.SamplePrintPoInfo;
import net.huansi.equipment.equipmentapp.entity.blockBindInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BlockBindActivity extends BaseActivity{

    @BindView(R.id.block_billNo)
    TextView billNo;
    @BindView(R.id.block_shelfNo)
    TextView shelfNo;
    @BindView(R.id.Block_bindInfo_lv)
    ListView bindInfo_lv;
    @BindView(R.id.et_blockInfo)
    EditText blockInfo;
    private BlockBindEntity bindInfo;
    private List<BlockBindEntity> bindSumEntities;
    private List<BlockBindEntity> bindDtEntities;
    private LoadProgressDialog dialog;
    private BlockBindAdapter blockBindAdapter;
    private int REQUEST_CODE_BILL=0;
    private int REQUEST_CODE_SHELF=1;
    private boolean isoncl=true;
    private int POSITION=-1;
    private String Str;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_block_bind;
    }

    @Override
    public void init() {
        setToolBarTitle("绑定");
        dialog=new LoadProgressDialog(this);
        ZXingLibrary.initDisplayOpinion(this);
//        bindInfo_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }
    @OnClick(R.id.block_billNo)
    void GetBillNo(){
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_BILL);
    }
    @OnClick(R.id.block_shelfNo)
    void GetShelfNo(){
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SHELF);
    }

    //接收扫描二维码数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BILL) {
            //处理扫描结果
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING).trim();
                    Log.e("TAG","扫描结果="+result);
                    billNo.setText(result);
                    getSumInfo(result);
                    getDtInfo(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }

        }
        if (requestCode == REQUEST_CODE_SHELF) {
            //处理扫描结果
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING).trim();
                    Log.e("TAG","扫描结果="+result);
                    shelfNo.setText(result);

                    if (POSITION==-1){
                        return;
                    }else {
                        BlockBindEntity blockBindEntity = bindSumEntities.get(POSITION);
                        Str=Str+result+";";
                        blockBindEntity.SHELFNO=Str;
                        bindSumEntities.set(POSITION,blockBindEntity);
                        blockBindAdapter.notifyDataSetChanged();
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }

        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (billNo.getText().toString().isEmpty()){
            String s = blockInfo.getText().toString().trim();
            billNo.setText(s);
            Log.e("TAG","s="+s);
            blockInfo.getText().clear();
            getSumInfo(s);
            getDtInfo(s);
        }else {
            String s = blockInfo.getText().toString().trim();
            shelfNo.setText(s);
            blockInfo.getText().clear();
            if (POSITION==-1){
                if (bindSumEntities!=null){
                    for (int m=0;m<bindSumEntities.size();m++){
                        bindSumEntities.get(m).SHELFNO=s;
                    }
                    blockBindAdapter.notifyDataSetChanged();
                }


            }else {
                BlockBindEntity blockBindEntity = bindSumEntities.get(POSITION);
                Str=Str+s+";";
                blockBindEntity.SHELFNO=Str;
                bindSumEntities.set(POSITION,blockBindEntity);
                blockBindAdapter.notifyDataSetChanged();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @OnClick(R.id.blockInfoBind)
    void Bind(){

        if (shelfNo.getText().toString().isEmpty()){
            OthersUtil.ToastMsg(this,"货架未扫描无法绑定!!");
            return;
        }
        int s1 = bindSumEntities.size();
        final int s2 = bindDtEntities.size();
        for (int j=0;j<s1;j++){
            for (int k=0;k<s2;k++){
                if (bindDtEntities.get(k).SSUBFEPOCODE.equalsIgnoreCase(bindSumEntities.get(j).SSUBFEPOCODE)&&bindDtEntities.get(k).SMODIFYSIZE.equalsIgnoreCase(bindSumEntities.get(j).SMODIFYSIZE)){
                    //String shelfno = bindSumEntities.get(j).SHELFNO;
                    bindDtEntities.get(k).SHELFNO=bindSumEntities.get(j).SHELFNO;
                }
            }
        }

        OthersUtil.showDoubleChooseDialog(this, "确认绑定?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                OthersUtil.showLoadDialog(dialog);

//                if (isoncl==true){
//                    isoncl=false;
                    final String code = billNo.getText().toString().trim();
                    for (int i=0;i<s2;i++){
                        final BlockBindEntity blockInfo = bindDtEntities.get(i);
                        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(BlockBindActivity.this,"")
                                .map(new Func1<String, HsWebInfo>() {
                                    @Override
                                    public HsWebInfo call(String s) {
                                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_InsertDataToSplitsScaningDetail",
                                                "PullOrderCode="+code+
                                                        ",FEPOCode="+blockInfo.SSUBFEPOCODE+
                                                        ",BedNo="+blockInfo.SBEDNO+
                                                        ",CombName="+blockInfo.SCOMBNAME+
                                                        ",SizeName="+blockInfo.SMODIFYSIZE+
                                                        ",PackageNo="+blockInfo.SPACKAGENO+
                                                        ",BarCode="+blockInfo.SBARCODE+
                                                        ",Quantity="+blockInfo.QTY+
                                                        ",FrameCodeStr="+blockInfo.SHELFNO
                                                ,String.class.getName(),false,"上传拼块成功");
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                            @Override
                            public void success(HsWebInfo hsWebInfo) {
                                String json = hsWebInfo.json;
                                Log.e("TAG",json);
                                //isoncl=true;
                                POSITION=-1;
                                OthersUtil.dismissLoadDialog(dialog);
                                OthersUtil.ToastMsg(getContext(),"提交成功!");

                            }
                            @Override
                            public void error(HsWebInfo hsWebInfo) {
                                Log.e("TAG","errorLog2="+hsWebInfo.json);

                            }
                        });
                    }
                    billNo.setText("");
                    shelfNo.setText("");
                    bindSumEntities.clear();
                    bindDtEntities.clear();
                    blockBindAdapter.notifyDataSetChanged();
               // }
            }
        });

    }


    @OnItemClick(R.id.Block_bindInfo_lv)
    void OnClick(int position){

        POSITION=position;
        Str="";
        if (bindDtEntities.isEmpty()){
            getDtInfo(billNo.getText().toString().trim());
        }
        if (bindSumEntities.isEmpty()){
            getSumInfo(billNo.getText().toString().trim());
        }
//        BlockBindEntity blockBindEntity = bindEntities.get(position);
//        String location="";
//
//        blockBindEntity.SHELFNO="jk";
//        bindEntities.set(position,blockBindEntity);
//        blockBindAdapter.notifyDataSetChanged();

    }




    private void getDtInfo(final String orderCode) {
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_GetSplitsDetailByPullOrderCode",
                                "OrderCode="+orderCode+
                                        ",Type="+"detail"
                                ,String.class.getName(),false,"明细获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG666",json);
                OthersUtil.dismissLoadDialog(dialog);
                blockBindInfo blockBindInfo = JSON.parseObject(json, blockBindInfo.class);
                List<blockBindInfo.DATABean> data = blockBindInfo.getDATA();
                bindDtEntities=new ArrayList<>();
                for (int i=0;i<data.size();i++){
                    bindInfo=new BlockBindEntity();
                    bindInfo.SBEDNO=data.get(i).getSBEDNO();
                    bindInfo.SSUBFEPOCODE=data.get(i).getSSUBFEPOCODE();
                    bindInfo.SCOMBNAME=data.get(i).getSCOMBNAME();
                    bindInfo.SPACKAGENO=data.get(i).getSPACKAGENO();
                    bindInfo.SBARCODE=data.get(i).getSBARCODE();
                    bindInfo.PACKAGECOUNT=data.get(i).getPACKAGECOUNT();
                    bindInfo.SMODIFYSIZE=data.get(i).getSMODIFYSIZE();
                    bindInfo.QTY=data.get(i).getQTY();
                    bindInfo.SHELFNO="";
                    bindDtEntities.add(bindInfo);
                }
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
                billNo.setText("");
                shelfNo.setText("");
                OthersUtil.ToastMsg(getContext(),"此拉布单已装车");
            }
        });
    }






    private void getSumInfo(final String orderCode) {
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_GetSplitsDetailByPullOrderCode",
                                "OrderCode="+orderCode+
                                        ",Type="+"sum"
                                ,String.class.getName(),false,"明细获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG666",json);
                OthersUtil.dismissLoadDialog(dialog);
                blockBindInfo blockBindInfo = JSON.parseObject(json, blockBindInfo.class);
                List<blockBindInfo.DATABean> data = blockBindInfo.getDATA();
                bindSumEntities=new ArrayList<>();
                for (int i=0;i<data.size();i++){
                    bindInfo=new BlockBindEntity();

                    bindInfo.SSUBFEPOCODE=data.get(i).getSSUBFEPOCODE();
                    bindInfo.PACKAGECOUNT=data.get(i).getPACKAGECOUNT();
                    bindInfo.SMODIFYSIZE=data.get(i).getSMODIFYSIZE();
                    bindInfo.QTY=data.get(i).getQTY();
                    bindInfo.SHELFNO="";
                    bindSumEntities.add(bindInfo);
                }

                blockBindAdapter=new BlockBindAdapter(bindSumEntities,getContext());
                bindInfo_lv.setAdapter(blockBindAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
                billNo.setText("");
                shelfNo.setText("");
                OthersUtil.ToastMsg(getContext(),"此拉布单已装车");
            }
        });
    }

    private class BlockBindAdapter extends HsBaseAdapter<BlockBindEntity>{
        public BlockBindAdapter(List<BlockBindEntity> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null) convertView=mInflater.inflate(R.layout.activity_block_bind_item,parent,false);
            TextView fepo = ViewHolder.get(convertView, R.id.block_fepo);//PO
            TextView pacCo = ViewHolder.get(convertView, R.id.block_pacCo);//包数
            TextView size = ViewHolder.get(convertView, R.id.block_size);//尺码
            TextView qty = ViewHolder.get(convertView, R.id.block_qty);//件数
            TextView add = ViewHolder.get(convertView, R.id.block_shelfNo);//架号


            pacCo.setText(mList.get(position).PACKAGECOUNT);
            fepo.setText(mList.get(position).SSUBFEPOCODE);
            size.setText(mList.get(position).SMODIFYSIZE);
            add.setText(mList.get(position).SHELFNO);
            qty.setText(mList.get(position).QTY);
            return convertView;
        }
    }
}
