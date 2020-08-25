package net.huansi.equipment.equipmentapp.activity.check_simple;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.MonitorDetail;
import net.huansi.equipment.equipmentapp.entity.MonitorSimpleInfo;
import net.huansi.equipment.equipmentapp.entity.SimpleSizesEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
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

public class CheckSimpleMonitorActivity extends BaseActivity{
    private LoadProgressDialog dialog;
    private List<MonitorDetail> simpleList;
    private MonitorDetail monitorDetail;
    private MonitorAdapter monitorAdapter;
    @BindView(R.id.SimpleMeasuredList)
    ListView simpleMeasuredList;
    @BindView(R.id.simple_advices_monitor)
    EditText advicesMonitor;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_simple_monitor;
    }

    @Override
    public void init() {
        dialog=new LoadProgressDialog(this);
        setToolBarTitle("选择一条查看");
        getSimpleNumber();
    }


    @OnItemClick(R.id.SimpleMeasuredList)
    void toSecondActivity(int position){
        final String produceorderid = getIntent().getStringExtra("PRODUCEORDERID");
        final String submitType = getIntent().getStringExtra("SUBMITTYPE");
        Intent intent=new Intent(this,CheckSimpleSecondActivity.class);
        intent.putExtra("PRODUCEORDERID",produceorderid);
        intent.putExtra("SUBMITTYPE",submitType);
        intent.putExtra("SUBMITSIZE",simpleList.get(position).SIZE);
        intent.putExtra("SUBMITCOLOR",simpleList.get(position).COLOR);
        intent.putExtra("SUBMITRANK",simpleList.get(position).HOWMANYTIMES);
        intent.putExtra("SUBMITADVICE",simpleList.get(position).DESIGNERADVICE);
        startActivity(intent);
    }

    @OnClick(R.id.simpleSign_off_monitor)
    void signOffMonitor(){
        final String produceorderid = getIntent().getStringExtra("PRODUCEORDERID");
        final String submitType = getIntent().getStringExtra("SUBMITTYPE");
        final String UserID = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
        Log.e("TAG",produceorderid);
        Log.e("TAG",submitType);
        Log.e("TAG",UserID);

        OthersUtil.showDoubleChooseDialog(CheckSimpleMonitorActivity.this, "确认签核至下一关吗?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface mDialog, int which) {
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CheckSimpleMonitorActivity.this,"")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_SubmitSampleCheckNextFlow",
                                        "PDMProduceOrderID="+produceorderid+
                                                ",UserID="+UserID+
                                                ",CheckGroupIndex="+submitType+
                                                ",CheckType="+"Y"+
                                                ",SampleAdvice="+advicesMonitor.getText().toString(),
                                        String.class.getName(),false,"组别获取成功");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        OthersUtil.ToastMsg(CheckSimpleMonitorActivity.this,"签核完毕");
                        startActivity(new Intent(CheckSimpleMonitorActivity.this,CheckSimpleDepartmentActivity.class));
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
    private void getSimpleNumber() {
        final String produceorderId = getIntent().getStringExtra("PRODUCEORDERID");
        final String sizeId = getIntent().getStringExtra("SIZEID");
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GetSampleCheckSizeMeasureResult",
                                "PDMProduceOrderID="+produceorderId +
                                        ",ResultListType="+"LIST"+
                                        ",Size="+""+
                                        ",NodeCategory="+"Sample"+
                                ",HowManyTimes="+""+",Color="+"",String.class.getName(),false,"记录获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","recordsJson="+json);
                MonitorSimpleInfo monitorSimpleInfo = JSON.parseObject(json, MonitorSimpleInfo.class);
                List<MonitorSimpleInfo.DATABean> data = monitorSimpleInfo.getDATA();
                simpleList=new ArrayList<>();
                for (int i=0;i<data.size();i++){
                    monitorDetail=new MonitorDetail();
                    monitorDetail.PRODUCEORDERITEMID=data.get(i).getPRODUCEORDERITEMID();
                    monitorDetail.COLOR=data.get(i).getCOLOR();
                    monitorDetail.SIZE=data.get(i).getSIZE();
                    monitorDetail.NODECATEGORY=data.get(i).getNODECATEGORY();
                    monitorDetail.HOWMANYTIMES=data.get(i).getHOWMANYTIMES();
                    monitorDetail.DESIGNERADVICE=data.get(i).getDESIGNERADVICE();
                    simpleList.add(monitorDetail);
                }
                monitorAdapter=new MonitorAdapter(simpleList,getApplicationContext());
                simpleMeasuredList.setAdapter(monitorAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });
    }

    private class MonitorAdapter extends HsBaseAdapter<MonitorDetail>{
        public MonitorAdapter(List<MonitorDetail> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null)  convertView=mInflater.inflate(R.layout.activity_check_simple_monitor_item,viewGroup,false);
            MonitorDetail monitorDetail = mList.get(position);
            TextView color_monitor= ViewHolder.get(convertView,R.id.color_monitor);
            TextView size_monitor= ViewHolder.get(convertView,R.id.size_monitor);
            TextView category_monitor= ViewHolder.get(convertView,R.id.category_monitor);
            TextView times_monitor= ViewHolder.get(convertView,R.id.times_monitor);
            color_monitor.setText(monitorDetail.COLOR);
            size_monitor.setText(monitorDetail.SIZE);
            category_monitor.setText(monitorDetail.NODECATEGORY);
            times_monitor.setText(monitorDetail.HOWMANYTIMES);
            return convertView;
        }
    }
}
