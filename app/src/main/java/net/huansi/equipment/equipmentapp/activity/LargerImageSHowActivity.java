package net.huansi.equipment.equipmentapp.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.polites.android.GestureImageView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.util.DrawableCache;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import butterknife.BindView;

import static net.huansi.equipment.equipmentapp.constant.Constant.LargerImageSHowActivityConstants.URL_PATH_PARAM;


/**
 * Created by Administrator on 2016/4/26 0026.
 */
public class LargerImageSHowActivity extends BaseActivity {
    @BindView(R.id.givPopEnlarge) GestureImageView givPopEnlarge;
    private LoadProgressDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.image_enlarge;
    }

    @Override
    public void init() {
        setToolBarTitle("图片");
        dialog=new LoadProgressDialog(this);
        String imagePath=getIntent().getStringExtra(URL_PATH_PARAM);
        Glide.with(getApplicationContext())
                .load(imagePath)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        OthersUtil.dismissLoadDialog(dialog);
                        givPopEnlarge.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        OthersUtil.dismissLoadDialog(dialog);
                        givPopEnlarge.setImageDrawable(DrawableCache.getInstance()
                                .getDrawable(R.drawable.icon_picture_error,getApplicationContext()));
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        OthersUtil.showLoadDialog(dialog);
                    }
                });
    }

}
