package net.huansi.equipment.equipmentapp.activity.move_cloth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tscdll.TSCActivity;
import com.tools.command.EscCommand;
import com.tools.command.LabelCommand;
import com.tools.io.BluetoothPort;
import com.tools.io.PortManager;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.logging_bill.LoggingBillActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.LogDtEntity;
import net.huansi.equipment.equipmentapp.entity.LogDtInfo;
import net.huansi.equipment.equipmentapp.entity.PortEvent;
import net.huansi.equipment.equipmentapp.entity.PrintPoInfo;
import net.huansi.equipment.equipmentapp.entity.RequestSimpleInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.JSONEntity;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.ThreadPool;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;
//连接蓝牙打印机的
public class ClothRegisterActivity extends BaseActivity implements Runnable{
    private static final int REQUEST_CODE = 1;
    private int SELECT_COUNT=1;
    public PortManager mPort;
    private ThreadPool threadPool;
    private BluetoothAdapter mBluetoothAdapter;
    private List<String> addressList;//蓝牙地址列表
    private List<String> nameList;//蓝牙名称列表
    private List<String> categoryList;//可选类别列表
    private LoadProgressDialog dialog;
    @BindView(R.id.simpleClothNumber)
    EditText simpleClothNumber;
    @BindView(R.id.register_time)
    TextView register_time;
    @BindView(R.id.register_kuanhao)
    TextView registerKuanHao;
    @BindView(R.id.register_sehao)
    EditText registerSeHao;
    @BindView(R.id.register_styleNo)
    EditText registerStyleNo;
    @BindView(R.id.register_chima)
    EditText registerChiMa;
    @BindView(R.id.register_jijie)
    EditText registerJiJie;
    @BindView(R.id.register_leibie)
    TextView registerLeiBie;
    @BindView(R.id.register_xianghao)
    EditText registerXiangHao;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cloth_register;
    }

    @Override
    public void init() {
        setToolBarTitle("样衣登记");
        //EventBus.getDefault().register(this);
        categoryList=new ArrayList<>();
        categoryList.add("大货QRS样");
        categoryList.add("样品QRS样");
        categoryList.add("大货水洗QRS样");
        categoryList.add("样品水洗QRS样");
        dialog=new LoadProgressDialog(this);
        ZXingLibrary.initDisplayOpinion(this);
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()).toString();
        register_time.setText(data);
        ZXingLibrary.initDisplayOpinion(this);
        String printPO = getIntent().getStringExtra("printPO");
        //mPort = getIntent().getCharSequenceExtra("port");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        // 注册用以接收到已搜索到的蓝牙设备的receiver
//        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, mFilter);
//        // 注册搜索完时的receiver
//        mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        registerReceiver(mReceiver, mFilter);
        // 获取所有已经绑定的蓝牙设备打开打印机端口
        run();

        initPoInfo(printPO);
    }

    private void initPoInfo(final String fepo) {
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ClothRegisterActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GetSampleRegisterInfo_Detail",
                                "FepoCode="+fepo.toString().trim()
                                ,String.class.getName(),false,"获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG",json);
                OthersUtil.dismissLoadDialog(dialog);
                PrintPoInfo printPoInfo = JSON.parseObject(json, PrintPoInfo.class);
                registerKuanHao.setText(printPoInfo.getDATA().get(0).getFEPO());
                registerStyleNo.setText(printPoInfo.getDATA().get(0).getSTYLE());
                registerJiJie.setText(printPoInfo.getDATA().get(0).getENGLISHNAME());
                registerSeHao.setText(printPoInfo.getDATA().get(0).getCOMBNAME());
                registerChiMa.setText(printPoInfo.getDATA().get(0).getSIZEID());
                simpleClothNumber.setText(printPoInfo.getDATA().get(0).getQUANTITY());
                //run();
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
            }
        });
    }

