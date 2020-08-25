package net.huansi.equipment.equipmentapp.activity.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.EPInventory;
import net.huansi.equipment.equipmentapp.entity.Factory;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.gen.InventoryDao;
import net.huansi.equipment.equipmentapp.gen.InventoryDetailDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.sqlite_db.Inventory;
import net.huansi.equipment.equipmentapp.sqlite_db.InventoryDetail;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.util.TimeUtils;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryMainActivityConstants.INVENTORY_ID_IN_SQLITE_PARAM;
import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by 单中年 on 2017/2/24.
 */

public class InventoryMainActivity extends BaseActivity {
    @BindView(R.id.spInventoryMainFactory) Spinner spInventoryMainFactory;//工厂的下拉框

    private LoadProgressDialog dialog;
    private ArrayAdapter<String> mFactoryAdapter;
    private List<Factory> mFactoryList;
    private List<String> mFactoryNameList;
    private Factory factory;


    @Override
    protected int getLayoutId() {
        return R.layout.inventory_main_activity;
    }

    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        setToolBarTitle("盘点");
        mFactoryList=new ArrayList<>();
        mFactoryNameList=new ArrayList<>();
        mFactoryAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,mFactoryNameList);
        spInventoryMainFactory.setAdapter(mFactoryAdapter);
        dialog=new LoadProgressDialog(this);
        //获得工厂信息
        getFactoryInfo();
    }

    /**
     * 获得工厂信息
     */
    private void getFactoryInfo(){
        mFactoryList.clear();
        mFactoryNameList.clear();
        OthersUtil.showLoadDialog(dialog);
        RxjavaWebUtils.requestByGetJsonData(this,
                "spAppEPDownAllFactory",
                "",
                getApplicationContext(),
                dialog,
                Factory.class.getName(),
                true,
                "没有查询到工厂的信息",
                new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
                        for(int i=0;i<entities.size();i++){
                            Factory factory= (Factory) entities.get(i);
                            mFactoryList.add(factory);
                            mFactoryNameList.add(factory.FACTORY);
                        }
                        mFactoryAdapter.notifyDataSetChanged();
                    }
                });
    }


    @OnItemSelected(R.id.spInventoryMainFactory)
    void showFactory(int position){
        factory=mFactoryList.get(position);
    }

    /**
     * 下载待盘点设备
     */
    @OnClick(R.id.btnInventoryMainDown)
    void down(){
        if(factory==null){
            OthersUtil.showTipsDialog(this,"请选择某一工厂！！");
            return;
        }

        OthersUtil.showChooseDialog(this, "确认下载工厂:" + factory.FACTORY + "的待盘点设备？",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                OthersUtil.showLoadDialog(dialog);

                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(InventoryMainActivity.this, "")
                        //先下载待盘点的设备
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                        "spAppEPDownEquipmentToInventory",
                                        "sFactory=" + factory.FACTORY +
                                                ",sUserNo=" + SPHelper.getLocalData(getApplicationContext(),
                                                USER_NO_KEY, String.class.getName(), ""),
                                        EPInventory.class.getName(),
                                        true,
                                        "不存在" + factory.FACTORY + "待盘点的设备");
                               /* return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                        "spGetOther_MaterialStockInByBarcode",
                                        "Barcode="+"helloWorld",
                                        EPInventory.class.getName(),
                                        true,
                                        "helloWorld");*/

                            }
                        })
                        //保存到本地数据库
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                if (!hsWebInfo.success) return hsWebInfo;
                                List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                                List<EPInventory> epInventoryList = new ArrayList<>();
                                for (int i = 0; i < entities.size(); i++) {
                                    EPInventory epInventory = (EPInventory) entities.get(i);
                                    epInventoryList.add(epInventory);
                                }
                                //先保存头表
                                InventoryDao inventoryDao = OthersUtil.getGreenDaoSession(getApplicationContext()).getInventoryDao();
                                Inventory inventory = new Inventory();
                                inventory.setInventoryUserNo(SPHelper.getLocalData
                                        (getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString());
                                inventory.setCreateUserNo(SPHelper.getLocalData
                                        (getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString());
                                inventory.setCreateTime(TimeUtils.getTime(new Date(), "-"));
                                inventory.setFactory(epInventoryList.get(0).FACTORY);
                                long inventoryId = inventoryDao.insertOrReplace(inventory);

                                //保存明细数据
                                List<InventoryDetail> inventoryDetailList = new ArrayList<>();
                                InventoryDetailDao inventoryDetailDao = OthersUtil.getGreenDaoSession(getApplicationContext()).getInventoryDetailDao();
                                for (int i = 0; i < epInventoryList.size(); i++) {
                                    EPInventory epInventory = epInventoryList.get(i);//从服务器返回的数据
                                    inventoryDetailList.add(OthersUtil.exchangeByDownInventory(epInventory, inventoryId, -2));
                                }
                                inventoryDetailDao.saveInTx(inventoryDetailList);
                                hsWebInfo.object = inventoryDao.load(inventoryId);
                                return hsWebInfo;
                            }
                        }), getApplicationContext(), dialog, new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        Intent intent = new Intent(InventoryMainActivity.this, InventoryDetailActivity.class);
                        intent.putExtra(INVENTORY_ID_IN_SQLITE_PARAM, (Inventory) hsWebInfo.object);
                        startActivity(intent);
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        OthersUtil.showTipsDialog(InventoryMainActivity.this, hsWebInfo.error.error);
                    }
                });
            }
        });
