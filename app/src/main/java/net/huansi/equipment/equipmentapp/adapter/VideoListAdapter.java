package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.make_bills.ShowPDFActivity;
import net.huansi.equipment.equipmentapp.activity.make_bills.ShowVideoActivity;
import net.huansi.equipment.equipmentapp.constant.VideoConstant;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.PDFEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.functions.Func1;

public class VideoListAdapter extends BaseAdapter {
    public static final String TAG = "JieCaoVideoPlayer";
    private List<String> videoUrl=new ArrayList<>();
    private LoadProgressDialog dialog;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    Context context;
    int pager = -1;

    public VideoListAdapter(Context context) {
        this.context = context;
    }

    private void loadUrl() {
        HsWebInfo jsonData = NewRxjavaWebUtils.getJsonData(context, "spGetUrl_ProduceOrderPDFAndGSDVideo", "FEPO=" + "d" +
                ",DataType=" + "GSD", String.class.getName(), false, "error");
        Log.e("TAG","date="+jsonData.json);
//        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ShowPDFActivity.this, hsWebInfo)
//                        .map(new Func1<HsWebInfo, HsWebInfo>() {
//                            @Override
//                            public HsWebInfo call(HsWebInfo hsWebInfo) {
//                                return NewRxjavaWebUtils.getJsonData(context,
//                                        "spGetUrl_ProduceOrderPDFAndGSDVideo",
//                                        "FEPO="+videoActivity.BillNumber+
//                                                ",DataType="+"PO",
//                                        String.class.getName(),
//                                        false,
//                                        "helloWorld");
//                            }
//                        })
//                ,context, dialog, new WebListener() {
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        String json = hsWebInfo.json;
//                        Log.e("TAG", "success1="+hsWebInfo.json);
//                        //json解析
//                    }
//
//                    @Override
//                    public void error(HsWebInfo hsWebInfo) {
//                        Log.e("TAG", "error1="+hsWebInfo.error.error);
//                    }
//                });
        videoUrl.add("http://10.17.111.23:8064/mp4/bar.mp4");
        videoUrl.add("http://10.17.111.23:8064/mp4/pcxb.mp4");
        videoUrl.add("http://10.17.111.23:8064/mp4/pcxk.mp4");
        videoUrl.add("http://10.17.111.23:8064/mp4/sbc.mp4");
    }

    public VideoListAdapter(Context context, int pager) {
        this.context = context;
        this.pager = pager;
    }

    @Override
    public int getCount() {
        //return pager == -1 ? videoIndexs.length : 4;
        return pager == -1 ? videoUrl.size() : 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.process_video_item, null);
            viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.item_videoview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
       // if (pager == -1) {
            viewHolder.jcVideoPlayer.setUp(
                    videoUrl.get(position).toString(), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    VideoConstant.videoTitles[0][position]);

            Glide.with(convertView.getContext())
                    .load(VideoConstant.videoThumbs[0][position])
                    .error(R.drawable.icon_wifi)
                    .fitCenter()
                    .into(viewHolder.jcVideoPlayer.thumbImageView);
//        } else {
//            viewHolder.jcVideoPlayer.setUp(
//                    VideoConstant.videoUrls[pager][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
//                    VideoConstant.videoTitles[pager][position]);
//
//            Glide.with(convertView.getContext())
//                    .load(VideoConstant.videoThumbs[pager][position])
//                    .error(R.drawable.icon_wifi)
//                    .fitCenter()
//                    .into(viewHolder.jcVideoPlayer.thumbImageView);
//        }
        return convertView;
    }

    public class ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;
    }
}
