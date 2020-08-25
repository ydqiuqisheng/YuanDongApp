package net.huansi.equipment.equipmentapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.trello.rxlifecycle.components.RxActivity;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.listener.BackListener;
import net.huansi.equipment.equipmentapp.listener.InitListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;


/**
 * Created by 单中年 on 2016/12/23.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements InitListener,BackListener {
    private TextView mToolbarTitle;
    private TextView mToolbarSubTitle;
    private Toolbar mToolbar;
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
       /*
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub Title");
        */
        mToolbarTitle = (TextView) findViewById(R.id.tooltitle);
        mToolbarSubTitle = (TextView) findViewById(R.id.subtitle);

        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(getTitle());
            //设置默认的标题不显示
            ActionBar actionBar=getSupportActionBar();
            if(actionBar!=null) actionBar.setDisplayShowTitleEnabled(false);
        }
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //判断是否有Toolbar,并默认显示返回按钮
        if(null != getToolbar() && isShowBacking()){
            showBack();
        }
    }

    /**
     * 获取头部标题的TextView
     * @return
     */
    public TextView getToolbarTitle(){
        return mToolbarTitle;
    }
    /**
     * 获取头部标题的TextView
     * @return
     */
    public TextView getSubTitle(){
        return mToolbarSubTitle;
    }

    /**
     * 设置头部标题
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if(mToolbarTitle != null){
            mToolbarTitle.setText(title);
        }else{
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack(){
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        getToolbar().setNavigationIcon(R.drawable.icon_return);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     * @return
     */
    protected boolean isShowBacking(){
        return true;
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     * @return res layout xml id
     */
    protected abstract int getLayoutId();


    /**
     * 监听返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            back();
            return true;
        }
        return false;
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
