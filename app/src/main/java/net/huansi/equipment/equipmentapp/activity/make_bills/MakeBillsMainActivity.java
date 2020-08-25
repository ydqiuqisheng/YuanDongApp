package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.VideoEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Func1;

import static net.huansi.equipment.equipmentapp.util.SPHelper.ROLE_CODE_KEY;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class MakeBillsMainActivity extends BaseActivity {
    @BindView(R.id.etInputBills) EditText etInputBills;
    @BindView(R.id.btnTransformPo) Button poChange;
    private DownloadManager downloadManager;
    long reference;
    private LoadProgressDialog dialog;
    private CompleteReceiver  completeReceiver;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private List<String> videoUrl=new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.make_bills_main_activity;
    }

    @Override
    public void init() {
        setToolBarTitle("款式信息");
        dialog=new LoadProgressDialog(this);
        Log.e("TAG","init");
        //下载任务
        String serviceString = Context.DOWNLOAD_SERVICE;
        // 直接使用系统的下载管理器。是不是非常方便
        downloadManager = (DownloadManager)getBaseContext().getSystemService(serviceString);
        //注册下载的广播监听
        completeReceiver=new CompleteReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(completeReceiver,filter);
        String role = SPHelper.getLocalData(getApplicationContext(), ROLE_CODE_KEY, String.class.getName(), "").toString().toUpperCase();
        Log.e("TAG","role="+role);
        if (role.equalsIgnoreCase("G")||role.equalsIgnoreCase("A")){
            poChange.setVisibility(View.VISIBLE);
        }


    }


