package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.check_quality.CutPiecesActivity;
import net.huansi.equipment.equipmentapp.activity.logging_bill.LoggingBillActivity;
import net.huansi.equipment.equipmentapp.activity.repair.RepairDetailActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.YieldEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.JSONEntity;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SewLineUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ShowYieldActivity extends BaseActivity {
    private LoadProgressDialog dialog;
    private List<String> groupList;
    private List<String> stateList;
    private int countIn=0;
    private int countOut=0;
    @BindView(R.id.tv_getYield)
    TextView group;
    @BindView(R.id.tv_yield)
    TextView yield;
    @BindView(R.id.tv_inOrOut)
    TextView inOrOut;

    @Override
    protected int getLayoutId() {
        return R.layout.show_yield_activity;
    }

    @Override
    public void init() {
        //setToolBarTitle("查看进出量");
        dialog=new LoadProgressDialog(this);
        groupList=new ArrayList<>();
        stateList=new ArrayList<>();
        stateList.add("进线量/IN");
        stateList.add("出线量/OUT");
        //getGroupInfo();
        showTargetField("1M-1");

        yield.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getGroupInfo();
                return true;
            }
        });


    }

    private void getGroupInfo() {
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppEPSewLine","",String.class.getName(),false,"获取班组产线");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                Log.e("TAG","success="+hsWebInfo.json);
                OthersUtil.dismissLoadDialog(dialog);
                String json = hsWebInfo.json;
                SewLineUtil sewLineUtil = JSON.parseObject(json, SewLineUtil.class);
                List<SewLineUtil.DATABean> data = sewLineUtil.getDATA();
                Log.e("TAG","data="+data);
                for (int i=0;i<data.size();i++){
                    groupList.add(data.get(i).getDEPTNAME());
                }
                if (groupList==null){
                    return;
                }
                int size = groupList.size();
                final String[] array = groupList.toArray(new String[size]);
                AlertDialog alertDialog = new AlertDialog.Builder(ShowYieldActivity.this)
                        .setTitle("选择某一个班组")
                        .setIcon(R.drawable.app_icon)
                        .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int which) {
                                refreshInfo(array[which]);
                            }
                        }).create();
                alertDialog.show();

            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error");
            }
        });
    }

    private void refreshInfo(final String group) {
        showTargetField(group);
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                showTargetField(group);
                handler.postDelayed(this,2000*60);
            }
        };
        handler.postDelayed(runnable,2000*60);
    }

    @OnClick(R.id.tv_inOrOut)
    void setInOrOut(){
        if (stateList==null){
            return;
        }
        int size = stateList.size();

        final String[] array = stateList.toArray(new String[size]);
        AlertDialog alertDialog = new AlertDialog.Builder(ShowYieldActivity.this)
                .setTitle("选择进出线量")
                .setIcon(R.drawable.app_icon)
                .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {

                        String s = array[which];
                        int i = s.lastIndexOf("/");
                        String state = s.substring(i+1, s.length());
                        inOrOut.setText(state);
                        Log.e("TAG","IO="+state);
                        showTargetField(group.getText().toString());
                    }
                }).create();
        alertDialog.show();
    }



    @OnClick(R.id.tv_getYield)
    void getYield(){
        if (groupList==null){
            return;
        }
        int size = groupList.size();

        final String[] array = groupList.toArray(new String[size]);
        AlertDialog alertDialog = new AlertDialog.Builder(ShowYieldActivity.this)
                .setTitle("选择一个产线")
                .setIcon(R.drawable.app_icon)
                .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {
                        group.setText(array[which]);
                        String s = array[which];

                        showTargetField(s);
                    }
                }).create();
        alertDialog.show();
    }

    private void showTargetField(final String Group) {

//        if (Group.equalsIgnoreCase("选择产线")){
//            OthersUtil.ToastMsg(this,"未选择产线!!");
//        }else {
            Log.e("TAG","group="+Group);
            OthersUtil.showLoadDialog(dialog);
            int i = Group.lastIndexOf("/");
            final String group = Group.substring(i+1, Group.length());
            final String statement = inOrOut.getText().toString();
            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                    .map(new Func1<String, HsWebInfo>() {
                        @Override
                        public HsWebInfo call(String s) {
                            return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","Mes_GetCurrentCapacity",
                                    "InOrOut="+"IN"+
                                            ",PostName="+group,String.class.getName(),false,"获取班组产线");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                @Override
                public void success(HsWebInfo hsWebInfo) {
                    Log.e("TAG","yieldIn="+hsWebInfo.json);
                    OthersUtil.dismissLoadDialog(dialog);
                    String json = hsWebInfo.json;
                    YieldEntity yieldEntity = JSON.parseObject(json, YieldEntity.class);
                    String data = yieldEntity.getDATA().get(0).getCOLUMN1();

                    countIn=Integer.parseInt(data);
                    yield.setText(data);
                }

                @Override
                public void error(HsWebInfo hsWebInfo) {
                    Log.e("TAG","error");
                }
            });



//        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
//                .map(new Func1<String, HsWebInfo>() {
//                    @Override
//                    public HsWebInfo call(String s) {
//                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","Mes_GetCurrentCapacity",
//                                "InOrOut="+"OUT"+
//                                        ",PostName="+group,String.class.getName(),false,"获取班组产线");
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
//            @Override
//            public void success(HsWebInfo hsWebInfo) {
//                Log.e("TAG","yieldOut="+hsWebInfo.json);
//                OthersUtil.dismissLoadDialog(dialog);
//                String json = hsWebInfo.json;
//                YieldEntity yieldEntity = JSON.parseObject(json, YieldEntity.class);
//                String data = yieldEntity.getDATA().get(0).getCOLUMN1();
//                countOut=Integer.parseInt(data);
//                //yield.setText(data);
//            }
//
//            @Override
//            public void error(HsWebInfo hsWebInfo) {
//                Log.e("TAG","error");
//            }
//        });
       // }

        //setColor();


    }

    private void setColor() {
        if (countIn-countOut<=20){
            yield.setTextColor(Color.WHITE);
            //yield.setText(countIn);
        }else if(countIn-countOut>20){
            yield.setTextColor(Color.RED);
            //yield.setText(countIn);
        }
    }
}
