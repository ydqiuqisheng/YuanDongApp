package net.huansi.equipment.equipmentapp.activity.call_repair;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.awake_goods.AwakeMainActivity;
import net.huansi.equipment.equipmentapp.activity.check_quality.CutPiecesActivity;
import net.huansi.equipment.equipmentapp.activity.repair.RepairMainActivity;
import net.huansi.equipment.equipmentapp.entity.CutPiecesDetail;
import net.huansi.equipment.equipmentapp.entity.CutPiecesUtil;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.RepairHdrSubmit;
import net.huansi.equipment.equipmentapp.entity.RepairList;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairHdrInSQLite;
import net.huansi.equipment.equipmentapp.util.CallRepairUtil;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.SewLineUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.PLAN_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.SQLITE_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class CallRepairDetailActivity extends BaseActivity {
    @BindView(R.id.tvCallRepairEquipmentName) TextView tvCallRepairEquipmentName;//设备名称
    @BindView(R.id.tvCallRepairCostCenter) TextView tvCallRepairCostCenter;//成本中心
    @BindView(R.id.tvCallRepairEPCode) TextView tvCallRepairEPCode;//设备编码
    @BindView(R.id.tvCallRepairOutOfFactoryCode) TextView tvCallRepairOutOfFactoryCode;//出厂编码
    @BindView(R.id.tvCallRepairEquipmentModel) TextView tvCallRepairEquipmentModel;//设备型号
    @BindView(R.id.tvCallRepairAssetsCode) TextView tvCallRepairAssetsCode;//资产编码
    @BindView(R.id.callRepairArea) Spinner callRepairArea;
    @BindView(R.id.callRepairPeople) TextView callRepairPeople;
    @BindView(R.id.callRepairDescription) EditText callRepairDescription;
    @BindView(R.id.btCallRepairSubmit)  Button btCallRepairSubmit;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private LoadProgressDialog dialog;
    private List<String> groupList;
    private List<String> postIDList;
    private int selectedPosition=0;
    private ArrayAdapter groupAdapter;
    private RepairHdrInSQLite repairHdrInSQLite;//SQLite中维修的主表
    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_repair_detail;
    }

    @Override
    public void init() {
        setToolBarTitle("叫修数据");
        dialog=new LoadProgressDialog(this);
        callRepairPeople.setText(SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString());
        initGroup();
        repairHdrInSQLite= (RepairHdrInSQLite) getIntent().getSerializableExtra(SQLITE_DATA_PARAM);
        RepairList repairEquipment = (RepairList) getIntent().getSerializableExtra(PLAN_PARAM);
        repairHdrInSQLite = new RepairHdrInSQLite();
        repairHdrInSQLite.setAssetsCode(repairEquipment.ASSETSCODE);
        repairHdrInSQLite.setCostCenter(repairEquipment.COSTCENTER);
        repairHdrInSQLite.setEPCode(repairEquipment.EPCCODE);
        repairHdrInSQLite.setEquipmentModel(repairEquipment.MODEL);
        repairHdrInSQLite.setEquipmentName(repairEquipment.EQUIPMENTNAME);
        repairHdrInSQLite.setEquipmentId(repairEquipment.EQUIPMENTDETAILID);
        repairHdrInSQLite.setOutOfCode(repairEquipment.OUTFACTORYCODE);
        tvCallRepairEquipmentName.setText(repairHdrInSQLite.getEquipmentName());
        tvCallRepairCostCenter.setText(repairHdrInSQLite.getCostCenter());
        tvCallRepairEPCode.setText(repairHdrInSQLite.getEPCode());
        tvCallRepairOutOfFactoryCode.setText(repairHdrInSQLite.getOutOfCode());
        tvCallRepairEquipmentModel.setText(repairHdrInSQLite.getEquipmentModel());
        tvCallRepairAssetsCode.setText(repairHdrInSQLite.getAssetsCode());
    }
    @OnClick(R.id.btCallRepairSubmit)
    void CallRepairSubmit(){
        if (callRepairArea.getSelectedItem().toString().isEmpty()){
            OthersUtil.showTipsDialog(this,"请先选择正确的班组！");
        }else {
            btCallRepairSubmit.setClickable(false);
            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CallRepairDetailActivity.this, hsWebInfo)
                            .map(new Func1<HsWebInfo, HsWebInfo>() {
                                @Override
                                public HsWebInfo call(HsWebInfo hsWebInfo) {
                                    return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                            "spAppSbumitCallRepairRecord",
                                            "ActionType=" + "Submit" +
                                                    ",ItemID=" + "" +
                                                    ",AssetsCode=" + tvCallRepairAssetsCode.getText().toString() +
                                                    ",POSTID=" + postIDList.get(selectedPosition) +
                                                    ",CallRepairEmployeeID=" + callRepairPeople.getText().toString() +
                                                    ",IssueDesc=" + callRepairDescription.getText().toString() +
                                                    ",Comments=" + ""+
                                            ",ActionDate="+"",
                                            String.class.getName(),
                                            false,
                                            "helloWorld");
                                }
                            })
                    , CallRepairDetailActivity.this, dialog, new WebListener() {
                        @Override
                        public void success(HsWebInfo hsWebInfo) {
                            Log.e("TAG", "success1=" + hsWebInfo.json);

                        }

                        @Override
                        public void error(HsWebInfo hsWebInfo) {
                            Log.e("TAG", "error1=" + hsWebInfo.json);
                            OthersUtil.showTipsDialog(CallRepairDetailActivity.this, "提交成功维修人员正在赶来...", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(CallRepairDetailActivity.this, CallRepairMainActivity.class));
                                }
                            });

                        }
                    });
        }
    }
    private void initGroup() {
        groupList=new ArrayList<>();
        postIDList=new ArrayList<>();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppEPSewLine","",String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","groupJson="+json);
                SewLineUtil sewLineUtil = JSON.parseObject(json, SewLineUtil.class);
                List<SewLineUtil.DATABean> data = sewLineUtil.getDATA();
                groupList.add("");
                postIDList.add("");
                for (int i=0;i<data.size();i++){
                    groupList.add(i,data.get(i).getDEPTNAME());
                    postIDList.add(i,data.get(i).getPOSTID());
                }
                groupAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.string_item,R.id.text,groupList);
                callRepairArea.setAdapter(groupAdapter);
                callRepairArea.setSelection(groupList.size()-1);
                callRepairArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedPosition=position;
                        Log.e("TAG","selectedPosition="+callRepairArea.getSelectedItem().toString());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error");
            }
        });
    }

}
