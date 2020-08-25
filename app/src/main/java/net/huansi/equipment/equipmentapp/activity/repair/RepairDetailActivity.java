package net.huansi.equipment.equipmentapp.activity.repair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.LargerImageSHowActivity;
import net.huansi.equipment.equipmentapp.adapter.PictureAddCommonAdapter;
import net.huansi.equipment.equipmentapp.adapter.RepairDetailAdapter;
import net.huansi.equipment.equipmentapp.entity.AttachDtl;
import net.huansi.equipment.equipmentapp.entity.BaseData;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.PictureCommitHdr;
import net.huansi.equipment.equipmentapp.entity.RepairDetail;
import net.huansi.equipment.equipmentapp.entity.RepairEvaluate;
import net.huansi.equipment.equipmentapp.entity.RepairHdrSubmit;
import net.huansi.equipment.equipmentapp.entity.RepairList;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.gen.RepairDetailInSQLiteDao;
import net.huansi.equipment.equipmentapp.gen.RepairHdrInSQLiteDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairDetailInSQLite;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairHdrInSQLite;
import net.huansi.equipment.equipmentapp.util.CallRepairUtil;
import net.huansi.equipment.equipmentapp.util.NetUtil;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.PictureAddDialogUtils;
import net.huansi.equipment.equipmentapp.util.PictureCompressUtils;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.SewLineUtil;
import net.huansi.equipment.equipmentapp.widget.HorizontalListView;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.greendao.query.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.constant.Constant.LargerImageSHowActivityConstants.URL_PATH_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.PICTURE_ALL_NUMBER_PER;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.ADD_DETAIL_QUEST_CODE;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.PLAN_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.SQLITE_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailActivityConstants.UPDATE_DETAIL_QUEST_CODE;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailAddActivityConstants.DETAIL_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailAddActivityConstants.DETAIL_DATA_POSITION_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailAddActivityConstants.RETURN_DATA_KEY;
import static net.huansi.equipment.equipmentapp.util.PictureAddDialogUtils.ALBUM_PICTURE_REQUEST_CODE;
import static net.huansi.equipment.equipmentapp.util.PictureAddDialogUtils.CAMERA_PICTURE_REQUEST_CODE;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by 单中年 on 2017/3/1.
 */

public class RepairDetailActivity extends BaseActivity {
    @BindView(R.id.etRepairDetailRemark) EditText etRepairDetailRemark;//备注
    @BindView(R.id.tvRepairDetailEquipmentName) TextView tvRepairDetailEquipmentName;//设备名称
    @BindView(R.id.tvRepairDetailCostCenter) TextView tvRepairDetailCostCenter;//成本中心
    @BindView(R.id.tvRepairDetailEPCode) TextView tvRepairDetailEPCode;//设备编码
    @BindView(R.id.tvRepairDetailOutOfFactoryCode) TextView tvRepairDetailOutOfFactoryCode;//出厂编码
    @BindView(R.id.tvRepairDetailEquipmentModel) TextView tvRepairDetailEquipmentModel;//设备型号
    @BindView(R.id.tvRepairDetailAssetsCode) TextView tvRepairDetailAssetsCode;//资产编码
    @BindView(R.id.lvRepairDetail) ListView lvRepairDetail;
    @BindView(R.id.btnRepairDetailStart) Button btnRepairDetailStart;//开始维修
    @BindView(R.id.timer) Chronometer timer;
    @BindView(R.id.repairDetailMainActionLayout) LinearLayout repairDetailMainActionLayout;//提交、保存、添加明细的layout
    @BindView(R.id.hlvRepairDetailPicture)HorizontalListView hlvRepairDetailPicture;//图片
    @BindView(R.id.imvRepairDetailPictureAdd) ImageView imvRepairDetailPictureAdd;
    @BindView(R.id.tv_record) TextView tv_record;
    @BindView(R.id.tv_Group) TextView tv_Group;
    @BindView(R.id.ib_showRecord) ImageButton ib_showRecord;
    @BindView(R.id.ib_showGroup) ImageButton ib_showGroup;
    //@BindView(R.id.spGroupConfig) Spinner spGroupConfig;
    //@BindView(R.id.btnRepairDetailContinue) Button btnRepairDetailContinue;
    private List<RepairDetail> mDetailList;
    private RepairDetailAdapter mAdapter;
    private LoadProgressDialog dialog;
    private PictureAddCommonAdapter mPictureAdapter;//图片的适配
    private List<String> mPathList;//图片地址的数组
    private RepairHdrInSQLite repairHdrInSQLite;//SQLite中维修的主表
    private List<RepairDetailInSQLite> detailInSQLiteList;//SQLite中的维修明细

    private PictureAddDialogUtils pictureAddDialogUtils;

    private RecordAdapter recordAdapter;
    private GroupAdapter groupAdapter;
    private List<String> numberList;
    private PopupWindow pw;
    private List<CallRepairUtil.DATABean> recordData;//叫修记录

    private List<SewLineUtil.DATABean> groupData;//维修产线、班组

    @Override
    protected int getLayoutId() {
        return R.layout.repair_detail_activity;
    }



