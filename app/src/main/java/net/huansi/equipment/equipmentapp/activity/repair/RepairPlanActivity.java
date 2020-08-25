package net.huansi.equipment.equipmentapp.activity.repair;//package net.huansi.equipment.equipmentapp.activity.repair;
//
//import android.content.Intent;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ExpandableListView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.activity.BaseActivity;
//import net.huansi.equipment.equipmentapp.adapter.RepairPlanAddChooseEPAdapter;
//import net.huansi.equipment.equipmentapp.adapter.RepairPlanListAdapter;
//import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
//import net.huansi.equipment.equipmentapp.entity.RepairPlan;
//import net.huansi.equipment.equipmentapp.entity.RepairPlanDetail;
//import net.huansi.equipment.equipmentapp.entity.WsEntity;
//import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
//import net.huansi.equipment.equipmentapp.util.OthersUtil;
//import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
//import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import butterknife.OnItemClick;
//
//import static net.huansi.equipment.equipmentapp.constant.Constant.RepairAddPlanActivityConstants.PLAN_EP_INFO_PARAM;
//
///**
// * Created by 单中年 on 2017/2/28.
// */
//
//public class RepairPlanActivity extends BaseActivity{
//    @BindView(R.id.elvRepairPlan) ExpandableListView elvRepairPlan;
//    @BindView(R.id.etRepairPlanSearch) EditText etRepairPlanSearch;
//    @BindView(R.id.btnRepairPlanAddPlanOk) Button btnRepairPlanAddPlanOk;//确认添加
//    @BindView(R.id.lvRepairPlanWatch) ListView lvRepairPlanWatch;//主要用来选择设备的维修计划安排时选择的设备
//
//    private TextView tvAdd;
//
//    private LoadProgressDialog dialog;
//    private List<RepairPlan> mParentList;
//    private List<List<RepairPlanDetail>> mChildList;
//    private List<RepairPlanDetail> mSourceList;//源数据
//    private RepairPlanListAdapter mAdapter;
//    private RepairPlanAddChooseEPAdapter mChooseAdapter;//选择设备进行添加计划
//    private List<RepairPlan> mChooseList;
//
//    private boolean isAddPlan=false;//是否添加计划
//
//    private int lastChoosePosition=-1;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.repair_plan_list_activity;
//    }
//
//
//    @Override
//    public void init() {
////        tvAdd=getSubTitle();//添加按钮
////        tvAdd.setText("添加");
//        tvAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isAddPlan=!isAddPlan;
//                lastChoosePosition=-1;
//                if(isAddPlan){
//                    tvAdd.setText("取消");
//                    btnRepairPlanAddPlanOk.setVisibility(View.VISIBLE);
//                    lvRepairPlanWatch.setVisibility(View.VISIBLE);
//                    elvRepairPlan.setVisibility(View.GONE);
//                }else {
//                    tvAdd.setText("添加");
//                    btnRepairPlanAddPlanOk.setVisibility(View.GONE);
//                    lvRepairPlanWatch.setVisibility(View.GONE);
//                    elvRepairPlan.setVisibility(View.VISIBLE);
//                }
//                clearChooseList();
//                mChooseAdapter.notifyDataSetChanged();
//                if(mChooseAdapter.getCount()>0) lvRepairPlanWatch.setSelection(0);
//            }
//        });
//        OthersUtil.hideInputFirst(this);
//        mParentList=new ArrayList<>();
//        mChildList=new ArrayList<>();
//        mSourceList=new ArrayList<>();
//        mChooseList=new ArrayList<>();
//        dialog=new LoadProgressDialog(this);
//        mAdapter=new RepairPlanListAdapter(mParentList,mChildList,getApplicationContext());
//        mChooseAdapter=new RepairPlanAddChooseEPAdapter(mChooseList,getApplicationContext());
//        lvRepairPlanWatch.setAdapter(mChooseAdapter);
//    }
//
//
//
//    /**
//     * 清空设备选择的多选框
//     */
//    private void clearChooseList(){
//        for(int i=0;i<mChooseList.size();i++){
//            mChooseList.get(i).isChoose=false;
//        }
//        mChooseAdapter.notifyDataSetChanged();
//    }
//
//    @OnItemClick(R.id.lvRepairPlanWatch)
//    void chooseEP(int position){
//        if(lastChoosePosition==position){
//            mChooseList.get(position).isChoose=false;
//            lastChoosePosition=-1;
//        }else {
//            mChooseList.get(position).isChoose=true;
//            if(lastChoosePosition!=-1&&lastChoosePosition<mChooseList.size()-1){
//                mChooseList.get(lastChoosePosition).isChoose=false;
//            }
//            lastChoosePosition=position;
//        }
//        mChooseAdapter.notifyDataSetChanged();
//    }
//
//    /**
//     * 根据工厂搜索计划
//     */
//    @OnClick(R.id.btnRepairPlanSearch)
//    void searchPlanByFactory(){
//        String search=etRepairPlanSearch.getText().toString().trim();
//        OthersUtil.showLoadDialog(dialog);
//        isAddPlan=false;
//        btnRepairPlanAddPlanOk.setVisibility(View.GONE);
//        lvRepairPlanWatch.setVisibility(View.GONE);
//        elvRepairPlan.setVisibility(View.VISIBLE);
//        tvAdd.setText("添加");
//        mParentList.clear();
//        mSourceList.clear();
//        mChildList.clear();
//        RxjavaWebUtils.requestByGetJsonData(this,
//                "spAppEPQueryRepairPlanList",
//                "sSearch=" + search,
//                getApplicationContext(),
//                dialog,
//                RepairPlanDetail.class.getName(),
//                true,
//                "没有您要查询的维修/维护计划",
//                new SimpleHsWeb() {
//                    @Override
//                    public void error(HsWebInfo hsWebInfo) {
//                        super.error(hsWebInfo);
//                        elvRepairPlan.setAdapter(mAdapter);
//                    }
//
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        OthersUtil.dismissLoadDialog(dialog);
//                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
//                        for(int i=0;i<entities.size();i++){
//                            mSourceList.add((RepairPlanDetail) entities.get(i));
//                        }
//                        sortData();
//                        elvRepairPlan.setAdapter(mAdapter);
//                    }
//                });
//    }
//
//
//    @OnClick(R.id.btnRepairPlanAddPlanOk)
//    void addPlan(){
//        int choosePosition=-1;
//        for(int i=0;i<mParentList.size();i++){
//            RepairPlan repairPlan=mParentList.get(i);
//            if(repairPlan.isChoose){
//                choosePosition=i;
//                break;
//            }
//        }
//        if(choosePosition==-1){
//            OthersUtil.showTipsDialog(this,"请选择要添加维修计划的设备！！");
//            return;
//        }
//        Intent intent=new Intent(this,RepairAddPlanActivity.class);
//        intent.putExtra(PLAN_EP_INFO_PARAM, (Serializable) mChildList.get(choosePosition));
//        startActivity(intent);
//    }
//
//    /**
//     * 对数据进行排布
//     */
//    private void sortData(){
//        mChildList.clear();
//        mParentList.clear();
//        mChooseList.clear();
//        Map<String,List<RepairPlanDetail>> map=new HashMap<>();
//        //根据设备进行分类
//        for(int i=0;i<mSourceList.size();i++){
//            RepairPlanDetail detail=mSourceList.get(i);
//            List<RepairPlanDetail> list;
//            if(map.get(detail.EQUIPMENTDETAILID)==null){
//                list=new ArrayList<>();
//            }else {
//                list=map.get(detail.EQUIPMENTDETAILID);
//            }
//            list.add(detail);
//            map.put(detail.EQUIPMENTDETAILID,list);
//        }
//        Iterator<Entry<String,List<RepairPlanDetail>>> it=map.entrySet().iterator();
//        while (it.hasNext()){
//            Entry<String,List<RepairPlanDetail>> entry=it.next();
//            List<RepairPlanDetail> list=entry.getValue();
//            if(list==null||list.isEmpty()) continue;
//            RepairPlan repairPlan= new RepairPlan();
//            RepairPlanDetail detail=list.get(0);
//            repairPlan.ASSETSCODE=detail.ASSETSCODE;
//            repairPlan.COSTCENTER=detail.COSTCENTER;
//            repairPlan.TSTARTREPAIRTIME=detail.TSTARTREPAIRTIME;
//            repairPlan.TENDREPAIRTIME=detail.TENDREPAIRTIME;
//            repairPlan.EPCCODE=detail.EPCCODE;
//            repairPlan.EQUIPMENTNAME=detail.EQUIPMENTNAME;
//            repairPlan.FACTORY=detail.FACTORY;
//            repairPlan.OUTFACTORYCODE=detail.OUTFACTORYCODE;
//            repairPlan.EQUIPMENTDETAILID=detail.EQUIPMENTDETAILID;
//            for(int i=0;i<list.size();i++){
//                RepairPlanDetail repairPlanDetail=list.get(i);
//                if(repairPlanDetail.TSTARTREPAIRTIME.trim().isEmpty()){
//                    list.remove(i);
//                    i--;
//                }
//            }
//            mChildList.add(list);
//            mParentList.add(repairPlan);
//        }
//        mChooseList.addAll(mParentList);
//    }
//}
