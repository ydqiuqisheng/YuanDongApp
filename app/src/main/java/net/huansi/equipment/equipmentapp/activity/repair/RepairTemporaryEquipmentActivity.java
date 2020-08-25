package net.huansi.equipment.equipmentapp.activity.repair;//package net.huansi.equipment.equipmentapp.activity.repair;
//
//import android.content.Intent;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.activity.BaseActivity;
//import net.huansi.equipment.equipmentapp.adapter.RepairListAdapter;
//import net.huansi.equipment.equipmentapp.adapter.RepairTemporaryEquipmentAdapter;
//import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
//import net.huansi.equipment.equipmentapp.entity.RepairList;
//import net.huansi.equipment.equipmentapp.entity.WsEntity;
//import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
//import net.huansi.equipment.equipmentapp.util.OthersUtil;
//import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
//import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import butterknife.OnItemClick;
//
//import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.PLAN_PARAM;
//
///**
// * Created by shanz on 2017/3/5.
// */
//
//public class RepairTemporaryEquipmentActivity extends BaseActivity{
//    @BindView(R.id.etTemporaryEquipmentSearch)EditText etTemporaryEquipmentSearch;
//    @BindView(R.id.btnTemporaryEquipmentSearch)Button btnTemporaryEquipmentSearch;
//    @BindView(R.id.lvTemporaryEquipment)ListView lvTemporaryEquipment;
//
//    private List<RepairList> mList;
//    private RepairListAdapter mAdapter;
//    private LoadProgressDialog dialog;
//
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.repair_temporary_equipment_activity;
//    }
//
//    @Override
//    public void init() {
//        setToolBarTitle("维修");
//        OthersUtil.hideInputFirst(this);
//        dialog=new LoadProgressDialog(this);
//        mList=new ArrayList<>();
//        mAdapter=new RepairListAdapter(mList,getApplicationContext());
//        lvTemporaryEquipment.setAdapter(mAdapter);
//    }
//
//    @Override
//    public void back() {
//
//    }
//
//    @OnClick(R.id.btnTemporaryEquipmentSearch)
//    void search(){
//        mList.clear();
//        OthersUtil.showLoadDialog(dialog);
//        RxjavaWebUtils.requestByGetJsonData(this,
//                "spAppEPQueryEquipmentUsable",
//                "sSearch=" + etTemporaryEquipmentSearch.getText().toString().trim(),
//                getApplicationContext(),
//                dialog,
//                RepairList.class.getName(),
//                true,
//                "未查到设备信息",
//                new SimpleHsWeb() {
//                    @Override
//                    public void error(HsWebInfo hsWebInfo) {
//                        super.error(hsWebInfo);
//                        mAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        OthersUtil.dismissLoadDialog(dialog);
//                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
//                        for(int i=0;i<entities.size();i++){
//                            mList.add((RepairList) entities.get(i));
//                        }
//                        mAdapter.notifyDataSetChanged();
//                    }
//                });
//    }
//
//    /**
//     * 进行维修界面的转换
//     */
//    @OnItemClick(R.id.lvTemporaryEquipment)
//    void toRepair(int position){
//        RepairList plan=mList.get(position);
//        Intent intent=new Intent(this,RepairDetailActivity.class);
//        intent.putExtra(PLAN_PARAM,plan);
//        startActivity(intent);
//    }
//}
