package net.huansi.equipment.equipmentapp.activity.move_cloth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tools.command.EscCommand;
import com.tools.command.LabelCommand;
import com.tools.io.BluetoothPort;
import com.tools.io.EthernetPort;
import com.tools.io.PortManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.ThreadPool;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

//连接WiFi打印机的
public class ClothWiFiRegisterActivity extends BaseActivity implements Runnable{
    private int SELECT_COUNT=1;
    public PortManager mPort;
    private ThreadPool threadPool;
    private LoadProgressDialog dialog;

    @BindView(R.id.simpleClothNumber)
    EditText simpleClothNumber;
    @BindView(R.id.register_time)
    TextView register_time;

    @BindView(R.id.register_kuanhao)
    TextView registerKuanHao;
    @BindView(R.id.register_sehao)
    TextView registerSeHao;
    @BindView(R.id.register_styleNo)
    TextView registerStyleNo;
    @BindView(R.id.register_chima)
    TextView registerChiMa;
    @BindView(R.id.register_jijie)
    TextView registerJiJie;
    @BindView(R.id.register_leibie)
    TextView registerJiBie;
    @BindView(R.id.register_xianghao)
    TextView registerXiangHao;
    @Override
    protected int getLayoutId() {
        return  R.layout.activity_cloth_register;
    }

    @Override
    public void init() {
        setToolBarTitle("样衣登记");
        dialog=new LoadProgressDialog(this);
        ZXingLibrary.initDisplayOpinion(this);
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()).toString();
        register_time.setText(data);

        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        // 注册用以接收到已搜索到的蓝牙设备的receiver
//        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, mFilter);
//        // 注册搜索完时的receiver
//        mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        registerReceiver(mReceiver, mFilter);
        // 获取所有已经绑定的蓝牙设备打开打印机端口
        run();
    }

    @Override
    public void run() {
        //获取wifi管理器
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
//        long gateway = dhcpInfo.gateway;
//        String gatwayIpS=long2ip(gateway);//网关地址
//        Log.e("TAG","gatwayIpS="+gatwayIpS);
//        Log.e("TAG","IP="+dhcpInfo);
//        mPort=new EthernetPort(gatwayIpS,9100);
//        boolean b = mPort.openPort();
//        OthersUtil.ToastMsg(this,"wifi打印机连接状态"+b);
        //1.创建连接
        //PRINT_IP：打印机对应的IP
        //PRINT_PORT：打印机端口，可以在打印机上面查看。
        TcpConnection connection = new TcpConnection("",9500);


        //2.打开连接并且创建打印机实例（ZebraPrinterFactory是ZSDK_ANDROID_API.jar提供）。
        ZebraPrinter printer = null;
        try {
            connection.open();
            printer = ZebraPrinterFactory.getInstance(connection);
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (ZebraPrinterLanguageUnknownException e) {
            e.printStackTrace();
        }

        //3.获取打印机控制语言（一般为CPCL或ZPL）
        PrinterLanguage pl = printer.getPrinterControlLanguage();

    }
    //长整型转化为IP地址
    public String long2ip(long ip){
        StringBuffer sb=new StringBuffer();
        sb.append(String.valueOf((int)(ip&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>8)&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>16)&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>24)&0xff)));
        return sb.toString();
    }
    /**
     * ^XA              指令块的开始
     * ^MD              设置色带颜色的深度,取值范围从-30到30,上面的示意指令将颜色调到了最深.
     * ^LH              设置条码纸的边距的.
     * ^CF              改变字符字体默认字体
     */
    private static String lableStart_ZPL(int md,int x,int y){
        String strLable="^XA"+"^CF0,15,7"+ "^MD"+String.valueOf(md)+ "^LH"+String.valueOf(x)+","+String.valueOf(y);

        return strLable;
    }
    /**
     *打印文字
     * @param x           起始水平坐标
     * @param y           起始垂直坐标
     * @param strValue    内容
     * @return
     */
    private static String printText_ZPL(int x,int y,String strValue) {
        String strText = "^FO"+String.valueOf(x)+","+String.valueOf(y)+
                "^AD" +
                "^FD"+ strValue + "^FS";

        return strText;
    }
    @OnClick(R.id.submit_printInfo)
    void printQr(){
        threadPool=ThreadPool.getInstantiation();
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
                submitSimpleInfo();
//                OthersUtil.showDoubleChooseDialog(ClothRegisterActivity.this, "以防误触确认提交并打印吗?请注意打印机出口", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                }, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        submitSimpleInfo();
//                    }
//                });
            }
        });


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
                                "SampleType="+"无"+
                                        ",CompanyStyle="+registerKuanHao.getText().toString()+
                                        ",BillNo="+"无"+
                                        ",Comb="+registerSeHao.getText().toString()+
                                        ",StyleNo="+registerStyleNo.getText().toString()+
                                        ",Sizes="+registerChiMa.getText().toString()+
                                        ",Season="+registerJiJie.getText().toString()+
                                        ",SampleLevel="+registerJiBie.getText().toString()+
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
                OthersUtil.ToastMsg(ClothWiFiRegisterActivity.this,"数据传输失败检查网络连接");
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
        tsc.addQRCode(150,150, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, UUID);
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
            OthersUtil.showTipsDialog(ClothWiFiRegisterActivity.this,"最少一件");
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

    @Override
    protected void onDestroy() {

        boolean closePort = mPort.closePort();
        Log.e("TAG","closePort="+closePort);

        super.onDestroy();
    }


}
