package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.github.barteksc.pdfviewer.PDFView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.PDFEntity;
import net.huansi.equipment.equipmentapp.entity.PdfDetail;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class PDFViewerActivity extends BaseActivity {
    private Thread mThread;
    private LoadProgressDialog dialog;
    @BindView(R.id.UnSeePdfViewer)
    PDFView pdfViewer;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pdf_viewer;
    }

    @Override
    public void init() {
        setToolBarTitle("PDF明细");
        dialog=new LoadProgressDialog(this);
        startDownload();
        insertLog();//已经查看过的后台打标识
    }

    private void startDownload() {
        final String pdfUrl = getIntent().getStringExtra("PDFURL");
        Log.e("TAG","PDFURL="+pdfUrl);
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

    private void insertLog() {
        final String produceOrderItemId = getIntent().getStringExtra("PDFPRODUCEORDERITEMID");
        final String buyMsg = getIntent().getStringExtra("PDFBUYMSG");
        Log.e("TAG","produceOrderItemId="+produceOrderItemId);
        Log.e("TAG","buyMsg="+buyMsg);
        final String user = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_PDMProduceOrderReceive_Log",
                                "ProduceOrderItemID="+produceOrderItemId+
                                        ",BuyMsg="+buyMsg+
                                        ",ReceiveUserID="+user,String.class.getName(),false,"获取失败");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","successLog="+json);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog="+hsWebInfo.json);
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.e("TAG","Destory");

        mThread.interrupt();
        super.onDestroy();
    }
}
