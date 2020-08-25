package net.huansi.equipment.equipmentapp.activity.check_simple;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.move_cloth.ClothQueryRecordActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.ClothMoveRecords;
import net.huansi.equipment.equipmentapp.entity.EPUser;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.SimpleEntity;
import net.huansi.equipment.equipmentapp.entity.SimplePendMeasureEntity;
import net.huansi.equipment.equipmentapp.entity.SimpleUnMeasureUtils;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CheckSimplePendingActivity extends BaseActivity {
    private List<SimpleUnMeasureUtils> mList;
    private List<SimpleUnMeasureUtils> mShowList;
    private SimpleUnMeasureUtils unMeasureSimple;
    private LoadProgressDialog dialog;
    private PendingSimpleAdapter mAdapter;
    @BindView(R.id.UnMeasureSimpleList)
    ListView UnMeasureSimpleList;
    @BindView(R.id.etPOSearch)
    EditText etPOSearch;

    private boolean isCreate=true;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_simple_pending;
    }

    @Override
    public void init() {
    setToolBarTitle("选择单据");
    mList=new ArrayList<>();
    dialog=new LoadProgressDialog(this);
    mShowList=new ArrayList<>();
        mAdapter=new PendingSimpleAdapter(mShowList,CheckSimplePendingActivity.this);
        UnMeasureSimpleList.setAdapter(mAdapter);
    etPOSearch.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            showFilter();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });
    initPendingData();
    }

    /**
     * 显示筛选的数据
     */
    private void showFilter(){
        mShowList.clear();
        String search=etPOSearch.getText().toString().trim().toUpperCase();
        for(int i=0;i<mList.size();i++){
            SimpleUnMeasureUtils user=mList.get(i);

            if(user.FEPO.contains(search)){
                mShowList.add(user);
            }
        }

        mAdapter.notifyDataSetChanged();
    }


    private void initPendingData() {
        final String type = getIntent().getStringExtra("UNITTYPE");
        Log.e("TAG","类型="+type);
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GetSampleCheckListByRole",
                                "CheckGroupIndex="+type,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                OthersUtil.dismissLoadDialog(dialog);
                String json = hsWebInfo.json;
                Log.e("TAG","pendingJson="+json);
                SimplePendMeasureEntity measureEntity = JSON.parseObject(json, SimplePendMeasureEntity.class);//待测量样衣实体类
                List<SimplePendMeasureEntity.DATABean> data = measureEntity.getDATA();
                for (int i=0;i<data.size();i++){
                    unMeasureSimple=new SimpleUnMeasureUtils();
                    unMeasureSimple.CUSTOMERNAME=data.get(i).getCUSTOMERNAME();
                    unMeasureSimple.FEPO=data.get(i).getFEPO();
                    unMeasureSimple.SAMPLETYPENAME=data.get(i).getSAMPLETYPENAME();
                    unMeasureSimple.SUBMITUSERNAME=data.get(i).getSUBMITUSERNAME();
                    unMeasureSimple.SUBMITDATE=data.get(i).getSUBMITDATE();
                    unMeasureSimple.PRODUCEORDERID=data.get(i).getPRODUCEORDERID();
                    mList.add(unMeasureSimple);
                }
                if(isCreate) {
                    mShowList.addAll(mList);
                    mAdapter.notifyDataSetChanged();
                    isCreate=false;
                }else {
                    showFilter();
                }

            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });
    }
    @OnItemClick(R.id.UnMeasureSimpleList)
    void intentSimpleSizes(int position){
        String type = getIntent().getStringExtra("UNITTYPE");
        switch (type){
            case "0"://版师前段
                //电脑端操作

                break;
            case "1"://样品组
                Intent intent1=new Intent(this,CheckSimpleSizesActivity.class);
                intent1.putExtra("PRODUCEORDERID",mShowList.get(position).PRODUCEORDERID);
                intent1.putExtra("SUBMITTYPE",type);
                startActivity(intent1);
                break;
            case "2"://班长
                Intent intent2=new Intent(this,CheckSimpleMonitorActivity.class);
                intent2.putExtra("PRODUCEORDERID",mShowList.get(position).PRODUCEORDERID);
                intent2.putExtra("SUBMITTYPE",type);
                startActivity(intent2);
                break;
            case "3"://品管
                //电脑端操作
                break;
        }
        Log.e("TAG",mList.get(position).PRODUCEORDERID);


    }

    private class PendingSimpleAdapter extends HsBaseAdapter<SimpleUnMeasureUtils>{
        private PendingSimpleAdapter(List<SimpleUnMeasureUtils> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null)  convertView=mInflater.inflate(R.layout.pend_simple_item,viewGroup,false);
            TextView pend_customerName = ViewHolder.get(convertView, R.id.pend_customerName);
            TextView pend_fepo = ViewHolder.get(convertView, R.id.pend_fepo);
            TextView pend_sampleTypeName = ViewHolder.get(convertView, R.id.pend_sampleTypeName);
            TextView pend_submitUserName = ViewHolder.get(convertView, R.id.pend_submitUserName);
            TextView pend_submitDate = ViewHolder.get(convertView, R.id.pend_submitDate);
            SimpleUnMeasureUtils utils = mList.get(position);
            pend_customerName.setText(utils.CUSTOMERNAME);
            pend_fepo.setText(utils.FEPO);
            pend_sampleTypeName.setText(utils.SAMPLETYPENAME);
            pend_submitUserName.setText(utils.SUBMITUSERNAME);
            pend_submitDate.setText(utils.SUBMITDATE);
            return convertView;
        }
    }
}
