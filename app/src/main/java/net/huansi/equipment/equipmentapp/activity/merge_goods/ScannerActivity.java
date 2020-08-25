package net.huansi.equipment.equipmentapp.activity.merge_goods;

import android.content.ContentProvider;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.MoveBoxShowInfo;
import net.huansi.equipment.equipmentapp.entity.ScannerEntity;
import net.huansi.equipment.equipmentapp.entity.ScannerInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhou.mi on 2018/1/31.
 */


public class ScannerActivity extends BaseActivity {
    @BindView(R.id.smInfo)
    EditText smInfo;
    @BindView(R.id.scanner_location)
    EditText location;
    @BindView(R.id.scanner_gallery)
    EditText gallery;

    @BindView(R.id.oldShelf)
    TextView oldShelf;
    @BindView(R.id.storeMoveNum)
    TextView storeMoveNum;
    @BindView(R.id.newShelf)
    TextView newShelf;
    @BindView(R.id.lvBoxMove)
    ListView lvBoxMove;
    @BindView(R.id.createTime)
    TextView createTime;
    @BindView(R.id.move_confirm_abandon)
    LinearLayout move_confirm_abandon;
    private LoadProgressDialog dialog;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private List<String> allCtn;
    private ScannerActivity.MoveClothAdapter moveClothAdapter;
    private List<ScannerInfo> scannerList;
    private ScannerInfo scannerInfo;
    private List<Boolean> selectList = new ArrayList(); // 判断listview单选位置
    private List<String> mergeBarcode =new ArrayList<>();//需要挪的条码号集合（CheckBox选中的）

    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        setToolBarTitle("原布挪料并架");
        dialog=new LoadProgressDialog(this);
        moveClothAdapter=new MoveClothAdapter();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (newShelf.getText().toString().isEmpty()){
            newShelf.setText(smInfo.getText().toString().trim());
            smInfo.getText().clear();
            Log.e("TAG","相淇译");
        }else {
            if (smInfo.getText().toString().length()>12){
                selectMoveBox();

            }
            //根据货架号获取剩余布匹条码
//            if (smInfo.getText().toString().length()>12&& StringUtil.isNumber(smInfo.getText().toString().trim())) {
//                Log.e("TAG","3");
//                selectMoveBox();
//            }
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.scanner_activity;
    }


