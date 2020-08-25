package net.huansi.equipment.equipmentapp.activity.move_cloth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import net.huansi.equipment.equipmentapp.entity.ClothMoveRecords;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.SimpleEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
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

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class ClothQueryRecordActivity extends BaseActivity {
    @BindView(R.id.lv_clothQueryList)
    ListView clothQueryList;
    @BindView(R.id.simpleStyleNo)
    EditText simpleStyleNo;
    @BindView(R.id.liuzhuan_result)
    TextView liuzhuan_result;
    @BindView(R.id.simple_about)
    ListView simple_about;
    private List<String> aboutList;
    private List<String> uuidList;
    private ArrayAdapter<String> aboutAdapter;
    private LoadProgressDialog dialog;
    private ClothRecordAdapter clothRecordAdapter;
    private ClothMoveRecords moveRecords;
    private List<ClothMoveRecords> recordsList;
    private int REQUEST_CODE=1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cloth_query_record;
    }

    @Override
    public void init() {
        setToolBarTitle("流转查询");
        ZXingLibrary.initDisplayOpinion(this);
        dialog=new LoadProgressDialog(this);
        recordsList=new ArrayList<>();
        uuidList=new ArrayList<>();
        aboutList=new ArrayList<>();
        aboutAdapter=new ArrayAdapter<>(ClothQueryRecordActivity.this,R.layout.string_item,R.id.text,aboutList);

    }
    //调起二维码扫描
    @OnClick(R.id.scanUUID)
    void scanUUID(){
        Intent intent = new Intent(ClothQueryRecordActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.querySimpleRecord)
    void querySimpleRecord(){
        String result = simpleStyleNo.getText().toString();

        getClothInfo(result);
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
                    getClothInfo(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(ClothQueryRecordActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getClothInfo(final String result) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GteSampleTranferLog",
                                "uuID="+result,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","takeJson="+json);
                SimpleEntity simpleEntity = JSON.parseObject(json, SimpleEntity.class);
                List<SimpleEntity.DATABean> data = simpleEntity.getDATA();
                String rt = data.get(0).getRESULT();

                String[] split = rt.split("\\/");

                String s="";
                for (int i=0;i<split.length;i++){
                    s=s+"&&"+split[i];
                }
                liuzhuan_result.setText(s);
                aboutList.clear();
                uuidList.clear();
                for (int i=0;i<data.size();i++){
                    uuidList.add(data.get(i).getUUID());
                    aboutList.add("登记人:"+"  "+data.get(i).getCREATEUSERID()+"  "+"款号:"+"  "+data.get(i).getCOMPANYSTYLE()+"\n"+
                            "季节:"+"  "+data.get(i).getSEASON()+"  "+"尺码:"+"  "+data.get(i).getSIZES()+"  "+"箱号:"+"  "+data.get(i).getBOXNO()+"\n"+
                            "色号:"+"  "+data.get(i).getCOMB()+"  "+"sNo:"+"  "+data.get(i).getSTYLENO()+"  "+"单号:"+"  "+data.get(i).getBILLNO());
                }

                simple_about.setAdapter(aboutAdapter);

                recordsList.clear();
                for (int i=0;i<data.size();i++){
                    moveRecords=new ClothMoveRecords();
                    moveRecords.AIMUSERID=data.get(i).getAIMUSERID();
                    moveRecords.SOURCEID=data.get(i).getSOURCEID();
                    moveRecords.QTY=data.get(i).getQTY();
                    moveRecords.LOGDATE=data.get(i).getLOGDATE();
                    moveRecords.RESULT=data.get(i).getRESULT();
                    moveRecords.SUSERNO=data.get(i).getSUSERNO();
                    recordsList.add(moveRecords);
                }
                clothRecordAdapter=new ClothRecordAdapter(recordsList,getApplicationContext());
                clothQueryList.setAdapter(clothRecordAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });
    }


@OnItemClick(R.id.simple_about)
void deleteRecord(final int position){
    final String UserID = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
    if (recordsList.get(position).SUSERNO.equalsIgnoreCase(UserID)){
        OthersUtil.showDoubleChooseDialog(ClothQueryRecordActivity.this, "确认删除该条信息吗,删除后之前的流转记录将清空需重新录入", null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int i) {
                        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ClothQueryRecordActivity.this,"")
                                .map(new Func1<String, HsWebInfo>() {
                                    @Override
                                    public HsWebInfo call(String s) {
                                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_SampleDelete",
                                                "uuID="+uuidList.get(position)+
                                                        ",CreateUserID="+UserID,String.class.getName(),false,"组别获取成功");
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                            @Override
                            public void success(HsWebInfo hsWebInfo) {
                                String json = hsWebInfo.json;
                                OthersUtil.ToastMsg(ClothQueryRecordActivity.this,"删除成功!");
                                Log.e("TAG","takeJson="+json);
                                getClothInfo(simpleStyleNo.getText().toString());
//                            aboutList.remove(position);
//                            aboutAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void error(HsWebInfo hsWebInfo) {
                                Log.e("TAG","error="+hsWebInfo.json);
                            }
                        });
                    }
                });
    }else {
        OthersUtil.showTipsDialog(ClothQueryRecordActivity.this,"您无权删除信息");
    }
}


    private class ClothRecordAdapter extends HsBaseAdapter<ClothMoveRecords>{
        public ClothRecordAdapter(List<ClothMoveRecords> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null)  convertView=mInflater.inflate(R.layout.activity_cloth_query_record_item,viewGroup,false);
            TextView show_time = ViewHolder.get(convertView, R.id.show_time);
            TextView clothMoveRecord = ViewHolder.get(convertView, R.id.clothMoveRecord);
            ClothMoveRecords clothMoveRecords = mList.get(position);
            show_time.setText(clothMoveRecords.LOGDATE);
            clothMoveRecord.setText(clothMoveRecords.AIMUSERID+" "+"从"+clothMoveRecords.SOURCEID+" "+"处拿走"+" "+clothMoveRecords.QTY+" "+"件");
            return convertView;
        }
    }
}
