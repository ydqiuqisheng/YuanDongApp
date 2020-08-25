package net.huansi.equipment.equipmentapp.activity.send_card;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.BindCardMainAdapter;
import net.huansi.equipment.equipmentapp.adapter.HsArrayAdapter;
import net.huansi.equipment.equipmentapp.entity.EpWithRFID;
import net.huansi.equipment.equipmentapp.entity.EpWithoutRFID;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.event.RFIDActionEvent;
import net.huansi.equipment.equipmentapp.gen.EquipmentWithoutRFIDInSQLiteDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.sqlite_db.EquipmentWithoutRFIDInSQLite;
import net.huansi.equipment.equipmentapp.util.NetUtil;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.EQUIPMENT_INFO_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.POSITION_IN_ALL_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.POSITION_IN_SHOW_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.RETURN_DATA_KEY;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardDetailActivityConstants.RFID_HAS_BIND;
import static net.huansi.equipment.equipmentapp.constant.Constant.BindCardMainActivityConstants.BIND_CARD_QUEST_CODE;
import static net.huansi.equipment.equipmentapp.event.RFIDActionEvent.DELETE_INDEX;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by shanz on 2017/3/5.
 */

public class BindCardMainActivity extends BaseActivity {
    @BindView(R.id.lvBindCardMain)ListView lvBindCardMain;
    @BindView(R.id.cbBindCardMainShowBindEP)CheckBox cbBindCardMainShowBindEP;//显示已经绑定的设备的多选框
    @BindView(R.id.etBindCardMainEquipmentName)AutoCompleteTextView etBindCardMainEquipmentName;//设备名称
    @BindView(R.id.etBindCardMainBrand)AutoCompleteTextView etBindCardMainBrand;//品牌
    @BindView(R.id.etBindCardMainModel)AutoCompleteTextView etBindCardMainModel;//型号

    @BindView(R.id.etBindCardMainAssetsCode) EditText etBindCardMainAssetsCode;//资产编号/出产编号的输入框

    @BindView(R.id.bindCardMainShowBindEPLayout) LinearLayout bindCardMainShowBindEPLayout;//是否显示绑定的设备信息
    @BindView(R.id.bindCardMainFilterLayout) LinearLayout bindCardMainFilterLayout;//筛选设备的layout

    @BindView(R.id.cbBindCardMainQueryInAll) CheckBox cbBindCardMainQueryInAll;// true 在全部中筛选 false 在筛选情况下再次筛选

//    @BindView(R.id.btnBindCardMainSearchSubmit)
//    Button btnBindCardMainSearchSubmit;
    private boolean isShowBindEP;//是否显示绑定的设备
    private BindCardMainAdapter mAdapter;
    private List<EquipmentWithoutRFIDInSQLite> mList;
    private List<EquipmentWithoutRFIDInSQLite> mShowList;
    private LoadProgressDialog dialog;


    private List<String> mEquipmentNameList;
    private List<String> mBrandList;
    private List<String> mModelList;

    private HsArrayAdapter<String> mEquipmentAdapter;
    private HsArrayAdapter<String> mBrandAdapter;
    private HsArrayAdapter<String> mModelAdapter;

    private HashMap<String,String> RFIDMap;//保存已绑定的RFID卡
    private HashMap<String,Integer> equipmentMap;//总的数据的一个Map


    @Override
    protected int getLayoutId() {
        return R.layout.bind_card_main_activity;
    }



