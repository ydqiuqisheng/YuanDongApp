package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v4.view.ViewConfigurationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.LargeTextShowActivity;
import net.huansi.equipment.equipmentapp.activity.LargerImageSHowActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.constant.VideoConstant;
import net.huansi.equipment.equipmentapp.entity.GSDEntity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.VideoEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.HorizontalListView;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.functions.Func1;

import static net.huansi.equipment.equipmentapp.constant.Constant.LargerImageSHowActivityConstants.URL_PATH_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.LargerTextSHowActivityConstants.TEXT_CONTENT_PARAM;

public class ShowVideoActivity extends BaseActivity {
    private List<String> videoUrl=new ArrayList<>();
    private List<String> picUrl=new ArrayList<>();
    private List<String> processName=new ArrayList<>();
    private LoadProgressDialog dialog;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private VideoListAdapter videoListAdapter;
    private SensorManager sensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private String PICURL=null;
    private String PELink_URL=null;//外部链接，所以pe视频
    private String packageName="net.huansi.equipment.equipmentapp";
    @BindView(R.id.StylePicUrl)
    ImageView stylePicUrl;
    @BindView(R.id.Season)
    TextView season;
    @BindView(R.id.FEPO)
    TextView fepo;
    @BindView(R.id.SampleType)
    TextView sampleType;
    @BindView(R.id.MobanDs)
    TextView mobanDs;
    @BindView(R.id.FuZhuGongJuDs)
    TextView fuZhuGongJuDs;
    @BindView(R.id.TeShuSheBeiDs)
    TextView teShuSheBeiDs;
    @BindView(R.id.SheBeiCanShuDs)
    TextView sheBeiCanShuDs;
    @BindView(R.id.MoBanZhongDianDs)
    TextView moBanZhongDianDs;
    @BindView(R.id.GongYiDs)
    TextView gongYiDs;
    @BindView(R.id.XianYangFengXianDs)
    TextView xianYangFengXianDs;
    @BindView(R.id.OtherDs)
    TextView otherDs;
    @BindView(R.id.gridVideo)
    GridView gridVideo;
    @Override
    protected int getLayoutId() {
        return R.layout.show_video_activity;
    }

    @Override
    public void init() {
        setToolBarTitle("远东服装GSD工序视频演示");
        gongYiDs.setMovementMethod(ScrollingMovementMethod.getInstance());
        fuZhuGongJuDs.setMovementMethod(ScrollingMovementMethod.getInstance());
        mobanDs.setMovementMethod(ScrollingMovementMethod.getInstance());
        dialog=new LoadProgressDialog(this);
        videoListAdapter = new VideoListAdapter();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        getDescription();//获取履历表描述内容
        getUrl();//获取视频和缩略图URL

    }

    @OnClick(R.id.PEVideo_link)
    void toSeeVideo(){
       if (PELink_URL==null){
           OthersUtil.ToastMsg(this,getString(R.string.link_error));
           return;
       }else {
           Uri uri = Uri.parse(PELink_URL);
           Intent intent = new Intent(Intent.ACTION_VIEW, uri);
           startActivity(intent);
       }

    }
    private void getDescription() {
        final String billNumber = getIntent().getStringExtra("BillNumber");
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ShowVideoActivity.this, hsWebInfo)
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP",
                                        "spGetUrl_ProduceOrderPDFAndGSDVideo",
                                        "FEPO="+billNumber+
                                                ",DataType="+"PEInfo",
                                        String.class.getName(),
                                        false,
                                        "helloWorld");
                            }
                        })
                ,ShowVideoActivity.this, dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        Log.e("TAG", "descriptionSuccess="+hsWebInfo.json);
                        //json解析
                        GSDEntity gsdEntity = JSON.parseObject(json, GSDEntity.class);
                        List<GSDEntity.DATABean> data = gsdEntity.getDATA();
                        //Glide.clear(stylePicUrl);
                        String s = data.get(0).getSHAREDVIDEO().toString();
                        if (s.toString().isEmpty()){
                           OthersUtil.ToastMsg(ShowVideoActivity.this,getString(R.string.invalid_link));
                        }else {
                            String url = OthersUtil.encodeUrl(s);
                            PELink_URL=url;
                        }
                        String picUrl = data.get(0).getPICURL().toString();
                        PICURL=picUrl;
                        Glide.with(getApplicationContext()).load(picUrl).asBitmap().fitCenter().into(stylePicUrl);
                        season.setText(data.get(0).getSEASON());
                        fepo.setText(data.get(0).getFEPO());
                        sampleType.setText(data.get(0).getSAMPLETYPE());
                        mobanDs.setText(data.get(0).getMOBANDS());
                        fuZhuGongJuDs.setText(data.get(0).getFUZHUGONGJUDS());
                        teShuSheBeiDs.setText(data.get(0).getTESHUSHEBEIDS());
                        sheBeiCanShuDs.setText(data.get(0).getSHEBEICANSHUDS());
                        moBanZhongDianDs.setText(data.get(0).getMOBANZHONGDIANDS());
                        gongYiDs.setText(data.get(0).getGONGYIDS());
                        xianYangFengXianDs.setText(data.get(0).getXIANYANGFENGXIANDS());
                        otherDs.setText(data.get(0).getOTHERDS());
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "descriptionError="+hsWebInfo.json);
                        //json解析
