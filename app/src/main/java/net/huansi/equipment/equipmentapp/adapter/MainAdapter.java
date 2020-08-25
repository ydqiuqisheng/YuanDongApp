package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.MainItem;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by 单中年 on 2017/2/22.
 */

public class MainAdapter extends HsBaseAdapter<MainItem> {


    public MainAdapter(List<MainItem> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) view=mInflater.inflate(R.layout.main_item,viewGroup,false);
        ImageView imvMainItem= ViewHolder.get(view,R.id.imvMainItem);
        TextView tvMainItemName=ViewHolder.get(view,R.id.tvMainItemName);
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,(int)(viewGroup.getHeight()/2.0));
        view.setLayoutParams(params);
        ViewGroup.LayoutParams layoutParam=imvMainItem.getLayoutParams();
        layoutParam.width=viewGroup.getWidth()/7;
        layoutParam.height=viewGroup.getHeight()/7;
        imvMainItem.setLayoutParams(layoutParam);
        MainItem mainItem=mList.get(i);
        Glide.with(mContext)
                .load(mainItem.drawableId)
                .placeholder(R.drawable.icon_equipment)
                .into(imvMainItem);
        tvMainItemName.setText(mainItem.name);
        return view;
    }
}
