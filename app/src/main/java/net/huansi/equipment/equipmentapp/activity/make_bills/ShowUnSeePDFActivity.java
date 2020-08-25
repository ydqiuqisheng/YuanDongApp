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
import net.huansi.equipment.equipmentapp.entity.SimpleEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class ShowUnSeePDFActivity extends BaseActivity {
    private LoadProgressDialog dialog;
    private PdfDetail pdfDetail;
    private PDFAdapter mPdfAdapter;
    private List<PdfDetail> mList;
    private int requestCode=1;
    @BindView(R.id.UnSeePDFList)
    ListView UnSeePDFList;
    @Override
    protected int getLayoutId() {
        return R.layout.show_unsee_pdf_activity;
    }

    @Override
    public void init() {
        dialog=new LoadProgressDialog(this);
        mList=new ArrayList<>();
        getNeedPushList();
    }

    @OnItemClick(R.id.UnSeePDFList)
    void showUnSeeList(int position){
        Intent intent=new Intent(this,PDFViewerActivity.class);
        intent.putExtra("PDFURL",mList.get(position).PDF.toString());
        intent.putExtra("PDFPRODUCEORDERITEMID",mList.get(position).PRODUCEORDERITEMID.toString());
        intent.putExtra("PDFBUYMSG",mList.get(position).BUYMSG.toString());
        startActivityForResult(intent,requestCode);
    }
    private void getNeedPushList() {
        final String userID = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
        Log.e("TAG","userID="+userID);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GetNeedPushProduceOrderList",
                                "UserID="+userID,String.class.getName(),false,"获取失败");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","needPush="+json);
                //json解析
                PDFEntity pdfEntity = JSON.parseObject(json, PDFEntity.class);
                List<PDFEntity.DATABean> data = pdfEntity.getDATA();
                for (int i=0;i<data.size();i++){
                    pdfDetail=new PdfDetail();
                    pdfDetail.FEPO=data.get(i).getFEPO();
                    pdfDetail.SAMPLETYPE=data.get(i).getSAMPLETYPE();
                    pdfDetail.PRODUCEORDERITEMID=data.get(i).getPRODUCEORDERITEMID();
                    pdfDetail.BUYMSG=data.get(i).getBUYMSG();
                    pdfDetail.PDF=data.get(i).getPDF();
                    pdfDetail.REMARK=data.get(i).getREMARK();
                    mList.add(pdfDetail);
                }
                mPdfAdapter=new PDFAdapter(mList,getApplicationContext());
                UnSeePDFList.setAdapter(mPdfAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1){
            Log.e("TAG","onActivityResult(╥╯^╰╥)");
            mList.clear();
            getNeedPushList();
        }
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
            TextView pdf_Remark = ViewHolder.get(convertView, R.id.pdf_remark);
            PdfDetail pdfDetail = mList.get(position);
            pdf_Fepo.setText(pdfDetail.FEPO);
            pdf_SampleType.setText(pdfDetail.SAMPLETYPE);
            pdf_BuyMsg.setText(pdfDetail.BUYMSG);
            pdf_Remark.setText(pdfDetail.REMARK);
            return convertView;
        }
    }
}
