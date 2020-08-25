package net.huansi.equipment.equipmentapp.activity.awake_goods;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;

import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import rx.functions.Func1;

import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.RFID_HAS_BIND;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by zhou.mi on 2018/1/15.
 */

public class AwakeMainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.rb12)
    RadioButton rb12;
    @BindView(R.id.rb24)
    RadioButton rb24;
    @BindView(R.id.rb48)
    RadioButton rb48;
    @BindView(R.id.lv_AwakeList)
    ListView lv_AwakeList;
    @BindView(R.id.etAwakeDetail)
    EditText etAwakeDetail;
    private Map<String,String> showRFIDMap;//检查数组中是否存在扫到的RFID卡
    private ArrayAdapter<String> mAdapter;
    private List<String> rFIDInfoList;
    private LoadProgressDialog dialog;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private HashMap<String,String> bindRFIDMap;//已经绑定的RFID
    private int LooseStandardHours;

    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        setToolBarTitle("松布");
        radioGroup.setOnCheckedChangeListener(this);
        //etAwakeDetail.setOnKeyListener(this);
        rFIDInfoList=new ArrayList<>();
        mAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,rFIDInfoList);
        lv_AwakeList.setAdapter(mAdapter);
        showRFIDMap=new HashMap<>();
        dialog=new LoadProgressDialog(this);
        bindRFIDMap= (HashMap<String, String>) getIntent().getSerializableExtra(RFID_HAS_BIND);
        if(bindRFIDMap==null) bindRFIDMap=new HashMap<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.awake_main_activity;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId==rb12.getId()){
            Log.e("TAG",rb12.getText().toString());
        }
        if (checkedId==rb24.getId()){
            Log.e("TAG",rb24.getText().toString());
        }
        if (checkedId==rb48.getId()){
            Log.e("TAG",rb48.getText().toString());
        }
    }
    @OnClick(R.id.submit)
    void submitAwakeData(){
        if (radioGroup.getCheckedRadioButtonId()==-1||rFIDInfoList.isEmpty()){
            OthersUtil.showTipsDialog(this,"您还未选择松布标准或未扫描条码！！");
        }else {
            final String submitter = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
            OthersUtil.showLoadDialog(dialog);
            if (radioGroup.getCheckedRadioButtonId()==rb12.getId()){
                LooseStandardHours=0;
            }
            if (radioGroup.getCheckedRadioButtonId()==rb24.getId()){
                LooseStandardHours=Integer.parseInt(rb24.getText().toString());
            }
            if (radioGroup.getCheckedRadioButtonId()==rb48.getId()){
                LooseStandardHours=Integer.parseInt(rb48.getText().toString());
            }

            for (int i=0;i<rFIDInfoList.size();i++){
                final int finalI = i;
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(AwakeMainActivity.this, hsWebInfo)
                                .map(new Func1<HsWebInfo, HsWebInfo>() {
                                    @Override
                                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                                "spSubmitLoose_Other_MaterialStockInByBarcode",
                                                "Barcode="+rFIDInfoList.get(finalI)+
                                                        ",LooseUser="+ submitter+
                                                        ",LooseStandardHours="+LooseStandardHours,
                                                String.class.getName(),
                                                true,
                                                "helloWorld");
                                    }
                                })
                        ,AwakeMainActivity.this, dialog, new WebListener() {
                            @Override
                            public void success(HsWebInfo hsWebInfo) {
                                Log.e("TAG", "success1");

                            }

                            @Override
                            public void error(HsWebInfo hsWebInfo) {
                                Log.e("TAG", "error1="+hsWebInfo.json);
                            }
                        });
            }
            OthersUtil.showTipsDialog(this,"提交完毕");
        }

    }

//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//        switch (keyCode){
//            case KeyEvent.KEYCODE_ENTER:
//            case KeyEvent.KEYCODE_NUMPAD_ENTER:
//                if(event.getAction()==KeyEvent.ACTION_UP) {
//                    String rfid = etAwakeDetail.getText().toString().trim();
//                    Log.e("TAG","RFID="+rfid);
//                    if (showRFIDMap.get(rfid) == null && bindRFIDMap.get(rfid) == null) {
//                        rFIDInfoList.add(rfid);
//                        showRFIDMap.put(rfid, rfid);
//                    }
//                    mAdapter.notifyDataSetChanged();
//                    etAwakeDetail.getText().clear();
//                }
//                break;
//        }
//        return false;
//    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.e("TAG","onKeyUp");
        String rfid = etAwakeDetail.getText().toString().trim();
        Log.e("TAG","RFID="+rfid);
        if (showRFIDMap.get(rfid) == null && bindRFIDMap.get(rfid) == null) {
            rFIDInfoList.add(rfid);
            showRFIDMap.put(rfid, rfid);
        }
        mAdapter.notifyDataSetChanged();
        etAwakeDetail.getText().clear();
        return super.onKeyUp(keyCode, event);
    }
    @OnItemLongClick(R.id.lv_AwakeList)
    boolean delete(final int position){
        OthersUtil.showChooseDialog(this, "确认删除吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("TAG","weiz="+position);
                rFIDInfoList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        return true;
    }
}