//    @Subscribe
//    public void onPortMessage(PortEvent event){
//        mPort= event.getmPort();
//    }







    @OnClick(R.id.scan_getSimpleInfo)
    void getSimpleInfoByQrCode(){
        Intent intent = new Intent(ClothRegisterActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // 搜索到的不是已经绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 显示在TextView上

                }
                // 搜索完成
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setTitle("搜索蓝牙设备");
            }
        }
    };
    //打印二维码
    @OnClick(R.id.submit_printInfo)
    void printQr(){
        threadPool=ThreadPool.getInstantiation();
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
//                if (registerKuanHao.getText().length()==8){
                    submitSimpleInfo();
//                }else {
//                    OthersUtil.showTipsDialog(ClothRegisterActivity.this,"请输入八码款号！");
//                }
            }
        });


    }
    //接收扫描二维码数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("TAG","扫描结果="+result);
                    // TODO: 2019/10/23
                    requestSimpleInfo(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(ClothRegisterActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void requestSimpleInfo(final String result) {//upc贴纸条码
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ClothRegisterActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAPP_RequestSampleInfo",
                                "UPCNo="+result.trim().toString()
                                ,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG",json);
                OthersUtil.dismissLoadDialog(dialog);
                RequestSimpleInfo requestSimpleInfo = JSON.parseObject(json, RequestSimpleInfo.class);
                RequestSimpleInfo.DATABean simpleInfo = requestSimpleInfo.getDATA().get(0);
                registerSeHao.setText(simpleInfo.getCOMBNAME());
                registerStyleNo.setText(simpleInfo.getSNO());
                registerChiMa.setText(simpleInfo.getSIZENAME());
                registerJiJie.setText(simpleInfo.getSEASON());
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
            }
        });

    }

    @OnClick(R.id.register_leibie)
    void selectLeiBie(){
        if (categoryList==null||categoryList.isEmpty()){
            return;
        }
        int size = categoryList.size();

        final String[] array = categoryList.toArray(new String[size]);
        android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(ClothRegisterActivity.this)
                .setTitle("选择一个类别")
                .setIcon(R.drawable.app_icon)
                .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {
                        registerLeiBie.setText(array[which]);
                    }
                }).create();
        alertDialog.show();
    }


    private void submitSimpleInfo() {
        //生成UUID标识码
        final String uuid = UUID.randomUUID().toString();
        final String roleCode= SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString().toUpperCase();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_SampleRegister",
                                "SampleType="+registerLeiBie.getText().toString()+
                                        ",CompanyStyle="+registerKuanHao.getText().toString()+
                                        ",BillNo="+"无"+
                                        ",Comb="+registerSeHao.getText().toString()+
                                        ",StyleNo="+registerStyleNo.getText().toString()+
                                        ",Sizes="+registerChiMa.getText().toString()+
                                        ",Season="+registerJiJie.getText().toString()+
                                        ",SampleLevel="+"无"+
                                        ",BoxNo="+registerXiangHao.getText().toString()+
                                        ",RegisterQty="+Integer.parseInt(simpleClothNumber.getText().toString())+
                                        ",CreateUserID="+roleCode+
                                        ",uuID="+uuid,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                Log.e("TAG","success="+hsWebInfo.json);
                sendLabel(uuid);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
                OthersUtil.ToastMsg(ClothRegisterActivity.this,"数据传输失败检查网络连接");
            }
        });
    }

    void sendLabel(String UUID) {
        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸，按照实际尺寸设置
        tsc.addSize(60, 40);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(2);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        // 设置原点坐标
        tsc.addReference(0, 0);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();
         //绘制简体中文
         String remark1 = "款号:"+registerKuanHao.getText().toString()+"/"+"色号:"+registerSeHao.getText().toString();
         String remark2 = "尺码:"+ registerChiMa.getText().toString()+"/"+"箱号:"+registerXiangHao.getText().toString();
         String remark3 = "季节:"+registerJiJie.getText().toString()+ "/" + "sNo:"+registerStyleNo.getText().toString() ;
        tsc.addText(80, 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                remark1);
        tsc.addText(80, 60, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                remark2);
        tsc.addText(80, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                remark3);
////        // 绘制图片
////        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bird);
////        tsc.addBitmap(10, 20, LabelCommand.BITMAP_MODE.OVERWRITE, 300, b);
        //绘制二维码
        tsc.addQRCode(150,150, LabelCommand.EEC.LEVEL_H, 5, LabelCommand.ROTATION.ROTATION_0, UUID);
        // 绘制一维条码
        //tsc.add1DBarcode(30, 50, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "SMARNET");
        // 打印标签
        //需要打印的份数
        int copy = Integer.parseInt(simpleClothNumber.getText().toString());
        tsc.addPrint(copy, 1);
        // 打印标签后 蜂鸣器响
        tsc.addSound(2, 100);
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        sendDataImmediately(datas);
    }

    public void sendDataImmediately(final Vector<Byte> data) {
        if (this.mPort == null) {
            return;
        }
        try {
            //  Log.e(TAG, "data -> " + new String(com.gprinter.command.GpUtils.convertVectorByteTobytes(data), "gb2312"));

            this.mPort.writeDataImmediately(data, 0, data.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.bt_minus)
    void numberMinus(){
        if (simpleClothNumber.getText().toString().equalsIgnoreCase("1")){
            OthersUtil.showTipsDialog(ClothRegisterActivity.this,"最少一件");
        }else {
            SELECT_COUNT = Integer.parseInt(simpleClothNumber.getText().toString());
            SELECT_COUNT--;
            simpleClothNumber.setText(SELECT_COUNT+"");
        }
    }

    @OnClick(R.id.bt_plus)
    void numberPlus(){
        SELECT_COUNT = Integer.parseInt(simpleClothNumber.getText().toString());
        SELECT_COUNT++;
        simpleClothNumber.setText(SELECT_COUNT+"");
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean closePort = mPort.closePort();
       Log.e("TAG","closePort="+closePort);
        //EventBus.getDefault().unregister(this);
    }



    @Override
    public void run() {
        final Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            addressList = new ArrayList<>();
            nameList=new ArrayList<>();
            for (final BluetoothDevice bluetoothDevice : devices) {
                String address = bluetoothDevice.getAddress();
                String name = bluetoothDevice.getName();
                addressList.add(address);
                nameList.add(name);
            }
            mPort = new BluetoothPort(addressList.get(0));
            boolean openPort = mPort.openPort();
            Log.e("TAG","openPort="+openPort);
            if (openPort==true){
                Toast.makeText(ClothRegisterActivity.this, "打印机连接成功", Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(ClothRegisterActivity.this, "打印机连接失败请重新连接", Toast.LENGTH_LONG).show();
            }



//            int size = nameList.size();
//            final String[] sexArry = nameList.toArray(new String[size]);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
//                builder.setIcon(R.drawable.icon_bluetooth);
//                builder.setTitle("选择打印机");
//                builder.setCancelable(false);
//                builder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 默认的选中
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
//                        //showToast(which+"");
//                        //selectAdress.setText(sexArry[which]);
//                        mPort = new BluetoothPort(addressList.get(which));
//                        boolean openPort = mPort.openPort();
//                        Log.e("TAG","openPort="+openPort);
//                        if (openPort==true){
//                            Toast.makeText(ClothRegisterActivity.this,sexArry[which] + "打印机连接成功", Toast.LENGTH_LONG).show();
//
//                        }else {
//                            Toast.makeText(ClothRegisterActivity.this,sexArry[which] + "打印机连接失败请重新连接", Toast.LENGTH_LONG).show();
//                        }
//                        dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
//                    }
//                });
//                builder.show();// 让弹出框显示
          //  }

        }
    }
}
