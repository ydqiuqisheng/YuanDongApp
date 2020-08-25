package net.huansi.equipment.equipmentapp.activity.move_cloth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tools.io.BluetoothPort;
import com.tools.io.PortManager;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.check_goods.CheckMainActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.PortEvent;
import net.huansi.equipment.equipmentapp.entity.PrintSamplePo;
import net.huansi.equipment.equipmentapp.entity.RequestSimpleInfo;
import net.huansi.equipment.equipmentapp.entity.SamplePrintPoInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ThreadPool;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;


import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ClothPoActivity extends BaseActivity {

    @BindView(R.id.po_startTime)
    TextView startTime;
    @BindView(R.id.po_endTime)
    TextView endTime;
    @BindView(R.id.customer_keyWord)
    EditText customer;
    @BindView(R.id.fepo_keyWord)
    EditText fepo;
    @BindView(R.id.samplePoAndCustomerList)
    ListView lv_customer;
//    public PortManager mPort;
//    private BluetoothAdapter mBluetoothAdapter;
//    private List<String> addressList;//蓝牙地址列表
//    private List<String> nameList;//蓝牙名称列表
    private LoadProgressDialog dialog;
    private PrintPoAdapter printPoAdapter;
    private PrintSamplePo samplePo;
    private List<PrintSamplePo> samplePos;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cloth_po;
    }

    @Override
    public void init() {
    setToolBarTitle("输入条件查询需打印款");
    TextView subTitle = getSubTitle();
    subTitle.setText("扫码查询");
    subTitle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(ClothPoActivity.this,ClothRegisterActivity.class));
        }
    });
    dialog=new LoadProgressDialog(this);
        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        List<String> weekDay = getWeekDay();
        Log.e("TAG","weekDay="+weekDay);
        startTime.setText(weekDay.get(0).toString());
        endTime.setText(weekDay.get(5).toString());
        //run();
    }

    private static List<String> getWeekDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 获取本周的第一天
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            //WeekDay weekDay = new WeekDay();
            // 获取星期的显示名称，例如：周一、星期一、Monday等等
            //weekDay.week = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
            String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

            list.add(date);
        }

        return list;
    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        boolean closePort = mPort.closePort();
//        Log.e("TAG","closePort="+closePort);
//    }



    @OnClick(R.id.po_startTime)
    void chooseStartTime(){
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(ClothPoActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                String sHour=null;
                if (hour<10){
                    sHour="0"+hour;
                }else {
                    sHour=""+hour;
                }
                String sMinute=null;
                if (minute<10){
                    sMinute="0"+minute;
                }else {
                    sMinute=""+minute;
                }
                startTime.setText(year + "-" + sMonth + "-" +sDay);

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
    @OnClick(R.id.po_endTime)
    void chooseEndTime(){
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(ClothPoActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                String sHour=null;
                if (hour<10){
                    sHour="0"+hour;
                }else {
                    sHour=""+hour;
                }
                String sMinute=null;
                if (minute<10){
                    sMinute="0"+minute;
                }else {
                    sMinute=""+minute;
                }
                startTime.setText(year + "-" + sMonth + "-" +sDay);

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.samplePo_search)
    void searchSamplePo(){
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ClothPoActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GetSampleRegisterFepo",
                                "StartDate="+startTime.getText().toString().trim()+
                                        ",EndTime="+endTime.getText().toString().trim()+
                                        ",Customer="+customer.getText().toString().trim()+
                                        ",FepoCode="+fepo.getText().toString().trim()
                                ,String.class.getName(),false,"打印po获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG",json);
                OthersUtil.dismissLoadDialog(dialog);
                SamplePrintPoInfo samplePrintPoInfo = JSON.parseObject(json, SamplePrintPoInfo.class);
                List<SamplePrintPoInfo.DATABean> data = samplePrintPoInfo.getDATA();
                samplePos=new ArrayList<>();
                for (int i=0;i<data.size();i++){
                    samplePo=new PrintSamplePo();
                    samplePo.FEPO=data.get(i).getFEPO();
                    samplePo.CUSTOMERNAME=data.get(i).getCUSTOMERNAME();
                    samplePo.SAMPLETYPE=data.get(i).getSAMPLETYPE();
                    samplePos.add(samplePo);
                }
                printPoAdapter=new PrintPoAdapter(samplePos,getApplicationContext());
                lv_customer.setAdapter(printPoAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
            }
        });

    }

//    @Override
//    public void run() {
//        final Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
//        if (devices.size() > 0) {
//            addressList = new ArrayList<>();
//            nameList=new ArrayList<>();
//            for (final BluetoothDevice bluetoothDevice : devices) {
//                String address = bluetoothDevice.getAddress();
//                String name = bluetoothDevice.getName();
//                addressList.add(address);
//                nameList.add(name);
//            }
//            int size = nameList.size();
//            final String[] sexArry = nameList.toArray(new String[size]);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
//            builder.setIcon(R.drawable.icon_bluetooth);
//            builder.setTitle("选择打印机");
//            builder.setCancelable(false);
//            builder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 默认的选中
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
//                    //showToast(which+"");
//                    //selectAdress.setText(sexArry[which]);
//                    mPort = new BluetoothPort(addressList.get(which));
//                    boolean openPort = mPort.openPort();
//                    Log.e("TAG","openPort="+openPort);
//                    if (openPort==true){
//                        Toast.makeText(ClothPoActivity.this,sexArry[which] + "打印机连接成功", Toast.LENGTH_LONG).show();
//                    }else {
//                        Toast.makeText(ClothPoActivity.this,sexArry[which] + "打印机连接失败请重新连接", Toast.LENGTH_LONG).show();
//                    }
//                    dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
//                }
//            });
//            builder.show();// 让弹出框显示
//            //  }
//
//        }
//    }


    private class PrintPoAdapter extends HsBaseAdapter<PrintSamplePo>{
        public PrintPoAdapter(List<PrintSamplePo> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null) convertView=mInflater.inflate(R.layout.activity_cloth_po_item,viewGroup,false);
            TextView po = ViewHolder.get(convertView, R.id.printSamplePo);
            TextView customer = ViewHolder.get(convertView, R.id.printSampleCustomer);
            TextView type = ViewHolder.get(convertView, R.id.printSampleType);
            PrintSamplePo printSamplePo = mList.get(position);
            po.setText(printSamplePo.FEPO);
            customer.setText(printSamplePo.CUSTOMERNAME);
            type.setText(printSamplePo.SAMPLETYPE);
            return convertView;
        }
    }

    @OnItemClick(R.id.samplePoAndCustomerList)
    void searchInfoByPo(int position){
        final String fepo = samplePos.get(position).FEPO;
        Log.e("TAG","点了"+fepo);
        Intent intent=new Intent(ClothPoActivity.this,ClothRegisterActivity.class);
        intent.putExtra("printPO",fepo);
        //intent.putExtra("port", (CharSequence) mPort);
        startActivity(intent);
        //EventBus.getDefault().post(new PortEvent(mPort));
    }

}
