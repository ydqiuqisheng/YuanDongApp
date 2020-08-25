package net.huansi.equipment.equipmentapp.activity.version_control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.IPConfigActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.VersionEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class VersionControlMainActivity extends BaseActivity {
    private LoadProgressDialog dialog;
    @BindView(R.id.version_code)
    EditText versionCode;
    @BindView(R.id.version_log)
    EditText versionLog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_version_control_main;
    }

    @Override
    public void init() {
        setToolBarTitle("版本更新");
        dialog=new LoadProgressDialog(this);
    }
    @OnClick(R.id.submit_versionInfo)
    void submitVersionInfo(){
        OthersUtil.showDoubleChooseDialog(this, "确认更新?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(VersionControlMainActivity.this,"")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAPPSubmit_APPVersion",
                                        "VERSION="+Integer.parseInt(versionCode.getText().toString())+
                                                ",UPDATELOG="+versionLog.getText().toString()
                                        ,String.class.getName(),false,"更新log失败");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        Log.e("TAG","successVersion="+hsWebInfo.json);
                        OthersUtil.ToastMsg(VersionControlMainActivity.this,hsWebInfo.json);
                    }
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG","errorVersion="+hsWebInfo.json);
                        OthersUtil.ToastMsg(VersionControlMainActivity.this,"数据传输失败检查网络连接");
                    }
                });
            }
        });

    }
}
