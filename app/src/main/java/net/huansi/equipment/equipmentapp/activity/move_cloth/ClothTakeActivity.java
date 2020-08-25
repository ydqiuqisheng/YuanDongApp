package net.huansi.equipment.equipmentapp.activity.move_cloth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.SimpleEntity;
import net.huansi.equipment.equipmentapp.entity.SimpleProvider;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.ROLE_CODE_KEY;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class ClothTakeActivity extends BaseActivity {
    private int REQUEST_CODE=1;
    private int SELECT_COUNT=1;
    private String UUID="";
    private LoadProgressDialog dialog;
    @BindView(R.id.take_time)
    TextView takeTime;
    @BindView(R.id.take_leibie)
    TextView takeLeiBie;
    @BindView(R.id.take_kuanhao)
    TextView takeKuanHao;

    @BindView(R.id.take_sehao)
    TextView takeSeHao;
    @BindView(R.id.take_styleNo)
    TextView takeStyleNo;
    @BindView(R.id.take_chima)
    TextView takeChiMa;
    @BindView(R.id.take_jijie)
    TextView takeJiJie;

    @BindView(R.id.take_xianghao)
    TextView takeXiangHao;
    @BindView(R.id.takeClothNumber)
    TextView takeClothNumber;
    @BindView(R.id.take_submitter)
    TextView takeSubmitter;
    @BindView(R.id.take_provider)
    Spinner takeProvider;
    private ArrayAdapter providerAdapter;
    private List<String> providerList;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cloth_take;
    }

    @Override
    public void init() {
        setToolBarTitle("样衣借取");
        dialog=new LoadProgressDialog(this);
        ZXingLibrary.initDisplayOpinion(this);
        String roleCode=SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString().toUpperCase();
        takeSubmitter.setText(roleCode);
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()).toString();
        takeTime.setText(data);

    }
    @OnItemSelected(R.id.take_provider)
    void numberBack(){
        takeClothNumber.setText("1");
    }
    private void initProvider(final String result){
            Log.e("TAG","肥嘟嘟");
        providerList=new ArrayList<>();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GteSampleLeftDtl",
                                "uuID="+result,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","providerJson="+json);
                SimpleProvider simpleProvider = JSON.parseObject(json, SimpleProvider.class);
                List<SimpleProvider.DATABean> data = simpleProvider.getDATA();
                for (int i=0;i<data.size();i++){
                    providerList.add(data.get(i).getUSERID()+"可提供"+data.get(i).getQTY()+"件");
                }
                providerAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.string_item,R.id.text,providerList);
                takeProvider.setAdapter(providerAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);


            }
        });
    }
    @OnClick(R.id.bt_simpleClothQrCode)
    void simpleClothQrCode(){
        Intent intent = new Intent(ClothTakeActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    @OnClick(R.id.submit_printInfo)
    void submitInfo(){
        if (takeProvider.getSelectedItem().toString().isEmpty()){
            OthersUtil.showTipsDialog(this,"还未选择提供者工号");
        }else {
            String s = takeProvider.getSelectedItem().toString();
            String[] split = s.split("\\-");
            final String sourceId = split[0];
            Log.e("TAG","工号="+split[0]);
            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                    .map(new Func1<String, HsWebInfo>() {
                        @Override
                        public HsWebInfo call(String s) {
                            return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_SampleTranferSubmit",
                                    "uuID="+UUID+
                                    ",SourceID="+sourceId+
                                    ",AimUserID="+takeSubmitter.getText().toString()+
                                    ",Qty="+Integer.parseInt(takeClothNumber.getText().toString()),String.class.getName(),false,"组别获取成功");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                @Override
                public void success(HsWebInfo hsWebInfo) {
                    String json = hsWebInfo.json;
                    Log.e("TAG","submitJson="+json);

                }
                @Override
                public void error(HsWebInfo hsWebInfo) {
                    Log.e("TAG","error="+hsWebInfo.json);
                    OthersUtil.showTipsDialog(ClothTakeActivity.this, "提交完毕", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(ClothTakeActivity.this,ClothMoveMainActivity.class));
                        }
                    });

                }
            });
        }
    }
    @OnClick(R.id.bt_less)
    void numberMinus(){
        if (takeClothNumber.getText().toString().equalsIgnoreCase("1")){
            OthersUtil.showTipsDialog(ClothTakeActivity.this,"最少一件");
        }else {
            SELECT_COUNT = Integer.parseInt(takeClothNumber.getText().toString());
            SELECT_COUNT--;
            takeClothNumber.setText(SELECT_COUNT+"");
        }
    }

    @OnClick(R.id.bt_more)
    void numberPlus(){
        if (takeKuanHao.getText().toString().isEmpty()){
            return;
        }
        List<String> digitList = new ArrayList<String>();
        String str = takeProvider.getSelectedItem().toString();
        for(String sss:str.replaceAll("[^0-9]", ",").split(",")){
            if (sss.length()>0)
                digitList.add(sss);
        }
        Log.e("TAG","数字是"+digitList);
        String s = digitList.get(digitList.size() - 1);
        //String substring = takeProvider.getSelectedItem().toString().substring(length - 2, length - 1);
        int point = Integer.parseInt(takeClothNumber.getText().toString());
        if (point<Integer.parseInt(s)){
            SELECT_COUNT = Integer.parseInt(takeClothNumber.getText().toString());
            SELECT_COUNT++;
            takeClothNumber.setText(SELECT_COUNT+"");
        }else {
            OthersUtil.showTipsDialog(this,"最多选择"+s+"件！");
        }

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
                    UUID=result;
                   getClothInfo(result);
                   initProvider(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(ClothTakeActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getClothInfo(final String result) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GteSampleTranferLog ",
                                "uuID="+result,String.class.getName(),false,"获取失败");
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
                SimpleEntity.DATABean dataBean = data.get(data.size() - 1);
                takeLeiBie.setText(dataBean.getSAMPLETYPE());
                takeKuanHao.setText(dataBean.getCOMPANYSTYLE());
                //takeDanHao.setText(dataBean.getBILLNO());
                takeSeHao.setText(dataBean.getCOMB());
                takeStyleNo.setText(dataBean.getSTYLENO());
                takeChiMa.setText(dataBean.getSIZES());
                takeJiJie.setText(dataBean.getSEASON());
                //takeJiBie.setText(dataBean.getSAMPLELEVEL());
                takeXiangHao.setText(dataBean.getBOXNO());
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });
    }
}
