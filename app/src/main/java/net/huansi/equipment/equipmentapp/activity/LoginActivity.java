package net.huansi.equipment.equipmentapp.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.LoginUser;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import butterknife.BindView;
import butterknife.OnClick;

import static net.huansi.equipment.equipmentapp.util.SPHelper.ROLE_CODE_KEY;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_PWS;

/**
 * Created by 单中年 on 2017/2/22.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.etLoginPwd) EditText etLoginPwd;//密码
    @BindView(R.id.etLoginUserNo) EditText etLoginUserNo;//用户名
    @BindView(R.id.btnLogin) Button btnLogin;//登录


    private LoadProgressDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        dialog=new LoadProgressDialog(this);
        etLoginUserNo.setText(SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString());

        Log.e("TAG","执行了吗");
    }


    /**
     * 登陆
     */
    @OnClick(R.id.btnLogin)
    void login(){
        String userNo=etLoginUserNo.getText().toString().trim();
        String passward=etLoginPwd.getText().toString().trim();
        if(userNo.isEmpty()){
            OthersUtil.ToastMsg(getApplicationContext(),"请输入用户名");
            return;
        }
        if(passward.isEmpty()){
            OthersUtil.ToastMsg(getApplicationContext(),"请输入密码");
            return;
        }

        OthersUtil.showLoadDialog(dialog);
        RxjavaWebUtils.requestByGetJsonData(this,"spAppEPUserLogin",
                        "sUserNo=" + userNo +
                        ",sPassword=" + passward,
                getApplicationContext(), dialog,
                LoginUser.class.getName(),
                true,
                getResources().getString(R.string.connect_server_error),
                new SimpleHsWeb() {
                    @Override
                    public int hashCode() {
                        return super.hashCode();
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        super.error(hsWebInfo);
                    }

                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        LoginUser loginUser= (LoginUser) hsWebInfo.wsData.LISTWSDATA.get(0);
                        Log.e("TAG","用户信息"+hsWebInfo.json);
                        SPHelper.saveLocalData(getApplicationContext(),USER_NO_KEY,loginUser.SUSERNO,String.class.getName());
                        SPHelper.saveLocalData(getApplicationContext(),ROLE_CODE_KEY,loginUser.SROLECODE,String.class.getName());
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    /**
     * 叫修快速登陆（无需密码）
     */
    @OnClick(R.id.btnFastLogin)
    void fastLogin(){
        String userNo=etLoginUserNo.getText().toString().trim();
        String password="callRepair";
        if(userNo.isEmpty()){
            OthersUtil.ToastMsg(getApplicationContext(),"请输入用户名");
            return;
        }
        String substring = userNo.substring(0, 1);
        if ((substring.equalsIgnoreCase("A")||substring.equalsIgnoreCase("B"))&&userNo.length()==6){
            Log.e("TAG","核对成功");
            OthersUtil.showLoadDialog(dialog);
            RxjavaWebUtils.requestByGetJsonData(this,"spAppEPUserLogin",
                    "sUserNo=" + userNo +
                            ",sPassword=" + password,
                    getApplicationContext(), dialog,
                    LoginUser.class.getName(),
                    true,
                    getResources().getString(R.string.connect_server_error),
                    new SimpleHsWeb() {
                        @Override
                        public int hashCode() {
                            return super.hashCode();
                        }

                        @Override
                        public void error(HsWebInfo hsWebInfo) {
                            super.error(hsWebInfo);
                        }

                        @Override
                        public void success(HsWebInfo hsWebInfo) {
                            OthersUtil.dismissLoadDialog(dialog);
                            LoginUser loginUser= (LoginUser) hsWebInfo.wsData.LISTWSDATA.get(0);
                            SPHelper.saveLocalData(getApplicationContext(),USER_NO_KEY,loginUser.SUSERNO,String.class.getName());
                            SPHelper.saveLocalData(getApplicationContext(),ROLE_CODE_KEY,loginUser.SROLECODE,String.class.getName());
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
        }else {
            OthersUtil.showTipsDialog(this,"请输入正确的工号！");
        }

    }
}
