package net.huansi.equipment.equipmentapp.activity.call_repair;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.merge_goods.ScannerActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.CallRepairRecords;
import net.huansi.equipment.equipmentapp.entity.CallRepairStatistics;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SewLineUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CallRepairRecordsActivity extends BaseActivity {
    @BindView(R.id.et_queryDay)
    EditText et_queryDay;
    @BindView(R.id.sp_queryGroup)
    Spinner sp_queryGroup;
    @BindView(R.id.lv_CallRepairRecords)
    ListView lv_CallRepairRecords;
    private int selectedPosition=0;
    private LoadProgressDialog dialog;
    private List<String> groupList;
    private List<String> postIDList;
    private List<CallRepairStatistics> statisticsList;
    private CallRepairStatistics statistics;
    private ArrayAdapter groupAdapter;
    private RecordsAdapter recordsAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_repair_records;
    }

    @Override
    public void init() {
        setToolBarTitle("叫修清单");
        dialog=new LoadProgressDialog(this);
        statisticsList=new ArrayList<>();
        recordsAdapter=new RecordsAdapter(statisticsList,this);
        initTime();
        initGroup();
    }

    private void initTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        String sMonth = null;
        if (month < 9) {
            month=month+1;
            sMonth = "0" + month;
        }else {
            month=month+1;
            sMonth=""+month;
        }
        String sDay = null;
        if (dayOfMonth < 10) {
            sDay = "0" + dayOfMonth;
        }else {
            sDay=""+dayOfMonth;
        }
        et_queryDay.setText(year + "-" + sMonth + "-" + sDay);
    }

    private void initGroup() {
        groupList=new ArrayList();
        postIDList=new ArrayList<>();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppEPSewLine","",String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","groupJson="+json);
                SewLineUtil sewLineUtil = JSON.parseObject(json, SewLineUtil.class);
                List<SewLineUtil.DATABean> data = sewLineUtil.getDATA();
                groupList.add("所有");
                postIDList.add("");
                for (int i=0;i<data.size();i++){
                    groupList.add(i,data.get(i).getDEPTNAME());
                    postIDList.add(i,data.get(i).getPOSTID());
                }
                groupAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.string_item,R.id.text,groupList);
                sp_queryGroup.setAdapter(groupAdapter);
                sp_queryGroup.setSelection(groupList.size()-1);
                sp_queryGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedPosition=position;
                        Log.e("TAG","selectedPosition="+position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error");
            }
        });
    }

    @OnClick(R.id.searchCallRepairRecords)
    void searchCallRepairRecords(){
        recordsAdapter.notifyDataSetChanged();
        statisticsList.clear();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spappQueryRepairRecords",
                                "CallRepairDate="+et_queryDay.getText().toString()
                                +",POSTID="+postIDList.get(selectedPosition),String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","records="+json);
                CallRepairRecords callRepairRecords = JSON.parseObject(json, CallRepairRecords.class);
                List<CallRepairRecords.DATABean> data = callRepairRecords.getDATA();
                for (int i=0;i<data.size();i++){
                    statistics=new CallRepairStatistics();
                    statistics.SEWLINE=data.get(i).getSEWLINE();
                    statistics.CALLREPAIREMPLOYEEID=data.get(i).getCALLREPAIREMPLOYEEID();
                    statistics.EQUNAME=data.get(i).getEQUNAME();
                    statistics.CALLREPTIME=data.get(i).getCALLREPTIME();
                    statistics.RESPTIME=data.get(i).getRESPTIME();
                    statistics.RESPONSETIMES=data.get(i).getRESPONSETIMES();
                    statistics.REPAIRTIMES=data.get(i).getREPAIRTIMES();
                    statistics.REPAIREDTIME=data.get(i).getREPAIREDTIME();
                    statisticsList.add(statistics);
                }
                    lv_CallRepairRecords.setAdapter(recordsAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });
    }
    @OnClick(R.id.et_queryDay)
   void timePicker(){
       Calendar calendar=Calendar.getInstance();
       DatePickerDialog dialog=new DatePickerDialog(CallRepairRecordsActivity.this, new DatePickerDialog.OnDateSetListener() {
           @Override
           public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               Log.e("TAG","month="+month);
               String sMonth = null;
               if (month < 9) {
                   month=month+1;
                   sMonth = "0" + month;
               }else {
                   month=month+1;
                   sMonth=""+month;
               }
               String sDay = null;
               if (dayOfMonth < 10) {
                   sDay = "0" + dayOfMonth;
               }else {
                   sDay=""+dayOfMonth;
               }
               et_queryDay.setText(year + "-" + sMonth + "-" + sDay);

           }
       },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
       dialog.show();
   }

    private class RecordsAdapter extends HsBaseAdapter<CallRepairStatistics>{
        public RecordsAdapter(List<CallRepairStatistics> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null) convertView=mInflater.inflate(R.layout.activity_call_repair_records_item,viewGroup,false);
            CallRepairStatistics statistics = mList.get(position);
            TextView SEWLINE=ViewHolder.get(convertView,R.id.SEWLINE);
            TextView CALLREPAIREMPLOYEEID=ViewHolder.get(convertView,R.id.CALLREPAIREMPLOYEEID);
            TextView EQUNAME=ViewHolder.get(convertView,R.id.EQUNAME);
            TextView CALLREPTIME=ViewHolder.get(convertView,R.id.CALLREPTIME);
            TextView RESPTIME=ViewHolder.get(convertView,R.id.RESPTIME);
            TextView RESPONSETIMES=ViewHolder.get(convertView,R.id.RESPONSETIMES);
            TextView REPAIRTIMES=ViewHolder.get(convertView,R.id.REPAIRTIMES);
            TextView REPAIREDTIME=ViewHolder.get(convertView,R.id.REPAIREDTIME);
            SEWLINE.setText(statistics.SEWLINE);
            CALLREPAIREMPLOYEEID.setText(statistics.CALLREPAIREMPLOYEEID);
            EQUNAME.setText(statistics.EQUNAME);
            CALLREPTIME.setText(statistics.CALLREPTIME);
            RESPTIME.setText(statistics.RESPTIME);
            RESPONSETIMES.setText(statistics.RESPONSETIMES);
            REPAIRTIMES.setText(statistics.REPAIRTIMES);
            REPAIREDTIME.setText(statistics.REPAIREDTIME);
            return convertView;
        }
    }
}
