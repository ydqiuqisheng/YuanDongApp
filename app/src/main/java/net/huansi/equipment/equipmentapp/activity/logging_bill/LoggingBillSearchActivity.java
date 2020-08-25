package net.huansi.equipment.equipmentapp.activity.logging_bill;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.LogSearchEntity;
import net.huansi.equipment.equipmentapp.entity.LogSearchInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoggingBillSearchActivity extends BaseActivity{
    @BindView(R.id.log_FrameNo_Search)
    TextView frameNo;
    @BindView(R.id.lv_SearchLogs)
    ListView lv_searchInfo;

    private LogSearchInfo searchInfo;
    private List<LogSearchInfo> searchInfoList;
    private SearchInfoAdapter mAdapter;
    private int REQUEST_CODE=1;
    private LoadProgressDialog dialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_logging_bill_search;
    }

    @Override
    public void init() {
        setToolBarTitle("单据查询");
        dialog=new LoadProgressDialog(this);
        searchInfoList=new ArrayList<>();
    }


    @OnClick(R.id.log_FrameNo_Search)
    void openCamera(){

        Intent intent = new Intent(LoggingBillSearchActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.log_double_commit)
    void commit(){
        if (frameNo.getText().toString().isEmpty()){
            OthersUtil.ToastMsg(getApplicationContext(),"请先扫描框号!");
            return;
        }
        OthersUtil.showDoubleChooseDialog(this, "确认二次提交?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                //todo 找一个框号测试
                if (searchInfoList.size()==0){
                    OthersUtil.ToastMsg(getApplicationContext(),"无数据可提交");
                    return;
                }
                OthersUtil.showLoadDialog(dialog);
                final String itemid = searchInfoList.get(0).ITEMID;
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(LoggingBillSearchActivity.this,"")
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"CommitDataToSTP_SewFastDeliver",
                                        "ItemID="+itemid,
                                        String.class.getName(),false,"组别获取成功");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        Log.e("TAG","successLog="+hsWebInfo.json);
                        searchInfoList.clear();
                        mAdapter.notifyDataSetChanged();
                        OthersUtil.ToastMsg(getApplicationContext(),"提交成功");
                    }
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG","errorLog="+hsWebInfo.json);
                        searchInfoList.clear();
                        mAdapter.notifyDataSetChanged();
                        OthersUtil.ToastMsg(getApplicationContext(),"提交成功");
                    }
                });
                OthersUtil.dismissLoadDialog(dialog);
                finish();
//                NewRxjavaWebUtils.getJsonData(getApplicationContext(),"CommitDataToSTP_SewFastDeliver","ItemID="+itemid,String.class.getName(),
//                        false,"error");

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
                    searchBillInfo(result);
                    frameNo.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(LoggingBillSearchActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void searchBillInfo(final String result) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(LoggingBillSearchActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spQueryDataSewFastDeliver",
                                "FrameCode="+result,
                                String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","searchJson="+json);
                LogSearchEntity logSearchEntity = JSON.parseObject(json, LogSearchEntity.class);
                List<LogSearchEntity.DATABean> data = logSearchEntity.getDATA();
                searchInfoList.clear();
                for (int i=0;i<data.size();i++){
                    searchInfo=new LogSearchInfo();
                    searchInfo.ITEMID=data.get(i).getITEMID();
                    searchInfo.FRAMECODE=data.get(i).getFRAMECODE();
                    searchInfo.POSTNAME=data.get(i).getPOSTNAME();
                    searchInfo.FEPOCODE=data.get(i).getFEPOCODE();
                    searchInfo.CUSTOMERPO=data.get(i).getCUSTOMERPO();
                    searchInfo.COMBNAME=data.get(i).getCOMBNAME();
                    searchInfo.SIZENAME=data.get(i).getSIZENAME();
                    searchInfo.QUANTITY=data.get(i).getQUANTITY();
                    searchInfo.FINALCHECK=data.get(i).getFINALCHECK();
                    searchInfoList.add(searchInfo);
                }
                mAdapter=new SearchInfoAdapter(searchInfoList,getApplicationContext());
                lv_searchInfo.setAdapter(mAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog="+hsWebInfo.json);
            }
        });
    }

    private class SearchInfoAdapter extends HsBaseAdapter<LogSearchInfo>{
        public SearchInfoAdapter(List<LogSearchInfo> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null) convertView= mInflater.inflate(R.layout.logging_bill_search_item,viewGroup,false);
            TextView group = ViewHolder.get(convertView, R.id.log_searchGroup);
            TextView po = ViewHolder.get(convertView, R.id.log_searchPo);
            TextView comb = ViewHolder.get(convertView, R.id.log_searchComb);
            TextView size = ViewHolder.get(convertView, R.id.log_searchSize);
            TextView qty = ViewHolder.get(convertView, R.id.log_searchQty);
            TextView cp = ViewHolder.get(convertView, R.id.log_searchCP);//客户po
            TextView state = ViewHolder.get(convertView, R.id.log_searchState);
            LogSearchInfo logSearchInfo = mList.get(position);
            group.setText(logSearchInfo.POSTNAME);
            po.setText(logSearchInfo.FEPOCODE);
            comb.setText(logSearchInfo.COMBNAME);
            size.setText(logSearchInfo.SIZENAME);
            qty.setText(logSearchInfo.QUANTITY);
            cp.setText(logSearchInfo.CUSTOMERPO);
            state.setText(logSearchInfo.FINALCHECK);
            return convertView;
        }
    }
}