    @Override
    public void init() {
        OthersUtil.registerEvent(this);
        mShowList = new ArrayList<>();
        OthersUtil.hideInputFirst(this);
        TextView tvOperate = getSubTitle();
        setToolBarTitle("发卡");
        tvOperate.setText("操作");
        tvOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BindCardMainActivity.this);
                builder.setItems(new String[]{"下载需绑定RFID的设备", "清空数据", "上传数据","查找多绑"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                OthersUtil.showChooseDialog(BindCardMainActivity.this, "确认下载需要绑定RFID的设备？",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                downEquipmentWithoutRFID();
                                            }
                                        });
                                break;
                            case 1:
                                OthersUtil.showChooseDialog(BindCardMainActivity.this, "确认清空数据？",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                clearData();
                                            }
                                        });
                                break;
                            case 2:
                                OthersUtil.showChooseDialog(BindCardMainActivity.this, "确认上传数据？",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                submitData();
                                            }
                                        });
                                break;
                            case 3:
                                Intent intent=new Intent(BindCardMainActivity.this,BindMoreCardActivity.class);
                                startActivity(intent);
                                break;

                        }
                    }
                })
                        .show();
            }
        });
        dialog = new LoadProgressDialog(this);
        mList = new ArrayList<>();
        equipmentMap = new HashMap<>();
        mEquipmentNameList = new ArrayList<>();
        mBrandList = new ArrayList<>();
        mModelList = new ArrayList<>();
        RFIDMap = new HashMap<>();

        mAdapter = new BindCardMainAdapter(mShowList, getApplicationContext());
        lvBindCardMain.setAdapter(mAdapter);
        filter();
        etBindCardMainAssetsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showEPDetail();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        OthersUtil.showLoadDialog(dialog);
        initEquipment();
    }


    /**
     * 服务器中删除一个RFID，更新这里面的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateByRFIDAction(RFIDActionEvent event){
        switch (event.index){
            case DELETE_INDEX:
                if(mList.isEmpty()) return;
                EpWithRFID epWithRFID= (EpWithRFID) event.object;
                EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite=new EquipmentWithoutRFIDInSQLite();
                equipmentWithoutRFIDInSQLite.setAssetsCode(epWithRFID.ASSETSCODE);
                equipmentWithoutRFIDInSQLite.setBrand(epWithRFID.BRAND);
                equipmentWithoutRFIDInSQLite.setBUsable(1);
                equipmentWithoutRFIDInSQLite.setCostCenter(epWithRFID.COSTCENTER);
                equipmentWithoutRFIDInSQLite.setDeclarationNum(epWithRFID.DECLARATIONNUM);
                equipmentWithoutRFIDInSQLite.setDepreciationStartingDate(epWithRFID.DEPRECIATIONSTARTINGDATE);
                equipmentWithoutRFIDInSQLite.setEquipmentDetailID(epWithRFID.EQUIPMENTDETAILID);
                equipmentWithoutRFIDInSQLite.setEquipmentName(epWithRFID.EQUIPMENTNAME);
                equipmentWithoutRFIDInSQLite.setInFactoryDate(epWithRFID.INFACTORYDATE);
                equipmentWithoutRFIDInSQLite.setIsBindRFID(0);
                equipmentWithoutRFIDInSQLite.setModel(epWithRFID.MODEL);
                equipmentWithoutRFIDInSQLite.setOutFactoryCode(epWithRFID.OUTFACTORYCODE);
                Observable.just(equipmentWithoutRFIDInSQLite)
                        .compose(this.<EquipmentWithoutRFIDInSQLite>bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<EquipmentWithoutRFIDInSQLite, EquipmentWithoutRFIDInSQLite>() {
                            @Override
                            public EquipmentWithoutRFIDInSQLite call(EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite) {
                                EquipmentWithoutRFIDInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getEquipmentWithoutRFIDInSQLiteDao();
                                long id=dao.insertOrReplace(equipmentWithoutRFIDInSQLite);
                                equipmentWithoutRFIDInSQLite=dao.load(id);
                                return equipmentWithoutRFIDInSQLite;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<EquipmentWithoutRFIDInSQLite>() {
                            @Override
                            public void call(EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite) {
                                mList.add(equipmentWithoutRFIDInSQLite);
                                showEPDetail();
                            }
                        });
                break;
        }
    }


    /**
     * 筛选
     */
    private void filter(){
        etBindCardMainEquipmentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etBindCardMainEquipmentName.showDropDown();
            }
        });
        etBindCardMainBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etBindCardMainBrand.showDropDown();
            }
        });

        etBindCardMainModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etBindCardMainModel.showDropDown();
            }
        });
        etBindCardMainEquipmentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showEPDetail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etBindCardMainBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showEPDetail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etBindCardMainModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showEPDetail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
