package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.github.barteksc.pdfviewer.PDFView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.MeetingEntity;
import net.huansi.equipment.equipmentapp.entity.PDFEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MeetingPdfActivity  extends BaseActivity{
    private Thread mThread;
    private LoadProgressDialog dialog;
    @BindView(R.id.meeting_pdf)
    PDFView pdfViewer;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_pdf;
    }

    @Override
    public void init() {
        setToolBarTitle("会议记录明细");
        dialog=new LoadProgressDialog(this);
        getPdfUrl();

    }

    private void getPdfUrl() {
        final String fepoCode = getIntent().getStringExtra("MTFEPO");
        final String sampleType = getIntent().getStringExtra("MTSAMPLETYPE");
        final String buyMsg = getIntent().getStringExtra("MTBUYMSG");
        final int position = getIntent().getIntExtra("MTPOSITION",1);
        Log.e("TAG",fepoCode+buyMsg+sampleType);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","Proc_GetConferencePDF",
                                "FEPOCode="+fepoCode+
                                        ",BuyMsg="+buyMsg+
                                        ",SampleType="+sampleType,String.class.getName(),false,"获取失败");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                MeetingEntity meetingEntity = JSON.parseObject(json, MeetingEntity.class);
                String pdfUrl = meetingEntity.getDATA().get(position).getPDF();
                Log.e("TAG","pdfUrl="+pdfUrl);
                startDownload(pdfUrl);
                Log.e("TAG","successUrl="+json);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog="+hsWebInfo.error.error);
            }
        });
    }

    private void startDownload(final String pdfUrl) {

        //根据返回URL,开启下载线程
        mThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(pdfUrl);
                    //打开连接
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    //实现连接
                    conn.connect();
                    //打开输入流
                    InputStream is = conn.getInputStream();
                    pdfViewer.fromStream(is)
                            .defaultPage(0)
                            .load();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    @Override
    protected void onDestroy() {
        mThread.interrupt();
        super.onDestroy();
    }
}
