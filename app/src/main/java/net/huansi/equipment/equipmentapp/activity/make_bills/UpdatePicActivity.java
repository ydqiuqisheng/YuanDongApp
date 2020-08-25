package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.content.Intent;
import android.widget.ImageView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.LargerImageSHowActivity;
import net.huansi.equipment.equipmentapp.adapter.PictureAddCommonAdapter;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.PictureAddDialogUtils;
import net.huansi.equipment.equipmentapp.widget.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

import static net.huansi.equipment.equipmentapp.constant.Constant.LargerImageSHowActivityConstants.URL_PATH_PARAM;
import static net.huansi.equipment.equipmentapp.constant.Constant.PICTURE_ALL_NUMBER_PER;
import static net.huansi.equipment.equipmentapp.constant.Constant.PICTURE_UP_NUMBER_PER;
import static net.huansi.equipment.equipmentapp.util.PictureAddDialogUtils.ALBUM_PICTURE_REQUEST_CODE;
import static net.huansi.equipment.equipmentapp.util.PictureAddDialogUtils.CAMERA_PICTURE_REQUEST_CODE;

public class UpdatePicActivity extends BaseActivity{

    @Override
    protected int getLayoutId() {
        return R.layout.update_pic_activity;
    }

    @Override
    public void init() {


    }

}
