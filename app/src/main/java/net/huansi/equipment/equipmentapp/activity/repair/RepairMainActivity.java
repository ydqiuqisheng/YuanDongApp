package net.huansi.equipment.equipmentapp.activity.repair;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.DeviceSettingActivity;
import net.huansi.equipment.equipmentapp.activity.call_repair.CallRepairInventoryActivity;
import net.huansi.equipment.equipmentapp.entity.BaseData;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.RepairEvaluate;
import net.huansi.equipment.equipmentapp.entity.RepairList;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.gen.DaoSession;
import net.huansi.equipment.equipmentapp.gen.RepairBaseDataInSQLiteDao;
import net.huansi.equipment.equipmentapp.gen.RepairEquipmentInSQLiteDao;
import net.huansi.equipment.equipmentapp.gen.RepairHdrInSQLiteDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairBaseDataInSQLite;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairEquipmentInSQLite;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairHdrInSQLite;
import net.huansi.equipment.equipmentapp.util.CallRepairUtil;
import net.huansi.equipment.equipmentapp.util.NetUtil;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.SQLITE_DATA_PARAM;

/**
 * Created by 单中年 on 2017/2/27.
 */

public class RepairMainActivity extends BaseActivity {
    private LoadProgressDialog dialog;

    private RepairMainActivity.LineAdapter lineAdapter;
    private List<String> numberList;
    private RepairEvaluate machineDetail;//维修设备的详细信息
    private List<RepairEvaluate> machineDetailList;
    private PopupWindow pw;
    @BindView(R.id.btnRepairMainUpdateRepairBaseData)Button btnRepairMainUpdateRepairBaseData;
    private boolean isNeedUpdateBaseData=true;//true 需要下载维修基本数据
    private int REQUEST_CODE=2;
    private String RESULT_CODE;
    private int REQUEST_POSITION;
    @Override
    protected int getLayoutId() {
        return R.layout.repair_main_activity;
    }

    @Override
    public void init() {
        dialog=new LoadProgressDialog(this);
        setToolBarTitle("设备维修");
        queryRepairBaseDataInSQLite();
        ZXingLibrary.initDisplayOpinion(this);
    }


