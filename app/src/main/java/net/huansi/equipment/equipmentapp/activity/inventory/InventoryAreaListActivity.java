package net.huansi.equipment.equipmentapp.activity.inventory;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.InventoryAreaAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.InventoryArea;
import net.huansi.equipment.equipmentapp.gen.InventoryDetailDao;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.sqlite_db.Inventory;
import net.huansi.equipment.equipmentapp.sqlite_db.InventoryDetail;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnItemClick;
import rx.functions.Func1;

import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryAreaListActivityConstants.RETURN_AREA_DATA;
import static net.huansi.equipment.equipmentapp.constant.Constant.InventoryMainActivityConstants.INVENTORY_ID_IN_SQLITE_PARAM;

/**
 * Created by shanz on 2017/7/6.
 * 盘点区域的列表
 */

public class InventoryAreaListActivity extends BaseActivity{
    @BindView(R.id.tvInventoryAreaTop) TextView tvInventoryAreaTop;
    @BindView(R.id.lvInventoryArea) ListView lvInventoryArea;
    private LoadProgressDialog dialog;
    private Inventory inventoryHdrInSQLite;
    private List<InventoryArea> mList;
    private InventoryAreaAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.inventory_area_list_activity;
    }


    @Override
    public void init() {
        setTitle("盘点区域列表");
        dialog=new LoadProgressDialog(this);
        inventoryHdrInSQLite= (Inventory) getIntent().getSerializableExtra(INVENTORY_ID_IN_SQLITE_PARAM);
        mList=new ArrayList<>();
        mAdapter=new InventoryAreaAdapter(mList,getApplicationContext());
        lvInventoryArea.setAdapter(mAdapter);
        getAreaData();
    }


    /**
     * 返回上一层并且显示区域盘点
     */
    @OnItemClick(R.id.lvInventoryArea)
    void showDetailByArea(int position){
        InventoryArea area=mList.get(position);
        Intent intent=new Intent();
        intent.putExtra(RETURN_AREA_DATA,area);
        setResult(RESULT_OK,intent);
        finish();
    }

    /**
     * 获取基本数据
     */
    @SuppressWarnings("unchecked")
    private void getAreaData(){
        OthersUtil.showLoadDialog(dialog);
        mList.clear();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        HsWebInfo hsWebInfo=new HsWebInfo();
                        InventoryDetailDao dao=OthersUtil.getGreenDaoSession(getApplicationContext()).getInventoryDetailDao();
                        List<InventoryDetail> detailList=dao.queryBuilder()
                                .where(InventoryDetailDao.Properties.InventoryHdrIdInSQLite.eq(inventoryHdrInSQLite.getId()))
                                .list();
                        if(detailList==null||detailList.isEmpty()){
                            hsWebInfo.success=false;
                            return hsWebInfo;
                        }
                        int totalNoInventoryQty=0;//未盘的数量
                        Map<String,InventoryArea> areaMap=new HashMap<>();
                        for(int i=0;i<detailList.size();i++){
                            InventoryDetail detail=detailList.get(i);
                            if(detail.getStatus()==-2||detail.getStatus()==-1){
                                totalNoInventoryQty++;
                                continue;
                            }
                            InventoryArea area=areaMap.get(detail.getArea());
                            if(area==null) area=new InventoryArea();
                            area.name=detail.getArea();
                            switch (detail.getStatus()){
                                //已盘
                                case 0:
                                    area.already++;
                                    break;
                                //盘盈
                                case 1:
                                    area.overage++;
                                    break;
                            }
                            areaMap.put(detail.getArea(),area);
                        }
                        Map<String,Object> returnMap=new HashMap<>();
                        returnMap.put("totalNoInventoryQty",totalNoInventoryQty);
                        returnMap.put("areaMap",areaMap);
                        hsWebInfo.object=returnMap;
                        return hsWebInfo;
                    }
                }), getApplicationContext(), dialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                Map<String,Object> map= (Map<String, Object>) hsWebInfo.object;
                int totalNoInventoryQty= (int) map.get("totalNoInventoryQty");
                Map<String,InventoryArea> areaMap= (Map<String, InventoryArea>) map.get("areaMap");
                Iterator<Map.Entry<String,InventoryArea>> it=areaMap.entrySet().iterator();
                while (it.hasNext()){
                    mList.add(it.next().getValue());
                }
                mAdapter.notifyDataSetChanged();
                tvInventoryAreaTop.setVisibility(View.VISIBLE);
                tvInventoryAreaTop.setText("剩余"+totalNoInventoryQty+"台未盘的资产设备");
            }

            @Override
            public void error(HsWebInfo hsWebInfo) {
                tvInventoryAreaTop.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