    @Override
    public void init() {

        setToolBarTitle("维修");
        OthersUtil.hideInputFirst(this);
        repairHdrInSQLite= (RepairHdrInSQLite) getIntent().getSerializableExtra(SQLITE_DATA_PARAM);
        //新建的维修记录
        //Log.e("TAG","对象="+repairHdrInSQLite);
        if(repairHdrInSQLite==null) {
            RepairList repairEquipment = (RepairList) getIntent().getSerializableExtra(PLAN_PARAM);
            RepairEvaluate callRepairRecord = (RepairEvaluate) getIntent().getSerializableExtra("CallRepairRecord");
            //Log.e("TAG","设备iD="+repairEquipment.EQUIPMENTDETAILID);
            if (callRepairRecord==null){//主动维修、保养
                initiativeRepair(repairEquipment);
                //选完班组后初始化数据库保存数据
            }else if (repairEquipment==null){//叫修清单跳转带过来数据并本地sql保存
                repairHdrInSQLite = new RepairHdrInSQLite();
                repairHdrInSQLite.setAssetsCode(callRepairRecord.ASSETSCODE);
                repairHdrInSQLite.setCostCenter(callRepairRecord.COSTCENTER);
                repairHdrInSQLite.setEPCode(callRepairRecord.EPCCODE);
                repairHdrInSQLite.setEquipmentModel(callRepairRecord.MODEL);
                repairHdrInSQLite.setEquipmentName(callRepairRecord.EQUIPMENTNAME);
                repairHdrInSQLite.setEquipmentId(callRepairRecord.EQUIPMENTDETAILID);
                repairHdrInSQLite.setOutOfCode(callRepairRecord.OUTFACTORYCODE);
                repairHdrInSQLite.setCallRepairItemId(callRepairRecord.CALLREPAIRITEMID);
                repairHdrInSQLite.setCallRepairGroupId(callRepairRecord.POSTID);
                repairHdrInSQLite.setCallRepairRecord(callRepairRecord.CALLREPAIRDATE);
                repairHdrInSQLite.setCallRepairGroup(callRepairRecord.SEWLINE);

            }

        }else {//继续上一次的维修记录
            tv_record.setText(repairHdrInSQLite.getCallRepairRecord());
            tv_Group.setText(repairHdrInSQLite.getCallRepairGroup());

            //Log.e("TAG","奔溃时间："+Long.toString(repairHdrInSQLite.getRepairTime()));
            try {
                timer.setBase(SystemClock.elapsedRealtime()-repairHdrInSQLite.getRepairTime());
            } catch (Exception e) {
                e.printStackTrace();
            }

            btnRepairDetailStart.setVisibility(View.INVISIBLE);
            repairDetailMainActionLayout.setVisibility(View.VISIBLE);
            timer.start();
            timer.setVisibility(View.VISIBLE);
        }

        pictureAddDialogUtils=new PictureAddDialogUtils();
        dialog=new LoadProgressDialog(this);
        mDetailList=new ArrayList<>();
        detailInSQLiteList=new ArrayList<>();
        mPathList=new ArrayList<>();
        initEPInfo();
        mPictureAdapter=new PictureAddCommonAdapter(mPathList,getApplicationContext());
        hlvRepairDetailPicture.setAdapter(mPictureAdapter);
        mAdapter=new RepairDetailAdapter(mDetailList,getApplicationContext());
        lvRepairDetail.setAdapter(mAdapter);
        getRepairDetailInSQLite();

    }

