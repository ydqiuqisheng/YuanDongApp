package net.huansi.equipment.equipmentapp.activity.repair;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcel;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.BaseData;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.RepairDetail;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.gen.RepairBaseDataInSQLiteDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.sqlite_db.RepairBaseDataInSQLite;
import net.huansi.equipment.equipmentapp.util.NetUtil;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import rx.functions.Func1;

import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailAddActivityConstants.DETAIL_DATA_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailAddActivityConstants.DETAIL_DATA_POSITION_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.RepairDetailAddActivityConstants.RETURN_DATA_KEY;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by 单中年 on 2017/3/1.
 */

public class RepairDetailAddActivity extends BaseActivity {
    @BindView(R.id.spRepairDetailAddProject) Spinner spRepairDetailAddProject;//维护项目
    @BindView(R.id.etRepairDetailAddMethod) EditText etRepairDetailAddMethod;//维修方法
    @BindView(R.id.spRepairDetailAddResult) Spinner spRepairDetailAddResult;//维修结果
    @BindView(R.id.etRepairDetailRemark) EditText etRepairDetailRemark;//备注
//    @BindView(R.id.gvRepairDetailAdd) GridView gvRepairDetailAdd;//图片






    private RepairDetail repairDetail;

    private LoadProgressDialog dialog;

    private List<String> mProjectNameList;
    private List<BaseData> mProjectList;
    private List<String> mResultNameList;
    private List<BaseData> mResultList;

    private BaseData project;
    private BaseData result;


