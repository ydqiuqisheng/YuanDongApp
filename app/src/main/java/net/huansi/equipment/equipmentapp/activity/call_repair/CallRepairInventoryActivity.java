package net.huansi.equipment.equipmentapp.activity.call_repair;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.check_quality.CutPiecesActivity;
import net.huansi.equipment.equipmentapp.activity.repair.RepairDetailActivity;
import net.huansi.equipment.equipmentapp.activity.repair.RepairListActivity;
import net.huansi.equipment.equipmentapp.adapter.RepairListAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.RepairList;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.gen.RepairEquipmentInSQLiteDao;
import net.huansi.equipment.equipmentapp.gen.RepairHdrInSQLiteDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairEquipmentInSQLite;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairHdrInSQLite;
import net.huansi.equipment.equipmentapp.util.NetUtil;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.PLAN_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.SQLITE_DATA_PARAM;

public class CallRepairInventoryActivity extends BaseActivity implements View.OnKeyListener {
    @BindView(R.id.callRepairCode)
    TextView callRepairCode;
    @BindView(R.id.callRepairList)
    ListView callRepairList;
    private LoadProgressDialog dialog;
    private List<RepairList> mList;
    private RepairListAdapter mAdapter;
    private int REQUEST_CODE=1;
    @Override
    protected int getLayoutId() {
        return R.layout.call_repair_inventory_activity;
    }

