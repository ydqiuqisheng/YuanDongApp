package net.huansi.equipment.equipmentapp.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Administrator on 2016/2/27.
 */
public class OnCamera {
    private OnCamera(){}
    public static final String PICTURE_FOLDER=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator + "DCIM/Camera";




    public static String getPathFromCamera(Activity activity,int requestCode) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            OthersUtil.ToastMsg(activity,"请插入SD卡！！");
            return null;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File folder = new File(PICTURE_FOLDER);
        if (!folder.exists()) folder.mkdirs();
        File file = new File(folder, "IMG_" + System.currentTimeMillis() + ".jpg");
        Uri mOutPutFileUri = Uri.fromFile(file);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
        return mOutPutFileUri.getPath();
    }


    public static String  getPictureFromAlbum(Activity activity,Uri uri){
        String path;

                //显得到bitmap图片这里开始的第二部分，获取图片的路径：

                String[] proj = {MediaStore.Images.Media.DATA};

                //好像是android多媒体数据库的封装接口，具体的看Android文档
//         = activity.managedQuery(uri, proj, null, null, null);
        ContentResolver cr = activity.getContentResolver();
        Cursor cursor = cr.query(uri, proj,null, null, null);
        if(cursor!=null) {
            //按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径
            path = cursor.getString(column_index);
        }else {
            path=uri.getPath();
        }
        if(cursor!=null)  cursor.close();
        return path;
//        return uri.getA;
    }


}
