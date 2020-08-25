package net.huansi.equipment.equipmentapp.activity.store_goods;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Joiner;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class StoreGoodsMainActivity extends BaseActivity{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_goods_main;
    }

    @Override
    public void init() {
        setToolBarTitle("功能");
    }
    @OnClick(R.id.storeIn)
    void storeGoodsIn(){
        startActivity(new Intent(this,StoreGoodsInActivity.class));
    }
    @OnClick(R.id.storeQuery)
    void storeGoodsQuery(){
        startActivity(new Intent(this,StoreGoodsQueryActivity.class));
    }
    @OnClick(R.id.storeMove)
    void storeGoodsMove(){
        startActivity(new Intent(this,StoreGoodsMoveActivity.class));
    }
}