//                        GSDEntity gsdEntity = JSON.parseObject(json, GSDEntity.class);
//                        List<GSDEntity.DATABean> data = gsdEntity.getDATA();
//                        //Glide.clear(stylePicUrl);
//                        String picUrl = data.get(0).getPICURL().toString();
//                        PICURL=picUrl;
//                        Glide.with(getApplicationContext()).load(picUrl).asBitmap().fitCenter().into(stylePicUrl);
//                        season.setText(data.get(0).getSEASON());
//                        fepo.setText(data.get(0).getFEPO());
//                        sampleType.setText(data.get(0).getSAMPLETYPE());
//                        mobanDs.setText(data.get(0).getMOBANDS());
//                        fuZhuGongJuDs.setText(data.get(0).getFUZHUGONGJUDS());
//                        teShuSheBeiDs.setText(data.get(0).getTESHUSHEBEIDS());
//                        sheBeiCanShuDs.setText(data.get(0).getSHEBEICANSHUDS());
//                        moBanZhongDianDs.setText(data.get(0).getMOBANZHONGDIANDS());
//                        gongYiDs.setText(data.get(0).getGONGYIDS());
//                        xianYangFengXianDs.setText(data.get(0).getXIANYANGFENGXIANDS());
//                        otherDs.setText(data.get(0).getOTHERDS());
                    }
                });
    }

    private void getUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String billNumber = getIntent().getStringExtra("BillNumber");
                NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(ShowVideoActivity.this, hsWebInfo)
                                .map(new Func1<HsWebInfo, HsWebInfo>() {
                                    @Override
                                    public HsWebInfo call(HsWebInfo hsWebInfo) {
                                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP",
                                                "spGetUrl_ProduceOrderPDFAndGSDVideo",
                                                "FEPO="+billNumber+
                                                        ",DataType="+"GSD",
                                                String.class.getName(),
                                                false,
                                                "helloWorld");
                                    }
                                })
                        ,ShowVideoActivity.this, dialog, new WebListener() {
                            @Override
                            public void success(HsWebInfo hsWebInfo) {
                                String json = hsWebInfo.json;
                                Log.e("TAG", "success1="+hsWebInfo.json);
                                //json解析
                                VideoEntity videoEntity = JSON.parseObject(json, VideoEntity.class);
                                List<VideoEntity.DATABean> data = videoEntity.getDATA();
                                File dir=new File("/sdcard/"+billNumber);
                                //若无本地文件就在线播放
                                if (dir == null || !dir.exists() || !dir.isDirectory()){
                                    for (int i=0;i<data.size();i++){
                                        Log.e("TAG","目录空");
                                        videoUrl.add(data.get(i).getVIDEOURL());
                                        picUrl.add(data.get(i).getPICURL());
                                        processName.add(data.get(i).getPROCESSNAME());
                                    }
                                }else {
                                    //本地播放
                                    for (int i=0;i<data.size();i++){
                                        String url = data.get(i).getVIDEOURL().toString();
                                        int index = url.lastIndexOf("/");
                                        String fileName = url.substring(index, url.length());
                                        videoUrl.add("/sdcard/"+billNumber + "/"+fileName);
                                        picUrl.add(data.get(i).getPICURL());
                                        processName.add(data.get(i).getPROCESSNAME());
                                    }
                                }

                                //网络获取数据是耗时操作，一定要先获取成功以后再设置进适配器
                                //getPic();//这段注释掉的部分是获取视频第一帧的
//                                for (int i=0;i<videoUrl.size();i++){
//                                    Bitmap videoThumbnail = OthersUtil.createVideoThumbnail(OthersUtil.encodeUrl(videoUrl.get(i).toString()), MediaStore.Images.Thumbnails.MINI_KIND);
//                                    map.put(i,videoThumbnail);
//                                }
                                gridVideo.setAdapter(videoListAdapter);
                            }

                            @Override
                            public void error(HsWebInfo hsWebInfo) {
                                Log.e("TAG", "error1="+hsWebInfo.error.error);
                            }
                        });
            }
        //截取视频第一帧的方法，太慢暂时不用
