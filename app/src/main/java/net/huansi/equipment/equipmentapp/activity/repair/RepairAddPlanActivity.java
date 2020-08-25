package net.huansi.equipment.equipmentapp.activity.repair;//package net.huansi.equipment.equipmentapp.activity.repair;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.os.Parcel;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.bigkoo.pickerview.TimePickerView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.activity.BaseActivity;
//import net.huansi.equipment.equipmentapp.adapter.RepairAddPlanAdapter;
//import net.huansi.equipment.equipmentapp.entity.RepairAddPlan;
//import net.huansi.equipment.equipmentapp.entity.RepairPlan;
//import net.huansi.equipment.equipmentapp.entity.RepairPlanDetail;
//import net.huansi.equipment.equipmentapp.util.OthersUtil;
//import net.huansi.equipment.equipmentapp.util.TimeUtils;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import butterknife.OnItemClick;
//import butterknife.OnItemLongClick;
//
//import static com.bigkoo.pickerview.TimePickerView.Type.ALL;
//import static net.huansi.equipment.equipmentapp.constant.Constant.RepairAddPlanActivityConstants.PLAN_EP_INFO_PARAM;
//
///**
// * Created by 单中年 on 2017/3/2.
// */
//
//public class RepairAddPlanActivity extends BaseActivity {
//    @BindView(R.id.tvRepairAddPlanFactory)
//    TextView tvRepairAddPlanFactory;//工厂
//    @BindView(R.id.tvRepairAddPlanEquipmentName)
//    TextView tvRepairAddPlanEquipmentName;//设备名称
//    @BindView(R.id.tvRepairAddPlanEPCode)
//    TextView tvRepairAddPlanEPCode;//设备Code
//    @BindView(R.id.tvRepairAddPlanCostCenter)
//    TextView tvRepairAddPlanCostCenter;//成本中心
//    @BindView(R.id.tvRepairAddPlanAssetsCode)
//    TextView tvRepairAddPlanAssetsCode;//资产编号
//    @BindView(R.id.tvRepairAddPlanOutFactoryCode)
//    TextView tvRepairAddPlanOutFactoryCode;//出厂编号
//    @BindView(R.id.tvRepairAddPlanStartTime)
//    TextView tvRepairAddPlanStartTime;//开始时间
//    @BindView(R.id.tvRepairAddPlanEndTime)
//    TextView tvRepairAddPlanEndTime;//结束时间
////    @BindView(R.id.btnRepairAddPlanAdd)
////    Button btnRepairAddPlanAdd;//添加的按钮
//    @BindView(R.id.lvRepairAddPlan)
//    ListView lvRepairAddPlan;
//
//    private Date startDate;//开始日期
//    private Date endDate;//结束日期
//
//    private List<RepairAddPlan> mList;
//    private RepairAddPlanAdapter mAdapter;
//
//
//    private TimePickerView pickerView;//时间选择器
//    private List<RepairPlanDetail> mLastPlanDetailList;//维修计划（主要是拿的头部的数据）
//    private int timeType=0;//0=开始，1结束
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.repair_add_plan_activity;
//    }
//
//    @Override
//    public void init() {
//        mList=new ArrayList<>();
//        pickerView=new TimePickerView(this, ALL);
//        mLastPlanDetailList= (List<RepairPlanDetail>) getIntent().getSerializableExtra(PLAN_EP_INFO_PARAM);
//        startDate=new Date();
//        endDate=new Date();
//        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date) {
//                switch (timeType){
//                    case 0:
//                        startDate=date;
//                        tvRepairAddPlanStartTime.setText(TimeUtils.getTime(startDate,"-"));
//                        break;
//                    case 1:
//                        endDate=date;
//                        tvRepairAddPlanEndTime.setText(TimeUtils.getTime(endDate,"-"));
//                        break;
//                }
//            }
//        });
//        if(mLastPlanDetailList!=null&&mLastPlanDetailList.size()>0) {
//            RepairPlanDetail planDetail=mLastPlanDetailList.get(0);
//            tvRepairAddPlanFactory.setText(planDetail.FACTORY);
//            tvRepairAddPlanEquipmentName.setText(planDetail.EQUIPMENTNAME);
//            tvRepairAddPlanEPCode.setText(planDetail.EPCCODE);
//            tvRepairAddPlanCostCenter.setText(planDetail.COSTCENTER);
//            tvRepairAddPlanAssetsCode.setText(planDetail.ASSETSCODE);
//            tvRepairAddPlanOutFactoryCode.setText(planDetail.OUTFACTORYCODE);
//            for(int i=0;i<mLastPlanDetailList.size();i++){
//                RepairPlanDetail detail=mLastPlanDetailList.get(0);
//                RepairAddPlan plan=RepairAddPlan.CREATOR.createFromParcel(Parcel.obtain());
//                try {
//                    plan.endDate=TimeUtils.getDate(detail.TENDREPAIRTIME,"-");
//                    plan.startDate=TimeUtils.getDate(detail.TSTARTREPAIRTIME,"-");
//                    plan.isAdd=false;
//                    mList.add(plan);
//                } catch (Exception e) {
//                    continue;
//                }
//            }
//        }
//        mAdapter=new RepairAddPlanAdapter(mList,getApplicationContext());
//        lvRepairAddPlan.setAdapter(mAdapter);
//        tvRepairAddPlanStartTime.setText(TimeUtils.getTime(new Date(),"-"));
//        tvRepairAddPlanEndTime.setText(TimeUtils.getTime(new Date(),"-"));
//    }
//
//    /**
//     * 显示开始时间
//     */
//    @OnClick(R.id.tvRepairAddPlanStartTime)
//    void showStartTime(){
//        timeType=0;
//        if(pickerView.isShowing()) return;
//        if(startDate==null) startDate=new Date();
//        pickerView.setTime(startDate);
//        pickerView.show();
//    }
//
//    /**
//     * 显示结束时间
//     */
//    @OnClick(R.id.tvRepairAddPlanEndTime)
//    void showEndTime(){
//        timeType=1;
//        if(pickerView.isShowing()) return;
//        if(endDate==null) endDate=new Date();
//        pickerView.setTime(endDate);
//        pickerView.show();
//    }
//
//    /**
//     * 添加一个计划
//     */
//    @OnClick(R.id.btnRepairAddPlanAdd)
//    void addPlan(){
//        RepairAddPlan plan=RepairAddPlan.CREATOR.createFromParcel(Parcel.obtain());
//        plan.startDate=startDate;
//        plan.endDate=endDate;
//        mList.add(0,plan);
//        mAdapter.notifyDataSetChanged();
//    }
//
//    @OnItemLongClick(R.id.lvRepairAddPlan)
//    boolean delPlan(final int position){
//        RepairAddPlan plan=mList.get(position);
//        if(plan.isAdd){
//            OthersUtil.showChooseDialog(this, "是否删除这个计划？", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    mList.remove(position);
//                    mAdapter.notifyDataSetChanged();
//                }
//            });
//        }
//        return true;
//    }
//}