    @Override
    public void init() {
    setToolBarTitle("机台列表");
        mList=new ArrayList<>();
        dialog=new LoadProgressDialog(this);
        mAdapter=new RepairListAdapter(mList,getApplicationContext());
        callRepairList.setAdapter(mAdapter);
        callRepairCode.setOnKeyListener(this);
        ZXingLibrary.initDisplayOpinion(this);
    }
    @OnClick(R.id.scanMachineCode)
    void openCamera(){
        Intent intent = new Intent(CallRepairInventoryActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    /**
     * 维修详情界面
     */
    @OnItemClick(R.id.callRepairList)
    void toCallRepairDetail(int position){
        final RepairList list=mList.get(position);
        Log.e("TAG","state="+list.EPSTATUS);
        if (list.EPSTATUS.isEmpty()){
            OthersUtil.showLoadDialog(dialog);
            Observable.just(list)
                    .compose(this.<RepairList>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<RepairList, RepairHdrInSQLite>() {
                        @Override
                        public RepairHdrInSQLite call(RepairList repairList) {
                            RepairHdrInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext())
                                    .getRepairHdrInSQLiteDao();
                            Query<RepairHdrInSQLite> query=dao.queryBuilder()
                                    .where(RepairHdrInSQLiteDao.Properties.EquipmentId.eq(repairList.EQUIPMENTDETAILID))
                                    .where(RepairHdrInSQLiteDao.Properties.SubmitStatus.eq(0))
                                    .orderDesc(RepairHdrInSQLiteDao.Properties.Id)
                                    .build();
                            if(query==null) return null;
                            List<RepairHdrInSQLite> list=query.list();
                            if(list==null||list.isEmpty()) return null;
                            return list.get(0);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<RepairHdrInSQLite>() {
                        @Override
                        public void call(final RepairHdrInSQLite repairHdrInSQLite) {
                            OthersUtil.dismissLoadDialog(dialog);
                            Intent intent = new Intent(CallRepairInventoryActivity.this, CallRepairDetailActivity.class);
                            intent.putExtra(PLAN_PARAM, list);
                            startActivity(intent);
                        }
                    });
        }else {
            OthersUtil.showTipsDialog(CallRepairInventoryActivity.this,"该机器一周内已经叫修过,且目前为止未得到修理,需要先修理完毕才能继续叫修");
        }

    }

    /**
     * 查询维修的设备
     */
    private synchronized void searchEquipmentByRepair(String search){
        if(search.trim().isEmpty()){
            return;
        }
        OthersUtil.showLoadDialog(dialog);
        mList.clear();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, search)
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        //有网络就在线查询
                        if(NetUtil.isNetworkAvailable(getApplicationContext())){
                            return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                    "spAppEPQueryRepairPlanList",
                                    "sSearch=" + s,
                                    RepairList.class.getName(),
                                    false,
                                    "没有您要查询的维修/维护设备233");
                            //离线查询
                        }else {
                            HsWebInfo hsWebInfo=new HsWebInfo();
                            RepairEquipmentInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getRepairEquipmentInSQLiteDao();
                            List<RepairEquipmentInSQLite> repairEquipmentInSQLiteList=dao.loadAll();
                            List<WsEntity> entities=new ArrayList<>();
                            for(RepairEquipmentInSQLite repairEquipmentInSQLite:repairEquipmentInSQLiteList){
                                if(repairEquipmentInSQLite.getEpcCode().equalsIgnoreCase(s)||
                                        repairEquipmentInSQLite.getAssetsCode().equalsIgnoreCase(s)||
                                        repairEquipmentInSQLite.getOutFactoryCode().equalsIgnoreCase(s)){
                                    RepairList repairList=new RepairList();
                                    Log.e("TAG","a="+repairEquipmentInSQLite.getAssetsCode());
                                    repairList.ASSETSCODE=repairEquipmentInSQLite.getAssetsCode();
                                    Log.e("TAG","b="+repairEquipmentInSQLite.getCostCenter());
                                    repairList.COSTCENTER=repairEquipmentInSQLite.getCostCenter();
                                    Log.e("TAG","c="+repairEquipmentInSQLite.getEpcCode());
                                    repairList.EPCCODE=repairEquipmentInSQLite.getEpcCode();
                                    repairList.EQUIPMENTDETAILID=repairEquipmentInSQLite.getEquipmentDetailID();
                                    repairList.EQUIPMENTNAME=repairEquipmentInSQLite.getEquipmentName();
                                    repairList.FACTORY=repairEquipmentInSQLite.getFactory();
                                    repairList.MODEL=repairEquipmentInSQLite.getModel();
                                    repairList.OUTFACTORYCODE=repairEquipmentInSQLite.getOutFactoryCode();
                                    entities.add(repairList);
                                }
                            }
                            if(entities.isEmpty()){
                                hsWebInfo.success=false;
                                hsWebInfo.error.error="没有您要查询的维修/维护设备";
                                return hsWebInfo;
                            }
                            hsWebInfo.wsData=new WsData();
                            hsWebInfo.wsData.LISTWSDATA=entities;
                            Log.e("TAG","entities1="+entities);
                            return hsWebInfo;
                        }
                    }
                }), CallRepairInventoryActivity.this, dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                Log.e("TAG","entities2222="+hsWebInfo.json);
                for(int i=0;i<entities.size();i++){
                    mList.add((RepairList) entities.get(i));
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                super.error(hsWebInfo);
                OthersUtil.showTipsDialog(CallRepairInventoryActivity.this,hsWebInfo.error.error);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if(dialog.isShowing()) return false;
                if(event.getAction()==KeyEvent.ACTION_UP) {
                    String rfid = callRepairCode.getText().toString().trim();
                    //callRepairCode.getText().clear();
                    searchEquipmentByRepair(rfid);
                }
                break;
        }
        return false;
    }
//    @OnClick(R.id.searchMachineByCode)
//    void searchByCode(){
//        if (callRepairCode.getText().toString().isEmpty()){
//            OthersUtil.showTipsDialog(this,"请扫描或输入内容!");
//        }else {
//            String code = callRepairCode.getText().toString();
//            searchEquipmentByRepair(code);
//        }
//    }
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
                    searchEquipmentByRepair(replaceBlank(result));
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(CallRepairInventoryActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
//替换字符串中的空格、回车、换行符、制表符
        public static String replaceBlank(String str) {
            String dest = "";
            if (str != null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(str);
                dest = m.replaceAll("");
            }
            return dest;
        }
}
