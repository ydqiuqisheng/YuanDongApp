package net.huansi.equipment.equipmentapp.activity.check_simple;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.ScrollablePanelAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.SimpleSizesEntity;
import net.huansi.equipment.equipmentapp.entity.SimpleStandardEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.JSONEntity;
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

public class CheckSimpleSizesActivity extends BaseActivity {
    private LoadProgressDialog dialog;
    private List<String> sizeLists;
    private List<String> adviceLists;
    private ArrayAdapter mAdapter;
    @BindView(R.id.SimpleSizeLists)
    ListView lvSimpleSize;
    @BindView(R.id.simple_advices)
    EditText simpleAdvices;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_simple_sizes;
    }

    @Override
    public void init() {
        setToolBarTitle("选择尺码");
        dialog=new LoadProgressDialog(this);
        lvSimpleSize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e("TAG",sizeLists.get(position));
                Intent intent=new Intent(CheckSimpleSizesActivity.this,CheckSimpleMainActivity.class);
                final String produceorderid = getIntent().getStringExtra("PRODUCEORDERID");
                intent.putExtra("PRODUCEORDERID",produceorderid);
                intent.putExtra("SIZEID",sizeLists.get(position));
                intent.putExtra("SIZEADVICE",adviceLists.get(position));
                startActivity(intent);
            }
        });
        initSizeInfo();
    }


//    private void initUI() {
//        String submittype = getIntent().getStringExtra("SUBMITTYPE");
//        switch (submittype){
//            case "0"://版师
//                simpleAdvices.setVisibility(View.VISIBLE);
//
//                break;
//            case "1"://样品组
//
//                break;
//            case "2"://班长
//                simpleAdvices.setVisibility(View.VISIBLE);
//                break;
//            case "3"://品管
//                simpleAdvices.setVisibility(View.VISIBLE);
//
//                break;
//        }
//    }

    private void initSizeInfo() {
        final String produceorderid = getIntent().getStringExtra("PRODUCEORDERID");
        Log.e("TAG",produceorderid);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GetSampleCheckSizeIDList",
                                "PDMProduceOrderID="+produceorderid,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","sizesJson="+json);
                SimpleSizesEntity simpleSizesEntity = JSON.parseObject(json, SimpleSizesEntity.class);
                List<SimpleSizesEntity.DATABean> data = simpleSizesEntity.getDATA();
                sizeLists=new ArrayList<>();
                adviceLists=new ArrayList<>();
                for (int i=0;i<data.size();i++){
                    sizeLists.add(data.get(i).getSIZEID());
                    adviceLists.add(data.get(i).getDESIGNERADVICE());
                }
                mAdapter=new ArrayAdapter<String>(CheckSimpleSizesActivity.this,R.layout.string_item_max_simple,R.id.text_simple_max,sizeLists);
                lvSimpleSize.setAdapter(mAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });
    }
    @OnClick(R.id.simpleSign_off_checker)
    void simpleSign(){
        final String produceorderid = getIntent().getStringExtra("PRODUCEORDERID");
        final String submitType = getIntent().getStringExtra("SUBMITTYPE");
        final String UserID = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
        Log.e("TAG",produceorderid);
        Log.e("TAG",submitType);
        Log.e("TAG",UserID);

        OthersUtil.showDoubleChooseDialog(CheckSimpleSizesActivity.this, "确认签核至下一关吗?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface mDialog, int which) {
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CheckSimpleSizesActivity.this,"")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_SubmitSampleCheckNextFlow",
                                        "PDMProduceOrderID="+produceorderid+
                                                ",UserID="+UserID+
                                                ",CheckGroupIndex="+submitType+
                                                ",CheckType="+"Y"+ ",SampleAdvice="+"",
                                                String.class.getName(),false,"组别获取成功");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        OthersUtil.ToastMsg(CheckSimpleSizesActivity.this,"签核完毕");
                        startActivity(new Intent(CheckSimpleSizesActivity.this,CheckSimpleDepartmentActivity.class));
                        Log.e("TAG","signJson="+json);
                    }
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG","error="+hsWebInfo.json);

                    }
                });
            }
        });
    }
}
