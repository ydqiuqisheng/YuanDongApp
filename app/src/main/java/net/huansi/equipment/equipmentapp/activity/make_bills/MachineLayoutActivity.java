package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.MachineLayoutEntity;
import net.huansi.equipment.equipmentapp.entity.WorkGuidanceEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Func1;

public class MachineLayoutActivity extends BaseActivity{
    @BindView(R.id.MLViewpager)
    ViewPager viewPager;
    private LoadProgressDialog dialog;
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private List<String> list ;
    private MyVpAdater adater;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_machine_layout;
    }

    @Override
    public void init() {
    setToolBarTitle("车位图");
        String billNumber = getIntent().getStringExtra("BillNumber");
        viewPager.setPageMargin(80);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(false,new AlphaTransformer());
        viewPager.setPageTransformer(false,new ScaleTransformer());
        dialog=new LoadProgressDialog(this);
        getPicDetail(billNumber);
    }


    private void getPicDetail(final String billNumber) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(MachineLayoutActivity.this, hsWebInfo)
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP",
                                        "Proc_GetparkingPicPDFData",
                                        "FEPOCode="+billNumber,
                                        String.class.getName(),
                                        false,
                                        "helloWorld");
                            }
                        })
                ,MachineLayoutActivity.this, dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        Log.e("TAG", "picInfo="+hsWebInfo.json);
                        //json解析
                        MachineLayoutEntity entity = JSON.parseObject(json, MachineLayoutEntity.class);
                        List<MachineLayoutEntity.DATABean> data = entity.getDATA();
                        list = new ArrayList<>();
                        for (int i=0;i<data.size();i++){
                            list.add(data.get(i).getCOLUMN1());
                        }
                        adater = new MyVpAdater(getApplicationContext(), list);
                        viewPager.setAdapter(adater);
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "errorMeeting="+hsWebInfo.json);
                        OthersUtil.ToastMsg(MachineLayoutActivity.this,"该款车位图不存在或未上传no picture found");
                    }
                });
    }

    //设置左右两边缩放
    public class ScaleTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.70f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(View page, float position) {
            if (position < -1 || position > 1) {
                page.setAlpha(MIN_ALPHA);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            } else if (position <= 1) { // [-1,1]
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                if (position < 0) {
                    float scaleX = 1 + 0.3f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                } else {
                    float scaleX = 1 - 0.3f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                }
                page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }
    }
    //设置左右两边透明度
    public class AlphaTransformer implements ViewPager.PageTransformer {
        private float MINALPHA = 0.5f;

        /**
         * position取值特点：
         * 假设页面从0～1，则：
         * 第一个页面position变化为[0,-1]
         * 第二个页面position变化为[1,0]
         *
         * @param page
         * @param position
         */
        @Override
        public void transformPage(View page, float position) {
            if (position < -1 || position > 1) {
                page.setAlpha(MINALPHA);
            } else {
                //不透明->半透明
                if (position < 0) {//[0,-1]
                    page.setAlpha(MINALPHA + (1 + position) * (1 - MINALPHA));
                } else {//[1,0]
                    //半透明->不透明
                    page.setAlpha(MINALPHA + (1 - position) * (1 - MINALPHA));
                }
            }
        }
    }

    public class MyVpAdater extends PagerAdapter {
        private List<String> list;
        private Context context;

        public MyVpAdater(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView iv = new ImageView(context);
            Glide.with(getApplicationContext()).load(list.get(position)).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv.setImageBitmap(resource);
                }
            });
            //iv.setImageResource(list.get(position));
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