//

    @OnCheckedChanged(R.id.cbBindCardMainQueryInAll)
    void showAllOrFilter(boolean isChecked){
        showEPDetail();
    }

    /**
     * 查看已上传设备的信息
     */
    @OnClick(R.id.btnBindCardMainSearchSubmit)
    void searchEPSubmit(){
        Intent intent=new Intent(this,BindCardQueryEquipmentSubmitActivity.class);
        intent.putExtra(RFID_HAS_BIND,RFIDMap);
        startActivity(intent);
    }

    /**
     * 长按删除这个RFID
     */
    @OnItemLongClick(R.id.lvBindCardMain)
    boolean deleteRFID(final int position){
        final EquipmentWithoutRFIDInSQLite  equipmentWithoutRFIDInSQLite=mShowList.get(position);
        //已绑定的提示他是否删除
        if(equipmentWithoutRFIDInSQLite.getIsBindRFID()==1){
            OthersUtil.showChooseDialog(this, "是否取消这个绑定？",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                    OthersUtil.showLoadDialog(dialog);
                    Observable.just(equipmentWithoutRFIDInSQLite)
                            .subscribeOn(Schedulers.io())
                            .compose(BindCardMainActivity.this.<EquipmentWithoutRFIDInSQLite>bindToLifecycle())
                            .map(new Func1<EquipmentWithoutRFIDInSQLite, EquipmentWithoutRFIDInSQLite>() {
                                @Override
                                public EquipmentWithoutRFIDInSQLite call(EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite) {
                                    EquipmentWithoutRFIDInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getEquipmentWithoutRFIDInSQLiteDao();
                                    equipmentWithoutRFIDInSQLite.setRFID("");
                                    equipmentWithoutRFIDInSQLite.setIsBindRFID(0);
                                    dao.insertOrReplaceInTx(equipmentWithoutRFIDInSQLite);
                                    return equipmentWithoutRFIDInSQLite;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<EquipmentWithoutRFIDInSQLite>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    OthersUtil.dismissLoadDialog(dialog);
                                    OthersUtil.showTipsDialog(BindCardMainActivity.this,"取消绑定失败！！");
                                }

                                @Override
                                public void onNext(EquipmentWithoutRFIDInSQLite afterEP) {
                                    OthersUtil.dismissLoadDialog(dialog);
                                    Integer totalPosition = equipmentMap.get(equipmentWithoutRFIDInSQLite.getAssetsCode());
                                    if (totalPosition == null||totalPosition>mList.size()-1) return;
                                    mList.set(totalPosition,afterEP);
                                    mShowList.set(position,afterEP);
                                    RFIDMap.remove(equipmentWithoutRFIDInSQLite.getRFID());
                                    mAdapter.notifyDataSetChanged();
                                }

                            });
                }
            });
        }
        return true;
    }


