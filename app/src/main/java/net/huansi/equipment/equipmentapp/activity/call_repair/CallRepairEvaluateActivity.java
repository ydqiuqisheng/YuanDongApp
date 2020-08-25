package net.huansi.equipment.equipmentapp.activity.call_repair;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.MainActivity;
import net.huansi.equipment.equipmentapp.activity.logging_bill.LoggingBillActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.EvaluateEntities;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.LogDtInfo;
import net.huansi.equipment.equipmentapp.entity.RepairEvaluate;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.TimePickerDialog;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import rx.functions.Func1;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class CallRepairEvaluateActivity extends BaseActivity {
    @BindView(R.id.lv_EvaluateList) ListView lv_EvaluateList;
    private EvaluateAdapter evaluateAdapter;
    private RepairEvaluate repairEvaluate;
    private List<RepairEvaluate> evaluates=new ArrayList<>();
    private LoadProgressDialog dialog;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private final String[] items = new String[]{"非常满意", "比较满意", "需要改进", "差评"};
    private List<String> itemList=new ArrayList<>();
    private int index=0;//对应评价等级
    private TimePickerDialog mTimePickerDialog;
    private TimePickerView pvTime;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_repair_evaluate;
    }

    @Override
    public void init() {
        setToolBarTitle("叫修评价");
        dialog=new LoadProgressDialog(this);
        //mTimePickerDialog=new TimePickerDialog(CallRepairEvaluateActivity.this);
        initInfo();
    }
    private void initInfo() {

        final String caller = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CallRepairEvaluateActivity.this, hsWebInfo)
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                        "spAppGET_EPCallRepairRecord",
                                        "ActionType=" + "Mine" +
                                                ",CallRepairEmployeeID=" + caller,
                                        String.class.getName(),
                                        false,
                                        "helloWorld");
                            }
                        })
                ,CallRepairEvaluateActivity.this, dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "success1="+hsWebInfo.json);
                        String json = hsWebInfo.json;
                        EvaluateEntities evaluateEntities = JSON.parseObject(json, EvaluateEntities.class);
                        List<EvaluateEntities.DATABean> data = evaluateEntities.getDATA();
                        for (int i=0;i<data.size();i++){
                            repairEvaluate=new RepairEvaluate();
                            repairEvaluate.ID=data.get(i).getID();
                            repairEvaluate.EQUIPMENTDETAILID=data.get(i).getEQUIPMENTDETAILID();
                            repairEvaluate.CALLREPAIRITEMID=data.get(i).getCALLREPAIRITEMID();
                            repairEvaluate.CALLREPAIRDATE=data.get(i).getCALLREPAIRDATE();
                            repairEvaluate.ASSETSCODE=data.get(i).getASSETSCODE();
                            repairEvaluate.SEWLINE=data.get(i).getSEWLINE();
                            repairEvaluate.CALLREPAIR=data.get(i).getCALLREPAIR();
                            repairEvaluate.CALLREPAIREMPLOYEEID=data.get(i).getCALLREPAIREMPLOYEEID();
                            repairEvaluate.STATUS=data.get(i).getSTATUS();
                            repairEvaluate.COSTCENTER=data.get(i).getCOSTCENTER();
                            repairEvaluate.OUTFACTORYCODE=data.get(i).getOUTFACTORYCODE();
                            repairEvaluate.EPCCODE=data.get(i).getEPCCODE();
                            repairEvaluate.EQUIPMENTNAME=data.get(i).getEQUIPMENTNAME();
                            repairEvaluate.MODEL=data.get(i).getMODEL();
                            repairEvaluate.REPAIRUSER=data.get(i).getREPAIRUSER();
                            repairEvaluate.REPAIRSTARTDATE=data.get(i).getREPAIRSTARTDATE();
                            repairEvaluate.EQUIPCOMPLETEDATE=data.get(i).getEQUIPCOMPLETEDATE();
                            repairEvaluate.SEWCOMPLETEDATE=data.get(i).getSEWCOMPLETEDATE();
                            evaluates.add(repairEvaluate);
                        }
                        evaluateAdapter=new EvaluateAdapter(evaluates,getApplicationContext());
                        lv_EvaluateList.setAdapter(evaluateAdapter);

                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "error1="+hsWebInfo.json);
                    }
                });
    }


    private class EvaluateAdapter extends HsBaseAdapter<RepairEvaluate>{


        public EvaluateAdapter(List<RepairEvaluate> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null) convertView=mInflater.inflate(R.layout.activity_call_repair_evaluate_item,viewGroup,false);
            CardView cardView = ViewHolder.get(convertView, R.id.cardView);
            TextView tvCallRepairEquipmentName = ViewHolder.get(convertView, R.id.tvCallRepairEquipmentName);//设备名称
            TextView tvCallRepairCostCenter = ViewHolder.get(convertView, R.id.tvCallRepairCostCenter);//成本中心
            TextView tvCallRepairEPCode = ViewHolder.get(convertView, R.id.tvCallRepairEPCode);//设备编号
            TextView tvCallRepairOutOfFactoryCode = ViewHolder.get(convertView, R.id.tvCallRepairOutOfFactoryCode);//出厂编号
            TextView tvCallRepairEquipmentModel = ViewHolder.get(convertView, R.id.tvCallRepairEquipmentModel);//设备型号
            TextView tvCallRepairAssetsCode = ViewHolder.get(convertView, R.id.tvCallRepairAssetsCode);//资产编号
            TextView tvCallRepairRepairUser = ViewHolder.get(convertView, R.id.tvCallRepairRepairUser);//维修人
            TextView tvCallRepairRepairStartDate = ViewHolder.get(convertView, R.id.tvCallRepairRepairStartDate);//维修开始时间
            TextView tvCallRepairTime = ViewHolder.get(convertView, R.id.tvCallRepairTime);//叫修时间
            TextView tvCallRepairCompleteDate = ViewHolder.get(convertView, R.id.tvCallRepairCompleteDate);//保全确认时间
            TextView tvCallRepairConfirmDate = ViewHolder.get(convertView, R.id.tvCallRepairConfirmDate);//机缝确认时间
            TextView tvCallRepairEquipmentState = ViewHolder.get(convertView, R.id.tvCallRepairEquipmentState);//维修状态
            TextView btCancelCallRepair = ViewHolder.get(convertView, R.id.cancelCallRepair);//取消叫修
            RepairEvaluate repairEvaluate = mList.get(position);
            tvCallRepairEquipmentName.setText(repairEvaluate.EQUIPMENTNAME);
            tvCallRepairCostCenter.setText(repairEvaluate.COSTCENTER);
            tvCallRepairEPCode.setText(repairEvaluate.EPCCODE);
            tvCallRepairOutOfFactoryCode.setText(repairEvaluate.OUTFACTORYCODE);
            tvCallRepairEquipmentModel.setText(repairEvaluate.MODEL);
            tvCallRepairAssetsCode.setText(repairEvaluate.ASSETSCODE);
            tvCallRepairRepairUser.setText(repairEvaluate.REPAIRUSER);
            tvCallRepairRepairStartDate.setText(repairEvaluate.REPAIRSTARTDATE);
            tvCallRepairTime.setText(repairEvaluate.CALLREPAIR);
            tvCallRepairCompleteDate.setText(repairEvaluate.EQUIPCOMPLETEDATE);
            tvCallRepairConfirmDate.setText(repairEvaluate.SEWCOMPLETEDATE);
            tvCallRepairEquipmentState.setText(repairEvaluate.STATUS);
             //if (evaluates.get(position).STATUS.equalsIgnoreCase("等待维修")){
                btCancelCallRepair.setVisibility(View.VISIBLE);
                btCancelCallRepair.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OthersUtil.showDoubleChooseDialog(CallRepairEvaluateActivity.this, "确认取消该笔叫修吗?", null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelRepair(position);
                            }
                        });
                    }
                });