    private void initiativeRepair(RepairList repairEquipment) {
        repairHdrInSQLite = new RepairHdrInSQLite();
        repairHdrInSQLite.setAssetsCode(repairEquipment.ASSETSCODE);
        repairHdrInSQLite.setCostCenter(repairEquipment.COSTCENTER);
        repairHdrInSQLite.setEPCode(repairEquipment.EPCCODE);
        repairHdrInSQLite.setEquipmentModel(repairEquipment.MODEL);
        repairHdrInSQLite.setEquipmentName(repairEquipment.EQUIPMENTNAME);
        repairHdrInSQLite.setEquipmentId(repairEquipment.EQUIPMENTDETAILID);
        repairHdrInSQLite.setOutOfCode(repairEquipment.OUTFACTORYCODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            RepairHdrInSQLite data_key = (RepairHdrInSQLite) savedInstanceState.getSerializable("data_key");
            repairHdrInSQLite=data_key;
            initEPInfo();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //RepairList repairEquipment = (RepairList) getIntent().getSerializableExtra(PLAN_PARAM);
        outState.putSerializable("data_key",repairHdrInSQLite);
    }


    @Override
    public void back() {
        if(repairDetailMainActionLayout.getVisibility()==View.VISIBLE) {
            OthersUtil.showTipsDialog(this,"您还未保存数据,请先本地保存后退出！！");
        }else {
            finish();
        }
    }
    @OnClick(R.id.ib_showGroup)
    void showGroupList(View view){
        //初始化listview控件和里边的数据
        ListView mListView = initGroupListView();
        // 弹出一个PopupWindow的窗体, 把ListView作为其内容部分显示.
        pw = new PopupWindow(mListView,this.getWindowManager().getDefaultDisplay().getWidth()-40, 900);
        //设置可以使用焦点
        pw.setFocusable(true);
        //设置点击pop外部可以被关闭
        pw.setOutsideTouchable(true);
        //设置一个pop的背景
        pw.setBackgroundDrawable(new BitmapDrawable());
        // 把popupwindow显示出来, 显示的位置是: 在输入框的下面, 和输入框是连着的.
        pw.showAtLocation(view,1,5,-5);

    }

    private ListView initGroupListView() {
        final ListView mListView = new ListView(this);
        //去掉listview的下分割线
        //mListView.setDividerHeight(0);
        mListView.setBackgroundResource(R.drawable.shadow);
        //去掉右侧垂直滑动条
        mListView.setVerticalScrollBarEnabled(false);
        //mListView.setOnItemClickListener(this);
        //获取数据
        numberList = new ArrayList<>();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppEPSewLine","",String.class.getName(),false,"获取班组产线");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                Log.e("TAG","success="+hsWebInfo.json);
                String json = hsWebInfo.json;
                SewLineUtil sewLineUtil = JSON.parseObject(json, SewLineUtil.class);
                List<SewLineUtil.DATABean> data = sewLineUtil.getDATA();
                groupData=data;
                Log.e("TAG","data="+data);
                for (int i=0;i<data.size();i++){
                    numberList.add(i,data.get(i).getDEPTNAME());
                }
                groupAdapter=new GroupAdapter();
                mListView.setAdapter(groupAdapter);
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error");
            }
        });
        return mListView;
    }

    @OnClick(R.id.ib_showRecord)//展示可供选择的叫修记录
    public void showRecordList(View view) {
        //初始化listview控件和里边的数据
        ListView mListView = initRecordListView();
        // 弹出一个PopupWindow的窗体, 把ListView作为其内容部分显示.
        pw = new PopupWindow(mListView,this.getWindowManager().getDefaultDisplay().getWidth()-40, 900);
        //设置可以使用焦点
        pw.setFocusable(true);
        //设置点击pop外部可以被关闭
        pw.setOutsideTouchable(true);
        //设置一个pop的背景
        pw.setBackgroundDrawable(new BitmapDrawable());
        // 把popupwindow显示出来, 显示的位置是: 在输入框的下面, 和输入框是连着的.
        pw.showAtLocation(view,1,5,-5);

    }
    private ListView initRecordListView() {
        final ListView mListView = new ListView(this);
        //去掉listview的下分割线
        //mListView.setDividerHeight(0);
        mListView.setBackgroundResource(R.drawable.shadow);
        //去掉右侧垂直滑动条
        mListView.setVerticalScrollBarEnabled(false);
        //mListView.setOnItemClickListener(this);
        //获取数据
        numberList = new ArrayList<>();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spAppEPQueryCallRepairRecord","",String.class.getName(),false,"数据获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                Log.e("TAG","success="+hsWebInfo.json);
                String json = hsWebInfo.json;
                CallRepairUtil callRepairUtil = JSON.parseObject(json, CallRepairUtil.class);
                List<CallRepairUtil.DATABean> data = callRepairUtil.getDATA();
                recordData=data;
                Log.e("TAG","data="+data);
                for (int i=0;i<data.size();i++){
                    numberList.add(i,data.get(i).getCALLREPAIRDATE());
                }
                recordAdapter=new RecordAdapter();
                mListView.setAdapter(recordAdapter);
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error");
            }
        });
        return mListView;
    }
    private class GroupAdapter extends BaseAdapter {
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
            NumberHolder mHolder = null;
            if(convertView==null){
                convertView =View.inflate(RepairDetailActivity.this,R.layout.listview_item,null);
                mHolder=new NumberHolder();
                mHolder.tvLine= (TextView) convertView.findViewById(R.id.tv_listview_item_line);
                //mHolder.ibDelete= (ImageButton) convertView.findViewById(R.id.ib_listview_item_delete);
                convertView.setTag(mHolder);
            }else {
                mHolder = (NumberHolder) convertView.getTag();
            }
            mHolder.tvLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    repairHdrInSQLite.setCallRepairGroupId(groupData.get(position).getPOSTID());
                    //Log.e("TAG","班组ID="+repairHdrInSQLite.getCallRepairGroupId());
                    //groupPosition=position;
                    tv_Group.setText(numberList.get(position));
                    pw.dismiss();
                }
            });
            mHolder.tvLine.setText(numberList.get(position));
            return convertView;
        }
    }
    private class RecordAdapter extends BaseAdapter {
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
            NumberHolder mHolder = null;
            if(convertView==null){
                convertView =View.inflate(RepairDetailActivity.this,R.layout.listview_item,null);
                mHolder=new NumberHolder();
                mHolder.tvLine= (TextView) convertView.findViewById(R.id.tv_listview_item_line);
                //mHolder.ibDelete= (ImageButton) convertView.findViewById(R.id.ib_listview_item_delete);
                convertView.setTag(mHolder);
            }else {
                mHolder = (NumberHolder) convertView.getTag();
            }
            mHolder.tvLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    repairHdrInSQLite.setCallRepairItemId(recordData.get(position).getCALLREPAIRITEMID());
                    Log.e("TAG","叫修ID="+repairHdrInSQLite.getCallRepairItemId());
                    //recordPosition=position;
                    tv_record.setText(numberList.get(position));
                    pw.dismiss();
                }
            });
            mHolder.tvLine.setText(numberList.get(position));
            return convertView;
        }
    }
    class NumberHolder {
        TextView tvLine;
        //ImageButton ibDelete;
    }
    /**
     * 获得SQLite中的维修明细
     */
    @SuppressWarnings("unchecked")
    private void getRepairDetailInSQLite(){
        //必须是上一次的维修记录才有维修明细
        if(repairHdrInSQLite.getId()==null||repairHdrInSQLite.getId()<0) return;
        mDetailList.clear();
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        HsWebInfo hsWebInfo=new HsWebInfo();
                        RepairDetailInSQLiteDao detailInSQLiteDao= OthersUtil.getGreenDaoSession(getApplicationContext())
                                .getRepairDetailInSQLiteDao();
                        Query<RepairDetailInSQLite> query=detailInSQLiteDao
                                .queryBuilder()
                                .where(RepairDetailInSQLiteDao.Properties.HdrId.eq(repairHdrInSQLite.getId()))
                                .build();
                        if(query==null) {
                            hsWebInfo.success=false;
                            return hsWebInfo;
                        }
                        hsWebInfo.object=query.list();
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                List<RepairDetailInSQLite> repairDetailInSQLites= (List<RepairDetailInSQLite>) hsWebInfo.object;
                if(repairDetailInSQLites==null){
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                detailInSQLiteList.addAll(repairDetailInSQLites);
                for(RepairDetailInSQLite detailInSQLite:repairDetailInSQLites ){
                    RepairDetail detail=detailChangeFromSQLite(detailInSQLite);
                    mDetailList.add(detail);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 初始化设备信息
     */
    private void initEPInfo(){
        if(repairHdrInSQLite==null) return;
        etRepairDetailRemark.setText(repairHdrInSQLite.getRemark());
        tvRepairDetailEquipmentName.setText(repairHdrInSQLite.getEquipmentName());
        tvRepairDetailCostCenter.setText(repairHdrInSQLite.getCostCenter());
        tvRepairDetailEPCode.setText(repairHdrInSQLite.getEPCode());
        tvRepairDetailOutOfFactoryCode.setText(repairHdrInSQLite.getOutOfCode());
        tvRepairDetailEquipmentModel.setText(repairHdrInSQLite.getEquipmentModel());
        tvRepairDetailAssetsCode.setText(repairHdrInSQLite.getAssetsCode());
        tv_record.setText(repairHdrInSQLite.getCallRepairRecord());
        tv_Group.setText(repairHdrInSQLite.getCallRepairGroup());
        if (!tv_Group.getText().toString().isEmpty()&&!tv_record.getText().toString().isEmpty()){
            ib_showRecord.setClickable(false);
            ib_showGroup.setClickable(false);
        }
        if(repairHdrInSQLite.getPathList()==null||repairHdrInSQLite.getPathList().trim().isEmpty()) return;
        String [] pathArr=repairHdrInSQLite.getPathList().split("@@");
        Log.e("TAG","pathArr="+pathArr);
        for(int i=0;i<pathArr.length;i++){
            mPathList.add(pathArr[i]);
        }
    }
    /**
     * 开始维修
     */
    @OnClick(R.id.btnRepairDetailStart)
    void startRepair(){

       if (tv_Group.getText().toString().isEmpty()||tv_record.getText().toString().isEmpty()){
           OthersUtil.showTipsDialog(RepairDetailActivity.this,"主动保养无需叫修记录，请提前选择好产线/组别！！");
           return;
       }

       if (NetUtil.isNetworkAvailable(this)){
           saveLocal();

           //计时器开始计时
           timer.setVisibility(View.VISIBLE);
           timer.setBase(SystemClock.elapsedRealtime());
           int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60 /60 );
           Log.e("TAG","hour="+hour);
           //timer.setFormat("0"+String.valueOf(hour)+":%s");
           timer.start();
           //记录下系统当前时间
           repairHdrInSQLite.setCreateTime(System.currentTimeMillis());
           //记录下数据
       }else  if(!NetUtil.isNetworkAvailable(getApplicationContext())){
           OthersUtil.ToastMsg(this,"目前网络不通畅");
           return;
       }
        //btnRepairDetailStart.setClickable(false);



    }

    private void insert() {//后台开启记时
        OthersUtil.showLoadDialog(dialog);
        String user = SPHelper.getLocalData(getApplicationContext(),
                USER_NO_KEY, String.class.getName(), "").toString();
        RxjavaWebUtils.requestByGetJsonData(this,
                "spAppEPStartRepairNew",
                "uEquipmentListDetailID=" + repairHdrInSQLite.getEquipmentId() +//设备id
                        ",ApplyRepairID="+repairHdrInSQLite.getCallRepairItemId()+//叫修id
                        ",sUserNo=" +user ,
                getApplicationContext(),
                dialog,
                RepairHdrSubmit.class.getName(),
                false,
                "服务器连接异常",
                new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                        if(entities.isEmpty()){
                            OthersUtil.dismissLoadDialog(dialog);
                            OthersUtil.showTipsDialog(RepairDetailActivity.this,"服务器连接异常");
                            return;
                        }
                        repairHdrInSQLite.setRepairPlanHdrId(((RepairHdrSubmit)(entities.get(0))).UAPPEPREPAIRHDRID);
                        Observable.just(repairHdrInSQLite)
                                .compose(RepairDetailActivity.this.<RepairHdrInSQLite>bindToLifecycle())
                                .subscribeOn(Schedulers.io())
                                .map(new Func1<RepairHdrInSQLite, RepairHdrInSQLite>() {
                                    @Override
                                    public RepairHdrInSQLite call(RepairHdrInSQLite repairHdrInSQLite) {
                                        RepairHdrInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getRepairHdrInSQLiteDao();
                                        long id=dao.insertOrReplace(repairHdrInSQLite);
                                        Query<RepairHdrInSQLite> query = dao.queryBuilder()
                                                .where(RepairHdrInSQLiteDao.Properties.Id.eq(id))
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
                                    public void call(RepairHdrInSQLite repairHdrInSQLite) {
                                        OthersUtil.dismissLoadDialog(dialog);
                                        if(repairHdrInSQLite==null){
                                            OthersUtil.showTipsDialog(RepairDetailActivity.this,"服务器连接异常");
                                            return;
                                        }
                                        RepairDetailActivity.this.repairHdrInSQLite=repairHdrInSQLite;
                                        btnRepairDetailStart.setVisibility(View.GONE);
                                        repairDetailMainActionLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                    }
                });

    }


    /**
     * 添加明细
     */
    @OnClick(R.id.btnRepairDetailAdd)
    void addDetail(){
        Intent intent=new Intent(this,RepairDetailAddActivity.class);
        startActivityForResult(intent,ADD_DETAIL_QUEST_CODE);
    }

    /**
     * 上传数据
     */
    @OnClick(R.id.btnRepairDetailCommit)
    void commit(){
        final long committime = SystemClock.elapsedRealtime() - timer.getBase();
        repairHdrInSQLite.setRepairTime(committime);
        if (tv_record.getText().toString().isEmpty()||tv_Group.getText().toString().isEmpty()){
            OthersUtil.showTipsDialog(RepairDetailActivity.this,"叫修记录或者产线没选！！");
        }else {
            Long createTime = repairHdrInSQLite.getCreateTime();
            long time = System.currentTimeMillis() - createTime;
            int t = (int) (time / 1000 / 60 );
            Log.e("TAG","time="+time);
            if (t>30){
                if (etRepairDetailRemark.getText().toString().isEmpty()){
                    OthersUtil.showTipsDialog(this,"维修超过半小时需在备注栏填写维修难点！");
                }else {
                    updateData();
                }
            }else {
                updateData();
            }
        }
    }

    private void updateData() {
        if (mDetailList.isEmpty()){
            OthersUtil.ToastMsg(this,"您还未添加维修明细");
            return;
        }
        OthersUtil.showChooseDialog(this, "确认维修完成、上传您的维修记录吗？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OthersUtil.showLoadDialog(dialog);
                        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(RepairDetailActivity.this, "")
                                        //先要上传图片
                                        .map(new Func1<String, HsWebInfo>() {
                                            @Override
                                            public HsWebInfo call(String s) {
                                                if(!mPathList.isEmpty()) {
                                                    return submitPicture();
                                                }
                                                return new HsWebInfo();
                                            }
                                        })
                                        //上传维修头表
                                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                                            @Override
                                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                                if(!hsWebInfo.success) return hsWebInfo;
                                                if(!mPathList.isEmpty()){
                                                    return submitHdrText(hsWebInfo.object==null?"":hsWebInfo.object.toString());
                                                }else {
                                                    return submitHdrText("");
                                                }
                                            }
                                        })
                                        //上传维修明细
                                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                                            @Override
                                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                                if(!hsWebInfo.success) return hsWebInfo;
                                                RepairHdrSubmit repairHdr= (RepairHdrSubmit) hsWebInfo.wsData.LISTWSDATA.get(0);
                                                return submitRepairDetail(repairHdr.UAPPEPREPAIRHDRID);
                                            }
                                        })
                                        //修改本地维修数据的状态
                                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                                            @Override
                                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                                if (!hsWebInfo.success) return hsWebInfo;
                                                RepairHdrInSQLiteDao hdrInSQLiteDao = OthersUtil.getGreenDaoSession
                                                        (getApplicationContext()).getRepairHdrInSQLiteDao();
                                                repairHdrInSQLite.setSubmitStatus(1);
                                                if (repairHdrInSQLite.getId() != null) {
                                                    hdrInSQLiteDao.insertOrReplace(repairHdrInSQLite);
                                                }
                                                return hsWebInfo;
                                            }
                                        })
                                , getApplicationContext(), dialog, new SimpleHsWeb() {
                                    @Override
                                    public void success(HsWebInfo hsWebInfo) {
                                        //上传成功以后更改本地维修数据状态
//                                        RepairHdrInSQLiteDao hdrInSQLiteDao = OthersUtil.getGreenDaoSession
//                                                (getApplicationContext()).getRepairHdrInSQLiteDao();
//                                        repairHdrInSQLite.setSubmitStatus(1);
//                                        if (repairHdrInSQLite.getId() != null) {
//                                            hdrInSQLiteDao.insertOrReplace(repairHdrInSQLite);
//                                        }
                                        timer.stop();
                                        Intent intent = new Intent(RepairDetailActivity.this, RepairMainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void error(HsWebInfo hsWebInfo) {
                                        OthersUtil.showTipsDialog(RepairDetailActivity.this,"网络异常未上传成功,请稍后重试");
                                    }
                                });


                    }
                });
    }

    /**
     * 上传头部图片
     */
    private HsWebInfo submitPicture(){
        List<String> dateTypeList = new ArrayList<>();
        Log.e("TAG","mPathList=-"+mPathList);
        for (int i = 0; i < mPathList.size(); i++) {
            dateTypeList.add("IMAGE");
        }
        //首先上传图片的头表
        Map<String, String> hdrMap = new HashMap<>();
        hdrMap.put("sAppUserNo", SPHelper.getLocalData(this, USER_NO_KEY, String.class.getName(), "").toString());
        hdrMap.put("sDataKey", "");
        hdrMap.put("sTitle", "维修图片");
        hdrMap.put("sContents", "维修图片");
        HsWebInfo hsWebInfo=NewRxjavaWebUtils.getNormalFunction(getApplicationContext(),
                "AttachHdrSubmit",hdrMap,
                PictureCommitHdr.class.getName(),false,"上传失败");
        if(!hsWebInfo.success) return hsWebInfo;
        PictureCommitHdr pictureCommitHdr = (PictureCommitHdr) hsWebInfo.wsData.LISTWSDATA.get(0);
        Log.e("TAG","pictureCommitHdr=-"+pictureCommitHdr);
        //上传图片明细
        for (int i = 0; i < mPathList.size(); i++) {
            Map<String, String> dtlMap = new HashMap<>();
            dtlMap.put("iSeq", i + "");
            dtlMap.put("uAttachHdrGUID", pictureCommitHdr.IIDEN);
            dtlMap.put("sFolder","file");
            dtlMap.put("sDataType", dateTypeList.get(i));
            dtlMap.put("sLocalPicPath", mPathList.get(i));
            dtlMap.put("iSaveToDB", "0");
            dtlMap.put("byteArray", PictureCompressUtils.compressImage(mPathList.get(i),40));
            hsWebInfo = NewRxjavaWebUtils.getNormalFunction(getApplicationContext(),
                    "AttachDtlSubmit", dtlMap,
                    WsData.class.getName(), false, "上传失败");
            if (!hsWebInfo.success) return hsWebInfo;
        }
        //查询上传的图片信息
        hsWebInfo=NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                "spAppEPQueryServerPicturePath",
                "uAttachHdrGUID="+pictureCommitHdr.IIDEN,
                AttachDtl.class.getName(),
                true,
                "上传出错！！");
        if(!hsWebInfo.success) return hsWebInfo;
        StringBuilder sbPath=new StringBuilder();
        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
        Log.e("TAG","entities="+entities.toString());
        for(int i=0;i<entities.size();i++){
            AttachDtl attachDtl= (AttachDtl) entities.get(i);
            sbPath.append(attachDtl.SFOLDER)
                    .append("/")
                    .append(attachDtl.SFILENAME);
            if(i!=entities.size()-1){
                sbPath.append(";");
            }
            Log.e("TAG","sbPath="+sbPath);
        }
        hsWebInfo.object=sbPath.toString();
        return hsWebInfo;
    }

    /**
     * 上传头部数据
     */
    private HsWebInfo submitHdrText(String picturePath) {
//        String callrepairitemid = recordData.get(recordPosition).getCALLREPAIRITEMID();
////        String postid = groupData.get(groupPosition).getPOSTID();
////        Log.e("TAG","callrepairitemid="+callrepairitemid);
////        Log.e("TAG","postid="+postid);
        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                "spAppEPSubmitRepairHdr",
                "uAppEPRepairHdrId=" + repairHdrInSQLite.getRepairPlanHdrId() +
                        ",uEquipmentListDetailID=" + repairHdrInSQLite.getEquipmentId() +
                        ",sCallRepairItemID=" + repairHdrInSQLite.getCallRepairItemId() +
                        ",sPostID="+repairHdrInSQLite.getCallRepairGroupId()+
                        ",sRemark=" + etRepairDetailRemark.getText().toString() +
                        ",sUserNo=" + SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY,
                        String.class.getName(), "").toString() +
                        ",sPicturePath=" + picturePath,
                RepairHdrSubmit.class.getName(),
                false,
                "上传失败");

    }


    /**
     * 修改数据
     */
    @OnItemClick(R.id.lvRepairDetail)
    void updateDetail(int position){
        Intent intent=new Intent(this,RepairDetailAddActivity.class);
        intent.putExtra(DETAIL_DATA_PARAM,mDetailList.get(position));
        intent.putExtra(DETAIL_DATA_POSITION_PARAM,position);
        startActivityForResult(intent,UPDATE_DETAIL_QUEST_CODE);
    }

    /**
     * 添加图片
     */
    @OnClick(R.id.imvRepairDetailPictureAdd)
    void addPicture(){
        if(btnRepairDetailStart.getVisibility()==View.VISIBLE){
            OthersUtil.showTipsDialog(this,"请先开始维修！！");
            return;
        }
        if(mPathList.size()>=PICTURE_ALL_NUMBER_PER){
            OthersUtil.showTipsDialog(this,"一维修张单只能上传两张，请检查！！");
            return;
        }
        pictureAddDialogUtils.showPictureAddDialog(this);
    }

    /**
     * 删除图片
     */
    @OnItemLongClick(R.id.hlvRepairDetailPicture)
    boolean delPicture(int position){
        mPathList.remove(position);
        mPictureAdapter.notifyDataSetChanged();
        return true;
    }

    /**
     * 放大图片
     * @param position
     */
    @OnItemClick(R.id.hlvRepairDetailPicture)
    void showLargerPicture(int position){
        Intent intent=new Intent(this, LargerImageSHowActivity.class);
        intent.putExtra(URL_PATH_PARAM,mPathList.get(position));
        startActivity(intent);
    }

    /**
     * 长按删除明细
     * @return
     */
    @OnItemLongClick(R.id.lvRepairDetail)
    boolean delDetail(final int position){
        OthersUtil.showChooseDialog(this, "是否删除这个明细？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                OthersUtil.showLoadDialog(dialog);
                Observable.just(position)
                        .compose(RepairDetailActivity.this.<Integer>bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<Integer, Integer>() {
                            @Override
                            public Integer call(Integer position) {
                                RepairDetailInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).
                                        getRepairDetailInSQLiteDao();
                                RepairDetailInSQLite  detailInSQLite=detailInSQLiteList.get(position);
                                if(detailInSQLite.getId()==null) return position;
                                dao.delete(detailInSQLite);
                                return position;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer position) {
                                OthersUtil.dismissLoadDialog(dialog);
                                if(position==null) return;
                                detailInSQLiteList.remove(position.intValue());
                                mDetailList.remove(position.intValue());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });
        return true;
    }

    /**
     * 上传维修明细
     */
    private HsWebInfo submitRepairDetail(String repairHdrId){
        StringBuilder sb=new StringBuilder();
        for( RepairDetail detail:mDetailList){
            sb.append("exec spAppEPSubmitRepairDtl ")
                    .append("@uAppEPRepairHdrId='").append(repairHdrId).append("'")
                    .append(",@sProject='").append(detail.project.CODEID).append("'")
                    .append(",@sMethod='").append(detail.method).append("'")
                    .append(",@sResult='").append(detail.result.CODEID).append("'")
                    .append(",@sUserNo='").append(detail.repairUserNo).append("'")
                    .append(",@sRemark='").append(detail.remark).append("'")
                    .append(";");
        }

        return  NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                sb.toString(),"",
                WsData.class.getName(),
                false,
                "上传失败！");
    }

    /**
     * 保存数据到本地(可选会变的数据)
     */
    @OnClick(R.id.btnRepairDetailSave)
    void save(){
        saveLocal();
        timer.stop();
        Intent intent = new Intent(RepairDetailActivity.this, RepairMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void saveLocal() {
        final long time = SystemClock.elapsedRealtime() - timer.getBase();
        Log.e("TAG","Localtime="+time);
        OthersUtil.showLoadDialog(dialog);
        Observable.just("")
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String str) {
                        repairHdrInSQLite.setRemark(etRepairDetailRemark.getText().toString().trim());
                        repairHdrInSQLite.setRepairTime(time);
                        repairHdrInSQLite.setCallRepairRecord(tv_record.getText().toString());
                        repairHdrInSQLite.setCallRepairGroup(tv_Group.getText().toString());
                        StringBuilder sbPath=new StringBuilder();
                        for(int i=0;i<mPathList.size();i++){
                            sbPath.append(mPathList.get(i));
                            if(i!=mPathList.size()-1){
                                sbPath.append("@@");
                            }
                        }
                        repairHdrInSQLite.setPathList(sbPath.toString());
                        try{
                            RepairHdrInSQLiteDao hdrDao=OthersUtil.getGreenDaoSession(getApplicationContext()).getRepairHdrInSQLiteDao();
                            long hdrId=hdrDao.insertOrReplace(repairHdrInSQLite);
                            RepairDetailInSQLiteDao detailDao=OthersUtil.getGreenDaoSession(getApplicationContext()).getRepairDetailInSQLiteDao();
                            for(int i=0;i<detailInSQLiteList.size();i++){
                                RepairDetailInSQLite detailInSQLite=detailInSQLiteList.get(i);
                                detailInSQLite.setHdrId(hdrId);
                                detailInSQLiteList.set(i,detailInSQLite);
                            }
                            detailDao.insertOrReplaceInTx(detailInSQLiteList);
                            return true;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        return false;


                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        OthersUtil.dismissLoadDialog(dialog);
                        if(aBoolean){
                            OthersUtil.ToastMsg(getApplicationContext(),"保存成功！！");
                            insert();
                        }else {
                            OthersUtil.showTipsDialog(RepairDetailActivity.this,"保存失败！！");
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;
        switch (requestCode) {
            //添加明细
            case ADD_DETAIL_QUEST_CODE:
                if (data == null) return;
                RepairDetail addDetail = (RepairDetail) data.getSerializableExtra(RETURN_DATA_KEY);
                mDetailList.add(addDetail);
                RepairDetailInSQLite addDetailInSQLite = detailChangeToSQLite(addDetail);
                detailInSQLiteList.add(addDetailInSQLite);
                mAdapter.notifyDataSetChanged();
                break;
            //修改明细
            case UPDATE_DETAIL_QUEST_CODE:
                if (data == null) return;
                RepairDetail updateDetail = (RepairDetail) data.getSerializableExtra(RETURN_DATA_KEY);
                int updatePosition = data.getIntExtra(DETAIL_DATA_POSITION_PARAM, -1);
                if (updatePosition == -1 || updatePosition > mDetailList.size() - 1 || updatePosition > detailInSQLiteList.size() - 1)
                    return;
                mDetailList.set(updatePosition, updateDetail);
                RepairDetailInSQLite updateDetailInSQLite=detailInSQLiteList.get(updatePosition);
                updateDetailInSQLite.setMethod(updateDetail.method);
                updateDetailInSQLite.setRepairUserNo(updateDetail.repairUserNo);
                updateDetailInSQLite.setProjectID(updateDetail.project.CODEID);
                updateDetailInSQLite.setProjectName(updateDetail.project.NAME);

                updateDetailInSQLite.setResultID(updateDetail.result.CODEID);
                updateDetailInSQLite.setResultName(updateDetail.result.NAME);
                updateDetailInSQLite.setRemark(updateDetail.remark);
                detailInSQLiteList.set(updatePosition, updateDetailInSQLite);
                mAdapter.notifyDataSetChanged();
                break;
            //相册
            case ALBUM_PICTURE_REQUEST_CODE:
                pictureAddDialogUtils.initPictureAfterChooseByAlbum(data, this, mPathList);
                mPictureAdapter.notifyDataSetChanged();
                break;
            //拍照
            case CAMERA_PICTURE_REQUEST_CODE:
                pictureAddDialogUtils.initPictureAfterChooseByCamera(data, pictureAddDialogUtils.getPicturePath(), mPathList);
                mPictureAdapter.notifyDataSetChanged();
                break;
        }
    }


    /**
     * RepairDetail转成RepairDetailInSQLite类型
     * @param detail
     */
    private RepairDetailInSQLite detailChangeToSQLite(RepairDetail detail){
        RepairDetailInSQLite detailInSQLite=new RepairDetailInSQLite();
        detailInSQLite.setMethod(detail.method);
        detailInSQLite.setRepairUserNo(detail.repairUserNo);
        detailInSQLite.setProjectID(detail.project.CODEID);
        detailInSQLite.setProjectName(detail.project.NAME);

        detailInSQLite.setResultID(detail.result.CODEID);
        detailInSQLite.setResultName(detail.result.NAME);
        detailInSQLite.setRemark(detail.remark);
//        if(detail.pathList==null||detail.pathList.isEmpty()){
//            detailInSQLite.setPathList("");
//        }else {
//            StringBuffer sbPath=new StringBuffer();
//            for(int i=0;i<detail.pathList.size();i++){
//                String path=detail.pathList.get(i);
//                sbPath.append(path);
//                if(i!=detail.pathList.size()-1){
//                    sbPath.append("@@");
//                }
//            }
//            detailInSQLite.setPathList(sbPath.toString());
//        }
        return detailInSQLite;
    }

    /**
     * RepairDetailInSQLite转成RepairDetail类型
     */
    private RepairDetail detailChangeFromSQLite(RepairDetailInSQLite detailInSQLite){
        RepairDetail detail=new RepairDetail();
        detail.method=detailInSQLite.getMethod();
        detail.repairUserNo=detailInSQLite.getRepairUserNo();
        BaseData result=new BaseData();
        result.CODEID=detailInSQLite.getResultID();
        result.NAME=detailInSQLite.getResultName();
        detail.result=result;
        BaseData project=new BaseData();
        project.CODEID=detailInSQLite.getProjectID();
        project.NAME=detailInSQLite.getProjectName();
        detail.project=project;
        detail.remark=detailInSQLite.getRemark();
//        String [] pathArr=detailInSQLite.getPathList().split("@@");
//        ArrayList<String> pathList=new ArrayList<>();
//        for(int i=0;i<pathArr.length;i++) {
//            if(pathArr[i]==null||pathArr[i].isEmpty()) continue;
//            pathList.add(pathArr[i]);
//        }
//        detail.pathList=pathList;
        return detail;
    }

 //   @Override
//    protected void onUserLeaveHint() {
//           Log.e("TAG","onUserLeaveHint");
//           long time = SystemClock.elapsedRealtime() - timer.getBase();
//           repairHdrInSQLite.setRepairTime(time);
//           OthersUtil.getGreenDaoSession(getApplicationContext()).getRepairHdrInSQLiteDao().update(repairHdrInSQLite);
//
//        super.onUserLeaveHint();
//    }

    @Override
    protected void onDestroy() {
            //Log.e("TAG","onDestroy");
            if (timer.getVisibility()==View.VISIBLE){
                long time = SystemClock.elapsedRealtime() - timer.getBase();
                repairHdrInSQLite.setRepairTime(time);
                //repairHdrInSQLite
                OthersUtil.getGreenDaoSession(getApplicationContext()).getRepairHdrInSQLiteDao().update(repairHdrInSQLite);
            }
            super.onDestroy();
    }
}