//    @OnClick(R.id.btnChart)
//    void ChartTest(){
//        startActivity(new Intent(this,ShowChartActivity.class));
//    }

    @OnClick(R.id.btnDownLoadVideo)
        void downLoadVideo() {
        if (etInputBills.getText().toString().isEmpty()) {
            OthersUtil.showTipsDialog(this, "请先输入款号后下载！");
        } else {
            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(MakeBillsMainActivity.this, hsWebInfo)
                            .map(new Func1<HsWebInfo, HsWebInfo>() {
                                @Override
                                public HsWebInfo call(HsWebInfo hsWebInfo) {
                                    return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                            "spGetUrl_ProduceOrderPDFAndGSDVideo",
                                            "FEPO=" + etInputBills.getText().toString() +
                                                    ",DataType=" + "GSD",
                                            String.class.getName(),
                                            false,
                                            "helloWorld");
                                }
                            })
                    , MakeBillsMainActivity.this, dialog, new WebListener() {
                        @Override
                        public void success(HsWebInfo hsWebInfo) {
                            String json = hsWebInfo.json;
                            Log.e("TAG", "success1=" + hsWebInfo.json);
                            //json解析
                            VideoEntity videoEntity = JSON.parseObject(json, VideoEntity.class);
                            List<VideoEntity.DATABean> data = videoEntity.getDATA();
                            videoUrl.clear();
                            for (int i = 0; i < data.size(); i++) {
                                //videoUrl.add(Environment.getExternalStorageDirectory().getPath()+"/"+data.get(i).getVIDEOURL());
                                videoUrl.add(data.get(i).getVIDEOURL());
                            }
                            
                            OthersUtil.showTipsDialog(MakeBillsMainActivity.this, "确认下载吗？", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startDownLoad();
                                }
                            });
                        }
                        @Override
                        public void error(HsWebInfo hsWebInfo) {
                            Log.e("TAG", "error1=" + hsWebInfo.error.error);
                            Toast.makeText(getApplicationContext(),"输入款号没有可下载视频！",Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    private void startDownLoad() {
//        List<String> filesAllName = getFilesAllName("/sdcard/"+Environment.DIRECTORY_MOVIES);
//        Log.e("TAG","filesAllName="+filesAllName);
//        for (int i=0;i<filesAllName.size();i++){
//            String singleFile = filesAllName.get(i).toString();
//            int fileIndex = singleFile.lastIndexOf("/");
//            String singleName = singleFile.substring(fileIndex, singleFile.length());
//
//        }

        String subFolder = etInputBills.getText().toString();
        deleteDir("/sdcard/"+subFolder);
        for (int i=0;i<videoUrl.size();i++){
            String path = videoUrl.get(i).toString();
            Uri uri = Uri.parse(path);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            //通知栏的标题
            request.setTitle("视频下载");
            request.setVisibleInDownloadsUi(true ) ;
            //下载到哪个文件夹下，以及命名
            int index = path.lastIndexOf("/");
            String fileName = path.substring(index, path.length());
            //Log.e("TAG",Environment.DIRECTORY_DOWNLOADS+fileName);
            //File file = new File(fileName);
            //if (file.exists()){
            //    OthersUtil.showTipsDialog(this,"已经下载过了");
           // }else {
                //request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS,fileName);
                request.setDestinationInExternalPublicDir( subFolder,fileName);
                //下载的唯一标识，可以用这个标识来控制这个下载的任务enqueue（）开始执行这个任务
                reference = downloadManager.enqueue(request);
          //  }
        }
    }
//删除文件夹和文件夹里面的文件
public static void deleteDir(final String pPath) {
    File dir = new File(pPath);
    deleteDirWihtFile(dir);
}
public static void deleteDirWihtFile(File dir) {
    if (dir == null || !dir.exists() || !dir.isDirectory())
        return;
    for (File file : dir.listFiles()) {
        if (file.isFile())
            file.delete(); // 删除所有文件
        else if (file.isDirectory())
            deleteDirWihtFile(file); // 递规的方式删除文件夹
    }
    dir.delete();// 删除目录本身
}
    @OnClick(R.id.unSeePDF)
    void intentUnSeePdf(){
        startActivity(new Intent(this,ShowUnSeePDFActivity.class));
    }
    @OnClick(R.id.btnViewPDF)
    void intentPDF(){
        if (etInputBills.getText().toString().isEmpty()){
            OthersUtil.showTipsDialog(this,"请先输入单号！");
        }else {
            Intent intent=new Intent(this,ShowPDFActivity.class);
            intent.putExtra("BillNumber",etInputBills.getText().toString());
            startActivity(intent);
        }
    }
    @OnClick(R.id.btnViewVideo)
    void intentVideo(){
        if (etInputBills.getText().toString().isEmpty()){
            OthersUtil.showTipsDialog(this,"请先输入单号！");
        }else {
            Intent intent=new Intent(this,ShowVideoActivity.class);
            intent.putExtra("BillNumber",etInputBills.getText().toString());
            startActivity(intent);
        }
    }
    @OnClick(R.id.btnMeetingRecords)
    void intentRecords(){
        if (etInputBills.getText().toString().isEmpty()){
            OthersUtil.showTipsDialog(this,"请先输入单号！");
        }else {
            Intent intent=new Intent(this,MakeBillsSecondActivity.class);
            intent.putExtra("BillNumber",etInputBills.getText().toString());
            startActivity(intent);
        }
    }
    class CompleteReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
               Toast.makeText(getApplicationContext(),"视频下载完毕！",Toast.LENGTH_LONG).show();
            }
            if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
                downloadManager.remove((Long)reference);
                Log.e("TAG","下载完成");
            }
        }
    }

    @OnClick(R.id.btnTargetYield)
    void getYield(){
        //产量
        Intent intent=new Intent(this,ShowYieldActivity.class);
        startActivity(intent);
    }

//    @OnClick(R.id.btnChangePo)
//    void intentChangePo(){
//        //转换款页面
//        Intent intent=new Intent(this,ChangePoActivity.class);
//        startActivity(intent);
//    }
    @OnClick(R.id.btnTransformPo)
    void intentTransformPo(){
        //转换款页面
        String role = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
        if (role.equalsIgnoreCase("B82211")){
            Intent intent=new Intent(this,PoTransformActivity.class);
            startActivity(intent);
        }else {
            Intent intent=new Intent(this,TransformPoListActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(completeReceiver);
        super.onDestroy();
    }
}