//            }else {
//                btCancelCallRepair.setVisibility(View.GONE);
//            }
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    final String id = evaluates.get(position).CALLREPAIRITEMID;
                    String equipDate = evaluates.get(position).EQUIPCOMPLETEDATE;
                    Log.e("TAG","保全时间+"+equipDate);
//                    int year = Integer.parseInt(equipDate.substring(0, 4));
//                    int month = Integer.parseInt(equipDate.substring(5, 7));
//                    int day = Integer.parseInt(equipDate.substring(8, 10));
//                    int hour = Integer.parseInt(equipDate.substring(11, 13));
//                    int minute = Integer.parseInt(equipDate.substring(14, 16));
//                    int second = Integer.parseInt(equipDate.substring(17, 19));
//                    Calendar startDate = Calendar.getInstance();
//                    startDate.set(year,month-1,day,hour,minute,second);
                    if (evaluates.get(position).STATUS.equalsIgnoreCase("维修完成")){
                        //mTimePickerDialog.showDateAndTimePickerDialog();
                       pvTime=new TimePickerBuilder(CallRepairEvaluateActivity.this, new OnTimeSelectListener() {
                           @Override
                           public void onTimeSelect(Date date, View v) {
                               final String formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                               Log.e("TAG","wcwc"+formatDate);
                               android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(CallRepairEvaluateActivity.this);
                               View areaDialogView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.comment_input_dialog,null);
                               final EditText editText= (EditText) areaDialogView.findViewById(R.id.etInventoryAreaDialog);
                               editText.setHint("请输入评价(与保全时间不一致原因)");
                               editText.setTextColor(Color.BLACK);
                               builder.setView(areaDialogView)
                                       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int which) {
                                               String comment=editText.getText().toString().trim();
                                               if(comment.isEmpty()){
                                                   OthersUtil.dialogNotDismissClickOut(dialogInterface);
                                                   OthersUtil.ToastMsg(getApplicationContext(),"内容不允许空白!");
                                                   return;
                                               }
                                               submitEvaluate(id,formatDate,comment);
                                               OthersUtil.dialogDismiss(dialogInterface);
                                               dialogInterface.dismiss();
                                           }
                                       })
                                       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int which) {

                                               OthersUtil.dialogDismiss(dialogInterface);
                                               dialogInterface.dismiss();

                                           }
                                       })
                                       .setCancelable(true)
                                       .show();

                           }
                       }) .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                           @Override
                           public void onTimeSelectChanged(Date date) {
                               Log.e("TAG", "onTimeSelectChanged"+date);
                           }
                       })
                               .setType(new boolean[]{true, true, true, true, true,true})
                               .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                               .addOnCancelClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Log.e("TAG", "onCancelClickListener");
                                   }
                               })
                               .setTitleText("选择机缝确认时间(默认为保全确认时间)")
                               .setTitleColor(Color.RED)
                               //.setDate(startDate)
                               .isCyclic(true)//是否循环滚动
                               .build();
                        Dialog mDialog = pvTime.getDialog();
                        if (mDialog != null) {

                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    Gravity.BOTTOM);

                            params.leftMargin = 0;
                            params.rightMargin = 0;
                            pvTime.getDialogContainerLayout().setLayoutParams(params);

                            Window dialogWindow = mDialog.getWindow();
                            if (dialogWindow != null) {
                                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                                dialogWindow.setDimAmount(0.1f);
                            }
                        }
                        pvTime.show();