    /**
     * 查询本地的基础数据
     */
    private void queryRepairBaseDataInSQLite(){
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        HsWebInfo hsWebInfo=new HsWebInfo();
                        DaoSession daoSession=OthersUtil.getGreenDaoSession(getApplicationContext());
                        RepairEquipmentInSQLiteDao repairEquipmentInSQLiteDao=daoSession.getRepairEquipmentInSQLiteDao();
                        RepairBaseDataInSQLiteDao repairBaseDataInSQLiteDao=daoSession.getRepairBaseDataInSQLiteDao();
                        //资产设备
                        List<RepairEquipmentInSQLite> repairEquipmentInSQLiteList=repairEquipmentInSQLiteDao.loadAll();
                        if(repairEquipmentInSQLiteList==null||repairEquipmentInSQLiteList.isEmpty()){
                            hsWebInfo.success=false;
                            return hsWebInfo;
                        }
                        //维护项目
                        List<RepairBaseDataInSQLite> projectList=repairBaseDataInSQLiteDao.queryBuilder()
                                .where(RepairBaseDataInSQLiteDao.Properties.Type.eq(0))
                                .list();
                        if(projectList==null||projectList.isEmpty()) {
                            hsWebInfo.success=false;
                            return hsWebInfo;
                        }
                        //维修结果
                        List<RepairBaseDataInSQLite> resultList=repairBaseDataInSQLiteDao.queryBuilder()
                                .where(RepairBaseDataInSQLiteDao.Properties.Type.eq(1))
                                .list();
                        if(resultList==null||resultList.isEmpty()) {
                            hsWebInfo.success=false;
                            return hsWebInfo;
                        }
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {//本地保存成功
                Log.e("TAG","whySuccess="+hsWebInfo);
                isNeedUpdateBaseData=false;
                showRepairBaseData();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","whyError="+hsWebInfo);
                isNeedUpdateBaseData=true;
                showRepairBaseData();
            }
        });
    }

    /**
     * 显示维修的基本数据是否全部下载完
     */
    private void showRepairBaseData(){
        if(isNeedUpdateBaseData){
            btnRepairMainUpdateRepairBaseData.setText("更新（下载）维修基本数据（此数据仅在无网络使用）<未下载>");
//            OthersUtil.setTextWithColor(Color.RED,btnRepairMainUpdateRepairBaseData,"<未下载>","更新维修基本数据（数据仅在无网络使用）",true);
        }else {
            btnRepairMainUpdateRepairBaseData.setText("更新（下载）维修基本数据（此数据仅在无网络使用）<已下载>");
//            OthersUtil.setTextWithColor(Color.GREEN,btnRepairMainUpdateRepairBaseData,"<已下载>","更新维修基本数据（数据仅在无网络使用）",true);
        }
    }


    /**
     * 维修
     */
    @OnClick(R.id.btnRepairMainRepair)
    void showRepairList(){
        //无网络 并且基础数据没有
        if(isNeedUpdateBaseData&&!NetUtil.isNetworkAvailable(getApplicationContext())){
            OthersUtil.showTipsDialog(this,"您的平板现在没有网络，您有两种选择：\n" +
                                            "1、保持网络通畅\n" +
                                            "2、在本界面点击按钮“更新维修基本数据”，进行更新（下载）基本的维修数据");
            return;
        }
        Intent intent=new Intent();
        intent.setClass(this,RepairListActivity.class);
        startActivity(intent);
    }

    /**
     * 继续维修
     */
    @OnClick(R.id.btnRepairMainContinue)
    void continueRepair(){
        OthersUtil.showLoadDialog(dialog);
        Observable.just("")
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<RepairHdrInSQLite>>() {
                    @Override
                    public List<RepairHdrInSQLite> call(String s) {
                        RepairHdrInSQLiteDao hdrInSQLiteDao=OthersUtil.getGreenDaoSession(getApplicationContext())
                                .getRepairHdrInSQLiteDao();
                        Query<RepairHdrInSQLite> query=hdrInSQLiteDao
                                .queryBuilder()
                                .where(RepairHdrInSQLiteDao.Properties.SubmitStatus.eq(0))//查询条件
                                .orderDesc(RepairHdrInSQLiteDao.Properties.Id)
                                .build();
                        if(query==null) return null;
                        return query.list();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RepairHdrInSQLite>>() {
                    @Override
                    public void call(final List<RepairHdrInSQLite> repairHdrInSQLites) {
                        OthersUtil.dismissLoadDialog(dialog);
                        if(repairHdrInSQLites==null||repairHdrInSQLites.isEmpty()){
                            OthersUtil.showTipsDialog(RepairMainActivity.this,"暂不存在继续维修的设备");
                            return;
                        }
                        String [] names=new String[repairHdrInSQLites.size()];
                        for(int i=0;i<repairHdrInSQLites.size();i++){
                            RepairHdrInSQLite hdr= repairHdrInSQLites.get(i);
                            names[i]="资产编码："+hdr.getAssetsCode()+"\n"+
                                    "设备名："+hdr.getEquipmentName()+"\n"+
                                    "创建时间："+hdr.getCreateTime()+"\n";

                        }
                        AlertDialog.Builder builder=new AlertDialog.Builder(RepairMainActivity.this);
                        builder.setItems(names, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RepairHdrInSQLite repairHdrInSQLite=repairHdrInSQLites.get(which);
                                Intent intent=new Intent(RepairMainActivity.this,RepairDetailActivity.class);
                                intent.putExtra(SQLITE_DATA_PARAM,repairHdrInSQLite);
                                startActivity(intent);
                                //跳转过去后继续之前的计时
                            }
                        })
                                .show();
                    }
                });
    }

    /**
     * 查看叫修
     */
    @OnClick(R.id.btnRepairCallRepair)
    void CallRepair(View view){
        if(isNeedUpdateBaseData&&!NetUtil.isNetworkAvailable(getApplicationContext())){
            OthersUtil.showTipsDialog(this,"您的平板现在没有网络,请连接网络或者稍后重试");
            return;
        }
        //初始化listview控件和里边的数据
        ListView mListView = initListView();
        pw = new PopupWindow(mListView,this.getWindowManager().getDefaultDisplay().getWidth()-40, 1000);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.showAtLocation(view,1,5,-5);
    }

    private ListView initListView() {
        final ListView mListView = new ListView(this);
        mListView.setBackgroundResource(R.drawable.shadow);
        numberList=new ArrayList<>();
        machineDetailList=new ArrayList<>();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppGET_EPCallRepairRecord",
                                "ActionType=" + "CallList" +
                                        ",CallRepairEmployeeID=" + "",String.class.getName(),false,"数据获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                Log.e("TAG","叫修清单="+hsWebInfo.json);
                String json = hsWebInfo.json;
                CallRepairUtil callRepairUtil = JSON.parseObject(json, CallRepairUtil.class);
                List<CallRepairUtil.DATABean> data = callRepairUtil.getDATA();
                Log.e("TAG","data="+data);
                if (data.isEmpty()){
                    OthersUtil.showTipsDialog(RepairMainActivity.this,"暂不存在叫修设备");
                }else {
                    for (int i=0;i<data.size();i++){
                        machineDetail=new RepairEvaluate();
                        machineDetail.EQUIPMENTDETAILID=data.get(i).getEQUIPMENTDETAILID();
                        machineDetail.CALLREPAIRITEMID=data.get(i).getCALLREPAIRITEMID();
                        machineDetail.EQUIPMENTNAME=data.get(i).getEQUIPMENTNAME();
                        machineDetail.COSTCENTER=data.get(i).getCOSTCENTER();
                        machineDetail.EPCCODE=data.get(i).getEPCCODE();
                        machineDetail.OUTFACTORYCODE=data.get(i).getOUTFACTORYCODE();
                        machineDetail.MODEL=data.get(i).getMODEL();
                        machineDetail.ASSETSCODE=data.get(i).getASSETSCODE();
                        machineDetail.CALLREPAIRDATE=data.get(i).getCALLREPAIRDATE();
                        machineDetail.SEWLINE=data.get(i).getSEWLINE();
                        machineDetail.POSTID=data.get(i).getPOSTID();
                        numberList.add(data.get(i).getCALLREPAIRDATE()+"/"+data.get(i).getEQUIPMENTNAME()+"/"+data.get(i).getASSETSCODE()+"/"+data.get(i).getCALLREPAIREMPLOYEEID());
                        machineDetailList.add(machineDetail);
                    }
                    lineAdapter=new RepairMainActivity.LineAdapter();
                    mListView.setAdapter(lineAdapter);
                }
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error");
            }
        });
        return mListView;
    }
    private class LineAdapter extends BaseAdapter  {

        @Override
        public int getCount() {
            return numberList.size();
        }

        @Override
        public Object getItem(int position) {
            return numberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            NumberHolder mHolder=null;
            if (convertView==null){
                convertView=View.inflate(RepairMainActivity.this,R.layout.listview_item,null);
                mHolder=new NumberHolder();
                mHolder.tvLine= (TextView) convertView.findViewById(R.id.tv_listview_item_line);
                convertView.setTag(mHolder);
            }else {
                mHolder= (NumberHolder) convertView.getTag();
            }
            mHolder.tvLine.setText(numberList.get(position));
            mHolder.tvLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  OthersUtil.showDoubleChooseDialog(RepairMainActivity.this, "是否扫描机台二维码后维修?", new DialogInterface.OnClickListener() {
                  //      @Override
                     //   public void onClick(DialogInterface dialogInterface, int i) {//否
                     //       Intent intent1 = new Intent(RepairMainActivity.this, RepairDetailActivity.class);
                    //        intent1.putExtra("CallRepairRecord", machineDetailList.get(position));
                   //         startActivity(intent1);
                  //          pw.dismiss();
                 //       }
                 //   }, new DialogInterface.OnClickListener() {
                   //     @Override
                   //     public void onClick(DialogInterface dialogInterface, int i) {//是
                            Intent intent = new Intent(RepairMainActivity.this, CaptureActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                            REQUEST_POSITION=position;
                    //    }
                  //  });

                }
            });
            return convertView;
        }


    }
    class NumberHolder {
        TextView tvLine;
        //ImageButton ibDelete;
    }

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
                    String result = bundle.getString(CodeUtils.RESULT_STRING).trim();
                    Log.e("TAG","扫描结果="+result);

                    if (machineDetailList.get(REQUEST_POSITION).ASSETSCODE.equalsIgnoreCase(result)) {//机修要到现场扫完正确机台号后方可开始维修
                        Intent intent1 = new Intent(RepairMainActivity.this, RepairDetailActivity.class);
                        intent1.putExtra("CallRepairRecord", machineDetailList.get(REQUEST_POSITION));
                        startActivity(intent1);
                        pw.dismiss();
                    }else {
                        OthersUtil.ToastMsg(RepairMainActivity.this,"扫描二维码与点击信息不一致,请扫描正确的需要维修的机台二维码");
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(RepairMainActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 更新维修基本数据
     */
    @SuppressWarnings("unchecked")
    @OnClick(R.id.btnRepairMainUpdateRepairBaseData)
    void updateRepairBaseData(){
        OthersUtil.showChooseDialog(this, "确认更新（下载）维修基本数据？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                OthersUtil.showLoadDialog(dialog);
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(RepairMainActivity.this, "")
                                .map(new Func1<String, HsWebInfo>() {
                                    @Override
                                    public HsWebInfo call(String s) {
                                        HsWebInfo hsWebInfo=NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                                "spAppEPQueryRepairPlanList","",
                                                RepairList.class.getName(),true,"维修基本数据更新（下载）失败！");
                                        if(!hsWebInfo.success) return hsWebInfo;
                                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                                        List<RepairEquipmentInSQLite> repairEquipmentInSQLiteList=new ArrayList<>();
                                        for(WsEntity entity:entities){
                                            RepairList repairItem= (RepairList) entity;
                                            Log.e("TAG","repairItem="+repairItem);
                                            RepairEquipmentInSQLite repairEquipmentInSQLite=new RepairEquipmentInSQLite();
                                            repairEquipmentInSQLite.setAssetsCode(repairItem.ASSETSCODE);
                                            repairEquipmentInSQLite.setCostCenter(repairItem.COSTCENTER);
                                            repairEquipmentInSQLite.setEpcCode(repairItem.EPCCODE);
                                            repairEquipmentInSQLite.setEquipmentDetailID(repairItem.EQUIPMENTDETAILID);
                                            repairEquipmentInSQLite.setEquipmentName(repairItem.EQUIPMENTNAME);
                                            repairEquipmentInSQLite.setFactory(repairItem.FACTORY);
                                            repairEquipmentInSQLite.setModel(repairItem.MODEL);
                                            repairEquipmentInSQLite.setOutFactoryCode(repairItem.OUTFACTORYCODE);
                                            repairEquipmentInSQLiteList.add(repairEquipmentInSQLite);
                                        }
                                        Map<String,Object> data=new HashMap<>();
                                        data.put("repairEquipmentInSQLiteList",repairEquipmentInSQLiteList);
                                        hsWebInfo.object=data;
                                        Log.e("TAG","data1="+data);
                                        return hsWebInfo;
                                    }
                                })
                                .map(new Func1<HsWebInfo, HsWebInfo>() {
                                    @Override
                                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                                        if(!hsWebInfo.success) return hsWebInfo;
                                        //维修结果
                                        Map<String,Object> data= (Map<String, Object>) hsWebInfo.object;
                                        hsWebInfo=NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                                "spAPPEquipmentQueryBaseData",
                                                "type=维修结果",
                                                BaseData.class.getName(),true,"维修基本数据更新（下载）失败！");
                                        if(!hsWebInfo.success) return hsWebInfo;
                                        List<WsEntity> repairResultList=hsWebInfo.wsData.LISTWSDATA;
                                        List<RepairBaseDataInSQLite> resultInSQLiteList=new ArrayList<>();
                                        for(WsEntity entity:repairResultList){
                                            BaseData result= (BaseData) entity;
                                            RepairBaseDataInSQLite resultInSQLite=new RepairBaseDataInSQLite();
                                            resultInSQLite.setType(1);
                                            resultInSQLite.setCode(result.CODEID);
                                            resultInSQLite.setName(result.NAME);
                                            resultInSQLiteList.add(resultInSQLite);
                                        }
                                        data.put("resultInSQLiteList",resultInSQLiteList);
                                        //维护项目
                                        hsWebInfo=NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                                "spAPPEquipmentQueryBaseData",
                                                "type=维护项目",
                                                BaseData.class.getName(),true,"维修基本数据更新（下载）失败！");
                                        if(!hsWebInfo.success) return hsWebInfo;
                                        List<RepairBaseDataInSQLite> projectInSQLiteList=new ArrayList<>();
                                        List<WsEntity> repairProjectList=hsWebInfo.wsData.LISTWSDATA;
                                        for(WsEntity entity:repairProjectList){
                                            BaseData result= (BaseData) entity;
                                            RepairBaseDataInSQLite projectInSQLite=new RepairBaseDataInSQLite();
                                            projectInSQLite.setType(0);
                                            projectInSQLite.setCode(result.CODEID);
                                            projectInSQLite.setName(result.NAME);
                                            projectInSQLiteList.add(projectInSQLite);
                                        }
                                        data.put("projectInSQLiteList",projectInSQLiteList);
                                        hsWebInfo.object=data;
                                        return hsWebInfo;
                                    }
                                })
                                .map(new Func1<HsWebInfo, HsWebInfo>() {
                                    @Override
                                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                                        if(!hsWebInfo.success) return hsWebInfo;
                                        Map<String,Object> data= (Map<String, Object>) hsWebInfo.object;
                                        Log.e("TAG","data2="+data);
                                        List<RepairBaseDataInSQLite> projectInSQLiteList= (List<RepairBaseDataInSQLite>) data.get("projectInSQLiteList");
                                        List<RepairBaseDataInSQLite> resultInSQLiteList= (List<RepairBaseDataInSQLite>) data.get("resultInSQLiteList");
                                        List<RepairEquipmentInSQLite> repairEquipmentInSQLiteList= (List<RepairEquipmentInSQLite>) data.get("repairEquipmentInSQLiteList");
                                        Log.e("TAG", "repairEquipmentInSQLiteList=" + repairEquipmentInSQLiteList);
                                        if(resultInSQLiteList==null||resultInSQLiteList.isEmpty()||
                                                projectInSQLiteList==null||projectInSQLiteList.isEmpty()||
                                                repairEquipmentInSQLiteList==null||repairEquipmentInSQLiteList.isEmpty()){
                                            hsWebInfo.success=false;
                                            hsWebInfo.error.error="维修基本数据更新（下载）失败！";
                                            return hsWebInfo;
                                        }
                                        DaoSession daoSession=OthersUtil.getGreenDaoSession(getApplicationContext());
                                        RepairEquipmentInSQLiteDao repairEquipmentInSQLiteDao=daoSession.getRepairEquipmentInSQLiteDao();
                                        Log.e("TAG", "repairEquipmentInSQLiteDaoBefore=" + repairEquipmentInSQLiteDao);
                                        RepairBaseDataInSQLiteDao repairBaseDataInSQLiteDao=daoSession.getRepairBaseDataInSQLiteDao();
                                        repairEquipmentInSQLiteDao.deleteAll();//删除
                                        repairBaseDataInSQLiteDao.deleteAll();
                                        repairEquipmentInSQLiteDao.insertOrReplaceInTx(repairEquipmentInSQLiteList);
                                        Log.e("TAG", "repairEquipmentInSQLiteDaoAfter=" + repairEquipmentInSQLiteDao);
                                        repairBaseDataInSQLiteDao.insertOrReplaceInTx(projectInSQLiteList);//插入或替换
                                        repairBaseDataInSQLiteDao.insertOrReplaceInTx(resultInSQLiteList);
                                        Log.e("TAG","hsWebInfo2="+hsWebInfo);
                                        return hsWebInfo;
                                    }
                                })

                        , getApplicationContext(), dialog, new SimpleHsWeb() {
                            @Override
                            public void success(HsWebInfo hsWebInfo) {
                                OthersUtil.ToastMsg(getApplicationContext(),"维修基本数据更新（下载）成功！！");
                                isNeedUpdateBaseData=false;
                                showRepairBaseData();
                                Log.e("TAG","success");
                            }

                            @Override
                            public void error(HsWebInfo hsWebInfo) {
                                super.error(hsWebInfo);
                                OthersUtil.showTipsDialog(RepairMainActivity.this,hsWebInfo.error.error);
                                Log.e("TAG","error");
                            }
                        });
            }
        });
    }


}