    @OnClick(R.id.scanner_start)
    void moveBegin(){

        OthersUtil.showDoubleChooseDialog(ScannerActivity.this, "确认将选中布匹转移至新的库位？", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OthersUtil.showLoadDialog(dialog);
                final String join = Joiner.on(";").join(mergeBarcode);
                Log.e("TAG","join="+join);
                try {
                    //final String user = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
                    NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ScannerActivity.this,"")
                            .map(new Func1<String, HsWebInfo>() {
                                @Override
                                public HsWebInfo call(String s) {
                                    return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppFabricStorageMove",
                                            "Type="+Integer.parseInt("2")+
                                                    ",Barcode="+join.trim() +//单号或箱号
                                                    ",Area="+location.getText().toString().trim()+
                                                    ",Location="+gallery.getText().toString().trim()+
                                                    ",Shelf="+newShelf.getText().toString().trim(),String.class.getName(),true,"挪位成功");
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                        @Override
                        public void success(HsWebInfo hsWebInfo) {
                            String json = hsWebInfo.json;
                            Log.e("TAG","JJson="+json);

                            OthersUtil.dismissLoadDialog(dialog);


                        }
                        @Override
                        public void error(HsWebInfo hsWebInfo) {
                            OthersUtil.ToastMsg(getApplicationContext(),"转移库位成功！");
                            Log.e("TAG","error="+hsWebInfo.json);
                        }
                    });
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void selectMoveBox() {
        OthersUtil.showLoadDialog(dialog);
        try {
            //final String user = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ScannerActivity.this,"")
                    .map(new Func1<String, HsWebInfo>() {
                        @Override
                        public HsWebInfo call(String s) {
                            return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppFabricStorageMove",
                                    "Type="+Integer.parseInt("1")+
                                            ",Barcode="+smInfo.getText().toString().trim() +//单号或箱号
                                            ",Area="+""+
                                            ",Location="+""+
                                            ",Shelf="+"",String.class.getName(),false,"组别获取成功");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                @Override
                public void success(HsWebInfo hsWebInfo) {
                    String json = hsWebInfo.json;
                    Log.e("TAG","JJson="+json);
                    smInfo.getText().clear();
                    OthersUtil.dismissLoadDialog(dialog);
                    if (json==null){
                        return;
                    }
                    ScannerEntity scannerEntity = JSON.parseObject(json, ScannerEntity.class);
                    List<ScannerEntity.DATABean> data = scannerEntity.getDATA();
                    oldShelf.setText(data.get(0).getSHELF().trim());//货架
                    location.setText(data.get(0).getAREA().trim());//库区
                    gallery.setText(data.get(0).getLOCATION().trim());//通道
                    scannerList=new ArrayList<>();
                    allCtn=new ArrayList<>();
                    for (ScannerEntity.DATABean item:data){
                        scannerInfo=new ScannerInfo();
                        allCtn.add(item.getBARCODE());
                        scannerInfo.AREA=item.getAREA();
                        scannerInfo.LOCATION=item.getLOCATION();
                        scannerInfo.SHELF=item.getSHELF();
                        scannerInfo.MATERIALCODE=item.getMATERIALCODE();
                        scannerInfo.BARCODE=item.getBARCODE();
                        scannerInfo.COLORCODE=item.getCOLORCODE();
                        scannerInfo.COLORNAME=item.getCOLORNAME();
                        scannerInfo.QUANTITY=item.getQUANTITY();
                        scannerInfo.CREATEDATETIME=item.getCREATEDATETIME();
                        scannerInfo.VATNO=item.getVATNO();
                        scannerInfo.PNO=item.getPNO();
                        scannerInfo.QUANTITYPS=item.getQUANTITYPS();
                        scannerList.add(scannerInfo);
                    }
                    initCheck(false);
                    lvBoxMove.setAdapter(moveClothAdapter);
                    if (scannerList.isEmpty()){
                        move_confirm_abandon.setVisibility(View.GONE);
                    }else {
                        move_confirm_abandon.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void error(HsWebInfo hsWebInfo) {
                    smInfo.getText().clear();
                    OthersUtil.ToastMsg(getApplicationContext(),"该PO还未绑定库位");
                    Log.e("TAG","error="+hsWebInfo.json);
                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    private void initCheck( Boolean flag) {
        for (int i=0;i<scannerList.size();i++){
            selectList.add(i,flag);
        }
    }
    //自动接收的数据
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(MessageEvent event){
//        Log.e("TAG", "onEvent...."+event.getMessage());
//        etSearchBarcode.getText().clear();
//        etSearchBarcode.setText(event.getMessage());
//    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

    }


    private class MoveClothAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return scannerList.size();
        }

        @Override
        public Object getItem(int position) {
            return scannerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            Log.e("TAG","getView了");
            convertView= LayoutInflater.from(ScannerActivity.this).inflate(R.layout.listview_move_merge_item,null);
            CheckBox cbMerge= (CheckBox) convertView.findViewById(R.id.cbMoveMergeShowData);
            ScannerInfo scannerInfo = scannerList.get(position);
            createTime.setText(scannerInfo.CREATEDATETIME);
            cbMerge.setText(
                    scannerInfo.MATERIALCODE+"/"+scannerInfo.PNO+"/"+scannerInfo.COLORNAME+"/"+scannerInfo.QUANTITYPS+"/"+scannerInfo.VATNO
            );
            cbMerge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked){
                        selectList.set(position,true);

                        //PoList.add(moveMergeShowList.get(position).BARCODE);
                        Log.e("TAG","true");
                        if (!mergeBarcode.contains(scannerList.get(position).BARCODE)){
                            mergeBarcode.add(scannerList.get(position).BARCODE);
                        }
                        storeMoveNum.setText("已选"+mergeBarcode.size()+"箱");
                        moveClothAdapter.notifyDataSetChanged();
                    }else {
                        selectList.set(position,false);
                        //PoList.remove(moveMergeShowList.get(position).BARCODE);
                        Log.e("TAG","false");
                        mergeBarcode.remove(scannerList.get(position).BARCODE);
                        storeMoveNum.setText("已选"+mergeBarcode.size()+"箱");
                        moveClothAdapter.notifyDataSetChanged();
                    }


                }
            });
            cbMerge.setChecked(selectList.get(position));
            Log.e("TAG","mergeBarcode="+mergeBarcode);

            return convertView;
        }
    }


    @OnClick(R.id.confirmAll)
    void confirmAll(){
        initCheck(true);
        if (!mergeBarcode.containsAll(allCtn)){
            mergeBarcode.clear();
            mergeBarcode.addAll(allCtn);
        }
        storeMoveNum.setText("已选"+mergeBarcode.size()+"箱");
        moveClothAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.abandonAll)
    void abandonAll(){
        initCheck(false);
        mergeBarcode.removeAll(allCtn);
        storeMoveNum.setText("已选"+mergeBarcode.size()+"箱");
        moveClothAdapter.notifyDataSetChanged();
    }
}
