package net.huansi.equipment.equipmentapp.activity.store_goods;

import android.app.Notification;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.BusStoreEvent;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.StoreGoodsDetail;
import net.huansi.equipment.equipmentapp.entity.StoreGoodsSummary;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.event.MessageEvent;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.StoreDetailUtil;
import net.huansi.equipment.equipmentapp.util.StoreSummaryUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.entity.BusStoreEvent.Type.DETAIL_SUCCESS;
import static net.huansi.equipment.equipmentapp.entity.BusStoreEvent.Type.SUM_SUCCESS;

public  class StoreGoodsQueryActivity extends BaseActivity {
    @BindView(R.id.vp_container)
    ViewPager mcContainer;
    @BindView(R.id.goodsSummary)
    TextView goodsSummary;
    @BindView(R.id.goodsDetail)
    TextView goodsDetail;
    @BindView(R.id.et_ClientPo)
    EditText et_ClientPo;
    @BindView(R.id.store_query0)
    Button store_query0;
    @BindView(R.id.goodsSum)
    TextView goodsTotal;
    @BindView(R.id.et_ctno)
    EditText et_ctno;
    private int goodsNum=0;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private List<TextView> textViewList;
    private LoadProgressDialog dialog;
    private List<StoreGoodsDetail> lvGoodsDetail=new ArrayList<>();
    private List<StoreGoodsSummary> lvGoodsSummary=new ArrayList<>();
    private StoreGoodsDetail storeGoodsDetail;
    private StoreGoodsSummary storeGoodsSummary;
    private int mDefaultColor= Color.BLACK;

    private int mActiveColor=Color.RED;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_goods_query;
    }
    @Override
    public void init() {
        setToolBarTitle("查询页面");
        dialog=new LoadProgressDialog(this);
        ButterKnife.bind(this);
        fragmentList=new ArrayList<Fragment>();
        fragmentList.add(new FragmentSummary());
        fragmentList.add(new FragmentDetail());

        textViewList=new ArrayList<TextView>();
        textViewList.add(goodsSummary);
        textViewList.add(goodsDetail);
        textViewList.get(0).setTextColor(mActiveColor);
        fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList != null ? fragmentList.get(i) : null;
            }

            @Override
            public int getCount() {
                return fragmentList != null ? fragmentList.size() : 0;
            }
        };
        mcContainer.setAdapter(fragmentPagerAdapter);
        mcContainer.setAlpha(1);
        mcContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (TextView viewer :
                        textViewList) {
                    viewer.setTextColor(mDefaultColor);
                }
                textViewList.get(position).setTextColor(mActiveColor);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.store_query0)
    void storeQuery0(){
        final String PO = et_ClientPo.getText().toString().trim();
        searchBasePara(PO,"");//根据Po查询
    }

    @OnClick(R.id.store_query1)
    void storeQuery1(){
        final String BO =et_ctno .getText().toString().trim();
        //String substring = BO.substring(0, BO.length()-1);
        //Log.e("TAG","bo="+substring);
        searchBasePara("",BO);//根据箱号查询
    }






    private void searchBasePara(final String po,final String bo) {
        Log.e("TAG","po="+po+"bo="+bo);
        goodsNum=0;
        if (po.isEmpty()&&bo.isEmpty()){
            OthersUtil.ToastMsg(this,"查询内容为空！");
            return;
        }
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(StoreGoodsQueryActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(), "spAppProductStorageMove_Search",
                                "CustomerPO=" + po +
                                        ",Type=" + "Detail"+
                                        ",BoxID=" + bo,
                                String.class.getName(), false, "组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","查询结果det="+json);
                StoreDetailUtil storeDetailUtil = JSON.parseObject(json, StoreDetailUtil.class);
                List<StoreDetailUtil.DATABean> data = storeDetailUtil.getDATA();
                lvGoodsDetail.clear();
                for (int i=0;i<data.size();i++){
                    storeGoodsDetail=new StoreGoodsDetail();
                    storeGoodsDetail.STOCKPOSITION_CURRENT=data.get(i).getSTOCKPOSITION_CURRENT();
                    storeGoodsDetail.BOXBARCODE=data.get(i).getBOXBARCODE();
                    storeGoodsDetail.QUANTITY=data.get(i).getQUANTITY();
                    storeGoodsDetail.CHTNAME=data.get(i).getCHTNAME();
                    lvGoodsDetail.add(storeGoodsDetail);
                }
                Log.e("TAG",lvGoodsDetail.toString());
                EventBus.getDefault().post(new BusStoreEvent(DETAIL_SUCCESS,lvGoodsDetail));
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
                OthersUtil.ToastMsg(getApplicationContext(),"未查到相关信息");

                lvGoodsDetail.clear();
                EventBus.getDefault().post(new BusStoreEvent(DETAIL_SUCCESS,lvGoodsDetail));
            }
        });


        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(StoreGoodsQueryActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(), "spAppProductStorageMove_Search",
                                "CustomerPO=" + po +
                                        ",Type=" + "Sum"+
                                        ",BoxID=" + bo ,
                                String.class.getName(), false, "组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","查询结果sum="+json);
                StoreSummaryUtil storeSummaryUtil = JSON.parseObject(json, StoreSummaryUtil.class);
                List<StoreSummaryUtil.DATABean> data = storeSummaryUtil.getDATA();
                lvGoodsSummary.clear();
                for (int i=0;i<data.size();i++){
                    storeGoodsSummary=new StoreGoodsSummary();
                    storeGoodsSummary.STOCKPOSITION_CURRENT=data.get(i).getSTOCKPOSITION_CURRENT();
                    storeGoodsSummary.NUM=data.get(i).getNUM();
                    goodsNum=goodsNum+Integer.parseInt(data.get(i).getNUM());
                    storeGoodsSummary.QUANTITY=data.get(i).getQUANTITY();
                    storeGoodsSummary.CHTNAME=data.get(i).getCHTNAME();
                    lvGoodsSummary.add(storeGoodsSummary);
                }
                goodsTotal.setText("总计"+goodsNum+"箱");
                Log.e("TAG",lvGoodsSummary.toString());
                EventBus.getDefault().post(new BusStoreEvent(SUM_SUCCESS,lvGoodsSummary));
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error="+hsWebInfo.json);
                OthersUtil.ToastMsg(getApplicationContext(),"未查到相关信息");
                goodsTotal.setText("总计"+goodsNum+"箱");
                lvGoodsSummary.clear();
                EventBus.getDefault().post(new BusStoreEvent(SUM_SUCCESS,lvGoodsSummary));
            }
        });
    }
}
