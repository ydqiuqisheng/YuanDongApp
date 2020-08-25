package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.LargerImageSHowActivity;
import net.huansi.equipment.equipmentapp.activity.repair.RepairDetailActivity;
import net.huansi.equipment.equipmentapp.entity.RepairDetail;
import net.huansi.equipment.equipmentapp.event.AdapterToActivityEvent;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static net.huansi.equipment.equipmentapp.constant.Constant.LargerImageSHowActivityConstants.URL_PATH_PARAM;
import static net.huansi.equipment.equipmentapp.event.AdapterToActivityEvent.DELETE_INDEX;

/**
 * Created by 单中年 on 2017/3/1.
 */

public class RepairDetailAdapter extends HsBaseAdapter<RepairDetail> {
    public RepairDetailAdapter(List<RepairDetail> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view==null) view=mInflater.inflate(R.layout.repair_detail_item,viewGroup,false);
        TextView tvRepairDetailItemProject= ViewHolder.get(view,R.id.tvRepairDetailItemProject);//维修项目
        TextView tvRepairDetailItemMethod= ViewHolder.get(view,R.id.tvRepairDetailItemMethod);//维修方法
        TextView tvRepairDetailItemRepairMan= ViewHolder.get(view,R.id.tvRepairDetailItemRepairMan);//维修人员
        TextView tvRepairDetailItemResult= ViewHolder.get(view,R.id.tvRepairDetailItemResult);//维修结果
        TextView tvRepairDetailItemRemark=ViewHolder.get(view,R.id.tvRepairDetailItemRemark);//备注
//        HorizontalScrollView hsvRepairDetailItem=ViewHolder.get(view,R.id.hsvRepairDetailItem);//图片的显示
//        final LinearLayout repairDetailItemPictureLayout=ViewHolder.get(view,R.id.repairDetailItemPictureLayout);
        RepairDetail detail=mList.get(i);
        tvRepairDetailItemProject.setText(detail.project.NAME);
        tvRepairDetailItemMethod.setText(detail.method);
        tvRepairDetailItemRepairMan.setText(detail.repairUserNo);
        tvRepairDetailItemResult.setText(detail.result.NAME);
        tvRepairDetailItemRemark.setText(detail.remark);
//        repairDetailItemPictureLayout.removeAllViews();
//        if(pathList==null||pathList.isEmpty()){
//            hsvRepairDetailItem.setVisibility(View.GONE);
//        }else {
//            hsvRepairDetailItem.setVisibility(View.VISIBLE);
//            ViewGroup.LayoutParams lp=hsvRepairDetailItem.getLayoutParams();
//            int pictureHeight=lp.height;
//            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(pictureHeight,pictureHeight);
//            layoutParams.setMargins(5,0,5,0);
//            for(int j=0;j<pathList.size();j++){
//                final String path=pathList.get(j);
//                ImageView imv=new ImageView(mContext);
//                imv.setLayoutParams(layoutParams);
//                Glide.with(mContext)
//                        .load(path)
//                        .placeholder(R.drawable.icon_picture_error)
//                        .error(R.drawable.icon_picture_error)
//                        .centerCrop()
//                        .into(imv);
//                imv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent=new Intent(mContext, LargerImageSHowActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra(URL_PATH_PARAM,path);
//                        mContext.startActivity(intent);
//                    }
//                });
//                repairDetailItemPictureLayout.addView(imv);
//                final int finalJ = j;
//                imv.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        EventBus.getDefault().post(new AdapterToActivityEvent(RepairDetailAdapter.class,
//                                RepairDetailActivity.class,i, finalJ, DELETE_INDEX));
//                        return true;
//                    }
//                });
//            }
//        }

        return view;
    }
}
