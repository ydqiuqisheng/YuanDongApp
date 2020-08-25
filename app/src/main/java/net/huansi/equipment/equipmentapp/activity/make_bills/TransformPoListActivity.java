package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.logging_bill.LoggingBillActivity;
import net.huansi.equipment.equipmentapp.activity.logging_bill.LoggingBillSearchActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.LogBillGroup;
import net.huansi.equipment.equipmentapp.entity.TransformPoDetail;
import net.huansi.equipment.equipmentapp.entity.TransformPoLists;
import net.huansi.equipment.equipmentapp.entity.TransformPoRecords;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class TransformPoListActivity extends BaseActivity {

    @BindView(R.id.TransformPoList)
    ListView lv_poList;
    private TransformPoDetail poDetail;
    private List<TransformPoDetail> poDetails;
    private LoadProgressDialog dialog;
    private TransformPoAdapter poAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_transform_polist;
    }

    @Override
    public void init() {
        TextView subTitle = getSubTitle();

        //转换款页面
        String role = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
        if (role.equalsIgnoreCase("A10010")){
            subTitle.setText("建单");
            subTitle.setVisibility(View.VISIBLE);
        }else {
            subTitle.setText("修改");
            subTitle.setVisibility(View.VISIBLE);
        }

    setToolBarTitle("你的待确认款");
    dialog=new LoadProgressDialog(this);
    poDetails=new ArrayList<>();
        //String role = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
        initPoInfo(role);

        subTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TransformPoListActivity.this,PoTransformActivity.class);
                startActivity(intent);
            }
        });

    }



//    @Override
//    protected void onResume() {
//        poDetails=new ArrayList<>();
//        String role = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
//        initPoInfo(role);
//        super.onResume();
//    }

    @OnItemClick(R.id.TransformPoList)
    void getTransformInfo(int position){
        finish();
        Intent intent = new Intent(this,PoTransformActivity.class);
        String fepo = poDetails.get(position).FEPO;
        String sewline = poDetails.get(position).SEWLINE;
        String transformday = poDetails.get(position).TRANSFORMDAY;
        //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("FEPO",fepo);
        intent.putExtra("SEW",sewline);
        intent.putExtra("DATE",transformday);
        startActivity(intent);


    }

    @OnItemLongClick(R.id.TransformPoList)
    boolean deleteTransformInfo(int position){
        inputAreaDialog(position,false);
        return true;
    }

    private void inputAreaDialog(final int position, final boolean isNotDismissByNotInput){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View areaDialogView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.area_input_dialog,null);
        final EditText editText= (EditText) areaDialogView.findViewById(R.id.etInventoryAreaDialog);
        final TextView textView= (TextView) areaDialogView.findViewById(R.id.tvInventoryAreaTitle);
        editText.setHint("在此输入");
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        textView.setText("删除此单请下面框中输入确认码并点击确定");
        editText.setTextColor(Color.BLACK);
        builder.setView(areaDialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String password=editText.getText().toString().trim();
                        if(password.isEmpty()){
                            OthersUtil.dialogNotDismissClickOut(dialogInterface);
                            OthersUtil.ToastMsg(getApplicationContext(),"输入不能为空白");
                            return;
                        }
                        OthersUtil.dialogDismiss(dialogInterface);
                        dialogInterface.dismiss();
                        DeletePoInfo(position,password);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(isNotDismissByNotInput){
                            OthersUtil.dialogNotDismissClickOut(dialogInterface);
                        }else {
                            OthersUtil.dialogDismiss(dialogInterface);
                            dialogInterface.dismiss();
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void DeletePoInfo(final int position, String password) {
        final String fepo = poDetails.get(position).FEPO;
        final String sewline = poDetails.get(position).SEWLINE;
        if (password.equalsIgnoreCase("123")) {
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(TransformPoListActivity.this, "")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(), "spAPP_DeleteTransformPo_list",
                                "Fepo=" + fepo +
                                        ",SewLine=" + sewline, String.class.getName(), false, "info获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                OthersUtil.ToastMsg(getApplicationContext(),"删除成功");

                OthersUtil.dismissLoadDialog(dialog);
                        poDetails.remove(position);
                        poAdapter.notifyDataSetChanged();

            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG", "errorLog2=" + hsWebInfo.json);
            }
        });


    }else {
            OthersUtil.ToastMsg(getApplicationContext(),"输入不正确");
        }
    }


    private void initPoInfo(final String role) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(TransformPoListActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAPP_GetTransformPo_list",
                                "Type="+role,String.class.getName(),false,"info获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAGG","Json111="+json);
                TransformPoLists PoLists = JSON.parseObject(json, TransformPoLists.class);
                List<TransformPoLists.DATABean> data = PoLists.getDATA();
                for (int i=0;i<data.size();i++){
                    poDetail=new TransformPoDetail();
                    poDetail.FEPO=data.get(i).getFEPO();
                    poDetail.SEWLINE=data.get(i).getSEWLINE();
                    poDetail.TRANSFORMDAY=data.get(i).getTRANSFORMDAY();
                    poDetail.CUSTOMERNAME=data.get(i).getCUSTOMERNAME();
                    poDetails.add(poDetail);
                }
                poAdapter=new TransformPoAdapter(poDetails,getApplicationContext());
                lv_poList.setAdapter(poAdapter);

            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
            }
        });



    }

    private class TransformPoAdapter extends HsBaseAdapter<TransformPoDetail>{
        public TransformPoAdapter(List<TransformPoDetail> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null)  convertView=mInflater.inflate(R.layout.activity_transform_polist_item,viewGroup,false);
            TransformPoDetail transformPoDetail = mList.get(position);
            TextView fepo = ViewHolder.get(convertView, R.id.transformFEPO);
            TextView sew = ViewHolder.get(convertView, R.id.transformSewLine);
            TextView date = ViewHolder.get(convertView, R.id.transformPoDate);
            TextView brand = ViewHolder.get(convertView, R.id.transformPoBrand);
            fepo.setText(transformPoDetail.FEPO);
            sew.setText(transformPoDetail.SEWLINE);
            date.setText(transformPoDetail.TRANSFORMDAY);
            brand.setText(transformPoDetail.CUSTOMERNAME);
            return convertView;
        }
    }




}