//    /**
//     * 初始化三层联动的数据
//     */
//    private void initThreeLayersData(){
//        Map<String,List<EquipmentWithoutRFIDInSQLite>> equipmentNameMap=new HashMap<>();
//
//        for(int i=0;i<mList.size();i++) {
//            EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite = mList.get(i);
//            List<EquipmentWithoutRFIDInSQLite> brandList = equipmentNameMap.get(equipmentWithoutRFIDInSQLite.getEquipmentName());
//            if (brandList == null) brandList = new ArrayList<>();
//            brandList.add(equipmentWithoutRFIDInSQLite);
//            equipmentNameMap.put(equipmentWithoutRFIDInSQLite.getEquipmentName(),brandList);
//        }
//        Iterator<Map.Entry<String,List<EquipmentWithoutRFIDInSQLite>>> itEquipmentName=equipmentNameMap.entrySet().iterator();
//        List<List<EquipmentWithoutRFIDInSQLite>> brandList=new ArrayList<>();
//        while (itEquipmentName.hasNext()){
//            Map.Entry<String,List<EquipmentWithoutRFIDInSQLite>> entry=itEquipmentName.next();
//            mEquipmentNameList.add(entry.getKey());
//            brandList.add(entry.getValue());
//        }
//
//
//
//        List<List<List<EquipmentWithoutRFIDInSQLite>>> modelList=new ArrayList<>();
//        for(int i=0;i<brandList.size();i++){
//            List<EquipmentWithoutRFIDInSQLite> list=brandList.get(i);
//            List<String> brandNameList=new ArrayList<>();
//            List<List<EquipmentWithoutRFIDInSQLite>> brandEPList=new ArrayList<>();
//            Map<String,List<EquipmentWithoutRFIDInSQLite>> map=new HashMap<>();
//            for(int j=0;j<list.size();j++){
//                EquipmentWithoutRFIDInSQLite e=list.get(j);
//                List<EquipmentWithoutRFIDInSQLite> subList=map.get(e.getBrand());
//                if(subList==null) subList=new ArrayList<>();
//                subList.add(e);
//                map.put(e.getBrand(),subList);
//            }
//            Iterator<Map.Entry<String,List<EquipmentWithoutRFIDInSQLite>>> itBrand=map.entrySet().iterator();
//            while (itBrand.hasNext()){
//                Map.Entry<String,List<EquipmentWithoutRFIDInSQLite>> entry=itBrand.next();
//                brandNameList.add(entry.getKey());
//                brandEPList.add(entry.getValue());
//            }
//            modelList.add(brandEPList);
//            brandDataList.add(brandNameList);
//        }
//
//
//        for(int i=0;i<modelList.size();i++){
//            List<List<EquipmentWithoutRFIDInSQLite>> secondList=modelList.get(i);
//
//            List<List<String>> modelNameList=new ArrayList<>();
//            for(int j=0;j<secondList.size();j++){
//                Map<String,List<EquipmentWithoutRFIDInSQLite>> map=new HashMap<>();
//                List<EquipmentWithoutRFIDInSQLite> thirdList=secondList.get(j);
//                List<String> subModelNameList=new ArrayList<>();
//                for(int k=0;k<thirdList.size();k++){
//                    EquipmentWithoutRFIDInSQLite e=thirdList.get(k);
//                    List<EquipmentWithoutRFIDInSQLite> l=map.get(e.getModel());
//                    if(l==null) l=new ArrayList<>();
//                    l.add(e);
//                    map.put(e.getModel(),l);
//                }
//                Iterator<Map.Entry<String,List<EquipmentWithoutRFIDInSQLite>>> itModel=map.entrySet().iterator();
//                while (itModel.hasNext()){
//                    Map.Entry<String,List<EquipmentWithoutRFIDInSQLite>> entry=itModel.next();
//                    subModelNameList.add(entry.getKey());
//                }
//                modelNameList.add(subModelNameList);
//            }
//            modelDataList.add(modelNameList);
//        }
//
//    }


    /**
     * 初始化筛选的数据
     */
    private void initFilterPopData(){
        mEquipmentNameList.clear();
        mBrandList.clear();
        mModelList.clear();

        Map<String,String> equipmentNameMap=new HashMap<>();
        Map<String,String> brandMap=new HashMap<>();
        Map<String,String> modelMap=new HashMap<>();



        for(int i=0;i<mList.size();i++){
            EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite=mList.get(i);
            equipmentNameMap.put(equipmentWithoutRFIDInSQLite.getEquipmentName(),
                    equipmentWithoutRFIDInSQLite.getEquipmentName());
            brandMap.put(equipmentWithoutRFIDInSQLite.getBrand(),
                    equipmentWithoutRFIDInSQLite.getBrand());
            modelMap.put(equipmentWithoutRFIDInSQLite.getModel(),
                    equipmentWithoutRFIDInSQLite.getModel());
        }

        Iterator<Map.Entry<String,String>> equipmentIt=equipmentNameMap.entrySet().iterator();
        Iterator<Map.Entry<String,String>> brandIt=brandMap.entrySet().iterator();
        Iterator<Map.Entry<String,String>> modelIt=modelMap.entrySet().iterator();

        while (equipmentIt.hasNext()){
            mEquipmentNameList.add(equipmentIt.next().getKey());
        }
        while (brandIt.hasNext()){
            mBrandList.add(brandIt.next().getKey());
        }
        while (modelIt.hasNext()){
            mModelList.add(modelIt.next().getKey());

        }

        mEquipmentAdapter=new HsArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mEquipmentNameList);
        mBrandAdapter=new HsArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mBrandList);
        mModelAdapter=new HsArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mModelList);

        etBindCardMainEquipmentName.setAdapter(mEquipmentAdapter);
        etBindCardMainBrand.setAdapter(mBrandAdapter);
        etBindCardMainModel.setAdapter(mModelAdapter);
    }


    @OnClick(R.id.bindCardMainShowBindEPLayout)
    void showBindEP(){
        isShowBindEP=!isShowBindEP;
        cbBindCardMainShowBindEP.setChecked(isShowBindEP);
        showEPDetail();
    }


