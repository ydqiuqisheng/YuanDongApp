package net.huansi.equipment.equipmentapp.activity.check_simple;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.move_cloth.ClothMoveMainActivity;
import net.huansi.equipment.equipmentapp.activity.move_cloth.ClothQueryRecordActivity;
import net.huansi.equipment.equipmentapp.entity.CheckUser;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.SimplePendMeasureEntity;
import net.huansi.equipment.equipmentapp.entity.SimpleUnMeasureUtils;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class CheckSimpleDepartmentActivity extends BaseActivity {
    private List<String> checker;
    private List<String> monitor;
    private LoadProgressDialog dialog;
    private String user;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_simple_department;
    }

    @Override
    public void init() {
        setToolBarTitle("选择部门");
        dialog=new LoadProgressDialog(this);
        checker=new ArrayList<>();
        monitor=new ArrayList<>();
        OthersUtil.showLoadDialog(dialog);
        getCheckerInfo();
        getMonitorInfo();
    }

    private void getMonitorInfo() {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","proc_SampleCheckUser",
                                "CheckGroupIndex="+"2",String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                OthersUtil.dismissLoadDialog(dialog);
                String json = hsWebInfo.json;
                CheckUser checkUser = JSON.parseObject(json, CheckUser.class);
                List<CheckUser.DATABean> data = checkUser.getDATA();
                for(int i=0;i<data.size();i++){
                    monitor.add(data.get(i).getUSERID().toString().toUpperCase());
                }
                monitor.add("ADMIN");
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });
        user = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
    }

    private void getCheckerInfo() {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","proc_SampleCheckUser",
                                "CheckGroupIndex="+"1",String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                OthersUtil.dismissLoadDialog(dialog);
                String json = hsWebInfo.json;
                Log.e("TAG","1Json="+json);
                CheckUser checkUser = JSON.parseObject(json, CheckUser.class);
                List<CheckUser.DATABean> data = checkUser.getDATA();
                for(int i=0;i<data.size();i++){
                    checker.add(data.get(i).getUSERID().toString().toUpperCase());
                }
                checker.add("ADMIN");
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });

        user = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
    }

    @OnClick(R.id.CheckSample_designerFront)//
    void toDesignerA(){
        Intent intent=new Intent(this,CheckSimplePendingActivity.class);
        intent.putExtra("UNITTYPE","0");
        startActivity(intent);
    }
    @OnClick(R.id.CheckSample_checker)
    void toChecker(){
        Log.e("TAG","check="+checker);
        if (checker.toString().isEmpty()){
            getCheckerInfo();
        }else {
            if (checker.contains(user)){
                Intent intent=new Intent(this,CheckSimplePendingActivity.class);
                intent.putExtra("UNITTYPE","1");
                startActivity(intent);
            }else {
                OthersUtil.ToastMsg(this,"目前登录账号不是检验账号");
            }
        }
    }
    @OnClick(R.id.CheckSample_monitor)
    void toMonitor(){
        Log.e("TAG","monitor="+monitor);
        if (monitor.toString().isEmpty()){
            getMonitorInfo();
        }else {
            if (monitor.contains(user)){
                Intent intent=new Intent(this,CheckSimplePendingActivity.class);
                intent.putExtra("UNITTYPE","2");
                startActivity(intent);
            }else {
                OthersUtil.ToastMsg(this,"目前登录账号不是班长账号");
            }
        }
    }

    @OnClick(R.id.CheckSample_designerBehind)//
    void toDesignerB(){
        Intent intent=new Intent(this,CheckSimplePendingActivity.class);
        intent.putExtra("UNITTYPE","3");
        startActivity(intent);
    }
    @OnClick(R.id.CheckSample_QC)
    void toQc(){
        Intent intent=new Intent(this,CheckSimplePendingActivity.class);
        intent.putExtra("UNITTYPE","4");
        startActivity(intent);
    }
}
