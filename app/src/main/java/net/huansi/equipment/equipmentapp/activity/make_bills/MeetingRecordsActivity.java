package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.PDFEntity;
import net.huansi.equipment.equipmentapp.entity.PdfDetail;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
import rx.functions.Func1;

public class MeetingRecordsActivity extends BaseActivity {
    private LoadProgressDialog dialog;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private PdfDetail pdfDetail;
    private List<PdfDetail> mList;
    private PDFAdapter mPdfAdapter;
    private int requestCode=1;
    @BindView(R.id.meeting_records) ListView pdfList;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_records;
    }

    @Override
    public void init() {
        setToolBarTitle("所有款号");
        dialog=new LoadProgressDialog(this);
        mList=new ArrayList<>();
        String billNumber = getIntent().getStringExtra("BillNumber");
        getPDFDetail(billNumber);
    }



    private void getPDFDetail(final String billNumber) {
        Log.e("TAG","bill="+billNumber);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(MeetingRecordsActivity.this, hsWebInfo)
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP",
                                        "Proc_GetConferencePDFData",
                                        "FEPOCode="+billNumber,
                                        String.class.getName(),
                                        false,
                                        "helloWorld");
                            }
                        })
                ,MeetingRecordsActivity.this, dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        Log.e("TAG", "successMeeting="+hsWebInfo.json);
                        //json解析
                        PDFEntity pdfEntity = JSON.parseObject(json, PDFEntity.class);
                        List<PDFEntity.DATABean> data = pdfEntity.getDATA();
                        for (int i=0;i<data.size();i++){
                            pdfDetail=new PdfDetail();
                            pdfDetail.FEPO=data.get(i).getFEPO();
                            pdfDetail.SAMPLETYPE=data.get(i).getSAMPLETYPE();
                            pdfDetail.BUYMSG=data.get(i).getBUYMSG();
                            pdfDetail.REMARK=data.get(i).getPDF();
                            mList.add(pdfDetail);
                        }
                        mPdfAdapter=new MeetingRecordsActivity.PDFAdapter(mList,getApplicationContext());
                        pdfList.setAdapter(mPdfAdapter);
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "errorMeeting="+hsWebInfo.json);
                    }
                });
    }


    @OnItemClick(R.id.meeting_records)
    void showUnSeeList(int position){
        Log.e("TAG","下标="+position);
        Log.e("TAG","date="+mList.get(position).toString());
        Intent intent=new Intent(this,MeetingPdfActivity.class);
        intent.putExtra("MTFEPO",mList.get(position).FEPO.toString());
        intent.putExtra("MTSAMPLETYPE",mList.get(position).SAMPLETYPE.toString());
        intent.putExtra("MTBUYMSG",mList.get(position).BUYMSG.toString());
        intent.putExtra("MTPOSITION",position);
        startActivityForResult(intent,requestCode);
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
            TextView pdf_remark=ViewHolder.get(convertView,R.id.pdf_remark);
            PdfDetail pdfDetail = mList.get(position);
            pdf_Fepo.setText(pdfDetail.FEPO);
            pdf_SampleType.setText(pdfDetail.SAMPLETYPE);
            pdf_BuyMsg.setText(pdfDetail.BUYMSG);
            int i = pdfDetail.REMARK.indexOf(pdfDetail.BUYMSG);
            int i1 = pdfDetail.REMARK.indexOf(".");
            Log.e("TAG","i="+i+"i1="+i1+"");
            pdf_remark.setText(pdfDetail.REMARK.substring(i,i1));
            return convertView;
        }
    }
}
