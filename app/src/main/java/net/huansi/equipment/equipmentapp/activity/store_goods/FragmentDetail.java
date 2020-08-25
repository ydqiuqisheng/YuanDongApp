package net.huansi.equipment.equipmentapp.activity.store_goods;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trello.rxlifecycle.components.RxFragment;
import com.trello.rxlifecycle.components.support.RxFragmentActivity;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.BusStoreEvent;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.StoreGoodsDetail;
import net.huansi.equipment.equipmentapp.event.MessageEvent;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FragmentDetail extends ListFragment{
    private List<StoreGoodsDetail> mDetailList;
    private StoreDetailAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_goods_detail_fragement, container, false);
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mDetailList=new ArrayList<>();
        //initGoodsInfo();
        adapter = new StoreDetailAdapter(mDetailList,getContext());
        this.setListAdapter(adapter);
    }
    private class StoreDetailAdapter extends HsBaseAdapter<StoreGoodsDetail> {


        public StoreDetailAdapter(List<StoreGoodsDetail> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null)  convertView=mInflater.inflate(R.layout.list_store_item,viewGroup,false);
            TextView storeLocation = ViewHolder.get(convertView, R.id.storeLocation);
            TextView storeCtnNo = ViewHolder.get(convertView, R.id.storeCtnNo);
            TextView storeNumber = ViewHolder.get(convertView, R.id.storeNumber);
            TextView storeTradeMode = ViewHolder.get(convertView, R.id.storeTradeMode);
            StoreGoodsDetail storeGoodsDetail = mList.get(position);
            storeLocation.setText(storeGoodsDetail.STOCKPOSITION_CURRENT);
            storeCtnNo.setText(storeGoodsDetail.BOXBARCODE);
            storeNumber.setText(storeGoodsDetail.QUANTITY);
            storeTradeMode.setText(storeGoodsDetail.CHTNAME);
            return convertView;
        }
    }
    @Subscribe
    public void onMessage(BusStoreEvent event) {
        Log.e("TAG","det数据="+event.data);
        switch (event.type){
            case SUM_SUCCESS:
                break;
            case DETAIL_SUCCESS:
                mDetailList.clear();
                mDetailList.addAll(event.data);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