//                RxjavaWebUtils.requestByGetJsonData(InventoryMainActivity.this,
//                        "spAppEPDownEquipmentToInventory",
//                        "sFactory=" + factory.FACTORY +
//                                ",sUserNo=" + SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), ""),
//                        getApplicationContext(),
//                        dialog,
//                        EPInventory.class.getName(),
//                        true,
//                        "不存在待盘点的设备",
//                        new SimpleHsWeb() {
//                            @Override
//                            public void success(HsWebInfo hsWebInfo) {
//                                OthersUtil.dismissLoadDialog(dialog);
//                                List<WsEntity> entities=hsWebInfo.wsData.LISTWSDATA;
//                                List<EPInventory> epInventoryList=new ArrayList<>();
//                                for(int i=0;i<entities.size();i++){
//                                    EPInventory epInventory= (EPInventory) entities.get(i);
//                                    epInventoryList.add(epInventory);
//                                }
//                                saveToSQLite(epInventoryList);
//                            }
//                        });
//            }
//        });

    }

    /**
     * 继续之前的盘点
     */
    @OnClick(R.id.btnInventoryMainContinue)
    void continueBefore(){
        OthersUtil.showLoadDialog(dialog);
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<Inventory>>() {
                    @Override
                    public List<Inventory> call(String s) {
                        InventoryDao inventoryDao=OthersUtil.getGreenDaoSession(getApplicationContext()).getInventoryDao();
                        Query<Inventory> query=inventoryDao.queryBuilder()
                                .orderDesc(InventoryDao.Properties.CreateTime)
                                .build();
                        if(query==null) return null;
                        return query.list();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Inventory>>() {
                    @Override
                    public void call(final List<Inventory> inventories) {
                        OthersUtil.dismissLoadDialog(dialog);
                        if(inventories==null||inventories.isEmpty()) {
                            OthersUtil.showTipsDialog(InventoryMainActivity.this,
                                    "不存在需要继续盘点的记录，请尝试下载新待验证盘点设备！");
                            return;
                        }
                        String [] inventoryArr=new String[inventories.size()];
                        for(int i=0;i<inventories.size();i++){
                            Inventory inventory=inventories.get(i);
                            inventoryArr[i]="工厂："+inventory.getFactory()+"\n"
                                    +"创建者："+inventory.getCreateUserNo()+"\n"
                                    +"上次盘点者："+inventory.getInventoryUserNo()+"\n"
                                    +"创建时间："+inventory.getCreateTime()+"\n";
                        }
                        AlertDialog.Builder builder=new AlertDialog.Builder(InventoryMainActivity.this);
                        builder.setItems(inventoryArr, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(InventoryMainActivity.this,InventoryDetailActivity.class);
                                intent.putExtra(INVENTORY_ID_IN_SQLITE_PARAM,inventories.get(i));
                                startActivity(intent);
                            }
                        })
                        .show();
                    }
                });
    }

//    /**
//     * 保存到本地SQLite中
//     */
//    private void saveToSQLite(List<EPInventory> epInventoryList){
//        Observable.just(epInventoryList)
//                .subscribeOn(Schedulers.io())
//                .map(new Func1<List<EPInventory>, Long>() {
//                    @Override
//                    public Long call(List<EPInventory> list) {
//                        if(list==null||list.isEmpty()) return null;
//                        try {
//                            //先保存头表
//                            InventoryDao inventoryDao =OthersUtil.getGreenDaoSession(getApplicationContext()).getInventoryDao();
//                            Inventory inventory=new Inventory();
//                            inventory.setInventoryUserNo(SPHelper.getLocalData
//                                    (getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString());
//                            inventory.setCreateUserNo(SPHelper.getLocalData
//                                    (getApplicationContext(),USER_NO_KEY,String.class.getName(),"").toString());
//                            inventory.setCreateTime(TimeUtils.getTime(new Date(),"-"));
//                            inventory.setFactory(list.get(0).FACTORY);
//                            long inventoryId=inventoryDao.insertOrReplace(inventory);
//                            List<InventoryDetail> inventoryDetailList =new ArrayList<>();
//                            InventoryDetailDao  inventoryDetailDao=OthersUtil.getGreenDaoSession(getApplicationContext()).getInventoryDetailDao();
//                            for(int i=0;i<list.size();i++){
//                                EPInventory epInventory=list.get(i);//从服务器返回的数据
//                                inventoryDetailList.add(OthersUtil.exchangeByDownInventory(epInventory,inventoryId,-2));
//                            }
//                            inventoryDetailDao.saveInTx(inventoryDetailList);
//                            return inventoryId;
//                        }catch (Exception e){}
//                        return null;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long id) {
//                        if(id==null){
//                            OthersUtil.showTipsDialog(InventoryMainActivity.this,"下载出错，请检查！！");
//                            return;
//                        }
//                        Intent intent=new Intent(InventoryMainActivity.this,InventoryDetailActivity.class);
//                        intent.putExtra(INVENTORY_ID_IN_SQLITE_PARAM,id);
//                        startActivity(intent);
//                    }
//                });

}
