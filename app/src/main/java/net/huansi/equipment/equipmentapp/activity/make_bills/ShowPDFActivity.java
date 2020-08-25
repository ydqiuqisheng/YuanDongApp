package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.squareup.picasso.Picasso;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.LargerImageSHowActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.CutPiecesDetail;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.PDFEntity;
import net.huansi.equipment.equipmentapp.entity.PdfDetail;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.JSONEntity;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import rx.functions.Func1;

import static net.huansi.equipment.equipmentapp.constant.Constant.LargerImageSHowActivityConstants.URL_PATH_PARAM;

public class ShowPDFActivity extends BaseActivity {
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private LoadProgressDialog dialog;
    private PDFAdapter mPdfAdapter;
    private List<PdfDetail> mList;
    private PdfDetail pdfDetail;
    private Thread mThread;
    @BindView(R.id.pdfViewer) PDFView pdfViewer;
    @BindView(R.id.PDFList) ListView PDFList;
    @BindView(R.id.PDFTitle) LinearLayout PDFTitle;
    @BindView(R.id.photoViewer)
    PhotoView photoView;
    @Override
    protected int getLayoutId() {
        return R.layout.show_pdf_activity;
    }

    @Override
    public void init() {
        setToolBarTitle("点击切换明细列表/PDF");
        dialog=new LoadProgressDialog(this);
        String billNumber = getIntent().getStringExtra("BillNumber");
        Log.e("TAG","number="+billNumber);
        mList=new ArrayList<>();
        getPDFDetail(billNumber);
        PDFList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String pdfUrl = mList.get(position).PDF;
                PDFTitle.setVisibility(View.GONE);
                PDFList.setVisibility(View.GONE);
                String substring = pdfUrl.substring(pdfUrl.length() - 3, pdfUrl.length());
                switch (substring){
                    case "peg":
                        photoView.setVisibility(View.VISIBLE);
                        photoView.enable();
                        photoView.setAnimaDuring(1000);
                        Log.e("TAG","picUrl="+pdfUrl);
                        Glide.with(ShowPDFActivity.this).load(pdfUrl).asBitmap().into(photoView);
                        break;
                    case "pdf":
                        pdfViewer.setVisibility(View.VISIBLE);
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
                        break;
                }
                Log.e("TAG","后缀="+substring);
            }
        });
    }
    @OnClick(R.id.tooltitle)
    void showPDFList(){
        //mThread.interrupt();
        PDFTitle.setVisibility(View.VISIBLE);
        PDFList.setVisibility(View.VISIBLE);
        pdfViewer.setVisibility(View.GONE);
    }
    private void getPDFDetail(final String billNumber) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ShowPDFActivity.this, hsWebInfo)
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP",
                                        "spAPP_GetUrl_ProduceOrderPDFAndGSDVideo",
                                        "FEPO="+billNumber+
                                                ",DataType="+"PO",
                                        String.class.getName(),
                                        false,
                                        "helloWorld");
                            }
                        })
                ,ShowPDFActivity.this, dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        Log.e("TAG", "successAPP="+hsWebInfo.json);
                        //json解析
                        PDFEntity pdfEntity = JSON.parseObject(json, PDFEntity.class);
                        List<PDFEntity.DATABean> data = pdfEntity.getDATA();
                        mList.clear();
                        for (int i=0;i<data.size();i++){
                            pdfDetail=new PdfDetail();
                            pdfDetail.FEPO=data.get(i).getFEPO();
                            pdfDetail.SAMPLETYPE=data.get(i).getSAMPLETYPE();
                            pdfDetail.BUYMSG=data.get(i).getBUYMSG();
                            pdfDetail.PDF=data.get(i).getPDF();
                            pdfDetail.FEPOLIST=data.get(i).getFEPOLIST();
                            pdfDetail.STYLEYEAR=data.get(i).getSTYLEYEAR();
                            pdfDetail.STYLESEASON=data.get(i).getSTYLESEASON();
                            mList.add(pdfDetail);
                        }
                        mPdfAdapter=new PDFAdapter(mList,getApplicationContext());
                        PDFList.setAdapter(mPdfAdapter);
                        //根据返回URL,开启下载线程
//                        DownloadThread downloadThread = new DownloadThread();
//                        downloadThread.start();
//                        uiHandler.post(downloadThread);
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "error1="+hsWebInfo.error.error);
                    }
                });

    }

    private class PDFAdapter extends HsBaseAdapter<PdfDetail>{

        public PDFAdapter(List<PdfDetail> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null) convertView=mInflater.inflate(R.layout.pdf_detail_item,viewGroup,false);
            TextView pdf_Fepo = ViewHolder.get(convertView, R.id.pdf_Fepo);
            TextView pdf_SampleType = ViewHolder.get(convertView, R.id.pdf_SampleType);
            TextView pdf_BuyMsg = ViewHolder.get(convertView, R.id.pdf_BuyMsg);
            TextView pdf_poList = ViewHolder.get(convertView, R.id.pdf_poList);
            TextView pdf_remark = ViewHolder.get(convertView, R.id.pdf_remark);
            PdfDetail pdfDetail = mList.get(position);
            pdf_Fepo.setText(pdfDetail.FEPO);
            pdf_SampleType.setText(pdfDetail.SAMPLETYPE);
            pdf_BuyMsg.setText(pdfDetail.BUYMSG);
            pdf_poList.setText(pdfDetail.FEPOLIST);
            pdf_remark.setText(pdfDetail.STYLEYEAR+"/"+pdfDetail.STYLESEASON);
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        try {
            mThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
