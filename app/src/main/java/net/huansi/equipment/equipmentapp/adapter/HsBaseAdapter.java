package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 单中年 on 2017/2/22.
 */

public abstract class HsBaseAdapter<T> extends BaseAdapter {
    protected List<T> mList;
    protected LayoutInflater mInflater;
    protected Context mContext;

    public HsBaseAdapter(List<T> list,Context context){
        mList=list;
        mContext=context;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