    private ArrayAdapter<String> mProjectAdapter;
    private ArrayAdapter<String> mResultAdapter;



//    btnRepairDetailAdd
    @Override
    protected int getLayoutId() {
        return R.layout.repair_detail_add_activity;
    }

    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        dialog=new LoadProgressDialog(this);
        //修改的数据
        repairDetail= (RepairDetail) getIntent().getSerializableExtra(DETAIL_DATA_PARAM);
        if(repairDetail==null) repairDetail=new RepairDetail();
        etRepairDetailRemark.setText(repairDetail.remark);
        etRepairDetailAddMethod.setText(repairDetail.method);
        mProjectList=new ArrayList<>();
        mResultList=new ArrayList<>();
        mProjectNameList=new ArrayList<>();
        mResultNameList=new ArrayList<>();
        mProjectAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mProjectNameList);
        mResultAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mResultNameList);
        spRepairDetailAddProject.setAdapter(mProjectAdapter);
        spRepairDetailAddResult.setAdapter(mResultAdapter);
        getBaseData();
    }

    /**
     * 获取维修项目
     */
    @SuppressWarnings("unchecked")
    private void getBaseData() {
        mProjectList.clear();
        mProjectNameList.clear();
        mResultList.clear();
        mResultNameList.clear();
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                //获取维护项目
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        HsWebInfo hsWebInfo;
                        Map<String, Object> data = new HashMap<>();
                        List<WsEntity> entities = null;
                        if (NetUtil.isNetworkAvailable(getApplicationContext())) {
                            hsWebInfo = NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                    "spAPPEquipmentQueryBaseData",
                                    "type=维护项目",
                                    BaseData.class.getName(),
                                    true, "未查到维护项目！！");
                            if (!hsWebInfo.success) return hsWebInfo;
                            entities = hsWebInfo.wsData.LISTWSDATA;
                        } else {
                            hsWebInfo = new HsWebInfo();
                            RepairBaseDataInSQLiteDao dao = OthersUtil.getGreenDaoSession(getApplicationContext()).getRepairBaseDataInSQLiteDao();
                            List<RepairBaseDataInSQLite> projectInSQLiteList = dao.queryBuilder()
                                    .where(RepairBaseDataInSQLiteDao.Properties.Type.eq(0))
                                    .list();
                            if (projectInSQLiteList == null || projectInSQLiteList.isEmpty()) {
                                hsWebInfo.success = false;
                                hsWebInfo.error.error = "未查到维修项目！！";
                                return hsWebInfo;
                            }
                            entities = new ArrayList<>();
                            for (RepairBaseDataInSQLite projectInSQLite : projectInSQLiteList) {
                                BaseData baseData = new BaseData();
                                baseData.CODEID = projectInSQLite.getCode();
                                baseData.NAME = projectInSQLite.getName();
                                entities.add(baseData);
                            }
                        }
                        data.put("projectList", entities);
                        hsWebInfo.object = data;
                        return hsWebInfo;
                    }
                })
                //获取维修结果
                .map(new Func1<HsWebInfo, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                        if (!hsWebInfo.success) return hsWebInfo;
                        Map<String, Object> data = (Map<String, Object>) hsWebInfo.object;
                        List<WsEntity> entities = null;
                        if (NetUtil.isNetworkAvailable(getApplicationContext())) {
                            hsWebInfo = NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                    "spAPPEquipmentQueryBaseData",
                                    "type=维修结果",
                                    BaseData.class.getName(),
                                    true, "未查到维修结果！！");
                            if (!hsWebInfo.success) return hsWebInfo;
                            entities = hsWebInfo.wsData.LISTWSDATA;
                        } else {
                            hsWebInfo = new HsWebInfo();
                            RepairBaseDataInSQLiteDao dao = OthersUtil.getGreenDaoSession(getApplicationContext()).getRepairBaseDataInSQLiteDao();
                            List<RepairBaseDataInSQLite> resultInSQLiteList = dao.queryBuilder()
                                    .where(RepairBaseDataInSQLiteDao.Properties.Type.eq(1))
                                    .list();
                            if (resultInSQLiteList == null || resultInSQLiteList.isEmpty()) {
                                hsWebInfo.success = false;
                                hsWebInfo.error.error = "未查到维修结果！！";
                                return hsWebInfo;
                            }
                            entities = new ArrayList<>();
                            for (RepairBaseDataInSQLite resultInSQLite : resultInSQLiteList) {
                                BaseData baseData = new BaseData();
                                baseData.CODEID = resultInSQLite.getCode();
                                baseData.NAME = resultInSQLite.getName();
                                entities.add(baseData);
                            }
                        }
                        data.put("resultList", entities);
                        hsWebInfo.object = data;
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                Map<String, Object> data = (Map<String, Object>) hsWebInfo.object;
                //维护项目
                List<WsEntity> projectList = (List<WsEntity>) data.get("projectList");
                for (int i = 0; i < projectList.size(); i++) {
                    BaseData baseData = (BaseData) projectList.get(i);
                    mProjectList.add(baseData);
                    mProjectNameList.add(baseData.NAME);
                }
                mProjectAdapter.notifyDataSetChanged();
                int projectShowPosition = -1;
                if (repairDetail.project != null) {
                    for (int i = 0; i < mProjectList.size(); i++) {
                        if (repairDetail.project.CODEID.equals(mProjectList.get(i).CODEID)) {
                            projectShowPosition = i;
                            break;
                        }
                    }
                }
                if (projectShowPosition != -1) {
                    spRepairDetailAddProject.setSelection(projectShowPosition, true);
                } else {
                    if (mProjectList.size() > 0) {
                        spRepairDetailAddProject.setSelection(0, true);
                    }
                }

                //维修结果
                List<WsEntity> resultList = (List<WsEntity>) data.get("resultList");
                for (int i = 0; i < resultList.size(); i++) {
                    BaseData baseData = (BaseData) resultList.get(i);
                    mResultList.add(baseData);
                    mResultNameList.add(baseData.NAME);
                }
                mResultAdapter.notifyDataSetChanged();
                int resultShowPosition = -1;
                if (repairDetail.result != null) {
                    for (int i = 0; i < mResultList.size(); i++) {
                        if (repairDetail.result.CODEID.equals(mResultList.get(i).CODEID)) {
                            resultShowPosition = i;
                            break;
                        }
                    }
                }
                if (resultShowPosition != -1) {
                    spRepairDetailAddResult.setSelection(resultShowPosition, true);
                } else {
                    if (mResultList.size() > 0) {
                        spRepairDetailAddResult.setSelection(0, true);
                    }
                }
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                OthersUtil.showTipsDialog(RepairDetailAddActivity.this, hsWebInfo.error.error);
            }
        });
    }
