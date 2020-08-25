package net.huansi.equipment.equipmentapp.activity.store_goods;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.logging_bill.LoggingBillActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.StoreLocation;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.StoreDetailUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class StoreGoodsInActivity extends BaseActivity {
    private LoadProgressDialog dialog;
    private List<String> billList;
    private ArrayAdapter billAdapter;
    private List<String> locationList;
    @BindView(R.id.tv_StoreNumber)
    TextView storeNumber;
    @BindView(R.id.lvBillNumberList)
    ListView lvBillNumber;
    @BindView(R.id.et_StoreNumber)
    EditText et_StoreNumber;
    @BindView(R.id.store_bill_bind)
    Button store_bill_bind;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_goods_in;
    }

    @Override
    public void init() {
        setToolBarTitle("入库");
        dialog=new LoadProgressDialog(this);
        billList=new ArrayList<>();
        billAdapter=new ArrayAdapter(getApplicationContext(),R.layout.string_item,R.id.text,billList);
        lvBillNumber.setAdapter(billAdapter);
        lvBillNumber.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = billList.get(i).toString().trim();
                String barcode = s.substring(0, s.length() - 1);
                searchLocation(barcode);
                Log.e("TAG","长按");
                return true;
            }
        });
    }

    @OnClick(R.id.store_clear)
    void clearStorage(){
        storeNumber.setText("");
    }
    @OnItemClick(R.id.lvBillNumberList)
    void delete(final int position){
        OthersUtil.showDoubleChooseDialog(StoreGoodsInActivity.this, "确认删除该单据号?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                billList.remove(position);

                billAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.store_bill_bind)
    void bindStore2Bill(){
        OthersUtil.showDoubleChooseDialog(StoreGoodsInActivity.this, "确认绑定吗?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OthersUtil.showLoadDialog(dialog);
                if (storeNumber.getText().toString().isEmpty()&&billList.isEmpty()){
                    OthersUtil.ToastMsg(StoreGoodsInActivity.this,"内容为空无法绑定!");
                    return;
                }
                final String join = Joiner.on(";").join(billList);
                Log.e("TAG","billList="+ join);
                final String user = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
                try {

                    NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(StoreGoodsInActivity.this,"")
                            .map(new Func1<String, HsWebInfo>() {
                                @Override
                                public HsWebInfo call(String s) {
                                    return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppProductStorageMove",
                                            "Type="+Integer.parseInt("0")+
                                                    ",OrderCode="+ join+//单号或箱号
                                                    ",Origin_Position="+""+
                                                    ",Current_Position="+storeNumber.getText().toString().trim()+
                                                    ",UserID="+user,String.class.getName(),false,"组别获取成功");
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                        @Override
                        public void success(HsWebInfo hsWebInfo) {
                            String json = hsWebInfo.json;
                            Log.e("TAG","sizesJson="+json);
                            OthersUtil.dismissLoadDialog(dialog);
                            OthersUtil.ToastMsg(getApplicationContext(),"绑定成功");
                            storeNumber.setText("");
                            billList.clear();
                            billAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void error(HsWebInfo hsWebInfo) {
                            Log.e("TAG","error="+hsWebInfo.json);
                            storeNumber.setText("");
                            OthersUtil.ToastMsg(getApplicationContext(),"绑定失败原因"+hsWebInfo.json);
                        }
                    });
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (storeNumber.getText().toString().isEmpty()){
           storeNumber.setText( et_StoreNumber.getText().toString().trim());
           et_StoreNumber.getText().clear();
        }else {
                String s = et_StoreNumber.getText().toString().trim();
            if (!billList.contains(s)&&!s.isEmpty()){
                billList.add(s);
            }
            billAdapter.notifyDataSetChanged();
            store_bill_bind.setText("绑定"+billList.size()+"条记录");
            et_StoreNumber.getText().clear();
        }
        return super.onKeyUp(keyCode, event);

    }

    private void searchLocation(final String barcode) {
        try {

            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(StoreGoodsInActivity.this,"")
                    .map(new Func1<String, HsWebInfo>() {
                        @Override
                        public HsWebInfo call(String s) {
                            return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppProductStorageMove_QueryByBarCode",
                                    "BoxBarCode="+barcode,String.class.getName(),false,"组别获取成功");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                @Override
                public void success(HsWebInfo hsWebInfo) {
                    String json = hsWebInfo.json;
                    Log.e("TAG","locationJson="+json);
                    StoreLocation storeLocation = JSON.parseObject(json, StoreLocation.class);
                    List<StoreLocation.DATABean> data = storeLocation.getDATA();
                    locationList=new ArrayList<>();
                    for (int i=0;i<data.size();i++){
                        locationList.add(data.get(i).getSTOCKPOSITION_CURRENT().toString());
                    }
                    final String[] array = locationList.toArray(new String[data.size()]);
                    AlertDialog alertDialog = new AlertDialog.Builder(StoreGoodsInActivity.this)
                            .setTitle("选择一个储位")
                            .setIcon(R.drawable.app_icon)
                            .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                                @Override
                                public void onClick(DialogInterface dialogInterface, final int which) {
                                    storeNumber.setText(array[which]);
                                }
                            }).create();
                    alertDialog.show();


                }
                @Override
                public void error(HsWebInfo hsWebInfo) {
                    Log.e("TAG","error="+hsWebInfo.json);
                    OthersUtil.ToastMsg(getApplicationContext(),"所属PO没有已绑定库位");
                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
}