//            private void getPic() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i=0;i<videoUrl.size();i++){
//                            Bitmap videoThumbnail = OthersUtil.createVideoThumbnail(OthersUtil.encodeUrl(videoUrl.get(i).toString()), MediaStore.Images.Thumbnails.MINI_KIND);
//                            map.put(i,videoThumbnail);
//                        }
//                    }
//                }).start();
//                lvVideo.setAdapter(videoListAdapter);
//            }
        }).start();
    }

    @OnClick(R.id.StylePicUrl)
    void showLargerPicture(){
        Intent intent=new Intent(this, LargerImageSHowActivity.class);
        intent.putExtra(URL_PATH_PARAM,PICURL);
        startActivity(intent);
    }
    @OnClick(R.id.XianYangFengXianDs)
    void showRiskLargerText(){
        Intent intent=new Intent(this, LargeTextShowActivity.class);
        intent.putExtra(TEXT_CONTENT_PARAM,xianYangFengXianDs.getText().toString());
        startActivity(intent);
    }
    @OnClick(R.id.GongYiDs)
    void showGSLargerText(){
        Intent intent=new Intent(this, LargeTextShowActivity.class);
        intent.putExtra(TEXT_CONTENT_PARAM,gongYiDs.getText().toString());
        startActivity(intent);
    }
    private class VideoListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return videoUrl.size();
        }

        @Override
        public Object getItem(int position) {
            return videoUrl.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            Log.e("TAG","Position="+position);
            final ViewHolder mHolder;
            if (convertView==null){
                mHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(ShowVideoActivity.this);
                convertView = mInflater.inflate(R.layout.process_video_item, null);
                mHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.item_videoview);
                mHolder.videoDescription= (TextView) convertView.findViewById(R.id.videoDescription);
                convertView.setTag(mHolder);
            }else {
                mHolder= (ViewHolder) convertView.getTag();
            }
            Log.e("TAG","kkk="+OthersUtil.encodeUrl(videoUrl.get(position).toString()));
            //OthersUtil.encodeUrl(videoUrl.get(position).toString()"http://10.17.111.3:8040/2018技转履历表视频资料/7月履历表视频/8KC34S3A/上领.mp4"
            mHolder.jcVideoPlayer.setUp(OthersUtil.encodeUrl(videoUrl.get(position).toString()), JCVideoPlayer.SCREEN_LAYOUT_NORMAL,
                   processName.get(position).toString());
            mHolder.videoDescription.setText(processName.get(position));
            Glide.with(ShowVideoActivity.this)
                    .load(picUrl.get(position).toString())
                    .error(R.drawable.icon_wifi)
                    .centerCrop()
                    .crossFade(1000)
                    .into(mHolder.jcVideoPlayer.thumbImageView);
            return convertView;
        }

    }

    public class ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;
        TextView videoDescription;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        Log.e("TAG","onBackPressed");
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        Log.e("TAG","onDestroy");
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }


}
