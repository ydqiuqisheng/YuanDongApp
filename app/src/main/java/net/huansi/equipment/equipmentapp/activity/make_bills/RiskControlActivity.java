package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.twelvemonkeys.util.Time;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.cut_parts.CutPartsLoadActivity;
import net.huansi.equipment.equipmentapp.entity.CutPartsLoadEntity;
import net.huansi.equipment.equipmentapp.entity.CutPartsLoadInfo;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.RiskControlEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RiskControlActivity extends BaseActivity {
    @BindView(R.id.riskSeason)
    TextView season;
    @BindView(R.id.risk_po)
    TextView fepo;
    @BindView(R.id.risk_accessory)
    TextView accessory;
    @BindView(R.id.risk_style)
    TextView style;
    @BindView(R.id.risk_describe)
    TextView describe;
    @BindView(R.id.risk_size)
    TextView size;
    @BindView(R.id.risk_washCondition)
    TextView washCondition;
    @BindView(R.id.risk_comment)
    TextView comment;
    @BindView(R.id.risk_conclusion)
    TextView conclusion;
    @BindView(R.id.riskYear)
    EditText Year;
    private List<String> seasonList;
    private LoadProgressDialog dialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_risk_control;
    }

    @Override
    public void init() {
        setToolBarTitle("销样风险管控表");
        dialog=new LoadProgressDialog(this);
        seasonList=new ArrayList<>();
        accessory.setMovementMethod(ScrollingMovementMethod.getInstance());
        style.setMovementMethod(ScrollingMovementMethod.getInstance());
        describe.setMovementMethod(ScrollingMovementMethod.getInstance());
        washCondition.setMovementMethod(ScrollingMovementMethod.getInstance());
        comment.setMovementMethod(ScrollingMovementMethod.getInstance());
        conclusion.setMovementMethod(ScrollingMovementMethod.getInstance());
        size.setMovementMethod(ScrollingMovementMethod.getInstance());
        String year = new SimpleDateFormat("yyyy").format(new Date()).toString();
        String subYear = year.substring(2, 4);
        seasonList.add("SP");
        seasonList.add("SU");
        seasonList.add("FA");
        seasonList.add("HO");

    }



    @OnClick(R.id.riskSeason)
    void showSeason(){
        int size = seasonList.size();
        final String[] array = seasonList.toArray(new String[size]);
        AlertDialog alertDialog = new AlertDialog.Builder(RiskControlActivity.this)
                .setTitle("选择一个尺码")
                .setIcon(R.drawable.app_icon)
                .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {
                        season.setText(array[which]);
                    }
                }).create();
        alertDialog.show();
    }

    @OnClick(R.id.riskQuery)
    void queryTable(){
        if (season.getText().toString().isEmpty()){
            OthersUtil.ToastMsg(RiskControlActivity.this,"季节为空!!");
            return;
        }
        final String billNumber = getIntent().getStringExtra("BillNumber");
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(RiskControlActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","GetPDMSampleRiskImport",
                                "FEPO="+billNumber+
                                ",Season="+season.getText().toString().trim()+
                                ",Years="+Year.getText().toString().trim(),
                                String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","riskJson="+json);
                RiskControlEntity risk = JSON.parseObject(json, RiskControlEntity.class);
                fepo.setText(risk.getDATA().get(0).getFEPO());
                accessory.setText(risk.getDATA().get(0).getACCESSORIES());
                style.setText(risk.getDATA().get(0).getSTYLE());
                describe.setText(risk.getDATA().get(0).getPRODUCTION());
                size.setText(risk.getDATA().get(0).getSIZE());
                washCondition.setText(risk.getDATA().get(0).getWASHCONDITIONS());
                comment.setText(risk.getDATA().get(0).getGUESTCOMMENT());
                conclusion.setText(risk.getDATA().get(0).getCONCLUSION());
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
                String json = hsWebInfo.json;

//                RiskControlEntity risk = JSON.parseObject(json, RiskControlEntity.class);
//                List<RiskControlEntity.DATABean> data = risk.getDATA();
//
//                fepo.setText(data.get(0).getFEPO());
//                accessory.setText(data.get(0).getACCESSORIES());
//                style.setText(data.get(0).getSTYLE());
//                describe.setText(data.get(0).getPRODUCTION());
//                size.setText(data.get(0).getSIZE());
//                washCondition.setText(data.get(0).getWASHCONDITIONS());
//                comment.setText(data.get(0).getGUESTCOMMENT());
//                conclusion.setText(data.get(0).getCONCLUSION());
            }
        });
    }
}
