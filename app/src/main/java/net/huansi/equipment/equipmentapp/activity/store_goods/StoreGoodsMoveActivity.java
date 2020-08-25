package net.huansi.equipment.equipmentapp.activity.store_goods;

import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.zxing.common.StringUtils;
import com.twelvemonkeys.lang.StringUtil;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.merge_goods.ScannerActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.MoveBoxInfo;
import net.huansi.equipment.equipmentapp.entity.MoveBoxShowInfo;
import net.huansi.equipment.equipmentapp.entity.MoveMergeInfo;
import net.huansi.equipment.equipmentapp.entity.MoveMergeShowInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class StoreGoodsMoveActivity extends BaseActivity {

    @BindView(R.id.smInfo)
    EditText smInfo;
    @BindView(R.id.storeMovePo)
    TextView storeMovePo;
    @BindView(R.id.storeMoveNum)
    TextView storeMoveNum;
    @BindView(R.id.newShelf)
    TextView newShelf;
    @BindView(R.id.lvBoxMove)
    ListView lvBoxMove;
    @BindView(R.id.move_confirm_abandon)
    LinearLayout move_confirm_abandon;
    private LoadProgressDialog dialog;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private List<String> allCtn;
    private MoveBoxAdapter moveBoxAdapter;
    private List<MoveBoxShowInfo> moveBoxShowList;
    private MoveBoxShowInfo moveBoxShowInfo;
    private List<Boolean> selectList = new ArrayList(); // 判断listview单选位置
    private List<String> mergeBarcode =new ArrayList<>();//需要并架的条码号集合（CheckBox选中的）
    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_goods_move;
    }

    @Override
    public void init() {
        setToolBarTitle("根据PO转移货架");
        dialog=new LoadProgressDialog(this);
        moveBoxAdapter=new MoveBoxAdapter();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (newShelf.getText().toString().isEmpty()){
            newShelf.setText(smInfo.getText().toString().trim());
            smInfo.getText().clear();
        }else {
            if (smInfo.getText().toString().length()<12){
                OthersUtil.ToastMsg(getApplicationContext(),"非箱号");
                smInfo.getText().clear();
            }
            //根据箱号获取PO下所有箱号信息
            if (smInfo.getText().toString().length()>12&& StringUtil.isNumber(smInfo.getText().toString().trim())) {
                Log.e("TAG","3");
                selectMoveBox();
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    private void selectMoveBox() {
        OthersUtil.showLoadDialog(dialog);
        try {
            final String user = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(StoreGoodsMoveActivity.this,"")
                    .map(new Func1<String, HsWebInfo>() {
                        @Override
                        public HsWebInfo call(String s) {
                            return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppProductStorageMove",
                                    "Type="+Integer.parseInt("1")+
                                            ",OrderCode="+smInfo.getText().toString().trim() +//单号或箱号
                                            ",Origin_Position="+""+
                                            ",Current_Position="+""+
                                            ",UserID="+user,String.class.getName(),false,"组别获取成功");
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
                    MoveBoxInfo moveBoxInfo = JSON.parseObject(json, MoveBoxInfo.class);
                    List<MoveBoxInfo.DATABean> data = moveBoxInfo.getDATA();
                    storeMovePo.setText(data.get(0).getCUSTOMERPO().trim());
                    moveBoxShowList=new ArrayList<>();
                    allCtn=new ArrayList<>();
                    for (MoveBoxInfo.DATABean item:data){
                        moveBoxShowInfo=new MoveBoxShowInfo();
                        allCtn.add(item.getBOXBARCODE());
                        moveBoxShowInfo.CUSTOMERPO=item.getCUSTOMERPO();
                        moveBoxShowInfo.BOXBARCODE=item.getBOXBARCODE();
                        moveBoxShowList.add(moveBoxShowInfo);
                    }
                    initCheck(false);
                    lvBoxMove.setAdapter(moveBoxAdapter);
                    if (moveBoxShowList.isEmpty()){
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


    @OnClick(R.id.box_storeInfo)
    void box_storeInfo(){
        final String join = Joiner.on(";").join(mergeBarcode);
        Log.e("TAG","join="+join);
        OthersUtil.showDoubleChooseDialog(StoreGoodsMoveActivity.this, "选中的货物将转移到" + newShelf.getText().toString() + "货架", null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OthersUtil.showLoadDialog(dialog);
                        try {
                            final String user = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
                            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(StoreGoodsMoveActivity.this,"")
                                    .map(new Func1<String, HsWebInfo>() {
                                        @Override
                                        public HsWebInfo call(String s) {
                                            return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppProductStorageMove",
                                                    "Type="+Integer.parseInt("2")+
                                                            ",OrderCode="+join +//单号或箱号
                                                            ",Origin_Position="+""+
                                                            ",Current_Position="+newShelf.getText().toString().trim()+
                                                            ",UserID="+user,String.class.getName(),false,"组别获取成功");
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                                @Override
                                public void success(HsWebInfo hsWebInfo) {
                                    String json = hsWebInfo.json;
                                    Log.e("TAG","JJson="+json);
                                    OthersUtil.dismissLoadDialog(dialog);
                                    OthersUtil.ToastMsg(StoreGoodsMoveActivity.this,"操作成功！");
                                }
                                @Override
                                public void error(HsWebInfo hsWebInfo) {
                                    OthersUtil.ToastMsg(getApplicationContext(),hsWebInfo.json);
                                    Log.e("TAG","error="+hsWebInfo.json);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }






    private void initCheck( Boolean flag) {
        for (int i=0;i<moveBoxShowList.size();i++){
            selectList.add(i,flag);
        }
    }


    private class MoveBoxAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return moveBoxShowList.size();
        }

        @Override
        public Object getItem(int position) {
            return moveBoxShowList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            Log.e("TAG","getView了");
            convertView= LayoutInflater.from(StoreGoodsMoveActivity.this).inflate(R.layout.listview_move_merge_item,null);
            CheckBox cbMerge= (CheckBox) convertView.findViewById(R.id.cbMoveMergeShowData);
            cbMerge.setText(
                    moveBoxShowList.get(position).BOXBARCODE
            );
            cbMerge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked){
                        selectList.set(position,true);

                        //PoList.add(moveMergeShowList.get(position).BARCODE);
                        Log.e("TAG","true");
                        if (!mergeBarcode.contains(moveBoxShowList.get(position).BOXBARCODE)){
                            mergeBarcode.add(moveBoxShowList.get(position).BOXBARCODE);
                        }
                        storeMoveNum.setText("已选"+mergeBarcode.size()+"箱");
                        moveBoxAdapter.notifyDataSetChanged();
                    }else {
                        selectList.set(position,false);
                        //PoList.remove(moveMergeShowList.get(position).BARCODE);
                        Log.e("TAG","false");
                        mergeBarcode.remove(moveBoxShowList.get(position).BOXBARCODE);
                        storeMoveNum.setText("已选"+mergeBarcode.size()+"箱");
                        moveBoxAdapter.notifyDataSetChanged();
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
        moveBoxAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.abandonAll)
    void abandonAll(){
        initCheck(false);
        mergeBarcode.removeAll(allCtn);
        storeMoveNum.setText("已选"+mergeBarcode.size()+"箱");
        moveBoxAdapter.notifyDataSetChanged();
    }
}
