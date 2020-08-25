package net.huansi.equipment.equipmentapp.activity.store_goods;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.BusStoreEvent;
import net.huansi.equipment.equipmentapp.entity.StoreGoodsDetail;
import net.huansi.equipment.equipmentapp.entity.StoreGoodsSummary;
import net.huansi.equipment.equipmentapp.event.MessageEvent;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FragmentSummary extends ListFragment {
    private List<StoreGoodsSummary> mSummaryList;
    private StoreSummaryAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_goods_summary_fragement, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mSummaryList=new ArrayList<>();
        //initGoodsInfo();
        adapter = new StoreSummaryAdapter(mSummaryList,getContext());
        this.setListAdapter(adapter);
    }

    private class StoreSummaryAdapter extends HsBaseAdapter<StoreGoodsSummary>{

        public StoreSummaryAdapter(List<StoreGoodsSummary> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null)  convertView=mInflater.inflate(R.layout.list_store_item,viewGroup,false);
            TextView storeLocation = ViewHolder.get(convertView, R.id.storeLocation);
            TextView storeCtnNo = ViewHolder.get(convertView, R.id.storeCtnNo);
            TextView storeNumber = ViewHolder.get(convertView, R.id.storeNumber);
            TextView storeTradeMode = ViewHolder.get(convertView, R.id.storeTradeMode);
            StoreGoodsSummary storeGoodsSummary = mList.get(position);
            storeLocation.setText(storeGoodsSummary.STOCKPOSITION_CURRENT);
            storeCtnNo.setText(storeGoodsSummary.NUM);
            storeNumber.setText(storeGoodsSummary.QUANTITY);
            storeTradeMode.setText(storeGoodsSummary.CHTNAME);
            return convertView;
        }
    }

    @Subscribe
    public void onMessage(BusStoreEvent event) {
        Log.e("TAG","sum数据="+event.data);
        switch (event.type){
            case SUM_SUCCESS:
                mSummaryList.clear();
                mSummaryList.addAll(event.data);
                break;
            case DETAIL_SUCCESS:
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