//        RxjavaWebUtils.requestByGetJsonData(this,
//                "spAPPEquipmentQueryBaseData",
//                "type=维护项目",
//                getApplicationContext(),
//                dialog,
//                BaseData.class.getName(),
//                true,
//                "未查到维修项目！！",
//                new SimpleHsWeb() {
//                    @Override
//                    public void error(HsWebInfo hsWebInfo) {
//                        super.error(hsWebInfo);
//                        mProjectAdapter.notifyDataSetChanged();
//                        getResultData();
//                    }
//
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
//                        for (int i = 0; i < entities.size(); i++) {
//                            BaseData data = (BaseData) entities.get(i);
//                            mProjectList.add(data);
//                            mProjectNameList.add(data.NAME);
//                        }
//                        mProjectAdapter.notifyDataSetChanged();
//                        int showPosition=-1;
//                        if (repairDetail.project != null ) {
//                            for (int i = 0; i < mProjectList.size(); i++) {
//                                if(repairDetail.project.CODE.equals(mProjectList.get(i).CODE)){
//                                    showPosition=i;
//                                    break;
//                                }
//                            }
//                        }
//                        if(showPosition!=-1) {
//                            spRepairDetailAddProject.setSelection(showPosition,true);
//                        }else {
//                            if(mProjectList.size()>0){
//                                spRepairDetailAddProject.setSelection(0,true);
//                            }
//                        }
//
//                        getResultData();
//                    }
//                });
//    }
//
//    /**
//     * 获得维修结果的数据
//     */
//    private void getResultData(){
//        mResultList.clear();
//        RxjavaWebUtils.requestByGetJsonData(this,
//                "spAPPEquipmentQueryBaseData",
//                "type=维修结果",
//                getApplicationContext(),
//                dialog,
//                BaseData.class.getName(),
//                true,
//                "未查到维修结果！！",
//                new SimpleHsWeb() {
//                    @Override
//                    public void error(HsWebInfo hsWebInfo) {
//                        super.error(hsWebInfo);
//                        mProjectAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        OthersUtil.dismissLoadDialog(dialog);
//                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
//                        for(int i=0;i<entities.size();i++){
//                            BaseData data= (BaseData) entities.get(i);
//                            mResultList.add(data);
//                            mResultNameList.add(data.NAME);
//                        }
//                        mResultAdapter.notifyDataSetChanged();
//                        int showPosition=-1;
//                        if (repairDetail.result != null ) {
//                            for (int i = 0; i < mResultList.size(); i++) {
//                                if(repairDetail.result.CODE.equals(mResultList.get(i).CODE)){
//                                    showPosition=i;
//                                    break;
//                                }
//                            }
//                        }
//                        if(showPosition!=-1) {
//                            spRepairDetailAddResult.setSelection(showPosition,true);
//                        }else {
//                            if(mResultList.size()>0){
//                                spRepairDetailAddResult.setSelection(0,true);
//                            }
//                        }
//
//                    }
//                });
//    }

    @OnItemSelected(R.id.spRepairDetailAddProject)
    void showProject(int position){
        project=mProjectList.get(position);
    }


    @OnItemSelected(R.id.spRepairDetailAddResult)
    void showResult(int position){
        result=mResultList.get(position);
    }


    @Override
    public void back() {
        OthersUtil.showChooseDialog(this, "您还未保存这个明细数据，是否返回？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    /**
     * 保存
     */
    @OnClick(R.id.btnRepairDetailAddSave)
    void save(){
        String method=etRepairDetailAddMethod.getText().toString().trim();
        if(project==null){
            OthersUtil.showTipsDialog(RepairDetailAddActivity.this,"请选择要维护的项目");
            return;
        }
        if(result==null){
            OthersUtil.showTipsDialog(RepairDetailAddActivity.this,"请选择维修的结果");
            return;
        }
        Intent intent=new Intent();
        repairDetail.method=method;
        repairDetail.project=project;
        repairDetail.result=result;
        repairDetail.remark=etRepairDetailRemark.getText().toString().trim();
        repairDetail.repairUserNo= SPHelper.getLocalData(getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString();
        intent.putExtra(RETURN_DATA_KEY,repairDetail);
        intent.putExtra(DETAIL_DATA_POSITION_PARAM,getIntent().getIntExtra(DETAIL_DATA_POSITION_PARAM,-1));
        setResult(RESULT_OK,intent);
        finish();
    }

}
