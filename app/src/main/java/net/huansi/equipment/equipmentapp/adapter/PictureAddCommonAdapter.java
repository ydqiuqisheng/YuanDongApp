package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.constant.Constant;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

//import com.nostra13.universalimageloader.core.ImageLoader;
//import net.huansi.hsapp.util.ImageLoadeUtil;

/**
 * Created by SZN on 2016/6/7.
 */
public class PictureAddCommonAdapter extends HsBaseAdapter<String>{
    private boolean isLocalPath=true;//是不是本地的图片

    public PictureAddCommonAdapter(List<String> list, Context context) {
        super(list, context);
    }

    public PictureAddCommonAdapter(List<String> localPathList, Context context, List<String>serverPathList){
        this(localPathList,context);
        if(localPathList==null){
            mList=serverPathList;
            isLocalPath=false;
        }else {
            mList=localPathList;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView=mInflater.inflate(R.layout.picture_add_common_item,parent,false);
            parent.measure(0,0);
            convertView.setLayoutParams(new AbsListView.LayoutParams(parent.getHeight(),parent.getMeasuredHeight()));
        }
        ImageView imvPictureAddCommon= ViewHolder.get(convertView,R.id.imvPictureAddCommon);
        String path = mList.get(position);
        if(isLocalPath) {
            showPictureByLocal(imvPictureAddCommon, path);
        }else {
            showPictureOnline(path,imvPictureAddCommon);
        }
        return convertView;
    }


    /**
     * 显示本地图片
     */
    private void showPictureByLocal(ImageView imv,String path){
        Glide.with(mContext)
                .load(path)
                .centerCrop()
                .placeholder(R.drawable.icon_picture_error)
                .into(imv);
    }

    /**
     * 显示网络上的图片
     */
    private void showPictureOnline(String url,ImageView imv){
        Glide.with(mContext)
                .load(Constant.PICTURE_ONLINE_HEAD+url)
                .centerCrop()
                .placeholder(R.drawable.icon_picture_error)
                .into(imv);
    }
}