//    /**
//     * 根据设备名显示信息
//     * @param position
//     */
//    @OnItemSelected(R.id.spBindCardMainEquipmentAssetsCode)
//    void showByAssetsCode(int position){
//        assetsCode=mAssetsCodeList.get(position);
//        showBindEPDetail();
//    }

    /**
     * 具体的显示设备
     */
    private synchronized void showEPDetail(){
        if(cbBindCardMainQueryInAll.isChecked()){
            showEPDetailByInAll();
        }else {
            showEPDetailByInFilter();
        }
    }

    /**
     * 全部中进行筛选资产编号
     */
    private void showEPDetailByInAll(){
        String assetsCode=etBindCardMainAssetsCode.getText().toString().trim();
        mShowList.clear();
        for(int i=0;i<mList.size();i++){
            EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite=mList.get(i);
            if(equipmentWithoutRFIDInSQLite.getAssetsCode().toLowerCase().contains(assetsCode.toLowerCase())||
                    equipmentWithoutRFIDInSQLite.getOutFactoryCode().toLowerCase().contains(assetsCode.toLowerCase())){
                //显示已绑定的设备
                if(isShowBindEP){
                    mShowList.add(equipmentWithoutRFIDInSQLite);
                }else {
                    if(equipmentWithoutRFIDInSQLite.getIsBindRFID()==0){
                        mShowList.add(equipmentWithoutRFIDInSQLite);
                    }
                }

            }
        }
        sortList();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 筛选中进行再次根据资产编号进行显示
     */
    private void showEPDetailByInFilter(){
        mShowList.clear();
        String equipmentName=etBindCardMainEquipmentName.getText().toString().trim().toLowerCase();
        String brand=etBindCardMainBrand.getText().toString().trim().toLowerCase();
        String model=etBindCardMainModel.getText().toString().trim().toLowerCase();
        String assetsCode=etBindCardMainAssetsCode.getText().toString().trim().toLowerCase();

        for(int i=0;i<mList.size();i++){
            EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite=mList.get(i);
            if(equipmentWithoutRFIDInSQLite.getEquipmentName().toLowerCase().contains(equipmentName)&&
                    equipmentWithoutRFIDInSQLite.getBrand().toLowerCase().contains(brand)&&
                    equipmentWithoutRFIDInSQLite.getModel().toLowerCase().contains(model)&&
                    (equipmentWithoutRFIDInSQLite.getAssetsCode().toLowerCase().contains(assetsCode)||
                            equipmentWithoutRFIDInSQLite.getOutFactoryCode().toLowerCase().contains(assetsCode.toLowerCase()))){
                //显示已绑定的设备
                if(isShowBindEP){
                    mShowList.add(equipmentWithoutRFIDInSQLite);
                }else {
                    if(equipmentWithoutRFIDInSQLite.getIsBindRFID()==0){
                        mShowList.add(equipmentWithoutRFIDInSQLite);
                    }
                }
            }
        }
        sortList();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 排序列表
     */
    private void sortList(){
        Collections.sort(mShowList, new Comparator<EquipmentWithoutRFIDInSQLite>() {
            @Override
            public int compare(EquipmentWithoutRFIDInSQLite o1, EquipmentWithoutRFIDInSQLite o2) {
                if(((o1.getRFID()==null||o1.getRFID().trim().isEmpty())&&
                        (o2.getRFID()==null||o2.getRFID().trim().isEmpty()))||
                        ((o1.getRFID()!=null&&!o1.getRFID().trim().isEmpty())&&
                                (o2.getRFID()!=null&&!o2.getRFID().trim().isEmpty()))) return 0;
                if(o1.getRFID()==null||o1.getRFID().trim().isEmpty()) return 1;
                if(o2.getRFID()==null||o2.getRFID().trim().isEmpty()) return -1;
                return 0;
            }
        });
    }



    /**
     * 初始化信息
     */
    private void initEquipment(){
        mList.clear();
        mShowList.clear();
        Observable.just("")
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<EquipmentWithoutRFIDInSQLite>>() {
                    @Override
                    public List<EquipmentWithoutRFIDInSQLite> call(String s) {
                        EquipmentWithoutRFIDInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext())
                                .getEquipmentWithoutRFIDInSQLiteDao();
                        Query<EquipmentWithoutRFIDInSQLite>query= dao.queryBuilder().where(EquipmentWithoutRFIDInSQLiteDao.Properties.BUsable.eq(1))
                                .build();
                        if(query==null) return null;
                        return  query.list();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<EquipmentWithoutRFIDInSQLite>>() {
                    @Override
                    public void call(List<EquipmentWithoutRFIDInSQLite> equipmentWithoutRFIDInSQLites) {

                        if(equipmentWithoutRFIDInSQLites==null) {
                            mAdapter.notifyDataSetChanged();
                            return;
                        }
                        mList.addAll(equipmentWithoutRFIDInSQLites);
                        for(int i=0;i<mList.size();i++){
                            equipmentMap.put(mList.get(i).getAssetsCode(),i);
                        }

                        if(mList.isEmpty()){
                            bindCardMainFilterLayout.setVisibility(View.GONE);
                            bindCardMainShowBindEPLayout.setVisibility(View.GONE);
                        }else {
                            bindCardMainFilterLayout.setVisibility(View.VISIBLE);
                            bindCardMainShowBindEPLayout.setVisibility(View.VISIBLE);
                        }
                        initFilterPopData();
                        showEPDetail();
                        saveRFIDScanned();
                        OthersUtil.dismissLoadDialog(dialog);

                    }
                });
    }

    /**
     * 下载未绑定RFID卡设备
     */
    private void downEquipmentWithoutRFID(){
        Observable.just("")
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        EquipmentWithoutRFIDInSQLiteDao dao=OthersUtil.getGreenDaoSession(getApplicationContext())
                                .getEquipmentWithoutRFIDInSQLiteDao();
                        Query<EquipmentWithoutRFIDInSQLite> query=dao.queryBuilder()
                                .where(EquipmentWithoutRFIDInSQLiteDao.Properties.BUsable.eq(1))
                                .build();
                        if(query==null) return false;
                        List<EquipmentWithoutRFIDInSQLite> list=query.list();
                        return list!=null&&!list.isEmpty();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(aBoolean){
                            OthersUtil.showTipsDialog(BindCardMainActivity.this,"您本地还有未上传的数据，请检查！！");
                            return;
                        }
                        downEquipmentWithoutRFIDFromServer();
                    }
                });
    }

    /**
     * 服务器下载数据
     */
    private void downEquipmentWithoutRFIDFromServer(){
        OthersUtil.showLoadDialog(dialog);
        RxjavaWebUtils.requestByGetJsonData(this,
                "spAppEPDownEquipmentWithoutRFID",
                "",
                getApplicationContext(),
                dialog,
                EpWithoutRFID.class.getName(),
                true,
                "服务器没有设备需要绑定RFID卡",
                new SimpleHsWeb() {
                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        super.error(hsWebInfo);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                        List<EpWithoutRFID> list=new ArrayList<>();
                        for(int i=0;i<entities.size();i++){
                            list.add((EpWithoutRFID) entities.get(i));
                        }
                        saveToSQLite(list);
                    }
                });
    }

    /**
     * 保存到本地
     */
    private void saveToSQLite(List<EpWithoutRFID> list) {
        Observable.just(list)
                .compose(this.<List<EpWithoutRFID>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<EpWithoutRFID>, List<EquipmentWithoutRFIDInSQLite>>() {
                    @Override
                    public List<EquipmentWithoutRFIDInSQLite> call(List<EpWithoutRFID> epWithoutRFIDs) {
                        try {
                            EquipmentWithoutRFIDInSQLiteDao dao = OthersUtil.getGreenDaoSession(getApplicationContext())
                                    .getEquipmentWithoutRFIDInSQLiteDao();
                            for (EpWithoutRFID epWithoutRFID : epWithoutRFIDs) {
                                EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite = new EquipmentWithoutRFIDInSQLite();
                                equipmentWithoutRFIDInSQLite.setEquipmentDetailID(epWithoutRFID.EQUIPMENTDETAILID);

                                equipmentWithoutRFIDInSQLite.setDepreciationStartingDate(epWithoutRFID.DEPRECIATIONSTARTINGDATE);
                                equipmentWithoutRFIDInSQLite.setInFactoryDate(epWithoutRFID.INFACTORYDATE);
                                equipmentWithoutRFIDInSQLite.setAssetsCode(epWithoutRFID.ASSETSCODE);
                                equipmentWithoutRFIDInSQLite.setOutFactoryCode(epWithoutRFID.OUTFACTORYCODE);
                                equipmentWithoutRFIDInSQLite.setCostCenter(epWithoutRFID.COSTCENTER);
                                equipmentWithoutRFIDInSQLite.setDeclarationNum(epWithoutRFID.DECLARATIONNUM);

                                equipmentWithoutRFIDInSQLite.setEquipmentName(epWithoutRFID.EQUIPMENTNAME);
                                equipmentWithoutRFIDInSQLite.setBrand(epWithoutRFID.BRAND);
                                equipmentWithoutRFIDInSQLite.setModel(epWithoutRFID.MODEL);
                                equipmentWithoutRFIDInSQLite.setIsBindRFID(0);
                                equipmentWithoutRFIDInSQLite.setBUsable(1);

                                dao.insertOrReplaceInTx(equipmentWithoutRFIDInSQLite);
                            }
                            Query<EquipmentWithoutRFIDInSQLite> query = dao.queryBuilder()
                                    .where(EquipmentWithoutRFIDInSQLiteDao.Properties.BUsable.eq(1))
                                    .build();
                            if (query == null) return null;
                            return query.list();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<EquipmentWithoutRFIDInSQLite>>() {
                    @Override
                    public void call(List<EquipmentWithoutRFIDInSQLite> list) {
                        if (list != null && list.size() > 0) {
                            mList.addAll(list);
                            for(int i=0;i<mList.size();i++){
                                equipmentMap.put(mList.get(i).getAssetsCode(),i);
                            }
                            showEPDetail();
                            bindCardMainShowBindEPLayout.setVisibility(View.VISIBLE);
                            bindCardMainFilterLayout.setVisibility(View.VISIBLE);
                            initFilterPopData();
                            showEPDetail();
                            OthersUtil.ToastMsg(getApplicationContext(), "下载成功！！");
                            OthersUtil.dismissLoadDialog(dialog);
                            return;
                        }
                        OthersUtil.dismissLoadDialog(dialog);
                        OthersUtil.showTipsDialog(BindCardMainActivity.this, "下载失败！！");

                    }
                });
    }

    /**
     * 保存已经绑定的RFID卡
     */
    private void saveRFIDScanned(){
        RFIDMap.clear();
        for(int i=0;i<mList.size();i++){
            EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite=mList.get(i);
            String RFIDInfo=equipmentWithoutRFIDInSQLite.getRFID();
            RFIDMap.put(RFIDInfo==null?"":RFIDInfo,RFIDInfo);
        }

    }

    /**
     * 清空数据
     */
    private void clearData(){
        OthersUtil.showLoadDialog(dialog);
        Observable.just("")
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                       try{
                           EquipmentWithoutRFIDInSQLiteDao dao= OthersUtil.getGreenDaoSession(getApplicationContext())
                                   .getEquipmentWithoutRFIDInSQLiteDao();
                           Query<EquipmentWithoutRFIDInSQLite> query=dao.queryBuilder().build();
                           if(query==null) return false;
                           List<EquipmentWithoutRFIDInSQLite> list=query.list();
                           for(int i=0;i<list.size();i++){
                               EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite=list.get(i);
                               equipmentWithoutRFIDInSQLite.setBUsable(0);
                               list.set(i,equipmentWithoutRFIDInSQLite);
                           }
                           dao.insertOrReplaceInTx(list);
                           return true;
                       }catch (Exception e){}
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        OthersUtil.dismissLoadDialog(dialog);
                        if(aBoolean){
                            OthersUtil.ToastMsg(getApplicationContext(),"清空数据成功！！");
                            mList.clear();
                            mShowList.clear();
                            isShowBindEP=false;
                            cbBindCardMainShowBindEP.setChecked(false);
                            cbBindCardMainQueryInAll.setChecked(false);
                            bindCardMainFilterLayout.setVisibility(View.GONE);
                            bindCardMainShowBindEPLayout.setVisibility(View.GONE);
                            mEquipmentNameList.clear();
                            mModelList.clear();
                            mBrandList.clear();
                            RFIDMap.clear();
                            etBindCardMainAssetsCode.getText().clear();
                            mBrandAdapter.notifyDataSetChanged();
                            mModelAdapter.notifyDataSetChanged();
                            mEquipmentAdapter.notifyDataSetChanged();
                            mAdapter.notifyDataSetChanged();
                        }else {
                            OthersUtil.showTipsDialog(BindCardMainActivity.this,"清空数据失败！！");
                        }
                    }
                });
    }

    /**
     * 上传数据
     */
    private void submitData(){
        boolean isScan=false;
        for(int i=0;i<mList.size();i++){
            EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite=mList.get(i);
            if(equipmentWithoutRFIDInSQLite.getIsBindRFID()==1){
                isScan=true;
                break;
            }
        }
        if(!isScan){
            OthersUtil.showTipsDialog(this,"您还未绑定任何设备，请检查！！");
            return;
        }
        OthersUtil.showLoadDialog(dialog);
        Observable.just("")
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        Query<EquipmentWithoutRFIDInSQLite> query=OthersUtil.getGreenDaoSession(getApplicationContext())
                                .getEquipmentWithoutRFIDInSQLiteDao()
                                .queryBuilder()
                                .where(EquipmentWithoutRFIDInSQLiteDao.Properties.BUsable.eq(1))
                                .build();
                        if(!NetUtil.isNetworkAvailable(getApplicationContext())){
                            HsWebInfo hsWebInfo=new HsWebInfo();
                            hsWebInfo.success=false;
                            hsWebInfo.error.error=getResources().getString(R.string.net_no_active);
                            return hsWebInfo;
                        }
                        if(query==null) {
                            HsWebInfo hsWebInfo=new HsWebInfo();
                            hsWebInfo.success=false;
                            hsWebInfo.error.error="上传失败！！";
                            return hsWebInfo;
                        }
                        List<EquipmentWithoutRFIDInSQLite> list=query.list();
                        if(list==null||list.isEmpty()) {
                            HsWebInfo hsWebInfo=new HsWebInfo();
                            hsWebInfo.success=false;
                            hsWebInfo.error.error="上传失败！！";
                            return hsWebInfo;
                        }
                        StringBuilder sbData=new StringBuilder();
                        for(int i=0;i<list.size();i++) {
                            EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite = list.get(i);
                            if (equipmentWithoutRFIDInSQLite.getIsBindRFID() == 0 ||
                                    equipmentWithoutRFIDInSQLite.getRFID() == null ||
                                    equipmentWithoutRFIDInSQLite.getRFID().trim().isEmpty())
                                continue;
                            sbData.append("exec spAppEPBindRFIDCard ");
                            sbData.append("@uEquipmentDetailID='" + equipmentWithoutRFIDInSQLite.getEquipmentDetailID()+"'"+
                                    ",@sRFID='" + equipmentWithoutRFIDInSQLite.getRFID()+"'"+
                                    ",@sUserNo='" + SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString() + "' ;");
//                            HsWebInfo hsWebInfo = RxjavaWebUtils.getJsonData(getApplicationContext(),
//                                    "spAppEPBindRFIDCard",
//                                    "uEquipmentDetailID=" + equipmentWithoutRFIDInSQLite.getEquipmentDetailID() +
//                                            ",sRFID=" +
//                                            (equipmentWithoutRFIDInSQLite.getRFID() == null ? "" : equipmentWithoutRFIDInSQLite.getRFID()) +
//                                            ",sUserNo=" + SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString(),
//                                    WsData.class.getName(),
//                                    false, "上传失败！！");
//                            if (!hsWebInfo.success) return hsWebInfo;
                        }
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),sbData.toString(),"",
                                WsData.class.getName(),false,"上传失败！！");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HsWebInfo>() {
                    @Override
                    public void call(HsWebInfo hsWebInfo) {
                        if(hsWebInfo.success){
                            OthersUtil.ToastMsg(getApplicationContext(),"上传成功！！");
                            clearData();
                        }else {
                            OthersUtil.dismissLoadDialog(dialog);
                            OthersUtil.showTipsDialog(BindCardMainActivity.this,
                                    hsWebInfo.error.error==null||hsWebInfo.error.error.isEmpty()?"上传失败！！":hsWebInfo.error.error);
                        }
                    }
                });
    }


    @OnItemClick(R.id.lvBindCardMain)
    void bindCard(int position) {
        EquipmentWithoutRFIDInSQLite equipmentWithoutRFIDInSQLite = mShowList.get(position);
        Intent intent = new Intent(this, BindCardDetailActivity.class);
        intent.putExtra(EQUIPMENT_INFO_PARAM, equipmentWithoutRFIDInSQLite);
        intent.putExtra(RFID_HAS_BIND, RFIDMap);
        intent.putExtra(POSITION_IN_SHOW_PARAM, position);
        intent.putExtra(POSITION_IN_ALL_PARAM, equipmentMap.get(equipmentWithoutRFIDInSQLite.getAssetsCode()));
        startActivityForResult(intent, BIND_CARD_QUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;
        switch (requestCode){
            //绑定卡
            case BIND_CARD_QUEST_CODE:
                bindCard(data);
                break;
        }
    }

    /**
     * 绑定卡
     */
    private void bindCard(Intent data){
        if(data==null) return;
        EquipmentWithoutRFIDInSQLite e= (EquipmentWithoutRFIDInSQLite) data.getSerializableExtra(RETURN_DATA_KEY);
        if(e==null) return;
        int positionInAll=data.getIntExtra(POSITION_IN_ALL_PARAM,-1);
        int positionInShow=data.getIntExtra(POSITION_IN_SHOW_PARAM,-1);
        if(positionInAll==-1||positionInShow==-1) return;
        mList.set(positionInAll,e);
        if(isShowBindEP){
            mShowList.set(positionInShow,e);
        }else {
            mShowList.remove(positionInShow);
        }
        RFIDMap.put(e.getRFID(),e.getRFID());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OthersUtil.unregisterEvent(this);
    }
}