//                        AlertDialog alertDialog = new AlertDialog.Builder(CallRepairEvaluateActivity.this)
//                                .setTitle("为了方便改进工作，请务必给出您宝贵的评价")
//                                .setIcon(R.drawable.app_icon)
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        //submitEvaluate(id);
//                                    }
//                                })
//
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                    }
//                                })
//                                .setView()
//                                .create();
//                        alertDialog.show();
                    }else {
                        OthersUtil.showTipsDialog(CallRepairEvaluateActivity.this,"当前状态为"+evaluates.get(position).STATUS+"无法评价");
                    }
                    return true;
                }
            });

            return convertView;
        }
    }

    private void cancelRepair(int Position) {
        final String itemID = evaluates.get(Position).CALLREPAIRITEMID;
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CallRepairEvaluateActivity.this, hsWebInfo)
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                        "spAppSbumitCallRepairRecord",
                                        "ActionType=" + "Cancel" +
                                                ",ItemID=" + itemID +
                                                ",AssetsCode=" + "" +
                                                ",POSTID=" + "" +
                                                ",CallRepairEmployeeID=" +"" +
                                                ",IssueDesc=" +""+
                                                ",Comments="+""+
                                        ",ActionDate="+"",
                                        String.class.getName(),
                                        false,
                                        "helloWorld");
                            }
                        })
                ,CallRepairEvaluateActivity.this, dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "success="+hsWebInfo.json);
                        OthersUtil.showTipsDialog(CallRepairEvaluateActivity.this,"取消成功");
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "error="+hsWebInfo.json);
                        evaluates.clear();
                        evaluateAdapter.notifyDataSetChanged();
                        initInfo();
                        OthersUtil.showTipsDialog(CallRepairEvaluateActivity.this,"取消成功");
                    }
                });

    }

    private void submitEvaluate(final String itemID, final String date, final String comment) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CallRepairEvaluateActivity.this, hsWebInfo)
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                        "spAppSbumitCallRepairRecord",
                                        "ActionType=" + "Complete" +
                                                ",ItemID=" + itemID +
                                                ",AssetsCode=" + "" +
                                                ",POSTID=" + "" +
                                                ",CallRepairEmployeeID=" +"" +
                                                ",IssueDesc=" +""+
                                                ",Comments="+comment+
                                        ",ActionDate="+date,
                                        String.class.getName(),
                                        false,
                                        "helloWorld");
                            }
                        })
                ,CallRepairEvaluateActivity.this, dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "success="+hsWebInfo.json);
                        OthersUtil.ToastMsg(CallRepairEvaluateActivity.this,"时间意见提交完毕");
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "error="+hsWebInfo.json);
                        OthersUtil.ToastMsg(CallRepairEvaluateActivity.this,"时间意见提交完毕");
                    }
                });
    }
}
